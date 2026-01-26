package com.mlm.resume_app.dao;

import com.mlm.resume_app.model.ResumeModels;

import java.util.List;

public interface ResumeDao {
    ResumeModels loadResume();

    ResumeModels loadResumeByPk(String pk);

    List<String> loadAllResumePk();

    void putResume(ResumeModels resume);

}
