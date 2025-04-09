package sakhno.springframework.ms_store_inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakhno.springframework.ms_store_inventory_service.model.InventoryEntity;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findBySkuCode(String skuCode);

    List<InventoryEntity> findBySkuCodeIn(List<String> skuCode);
}
