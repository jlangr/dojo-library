package com.loc.material.api;

public enum MaterialType {
   Book(21, 10),
   AudioCassette(14, 10),
   VinylRecording(14, 10),
   MicroFiche(7, 200),
   AudioCD(7, 100),
   SoftwareCD(7, 500),
   DVD(3, 100),
   NewReleaseDVD(1, 200),
   BluRay(3, 200),
   VideoCassette(7, 10);

   private int checkoutPeriod;
   private int dailyFine;

   MaterialType(int checkoutPeriod) {
      this(checkoutPeriod, 10);
   }

   MaterialType(int checkoutPeriod, int dailyFine) {
      this.checkoutPeriod = checkoutPeriod;
      this.dailyFine = dailyFine;
   }

   public int getCheckoutPeriod() {
      return checkoutPeriod;
   }

   public int getDailyFine() {
      return dailyFine;
   }
}
