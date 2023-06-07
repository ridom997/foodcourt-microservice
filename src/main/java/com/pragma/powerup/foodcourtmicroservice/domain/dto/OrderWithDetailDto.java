package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;
import com.pragma.powerup.foodcourtmicroservice.domain.model.OrderDish;

import java.util.List;

public class OrderWithDetailDto {
    private Order order;
    private List<OrderDish> detail;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDish> getDetail() {
        return detail;
    }

    public void setDetail(List<OrderDish> detail) {
        this.detail = detail;
    }

    public OrderWithDetailDto(Order order, List<OrderDish> detail) {
        this.order = order;
        this.detail = detail;
    }

    public OrderWithDetailDto() {
    }
}
