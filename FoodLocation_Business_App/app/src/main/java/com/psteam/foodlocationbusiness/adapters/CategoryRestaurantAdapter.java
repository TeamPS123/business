package com.psteam.foodlocationbusiness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.databinding.LayoutCategoryRestaurantItemContainerBinding;

import java.util.List;

public class CategoryRestaurantAdapter extends RecyclerView.Adapter<CategoryRestaurantAdapter.CategoryRestaurantViewHolder> {
    private final List<CategoryRestaurant> categoryRestaurants;
    private final Context context;
    private final CategoryRestaurantListeners categoryRestaurantListeners;

    public CategoryRestaurantAdapter(List<CategoryRestaurant> categoryRestaurants, Context context, CategoryRestaurantListeners categoryRestaurantListeners) {
        this.categoryRestaurants = categoryRestaurants;
        this.context = context;
        this.categoryRestaurantListeners = categoryRestaurantListeners;
    }

    @NonNull
    @Override
    public CategoryRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryRestaurantViewHolder(LayoutCategoryRestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRestaurantViewHolder holder, int position) {
        holder.setData(categoryRestaurants.get(position));

    }

    @Override
    public int getItemCount() {
        return categoryRestaurants != null ? categoryRestaurants.size() : 0;
    }

    class CategoryRestaurantViewHolder extends RecyclerView.ViewHolder {

        private LayoutCategoryRestaurantItemContainerBinding binding;

        public CategoryRestaurantViewHolder(@NonNull LayoutCategoryRestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(CategoryRestaurant categoryRestaurant) {
            binding.textViewName.setText(categoryRestaurant.getName());
            binding.imageViewIconCategoryRestaurant.setImageResource(categoryRestaurant.getImage());

            binding.getRoot().setOnClickListener(v -> {
                isSelectedItem(categoryRestaurant);
            });

            if(categoryRestaurant.isSelected){
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant_selected));
                binding.imageViewIconCategoryRestaurant.setTag("Selected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),true,binding.imageViewIconCategoryRestaurant);
            }else {
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant));
                binding.imageViewIconCategoryRestaurant.setTag("unSelected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),false,binding.imageViewIconCategoryRestaurant);
            }
        }

        public void isSelectedItem(CategoryRestaurant categoryRestaurant){
            if(binding.imageViewIconCategoryRestaurant.getTag().equals("unSelected")){
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant_selected));
                binding.imageViewIconCategoryRestaurant.setTag("Selected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),true,binding.imageViewIconCategoryRestaurant);
            }else {
                binding.imageViewIconCategoryRestaurant.setBackground(context.getDrawable(R.drawable.layout_category_restaurant));
                binding.imageViewIconCategoryRestaurant.setTag("unSelected");
                categoryRestaurantListeners.onCategoryRestaurantClicked(categoryRestaurant,getAdapterPosition(),false,binding.imageViewIconCategoryRestaurant);
            }
        }
    }

    public interface CategoryRestaurantListeners{
        void onCategoryRestaurantClicked(CategoryRestaurant categoryRestaurant, int position, boolean isSelected, ImageView imageView);
    }

    public static class CategoryRestaurant {
        private String name;
        private int image;
        private boolean isSelected;

        public CategoryRestaurant(String name, int image, boolean isSelected) {
            this.name = name;
            this.image = image;
            this.isSelected = isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    }
}
