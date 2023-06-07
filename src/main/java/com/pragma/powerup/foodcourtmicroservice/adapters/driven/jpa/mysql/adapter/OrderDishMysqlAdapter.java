package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IOrderDishEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderDishEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderDishPersistencePort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OrderDishMysqlAdapter implements IOrderDishPersistencePort {

    private final IOrderDishEntityRepository orderDishEntityRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public List<OrderDish> saveAll(List<OrderDish> listOrderDish) {
        return orderDishEntityRepository.saveAll(
                listOrderDish.stream()
                .map(orderDishEntityMapper::mapToEntity).toList()
        ).stream()
                .map(orderDishEntityMapper::toOrderDish).toList();
    }

    @Override
    public List<OrderDish> getAllByOrder(Order order) {
        return orderDishEntityRepository.findAllByIdOrder(orderEntityMapper.mapToEntity(order))
                .stream()
                .map(orderDishEntityMapper::toOrderDish).toList();
    }
}
