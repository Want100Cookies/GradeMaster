package com.datbois.grademaster.configuration;

import com.datbois.grademaster.exception.BadRequestException;
import com.datbois.grademaster.exception.ForbiddenException;
import com.datbois.grademaster.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class, BadRequestException.class, ForbiddenException.class})
    protected ResponseEntity<Object> handleException(HttpStatusCodeException exception, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", exception.getMessage());
        response.put("code", exception.getRawStatusCode());

        return handleExceptionInternal(
                exception,
                response,
                new HttpHeaders(),
                exception.getStatusCode(),
                request
        );
    }
}
