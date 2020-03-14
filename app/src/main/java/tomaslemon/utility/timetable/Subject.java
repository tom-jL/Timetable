package tomaslemon.utility.timetable;


import android.graphics.Color;

public class Subject {


    private String name;
    private Day day;
    private int time;
    private int duration;
    private int color;
    private String room;


    public enum Day{
        MON,
        TUE,
        WED,
        THU,
        FRI
    }

    public Subject(){
        this.name = "";
        this.day = Day.MON;
        this.time = 9;
        this.duration = 1;
        this.color = Color.BLUE;
        this.room = "";
    }

    public Subject(String name, Day day, int time, int duration, int color, String room){
        this.name = name;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.color = color;
        this.room = room;
    }

    public Day getDay() {
        return day;
    }

    public int getDuration() {
        return duration;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getRoom() {
        return room;
    }
}
