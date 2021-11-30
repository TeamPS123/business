package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.PromotionItemContainerBinding;
import com.psteam.foodlocationbusiness.models.PromotionModel;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private final List<PromotionModel> promotionModels;
    private final PromotionListeners promotionListeners;

    public PromotionAdapter(List<PromotionModel> promotionModels, PromotionListeners promotionListeners) {
        this.promotionModels = promotionModels;
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
        holder.setData(promotionModels.get(position));
    }

    @Override
    public int getItemCount() {
        return promotionModels != null ? promotionModels.size() : 0;
    }

    class PromotionViewHolder extends RecyclerView.ViewHolder {

        final PromotionItemContainerBinding binding;

        public PromotionViewHolder(@NonNull PromotionItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(PromotionModel promotionModel) {
            binding.textViewPromotion.setText(promotionModel.getName());
            binding.buttonEdit.setOnClickListener(v -> {
                promotionListeners.onEditClick(promotionModel,1);
            });

            binding.buttonDelete.setOnClickListener(v -> {
                promotionListeners.onDeleteClick(promotionModel, getAdapterPosition());
            });

            binding.buttonStatus.setOnClickListener(v -> {
                promotionListeners.onChangeStatus(promotionModel);
            });

            binding.getRoot().setOnClickListener(v -> {
                promotionListeners.onEditClick(promotionModel,0);
            });
        }
    }

    public interface PromotionListeners {
        // 1 edit 0 details
        void onEditClick(PromotionModel promotionModel,int mode);

        void onDeleteClick(PromotionModel promotionModel, int position);

        void onChangeStatus(PromotionModel promotionModel);
    }
}
