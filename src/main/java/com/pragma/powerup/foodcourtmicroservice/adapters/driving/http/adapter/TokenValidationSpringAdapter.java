package com.pragma.powerup.foodcourtmicroservice.adapters.driving.http.adapter;

import com.pragma.powerup.foodcourtmicroservice.configuration.security.jwt.JwtProvider;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.ITokenValidationPort;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TokenValidationSpringAdapter implements ITokenValidationPort {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationSpringAdapter.class);


    private final JwtProvider jwtProvider;

    @Override
    public Boolean userIsInToken(Long idUser, String token) {
        if (token == null)
            return false;
        Long idUserFromToken =  getIdUserFromToken(token);
        if (idUserFromToken == null)
            return false;
        return idUser.equals(idUserFromToken);
    }

    @Override
    public Long findIdUserFromToken(String token) {
        if (token == null)
            return null;
        return getIdUserFromToken(token);
    }

    private Long getIdUserFromToken(String token){
        Claims claimsFromToken = jwtProvider.getClaimsFromToken(token);
        try{
            return ((Integer) claimsFromToken.get("idUser")).longValue();
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return null;
        }
    }
}
