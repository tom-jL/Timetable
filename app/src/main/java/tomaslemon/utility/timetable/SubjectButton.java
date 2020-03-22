package tomaslemon.utility.timetable;

import android.content.Context;
import android.widget.Button;
import android.widget.GridLayout;

public class SubjectButton extends androidx.appcompat.widget.AppCompatButton {

    Subject subject;
    Subject.Day day;
    int hour;


    //Constructor for unassigned subject.
    public SubjectButton(Context context, Subject.Day day, int hour){
        super(context);
        this.day = day;
        this.hour = hour;
        setBackgroundColor(getResources().getColor(android.R.color.white));
        GridLayout.LayoutParams params=new GridLayout.LayoutParams();
        params.height = (int)(getResources().getDisplayMetrics().heightPixels / 9.3);
        params.bottomMargin = (int) getResources().getDisplayMetrics().density;
        params.rightMargin = (int) getResources().getDisplayMetrics().density;
        params.width = GridLayout.LayoutParams.MATCH_PARENT;
        params.columnSpec = GridLayout.spec(0);
        params.rowSpec = GridLayout.spec(hour-9);
        setLayoutParams(params);
    }

    //Constructor for assigned subject.
    public SubjectButton(Context context, Subject subject){
        super(context);
        this.subject = subject;
        this.day = subject.getDay();
        this.hour = subject.getTime();
        setText(subject.getName());
        setBackgroundColor(subject.getColor());
        GridLayout.LayoutParams params=new GridLayout.LayoutParams();
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(0);
        params.rowSpec = GridLayout.spec(hour-9, subject.getDuration());
        setLayoutParams(params);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public Subject.Day getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }


}
