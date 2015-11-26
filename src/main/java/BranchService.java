import java.util.*;

public class BranchService {
   private static Map<String, Branch> branches = new HashMap<>();
   private static int idIndex = 0;

   public void deleteAll() {
      branches.clear();
   }

   public Collection<Branch> allBranches() {
      return branches.values();
   }

   public Branch find(String scanCode) {
      if (scanCode.equals(Branch.CHECKED_OUT.getScanCode())) return Branch.CHECKED_OUT;
      for (Branch branch: branches.values())
         if (branch.getScanCode().equals(scanCode)) return branch;
      return null;
   }

   public String add(String name) {
      return save(new Branch(name));
   }

   public String add(String name, String scanCode) {
      if (!scanCode.startsWith("b"))
         throw new IllegalArgumentException("invalid branch code");
      return save(new Branch(name, scanCode));
   }

   private String save(Branch branch) {
      if (findByScanCode(branch.getScanCode()) != null)
         throw new IllegalArgumentException("duplicate branch code");
      if (branch.getScanCode().equals(""))
         branch.setScanCode("b" + (++idIndex));
      branches.put(branch.getName(), copy(branch));
      return branch.getScanCode();
   }

   private Branch findByScanCode(String scanCode) {
      if (scanCode.equals(Branch.CHECKED_OUT.getScanCode()))
         return Branch.CHECKED_OUT;

      for (Branch branch: branches.values())
         if (branch.getScanCode().equals(scanCode)) return branch;
      return null;
   }

   private Branch copy(Branch branch) {
      Branch newBranch = new Branch(branch.getName());
      newBranch.setScanCode(branch.getScanCode());
      return newBranch;
   }
}