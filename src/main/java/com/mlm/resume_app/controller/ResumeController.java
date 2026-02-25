package com.mlm.resume_app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.mlm.resume_app.model.Competenza;
import com.mlm.resume_app.model.CompetenzaLinguistica;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.EsperienzaLavorativa;
import com.mlm.resume_app.model.IstruzioneFormazione;
import com.mlm.resume_app.model.ResumeModels;
import com.mlm.resume_app.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createResume(@Valid @RequestBody ResumeModels resume) {
        resumeService.saveResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getResumeList() {
        return ResponseEntity.ok(resumeService.getAllCodiciFiscali());
    }

    @GetMapping("/{pk}")
    public ResumeModels getResume(@PathVariable("pk") String pk) {
        logger.info("GET /api/resume/{}", pk);
        return resumeService.getResumeByPk(pk);
    }

    // Return the latest resume explicitly (same as GET /{pk} but clearer for clients)
    @GetMapping("/{pk}/latest")
    public ResumeModels getLatestResume(@PathVariable("pk") String pk) {
        logger.info("GET /api/resume/{}/latest", pk);
        return resumeService.getResumeByPk(pk);
    }

    // Return list of version SKs (epoch seconds as string) for the given pk, newest first
    @GetMapping("/{pk}/versions")
    public ResponseEntity<java.util.List<String>> getResumeVersions(@PathVariable("pk") String pk) {
        logger.info("GET /api/resume/{}/versions", pk);
        var versions = resumeService.getResumeVersionsByPk(pk);
        return ResponseEntity.ok(versions);
    }

    // The following per-card endpoints are kept for backward compatibility but commented out to reduce redundant calls from the frontend.
    // When the dashboard loads, frontend now requests the full resume (latest) and distributes data to cards locally.
    @GetMapping("/personal-data/{pk}")
    public DatiGenerali getPersonalData(@PathVariable("pk") String pk) {
        logger.info("GET /api/personal-data/{}", pk);
        return resumeService.getDatiGeneraliByPk(pk);
    }

    @GetMapping("/working-experience/{pk}")
    public List<EsperienzaLavorativa> getWorkingExperience(@PathVariable("pk") String pk) {
        logger.info("GET /api/working-experience/{}", pk);
        return resumeService.getEsperienzeByPk(pk);
    }

    @GetMapping("/working-experience/{pk}/{index}")
    public ResponseEntity<EsperienzaLavorativa> getWorkingExperienceByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/working-experience/{}/{}", pk, index);
        var item = resumeService.getEsperienzaByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/education-training/{pk}")
    public List<IstruzioneFormazione> getEducationTraining(@PathVariable("pk") String pk) {
        logger.info("GET /api/education-training/{}", pk);
        return resumeService.getIstruzioneByPk(pk);
    }

    @GetMapping("/education-training/{pk}/{index}")
    public ResponseEntity<IstruzioneFormazione> getEducationTrainingByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/education-training/{}/{}", pk, index);
        var item = resumeService.getIstruzioneByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/language-skills/{pk}")
    public List<CompetenzaLinguistica> getLanguageSkills(@PathVariable("pk") String pk) {
        logger.info("GET /api/language-skills/{}", pk);
        return resumeService.getLingueByPk(pk);
    }

    @GetMapping("/language-skills/{pk}/{index}")
    public ResponseEntity<CompetenzaLinguistica> getLanguageSkillsByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/language-skills/{}/{}", pk, index);
        var item = resumeService.getLinguaByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/soft-skills/{pk}")
    public List<Competenza> getSoftSkills(@PathVariable("pk") String pk) {
        logger.info("GET /api/soft-skills/{}", pk);
        return resumeService.getTrasversaliByPk(pk);
    }

    @GetMapping("/soft-skills/{pk}/{index}")
    public ResponseEntity<Competenza> getSoftSkillsByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/soft-skills/{}/{}", pk, index);
        var item = resumeService.getTrasversaleByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/technical-skills/{pk}")
    public List<Competenza> getTechnicalSkills(@PathVariable("pk") String pk) {
        logger.info("GET /api/technical-skills/{}", pk);
        return resumeService.getTecnologicheByPk(pk);
    }

    @GetMapping("/technical-skills/{pk}/{index}")
    public ResponseEntity<Competenza> getTechnicalSkillsByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/technical-skills/{}/{}", pk, index);
        var item = resumeService.getTecnologicaByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/organizational-skills/{pk}")
    public List<Competenza> getOrganizationalSkills(@PathVariable("pk") String pk) {
        logger.info("GET /api/organizational-skills/{}", pk);
        return resumeService.getOrganizzativeByPk(pk);
    }

    @GetMapping("/organizational-skills/{pk}/{index}")
    public ResponseEntity<Competenza> getOrganizationalSkillsByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/organizational-skills/{}/{}", pk, index);
        var item = resumeService.getOrganizzativaByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/functional-skills/{pk}")
    public List<Competenza> getFunctionalSkills(@PathVariable("pk") String pk) {
        logger.info("GET /api/functional-skills/{}", pk);
        return resumeService.getFunzionaliByPk(pk);
    }

    @GetMapping("/functional-skills/{pk}/{index}")
    public ResponseEntity<Competenza> getFunctionalSkillsByIndex(@PathVariable("pk") String pk, @PathVariable("index") Integer index) {
        logger.info("GET /api/functional-skills/{}/{}", pk, index);
        var item = resumeService.getFunzionaleByPkAndIndex(pk, index);
        return ResponseEntity.ok(item);
    }

}
