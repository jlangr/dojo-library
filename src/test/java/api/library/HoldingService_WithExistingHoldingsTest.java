package api.library;

import static domain.core.MaterialTestData.*;
import static domain.core.BranchTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import com.loc.material.api.*;
import persistence.*;
import util.*;
import domain.core.*;

public class HoldingService_WithExistingHoldingsTest {
   private MockHoldingService service;
   private Holding theTrialCopy1AtEast;
   private Holding theTrialCopy2AtWest;
   private Holding agileJavaAtWest;
   private String eastScanCode;
   private String westScanCode;
   private String joeId;

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      service = new MockHoldingService();

      addTwoBranches();
      addThreeNewHoldings();
      addPatron();
   }

   private void addPatron() {
      joeId = new PatronService().add("joe");
   }

   private void addTwoBranches() {
      eastScanCode = add(BRANCH_EAST.getName());
      westScanCode = add(BRANCH_WEST.getName());
   }

   private String add(String name) {
      return new BranchService().add(name);
   }

   private void addThreeNewHoldings() {
      theTrialCopy1AtEast = addHolding(eastScanCode, THE_TRIAL, 1);
      theTrialCopy2AtWest = addHolding(westScanCode, THE_TRIAL, 2);
      agileJavaAtWest = addHolding(westScanCode, AGILE_JAVA, 1);
   }

   private Holding addHolding(String branchScanCode, MaterialDetails material, int copyNumber) {
      String holdingId = Holding.createBarCode(material.getClassification(), copyNumber);
      service.addTestBookToMaterialService(material);

      service.add(holdingId, branchScanCode);
      return service.find(holdingId);
   }

   @Test
   public void returnsEntireInventoryOfHoldings() {
      HoldingMap holdings = service.allHoldings();

      assertEquals(3, holdings.size());
      assertTrue(holdings.contains(theTrialCopy1AtEast));
      assertTrue(holdings.contains(theTrialCopy2AtWest));
      assertTrue(holdings.contains(agileJavaAtWest));
   }

   @Test
   public void storesNewHoldingAtBranch() {
      String holdingId = Holding.createBarCode(THE_TRIAL.getClassification(), 3);
      service.addTestBookToMaterialService(THE_TRIAL);

      service.add(holdingId, eastScanCode);

      Holding added = service.find(holdingId);
      assertEquals(eastScanCode, added.getBranch().getScanCode());
   }

   @Test
   public void findByBarCodeReturnsNullWhenNotFound() {
      assertThat(service.find("999:1"), is((Holding)null));
   }

   @Test
   public void updatesBranchOnHoldingTransfer() {
      service.transfer(agileJavaAtWest, BRANCH_EAST);

      assertEquals(BRANCH_EAST, agileJavaAtWest.getBranch());
   }

   @Test
   public void holdingIsAvailableWhenNotCheckedOut() {
      assertThat(service.isAvailable(agileJavaAtWest.getBarCode()), is(true));
   }

   @Test
   public void holdingMadeUnavailableOnCheckout() {
      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());

      assertThat(service.isAvailable(agileJavaAtWest.getBarCode()), is(false));
   }

   @Test(expected = HoldingNotFoundException.class)
   public void availabilityCheckThrowsWhenHoldingNotFound() {
      service.isAvailable("345:1");
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkoutThrowsWhenHoldingIdNotFound() {
      service.checkOut(joeId, "999:1", new Date());
   }

   @Test(expected = HoldingNotFoundException.class)
   public void checkinThrowsWhenHoldingIdNotFound() {
      service.checkIn("999:1", new Date(), eastScanCode);
   }

   @Test(expected = HoldingAlreadyCheckedOutException.class)
   public void checkoutThrowsWhenUnavailable() {
      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());
      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());
   }

   @Test
   public void updatesPatronWithHoldingOnCheckout() {
      String barCode = Holding.createBarCode(LANGR_CLASSIFICATION, 1);

      service.checkOut(joeId, barCode, new Date());

      HoldingMap patronHoldings = retrieve(joeId).holdings();
      assertEquals(1, patronHoldings.size());
      assertTrue(patronHoldings.contains(service.find(barCode)));
   }

   @Test
   public void returnsHoldingToBranchOnCheckIn() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());
      service.checkIn(barCode, DateUtil.tomorrow(), eastScanCode);

      assertTrue(agileJavaAtWest.isAvailable());
      assertEquals(eastScanCode, agileJavaAtWest.getBranch().getScanCode());
   }

   @Test
   public void removesHoldingFromPatronOnCheckIn() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());

      service.checkIn(barCode, DateUtil.tomorrow(), westScanCode);

      assertTrue(retrieve(joeId).holdings().isEmpty());
   }

   @Test
   public void answersDueDate() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());

      Date due = service.dateDue(barCode);

      Holding holding = service.find(barCode);
      assertThat(due, is(holding.dateDue()));
   }

   @Test
   public void checkinReturnsDaysLate() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());
      Date fiveDaysLate = DateUtil.addDays(service.dateDue(barCode), 5);

      int daysLate = service.checkIn(barCode, fiveDaysLate, EAST_SCAN);

      assertThat(daysLate, is(5));
   }

   @Test
   public void updatesFinesOnLateCheckIn() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());

      Holding holding = service.find(barCode);
      Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);
      service.checkIn(barCode, oneDayLate, eastScanCode);

      assertEquals(MaterialType.Book.getDailyFine(), retrieve(joeId).fineBalance());
   }

   private Patron retrieve(String id) {
      return new PatronStore().find(id);
   }
}
