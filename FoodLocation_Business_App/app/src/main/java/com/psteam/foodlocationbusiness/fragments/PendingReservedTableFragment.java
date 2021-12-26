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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.activites.BusinessActivity;
import com.psteam.foodlocationbusiness.activites.ReserveTableDetailsActivity;
import com.psteam.foodlocationbusiness.activites.RestaurantRegistrationActivity;
import com.psteam.foodlocationbusiness.activites.VerifyOTPActivity;
import com.psteam.foodlocationbusiness.adapters.ReserveTableAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentPendingReservedTableBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromRes;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.socket.models.MessageSenderFromRes;
import com.psteam.foodlocationbusiness.socket.setupSocket;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;


public class PendingReservedTableFragment extends Fragment {

    private FragmentPendingReservedTableBinding binding;

    private ReserveTableAdapter reserveTableAdapter;
    private ArrayList<BodySenderFromUser> reserveTables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingReservedTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        initReserveTable();
        socket();
    }

    private void initReserveTable() {
        reserveTables = new ArrayList<>();

        getAllReserveTable();

        reserveTableAdapter = new ReserveTableAdapter(reserveTables, new ReserveTableAdapter.ReserveTableListeners() {
            @Override
            public void onConfirmClicked(BodySenderFromUser reserveTable, int position) {
                updateReserveTable(1, reserveTable, position);
            }

            @Override
            public void onDenyClicked(BodySenderFromUser reserveTable, int position) {
                updateReserveTable(2, reserveTable, position);
            }

            @Override
            public void onClicked(BodySenderFromUser reserveTable, int position) {
                Intent intent = new Intent(getContext(), ReserveTableDetailsActivity.class);
                intent.putExtra("response", reserveTable);
                intent.putExtra("position", position);
                startActivityForResult(intent, 10);
            }
        });

        binding.recycleView.setAdapter(reserveTableAdapter);

    }

    private void socket() {
        setupSocket.mSocket.connect();
        // receiver notification when used app
        setupSocket.mSocket.on("send_notication", onNotification);
    }

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sender = data.optString("sender");
                    String title = data.optString("title");

                    JSONObject body = data.optJSONObject("body");

                    BodySenderFromUser reserveTable = new BodySenderFromUser();
                    reserveTable.setName(body.optString("name"));
                    reserveTable.setQuantity(Integer.parseInt(body.optString("quantity")));
                    reserveTable.setReserveTableId(body.optString("reserveTableId"));
                    reserveTable.setNote(body.optString("note"));
                    reserveTable.setPhone(body.optString("phone"));
                    reserveTable.setPromotionId(body.optString("promotionId"));
                    reserveTable.setRestaurantId(body.optString("restaurantId"));
                    reserveTable.setTime(body.optString("time"));
                    reserveTable.setUserId(data.optString("sender"));

                    reserveTables.add(0, reserveTable);
                    reserveTableAdapter.notifyItemInserted(0);
                    ManagerReserveTableFragment.updateCountTabPending(reserveTables.size());
                }
            });
        }
    };

    private void getAllReserveTable() {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllReserveTable> call = serviceAPI.getAllReserveTables(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId(), 0);
        call.enqueue(new Callback<messageAllReserveTable>() {
            @Override
            public void onResponse(Call<messageAllReserveTable> call, Response<messageAllReserveTable> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    if (response.body().getReserveTables().size() > 0) {
                        reserveTables.clear();
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
                        ManagerReserveTableFragment.updateCountTabPending(reserveTables.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<messageAllReserveTable> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reserveTables.size() != ManagerReserveTableFragment.getQuantityInTab(0)) {
            getAllReserveTable();
        }
    }

    private void updateReserveTable(int code, BodySenderFromUser reserveTable, int position) {
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

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

                    reserveTables.remove(position);
                    reserveTableAdapter.notifyDataSetChanged();
                    if (code == 1)
                        ManagerReserveTableFragment.updateCountTabPendingAndProcessing(reserveTables.size());
                    else
                        ManagerReserveTableFragment.updateCountTabPending(reserveTables.size());
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
            if (data.getIntExtra("code",-1) == 1)
                ManagerReserveTableFragment.updateCountTabPendingAndProcessing(reserveTables.size());
            else
                ManagerReserveTableFragment.updateCountTabPending(reserveTables.size());
        }
    }
}