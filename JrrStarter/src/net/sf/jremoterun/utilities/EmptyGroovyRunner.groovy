package net.sf.jremoterun.utilities


import net.sf.jremoterun.utilities.classpath.ClRef

import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import groovy.transform.CompileStatic;


@CompileStatic
class EmptyGroovyRunner extends GroovyRunnerConfigurator2{

    public static ClRef cnr = new ClRef(EmptyGroovyRunner);

    @Override
    void doConfig() {
    }
}
