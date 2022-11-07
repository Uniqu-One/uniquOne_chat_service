package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    FAIL_CODE("F001","OK"),

    NO_SUCH_ELEMENT_EXCEPTION("F002","데이터를 찾을 수 없습니다."),

    DON_T_HAVE_ACCESS("F011","접근 권한이 없습니다."),

    INVALID_TOKEN("F006" ,"올바르지 않은 토큰 입니다."),
    Expired_TOKEN("F007", "토큰 유효시간이 지났습니다."),
    UNSUPPORTED_TOKEN("F008", "지원하지 않는 토큰 입니다."),
    EMPTY_PAYLOAD_TOKEN("F009", "토큰 내용이 비어 있습니다.");

    private String code;
    private String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message =message;
    }
}
