package domain.core;

public class Patron {
   private final String name;
   private String id;
   private HoldingMap s = new HoldingMap();
   private int bal = 0;

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
      if (object == null)
         return false;
      if ((object.getClass() != this.getClass()))
         return false;
      Patron that = (Patron)object;
      return this.getId().equals(that.getId());
   }

   public HoldingMap holdings() {
      return s;
   }

   public void add(Holding holding) {
      s.add(holding);
   }

   public void remove(Holding holding) {
      s.remove(holding);
   }

   public int fineBalance() {
      return bal;
   }

   /**
    * add a fine to the patron's balance
    * 
    * @param a
    *           the amount to add to the balance
    */
   public void addFine(int a) {
      bal += a;
   }

   /**
    * add a fine to the patron's balance
    * 
    * @param a
    *           the amount to add to the balance
    */
   public void remit(int a) {
      bal -= a;
   }
}
