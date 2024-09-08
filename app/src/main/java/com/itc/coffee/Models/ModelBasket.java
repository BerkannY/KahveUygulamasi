package com.itc.coffee.Models;

import java.util.List;

public class ModelBasket {
    private ModelCoffees coffee;
    private ModelNatural natural;
    private ModelBooks book;
    private ModelDeserts dessert; // Tatlılar için yeni bir alan eklendi
    private String size;
    private List<String> extras;
    private int quantity;
    private double totalPrice;

    // Kahve için Constructor
    public ModelBasket(ModelCoffees coffee, String size, List<String> extras, int quantity) {
        this.coffee = coffee;
        this.size = size;
        this.extras = extras;
        this.quantity = quantity;
        updateTotalPrice(); // Toplam fiyatı güncelle
    }

    public ModelBasket() {
    }

    // Doğal içecekler için Constructor
    public ModelBasket(ModelNatural natural, String size, List<String> extras, int quantity) {
        this.natural = natural;
        this.size = size;
        this.extras = extras;
        this.quantity = quantity;
        updateTotalPrice(); // Toplam fiyatı güncelle
    }

    // Kitaplar için Constructor
    public ModelBasket(ModelBooks book, String size, List<String> extras, int quantity) {
        this.book = book;
        this.size = size;
        this.extras = extras;
        this.quantity = quantity;
        updateTotalPrice(); // Toplam fiyatı güncelle
    }

    // Tatlılar için Constructor
    public ModelBasket(ModelDeserts dessert, String size, List<String> extras, int quantity) {
        this.dessert = dessert;
        this.size = size;
        this.extras = extras;
        this.quantity = quantity;
        updateTotalPrice(); // Toplam fiyatı güncelle
    }

    // Toplam fiyatı güncelleyen metod
    public void updateTotalPrice() {
        totalPrice = 0.0; // Toplam fiyatı sıfırla

        // Ürün tipi belirle ve temel fiyatı ata
        if (coffee != null) {
            totalPrice = coffee.getPrice();
        } else if (natural != null) {
            totalPrice = natural.getPrice();
        } else if (book != null) {
            totalPrice = book.getPrice();
        } else if (dessert != null) {
            totalPrice = dessert.getPrice();
        }

        // Boyut fiyatını ekle, eğer boyut seçilmişse
        if (size != null) {
            totalPrice += getSizePrice(size);
        }

        // Ekstra fiyatları ekle, eğer ekstralar varsa
        if (extras != null && !extras.isEmpty()) {
            for (String extra : extras) {
                totalPrice += getExtraPrice(extra);
            }
        }

        totalPrice *= quantity; // Miktarı ekleyerek toplam fiyatı hesapla
    }

    // Boyut fiyatını döndüren metod
    private double getSizePrice(String size) {
        double sizePrice = 0.0;
        if (coffee != null) {
            sizePrice = coffee.getSizePrice(size); // Kahvelerde boyut fiyatını belirle
        } else if (natural != null) {
            sizePrice = natural.getSizePrice(size); // Doğal içeceklerde boyut fiyatını belirle
        } else if (book != null) {
            sizePrice = book.getSizePrice(size); // Kitaplarda boyut fiyatını belirle
        } else if (dessert != null) {
            // Tatlılarda boyut fiyatı uygulanıyor ise burada belirle
        }
        return sizePrice;
    }

    // Ekstra fiyatını döndüren metod
    private double getExtraPrice(String extra) {
        // Ekstra fiyatları hesaplama mantığı
        // Örneğin her ekstra 1.0 TL olarak belirlenmişse
        return 1.0; // Gerektiğinde gerçek mantığı uygulayın
    }

    // Getters ve setters
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ModelCoffees getCoffee() {
        return coffee;
    }

    public ModelNatural getNatural() {
        return natural;
    }

    public ModelBooks getBook() {
        return book;
    }

    public ModelDeserts getDessert() {
        return dessert;
    }

    public String getSize() {
        return size;
    }

    public List<String> getExtras() {
        return extras;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice(); // Miktar değiştiğinde toplam fiyatı güncelle
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
        updateTotalPrice(); // Ekstralar değiştiğinde toplam fiyatı güncelle
    }

    public void setCoffee(ModelCoffees coffee) {
        this.coffee = coffee;
    }

    public void setNatural(ModelNatural natural) {
        this.natural = natural;
    }

    public void setBook(ModelBooks book) {
        this.book = book;
    }

    public void setDessert(ModelDeserts dessert) {
        this.dessert = dessert;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ModelBasket{" +
                "coffee=" + coffee +
                ", natural=" + natural +
                ", book=" + book +
                ", dessert=" + dessert +
                ", size='" + size + '\'' +
                ", extras=" + extras +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
