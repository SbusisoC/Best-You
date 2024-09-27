package com.example.bestyou.adapters;

import static android.graphics.Color.BLACK;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestyou.activities.ChatActivity;
import com.example.bestyou.R;
import com.example.bestyou.models.ChatMessageModel;
import com.example.bestyou.utils.AndroidUtil;
import com.example.bestyou.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, RecyclerView.ViewHolder> {

    private static final int TYPE_MESSAGE = 1;
    private static final int TYPE_DATE_HEADER = 2;
    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || !getItem(position).isSameDay(getItem(position - 1))) {
            return TYPE_DATE_HEADER;
        } else {
            return TYPE_MESSAGE;
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (getItemViewType(position) == TYPE_DATE_HEADER) {
            DateHeaderViewHolder dateHolder = (DateHeaderViewHolder) holder;
            dateHolder.bind(model);
        } else {
            ChatModelViewHolder messageHolder = (ChatModelViewHolder) holder;
            if (model.getSenderId().equals(FirebaseUtil.currentUserId())) {
                messageHolder.leftChatLayout.setVisibility(View.GONE);
                messageHolder.rightChatLayout.setVisibility(View.VISIBLE);
                messageHolder.rightChatTextview.setText(model.getMessage());

            } else {
                messageHolder.rightChatLayout.setVisibility(View.GONE);
                messageHolder.leftChatLayout.setVisibility(View.VISIBLE);
                messageHolder.leftChatTextview.setText(model.getMessage());
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_date_header, parent, false);
            return new DateHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
            return new ChatModelViewHolder(view);
        }
    }

    // ViewHolder for the message
    class ChatModelViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }

    // ViewHolder for the date header
    class DateHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView dateText;

        public DateHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_header);
        }

        public void bind(ChatMessageModel model) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            dateText.setText(dateFormat.format(model.getTimestamp().toDate()));

        }
    }
}
