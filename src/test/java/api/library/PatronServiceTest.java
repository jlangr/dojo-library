package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import persistence.*;
import domain.core.*;

public class PatronServiceTest {
   static String RAVI = "Ravi Sankaran";

   PatronService service;

   @Before
   public void initialize() {
      PatronStore.deleteAll();
      service = new PatronService();
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
}
