package com.psteam.foodlocationbusiness.activites;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityBusinessBinding;
import com.psteam.foodlocationbusiness.databinding.ActivityReserveTableDetailsBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;

public class ReserveTableDetailsActivity extends AppCompatActivity {

    private ActivityReserveTableDetailsBinding binding;
    private BodySenderFromUser response;
    private String userId = "restaurant";

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
        }
    }

    private void getDataFromNoti(){
        if(getIntent().getExtras().getString("reserveTableId") != null){
            response = new BodySenderFromUser();
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
            MessageSenderFromRes message = new MessageSenderFromRes(userId, response.getUserId(), "thông báo", new BodySenderFromRes("Nhà hàng đã xác nhận đơn đặt bàn của bạn", response.getReserveTableId()));
            setupSocket.reserveTable(message);
        });
        binding.buttonDeny.setOnClickListener((v) -> {
            MessageSenderFromRes message = new MessageSenderFromRes(userId, response.getUserId(), "thông báo", new BodySenderFromRes("Nhà hàng đã từ chối đơn đặt bàn của bạn", response.getReserveTableId()));
            setupSocket.reserveTable(message);
        });
    }

}