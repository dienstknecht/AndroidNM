package com.example.android;

public class ListItem {
    private String name;
    private int duration;

    public ListItem(String name, int duration){
        this.name = name;
        this.duration = duration;

    }

    public String getName(){
        return this.name;
    }

    public int getDuration(){
        return duration;
    }
}
