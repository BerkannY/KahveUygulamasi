package com.itc.coffee.Menufragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelBasket;
import com.itc.coffee.Mainfragments.BasketManager;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.Locale;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ModelBooks> booksList;

    public BooksAdapter(Context context, ArrayList<ModelBooks> booksList) {
        this.context = context;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.books_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
        ModelBooks modelBooks = booksList.get(position);
        holder.bindfunc(modelBooks, context);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookImage;
        private TextView bookName, bookPrice, sizeSmall, sizeMedium;
        private Button buttonAddToCart;
        private double basePrice;
        private double currentPrice;
        private String selectedSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.imageViewCoffee);
            bookName = itemView.findViewById(R.id.textViewCoffeeName);
            bookPrice = itemView.findViewById(R.id.textViewPrice);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
            sizeSmall = itemView.findViewById(R.id.textViewSmall);
            sizeMedium = itemView.findViewById(R.id.textViewMedium);

            buttonAddToCart.setOnClickListener(v -> addToCart());
        }

        public void bindfunc(ModelBooks modelBooks, Context context) {
            basePrice = modelBooks.getPrice(); // Temel fiyatı sakla
            selectedSize = "Small"; // Varsayılan olarak "Small" boyutunu seç
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            bookName.setText(modelBooks.getName());
            currentPrice = basePrice; // Başlangıçta temel fiyatı kullan
            bookPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));

            Glide.with(context)
                    .load(modelBooks.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(bookImage);

            double bookPriceChange = modelBooks.getBookBuy();
            sizeSmall.setOnClickListener(v -> toggleSizeSelection(sizeSmall, "Günlük Kirala", 0, bookPriceChange));
            sizeMedium.setOnClickListener(v -> toggleSizeSelection(sizeMedium, "Satın Alma", bookPriceChange, bookPriceChange));
        }

        private void toggleSizeSelection(TextView sizeView, String size, double priceChange, double bookPriceChange) {
            if (!size.equals(selectedSize)) {
                updateSizeBackground(selectedSize, android.R.color.darker_gray);
                currentPrice = getUpdatedPrice(selectedSize, bookPriceChange);
                sizeView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
                currentPrice += priceChange;
                selectedSize = size;
                bookPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
            }
        }

        private double getUpdatedPrice(String size, double bookPriceChange) {
            switch (size) {
                case "Small":
                    return basePrice; // Küçük boyut için temel fiyatı döndür
                case "Medium":
                    return basePrice + bookPriceChange; // Orta boyut için güncellenmiş fiyatı döndür
                default:
                    return basePrice;
            }
        }

        private void updateSizeBackground(String size, int color) {
            switch (size) {
                case "Small":
                    sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
                case "Medium":
                    sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
            }
        }

        private void addToCart() {
            if (selectedSize != null) {
                ModelBooks selectedBook = booksList.get(getAdapterPosition());
                BasketManager.addToCart(selectedBook, selectedSize, new ArrayList<>(), 1);
                Toast.makeText(context, "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
                resetSelections();
            } else {
                Toast.makeText(context, "Lütfen bir satın alma yada kiralama  seçin!", Toast.LENGTH_SHORT).show();
            }
        }

        private void resetSelections() {
            selectedSize = "Small";
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            currentPrice = basePrice; // Temel fiyatı geri al
            bookPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
        }
    }
}
