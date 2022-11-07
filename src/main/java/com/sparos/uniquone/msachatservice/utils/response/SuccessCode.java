package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public enum SuccessCode {

    SUCCESS_CODE("S001","OK"),
    SUCCESS_NOT_TOKEN_CODE("S002","OK BUT NO TOKEN");

    private String code;
    private String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message =message;
    }
}
