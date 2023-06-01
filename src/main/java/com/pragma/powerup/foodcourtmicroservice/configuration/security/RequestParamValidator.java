package com.pragma.powerup.foodcourtmicroservice.configuration.security;

import com.pragma.powerup.foodcourtmicroservice.configuration.security.exception.InvalidRequestParamException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class RequestParamValidator {

    private RequestParamValidator(){}
    private static final List<String> ALLOWED_PARAMS = Arrays.asList("size", "page");
    public static void validate(HttpServletRequest request) {
        //get list of request params.
        Enumeration<String> requestParamNames = request.getParameterNames();
        ArrayList<String> requestParamNamesList = Collections.list(requestParamNames);

        //verify if any unsupported request param
        boolean existAnyUnsupportedRequestParam = requestParamNamesList.stream().anyMatch(item -> !ALLOWED_PARAMS.contains(item));
        if(existAnyUnsupportedRequestParam)
            throw new InvalidRequestParamException();
    }
}
