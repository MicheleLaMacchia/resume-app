package com.mlm.resume_app.dao;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.Instant;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlm.resume_app.exception.DuplicateResumeException;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.ResumeModels;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Primary
@Repository
@Profile({"default",
          "local",
          "!local-inmemory"
})
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
        // Try legacy ROOT/RESUME first for backward compatibility
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

        // Try to obtain any codice fiscale from the INDEX (efficient)
        try {
            QueryRequest idxReq = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .keyConditionExpression(PK + " = :indexPk")
                    .expressionAttributeValues(Map.of(":indexPk", AttributeValue.builder().s("INDEX").build()))
                    .limit(1)
                    .build();
            var idxResp = client.query(idxReq);
            if (idxResp != null && idxResp.count() > 0 && idxResp.items() != null && !idxResp.items().isEmpty()) {
                var item = idxResp.items().get(0);
                var cfAv = item.get(SK);
                if (cfAv != null && cfAv.s() != null) {
                    return loadResumeByPk(cfAv.s());
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback: scan any item (legacy behavior)
        try {
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
            QueryRequest req = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .keyConditionExpression(PK + " = :pk")
                    .expressionAttributeValues(Map.of(":pk", AttributeValue.builder().s(pk).build()))
                    .scanIndexForward(false) // newest first
                    .limit(1)
                    .build();
            QueryResponse resp = client.query(req);
            if (resp == null || resp.count() == 0 || resp.items() == null || resp.items().isEmpty()) {
                return null;
            }
            AttributeValue av = resp.items().get(0).get(DATA);
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
            // Use the INDEX items to retrieve all codice fiscale values efficiently
            QueryRequest req = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .keyConditionExpression(PK + " = :indexPk")
                    .expressionAttributeValues(Map.of(":indexPk", AttributeValue.builder().s("INDEX").build()))
                    .build();
            QueryResponse response = client.query(req);

            if (response == null || response.items() == null) {
                return List.of();
            }

            return response.items().stream()
                    .map(item -> item.get(SK).s())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load all resume PKs", e);
        }
    }

    @Override
    public ResumeModels loadResumeByPkAndSk(String pk, String sk) {
        if (pk == null || pk.isBlank() || sk == null || sk.isBlank()) {
            return null;
        }
        try {
            GetItemRequest req = GetItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(Map.of(PK, AttributeValue.builder().s(pk).build(), SK, AttributeValue.builder().s(sk).build()))
                    .build();
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
            throw new RuntimeException("Failed to load resume by pk and sk", e);
        }
    }

    @Override
    public java.util.List<String> loadResumeVersions(String pk) {
        if (pk == null || pk.isBlank()) {
            return List.of();
        }
        try {
            QueryRequest req = QueryRequest.builder()
                    .tableName(TABLE_NAME)
                    .keyConditionExpression(PK + " = :pk")
                    .expressionAttributeValues(Map.of(":pk", AttributeValue.builder().s(pk).build()))
                    .scanIndexForward(false) // newest first
                    .projectionExpression(SK)
                    .build();
            QueryResponse resp = client.query(req);
            if (resp == null || resp.items() == null) {
                return List.of();
            }
            return resp.items().stream()
                    .map(item -> item.get(SK))
                    .filter(Objects::nonNull)
                    .map(AttributeValue::s)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resume versions for pk", e);
        }
    }

    @Override
    public void putResume(ResumeModels resume) {
        try {
            String codiceFiscale = null;
            if (resume != null && resume.datiGenerali() != null) {
                codiceFiscale = resume.datiGenerali().codiceFiscale();
            }
            // if codice fiscale is present, use it as PK and create a timestamp SK; otherwise fallback to ROOT/RESUME
            Map<String, AttributeValue> item;
            if (codiceFiscale != null && !codiceFiscale.isBlank()) {
                long epoch = Instant.now().getEpochSecond();
                // create a new ResumeModels instance with createdAt set
                ResumeModels updatedResume = new ResumeModels(epoch, resume.datiGenerali(), resume.esperienzeLavorative(), resume.istruzioneFormazione(), resume.competenzeLinguistiche(), resume.competenzeTrasversali(), resume.competenzeTecnologiche(), resume.competenzeOrganizzative(), resume.competenzeFunzionali());
                String json = mapper.writeValueAsString(updatedResume);

                String timestamp = String.valueOf(epoch);
                item = Map.of(
                        PK, AttributeValue.builder().s(codiceFiscale).build(),
                        SK, AttributeValue.builder().s(timestamp).build(),
                        DATA, AttributeValue.builder().s(json).build()
                );

                // index item to allow efficient listing of all CFs
                Map<String, AttributeValue> indexItem = Map.of(
                        PK, AttributeValue.builder().s("INDEX").build(),
                        SK, AttributeValue.builder().s(codiceFiscale).build(),
                        DATA, AttributeValue.builder().s("{}" ).build()
                );

                // attempt an atomic transaction: put resume + put index (index put is conditional)
                Put resumePut = Put.builder().tableName(TABLE_NAME).item(item).build();
                Put indexPut = Put.builder().tableName(TABLE_NAME).item(indexItem).conditionExpression("attribute_not_exists(" + PK + ")").build();

                TransactWriteItemsRequest txReq = TransactWriteItemsRequest.builder()
                        .transactItems(TransactWriteItem.builder().put(resumePut).build(),
                                TransactWriteItem.builder().put(indexPut).build())
                        .build();
                try {
                    client.transactWriteItems(txReq);
                } catch (TransactionCanceledException e) {
                    // index likely already exists; fallback to single put of resume
                    PutItemRequest req = PutItemRequest.builder().tableName(TABLE_NAME).item(item).build();
                    client.putItem(req);
                }

            } else {
                String jsonRoot = mapper.writeValueAsString(resume);
                item = Map.of(
                        PK, AttributeValue.builder().s("ROOT").build(),
                        SK, AttributeValue.builder().s("RESUME").build(),
                        DATA, AttributeValue.builder().s(jsonRoot).build()
                );
                PutItemRequest req = PutItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .item(item)
                        .conditionExpression("attribute_not_exists(" + PK + ")")
                        .build();
                client.putItem(req);
            }
        } catch (ConditionalCheckFailedException e) {
            throw new DuplicateResumeException("Il Resume con questo Codice Fiscale esiste già a sistema.");
        } catch (Exception ex) {
            throw new RuntimeException("Failed to put resume into DynamoDB", ex);
        }
    }
}
