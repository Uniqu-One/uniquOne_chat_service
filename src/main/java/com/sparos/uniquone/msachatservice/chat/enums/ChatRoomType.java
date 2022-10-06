package com.sparos.uniquone.msachatservice.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatRoomType {

    SELLER("판매"),
    BUYER("구매"),
    NORMAL("일반");

    String type;
}
