package sakhno.springframework.ms_store_product_service.service;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sakhno.springframework.ms_store_product_service.dto.InventoryResponse;
import sakhno.springframework.ms_store_product_service.dto.OrderLineItemDto;
import sakhno.springframework.ms_store_product_service.dto.OrderRequestDto;
import sakhno.springframework.ms_store_product_service.event.OrderPlacedEvent;
import sakhno.springframework.ms_store_product_service.model.OrderEntity;
import sakhno.springframework.ms_store_product_service.model.OrderLineItemEntity;
import sakhno.springframework.ms_store_product_service.repository.OrderRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public CompletableFuture<String> placeOrder(OrderRequestDto orderRequestDto) {
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

        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        String spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
        List<InventoryResponse> result = webClientBuilder.build()
                .get()
                .uri("http://MS-STORE-INVENTORY-SERVICE/api/v1/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("sku_code", skuCodes)
                                .build())
                .header("X-B3-TraceId", traceId)
                .header("X-B3-SpanId", spanId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<InventoryResponse>>() {})
                .block();
        boolean allProductsInStock = result.stream().allMatch(InventoryResponse::isInStock);
        if(Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.save(orderEntity);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(orderEntity.getOrderNumber()));
            return CompletableFuture.completedFuture("Order Placed Successfully");
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
