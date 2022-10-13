package com.sparos.uniquone.msachatservice.outband.post.service;

import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "uniquone-post-service")
public interface IPostConnect {

    @GetMapping("/chat/postInfo/{postId}/{otherUserId}")
    PostResponseDto getPostInfo(@PathVariable("postId") Long postId, @PathVariable("otherUserId") Long otherUserId);

    @GetMapping("/chat/existPost/{postId}/{userId}")
    Boolean getExistPost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId);

}
