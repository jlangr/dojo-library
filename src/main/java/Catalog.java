import java.util.*;

public class Catalog implements Iterable<Holding> {
   private static Map<String, List<Holding>> holdings = new HashMap<>();

   public void deleteAll() {
      holdings.clear();
   }

   public void add(Holding holding) {
      List<Holding> existing = findAll(holding.getMaterial().getClassification());
      if (!existing.isEmpty()) holding.setCopyNumber(existing.size() + 1);

      put(holding.getMaterial().getClassification(), copy(holding));
   }

   private void put(String key, Holding value) {
      if (key == null) throw new NullPointerException();
      List<Holding> list = holdings.get(key);
      if (list == null) {
         list = new ArrayList<>();
         holdings.put(key, list);
      }
      list.add(value);
   }

   private Holding copy(Holding holding) {
      return new Holding(holding.getMaterial(), holding.getBranch(), holding.getCopyNumber());
   }

   public List<Holding> findAll(String classification) {
      List<Holding> results = holdings.get(classification);
      if (results == null) return new ArrayList<Holding>();
      return results;
   }

   @Override
   public Iterator<Holding> iterator() {
      List<Holding> results = new ArrayList<>();
      for (Iterator<List<Holding>> it = holdings.values().iterator(); it.hasNext();) {
         List<Holding> list = it.next();
         results.addAll(list);
      }
      return results.iterator();
   }

   public Holding find(String barCode) {
      List<Holding> results = holdings.get(classificationFrom(barCode));
      if (results == null) return null;
      for (Holding holding: results)
         if (holding.getBarCode().equals(barCode)) return holding;
      return null;
   }

   private String classificationFrom(String barCode) {
      int index = barCode.indexOf(Holding.BARCODE_SEPARATOR);
      return barCode.substring(0, index);
   }
}