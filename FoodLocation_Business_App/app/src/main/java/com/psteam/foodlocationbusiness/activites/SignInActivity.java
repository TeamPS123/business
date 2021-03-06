package com.psteam.foodlocationbusiness.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivitySignInBinding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Input.signIn;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;


public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkSaveUser();
        setListeners();

        checkSelfPermission();
    }

    private void checkSaveUser() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplicationContext());
        if(!dataTokenAndUserId.getUserId().equals("")){
            if(dataTokenAndUserId.getToken() != "0"){
                Intent intent = new Intent(SignInActivity.this, BusinessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                loading(false);
                finish();
            }
        }
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(v -> {
            loading(true);
            if (isValidSignIn()) {
                signIn();
            }
            loading(false);
        });

        binding.textviewSignUp.setOnClickListener(v -> {
            setFullScreen();

            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private boolean isValidSignIn() {
        if (binding.inputPhone.getText().toString().trim().isEmpty()) {
            showToast("Vui l??ng nh???p s??? ??i???n tho???i");
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhone.getText().toString()).matches()) {
            showToast("Vui l??ng nh???p ????ng ?????nh d???ng s??? ??i???n tho???i");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Vui l??ng nh??p m???t kh???u");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignIn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void setFullScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void checkRes(String userId, String token){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.checkRes(userId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
                    dataTokenAndUserId.saveToken(token);
                    dataTokenAndUserId.saveUserId(userId);

                    Call<message> call1 = serviceAPI_lib.getRestaurantId(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId());
                    call1.enqueue(new Callback<message>() {
                        @Override
                        public void onResponse(Call<message> call, Response<message> response1) {

                            dataTokenAndUserId.saveRestaurantId(response1.body().getId());

                            Intent intent = new Intent(SignInActivity.this, BusinessActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            loading(false);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<message> call, Throwable t) {

                        }
                    });

                    Toast.makeText(SignInActivity.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignInActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "????ng nh???p th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(){
        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.signin(new signIn(binding.inputPhone.getText()+"", binding.inputPassword.getText()+""));
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

                if(response.body().getStatus() == 1){
                    checkRes(response.body().getId(), response.body().getNotification());
                }else{
                    Toast.makeText(SignInActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "????ng nh???p th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final static int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    SignInActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                CustomToast.makeText(getApplicationContext(), "Permission denied", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        }
    }
}