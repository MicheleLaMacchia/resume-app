package com.mlm.resume_app.config;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.mlm.resume_app.dao.DynamoResumeDaoImpl;
import com.mlm.resume_app.dao.InMemoryResumeDaoImpl;
import com.mlm.resume_app.model.ResumeModels;

@Component
public class DynamoDbInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbInitializer.class);

    private final DynamoResumeDaoImpl dynamoDao;
    private final InMemoryResumeDaoImpl inMemoryDao;

    public DynamoDbInitializer(DynamoResumeDaoImpl dynamoDao, @Autowired(required = false) InMemoryResumeDaoImpl inMemoryDao) {
        this.dynamoDao = dynamoDao;
        this.inMemoryDao = inMemoryDao;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing DynamoDB and seeding data (if necessary)");
        dynamoDao.createTableIfNotExists();
        ResumeModels existing = dynamoDao.loadResume();
        if (existing == null) {
            logger.info("No resume found in DynamoDB - attempting to seed initial data");
            ResumeModels seed = null;
            if (inMemoryDao != null) {
                seed = inMemoryDao.loadResume();
            } else {
                logger.warn("InMemoryResumeDaoImpl bean not present for this profile - skipping automatic seeding");
            }
            if (seed != null) {
                dynamoDao.putResume(seed);
                logger.info("Seeded initial resume into DynamoDB");
            } else {
                logger.warn("No seed data available - nothing to seed");
            }
        } else {
            logger.info("Resume already exists in DynamoDB - skipping seed");
        }
    }
}
