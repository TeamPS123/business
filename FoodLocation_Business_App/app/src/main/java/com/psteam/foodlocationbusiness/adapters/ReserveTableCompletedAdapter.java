package com.psteam.foodlocationbusiness.adapters;

import static com.psteam.foodlocationbusiness.ultilities.Constants.coverStringToDate;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.ReservedTableItemCompletedBinding;
import com.psteam.foodlocationbusiness.socket.models.BodySenderFromUser;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReserveTableCompletedAdapter extends RecyclerView.Adapter<ReserveTableCompletedAdapter.ReserveTableViewHolder>{
    private final List<BodySenderFromUser> reserveTableList;
    private final ReserveTableListeners reserveTableListeners;
    private final Context context;

    public ReserveTableCompletedAdapter(List<BodySenderFromUser> reserveTableList, ReserveTableListeners reserveTableListeners, Context context) {
        this.reserveTableList = reserveTableList;
        this.reserveTableListeners = reserveTableListeners;
        this.context = context;
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
            binding.textViewPhoneNumber.setText(reserveTable.getPhone());
            binding.textViewNumberPeople.setText(String.valueOf(reserveTable.getQuantity()));

            binding.textViewTime.setText(new SimpleDateFormat("hh:mm a").format(coverStringToDate(reserveTable.getTime())));
            binding.textViewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(coverStringToDate(reserveTable.getTime())));

            binding.buttonConfirmed.setOnClickListener(v -> {
                reserveTableListeners.onConfirmClicked(reserveTable, getAdapterPosition());
            });

            binding.getRoot().setOnClickListener(v -> {
                reserveTableListeners.onClicked(reserveTable, getAdapterPosition());
            });
            String text="Đã\n hoàn thành\n"+"(Chi tiết)";
            SpannableString spannableString=new SpannableString(text);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,2,0);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),3,14,0);
            spannableString.setSpan(new ForegroundColorSpan(context.getColor(R.color.colorSubtext)),15,25,0);
            spannableString.setSpan(new RelativeSizeSpan(0.7f), 15,25, 0);
            binding.buttonConfirmed.setText(spannableString);

        }
    }

    public interface ReserveTableListeners {
        void onConfirmClicked(BodySenderFromUser reserveTable, int position);

        void onClicked(BodySenderFromUser reserveTable, int position);
    }
}
