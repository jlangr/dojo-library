package persistence;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import testutil.*;
import domain.core.*;

public class PatronStoreTest {
   private PatronStore store;
   private static Patron joe = new Patron("joe", "p1");

   @Before
   public void initialize() {
      PatronStore.deleteAll();
      store = new PatronStore();
   }

   @Test
   public void persistsAddedPatron() {
      store.add(joe);

      Collection<Patron> patrons = store.getAll();

      assertEquals(joe, CollectionsUtil.soleElement(patrons));
   }

   @Test
   public void assignsIdToBranch() {
      Patron patron = new Patron("name");
      store.add(patron);
      assertTrue(patron.getId().startsWith("p"));
   }

   @Test
   public void assignedIdIsUnique() {
      Patron patronA = new Patron("a");
      store.add(patronA);

      Patron patronB = new Patron("b");
      store.add(patronB);

      assertFalse(patronA.getId().equals(patronB.getId()));
   }
   
   @Test
   public void doesNotOverwriteIdIfExists() {
      Patron patron = new Patron("a", "p12345");
      
      store.add(patron);
      
      assertNotNull(store.find("p12345"));
   }

   @Test
   public void returnsRetrievedPatronAsNewInstance() {
      store.add(joe);
      store = new PatronStore();

      Collection<Patron> patrons = store.getAll();

      assertNotSame(joe, CollectionsUtil.soleElement(patrons));
   }

   @Test
   public void storesHoldingsAddedToPatron() {
      Holding holding = new Holding(MaterialTestData.THE_TRIAL, Branch.CHECKED_OUT);
      store.add(joe);
      store.addHoldingToPatron(joe, holding);

      Collection<Patron> patrons = store.getAll();

      Patron patron = CollectionsUtil.soleElement(patrons);
      HoldingMap holdings = patron.holdings();
      assertEquals(1, holdings.size());
      Holding retrieved = holdings.get(holding.getBarCode());
      assertEquals(MaterialTestData.THE_TRIAL, retrieved.getMaterial());
      assertEquals(Branch.CHECKED_OUT, retrieved.getBranch());
   }

   @Test(expected = PatronNotFoundException.class)
   public void throwsWhenAddHoldingButPatronDoesNotExist() {
      store.addHoldingToPatron(joe, new Holding(MaterialTestData.THE_TRIAL, Branch.CHECKED_OUT));
   }

   @Test
   public void findsPersistedPatronById() {
      Patron patron = new Patron("abc");
      store.add(patron);

      Patron found = store.find(patron.getId());

      assertEquals("abc", found.getName());
   }

   @Test
   public void returnsPersistedPatronAsNewInstance() {
      Patron patron = new Patron("Joe", "p1");
      store.add(patron);

      Patron found = store.find("p1");

      assertNotSame(patron, found);
   }
}
