package com.example.bestyou.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    List<String> repsMap = new ArrayList<>();
    List<String> weightsMap = new ArrayList<>();

    public MyCartModel() {
    }

    public MyCartModel(String img_url, String workoutName, String bodyPart, String numberOfReps, String numberOfSets, String dayPlanned, Long time, String documentId, String currentDate, String currentTime, String workoutTime, String type, String timestamp, boolean isChecked, boolean isDateHeader, List<String> repsMap, List<String> weightsMap) {
        this.img_url = img_url;
        this.workoutName = workoutName;
        this.bodyPart = bodyPart;
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.dayPlanned = dayPlanned;
        this.time = time;
        this.documentId = documentId;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.workoutTime = workoutTime;
        this.type = type;
        this.timestamp = timestamp;
        this.isChecked = isChecked;
        this.isDateHeader = isDateHeader;
        this.repsMap = repsMap;
        this.weightsMap = weightsMap;
    }

/*public MyCartModel(String img_url, String workoutName, String bodyPart, String numberOfReps, String numberOfSets, String dayPlanned, String currentDate, String currentTime, String workoutTime, String type) {
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
    }*/

    public String getSetRepsForPosition(int setNumber) {
        if (repsMap.size() >= setNumber){
            return repsMap.get(setNumber);
        }
        return "0";
    }

    public List<String> getRepsMap() {
        return repsMap;
    }

    public List<String> getWeightsMap() {
        return weightsMap;
    }

    public String getSetWeightForPosition(int setNumber) {
        if (weightsMap.size() >= setNumber){
            return weightsMap.get(setNumber);
        }
        return "0";
    }

    public void setRepsMap(List<String> repsMap) {
        this.repsMap = repsMap;
    }

    public void setWeightsMap(List<String> weightsMap) {
        this.weightsMap = weightsMap;
    }

    public void setSetReps(String reps) {
        repsMap.add(reps);
    }

    public void setSetWeight(String weight) {
        weightsMap.add(weight);
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

    @Override
    public String toString() {
        return "MyCartModel{" +
                "img_url='" + img_url + '\'' +
                ", workoutName='" + workoutName + '\'' +
                ", bodyPart='" + bodyPart + '\'' +
                ", numberOfReps='" + numberOfReps + '\'' +
                ", numberOfSets='" + numberOfSets + '\'' +
                ", dayPlanned='" + dayPlanned + '\'' +
                ", time=" + time +
                ", documentId='" + documentId + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", workoutTime='" + workoutTime + '\'' +
                ", type='" + type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isChecked=" + isChecked +
                ", isDateHeader=" + isDateHeader +
                ", repsMap=" + repsMap +
                ", weightsMap=" + weightsMap +
                '}';
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
