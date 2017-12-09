package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;

@Deprecated
@CompileStatic
class ClassNameReference extends ClRef{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    ClassNameReference(String className) {
        super(className);
    }
}
