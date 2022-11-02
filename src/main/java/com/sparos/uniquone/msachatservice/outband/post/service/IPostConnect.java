package com.sparos.uniquone.msachatservice.outband.post.service;

import com.sparos.uniquone.msachatservice.outband.post.dto.ChatPushDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "uniquone-post-service")
public interface IPostConnect {

    @GetMapping("/chat/postInfo/{postId}/{otherUserId}")
    PostResponseDto getPostInfo(@PathVariable("postId") Long postId, @PathVariable("otherUserId") Long otherUserId);

    @GetMapping("/chat/existPost/{postId}/{userId}")
    Boolean getExistPost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId);

    @GetMapping("/chat/{postId}")
    Long getUserIdByCorn(@PathVariable("postId") Long postId);

    @PostMapping("/chat/sendPush")
    void chatPush(@RequestBody ChatPushDto chatPushDto);

}
