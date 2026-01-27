package com.mlm.resume_app.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ResumeModels(
    @NotNull
    @Valid
    DatiGenerali datiGenerali,
    List<EsperienzaLavorativa> esperienzeLavorative,
    List<IstruzioneFormazione> istruzioneFormazione,
    List<CompetenzaLinguistica> competenzeLinguistiche,
    List<Competenza> competenzeTrasversali,
    List<Competenza> competenzeTecnologiche,
    List<Competenza> competenzeOrganizzative,
    List<Competenza> competenzeFunzionali
) {}
