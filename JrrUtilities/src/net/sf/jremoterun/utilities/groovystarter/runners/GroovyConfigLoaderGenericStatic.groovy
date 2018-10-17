package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile
import net.sf.jremoterun.utilities.classpath.FileScriptSource

import java.security.MessageDigest
import java.util.logging.Logger

@CompileStatic
class GroovyConfigLoaderGenericStatic extends GroovyConfigLoaderGenericStaticSuper {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    public GroovyClassLoader groovyClassLoader;

    GroovyConfigLoaderGenericStatic(GroovyClassLoader groovyClassLoader) {
        this.groovyClassLoader = groovyClassLoader
    }

    Class parseClassImpl(String scriptSource) {
        if (groovyClassLoader == null) {
            throw new Exception("groovy classloader is null")
        }
        return groovyClassLoader.parseClass(scriptSource)
    }


}
