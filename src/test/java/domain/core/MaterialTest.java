package domain.core;

import static org.junit.Assert.assertEquals;

import com.loc.material.api.*;

public class MaterialTest {
   public static void assertMaterialsEqual(MaterialDetails expected, MaterialDetails actual) {
      assertEquals(expected.getAuthor(), actual.getAuthor());
      assertEquals(expected.getClassification(), actual.getClassification());
      assertEquals(expected.getTitle(), actual.getTitle());
      assertEquals(expected.getYear(), actual.getYear());
   }
}