package com.sparos.uniquone.msachatservice.utils;

import com.sparos.uniquone.msachatservice.utils.response.ExceptionResponse;
import com.sparos.uniquone.msachatservice.utils.response.UniquOneServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UniquOneServiceException.class)
    public ResponseEntity<?> applicationHandler(UniquOneServiceException e){
        log.error("Error : {}", e.toString());
        return ResponseEntity.status(e.getStatus()).body(ExceptionResponse.of(e.getExceptionCode()));
    }
}
