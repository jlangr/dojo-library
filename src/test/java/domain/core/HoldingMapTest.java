package domain.core;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import testutil.TestUtil;

public class HoldingMapTest {
   private HoldingMap map;
   private static final Holding HOLDING1 = new Holding(MaterialTestData.THE_TRIAL);
   private static final Holding HOLDING2 = new Holding(MaterialTestData.AGILE_JAVA);

   @Before
   public void initialize() {
      map = new HoldingMap();
   }

   @Test
   public void create() {
      assertSize(0);
   }

   @Test
   public void containsFailsWhenHoldingNotFound() {
      assertFalse(map.contains(HOLDING1));
   }

   @Test
   public void containsAddedHolding() {
      map.add(HOLDING1);

      assertTrue(map.contains(HOLDING1));
   }

   @Test
   public void sizeIncrementedOnAddingHolding() {
      map.add(HOLDING1);
      assertSize(1);
   }

   @Test
   public void retrievesHoldingByBarcode() {
      map.add(HOLDING1);

      Holding retrieved = map.get(HOLDING1.getBarCode());

      assertSame(retrieved, HOLDING1);
   }

   @Test
   public void supportsMultipleHoldings() {
      map.add(HOLDING1);
      map.add(HOLDING2);

      assertSize(2);
      assertTrue(map.contains(HOLDING1));
      assertTrue(map.contains(HOLDING2));
   }

   @Test
   public void returnsAllHoldings() {
      map.add(HOLDING1);
      map.add(HOLDING2);

      Collection<Holding> holdings = map.holdings();

      assertTrue(holdings.contains(HOLDING1));
      assertTrue(holdings.contains(HOLDING2));
   }

   @Test
   public void removeHolding() {
      map.add(HOLDING1);

      map.remove(HOLDING1);

      assertFalse(map.contains(HOLDING1));
   }

   @Test
   public void removeHoldingDecrementsSize() {
      map.add(HOLDING1);

      map.remove(HOLDING1);

      assertSize(0);
   }

   @Test
   public void supportsIteration() {
      map.add(HOLDING1);
      map.add(HOLDING2);

      Collection<Holding> retrieved = new ArrayList<Holding>();
      for (Holding holding: map)
         retrieved.add(holding);

      TestUtil.containsExactly(retrieved, HOLDING1, HOLDING2);
   }

   private void assertSize(int expected) {
      assertEquals(expected == 0, map.isEmpty());
      assertEquals(expected, map.size());
   }
}
