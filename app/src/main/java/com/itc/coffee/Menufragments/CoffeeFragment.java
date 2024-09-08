package com.itc.coffee.Menufragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.R;
import com.itc.coffee.databinding.FragmentCoffeeBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CoffeeFragment extends Fragment {
    private FragmentCoffeeBinding binding;
    private CoffeesAdapter adapter;
    private FirebaseFirestore firestore;
    private TextView textViewPrice;
    private ArrayList<ModelCoffees> coffeeList;
    private ModelCoffees currentCoffee;
    private DocumentSnapshot lastVisible;
    private boolean isLastPage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoffeeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        textViewPrice = view.findViewById(R.id.textViewPrice);

        firestore = FirebaseFirestore.getInstance();
        coffeeList = new ArrayList<>();
        adapter = new CoffeesAdapter(getActivity(),coffeeList);
        binding.recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

//        LoadingDataFunc();
        loadInitialData();

        // Daha fazla veri yükleme butonunu ayarlayın
        binding.loadMoreButton.setOnClickListener(v -> {
            if (!isLastPage) {
                loadMoreData();
            }
        });


        return view;
    }
    private void LoadingDataFunc() {
        coffeeList.clear();
        firestore.collection("coffees").orderBy("productId", Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {
            if(error != null)
                Toast.makeText(getActivity(), "document loaded", Toast.LENGTH_SHORT).show();
            for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                if(dc.getType()== DocumentChange.Type.ADDED){
                    coffeeList.add(dc.getDocument().toObject(ModelCoffees.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void setupTextViewListeners(ModelCoffees coffee, TextView textViewSmall, TextView textViewMedium, TextView textViewLarge,
                                       TextView textViewExtraMilk, TextView textViewExtraSyrup, TextView textViewExtraEspresso) {
        currentCoffee = coffee; // Seçili Coffee'yi güncelle

        textViewSmall.setOnClickListener(v -> {
            currentCoffee.selectSize("Small");
            updatePrice();
        });
        textViewMedium.setOnClickListener(v -> {
            currentCoffee.selectSize("Medium");
            updatePrice();
        });
        textViewLarge.setOnClickListener(v -> {
            currentCoffee.selectSize("Large");
            updatePrice();
        });
        textViewExtraMilk.setOnClickListener(v -> {
            currentCoffee.toggleExtra("Milk");
            updatePrice();
        });
        textViewExtraSyrup.setOnClickListener(v -> {
            currentCoffee.toggleExtra("Syrup");
            updatePrice();
        });
        textViewExtraEspresso.setOnClickListener(v -> {
            currentCoffee.toggleExtra("Espresso");
            updatePrice();
        });
    }

    private void updatePrice() {
        if (currentCoffee != null) {
            double totalPrice = currentCoffee.calculateTotalPrice();
             textViewPrice.setText(String.format("Total Price: %.2f TL", totalPrice)); // Fiyatı TextView'de göster
        }
    }




    private void loadInitialData() {
        Query initialQuery = firestore.collection("coffees")
                .orderBy("productId", Query.Direction.ASCENDING)
                .limit(6);

        initialQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        coffeeList.add(document.toObject(ModelCoffees.class));
                    }
                    if (snapshot.size() > 0) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMoreData() {
        if (lastVisible == null) {
            return; // Eğer son belge yoksa, daha fazla veri yükleyemeyiz
        }

        Query nextQuery = firestore.collection("coffees")
                .orderBy("productId", Query.Direction.ASCENDING)
                .startAfter(lastVisible)
                .limit(6);

        nextQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        coffeeList.add(document.toObject(ModelCoffees.class));
                    }
                    if (snapshot.size() > 0) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    } else {
                        isLastPage = true; // Eğer veri kalmadıysa, son sayfayı işaretle
                        Toast.makeText(requireContext(), "Tüm içerikleri gördünüz...", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), "Error loading more data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
