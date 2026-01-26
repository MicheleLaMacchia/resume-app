package com.mlm.resume_app.dao;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.ResumeModels;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Primary
@Repository
public class DynamoResumeDaoImpl implements ResumeDao {

    private static final String TABLE_NAME = "ResumeTable";
    private static final String PK = "pk";
    private static final String SK = "sk";
    private static final String DATA = "data";
    private static final String PROFILE_SK = "PROFILE";

    private final DynamoDbClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public DynamoResumeDaoImpl(DynamoDbClient client) {
        this.client = client;
    }

    public void createTableIfNotExists() {
        try {
            DescribeTableRequest describe = DescribeTableRequest.builder().tableName(TABLE_NAME).build();
            DescribeTableResponse resp = client.describeTable(describe);
            // table exists if no exception
        } catch (ResourceNotFoundException e) {
            // create table with composite key (pk, sk) — keep sk for future items
            client.createTable(CreateTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .attributeDefinitions(
                            AttributeDefinition.builder().attributeName(PK).attributeType(ScalarAttributeType.S).build(),
                            AttributeDefinition.builder().attributeName(SK).attributeType(ScalarAttributeType.S).build()
                    )
                    .keySchema(
                            KeySchemaElement.builder().attributeName(PK).keyType(KeyType.HASH).build(),
                            KeySchemaElement.builder().attributeName(SK).keyType(KeyType.RANGE).build()
                    )
                    .billingMode(software.amazon.awssdk.services.dynamodb.model.BillingMode.PAY_PER_REQUEST)
                    .build());
        }
    }

    @Override
    public ResumeModels loadResume() {
        // retrieve profile by codiceFiscale
        // we expect an item with PK = codiceFiscale and SK = PROFILE
        // fallback: original ROOT/RESUME
        DatiGenerali dg = null;
        String cf = null;
        try {
            GetItemRequest rootReq = GetItemRequest.builder().tableName(TABLE_NAME)
                    .key(Map.of(PK, AttributeValue.builder().s("ROOT").build(), SK, AttributeValue.builder().s("RESUME").build()))
                    .build();
            var rootResp = client.getItem(rootReq);
            if (rootResp != null && rootResp.item() != null && !rootResp.item().isEmpty()) {
                var av = rootResp.item().get(DATA);
                if (av != null && av.s() != null) {
                    ResumeModels rm = mapper.readValue(av.s(), ResumeModels.class);
                    return rm;
                }
            }
        } catch (Exception ignored) {
        }

        // If no ROOT item, try read by codice fiscale; assume only one resume seeded with InMemory
        try {
            // perform a scan to find any PROFILE item and use its codice fiscale
            var scanReq = software.amazon.awssdk.services.dynamodb.model.ScanRequest.builder().tableName(TABLE_NAME).limit(1).build();
            var scanResp = client.scan(scanReq);
            if (scanResp.count() > 0 && scanResp.items() != null) {
                for (var item : scanResp.items()) {
                    var dataAv = item.get(DATA);
                    if (dataAv != null && dataAv.s() != null) {
                        ResumeModels rm = mapper.readValue(dataAv.s(), ResumeModels.class);
                        return rm;
                    }
                }
            }
        } catch (Exception ex) {
            // ignore and return null
        }

        return null;
    }

    @Override
    public ResumeModels loadResumeByPk(String pk) {
        if (pk == null || pk.isBlank()) {
            return null;
        }
        try {
            var key = Map.of(PK, AttributeValue.builder().s(pk).build(), SK, AttributeValue.builder().s(PROFILE_SK).build());
            GetItemRequest req = GetItemRequest.builder().tableName(TABLE_NAME).key(key).build();
            var resp = client.getItem(req);
            if (resp == null || resp.item() == null || resp.item().isEmpty()) {
                return null;
            }
            AttributeValue av = resp.item().get(DATA);
            if (av == null || av.s() == null) {
                return null;
            }
            return mapper.readValue(av.s(), ResumeModels.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resume by pk", e);
        }
    }

    @Override
    public List<String> loadAllResumePk() {
        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(TABLE_NAME)
                    .projectionExpression("#pk")
                    .filterExpression("#sk = :skValue")
                    .expressionAttributeNames(Map.of(
                            "#pk", PK,
                            "#sk", SK
                    ))
                    .expressionAttributeValues(Map.of(
                            ":skValue", AttributeValue.builder().s(PROFILE_SK).build()
                    ))
                    .build();

            ScanResponse response = client.scan(scanRequest);

            return response.items().stream()
                    .map(item -> item.get(PK).s())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load all resume PKs", e);
        }
    }

    @Override
    public void putResume(ResumeModels resume) {
        try {
            String json = mapper.writeValueAsString(resume);
            String codiceFiscale = null;
            if (resume != null && resume.datiGenerali() != null) {
                codiceFiscale = resume.datiGenerali().codiceFiscale();
            }
            // if codice fiscale is present, use it as PK; otherwise fallback to ROOT/RESUME
            Map<String, AttributeValue> item;
            if (codiceFiscale != null && !codiceFiscale.isBlank()) {
                item = Map.of(
                        PK, AttributeValue.builder().s(codiceFiscale).build(),
                        SK, AttributeValue.builder().s(PROFILE_SK).build(),
                        DATA, AttributeValue.builder().s(json).build()
                );
            } else {
                item = Map.of(
                        PK, AttributeValue.builder().s("ROOT").build(),
                        SK, AttributeValue.builder().s("RESUME").build(),
                        DATA, AttributeValue.builder().s(json).build()
                );
            }
            PutItemRequest req = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(item)
                    .conditionExpression("attribute_not_exists(" + PK + ")")
                    .build();

            client.putItem(req);
        } catch (ConditionalCheckFailedException e) {
            throw new RuntimeException("Il Resume con questo Codice Fiscale esiste già a sistema.");
        } catch (Exception ex) {
            throw new RuntimeException("Failed to put resume into DynamoDB", ex);
        }
    }
}
