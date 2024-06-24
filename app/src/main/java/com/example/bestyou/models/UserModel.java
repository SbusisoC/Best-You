package com.example.bestyou.models;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {

    private String currentWeight;
    private String targetWeight;
    private String initialWeight;
    private String username;
    private String age;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;
    private List<WeightEntry> weightHistory;

    public UserModel() {
        weightHistory = new ArrayList<>();
    }

    public UserModel(String currentWeight, String targetWeight, String initialWeight, String username, Timestamp createdTimestamp,String userId, String age) {
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.initialWeight = initialWeight;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.age = age;
        this.weightHistory = new ArrayList<>();
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getInitialWeight() { return initialWeight;}
    public void setInitialWeight(String initialWeight) { this.initialWeight = initialWeight; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAge(){return age;}
    public void setAge(String age) {this.age = age;}

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<WeightEntry> getWeightHistory() {
        return weightHistory;
    }

    public List<Map<String, Object>> getWeightHistoryAsMap() {
        List<Map<String, Object>> weightHistoryMap = new ArrayList<>();
        for (WeightEntry entry : weightHistory) {
            Map<String, Object> map = new HashMap<>();
            map.put("weight", entry.getWeight());
            map.put("timestamp", entry.getTimestamp());
            weightHistoryMap.add(map);
        }
        return weightHistoryMap;
    }

    public void addWeightEntry(String weight, Timestamp timestamp) {
        if (weightHistory == null) {
            weightHistory = new ArrayList<>();
        }
        weightHistory.add(new WeightEntry(weight, timestamp));
        setCurrentWeight(weight);
    }
}
