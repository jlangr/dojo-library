package domain.core;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.ClassificationService;

public class ClassificationApiFactory {
   private static final ClassificationApi DefaultService = new ClassificationService();
   private static ClassificationApi Service = DefaultService;

   public static void setService(ClassificationApi service) {
      Service = service;
   }

   public static ClassificationApi getService() {
      return Service;
   }
}
