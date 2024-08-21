package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.OrderDetailDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.Order;
import com.locboy.locboybackend.models.OrderDetail;
import com.locboy.locboybackend.models.Product;
import com.locboy.locboybackend.repositories.OrderDetailRepository;
import com.locboy.locboybackend.repositories.OrderRepository;
import com.locboy.locboybackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {

        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Can not found order with Id: " + orderDetailDTO.getOrderId()));


        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() ->
                        new DataNotFoundException("Can not found product with Id: " + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetail with id: "+id));
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long id) {
        return orderDetailRepository.findByOrderId(id);
    }
}
