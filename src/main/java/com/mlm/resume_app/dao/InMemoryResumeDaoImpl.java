package com.mlm.resume_app.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlm.resume_app.exception.DuplicateResumeException;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.ResumeModels;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

@Repository
@Profile("local-inmemory")
public class InMemoryResumeDaoImpl implements ResumeDao {

    private static final List<ResumeModels> resumeList = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try (InputStream inputStream = new ClassPathResource("resumeJson/seed.json").getInputStream()) {
            ResumeModels resume = mapper.readValue(inputStream, ResumeModels.class);
            resumeList.clear();
            resumeList.add(resume);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load seed data from JSON", e);
        }
    }

    @Override
    public ResumeModels loadResume() {
        return resumeList.isEmpty() ? null : resumeList.get(0);
    }

    @Override
    public ResumeModels loadResumeByPk(String pk) {
        for (ResumeModels resume : resumeList) {
            if (resume != null && resume.datiGenerali() != null &&
                    resume.datiGenerali().codiceFiscale() != null &&
                    resume.datiGenerali().codiceFiscale().equals(pk)) {
                return resume;
            }
        }
        return null;
    }

    @Override
    public List<String> loadAllResumePk() {
        if (!resumeList.isEmpty()) {
            return resumeList.stream()
                    .map(ResumeModels::datiGenerali)
                    .map(DatiGenerali::codiceFiscale)
                    .toList();
        }
        return List.of();
    }

    @Override
    public void putResume(ResumeModels resume) {
        try {
            if (resume == null || resume.datiGenerali() == null || resume.datiGenerali().codiceFiscale() == null) {
                throw new RuntimeException("Codice fiscale non presente");
            }
            String codiceFiscale = resume.datiGenerali().codiceFiscale();
            boolean exists = resumeList.stream()
                    .anyMatch(res -> res != null &&
                                            res.datiGenerali() != null &&
                                            codiceFiscale.equals(res.datiGenerali().codiceFiscale()));
            if (exists) {
                throw new DuplicateResumeException("Il Resume con questo Codice Fiscale esiste già a sistema.");
            }
            long epoch = Instant.now().getEpochSecond();
            ResumeModels updatedResume = new ResumeModels(epoch, resume.datiGenerali(), resume.esperienzeLavorative(), resume.istruzioneFormazione(), resume.competenzeLinguistiche(), resume.competenzeTrasversali(), resume.competenzeTecnologiche(), resume.competenzeOrganizzative(), resume.competenzeFunzionali());
            resumeList.add(updatedResume);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to put resume", ex);
        }
    }
}
