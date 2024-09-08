package com.itc.coffee.view;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.itc.coffee.Network.NetworkUtil;
import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    public FirebaseAuth auth;
    private FirebaseFirestore firestore;
    boolean networkControl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.animationView.setSpeed(1.5f);
        binding.animationView.playAnimation();

        binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                if (!NetworkUtil.isNetworkAvailable(SplashActivity.this)) {
                    showNoInternetDialog();
                    networkControl = false;
                }
                else networkControl=true;
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                if (networkControl){
                    auth = FirebaseAuth.getInstance();
                    firestore = FirebaseFirestore.getInstance();

                    FirebaseUser currentUser = auth.getCurrentUser();

                    if (currentUser != null) {
                        FirebaseUser user = auth.getCurrentUser();

                        // Kullanıcının e-posta doğrulamasını kontrol et
                        if (user != null && user.isEmailVerified()) {
                            // E-posta doğrulandı, giriş işlemini tamamla
                            Toast.makeText(SplashActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                            // Ana ekrana yönlendir
                            finish();
                            startActivity(new Intent(SplashActivity.this, MenuActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        } else {
                            // E-posta doğrulanmamış
                            showEmailVerificationAlert();  // E-posta doğrulama mesajını göster
                        }
                    } else {
                        pagechange();
                    }
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });



        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void pagechange(){
        Intent firstActivitypage = new Intent(SplashActivity.this, FirstActivity.class);
        startActivity(firstActivitypage);
        finish();

    }
    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sözlük Kafe")
                .setIcon(R.drawable.itc_logo2)
                .setMessage("Lütfen internet bağlantısını kontrol edip tekrar deneyin.")
                .setCancelable(false)
                .setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(1);
                    }
                }).setNeutralButton("Yeni hesap ile giriş yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pagechange();
                    }
                })
                .create()
                .show();
    }
    private void showEmailVerificationAlert() {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Sözlük Kafe")
                .setIcon(R.drawable.itc_logo2)
                .setCancelable(false)
                .setMessage("Mail onaylama işlemini yapmadan sisteme giriş yapamazsınız.")
                .setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "Çıkış" butonuna basıldığında uygulamadan çık
                        auth.signOut();  // Oturumu kapat
                        System.exit(1);
                    }
                }).setNeutralButton("Yeni hesap ile giriş yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pagechange();
                    }
                })
                .create()
                .show();
    }
}