package sakhno.springframework.ms_store_inventory_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import sakhno.springframework.ms_store_inventory_service.model.InventoryEntity;
import sakhno.springframework.ms_store_inventory_service.repository.InventoryRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class MsStoreInventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsStoreInventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository){
        return args -> {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setSkuCode("iphone_13");
            inventory.setQuantity(100);

            InventoryEntity inventory1 = new InventoryEntity();
            inventory.setSkuCode("iphone_13_red");
            inventory.setQuantity(0);
            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
        };
    }

}
