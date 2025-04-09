package sakhno.springframework.ms_store_product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakhno.springframework.ms_store_product_service.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
