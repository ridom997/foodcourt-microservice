package com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.adapter;

import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.OrderEntity;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers.IOrderEntityMapper;
import com.pragma.powerup.foodcourtmicroservice.adapters.driven.jpa.mysql.repositories.IOrderEntityRepository;
import com.pragma.powerup.foodcourtmicroservice.domain.adapter.ExternalCommunicationDomainAdapter;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.OrderLogDto;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.response.OrderDurationInfoDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class OrderMysqlAdapter implements IOrderPersistencePort {

    private final IOrderEntityRepository orderEntityRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final ExternalCommunicationDomainAdapter externalCommunicationDomainAdapter;

    @Override
    public Order saveOrder(Order order) {
        return orderEntityMapper.mapToOrder(orderEntityRepository.save(orderEntityMapper.mapToEntity(order)));
    }

    @Override
    @Transactional
    public Order saveOrderAndTraceability(Order order, OrderLogDto orderLogDto) {
        Order savedOrder = saveOrder(order);
        externalCommunicationDomainAdapter.saveOrderLog(orderLogDto);
        return savedOrder;
    }


    @Override
    public Order findById(Long idOrder) {
        Optional<OrderEntity> orderEntity = orderEntityRepository.findById(idOrder);
        if(orderEntity.isPresent())
            return orderEntityMapper.mapToOrder(orderEntity.get());
        return null;
    }

    @Override
    public Boolean existOrderOfClientWithDifferentStatus(Long idClient, Integer status, Long idRestaurant) {
        Integer numberOfOrdersOfClientWithDifferentStatus = orderEntityRepository.findNumberOfOrdersOfClientWithDifferentStatus(idClient, status, idRestaurant);
        return numberOfOrdersOfClientWithDifferentStatus > 0;
    }

    @Override
    public List<Order> getOrdersByRestaurantAndStatus(Integer page, Integer sizePage, Long idRestaurant, Integer status) {
        Pageable pageable = PageRequest.of(page, sizePage);
        Page<OrderEntity> ordersByIdRestaurantAndStatus = orderEntityRepository.getOrdersByIdRestaurantAndStatus(idRestaurant, status, pageable);
        return ordersByIdRestaurantAndStatus.stream()
                .map(orderEntityMapper::mapToOrder)
                .toList();
    }

    @Override
    public List<OrderDurationInfoDto> findAllPagedCompletedOrdersByIdRestaurant(Long idRestaurant, Integer page, Integer sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        Page<OrderEntity> completedOrdersByIdRestaurant = orderEntityRepository.getCompletedOrdersByIdRestaurant(idRestaurant, pageable);
        return completedOrdersByIdRestaurant.stream()
                .map(order -> new OrderDurationInfoDto(order.getId(),order.getDate(),order.getDateFinished(),order.getStatus()))
                .toList();
    }
}
