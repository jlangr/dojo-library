package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static java.util.Calendar.*;

public class DateUtil {
   private static final long MS_IN_DAY = 1000L * 60 * 60 * 24;
   private static Calendar calendar = GregorianCalendar.getInstance();
   private static Calendar calendar2 = GregorianCalendar.getInstance();

   public static Date create(int year, int month, int dayOfMonth) {
      calendar.set(year, month, dayOfMonth, 0, 0, 0);
      calendar.set(MILLISECOND, 0);
      return calendar.getTime();
   }

   public static Date addDays(Date date, int days) {
      return new Date(date.getTime() + days * MS_IN_DAY);
   }

   public static Date tomorrow() {
      return addDays(new Date(), 1);
   }

   // this stinks
   public static int daysFrom(Date start, Date stop) {
      calendar.setTime(start);
      Calendar stopCalendar = GregorianCalendar.getInstance();
      stopCalendar.setTime(stop);

      int startDays = calendar.get(DAY_OF_YEAR);
      int startYear = calendar.get(YEAR);
      int stopYear = stopCalendar.get(YEAR);
      int stopDays = stopCalendar.get(DAY_OF_YEAR);

      if (startYear == stopYear) return stopDays - startDays;

      int days = calendar.getActualMaximum(DAY_OF_YEAR) - startDays;
      for (int i = startYear + 1; i < stopYear; i++) {
         stopCalendar.set(YEAR, i);
         days += stopCalendar.getActualMaximum(DAY_OF_YEAR);
      }
      days += stopDays;
      return days;
   }

   public static int daysAfter(Date first, Date second) {
      if (!second.after(first))
         return 0;

      calendar.setTime(first);
      int dayOfYear1 = calendar.get(DAY_OF_YEAR);

      calendar2.setTime(second);
      int dayOfYear2 = calendar2.get(DAY_OF_YEAR);

      for (int year = calendar.get(YEAR); year < calendar2.get(YEAR); year++) {
         calendar.set(YEAR, year);
         dayOfYear2 += calendar.getActualMaximum(DAY_OF_YEAR);
      }
      return dayOfYear2 - dayOfYear1;
   }
}
