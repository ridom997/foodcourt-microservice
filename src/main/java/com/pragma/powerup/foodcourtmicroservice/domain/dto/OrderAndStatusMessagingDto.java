package com.pragma.powerup.foodcourtmicroservice.domain.dto;

import com.pragma.powerup.foodcourtmicroservice.domain.model.Order;

public class OrderAndStatusMessagingDto {
    private Order order;
    private Boolean errorSendingSms;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Boolean getErrorSendingSms() {
        return errorSendingSms;
    }

    public void setErrorSendingSms(Boolean errorSendingSms) {
        this.errorSendingSms = errorSendingSms;
    }

    public OrderAndStatusMessagingDto(Order order, Boolean errorSendingSms) {
        this.order = order;
        this.errorSendingSms = errorSendingSms;
    }

    public OrderAndStatusMessagingDto() {
    }
}
