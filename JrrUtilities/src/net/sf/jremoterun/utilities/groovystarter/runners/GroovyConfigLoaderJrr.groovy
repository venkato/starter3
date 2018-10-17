package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I


@CompileStatic
class GroovyConfigLoaderJrr{




    public static GroovyConfigLoaderGeneric configLoader = new GroovyConfigLoaderGeneric()


    public static GroovyConfigLoaderGeneric<GroovyConfigLoader2I<Object>> configLoaderAdvance = (GroovyConfigLoaderGeneric)configLoader

    GroovyConfigLoaderJrr() {
    }
}
