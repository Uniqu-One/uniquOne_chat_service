package com.sparos.uniquone.msachatservice.outband.post.dto;

import com.sparos.uniquone.msachatservice.utils.enums.PostType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String postDsc;
    private Long postPrice;
    private PostType postType;
    private Boolean isOffer;
    private String postImg;
    private String cornImg;

}
