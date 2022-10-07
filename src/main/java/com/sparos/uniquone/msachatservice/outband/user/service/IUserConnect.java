package com.sparos.uniquone.msachatservice.outband.user.service;

import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "uniquone-user-service")
public interface IUserConnect {

    @GetMapping("/chat/userInfo/{userId}")
    UserResponseDto getUserInfo(@PathVariable("userId") Long userId);

}
