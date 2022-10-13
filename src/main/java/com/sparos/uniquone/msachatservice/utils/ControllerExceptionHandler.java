/*
package com.sparos.uniquone.msachatservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionResponse response = ExceptionResponse.of(ExceptionCode.FAIL_CODE);
        response.setData(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
*/
