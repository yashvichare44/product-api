package com.example.demo.service;

import com.example.demo.domain.dtos.ProductDto;
import com.example.demo.domain.entities.Product;
import com.example.demo.domain.entities.User;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<List<User>> getAllUsers() {
        WebClient webClient = webClientBuilder.build();

        return webClient.get()
                .uri("http://USER-SERVICE/users/all") // Replace with your service name and endpoint
                .retrieve()
                .bodyToFlux(User.class)
                .collectList();
    }

    public ProductDto saveProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        product = productRepository.save(product);
        return convertToDto(product);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(this::convertToDto).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Helper method to convert ProductDto to Product entity
    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product); // Copy properties from dto to entity
        return product;
    }

    // Helper method to convert Product entity to ProductDto
    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto); // Copy properties from entity to dto
        return productDto;
    }
}

