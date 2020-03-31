package tomaslemon.utility.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.GridLayout;

@SuppressLint("ViewConstructor")
public class SubjectButton extends androidx.appcompat.widget.AppCompatButton {

    Subject subject;
    Subject.Day day;
    int hour;

    boolean assigned;


    //Constructor for unassigned subject.
    public SubjectButton(Context context, Subject.Day day, int hour, int cellHeight){
        super(context);
        assigned = false;
        this.day = day;
        this.hour = hour;
        setSingleLine();
        setBackgroundColor(getResources().getColor(android.R.color.white));
        GridLayout.LayoutParams params=new GridLayout.LayoutParams();
        params.height = cellHeight;
        params.bottomMargin = (int) getResources().getDisplayMetrics().density;
        params.rightMargin = (int) getResources().getDisplayMetrics().density;
        params.width = GridLayout.LayoutParams.MATCH_PARENT;
        params.columnSpec = GridLayout.spec(0);
        params.rowSpec = GridLayout.spec(hour-9);
        setLayoutParams(params);
    }

    //Constructor for assigned subject.
    @SuppressLint("SetTextI18n")
    public SubjectButton(Context context, Subject subject, int cellHeight){
        super(context);
        assigned = true;
        this.subject = subject;
        this.day = subject.getDay();
        this.hour = subject.getTime();
        setText(subject.getName()+ " "+subject.getRoom());
        setBackgroundColor(subject.getColor());
        setLines(subject.getDuration());
        GridLayout.LayoutParams params=new GridLayout.LayoutParams();
        params.height = cellHeight * subject.getDuration();
        params.bottomMargin = (int) getResources().getDisplayMetrics().density;
        params.rightMargin = (int) getResources().getDisplayMetrics().density;
        params.width = GridLayout.LayoutParams.MATCH_PARENT;
        params.columnSpec = GridLayout.spec(0);
        params.rowSpec = GridLayout.spec(hour-9, subject.getDuration());
        setLayoutParams(params);
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void resetColor(){
        setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    public void setSelected(){
        setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    public Subject.Day getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public Subject getSubject() {
        return subject;
    }
}
