package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic

@CompileStatic
interface GroovyConfigLoader2ParamsI<T,P> {

    Object loadConfig(T b,P p);


}
