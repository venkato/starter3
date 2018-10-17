package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile
import net.sf.jremoterun.utilities.classpath.FileScriptSource
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I

import java.security.MessageDigest
import java.util.logging.Logger

@CompileStatic
class GroovyConfigLoaderGeneric<T> extends GroovyConfigLoaderGenericStatic {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

//    @Deprecated
//    public static GroovyConfigLoaderGeneric configLoaderGeneric = new GroovyConfigLoaderGeneric()

    GroovyConfigLoaderGeneric() {
        super(GroovyClassLoaderDefault.receiveGroovyClassLoader2())
    }

    GroovyConfigLoaderGeneric(GroovyClassLoader groovyClassLoader) {
        super(groovyClassLoader)
    }

    T createInstance(Class clazz) {
        return clazz.newInstance()
    }

    T parseConfig(String script) {
        T config1 = createInstance(parseConfigClass(script,null))
        selfConfigure(config1)
        return config1
    }

    T parseConfig(File f) {
        T config1 = createInstance(parseConfigClass(f))
        if (config1 instanceof FileScriptSource) {
            FileScriptSource configLoaderLocationAware = (FileScriptSource) config1;
            configLoaderLocationAware.setFileScriptSource(f)
        }
        selfConfigure(config1)
        return config1
    }

    void selfConfigure(Object config1) {
        if (config1 instanceof GroovyConfigLoaderSelfConfigurable) {
            GroovyConfigLoaderSelfConfigurable configLoaderLocationAware = (GroovyConfigLoaderSelfConfigurable) config1;
            configLoaderLocationAware.doConfigure(this)
        }
    }

}
