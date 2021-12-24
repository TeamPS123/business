package com.psteam.foodlocationbusiness.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.ManagerFoodAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityBusinessBinding;
import com.psteam.foodlocationbusiness.databinding.ActivityReserveTableDetailsBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.getFood;
import com.psteam.lib.Models.Get.messageAllFood;
import com.psteam.lib.Models.Insert.reserveTable;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.foodlocationbusiness.ultilities.Constants.coverStringToDate;
import static com.psteam.foodlocationbusiness.ultilities.Constants.formatToYesterdayOrToday;
import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class ReserveTableDetailsActivity extends AppCompatActivity {

    private ActivityReserveTableDetailsBinding binding;
    private BodySenderFromUser response;

    private ManagerFoodAdapter managerFoodAdapter;
    private ArrayList<getFood> foods;
    private int postion;
    private String tabProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReserveTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        setFullScreen();
        setupSocket.mSocket.connect();
        if (getIntent().getExtras() != null) {
            getDataFromNoti();
            setBinding();
            setListeners();
            setAdapter(response.getReserveTableId());
            tabProcessing = getIntent().getStringExtra("tabProcessing");
        }
        if (tabProcessing != null && tabProcessing.equals("tabProcessing")) {
            binding.buttonDeny.setVisibility(View.GONE);
            binding.buttonConfirmed.setText("Khách đã đến");
        } else if (tabProcessing != null && tabProcessing.equals("tabConfirmed")) {
            binding.buttonDeny.setVisibility(View.GONE);
            binding.buttonConfirmed.setText("Đã hoàn thành");
            binding.buttonConfirmed.setBackgroundColor(getColor(R.color.ColorButtonReserve));
            binding.buttonConfirmed.setTextColor(getColor(R.color.white));
            binding.buttonConfirmed.setEnabled(false);
        } else if (tabProcessing != null && tabProcessing.equals("tabLate")) {
            binding.buttonDeny.setText("Huỷ");
        }

        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });

    }

    private void setAdapter(String reserveTableId) {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllFood> call = serviceAPI_lib.getAllFoodByReserveTableId(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId);
        call.enqueue(new Callback<messageAllFood>() {
            @Override
            public void onResponse(Call<messageAllFood> call, Response<messageAllFood> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    foods = response.body().getFoodList();
                    initFoodManagerAdapter();
                }
            }

            @Override
            public void onFailure(Call<messageAllFood> call, Throwable t) {

            }
        });
    }

    private void initFoodManagerAdapter() {
        managerFoodAdapter = new ManagerFoodAdapter(foods, getApplication(), new ManagerFoodAdapter.FoodListeners() {
            @Override
            public void onEditClick(getFood food, int position) {

            }

            @Override
            public void onDeleteClick(getFood food, int position) {

            }

            @Override
            public void onChangeStatus(getFood food, int position) {

            }
        });
        binding.recycleViewFoodReserve.setAdapter(managerFoodAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getApplication(), R.drawable.divider));
        binding.recycleViewFoodReserve.addItemDecoration(itemDecoration);
    }

    private void getDataFromNoti() {
        response = new BodySenderFromUser();
        if (getIntent().getExtras().getString("reserveTableId") != null) {
            response.setReserveTableId(getIntent().getExtras().getString("reserveTableId"));
            response.setName(getIntent().getExtras().getString("name"));
            response.setQuantity(Integer.parseInt(getIntent().getExtras().getString("quantity")));
            response.setNote(getIntent().getExtras().getString("note"));
            response.setPhone(getIntent().getExtras().getString("phone"));
            response.setPromotionId(getIntent().getExtras().getString("promotionId"));
            response.setRestaurantId(getIntent().getExtras().getString("restaurantId"));
            response.setTime(getIntent().getExtras().getString("time"));
            response.setUserId(getIntent().getExtras().getString("userId"));
        } else {
            response = (BodySenderFromUser) getIntent().getSerializableExtra("response");
            postion = getIntent().getIntExtra("position", -1);
        }
    }

    private void setBinding() {
        binding.textViewRestaurantAddress.setText(response.getName());
        binding.textViewCountPeople.setText(String.format("%d người",response.getQuantity()));
        binding.textViewTimeReserve.setText(formatToYesterdayOrToday(coverStringToDate(response.getTime())));
        binding.textViewPhoneNumber.setText(response.getPhone());
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void setListeners() {
        binding.buttonConfirmed.setOnClickListener((v) -> {

            if (tabProcessing != null && tabProcessing.equals("tabProcessing")) {
                updateReserveTable(4, response.getReserveTableId());
            } else {
                updateReserveTable(1, response.getReserveTableId());
            }

        });
        binding.buttonDeny.setOnClickListener((v) -> {
            updateReserveTable(2, response.getReserveTableId());
        });
    }

    private void updateReserveTable(int code, String reserveTableId) {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updateReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId, code);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response1) {
                if (response1.body() != null && response1.body().getStatus() == 1) {
                    //xác nhận phiếu
                    if (code == 1) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "Thông báo đặt bàn", new BodySenderFromRes("Nhà hàng đã xác nhận đơn đặt bàn của bạn", reserveTableId));
                        setupSocket.reserveTable(message);
                    } else if (code == 2) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "Thông báo đặt bàn", new BodySenderFromRes("Nhà hàng đã từ chối đơn đặt bàn của bạn", reserveTableId));
                        setupSocket.reserveTable(message);
                    }

                    Intent intent = new Intent();
                    intent.putExtra("positionResult", postion);
                    intent.putExtra("code", code);
                    setResult(11, intent);
                    finish();
                }

                //Toast.makeText(getApplication(), response1.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplication(), "Câp nhật phiếu đặt bàn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}