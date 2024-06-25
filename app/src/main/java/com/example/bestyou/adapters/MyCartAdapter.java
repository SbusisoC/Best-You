package com.example.bestyou.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bestyou.models.MyCartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MyCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MyCartModel> list;
    FirebaseFirestore fireStore;
    FirebaseAuth auth;
    private boolean isDoneClicked = false;
    private OnItemCheckedChangeListener listener;

    private static final int TYPE_DATE_HEADER = 0;
    private static final int TYPE_CART_ITEM = 1;

    public MyCartAdapter(Context context, List<MyCartModel> list, OnItemCheckedChangeListener listener) {
        this.context = context;
        this.list = list;
        fireStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.listener = listener;
        setList(list);
    }

    @Override
    public int getItemViewType(int position) {
        MyCartModel item = list.get(position);
        return item.isDateHeader() ? TYPE_DATE_HEADER : TYPE_CART_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_header, parent, false);
            return new DateHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false);
            return new CartItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_DATE_HEADER) {
            DateHeaderViewHolder dateHolder = (DateHeaderViewHolder) holder;
            MyCartModel dateHeaderItem = list.get(position);
            dateHolder.bind(dateHeaderItem.getCurrentDate());
        } else {
            CartItemViewHolder cartHolder = (CartItemViewHolder) holder;
            MyCartModel cartItem = list.get(position);

            Glide.with(context).load(cartItem.getImg_url()).into(cartHolder.mItemImage);
            cartHolder.nPart.setText(cartItem.getBodyPart());
            cartHolder.mName.setText(cartItem.getWorkoutName());
            cartHolder.Sets.setText(cartItem.getNumberOfSets());
            cartHolder.Reps.setText(cartItem.getNumberOfReps());

            if (isDoneClicked && cartItem.isChecked()) {
                cartHolder.itemView.setAlpha(0.5f);
            } else {
                cartHolder.itemView.setAlpha(1.0f);
            }

            cartHolder.checkBox.setOnCheckedChangeListener(null);

            cartHolder.checkBox.setChecked(cartItem.isChecked());
            cartHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cartItem.setChecked(isChecked);
                    if (listener != null) {
                        listener.onCheckedChanged(position, isChecked);
                    }
                    if (!isChecked) {
                        ((CartActivity) context).removeFromWorkoutComplete(cartItem); // Call the remove method
                        cartHolder.itemView.setAlpha(1.0f);
                    }
                }
            });
        }
    }
            /*cartHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cartItem.setChecked(isChecked);
                    listener.onCheckedChanged(position, isChecked);
                }
            });

            cartHolder.checkBox.setChecked(cartItem.isChecked());
        }
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemCheckedChangeListener {
        void onCheckedChanged(int position, boolean isChecked);
    }

    public void removeCheckedItems(final FirebaseFirestore fireStore, final FirebaseAuth auth) {
        List<MyCartModel> itemsToRemove = new ArrayList<>();
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
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        });
                itemsToRemove.add(item);

            }
        }
        list.removeAll(itemsToRemove);
        notifyDataSetChanged();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImage;
        private TextView nPart;
        private TextView mName, Sets, Reps;
        private CheckBox checkBox;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage = itemView.findViewById(R.id.item_image);
            nPart = itemView.findViewById(R.id.body_part);
            mName = itemView.findViewById(R.id.dateText);
            Sets = itemView.findViewById(R.id.sets);
            Reps = itemView.findViewById(R.id.reps);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public class DateHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView nDate;

        public DateHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            nDate = itemView.findViewById(R.id.dateText);
        }

        public void bind(String date) {
            nDate.setText(date);
        }
    }

    public void setDoneClicked(boolean doneClicked) {
        isDoneClicked = doneClicked;
        notifyDataSetChanged();
    }

    public boolean isDoneClicked() {
        return isDoneClicked;
    }

    public void setList(List<MyCartModel> newList) {
        list.clear();
        HashMap<String, List<MyCartModel>> groupedItems = new HashMap<>();

        for (MyCartModel item : newList) {
            String currentDate = item.getCurrentDate();
            if (currentDate != null) {
                if (!groupedItems.containsKey(currentDate)) {
                    groupedItems.put(currentDate, new ArrayList<MyCartModel>());
                }
                groupedItems.get(currentDate).add(item);
            }
        }

        // Sort the keys (dates) in descending order
        List<String> sortedDates = new ArrayList<>(groupedItems.keySet());
        Collections.sort(sortedDates, new Comparator<String>() {
            @Override
            public int compare(String date1, String date2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    return dateFormat.parse(date2).compareTo(dateFormat.parse(date1));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        // Add headers and items to the list
        for (String date : sortedDates) {
            MyCartModel dateHeader = new MyCartModel();
            dateHeader.setDateHeader(true);
            dateHeader.setCurrentDate(date);
            list.add(dateHeader);
            list.addAll(groupedItems.get(date));
        }

        notifyDataSetChanged();
    }
}
