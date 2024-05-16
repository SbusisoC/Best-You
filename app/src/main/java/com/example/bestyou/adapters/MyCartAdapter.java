package com.example.bestyou.adapters;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bestyou.R;
import com.example.bestyou.activities.CartActivity;
import com.example.bestyou.activities.DetailedActivity;
import com.example.bestyou.activities.ShowAllWorkoutsActivity;
import com.example.bestyou.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;
    FirebaseFirestore fireStore;
    FirebaseAuth auth;
    private boolean isDoneClicked = false;
    private OnItemCheckedChangeListener listener;

    public MyCartAdapter(Context context, List<MyCartModel> list, OnItemCheckedChangeListener listener){
        this.context = context;
        this.list = list;
        fireStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.listener = listener;
    }
     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyCartModel cartItem = list.get(position);

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.mItemImage);
        holder.nPart.setText(list.get(position).getBodyPart());
        holder.mName.setText(list.get(position).getWorkoutName());
        holder.Sets.setText(list.get(position).getNumberOfSets());
        holder.Reps.setText(list.get(position).getNumberOfReps());

        if(isDoneClicked && list.get(position).isChecked()) {
            holder.itemView.setAlpha(0.5f);

        }else{
            holder.itemView.setAlpha(1.0f);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the checked state of the item in the model
                list.get(position).setChecked(isChecked);

                listener.onCheckedChanged(position, isChecked);
            }
        });

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

    public interface OnItemCheckedChangeListener {
        void onCheckedChanged(int position, boolean isChecked);
    }


    public void removeCheckedItems(final FirebaseFirestore fireStore, final FirebaseAuth auth) {
        /*Iterator<MyCartModel> iterator = list.iterator();*/
        List<MyCartModel> itemsToRemove = new ArrayList<>(); // List to hold items to be removed
        Iterator<MyCartModel> iterator = list.iterator();
        while (iterator.hasNext()) {
            final MyCartModel item = iterator.next();
            if (item.isChecked()) {
                final String documentId = item.getDocumentId();
                fireStore.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("User")
                        .document(documentId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Remove the item from the list
                               /* iterator.remove();
                                notifyDataSetChanged();*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        });
                itemsToRemove.add(item);
            }
        }
        // Remove the items from the main list after the loop
        list.removeAll(itemsToRemove);
        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mItemImage;
        private TextView nPart;
        private TextView mName, Sets, Reps;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.item_image);
            nPart = itemView.findViewById(R.id.body_part);
            mName = itemView.findViewById(R.id.workout_name);
            Sets = itemView.findViewById(R.id.sets);
            Reps = itemView.findViewById(R.id.reps);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void setDoneClicked(boolean doneClicked) {
        isDoneClicked = doneClicked;
        notifyDataSetChanged();
    }
}
