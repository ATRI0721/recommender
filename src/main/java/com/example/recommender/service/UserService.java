package com.example.recommender.service;

import org.nlpcn.commons.lang.util.tuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Collections;
import com.example.recommender.mapper.UserMapper;
import com.example.recommender.pojo.User;
import com.example.recommender.utils.DataDeal;
import com.example.recommender.utils.UserDataDeal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductService productService;

    private Gson gson = new Gson();

    public String login(String userdata) {
        User user = new Gson().fromJson(userdata, User.class);
        Gson gson = new Gson();
        List<User> userList = userMapper.selectByMap(new HashMap<String, Object>() {
            {
                put("username", user.getUsername());
                put("password", user.getPassword());
            }
        });
        if (userList.size() == 0)
            return gson.toJson(-1);
        return gson.toJson(userList.get(0).getId());
    }

    public Boolean userAndProductExist(User user, String productId) {
        if (user == null)
            return false;
        return DataDeal.productExists(productId);
    }

    public JsonArray getJsonCart(String userId) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        if (user == null)
            return null;
        String scList = user.getShoppingCart();
        JsonArray jsonArray = JsonParser.parseString(scList).getAsJsonArray();
        return jsonArray;
    }

    public JsonArray resolveCart(String cart) {
        return JsonParser.parseString(cart).getAsJsonObject().get("cart").getAsJsonArray();
    }

    public String addToCart(String userId, String productId, int quantity) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        Map<String, String> response = new HashMap<>();
        if (!userAndProductExist(user, productId)) {
            response.put("code", "-1");
            response.put("msg", "product or user not exists");
            return gson.toJson(response);
        }
        JsonArray jsonArray = getJsonCart(userId);
        for (JsonElement obj : jsonArray) {
            if (obj.getAsJsonObject().get("id").getAsString().equals(productId)) {
                response.put("code", "-1");
                response.put("msg", "update quantity success");
                user.setShoppingCart(jsonArray.toString());
                userMapper.updateById(user);
                return gson.toJson(response);
            }
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("id", productId);
        obj.addProperty("quantity", quantity);
        jsonArray.add(obj);
        user.setShoppingCart(jsonArray.toString());
        userMapper.updateById(user);
        response.put("code", "1");
        response.put("msg", "add to shopping cart success");
        return gson.toJson(response);
    }

    public String updataCart(String userId, String cart) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        user.setShoppingCart(resolveCart(cart).toString());
        userMapper.updateById(user);
        return gson.toJson(Map.of("code", 1));
    }

    public String removeFromSC(String userId, String productId) {
        Map<String, String> response = new HashMap<>();
        User user = userMapper.selectById(Integer.parseInt(userId));
        if (!userAndProductExist(user, productId)) {
            response.put("code", "-1");
            response.put("msg", "product or user not exists");
            return gson.toJson(response);
        }
        response.put("code", "1");
        JsonArray jsonArray = getJsonCart(userId);
        for (JsonElement obj : jsonArray) {
            if (obj.getAsJsonObject().get("id").getAsString().equals(productId)) {
                jsonArray.remove(obj);
                user.setShoppingCart(jsonArray.toString());
                userMapper.updateById(user);
                response.put("msg", "remove from shopping cart success");
                return gson.toJson(response);
            }
        }
        response.put("msg", "product not in shopping cart");
        userMapper.updateById(user);
        return gson.toJson(response);
    }

    public JsonObject getShoppingCart(String userId) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        JsonObject responseJson = new JsonObject();
        if (user == null) {
            responseJson.addProperty("code", "-1");
            responseJson.addProperty("msg", "user not exists");
            return responseJson;
        }
        String scList = user.getShoppingCart();
        JsonArray jsonArray = JsonParser.parseString(scList).getAsJsonArray();
        responseJson.addProperty("code", "1");
        responseJson.add("cart", jsonArray);
        return responseJson;
    }

    public String purchase(String userId, String products) {
        Map<String, String> response = new HashMap<>();
        User user = userMapper.selectById(Integer.parseInt(userId));
        if (user == null) {
            response.put("code", "-1");
            response.put("msg", "user not exists");
            return gson.toJson(response);
        }
        Set<String> productTopurchase = new HashSet<>();
        JsonParser.parseString(products).getAsJsonObject().get("cart").getAsJsonArray()
                .forEach(e -> productTopurchase.add(e.getAsString()));
        if (productTopurchase.size() == 0) {
            response.put("code", "-1");
            response.put("msg", "no product in cart");
            return gson.toJson(response);
        }
        StringJoiner newPurchaseList = new StringJoiner(",", "[", "]");
        JsonParser.parseString(user.getPurchaseList()).getAsJsonArray().forEach(e -> {
            if (!productTopurchase.contains(e.getAsString())) {
                newPurchaseList.add(e.getAsString());
            }
        });
        productTopurchase.forEach(e -> newPurchaseList.add(e));
        user.setPurchaseList(newPurchaseList.toString());
        userMapper.updateById(user);
        response.put("code", "1");
        response.put("msg", "purchase success");
        return gson.toJson(response);
    }

    public String sava(String newuser) {
        Gson gson = new Gson();
        User user = UserDataDeal.getUser(newuser);
        if (!userMapper.selectByMap(new HashMap<String, Object>() {
            {
                put("username", user.getUsername());
            }
        }).isEmpty())
            return gson.toJson(-1);
        if (user.getUsername().length() > 15 || user.getPassword().length() > 15) {
            return null;
        }
        int res = userMapper.insert(user);
        int id = user.getId() == null && res == 1 ? -1 : user.getId();
        return gson.toJson(id);
    }

    public void addClick(String userId, String productId) {
        int userIdInt = Integer.parseInt(userId);
        if (userIdInt == 0 || !DataDeal.productExists(productId)) {
            return;
        }
        User user = userMapper.selectById(userIdInt);
        if (user == null) {
            return;
        }
        StringJoiner newClickList = new StringJoiner(",", "[", "]");
        JsonParser.parseString(user.getClickList()).getAsJsonArray().forEach(e -> {
            if (!e.getAsString().equals(productId)) {
                newClickList.add(e.getAsString());
            }
        });
        newClickList.add(productId);
        user.setClickList(newClickList.toString());
        userMapper.updateById(user);
    }

    public String getClicks(String userId) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        JsonArray jsonArray = new JsonArray();
        String clickList = user.getClickList();
        Arrays.stream(clickList.substring(1, clickList.length() - 1).split(","))
                .forEach(e -> jsonArray.add(productService.getProduct(e)));
        return jsonArray.toString();
    }

    public String getPurchaseList(String userId) {
        User user = userMapper.selectById(Integer.parseInt(userId));
        JsonArray jsonArray = new JsonArray();
        String purchaseList = user.getPurchaseList();
        Arrays.stream(purchaseList.substring(1, purchaseList.length() - 1).split(","))
                .forEach(e -> {
                    jsonArray.add(productService.getProduct(e));
                });
        return jsonArray.toString();
    }

    public Set<String> getRecommend(String userId) {
        if (userId == null || userId.isEmpty() || userId.equals("0") || userId.equals("null")) {
            return DataDeal.getRandSet(20);
        }
        User user = userMapper.selectById(Integer.parseInt(userId));
        if (user == null) {
            return DataDeal.getRandSet(20);
        }

        List<List<Float>> scores = new ArrayList<>();

        String clickList = user.getClickList();
        String purchaseList = user.getPurchaseList();
        String tmpShoppingCart = user.getShoppingCart();

        List<String> shoppingCart = new ArrayList<>();
        JsonParser.parseString(tmpShoppingCart).getAsJsonArray()
                .forEach(e -> shoppingCart.add(e.getAsJsonObject().get("id").getAsString()));

        List<List<String>> lists = new ArrayList<>();
        lists.add(UserDataDeal.getList(clickList));
        lists.add(shoppingCart);
        lists.add(UserDataDeal.getList(purchaseList));
        for (List<String> list : lists) {
            List<Float> score = UserDataDeal.getScore(list);
            scores.add(score);
        }
        HashSet<String> setOfProducts = new HashSet<>();
        lists.forEach(e -> setOfProducts.addAll(e));

        List<Float> userVec = calculateUserVec(scores);
        Set<String> recommendList = UserDataDeal.getRecommendList(setOfProducts);
        List<Pair<String, Double>> recommendScore = calculateRecommendScores(recommendList, userVec);
        return getTopRecommendations(recommendScore, 20);
    }

    private List<Float> calculateUserVec(List<List<Float>> scores) {
        List<Float> userVec = new ArrayList<>(Collections.nCopies(scores.get(0).size(), 0.0f));
        for (int i = 0; i < userVec.size(); i++) {
            userVec.set(i, (scores.get(0).get(i) + 6 * scores.get(1).get(i) + 3 * scores.get(2).get(i)));
        }
        return userVec;
    }

    private List<Pair<String, Double>> calculateRecommendScores(Set<String> recommendList, List<Float> userVec) {
        List<Pair<String, Double>> recommendScore = new ArrayList<>();
        for (String productId : recommendList) {
            Double similarity = DataDeal.getSimilarity(DataDeal.getVec(productId), (ArrayList<Float>) userVec);
            recommendScore.add(new Pair<>(productId, similarity));
        }
        recommendScore.sort((a, b) -> b.getValue1().compareTo(a.getValue1()));
        return recommendScore;
    }

    private Set<String> getTopRecommendations(List<Pair<String, Double>> recommendScore, int count) {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < count && i < recommendScore.size(); i++) {
            result.add(recommendScore.get(i).getValue0());
        }
        if (result.size() < count)
            result.addAll(DataDeal.getRandSet(count - result.size()));
        return result;
    }

}
