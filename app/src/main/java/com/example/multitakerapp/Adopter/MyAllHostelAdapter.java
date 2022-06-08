package com.example.multitakerapp.Adopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multitakerapp.Model.UserHostelDetail;
import com.example.multitakerapp.databinding.ItemAllHostelsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAllHostelAdapter extends RecyclerView.Adapter<MyAllHostelAdapter.ViewHolder> {

    Context c;
    ArrayList<UserHostelDetail> models;


    public MyAllHostelAdapter(Context c, ArrayList<UserHostelDetail> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllHostelsBinding binding = ItemAllHostelsBinding.inflate(LayoutInflater.from(c), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserHostelDetail userHostelDetail = models.get(position);

        holder.binding.hostelNameTV.setText(userHostelDetail.getHostelName());
        holder.binding.hostelContactTV.setText(userHostelDetail.getHostelnumber());
        holder.binding.areaTV.setText(userHostelDetail.getHostelArea());
        holder.binding.rentTV.setText(userHostelDetail.getHostelrent());
        holder.binding.roomsTV.setText(userHostelDetail.getHostelrooms());


    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAllHostelsBinding binding;

        public ViewHolder(@NonNull ItemAllHostelsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
