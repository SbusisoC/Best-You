package com.example.bestyou.models;

import java.io.Serializable;

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
    private String timestamp;
    private boolean isChecked;
    private boolean isDateHeader;
    public MyCartModel() {
    }


    public MyCartModel(String img_url, String workoutName, String bodyPart, String numberOfReps, String numberOfSets, String dayPlanned, String currentDate, String currentTime) {
        this.img_url = img_url;
        this.workoutName = workoutName;
        this.bodyPart = bodyPart;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.dayPlanned = dayPlanned;
        this.isDateHeader = false;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
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

}
