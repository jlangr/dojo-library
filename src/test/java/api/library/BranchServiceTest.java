package api.library;

import static domain.core.BranchTest.BRANCH_EAST;
import static domain.core.BranchTest.BRANCH_WEST;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import testutil.CollectionsUtil;
import domain.core.Branch;
import domain.core.BranchTest;

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

      assertThat(branch.getName(), is("name"));
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
   public void capturesBranchFields() {
      service.add(BranchTest.BRANCH_EAST.getName());
      Branch branch = CollectionsUtil.soleElement(new BranchService().allBranches());

      assertThat(branch.getName(), is(BRANCH_EAST.getName()));
   }

   @Test
   public void findsBranchMatchingScanCode() {
      String scanCode = service.add(BranchTest.BRANCH_EAST.getName());
      Branch branch = service.find(scanCode);

      assertThat(branch.getName(), is(BRANCH_EAST.getName()));
      assertThat(branch.getScanCode(), is(scanCode));
   }

   @Test
   public void returnsListOfAllPersistedBranches() {
      String eastScanCode = service.add(BRANCH_EAST.getName());
      String westScanCode = service.add(BRANCH_WEST.getName());

      Collection<Branch> all = new BranchService().allBranches();

      Branch east = service.find(eastScanCode);
      Branch west = service.find(westScanCode);
      assertTrue(CollectionsUtil.containsExactly(all, east, west));
   }
}
