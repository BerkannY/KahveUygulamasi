package com.itc.coffee.Mainfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.Models.ModelOrder;
import com.itc.coffee.R;

import java.util.ArrayList;
import java.util.List;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.OrderViewHolder>{
    private List<ModelOrder> orderList;
    Context context;

    public PastOrderAdapter(List<ModelOrder> orderList,Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public PastOrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pastorder, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderAdapter.OrderViewHolder holder, int position) {
        ModelOrder modelOrder = orderList.get(position);
        holder.bindfunc(modelOrder, context);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView coffeeImage;
        private TextView textViewStatus,textViewCount,coffeeName, coffeePrice,textViewSize,textViewExtra1, textViewExtra2, textViewExtra3,textViewtime,textViewDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            coffeeImage = itemView.findViewById(R.id.imageViewCoffee);
            coffeeName = itemView.findViewById(R.id.textViewCoffeeName);
            coffeePrice = itemView.findViewById(R.id.textViewPrice);
            textViewSize = itemView.findViewById(R.id.textViewSize);
            textViewExtra1 = itemView.findViewById(R.id.textViewExtra1);
            textViewExtra2 = itemView.findViewById(R.id.textViewExtra2);
            textViewExtra3 = itemView.findViewById(R.id.textViewExtra3);
            textViewtime = itemView.findViewById(R.id.textViewtime);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewCount = itemView.findViewById((R.id.textViewCount));
            textViewStatus = itemView.findViewById(R.id.textViewStatus);

        }

        public void bindfunc(ModelOrder modelOrder, Context context) {
            coffeeName.setText(modelOrder.getProductName());
            coffeePrice.setText("Tutar: "+String.valueOf(modelOrder.getLastPriceTotal())+"₺");
            textViewSize.setText(modelOrder.getProductSize());
            textViewtime.setText("Saat: " +modelOrder.getTime());
            textViewDate.setText("Tarih: " +modelOrder.getDate());
            int value = (int)modelOrder.getProductCounter();
            textViewCount.setText("Adet: "+String.valueOf(value));



            Glide.with(context)
                    .load(modelOrder.getProductImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(coffeeImage);

            if (modelOrder.isExtraExpresso()){
                textViewExtra3.setVisibility(View.VISIBLE);
                textViewExtra3.setText("Expresso");
            }else {
                textViewExtra3.setVisibility(View.GONE);
            }
            if (modelOrder.isExtraMilk()){
                textViewExtra1.setVisibility(View.VISIBLE);
                textViewExtra1.setText("Süt");
            }else {
                textViewExtra1.setVisibility(View.GONE);
            }
            if (modelOrder.isExtraSyrup()){
                textViewExtra2.setVisibility(View.VISIBLE);
                textViewExtra2.setText("Şurup");
            }else {
                textViewExtra2.setVisibility(View.GONE);
            }
            if (modelOrder.isOrderStatus()){
                textViewStatus.setVisibility(View.VISIBLE);
                textViewStatus.setText("Tamamlandı");
            }
            else {
                textViewStatus.setText("Sipariş Bekleniyor");
            }
        }
    }
}
