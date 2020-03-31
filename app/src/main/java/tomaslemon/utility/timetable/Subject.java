package tomaslemon.utility.timetable;


import android.graphics.Color;

public class Subject {


    private String name;
    private Day day;
    private int time;
    private int duration;
    private int color;
    private String room;
    private long id;


    public enum Day{
        MON,
        TUE,
        WED,
        THU,
        FRI
    }

    Subject(int time, int duration, Day day){
        this.name = "";
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.color = Color.BLUE;
        this.room = "";
    }



    Subject(long id, String name, Day day, int time, int duration, int color, String room){
        this.id = id;
        this.name = name;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.color = color;
        this.room = room;
    }

    void setColor(int color) {
        this.color = color;
    }

    void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setRoom(String room) {
        this.room = room;
    }

    Day getDay() {
        return day;
    }

    int getDuration() {
        return duration;
    }

    int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    int getColor() {
        return color;
    }

    String getRoom() {
        return room;
    }

    long getId() {
        return id;
    }
}
