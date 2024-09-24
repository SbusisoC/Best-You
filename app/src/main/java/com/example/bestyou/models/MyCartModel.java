package com.example.bestyou.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyCartModel implements Serializable {

    String img_url;
    String workoutName;
    String bodyPart;
    String numberOfReps;
    String numberOfSets;
    String dayPlanned;
    Long time;
    String documentId;
    String currentDate;
    String currentTime;
    String workoutTime;
    String type;
    private String timestamp;
    private boolean isChecked;
    private boolean isDateHeader;

    private Map<Integer, String> repsMap = new HashMap<>();
    private Map<Integer, String> weightsMap = new HashMap<>();

    public MyCartModel() {
    }


    public MyCartModel(String img_url, String workoutName, String bodyPart, String numberOfReps, String numberOfSets, String dayPlanned, String currentDate, String currentTime, String workoutTime, String type) {
        this.img_url = img_url;
        this.workoutName = workoutName;
        this.bodyPart = bodyPart;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.dayPlanned = dayPlanned;
        this.isDateHeader = false;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.workoutTime = workoutTime;
        this.type= type;
    }

    public String getSetReps(int setNumber) {
        return repsMap.getOrDefault(setNumber, "0");
    }

    public String getSetWeight(int setNumber) {
        return weightsMap.getOrDefault(setNumber, "0");
    }

    public void setSetReps(int setNumber, String reps) {
        repsMap.put(setNumber, reps);
    }

    public void setSetWeight(int setNumber, String weight) {
        weightsMap.put(setNumber, weight);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public boolean isDateHeader() {
        return isDateHeader;
    }

    public void setDateHeader(boolean dateHeader) {
        isDateHeader = dateHeader;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getNumberOfReps() {
        return numberOfReps;
    }

    public void setNumberOfReps(String numberOfReps) {
        this.numberOfReps = numberOfReps;
    }

    public String getNumberOfSets() {
        return numberOfSets;
    }

    public void setNumberOfSets(String numberOfSets) {
        this.numberOfSets = numberOfSets;
    }

    public String getDayPlanned(){
        return dayPlanned;
    }

    public void setDayPlanned(String dayPlanned){
        this.dayPlanned = dayPlanned;
    }

    public String getCurrentDate(){
        return currentDate;
    }

    public void setCurrentDate(String currentDate){
        this.currentDate = currentDate;
    }
    public String getCurrentTime(){
        return currentTime;
    }
    public void setCurrentTime(String currentTime){
        this.currentTime = currentTime;
    }
    public String getWorkoutTime(){
        return workoutTime;
    }
    public void setWorkoutTime(String workoutTime){
        this.workoutTime = workoutTime;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

}
