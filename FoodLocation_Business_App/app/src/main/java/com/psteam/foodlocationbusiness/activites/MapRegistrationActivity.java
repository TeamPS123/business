package com.psteam.foodlocationbusiness.activites;

import static com.psteam.foodlocationbusiness.ultilities.RetrofitClient.getRetrofitGoogleMapAPIADDRESS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ActivityMapRegistrationBinding;
import com.psteam.foodlocationbusiness.models.GeyLngLat;
import com.psteam.foodlocationbusiness.services.FetchAddressIntentServices;
import com.psteam.foodlocationbusiness.services.ServiceAPI;
import com.psteam.foodlocationbusiness.ultilities.Constants;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.Para;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapRegistrationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMapRegistrationBinding binding;

    private GoogleMap mMap;
    private View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

    }

    String address;

    private void init() {
        setFullScreen();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();


        address = getIntent().getStringExtra("address");
        checkSelfPermission();
        binding.buttonOK.setOnClickListener(v -> {
            final Intent data = new Intent();
            data.putExtra("latitude", binding.textViewLatitude.getText().toString());
            data.putExtra("longitude", binding.textViewLongitude.getText().toString());
            setResult(2, data);
            finish();
        });

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runThread(s.toString().trim(), 3000);
            }

            @Override
            public void afterTextChanged(Editable s) {
                AfterTextChange = s.toString().trim();
            }
        });
    }

    private static String AfterTextChange = "";

    private void runThread(String textChange, long millis) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(millis);
                    if (textChange.equals(AfterTextChange)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getlnglatFromAddress(textChange);
                            }
                        });
                    }
                } catch (
                        InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng position = mMap.getCameraPosition().target;
                binding.textViewLatitude.setText(String.valueOf(position.latitude));
                binding.textViewLongitude.setText(String.valueOf(position.longitude));
                Location location = new Location("providerNA");
                location.setLatitude(position.latitude);
                location.setLongitude(position.longitude);
                fetchAddressFromLatLong(location);
            }
        });
    }


    private final static int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private void checkSelfPermission() {
        resultReceiver = new AddressResultReceiver(new Handler());
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MapRegistrationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            getlnglatFromAddress(address);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
            else {
                CustomToast.makeText(getApplicationContext(), "Permission denied", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MapRegistrationActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(MapRegistrationActivity.this)
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLastLocation() != null) {
                                    double latitude =
                                            locationResult.getLastLocation().getLatitude();
                                    double longitude = locationResult.getLastLocation().getLongitude();
                                    Para.latitude = latitude;
                                    Para.longitude = longitude;

                                    Location location = new Location("providerNA");
                                    location.setLatitude(latitude);
                                    location.setLongitude(longitude);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                                } else {

                                }
                            }
                        }
                        , Looper.getMainLooper());
    }

    private ResultReceiver resultReceiver;

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                Para.currentAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                binding.textViewAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                //Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getlnglatFromAddress(String address) {
        ServiceAPI serviceAPI = getRetrofitGoogleMapAPIADDRESS().create(ServiceAPI.class);
        Call<GeyLngLat> call = serviceAPI.getLngLat(address, getString(R.string.google_map_api_key));
        call.enqueue(new Callback<GeyLngLat>() {
            @Override
            public void onResponse(Call<GeyLngLat> call, Response<GeyLngLat> response) {
                String lng = response.body().getResults().get(0).getGeometry().getLocation().getLat();
                String lat = response.body().getResults().get(0).getGeometry().getLocation().getLng();
                binding.textViewLatitude.setText(lat);
                binding.textViewLongitude.setText(lng);
                getMyLocation(Double.parseDouble(lat), Double.parseDouble(lng));

            }

            @Override
            public void onFailure(Call<GeyLngLat> call, Throwable t) {
                String s;
            }
        });
    }

    private void getMyLocation(Double lng, Double lat) {
        LatLng latLng = new LatLng(lat, lng);
        //viewPagerRestaurantMap.setCurrentItem(0,true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate,500,null);
    }
}