package com.itc.coffee.Models;

import java.util.Map;

public class ModelBooks {
    private String id, name, imageUrl;
    private double price,bookBuy;
    private Map<String, Double> sizes;
    private String selectedSize = "Small";


    public ModelBooks() {
    }


    public ModelBooks(String id, String name, String imageUrl, double price, double bookBuy, Map<String, Double> sizes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sizes = sizes;
        this.bookBuy = bookBuy;


    }

    public double getBookBuy() {
        return bookBuy;
    }

    public void setBookBuy(double bookBuy) {
        this.bookBuy = bookBuy;
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
        double totalPrice = price + getSizePrice(selectedSize );

        return totalPrice;
    }

    // Boyut seçimini ayarla
    public void selectSize(String size) {
        selectedSize = size;
    }


}