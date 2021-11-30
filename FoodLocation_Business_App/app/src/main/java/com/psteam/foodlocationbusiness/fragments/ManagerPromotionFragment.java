package com.psteam.foodlocationbusiness.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.PromotionAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerPromotionBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutPromotionInsertDialogBinding;
import com.psteam.foodlocationbusiness.models.PromotionModel;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;

import java.util.ArrayList;


public class ManagerPromotionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentManagerPromotionBinding binding;

    private ArrayList<PromotionModel> promotionModels;
    private PromotionAdapter promotionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerPromotionBinding.inflate(inflater, container, false);
        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        promotionModels = new ArrayList<>();
        initPromotionAdapter();
    }

    private void initPromotionAdapter() {
        promotionModels.add(new PromotionModel("Khuyễn mãi cuối tuần giảm 5% tôngt hoá đơn", "Khi ăn uống tại nhà hàng vào thứ 7 và CN sẽ đuọc giảm 5% tổng hoá đơn", "5"));
        promotionModels.add(new PromotionModel("Khuyễn mãi cuối tuần giảm 15% tôngt hoá đơn", "Khi ăn uống tại nhà hàng vào thứ 7 và CN sẽ đuọc giảm 5% tổng hoá đơn", "15"));
        promotionModels.add(new PromotionModel("Khuyễn mãi cuối tuần giảm 25% tôngt hoá đơn", "Khi ăn uống tại nhà hàng vào thứ 7 và CN sẽ đuọc giảm 5% tổng hoá đơn", "25"));
        promotionModels.add(new PromotionModel("Khuyễn mãi cuối tuần giảm 35% tôngt hoá đơn", "Khi ăn uống tại nhà hàng vào thứ 7 và CN sẽ đuọc giảm 5% tổng hoá đơn", "35"));
        promotionAdapter = new PromotionAdapter(promotionModels, new PromotionAdapter.PromotionListeners() {
            @Override
            public void onEditClick(PromotionModel promotionModel, int mode) {
                if (mode == 1) {
                    openInsertPromotionDialog(promotionModel);
                    layoutPromotionInsertDialogBinding.text1.setText("Cập nhật khuyễn mãi");
                }
                else {
                    openInsertPromotionDialog(promotionModel);
                    layoutPromotionInsertDialogBinding.text1.setText("Thông tin khuyến mãi");
                    layoutPromotionInsertDialogBinding.buttonAddCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDeleteClick(PromotionModel promotionModel, int position) {

            }

            @Override
            public void onChangeStatus(PromotionModel promotionModel) {

            }
        });

        binding.recycleView.setAdapter(promotionAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(dividerItemDecoration);
    }

    private void setListeners() {
        binding.buttonInsertPromotion.setOnClickListener(v -> {
            openInsertPromotionDialog(null);
        });
    }

    private AlertDialog dialog;

    private LayoutPromotionInsertDialogBinding layoutPromotionInsertDialogBinding;

    private void openInsertPromotionDialog(PromotionModel promotionModel) {
        layoutPromotionInsertDialogBinding =
                LayoutPromotionInsertDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutPromotionInsertDialogBinding.getRoot());
        builder.setCancelable(false);

        if (promotionModel != null) {
            layoutPromotionInsertDialogBinding.inputName.setText(promotionModel.getName());
            layoutPromotionInsertDialogBinding.inputInfo.setText(promotionModel.getInfo());
            layoutPromotionInsertDialogBinding.inputValue.setText(promotionModel.getValue());
        }

        layoutPromotionInsertDialogBinding.buttonAddCategory.setOnClickListener(v -> {

        });
        layoutPromotionInsertDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog = builder.create();
        dialog.show();
    }


}