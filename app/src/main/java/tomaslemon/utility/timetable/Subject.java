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

    public Subject(){
        this.name = "";
        this.day = Day.MON;
        this.time = 9;
        this.duration = 1;
        this.color = Color.BLUE;
        this.room = "";
    }

    public Subject(long id, String name, Day day, int time, int duration, int color, String room){
        this.id = id;
        this.name = name;
        this.day = day;
        this.time = time;
        this.duration = duration;
        this.color = color;
        this.room = room;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTime(int time) {
        this.time = time;
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

    public long getId() {
        return id;
    }
}
