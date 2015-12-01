import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.*;

public class BranchTest {
    public static final String EAST = "east";
    public static final String WEST = "west";
    public static final String EAST_SCAN = "b111";
    public static final String WEST_SCAN = "b222";
    public static final Branch BRANCH_EAST = new Branch(EAST);
    public static final Branch BRANCH_WEST = new Branch(WEST);
    private Branch eastBranch;

    @Before
    public void initialize() {
        eastBranch = new Branch(EAST);
    }

    @Test
    public void defaultsIdToEmpty() {
        assertThat(new Branch("alpha").getScanCode(), equalTo(""));
    }

    @Test
    public void initializesNameOnCreation() {
        assertThat(eastBranch.getName(), equalTo(EAST));
    }

    @Test
    public void supportsEquality() {
        Branch branch1 = BRANCH_EAST;
        branch1.setScanCode(EAST_SCAN);
        Branch branch1Copy1 = new Branch(EAST);
        branch1Copy1.setScanCode(EAST_SCAN);
        Branch branch1Copy2 = new Branch(EAST);
        branch1Copy2.setScanCode(EAST_SCAN);
        Branch branch2 = BRANCH_WEST;
        branch2.setScanCode(WEST_SCAN);
        Branch branch1Subtype = new Branch(EAST) {
        };
        branch1Subtype.setScanCode(EAST_SCAN);

        new EqualityTester(branch1, branch1Copy1, branch1Copy2, branch2, branch1Subtype).verify();
    }
}
