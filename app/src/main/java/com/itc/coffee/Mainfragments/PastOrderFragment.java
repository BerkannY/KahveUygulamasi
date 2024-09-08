package com.itc.coffee.Mainfragments;

import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Menufragments.CoffeesAdapter;
import com.itc.coffee.Menufragments.DesertsAdapter;
import com.itc.coffee.Models.ModelBooks;
import com.itc.coffee.Models.ModelCoffees;
import com.itc.coffee.Models.ModelNatural;
import com.itc.coffee.Models.ModelOrder;
import com.itc.coffee.databinding.FragmentPastOrderBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PastOrderFragment extends Fragment {
    FragmentPastOrderBinding binding;
    FirebaseFirestore db;
    public FirebaseAuth auth;
    private PastOrderAdapter adapter;
    private ArrayList<ModelOrder> orderList;
    private DocumentSnapshot lastVisible;
    private boolean isLastPage = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPastOrderBinding.inflate(inflater, container, false);
        View view = binding.getRoot();



        db = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        adapter = new PastOrderAdapter(orderList, getActivity());
        binding.recyclerView.setHasFixedSize(true);
        binding.orderStatustext.setVisibility(View.INVISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);

//        LoadingDataFunc();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        loadInitialData(uid);

        binding.loadMoreButton.setOnClickListener(v -> {
            if (!isLastPage) {
                loadMoreData(uid);
            }
        });

        return  view;
    }



    private void LoadingDataFunc() {
        orderList.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("Orders")
                    .whereEqualTo("customerUID", uid)
                    .orderBy("dateReverse", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            binding.orderStatustext.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    orderList.add(dc.getDocument().toObject(ModelOrder.class));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadInitialData(String uid) {
        Query initialQuery = db.collection("Orders")
                .whereEqualTo("customerUID", uid)
                .orderBy("dateReverse", Query.Direction.DESCENDING)
                .limit(8);

        initialQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        orderList.add(document.toObject(ModelOrder.class));
                    }
                    if (snapshot.size() > 0) {
                        lastVisible = snapshot.getDocuments().get(snapshot.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    binding.orderStatustext.setVisibility(View.VISIBLE);
                }
            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
                System.out.println(task.getException().getMessage());
            }
        });
    }
    private void loadMoreData(String uid) {
        if (lastVisible == null) {
            return; // Eğer son belge yoksa, daha fazla veri yükleyemeyiz
        }

        Query nextQuery = db.collection("Orders").whereEqualTo("customerUID", uid)
                .orderBy("dateReverse", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(8);

        nextQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null && !snapshot.isEmpty()) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        orderList.add(document.toObject(ModelOrder.class));
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