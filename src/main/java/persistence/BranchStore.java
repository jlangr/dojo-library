package persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import domain.core.Branch;

public class BranchStore {
   private static Map<String, Branch> branches = new HashMap<String, Branch>();
   private static int idIndex = 0;

   public void save(Branch branch) {
      if (branch.getScanCode().equals(""))
         branch.setScanCode("b" + (++idIndex));
      branches.put(branch.getName(), copy(branch));
   }

   public Branch find(String name) {
      return branches.get(name);
   }

   public Branch findByScanCode(String scanCode) {
      if (scanCode.equals(Branch.CHECKED_OUT.getScanCode()))
         return Branch.CHECKED_OUT;

      for (Branch branch: branches.values()) {
         if (branch.getScanCode().equals(scanCode))
            return branch;
      }
      return null;
   }

   private Branch copy(Branch branch) {
      Branch newBranch = new Branch(branch.getName());
      newBranch.setScanCode(branch.getScanCode());
      return newBranch;
   }

   public Collection<Branch> getAll() {
      return branches.values();
   }

   public static void deleteAll() {
      branches.clear();
   }
}
