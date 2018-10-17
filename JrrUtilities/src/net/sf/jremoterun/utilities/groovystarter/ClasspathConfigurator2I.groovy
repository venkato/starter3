 package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.AddFilesToClassLoaderGroovy

@CompileStatic
interface ClasspathConfigurator2I extends GroovyConfigLoader2I<AddFilesToClassLoaderGroovy> {

    @Override
    void loadConfig(AddFilesToClassLoaderGroovy b);


}
