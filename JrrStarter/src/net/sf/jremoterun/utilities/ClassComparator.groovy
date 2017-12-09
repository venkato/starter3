package net.sf.jremoterun.utilities;

import net.sf.jremoterun.utilities.JrrClassUtils;

import java.util.Comparator;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class ClassComparator implements Comparator<Class> {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClassComparator classComparator = new ClassComparator();

    int compare(final Class o1, final Class o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
