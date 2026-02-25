package com.mlm.resume_app.controller;

import com.mlm.resume_app.dao.InMemoryResumeDaoImpl;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.time.Instant;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private final ResumeService resumeService;
    private final InMemoryResumeDaoImpl inMemoryResumeDaoImpl;

    public TestController(ResumeService resumeService, @Autowired(required = false) InMemoryResumeDaoImpl inMemoryResumeDaoImpl) {
        this.resumeService = resumeService;
        this.inMemoryResumeDaoImpl = inMemoryResumeDaoImpl;
    }

    @GetMapping("/mock")
    public ResumeModels getTest() {
        logger.info("GET /api/test/mock");
        return resumeService.getResumeByPk("AAAAAA00A00A000A");
    }

    @GetMapping("/create-mock")
    public ResponseEntity<ResumeModels> createMockResume() {
        logger.info("GET /api/test/create-mock");

        if (inMemoryResumeDaoImpl == null) {
            logger.warn("InMemoryResumeDaoImpl bean not present - cannot create mock resume");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        ResumeModels seedData = inMemoryResumeDaoImpl.loadResume();

        DatiGenerali newDatiGenerali = new DatiGenerali(
            generateRandomCodiceFiscale(),
            seedData.datiGenerali().nome(),
            seedData.datiGenerali().cognome(),
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
            Instant.now().getEpochSecond(),
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
