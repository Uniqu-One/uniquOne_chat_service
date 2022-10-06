package com.sparos.uniquone.msachatservice.outband.user.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto {

    private Long userId;
    private String nickname;

}
