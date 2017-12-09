package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
interface CustomObjectHandler {


    void add(AddFilesToClassLoaderCommon adder,Object object);

    File resolveToFile(Object object);

}
