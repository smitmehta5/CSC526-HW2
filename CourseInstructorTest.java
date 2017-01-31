import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mhan on 11/26/2016.
 */
public class CourseInstructorTest {

    static Set<Weekday> toEnumSet(String days){
        Set<Weekday> set = null;
        for(int i = 0 ; i < days.length(); i++){
            if(set == null)
                set = EnumSet.of(Weekday.fromString(days.charAt(i)+""));
            else
                set.add(Weekday.fromString(days.charAt(i)+""));
        }
        return set;
    }

    private void conflictTestHelper(boolean expectedResult, String t1, String t2, String d1, String d2, int m1, int m2){
        Set<Weekday> days1 = toEnumSet(d1);
        Set<Weekday> days2 = toEnumSet(d2);

        Course c1 = new Course("EGR 111", 3, days1, Time.fromString(t1), m1);
        Course c2 = new Course("EGR 222", 3, days2, Time.fromString(t2), m2);

        Assert.assertEquals("conflict check btw [" + c1 + "] and [" + c2 + "] should return " + expectedResult,
                expectedResult, c1.conflictsWith(c2));
        //Assert.assertEquals("conflict check btw [" + c2 + "] and [" + c1 + "] should return " + expectedResult,
          //      expectedResult, c2.conflictsWith(c1));
    }

    private void containsTestHelper(boolean expectedResult, String t1, String d1, int m1, Weekday d, Time t){
        Set<Weekday> days1 = toEnumSet(d1);
        Course c1 = new Course("EGR 111", 3, days1, Time.fromString(t1), m1);
        String message = "should";
        if(!expectedResult)
            message = "should not";
        Assert.assertEquals("Course " + c1 + " " + message + " contain " + t, expectedResult, c1.contains(d, t));
    }

    private void endTimeTestHelper(String startTimeStr, String expectedEndTimeStr, int duration){
        Time startTime = Time.fromString(startTimeStr);
        Time expectedEndTime = Time.fromString(expectedEndTimeStr);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course c = new Course("EGR 111", 3, days, startTime, duration);
        Assert.assertTrue("End time should be " + expectedEndTimeStr + " when start time is " + startTimeStr +
                " and duration is " + duration, expectedEndTime.equals(c.getEndTime()));
    }

    private void constNegativeHelper(String n, int c, Set<Weekday> d, Time s, int m, String message){
        try {
            Course t = new Course(n, c, d, s, m);
            Assert.fail(message);
        }catch(IllegalArgumentException e){
        }
    }

    @Test
    public void cloneTest(){
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course egr222 = new Course("EGR 222", 3, days, startTime, 60);
        Course copy = egr222.clone();
        Assert.assertTrue(egr222.equals(copy));
        Assert.assertFalse(egr222.getStartTime() == startTime);
        Assert.assertFalse(egr222.getStartTime() == copy.getStartTime());
    }

    @Test
    public void toStringTest(){
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course egr222 = new Course("EGR 222", 3, days, startTime, 60);
        Assert.assertTrue("Course toString is incorrect", egr222.toString().equals("EGR 222,3,MWF,05:00 PM,60"));
    }

    @Test
    public void constructorTest(){
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course egr222 = new Course("EGR 222", 3, days, startTime, 60);
        Assert.assertEquals("EGR 222", egr222.getName());
        Assert.assertEquals(3, egr222.getCredits());
        Assert.assertTrue(startTime.equals(egr222.getStartTime()));
        Assert.assertEquals(60, egr222.getDuration());
        //Assert.assertTrue(days.equals(egr222.getDaysOffered()));
    }

    @Test
    public void constructorNegativeTest(){
        String name = "EGR 222";
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);

        //invalid arguments
        constNegativeHelper("", 3, days, startTime, 60, "Empty name should be invalid");
        constNegativeHelper(null, 3, days, startTime, 60, "null name should be invalid");
        constNegativeHelper("EGR222", 3, days, startTime, 60, "Name should contain space");
        constNegativeHelper(name, 0, days, startTime, 60, "0 credit should be invalid");
        constNegativeHelper(name, 6, days, startTime, 60, "6 credit or above should be invalid");
        constNegativeHelper(name, -20, days, startTime, 60, "negative credit should be invalid");
        constNegativeHelper(name, 3, null, startTime, 60, "null days should be invalid");
        constNegativeHelper(name, 3, days, null, 60, "null time should be invalid");
        constNegativeHelper(name, 3, days, startTime, 0, "duration should be greater than 0");
    }

    @Test
    public void endTimeTest(){
        endTimeTestHelper("11:59 AM", "12:00 PM", 1);
        endTimeTestHelper("11:00 AM", "12:00 PM", 60);
        endTimeTestHelper("11:59 AM", "12:00 PM", 1);
        endTimeTestHelper("11:00 AM", "01:00 PM", 120);
        endTimeTestHelper("10:00 AM", "12:05 PM", 125);
        endTimeTestHelper("07:30 AM", "10:00 AM", 150);
        endTimeTestHelper("11:00 PM", "11:59 PM", 59);
    }

    @Test
    public void startTimeTest(){
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course egr222 = new Course("EGR 222", 3, days, startTime, 60);
        Assert.assertTrue(Time.fromString("05:00 PM").equals(egr222.getStartTime()));
        startTime.shift(90);
        Assert.assertTrue(Time.fromString("06:30 PM").equals(startTime));
        Assert.assertFalse(startTime.equals(egr222.getStartTime()));
        Assert.assertTrue(Time.fromString("05:00 PM").equals(egr222.getStartTime()));
    }

    @Test
    public void conflictTest() {

        // test when days doesn't match
        conflictTestHelper(false, "05:00 PM", "05:00 PM", "MW", "TR", 1, 1);
        conflictTestHelper(false, "05:00 PM", "05:00 PM", "W", "TR", 2, 1);
        conflictTestHelper(false, "05:00 PM", "05:00 PM", "W", "TR", 1, 2);
        conflictTestHelper(false, "04:30 PM", "05:00 PM", "W", "TR", 31, 1);
        conflictTestHelper(false, "05:00 PM", "04:30 PM", "WF", "TR", 2, 32);
        conflictTestHelper(false, "11:00 AM", "12:30 PM", "T", "R", 120, 1);
    }

    @Test
    public void conflictTest2() {
        conflictTestHelper(true, "05:00 PM", "05:00 PM", "W", "W", 1, 1); //start at the same time (end at the same time)
        conflictTestHelper(true, "05:00 PM", "05:00 PM", "MW", "W", 2, 1); //start at the same time
        conflictTestHelper(true, "05:00 PM", "05:00 PM", "MWF", "W", 1, 2); //start at the same time

        conflictTestHelper(true, "04:30 PM", "05:00 PM", "MW", "W", 31, 1); //end at the same time
        conflictTestHelper(true, "05:00 PM", "04:30 PM", "MW", "W", 2, 32); //end at the same time
        conflictTestHelper(true, "05:00 PM", "04:59 PM", "MWF", "W", 1, 2); //end at the same time

        conflictTestHelper(true, "05:25 AM", "05:30 AM", "F", "F", 15, 15);

        conflictTestHelper(true, "11:00 AM", "12:30 PM", "T", "T", 120, 1); //entirely spans the other time
        conflictTestHelper(false, "05:00 PM", "04:59 PM", "MW", "W", 1, 1);
        conflictTestHelper(false, "05:00 PM", "04:59 PM", "MWF", "R", 1, 2);
        conflictTestHelper(false, "03:00 AM", "02:30 AM", "M", "M", 60 , 30);
        conflictTestHelper(false, "12:00 PM", "11:30 AM", "F", "F", 60, 15);
    }

    @Test
    public void containsTest(){
        containsTestHelper(true, "10:00 AM", "MWF", 1, Weekday.FRIDAY, new Time(10, 0, false));
        containsTestHelper(false, "10:00 AM", "MWF", 1, Weekday.TUESDAY, new Time(10, 0, false));
        containsTestHelper(false, "10:00 AM", "MWF", 1, Weekday.FRIDAY, new Time(10, 1, false));
        containsTestHelper(true, "10:00 AM", "T", 120, Weekday.TUESDAY, new Time(11,59, false));
        containsTestHelper(false, "10:00 AM", "T", 120, Weekday.TUESDAY, new Time(12,0, true));
    }

    @Test
    public void hashCodeTest(){
        Time startTime = new Time (5, 0, true);
        Set<Weekday> days =  EnumSet.of(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY);
        Course c1 = new Course("EGR 222", 3, days, startTime, 60);
        Course c2 = new Course("EGR 222", 3, days, startTime, 60);

        Set<Course> courses = new HashSet<>();
        courses.add(c1); //added c1
        Assert.assertTrue(courses.contains(c1));
        Assert.assertTrue(courses.contains(c2)); //should return true for c2 as well
        Assert.assertEquals(c1.hashCode(), c2.hashCode());
    }
}
