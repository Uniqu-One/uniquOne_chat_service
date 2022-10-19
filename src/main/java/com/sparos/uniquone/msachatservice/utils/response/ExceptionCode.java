package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    FAIL_CODE("F001","OK"),

    NO_SUCH_ELEMENT_EXCEPTION("F002","데이터를 찾을 수 없습니다."),

    DON_T_HAVE_ACCESS("F011","접근 권한이 없습니다.");


    private String code;
    private String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message =message;
    }
}
