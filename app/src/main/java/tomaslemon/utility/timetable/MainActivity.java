package tomaslemon.utility.timetable;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjects = new ArrayList<>();
        selectedBtns = new ArrayList<>();

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

        buildTimetable();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void buildTimetable(){
        TableRow tableRow = (TableRow) findViewById(R.id.subjectsRow);
        boolean available;
        for(Subject.Day day : Subject.Day.values()){
            GridLayout dayGrid = (GridLayout) tableRow.getChildAt(day.ordinal()+1);
            for(int hour = 9; hour<17; hour++){
                available = true;
                for(Subject subject : subjects){
                    if(subject.getDay() == day && subject.getTime() == hour){
                        available = false;
                        SubjectButton subjectBtn = new SubjectButton(this, subject);
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
                    SubjectButton addSubjectBtn = new SubjectButton(this, day, hour);
                    addSubjectBtn.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            SubjectButton subjectButton = (SubjectButton) v;
                            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                                subjectButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                                subjectButton.setText(subjectButton.getHour() + "" + subjectButton.getDay()); // For testing
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

            Intent intent = new Intent(this, SubjectActivity.class);
    }

    public void dayClicked(View view) {
    }

    public void hrsClicked(View view) {
    }
}
