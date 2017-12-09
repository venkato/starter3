package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderGroovy

import java.util.logging.Logger

@CompileStatic
abstract class ClasspathConfigurator extends GroovyConfigLoader<AddFilesToClassLoaderGroovy> {

    public static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();




    @Override
    void loadConfig(AddFilesToClassLoaderGroovy b) {
        addCp(b)
    }

    @Deprecated
    void addCp(AddFilesToClassLoaderGroovy b) {}
}
