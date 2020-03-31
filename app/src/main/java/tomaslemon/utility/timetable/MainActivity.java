package tomaslemon.utility.timetable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Subject> subjects; //subjects to get displayed on timetable
    ArrayList<SubjectButton> selectedBtns; //temporarily selected buttons for subject creation.
    int cellHeight;
    Button selectedDay; // current maximized day
    Handler btnHandler; // button handler for deleting subjects when pressed.
    boolean justRemovedSubject; //flag to show if subject just deleted.


    /*
    On creation of this main activity, the layouts are setup and members are initialized.
    The appropriate cell height based on the devices screen is set.
    The subjects array is populated from the database and the timetable is built.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize
        subjects = new ArrayList<>();
        selectedBtns = new ArrayList<>();

        btnHandler = new Handler();
        justRemovedSubject = false;

        cellHeight = getResources().getDisplayMetrics().heightPixels / 9;
        TableRow rowDay = findViewById(R.id.rowDay);
        rowDay.setMinimumHeight(cellHeight);

        getSubjects();
        buildTimetable();

    }


    /*
    This method first clears the current subjects in the app then retrieves subjects stored in the database
    and stores the data in the MainActivity subjects array.
    If there is any error with retrieving each subject a toast message will display.
     */
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

    /*
    This method is responsible for building the time table including all current subjects and
     inactive subjects (buttons) that have not been assigned yet as well as their touch events to add and remove
     them. Each day has its own GridLayout that subject Buttons can be assigned to. Each GridLayout also deals
     with touch events that determine where a subject is created and how long for. If a subject button is held down
     for more than a second it will be removed from the database and then the view will be created without it.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void buildTimetable(){
        TableRow tableRow = findViewById(R.id.subjectsRow);
        boolean available;
        for(Subject.Day day : Subject.Day.values()){
            final GridLayout dayGrid = (GridLayout) tableRow.getChildAt(day.ordinal()+1);
            dayGrid.removeAllViewsInLayout();
            for(int hour = 9; hour<17; hour++){
                available = true;
                for(Subject subject : subjects){
                    if(subject.getDay() == day && subject.getTime() == hour){
                        available = false;
                        final SubjectButton subjectBtn = new SubjectButton(this, subject, cellHeight);
                        final Runnable removeSubjectTask = new Runnable() {
                            @Override
                            public void run() {
                                removeSubject(subjectBtn);

                            }
                        };
                        subjectBtn.setOnTouchListener(new OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(event.getAction() == MotionEvent.ACTION_DOWN){
                                    btnHandler.postDelayed(removeSubjectTask, 1000);
                                } else if (event.getAction() == MotionEvent.ACTION_UP){
                                    btnHandler.removeCallbacks(removeSubjectTask);
                                }
                                return true;
                            }
                        });
                        dayGrid.addView(subjectBtn);
                        hour += subject.getDuration()-1;
                    }
                }
                if(available){
                    SubjectButton addSubjectBtn = new SubjectButton(this, day, hour, cellHeight);
                    addSubjectBtn.setClickable(false);
                    dayGrid.addView(addSubjectBtn);
                }

            }
            dayGrid.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN){
                        justRemovedSubject = false;
                        for(int i = 0; i < dayGrid.getChildCount(); i++){
                            SubjectButton subjectBtn = (SubjectButton) dayGrid.getChildAt(i);
                            if(event.getY() >= subjectBtn.getY() && event.getY() <= subjectBtn.getY() + subjectBtn.getHeight() && !subjectBtn.isAssigned()){
                                subjectBtn.setSelected();
                                if(!selectedBtns.contains(subjectBtn))
                                    selectedBtns.add(subjectBtn);
                            }
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP){
                        if(!justRemovedSubject)
                            addSubject();
                        for(SubjectButton subjectBtn : selectedBtns){
                            subjectBtn.resetColor();
                        }
                        selectedBtns.clear();
                    }
                    return true;
                }
            });

        }

    }

    /*
    This method will add a subject based on the first selected button from selected buttons array.
    It takes the buttons time and duration and passes this information to the subject activity where
    the subject details can be configured.
     */
    public void addSubject(){
            int hour = selectedBtns.get(0).getHour();
            Subject.Day day = selectedBtns.get(0).getDay();
            int duration = selectedBtns.size();
            Intent intent = new Intent(this, SubjectActivity.class);
            intent.putExtra(SubjectActivity.EXTRA_HOUR,hour);
            intent.putExtra(SubjectActivity.EXTRA_DAY,day);
            intent.putExtra(SubjectActivity.EXTRA_DURATION,duration);
            startActivityForResult(intent,SubjectActivity.SUBJECT_REQUEST);
    }

    /*
    This method removes the subject associated with the passed subject button from the database
    and current array. It then rebuilds the timetable.
     */
    public void removeSubject(SubjectButton subjectBtn){
        SubjectDatabaseHelper subjectDatabaseHelper = new SubjectDatabaseHelper(this);
        try{
            subjectDatabaseHelper.deleteSubject(subjectBtn.getSubject());
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        subjects.remove(subjectBtn.getSubject());
        justRemovedSubject = true;
        buildTimetable();
    }


    /*
    The result of the subject activity will be passed back to the main activity and if a subject was created
    the subjects array will be populated and the timetable rebuilt.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SubjectActivity.SUBJECT_REQUEST){
            if(resultCode == RESULT_OK){
                getSubjects();
                buildTimetable();
            }
        }

    }

    //If a day button in the header of the timetable was clicked than maximize the day, if clicked again minimize it.
    public void dayClicked(View view) {
        TableLayout timeTable = findViewById(R.id.timeTable);
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

}
