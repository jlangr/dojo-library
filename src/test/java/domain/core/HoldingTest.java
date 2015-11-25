package domain.core;

import static domain.core.MaterialTestData.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;
import com.loc.material.api.*;
import testutil.*;
import util.*;

public class HoldingTest {
   private Holding holding;
   private static final Date TODAY = new Date();
   private static final int COPY_NUMBER_1 = 1;
   private static final Branch EAST_BRANCH = new Branch("East");

   @Before
   public void setUp() {
      holding = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, COPY_NUMBER_1);
   }

   @Test
   public void create() {
      assertMaterial(THE_TRIAL, holding);
      assertEquals(BranchTest.BRANCH_EAST, holding.getBranch());
      assertEquals(1, holding.getCopyNumber());
   }

   @Test
   public void createWithBranchAndCopyDefaults() {
      Holding holding = new Holding(THE_TRIAL);
      assertEquals(Branch.CHECKED_OUT, holding.getBranch());
      assertEquals(1, holding.getCopyNumber());
   }

   @Test
   public void createWithCopyDefaults() {
      Holding holding = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST);
      assertEquals(1, holding.getCopyNumber());
   }

   @Test
   public void generatesBarCodeFromClassificationAndCopy() {
      assertEquals(THE_TRIAL.getClassification() + Holding.BARCODE_SEPARATOR
            + COPY_NUMBER_1, holding.getBarCode());

      assertEquals("a" + Holding.BARCODE_SEPARATOR + "2",
            Holding.createBarCode("a", 2));
   }

   @Test
   public void extractsClassificationFromBarcode() {
      assertThat(Holding.getClassification("123:1"), is("123"));
      assertThat(Holding.getClassification("456:1"), is("456"));
   }

   @Test
   public void extractsCopyNumberFromBarcode() {
      assertThat(Holding.getCopyNumber("123:1"), is(1));
      assertThat(Holding.getCopyNumber("123:2"), is(2));
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsWhenExtractingClassificationFromInvalidBarcode() {
      Holding.getClassification("1234");
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsWhenExtractingCopyNumberFromInvalidBarcode() {
      Holding.getCopyNumber("1234");
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsWhenExtractingCopyNumberFromBarcodeWithNonNumericCopyNumber() {
      Holding.getCopyNumber("1234:x");
   }

   @Test(expected = IllegalArgumentException.class)
   public void throwsWhenExtractingNonPositiveCopyNumberFromBarcode() {
      Holding.getCopyNumber("1234:0");
   }

   @Test
   public void changesBranchOnTransfer() {
      holding.transfer(BranchTest.BRANCH_WEST);
      assertEquals(BranchTest.BRANCH_WEST, holding.getBranch());
   }

   @Test
   public void ck() {
      holding.checkOut(TODAY);
      assertEquals(TODAY, holding.dateCheckedOut());
      assertTrue(holding.dateDue().after(TODAY));
      assertEquals(Branch.CHECKED_OUT, holding.getBranch());
      assertFalse(holding.isAvailable());

      holding.checkOut(TODAY);
      Date tomorrow = new Date(TODAY.getTime() + 60L + 60 * 1000 * 24);
      holding.checkIn(tomorrow, EAST_BRANCH);
      assertEquals(tomorrow, holding.dateLastCheckedIn());
      assertTrue(holding.isAvailable());
      assertEquals(EAST_BRANCH, holding.getBranch());
   }

   @Test
   public void returnDateForStandardBook() {
      holding.checkOut(TODAY);
      Date dateDue = holding.dateDue();
      assertDateEquals(addDays(TODAY, MaterialType.Book.getCheckoutPeriod()), dateDue);
   }

   @Test
   public void testSomething() {
      // movie
      checkOutToday(DR_STRANGELOVE, BranchTest.BRANCH_EAST);
      Date expected = addDays(TODAY, MaterialType.DVD.getCheckoutPeriod());
      assertDateEquals(addDays(TODAY, MaterialType.DVD.getCheckoutPeriod()), holding.dateDue());

      // childrens movie
      checkOutToday(THE_TRIAL_NEW_EDITION, BranchTest.BRANCH_EAST);
      expected = addDays(TODAY, MaterialType.Book.getCheckoutPeriod());
      assertDateEquals(expected, holding.dateDue());
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedSameDay() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);

      int daysLate = holding.checkIn(TODAY, BranchTest.BRANCH_EAST);

      assertThat(daysLate, is(0));
   }

   @Test
   public void answersDaysLateOfZeroWhenReturnedOnDateDue() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);

      int daysLate = holding.checkIn(holding.dateDue(), BranchTest.BRANCH_EAST);

      assertThat(daysLate, is(0));
   }

   @Test
   public void answersDaysLateWhenReturnedAfterDueDate() {
      checkOutToday(THE_TRIAL, BranchTest.BRANCH_EAST);
      Date threeDaysLate = DateUtil.addDays(holding.dateDue(), 3);

      int daysLate = holding.checkIn(threeDaysLate, BranchTest.BRANCH_EAST);

      assertThat(daysLate, is(3));
   }

   private void checkOutToday(MaterialDetails material, Branch branch) {
      holding = new Holding(material, branch);
      holding.checkOut(TODAY);
   }

   static void assertContains(List<Holding> holdings, MaterialDetails expectedMaterial) {
      assertMaterial(expectedMaterial, holdings.get(0));
   }

   static void assertMaterial(MaterialDetails expected, Holding holding) {
      MaterialTest.assertMaterialsEqual(expected, holding.getMaterial());
   }

   public static Date addDays(Date date, int days) {
      return new Date(date.getTime() + days * 60L * 1000 * 60 * 24);
   }

   public static void assertDateEquals(Date expectedDate, Date actualDate) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(expectedDate);
      int expectedYear = calendar.get(Calendar.YEAR);
      int expectedDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
      calendar.setTime(actualDate);
      assertEquals(expectedYear, calendar.get(Calendar.YEAR));
      assertEquals(expectedDayOfYear, calendar.get(Calendar.DAY_OF_YEAR));
   }

   @Test
   public void equality() {
      Holding holding1 = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, 1);
      Holding holding1Copy1 = new Holding(THE_TRIAL, BranchTest.BRANCH_WEST, 1); // diff loc but same copy
      Holding holding1Copy2 = new Holding(THE_TRIAL, Branch.CHECKED_OUT, 1);
      Holding holding2 = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST, 2); // 2nd copy
      Holding holding1Subtype = new Holding(THE_TRIAL, BranchTest.BRANCH_EAST,
            1) {
      };

      new EqualityTester(holding1, holding1Copy1, holding1Copy2, holding2,
            holding1Subtype).verify();
   }
}