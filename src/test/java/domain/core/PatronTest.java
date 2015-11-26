package domain.core;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import org.junit.*;
import testutil.EqualityTester;

public class PatronTest {
   private Patron jane;
   private static final Holding A_HOLDING = new Holding(MaterialTestData.THE_TRIAL, BranchTest.BRANCH_EAST);

   @Before
   public void initialize() {
      jane = new Patron("Jane");
   }

   @Test
   public void defaultsIdToEmpty() {
      assertThat(jane.getId(), equalTo(""));
   }

   @Test
   public void fineBalanceIsZeroOnCreation() {
      assertEquals(0, jane.fineBalance());
   }

   @Test
   public void holdingsAreEmptyOnCreation() {
      assertTrue(jane.holdings().isEmpty());
   }

   @Test
   public void returnsHoldingsAdded() {
      jane.add(A_HOLDING);

      assertEquals(1, jane.holdings().size());
      assertTrue(jane.holdings().contains(A_HOLDING));
   }

   @Test
   public void removesHoldingFromPatron() {
      jane.add(A_HOLDING);

      jane.remove(A_HOLDING);

      assertTrue(jane.holdings().isEmpty());
   }

   @Test
   public void storesFines() {
      jane.addFine(10);
      assertEquals(10, jane.fineBalance());
   }

   @Test
   public void increasesBalanceOnAdditionalFines() {
      jane.addFine(10);
      jane.addFine(30);
      assertEquals(40, jane.fineBalance());
   }

   @Test
   public void decreasesBalanceWhenPatronRemitsAmount() {
      jane.addFine(40);
      jane.remit(25);
      assertEquals(15, jane.fineBalance());
      jane.remit(15);
      assertEquals(0, jane.fineBalance());
   }

   @Test
   public void supportsEqualityComparison() {
      Patron patron1 = new Patron("Joe", "p1");
      Patron patron1Copy1 = new Patron("", "p1");
      Patron patron1Copy2 = new Patron("", "p1");
      Patron patron1Subtype = new Patron("", "p1") {
      };
      Patron patron2 = new Patron("", "p2");

      new EqualityTester(patron1, patron1Copy1, patron1Copy2, patron2, patron1Subtype).verify();
   }
}