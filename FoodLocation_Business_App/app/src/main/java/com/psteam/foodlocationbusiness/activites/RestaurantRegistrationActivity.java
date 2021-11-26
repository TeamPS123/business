package com.psteam.foodlocationbusiness.activites;

import static com.psteam.foodlocationbusiness.ultilities.RetrofitClient.getRetrofit;
import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityRestaurantRegistrationBinding;
import com.psteam.foodlocationbusiness.models.DistrictModel;
import com.psteam.foodlocationbusiness.models.ProvinceModel;
import com.psteam.foodlocationbusiness.models.WardModel;
import com.psteam.foodlocationbusiness.services.ServiceAPI;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Get.getCategoryRes;
import com.psteam.lib.Models.Get.messageCategoryRes;
import com.psteam.lib.Models.Insert.insertRestaurant;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegistrationActivity extends AppCompatActivity {

    private ActivityRestaurantRegistrationBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    private ArrayList<ProvinceModel> provinceModels;
    private ArrayList<DistrictModel> districtModels;
    private ArrayList<getCategoryRes> getCategoryResArrayList;
    private ArrayAdapter<DistrictModel> districtAdapter;
    private ArrayAdapter<ProvinceModel> provinceAdapter;
    private ArrayAdapter<getCategoryRes> categoryResArrayAdapter;
    private ArrayAdapter<WardModel> wardAdapter;
    private ArrayList<WardModel> wardModels;
    private ProvinceModel provinceModel;
    private DistrictModel districtModel;
    private WardModel wardModel;
    private getCategoryRes getCategoryRes;
    private String LatLng;
    private int categoryResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRestaurantRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        provinceModel = new ProvinceModel();
        districtModel = new DistrictModel();
        wardModel = new WardModel();
        districtModels = new ArrayList<>();

        getProvinces();
        getProvince("0");
        getDistrict("0");
        GetCategoryRes();
    }

    private void setFullScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void getProvinces() {
        provinceModels = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<ArrayList<ProvinceModel>> call = serviceAPI.GetProvinces();
        call.enqueue(new Callback<ArrayList<ProvinceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProvinceModel>> call, Response<ArrayList<ProvinceModel>> response) {
                provinceModels = response.body();
                ArrayAdapter<ProvinceModel> cityAdapter = new ArrayAdapter<ProvinceModel>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provinceModels);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerCity.setAdapter(cityAdapter);
                binding.spinnerCity.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        ProvinceModel provinceModelItem = (ProvinceModel) item;
                        // If user change the default selection
                        // First item is disable and it is used for hint
                        if (position >= 0) {
                            binding.spinnerCity.setError(null);
                            getProvince(provinceModelItem.getCode());
                            provinceModel = provinceModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<ProvinceModel>> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void getProvince(String code) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<ProvinceModel> call = serviceAPI.GetDistricts(code);
        call.enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                districtModels.add(new DistrictModel("-1", "huyen", "-1", "Quận / Huyện", "-1"));
                if (response.body() != null && response.body().getDistricts().size() > 0) {
                    districtModels = response.body().getDistricts();
                }
                districtAdapter = new ArrayAdapter<DistrictModel>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, districtModels);
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerDistrict.setAdapter(districtAdapter);
                binding.spinnerDistrict.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        DistrictModel districtModelItem = (DistrictModel) item;
                        if (position >= 0) {
                            binding.spinnerDistrict.setError(null);
                            getDistrict(districtModelItem.getCode());
                            districtModel = districtModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void getDistrict(String code) {
        wardModels = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<DistrictModel> call = serviceAPI.GetWards(code);
        call.enqueue(new Callback<DistrictModel>() {
            @Override
            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
                wardModels.add(new WardModel("-1", "phuong", "-1", "-1", "Phường / Xã"));
                if (response.body() != null && response.body().getWards().size() > 0) {
                    wardModels = response.body().getWards();
                }
                ArrayAdapter<WardModel> wardAdapters = new ArrayAdapter<WardModel>(getApplicationContext(), android.R.layout.simple_list_item_1, wardModels);
                wardAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerWard.setAdapter(wardAdapters);
                binding.spinnerWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        WardModel wardModelItem = (WardModel) item;
                        if (position >= 0) {
                            binding.spinnerWard.setError(null);
                            wardModel = wardModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<DistrictModel> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void setListeners() {

        binding.buttonNextStep.setOnClickListener(v -> {
            loading(true);
            if (isValidRestaurantRegistration()) {
                addRes();
            }
            loading(false);
        });

        TimePickerDialog.OnTimeSetListener onTimeOpenSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", new Locale("vi", "VN"));
                binding.inputTimeOpen.setText(sdf.format(myCalendar.getTime()));
            }
        };

        TimePickerDialog.OnTimeSetListener onTimeCloseSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", new Locale("vi", "VN"));
                binding.inputTimeClose.setText(sdf.format(myCalendar.getTime()));
            }
        };

        binding.inputTimeOpen.setOnClickListener(v -> {
            new TimePickerDialog(RestaurantRegistrationActivity.this, onTimeOpenSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE), true).show();
            binding.inputTimeOpen.setError(null);
        });

        binding.inputTimeClose.setOnClickListener(v -> {
            new TimePickerDialog(RestaurantRegistrationActivity.this, onTimeCloseSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE), true).show();
            binding.inputTimeClose.setError(null);
        });

        binding.inputGetLocation.setOnClickListener(v -> {
            setFullScreen();
            Intent intent = new Intent(getApplicationContext(), MapRegistrationActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 2) {
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                LatLng = latitude+","+longitude;
                binding.inputGetLocation.setText("Lấy vị trí thành công");
            }
        }
    }

    private boolean isValidRestaurantRegistration() {
        if (binding.inputNameRestaurant.getText().toString().trim().isEmpty()) {
            binding.inputNameRestaurant.setError("Tên nhà hàng không được để trống");
            return false;
        } else if (provinceModel.getCode() == null) {
            binding.spinnerCity.setError("Tỉnh, thành phố không hợp lệ");
            return false;
        } else if (districtModel.getCode() == null || districtModel.getCode() == "-1") {
            binding.spinnerDistrict.setError("Quận huyện không hợp lệ");
            return false;
        } else if (wardModel.getCode() == null || wardModel.getCode() == "-1") {
            binding.spinnerWard.setError("Phường xã không hợp lệ");
            return false;
        } else if (binding.inputLine.getText().toString().trim().isEmpty()) {
            binding.inputLine.setError("Địa chỉ kinh doanh không được bỏ trống");
            return false;
        } else if (binding.inputTimeOpen.getText().toString().trim().isEmpty()) {
            binding.inputTimeOpen.setError("Giờ mở cửa không được để trống");
            return false;
        } else if (binding.inputTimeClose.getText().toString().trim().isEmpty()) {
            binding.inputTimeClose.setError("Giờ đóng cửa không được để trống");
            return false;
        } else {
            return true;
        }
    }

    private void addRes(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        insertRestaurant restaurant = new insertRestaurant();
        restaurant.setName(binding.inputNameRestaurant.getText()+"");
        restaurant.setPhone(binding.inputPhoneNumber.getText()+"");
        restaurant.setCity(binding.spinnerCity.getText() +"");
        restaurant.setDistrict(binding.spinnerDistrict.getText()+"");
        restaurant.setCategoryResId(categoryResId);
        restaurant.setLine(binding.inputLine.getText()+", "+ binding.spinnerWard.getText());
        restaurant.setOpenTime(binding.inputTimeOpen.getText()+"");
        restaurant.setCloseTime(binding.inputTimeClose.getText()+"");
        restaurant.setUserId(dataTokenAndUserId.getUserId());
        restaurant.setLongLat(LatLng);

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.addRestaurant(dataTokenAndUserId.getToken(), restaurant);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if(response.body().getStatus() == 1){
                    dataTokenAndUserId.saveRestaurantId(response.body().getId());

                    Intent intent = new Intent(RestaurantRegistrationActivity.this, RestaurantRegistrationStep2Activity.class);

                    startActivity(intent);

                }
                Toast.makeText(RestaurantRegistrationActivity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
                loading(false);
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(RestaurantRegistrationActivity.this, "Thêm nhà hàng thất bại", Toast.LENGTH_SHORT).show();
                loading(false);
            }
        });
    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonNextStep.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonNextStep.setVisibility(View.VISIBLE);
        }
    }

    private  void GetCategoryRes(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageCategoryRes> call = serviceAPI_lib.getCategoryRes();
        call.enqueue(new Callback<messageCategoryRes>() {
            @Override
            public void onResponse(Call<messageCategoryRes> call, Response<messageCategoryRes> response) {
                if(response.body().getStatus() == 1){
                    if(response.body().getCategoryResList().size() > 0){
                        getCategoryResArrayList = response.body().getCategoryResList();
                    }

                    ArrayAdapter<getCategoryRes> getCategoryResArrayAdapter = new ArrayAdapter<getCategoryRes>(getApplicationContext(), android.R.layout.simple_list_item_1, getCategoryResArrayList);
                    getCategoryResArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerCategoryRes.setAdapter(getCategoryResArrayAdapter);
                    binding.spinnerCategoryRes.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                            getCategoryRes wardModelItem = (getCategoryRes) item;
                            if (position >= 0) {
                                binding.spinnerCategoryRes.setError(null);
                                getCategoryRes = wardModelItem;
                                categoryResId = wardModelItem.getId();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<messageCategoryRes> call, Throwable t) {

            }
        });
    }
}