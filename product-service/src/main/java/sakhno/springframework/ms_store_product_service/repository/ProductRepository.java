package sakhno.springframework.ms_store_product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sakhno.springframework.ms_store_product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
