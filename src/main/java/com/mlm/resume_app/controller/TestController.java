package com.mlm.resume_app.controller;

import com.mlm.resume_app.dao.InMemoryResumeDao;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.ResumeModels;
import com.mlm.resume_app.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private final ResumeService resumeService;
    private final InMemoryResumeDao inMemoryResumeDao;

    public TestController(ResumeService resumeService, InMemoryResumeDao inMemoryResumeDao) {
        this.resumeService = resumeService;
        this.inMemoryResumeDao = inMemoryResumeDao;
    }

    @GetMapping("/resume")
    public ResumeModels getTest() {
        logger.info("GET /api/test/resume");
        return resumeService.getResumeByPk("AAAAAA00A00A000A");
    }

    @GetMapping("/create-mock")
    public ResponseEntity<ResumeModels> createMockResume() {
        logger.info("GET /api/test/create-mock");

        ResumeModels seedData = inMemoryResumeDao.loadResume();

        DatiGenerali newDatiGenerali = new DatiGenerali(
            generateRandomCodiceFiscale(),
            seedData.datiGenerali().dataDiNascita(),
            seedData.datiGenerali().paeseDiNascita(),
            seedData.datiGenerali().luogoDiNascita(),
            seedData.datiGenerali().indirizzoResidenza(),
            seedData.datiGenerali().capResidenza(),
            seedData.datiGenerali().cittaResidenza(),
            seedData.datiGenerali().paeseResidenza(),
            seedData.datiGenerali().indirizzoDomicilio(),
            seedData.datiGenerali().capDomicilio(),
            seedData.datiGenerali().cittaDomicilio(),
            seedData.datiGenerali().paeseDomicilio(),
            seedData.datiGenerali().nazionalita(),
            seedData.datiGenerali().telefono(),
            seedData.datiGenerali().email()
        );

        ResumeModels mockResume = new ResumeModels(
            newDatiGenerali,
            seedData.esperienzeLavorative(),
            seedData.istruzioneFormazione(),
            seedData.competenzeLinguistiche(),
            seedData.competenzeTrasversali(),
            seedData.competenzeTecnologiche(),
            seedData.competenzeOrganizzative(),
            seedData.competenzeFunzionali()
        );

        resumeService.saveResume(mockResume);
        return ResponseEntity.status(HttpStatus.CREATED).body(mockResume);
    }

    private String generateRandomCodiceFiscale() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
