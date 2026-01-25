package com.mlm.resume_app.dao;

import com.mlm.resume_app.model.ResumeModels;

public interface ResumeDao {
    ResumeModels loadResume();

    ResumeModels loadResumeByPk(String pk);
}
