package domain.core;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import testutil.EqualityTester;

public class BranchTest {
   public static final String EAST_NAME = "east";
   public static final String WEST_NAME = "west";
   public static final String EAST_SCAN = "b111";
   public static final String WEST_SCAN = "b222";
   public static final Branch BRANCH_EAST = new Branch(EAST_NAME);
   public static final Branch BRANCH_WEST = new Branch(WEST_NAME);
   private Branch branch;

   @Before
   public void initialize() {
      branch = new Branch(EAST_NAME);
   }

   @Test
   public void defaultsIdToEmpty() {
      assertEquals("", new Branch("alpha").getScanCode());
   }

   @Test
   public void initializesNameOnCreation() {
      assertEquals(EAST_NAME, branch.getName());
   }

   @Test
   public void supportsEquality() {
      Branch branch1 = BRANCH_EAST;
      branch1.setScanCode(EAST_SCAN);
      Branch branch1Copy1 = new Branch(EAST_NAME);
      branch1Copy1.setScanCode(EAST_SCAN);
      Branch branch1Copy2 = new Branch(EAST_NAME);
      branch1Copy2.setScanCode(EAST_SCAN);
      Branch branch2 = BRANCH_WEST;
      branch2.setScanCode(WEST_SCAN);
      Branch branch1Subtype = new Branch(EAST_NAME) {
      };
      branch1Subtype.setScanCode(EAST_SCAN);

      new EqualityTester(branch1, branch1Copy1, branch1Copy2, branch2,
            branch1Subtype).verify();
   }
}