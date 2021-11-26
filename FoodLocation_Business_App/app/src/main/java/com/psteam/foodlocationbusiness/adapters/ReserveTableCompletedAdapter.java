package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.databinding.ReservedTableItemCompletedBinding;
import com.psteam.foodlocationbusiness.databinding.ReservedTableItemConfirmBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

import java.util.List;

public class ReserveTableCompletedAdapter extends RecyclerView.Adapter<ReserveTableCompletedAdapter.ReserveTableViewHolder>{
    private final List<BodySenderFromUser> reserveTableList;
    private final ReserveTableListeners reserveTableListeners;

    public ReserveTableCompletedAdapter(List<BodySenderFromUser> reserveTableList, ReserveTableListeners reserveTableListeners) {
        this.reserveTableList = reserveTableList;
        this.reserveTableListeners = reserveTableListeners;
    }

    @NonNull
    @Override
    public ReserveTableCompletedAdapter.ReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReserveTableCompletedAdapter.ReserveTableViewHolder(ReservedTableItemCompletedBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveTableCompletedAdapter.ReserveTableViewHolder holder, int position) {
        holder.setData(reserveTableList.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTableList != null ? reserveTableList.size() : 0;
    }

    class ReserveTableViewHolder extends RecyclerView.ViewHolder {

        private ReservedTableItemCompletedBinding binding;

        public ReserveTableViewHolder(@NonNull ReservedTableItemCompletedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(BodySenderFromUser reserveTable) {
            binding.textViewFullName.setText(reserveTable.getName());
            binding.textViewNumberPeople.setText(reserveTable.getPhone());
            binding.textViewDateReserve.setText(reserveTable.getTime());
            binding.textViewNumberPeople.setText(String.format("Đặt chỗ cho %d người", reserveTable.getQuantity()));

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable, getAdapterPosition());
            });

        }
    }

    public interface ReserveTableListeners {
        void onClicked(BodySenderFromUser reserveTable, int position);
    }
}
