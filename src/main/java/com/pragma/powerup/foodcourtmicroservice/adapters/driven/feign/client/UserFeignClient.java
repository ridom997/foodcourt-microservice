package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.dto.request.UserAndRoleRequestDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-microservice", url = "localhost:8090/user")
public interface UserFeignClient {

    @PostMapping("/validateRole")
    Map<String,Boolean> userHasRole(@RequestBody UserAndRoleRequestDto userAndRoleRequestDto);

    @GetMapping(value = "/validate-restaurant/{id_restaurant}")
    Map<String,Boolean> existsRelationWithUserAndIdRestaurant(@PathVariable("id_restaurant") Long idRestaurant);

    @PostMapping(value = "/get-basic-info")
    List<UserBasicInfoDto> getBasicInfoOfUsers(@RequestBody List<Long> userIdList);

}
