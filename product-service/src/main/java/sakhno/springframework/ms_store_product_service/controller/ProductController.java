package sakhno.springframework.ms_store_product_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sakhno.springframework.ms_store_product_service.dto.ProductRequest;
import sakhno.springframework.ms_store_product_service.dto.ProductResponse;
import sakhno.springframework.ms_store_product_service.service.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productService.getAllProducts();
    }
}
