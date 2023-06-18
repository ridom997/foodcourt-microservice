package com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.client.TraceabilityFeignClient;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.BadRequestFeignException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.FailConnectionToExternalMicroserviceException;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.feign.exceptions.UnauthorizedFeignException;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.TraceabilityOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.NoDataFoundException;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITraceabilityCommunicationPort;
import feign.FeignException;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
        } catch (RetryableException e) {
            throw new FailConnectionToExternalMicroserviceException();
        }
    }

    @Override
    public List<TraceabilityOrderDto> getTraceabilityListOfOrder(Long idOrder){
        try {
            return traceabilityFeignClient.getTraceabilityListOfOrder(idOrder);
        } catch (FeignException.Unauthorized e) {
            throw new UnauthorizedFeignException("Unauthorized response from traceability microservice");
        } catch (FeignException.BadRequest e) {
            throw new BadRequestFeignException();
        } catch (FeignException.NotFound e) {
            throw new NoDataFoundException("No logs found of the given order");
        } catch (RetryableException e) {
            throw new FailConnectionToExternalMicroserviceException();
        }
    }

}
