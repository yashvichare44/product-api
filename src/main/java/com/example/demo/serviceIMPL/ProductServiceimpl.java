package com.example.demo.serviceIMPL;

import java.util.List;
import com.example.demo.domain.entities.Product;

public interface ProductServiceimpl {
    void deleteProduct(Long id);
    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
}
