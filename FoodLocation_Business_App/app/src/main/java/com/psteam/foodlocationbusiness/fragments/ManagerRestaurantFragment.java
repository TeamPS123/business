package com.psteam.foodlocationbusiness.fragments;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerRestaurantBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Get.messageResDetail;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        getResDetail();

//        status=1;
//        isSelected = status != 1 ? true : false;
//        if(isSelected) {
//            binding.buttonSwitch.setMinAndMaxProgress(0.5f, 1.0f);
//            binding.textStatus.setText("Mở cửa");
//        }else {
//            binding.buttonSwitch.setMinAndMaxProgress(0.0f, 0.5f);
//            binding.textStatus.setText("Đóng cửa");
//        }

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

                updateStatus(isSelected);
            }
        });
    }

    public void getResDetail(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageResDetail> call = serviceAPI_lib.getResDetail(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageResDetail>() {
            @Override
            public void onResponse(Call<messageResDetail> call, Response<messageResDetail> response) {
                if(response.body().getStatus() == 1){
                    binding.amountDay.setText(response.body().getResDetail().getAmountDay()+"");
                    binding.amountWeek.setText(response.body().getResDetail().getAmountWeek()+"");

                    if (!response.body().getResDetail().isStatus()) {
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
                Toast.makeText(getActivity(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<messageResDetail> call, Throwable t) {
                Toast.makeText(getActivity(), "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(boolean status1){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.changeStatus(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), status1);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }
}