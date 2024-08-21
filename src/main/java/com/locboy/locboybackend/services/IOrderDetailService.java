package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.OrderDetailDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDetail(Long id ) throws DataNotFoundException;

    void deleteOrderDetail(Long id );

    List<OrderDetail> findByOrderId(Long id);
}
