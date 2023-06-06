package com.pragma.powerup.foodcourtmicroservice.domain.usecase;

import com.pragma.powerup.foodcourtmicroservice.domain.api.IOrderDishServicePort;
import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;
import com.pragma.powerup.foodcourtmicroservice.domain.spi.IOrderDishPersistencePort;

import java.util.List;

public class OrderDishUseCase implements IOrderDishServicePort {

    private final IOrderDishPersistencePort orderDishPersistencePort;

    public OrderDishUseCase(IOrderDishPersistencePort orderDishPersistencePort) {
        this.orderDishPersistencePort = orderDishPersistencePort;
    }

    @Override
    public List<OrderDish> saveListOrderDish(Order order, List<DishAndAmountDto> dishListWithAmount) {
        List<OrderDish> orderDishList = dishListWithAmount.stream()
                .map(dishWithAmount -> new OrderDish(order,dishWithAmount.getDish(),dishWithAmount.getAmount()))
                .toList();
        return orderDishPersistencePort.saveAll(orderDishList);
    }
}
