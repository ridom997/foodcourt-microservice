package com.pragma.powerup.foodcourtmicroservice.configuration.security;

import com.pragma.powerup.foodcourtmicroservice.configuration.security.exception.InvalidRequestParamException;
import com.pragma.powerup.foodcourtmicroservice.configuration.security.exception.NonUniqueRequestParamException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;


public class RequestParamValidator {
    private static final List<String> ALLOWED_PARAMS = Arrays.asList("size", "page","idCategory");
    private RequestParamValidator(){}

    public static void validate(HttpServletRequest request) {
        //get list of request params.
        Enumeration<String> requestParamNames = request.getParameterNames();
        ArrayList<String> requestParamNamesList = Collections.list(requestParamNames);

        //verify if any unsupported request param
        boolean existAnyUnsupportedRequestParam = requestParamNamesList.stream().anyMatch(item -> !ALLOWED_PARAMS.contains(item));
        if(existAnyUnsupportedRequestParam)
            throw new InvalidRequestParamException();

        //verify is there is a non unique param request
        requestParamNamesList.stream().forEach(param -> {
            String[] values = request.getParameterValues(param);
            if (values.length > 1)
                throw new NonUniqueRequestParamException(param + " request param is non unique");
        });
    }
}
