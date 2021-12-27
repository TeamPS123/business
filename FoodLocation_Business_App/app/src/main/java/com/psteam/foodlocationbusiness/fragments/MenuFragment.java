package com.psteam.foodlocationbusiness.fragments;

import static android.app.Activity.RESULT_OK;
import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocationbusiness.adapters.ManagerFoodAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentMenuBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutInsertCategoryDialogBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutInsertFoodDialogBinding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.getCategory;
import com.psteam.lib.Models.Get.getFood;
import com.psteam.lib.Models.Get.getMenu;
import com.psteam.lib.Models.Get.messageAllCategory;
import com.psteam.lib.Models.Insert.insertFood;
import com.psteam.lib.Models.Update.updateFood;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

    private ManagerFoodAdapter managerFoodAdapter;
    private ArrayList<getFood> foods;

    private ArrayList<getCategory> categories;
    private ArrayList<Uri> uris;
    private ArrayList<String> pathList;

    private String categoryId;
    private String categoryName;

    private String menuId;
    private getMenu menu;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private DataTokenAndUserId dataTokenAndUserId;

    public static Fragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);

        menuId = getArguments().getString("menuId");
        menu = (getMenu)getArguments().getSerializable("menu");

        dataTokenAndUserId = new DataTokenAndUserId(getContext());

        verifyStorePermission(getActivity());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.buttonAddFood.setOnClickListener(v -> {
            openDialogInsertFood();
        });
    }

    private AlertDialog dialog;
    private LayoutInsertFoodDialogBinding layoutInsertFoodDialogBinding;

    private void openDialogInsertFood() {
        uris.clear();
        layoutInsertFoodDialogBinding
                = LayoutInsertFoodDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutInsertFoodDialogBinding.getRoot());

        dialog = builder.create();

        layoutInsertFoodDialogBinding.buttonBack.setOnClickListener(v -> {
            uris.clear();
            imageRestaurantAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        spinnerCategory();
        imageRestaurantAdapter = new ImageRestaurantAdapter(uris, new ImageRestaurantAdapter.ImageResListeners() {
            @Override
            public void onRemoveClick(int position, Uri uri) {

            }
        });
        layoutInsertFoodDialogBinding.recycleView.setAdapter(imageRestaurantAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(layoutInsertFoodDialogBinding.recycleView);

        layoutInsertFoodDialogBinding.circleIndicator.attachToRecyclerView(layoutInsertFoodDialogBinding.recycleView, pagerSnapHelper);

        layoutInsertFoodDialogBinding.buttonAddCategory.setOnClickListener(v -> {
            openInsertCategoryDialog();
        });

        layoutInsertFoodDialogBinding.buttonAddMenu.setOnClickListener(v -> {
            if (isValidateInsertFood(layoutInsertFoodDialogBinding.inputFoodName.getText().toString(), layoutInsertFoodDialogBinding.inputPreice.getText().toString(),
                    layoutInsertFoodDialogBinding.inputUnit.getText().toString())) {

                //api add food
                DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

                insertFood food = new insertFood();
                food.setName(layoutInsertFoodDialogBinding.inputFoodName.getText()+"");
                food.setPrice(Double.parseDouble(layoutInsertFoodDialogBinding.inputPreice.getText()+""));
                food.setUnit(layoutInsertFoodDialogBinding.inputUnit.getText()+"");
                food.setUserId(dataTokenAndUserId.getUserId());
                food.setMenuId(menuId);
                food.setCategoryId(categoryId);

                ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
                Call<message> call = serviceAPI_lib.addFood(dataTokenAndUserId.getToken(), food);
                call.enqueue(new Callback<message>() {
                    @Override
                    public void onResponse(Call<message> call, Response<message> response) {
                        Toast.makeText(getContext(), response.body().getNotification(), Toast.LENGTH_SHORT).show();

                        List<MultipartBody.Part> photo = new ArrayList<>();

                        for (int i = 0 ; i < pathList.size() ; i++){
                            File f = new File(pathList.get(i));
                            RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                            photo.add(MultipartBody.Part.createFormData("photo", f.getName(), photoContext));
                        }

                        Call<message> call1 = serviceAPI_lib.addImgFood(dataTokenAndUserId.getToken(), photo, dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), response.body().getId());
                        call1.enqueue(new Callback<message>() {
                            @Override
                            public void onResponse(Call<message> call, Response<message> response1) {
                                getFood food1 = new getFood();
                                food1.setCategoryName(categoryName);
                                food1.setFoodId(response.body().getId());
                                food1.setMenuId(menuId);
                                food1.setPrice(Double.parseDouble(layoutInsertFoodDialogBinding.inputPreice.getText()+""));
                                food1.setUnit(layoutInsertFoodDialogBinding.inputUnit.getText()+"");
                                food1.setName(layoutInsertFoodDialogBinding.inputFoodName.getText()+"");
                                food1.setStatus(true);
                                List<String> pic = new ArrayList<>();
                                pic.add(response1.body().getId());
                                food1.setPic(pic);

                                foods.add(food1);
                                managerFoodAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<message> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<message> call, Throwable t) {

                    }
                });


//                foods.add(new ManagerFoodAdapter.Food(uris, layoutInsertFoodDialogBinding.inputFoodName.getText().toString(),
//                        Double.parseDouble(layoutInsertFoodDialogBinding.inputPreice.getText().toString()), "Đồ ăn"));
//                managerFoodAdapter.notifyDataSetChanged();
//                dialog.dismiss();
            }
        });

        layoutInsertFoodDialogBinding.buttonAddImageFood.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(result, 10);
        });

        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pathList = new ArrayList<>();

        if(requestCode == 10){
            ActivityResult result = new ActivityResult(resultCode, data);

            if(result.getResultCode() == RESULT_OK){
                int count = (result.getData().getClipData().getItemCount() + imageRestaurantAdapter.getItemCount());
                if (count > 5) {
                    Toast.makeText(getActivity(), "Chỉ được chọn tối đa 5 hình ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(int i = 0; i < result.getData().getClipData().getItemCount(); i ++){
                    Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                    pathList.add(getRealPathFromURI(imageUri));

                    uris.add(imageUri);
                }

                imageRestaurantAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private static void verifyStorePermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private ImageRestaurantAdapter imageRestaurantAdapter;

    private void init() {
        if(menu == null){
            foods = new ArrayList<>();
        }else{
            foods = menu.getFoodList();
        }
        uris = new ArrayList<>();

        initFoodManagerAdapter();
    }

    private void initFoodManagerAdapter() {
        managerFoodAdapter = new ManagerFoodAdapter(foods, getContext(), new ManagerFoodAdapter.FoodListeners() {
            @Override
            public void onEditClick(getFood food, int position) {

            }

            @Override
            public void onDeleteClick(getFood food, int position) {
                delFood(food, position);
            }

            @Override
            public void onChangeStatus(getFood food, int position) {
                updateFood(food, position);
            }
        });
        binding.recycleView.setAdapter(managerFoodAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);
    }

    private ArrayAdapter<getCategory> categoryArrayAdapter;

    private void spinnerCategory() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllCategory> call = serviceAPI.getAllCategory(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageAllCategory>() {
            @Override
            public void onResponse(Call<messageAllCategory> call, Response<messageAllCategory> response) {
                categories = response.body().getCategories();

                categoryArrayAdapter = new ArrayAdapter<getCategory>(getContext()
                        , android.R.layout.simple_list_item_1, categories);
                categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                layoutInsertFoodDialogBinding.spinnerCategory.setAdapter(categoryArrayAdapter);
                layoutInsertFoodDialogBinding.spinnerCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        getCategory category= (getCategory) item;

                        categoryId = category.getId();
                        categoryName = category.getName();
                    }
                });

                layoutInsertFoodDialogBinding.spinnerCategory.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(layoutInsertFoodDialogBinding.inputFoodName.getWindowToken(), 0);
                        return false;
                    }
                });
            }

            @Override
            public void onFailure(Call<messageAllCategory> call, Throwable t) {

            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void openInsertCategoryDialog() {
        AlertDialog dialog;
        final LayoutInsertCategoryDialogBinding insertCategoryDialogBinding
                = LayoutInsertCategoryDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(insertCategoryDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        insertCategoryDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        insertCategoryDialogBinding.buttonAddCategory.setOnClickListener(v -> {

            if (isValidateInsertCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString())) {
                categories.add(new getCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString(), "1", true));
                categoryArrayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isValidateInsertCategory(String name) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Tên loại món ăn không được bỏ trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            imageRestaurantAdapter.removeImage(item.getGroupId());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private boolean isValidateInsertFood(String name, String price, String unit) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Tên món ăn không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (price.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Giá không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (unit.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Đơn vị tính không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (uris.size() == 0 || imageRestaurantAdapter.getItemCount() == 0) {
            CustomToast.makeText(getContext(), "Vui lòng thêm hình ảnh món ăn", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

    private void delFood(getFood food, int position){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.delFood(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), food.getFoodId());
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    foods.remove(position);
                    managerFoodAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }

    private void updateFood(getFood food, int position){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        updateFood food1 = new updateFood();
        food1.setFoodId(food.getFoodId());
        food1.setPrice(food.getPrice());
        food1.setStatus(!food.getStatus());
        food1.setUnit(food.getUnit());
        food1.setCategoryId(food.getCategoryId());
        food1.setName(food.getName());
        food1.setUserId(dataTokenAndUserId.getUserId());

        Call<message> call = serviceAPI_lib.updateFood(dataTokenAndUserId.getToken(), food1);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    foods.set(position, new getFood(menuId, food.getFoodId(), food1.getName(), food1.getPrice(), food1.getUnit(), food.getQuantity(), food.getCategoryId(), food.getCategoryId(), food.getPic(), food1.getStatus()));
                    managerFoodAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });
    }
}