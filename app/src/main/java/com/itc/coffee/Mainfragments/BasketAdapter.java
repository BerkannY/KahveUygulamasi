package com.itc.coffee.Mainfragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itc.coffee.Models.ModelBasket;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.Models.ModelNatural;
import com.itc.coffee.Models.ModelDeserts;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.CartViewHolder> {
    private List<ModelBasket> cartItemList;
    private Context context;
    private TextView textViewTotalPrice;

    public BasketAdapter(List<ModelBasket> cartItemList, Context context, TextView textViewTotalPrice) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.textViewTotalPrice = textViewTotalPrice;
        calculateTotalPrice(); // İlk toplam fiyatı hesapla
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ModelBasket cartItem = cartItemList.get(position);

        // Kahve ürünleri için veri gösterimi
        if (cartItem.getCoffee() != null) {
            double coffeeTotalPrice = cartItem.getTotalPrice();
            ModelCoffees coffee = cartItem.getCoffee();
            holder.coffeeName.setText(coffee.getName());
            List<String> sortedList = new ArrayList<>(coffee.getSelectedExtrasGetAll());
            Collections.sort(sortedList);
            for (String item : sortedList) {
                System.out.println("-"+item+"-");
                if ("Espresso".equals(item)){
                    coffeeTotalPrice = coffeeTotalPrice+(coffee.getExtraExpressoPrice()*cartItem.getQuantity());
                } else if ("Milk".equals(item)) {
                    coffeeTotalPrice = coffeeTotalPrice+(coffee.getExtraMilkPrice()*cartItem.getQuantity());
                }else if ("Syrup".equals(item)) {
                    coffeeTotalPrice = coffeeTotalPrice+(coffee.getExtraSyrupPrice()*cartItem.getQuantity());
                }
            }
            holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", coffeeTotalPrice));
            holder.textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context)
                    .load(coffee.getImageUrl())
                    .placeholder(R.drawable.itc_logo)
                    .error(R.drawable.itc_logo2)
                    .into(holder.coffeeImage);

            holder.coffeeSize.setText("Boyut: " + cartItem.getSize());
            holder.coffeeExtras.setText("Ekstralar: " + ( String.join(", ", cartItem.getCoffee().getSelectedExtrasGetAll())));
        }
        // Doğal içecekler için veri gösterimi
        else if (cartItem.getNatural() != null) {
            ModelNatural natural = cartItem.getNatural();
            holder.coffeeName.setText(natural.getName());
            holder.coffeeSize.setText("Boyut: " + cartItem.getSize());
            if(cartItem.getSize().equals("Medium") )
                holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", (cartItem.getTotalPrice()+(natural.getMidPrice()*cartItem.getQuantity()))));
            else if (cartItem.getSize().equals("Large")) {
                holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", (cartItem.getTotalPrice()+(natural.getBigPrice()*cartItem.getQuantity()))));
            }else{
                holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", cartItem.getTotalPrice()));
            }



            holder.textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context)
                    .load(natural.getImageUrl())
                    .placeholder(R.drawable.itc_logo)
                    .error(R.drawable.itc_logo2)
                    .into(holder.coffeeImage);


         }
        // Kitaplar için veri gösterimi
        else if (cartItem.getBook() != null) {
            ModelBooks book = cartItem.getBook();
            holder.coffeeName.setText(book.getName());
//            holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", cartItem.getTotalPrice()));
            holder.textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context)
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.itc_logo)
                    .error(R.drawable.itc_logo2)
                    .into(holder.coffeeImage);
            if (cartItem.getSize().equals("Small")){
                holder.coffeeSize.setText("Günlük Kiralama");
                holder.coffeePrice.setText(String.format(Locale.getDefault(),"%.2f TL",cartItem.getTotalPrice()));
            } if (cartItem.getSize().equals("Satın Alma")){
                holder.coffeeSize.setText(cartItem.getSize());
                double bookBuyPrice = cartItem.getTotalPrice()+cartItem.getBook().getBookBuy();
                double bookBuyPrice2 = cartItem.getBook().getBookBuy()*(cartItem.getQuantity()-1);
                if(cartItem.getQuantity() >1){
                    holder.coffeePrice.setText(String.format(Locale.getDefault(),"%.2f TL",(bookBuyPrice+bookBuyPrice2)));

                }else
                    holder.coffeePrice.setText(String.format(Locale.getDefault(),"%.2f TL",(bookBuyPrice)));
            } holder.coffeeExtras.setText("");

       }
        // Tatlılar için veri gösterimi
        else if (cartItem.getDessert() != null) {
            ModelDeserts dessert = cartItem.getDessert();
            holder.coffeeName.setText(dessert.getName());
            holder.coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", cartItem.getTotalPrice()));
            holder.textViewQuantity.setText(String.valueOf(cartItem.getQuantity()));
            holder.coffeeExtras.setText("");
            holder.coffeeSize.setText("");

            Glide.with(context)
                    .load(dessert.getImageUrl())
                    .placeholder(R.drawable.itc_logo)
                    .error(R.drawable.itc_logo2)
                    .into(holder.coffeeImage);


       }

        // + Butonuna tıklama işlemi
        holder.buttonIncrement.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            cartItem.setQuantity(++quantity); // Miktarı artır
            cartItem.updateTotalPrice(); // Fiyatı güncelle
            holder.textViewQuantity.setText(String.valueOf(quantity));
            calculateTotalPrice(); // Toplam fiyatı güncelle
            notifyDataSetChanged();
        });

        // - Butonuna tıklama işlemi
        holder.buttonDecrement.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                cartItem.setQuantity(--quantity); // Miktarı azalt
                cartItem.updateTotalPrice(); // Fiyatı güncelle
                holder.textViewQuantity.setText(String.valueOf(quantity));
                notifyDataSetChanged();
            } else {
                // Ürünü listeden çıkar
                cartItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItemList.size());
            }
            calculateTotalPrice(); // Toplam fiyatı güncelle
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    // Toplam fiyatı hesaplama
    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        for (ModelBasket item : BasketManager.getCartItems()) {
            if(item.getCoffee()!=null){
                List<String> sortedList = new ArrayList<>(item.getCoffee().getSelectedExtrasGetAll());
                Collections.sort(sortedList);
                for (String item1 : sortedList) {
                    if ("Espresso".equals(item1)){
                        totalPrice = totalPrice+ (item.getCoffee().getExtraExpressoPrice()*item.getQuantity());
                    } else if ("Milk".equals(item1)) {
                        totalPrice = totalPrice+ (item.getCoffee().getExtraMilkPrice()*item.getQuantity());
                    }else if ("Syrup".equals(item1)) {
                        totalPrice = totalPrice+ (item.getCoffee().getExtraSyrupPrice()*item.getQuantity());
                    }
                }
            }if(item.getNatural()!=null){
                if(item.getSize().equals("Medium")){
                    totalPrice = totalPrice+(item.getNatural().getMidPrice()*item.getQuantity());
                } else if (item.getSize().equals("Large")) {
                    totalPrice = totalPrice+(item.getNatural().getBigPrice()*item.getQuantity());
                }
            }if(item.getBook()!=null){
                if(item.getSize().equals("Satın Alma")){
                    totalPrice = totalPrice+(item.getBook().getBookBuy()*item.getQuantity());
                }
            }
            totalPrice += item.getTotalPrice();
        }
        notifyDataSetChanged();
        textViewTotalPrice.setText(String.format(Locale.getDefault(), "Toplam: %.2f TL", totalPrice));
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView coffeeImage;
        TextView coffeeName, coffeePrice;
        TextView coffeeSize, coffeeExtras;
        ImageView buttonIncrement, buttonDecrement;
        TextView textViewQuantity;

        CartViewHolder(View itemView) {
            super(itemView);
            coffeeImage = itemView.findViewById(R.id.imageViewCoffee);
            coffeeName = itemView.findViewById(R.id.textViewCoffeeName);
            coffeePrice = itemView.findViewById(R.id.textViewPrice);
            coffeeSize = itemView.findViewById(R.id.textViewSize);
            coffeeExtras = itemView.findViewById(R.id.textViewExtras);
            buttonIncrement = itemView.findViewById(R.id.buttonIncrement);
            buttonDecrement = itemView.findViewById(R.id.buttonDecrement);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        }
    }
    public void updateCartItems(List<ModelBasket> newCartItems) {
        this.cartItemList = newCartItems;
        notifyDataSetChanged();// Veriler güncellendiğinde görünümü yenile
        calculateTotalPrice(); // Veriler güncellendiğinde fiyati yenile
    }
}
