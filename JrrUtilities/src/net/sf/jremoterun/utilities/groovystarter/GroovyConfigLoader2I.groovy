package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic

@CompileStatic
interface GroovyConfigLoader2I<T> {

    void loadConfig(T b);


}
