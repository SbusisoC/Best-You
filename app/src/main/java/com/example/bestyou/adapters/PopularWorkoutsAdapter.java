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
import com.example.bestyou.models.PopularWorkoutsModel;

import java.util.List;

public class PopularWorkoutsAdapter extends RecyclerView.Adapter<PopularWorkoutsAdapter.ViewHolder>{

    private Context context;
    private List<PopularWorkoutsModel> popularWorkoutsModelList;

    public PopularWorkoutsAdapter(Context context, List<PopularWorkoutsModel> popularWorkoutsModelList) {
        this.context = context;
        this.popularWorkoutsModelList = popularWorkoutsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_workouts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(popularWorkoutsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(popularWorkoutsModelList.get(position).getName());
        holder.part.setText(String.valueOf(popularWorkoutsModelList.get(position).getBodyPart()));

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",popularWorkoutsModelList.get(position));
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return popularWorkoutsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, part;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            part = itemView.findViewById(R.id.all_price);
        }
    }
}
