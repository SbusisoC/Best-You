package com.example.bestyou.models;

import java.io.Serializable;
public class ShowAllWorkoutsModel implements Serializable {

    String name;
    String img_url;
    String type;
    String bodyPart;

    public ShowAllWorkoutsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public ShowAllWorkoutsModel(String name, String img_url, String type, String bodyPart) {
        this.name = name;
        this.img_url = img_url;
        this.type = type;
        this.bodyPart = bodyPart;
    }
}
