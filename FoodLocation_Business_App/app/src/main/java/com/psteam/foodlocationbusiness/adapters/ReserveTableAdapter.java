package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.ReservedTableItemContainerBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

import java.util.List;

public class ReserveTableAdapter extends RecyclerView.Adapter<ReserveTableAdapter.ReserveTableViewHolder> {
    private final List<BodySenderFromUser> reserveTableList;
    private final ReserveTableListeners reserveTableListeners;

    public ReserveTableAdapter(List<BodySenderFromUser> reserveTableList, ReserveTableListeners reserveTableListeners) {
        this.reserveTableList = reserveTableList;
        this.reserveTableListeners = reserveTableListeners;
    }

    @NonNull
    @Override
    public ReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReserveTableViewHolder(ReservedTableItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveTableViewHolder holder, int position) {
        holder.setData(reserveTableList.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTableList != null ? reserveTableList.size() : 0;
    }

    class ReserveTableViewHolder extends RecyclerView.ViewHolder {

        private ReservedTableItemContainerBinding binding;

        public ReserveTableViewHolder(@NonNull ReservedTableItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(BodySenderFromUser reserveTable) {
            binding.textViewFullName.setText(reserveTable.getName());
            binding.textViewNumberPeople.setText(reserveTable.getPhone());
            binding.textViewDateReserve.setText(reserveTable.getTime());
            binding.textViewNumberPeople.setText(String.format("Đặt chỗ cho %d người", reserveTable.getQuantity()));

            binding.buttonConfirmed.setOnClickListener(v -> {
                reserveTableListeners.onConfirmClicked(reserveTable, getAdapterPosition());
            });

            binding.buttonDeny.setOnClickListener(v -> {
                reserveTableListeners.onDenyClicked(reserveTable, getAdapterPosition());
            });

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable, getAdapterPosition());
            });

        }
    }

    public interface ReserveTableListeners {
        void onConfirmClicked(BodySenderFromUser reserveTable, int position);

        void onDenyClicked(BodySenderFromUser reserveTable, int position);

        void onClicked(BodySenderFromUser reserveTable, int position);
    }


    public static class ReserveTable {
        private String name;
        private String phone;
        private String dateReserve;
        private int numberPeople;

        public ReserveTable(String name, String phone, String dateReserve, int numberPeople) {
            this.name = name;
            this.phone = phone;
            this.dateReserve = dateReserve;
            this.numberPeople = numberPeople;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDateReserve() {
            return dateReserve;
        }

        public void setDateReserve(String dateReserve) {
            this.dateReserve = dateReserve;
        }

        public int getNumberPeople() {
            return numberPeople;
        }

        public void setNumberPeople(int numberPeople) {
            this.numberPeople = numberPeople;
        }
    }
}
