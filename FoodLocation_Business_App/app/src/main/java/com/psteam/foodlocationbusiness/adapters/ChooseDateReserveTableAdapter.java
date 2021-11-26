package com.psteam.foodlocationbusiness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.databinding.ChooseNumberPeopleItemContainerBinding;

import java.util.List;

public class ChooseDateReserveTableAdapter extends RecyclerView.Adapter<ChooseDateReserveTableAdapter.ChooseDateReserveTableViewHolder>{

    private final List<DateReserveTable> dateReserveTables;
    private final ChooseDateReserveTableListener chooseDateReserveTableListener;
    private static int selectedPos;

    public ChooseDateReserveTableAdapter(List<DateReserveTable> dateReserveTables, ChooseDateReserveTableListener chooseDateReserveTableListener) {
        this.dateReserveTables = dateReserveTables;
        this.chooseDateReserveTableListener = chooseDateReserveTableListener;
    }

    @NonNull
    @Override
    public ChooseDateReserveTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChooseDateReserveTableViewHolder(ChooseNumberPeopleItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseDateReserveTableViewHolder holder, int position) {
        holder.setData(dateReserveTables.get(position));
        if(selectedPos==position){
            holder.setSelected(true);
        }else {
            holder.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return dateReserveTables!=null?dateReserveTables.size():0;
    }

    class ChooseDateReserveTableViewHolder extends RecyclerView.ViewHolder {

        private final ChooseNumberPeopleItemContainerBinding binding;

        public ChooseDateReserveTableViewHolder(@NonNull ChooseNumberPeopleItemContainerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(DateReserveTable dateReserveTable){
            binding.textViewNumberPeople.setText(dateReserveTable.getDay());
            binding.getRoot().setOnClickListener(v->{
                chooseDateReserveTableListener.onChooseDateReserveTableClicked(dateReserveTable);
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            });
        }
        public void setSelected(Boolean selected){
            if(selected){
                binding.imageSelected.setVisibility(View.VISIBLE);
            }else {
                binding.imageSelected.setVisibility(View.GONE);
            }
        }
    }

    public interface ChooseDateReserveTableListener{
        void onChooseDateReserveTableClicked(DateReserveTable dateReserveTable);
    }

    public static class DateReserveTable{
        private String day;

        public DateReserveTable(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }
}
