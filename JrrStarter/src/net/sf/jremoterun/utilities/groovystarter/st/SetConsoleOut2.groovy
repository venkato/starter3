package net.sf.jremoterun.utilities.groovystarter.st;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.st.ProxyInputStream
import net.sf.jremoterun.utilities.st.ProxyOutputStream2;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class SetConsoleOut2 {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static PrintStream originalOut;
    public static PrintStream originalErr;
    public static InputStream originalIn;
    public static ProxyOutputStream2 proxyOut;
    public static PrintStream proxyPrintStreamOut;
    public static PrintStream proxyPrintStreamErr;
    public static ProxyOutputStream2 proxyErr;
    public static ProxyInputStream proxyin;


    static void setConsoleOutIfNotInited(){
        if(originalErr==null){
            setConsoleOut()
        }
    }

    static void setConsoleOut(){
        assert originalOut ==null
        assert originalErr ==null
        assert proxyOut ==null
        assert proxyErr ==null
        proxyOut = new ProxyOutputStream2(System.out)
        proxyErr = new ProxyOutputStream2(System.err)
        originalOut = System.out
        originalErr = System.err
        originalIn = System.in
        proxyPrintStreamOut =new PrintStream(proxyOut)
        proxyPrintStreamErr =new PrintStream(proxyErr)
        proxyin =new ProxyInputStream(System.in)
        System.setOut(proxyPrintStreamOut);
        System.setErr(proxyPrintStreamErr);
        System.setIn(proxyin)
        synchronized (proxyErr){
            proxyErr.getClass();
        }
//        log.info "system in set"
    }
}
