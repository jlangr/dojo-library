package com.loc.material.api;

public interface ClassificationApi {
   boolean isValid(String classification);
   MaterialDetails getMaterialDetails(String classification);
   java.util.Collection<MaterialDetails> allMaterials();
}
