package com.example.recommender.utils;

import com.example.recommender.pojo.User;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class UserDataDeal {

    // private static String get(String data, String regex) {
    //     Pattern pattern = Pattern.compile(regex);
    //     Matcher matcher = pattern.matcher(data);
    //     if (matcher.find()) {
    //         return matcher.group(1);
    //     }
    //     return null;
    // }

    public static User getUser(String newuser) {
        // 使用Gson库解析JSON数据
        Gson gson = new Gson();
        User user = gson.fromJson(newuser, User.class);
        user.setPurchaseList("[]");
        user.setClickList("[]");
        user.setShoppingCart("[]");
        return user;
    }

    public static List<String> getList(String s) {
        return List.of(s.substring(1, s.length() - 1).split(","));
    }

    public static ArrayList<Float> getScore(List<String> scores) {
        ArrayList<Float> scoreList = new ArrayList<>(Collections.nCopies(768, 0.0f));
        for (String score : scores) {
            ArrayList<Float> vec = DataDeal.getVec(score);
            for (int k = 0; k < vec.size(); k++) {
                scoreList.set(k, scoreList.get(k) + vec.get(k));
            }
        }
        return scoreList;
    }

    public static HashSet<String> getRecommendList(HashSet<String> set) {
        HashSet<String> recommendList = new HashSet<String>();
        for (String s : set) {
            recommendList.addAll(DataDeal.getCorrelatedProducts(s));
        }
        recommendList.removeAll(set);
        return recommendList;
    }

}
