package com.sparos.uniquone.msachatservice.utils.response;

import lombok.Setter;

@Setter
public class ExceptionResponse<T>{

    private String code;
    private String message;
    private String result;
    private T data;

    public ExceptionResponse(ExceptionCode code, String result, T data){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
        this.data = data;
    }

    public ExceptionResponse(ExceptionCode code,T data){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = null;
        this.data = data;
    }

    public ExceptionResponse(ExceptionCode code, String result){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = result;
        this.data = null;
    }

    private ExceptionResponse(ExceptionCode code){
        this.code = code.getCode();
        this.message = code.getMessage();
        this.result = null;
        this.data = null;
    }

    public static<T> ExceptionResponse of(ExceptionCode code,T data){
        return new ExceptionResponse(code, data);
    }

    public static<T> ExceptionResponse of(ExceptionCode code,String result, T data){
        return new ExceptionResponse(code, result, data);
    }

    public static<T> ExceptionResponse of(ExceptionCode code,String result){
        return new ExceptionResponse(code, result);
    }

    public static ExceptionResponse of(ExceptionCode code){
        return new ExceptionResponse(code);
    }
}
