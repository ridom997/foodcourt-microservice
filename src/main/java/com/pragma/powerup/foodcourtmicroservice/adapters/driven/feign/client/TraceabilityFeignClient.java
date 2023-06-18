package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.TraceabilityOrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "traceability-microservice", url = "localhost:8092")
public interface TraceabilityFeignClient {
    @PostMapping("/log")
    void saveLogOrder(@RequestBody OrderLogDto orderLogDto);

    @GetMapping("/log/order/{idOrder}")
    List<TraceabilityOrderDto> getTraceabilityListOfOrder(@PathVariable("idOrder") Long idOrder);
}
