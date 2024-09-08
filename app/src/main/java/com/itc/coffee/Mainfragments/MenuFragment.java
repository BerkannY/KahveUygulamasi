package com.itc.coffee.Mainfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.itc.coffee.Menufragments.BooksFragment;
import com.itc.coffee.Menufragments.CoffeeFragment;
import com.itc.coffee.Menufragments.DesertAndCakeFragment;
import com.itc.coffee.Menufragments.NaturalDrinkFragment;
import com.itc.coffee.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {
    FragmentMenuBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.ViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.ViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Kahveler");
                    break;
                case 1:
                    tab.setText("Doğal İçecekler");
                    break;
                case 2:
                    tab.setText("Tatlı ve Pastalar");
                    break;
                case 3:
                    tab.setText("Kitap ve Dergiler");
                    break;
            }
        }).attach();

        return  view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CoffeeFragment();
                case 1:
                    return new NaturalDrinkFragment();
                case 2:
                    return new DesertAndCakeFragment();
                case 3:
                    return new BooksFragment();
                default:
                    return new CoffeeFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4; // Number of tabs
        }
    }

}