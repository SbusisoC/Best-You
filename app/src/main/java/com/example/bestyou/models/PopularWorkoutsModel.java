package com.example.bestyou.models;

import java.io.Serializable;

public class PopularWorkoutsModel implements Serializable {

    String name;
    String bodyPart;
    String img_url;

    public PopularWorkoutsModel() {
    }

    public PopularWorkoutsModel(String name, String bodyPart, String img_url) {
        this.name = name;
        this.bodyPart = bodyPart;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
