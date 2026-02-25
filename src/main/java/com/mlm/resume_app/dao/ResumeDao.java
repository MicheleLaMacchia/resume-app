package com.mlm.resume_app.dao;

import com.mlm.resume_app.model.ResumeModels;

import java.util.List;

public interface ResumeDao {
    ResumeModels loadResume();

    ResumeModels loadResumeByPk(String pk);

    // Load a specific resume version identified by pk and sk (sk is epoch seconds as string)
    ResumeModels loadResumeByPkAndSk(String pk, String sk);

    // Return list of sk values (epoch seconds as string) for all versions of a given pk (sorted newest first)
    java.util.List<String> loadResumeVersions(String pk);

    List<String> loadAllResumePk();

    void putResume(ResumeModels resume);

}
