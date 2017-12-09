package net.sf.jremoterun.utilities.st

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class ProxyInputStream extends FilterInputStream{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ProxyInputStream(InputStream inn) {
        super(inn)
    }

    InputStream getNestedIn(){
        return this.in;
    }

    void setNestedIn(InputStream inNew){
        synchronized (this){
            this.in = inNew
        }

    }

}
