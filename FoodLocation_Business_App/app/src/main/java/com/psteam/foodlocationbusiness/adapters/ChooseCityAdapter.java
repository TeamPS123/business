package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.psteam.foodlocationbusiness.databinding.ChooseNumberPeopleItemContainerBinding;

import java.util.List;

public class ChooseCityAdapter extends RecyclerView.Adapter<ChooseCityAdapter.ChooseCityViewHolder> {

    private final List<City> cities;
    private final ChooseCityListener chooseCityListener;
    private static int selectedPos;

    public ChooseCityAdapter(List<City> cities, ChooseCityListener chooseCityListener) {
        this.cities = cities;
        this.chooseCityListener = chooseCityListener;
    }

    @NonNull
    @Override
    public ChooseCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChooseCityViewHolder(ChooseNumberPeopleItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseCityViewHolder holder, int position) {
        holder.setData(cities.get(position));
        if (selectedPos == position) {
            holder.setSelected(true);
        } else {
            holder.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return cities != null ? cities.size() : 0;
    }

    class ChooseCityViewHolder extends RecyclerView.ViewHolder {

        private final ChooseNumberPeopleItemContainerBinding binding;

        public ChooseCityViewHolder(@NonNull ChooseNumberPeopleItemContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(City city) {
            binding.textViewNumberPeople.setText(city.getName());
            binding.getRoot().setOnClickListener(v -> {
                chooseCityListener.onChooseCityClicked(city);
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            });
        }

        public void setSelected(Boolean selected) {
            if (selected) {
                binding.imageSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imageSelected.setVisibility(View.GONE);
            }
        }
    }

    public interface ChooseCityListener {
        void onChooseCityClicked(City city);
    }

    public static class City {
        private String name;
        private String code;

        public City(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
