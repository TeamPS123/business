package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.PromotionItemContainerBinding;
import com.psteam.foodlocationbusiness.models.PromotionModel;
import com.psteam.lib.Models.Get.getPromotion;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private final List<getPromotion> promotions;
    private final PromotionListeners promotionListeners;

    public PromotionAdapter(List<getPromotion> promotions, PromotionListeners promotionListeners) {
        this.promotions = promotions;
        this.promotionListeners = promotionListeners;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PromotionViewHolder(PromotionItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        holder.setData(promotions.get(position));
    }

    @Override
    public int getItemCount() {
        return promotions != null ? promotions.size() : 0;
    }

    class PromotionViewHolder extends RecyclerView.ViewHolder {

        final PromotionItemContainerBinding binding;

        public PromotionViewHolder(@NonNull PromotionItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(getPromotion promotions) {
            binding.textViewPromotion.setText(promotions.getName());
            binding.buttonEdit.setOnClickListener(v -> {
                promotionListeners.onEditClick(promotions,1);
            });

            binding.buttonDelete.setOnClickListener(v -> {
                promotionListeners.onDeleteClick(promotions, getAdapterPosition());
            });

            binding.buttonStatus.setOnClickListener(v -> {
                promotionListeners.onChangeStatus(promotions);
            });

            binding.getRoot().setOnClickListener(v -> {
                promotionListeners.onEditClick(promotions,0);
            });
        }
    }

    public interface PromotionListeners {
        // 1 edit 0 details
        void onEditClick(getPromotion promotions,int mode);

        void onDeleteClick(getPromotion promotions, int position);

        void onChangeStatus(getPromotion promotions);
    }
}
