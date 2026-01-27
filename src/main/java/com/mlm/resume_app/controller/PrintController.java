package com.mlm.resume_app.controller;

import com.mlm.resume_app.model.ResumeModels;
import com.mlm.resume_app.service.PdfGenerationService;
import com.mlm.resume_app.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    private static final Logger logger = LoggerFactory.getLogger(PrintController.class);

    private final ResumeService resumeService;
    private final PdfGenerationService pdfGenerationService;

    public PrintController(ResumeService resumeService, PdfGenerationService pdfGenerationService) {
        this.resumeService = resumeService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping("/pdf/{pk}")
    public ResponseEntity<byte[]> downloadResumeAsPdf(@PathVariable("pk") String pk) {
        try {
            ResumeModels resume = resumeService.getResumeByPk(pk);
            byte[] pdfBytes = pdfGenerationService.generatePdfFromResume(resume);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "resume_" + pk + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to generate PDF for resume with pk: {}", pk, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
