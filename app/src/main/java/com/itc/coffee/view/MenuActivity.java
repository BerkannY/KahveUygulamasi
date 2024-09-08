package com.itc.coffee.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.itc.coffee.R;
import com.itc.coffee.databinding.ActivityMenuBinding;
import com.itc.coffee.Mainfragments.HomeFragment;
import com.itc.coffee.Mainfragments.MenuFragment;
import com.itc.coffee.Mainfragments.OrderBasketFragment;
import com.itc.coffee.Mainfragments.PastOrderFragment;
import com.itc.coffee.Mainfragments.QrFragment;

public class MenuActivity extends AppCompatActivity {
    private ActivityMenuBinding binding;
    HomeFragment homeFragment;
    MenuFragment menuFragment;
    QrFragment qrFragment;
    OrderBasketFragment orderBasketFragment;
    PastOrderFragment pastOrderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            menuFragment = new MenuFragment();
            qrFragment = new QrFragment();
            orderBasketFragment = new OrderBasketFragment();
            pastOrderFragment = new PastOrderFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_frame_id, homeFragment, "home")
                    .commit();
        } else {
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
            menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag("menu");
            qrFragment = (QrFragment) getSupportFragmentManager().findFragmentByTag("qr");
            orderBasketFragment = (OrderBasketFragment) getSupportFragmentManager().findFragmentByTag("basket");
            pastOrderFragment = (PastOrderFragment) getSupportFragmentManager().findFragmentByTag("past");
        }
        binding.bottomNavMenuId.setOnItemSelectedListener(this::selectFragment);

//        homeFragment = new HomeFragment();
//        menuFragment = new MenuFragment();
//        qrFragment = new QrFragment();
//        orderBasketFragment = new OrderBasketFragment();
//        pastOrderFragment = new PastOrderFragment();
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, homeFragment).commit();
//
//
//
//        binding.bottomNavMenuId.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.homepage_id) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, homeFragment).commit();
//                return true;
//            } else if (item.getItemId() == R.id.menupage_id) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, menuFragment).commit();
//                return true;
//            } else if (item.getItemId() == R.id.qrpage_id) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, qrFragment).commit();
//                return true;
//            } else if (item.getItemId() == R.id.basketpage_id) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, orderBasketFragment).commit();
//                return true;
//            } else if (item.getItemId() == R.id.orderpage_id) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_id, pastOrderFragment).commit();
//                return true;
//            } else {
//                return false;
//            }
//        });




        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private boolean selectFragment(MenuItem item) {
        Fragment selectedFragment = null;
        String tag = null;
        int itemId = item.getItemId();
        if (itemId == R.id.homepage_id) {
            selectedFragment = homeFragment;
            tag = "home";
        } else if (itemId == R.id.menupage_id) {
            selectedFragment = menuFragment;
            tag = "menu";
        } else if (itemId == R.id.qrpage_id) {
            selectedFragment = qrFragment;
            tag = "qr";
        } else if (itemId == R.id.basketpage_id) {
            selectedFragment = orderBasketFragment;
            tag = "basket";
        } else if (itemId == R.id.orderpage_id) {
            selectedFragment = pastOrderFragment;
            tag = "past";
        }
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_frame_id, selectedFragment, tag)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}