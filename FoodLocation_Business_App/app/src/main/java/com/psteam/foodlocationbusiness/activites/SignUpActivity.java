package com.psteam.foodlocationbusiness.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivitySignUpBinding;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Input.signIn;
import com.psteam.lib.Models.Insert.signUp;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

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
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

        DataTokenAndUserId.mAuth = FirebaseAuth.getInstance();
    }

    private boolean isValidSignUp() {
        if (binding.inputFullName.getText().toString().trim().isEmpty()) {
            binding.inputFullName.setError("H??? v?? t??n kh??ng ???????c b??? tr???ng");
            return false;
        } else if (binding.inputPhone.getText().toString().trim().isEmpty()) {
            binding.inputPhone.setError("S??? ??i???n tho???i kh??ng ???????c b??? tr???ng");
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhone.getText().toString()).matches()) {
            binding.inputPhone.setError("Sai ?????nh d???ng s??? ??i???n tho???i");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            binding.inputPassword.setError("M???t kh???u kh??ng ???????c b??? tr???ng");
            return false;
        } else if (!isValidFormatPassword(binding.inputPassword.getText().toString().trim())) {
            binding.inputPassword.setError("????? d??i m???t kh???u t??? 6 ?????n 24 k?? t??? bao g???m ch???, s???, k?? t??? in hoa v?? k?? t??? ?????t bi???t");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            binding.inputConfirmPassword.setError("Nh???p l???i m???t kh???u kh??ng ???????c b??? tr???ng");
            return false;
        } else if (!binding.inputPassword.getText().toString().trim().equals(binding.inputConfirmPassword.getText().toString().trim())) {
            binding.inputConfirmPassword.setError("Nh???p l???i m???t kh???u kh??ng ????ng");
            return false;
        } else {
            return true;
        }
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonSignUp.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }

    public boolean isValidFormatPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners() {
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUp()) {
                loading(true);
                checkPhone();
            }
        });
        binding.buttonSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
    }

    //SignIn by Phone
    private void sendVerificationCode(String number){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(DataTokenAndUserId.mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback x??c th???c s??t
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            //H??m n??y ???????c g???i trong hai tr?????ng h???p:
            //1. Trong m???t s??? tr?????ng h???p, ??i???n tho???i di ?????ng ???????c x??c minh t??? ?????ng m?? kh??ng c???n m?? x??c minh.
            //2. Tr??n m???t s??? thi???t b???, c??c d???ch v??? c???a Google Play ph??t hi???n SMS ?????n v?? th???c hi???n quy tr??nh x??c minh m?? kh??ng c???n ng?????i d??ng th???c hi???n b???t k??? h??nh ?????ng n??o.
            Log.d("Send", "onVerificationCompleted:" + credential);

                        //t??? ?????ng ??i???n m?? OTP
//            edtNum1.setText(credential.getSmsCode().substring(0,1));
//            edtNum2.setText(credential.getSmsCode().substring(1,2));
//            edtNum3.setText(credential.getSmsCode().substring(2,3));
//            edtNum4.setText(credential.getSmsCode().substring(3,4));
//            edtNum5.setText(credential.getSmsCode().substring(4,5));
//            edtNum6.setText(credential.getSmsCode().substring(5,6));

            verifyCode(credential.getSmsCode());
            loading(false);
        }

        //fail
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Send", "onVerificationFailed", e);
            //ShowNotification.dismissProgressDialog();

//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Request fail");
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                ShowNotification.showAlertDialog(MainActivity.this, "Quota kh??ng ?????");
//            }
            loading(false);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            loading(false);

            Log.d("Send", "onCodeSent:" + verificationId);
            //ShowNotification.dismissProgressDialog();
            Toast.makeText(getApplicationContext(), "???? g???i OTP", Toast.LENGTH_SHORT).show();
            DataTokenAndUserId.mVerificationId = verificationId;
            mResendToken = token;

            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
            intent.putExtra("account", new signUp(binding.inputFullName.getText()+"", binding.inputPhone.getText()+"", binding.inputPassword.getText()+"", true, true));
            startActivity(intent);
        }
    };

    //code x??c th???c OTP
    private void verifyCode(String code) {
        //ShowNotification.showProgressDialog(MainActivity.this, "??ang x??c th???c");
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
                            //ShowNotification.showAlertDialog(MainActivity.this, "Th??nh c??ng");
                        } else {
                            Log.w("Confirm", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //ShowNotification.showAlertDialog(MainActivity.this, "L???i");
                            }
                        }
                    }
                });
    }

    private void checkPhone(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.checkPhone(binding.inputPhone.getText()+"");
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    sendVerificationCode("+84" + binding.inputPhone.getText().toString());
                }else{
                    Toast.makeText(getApplication(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
                    loading(false);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplication(), "L???i", Toast.LENGTH_SHORT).show();
                loading(false);
            }
        });
    }

    private void signUp(signUp account){
        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.signup(account);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
                    dataTokenAndUserId.saveToken(response.body().getNotification());
                    dataTokenAndUserId.saveUserId(response.body().getId());
                    startActivity(new Intent(getApplicationContext(), RestaurantRegistrationActivity.class));
                    loading(false);
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                loading(false);
            }
        });
    }
}