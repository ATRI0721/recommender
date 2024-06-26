package com.example.recommender.mapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Repository;
import com.example.recommender.pojo.Product;
import com.example.recommender.utils.DataDeal;

@Repository
public class ProductMapper {
    private static final String PATH = "D:\\download\\product_data\\products";

    public Product getProduct(String productId) {
        if (!DataDeal.productExists(productId))
            return null;
        Product product = new Product();
        product.setId(productId);
        product.setImage(getProductImage(productId));
        product.setName(getProductName(productId));
        product.setDescription(getProductDescription(productId));
        product.setPrice(getProductPrice(productId));
        product.setVector(null);
        product.setSimilarProducts(DataDeal.getCorrelatedProducts(productId));
        return product;
    }

    public String getProductName(String productId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(PATH + "\\" + productId + "\\" + productId + ".txt"), StandardCharsets.UTF_8))) {
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getProductDescription(String productId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(PATH + "\\" + productId + "\\" + productId + ".txt"), StandardCharsets.UTF_8))) {
            StringBuilder s = new StringBuilder();
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getProductImage(String productId) {
        if (!DataDeal.productExists(productId))
            return null;
        return PATH + "\\" + productId + "\\0.jpg";
    }

    public int getProductPrice(String productId) {
        try (FileReader reader = new FileReader(PATH + "\\" + productId + "\\" + productId + "price.txt")) {
            StringBuilder s = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                s.append((char) c);
            }
            return String.valueOf(s).trim().length() == 0 ? 0 : Integer.parseInt(s.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
