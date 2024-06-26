package com.example.recommender.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.recommender.mapper.ProductMapper;
import com.example.recommender.service.ProductService;
import com.example.recommender.service.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;

    @CrossOrigin
    @GetMapping("/{productId}")
    public String getProduct(@PathVariable String productId) {
        JsonObject productInfo = productService.getProduct(productId);
        return productInfo == null? "{}" : productInfo.toString();
    }

    @CrossOrigin
    @GetMapping("/getImage/{productId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String productId) {
        String imagePath = productMapper.getProductImage(productId);
        try {
            Path path = Paths.get(imagePath);
            byte[] imageData = Files.readAllBytes(path);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @GetMapping("/{userId}/{productId}")
    public String getCorrrrelatedProducts(@PathVariable String userId ,@PathVariable String productId) {
        userService.addClick(userId, productId);
        JsonArray relatedProducts = productService.getCorrelatedProducts(productId);
        return relatedProducts == null? "[]" : relatedProducts.toString();
    }
    
}
