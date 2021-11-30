package com.psteam.foodlocationbusiness.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityVerifyOtpBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.GenericTextWatcher;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class VerifyOTPActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    private String phoneNumber;
    private signUp account;

    private long leftTimeInSecond = 60000;

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
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();

    }

    private void setListeners() {
        binding.buttonVerifyOTP.setOnClickListener(v -> {
            loading(true);
            if (binding.inputCode1.getText().toString().trim().isEmpty() ||
                    binding.inputCode2.getText().toString().trim().isEmpty() ||
                    binding.inputCode3.getText().toString().trim().isEmpty() ||
                    binding.inputCode4.getText().toString().trim().isEmpty() ||
                    binding.inputCode5.getText().toString().trim().isEmpty() ||
                    binding.inputCode6.getText().toString().trim().isEmpty()) {
                showToast("Vui lòng nhập đầy đủ mã");
                loading(false);
                return;
            }
            String code = binding.inputCode1.getText().toString() +
                    binding.inputCode2.getText().toString() +
                    binding.inputCode3.getText().toString() +
                    binding.inputCode4.getText().toString() +
                    binding.inputCode5.getText().toString() +
                    binding.inputCode6.getText().toString();
            if (code.length() == 6) {
                signUp();
            }
        });

        binding.inputCode1.addTextChangedListener(new GenericTextWatcher(binding.inputCode1, binding.inputCode2));
        binding.inputCode2.addTextChangedListener(new GenericTextWatcher(binding.inputCode2, binding.inputCode3));
        binding.inputCode3.addTextChangedListener(new GenericTextWatcher(binding.inputCode3, binding.inputCode4));
        binding.inputCode4.addTextChangedListener(new GenericTextWatcher(binding.inputCode4, binding.inputCode5));
        binding.inputCode5.addTextChangedListener(new GenericTextWatcher(binding.inputCode5, binding.inputCode6));
        binding.inputCode6.addTextChangedListener(new GenericTextWatcher(binding.inputCode6, null));

        binding.inputCode2.setOnKeyListener(new GenericKeyEvent(binding.inputCode2, binding.inputCode1));
        binding.inputCode3.setOnKeyListener(new GenericKeyEvent(binding.inputCode3, binding.inputCode2));
        binding.inputCode4.setOnKeyListener(new GenericKeyEvent(binding.inputCode4, binding.inputCode3));
        binding.inputCode5.setOnKeyListener(new GenericKeyEvent(binding.inputCode5, binding.inputCode4));
        binding.inputCode6.setOnKeyListener(new GenericKeyEvent(binding.inputCode6, binding.inputCode5));

        binding.textviewReSendOTP.setOnClickListener(v -> {
            leftTimeInSecond = 60000;
            countDownResendOTP(leftTimeInSecond);
        });
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonVerifyOTP.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonVerifyOTP.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        account = (signUp)getIntent().getSerializableExtra("account");
        binding.textviewPhone.setText(account.getPhone()+"");

        countDownResendOTP(leftTimeInSecond);
    }

    private void countDownResendOTP(long timeLeft) {
        CountDownTimer countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTimeInSecond = millisUntilFinished / 1000;
                binding.textviewReSendOTP.setText(String.format("Gửi lại sau: %02d:%02d", leftTimeInSecond / 60, leftTimeInSecond % 60));
            }

            @Override
            public void onFinish() {
                binding.textviewReSendOTP.setText("Gửi lại");
            }
        };
        countDownTimer.start();
    }

    public class GenericKeyEvent implements View.OnKeyListener {

        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
                if (previousView != null) {
                    previousView.requestFocus();
                }
                return true;
            }
            return false;
        }
    }

    private void signUp(){
        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.signup(account);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {

                if(response.body().getStatus() == 1){
                    DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
                    dataTokenAndUserId.saveToken(response.body().getNotification());
                    dataTokenAndUserId.saveUserId(response.body().getId());

                    startActivity(new Intent(VerifyOTPActivity.this, RestaurantRegistrationActivity.class));
                    loading(false);

                    Toast.makeText(VerifyOTPActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerifyOTPActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(VerifyOTPActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                loading(false);
            }
        });
    }

}