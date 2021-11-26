package com.psteam.foodlocationbusiness.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.databinding.ContainerItemImageRestaurantBinding;

import java.util.ArrayList;

public class ImageRestaurantAdapter extends RecyclerView.Adapter<ImageRestaurantAdapter.ImageRestaurantViewHolder>{
    private final ArrayList<Uri> bitmaps;

    public ImageRestaurantAdapter(ArrayList<Uri> bitmaps ) {
        this.bitmaps = bitmaps;

    }

    @NonNull
    @Override
    public ImageRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageRestaurantViewHolder(ContainerItemImageRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRestaurantViewHolder holder, int position) {
        holder.setData(bitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }


    class ImageRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private final ContainerItemImageRestaurantBinding binding;

        public ImageRestaurantViewHolder(@NonNull ContainerItemImageRestaurantBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }

        public void setData(Uri bitmap){
            binding.imageViewRestaurant.setImageURI(bitmap);
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    bitmaps.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return false;
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 1, 0, "Xoá");
        }
    }

    public void removeImage(int position){
        bitmaps.remove(position);
        notifyItemRemoved(position);
    }
}
