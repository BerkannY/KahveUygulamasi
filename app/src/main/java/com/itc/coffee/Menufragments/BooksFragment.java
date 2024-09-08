package com.itc.coffee.Menufragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.Models.ModelDeserts;
import com.itc.coffee.R;
import com.itc.coffee.databinding.FragmentBooksBinding;
import com.itc.coffee.databinding.FragmentDesertAndCakeBinding;

import java.util.ArrayList;
import java.util.Objects;


public class BooksFragment extends Fragment {
    private FragmentBooksBinding binding;
    BooksAdapter adapter;
    FirebaseFirestore firestore;
    ArrayList<ModelBooks> booksList;
    private TextView textViewPrice;
    private ModelCoffees currentCoffee;
    private DocumentSnapshot lastVisible;
    private boolean isLastPage = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();
        booksList = new ArrayList<>();
        adapter = new BooksAdapter(getActivity(),booksList);
        binding.recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);


        textViewPrice = view.findViewById(R.id.textViewPrice);

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
        booksList.clear();
        firestore.collection("books").orderBy("productId", Query.Direction.ASCENDING).limit(10).addSnapshotListener((value, error) -> {
            if(error != null)
                Toast.makeText(getActivity(), "document loaded", Toast.LENGTH_SHORT).show();
            for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                if(dc.getType()== DocumentChange.Type.ADDED){
                    booksList.add(dc.getDocument().toObject(ModelBooks.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void setupTextViewListeners(ModelCoffees coffee, TextView textViewSmall, TextView textViewMedium) {
        currentCoffee = coffee; // Seçili Coffee'yi güncelle

        textViewSmall.setOnClickListener(v -> {
            currentCoffee.selectSize("Small");
            updatePrice();
        });
        textViewMedium.setOnClickListener(v -> {
            currentCoffee.selectSize("Medium");
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
        Query initialQuery = firestore.collection("books")
                .orderBy("productId", Query.Direction.ASCENDING)
                .limit(10);

        initialQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        booksList.add(document.toObject(ModelBooks.class));
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

        Query nextQuery = firestore.collection("books")
                .orderBy("productId", Query.Direction.ASCENDING)
                .startAfter(lastVisible)
                .limit(10);

        nextQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        booksList.add(document.toObject(ModelBooks.class));
                    }
                    if (snapshot.size() > 0) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    } else {
                        isLastPage = true; // Eğer veri kalmadıysa, son sayfayı işaretle
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