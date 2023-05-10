package com.pragma.powerup.foodcourtmicroservice.domain.spi;

public interface IUserValidationComunicationPort {
    Boolean userHasRole(Long idUser, Long idRole);
}
