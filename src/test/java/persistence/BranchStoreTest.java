package persistence;

import static domain.core.BranchTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import testutil.*;
import domain.core.*;

public class BranchStoreTest {
   private BranchStore store;

   @Before
   public void initialize() {
      BranchStore.deleteAll();
      store = new BranchStore();
   }
   
   @Test
   public void assignsIdToBranch() {
      Branch branch = new Branch("name");
      store.save(branch);
      assertTrue(branch.getScanCode().startsWith("b"));
   }
   
   @Test
   public void assignedIdIsUnique() {
      Branch branchA = new Branch("a");
      store.save(branchA);
      
      Branch branchB = new Branch("b");
      store.save(branchB);
      
      assertFalse(branchA.getScanCode().equals(branchB.getScanCode()));
   }
   
   @Test
   public void doesNotChangeIdIfAlreadyAssigned() {
      Branch branch = new Branch("", "b1964");
      
      store.save(branch);
      
      assertThat(branch.getScanCode(), is("b1964"));
   }

   @Test
   public void returnsSavedBranches() {
      store.save(BRANCH_EAST);

      Branch retrieved = store.find(EAST_NAME);

      assertEquals(BRANCH_EAST.getName(), retrieved.getName());
      assertEquals(BRANCH_EAST.getScanCode(), retrieved.getScanCode());
   }

   @Test
   public void returnsNewInstanceOfPersistedBranch() {
      store.save(BRANCH_EAST);
      store = new BranchStore();

      Branch retrieved = store.find(EAST_NAME);

      assertNotSame(BRANCH_EAST, retrieved);
   }

   @Test
   public void returnsListOfAllBranches() {
      store.save(BRANCH_EAST);

      Collection<Branch> branches = store.getAll();

      assertEquals(BRANCH_EAST, CollectionsUtil.soleElement(branches));
   }

   @Test
   public void findsBranchByScanCode() {
      store.save(BRANCH_EAST);

      Branch retrieved = store.findByScanCode(BRANCH_EAST.getScanCode());

      assertThat(retrieved, is(BRANCH_EAST));
   }

   @Test
   public void findsCheckedOutBranch() {
      assertThat(store.findByScanCode(Branch.CHECKED_OUT.getScanCode()),
            is(sameInstance(Branch.CHECKED_OUT)));
   }

   @Test
   public void findsBranchByScanCodeReturnsNullWhenNotFound() {
      assertThat(store.findByScanCode(""), is(nullValue()));
   }
}