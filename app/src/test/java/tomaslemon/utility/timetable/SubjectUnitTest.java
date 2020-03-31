package tomaslemon.utility.timetable;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubjectUnitTest {

    @Test
    public void constructor_isCorrect(){
        Subject subject = new Subject(9,1,Subject.Day.MON);
        assertEquals("",subject.getName());
        assertEquals(Subject.Day.MON, subject.getDay());
        assertEquals(9,subject.getTime());
        assertEquals(1,subject.getDuration());
        assertEquals(Color.BLUE, subject.getColor());
        assertEquals("",subject.getRoom());
    }

}
