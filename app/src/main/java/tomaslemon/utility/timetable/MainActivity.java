package tomaslemon.utility.timetable;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Subject> subjects;
    ArrayList<SubjectButton> selectedBtns; //temporarily selected buttons for subject creation.

    int cellHeight;

    Button selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjects = new ArrayList<>();
        selectedBtns = new ArrayList<>();

        cellHeight = (int)(getResources().getDisplayMetrics().heightPixels / 9.3);

        getSubjects();
        buildTimetable(cellHeight);



    }

    private void getSubjects() {
        subjects.clear();
        SQLiteOpenHelper subjectDatabaseHelper = new SubjectDatabaseHelper(this);
        try{
            SQLiteDatabase db = subjectDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("SUBJECT",
                    null, null,null,null,null,null);
            if (cursor.moveToFirst()){
                subjects.add(new Subject(cursor.getLong(0),
                        cursor.getString(1),
                        Subject.Day.values()[cursor.getInt(2)],
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getString(6)));
                while(cursor.moveToNext()){
                    subjects.add(new Subject(cursor.getLong(0),
                            cursor.getString(1),
                            Subject.Day.values()[cursor.getInt(2)],
                            cursor.getInt(3),
                            cursor.getInt(4),
                            cursor.getInt(5),
                            cursor.getString(6)));
                }
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void buildTimetable(int cellHeight){
        TableRow tableRow = (TableRow) findViewById(R.id.subjectsRow);
        boolean available;
        for(Subject.Day day : Subject.Day.values()){
            GridLayout dayGrid = (GridLayout) tableRow.getChildAt(day.ordinal()+1);
            dayGrid.removeAllViewsInLayout();
            for(int hour = 9; hour<17; hour++){
                available = true;
                for(Subject subject : subjects){
                    if(subject.getDay() == day && subject.getTime() == hour){
                        available = false;
                        SubjectButton subjectBtn = new SubjectButton(this, subject, cellHeight);
                        subjectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: Add button expansion capability.
                            }
                        });
                        dayGrid.addView(subjectBtn);
                        hour += subject.getDuration()-1;
                        //add duration onto hours to skip rows, make rowSpan based on duration
                    }
                }
                if(available){
                    SubjectButton addSubjectBtn = new SubjectButton(this, day, hour, cellHeight);
                    addSubjectBtn.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            SubjectButton subjectButton = (SubjectButton) v;
                            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                                selectedBtns.add(subjectButton);
                                return true;
                            } else if (event.getAction() == MotionEvent.ACTION_UP){
                                addSubject(subjectButton);
                                selectedBtns.clear();
                            }
                            return false;
                        }
                    });
                    dayGrid.addView(addSubjectBtn);
                }

            }
        }
    }

    public void addSubject(View view){
            int hour = selectedBtns.get(0).getHour();
            Subject.Day day = selectedBtns.get(0).getDay();
            int duration = selectedBtns.size();
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra(SubjectActivity.EXTRA_HOUR,hour);
            intent.putExtra(SubjectActivity.EXTRA_DAY,day);
            intent.putExtra(SubjectActivity.EXTRA_DURATION,duration);
            startActivityForResult(intent,SubjectActivity.SUBJECT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SubjectActivity.SUBJECT_REQUEST){
            if(resultCode == RESULT_OK){
                getSubjects();
                buildTimetable(cellHeight);
            }
        }


    }

    public void dayClicked(View view) {
        TableLayout timeTable = (TableLayout) findViewById(R.id.timeTable);
        TableRow tableRow = (TableRow) timeTable.getChildAt(0);
        if(view == selectedDay){
            for(int i = 1; i < tableRow.getChildCount(); i++){
                timeTable.setColumnCollapsed(i,false);
            }
            selectedDay = null;
        } else {
            selectedDay = (Button) view;
            for(int i = 1; i < tableRow.getChildCount(); i++){
                if(!(tableRow.getChildAt(i) == selectedDay)){
                    timeTable.setColumnCollapsed(i,true);
                }
            }
        }
    }

    public void hrsClicked(View view) {
    }

}
