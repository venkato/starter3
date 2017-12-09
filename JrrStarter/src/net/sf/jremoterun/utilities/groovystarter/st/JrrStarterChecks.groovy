package net.sf.jremoterun.utilities.groovystarter.st;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JrrStarterChecks implements Runnable{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ClRef cnr = new ClRef('javassist.util.proxy.ProxyFactory')

    @Override
    void run() {
        cnr.loadClass2()
    }
}
