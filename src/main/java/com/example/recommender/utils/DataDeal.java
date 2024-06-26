package com.example.recommender.utils;

import org.nlpcn.commons.lang.util.tuples.Pair;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

public class DataDeal {
    private static final String path = "D:\\download\\product_data\\products";

    private static Map<String, ArrayList<Float>> cache = new HashMap<>();


    public static HashSet<String> getRandSet(int num) {
        File[] files = new File(path).listFiles();
        return ThreadLocalRandom.current().ints(0, files.length)
                .distinct()
                .limit(num)
                .mapToObj(i -> files[i].getName())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public static String int2string(int num) {
        return String.format("%06d", num);
    }

    public static ArrayList<Float> getVec(String id) {
        if (cache.containsKey(id))
            return cache.get(id);
        ArrayList<Float> res = new ArrayList<Float>();
        if (!productExists(id))
            return res;
        try {
            FileReader file = new FileReader(path + '\\' + id + '\\' + id + "vec.txt");
            char[] tmp = new char[12000];
            file.read(tmp);
            String data = String.valueOf(tmp);
            String regex = "-?\\d\\.\\d+(e[+-]\\d+)?";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);
            while (matcher.find()) {
                res.add(Float.valueOf(matcher.group()));
            }
            file.close();
            cache.put(id, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Boolean productExists(String id) {
        File file = new File(path + '\\' + id);
        return file.exists();
    }

    public static HashSet<String> getCorrelatedProducts(String id) {
        HashSet<String> res = new HashSet<>();
        if (!productExists(id))
            return res;
        try {
            FileReader file = new FileReader(path + '\\' + id + '\\' + id + "rela.txt");
            char[] cache = new char[300];
            file.read(cache);
            Pattern pattern = Pattern.compile("\\d{6}");
            Matcher matcher = pattern.matcher(String.valueOf(cache));
            while (matcher.find()) {
                res.add(matcher.group());
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static double getSimilarity(ArrayList<Float> va, ArrayList<Float> vb) {
        if (va.isEmpty() || vb.isEmpty())
            return -1;
        double in = 0;
        double la = 0;
        double lb = 0;
        for (int i = 0; i < va.size(); i++) {
            in += va.get(i) * vb.get(i);
            la += va.get(i) * va.get(i);
            lb += vb.get(i) * vb.get(i);
        }
        la = Math.sqrt(la);
        lb = Math.sqrt(lb);
        return in / (la * lb);
    }

    private static int getMin(ArrayList<Pair<String, Double>> t) {
        int min = 0;
        for (int i = 0; i < 6; i++) {
            if (t.get(i).getValue1() < t.get(min).getValue1())
                min = i;
        }
        return min;
    }

    public static ArrayList<Pair<String, Double>> getNearPro(String s) {
        String[] files = new File(path).list();
        ArrayList<Float> vs = getVec(s);
        ArrayList<Pair<String, Double>> res = new ArrayList<>();
        if (vs.isEmpty())
            return res;
        for (int i = 0; i < 6; i++) {
            res.add(new Pair<>(files[i], getSimilarity(getVec(files[i]), vs)));
        }
        int min = getMin(res);
        for (int i = 6; i < files.length; i++) {
            ArrayList<Float> vec = getVec(files[i]);
            double t = getSimilarity(vec, vs);
            if (t > res.get(min).getValue1()) {
                res.set(min, new Pair<>(files[i], t));
            }
            min = getMin(res);
        }
        res.removeIf(t -> t.getValue0().equals(s));
        return res;
    }
}
