package com.itc.coffee.Mainfragments;

import com.itc.coffee.Models.ModelBasket;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.Models.ModelNatural;
import com.itc.coffee.Models.ModelDeserts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketManager {
    public static List<ModelBasket> cartItems = new ArrayList<>();


    // Sepete ekle metodu güncellendi
    public static void addToCart(Object item, String size, List<String> extras, int quantity) {
        ModelBasket cartItem;

        if (item instanceof ModelCoffees) {
            cartItem = new ModelBasket((ModelCoffees) item, size, extras, quantity);
        } else if (item instanceof ModelNatural) {
            cartItem = new ModelBasket((ModelNatural) item, size, extras, quantity);
        } else if (item instanceof ModelBooks) {
            cartItem = new ModelBasket((ModelBooks) item, size, extras, quantity);
        } else if (item instanceof ModelDeserts) {
            cartItem = new ModelBasket((ModelDeserts) item, size, extras, quantity); // Desert desteği eklendi
        } else {
            throw new IllegalArgumentException("Unsupported item type");
        }

        cartItems.add(cartItem);
    }



    public static List<ModelBasket> getCartItems() {
        return cartItems;
    }

    // Sepetteki ürünlerin fiyatlarını güncelle
    public static void updateCartItemPrices() {
        for (ModelBasket item : cartItems) {
            item.updateTotalPrice(); // `updateTotalPrice` metodu artık `public`
        }
    }
    public static void clearCart(){
        cartItems.clear();
    }
}
