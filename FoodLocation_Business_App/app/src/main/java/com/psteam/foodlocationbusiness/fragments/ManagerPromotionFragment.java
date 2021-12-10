package com.psteam.foodlocationbusiness.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.PromotionAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerPromotionBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutPromotionInsertDialogBinding;
import com.psteam.foodlocationbusiness.models.PromotionModel;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.getPromotion;
import com.psteam.lib.Models.Get.messagePromotion;
import com.psteam.lib.Models.Insert.insertPromotion;
import com.psteam.lib.Models.Update.updatePromotion;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;


public class ManagerPromotionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentManagerPromotionBinding binding;

    private ArrayList<getPromotion> promotions;
    private PromotionAdapter promotionAdapter;

    private DataTokenAndUserId dataTokenAndUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerPromotionBinding.inflate(inflater, container, false);

        dataTokenAndUserId = new DataTokenAndUserId(getContext());

        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        getPromotions();
    }

    private void initPromotionAdapter() {
        promotionAdapter = new PromotionAdapter(getContext(), promotions, new PromotionAdapter.PromotionListeners() {
            @Override
            public void onEditClick(getPromotion promotions, int mode, int position) {
                if (mode == 1) {
                    openInsertPromotionDialog(promotions, 3, position);
                    layoutPromotionInsertDialogBinding.text1.setText("Cập nhật khuyễn mãi");
                }
                else {
                    openInsertPromotionDialog(promotions, 2, 0);
                    layoutPromotionInsertDialogBinding.text1.setText("Thông tin khuyến mãi");
                    layoutPromotionInsertDialogBinding.buttonAddCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDeleteClick(getPromotion promotions, int position) {
                delPromotion(promotions.getPromotionId(), position);
            }

            @Override
            public void onChangeStatus(getPromotion promotions, int position) {
                upPromotion(new updatePromotion(promotions.getPromotionId(), promotions.getName(), promotions.getInfo(), promotions.getValue(), dataTokenAndUserId.getUserId(), !promotions.isStatus()), position);
            }
        });

        binding.recycleView.setAdapter(promotionAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(dividerItemDecoration);
    }

    private void setListeners() {
        binding.buttonInsertPromotion.setOnClickListener(v -> {
            openInsertPromotionDialog(null, 1, 0);
        });
    }

    private AlertDialog dialog;

    private LayoutPromotionInsertDialogBinding layoutPromotionInsertDialogBinding;

    private void openInsertPromotionDialog(getPromotion promotion, int code, int index) {

        layoutPromotionInsertDialogBinding =
                LayoutPromotionInsertDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutPromotionInsertDialogBinding.getRoot());
        builder.setCancelable(false);

        if(code == 1){
            layoutPromotionInsertDialogBinding.buttonAddCategory.setText("Thêm mới");
        }else if(code == 3){
            layoutPromotionInsertDialogBinding.buttonAddCategory.setText("Thay đổi");
        }

        if (promotion != null) {
            layoutPromotionInsertDialogBinding.inputName.setText(promotion.getName());
            layoutPromotionInsertDialogBinding.inputInfo.setText(promotion.getInfo());
            layoutPromotionInsertDialogBinding.inputValue.setText(promotion.getValue());
        }

        layoutPromotionInsertDialogBinding.buttonAddCategory.setOnClickListener(v -> {
            DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

            insertPromotion promotion1 = new insertPromotion();
            promotion1.setRestaurantId(dataTokenAndUserId.getRestaurantId());
            promotion1.setInfo(layoutPromotionInsertDialogBinding.inputInfo.getText()+"");
            promotion1.setName(layoutPromotionInsertDialogBinding.inputName.getText()+"");
            promotion1.setValue(layoutPromotionInsertDialogBinding.inputValue.getText()+"");
            promotion1.setUserId(dataTokenAndUserId.getUserId());

            ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
            if(code == 1){
                Call<message> call = serviceAPI_lib.addPromotion(dataTokenAndUserId.getToken(), promotion1);
                call.enqueue(new Callback<message>() {
                    @Override
                    public void onResponse(Call<message> call, Response<message> response) {
                        if(response.body().getStatus() == 1){
                            promotions.add(new getPromotion(response.body().getId(), promotion1.getName(), promotion1.getInfo(), promotion1.getValue(), true));

                            promotionAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getContext(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<message> call, Throwable t) {
                        Toast.makeText(getContext(), "Thêm khuyến mãi thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(code == 3){
                updatePromotion updatePromotion = new updatePromotion();
                updatePromotion.setPromotionId(promotion.getPromotionId());
                updatePromotion.setInfo(layoutPromotionInsertDialogBinding.inputInfo.getText()+"");
                updatePromotion.setName(layoutPromotionInsertDialogBinding.inputName.getText()+"");
                updatePromotion.setValue(layoutPromotionInsertDialogBinding.inputValue.getText()+"");
                updatePromotion.setUserId(dataTokenAndUserId.getUserId());

                Call<message> call = serviceAPI_lib.updatePromotion(dataTokenAndUserId.getToken(), updatePromotion);
                call.enqueue(new Callback<message>() {
                    @Override
                    public void onResponse(Call<message> call, Response<message> response) {
                        if(response.body().getStatus() == 1) {
                            getPromotion newPromotion = new getPromotion();
                            newPromotion.setPromotionId(promotion.getPromotionId());
                            newPromotion.setInfo(updatePromotion.getInfo());
                            newPromotion.setName(updatePromotion.getName());
                            newPromotion.setValue(updatePromotion.getValue());

                            promotions.set(index, newPromotion);
                            promotionAdapter.notifyItemChanged(index);
                        }
                        Toast.makeText(getContext(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<message> call, Throwable t) {
                        Toast.makeText(getContext(), "Cập nhật khuyến mãi thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            dialog.dismiss();
        });
        layoutPromotionInsertDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog = builder.create();
        dialog.show();
    }

    private void getPromotions(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messagePromotion> call = serviceAPI_lib.getPromotionList(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messagePromotion>() {
            @Override
            public void onResponse(Call<messagePromotion> call, Response<messagePromotion> response) {
                if(response.body().getStatus() == 1){
                    promotions = response.body().getProList();

                    initPromotionAdapter();
                }else {
                    promotions = new ArrayList<>();
                }

                Toast.makeText(getActivity(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<messagePromotion> call, Throwable t) {
                Toast.makeText(getActivity(), "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delPromotion(String promotionId, int index){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.delPromotion(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), promotionId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    promotions.remove(index);
                    promotionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }

    private void upPromotion(updatePromotion updatePromotion, int index){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updatePromotion(dataTokenAndUserId.getToken(), updatePromotion);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
//                if(response.body().getStatus() == 1){
//                    promotions.remove(index);
//                    promotionAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }
}