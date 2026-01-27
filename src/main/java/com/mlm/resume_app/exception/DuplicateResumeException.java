package com.mlm.resume_app.exception;

public class DuplicateResumeException extends RuntimeException {
    public DuplicateResumeException(String message) {
        super(message);
    }
}
