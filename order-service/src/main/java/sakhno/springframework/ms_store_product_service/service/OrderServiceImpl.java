package sakhno.springframework.ms_store_product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sakhno.springframework.ms_store_product_service.dto.InventoryResponse;
import sakhno.springframework.ms_store_product_service.dto.OrderLineItemDto;
import sakhno.springframework.ms_store_product_service.dto.OrderRequestDto;
import sakhno.springframework.ms_store_product_service.model.OrderEntity;
import sakhno.springframework.ms_store_product_service.model.OrderLineItemEntity;
import sakhno.springframework.ms_store_product_service.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    @Transactional
    public String placeOrder(OrderRequestDto orderRequestDto) {
        log.info("Place Order");
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItemEntity> orderLineItemsDto = orderRequestDto.getOrderLineItems().stream()
                .map(this::mapToOrderLineItem)
                .toList();
        orderEntity.setOrderLineItems(orderLineItemsDto);

        List<String> skuCodes = orderEntity.getOrderLineItems().stream()
                .map(OrderLineItemEntity::getSkuCode)
                .toList();


        List<InventoryResponse> result = webClientBuilder.build()
                .get()
                .uri("http://MS-STORE-INVENTORY-SERVICE/api/v1/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("sku_code", skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<InventoryResponse>>() {})
                .block();
        boolean allProductsInStock = result.stream().allMatch(InventoryResponse::isInStock);
        if(Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.save(orderEntity);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItemEntity mapToOrderLineItem(OrderLineItemDto orderLineItemDto) {
        OrderLineItemEntity orderLineItemEntity = new OrderLineItemEntity();
        orderLineItemEntity.setPrice(orderLineItemDto.getPrice());
        orderLineItemEntity.setQuantity(orderLineItemDto.getQuantity());
        orderLineItemEntity.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItemEntity;
    }
}
