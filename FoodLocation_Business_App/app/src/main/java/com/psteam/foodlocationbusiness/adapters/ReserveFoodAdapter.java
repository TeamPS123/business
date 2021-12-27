package com.psteam.foodlocationbusiness.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.LayoutFoodReserveItemContainerBinding;
import com.psteam.foodlocationbusiness.databinding.ManagerFoodItemContainerBinding;
import com.psteam.lib.Models.Get.getFood;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class ReserveFoodAdapter extends RecyclerView.Adapter<ReserveFoodAdapter.ReserveFoodViewHolder>{
    private final List<getFood> foodList;
    private final ReserveFoodViewListeners foodReserveListeners;
    private final Context context;

    public ReserveFoodAdapter(List<getFood> foodList, Context context, ReserveFoodViewListeners foodReserveListeners) {
        this.foodList = foodList;
        this.context = context;
        this.foodReserveListeners = foodReserveListeners;
    }

    @NonNull
    @Override
    public ReserveFoodAdapter.ReserveFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReserveFoodViewHolder(LayoutFoodReserveItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveFoodViewHolder holder, int position) {
        holder.setData(foodList.get(position));
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }

    class ReserveFoodViewHolder extends RecyclerView.ViewHolder {

        private final LayoutFoodReserveItemContainerBinding binding;

        public ReserveFoodViewHolder(@NonNull LayoutFoodReserveItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(getFood food) {
            if (food.getPic().size() > 0) {
                Glide.with(context).load(food.getPic().get(0)).thumbnail(0.3f).into(binding.imageViewFood);
            }
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(food.getPrice()));
            binding.textViewUnit.setText(food.getUnit());
            binding.textViewCategory.setText(food.getCategoryName());
            binding.textViewCount.setText(food.getQuantity()+"");
            binding.textViewAdd.setOnClickListener(v -> {
                int current = Integer.parseInt(binding.textViewCount.getText()+"");

                binding.textViewCount.setText((current+1)+"");

                foodReserveListeners.onAddFoodReserveClick(food, getAdapterPosition(), current+1);
            });

            binding.textViewMinus.setOnClickListener(v -> {
                int current = Integer.parseInt(binding.textViewCount.getText()+"");

                if(current-1 > 0) {
                    binding.textViewCount.setText((current-1)+"");

                    foodReserveListeners.onMinusFoodReserveClick(food, getAdapterPosition(), current-1);
                }else{
                    foodReserveListeners.onRemoveFoodReserveClick(food, getAdapterPosition());
                }
            });

            binding.imageViewRemove.setOnClickListener(v -> {
                foodReserveListeners.onRemoveFoodReserveClick(food, getAdapterPosition());
            });
        }
    }

    public interface ReserveFoodViewListeners {
        void onAddFoodReserveClick(getFood food, int position, int quantity);

        void onMinusFoodReserveClick(getFood food, int position, int quantity);

        void onRemoveFoodReserveClick(getFood food, int position);
    }
}
