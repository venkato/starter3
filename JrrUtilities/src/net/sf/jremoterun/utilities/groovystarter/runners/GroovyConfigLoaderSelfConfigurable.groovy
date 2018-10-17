package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
interface GroovyConfigLoaderSelfConfigurable {

    void doConfigure(GroovyConfigLoaderGeneric configLoaderGeneric)


}
