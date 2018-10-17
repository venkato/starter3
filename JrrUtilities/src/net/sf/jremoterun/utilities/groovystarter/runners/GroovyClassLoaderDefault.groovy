package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.JustStackTrace3;

import java.util.logging.Logger;

@CompileStatic
class GroovyClassLoaderDefault {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();



    public static ClassLoader thisClassLoader = JrrClassUtils.getCurrentClassLoader()
    public static volatile GroovyClassLoader groovyClassLoader
    public static volatile JustStackTrace3 groovyClassloaderCreationLocation

    static GroovyClassLoader receiveGroovyClassLoader2() {
        receiveGroovyClassLoader()
        return groovyClassLoader
    }

    static void receiveGroovyClassLoader() {
        if (groovyClassLoader == null) {
            if (thisClassLoader instanceof GroovyClassLoader) {
                groovyClassLoader = (GroovyClassLoader) thisClassLoader;
            } else {
                groovyClassLoader = new GroovyClassLoader(thisClassLoader)

            }
            groovyClassloaderCreationLocation = new JustStackTrace3()
        }
    }


}
