package com.itc.coffee.Models;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;


public class ModelNatural {
    private String id,name,imageUrl;
    private double price,midPrice,bigPrice;
    private Map<String, Double> sizes;
    private String selectedSize = "Small";

    public ModelNatural() {
    }

    public ModelNatural(String id, String name, String imageUrl, double price, Map<String, Double> sizes,double midPrice,double bigPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sizes = sizes;
        this.midPrice = midPrice;
        this.bigPrice = bigPrice;
    }

    public double getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(double midPrice) {
        this.midPrice = midPrice;
    }

    public double getBigPrice() {
        return bigPrice;
    }

    public void setBigPrice(double bigPrice) {
        this.bigPrice = bigPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Double> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Double> sizes) {
        this.sizes = sizes;
    }

    // Boyut fiyatını alma metodu
    public double getSizePrice(String size) {
        return sizes != null ? sizes.getOrDefault(size, 0.0) : 0.0;
    }



    // Toplam fiyat hesaplama metodu (temel fiyat + boyut fiyatı + ekstralar)
    public double calculateTotalPrice() {
        double totalPrice = price + getSizePrice(selectedSize);
        return totalPrice;
    }

    // Boyut seçimini ayarla
    public void selectSize(String size) {
        selectedSize = size;
    }



}
