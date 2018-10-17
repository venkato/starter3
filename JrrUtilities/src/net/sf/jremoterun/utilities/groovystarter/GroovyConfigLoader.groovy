package net.sf.jremoterun.utilities.groovystarter;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.FileScriptSource
import net.sf.jremoterun.utilities.groovystarter.ClasspathConfigurator
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import org.codehaus.groovy.runtime.MethodClosure;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@Deprecated
@CompileStatic
abstract class GroovyConfigLoader<T> extends GroovyRunnerConfigurator2 implements FileScriptSource, GroovyConfigLoader2I{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String varName = 'a'

    public static MethodClosure loadConfigMethod =(MethodClosure) (Closure)GroovyConfigLoader.&loadConfig;

    /**
     * Can be null.
     * Can be used to build ref to files located in subfolder
     */
    public File thisFile;

    @Override
    final void doConfig() {
        Object obj =  getVar(varName)
        loadConfig((T)obj)
    }

    abstract void loadConfig(T t);

    @Override
    void setFileScriptSource(File f) {
        thisFile = f
    }
}
