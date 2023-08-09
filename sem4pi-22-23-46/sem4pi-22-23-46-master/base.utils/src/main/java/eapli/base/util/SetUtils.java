package eapli.base.util;

import java.util.Iterator;
import java.util.Set;

public class SetUtils {
    public static <T> T getLastElement(Set<T> set) {
        Iterator<T> iterator = set.iterator();
        T lastElement = null;

        while (iterator.hasNext()) {
            lastElement = iterator.next();
        }

        return lastElement;
    }

}
