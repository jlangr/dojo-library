import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import java.util.*;
import org.junit.Test;

public class DateUtilTest {
    public static final Date NEW_YEARS_DAY = DateUtil.create(2011, Calendar.JANUARY, 1);
    public static final Date GROUNDHOG_DAY = DateUtil.create(2011, Calendar.FEBRUARY, 2);

    @Test
    public void createGeneratedProperDateElements() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(NEW_YEARS_DAY);
        assertThat(calendar.get(Calendar.YEAR), equalTo(2011));
        assertThat(calendar.get(Calendar.MONTH), equalTo(Calendar.JANUARY));
        assertThat(calendar.get(Calendar.DAY_OF_MONTH), equalTo(1));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), equalTo(0));
        assertThat(calendar.get(Calendar.MINUTE), equalTo(0));
        assertThat(calendar.get(Calendar.SECOND), equalTo(0));
        assertThat(calendar.get(Calendar.MILLISECOND), equalTo(0));
    }

    @Test
    public void addDays() {
        assertThat(DateUtil.addDays(NEW_YEARS_DAY, 1), equalTo(DateUtil.create(2011, Calendar.JANUARY, 2)));
        assertThat(DateUtil.addDays(NEW_YEARS_DAY, 367), equalTo(DateUtil.create(2012, Calendar.JANUARY, 3)));
    }

    @Test
    public void answersDaysFromWithinSameYear() {
        Date laterBy15 = DateUtil.addDays(NEW_YEARS_DAY, 15);

        assertThat(DateUtil.daysFrom(NEW_YEARS_DAY, laterBy15), equalTo(15));
    }

    @Test
    public void answersDaysFromToNextYear() {
        Date laterBy375 = DateUtil.addDays(NEW_YEARS_DAY, 375);

        assertThat(DateUtil.daysFrom(NEW_YEARS_DAY, laterBy375), equalTo(375));
    }

    @Test
    public void answersDaysFromManyYearsOut() {
        Date later = DateUtil.addDays(NEW_YEARS_DAY, 2100);

        assertThat(DateUtil.daysFrom(NEW_YEARS_DAY, later), equalTo(2100));
    }
}
