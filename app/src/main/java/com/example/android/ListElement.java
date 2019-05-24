package com.example.android;

/**
 * Klasse fuer die ListenElemente, in denen spaeter Informationen uebergeben werden koennen
 */
public class ListElement {

    private String workoutName;
    private int durationSeconds;
    private int sets;

    public ListElement(){

    }

    public ListElement(String workoutName, int durationSeconds, int sets){
        this.workoutName=workoutName;
        this.durationSeconds=durationSeconds;
        this.sets=sets;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public int getSets() {
        return sets;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }
}
