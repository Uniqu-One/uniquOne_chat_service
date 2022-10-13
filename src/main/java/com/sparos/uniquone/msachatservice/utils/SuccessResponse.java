package com.sparos.uniquone.msachatservice.utils;

import lombok.Getter;

@Getter
public class SuccessResponse <T>{
    private String code;
    private String message;
    private String result;
    private T data;

    public SuccessResponse(SuccessCode code, String result, T data){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
        this.data = data;
    }

    public SuccessResponse(SuccessCode code,T data){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = null;
        this.data = data;
    }

    public SuccessResponse(SuccessCode code, String result){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
        this.data = null;
    }

    private SuccessResponse(SuccessCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = null;
        this.data = null;
    }

    public static<T> SuccessResponse of(SuccessCode code,T data){
        return new SuccessResponse(code, data);
    }

    public static<T> SuccessResponse of(SuccessCode code,String result, T data){
        return new SuccessResponse(code, result, data);
    }

    public static<T> SuccessResponse of(SuccessCode code,String result){
        return new SuccessResponse(code, result);
    }

    public static SuccessResponse of(SuccessCode code){
        return new SuccessResponse(code);
    }
}
