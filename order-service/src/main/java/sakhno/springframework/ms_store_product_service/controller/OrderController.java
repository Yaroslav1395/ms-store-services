package sakhno.springframework.ms_store_product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sakhno.springframework.ms_store_product_service.dto.OrderRequestDto;
import sakhno.springframework.ms_store_product_service.service.OrderServiceImpl;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.placeOrder(orderRequestDto);
        return "Order Placed Successfully";
    }
}
