package com.example.android;

public class ListItem {
    private String name;
    private int duration;
    private long id;

    public ListItem(String name, int duration, long id){
        this.name = name;
        this.duration = duration;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public int getDuration(){
        return duration;
    }

    public long getId(){ return  id; }
}
