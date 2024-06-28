package com.example.recommender.controller;


import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.example.recommender.service.ProductService;
import com.example.recommender.service.UserService;
import com.google.gson.JsonArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @CrossOrigin
    @PostMapping("/register")
    public String register(@RequestBody String user) {
        return userService.sava(user);       
    }

    @CrossOrigin
    @PostMapping("/login")
    public String login(@RequestBody String user) {       
        String token = userService.login(user);
        return token;
    }
    
    @CrossOrigin
    @GetMapping("/{id}")
    public String getRerecommend(@PathVariable String id) {
        Set<String> recommend = userService.getRecommend(id);
        JsonArray array = new JsonArray();
        for(String item : recommend) {
            array.add(productService.getProduct(item));
        }
        return array.toString();
    }

    @CrossOrigin
    @PostMapping("/{userId}/updateCart")
    public String updateCart(@PathVariable String userId, @RequestBody String cart) {
        return userService.updataCart(userId, cart);
    }

    @CrossOrigin
    @GetMapping("/{userId}/addCart/{productId}/{quantity}")
    public String addCart(@PathVariable String userId, @PathVariable String productId, @PathVariable int quantity) {
        return userService.addToCart(userId, productId, quantity);
    }

    @CrossOrigin
    @GetMapping("/{userId}/getClicks")
    public String getClicks(@PathVariable String userId) {
        return userService.getClicks(userId);
    }

    @CrossOrigin
    @GetMapping("/{userId}/getPurchases")
    public String getPurchases(@PathVariable String userId) {
        return userService.getPurchaseList(userId);
    }

    @CrossOrigin
    @PostMapping("/{userId}/purchase")
    public String purchase(@PathVariable String userId, @RequestBody String products) {
        return userService.purchase(userId, products);
    }
    
    @CrossOrigin
    @GetMapping("/{userId}/getCart")
    public String getShoppingCart(@PathVariable String userId) {
        return userService.getShoppingCart(userId).toString();
    }
    
}
