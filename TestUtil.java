import java.util.*;
import org.junit.*;

public class TestUtil {
    static final String NO_ELEMENTS = "no elements";
    static final String MORE_THAN_ONE_ELEMENT = "more than one element";
    public static final String EXPECTED = "expected element not retrieved";

    public static <T> T soleElement(Collection<T> collection) {
        Iterator<T> it = collection.iterator();
        Assert.assertTrue(NO_ELEMENTS, it.hasNext());
        T sole = it.next();
        Assert.assertFalse(MORE_THAN_ONE_ELEMENT, it.hasNext());
        return sole;
    }

    @SafeVarargs
    public static <T> boolean containsExactly(Collection<T> collection, T... objects) {
        if (collection.size() != objects.length) return false;
        for (int i = 0; i < objects.length; i++)
            if (!collection.contains(objects[i])) return false;
        return true;
    }
}
