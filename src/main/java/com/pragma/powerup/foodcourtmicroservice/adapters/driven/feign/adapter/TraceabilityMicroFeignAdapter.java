package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client.TraceabilityFeignClient;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.BadRequestFeignException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.FailConnectionToExternalMicroserviceException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.UnauthorizedFeignException;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITraceabilityCommunicationPort;
import feign.FeignException;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TraceabilityMicroFeignAdapter implements ITraceabilityCommunicationPort {

    private TraceabilityFeignClient traceabilityFeignClient;
    @Override
    public void saveOrderLog(OrderLogDto orderLogDto) {
        try {
            traceabilityFeignClient.saveLogOrder(orderLogDto);
        } catch (FeignException.Unauthorized e) {
            throw new UnauthorizedFeignException("Unauthorized response from traceability microservice");
        } catch (FeignException.BadRequest e) {
            throw new BadRequestFeignException();
        }catch (RetryableException e){
            throw new FailConnectionToExternalMicroserviceException();
        }
    }
}
