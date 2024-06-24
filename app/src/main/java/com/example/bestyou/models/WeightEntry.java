package com.example.bestyou.models;
import com.google.firebase.Timestamp;

public class WeightEntry {
    private String weight;
    private Timestamp timestamp;

    public WeightEntry() {}

    public WeightEntry(String weight, Timestamp timestamp) {
        this.weight = weight;
        this.timestamp = timestamp;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
