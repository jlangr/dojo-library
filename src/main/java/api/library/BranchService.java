package api.library;

import java.util.Collection;

import persistence.BranchStore;
import domain.core.Branch;

public class BranchService {
   private BranchStore store = new BranchStore();

   public Collection<Branch> allBranches() {
      return store.getAll();
   }

   public Branch find(String scanCode) {
      return store.findByScanCode(scanCode);
   }

   public String add(String name) {
      return save(new Branch(name));
   }

   public String add(String name, String scanCode) {
      if (!scanCode.startsWith("b")) throw new InvalidBranchCodeException();
      return save(new Branch(name, scanCode));
   }

   private String save(Branch branch) {
      if (store.findByScanCode(branch.getScanCode()) != null)
         throw new DuplicateBranchCodeException(); 
      store.save(branch);
      return branch.getScanCode();
   }

}
