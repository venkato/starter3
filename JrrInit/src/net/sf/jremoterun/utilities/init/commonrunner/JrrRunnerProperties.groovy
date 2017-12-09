package net.sf.jremoterun.utilities.init.commonrunner;

import groovy.transform.CompileStatic;


@CompileStatic
interface JrrRunnerProperties {

    String jrrcasspathAddToSystemClassLoader = 'jrrcasspathAddToSystemClassLoader'
    String jrrOneJar = 'jrrOneJar'

    byte javaUsed = 2
    byte javawUsed = 3


}
