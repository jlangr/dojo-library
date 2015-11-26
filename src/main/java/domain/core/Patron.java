package domain.core;

public class Patron {
   private final String name;
   private String id;
   private HoldingMap holdings = new HoldingMap();
   private int balalance = 0;

   public Patron(String name, String id) {
      this.name = name;
      this.id = id;
   }

   public Patron(String name) {
      this(name, "");
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   @Override
   public String toString() {
      return "[" + getId() + "] " + getName();
   }

   @Override
   public boolean equals(Object object) {
      if (object == null) return false;
      if ((object.getClass() != this.getClass())) return false;
      Patron that = (Patron)object;
      return this.getId().equals(that.getId());
   }

   public HoldingMap holdings() {
      return holdings;
   }

   public void add(Holding holding) {
      holdings.add(holding);
   }

   public void remove(Holding holding) {
      holdings.remove(holding);
   }

   public int fineBalance() {
      return balalance;
   }

   public void addFine(int amount) {
      balalance += amount;
   }

   public void remit(int amount) {
      balalance -= amount;
   }
}
