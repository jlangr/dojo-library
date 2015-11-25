package domain.core;

public class Branch {
   public static final Branch CHECKED_OUT = new Branch("unavailable", "b999999");
   private String name;
   private String scanCode;

   public Branch(String name) {
      this(name, "");
   }
   
   public Branch(String name, String scanCode) {
      this.name = name;
      this.scanCode = scanCode;
   }

   public String getName() {
      return name;
   }

   public String getScanCode() {
      return scanCode;
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if ((object.getClass() != this.getClass()))
         return false;
      Branch that = (Branch)object;
      return this.getScanCode().equals(that.getScanCode());
   }

   @Override
   public String toString() {
      return "[Branch] " + name;
   }

   public void setScanCode(String scanCode) {
      this.scanCode = scanCode;
   }

   public static Branch createTest(String name, String scanCode) {
      return new Branch(name, scanCode);
   }
}