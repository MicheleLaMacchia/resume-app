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
}
