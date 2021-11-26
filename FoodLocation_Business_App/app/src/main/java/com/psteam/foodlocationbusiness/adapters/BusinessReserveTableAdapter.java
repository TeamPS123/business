package com.psteam.foodlocationbusiness.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.psteam.foodlocationbusiness.fragments.ConfirmedReservedTableFragment;
import com.psteam.foodlocationbusiness.fragments.LateReservedTableFragment;
import com.psteam.foodlocationbusiness.fragments.PendingReservedTableFragment;
import com.psteam.foodlocationbusiness.fragments.ProcessingReservedTableFragment;
import com.psteam.foodlocationbusiness.ultilities.Constants;

public class BusinessReserveTableAdapter extends FragmentStateAdapter {


    public BusinessReserveTableAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case Constants.TAB_POSITION_PENDING:
                return new PendingReservedTableFragment();
            case Constants.TAB_POSITION_PROCESSING:
                return new ProcessingReservedTableFragment();
            case Constants.TAB_POSITION_CONFIRMED:
                return new ConfirmedReservedTableFragment();
            default:
                return new LateReservedTableFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
