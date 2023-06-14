package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.adapter.ExternalCommunicationDomainAdapter;
import com.pragma.powerup.foodcourtmicroservice.domain.api.IUserValidationServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderActorsDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;

import java.util.List;

public class UserValidationUseCase implements IUserValidationServicePort {

    private final ExternalCommunicationDomainAdapter externalCommunicationDomainAdapter;

    public UserValidationUseCase(ExternalCommunicationDomainAdapter externalCommunicationDomainAdapter) {
        this.externalCommunicationDomainAdapter = externalCommunicationDomainAdapter;
    }


    @Override
    public OrderActorsDto findClientAndEmployeeInfo(Long idClient, Long idEmployee) {
        List<UserBasicInfoDto> userBasicInfoDtoList = externalCommunicationDomainAdapter.getBasicInfoOfUsers(List.of(idClient, idEmployee));
        OrderActorsDto orderActorsDto = new OrderActorsDto();
        // mapping client info and employee info.
        userBasicInfoDtoList.stream().forEach(user -> {
            if(user.getId().equals(idClient))
                orderActorsDto.setClient(user);
            else
                orderActorsDto.setEmployee(user);
        });
        return orderActorsDto;
    }

    @Override
    public UserBasicInfoDto findClientInfo(Long idClient) {
        List<UserBasicInfoDto> userBasicInfoDtoList = externalCommunicationDomainAdapter.getBasicInfoOfUsers(List.of(idClient));
        return userBasicInfoDtoList.get(0);
    }

    @Override
    public Boolean existsRelationWithUserAndIdRestaurant(Long idRestaurant) { //call user microservice and send the incoming jwtToken in the header.
        return externalCommunicationDomainAdapter.existsRelationWithUserAndIdRestaurant(idRestaurant);
    }
}
