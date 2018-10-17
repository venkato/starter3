package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class PhaseCheckException extends Exception{


    PhaseCheckException(String var1) {
        super(var1)
    }
}
