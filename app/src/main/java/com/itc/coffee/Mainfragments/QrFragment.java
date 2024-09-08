package com.itc.coffee.Mainfragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.itc.coffee.R;
import com.itc.coffee.databinding.FragmentQrBinding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrFragment extends Fragment {

    private FragmentQrBinding binding; // private erişim belirleyici ekledik
    private FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQrBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        storage = FirebaseStorage.getInstance();
        binding.qrCodeImageView1.setVisibility(View.VISIBLE);


        Bundle arguments = getArguments();
        if (arguments != null) {
            String status = arguments.getString("awardStatus");
            String qrCode = arguments.getString("qr");
            if ("yes".equals(status)) {
                String randomText = String.valueOf(qrCode);

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(randomText, BarcodeFormat.QR_CODE, 512, 512);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    binding.qrCodeImageView1.setVisibility(View.GONE);
                    binding.qrCodeImageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
