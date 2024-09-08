package com.itc.coffee.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;
import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public FirebaseAuth auth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth= FirebaseAuth.getInstance();


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resetpwpage = new Intent(MainActivity.this, ResetPwActivity.class);
                startActivity(resetpwpage);
                finish();
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
//    public void loginConfirm(View view){
//        Intent menuactvitypage = new Intent(MainActivity.this, MenuActivity.class);
//        startActivity(menuactvitypage);
//        finish();
//
//    }

    private void login() {
        String mailAdress = binding.loginmailText.getText().toString().trim();
        String password = binding.loginPWText.getText().toString();
        Log.d("MainActivity", "Giriş yapma denemesi: " + mailAdress);
        // E-posta formatı kontrolü
        if (mailAdress.isEmpty()|| mailAdress.length()<6) {
            binding.maillayout.setError("Lütfen geçerli bir mail adresi girin.");
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mailAdress).matches()) {
            binding.maillayout.setError("Lütfen geçerli bir mail adresi girin.");
        }
        else if (password.isEmpty()||password.length()<6) {
            binding.passwordlayout.setError("Lütfen geçerli bir şifre girin.");
        }
        // Burada gerçek giriş işlemi yapılacak
        else  {
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Giriş yapılıyor");
            mProgress.setMessage("Sözlük Kafe");
            mProgress.show();
            auth.signInWithEmailAndPassword(mailAdress,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if ( task.isSuccessful()){
                        progressSet();
                        FirebaseUser user = auth.getCurrentUser();

                        // Kullanıcının e-posta doğrulamasını kontrol et
                        if (user != null && user.isEmailVerified()) {
                            // E-posta doğrulandı, giriş işlemini tamamla
                            Toast.makeText(MainActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                            // Ana ekrana yönlendir
                            finish();
                            startActivity(new Intent(MainActivity.this, MenuActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        } else {
                            // E-posta doğrulanmamış
                            showEmailVerificationAlert();  // E-posta doğrulama mesajını göster
                        }}else {
                        progressSet();
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void progressSet ( ){
        if (mProgress.isShowing()){
            mProgress.dismiss();
        }
    }
    private void showEmailVerificationAlert() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Sözlük Kafe")
                .setIcon(R.drawable.itc_logo2)
                .setCancelable(false)
                .setMessage("Mail onaylama işlemini yapmadan sisteme giriş yapamazsınız.")
                .setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "Çıkış" butonuna basıldığında uygulamadan çık
                        auth.signOut();  // Oturumu kapat

                        // Uygulamadan tamamen çıkmak için tüm aktiviteleri kapatır
                        finishAffinity();
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
    public void pagechange(){
        Intent firstActivitypage = new Intent(MainActivity.this, FirstActivity.class);
        startActivity(firstActivitypage);
        finish();

    }
}