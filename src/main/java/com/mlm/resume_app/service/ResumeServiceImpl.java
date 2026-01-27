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
    public ResumeModels getResumeByPk(String pk) {
        logger.info("[START] getResumeByPk :: pk: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm;
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

    @Override
    public DatiGenerali getDatiGeneraliByPk(String pk) {
        logger.info("[START] getDatiGeneraliByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.datiGenerali();
    }

    @Override
    public List<EsperienzaLavorativa> getEsperienzeByPk(String pk) {
        logger.info("[START] getEsperienzeByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.esperienzeLavorative();
    }

    @Override
    public EsperienzaLavorativa getEsperienzaByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getEsperienzaByPkAndIndex, parametro: {}", pk);
        var list = getEsperienzeByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Esperienza non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Esperienza non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<IstruzioneFormazione> getIstruzioneByPk(String pk) {
        logger.info("[START] getIstruzioneByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.istruzioneFormazione();
    }

    @Override
    public IstruzioneFormazione getIstruzioneByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getIstruzioneByPkAndIndex, parametro: {}", pk);
        var list = getIstruzioneByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Istruzione non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Istruzione non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<CompetenzaLinguistica> getLingueByPk(String pk) {
        logger.info("[START] getLingueByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.competenzeLinguistiche();
    }

    @Override
    public CompetenzaLinguistica getLinguaByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getLinguaByPkAndIndex, parametro: {}", pk);
        var list = getLingueByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Lingua non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Lingua non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getTrasversaliByPk(String pk) {
        logger.info("[START] getTrasversaliByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.competenzeTrasversali();
    }

    @Override
    public Competenza getTrasversaleByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getTrasversaleByPkAndIndex, parametro: {}", pk);
        var list = getTrasversaliByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza trasversale non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza trasversale non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getTecnologicheByPk(String pk) {
        logger.info("[START] getTecnologicheByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.competenzeTecnologiche();
    }

    @Override
    public Competenza getTecnologicaByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getTecnologicaByPkAndIndex, parametro: {}", pk);
        var list = getTecnologicheByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza tecnologica non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza tecnologica non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getOrganizzativeByPk(String pk) {
        logger.info("[START] getOrganizzativeByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.competenzeOrganizzative();
    }

    @Override
    public Competenza getOrganizzativaByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getOrganizzativaByPkAndIndex, parametro: {}", pk);
        var list = getOrganizzativeByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza organizzativa non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza organizzativa non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public List<Competenza> getFunzionaliByPk(String pk) {
        logger.info("[START] getFunzionaliByPk, parametro: {}", pk);
        var rm = dao.loadResumeByPk(pk);
        if (rm == null) {
            logger.warn("Resume not found for pk: {}", pk);
            throw new ResourceNotFoundException("Resume non trovato per pk: " + pk);
        }
        return rm.competenzeFunzionali();
    }

    @Override
    public Competenza getFunzionaleByPkAndIndex(String pk, Integer index) {
        logger.info("[START] getFunzionaleByPkAndIndex :: pk: {} :: index: {}", pk, index);
        var list = getFunzionaliByPk(pk);
        if (index == null) {
            logger.warn("index is null");
            throw new ResourceNotFoundException("Competenza funzionale non trovata per index: null");
        }
        if (index < 0 || index >= list.size()) {
            logger.warn("index out of range: {}", index);
            throw new ResourceNotFoundException("Competenza funzionale non trovata per index: " + index);
        }
        return list.get(index);
    }

    @Override
    public ResumeModels saveResume(ResumeModels resume) {
        logger.info("[START] saveResume :: resume: {}", resume);
        dao.putResume(resume);
        logger.info("Resume saved successfully for pk: {}", resume.datiGenerali().codiceFiscale());
        return resume;
    }

    public List<String> getAllCodiciFiscali() {
        logger.info("[START] getAllCodiciFiscali");
        return dao.loadAllResumePk();
    }
}
