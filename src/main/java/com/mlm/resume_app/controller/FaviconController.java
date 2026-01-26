package com.mlm.resume_app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    private static final Logger logger = LoggerFactory.getLogger(FaviconController.class);

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        logger.debug("Favicon requested - returning 204 No Content");
        return ResponseEntity.noContent().build();
    }
}
