package com.locboy.locboybackend.services;

import com.locboy.locboybackend.dtos.OrderDTO;
import com.locboy.locboybackend.exceptions.DataNotFoundException;
import com.locboy.locboybackend.models.Order;
import com.locboy.locboybackend.models.OrderStatus;
import com.locboy.locboybackend.models.User;
import com.locboy.locboybackend.repositories.OrderRepository;
import com.locboy.locboybackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can't not found User with Id:" + orderDTO.getUserId()));
        modelMapper.getTypeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : order.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new Exception("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        return order;
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Can not found order with Id:" + id));

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Can not found User with Id: " +orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper ->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        orderRepository.save(order);
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
       Order order= orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return  orderRepository.findByUserId(userId);
    }
}
