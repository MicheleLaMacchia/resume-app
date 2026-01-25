package com.mlm.resume_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api")
public class ResumeController {

    @GetMapping("/resume")
    public ResumeModels getResume() {
        return buildResume();
    }

    // Nuovi endpoint richiesti

    @GetMapping("/personal-data")
    public DatiGenerali getPersonalData() {
        return buildResume().datiGenerali();
    }

    @GetMapping("/working-experience")
    public List<EsperienzaLavorativa> getWorkingExperience() {
        return buildResume().esperienzeLavorative();
    }

    @GetMapping("/working-experience/{index}")
    public ResponseEntity<EsperienzaLavorativa> getWorkingExperienceByIndex(@PathVariable("index") int index) {
        var esperienze = buildResume().esperienzeLavorative();
        if (index < 0 || index >= esperienze.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(esperienze.get(index));
    }

    @GetMapping("/education-training")
    public List<IstruzioneFormazione> getEducationTraining() {
        return buildResume().istruzioneFormazione();
    }

    @GetMapping("/education-training/{index}")
    public ResponseEntity<IstruzioneFormazione> getEducationTrainingByIndex(@PathVariable("index") int index) {
        var lista = buildResume().istruzioneFormazione();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    @GetMapping("/language-skills")
    public List<CompetenzaLinguistica> getLanguageSkills() {
        return buildResume().competenzeLinguistiche();
    }

    @GetMapping("/language-skills/{index}")
    public ResponseEntity<CompetenzaLinguistica> getLanguageSkillsByIndex(@PathVariable("index") int index) {
        var lista = buildResume().competenzeLinguistiche();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    @GetMapping("/soft-skills")
    public List<Competenza> getSoftSkills() {
        return buildResume().competenzeTrasversali();
    }

    @GetMapping("/soft-skills/{index}")
    public ResponseEntity<Competenza> getSoftSkillsByIndex(@PathVariable("index") int index) {
        var lista = buildResume().competenzeTrasversali();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    @GetMapping("/technical-skills")
    public List<Competenza> getTechnicalSkills() {
        return buildResume().competenzeTecnologiche();
    }

    @GetMapping("/technical-skills/{index}")
    public ResponseEntity<Competenza> getTechnicalSkillsByIndex(@PathVariable("index") int index) {
        var lista = buildResume().competenzeTecnologiche();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    @GetMapping("/organizational-skills")
    public List<Competenza> getOrganizationalSkills() {
        return buildResume().competenzeOrganizzative();
    }

    @GetMapping("/organizational-skills/{index}")
    public ResponseEntity<Competenza> getOrganizationalSkillsByIndex(@PathVariable("index") int index) {
        var lista = buildResume().competenzeOrganizzative();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    @GetMapping("/functional-skills")
    public List<Competenza> getFunctionalSkills() {
        return buildResume().competenzeFunzionali();
    }

    @GetMapping("/functional-skills/{index}")
    public ResponseEntity<Competenza> getFunctionalSkillsByIndex(@PathVariable("index") int index) {
        var lista = buildResume().competenzeFunzionali();
        if (index < 0 || index >= lista.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lista.get(index));
    }

    private ResumeModels buildResume() {
        var datiGenerali = new DatiGenerali(
            "27/01/1991",
            "Roma, Italia",
            "Via Giuseppe Lipparini, 13, 00143, Roma, Italia",
            "Italiana",
            "Tel: (+39) 3336517765, Email: michele.lamacchia91@gmail.com"
        );

        var esperienze = List.of(
            new EsperienzaLavorativa(
                "18/09/2023", "Attuale", 
                "ENGINEERING INGEGNERIA INFORMATICA",
                "SOFTWARE DEVELOPMENT SPECIALIST",
                "Software Engineer / Tech Lead su ecosistemi di micro-servizi e web app.",
                "Sviluppo Fullstack (Java/Spring, Angular), Cloud, Docker, Agile."
            ),
            new EsperienzaLavorativa(
                "01/05/2021", "15/09/2023",
                "NTTDATA",
                "SVILUPPATORE DI SOFTWARE",
                "Supporto tecnico progetto Notifica Digitale PagoPA.",
                "JavaEE, AWS (DynamoDB, Lambda, SQS), React, Node.js."
            ),
            new EsperienzaLavorativa(
                "31/08/2020", "02/04/2021", 
                "GRUPPO SINCRONO", 
                "JAVA DEVELOPER", 
                "Sviluppo CRM e consulenza NTTData per portale Agenzia delle Entrate.",
                "Java, MyBatis, Oracle, React."
            )
        );

        var istruzione = List.of(
            new IstruzioneFormazione(
                "22/12/2025", "22/12/2028",
                "Google Cloud",
                "ASSOCIATE CLOUD ENGINEER CERTIFICATION",
                "Certificazione tecnica Google Cloud." 
            ),
            new IstruzioneFormazione(
                "22/01/2022", "22/01/2025",
                "AWS",
                "AWS CERTIFIED CLOUD PRACTITIONER",
                "Certificazione base Cloud AWS."
            ),
            new IstruzioneFormazione(
                "02/02/2020", "15/07/2020", 
                "Gruppo Sincrono",
                "ATTESTATO DI PROGRAMMAZIONE JAVA", 
                "Corso intensivo di programmazione Java."
            )
        );

        var lingue = List.of(
            new CompetenzaLinguistica("ITALIANO", "Madrelingua", "Madrelingua", "Madrelingua", "Madrelingua"),
            new CompetenzaLinguistica("INGLESE", "B2", "B2", "B2", "B2") 
        );

        var trasversali = List.of(
            new Competenza("Problem Solving Analitico", "Analisi logica e risoluzione problematiche complesse."), 
            new Competenza("Comunicazione Efficace", "Gestione Stakeholder e trasmissione info tecniche."), 
            new Competenza("Flessibilità", "Adattamento a contesti dinamici e nuove tecnologie.")
        );

        var tecnologiche = List.of(
            new Competenza("Sviluppo Fullstack", "Java (Spring) e Angular/React."), 
            new Competenza("Cloud & Container", "AWS, GCP e Docker."), 
            new Competenza("AI-Assisted Engineering", "Integrazione AI generativa per efficienza codice.") 
        );

        var organizzative = List.of(
            new Competenza("Gestione Agile", "Operatività Jira e flussi di lavoro Agile."), 
            new Competenza("Tech Leadership", "Mentoring, onboarding e code review.") 
        );

        var funzionali = List.of(
            new Competenza("Analisi Requisiti", "Traduzione esigenze business in specifiche tecniche."), 
            new Competenza("Processi PA", "Conoscenza dinamiche funzionali Pubblica Amministrazione.") 
        );

        return new ResumeModels(
            datiGenerali, esperienze, istruzione, lingue, trasversali, tecnologiche, organizzative, funzionali
        );
    }
}
