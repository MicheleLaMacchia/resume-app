package com.mlm.resume_app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    public List<String> listTemplateNames() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:CVTemplates/*.html");
            List<String> templates = new ArrayList<>();
            for (Resource r : resources) {
                String filename = r.getFilename();
                if (filename != null && filename.endsWith(".html")) {
                    templates.add(filename.substring(0, filename.length() - 5));
                }
            }
            return templates;
        } catch (Exception e) {
            logger.error("Failed to list templates", e);
            return List.of();
        }
    }
}