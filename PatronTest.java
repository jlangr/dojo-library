import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import org.junit.*;

public class PatronTest {
   private Patron jane;
   private static final Holding A_HOLDING = new Holding(TestData.THE_TRIAL, BranchTest.BRANCH_EAST);

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
      assertThat(jane.fineBalance(), equalTo(0));
   }

   @Test
   public void holdingsAreEmptyOnCreation() {
      assertTrue(jane.getHoldings().isEmpty());
   }

   @Test
   public void returnsHoldingsAdded() {
      jane.add(A_HOLDING);

      assertThat(jane.getHoldings().size(), equalTo(1));
      assertTrue(jane.getHoldings().contains(A_HOLDING));
   }

   @Test
   public void removesHoldingFromPatron() {
      jane.add(A_HOLDING);

      jane.remove(A_HOLDING);

      assertTrue(jane.getHoldings().isEmpty());
   }

   @Test
   public void storesFines() {
      jane.addFine(10);
      assertThat(jane.fineBalance(), equalTo(10));
   }

   @Test
   public void increasesBalanceOnAdditionalFines() {
      jane.addFine(10);
      jane.addFine(30);
      assertThat(jane.fineBalance(), equalTo(40));
   }

   @Test
   public void decreasesBalanceWhenPatronRemitsAmount() {
      jane.addFine(40);
      jane.remit(25);
      assertThat(jane.fineBalance(), equalTo(15));
      jane.remit(15);
      assertThat(jane.fineBalance(), equalTo(0));
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