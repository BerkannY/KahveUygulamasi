package com.itc.coffee.Models;

public class ModelCustomerCard {
    double balance;
    int cardID;
    String createDate;
    String selectedImage;

    public ModelCustomerCard(double balance, int cardID, String createDate, String selectedImage) {
        this.balance = balance;
        this.cardID = cardID;
        this.createDate = createDate;
        this.selectedImage = selectedImage;
    }

    public ModelCustomerCard() {
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }
}
