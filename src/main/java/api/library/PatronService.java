package api.library;

import java.util.*;
import domain.core.*;

public class PatronService {
   private static Collection<Patron> patrons = new ArrayList<Patron>();
   private static int idIndex = 0;

   public void deleteAll() {
      patrons.clear();
   }

   public String add(String name) {
      return save(new Patron(name));
   }

   public String add(String name, String id) {
      if (!id.startsWith("p")) throw new InvalidPatronIdException();
      return save(new Patron(name, id));
   }

   private String save(Patron newPatron) {
      if (find(newPatron.getId()) != null)
         throw new DuplicatePatronException();

      if (newPatron.getId() == "")
         newPatron.setId("p" + (++idIndex));
      patrons.add(copy(newPatron));
      return newPatron.getId();
   }

   private Patron copy(Patron patron) {
      Patron newPatron = new Patron(patron.getName());
      newPatron.setId(patron.getId());
      return newPatron;
   }

   public Patron find(String id) {
      for (Patron each: patrons)
         if (id.equals(each.getId()))
            return each;
      return null;
   }

   public Collection<Patron> allPatrons() {
      return patrons;
   }

   public void addHoldingToPatron(Patron patron, Holding holding) {
      Patron found = find(patron.getId());
      if (found == null)
         throw new PatronNotFoundException();
      found.add(holding);
   }
}
