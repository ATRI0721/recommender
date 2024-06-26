package com.example.recommender.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("userTable")
public class User {
    private Integer id;
    private String username;
    private String password;
    private String clickList;
    private String shoppingCart;
    private String purchaseList;
}
