package testutil;

import java.util.*;
import static org.junit.Assert.*;

public class DateAsserts {
   public static void assertDateEquals(Date expectedDate, Date actualDate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(expectedDate);
      int expectedYear = calendar.get(Calendar.YEAR);
      int expectedDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
      calendar.setTime(actualDate);
      assertEquals(expectedYear, calendar.get(Calendar.YEAR));
      assertEquals(expectedDayOfYear, calendar.get(Calendar.DAY_OF_YEAR));
   }
}
