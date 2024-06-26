package com.example.recommender.utils;

import org.nlpcn.commons.lang.util.tuples.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadProducts {
    
    public static void exec() throws IOException {
        File[] products=new File("D:\\download\\product_data\\products").listFiles();
        File[] images=new File("D:\\download\\product_data\\image_text").listFiles();
        File[] videos=new File("D:\\download\\product_data\\video_text").listFiles();
        for(int i=1;i<5000;i++){
            File image=images[i];
            File video=videos[i];
            File product=products[i];
            File[] tmp=product.listFiles();
            for(int j=1;j< tmp.length;j++){
                tmp[j].delete();
            }
            FileWriter writer=new FileWriter(product.getAbsolutePath()+"\\"+image.getName());
            FileReader fi=new FileReader(image);
            FileReader fv=new FileReader(video);
            int ch;
            while ((ch=fi.read())!=-1){
                writer.append((char) ch);
            }
            writer.append('\n');
            while ((ch=fv.read())!=-1){
                writer.append((char) ch);
            }
            writer.close();
            fi.close();
            fv.close();
        }
    }

    public static void init() throws IOException {
        String path = "D:\\download\\product_data\\products";
        String[] products=new File(path).list();
        for(String product:products){
            ArrayList<Pair<String,Double>> t = DataDeal.getNearPro(product);
            FileWriter writer=new FileWriter(path+"\\"+product+"\\"+product+"rela.txt");
            for(Pair<String,Double> p:t) {
                if(p.getValue0().equals(product)) continue;
                writer.append('['+p.getValue0()+']');
            }
            writer.close();
        }
    }

    public static void clear() throws IOException {
        String path = "D:\\download\\product_data\\products";
        String[] products=new File(path).list();
        for (int i = 0; i < 5000; i++) {
            File data = new File(path+"\\"+products[i]+"\\"+products[i]+"rela.txt");
            FileReader reader = new FileReader(data);
            char[] tmp = new char[300];
            reader.read(tmp);
            String s = String.valueOf(tmp);
            reader.close();
            String regex = "\\[" + products[i] + ".+]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matter = pattern.matcher(s);
            String res = matter.replaceAll("");
            FileWriter writer = new FileWriter(data);
            writer.write(res);
            writer.close();
        }
    }

    public static void generateRandomPrice() throws IOException {
        String path = "D:\\download\\product_data\\products";
        String[] products=new File(path).list();
        for(String product:products){
            FileWriter writer=new FileWriter(path+"\\"+product+"\\"+product+"price.txt");
            writer.write(String.valueOf(Math.round(Math.random()*60) * 10 ));
            writer.close();
        }
    }

    /* public static void optimize() throws IOException {
        String path = "D:\\download\\product_data\\products";
        String[] products=new File(path).list();
        for(String product:products){
            FileWriter writer=new FileWriter(path+"\\"+product+"\\"+product+"rela.txt");
            File[] files=new File(path+"\\"+product).listFiles();
            for(File file:files){
                if(file.isFile()&&file.getName().endsWith("rela.txt")){
                    FileReader reader=new FileReader(file);
                    char[] tmp=new char[300];
                    reader.read(tmp);
                    String s=String.valueOf(tmp);
                    reader.close();
                    String regex="\\d{6},";
                    Pattern pattern=Pattern.compile(regex);
                    Matcher matter=pattern.matcher(s);
                    StringJoiner newdata = new StringJoiner(",", "[", "]");
                    while (matter.find()) {
                        newdata.add(matter.group().substring(0, 6));
                    }
                    writer.write(newdata.toString());
                }
            }
            writer.close();
        }
    } */

}
