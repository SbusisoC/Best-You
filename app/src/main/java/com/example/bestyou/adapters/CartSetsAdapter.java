package com.example.bestyou.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestyou.R;
import com.example.bestyou.models.SetModel;

import java.util.List;

public class CartSetsAdapter extends RecyclerView.Adapter<CartSetsAdapter.ViewHolder> {
    private List<SetModel> setList;

    public CartSetsAdapter(List<SetModel> setList) {
        this.setList = setList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartsets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SetModel setModel = setList.get(position);

        holder.setNumber.setText(String.valueOf(setModel.getSetNumber()));
        holder.reps.setText(String.valueOf(setModel.getReps()));
        holder.weight.setText(String.valueOf(setModel.getWeight()));
    }

    @Override
    public int getItemCount() {
        return setList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView setNumber, reps, weight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.cartSetNumber1);
            reps = itemView.findViewById(R.id.cartEditTextReps);
            weight = itemView.findViewById(R.id.cartWeightEntry);
        }
    }
}