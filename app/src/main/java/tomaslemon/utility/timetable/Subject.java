package tomaslemon.utility.timetable;


public class Subject {


    private String name;
    private Day day;
    private Float time;
    private Float duration;


    public enum Day{
        MON,
        TUE,
        WED,
        THU,
        FRI
    }


    public Subject(String name, Day day, Float time, Float duration){
        this.name = name;
        this.day = day;
        this.time = time;
        this.duration = duration;
    }

    public Day getDay() {
        return day;
    }

    public Float getDuration() {
        return duration;
    }

    public Float getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
