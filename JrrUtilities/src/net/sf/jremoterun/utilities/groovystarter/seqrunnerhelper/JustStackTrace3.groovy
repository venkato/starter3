package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class JustStackTrace3 extends Exception{


    JustStackTrace3() {
        super('JustStackTrace')
    }
}
