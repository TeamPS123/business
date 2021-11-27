package com.psteam.foodlocationbusiness.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerRestaurantBinding;

public class ManagerRestaurantFragment extends Fragment {

    private boolean isSelected = true;
    private int status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentManagerRestaurantBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerRestaurantBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();


    }

    private void init() {
        status=1;
        isSelected = status != 1 ? true : false;
        if(isSelected) {
            binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
            binding.textStatus.setText("Mở cửa");
        }else {
            binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
            binding.textStatus.setText("Đóng cửa");
        }

        binding.buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
                    binding.buttonSwitch.playAnimation();
                    binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.textStatus.setText("Đóng cửa");
                        }
                    });
                    isSelected = false;
                } else {
                    binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
                    binding.buttonSwitch.playAnimation();
                    binding.buttonSwitch.addAnimatorListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            binding.textStatus.setText("Mở cửa");
                        }
                    });

                    isSelected = true;
                }
            }
        });
    }
}