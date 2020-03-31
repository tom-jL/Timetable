package tomaslemon.utility.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SubjectDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "subjects";
    private static final int DB_VERSION = 1;

    SubjectDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE SUBJECT (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "NAME TEXT, "
            + "DAY INTEGER, "
            + "TIME INTEGER, "
            + "DURATION INTEGER, "
            + "COLOR INTEGER, "
            + "ROOM TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    void insertSubject(Subject subject){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues subjectValues = new ContentValues();
        subjectValues.put("NAME", subject.getName());
        subjectValues.put("DAY", subject.getDay().ordinal());
        subjectValues.put("TIME", subject.getTime());
        subjectValues.put("DURATION", subject.getDuration());
        subjectValues.put("COLOR", subject.getColor());
        subjectValues.put("ROOM", subject.getRoom());
        long id = db.insert("SUBJECT", null, subjectValues);
        subject.setId(id);
        db.close();
    }

    public void deleteSubject(Subject subject){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("SUBJECT","_id = ?", new String[] {Long.toString(subject.getId())});
        db.close();
    }
}
