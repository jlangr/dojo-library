package com.loc.material.api;

import java.util.Collection;

public interface ClassificationApi {
   boolean isValid(String classification);

   MaterialDetails getMaterialDetails(String classification);

   Collection<MaterialDetails> allMaterials();
}
