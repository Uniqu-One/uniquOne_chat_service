package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public class UniquOneServiceException extends RuntimeException {

    private ExceptionCode exceptionCode;
    private String data;

    public UniquOneServiceException(ExceptionCode code) {
        this.exceptionCode = code;
        this.data = null;
    }
}
