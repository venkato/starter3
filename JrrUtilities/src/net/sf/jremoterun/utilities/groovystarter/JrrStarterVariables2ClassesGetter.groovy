package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.javassist.codeinjector.InjectedCode;

import java.util.logging.Logger;

@CompileStatic
class JrrStarterVariables2ClassesGetter extends InjectedCode {


    @Override
    Object getImpl(Object key) throws Exception {
        List<File> ff = [JrrStarterVariables2.getInstance().classesDir, JrrStarterVariables2.getInstance().jrrConfigDir2, JrrStarterVariables2.getInstance().filesDir]
        return ff
    }
}
