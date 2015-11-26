package testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CollectionsUtilTest {
   private Collection<Object> collection;

   @Before
   public void initialize() {
      collection = new ArrayList<>();
   }

   @Test
   public void soleElementThrowsWhenNoElementsExist() {
      try {
         CollectionsUtil.soleElement(collection);
         fail("expected assertion failure");
      } catch (AssertionError expected) {
         assertEquals(testutil.CollectionsUtil.NO_ELEMENTS,
               expected.getMessage());
      }
   }

   @Test
   public void soleElementThrowsWhenMoreThanOneElement() {
      collection.add("a");
      collection.add("b");
      try {
         CollectionsUtil.soleElement(collection);
         fail("expected assertion failure");
      } catch (AssertionError expected) {
         assertEquals(testutil.CollectionsUtil.MORE_THAN_ONE_ELEMENT,
               expected.getMessage());
      }
   }

   @Test
   public void containsExactlyReturnsTrueWithMatchingSingleElement() {
      Set<Object> set = new HashSet<>();
      set.add("c");

      assertTrue(CollectionsUtil.containsExactly(set, "c"));
   }

   @Test
   public void containsExactlyReturnsFalseWithNoMatchingElement() {
      Set<Object> set = new HashSet<>();
      set.add("c");

      assertFalse(CollectionsUtil.containsExactly(set, "b"));
   }

   @Test
   public void containsExactlyPassesWithAllMatchingMultipleElements() {
      Set<Object> set = new HashSet<>();
      set.add("c");
      set.add("a");

      assertTrue(CollectionsUtil.containsExactly(set, "a", "c"));
   }

   @Test
   public void containsExactlyFalseWithNotAllMatchingMultipleElements() {
      Set<Object> set = new HashSet<>();
      set.add("c");
      assertFalse(CollectionsUtil.containsExactly(set, "a", "c"));
   }
}