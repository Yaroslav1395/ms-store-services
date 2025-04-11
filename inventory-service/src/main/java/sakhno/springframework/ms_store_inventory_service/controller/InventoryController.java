package sakhno.springframework.ms_store_inventory_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sakhno.springframework.ms_store_inventory_service.dto.InventoryResponse;
import sakhno.springframework.ms_store_inventory_service.service.InventoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final InventoryServiceImpl inventoryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("sku_code") List<String> skuCode) {
        log.info("Checking inventory");
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
