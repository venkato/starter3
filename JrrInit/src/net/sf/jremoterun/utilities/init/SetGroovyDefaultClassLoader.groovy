package net.sf.jremoterun.utilities.init


import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams

import java.util.logging.Logger

// @CompileStatic
class SetGroovyDefaultClassLoader implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    ClRef cnr = new ClRef('org.codehaus.groovy.runtime.typehandling.ShortTypeHandlingClassCast')

    @Override
    void run() {
        GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrpn
        Class aClass = cnr.loadClass(gmrp.groovyClassLoader)
        aClass.defaultClassLoader = gmrp.groovyClassLoader;
    }
}
