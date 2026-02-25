package com.mlm.resume_app.service;

import java.util.List;

import com.mlm.resume_app.model.Competenza;
import com.mlm.resume_app.model.CompetenzaLinguistica;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.EsperienzaLavorativa;
import com.mlm.resume_app.model.IstruzioneFormazione;
import com.mlm.resume_app.model.ResumeModels;

public interface ResumeService {

    ResumeModels getResume();

    ResumeModels getResumeByPk(String pk);

    DatiGenerali getDatiGenerali();

    List<EsperienzaLavorativa> getEsperienze();

    EsperienzaLavorativa getEsperienzaByIndex(Integer index);

    List<IstruzioneFormazione> getIstruzione();

    IstruzioneFormazione getIstruzioneByIndex(Integer index);

    List<CompetenzaLinguistica> getLingue();

    CompetenzaLinguistica getLinguaByIndex(Integer index);

    List<Competenza> getTrasversali();

    Competenza getTrasversaleByIndex(Integer index);

    List<Competenza> getTecnologiche();

    Competenza getTecnologicaByIndex(Integer index);

    List<Competenza> getOrganizzative();

    Competenza getOrganizzativaByIndex(Integer index);

    List<Competenza> getFunzionali();

    Competenza getFunzionaleByIndex(Integer index);

    // New PK-based methods
    DatiGenerali getDatiGeneraliByPk(String pk);

    List<EsperienzaLavorativa> getEsperienzeByPk(String pk);

    EsperienzaLavorativa getEsperienzaByPkAndIndex(String pk, Integer index);

    List<IstruzioneFormazione> getIstruzioneByPk(String pk);

    IstruzioneFormazione getIstruzioneByPkAndIndex(String pk, Integer index);

    List<CompetenzaLinguistica> getLingueByPk(String pk);

    CompetenzaLinguistica getLinguaByPkAndIndex(String pk, Integer index);

    List<Competenza> getTrasversaliByPk(String pk);

    Competenza getTrasversaleByPkAndIndex(String pk, Integer index);

    List<Competenza> getTecnologicheByPk(String pk);

    Competenza getTecnologicaByPkAndIndex(String pk, Integer index);

    List<Competenza> getOrganizzativeByPk(String pk);

    Competenza getOrganizzativaByPkAndIndex(String pk, Integer index);

    List<Competenza> getFunzionaliByPk(String pk);

    Competenza getFunzionaleByPkAndIndex(String pk, Integer index);

    ResumeModels saveResume(ResumeModels resume);

    List<String> getAllCodiciFiscali();

    // Retrieve a specific version by pk and sk (sk is epoch seconds as string)
    ResumeModels getResumeByPkAndSk(String pk, String sk);

    // Retrieve list of version sks for a pk (newest first)
    java.util.List<String> getResumeVersionsByPk(String pk);
}
