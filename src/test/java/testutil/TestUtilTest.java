package testutil;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class TestUtilTest {
   private Collection<Object> collection;
   private HashSet<Object> set;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   @Before
   public void initialize() {
      collection = new ArrayList<>();
      set = new HashSet<>();
   }

   @Test
   public void soleElementThrowsWhenNoElementsExist() {
      expect(AssertionError.class, testutil.TestUtil.NO_ELEMENTS);

      TestUtil.soleElement(collection);
   }

   @Test
   public void soleElementThrowsWhenMoreThanOneElement() {
      expect(AssertionError.class, testutil.TestUtil.MORE_THAN_ONE_ELEMENT);
      collection.add("a");
      collection.add("b");

      TestUtil.soleElement(collection);
   }

   @Test
   public void containsExactlyReturnsTrueWithMatchingSingleElement() {
      set.add("c");

      assertTrue(TestUtil.containsExactly(set, "c"));
   }

   @Test
   public void containsExactlyReturnsFalseWithNoMatchingElement() {
      set.add("c");

      assertFalse(TestUtil.containsExactly(set, "b"));
   }

   @Test
   public void containsExactlyPassesWithAllMatchingMultipleElements() {
      set.add("c");
      set.add("a");

      assertTrue(TestUtil.containsExactly(set, "a", "c"));
   }

   @Test
   public void containsExactlyFalseWithNotAllMatchingMultipleElements() {
      set.add("c");

      assertFalse(TestUtil.containsExactly(set, "a", "c"));
   }

   private void expect(Class<AssertionError> exceptionClass, String expectedMessage) {
      exception.expect(exceptionClass);
      exception.expectMessage(expectedMessage);
   }
}