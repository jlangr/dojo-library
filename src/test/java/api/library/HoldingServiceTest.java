package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingServiceTest {
   static final String CLASSIFICATION = "123";
   static final String WEST_BRANCH = "West";
   static final String HOLDING_BARCODE = Holding.createBarCode(CLASSIFICATION,
         1);
   private HoldingService service;
   private ClassificationApi classificationApi;
   private MaterialDetails tessMaterial;
   private String westScanCode;

   public MaterialDetails createTess(String classification) {
      return new MaterialDetails("Hardy, Thomas", "Tess of the d'Urbervilles",
            classification, MaterialType.Book, "1891");
   }

   @Before
   public void initialize() {
      LibraryData.deleteAll();
      service = new HoldingService();
      classificationApi = mock(ClassificationApi.class);
      ClassificationApiFactory.setService(classificationApi);

      westScanCode = new BranchService().add(WEST_BRANCH);

      tessMaterial = createTess(CLASSIFICATION);
      when(classificationApi.getMaterialDetails(CLASSIFICATION)).thenReturn(
            tessMaterial);
   }

   @Test
   public void usesClassificationServiceToRetrieveBookDetails() {
      service.add(HOLDING_BARCODE, westScanCode);

      Holding holding = service.find(HOLDING_BARCODE);
      assertThat(holding.getMaterial().getAuthor(), is(tessMaterial.getAuthor()));
   }

   @Test
   public void throwsExceptionWhenBranchNotFound() {
      try {
         service.add(HOLDING_BARCODE, "b99999");
      } catch (BranchNotFoundException expected) {
         assertThat(expected.getMessage(), is("Branch not found: b99999"));
      }
   }
}
