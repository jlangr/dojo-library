package util;

import java.util.*;

public class ListUtil {
   // TODO not tested!
   @SafeVarargs
   public static <T> List<T> list(T... values) {
      List<T> list = new ArrayList<T>();
      Collections.addAll(list, values);
      return list;
   }
}
