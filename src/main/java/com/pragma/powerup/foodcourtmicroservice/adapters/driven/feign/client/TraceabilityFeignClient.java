package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability-microservice", url = "localhost:8092/log")
public interface TraceabilityFeignClient {
    @PostMapping
    void saveLogOrder(@RequestBody OrderLogDto orderLogDto);
}
