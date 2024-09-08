package com.itc.coffee.Models;

public class ModelOrder {
    private String date;
    private boolean extraExpresso;
    private boolean extraMilk;
    private boolean extraSyrup;
    private double lastPricetotal;
    private boolean orderStatus;
    private double priceTotal;
    private double productCounter;
    private String productSize;
    private String productName;
    private String time;
    private String productImageUrl;

    public ModelOrder(String date, boolean extraExpresso, boolean extraMilk, boolean extraSyrup, double lastPricetotal, Boolean orderStatus, double priceTotal, double productCounter, String productSize, String productName, String time, String productImageUrl) {
        this.date = date;
        this.extraExpresso = extraExpresso;
        this.extraMilk = extraMilk;
        this.extraSyrup = extraSyrup;
        this.lastPricetotal = lastPricetotal;
        this.orderStatus = orderStatus;
        this.priceTotal = priceTotal;
        this.productCounter = productCounter;
        this.productSize = productSize;
        this.productName = productName;
        this.time = time;
        this.productImageUrl = productImageUrl;
    }

    public ModelOrder() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isExtraExpresso() {
        return extraExpresso;
    }

    public void setExtraExpresso(boolean extraExpresso) {
        this.extraExpresso = extraExpresso;
    }

    public boolean isExtraMilk() {
        return extraMilk;
    }

    public void setExtraMilk(boolean extraMilk) {
        this.extraMilk = extraMilk;
    }

    public boolean isExtraSyrup() {
        return extraSyrup;
    }

    public void setExtraSyrup(boolean extraSyrup) {
        this.extraSyrup = extraSyrup;
    }

    public double getLastPriceTotal() {
        return lastPricetotal;
    }

    public void setLastPriceTotal(double lastPriceTotal) {
        this.lastPricetotal = lastPricetotal;
    }


    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public double getProductCounter() {
        return productCounter;
    }

    public void setProductCounter(double productCounter) {
        this.productCounter = productCounter;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
