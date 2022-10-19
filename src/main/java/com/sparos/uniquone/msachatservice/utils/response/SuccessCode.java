package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public enum SuccessCode {

    SUCCESS_CODE("S001","OK");


    private String code;
    private String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message =message;
    }
}
