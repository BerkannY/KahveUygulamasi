package com.itc.coffee.Models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelCoffees {
    private String id, name, imageUrl;
    private double price, midPrice, bigPrice, extraExpressoPrice, extraMilkPrice, extraSyrupPrice;
    private Map<String, Double> sizes;
    private Map<String, Double> extras;
    private String selectedSize = "Small";
    private Set<String> selectedExtras = new HashSet<>();
    private Set<String> selectedExtrasGetAll = new HashSet<>();

    public ModelCoffees() {
    }

    public ModelCoffees(String id, String name, String imageUrl, double price, Map<String, Double> sizes, Map<String, Double> extras,
                        double midPrice, double bigPrice, double extraExpressoPrice, double extraMilkPrice, double extraSyrupPrice) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sizes = sizes;
        this.extras = extras;
        this.midPrice = midPrice;
        this.bigPrice = bigPrice;
        this.extraExpressoPrice = extraExpressoPrice;
        this.extraMilkPrice = extraMilkPrice;
        this.extraSyrupPrice = extraSyrupPrice;
    }

    public double getExtraMilkPrice() {
        return extraMilkPrice;
    }

    public void setExtraMilkPrice(double extraMilkPrice) {
        this.extraMilkPrice = extraMilkPrice;
    }

    public double getExtraExpressoPrice() {
        return extraExpressoPrice;
    }

    public void setExtraExpressoPrice(double extraExpressoPrice) {
        this.extraExpressoPrice = extraExpressoPrice;
    }

    public double getExtraSyrupPrice() {
        return extraSyrupPrice;
    }

    public void setExtraSyrupPrice(double extraSyrupPrice) {
        this.extraSyrupPrice = extraSyrupPrice;
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

    public Map<String, Double> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Double> extras) {
        this.extras = extras;
    }

    public Set<String> getSelectedExtrasGetAll() {
        return selectedExtrasGetAll;
    }

    public void setSelectedExtrasGetAll(Set<String> selectedExtrasGetAll) {
        this.selectedExtrasGetAll = selectedExtrasGetAll;
    }

    // Boyut fiyatını alma metodu
    public double getSizePrice(String size) {
        switch (size) {
            case "Medium":
                return midPrice;
            case "Large":
                return bigPrice;
            case "Small":
            default:
                return 0.0; // Küçük boyut için ek ücret yok, sadece temel fiyat kullanılır
        }
    }

    // Extra fiyatını alma metodu
    public double getExtraPrice(String extra) {
        switch (extra) {
            case "Expresso":
                return extraExpressoPrice;
            case "Milk":
                return extraMilkPrice;
            case "Syrup":
                return extraSyrupPrice;
            default:
                return 0.0;
        }
    }

    // Toplam fiyat hesaplama metodu (temel fiyat + boyut fiyatı + ekstralar)
    public double calculateTotalPrice() {
        double totalPrice = price + getSizePrice(selectedSize);
        for (String extra : selectedExtras) {
            totalPrice += getExtraPrice(extra);
        }
        return totalPrice;
    }

    // Boyut seçimini ayarla
    public void selectSize(String size) {
        selectedSize = size;
    }

    // Extra ekle veya çıkar
    public void toggleExtra(String extra) {
        if (selectedExtras.contains(extra)) {
            selectedExtras.remove(extra);
        } else {
            selectedExtras.add(extra);
        }
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public Set<String> getSelectedExtras() {
        return selectedExtras;
    }

    @Override
    public String toString() {
        return "ModelCoffees{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", midPrice=" + midPrice +
                ", bigPrice=" + bigPrice +
                ", extraExpressoPrice=" + extraExpressoPrice +
                ", extraMilkPrice=" + extraMilkPrice +
                ", extraSyrupPrice=" + extraSyrupPrice +
                ", sizes=" + sizes +
                ", extras=" + extras +
                ", selectedSize='" + selectedSize + '\'' +
                ", selectedExtras=" + selectedExtras +
                ", selectedExtrasGetAll=" + selectedExtrasGetAll +
                '}';
    }
}
