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

    Class parseClassImpl(String scriptSource,String filename) {
        if (groovyClassLoader == null) {
            throw new Exception("groovy classloader is null")
        }
        if(filename==null||filename.length()==0){
            return groovyClassLoader.parseClass(scriptSource)
        }
        return groovyClassLoader.parseClass(scriptSource,filename)
    }


}
