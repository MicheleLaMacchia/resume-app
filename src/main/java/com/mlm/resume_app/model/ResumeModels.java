package com.mlm.resume_app.model;

import java.util.List;

public record ResumeModels(
    DatiGenerali datiGenerali,
    List<EsperienzaLavorativa> esperienzeLavorative,
    List<IstruzioneFormazione> istruzioneFormazione,
    List<CompetenzaLinguistica> competenzeLinguistiche,
    List<Competenza> competenzeTrasversali,
    List<Competenza> competenzeTecnologiche,
    List<Competenza> competenzeOrganizzative,
    List<Competenza> competenzeFunzionali
) {}
