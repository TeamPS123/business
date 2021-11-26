package com.psteam.foodlocationbusiness.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.psteam.foodlocationbusiness.fragments.MenuFragment;
import com.psteam.foodlocationbusiness.ultilities.Para;
import com.psteam.lib.Models.Get.getMenu;

import java.util.ArrayList;


public class MenuFragmentAdapter extends FragmentStatePagerAdapter {
    private int numberTabs;
    private ArrayList<String> menuId;
    private ArrayList<getMenu> menu;

    public MenuFragmentAdapter(@NonNull FragmentManager fm, int numberTabs, ArrayList<String> menuId, ArrayList<getMenu> menu) {
        super(fm);
        this.numberTabs = numberTabs;
        Para.numberTabs=numberTabs;
        this.menuId = menuId;
        this.menu = menu;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("menuId", menuId.get(position));
        bundle.putSerializable("menu", menu.get(position));
        Fragment fragment = MenuFragment.newInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return Para.numberTabs;
    }
}
