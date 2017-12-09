package net.sf.jremoterun.utilities;

import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Level;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class CreationInfo {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    Exception callStack = new Exception("call stack")

    Date creationDate = new Date();

    Exception createExc(){
        log.log(Level.SEVERE,"created before", callStack)
        // log.log(Level.INFO, "created now", new Exception())
        return new Exception("created before");
    }




}
