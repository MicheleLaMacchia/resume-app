package com.mlm.resume_app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlm.resume_app.model.Competenza;
import com.mlm.resume_app.model.CompetenzaLinguistica;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.EsperienzaLavorativa;
import com.mlm.resume_app.model.IstruzioneFormazione;
import com.mlm.resume_app.model.ResumeModels;
import com.mlm.resume_app.service.ResumeService;

@RestController
@RequestMapping("/api")
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/resume")
    public ResumeModels getResume() {
        logger.info("GET /api/resume");
        return resumeService.getResume();
    }

    @GetMapping("/personal-data")
    public DatiGenerali getPersonalData() {
        logger.info("GET /api/personal-data");
        return resumeService.getDatiGenerali();
    }

    @GetMapping("/working-experience")
    public List<EsperienzaLavorativa> getWorkingExperience() {
        logger.info("GET /api/working-experience");
        return resumeService.getEsperienze();
    }

    @GetMapping("/working-experience/{index}")
    public ResponseEntity<EsperienzaLavorativa> getWorkingExperienceByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/working-experience/{}", index);
        var item = resumeService.getEsperienzaByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/education-training")
    public List<IstruzioneFormazione> getEducationTraining() {
        logger.info("GET /api/education-training");
        return resumeService.getIstruzione();
    }

    @GetMapping("/education-training/{index}")
    public ResponseEntity<IstruzioneFormazione> getEducationTrainingByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/education-training/{}", index);
        var item = resumeService.getIstruzioneByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/language-skills")
    public List<CompetenzaLinguistica> getLanguageSkills() {
        logger.info("GET /api/language-skills");
        return resumeService.getLingue();
    }

    @GetMapping("/language-skills/{index}")
    public ResponseEntity<CompetenzaLinguistica> getLanguageSkillsByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/language-skills/{}", index);
        var item = resumeService.getLinguaByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/soft-skills")
    public List<Competenza> getSoftSkills() {
        logger.info("GET /api/soft-skills");
        return resumeService.getTrasversali();
    }

    @GetMapping("/soft-skills/{index}")
    public ResponseEntity<Competenza> getSoftSkillsByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/soft-skills/{}", index);
        var item = resumeService.getTrasversaleByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/technical-skills")
    public List<Competenza> getTechnicalSkills() {
        logger.info("GET /api/technical-skills");
        return resumeService.getTecnologiche();
    }

    @GetMapping("/technical-skills/{index}")
    public ResponseEntity<Competenza> getTechnicalSkillsByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/technical-skills/{}", index);
        var item = resumeService.getTecnologicaByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/organizational-skills")
    public List<Competenza> getOrganizationalSkills() {
        logger.info("GET /api/organizational-skills");
        return resumeService.getOrganizzative();
    }

    @GetMapping("/organizational-skills/{index}")
    public ResponseEntity<Competenza> getOrganizationalSkillsByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/organizational-skills/{}", index);
        var item = resumeService.getOrganizzativaByIndex(index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/functional-skills")
    public List<Competenza> getFunctionalSkills() {
        logger.info("GET /api/functional-skills");
        return resumeService.getFunzionali();
    }

    @GetMapping("/functional-skills/{index}")
    public ResponseEntity<Competenza> getFunctionalSkillsByIndex(@PathVariable("index") Integer index) {
        logger.info("GET /api/functional-skills/{}", index);
        var item = resumeService.getFunzionaleByIndex(index);
        return ResponseEntity.ok(item);
    }
}
