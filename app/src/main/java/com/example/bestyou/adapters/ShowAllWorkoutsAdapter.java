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
import com.example.bestyou.activities.DetailedActivity;
import com.example.bestyou.models.ShowAllWorkoutsModel;

import java.util.List;
public class ShowAllWorkoutsAdapter extends RecyclerView.Adapter<ShowAllWorkoutsAdapter.ViewHolder> {

    private Context context;
    private List<ShowAllWorkoutsModel> list;

    public ShowAllWorkoutsAdapter(Context context, List<ShowAllWorkoutsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_workouts,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImage);
        holder.mBodyPart.setText(list.get(position).getBodyPart());
        holder.mName.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",list.get(position));
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
        private TextView mBodyPart;
        private TextView mName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.workout_image);
            mBodyPart = itemView.findViewById(R.id.body_part);
            mName = itemView.findViewById(R.id.workout_name);
        }
    }
}