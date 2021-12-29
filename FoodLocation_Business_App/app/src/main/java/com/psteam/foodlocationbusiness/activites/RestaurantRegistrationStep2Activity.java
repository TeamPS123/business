package com.psteam.foodlocationbusiness.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.psteam.foodlocationbusiness.R;

import com.psteam.foodlocationbusiness.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityRestaurantRegistrationStep2Binding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
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

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class RestaurantRegistrationStep2Activity extends AppCompatActivity {

    private ActivityRestaurantRegistrationStep2Binding binding;
    private ArrayList<Uri> uris;
    private List<String> pathList;
    private ImageRestaurantAdapter imageRestaurantAdapter;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
        binding = ActivityRestaurantRegistrationStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        verifyStorePermission(RestaurantRegistrationStep2Activity.this);

        init();
        setListeners();

    }

    private void init() {
        uris = new ArrayList<>();
        imageRestaurantAdapter = new ImageRestaurantAdapter(uris, new ImageRestaurantAdapter.ImageResListeners() {
            @Override
            public void onRemoveClick(int position, Uri uri) {
                uris.remove(position);
                pathList.remove(getRealPathFromURI(uri));
                imageRestaurantAdapter.notifyItemRemoved(position);
                if (imageRestaurantAdapter.getItemCount() >= 5) {
                    binding.textviewAddImage.setVisibility(View.GONE);
                } else if (imageRestaurantAdapter.getItemCount() <= 0) {
                    binding.buttonNextStep.setVisibility(View.GONE);
                } else {
                    binding.textviewAddImage.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.recycleViewImage.setAdapter(imageRestaurantAdapter);
    }

    private void setListeners() {
        binding.textviewAddImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(result, 10);
        });

        binding.buttonNextStep.setOnClickListener(v -> {
            if (pathList != null && pathList.size() != 0) {
                addImg();
            } else {
                CustomToast.makeText(getApplicationContext(), "Vui lòng chọn ít nhất 1 hình nhà hàng của bạn", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                uris.remove(item.getGroupId());
                imageRestaurantAdapter.notifyItemRemoved(item.getGroupId());
                if (imageRestaurantAdapter.getItemCount() >= 5) {
                    binding.textviewAddImage.setVisibility(View.GONE);
                } else {
                    binding.textviewAddImage.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pathList = new ArrayList<>();

        if (requestCode == 10) {
            ActivityResult result = new ActivityResult(resultCode, data);

            if (uris.size() >= 2 && uris.get(uris.size() - 1) == null) {
                uris.remove(uris.size() - 1);
            }

            if (result.getResultCode() == RESULT_OK) {
                if (result.getData().getClipData() == null) {
                    Uri imageUri = result.getData().getData();
                    pathList.add(getRealPathFromURI(imageUri));
                    uris.add(imageUri);
                } else {
                    int count = (result.getData().getClipData().getItemCount() + imageRestaurantAdapter.getItemCount());
                    if (count > 5) {
                        int c = (5 - imageRestaurantAdapter.getItemCount()) >= result.getData().getClipData().getItemCount() ? result.getData().getClipData().getItemCount() : (5 - imageRestaurantAdapter.getItemCount());
                        for (int i = 0; i < c; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            pathList.add(getRealPathFromURI(imageUri));
                            uris.add(imageUri);
                        }
                        CustomToast.makeText(getApplicationContext(), "Chỉ được chọn tối đa 5 hình ảnh", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
                    } else {
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            pathList.add(getRealPathFromURI(imageUri));
                            uris.add(imageUri);
                        }
                    }
                }

                imageRestaurantAdapter.notifyDataSetChanged();
                if (imageRestaurantAdapter.getItemCount() > 0) {
                    binding.recycleViewImage.setVisibility(View.VISIBLE);
                    binding.buttonNextStep.setVisibility(View.VISIBLE);
                    if (imageRestaurantAdapter.getItemCount() == 5) {
                        binding.textviewAddImage.setVisibility(View.GONE);
                    } else {
                        binding.textviewAddImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.recycleViewImage.setVisibility(View.GONE);
                    binding.buttonNextStep.setVisibility(View.GONE);
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private static void verifyStorePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void addImg() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
        List<MultipartBody.Part> photo = new ArrayList<>();

        for (int i = 0; i < pathList.size(); i++) {
            File f = new File(pathList.get(i));
            RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            photo.add(MultipartBody.Part.createFormData("photo", f.getName(), photoContext));
        }

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI.addImgRes(dataTokenAndUserId.getToken(), photo, dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    Intent intent = new Intent(getApplicationContext(), BusinessActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(RestaurantRegistrationStep2Activity.this, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}