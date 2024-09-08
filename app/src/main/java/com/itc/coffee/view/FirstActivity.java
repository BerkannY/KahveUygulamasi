package com.itc.coffee.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {
    private ActivityFirstBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        binding.thoseWhoContributed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pgechange = new Intent(FirstActivity.this, Who_contributedActivity.class);
                startActivity(pgechange);
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void singupScreen(View view){
        Intent loginpage = new Intent(FirstActivity.this, LoginActivity.class);
        startActivity(loginpage);

    }
    public void loginScreen(View view){
        Intent mainActivitypage = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(mainActivitypage);

    }
}