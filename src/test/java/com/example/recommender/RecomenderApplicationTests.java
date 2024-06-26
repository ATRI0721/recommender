package com.example.recommender;

import com.example.recommender.utils.*;
import com.google.gson.JsonParser;
import com.example.recommender.service.UserService;
import org.junit.jupiter.api.Test;
import org.nlpcn.commons.lang.util.tuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

    @Test
    void test(){
        String[] arr = "[a,006]".replace("[", "").replace("]", "").split(",");
        for(String s:arr){
            System.out.println(s);
        }
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
