package tomaslemon.utility.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectActivity extends AppCompatActivity {

    public static final int SUBJECT_REQUEST = 200;

    public static final String EXTRA_HOUR = "hour";
    public static final String EXTRA_DAY = "day";
    public static final String EXTRA_DURATION = "duration";

    Subject subject;


    /*
    On creation of this activity the layout is setup, information from the main activity is retrieved,
    a new subject is created and the title of the activity is set to the current timeslot.
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Intent intent = getIntent();
        int hour = intent.getIntExtra(EXTRA_HOUR,9);
        int duration = intent.getIntExtra(EXTRA_DURATION, 1);
        Subject.Day day = (Subject.Day) intent.getSerializableExtra(EXTRA_DAY);

        subject = new Subject(hour, duration, day);

        TextView timeView = findViewById(R.id.subjectTime);
        timeView.setText(hour + " " + day);

    }

    /*
    If a colour button is pressed the subject colour is set to the same colour.
     */
    public void selectColour(View view) {
        Button colorBtn = (Button) view;
        GridLayout colorBtns = findViewById(R.id.colorBtns);
        for(int i = 0; i < colorBtns.getChildCount(); i ++){
            ((Button)colorBtns.getChildAt(i)).setText("");
        }
        colorBtn.setText("X");
        subject.setColor(((ColorDrawable)colorBtn.getBackground()).getColor());
    }

    /*
    Option to cancel the subject creation.
     */
    public void cancelSubject(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /*
    When the user pressed Okay to add the subject, the subject members are set and the subject is
    added to the database. The app then returns to the main activity.
     */
    public void addSubject(View view) {
        subject.setName(((EditText)findViewById(R.id.subjectName)).getText().toString());
        subject.setRoom(((EditText)findViewById(R.id.subjectRoom)).getText().toString());

        SubjectDatabaseHelper subjectDatabaseHelper = new SubjectDatabaseHelper(this);
        try{
            subjectDatabaseHelper.insertSubject(subject);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
