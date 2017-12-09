package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener

import java.util.logging.Logger

@CompileStatic
public class PrintExceptionListener implements NewValueListener<Throwable> {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public static boolean printFullStackTrace = false;

    @Override
    public void newValue(Throwable t) {
        genericStuff(t)
        System.exit(1)
    }

    public static flushOutFile() {
        if (GroovyMethodRunnerParams.instance.fileOut != null) {
            try {
                GroovyMethodRunnerParams.instance.fileOut.flush()
            } catch (Throwable e2) {
            }
        }
    }

    public static String dumpPhase() {
        StringBuilder sb2 = new StringBuilder()
        if (GroovyMethodRunnerParams.instance.groovyMethodRunner != null) {
            sb2.append("At phase : ")
            sb2.append(GroovyMethodRunnerParams.instance.seqPatternRunnerGmrp.jrrRunnerPhase)
            sb2.append("\n")
        }
        return sb2.toString()

    }



    void genericStuff(Throwable t) {
        t = JrrUtils.getRootException(t)
        StringBuilder sb2 = new StringBuilder()
        sb2.append(dumpPhase())
        sb2.append(t);
        Throwable cause = t.getCause()
        if (cause != null) {
            sb2.append('\n nested exception: ')
            sb2.append(cause)
        }
        sb2.append('\n');

        if(printFullStackTrace){
            StringWriter sw3 = new StringWriter()
            PrintWriter printWriter = new PrintWriter(sw3);
            t.printStackTrace(printWriter);
            sb2.append(sw3)
        }else {
            sb2.append(JrrClassUtils.printExceptionWithoutIgnoreClasses2(t).toString())
        }
        System.err.println(sb2)
        flushOutFile()
    }
}
