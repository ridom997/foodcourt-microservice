package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderMysqlAdapter implements IOrderPersistencePort {

    private final IOrderEntityRepository orderEntityRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order saveOrder(Order order) {
        return orderEntityMapper.mapToOrder(orderEntityRepository.save(orderEntityMapper.mapToEntity(order)));
    }

    @Override
    public Boolean existOrderOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant) {
        Integer numberOfOrdersOfClientWithDifferentStatus = orderEntityRepository.findNumberOfOrdersOfClientWithDifferentStatus(idClient, status, idRestaurant);
        return numberOfOrdersOfClientWithDifferentStatus > 0;
    }
}
