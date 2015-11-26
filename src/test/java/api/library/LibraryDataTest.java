package api.library;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;

public class LibraryDataTest {
   // TODO move to PatronServiceTest
   @Ignore @Test
   public void deleteAllRemovesAllPatrons() {
      new PatronService().add("");

      new BranchService().add("");

      MockHoldingService holdingService = new MockHoldingService();
      holdingService.addTestBookToMaterialService(new MaterialDetails("", "", "123", MaterialType.Book, ""));
      holdingService.add("123:1", Branch.CHECKED_OUT.getScanCode());

      LibraryData.deleteAll();

      assertTrue(new PatronService().allPatrons().isEmpty());
      assertTrue(holdingService.allHoldings().isEmpty());
      assertTrue(new BranchService().allBranches().isEmpty());
   }
}
