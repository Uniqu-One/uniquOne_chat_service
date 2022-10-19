package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Getter;

@Getter
public class SuccessResponse <T>{
    private String code;
    private String message;
    private T data;

    public SuccessResponse(SuccessCode code, T data){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

    public SuccessResponse(SuccessCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = null;
    }

    public static<T> SuccessResponse of(SuccessCode code,T data){
        return new SuccessResponse(code, data);
    }

    public static SuccessResponse of(SuccessCode code){
        return new SuccessResponse(code);
    }
}
