package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.adapter.ExternalCommunicationDomainAdapter;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderActorsDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.UserBasicInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IUserValidationComunicationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationUseCaseTest {

    @Mock
    private ExternalCommunicationDomainAdapter mockUserValidationComunicationPort;

    @InjectMocks
    private UserValidationUseCase userValidationUseCaseUnderTest;

    @Test
    void findClientAndEmployeeInfoTest_successfully() {
        Long idClient = 1L;
        Long idEmployee = 2L;
        List<UserBasicInfoDto> userBasicInfoDtos = List.of(new UserBasicInfoDto(idClient, "name", "phone", "mail"),
                new UserBasicInfoDto(idEmployee, "name", "phone", "mail"));
        when(mockUserValidationComunicationPort.getBasicInfoOfUsers(List.of(idClient,idEmployee))).thenReturn(userBasicInfoDtos);

        final OrderActorsDto result = userValidationUseCaseUnderTest.findClientAndEmployeeInfo(idClient, idEmployee);

        assertEquals(idClient,result.getClient().getId());
        assertEquals(idEmployee,result.getEmployee().getId());
        verify(mockUserValidationComunicationPort,times(1)).getBasicInfoOfUsers(anyList());
    }

    @Test
    void userValidationUseCaseUnderTest_returnTrue() {
        when(mockUserValidationComunicationPort.existsRelationWithUserAndIdRestaurant(0L)).thenReturn(true);

        final Boolean result = userValidationUseCaseUnderTest.existsRelationWithUserAndIdRestaurant(0L);

        assertThat(result).isTrue();
    }

    @Test
    void userValidationUseCaseUnderTest_returnFalse() {
        when(mockUserValidationComunicationPort.existsRelationWithUserAndIdRestaurant(0L)).thenReturn(false);

        final Boolean result = userValidationUseCaseUnderTest.existsRelationWithUserAndIdRestaurant(0L);

        assertThat(result).isFalse();
    }

    @Test
    void findClientInfoTest_successfully(){
        Long idClient = 1L;
        List<UserBasicInfoDto> userBasicInfoDtos = List.of(new UserBasicInfoDto(idClient, "name", "phone", "mail"));
        when(mockUserValidationComunicationPort.getBasicInfoOfUsers(List.of(idClient))).thenReturn(userBasicInfoDtos);

        UserBasicInfoDto clientInfo = userValidationUseCaseUnderTest.findClientInfo(idClient);

        assertEquals(idClient,clientInfo.getId());
        verify(mockUserValidationComunicationPort,times(1)).getBasicInfoOfUsers(anyList());
    }
}
