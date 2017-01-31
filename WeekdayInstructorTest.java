import org.jcp.xml.dsig.internal.MacOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mhan on 11/9/2016.
 */
public class WeekdayInstructorTest {

    private static void negativeHelper(String str){
        try {
            Time.fromString(str);
            Assert.fail(str + " is invalid string to convert to Time");
        }catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void toStringTest(){
        Assert.assertEquals("Monday", Weekday.MONDAY.toString());
        Assert.assertEquals("Tuesday", Weekday.TUESDAY.toString());
        Assert.assertEquals("Wednesday", Weekday.WEDNESDAY.toString());
        Assert.assertEquals("Thursday", Weekday.THURSDAY.toString());
        Assert.assertEquals("Friday", Weekday.FRIDAY.toString());
    }

    @Test
    public void toShortNameTest(){
        Assert.assertEquals("M", Weekday.MONDAY.toShortName());
        Assert.assertEquals("T", Weekday.TUESDAY.toShortName());
        Assert.assertEquals("W", Weekday.WEDNESDAY.toShortName());
        Assert.assertEquals("R", Weekday.THURSDAY.toShortName());
        Assert.assertEquals("F", Weekday.FRIDAY.toShortName());
    }

    @Test
    public void fromStringTest(){
        Assert.assertEquals(Weekday.MONDAY, Weekday.fromString("MoNdAy"));
        Assert.assertEquals(Weekday.MONDAY, Weekday.fromString("M"));
        Assert.assertEquals(Weekday.TUESDAY, Weekday.fromString("tueSday"));
        Assert.assertEquals(Weekday.TUESDAY, Weekday.fromString("t"));
        Assert.assertEquals(Weekday.WEDNESDAY, Weekday.fromString("WEDNESDAY"));
        Assert.assertEquals(Weekday.WEDNESDAY, Weekday.fromString("W"));
        Assert.assertEquals(Weekday.THURSDAY, Weekday.fromString("R"));
        Assert.assertEquals(Weekday.THURSDAY, Weekday.fromString("ThuRsDaY"));
        Assert.assertEquals(Weekday.FRIDAY, Weekday.fromString("fRiDay"));
        Assert.assertEquals(Weekday.FRIDAY, Weekday.fromString("f"));
    }

    @Test
    public void fromStringNegativeTest(){
        negativeHelper("hello world");
        negativeHelper("TOnSday");
        negativeHelper("SaTurDaY");
        negativeHelper("Sunday");
    }

    @Test
    public void orderTest(){
        Assert.assertTrue(Weekday.MONDAY.compareTo(Weekday.TUESDAY) < 0);
        Assert.assertTrue(Weekday.TUESDAY.compareTo(Weekday.WEDNESDAY) < 0);
        Assert.assertTrue(Weekday.WEDNESDAY.compareTo(Weekday.THURSDAY) < 0);
        Assert.assertTrue(Weekday.THURSDAY.compareTo(Weekday.FRIDAY) < 0);
    }
}
