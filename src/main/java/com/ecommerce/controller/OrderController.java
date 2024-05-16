package com.ecommerce.controller;

import com.ecommerce.model.Orders;
import com.ecommerce.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "Admin/orders";
    }

    @GetMapping("/{orderId}/edit-status")
    public String editOrderStatus(@PathVariable Long orderId, Model model) {
        Orders order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "Admin/edit-order";
    }

    @PostMapping("/{orderId}/update-status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam("newStatus") String newStatus) {
        Orders order = orderService.findById(orderId);
        order.setStatus(newStatus);
        orderService.updateStatus(order);
        return "redirect:/orders";
    }
}
