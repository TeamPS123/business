package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.databinding.ReservedTableItemConfirmBinding;
import com.psteam.foodlocationbusiness.databinding.ReservedTableItemContainerBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

import java.util.List;

public class ReserveTableConfirmAdapter extends RecyclerView.Adapter<ReserveTableConfirmAdapter.ReserveTableViewHolder>{
    private final List<BodySenderFromUser> reserveTableList;
    private final ReserveTableListeners reserveTableListeners;

    public ReserveTableConfirmAdapter(List<BodySenderFromUser> reserveTableList, ReserveTableListeners reserveTableListeners) {
        this.reserveTableList = reserveTableList;
        this.reserveTableListeners = reserveTableListeners;
    }

    @NonNull
    @Override
    public ReserveTableConfirmAdapter.ReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReserveTableConfirmAdapter.ReserveTableViewHolder(ReservedTableItemConfirmBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveTableConfirmAdapter.ReserveTableViewHolder holder, int position) {
        holder.setData(reserveTableList.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTableList != null ? reserveTableList.size() : 0;
    }

    class ReserveTableViewHolder extends RecyclerView.ViewHolder {

        private ReservedTableItemConfirmBinding binding;

        public ReserveTableViewHolder(@NonNull ReservedTableItemConfirmBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(BodySenderFromUser reserveTable) {
            binding.textViewFullName.setText(reserveTable.getName());
            binding.textViewNumberPeople.setText(reserveTable.getPhone());
            binding.textViewDateReserve.setText(reserveTable.getTime());
            binding.textViewNumberPeople.setText(String.format("Đặt chỗ cho %d người", reserveTable.getQuantity()));

            binding.buttonConfirmed.setVisibility(View.GONE);

            binding.buttonConfirmed.setOnClickListener(v -> {
                reserveTableListeners.onConfirmClicked(reserveTable, getAdapterPosition());
            });

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable, getAdapterPosition());
            });

        }
    }

    public interface ReserveTableListeners {
        void onConfirmClicked(BodySenderFromUser reserveTable, int position);

        void onClicked(BodySenderFromUser reserveTable, int position);
    }
}
