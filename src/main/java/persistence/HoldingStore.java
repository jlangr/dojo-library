package persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import util.MultiMap;
import domain.core.Holding;

public class HoldingStore {
   private static MultiMap<String, Holding> holdings = new MultiMap<String, Holding>();

   public static void deleteAll() {
      holdings.clear();
   }

   public void save(Holding holding) {
      holdings.put(holding.getMaterial().getClassification(), copy(holding));
   }

   private Holding copy(Holding holding) {
      return new Holding(holding.getMaterial(), holding.getBranch(),
            holding.getCopyNumber());
   }

   public List<Holding> findAll(String classification) {
      List<Holding> results = holdings.get(classification);
      if (results == null)
         return new ArrayList<Holding>();
      return results;
   }

   public Collection<Holding> getAll() {
      return holdings.values();
   }

   public Holding find(String barCode) {
      List<Holding> results = holdings.get(classificationFrom(barCode));
      if (results == null)
         return null;
      for (Holding holding: results)
         if (holding.getBarCode().equals(barCode))
            return holding;
      return null;
   }

   private String classificationFrom(String barCode) {
      int index = barCode.indexOf(Holding.BARCODE_SEPARATOR);
      return barCode.substring(0, index);
   }
}
