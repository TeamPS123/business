package com.psteam.foodlocationbusiness.adapters;

import static com.psteam.foodlocationbusiness.ultilities.Constants.coverStringToDate;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.LayoutReservedTableItemContainerBinding;
import com.psteam.foodlocationbusiness.databinding.ReservedTableItemContainerBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReserveTableLateAdapter extends RecyclerView.Adapter<ReserveTableLateAdapter.ReserveTableLateViewHolder> {
    private final List<BodySenderFromUser> reserveTableList;
    private final ReserveTableListeners reserveTableListeners;

    public ReserveTableLateAdapter(List<BodySenderFromUser> reserveTableList, ReserveTableListeners reserveTableListeners) {
        this.reserveTableList = reserveTableList;
        this.reserveTableListeners = reserveTableListeners;
    }

    @NonNull
    @Override
    public ReserveTableLateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReserveTableLateViewHolder(LayoutReservedTableItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveTableLateViewHolder holder, int position) {
        holder.setData(reserveTableList.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTableList != null ? reserveTableList.size() : 0;
    }

    class ReserveTableLateViewHolder extends RecyclerView.ViewHolder {

        private LayoutReservedTableItemContainerBinding binding;

        public ReserveTableLateViewHolder(@NonNull LayoutReservedTableItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(BodySenderFromUser reserveTable) {
            binding.textViewFullName.setText(reserveTable.getName());
            binding.textViewPhoneNumber.setText(reserveTable.getPhone());
            binding.textViewNumberPeople.setText(String.valueOf(reserveTable.getQuantity()));

            binding.textViewTime.setText(new SimpleDateFormat("hh:mm a").format(coverStringToDate(reserveTable.getTime())));
            binding.textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(coverStringToDate(reserveTable.getTime())));

            binding.buttonConfirmed.setOnClickListener(v -> {
                reserveTableListeners.onConfirmClicked(reserveTable, getAdapterPosition());
            });

            binding.buttonDeny.setOnClickListener(v -> {
                reserveTableListeners.onDenyClicked(reserveTable, getAdapterPosition());
            });

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable, getAdapterPosition());
            });

            binding.buttonDeny.setText("Huỷ");

        }
    }

    public interface ReserveTableListeners {
        void onConfirmClicked(BodySenderFromUser reserveTable, int position);

        void onDenyClicked(BodySenderFromUser reserveTable, int position);

        void onClicked(BodySenderFromUser reserveTable, int position);
    }
}
