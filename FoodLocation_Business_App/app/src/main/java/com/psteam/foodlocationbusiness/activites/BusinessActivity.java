package com.psteam.foodlocationbusiness.activites;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.SearchReserveTableAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityBusinessBinding;
import com.psteam.foodlocationbusiness.fragments.ManagerReserveTableFragment;
import com.psteam.foodlocationbusiness.fragments.SearchReserveTableBottomSheet;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Get.getReserveTable;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.Get.messageInfoRes;
import com.psteam.lib.Models.Input.SearchInput;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class BusinessActivity extends AppCompatActivity {
    private ActivityBusinessBinding binding;
    public static TextView resName;
    private RoundedImageView resImg;
    private DataTokenAndUserId dataTokenAndUserId;
    public Socket mSocket;

    {
        try {
            mSocket = IO.socket(setupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dataTokenAndUserId = new DataTokenAndUserId(getApplicationContext());
        init();
        setListeners();
    }

    private void setListeners() {

        resName = binding.navigationView.getHeaderView(0).findViewById(R.id.textViewResName);
        resImg = binding.navigationView.getHeaderView(0).findViewById(R.id.textViewImageLogoRestaurant);

        getInfoRes();

        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        binding.navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.menuLogOut) {
                            DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
                            dataTokenAndUserId.saveUserId(null);
                            dataTokenAndUserId.saveToken(null);

                            setupSocket.signOut();
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finishAffinity();
                        }

                        // You need this line to handle the navigation
                        boolean handled = NavigationUI.onNavDestinationSelected(menuItem, navController);
                        if (handled) {
                            ViewParent parent = binding.navigationView.getParent();
                            if (parent instanceof DrawerLayout) {
                                ((DrawerLayout) parent).closeDrawer(binding.navigationView);
                            }
                        }
                        return handled;
                    }
                });


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                binding.textViewTitle.setText(navDestination.getLabel());
            }
        });

        binding.textViewSearch.setOnClickListener(v -> {
            clickOpenBottomSheetSearchFragment();
        });

    }

    private ArrayList<getReserveTable> reserveTables;

    private SearchReserveTableBottomSheet searchReserveTableBottomSheet;

    private void clickOpenBottomSheetSearchFragment() {
        searchReserveTableBottomSheet = new SearchReserveTableBottomSheet(new SearchReserveTableAdapter.SearchReserveTableListeners() {
            @Override
            public void onConfirmClicked(getReserveTable reserveTable) {
                updateReserveTable(4, reserveTable);
            }

            @Override
            public void onConfirmedClicked(getReserveTable reserveTable) {
                String tabProcessing="";
                if(reserveTable.getStatus().equals("2")){
                    tabProcessing="tabCancel";
                }else if(reserveTable.getStatus().equals("4")) {
                    tabProcessing = "tabConfirmed";
                }

                Intent intent = new Intent(getApplicationContext(), ReserveTableDetailsActivity.class);
                intent.putExtra("search", "search");
                intent.putExtra("response", reserveTable);
                intent.putExtra("tabProcessing", tabProcessing);
                startActivityForResult(intent, 10);
            }

            @Override
            public void onAgreeClicked(getReserveTable reserveTable) {
                updateReserveTable(1, reserveTable);
            }

            @Override
            public void onDenyClicked(getReserveTable reserveTable) {
                updateReserveTable(2, reserveTable);
            }

            @Override
            public void onClicked(getReserveTable reserveTable) {
                String tabProcessing="";
                if(reserveTable.getStatus().equals("1")){
                    tabProcessing="tabProcessing";
                }else if(reserveTable.getStatus().equals("4")){
                    tabProcessing="tabConfirmed";
                }else if(reserveTable.getStatus().equals("3")){
                    tabProcessing="tabLate";
                }

                Intent intent = new Intent(getApplicationContext(), ReserveTableDetailsActivity.class);
                intent.putExtra("search", "search");
                intent.putExtra("response", reserveTable);
                intent.putExtra("tabProcessing", tabProcessing);
                startActivityForResult(intent, 10);
            }
        });

        searchReserveTableBottomSheet.show(getSupportFragmentManager(), searchReserveTableBottomSheet.getTag());
    }


    private void getInfoRes() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageInfoRes> call = serviceAPI_lib.getInfoRestaurant(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageInfoRes>() {
            @Override
            public void onResponse(Call<messageInfoRes> call, Response<messageInfoRes> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    resName.setText(response.body().getRes().getName());
                    Glide.with(getApplication()).load(response.body().getRes().getPic()).into(resImg);
                } else if (response.body() != null && response.body().getStatus() == 3) {
                    startActivity(new Intent(BusinessActivity.this, SignInActivity.class));
                }
            }

            @Override
            public void onFailure(Call<messageInfoRes> call, Throwable t) {

            }
        });
    }

    private void init() {
        setFullScreen();
        setFCM();
    }

    private void updateReserveTable(int code, getReserveTable reserveTable) {
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updateReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTable.getReserveTableId(), code);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    //xác nhận phiếu
                    if (code == 1) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), reserveTable.getUserId(), "Thông báo đặt bàn", new BodySenderFromRes(BusinessActivity.resName.getText() + "" + " đã xác nhận đơn đặt bàn của bạn", reserveTable.getReserveTableId()));
                        setupSocket.reserveTable(message);
                    } else if (code == 2) {
                        MessageSenderFromRes message = new MessageSenderFromRes(dataTokenAndUserId.getUserId(), reserveTable.getUserId(), "Thông báo đặt bàn", new BodySenderFromRes(BusinessActivity.resName.getText() + "" + " đã từ chối đơn đặt bàn của bạn", reserveTable.getReserveTableId()));
                        setupSocket.reserveTable(message);
                    }

                    ManagerReserveTableFragment.getQuantityByCode(0);
                    ManagerReserveTableFragment.getQuantityByCode(1);
                    ManagerReserveTableFragment.getQuantityByCode(3);
                    ManagerReserveTableFragment.getQuantityByCode(4);

                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Câp nhật phiếu đặt bàn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
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

    //FCM
    private void setFCM() {
        // set notification FCM
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed Successfully";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.e("Notification form FCM", msg);
                    }
                });
    }

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sender = data.optString("sender");
                    String title = data.optString("title");

                    JSONObject body = data.optJSONObject("body");
                    String reserveTableId = body.optString("reserveTableId");
                    int quantity = body.optInt("quantity");
                    String time = body.optString("time");
                    String restaurantId = body.optString("restaurantId");
                    String name = body.optString("name");
                    String phone = body.optString("phone");
                    String promotionId = body.optString("promotionId");
                    String note = body.optString("note");

                    //receiver.setText(sender+": "+body);
                }
            });
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 11) {
            ManagerReserveTableFragment.getQuantityByCode(0);
            ManagerReserveTableFragment.getQuantityByCode(1);
            ManagerReserveTableFragment.getQuantityByCode(3);
            ManagerReserveTableFragment.getQuantityByCode(4);
            SearchReserveTableBottomSheet.updateSearch();
        }
    }
}
