package com.example.recommender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.recommender.mapper.ProductMapper;
import com.example.recommender.pojo.Product;
import com.example.recommender.utils.DataDeal;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public JsonArray getCorrelatedProducts(String productId) {
        if (!DataDeal.productExists(productId))
            return null;
        JsonArray jsonArray = new JsonArray();
        for (String correlatedProductId : DataDeal.getCorrelatedProducts(productId)) {
            JsonObject product = getProduct(correlatedProductId);
            if (product == null)
                continue;
            jsonArray.add(product);
        }
        return jsonArray;
    }

    // public String getJsonArray(String s) {
    //     JsonArray jsonArray = new JsonArray();
    //     for (String productId : s.split(",")) {
    //         if (DataDeal.productExists(productId)) {
    //             jsonArray.add(getProduct(productId));
    //         }
    //     }
    //     return jsonArray.toString();
    // }

    public JsonObject getProduct(String productId) {
        if (!DataDeal.productExists(productId))
            return null;
        Product product = productMapper.getProduct(productId);
        if (product == null)
            return null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("productId", productId);
        jsonObject.addProperty("productName", product.getName());
        jsonObject.addProperty("productDescription", product.getDescription());
        jsonObject.addProperty("productPrice", product.getPrice());
        jsonObject.addProperty("productImage", "http://localhost:8080/product/getImage/" + productId);
        // jsonObject.addProperty("productImage", product.getImage());
        return jsonObject;
    }

}
