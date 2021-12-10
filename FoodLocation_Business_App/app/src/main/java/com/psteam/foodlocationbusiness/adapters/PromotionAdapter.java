package com.psteam.foodlocationbusiness.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.PromotionItemContainerBinding;
import com.psteam.foodlocationbusiness.models.PromotionModel;
import com.psteam.lib.Models.Get.getPromotion;

import java.util.ArrayList;
import java.util.List;

import io.grpc.Context;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private final List<getPromotion> promotions;
    private final PromotionListeners promotionListeners;
    private final android.content.Context context;

    public PromotionAdapter(android.content.Context context, List<getPromotion> promotions, PromotionListeners promotionListeners) {
        this.context = context;
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

        public void setData(getPromotion promotion) {
            binding.textViewPromotion.setText(promotion.getName());
            binding.buttonStatus.setCheckable(promotion.isStatus());
            if(promotion.isStatus()){
                binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_toggle_on_24), null, null, null);
                binding.buttonStatus.setBackgroundColor(Color.parseColor("#2196F3"));
            }else{
                binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_toggle_off_24), null, null, null);
                binding.buttonStatus.setBackgroundColor(Color.parseColor("#454545"));
            }
            binding.buttonEdit.setOnClickListener(v -> {
                promotionListeners.onEditClick(promotion,1, getAdapterPosition());
            });

            binding.buttonDelete.setOnClickListener(v -> {
                promotionListeners.onDeleteClick(promotion, getAdapterPosition());
            });

            binding.buttonStatus.setOnClickListener(v -> {
                binding.buttonStatus.setCheckable(!binding.buttonStatus.isCheckable());
                if(binding.buttonStatus.isCheckable()){
                    binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_toggle_on_24), null, null, null);
                    binding.buttonStatus.setBackgroundColor(Color.parseColor("#2196F3"));
                }else{
                    binding.buttonStatus.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_baseline_toggle_off_24), null, null, null);
                    binding.buttonStatus.setBackgroundColor(Color.parseColor("#454545"));
                }
                promotionListeners.onChangeStatus(promotion, getAdapterPosition());
            });

            binding.getRoot().setOnClickListener(v -> {
                promotionListeners.onEditClick(promotion,0, getAdapterPosition());
            });
        }
    }

    public interface PromotionListeners {
        // 1 edit 0 details
        void onEditClick(getPromotion promotions,int mode, int position);

        void onDeleteClick(getPromotion promotions, int position);

        void onChangeStatus(getPromotion promotions, int position);
    }
}
