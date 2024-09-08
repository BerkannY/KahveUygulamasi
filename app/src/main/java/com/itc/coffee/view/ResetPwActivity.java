package com.itc.coffee.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivityResetPwBinding;

public class ResetPwActivity extends AppCompatActivity {
    public FirebaseAuth auth;
    private ActivityResetPwBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPwBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();

        binding.btnResetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.eMailText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    binding.eMailText.setError("E-posta adresinizi girin");
                    return;
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPwActivity.this,
                                    "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ResetPwActivity.this,
                                    "Şifre sıfırlama e-postası gönderilemedi. Lütfen tekrar deneyin.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}