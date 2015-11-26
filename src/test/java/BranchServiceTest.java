import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class BranchServiceTest {
   private BranchService service;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   @Before
   public void initialize() {
      service = new BranchService();
      service.deleteAll();
   }

   @Test
   public void supportsSpecifyingScanCode() {
      service.add("name", "b2");

      Branch branch = service.find("b2");

      assertThat(branch.getName(), equalTo("name"));
   }

   @Test
   public void rejectsDuplicateScanCode() {
      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("duplicate branch code");

      service.add("", "b559");
      service.add("", "b559");
   }

   @Test
   public void rejectsScanCodeNotStartingWithB() {
      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("invalid branch code");

      service.add("", "c2234");
   }

   @Test
   public void answersGeneratedId() {
      String scanCode = service.add(BranchTest.BRANCH_EAST.getName());
      assertTrue(scanCode.startsWith("b"));
   }

   @Test
   public void findsBranchMatchingScanCode() {
      String scanCode = service.add(BranchTest.BRANCH_EAST.getName());

      Branch branch = service.find(scanCode);

      assertThat(branch.getName(), equalTo(BranchTest.BRANCH_EAST.getName()));
      assertThat(branch.getScanCode(), equalTo(scanCode));
   }

   @Test
   public void allBranchesReturnsListOfAllPersistedBranches() {
      String eastScanCode = service.add(BranchTest.BRANCH_EAST.getName());
      String westScanCode = service.add(BranchTest.BRANCH_WEST.getName());

      Collection<Branch> all = new BranchService().allBranches();

      Branch east = service.find(eastScanCode);
      Branch west = service.find(westScanCode);
      assertTrue(TestUtil.containsExactly(all, east, west));
   }
}