package com.itc.coffee.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.NumberFormat.PhoneNumberFormattingTextWatcher;
import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivityLoginBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ProgressDialog mProgress;
    String verificationId  = "123456";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        binding.kphoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher(binding.kphoneText));

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void loginScreen(View view){
        Intent mainActivitypage = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivitypage);
        finish();
    }
    public void secondstepsavebtnfunc(View view){
        verifyCode();

    }
    private void verifyCode() {
        String code = binding.smsText.getText().toString();
        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Başarıyla kayıt yapıldı", Toast.LENGTH_SHORT).show();
                            Intent maineDon = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(maineDon);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Kod doğrulama başarısız", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // Kod başarıyla alındı, kodu kullanıcıya göstermeye gerek yok
            Toast.makeText(LoginActivity.this, "Otomatik Doğrulama! Başarıyla Kayıt yapıldı", Toast.LENGTH_SHORT).show();
            Intent maineDon = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(maineDon);
            finish();
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, "Doğrulama hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // SMS kodu gönderildi
            LoginActivity.this.verificationId = verificationId;
            Toast.makeText(LoginActivity.this, "Kod gönderildi", Toast.LENGTH_SHORT).show();
        }
    };
    public void savebtn(View view) {
        String kuser = binding.kuserText.getText().toString().trim();
        String kmail = binding.kmailText.getText().toString().trim();
        String kpw = binding.kpwText.getText().toString();
        String kpwretry = binding.kpwretry.getText().toString();
        //Telefon numarasını düzenleme
        String[] kphone_raw = binding.kphoneText.getText().toString().split("\\(");
        String[] kphone_raw1 = kphone_raw[1].split("\\)");
        String[] kphone_raw2 = kphone_raw1[1].split(" ");
        String kphone = kphone_raw1[0]+kphone_raw2[1]+kphone_raw2[2];

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(kmail).matches()) {
            binding.maillayout.setError("Lütfen geçerli bir mail adresi girin.");
        }

        else if (kpw.length()<6) {
            binding.passwordlayout.setError("Lütfen geçerli bir şifre girin.");
        }
        else if (kphone.length()<10) {
            binding.phonelayout.setError("Lütfen geçerli bir telefon girin.");
        }
        else if(kuser.equals("") || kmail.equals("") || kphone.equals("") || kpw.equals("") || kpwretry.equals("")) {
            Toast.makeText(this, "Tüm Alanları Doldurunuz", Toast.LENGTH_LONG).show();
        } else {
            if (!kpw.equals(kpwretry)) {
                Toast.makeText(this, "Şifreler Birbirine Denk Olmalı", Toast.LENGTH_LONG).show();
            } else if (kuser.length() < 5) {
                Toast.makeText(this, "İsim Soyisim eksiksik Olmalı", Toast.LENGTH_LONG).show();
            } else {
                mProgress = new ProgressDialog(this);
                mProgress.setTitle("Kayıt Oluşturuluyor");
                mProgress.setMessage("Sözlük Kafe");
                mProgress.show();
                auth.createUserWithEmailAndPassword(kmail, kpw).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                        public void onSuccess(AuthResult authResult) {
                            // Başarılı  kayıt işlemi
                            // Kullanıcıya e-posta doğrulama e-postası gönder ( EKLEDİM)
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // E-posta doğrulama e-postası gönderildi
                                                Toast.makeText(LoginActivity.this, "E-posta doğrulama e-postası gönderildi.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // E-posta doğrulama e-postası gönderilemedi
                                                Toast.makeText(LoginActivity.this, "Doğrulama e-postası gönderilemedi.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        double cardId_double = generateUniqueCardId();
                        Map<String, Object> customercard = new HashMap<>();
                        Date presentTime = new Date();
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        String[] dateformat = df.format(presentTime).split("/");
                        String date = dateformat[2]+"."+dateformat[1]+"."+dateformat[0];
                        customercard.put("balance", 0);
                        customercard.put("cardID", cardId_double);
                        customercard.put("createDate", date);
                        customercard.put("selectedImage", 0);
                            HashMap<String, Object> veriKaydet = new HashMap<>();
                        String userUID = auth.getCurrentUser().getUid();
                        veriKaydet.put("user", kuser);
                        veriKaydet.put("phone", kphone);
                        veriKaydet.put("mail", kmail);
                        veriKaydet.put("userUID",userUID);
                        veriKaydet.put("coffeeAward", "0");
                        veriKaydet.put("customercard", customercard);






                        firestore.collection("users").add(veriKaydet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressSet();
//                                Toast.makeText(LoginActivity.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_LONG).show();
                                binding.firstlayout.setVisibility(View.INVISIBLE);
                                binding.secondlayout.setVisibility(View.VISIBLE);

                                sendVerificationCode(kphone);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Kayıt İşlemi Başarısız", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                    //Hata olursa
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Başarısız
                        Toast.makeText(LoginActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        progressSet();
                    }
                });
            }
        }
        }

    private void sendVerificationCode(String kphone) {
        String newphone = "+90"+kphone;
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(newphone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        auth.setLanguageCode("tr");
    }




    private void progressSet ( ){
        if (mProgress.isShowing()){
            mProgress.dismiss();
        }
    }
    private double generateUniqueCardId() {
        Random random = new Random();
        int cardId = 100000 + random.nextInt(900000);
        cardIdInDatabase(cardId, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isUnique) {
                if (isUnique) {
                    System.out.println("benzersiz cardID basariyla uretildi...");
                } else {
                    generateUniqueCardId();
                }
            }
        });
        return cardId;
    }

    private void cardIdInDatabase(final int cardId, final OnSuccessListener<Boolean> callback) {
        firestore.collection("users")
                .whereEqualTo("customercard.cardID", cardId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                callback.onSuccess(true);
                                System.out.println("boolen girildi");
                            } else {
                                callback.onSuccess(false);
                                System.out.println("boolen hata");
                            }
                        } else {
                            System.out.println("Hata: " + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Veri eklenirken hata oluştu:2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





}



