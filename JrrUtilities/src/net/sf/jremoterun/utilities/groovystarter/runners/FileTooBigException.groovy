package net.sf.jremoterun.utilities.groovystarter.runners

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class FileTooBigException extends Exception{

    FileTooBigException() {
    }

    FileTooBigException(String var1) {
        super(var1)
    }
}
