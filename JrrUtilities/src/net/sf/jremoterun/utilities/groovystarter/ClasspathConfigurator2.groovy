package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderGroovy
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

@Deprecated
@CompileStatic
abstract class ClasspathConfigurator2 extends  GroovyConfigLoader2<AddFilesToClassLoaderGroovy> implements ClasspathConfigurator2I {

    @Override
    abstract void loadConfig(AddFilesToClassLoaderGroovy b);


}
