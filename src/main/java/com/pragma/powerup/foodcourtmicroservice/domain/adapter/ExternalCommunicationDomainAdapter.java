package com.pragma.powerup.foodcourtmicroservice.domain.adapter;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.TraceabilityOrderDto;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IMessagingCommunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITraceabilityCommunicationPort;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;

import java.util.List;

public class ExternalCommunicationDomainAdapter implements IMessagingCommunicationPort, IUserValidationComunicationPort, ITraceabilityCommunicationPort {
    private final IMessagingCommunicationPort messagingCommunicationPort;
    private final IUserValidationComunicationPort  userValidationCommunicationPort;
    private final ITraceabilityCommunicationPort traceabilityCommunicationPort;


    public ExternalCommunicationDomainAdapter(IMessagingCommunicationPort messagingCommunicationPort, ITraceabilityCommunicationPort traceabilityCommunicationPort, IUserValidationComunicationPort userValidationComunicationPort) {
        this.messagingCommunicationPort = messagingCommunicationPort;
        this.traceabilityCommunicationPort = traceabilityCommunicationPort;
        this.userValidationCommunicationPort = userValidationComunicationPort;
    }

    @Override
    public boolean sendSms(String phone, String smsBody) {
        return messagingCommunicationPort.sendSms(phone,smsBody);
    }

    @Override
    public Boolean userHasRole(Long idUser, Long idRole) {
        return userValidationCommunicationPort.userHasRole(idUser,idRole);
    }

    @Override
    public Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant) {
        return userValidationCommunicationPort.existsRelationWithUserAndIdRestaurant(idRestaurant);
    }

    @Override
    public List<UserBasicInfoDto> getBasicInfoOfUsers(List<Long> userIdList) {
        return userValidationCommunicationPort.getBasicInfoOfUsers(userIdList);
    }

    @Override
    public void saveOrderLog(OrderLogDto orderLogDto) {
        traceabilityCommunicationPort.saveOrderLog(orderLogDto);
    }

    @Override
    public List<TraceabilityOrderDto> getTraceabilityListOfOrder(Long idOrder) {
        return traceabilityCommunicationPort.getTraceabilityListOfOrder(idOrder);
    }
}
