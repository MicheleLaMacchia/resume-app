package com.mlm.resume_app.model;

public record EsperienzaLavorativa(
	    String dataInizio,
	    String dataFine,
	    String azienda,
	    String ruolo,
	    String descrizione,
	    String altro
	) {}
