package com.mlm.resume_app.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mlm.resume_app.model.Competenza;
import com.mlm.resume_app.model.CompetenzaLinguistica;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.EsperienzaLavorativa;
import com.mlm.resume_app.model.IstruzioneFormazione;
import com.mlm.resume_app.model.ResumeModels;

@Repository
public class InMemoryResumeDao implements ResumeDao {

    @Override
    public ResumeModels loadResume() {
        return loadSeed();
    }

    @Override
    public ResumeModels loadResumeByPk(String pk) {
        var seed = loadSeed();
        if (seed != null && seed.datiGenerali() != null && seed.datiGenerali().codiceFiscale() != null && seed.datiGenerali().codiceFiscale().equals(pk)) {
            return seed;
        }
        return null;
    }

    private ResumeModels loadSeed() {
        var datiGenerali = new DatiGenerali(
            "AAAAAA00A00A000A",   // codiceFiscale
            "27/01/1991",          // dataDiNascita
            "Italia",              // paeseDiNascita
            "Roma",                // luogoDiNascita
            "Via Giuseppe Lipparini, 13", // indirizzoResidenza
            "00143",               // capResidenza
            "Roma",                // cittaResidenza
            "Italia",              // paeseResidenza
            "Via Giuseppe Lipparini, 13", // indirizzoDomicilio
            "00143",               // capDomicilio
            "Roma",                // cittaDomicilio
            "Italia",              // paeseDomicilio
            "Italiana",            // nazionalita
            "3336517765",          // telefono
            "michele.lamacchia91@gmail.com" // email
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
