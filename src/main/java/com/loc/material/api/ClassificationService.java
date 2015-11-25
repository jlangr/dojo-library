package com.loc.material.api;

import java.util.Collection;

public class ClassificationService implements ClassificationApi {
   @Override
   public MaterialDetails getMaterialDetails(String classification) {
      throw new RuntimeException("unable to connect");
   }

   @Override
   public boolean isValid(String classification) {
      throw new RuntimeException("unable to connect");
   }

   @Override
   public Collection<MaterialDetails> allMaterials() {
      throw new RuntimeException("unable to connect");
   }
}
