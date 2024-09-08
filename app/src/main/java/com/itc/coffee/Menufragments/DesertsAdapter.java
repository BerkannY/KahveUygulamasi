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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itc.coffee.Models.ModelDeserts;
import com.itc.coffee.Models.ModelBasket;
import com.itc.coffee.Mainfragments.BasketManager;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.Locale;

public class DesertsAdapter extends RecyclerView.Adapter<DesertsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ModelDeserts> desertsList;

    public DesertsAdapter(Context context, ArrayList<ModelDeserts> desertsList) {
        this.context = context;
        this.desertsList = desertsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deserts_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelDeserts modelDeserts = desertsList.get(position);
        holder.bindfunc(modelDeserts, context);
    }

    @Override
    public int getItemCount() {
        return desertsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView dessertImage;
        private TextView dessertName, dessertPrice;
        private Button buttonAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dessertImage = itemView.findViewById(R.id.imageViewCoffee); // ID'yi güncelleyin
            dessertName = itemView.findViewById(R.id.textViewCoffeeName); // ID'yi güncelleyin
            dessertPrice = itemView.findViewById(R.id.textViewPrice); // ID'yi güncelleyin
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }

        public void bindfunc(ModelDeserts modelDeserts, Context context) {
            dessertName.setText(modelDeserts.getName());
            double currentPrice = modelDeserts.getPrice();
            dessertPrice.setText(String.format(Locale.getDefault(), "%.2f TL", currentPrice));

            Glide.with(context)
                    .load(modelDeserts.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(dessertImage);

            buttonAddToCart.setOnClickListener(v -> addToCart(modelDeserts, context));
        }

        private void addToCart(ModelDeserts dessert, Context context) {
            // Tatlıyı sepete ekle
            try {
                BasketManager.addToCart(dessert, "", new ArrayList<>(), 1); // Boş boyut ve ekstralar
                Toast.makeText(context, "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Ürün sepete eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
