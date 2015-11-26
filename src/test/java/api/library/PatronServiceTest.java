package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;

public class PatronServiceTest {
   static String RAVI = "Ravi Sankaran";

   PatronService service;

   @Before
   public void initialize() {
      service = new PatronService();
      service.deleteAll();
   }

   @Test
   public void answersGeneratedId() {
      String scanCode = service.add("name");
      assertTrue(scanCode.startsWith("p"));
   }

   @Test
   public void allowsAddingPatronWithId() {
      service.add("xyz", "p123");

      Patron patron = service.find("p123");

      assertThat(patron.getName(), is("xyz"));
   }

   @Test(expected=InvalidPatronIdException.class)
   public void rejectsPatronIdNotStartingWithP() {
      service.add("", "234");
   }

   @Test(expected=DuplicatePatronException.class)
   public void rejectsAddOfDuplicatePatron() {
      service.add("", "p556");
      service.add("", "p556");
   }

   @Test
   public void answersNullWhenPatronNotFound() {
      assertNull(service.find("nonexistent id"));
   }

   @Test
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
