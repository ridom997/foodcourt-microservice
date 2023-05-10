package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request.UserAndRoleRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "user-microservice", url = "localhost:8090/user")
public interface UserFeignClient {

    @PostMapping("/validateRole")
    Map<String,Boolean> userHasRole(@RequestBody UserAndRoleRequestDto userAndRoleRequestDto);
}
