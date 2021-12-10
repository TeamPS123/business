package com.psteam.foodlocationbusiness.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.psteam.foodlocationbusiness.activites.ManagerCategoryActivity;
import com.psteam.foodlocationbusiness.adapters.MenuFragmentAdapter;
import com.psteam.foodlocationbusiness.databinding.FragmentManagerMenuBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutAddMenuNameDialogBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DataTokenAndUserId;
import com.psteam.foodlocationbusiness.ultilities.Para;
import com.psteam.lib.Models.Get.getMenu;
import com.psteam.lib.Models.Get.messageAllMenu;
import com.psteam.lib.Models.Get.messageAllReserveTable;
import com.psteam.lib.Models.Insert.insertMenu;
import com.psteam.lib.Models.message;
import com.psteam.lib.Service.ServiceAPI_lib;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.psteam.lib.RetrofitServer.getRetrofit_lib;


public class ManagerMenuFragment extends Fragment {

    private FragmentManagerMenuBinding binding;
    private MenuFragmentAdapter menuFragmentAdapter;
    private ArrayList<String> menuIdList = new ArrayList<>();
    private ArrayList<getMenu> menus = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerMenuBinding.inflate(inflater, container, false);
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.buttonAddMenu.setOnClickListener(v -> {
            openDialogAddMenu();
        });

        binding.buttonManagerCategory.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ManagerCategoryActivity.class));
        });
    }

    private void init() {
        getAllMenu();

        initMenu();
    }

    private void initMenu() {
        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
        binding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //setDynamicFragmentToTabLayout();
    }

    private void setDynamicFragmentToTabLayout() {

        menuFragmentAdapter = new MenuFragmentAdapter(getActivity().getSupportFragmentManager(), binding.tabs.getTabCount(), menuIdList, menus);
        binding.viewPager.setAdapter(menuFragmentAdapter);
        binding.viewPager.setCurrentItem(0);

        if (menuFragmentAdapter.getCount() <= 0) {
            openDialogAddMenu();
        }
    }

    private AlertDialog dialog;

    private void openDialogAddMenu() {

        final LayoutAddMenuNameDialogBinding layoutAddMenuNameDialogBinding
                = LayoutAddMenuNameDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutAddMenuNameDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        layoutAddMenuNameDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        layoutAddMenuNameDialogBinding.buttonAddMenu.setOnClickListener(v -> {
            if (layoutAddMenuNameDialogBinding.inputMenuName.getText().toString().trim().isEmpty()) {
                CustomToast.makeText(getContext(), "Tên thực đơn không được để trống", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                return;
            }

            DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

            insertMenu menu = new insertMenu(dataTokenAndUserId.getRestaurantId(), layoutAddMenuNameDialogBinding.inputMenuName.getText()+"", dataTokenAndUserId.getUserId());

            ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
            Call<message> call = serviceAPI.addMenu(dataTokenAndUserId.getToken(), menu);
            call.enqueue(new Callback<message>() {
                @Override
                public void onResponse(Call<message> call, Response<message> response) {
                    if(response.body().getStatus() == 1){
                        menuIdList.add(response.body().getId());

                        menus.add(null);

                        binding.tabs.addTab(binding.tabs.newTab().setText(layoutAddMenuNameDialogBinding.inputMenuName.getText().toString()));
                        Para.numberTabs = binding.tabs.getTabCount();
                        menuFragmentAdapter.notifyDataSetChanged();
                        binding.viewPager.setCurrentItem(binding.tabs.getTabCount() - 1);
                        dialog.dismiss();
                    }
                    Toast.makeText(getContext(), response.body().getNotification(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<message> call, Throwable t) {
                    Toast.makeText(getContext(), "Thêm thực đơn thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();


    }

    private void getAllMenu(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getActivity());

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<messageAllMenu> call = serviceAPI.getAllMenu(dataTokenAndUserId.getToken(), dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<messageAllMenu>() {
            @Override
            public void onResponse(Call<messageAllMenu> call, Response<messageAllMenu> response) {
                if (response.body() != null && response.body().getStatus() == 1) {
                    for (int i = 0; i < response.body().getMenuList().size(); i++) {
                        getMenu menu = response.body().getMenuList().get(i);
                        menuIdList.add(menu.getMenuId());
                        menus.add(menu);
                        binding.tabs.addTab(binding.tabs.newTab().setText(menu.getName()));
                        Para.numberTabs = binding.tabs.getTabCount();
                        menuFragmentAdapter = new MenuFragmentAdapter(getActivity().getSupportFragmentManager(), binding.tabs.getTabCount(), menuIdList, menus);
                        binding.viewPager.setAdapter(menuFragmentAdapter);
                    }
                    if (menus.size() <= 0) {
                        openDialogAddMenu();
                    }
                }
            }

            @Override
            public void onFailure(Call<messageAllMenu> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}