package com.example.recommender;

import com.example.recommender.utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.example.recommender.service.ProductService;
import com.example.recommender.service.UserService;
import org.junit.jupiter.api.Test;
import org.nlpcn.commons.lang.util.tuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@SpringBootTest
class RecomenderApplicationTests {

    @Test
    void loading() throws IOException {
        LoadProducts.exec();
    }


    @Test
    void init() throws IOException {
        LoadProducts.init();
    }

    @Test
    void clear() throws IOException {
        LoadProducts.clear();
    }

    @Test
    void generateRandomPrice() throws IOException {
        LoadProducts.generateRandomPrice();
    }

    @Autowired
    ProductService productService = new ProductService();

    @Test
    void test(){
        Set<String> recommend = userService.getRecommend("16");
        System.out.println(recommend);
        JsonArray array = new JsonArray();
        for(String item : recommend) {
            array.add(productService.getProduct(item));
        }
        System.out.println(array.toString());
    }


    @Autowired
    private UserService userService;


    @Test
    void vec() throws Exception{
        for(Pair<String,Double> p:DataDeal.getNearPro("042136")){
            System.out.println(p);
        }
    }
}
