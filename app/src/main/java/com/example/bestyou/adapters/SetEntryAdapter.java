package com.example.bestyou.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestyou.R;

public class SetEntryAdapter extends RecyclerView.Adapter<SetEntryAdapter.SetEntryViewHolder> {
    private int numberOfSets;

    public SetEntryAdapter(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    @NonNull
    @Override
    public SetEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setsentry, parent, false);
        return new SetEntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetEntryViewHolder holder, int position) {
        holder.setNumber.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return numberOfSets;
    }

    public static class SetEntryViewHolder extends RecyclerView.ViewHolder {
        TextView setNumber;

        public SetEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setNumber);
        }
    }

    public void updateData(int numberOfSets) {
        this.numberOfSets = numberOfSets;
        notifyDataSetChanged();
    }
}
