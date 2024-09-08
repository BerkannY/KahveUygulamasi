package com.itc.coffee.Models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelDeserts {
    private String id,name,imageUrl;
    private double price;

    public ModelDeserts() {
    }

    public ModelDeserts(String id, String name, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;


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


    // Toplam fiyat hesaplama metodu (temel fiyat + boyut fiyatı + ekstralar)
    public double calculateTotalPrice() {
        double totalPrice = price ;
        return totalPrice;
    }




}
