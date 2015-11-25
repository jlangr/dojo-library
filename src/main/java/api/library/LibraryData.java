package api.library;

import persistence.BranchStore;
import persistence.HoldingStore;
import persistence.PatronStore;

public class LibraryData {
   public static void deleteAll() {
      BranchStore.deleteAll();
      HoldingStore.deleteAll();
      PatronStore.deleteAll();
   }
}
