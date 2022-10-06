package com.sparos.uniquone.msachatservice.outband.post.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String postImg;
    private String cornImg;

}
