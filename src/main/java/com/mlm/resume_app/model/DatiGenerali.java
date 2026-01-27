package com.mlm.resume_app.model;

import jakarta.validation.constraints.NotBlank;

public record DatiGenerali(
	    @NotBlank
	    String codiceFiscale,
	    String nome,
	    String cognome,
	    String dataDiNascita,
	    String paeseDiNascita,
	    String luogoDiNascita,
	    String indirizzoResidenza,
	    String capResidenza,
	    String cittaResidenza,
	    String paeseResidenza,
	    String indirizzoDomicilio,
	    String capDomicilio,
	    String cittaDomicilio,
	    String paeseDomicilio,
	    String nazionalita,
	    String telefono,
	    String email
	) {}
