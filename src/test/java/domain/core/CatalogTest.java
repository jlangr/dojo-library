package domain.core;

import static domain.core.BranchTest.*;
import static domain.core.MaterialTestData.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import persistence.*;
import static java.util.Arrays.*;

public class CatalogTest {
   private Catalog catalog;

   @Before
   public void initialize() {
      catalog = new Catalog();
      HoldingStore.deleteAll();
   }

   @Test
   public void isEmptyOnCreation() {
      assertEquals(0, catalog.size());
   }

   @Test
   public void incrementsSizeWhenMaterialAdded() {
      catalog.add(new Holding(MaterialTestData.THE_TRIAL, BRANCH_EAST));

      assertEquals(1, catalog.size());
   }

   @Test
   public void answersEmptyForNonexistentMaterial() {
      assertTrue(catalog.findAll(KAFKA_CLASSIFICATION).isEmpty());
   }

   @Test
   public void findAllReturnsListOfHoldings() {
      Holding holding1 = new Holding(THE_TRIAL, BRANCH_EAST, 1);
      Holding holding2 = new Holding(THE_TRIAL, BRANCH_EAST, 2);
      catalog.add(holding1);
      catalog.add(holding2);

      List<Holding> holdings = catalog.findAll(KAFKA_CLASSIFICATION);

      assertEquals(asList(holding1, holding2), holdings);
   }

   @Test
   public void findAllReturnsOnlyHoldingsWithMatchingClassification() {
      catalog.add(new Holding(THE_TRIAL, BRANCH_EAST, 1));
      catalog.add(new Holding(AGILE_JAVA, BRANCH_EAST, 1));

      List<Holding> retrievedForClassification1 = catalog.findAll(KAFKA_CLASSIFICATION);
      List<Holding> retrievedForClassification2 = catalog.findAll(LANGR_CLASSIFICATION);

      HoldingTest.assertContains(retrievedForClassification1, THE_TRIAL);
      HoldingTest.assertContains(retrievedForClassification2, AGILE_JAVA);
   }

   @Test
   public void findBarCode() {
      Holding holding = new Holding(THE_TRIAL, BRANCH_EAST, 1);
      catalog.add(holding);

      Holding retrieved = catalog.find(holding.getBarCode());

      assertEquals(holding, retrieved);
   }

   @Test
   public void supportsIteration() {
      Holding holding1 = new Holding(THE_TRIAL, BRANCH_EAST, 1);
      Holding holding2 = new Holding(THE_TRIAL, BRANCH_WEST, 2);
      catalog.add(holding1);
      catalog.add(holding2);

      List<Holding> results = new ArrayList<Holding>();
      for (Holding holding: catalog)
         results.add(holding);

      assertEquals(asList(holding1, holding2), results);
   }
}