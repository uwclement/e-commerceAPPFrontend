package com.ecommerce.service;

import com.ecommerce.model.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> findAll();
    Orders save(Orders order);
    Orders updateStatus(Orders order);
    void deleteById(Long id);
    Orders findById(Long orderId);
    List<Orders> getOrdersByEmail(String email);

}
