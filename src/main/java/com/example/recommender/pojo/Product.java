package com.example.recommender.pojo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String image;
    private String name;
    private String description;
    private int price;
    private ArrayList<Float> vector;
    private HashSet<String> similarProducts;
}
