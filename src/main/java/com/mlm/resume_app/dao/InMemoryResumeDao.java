package com.mlm.resume_app.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlm.resume_app.model.ResumeModels;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class InMemoryResumeDao implements ResumeDao {

    private ResumeModels seedData;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try (InputStream inputStream = new ClassPathResource("resumeJson/seed.json").getInputStream()) {
            this.seedData = mapper.readValue(inputStream, ResumeModels.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load seed data from JSON", e);
        }
    }

    @Override
    public ResumeModels loadResume() {
        return seedData;
    }

    @Override
    public ResumeModels loadResumeByPk(String pk) {
        if (seedData != null && seedData.datiGenerali() != null && seedData.datiGenerali().codiceFiscale() != null && seedData.datiGenerali().codiceFiscale().equals(pk)) {
            return seedData;
        }
        return null;
    }

    @Override
    public List<String> loadAllResumePk() {
        if (seedData != null && seedData.datiGenerali() != null && seedData.datiGenerali().codiceFiscale() != null) {
            return List.of(seedData.datiGenerali().codiceFiscale());
        }
        return List.of();
    }

    @Override
    public void putResume(ResumeModels resume) {
        // not implemented
    }
}

