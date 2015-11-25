package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MultiMapTest {
   private static final Object KEY1 = "k1";
   private static final Object VALUE1 = "v1";
   private static final Object KEY2 = "k2";
   private static final Object VALUE2 = "v2";
   private MultiMap<Object, Object> map;

   @Before
   public void initialize() {
      map = new MultiMap<Object, Object>();
   }

   @Test
   public void isEmptyOnCreation() {
      assertSize(0);
      assertValuesSize(0);
   }

   @Test
   public void returnsValuesAssociatedWithKeyAsList() {
      map.put(KEY1, VALUE1);

      List<Object> values = map.get(KEY1);

      assertValues(values, VALUE1);
      assertValuesSize(1);
   }

   @Test
   public void incrementsSizeForMultipleKeys() {
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);

      assertSize(2);
   }

   @Test
   public void valuesSizeRepresentsTotalCountOfValues() {
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);

      assertValuesSize(2);
   }

   @Test
   public void returnsOnlyValuesAssociatedWithKey() {
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);

      List<Object> values = map.get(KEY2);

      assertValues(values, VALUE2);
   }

   @Test
   public void allowsMultipleElementsSameKey() {
      map.put(KEY1, VALUE1);
      map.put(KEY1, VALUE2);

      List<Object> values = map.get(KEY1);

      assertValues(values, VALUE1, VALUE2);
   }

   @Test(expected = NullPointerException.class)
   public void throwsOnPutOfNullKey() {
      map.put(null, VALUE1);
   }

   @Test
   public void clearEliminatesAllData() {
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);

      map.clear();

      assertEquals(0, map.size());
      assertEquals(0, map.valuesSize());
   }

   @Test
   public void returnsCombinedCollectionOfAllValues() {
      map.put(KEY1, VALUE1);
      map.put(KEY2, VALUE2);

      Collection<Object> values = map.values();

      assertEquals(2, values.size());
      assertTrue(values.contains(VALUE1));
      assertTrue(values.contains(VALUE2));
   }

   // TODO better route via hamcrest
   @SafeVarargs
   private final <T> void assertValues(List<T> list, T... expected) {
      assertEquals(expected.length, list.size());
      for (int i = 0; i < expected.length; i++)
         assertEquals(expected[i], list.get(i));
   }

   private void assertSize(int expectedSize) {
      assertEquals(expectedSize == 0, map.isEmpty());
      assertEquals(expectedSize, map.size());
   }

   private void assertValuesSize(int expectedSize) {
      assertEquals(expectedSize, map.valuesSize());
   }
}