package persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import testutil.CollectionsUtil;
import domain.core.MaterialTestData;
import domain.core.Holding;

public class HoldingStoreTest {
   private HoldingStore store;
   private Holding savedHolding;

   @Before
   public void setUp() {
      HoldingStore.deleteAll();
      store = new HoldingStore();
      savedHolding = new Holding(MaterialTestData.THE_TRIAL);
      store.save(savedHolding);
   }

   @Test
   public void returnsAddedHoldings() {
      List<Holding> retrieved = store.findAll(savedHolding.getMaterial()
            .getClassification());

      assertEquals(savedHolding.getMaterial(),
            CollectionsUtil.soleElement(retrieved).getMaterial());
   }

   @Test
   public void returnsNewInstanceOnRetrieval() {
      store = new HoldingStore();

      List<Holding> retrieved = store.findAll(savedHolding.getMaterial()
            .getClassification());

      assertNotSame(savedHolding, CollectionsUtil.soleElement(retrieved));
   }

   @Test
   public void getAllReturnsAllSavedHoldings() {
      Holding holding2 = new Holding(MaterialTestData.AGILE_JAVA);
      store.save(holding2);

      Collection<Holding> retrieved = store.getAll();

      CollectionsUtil.containsExactly(retrieved, savedHolding, holding2);
   }

   @Test
   public void findBarCodeReturnsMatchingHolding() {
      String barCode = Holding.createBarCode(MaterialTestData.KAFKA_CLASSIFICATION,
            1);

      Holding holding = store.find(barCode);

      assertEquals(MaterialTestData.THE_TRIAL, holding.getMaterial());
      assertEquals(barCode, holding.getBarCode());
   }

   @Test
   public void findBarCodeNotFound() {
      assertNull(store.find("nonexistent barcode:1"));
   }
}