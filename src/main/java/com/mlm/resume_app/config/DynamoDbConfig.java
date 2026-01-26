package com.mlm.resume_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.region:eu-central-1}")
    private String awsRegion;

    @Value("${aws.dynamodb.endpoint:}")
    private String dynamoEndpoint;

    /**
     * Bean per sviluppo locale: usa l'endpoint configurato (es. http://localhost:8000)
     * e credenziali fittizie richieste da DynamoDB Local.
     */
    @Bean
    @Profile("local")
    public DynamoDbClient localDynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .region(Region.of(awsRegion));

        if (dynamoEndpoint != null && !dynamoEndpoint.isBlank()) {
            builder.endpointOverride(URI.create(dynamoEndpoint));
            builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")));
        }

        return builder.build();
    }

    /**
     * Bean per ambienti non-local (ad es. Lambda): non forniamo credenziali qui,
     * ci affidiamo al DefaultCredentialsProvider (IAM role su Lambda, ecc.).
     */
    @Bean
    @Profile("!local")
    public DynamoDbClient awsDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
