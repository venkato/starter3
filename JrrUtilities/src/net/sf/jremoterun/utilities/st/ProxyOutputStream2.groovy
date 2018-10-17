package net.sf.jremoterun.utilities.st;

import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class ProxyOutputStream2 extends FilterOutputStream{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public ProxyOutputStream2(OutputStream out) {
        super(out)
    }

    OutputStream getNestedOut(){
        return out;
    }

    @Override
    void write(byte[] b, int off, int len) throws IOException {
        getNestedOut().write(b, off, len)
    }

    void setNestedOut(OutputStream outNew){
        synchronized (this){
            this.out = outNew
        }

    }

}
