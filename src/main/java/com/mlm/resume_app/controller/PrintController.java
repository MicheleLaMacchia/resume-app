package com.mlm.resume_app.controller;

import com.mlm.resume_app.exception.PdfGenerationException;
import com.mlm.resume_app.exception.ResourceNotFoundException;
import com.mlm.resume_app.model.ResumeModels;
import com.mlm.resume_app.service.PdfGenerationService;
import com.mlm.resume_app.service.ResumeService;
import com.mlm.resume_app.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    private static final Logger logger = LoggerFactory.getLogger(PrintController.class);

    private final ResumeService resumeService;
    private final PdfGenerationService pdfGenerationService;
    private final TemplateService templateService;

    public PrintController(ResumeService resumeService, PdfGenerationService pdfGenerationService, TemplateService templateService) {
        this.resumeService = resumeService;
        this.pdfGenerationService = pdfGenerationService;
        this.templateService = templateService;
    }

    @GetMapping("/pdf/{pk}")
    public ResponseEntity<byte[]> downloadResumeAsPdf(@PathVariable("pk") String pk,
                                                      @RequestParam(name = "template", required = true) String template) {
        try {
            ResumeModels resume = resumeService.getResumeByPk(pk);
            byte[] pdfBytes = pdfGenerationService.generatePdfFromResume(resume, template);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "resume_" + pk + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            // Let the global exception handler map this to 404
            throw e;
        } catch (PdfGenerationException pex) {
            // Let GlobalExceptionHandler handle PdfGenerationException
            throw pex;
        } catch (Exception e) {
            logger.error("Failed to generate PDF for resume with pk: {} and template: {}", pk, template, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/templates")
    public ResponseEntity<java.util.List<String>> listAvailableTemplates() {
        try {
            java.util.List<String> templates = templateService.listTemplateNames();
            return ResponseEntity.ok(templates);
        } catch (Exception e) {
            logger.error("Failed to list templates", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
