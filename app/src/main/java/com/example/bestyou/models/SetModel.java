package com.example.bestyou.models;

public class SetModel {
    private int setNumber;
    private int reps;
    private int weight;

    public SetModel(int setNumber, int reps, int weight) {
        this.setNumber = setNumber;
        this.reps = reps;
        this.weight = weight;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }
}
