package com.sparos.uniquone.msachatservice.outband.post.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String postDsc;
    private Long postPrice;
    private String postType;
    private Boolean isOffer;
    private String postImg;
    private String cornImg;

}
