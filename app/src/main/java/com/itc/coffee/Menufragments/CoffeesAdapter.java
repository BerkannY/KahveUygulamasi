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
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CoffeesAdapter extends RecyclerView.Adapter<CoffeesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ModelCoffees> coffeesList;

    public CoffeesAdapter(Context context, ArrayList<ModelCoffees> coffeesList) {
        this.context = context;
        this.coffeesList = coffeesList;
    }

    @NonNull
    @Override
    public CoffeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coffees_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeesAdapter.ViewHolder holder, int position) {
        ModelCoffees modelCoffees = coffeesList.get(position);
        holder.bindfunc(modelCoffees, context);
    }

    @Override
    public int getItemCount() {
        return coffeesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView coffeeImage;
        private TextView coffeeName, coffeePrice, sizeSmall, sizeMedium, sizeLarge, extraMilk, extraSyrup, extraEspresso;
        private Button addToCartButton;
        private String selectedSize;
        private List<String> selectedExtras;
        private double initialPrice;
        private double currentPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coffeeImage = itemView.findViewById(R.id.imageViewCoffee);
            coffeeName = itemView.findViewById(R.id.textViewCoffeeName);
            coffeePrice = itemView.findViewById(R.id.textViewPrice);
            sizeSmall = itemView.findViewById(R.id.textViewSmall);
            sizeMedium = itemView.findViewById(R.id.textViewMedium);
            sizeLarge = itemView.findViewById(R.id.textViewLarge);
            extraMilk = itemView.findViewById(R.id.textViewExtraMilk);
            extraSyrup = itemView.findViewById(R.id.textViewExtraSyrup);
            extraEspresso = itemView.findViewById(R.id.textViewExtraEspresso);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
            selectedExtras = new ArrayList<>();
        }

        public void bindfunc(ModelCoffees modelCoffees, Context context) {
            // Başlangıçta Small seçeneğini seç
            selectedSize = "Small";
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            sizeLarge.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            coffeeName.setText(modelCoffees.getName());
            initialPrice = modelCoffees.getPrice();
            currentPrice = initialPrice;
            coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
            Glide.with(context)
                    .load(modelCoffees.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(coffeeImage);


            if (modelCoffees.getExtraSyrupPrice()==0.00){
                extraSyrup.setVisibility(View.INVISIBLE);
            }if (modelCoffees.getExtraExpressoPrice() ==0.00) {
                extraEspresso.setVisibility(View.INVISIBLE);
            }

            double midPrice = modelCoffees.getMidPrice();
            double bigPrice = modelCoffees.getBigPrice();
            double extraSyrupPrice = modelCoffees.getExtraSyrupPrice();
            double extraMilkPrice = modelCoffees.getExtraMilkPrice();
            double extraExpressoPrice = modelCoffees.getExtraExpressoPrice();
//            if (extraExpressoPrice ==0)
//                extraMilk.setVisibility(View.GONE);
//            else if (extraMilkPrice==0) {
//                extraEspresso.setVisibility( View.GONE);
//            } else if (extraSyrupPrice==0) {
//                extraSyrup.setVisibility(View.GONE);
//            }


            sizeSmall.setOnClickListener(v -> toggleSizeSelection(sizeSmall, "Small", 0, midPrice, bigPrice));
            sizeMedium.setOnClickListener(v -> toggleSizeSelection(sizeMedium, "Medium", midPrice, midPrice, bigPrice));
            sizeLarge.setOnClickListener(v -> toggleSizeSelection(sizeLarge, "Large", bigPrice, midPrice, bigPrice));
            extraMilk.setOnClickListener(v -> toggleExtraSelection(extraMilk, "Milk", extraMilkPrice));
            extraSyrup.setOnClickListener(v -> toggleExtraSelection(extraSyrup, "Syrup", extraSyrupPrice));
            extraEspresso.setOnClickListener(v -> toggleExtraSelection(extraEspresso, "Espresso", extraExpressoPrice));

            addToCartButton.setOnClickListener(v -> addToCart(modelCoffees, context));
        }

        private void toggleSizeSelection(TextView sizeView, String size, double priceChange, double midPrice, double bigPrice) {
            if (!size.equals(selectedSize)) {
                updateSizeBackground(selectedSize, android.R.color.darker_gray);
                // Küçük boyut seçildiğinde ek fiyat eklenmeyecek
                if (selectedSize.equals("Small")) {
                    currentPrice = initialPrice; // Küçük boyut seçilirse temel fiyatı geri yükle
                } else {
                    currentPrice -= getSizePrice(selectedSize, midPrice, bigPrice);
                }
                currentPrice += priceChange;
                sizeView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
                selectedSize = size;
                coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
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

        private void toggleExtraSelection(TextView extraView, String extraName, double priceChange) {

            if (selectedExtras.contains(extraName)) {
                extraView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
                selectedExtras.remove(extraName);
                currentPrice -= priceChange;  // Ekstra kaldırıldığında fiyatı düşür
            } else {
                extraView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
                selectedExtras.add(extraName);
                currentPrice += priceChange;  // Ekstra eklendiğinde fiyatı artır
            }
            coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
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

        private void addToCart(ModelCoffees coffee, Context context) {
            BasketManager.addToCart(coffee, selectedSize, selectedExtras, 1);
            Toast.makeText(context, "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
            resetSelections(coffee);
        }

        private void resetSelections(ModelCoffees coffee) {
            // Seçimleri sıfırla ama Small seçeneğini seçili bırak
            selectedSize = "Small";
            sizeSmall.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.icogreen));
            sizeMedium.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            sizeLarge.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            HashSet<String> myset = new HashSet<>();
            for (int i=0;i<selectedExtras.size();i++) {
                myset.add(selectedExtras.get(i));
            }
            coffee.setSelectedExtrasGetAll(myset);
            // Ekstraları sıfırla
            selectedExtras.clear();
            extraMilk.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            extraSyrup.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));
            extraEspresso.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray));

            // Fiyatı başlangıç fiyatına döndür
            currentPrice = coffee.getPrice();
            coffeePrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));
        }
    }
}
