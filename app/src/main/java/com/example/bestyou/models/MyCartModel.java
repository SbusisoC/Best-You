package com.example.bestyou.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {

    String img_url;
    String workoutName;
    String bodyPart;
    String numberOfReps;
    String numberOfSets;
    String dayPlanned;
    String documentId;
    private boolean isChecked;

    public MyCartModel() {
    }


    public MyCartModel(String img_url, String workoutName, String bodyPart, String numberOfReps, String numberOfSets, String dayPlanned) {
        this.img_url = img_url;
        this.workoutName = workoutName;
        this.bodyPart = bodyPart;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.dayPlanned = dayPlanned;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

}
