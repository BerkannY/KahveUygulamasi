package com.itc.coffee.Mainfragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Models.AwardOrder;
import com.itc.coffee.Models.ModelCustomerCard;
import com.itc.coffee.NumberFormat.PhoneNumberFormattingTextWatcher;
import com.itc.coffee.R;
import com.itc.coffee.databinding.FragmentHomeBinding;
import com.itc.coffee.time.TimeApi;
import com.itc.coffee.time.TimeTurkey;
import com.itc.coffee.view.SplashActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    public FragmentHomeBinding binding;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private Retrofit retrofit;
    private String baseurl = "https://worldtimeapi.org/api/timezone/";
    private TimeApi timeApi;
    private Call<TimeTurkey> timeTurkeyCall;
    private TimeTurkey timeTurkey;
    FirebaseUser currentUser;
    private String name,phone,coffeeAward,mail;
    private String userEmail;
    AwardOrder order;
    private ProgressDialog mProgress;
    ModelCustomerCard modelCustomerCard;
    int bgselection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();
        assert currentUser != null;
        userEmail = currentUser.getEmail();

        binding.kphoneTextt.addTextChangedListener(new PhoneNumberFormattingTextWatcher(binding.kphoneTextt));



        db.collection("users")
                .whereEqualTo("mail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    name = document.getString("user");
                                    mail = document.getString("mail");
                                    coffeeAward = document.getString("coffeeAward");
                                    phone = document.getString("phone");
                                    String[] parts = name.split(" ");
                                    binding.homeLayoutUserId.setText(parts[0].substring(0,1).toUpperCase()+parts[0].substring(1).toLowerCase());

                                    binding.tvCardHolder.setText(name.toUpperCase());

                                    coffeeAwardfunc(coffeeAward);
                                    fetchUserData();

                                    binding.kuserTextt.setText(name.toUpperCase());
                                    binding.kmailTextt.setText(mail);
                                    binding.kphoneTextt.setText(phone);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Kullanıcı Bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Eşlemede hata", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        binding.homeFragmentOfferFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuFragment menuFragment = new MenuFragment();


                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.menu_frame_id, menuFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_menu_id);
                bottomNavigationView.setSelectedItemId(R.id.menupage_id);
            }
        });
        binding.homeFragmentAwardFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ödül gorseline tıklandı....
            }
        });

        //günün saatinin sunucudan alınıp vakit olarak ekrana yazılması işlemi
//        setRetrofitSettings(); retrofit işlem sınırı yüzünden hata veriyor...
        setTimeSetting();

        binding.coffeAwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress = new ProgressDialog(getActivity());
                mProgress.setTitle("QR oluşturuluyor");
                mProgress.setMessage("Sözlük Kafe");
                mProgress.show();


                String customerUID = currentUser.getUid();

                CollectionReference ordersRef = db.collection("Orders");
                Query query = ordersRef.whereEqualTo("customerUID", customerUID).whereEqualTo("orderStatus", false).whereEqualTo("priceTotal", 0);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean islem = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                islem=true;
                                String qrCode_db = document.getString("qrCode");
//                                Boolean qrStatus_db = document.getBoolean("qrCodeStatus");
                                System.out.println("QR Code degeri: " + qrCode_db);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("awardStatus", "yes");
                                    bundle.putString("qr", qrCode_db);

                                    QrFragment qrFragment = new QrFragment();
                                    qrFragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.menu_frame_id, qrFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

//                            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_menu_id);
//                            bottomNavigationView.setSelectedItemId(R.id.qrpage_id);
                                    progressSet();
                                    break;
                            }
                            if(!islem){
                                double qrCode_double = generateUniqueQRCode();
                                int qrcode_int = (int) qrCode_double;
                                String qrCode = String.valueOf(qrcode_int);
                                currentUser = auth.getCurrentUser();
                                String customerUID = currentUser.getUid();

                                Date presentTime = new Date();
                                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                                String[] dateformat = df.format(presentTime).split("/");
                                String dateReverse = dateformat[0]+dateformat[1]+dateformat[2];
                                String date = dateformat[2]+"."+dateformat[1]+"."+dateformat[0];
                                String productImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Caffe_Latte_cup.jpg/330px-Caffe_Latte_cup.jpg";
                                SimpleDateFormat format=new SimpleDateFormat("HH:mm", Locale.getDefault());
                                String time = format.format(new Date().getTime());
                                System.out.println("ilk kısma girdi");

                                order = new AwardOrder(customerUID, false, false, false, dateReverse, date, 0, false, 0, 1, productImage, "Kahve", "Small", time, qrCode,"coffee",true);
                                System.out.println("ordermodelden dondu");



                                db.collection("Orders")
                                        .add(order)
                                        .addOnSuccessListener(documentReference -> {
                                            System.out.println("Veri başarıyla Orders tablosuna eklendi: " + documentReference.getId());

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

    //                            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_menu_id);
    //                            bottomNavigationView.setSelectedItemId(R.id.qrpage_id);
                                            progressSet();
                                        })
                                        .addOnFailureListener(e -> {
                                            System.out.println("Veri eklenirken hata oluştu: " + e.getMessage());
                                            Toast.makeText(getActivity(), "Veri eklenirken hata oluştu:1", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            System.out.println("orders tablosu veri çekmede hata yaşandı");
                            Toast.makeText(getActivity(), "Order hatası", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
        binding.imageB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedImage;
                selectedImage=0;
                bgselection=selectedImage;
                binding.cardRelativeLayout.setBackgroundResource(R.color.icogreen);

            }
        });
        binding.imageB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedImage;
                selectedImage=1;
                bgselection=selectedImage;
                binding.cardRelativeLayout.setBackgroundResource(R.drawable.coffeecardbg1);
            }
        });
        binding.imageB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedImage;
                selectedImage=2;
                bgselection=selectedImage;
                binding.cardRelativeLayout.setBackgroundResource(R.drawable.coffeecardbg2);
            }
        });
        binding.settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.bgselection.setVisibility(View.VISIBLE);
            }
        });

        binding.bgselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bgselection <= 2) {
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
                                                    DocumentReference userRef = db.collection("users").document(documentId);
                                                    userRef.update("customercard.selectedImage", bgselection);
                                                }
                                            } else {
                                                Log.d("Firestore", "No matching documents found.");
                                            }
                                        } else {
                                            Log.e("Firestore", "Fetch failed: " + task.getException().getMessage());
                                        }
                                    }
                                });
                    }else{
                        Log.e("Auth", "No current user is signed in.");
                        System.out.println("ilgili kullanıcı bulunamdı....");
                    }


                }
                binding.bgselection.setVisibility(View.GONE);
            }
        });


        //profil ekranına geçiş işlemleri
        binding.homeFragmentSettingsId.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               binding.homeFirstLayout.setVisibility(View.INVISIBLE);
               binding.homeSecondlayout.setVisibility(View.VISIBLE);
           }
       });

        //parola değiştirme işlemi
        binding.homePwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.homeSecondlayout.setVisibility(View.INVISIBLE);
                binding.homeThirdlayout.setVisibility(View.VISIBLE);
            }
        });
        binding.homepwFragmentbackId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.homeSecondlayout.setVisibility(View.VISIBLE);
                binding.homeThirdlayout.setVisibility(View.INVISIBLE);
            }
        });

        binding.pwchangeBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),
                                        "Parola sıfırlama için mail gönderildi!", Toast.LENGTH_LONG).show();

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeFragment.this.getActivity(), SplashActivity.class));
                                HomeFragment.this.getActivity().finish();

                            } else {
                                Toast.makeText(getActivity(),
                                        "Sıfırlama işlemi başarısız oldu!", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        //oturumu kapat işlemi
        binding.logoutBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeFragment.this.getActivity(), "Çıkış Yapılıyor", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeFragment.this.getActivity(), SplashActivity.class));
                HomeFragment.this.getActivity().finish();
            }
        });

        //profil ekranından anasayfaya donus islemi
        binding.homeFragmentbackId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.homeFirstLayout.setVisibility(View.VISIBLE);
                binding.homeSecondlayout.setVisibility(View.INVISIBLE);
            }
        });

        //hesap bilgileri guncelleme islemi
        binding.updateBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(userEmail);
            }
        });



        return  view;
    }


    private void coffeeAwardfunc(String coffeeAward) {
        switch (coffeeAward){

            case "0":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setVisibility(View.VISIBLE);
                binding.coffeAwardBtn.setVisibility(View.GONE);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 0/6");
                break;
            case "1":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 1/6");
                break;
            case "2":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 2/6");
                break;
            case "3":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 3/6");
                break;
            case "4":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 4/6");
                break;
            case "5":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeiconno);
                binding.coffeAwardCounterText.setText("Kahve Ödül Sayacı: 5/6");
                break;
            case "6":
                binding.coffeeAwardImage1.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage2.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage3.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage4.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage5.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeeAwardImage6.setBackgroundResource(R.drawable.coffeeicon);
                binding.coffeAwardBtn.setVisibility(View.VISIBLE);
                binding.coffeAwardCounterText.setVisibility(View.GONE);
                binding.coffeAwardTitleText.setText("Tebrikler Kahve Kazandınız...");
                break;
            default:
                System.out.println("Gecersiz odul degeri.");
                binding.coffeAwardTitleText.setText("Hata");
                break;

        }


    }

    private void progressSet ( ){
        if (mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    private void setTimeSetting() {
        SimpleDateFormat format=new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time1 = format.format(new Date().getTime());
        String time111 = time1.split(":")[0];
        int time11 = Integer.parseInt(time111);
            if (time11 > 5 && time11 < 12)
                binding.homeLayoutTimeId.setText("Günaydın");
            else if (time11 >= 12 && time11 < 18) {
                binding.homeLayoutTimeId.setText("İyi günler");
            } else if (time11 >= 18 && time11 <= 23) {
                binding.homeLayoutTimeId.setText("İyi akşamlar");
            }else if (time11<6|| time11==24) {
                binding.homeLayoutTimeId.setText("İyi geceler");
            } else{
                binding.homeLayoutTimeId.setText("Hoşgeldiniz");
            }
    }

    public void setRetrofitSettings(){

        retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        timeApi = retrofit.create(TimeApi.class);
        timeTurkeyCall = timeApi.getTime();
        timeTurkeyCall.enqueue(new Callback<TimeTurkey>() {
            @Override
            public void onResponse(Call<TimeTurkey> call, Response<TimeTurkey> response) {
                timeTurkey = response.body();
                if (timeTurkey != null) {
                    String date1 = timeTurkey.getDataTime().split("T")[1];
                    int date = (Integer.parseInt(date1.split(":")[0]));
                    if (date > 5 && date < 12)
                        binding.homeLayoutTimeId.setText("Günaydın");
                    else if (date >= 12 && date < 18) {
                        binding.homeLayoutTimeId.setText("İyi günler");
                    } else if (date >= 18 && date <= 23) {
                        binding.homeLayoutTimeId.setText("İyi akşamlar");
                    }else if (date<6|| date==24) {
                            binding.homeLayoutTimeId.setText("İyi geceler");
                    } else{
                        binding.homeLayoutTimeId.setText("Hoşgeldiniz");
                    }
//                    binding.textView4.setText(timeTurkey.getDataTime().split("T")[1].split(".")[0]);
                }
            }

            @Override
            public void onFailure(Call<TimeTurkey> call, Throwable throwable) {
                System.out.println(throwable.toString());
            }
        });
    }

    public void updateUser(String userEmail) {
        db.collection("users")
                .whereEqualTo("mail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                    String documentId = document.getId();
                                    String newName = binding.kuserTextt.getText().toString().trim();
                                    //Telefon numarasını düzenleme
                                    String[] kphone_raw = binding.kphoneTextt.getText().toString().split("\\(");
                                    String[] kphone_raw1 = kphone_raw[1].split("\\)");
                                    String[] kphone_raw2 = kphone_raw1[1].split(" ");
                                    String newPhone = (kphone_raw1[0]+kphone_raw2[1]+kphone_raw2[2]).trim();

                                    if (newPhone.isEmpty()||newName.isEmpty())
                                        Toast.makeText(getActivity(), "Alanlar boş olamaz!", Toast.LENGTH_SHORT).show();
                                    else if (newPhone.length()<10)
                                        Toast.makeText(getActivity(), "Telefon numarası eksik", Toast.LENGTH_SHORT).show();
                                    else if (newName.length()<5)
                                        Toast.makeText(getActivity(), "Ad soyad bilgisi eksik", Toast.LENGTH_SHORT).show();
                                    else{
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("user", newName);
                                        updates.put("phone", newPhone);

                                        db.collection("users")
                                                .document(documentId)
                                                .update(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "Başarıyla güncellendi.Yeniden başlatılıyor!", Toast.LENGTH_SHORT).show();
                                                        Intent restartapp = new Intent(HomeFragment.this.getActivity(),SplashActivity.class);
                                                        startActivity(restartapp);
                                                        HomeFragment.this.getActivity().finish();
//
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity(), "Veri güncellemede hata", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } }else {
                                Toast.makeText(getActivity(), "Veri bulunamadı.", Toast.LENGTH_SHORT).show();

                            }}
                             else {
                            Toast.makeText(getActivity(), "Verilerde hata var.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void fetchUserData() {
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
                                        System.out.println(documentId+" dokumnat ıd");
                                        Map<String, Object> data = document.getData();

                                        db.collection("users")
                                                .document(documentId).addSnapshotListener((documentSnapshot, e) -> {
                                                    if (e != null) {
                                                        // Hata durumu
                                                        Log.e("KartEkrani", "Fetch failed: " + e.getMessage());
                                                        return;
                                                    }

                                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                                        Map<String, Object> cardMap = (Map<String, Object>) documentSnapshot.getData();
                                                        if (cardMap != null) {
                                                            Map<String, Object> cardData = (Map<String, Object>) cardMap.get("customercard");
                                                            String balance = String.valueOf(cardData.get("balance"));
                                                                String cardID = String.valueOf(cardData.get("cardID"));

                                                                String selectecImage = String.valueOf(cardData.get("selectedImage"));

                                                                binding.tvCardBalance.setText(balance != null ? ("Bakiyeniz: "+balance+" ₺" ): "Bilinmiyor");
                                                                binding.tvCardNumber.setText(cardID != null ? ("5416 2345 00"+cardID.substring(0,2)+" "+cardID.substring(2)) : "Bilinmiyor");
                                                                if (selectecImage.equals("0")){
                                                                    binding.cardRelativeLayout.setBackgroundResource(R.color.icogreen);
                                                                } else if (selectecImage.equals("1")) {
                                                                    binding.cardRelativeLayout.setBackgroundResource(R.drawable.coffeecardbg1);
                                                                }
                                                                else if (selectecImage.equals("2")) {
                                                                    binding.cardRelativeLayout.setBackgroundResource(R.drawable.coffeecardbg2);
                                                                }else {
                                                                    binding.cardRelativeLayout.setBackgroundResource(R.color.icogreen);
                                                                }
                                                            } else {
                                                                Log.d("KartEkrani", "Kart field is null.");
                                                            }
                                                        } else {
                                                            System.out.println("KART: 5" );
                                                            Log.d("KartEkrani", "Document not found.");
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


        }

        else{

            Log.e("Auth", "No current user is signed in.");
            System.out.println("ilgili kullanıcı bulunamdı....");
        }
    }
}