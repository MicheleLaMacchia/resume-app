package com.mlm.resume_app.service;

import com.mlm.resume_app.exception.PdfGenerationException;
import com.mlm.resume_app.model.ResumeModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGenerationService.class);

    private final TemplateEngine templateEngine;

    public PdfGenerationService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdfFromResume(ResumeModels resume) {
        // backward-compatible: delegate to template-based method with default template
        return generatePdfFromResume(resume, "resume-template-test");
    }

    public byte[] generatePdfFromResume(ResumeModels resume, String templateId) {
        try {
            if (resume == null) {
                throw new PdfGenerationException("Resume is null");
            }
            // normalize and treat missing/invalid template ids as not found
            if (templateId == null) {
                throw new com.mlm.resume_app.exception.ResourceNotFoundException("Template non trovato: null");
            }
            templateId = templateId.trim();
            if (templateId.isEmpty()) {
                throw new com.mlm.resume_app.exception.ResourceNotFoundException("Template non trovato: ");
            }

            // basic validation to avoid path traversal - invalid ids are considered not found
            if (!templateId.matches("[A-Za-z0-9_-]+")) {
                throw new com.mlm.resume_app.exception.ResourceNotFoundException("Template non trovato: " + templateId);
            }

            // ensure template exists in classpath under CVTemplates
            org.springframework.core.io.support.PathMatchingResourcePatternResolver resolver = new org.springframework.core.io.support.PathMatchingResourcePatternResolver();
            org.springframework.core.io.Resource resource = resolver.getResource("classpath:CVTemplates/" + templateId + ".html");
            if (resource == null || !resource.exists()) {
                // map missing template to resource not found so global handler returns 404
                throw new com.mlm.resume_app.exception.ResourceNotFoundException("Template non trovato: " + templateId);
            }

            Context context = new Context();
            context.setVariable("resume", resume);
            // compute footerText and expose to template to avoid complex expressions in Thymeleaf
            String footerText;
            try {
                String tmp = null;
                if (resume.createdAt() != null) {
                    java.time.Instant instant = java.time.Instant.ofEpochSecond(resume.createdAt());
                    java.time.ZoneId zone = java.time.ZoneId.systemDefault();
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm").withZone(zone);
                    tmp = "versione CV del: " + fmt.format(instant);
                }
                footerText = (tmp != null) ? tmp : "versione CV del: N/D";
            } catch (Exception e) {
                logger.warn("Failed to format createdAt for resume: {}", (resume.datiGenerali() != null ? resume.datiGenerali().codiceFiscale() : "unknown"), e);
                footerText = "versione CV del: N/D";
            }
            context.setVariable("footerText", footerText);

            String html = templateEngine.process(templateId, context);

            if (html == null || html.trim().isEmpty()) {
                throw new PdfGenerationException("Generated HTML is empty for resume: " + (resume.datiGenerali() != null ? resume.datiGenerali().codiceFiscale() : "unknown"));
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(html);
                renderer.layout();
                renderer.createPDF(outputStream);
                return outputStream.toByteArray();
            }
        } catch (PdfGenerationException pex) {
            // rethrow custom exceptions as is
            logger.error("PdfGenerationException: {}", pex.getMessage(), pex);
            throw pex;
        } catch (com.mlm.resume_app.exception.ResourceNotFoundException rnfe) {
            // let global handler map this to 404
            logger.warn("Resource not found while generating PDF: {}", rnfe.getMessage());
            throw rnfe;
        } catch (Exception e) {
            logger.error("Unexpected error while generating PDF", e);
            throw new PdfGenerationException("Unexpected error while generating PDF: " + e.getMessage(), e);
        }
    }
}
