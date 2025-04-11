package sakhno.springframework.ms_store_product_service.controller;

/*import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;*/
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sakhno.springframework.ms_store_product_service.dto.OrderRequestDto;
import sakhno.springframework.ms_store_product_service.service.OrderServiceImpl;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@CircuitBreaker(name = "MS-STORE-INVENTORY-SERVICE", fallbackMethod = "fallbackMethod")
    //@TimeLimiter(name = "MS-STORE-INVENTORY-SERVICE")
    //@Retry(name = "MS-STORE-INVENTORY-SERVICE")
    public String placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("Placing order");
        return orderService.placeOrder(orderRequestDto);
    }

    public String fallbackMethod(OrderRequestDto orderRequestDto, RuntimeException ex) {
        return "Oops! Something went wrong. Please try again later";
    }
}
