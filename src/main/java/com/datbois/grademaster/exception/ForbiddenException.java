package com.datbois.grademaster.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ForbiddenException extends HttpStatusCodeException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String statusText) {
        super(HttpStatus.FORBIDDEN, statusText);
    }
}
