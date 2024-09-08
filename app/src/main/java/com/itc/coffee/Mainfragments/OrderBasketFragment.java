package com.itc.coffee.Mainfragments;

import static com.itc.coffee.Mainfragments.BasketManager.cartItems;
import static com.itc.coffee.Mainfragments.BasketManager.updateCartItemPrices;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Models.ModelBasket;
import com.itc.coffee.Models.ModelFirebaseBasketOrder;
import com.itc.coffee.R;
import com.itc.coffee.databinding.FragmentOrderBasketBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class OrderBasketFragment extends Fragment {
    private FragmentOrderBasketBinding binding;
    private TextView totalPriceTextView;
    BasketManager cartItemList;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    String ProductType;
    String ProductName;
    String ProductSize;
    int ProductQuantity;
    double ProducttotalPrice;
    boolean ProductExtraSyrup = false;
    boolean ProductExtraMilk = false;
    boolean ProductExtraExpresso = false;
    String imageUrl ;
    private ProgressDialog mProgress;
    String qrCode;
    BasketAdapter adapter;
    ModelFirebaseBasketOrder modelFirebaseBasketOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBasketBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();
        assert currentUser != null;


        // Toplam fiyat TextView'ını bul
        totalPriceTextView = view.findViewById(R.id.textViewTotalPrice);
        binding.buttonEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyCart();
            }
        });

        // RecyclerView ile sepetteki ürünleri göster
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Sepet verilerini al ve RecyclerView'a ata, toplam fiyatı güncelle
        adapter = new BasketAdapter(BasketManager.getCartItems(), getActivity(), totalPriceTextView);
        recyclerView.setAdapter(adapter);

        binding.buttonPlaceOrder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress = new ProgressDialog(getActivity());
                mProgress.setTitle("QR oluşturuluyor");
                mProgress.setMessage("Sözlük Kafe");
                mProgress.show();
                saveDataToFirebase(false);
                directionFunc();
                //                            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_menu_id);
                //                            bottomNavigationView.setSelectedItemId(R.id.qrpage_id);
                progressSet();
            }
        });


        binding.buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TotalPriceString = binding.textViewTotalPrice.getText().toString().split(" ")[1];
                System.out.println(TotalPriceString);
                if(!TotalPriceString.equals("0,00"))
                    binding.secondLayout.setVisibility(View.VISIBLE);
                else Toast.makeText(requireContext(), "Şuan sepetiniz BOŞ gözüküyor...", Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonPlaceOrder3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.secondLayout.setVisibility(View.INVISIBLE);
            }
        });

        binding.buttonPlaceOrder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress = new ProgressDialog(getActivity());
                mProgress.setTitle("Ödeme alınıyor");
                mProgress.setMessage("Sözlük Kafe");
                mProgress.show();
                if (auth.getCurrentUser() != null) {
                    String userId = auth.getCurrentUser().getUid();
                    db.collection("users")
                            .whereEqualTo("userUID", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                                                String documentId = document.getId();
                                                System.out.println(documentId + " dokumnat ıd");
                                                Map<String, Object> data = document.getData();

                                                db.collection("users")
                                                        .document(documentId).get().addOnCompleteListener(taskk -> {
                                                            if (taskk.isSuccessful()) {

                                                                DocumentSnapshot documentt = taskk.getResult();
                                                                if (documentt.exists()) {

                                                                    Map<String, Object> cardMap = (Map<String, Object>) documentt.getData();
                                                                    if (cardMap != null) {
                                                                        Map<String, Object> cardData = (Map<String, Object>) cardMap.get("customercard");
                                                                        String balance_string = String.valueOf(cardData.get("balance"));
                                                                        double balance = Double.parseDouble(balance_string);
                                                                        double lastPriceTotal = updateTotalPrice();
                                                                        if (balance >= lastPriceTotal) {
                                                                            saveDataToFirebase(true);
                                                                            progressSet();
                                                                            Toast.makeText(requireContext(), "Ödeme başarıyla tamamlandı.QR ile kasadan siparişinizi teslim alın...", Toast.LENGTH_LONG).show();
                                                                            directionFunc();
                                                                        } else {
                                                                            Toast.makeText(requireContext(), "Bakiye yetersiz...", Toast.LENGTH_SHORT).show();
                                                                            progressSet();
                                                                        }
                                                                    } else {
                                                                        Log.d("KartEkrani", "Kart field is null.");
                                                                    }
                                                                } else {
                                                                    System.out.println("KART: 5");
                                                                    Log.d("KartEkrani", "Document not found.");
                                                                }
                                                            } else {
                                                                System.out.println("KART: 6");
                                                                Log.e("KartEkrani", "Fetch failed: " + taskk.getException().getMessage());
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("Firestore", "No matching documents found.");
                                        }
                                    } else {
                                        Log.e("Firestore", "Fetch failed: " + task.getException().getMessage());
                                    }
                                }
                            });
                } else{
                    Log.e("Auth", "No current user is signed in.");
                    System.out.println("ilgili kullanıcı bulunamdı....");
                }
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Aktivite geri geldiğinde toplam fiyatı yeniden güncelle
        BasketManager.updateCartItemPrices(); // Sepetteki fiyatları güncelle
        updateTotalPrice();
    }


    private double updateTotalPrice() {
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
        adapter.notifyDataSetChanged();
        totalPriceTextView.setText(String.format(Locale.getDefault(), "Toplam: %.2f TL", totalPrice));
        adapter.notifyDataSetChanged();
        return totalPrice;

    }
    private void progressSet ( ){
        if (mProgress.isShowing()){
            mProgress.dismiss();
        }
    }
    private double generateUniqueQRCode() {
        Random random = new Random();
        int qrCode = 100000 + random.nextInt(900000);


        checkQRCodeInDatabase(qrCode, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isUnique) {
                if (isUnique) {
                    // QR kodu benzersiz, kullanabilirsiniz
                    System.out.println("benzersiz kod basariyla uretildi...");
                } else {
                    // QR kodu çakıştı, yeni bir sayı üret
                    generateUniqueQRCode();
                }
            }
        });
        return qrCode;
    }
    private void checkQRCodeInDatabase(final int qrCode, final OnSuccessListener<Boolean> callback) {
        db.collection("Orders")
                .whereEqualTo("qrCode", qrCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // QR kodu benzersiz
                                callback.onSuccess(true);
                            } else {
                                // QR kodu veritabanında mevcut
                                callback.onSuccess(false);
                            }
                        } else {
                            // Hata durumunda uygun işlemi yapabilirsiniz
                            System.err.println("Hata: " + task.getException().getMessage());
                            Toast.makeText(getActivity(), "Veri eklenirken hata oluştu:2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void directionFunc(){
        Bundle bundle = new Bundle();
        bundle.putString("awardStatus", "yes");
        bundle.putString("qr", qrCode);

        QrFragment qrFragment = new QrFragment();
        qrFragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.menu_frame_id, qrFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void saveDataToFirebase(boolean cardPayment){
        List<ModelBasket> cartItems = BasketManager.cartItems;
        double qrCode_double = generateUniqueQRCode();
        int qrcode_int = (int) qrCode_double;
        qrCode = String.valueOf(qrcode_int);
        String customerUID = currentUser.getUid();
        for (ModelBasket basket : cartItems) {
            CollectionReference ordersRef = db.collection("Orders");
            Query query = ordersRef.whereEqualTo("customerUID", customerUID).whereEqualTo("orderStatus", false);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean islem = false;
                        for (DocumentSnapshot document : task.getResult()) {
                            islem=true;
                            qrCode = document.getString("qrCode");
                            System.out.println("QR Code degeri: " + qrCode);
                            break;
                        }
                        if(!islem){
                            currentUser = auth.getCurrentUser();
                            String customerUID = currentUser.getUid();
                            if (basket.getCoffee() != null) {
                                ProductType = "coffee";
                                ProductName = basket.getCoffee().getName();
                                ProductSize = basket.getSize();
                                ProducttotalPrice = basket.getQuantity()*basket.getCoffee().getPrice();
                                if(ProductSize.equals("Medium"))
                                    ProducttotalPrice +=basket.getCoffee().getMidPrice()*basket.getQuantity();
                                else if(ProductSize.equals("Large"))
                                    ProducttotalPrice +=basket.getCoffee().getBigPrice()*basket.getQuantity();
                                ProductQuantity = basket.getQuantity();
                                imageUrl = basket.getCoffee().getImageUrl();
                                ProductExtraSyrup = false;
                                ProductExtraMilk = false;
                                ProductExtraExpresso = false;
                                List<String> sortedList = new ArrayList<>(basket.getCoffee().getSelectedExtrasGetAll());
                                Collections.sort(sortedList);
                                for (String item : sortedList) {
                                    if ("Espresso".equals(item)){
                                        ProductExtraExpresso = true;
                                        ProducttotalPrice += (basket.getCoffee().getExtraExpressoPrice()*basket.getQuantity());
                                    } else if ("Milk".equals(item)) {
                                        ProductExtraMilk = true;
                                        ProducttotalPrice += (basket.getCoffee().getExtraMilkPrice()*basket.getQuantity());
                                    }else if ("Syrup".equals(item)) {
                                        ProductExtraSyrup = true;
                                        ProducttotalPrice += (basket.getCoffee().getExtraSyrupPrice()*basket.getQuantity());
                                    }
                                }
                            } else if (basket.getNatural() != null) {
                                ProductType = "natural";
                                ProductName = basket.getNatural().getName();
                                ProductSize = basket.getSize();
                                imageUrl = basket.getNatural().getImageUrl();
                                ProductQuantity = basket.getQuantity();
                                ProductExtraSyrup = false;
                                ProductExtraMilk = false;
                                ProductExtraExpresso = false;
                                if(basket.getSize().equals("Medium"))
                                    ProducttotalPrice = (basket.getNatural().getMidPrice()*basket.getQuantity())+basket.getTotalPrice();
                                else if (basket.getSize().equals("Large")) {
                                    ProducttotalPrice = (basket.getNatural().getBigPrice()*basket.getQuantity())+basket.getTotalPrice();
                                }else{
                                    ProducttotalPrice = basket.getTotalPrice();
                                }
                            } else if (basket.getBook() != null) {
                                ProductType = "book";
                                ProductName = basket.getBook().getName();
                                ProductExtraSyrup = false;
                                ProductExtraMilk = false;
                                ProductExtraExpresso = false;
                                if (basket.getSize().equals("Small")) {
                                    ProductSize = "Günlük Kirala";
                                    ProducttotalPrice = basket.getTotalPrice();
                                } else {
                                    ProductSize = "Satın Alma";
                                    ProducttotalPrice = basket.getTotalPrice()+(basket.getBook().getBookBuy()*(basket.getQuantity()));
                                }
                                ProductQuantity = basket.getQuantity();
                                imageUrl = basket.getBook().getImageUrl();
                            } else if (basket.getDessert() != null) {
                                ProductType = "desert";
                                ProductName = basket.getDessert().getName();
                                ProductSize = "";
                                ProductQuantity = basket.getQuantity();
                                ProducttotalPrice = basket.getTotalPrice();
                                imageUrl = basket.getDessert().getImageUrl();
                                ProductExtraSyrup = false;
                                ProductExtraMilk = false;
                                ProductExtraExpresso = false;
                            }
//                            System.out.println("3) "+basket.getCoffee().toString());
                            Date presentTime = new Date();
                            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            String[] dateformat = df.format(presentTime).split("/");
                            String dateReverse = dateformat[0]+dateformat[1]+dateformat[2];
                            String date = dateformat[2]+"."+dateformat[1]+"."+dateformat[0];
                            SimpleDateFormat format=new SimpleDateFormat("HH:mm", Locale.getDefault());
                            String time = format.format(new Date().getTime());
                            double lastPriceTotal = updateTotalPrice();
                            modelFirebaseBasketOrder = new ModelFirebaseBasketOrder(customerUID, ProductExtraSyrup, ProductExtraMilk, ProductExtraExpresso, dateReverse, date, lastPriceTotal, false, ProducttotalPrice, ProductQuantity, imageUrl, ProductName, ProductSize, time, qrCode, ProductType,cardPayment);
                            System.out.println("ordermodelden dondu");
                            db.collection("Orders")
                                    .add(modelFirebaseBasketOrder)
                                    .addOnSuccessListener(documentReference -> {
                                        System.out.println("Veri başarıyla Orders tablosuna eklendi: " + documentReference.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        System.out.println("Veri eklenirken hata oluştu: " + e.getMessage());
                                        Toast.makeText(getActivity(), "Veri eklenirken hata oluştu:1", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        System.out.println("orders tablosu veri cekmede hata yasandi");
                        Toast.makeText(getActivity(), "Order hatası", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void emptyCart(){
        BasketManager.clearCart();
        updateTotalPrice();
        adapter.notifyDataSetChanged();
    }




}
