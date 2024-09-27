package com.example.bestyou.models;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class ChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String message, String senderId, Timestamp timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSameDay(ChatMessageModel other) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd MM, yyyy");
        return fmt.format(this.timestamp.toDate()).equals(fmt.format(other.timestamp.toDate()));
    }
}
