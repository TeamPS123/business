package com.psteam.foodlocationbusiness.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.ManagerCategoryAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityManagerCategoryBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutInsertCategoryDialogBinding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.foodlocationbusiness.ultilities.Para;
import com.psteam.lib.Models.Get.getCategory;
import com.psteam.lib.Models.Get.getMenu;
import com.psteam.lib.Models.Get.messageAllCategory;
import com.psteam.lib.Models.Get.messageAllMenu;
import com.psteam.lib.Models.Insert.insertCategory;
import com.psteam.lib.Models.Insert.insertMenu;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class ManagerCategoryActivity extends AppCompatActivity {

    private ActivityManagerCategoryBinding binding;

    private ManagerCategoryAdapter managerCategoryAdapter;
    private ArrayList<getCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();

    }

    private void init() {
        setFullScreen();
        initCategoryAdapter();

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

    private void initCategoryAdapter() {
        getAllCategory();
    }

    private void setListeners() {
        binding.buttonAddCategory.setOnClickListener(v -> {
            openInsertCategoryDialog();
        });

        binding.imageClose.setOnClickListener(v -> {
            finish();
        });
    }

    private AlertDialog dialog;

    @SuppressLint("NotifyDataSetChanged")
    private void openInsertCategoryDialog() {

        final LayoutInsertCategoryDialogBinding insertCategoryDialogBinding
                = LayoutInsertCategoryDialogBinding.inflate(LayoutInflater.from(ManagerCategoryActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerCategoryActivity.this);
        builder.setView(insertCategoryDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        insertCategoryDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        insertCategoryDialogBinding.buttonAddCategory.setOnClickListener(v -> {

            if (isValidateInsertCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString())) {
                DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

                insertCategory category = new insertCategory(insertCategoryDialogBinding.inputNameCategory.getText()+"", dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());

                ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
                Call<message> call = serviceAPI.addCategory(dataTokenAndUserId.getToken(), category);
                call.enqueue(new Callback<message>() {
                    @Override
                    public void onResponse(Call<message> call, Response<message> response) {
                        if(response.body().getStatus() == 1){
                            categories.add(new getCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString(), "1", true));
                            managerCategoryAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(ManagerCategoryActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<message> call, Throwable t) {
                        Toast.makeText(ManagerCategoryActivity.this, "Thêm loại món ăn thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private boolean isValidateInsertCategory(String name) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getApplicationContext(), "Tên loại món ăn không được bỏ trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

    private void getAllCategory(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllCategory> call = serviceAPI.getAllCategory(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageAllCategory>() {
            @Override
            public void onResponse(Call<messageAllCategory> call, Response<messageAllCategory> response) {

                categories = response.body().getCategories();
                managerCategoryAdapter = new ManagerCategoryAdapter(categories);
                binding.recycleView.setAdapter(managerCategoryAdapter);

                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
                binding.recycleView.addItemDecoration(itemDecoration);

            }

            @Override
            public void onFailure(Call<messageAllCategory> call, Throwable t) {

            }
        });
    }

}