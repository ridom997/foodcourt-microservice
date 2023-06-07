package com.pragma.powerup.foodcourtmicroservice.domain.api;

import com.pragma.powerup.foodcourtmicroservice.domain.dto.DishAndAmountDto;
import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;

import java.util.List;

public interface IOrderDishServicePort {
    List<OrderDish> saveListOrderDish(Order order, List<DishAndAmountDto> dishListWithAmount);
    List<OrderDish> getAllOrderDishByOrder(Order order);

}
