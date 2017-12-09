package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class AddFilesToClassLoaderCommonDummy extends AddFilesToClassLoaderCommon{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    @Override
    void addFileImpl(File file) throws Exception {

    }
}
