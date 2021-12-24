package com.psteam.foodlocationbusiness.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.BusinessReserveTableAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerReserveTableBinding;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.Constants;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

public class ManagerReserveTableFragment extends Fragment {

    private FragmentManagerReserveTableBinding binding;
    private static DataTokenAndUserId dataTokenAndUserId;
    public Socket mSocket;

    {
        try {
            mSocket = IO.socket(setupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerReserveTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        dataTokenAndUserId = new DataTokenAndUserId(getContext());
        initTabReserveTable();
        getQuantityByCode(0);
        getQuantityByCode(1);
        getQuantityByCode(3);
        getQuantityByCode(4);
        socket();
    }

    private static BadgeDrawable badgeDrawableTabPending;
    private static BadgeDrawable badgeDrawableTabProcessing;
    private static BadgeDrawable badgeDrawableTabConfirmed;
    private static BadgeDrawable badgeDrawableTabLate;

    public static void updateCountTabPending(int count) {
        badgeDrawableTabPending.setNumber(count);
    }

    public static void updateCountTabProcessing(int count) {
        badgeDrawableTabProcessing.setNumber(count);
    }

    public static void updateCountTabConfirmed(int count) {
        badgeDrawableTabConfirmed.setNumber(count);
    }

    public static void updateCountTabLate(int count) {
        badgeDrawableTabLate.setNumber(count);
    }

    public static void updateCountTabPendingAndProcessing(int countPending) {
        badgeDrawableTabPending.setNumber(countPending);
        int temp = (badgeDrawableTabProcessing.getNumber() + 1);
        badgeDrawableTabProcessing.setNumber(temp);
    }

    public static void updateCountTabLateAndProcessing(int countPending) {
        badgeDrawableTabLate.setNumber(countPending);
        int temp = (badgeDrawableTabProcessing.getNumber() + 1);
        badgeDrawableTabProcessing.setNumber(temp);
    }

    public static void updateCountTabProcessingAndConfirmed(int countPending) {
        badgeDrawableTabProcessing.setNumber(countPending);
        int temp = (badgeDrawableTabConfirmed.getNumber() + 1);
        badgeDrawableTabConfirmed.setNumber(temp);
    }

    private void initTabReserveTable() {

        binding.viewPager.setAdapter(new BusinessReserveTableAdapter(getActivity()));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position) {
                    case Constants.TAB_POSITION_PENDING: {
                        tab.setText("Chưa duyệt");
                        tab.setIcon(R.drawable.ic_round_pending_actions);
                        badgeDrawableTabPending = tab.getOrCreateBadge();
                        badgeDrawableTabPending.setBackgroundColor(
                                ContextCompat.getColor(getActivity(), R.color.black)
                        );
                        badgeDrawableTabPending.setMaxCharacterCount(3);
                        badgeDrawableTabPending.setVisible(true);
                        break;
                    }
                    case Constants.TAB_POSITION_PROCESSING: {
                        tab.setText("Đã duyệt");
                        tab.setIcon(R.drawable.ic_baseline_assignment);
                        badgeDrawableTabProcessing = tab.getOrCreateBadge();
                        badgeDrawableTabProcessing.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawableTabPending.setMaxCharacterCount(3);
                        badgeDrawableTabProcessing.setVisible(true);
                        break;
                    }
                    case Constants.TAB_POSITION_CONFIRMED: {
                        tab.setText("Hoàn tất");
                        tab.setIcon(R.drawable.ic_round_check_circle);

                        badgeDrawableTabConfirmed = tab.getOrCreateBadge();
                        badgeDrawableTabConfirmed.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawableTabConfirmed.setVisible(true);
                        badgeDrawableTabConfirmed.setMaxCharacterCount(3);
                        break;
                    }
                    case Constants.TAB_POSITION_LATE: {
                        tab.setText("Quá hạn");
                        tab.setIcon(R.drawable.ic_round_assignment_late_24);
                        badgeDrawableTabLate = tab.getOrCreateBadge();
                        badgeDrawableTabLate.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawableTabLate.setVisible(true);
                        badgeDrawableTabLate.setMaxCharacterCount(3);
                        break;
                    }
                }
            }
        }
        );

        tabLayoutMediator.attach();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });

    }

    private void socket() {
        setupSocket.mSocket = mSocket;
        setupSocket.mSocket.connect();

        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        setupSocket.signIn(dataTokenAndUserId.getUserId());
    }

    private void getQuantityNoConfirm() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getContext());

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.getQuantityReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), 0);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body().getStatus() == 1) {

                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {

            }
        });

    }

    public static int getQuantityInTab(int code) {
        switch (code) {
            case 0:
                return badgeDrawableTabPending.getNumber();
            case 1:
                return badgeDrawableTabProcessing.getNumber();
            case 4:
                return badgeDrawableTabConfirmed.getNumber();
            default:
                return badgeDrawableTabLate.getNumber();

        }
    }


    // 0: Chưa duyệt, 1: Đã duyệt, 2:Từ chối:, 3: Quá hạn, 4: Hoàn tất
    public static void getQuantityByCode(int code) {
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.getQuantityReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), code);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    switch (code) {
                        case 0:
                            updateCountTabPending(Integer.parseInt(response.body().getId().trim()));
                            break;
                        case 1:
                            updateCountTabProcessing(Integer.parseInt(response.body().getId().trim()));
                            break;
                        case 4:
                            updateCountTabConfirmed(Integer.parseInt(response.body().getId().trim()));
                            break;
                        default:
                            updateCountTabLate(Integer.parseInt(response.body().getId().trim()));
                            break;

                    }
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

}
