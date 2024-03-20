package com.example.bestyou.models;

public class WorkoutPlansModel {

    String img_url;
    String name;
    String dayPlanned;

    public WorkoutPlansModel() {
    }

    public WorkoutPlansModel(String img_url, String name, String dayPlanned) {
        this.img_url = img_url;
        this.name = name;
        this.dayPlanned = dayPlanned;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDayPlanned() {
        return dayPlanned;
    }

    public void setDayPlanned(String dayPlanned) {
        this.dayPlanned = dayPlanned;
    }
}
