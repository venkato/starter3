package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.FileScriptSource
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

@Deprecated
@CompileStatic
abstract class GroovyConfigLoader2<T> extends Script implements GroovyConfigLoader2I<T> , FileScriptSource{

    public static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String varName = 'a'

    public static MethodClosure loadConfigMethod =(MethodClosure) (Closure)GroovyConfigLoader2I.&loadConfig;

    /**
     * Can be null.
     * Can be used to build ref to files located in subfolder
     */
    public File thisFile;

    final void doConfig() {
        Object obj =  getVar(varName)
        loadConfig((T)obj)
    }

    Object getVar(String name1) {
        Object propertyValue = binding.getProperty(name1)
        if (propertyValue == null) {
            throw new NullPointerException("no such property ${name1}, available : ${binding.variables.keySet()}")
        }
        return propertyValue
    }

    abstract void loadConfig(T b);


    @Override
    final Object run() {
        doConfig()
        return null
    }

    @Override
    void setFileScriptSource(File f) {
        thisFile = f
    }
}
