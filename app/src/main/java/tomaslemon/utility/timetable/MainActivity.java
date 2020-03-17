package tomaslemon.utility.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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




    }

    public void buildTimetable(){
        TableRow dayRow = (TableRow) findViewById(R.id.rowDay);
        for(Subject.Day day : Subject.Day.values()){
            GridLayout dayGrid = (GridLayout) dayRow.getChildAt(day.ordinal()+1);
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

                }

            }
        }
    }

    public void dayClicked(View view) {
    }

    public void hrsClicked(View view) {
    }
}
