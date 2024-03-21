package com.example.bestyou.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestyou.R;
import com.example.bestyou.activities.CartActivity;
import com.example.bestyou.activities.ShowAllWorkoutsActivity;
import com.example.bestyou.models.MyCartModel;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;

    public MyCartAdapter(Context context, List<MyCartModel> list){
        this.context = context;
        this.list = list;
    }
     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImage);
        holder.nPart.setText(list.get(position).getBodyPart());
        holder.mName.setText(list.get(position).getWorkoutName());
        holder.Sets.setText(list.get(position).getNumberOfSets());
        holder.Reps.setText(list.get(position).getNumberOfReps());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CartActivity.class);
                /*intent.putExtra("dayPlanned",list.get(position).getDayPlanned());*/
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImage;
        private TextView nPart;
        private TextView mName, Sets, Reps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.item_image);
            nPart = itemView.findViewById(R.id.body_part);
            mName = itemView.findViewById(R.id.workout_name);
            Sets = itemView.findViewById(R.id.sets);
            Reps = itemView.findViewById(R.id.reps);
        }
    }
}
