import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

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
        eastScanCode = service.add(BranchTest.BRANCH_EAST.getName());
        westScanCode = service.add(BranchTest.BRANCH_WEST.getName());
    }

    private void addThreeNewHoldings() {
        theTrialCopy1AtEast = addHolding(eastScanCode, TestData.THE_TRIAL, 1);
        theTrialCopy2AtWest = addHolding(westScanCode, TestData.THE_TRIAL, 2);
        agileJavaAtWest = addHolding(westScanCode, TestData.AGILE_JAVA, 1);
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

        assertThat(holdings.size(), equalTo(3));
        assertTrue(holdings.contains(theTrialCopy1AtEast));
        assertTrue(holdings.contains(theTrialCopy2AtWest));
        assertTrue(holdings.contains(agileJavaAtWest));
    }

    @Test
    public void storesNewHoldingAtBranch() {
        String holdingId = Holding.createBarCode(TestData.THE_TRIAL.getClassification(), 3);
        classificationApi.add(TestData.THE_TRIAL);

        service.add(holdingId, eastScanCode);

        Holding added = service.find(holdingId);
        assertThat(added.getBranch().getScanCode(), equalTo(eastScanCode));
    }

    @Test
    public void findByBarCodeReturnsNullWhenNotFound() {
        assertThat(service.find("999:1"), equalTo((Holding)null));
    }

    @Test
    public void updatesBranchOnHoldingTransfer() {
        service.transfer(agileJavaAtWest, BranchTest.BRANCH_EAST);

        assertThat(agileJavaAtWest.getBranch(), equalTo(BranchTest.BRANCH_EAST));
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
        String barCode = Holding.createBarCode(TestData.LANGR_CLASSIFICATION, 1);

        service.checkOut(joeId, barCode, new Date());

        List<Holding> patronHoldings = retrieve(joeId).getHoldings();
        assertThat(patronHoldings.size(), equalTo(1));
        assertTrue(patronHoldings.contains(service.find(barCode)));
    }

    @Test
    public void returnsHoldingToBranchOnCheckIn() {
        String barCode = agileJavaAtWest.getBarCode();
        service.checkOut(joeId, barCode, new Date());
        service.checkIn(barCode, DateUtil.tomorrow(), eastScanCode);

        assertTrue(agileJavaAtWest.isAvailable());
        assertThat(agileJavaAtWest.getBranch().getScanCode(), equalTo(eastScanCode));
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

        int daysLate = service.checkIn(barCode, fiveDaysLate, BranchTest.EAST_SCAN);

        assertThat(daysLate, equalTo(5));
    }

    @Test
    public void updatesFinesOnLateCheckIn() {
        String barCode = agileJavaAtWest.getBarCode();
        service.checkOut(joeId, barCode, new Date());

        Holding holding = service.find(barCode);
        Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);
        service.checkIn(barCode, oneDayLate, eastScanCode);

        assertThat(retrieve(joeId).fineBalance(), equalTo(MaterialType.Book.getDailyFine()));
    }

    private Patron retrieve(String id) {
        return new PatronService().find(id);
    }
}
