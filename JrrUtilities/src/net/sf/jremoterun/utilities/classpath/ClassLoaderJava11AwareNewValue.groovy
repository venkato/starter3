package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.UrlClassLoaderAddFileJava11Aware;

import java.util.logging.Logger;

@CompileStatic
class ClassLoaderJava11AwareNewValue implements NewValueListener<File>{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ClassLoader classLoader1;
    public UrlClassLoaderAddFileJava11Aware urlClassLoaderAddFileJava11Aware = UrlClassLoaderAddFileJava11Aware.classLoaderAddFile;

    ClassLoaderJava11AwareNewValue(ClassLoader classLoader1) {
        this.classLoader1 = classLoader1
    }

    @Override
    void newValue(File file) {
        urlClassLoaderAddFileJava11Aware.addFileMain(classLoader1,file)
    }
}
