package domain.core;

import java.util.Iterator;
import java.util.List;

import persistence.HoldingStore;

public class Catalog implements Iterable<Holding> {
   private HoldingStore access = new HoldingStore();

   public int size() {
      return access.getAll().size();
   }

   public List<Holding> findAll(String classification) {
      return access.findAll(classification);
   }

   public void add(Holding holding) {
      List<Holding> existing = access.findAll(holding.getMaterial()
            .getClassification());
      if (!existing.isEmpty())
         holding.setCopyNumber(existing.size() + 1);
      access.save(holding);
   }

   @Override
   public Iterator<Holding> iterator() {
      return access.getAll().iterator();
   }

   public Holding find(String barCode) {
      return access.find(barCode);
   }
}