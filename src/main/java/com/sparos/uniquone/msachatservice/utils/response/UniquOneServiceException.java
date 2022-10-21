package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UniquOneServiceException extends RuntimeException {

    private ExceptionCode exceptionCode;
    private String data;
    private HttpStatus status;

    public UniquOneServiceException(ExceptionCode code, HttpStatus status) {
        this.exceptionCode = code;
        this.status = status;
        this.data = null;
    }
}
