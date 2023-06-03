package com.pragma.powerup.foodcourtmicroservice.domain.validations;

import com.pragma.powerup.foodcourtmicroservice.domain.exceptions.FailValidatingRequiredVariableException;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.PAGE_NOT_VALID_MESSAGE;
import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.SIZE_PAGE_NOT_VALID_MESSAGE;

public class PaginationValidations {

    private PaginationValidations() {
    }

    public static void validatePageAndSizePage(Integer page, Integer sizePage){
        if(page == null || page < 0)
            throw new FailValidatingRequiredVariableException(PAGE_NOT_VALID_MESSAGE);
        if(sizePage == null || sizePage <= 0)
            throw new FailValidatingRequiredVariableException(SIZE_PAGE_NOT_VALID_MESSAGE);
    }
}
