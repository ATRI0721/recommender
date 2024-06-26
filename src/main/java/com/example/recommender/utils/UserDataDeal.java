package com.example.recommender.utils;

import com.example.recommender.pojo.User;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;

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

    public static HashSet<String> getSet(String s) {
        HashSet<String> set = new HashSet<String>();
        String[] strs = s.split(",");
        for (int i = 1; i < strs.length; i++) {
            set.add(strs[i]);
        }
        return set;
    }

    public static ArrayList<Float> getScore(String s) {
        String[] scores = s.split(",");
        ArrayList<Float> scoreList = new ArrayList<Float>(768);
        for (int i = 0; i < 768; i++) {
            scoreList.add(0.0f);
        }
        for (int i = 1; i < scores.length; i++) {
            int j = scores.length - i;
            ArrayList<Float> vec = DataDeal.getVec(scores[i]);
            for (int k = 0; k < vec.size(); k++) {
                scoreList.set(k, scoreList.get(k) + vec.get(k) / (float) Math.sqrt(j));
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
