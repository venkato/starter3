package net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class JustStackTrace3 extends Exception implements Serializable{

    private static final long serialVersionUID = -3387516313124229948L;

    JustStackTrace3() {
        super('JustStackTrace')
    }
}
