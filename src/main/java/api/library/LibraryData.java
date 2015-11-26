package api.library;

import domain.core.*;
import persistence.BranchStore;

public class LibraryData {
   public static void deleteAll() {
      BranchStore.deleteAll();
      new Catalog().deleteAll();
      new PatronService().deleteAll();
   }
}
