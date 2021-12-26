package com.psteam.foodlocationbusiness.fragments;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.SearchReserveTableAdapter;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.lib.Models.Get.getReserveTable;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.Input.SearchInput;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchReserveTableBottomSheet extends BottomSheetDialogFragment {

    private static List<getReserveTable> reserveTables;
    private SearchReserveTableAdapter.SearchReserveTableListeners reserveTableListeners;
    private static SearchReserveTableAdapter searchReserveTableAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mBehavior;
    private static DataTokenAndUserId dataTokenAndUserId;

    private RecyclerView recyclerView;
    private EditText editText;
    private static LottieAnimationView lottieAnimationView;

    public SearchReserveTableBottomSheet(SearchReserveTableAdapter.SearchReserveTableListeners reserveTableListeners) {
        this.reserveTableListeners = reserveTableListeners;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = bottomSheetDialog.getLayoutInflater().inflate(R.layout.layout_search_reserve_table_bottom_sheet, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        editText = view.findViewById(R.id.inputSearch);
        ImageView imageViewClose = view.findViewById(R.id.imageViewCloseFragment);
        imageViewClose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        lottieAnimationView = view.findViewById(R.id.animationLoading);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loading(true);
                runThread(s.toString().trim(), 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
                AfterTextChange = s.toString().trim();
            }
        });

        recyclerView = view.findViewById(R.id.recycleViewSearchReserveTable);
        recyclerView.setHasFixedSize(true);
        searchReserveTableAdapter = new SearchReserveTableAdapter(new SearchReserveTableAdapter.SearchReserveTableListeners() {
            @Override
            public void onConfirmClicked(getReserveTable reserveTable) {
                reserveTableListeners.onConfirmClicked(reserveTable);
                getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), AfterTextChange));
            }

            @Override
            public void onConfirmedClicked(getReserveTable reserveTable) {
                reserveTableListeners.onConfirmedClicked(reserveTable);
            }

            @Override
            public void onAgreeClicked(getReserveTable reserveTable) {
                reserveTableListeners.onAgreeClicked(reserveTable);
                getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), AfterTextChange));
            }

            @Override
            public void onDenyClicked(getReserveTable reserveTable) {
                reserveTableListeners.onDenyClicked(reserveTable);
                getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), AfterTextChange));
            }

            @Override
            public void onClicked(getReserveTable reserveTable) {
                reserveTableListeners.onClicked(reserveTable);
            }
        }, reserveTables, getContext());
        recyclerView.setAdapter(searchReserveTableAdapter);
        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return bottomSheetDialog;
    }

    public static void updateSearch() {
        getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), AfterTextChange));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataTokenAndUserId = new DataTokenAndUserId(getContext());
        reserveTables = new ArrayList<>();
        getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), ""));
    }

    private static void loading(boolean Loading) {
        if (Loading) {
            lottieAnimationView.setVisibility(View.VISIBLE);
        } else {
            lottieAnimationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private static String AfterTextChange;

    private void runThread(String textChange, long millis) {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(millis);
                    if (textChange.equals(AfterTextChange)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getReserveTable(new SearchInput(dataTokenAndUserId.getRestaurantId(), dataTokenAndUserId.getUserId(), textChange));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static void getReserveTable(SearchInput searchInput) {

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllReserveTable> call = serviceAPI.getReserveTable(dataTokenAndUserId.getToken(), searchInput);
        call.enqueue(new Callback<messageAllReserveTable>() {
            @Override
            public void onResponse(Call<messageAllReserveTable> call, Response<messageAllReserveTable> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    if (response.body() != null && response.body().getReserveTables().size() > 0) {
                        reserveTables.clear();
                        reserveTables.addAll(response.body().getReserveTables());
                        Collections.sort(reserveTables, new Comparator<getReserveTable>() {
                            @Override
                            public int compare(getReserveTable o1, getReserveTable o2) {
                                return o2.getReserveTableId().compareTo(o1.getReserveTableId());
                            }
                        });
                        searchReserveTableAdapter.notifyDataSetChanged();
                    }
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<messageAllReserveTable> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }
}
