package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic

@CompileStatic
interface GroovyConfigLoader1ParamsI<T> {

    Object loadConfig(T b);


}
