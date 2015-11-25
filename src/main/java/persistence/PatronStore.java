package persistence;

import java.util.ArrayList;
import java.util.Collection;

import domain.core.*;

public class PatronStore {
   private static Collection<Patron> patrons = new ArrayList<Patron>();
   private static int idIndex = 0;

   public static void deleteAll() {
      patrons.clear();
   }
   
   public void add(Patron patron) {
      if (patron.getId() == "")
         patron.setId("p" + (++idIndex));
      patrons.add(copy(patron));
   }

   private Patron copy(Patron patron) {
      Patron newPatron = new Patron(patron.getName());
      newPatron.setId(patron.getId());
      return newPatron;
   }

   public Collection<Patron> getAll() {
      return patrons;
   }

   public void addHoldingToPatron(Patron patron, Holding holding) {
      Patron found = find(patron.getId());
      if (found == null)
         throw new PatronNotFoundException();
      found.add(holding);
   }

   public Patron find(String id) {
      for (Patron each: patrons)
         if (id.equals(each.getId()))
            return each;
      return null;
   }
}
