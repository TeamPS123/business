package com.psteam.foodlocationbusiness.adapters;

import static com.psteam.foodlocationbusiness.ultilities.Constants.coverStringToDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.databinding.LayoutReserveTableSearchItemContainerBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;
import com.psteam.lib.Models.Get.getReserveTable;

import java.text.SimpleDateFormat;
import java.util.List;

public class SearchReserveTableAdapter extends RecyclerView.Adapter<SearchReserveTableAdapter.SearchReserveTableViewHolder> {

    private final SearchReserveTableListeners reserveTableListeners;
    private final List<getReserveTable> reserveTableList;
    private final Context context;

    public SearchReserveTableAdapter(SearchReserveTableListeners reserveTableListeners, List<getReserveTable> reserveTableList, Context context) {
        this.reserveTableListeners = reserveTableListeners;
        this.reserveTableList = reserveTableList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchReserveTableViewHolder(LayoutReserveTableSearchItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchReserveTableViewHolder holder, int position) {
        holder.setData(reserveTableList.get(position));
    }

    @Override
    public int getItemCount() {
        return reserveTableList != null ? reserveTableList.size() : 0;
    }

    class SearchReserveTableViewHolder extends RecyclerView.ViewHolder {

        final LayoutReserveTableSearchItemContainerBinding binding;

        public SearchReserveTableViewHolder(@NonNull LayoutReserveTableSearchItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(getReserveTable reserveTable) {
            binding.textViewFullName.setText(reserveTable.getName());
            binding.textViewPhoneNumber.setText(reserveTable.getPhone());
            binding.textViewNumberPeople.setText(String.valueOf(reserveTable.getQuantity()));

            binding.textViewTime.setText(new SimpleDateFormat("hh:mm a").format(coverStringToDate(reserveTable.getTime())));
            binding.textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(coverStringToDate(reserveTable.getTime())));

            switch (reserveTable.getStatus()) {
                case "0"://Chưa duyệt
                    binding.buttonAgree.setVisibility(View.VISIBLE);
                    binding.buttonDeny.setVisibility(View.VISIBLE);
                    binding.buttonDeny.setText("Từ chối");
                    binding.buttonConfirm.setVisibility(View.GONE);
                    binding.buttonConfirmed.setVisibility(View.GONE);
                    binding.buttonAgree.setOnClickListener(v -> {
                        reserveTableListeners.onAgreeClicked(reserveTable);
                    });
                    binding.buttonDeny.setOnClickListener(v -> {
                        reserveTableListeners.onDenyClicked(reserveTable);
                    });
                    break;
                case "1"://Đã duyệt
                    binding.buttonAgree.setVisibility(View.GONE);
                    binding.buttonDeny.setVisibility(View.GONE);
                    binding.buttonConfirm.setVisibility(View.VISIBLE);
                    binding.buttonConfirmed.setVisibility(View.GONE);
                    binding.buttonConfirm.setOnClickListener(v -> {
                        reserveTableListeners.onConfirmClicked(reserveTable);
                    });
                    break;
                case "3"://Muộn
                    binding.buttonAgree.setVisibility(View.VISIBLE);
                    binding.buttonDeny.setVisibility(View.VISIBLE);
                    binding.buttonDeny.setText("Huỷ");
                    binding.buttonConfirm.setVisibility(View.GONE);
                    binding.buttonConfirmed.setVisibility(View.GONE);
                    binding.buttonAgree.setOnClickListener(v -> {
                        reserveTableListeners.onAgreeClicked(reserveTable);
                    });
                    binding.buttonDeny.setOnClickListener(v -> {
                        reserveTableListeners.onDenyClicked(reserveTable);
                    });
                    break;
                case "4"://Hoàn thành
                    binding.buttonAgree.setVisibility(View.GONE);
                    binding.buttonDeny.setVisibility(View.GONE);
                    binding.buttonConfirm.setVisibility(View.GONE);
                    binding.buttonConfirmed.setVisibility(View.VISIBLE);
                    binding.buttonConfirmed.setOnClickListener(v -> {
                        reserveTableListeners.onConfirmedClicked(reserveTable);
                    });
                    break;
                default:
                    binding.buttonAgree.setVisibility(View.GONE);
                    binding.buttonDeny.setVisibility(View.GONE);
                    binding.buttonConfirm.setVisibility(View.GONE);
                    binding.buttonConfirmed.setVisibility(View.VISIBLE);
                    binding.buttonConfirmed.setText("Đã huỷ");
                    binding.buttonConfirmed.setOnClickListener(v -> {
                        reserveTableListeners.onConfirmedClicked(reserveTable);
                    });
                    break;

            }

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable);
            });
        }
    }

    public interface SearchReserveTableListeners {
        void onConfirmClicked(getReserveTable reserveTable);

        void onConfirmedClicked(getReserveTable reserveTable);

        void onAgreeClicked(getReserveTable reserveTable);

        void onDenyClicked(getReserveTable reserveTable);

        void onClicked(getReserveTable reserveTable);
    }
}
