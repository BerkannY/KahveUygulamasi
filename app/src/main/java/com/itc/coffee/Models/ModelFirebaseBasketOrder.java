package com.itc.coffee.Models;

public class ModelFirebaseBasketOrder {
    public String customerUID;
    public boolean extraSyrup;
    public boolean extraMilk;
    public boolean extraExpresso;
    public String dateReverse;
    public String date;
    public double lastPricetotal;
    public boolean orderStatus;
    public double priceTotal;
    public int productCounter;
    public String productImageUrl;
    public String productName;
    public String productSize;
    public String time;
    public String qrCode;
    public String productType;
    public boolean cardPayment;


    public ModelFirebaseBasketOrder() {
    }

    public ModelFirebaseBasketOrder(String customerUID, boolean extraSyrup, boolean extraMilk, boolean extraExpresso, String dateReverse, String date, double lastPricetotal, boolean orderStatus, double priceTotal, int productCounter, String productImageUrl, String productName, String productSize, String time, String qrCode, String productType,boolean cardPayment) {
        this.customerUID = customerUID;
        this.extraSyrup = extraSyrup;
        this.extraMilk = extraMilk;
        this.extraExpresso = extraExpresso;
        this.dateReverse = dateReverse;
        this.date = date;
        this.lastPricetotal = lastPricetotal;
        this.orderStatus = orderStatus;
        this.priceTotal = priceTotal;
        this.productCounter = productCounter;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.productSize = productSize;
        this.time = time;
        this.qrCode = qrCode;
        this.productType = productType;
        this.cardPayment = cardPayment;
    }

    public String getCustomerUID() {
        return customerUID;
    }

    public void setCustomerUID(String customerUID) {
        this.customerUID = customerUID;
    }

    public boolean isExtraSyrup() {
        return extraSyrup;
    }

    public void setExtraSyrup(boolean extraSyrup) {
        this.extraSyrup = extraSyrup;
    }

    public boolean isExtraMilk() {
        return extraMilk;
    }

    public void setExtraMilk(boolean extraMilk) {
        this.extraMilk = extraMilk;
    }

    public boolean isExtraExpresso() {
        return extraExpresso;
    }

    public void setExtraExpresso(boolean extraExpresso) {
        this.extraExpresso = extraExpresso;
    }

    public String getDateReverse() {
        return dateReverse;
    }

    public void setDateReverse(String dateReverse) {
        this.dateReverse = dateReverse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLastPricetotal() {
        return lastPricetotal;
    }

    public void setLastPricetotal(double lastPricetotal) {
        this.lastPricetotal = lastPricetotal;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public int getProductCounter() {
        return productCounter;
    }

    public void setProductCounter(int productCounter) {
        this.productCounter = productCounter;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public boolean isCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(boolean cardPayment) {
        this.cardPayment = cardPayment;
    }
}
