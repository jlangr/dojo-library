package api.library;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
import com.loc.material.api.*;
import domain.core.*;

public class HoldingServiceTest {
   static final String CLASSIFICATION = "123";
   static final String WEST_BRANCH = "West";
   static final String HOLDING_BARCODE = Holding.createBarCode(CLASSIFICATION, 1);
   private HoldingService service;
   private ClassificationApi classificationApi;
   private MaterialDetails tessMaterial;
   private String westScanCode;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   public MaterialDetails createTess(String classification) {
      return new MaterialDetails("Hardy, Thomas", "Tess of the d'Urbervilles", classification,
            MaterialType.Book, "1891");
   }

   @Before
   public void initialize() {
      new Catalog().deleteAll();
      classificationApi = mock(ClassificationApi.class);
      service = new HoldingService(classificationApi);

      BranchService branchService = new BranchService();
      branchService.deleteAll();
      westScanCode = branchService.add(WEST_BRANCH);

      tessMaterial = createTess(CLASSIFICATION);
      when(classificationApi.getMaterialDetails(CLASSIFICATION)).thenReturn(tessMaterial);
   }

   @Test
   public void usesClassificationServiceToRetrieveBookDetails() {
      service.add(HOLDING_BARCODE, westScanCode);

      Holding holding = service.find(HOLDING_BARCODE);
      assertThat(holding.getMaterial().getAuthor(), equalTo(tessMaterial.getAuthor()));
   }

   @Test
   public void throwsExceptionWhenBranchNotFound() {
      exception.expect(RuntimeException.class);
      exception.expectMessage("Branch not found: b99999");

      service.add(HOLDING_BARCODE, "b99999");
   }
}
