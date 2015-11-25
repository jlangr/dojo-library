package domain.core;

import java.util.Date;
import com.loc.material.api.*;
import util.*;

public class Holding {
   public static final String BARCODE_SEPARATOR = ":";
   private MaterialDetails material;
   private Branch branch;
   private Date dateCheckedOut;
   private Date dateLastCheckedIn;
   private int copyNumber;

   public Holding(MaterialDetails material) {
      this(material, Branch.CHECKED_OUT);
   }

   public Holding(MaterialDetails material, Branch branch) {
      this(material, branch, 1);
   }

   public Holding(MaterialDetails material, Branch branch, int copyNumber) {
      this.material = material;
      this.branch = branch;
      this.copyNumber = copyNumber;
   }

   public MaterialDetails getMaterial() {
      return material;
   }

   public Branch getBranch() {
      return branch;
   }

   public int getCopyNumber() {
      return copyNumber;
   }

   public void transfer(Branch newBranch) {
      branch = newBranch;
   }

   public boolean isAvailable() {
      return branch != Branch.CHECKED_OUT;
   }

   public void checkOut(Date date) {
      branch = Branch.CHECKED_OUT;
      dateCheckedOut = date;
   }

   public Date dateCheckedOut() {
      return dateCheckedOut;
   }

   public Date dateDue() {
      return DateUtil.addDays(dateCheckedOut, getHoldingPeriod());
   }

   private int getHoldingPeriod() {
      return material.getFormat().getCheckoutPeriod();
   }

   public int checkIn(Date date, Branch branch) {
      this.dateLastCheckedIn = date;
      this.branch = branch;
      int daysLate = DateUtil.daysFrom(dateDue(), dateLastCheckedIn);
      return daysLate < 0 ? 0 : daysLate;
   }

   public Date dateLastCheckedIn() {
      return dateLastCheckedIn;
   }

   public String getBarCode() {
      return createBarCode(material.getClassification(), copyNumber);
   }

   public void setCopyNumber(int copyNumber) {
      this.copyNumber = copyNumber;
   }

   public static String createBarCode(String classification, int copyNumber) {
      return classification + BARCODE_SEPARATOR + copyNumber;
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if (this.getClass() != object.getClass())
         return false;
      Holding that = (Holding)object;
      return this.getBarCode().equals(that.getBarCode());
   }

   @Override
   public String toString() {
      return material.toString() + "(" + copyNumber + ") @ " + branch.getName();
   }

   @Override
   public int hashCode() {
      return getBarCode().hashCode();
   }

   public static int getCopyNumber(String barcode) {
      String copy = splitOnColon(barcode)[1];
      return parsePositiveInt(copy);
   }

   public static String getClassification(String barcode) {
      return splitOnColon(barcode)[0];
   }

   private static int parsePositiveInt(String text) {
      int number = parseInt(text);
      if (number < 1)
         throw new IllegalArgumentException();
      return number;
   }

   private static String[] splitOnColon(String barcode) {
      String[] barcodeParts = barcode.split(":");
      if (barcodeParts.length != 2)
         throw new IllegalArgumentException();
      return barcodeParts;
   }

   private static int parseInt(String text) {
      try {
         return Integer.parseInt(text, 10);
      } catch (Throwable t) {
         throw new IllegalArgumentException();
      }
   }

   public int daysLate() {
      return DateUtil.daysAfter(dateDue(), dateLastCheckedIn());
   }
}