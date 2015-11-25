package api.library;

import java.util.*;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.MaterialDetails;

import domain.core.ClassificationApiFactory;

public class MockHoldingService extends HoldingService {
   private FakeClassificationApi classificationApi;

   public MockHoldingService() {
      classificationApi = new FakeClassificationApi();
      ClassificationApiFactory.setService(classificationApi);
   }

   public void addTestBookToMaterialService(MaterialDetails material) {
      classificationApi.add(material);
   }

   class FakeClassificationApi implements ClassificationApi {
      private Map<String, MaterialDetails> materials = new HashMap<String, MaterialDetails>();

      @Override
      public boolean isValid(String classification) {
         return materials.containsKey(classification);
      }

      public void add(MaterialDetails material) {
         materials.put(material.getClassification(), material);
      }

      @Override
      public MaterialDetails getMaterialDetails(String classification) {
         return materials.get(classification);
      }

      @Override
      public Collection<MaterialDetails> allMaterials() {
         return materials.values();
      }
   }
}