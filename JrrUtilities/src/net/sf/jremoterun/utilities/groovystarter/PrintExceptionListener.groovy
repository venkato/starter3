package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.utilities.JavaStackTraceFilter
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener

import java.util.logging.Logger

@CompileStatic
public class PrintExceptionListener  extends JavaStackTraceFilter implements NewValueListener<Throwable> {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

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

    @Override
    void printBefore(StringBuilder sb2) {
        sb2.append(dumpPhase())
    }

    void genericStuff(Throwable t) {
        StringBuilder sb2 = genericStuff3(t)
        System.err.println(sb2)
        flushOutFile()
    }
}
