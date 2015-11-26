import java.util.*;

public class HoldingService {
   private Catalog catalog = new Catalog();
   private ClassificationApi classificationApi;

   public HoldingService(ClassificationApi classificationApi) {
      this.classificationApi = classificationApi;
   }

   public void add(String holdingBarcode, String branchId) {
      throwIfHoldingAlreadyExists(holdingBarcode);
      catalog.add(create(holdingBarcode, branchId));
   }

   private int extractCopyNumber(String code) {
      String copy = splitOnColon(code)[1];
      return parsePositiveInt(copy);
   }

   private String extractClassification(String barcode) {
      return splitOnColon(barcode)[0];
   }

   private int parsePositiveInt(String text) {
      int number = parseInt(text);
      if (number < 1)
         throw new IllegalArgumentException();
      return number;
   }

   private int parseInt(String text) {
      try {
         return Integer.parseInt(text, 10);
      } catch (Throwable t) {
         throw new IllegalArgumentException();
      }
   }

   private String[] splitOnColon(String barcode) {
      String[] barcodeParts = barcode.split(":");
      if (barcodeParts.length != 2)
         throw new IllegalArgumentException();
      return barcodeParts;
   }

   private Holding create(String holdingBarcode, String branchId) {
      String classification = extractClassification(holdingBarcode);
      MaterialDetails material = classificationApi.getMaterialDetails(classification);
      if (material == null)
         throw new RuntimeException("invalid classification");
      return new Holding(material, findBranch(branchId), extractCopyNumber(holdingBarcode));
   }

   private void throwIfHoldingAlreadyExists(String holdingBarcode) {
      if (find(holdingBarcode) != null)
         throw new RuntimeException("duplicate holding");
   }

   private Branch findBranch(String branchId) {
      Branch branch = new BranchService().find(branchId);
      if (branch == null)
         throw new RuntimeException("Branch not found: " + branchId);
      return branch;
   }

   public boolean isAvailable(String barCode) {
      Holding holding = find(barCode);
      if (holding == null)
         throw new RuntimeException("holding not found");
      return holding.isAvailable();
   }

   public List<Holding> holdings() {
      List<Holding> holdings = new ArrayList<>();
      for (Holding holding: catalog)
         holdings.add(holding);
      return holdings;
   }

   public Holding find(String barCode) {
      return catalog.find(barCode);
   }

   public void transfer(Holding holding, Branch branch) {
      holding.transfer(branch);
   }

   public Date dateDue(String barCode) {
      Holding holding = find(barCode);
      if (holding == null) {
         throw new RuntimeException("holding not found");
      }
      return holding.dateDue();
   }

   public void checkOut(String patronId, String barCode, Date date) {
      Holding holding = find(barCode);
      if (holding == null) {
         throw new RuntimeException("holding not found");
      }
      if (!holding.isAvailable())
         throw new RuntimeException("holding already checked out");
      holding.checkOut(date);

      PatronService patronService = new PatronService();
      Patron patron = patronService.find(patronId);
      patronService.addHoldingToPatron(patron, holding);
   }

   @SuppressWarnings("incomplete-switch")
   public int checkIn(String barCode, Date date, String branchScanCode) {
      Branch branch = new BranchService().find(branchScanCode);
      Holding hld = find(barCode);
      if (hld == null)
         throw new RuntimeException("holding not found");

      // set the holding to returned status
      List<Holding> holdings = null;
      hld.checkIn(date, branch);

      // locate the patron with the checked out book
      // could introduce a patron reference ID in the holding...
      Patron f = null;
      for (Patron p: new PatronService().allPatrons()) {
         holdings = p.getHoldings();
         for (Holding patHld: holdings) {
            if (hld.getBarCode().equals(patHld.getBarCode()))
               f = p;
         }
      }

      // remove the book from the patron
      f.remove(hld);

      // check for late returns
      boolean isLate = false;
      Calendar c = Calendar.getInstance();
      c.setTime(hld.dateDue());
      int d = Calendar.DAY_OF_YEAR;

      // check for last day in year
      if (c.get(d) > c.getActualMaximum(d)) {
         c.set(d, 1);
         c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
      }

      if (hld.dateLastCheckedIn().after(c.getTime())) // is it late?
         isLate = true;

      if (isLate) {
         int daysLate = hld.daysLate(); // calculate # of days past due
         int fineBasis = hld.getMaterial().getFormat().getDailyFine();
         switch (hld.getMaterial().getFormat()) {
            case Book:
               f.addFine(fineBasis * daysLate);
               break;
            case DVD:
               int fine = Math.min(1000, 100 + fineBasis * daysLate);
               f.addFine(fine);
               break;
            case NewReleaseDVD:
               f.addFine(fineBasis * daysLate);
               break;
         }
         return daysLate;
      }
      return 0;
   }
}