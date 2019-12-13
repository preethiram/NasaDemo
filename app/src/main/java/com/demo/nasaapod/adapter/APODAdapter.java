package com.demo.nasaapod.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.nasaapod.R;
import com.demo.nasaapod.model.APODModel;

import java.util.ArrayList;
import java.util.List;

public class APODAdapter extends RecyclerView.Adapter<APODAdapter.APODViewHolder> {

    private static List<APODModel> sApodModels = new ArrayList<>();

    @NonNull
    @Override
    public APODViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent, false);
        return new APODViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull APODViewHolder holder, int position) {
        APODModel thisApodModel = sApodModels.get(position);
        holder.apodAuthor.setText(thisApodModel.getCopyright());
        holder.apodDate.setText(thisApodModel.getDate());
        holder.apodTitle.setText(thisApodModel.getTitle());


        Glide.with(holder.apodIv)
                .load(thisApodModel.getUrl())
                .placeholder(R.drawable.place_holder)
                .into(holder.apodIv);

    }

    @Override
    public int getItemCount() {
        return sApodModels.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        sApodModels.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<APODModel> newApodModels) {
        sApodModels.addAll(newApodModels);
        notifyDataSetChanged();
    }


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
     static class APODViewHolder extends RecyclerView.ViewHolder {
        private final TextView apodAuthor;
        private final TextView apodDate;
        private final TextView apodTitle;
        private final ImageView apodIv;

        APODViewHolder(View v) {
            super(v);
            apodAuthor = v.findViewById(R.id.apod_copyright_tv);
            apodDate = v.findViewById(R.id.apod_date_tv);
            apodTitle = v.findViewById(R.id.apod_title_tv);
            apodIv = v.findViewById(R.id.apod_iv);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TEST", "Element " + getAdapterPosition() + " clicked.");
                   new AlertDialog.Builder(v.getContext())
                            .setTitle( sApodModels.get(getAdapterPosition()).getTitle())
                            .setMessage(sApodModels.get(getAdapterPosition()).getExplanation())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            });
        }

    }

}
