package net.sf.jremoterun.utilities.groovystarter;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import org.codehaus.groovy.runtime.MethodClosure;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
abstract class GroovyConfigLoader<T> extends GroovyRunnerConfigurator2{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String varName = 'a'

    public static MethodClosure loadConfigMethod =(MethodClosure) GroovyConfigLoader.&loadConfig;

    @Override
    final void doConfig() {
        Object obj =  getVar(varName)
        loadConfig((T)obj)
    }

    abstract void loadConfig(T t);


}
