package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilitiesFile
import net.sf.jremoterun.utilities.classpath.FileScriptSource

import net.sf.jremoterun.utilities.groovystarter.GroovyConfigLoader2I

import java.security.MessageDigest;
import java.util.logging.Logger;

@CompileStatic
class GroovyConfigLoaderJrr<T> extends GroovyConfigLoaderGeneric<GroovyConfigLoader2I<T>> {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static GroovyConfigLoaderJrr configLoader = new GroovyConfigLoaderJrr()

}
