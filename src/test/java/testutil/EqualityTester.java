package testutil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualityTester {
   private Object object1;
   private Object object1Copy1;
   private Object object1Copy2;
   private Object object2;
   private Object object1Subtype;

   public EqualityTester(Object object1, Object object1Copy1,
         Object object1Copy2, Object object2, Object object1Subtype) {
      this.object1 = object1;
      this.object1Copy1 = object1Copy1;
      this.object1Copy2 = object1Copy2;
      this.object2 = object2;
      this.object1Subtype = object1Subtype;
   }

   public void verify() {
      assertTrue(object1.equals(object1Copy1));
      assertFalse(object1.equals(object2));
      assertFalse(object1.equals(object1Subtype));

      assertNullComparison();
      assertConsistency();
      assertTransitivity();
      assertReflexivity();
      assertSymmetry();
   }

   private void assertNullComparison() {
      assertFalse(object1.equals(null));
   }

   private void assertConsistency() {
      assertTrue(object1.equals(object1Copy1));
      assertFalse(object1.equals(object2));
   }

   private void assertTransitivity() {
      assertTrue(object1Copy1.equals(object1Copy2));
      assertTrue(object1.equals(object1Copy2));
   }

   private void assertSymmetry() {
      assertTrue(object1.equals(object1Copy1));
      assertTrue(object1Copy1.equals(object1));
   }

   private void assertReflexivity() {
      assertTrue(object1.equals(object1));
   }
}
