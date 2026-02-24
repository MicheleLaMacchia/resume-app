package com.mlm.resume_app.config;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mlm.resume_app.dao.DynamoResumeDaoImpl;
import com.mlm.resume_app.dao.InMemoryResumeDaoImpl;
import com.mlm.resume_app.model.ResumeModels;

@Component
public class DynamoDbInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbInitializer.class);

    private final DynamoResumeDaoImpl dynamoDao;
    private final InMemoryResumeDaoImpl inMemoryDao;

    public DynamoDbInitializer(DynamoResumeDaoImpl dynamoDao, InMemoryResumeDaoImpl inMemoryDao) {
        this.dynamoDao = dynamoDao;
        this.inMemoryDao = inMemoryDao;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing DynamoDB and seeding data (if necessary)");
        dynamoDao.createTableIfNotExists();
        ResumeModels existing = dynamoDao.loadResume();
        if (existing == null) {
            logger.info("No resume found in DynamoDB - seeding initial data");
            ResumeModels seed = inMemoryDao.loadResume();
            if (seed != null) {
                dynamoDao.putResume(seed);
                logger.info("Seeded initial resume into DynamoDB");
            } else {
                logger.warn("InMemoryResumeDao returned null - nothing to seed");
            }
        } else {
            logger.info("Resume already exists in DynamoDB - skipping seed");
        }
    }
}
