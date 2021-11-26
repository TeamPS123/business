package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.FoodReserveItemContainerBinding;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class FoodReserveAdapter extends RecyclerView.Adapter<FoodReserveAdapter.FoodReserveViewHolder> {

    private final List<FoodReserve> foods;
    private final FoodReserveListeners foodReserveListeners;

    public FoodReserveAdapter(List<FoodReserve> foods, FoodReserveListeners foodReserveListeners) {
        this.foods = foods;
        this.foodReserveListeners = foodReserveListeners;
    }

    @NonNull
    @Override
    public FoodReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodReserveViewHolder(FoodReserveItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodReserveViewHolder holder, int position) {
        holder.setData(foods.get(position));

    }

    @Override
    public int getItemCount() {
        return foods != null ? foods.size() : 0;
    }

    class FoodReserveViewHolder extends RecyclerView.ViewHolder {

        public final FoodReserveItemContainerBinding binding;

        public FoodReserveViewHolder(@NonNull FoodReserveItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(FoodReserve food) {
            binding.imageViewFood.setImageResource(food.getImage());
            binding.textViewFoodName.setText(food.getName());
            binding.textViewPrice.setText(DecimalFormat.getCurrencyInstance(new Locale("vi", "VN")).format(food.getPrice()));
            binding.textViewFoodInfo.setText(food.getInfo());
            binding.textViewCount.setText(String.valueOf(food.getCount()));
            binding.textViewAdd.setOnClickListener(v -> {
                food.setCount(food.getCount() + 1);
                binding.textViewCount.setText(String.valueOf(food.getCount()));
                foodReserveListeners.onAddFoodReserveClick(food);
            });

            binding.textViewMinus.setOnClickListener(v -> {
                int count = food.getCount() - 1;
                if (count <= 0) {
                    foods.remove(food);
                    notifyItemRemoved(getLayoutPosition());
                    if (foods.size() <= 0) {
                        double price =food.getPrice();
                        foodReserveListeners.onRemoveFoodReserveClick(food,count,price);
                    }
                } else {
                    food.setCount(count);
                    binding.textViewCount.setText(String.valueOf(count));
                }
                foodReserveListeners.onMinusFoodReserveClick(food);
            });

            binding.imageViewRemove.setOnClickListener(v -> {
                int count = food.getCount();
                double price =food.getPrice();
                foods.remove(food);
                notifyItemRemoved(getLayoutPosition());
                foodReserveListeners.onRemoveFoodReserveClick(food,count,price);
            });
        }
    }

    public interface FoodReserveListeners {
        void onAddFoodReserveClick(FoodReserve food);

        void onMinusFoodReserveClick(FoodReserve food);

        void onRemoveFoodReserveClick(FoodReserve food,int count,double price);

        void onFoodClick(FoodReserve food);

    }

    public static class FoodReserve {

        private int image;
        private String name;
        private double price;
        private String info;
        private int count;


        public FoodReserve(int image, String name, double price, String info, int count) {
            this.image = image;
            this.name = name;
            this.price = price;
            this.info = info;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getImage() {
            return image;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
