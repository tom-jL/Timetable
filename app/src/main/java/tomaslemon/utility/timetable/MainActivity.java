package tomaslemon.utility.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Subject> subjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subjects = new ArrayList<>();

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

    public void buildTimetable(){
        TableRow tableRow = (TableRow) findViewById(R.id.subjectsRow);
        for(Subject.Day day : Subject.Day.values()){
            GridLayout dayGrid = (GridLayout) tableRow.getChildAt(day.ordinal()+1);
            for(int hour = 9; hour<17; hour++){
                boolean available = true;
                for(Subject subject : subjects){
                    if(subject.getDay() == day && subject.getTime() == hour){
                        available = false;
                        Button subjectBtn = new Button(this);
                        subjectBtn.setText(subject.getName());
                        subjectBtn.setBackgroundColor(subject.getColor());
                        GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        params.columnSpec = GridLayout.spec(0);
                        params.rowSpec = GridLayout.spec(hour-9, subject.getDuration());
                        subjectBtn.setLayoutParams(params);
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
                    ImageButton addSubjectBtn = new ImageButton(this);
                    addSubjectBtn.setImageResource(android.R.drawable.ic_input_add);
                    GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                    params.height = 65;
                    params.width = GridLayout.LayoutParams.MATCH_PARENT;
                    params.columnSpec = GridLayout.spec(0);
                    params.rowSpec = GridLayout.spec(hour-9);
                    addSubjectBtn.setLayoutParams(params);
                    //addSubjectClick(addSubjectBtn,day,hour);
                    dayGrid.addView(addSubjectBtn);
                }

            }
        }
    }

    private void addSubjectClick(final ImageButton btn, final Subject.Day day,final int hour){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject(v, day, hour);
            }
        });
    }

    public void addSubject(View view, Subject.Day day, int hour){
            Intent intent = new Intent(this, SubjectActivity.class);

    }

    public void dayClicked(View view) {
    }

    public void hrsClicked(View view) {
    }
}
