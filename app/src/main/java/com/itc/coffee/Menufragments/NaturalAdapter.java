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
import com.itc.coffee.Mainfragments.BasketManager;
import com.itc.coffee.Models.ModelNatural;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NaturalAdapter extends RecyclerView.Adapter<NaturalAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ModelNatural> naturalList;

    public NaturalAdapter(Context context, ArrayList<ModelNatural> naturalList) {
        this.context = context;
        this.naturalList = naturalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.naturals_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelNatural modelNatural = naturalList.get(position);
        holder.bindfunc(modelNatural, context);
    }

    @Override
    public int getItemCount() {
        return naturalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView naturalImage;
        private TextView naturalName, naturalPrice, sizeSmall, sizeMedium, sizeLarge;
        private Button addToCartButton;
        private String selectedSize;
        private double initialPrice;
        private double currentPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            naturalImage = itemView.findViewById(R.id.imageViewCoffee);
            naturalName = itemView.findViewById(R.id.textViewCoffeeName);
            naturalPrice = itemView.findViewById(R.id.textViewPrice);
            sizeSmall = itemView.findViewById(R.id.textViewSmall);
            sizeMedium = itemView.findViewById(R.id.textViewMedium);
            sizeLarge = itemView.findViewById(R.id.textViewLarge);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
        }

        public void bindfunc(ModelNatural modelNatural, Context context) {
            selectedSize = "Small";
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            sizeLarge.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            naturalName.setText(modelNatural.getName());
            initialPrice = modelNatural.getPrice();
            currentPrice = initialPrice;
            naturalPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));

            Glide.with(context)
                    .load(modelNatural.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(naturalImage);

            double midPrice = modelNatural.getMidPrice();
            double bigPrice = modelNatural.getBigPrice();

            sizeSmall.setOnClickListener(v -> toggleSizeSelection(sizeSmall, "Small", 0, midPrice, bigPrice));
            sizeMedium.setOnClickListener(v -> toggleSizeSelection(sizeMedium, "Medium", midPrice, midPrice, bigPrice));
            sizeLarge.setOnClickListener(v -> toggleSizeSelection(sizeLarge, "Large", bigPrice, midPrice, bigPrice));

            addToCartButton.setOnClickListener(v -> addToCart(modelNatural, context));
        }

        private void toggleSizeSelection(TextView sizeView, String size, double priceChange, double midPrice, double bigPrice) {
            if (!size.equals(selectedSize)) {
                updateSizeBackground(selectedSize, android.R.color.darker_gray);
                currentPrice -= getSizePrice(selectedSize, midPrice, bigPrice);
                sizeView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
                currentPrice += priceChange;
                selectedSize = size;
                naturalPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
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
                case "Large":
                    sizeLarge.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), color));
                    break;
            }
        }

        private double getSizePrice(String size, double midPrice, double bigPrice) {
            switch (size) {
                case "Small":
                    return 0.0;
                case "Medium":
                    return midPrice;
                case "Large":
                    return bigPrice;
                default:
                    return 0.0;
            }
        }

        private void addToCart(ModelNatural natural, Context context) {
            if (selectedSize != null) {
                // Bu kısımda BasketManager sınıfının genişletilmiş versiyonunu kullanıyoruz
                BasketManager.addToCart(natural, selectedSize, new ArrayList<>(), 1);
                Toast.makeText(context, "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
                resetSelections(natural);
            } else {
                Toast.makeText(context, "Lütfen bir boyut seçin", Toast.LENGTH_SHORT).show();
            }
        }

        private void resetSelections(ModelNatural natural) {
            selectedSize = "Small";
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            sizeLarge.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            currentPrice = natural.getPrice();
            naturalPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
        }
    }
}
