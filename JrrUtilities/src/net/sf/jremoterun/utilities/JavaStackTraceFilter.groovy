package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class JavaStackTraceFilter implements NewValueListener<Throwable>{

    public boolean printFullStackTrace = false
    public boolean printFirstAlways = true
    public boolean printOnlyRootException = true

    public HashSet<String> acceptClassesForCurrentClassThis = new HashSet<>()
    public HashSet<String> ignoreClassesForCurrentClassThis = new HashSet<>()

    Throwable getRootException(Throwable t){
        if(printOnlyRootException) {
            Throwable tCandidate = getRootException2(t,true)
            StackTraceElement[] trace = tCandidate.getStackTrace()
            if (trace == null || trace.length == 0) {
                return getRootException2(t,false)
            }
            return tCandidate
        }
        return t;
    }

    public static Throwable getRootException2(final Throwable throwable,boolean emptyStackTraceOk) {
        final Throwable cause = throwable.getCause();
        if (cause == null) {
            return throwable;
        }
        Throwable exception2 = getRootException2(cause, emptyStackTraceOk);
        if(!emptyStackTraceOk) {
            StackTraceElement[] stackTrace = cause.getStackTrace();
            if (stackTrace == null || stackTrace.length == 0) {
                return throwable;
            }
        }
        return exception2
    }

    @Override
    void newValue(Throwable throwable) {
        printValue(throwable)
    }

    void printBefore(StringBuilder sb2 ){

    }


    void printValue(Throwable t){
        StringBuilder sb2 = genericStuff3(t)
        System.err.println(sb2)
    }



    StringBuilder genericStuff3(Throwable t) {
        Throwable tCandidate = getRootException(t)
        StringBuilder sb2 = new StringBuilder()
        printBefore(sb2)
        sb2.append(t);
        Throwable cause = t.getCause()
        if (cause != null) {
            sb2.append('\n nested exception: ')
            sb2.append(cause)
        }
        sb2.append('\n');

        if(printFullStackTrace){
            sb2.append(convertWhole(tCandidate))
        }else {
            sb2.append(printExceptionWithoutIgnoreClasses2(tCandidate).toString())
        }
        return sb2
    }

    String convertWhole(Throwable t){
        StringWriter sw3 = new StringWriter()
        PrintWriter printWriter = new PrintWriter(sw3);
        t.printStackTrace(printWriter);
        return sw3.toString()
    }


    StringBuilder printExceptionWithoutIgnoreClasses2(Throwable t) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = t.getStackTrace();
        boolean stackTraceFound = false;
        int printedCount = 0
        for (StackTraceElement te : stackTrace) {
            if (checkIfStackTraceMatched3(te,printedCount)) {
                printedCount++
                printStackElement(te,sb)
                stackTraceFound = true;
            }
        }
        if (!stackTraceFound) {
            for (StackTraceElement te : stackTrace) {
                printStackElement(te,sb)
            }
        }
        return sb;
    }

    void printStackElement(StackTraceElement te,StringBuilder sb){
        sb.append("\tat ");
        sb.append(te);
        sb.append("\n");
    }


    boolean checkIfStackTraceMatched3(StackTraceElement te,int printedCount) {
        if(printFirstAlways && printedCount ==0){
            return true
        }
        String lcassName = te.getClassName();
        for (String ignore : acceptClassesForCurrentClassThis) {
            if (lcassName.startsWith(ignore)) {
                return true
            }
        }
        for (String ignore : JrrClassUtils.ignoreClassesForCurrentClass) {
            if (lcassName.startsWith(ignore)) {
                return false
            }
        }
        for (String ignore : ignoreClassesForCurrentClassThis) {
            if (lcassName.startsWith(ignore)) {
                return false
            }
        }
        return true
    }



}
