package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.codehaus.groovy.runtime.MethodClosure;

import java.util.logging.Logger;

@CompileStatic
class MethodClosureHelper {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static MethodClosure cast1(Closure closure1){
        return (MethodClosure)closure1
    }

}
