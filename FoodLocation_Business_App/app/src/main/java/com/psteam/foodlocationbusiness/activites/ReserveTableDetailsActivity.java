package com.psteam.foodlocationbusiness.activites;

import android.os.Bundle;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class ReserveTableDetailsActivity extends AppCompatActivity {

    private ActivityReserveTableDetailsBinding binding;
    private BodySenderFromUser response;

    private ManagerFoodAdapter managerFoodAdapter;
    private ArrayList<getFood> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReserveTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init(){
        setupSocket.mSocket.connect();
        if(getIntent().getExtras() != null){
            getDataFromNoti();
            setBinding();
            setListeners();
            setAdapter(response.getReserveTableId());
        }
    }

    private void setAdapter(String reserveTableId){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllFood> call = serviceAPI_lib.getAllFoodByReserveTableId(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId);
        call.enqueue(new Callback<messageAllFood>() {
            @Override
            public void onResponse(Call<messageAllFood> call, Response<messageAllFood> response) {
                if(response.body().getStatus() == 1){
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

    private void getDataFromNoti(){
        response = new BodySenderFromUser();
        if(getIntent().getExtras().getString("reserveTableId") != null){
            response.setReserveTableId(getIntent().getExtras().getString("reserveTableId"));
            response.setName(getIntent().getExtras().getString("name"));
            response.setQuantity(Integer.parseInt(getIntent().getExtras().getString("quantity")));
            response.setNote(getIntent().getExtras().getString("note"));
            response.setPhone(getIntent().getExtras().getString("phone"));
            response.setPromotionId(getIntent().getExtras().getString("promotionId"));
            response.setRestaurantId(getIntent().getExtras().getString("restaurantId"));
            response.setTime(getIntent().getExtras().getString("time"));
            response.setUserId(getIntent().getExtras().getString("userId"));
        }else{
            response = (BodySenderFromUser) getIntent().getSerializableExtra("response");
        }
    }

    private void setBinding(){
        binding.textViewRestaurantAddress.setText(response.getName());
        binding.textViewCountPeople.setText(response.getQuantity()+"");
        binding.textViewTimeReserve.setText(response.getTime());
        binding.textViewPhoneNumber.setText(response.getPhone());
    }

    private void setListeners(){
        binding.buttonConfirmed.setOnClickListener((v) -> {
            updateReserveTable(1, response.getReserveTableId());
        });
        binding.buttonDeny.setOnClickListener((v) -> {
            updateReserveTable(2, response.getReserveTableId());
        });

        binding.imageViewClose.setOnClickListener(v->{
            finish();
        });
    }

    private void updateReserveTable(int code, String reserveTableId){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updateReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTableId, code);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response1) {
                if(response1.body().getStatus() == 1){
                    //xác nhận phiếu
                    if(code == 1){
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "thông báo", new BodySenderFromRes("Nhà hàng đã xác nhận đơn đặt bàn của bạn", reserveTableId));
                        setupSocket.reserveTable(message);
                    }else if(code == 2){
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), response.getUserId(), "thông báo", new BodySenderFromRes("Nhà hàng đã từ chối đơn đặt bàn của bạn", reserveTableId));
                        setupSocket.reserveTable(message);
                    }
                }

                Toast.makeText(getApplication(), response1.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplication(), "Câp nhật phiếu đặt bàn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}