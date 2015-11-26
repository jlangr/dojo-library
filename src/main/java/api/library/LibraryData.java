package api.library;

import persistence.BranchStore;
import persistence.HoldingStore;

public class LibraryData {
   public static void deleteAll() {
      BranchStore.deleteAll();
      HoldingStore.deleteAll();
      new PatronService().deleteAll();
   }
}
