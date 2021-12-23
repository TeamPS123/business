package com.psteam.foodlocationbusiness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.activites.ReserveTableDetailsActivity;
import com.psteam.foodlocationbusiness.adapters.ReserveTableAdapter;
import com.psteam.foodlocationbusiness.adapters.ReserveTableConfirmAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentPendingReservedTableBinding;
import com.psteam.foodlocationbusiness.databinding.FragmentProcessingReservedTableBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;

// đã duyệt
public class ProcessingReservedTableFragment extends Fragment {

    private FragmentProcessingReservedTableBinding binding;

    private ReserveTableConfirmAdapter reserveTableAdapter;
    private ArrayList<BodySenderFromUser> reserveTables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProcessingReservedTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        initReserveTable();
        getAllReserveTable();
    }

    private void initReserveTable() {
        reserveTables = new ArrayList<>();
        reserveTableAdapter = new ReserveTableConfirmAdapter(reserveTables, new ReserveTableConfirmAdapter.ReserveTableListeners() {
            @Override
            public void onConfirmClicked(BodySenderFromUser reserveTable, int position) {
                updateReserveTable(4, reserveTable, position);
            }

            @Override
            public void onClicked(BodySenderFromUser reserveTable, int position) {
                Intent intent = new Intent(getContext(), ReserveTableDetailsActivity.class);
                intent.putExtra("response", reserveTable);
                intent.putExtra("position", position);
                intent.putExtra("tabProcessing", "tabProcessing");
                startActivityForResult(intent, 10);
            }
        });
        binding.recycleView.setAdapter(reserveTableAdapter);
    }

    private void getAllReserveTable() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllReserveTable> call = serviceAPI.getAllReserveTables(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), 1);
        call.enqueue(new Callback<messageAllReserveTable>() {
            @Override
            public void onResponse(Call<messageAllReserveTable> call, Response<messageAllReserveTable> response) {
                if (response.body().getStatus() == 1) {
                    if (response.body().getReserveTables().size() > 0) {
                        for (int i = 0; i < response.body().getReserveTables().size(); i++) {
                            BodySenderFromUser bodySenderFromUser = new BodySenderFromUser();
                            bodySenderFromUser.setUserId(response.body().getReserveTables().get(i).getUserId());
                            bodySenderFromUser.setTime(response.body().getReserveTables().get(i).getTime());
                            bodySenderFromUser.setRestaurantId(response.body().getReserveTables().get(i).getRestaurantId());
                            bodySenderFromUser.setPromotionId(response.body().getReserveTables().get(i).getPromotionId());
                            bodySenderFromUser.setPhone(response.body().getReserveTables().get(i).getPhone());
                            bodySenderFromUser.setNote(response.body().getReserveTables().get(i).getNote());
                            bodySenderFromUser.setReserveTableId(response.body().getReserveTables().get(i).getReserveTableId());
                            bodySenderFromUser.setQuantity(response.body().getReserveTables().get(i).getQuantity());
                            bodySenderFromUser.setName(response.body().getReserveTables().get(i).getName());
                            reserveTables.add(bodySenderFromUser);
                        }
                        Collections.sort(reserveTables, new Comparator<BodySenderFromUser>() {
                            @Override
                            public int compare(BodySenderFromUser o1, BodySenderFromUser o2) {
                                return o2.getReserveTableId().compareTo(o1.getReserveTableId());
                            }
                        });
                        reserveTableAdapter.notifyDataSetChanged();
                        ManagerReserveTableFragment.updateCountTabProcessing(reserveTables.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<messageAllReserveTable> call, Throwable t) {

            }
        });
    }

    private void updateReserveTable(int code, BodySenderFromUser reserveTable, int position) {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<message> call = serviceAPI_lib.updateReserveTable(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), reserveTable.getReserveTableId(), code);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    reserveTables.remove(position);
                    reserveTableAdapter.notifyItemRemoved(position);
                    ManagerReserveTableFragment.updateCountTabProcessing(reserveTables.size());
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Toast.makeText(getContext(), "Câp nhật phiếu đặt bàn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 11) {
            int position = data.getIntExtra("positionResult", -1);
            reserveTables.remove(position);
            reserveTableAdapter.notifyItemRemoved(position);
            ManagerReserveTableFragment.updateCountTabProcessing(reserveTables.size());
        }
    }
}