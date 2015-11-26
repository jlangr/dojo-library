package api.library;

import static domain.core.BranchTest.*;
import static domain.core.MaterialTestData.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import com.loc.material.api.*;
import domain.core.*;
import util.DateUtil;

public class HoldingService_WithExistingHoldingsTest {
   private HoldingService service;
   private Holding theTrialCopy1AtEast;
   private Holding theTrialCopy2AtWest;
   private Holding agileJavaAtWest;
   private String eastScanCode;
   private String westScanCode;
   private String joeId;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   // TODO delete by using mockito
   class FakeClassificationApi implements ClassificationApi {
      private Map<String, MaterialDetails> materials = new HashMap<String, MaterialDetails>();

      @Override
      public boolean isValid(String classification) {
         return materials.containsKey(classification);
      }

      public void add(MaterialDetails material) {
         materials.put(material.getClassification(), material);
      }

      @Override
      public MaterialDetails getMaterialDetails(String classification) {
         return materials.get(classification);
      }

      @Override
      public Collection<MaterialDetails> allMaterials() {
         return materials.values();
      }
   }

   private FakeClassificationApi classificationApi;

   @Before
   public void initialize() {
      new Catalog().deleteAll();
      classificationApi = new FakeClassificationApi();
      service = new HoldingService(classificationApi);

      addTwoBranches();
      addThreeNewHoldings();
      addPatron();
   }

   private void addPatron() {
      PatronService service = new PatronService();
      service.deleteAll();
      joeId = service.add("joe");
   }

   private void addTwoBranches() {
      BranchService service = new BranchService();
      service.deleteAll();
      eastScanCode = service.add(BRANCH_EAST.getName());
      westScanCode = service.add(BRANCH_WEST.getName());
   }

   private void addThreeNewHoldings() {
      theTrialCopy1AtEast = addHolding(eastScanCode, THE_TRIAL, 1);
      theTrialCopy2AtWest = addHolding(westScanCode, THE_TRIAL, 2);
      agileJavaAtWest = addHolding(westScanCode, AGILE_JAVA, 1);
   }

   private Holding addHolding(String branchScanCode, MaterialDetails material, int copyNumber) {
      String holdingId = Holding.createBarCode(material.getClassification(), copyNumber);
      classificationApi.add(material);

      service.add(holdingId, branchScanCode);
      return service.find(holdingId);
   }

   @Test
   public void returnsEntireInventoryOfHoldings() {
      List<Holding> holdings = service.holdings();

      assertEquals(3, holdings.size());
      assertTrue(holdings.contains(theTrialCopy1AtEast));
      assertTrue(holdings.contains(theTrialCopy2AtWest));
      assertTrue(holdings.contains(agileJavaAtWest));
   }

   @Test
   public void storesNewHoldingAtBranch() {
      String holdingId = Holding.createBarCode(THE_TRIAL.getClassification(), 3);
      classificationApi.add(THE_TRIAL);

      service.add(holdingId, eastScanCode);

      Holding added = service.find(holdingId);
      assertEquals(eastScanCode, added.getBranch().getScanCode());
   }

   @Test
   public void findByBarCodeReturnsNullWhenNotFound() {
      assertThat(service.find("999:1"), equalTo((Holding)null));
   }

   @Test
   public void updatesBranchOnHoldingTransfer() {
      service.transfer(agileJavaAtWest, BRANCH_EAST);

      assertEquals(BRANCH_EAST, agileJavaAtWest.getBranch());
   }

   @Test
   public void holdingIsAvailableWhenNotCheckedOut() {
      assertThat(service.isAvailable(agileJavaAtWest.getBarCode()), equalTo(true));
   }

   @Test
   public void holdingMadeUnavailableOnCheckout() {
      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());

      assertThat(service.isAvailable(agileJavaAtWest.getBarCode()), equalTo(false));
   }

   @Test
   public void availabilityCheckThrowsWhenHoldingNotFound() {
      exception.expect(RuntimeException.class);
      exception.expectMessage("holding not found");

      service.isAvailable("345:1");
   }

   @Test
   public void checkoutThrowsWhenHoldingIdNotFound() {
      exception.expect(RuntimeException.class);
      exception.expectMessage("holding not found");

      service.checkOut(joeId, "999:1", new Date());
   }

   @Test
   public void checkinThrowsWhenHoldingIdNotFound() {
      exception.expect(RuntimeException.class);
      exception.expectMessage("holding not found");

      service.checkIn("999:1", new Date(), eastScanCode);
   }

   @Test
   public void checkoutThrowsWhenUnavailable() {
      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());
      exception.expect(RuntimeException.class);
      exception.expectMessage("holding already checked out");

      service.checkOut(joeId, agileJavaAtWest.getBarCode(), new Date());
   }

   @Test
   public void updatesPatronWithHoldingOnCheckout() {
      String barCode = Holding.createBarCode(LANGR_CLASSIFICATION, 1);

      service.checkOut(joeId, barCode, new Date());

      List<Holding> patronHoldings = retrieve(joeId).getHoldings();
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

      assertTrue(retrieve(joeId).getHoldings().isEmpty());
   }

   @Test
   public void answersDueDate() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());

      Date due = service.dateDue(barCode);

      Holding holding = service.find(barCode);
      assertThat(due, equalTo(holding.dateDue()));
   }

   @Test
   public void checkinReturnsDaysLate() {
      String barCode = agileJavaAtWest.getBarCode();
      service.checkOut(joeId, barCode, new Date());
      Date fiveDaysLate = DateUtil.addDays(service.dateDue(barCode), 5);

      int daysLate = service.checkIn(barCode, fiveDaysLate, EAST_SCAN);

      assertThat(daysLate, equalTo(5));
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
      return new PatronService().find(id);
   }
}
