package com.pragma.powerup.foodcourtmicroservice.domain.mappers;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Restaurant;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.pragma.powerup.foodcourtmicroservice.configuration.Constants.PENDING_ORDER_STATUS_INT_VALUE;

public class OrderMapper {

    private OrderMapper(){}
    public static Order mapToPendingOrder(Restaurant restaurant, Long idClient){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setStatus(PENDING_ORDER_STATUS_INT_VALUE);
        order.setDate(currentDateTime);
        order.setIdClient(idClient);
        order.setIdChef(0L); //Id of chef in future releases
        return order;
    }

    public static OrderLogDto mapToOrderLogDto(Order order, Long idEmployee, Integer previousStatus){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("UTC-5"));
        OrderLogDto orderLogDto = new OrderLogDto();
        orderLogDto.setIdOrder(order.getId());
        orderLogDto.setIdClient(order.getIdClient());
        orderLogDto.setMailClient("client@correo.com");
        orderLogDto.setDate(currentDateTime);
        orderLogDto.setPreviousStatus(previousStatus);
        orderLogDto.setNewStatus(order.getStatus());
        orderLogDto.setIdEmployee(idEmployee);
        orderLogDto.setMailEmployee("employee@correo.com");
        return orderLogDto;
    }
}
