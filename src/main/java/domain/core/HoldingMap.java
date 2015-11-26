package domain.core;

import java.util.*;

// TODO can we delete this class

public class HoldingMap implements Iterable<Holding> {
   private Map<String, Holding> holdings = new HashMap<>();

   public boolean isEmpty() {
      return 0 == size();
   }

   public int size() {
      return holdings.size();
   }

   public void add(Holding holding) {
      holdings.put(holding.getBarCode(), holding);
   }

   public Holding get(String barCode) {
      return holdings.get(barCode);
   }

   public boolean contains(Holding holding) {
      return holdings.containsKey(holding.getBarCode());
   }

   public Collection<Holding> holdings() {
      return holdings.values();
   }

   public void remove(Holding holding) {
      holdings.remove(holding.getBarCode());
   }

   @Override
   public Iterator<Holding> iterator() {
      return holdings.values().iterator();
   }
}
