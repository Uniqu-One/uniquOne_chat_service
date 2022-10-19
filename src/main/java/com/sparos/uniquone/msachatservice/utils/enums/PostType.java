package com.sparos.uniquone.msachatservice.utils.enums;

public enum PostType {
    SALE("판매중"),
    SOLD_OUT("판매완료"),
    DISCONTINUED("판매중단"),
    SHARE("나눔"),
    STYLE("스타일");

    String postType;

    PostType(String postType){
        this.postType =postType;
    }

    public String value(){
        return postType;
    }
}
