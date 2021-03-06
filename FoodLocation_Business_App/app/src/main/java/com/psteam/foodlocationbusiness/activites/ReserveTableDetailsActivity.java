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

import com.bumptech.glide.Glide;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.ManagerFoodAdapter;
import com.psteam.foodlocationbusiness.adapters.ReserveFoodAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityBusinessBinding;
import com.psteam.foodlocationbusiness.databinding.ActivityReserveTableDetailsBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.getFood;
import com.psteam.lib.Models.Get.getReserveTable;
import com.psteam.lib.Models.Get.messageAllFood;
import com.psteam.lib.Models.Get.messageInfoRes;
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

    private ReserveFoodAdapter managerFoodAdapter;
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
            binding.buttonConfirmed.setText("Ho??n th??nh");
        } else if (tabProcessing != null && tabProcessing.equals("tabConfirmed")) {
            binding.buttonDeny.setVisibility(View.GONE);
            binding.buttonConfirmed.setText("???? ho??n th??nh");
            binding.buttonConfirmed.setBackgroundColor(getColor(R.color.ColorButtonReserve));
            binding.buttonConfirmed.setTextColor(getColor(R.color.white));
            binding.buttonConfirmed.setEnabled(false);
        } else if (tabProcessing != null && tabProcessing.equals("tabLate")) {
            binding.buttonDeny.setText("Hu???");
        } else if (tabProcessing != null && tabProcessing.equals("tabCancel")) {
            binding.buttonConfirmed.setText("???? b??? hu???");
            binding.buttonDeny.setVisibility(View.GONE);
            binding.buttonConfirmed.setEnabled(false);
        }

        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });

        getInfoRes();
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
        managerFoodAdapter = new ReserveFoodAdapter(foods, getApplication(), new ReserveFoodAdapter.ReserveFoodViewListeners() {
            @Override
            public void onAddFoodReserveClick(getFood food, int position, int quantity) {
                updateQuantity(response.getReserveTableId(), food.getFoodId(), quantity);
            }

            @Override
            public void onMinusFoodReserveClick(getFood food, int position, int quantity) {
                updateQuantity(response.getReserveTableId(), food.getFoodId(), quantity);
            }

            @Override
            public void onRemoveFoodReserveClick(getFood food, int position) {
                foods.remove(position);
                managerFoodAdapter.notifyItemRemoved(position);

                delReserveFood(response.getReserveTableId(), food.getFoodId());
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
        } else if (getIntent().getStringExtra("search") != null && getIntent().getStringExtra("search").equals("search")) {
            getReserveTable reserveTable = (getReserveTable) getIntent().getSerializableExtra("response");
            response.setReserveTableId(reserveTable.getReserveTableId());
            response.setName(reserveTable.getName());
            response.setQuantity(reserveTable.getQuantity());
            response.setNote(reserveTable.getNote());
            response.setPhone(reserveTable.getPhone());
            response.setPromotionId(reserveTable.getPromotionId());
            response.setRestaurantId(reserveTable.getRestaurantId());
            response.setTime(reserveTable.getTime());
            response.setUserId(reserveTable.getUserId());
        } else {
            response = (BodySenderFromUser) getIntent().getSerializableExtra("response");
            postion = getIntent().getIntExtra("position", -1);
        }
    }

    private void setBinding() {
        binding.textViewRestaurantAddress.setText(response.getName());
        binding.textViewCountPeople.setText(String.format("%d ng?????i", response.getQuantity()));
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
                    //x??c nh???n phi???u
                    if (code == 1) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "Th??ng b??o ?????t b??n", new BodySenderFromRes("Nh?? h??ng ???? x??c nh???n ????n ?????t b??n c???a b???n", reserveTableId));
                        setupSocket.reserveTable(message);
                    } else if (code == 2) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "Th??ng b??o ?????t b??n", new BodySenderFromRes("Nh?? h??ng ???? t??? ch???i ????n ?????t b??n c???a b???n", reserveTableId));
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
                Toast.makeText(getApplication(), "C??p nh???t phi???u ?????t b??n th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delReserveFood(String reserveTableId, String foodId){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.delFoodOfRes(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId, foodId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response1) {


                //Toast.makeText(getApplication(), response1.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplication(), "C??p nh???t phi???u ?????t b??n th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuantity(String reserveTableId, String foodId, int quantity){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updateQuantity(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId, foodId, quantity);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response1) {


                //Toast.makeText(getApplication(), response1.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplication(), "C??p nh???t phi???u ?????t b??n th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInfoRes() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageInfoRes> call = serviceAPI_lib.getInfoRestaurant(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageInfoRes>() {
            @Override
            public void onResponse(Call<messageInfoRes> call, Response<messageInfoRes> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                   binding.textViewRestaurantName.setText(response.body().getRes().getName());
                }
            }

            @Override
            public void onFailure(Call<messageInfoRes> call, Throwable t) {

            }
        });
    }

}