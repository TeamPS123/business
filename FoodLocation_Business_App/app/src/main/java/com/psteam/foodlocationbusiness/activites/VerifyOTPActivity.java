package com.psteam.foodlocationbusiness.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityVerifyOtpBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.GenericTextWatcher;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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
                verifyCode(code);
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

            sendVerificationCode("+84" + account.getPhone());
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

    //SignIn by Phone
    private void sendVerificationCode(String number){
        DataTokenAndUserId.mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(DataTokenAndUserId.mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback xác thực sđt
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            //Hàm này được gọi trong hai trường hợp:
            //1. Trong một số trường hợp, điện thoại di động được xác minh tự động mà không cần mã xác minh.
            //2. Trên một số thiết bị, các dịch vụ của Google Play phát hiện SMS đến và thực hiện quy trình xác minh mà không cần người dùng thực hiện bất kỳ hành động nào.
            Log.d("Send", "onVerificationCompleted:" + credential);

            //tự động điền mã OTP
//            edtNum1.setText(credential.getSmsCode().substring(0,1));
//            edtNum2.setText(credential.getSmsCode().substring(1,2));
//            edtNum3.setText(credential.getSmsCode().substring(2,3));
//            edtNum4.setText(credential.getSmsCode().substring(3,4));
//            edtNum5.setText(credential.getSmsCode().substring(4,5));
//            edtNum6.setText(credential.getSmsCode().substring(5,6));

            verifyCode(credential.getSmsCode());
        }

        //fail
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Send", "onVerificationFailed", e);
            //ShowNotification.dismissProgressDialog();

//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Request fail");
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Quota không đủ");
//            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("Send", "onCodeSent:" + verificationId);
            //ShowNotification.dismissProgressDialog();
            Toast.makeText(getApplicationContext(), "Đã gửi OTP", Toast.LENGTH_SHORT).show();
            DataTokenAndUserId.mVerificationId = verificationId;
            //mResendToken = token;
        }
    };

    //code xác thực OTP
    private void verifyCode(String code) {
        //ShowNotification.showProgressDialog(MainActivity.this, "Đang xác thực");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(DataTokenAndUserId.mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        DataTokenAndUserId.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //ShowNotification.dismissProgressDialog();
                        if (task.isSuccessful()) {
                            Log.d("Confirm", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            //ShowNotification.showAlertDialog(MainActivity.this, "Thành công");
                            signUp();
                        } else {
                            loading(false);
                            Log.w("Confirm", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //ShowNotification.showAlertDialog(MainActivity.this, "Lỗi");
                                Toast.makeText(VerifyOTPActivity.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}