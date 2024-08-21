package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.OrderDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.Order;

import java.util.List;

public interface IOrderService {
    Order getOrder(Long id);

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<Order> findByUserId(Long userId);
}
