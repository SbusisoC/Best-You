package com.example.bestyou.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.bestyou.models.WorkoutPlansModel;

import java.util.List;

public class WorkoutPlansAdapter extends RecyclerView.Adapter<WorkoutPlansAdapter.ViewHolder>{
    private Context context;
    private List<WorkoutPlansModel> list;

    public WorkoutPlansAdapter(Context context, List<WorkoutPlansModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list,parent,false));
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*Glide.with(context).load(list.get(position).getImg_url()).into(holder.catImg2);*/
        holder.catName2.setText(list.get(position).getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CartActivity.class);
                intent.putExtra("dayPlanned",list.get(position).getDayPlanned());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView catImg2;
        TextView catName2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg2 = itemView.findViewById(R.id.cat_img2);
            catName2 = itemView.findViewById(R.id.cat_name2);

        }
    }

}
