package com.sparos.uniquone.msachatservice.utils;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    FAIL_CODE("F001","OK");


    private String code;
    private String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message =message;
    }
}
