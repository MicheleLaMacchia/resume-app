package com.mlm.resume_app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mlm.resume_app.dao.ResumeDao;
import com.mlm.resume_app.exception.ResourceNotFoundException;
import com.mlm.resume_app.model.Competenza;
import com.mlm.resume_app.model.CompetenzaLinguistica;
import com.mlm.resume_app.model.DatiGenerali;
import com.mlm.resume_app.model.EsperienzaLavorativa;
import com.mlm.resume_app.model.IstruzioneFormazione;
import com.mlm.resume_app.model.ResumeModels;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeServiceImpl.class);

    private final ResumeDao dao;

    public ResumeServiceImpl(ResumeDao dao) {
        this.dao = dao;
    }

    @Override
    public ResumeModels getResume() {
        logger.info("[START] getResume");
        return dao.loadResume();
    }

    @Override
    public DatiGenerali getDatiGenerali() {
        logger.info("[START] getDatiGenerali");
        return getResume().datiGenerali();
    }

    @Override
    public List<EsperienzaLavorativa> getEsperienze() {
        logger.info("[START] getEsperienze");
        return getResume().esperienzeLavorative();
    }

    @Override
    public EsperienzaLavorativa getEsperienzaByIndex(Integer index) {
        logger.info("[START] getEsperienzaByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Esperienza non trovata per index: null");
        }
        var list = getEsperienze();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Esperienza non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<IstruzioneFormazione> getIstruzione() {
        logger.info("[START] getIstruzione");
        return getResume().istruzioneFormazione();
    }

    @Override
    public IstruzioneFormazione getIstruzioneByIndex(Integer index) {
        logger.info("[START] getIstruzioneByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Istruzione non trovata per index: null");
        }
        var list = getIstruzione();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Istruzione non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<CompetenzaLinguistica> getLingue() {
        logger.info("[START] getLingue");
        return getResume().competenzeLinguistiche();
    }

    @Override
    public CompetenzaLinguistica getLinguaByIndex(Integer index) {
        logger.info("[START] getLinguaByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Lingua non trovata per index: null");
        }
        var list = getLingue();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Lingua non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getTrasversali() {
        logger.info("[START] getTrasversali");
        return getResume().competenzeTrasversali();
    }

    @Override
    public Competenza getTrasversaleByIndex(Integer index) {
        logger.info("[START] getTrasversaleByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza trasversale non trovata per index: null");
        }
        var list = getTrasversali();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza trasversale non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getTecnologiche() {
        logger.info("[START] getTecnologiche");
        return getResume().competenzeTecnologiche();
    }

    @Override
    public Competenza getTecnologicaByIndex(Integer index) {
        logger.info("[START] getTecnologicaByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza tecnologica non trovata per index: null");
        }
        var list = getTecnologiche();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza tecnologica non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getOrganizzative() {
        logger.info("[START] getOrganizzative");
        return getResume().competenzeOrganizzative();
    }

    @Override
    public Competenza getOrganizzativaByIndex(Integer index) {
        logger.info("[START] getOrganizzativaByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza organizzativa non trovata per index: null");
        }
        var list = getOrganizzative();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza organizzativa non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getFunzionali() {
        logger.info("[START] getFunzionali");
        return getResume().competenzeFunzionali();
    }

    @Override
    public Competenza getFunzionaleByIndex(Integer index) {
        logger.info("[START] getFunzionaleByIndex :: index: {}", index);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza funzionale non trovata per index: null");
        }
        var list = getFunzionali();
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza funzionale non trovata per index: " + index);
        }
        return list.get(index);
    }
}
