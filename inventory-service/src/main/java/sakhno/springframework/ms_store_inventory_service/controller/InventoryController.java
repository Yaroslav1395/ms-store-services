package sakhno.springframework.ms_store_inventory_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sakhno.springframework.ms_store_inventory_service.dto.InventoryResponse;
import sakhno.springframework.ms_store_inventory_service.service.InventoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryServiceImpl inventoryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("sku_code") List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
