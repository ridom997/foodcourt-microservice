package com.pragma.powerup.foodcourtmicroservice.domain.utils;

import java.util.UUID;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.*;
import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.DELIVERED_ORDER_STATUS_VALUE;

public class OrderUtils {

    private OrderUtils(){}

    public static String mapOrderStatusToString(Integer integerStatus){
        switch (integerStatus){
            case 1:
                return PENDING_ORDER_STATUS_VALUE;
            case 2:
                return IN_PROGRESS_ORDER_STATUS_VALUE;
            case 3:
                return READY_ORDER_STATUS_VALUE;
            case 4:
                return DELIVERED_ORDER_STATUS_VALUE;
            default:
                return "NO DEFINED";
        }
    }

    public static String generateDeliveryPin(){
        return UUID.randomUUID().toString().substring(0,4);
    }
}
