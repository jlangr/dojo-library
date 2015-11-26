package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.*;
import domain.core.*;

public class PatronServiceTest {
   static String RAVI = "Ravi Sankaran";

   @Rule
   public ExpectedException exception = ExpectedException.none();

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

      assertThat(patron.getName(), equalTo("xyz"));
   }

   @Test
   public void rejectsPatronIdNotStartingWithP() {
      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("invalid patron id");

      service.add("", "234");
   }

   @Test
   public void rejectsAddOfDuplicatePatron() {
      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("duplicate patron");

      service.add("", "p556");
      service.add("", "p556");
   }

   @Test
   public void answersNullWhenPatronNotFound() {
      assertNull(service.find("nonexistent id"));
   }

   @Test
   public void deleteAllRemovesAllPatrons() {
      PatronService patrons = new PatronService();
      patrons.add("");

      patrons.deleteAll();

      assertTrue(patrons.allPatrons().isEmpty());
   }
}
