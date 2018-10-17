package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.utilities.JrrClassUtils

import java.text.SimpleDateFormat
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger;


@CompileStatic
public class JdkLogFormatter extends Formatter {

    public static JdkLogFormatter formatter = new JdkLogFormatter();

    public SimpleDateFormat sdfDayHourMin = new SimpleDateFormat("dd HH:mm:ss");

    public static String sep = "\n";

    public boolean logTime = false;

    public static Collection<String> ignoreClassesForCurrentClass = JrrClassUtils.ignoreClassesForCurrentClass;
    public static Logger rootLogger

    static Logger getRootLogger(){
        if(rootLogger==null) {
            rootLogger = Logger.getLogger("");
        }
        return rootLogger
    }


    static Handler[] findRootHandlers(){
        Logger logger = getRootLogger()
        Handler[] handlers = logger.getHandlers();
        return handlers
    }

    public static ConsoleHandler findConsoleHandler() {
        Handler[] handlers = findRootHandlers();
        if(handlers.length==0){
            throw new RuntimeException("No log handler found")
        }
        if(handlers.length>1){
            println "found many log handler ${handlers}"
        }
        ConsoleHandler consoleHandler = (ConsoleHandler) handlers.toList().find {it instanceof ConsoleHandler};
        if(consoleHandler==null){
            throw new RuntimeException("No console handler found from ${handlers.length} : ${handlers}")
        }
        return consoleHandler;
    }

    static JdkLogFormatter setLogFormatter() {
        SetConsoleOut2.setConsoleOutIfNotInited()
        return setLogFormatter2()
    }

    static JdkLogFormatter setLogFormatter2() {
        findConsoleHandler().setFormatter(formatter);
        return formatter;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        formatImpl(record, sb);
        return sb.toString();
    }

    void formatImpl(LogRecord logRecord, StringBuilder sb) {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        logStackElement(getStacktraceELement(stackTraces), sb);
        sb.append(" - ");
        logTime(logRecord, sb);

        logMessage(logRecord, sb);

        logStackTrace(logRecord, sb, stackTraces);
        sb.append(sep);
    }

    public void logMessage(LogRecord logRecord, StringBuilder sb) {
        sb.append(logRecord.getMessage());
    }

    void logStackElement(StackTraceElement se, StringBuilder sb) {
        sb.append(se);
//        String className = se.getClassName();
    }

    StackTraceElement getStacktraceELement(StackTraceElement[] stackTraces) {
        int k = 0;
        for (StackTraceElement stackTraceElement : stackTraces) {
            k++;
            if (k < 2) {
                continue;
            }
            if (isStackTraceElementFine(stackTraceElement)) {
                return stackTraceElement
            }
        }
        return null;
    }

    boolean isStackTraceElementFine(StackTraceElement stackTraceElement) {
        String lcassName = stackTraceElement.getClassName();
        for (String ignore : ignoreClassesForCurrentClass) {
            boolean res = lcassName.startsWith(ignore);
            if (res) {
                return false;
            } else {

            }
        }
        return true
    }


    String getTime() {
        String time;
        synchronized (sdfDayHourMin) {
            time = sdfDayHourMin.format(new Date());
        }
        return time;
    }


    void logTime(LogRecord logRecord, StringBuilder sb) {
        if (logRecord.getLevel().intValue() >= Level.WARNING.intValue()) {
            sb.append(getTime());
            sb.append(" ");
            sb.append(logRecord.getLevel().getName());
            sb.append(" ");
        } else {
            if (logTime) {
                sb.append(" ");
                sb.append(getTime());
                sb.append(" - ");
            }
        }
    }

    void logStackTrace(LogRecord logRecord, StringBuilder sb, StackTraceElement[] stackTraces) {
        boolean error = false;
        if (logRecord.getLevel().intValue() >= Level.WARNING.intValue()) {
            error = true;
        }
        Throwable ti = logRecord.getThrown();
        if (ti != null) {
            sb.append(" ");
            Throwable rootException = JrrUtils.getRootException(ti);
            if (error) {
                final StringWriter stringWriter = new StringWriter();
                rootException.printStackTrace(new PrintWriter(stringWriter));
                sb.append(stringWriter.getBuffer());
            } else {
                sb.append(rootException);
            }
        } else if (error) {

            int i = 0;
            for (StackTraceElement stackTraceElement : stackTraces) {
                i++;
                if (i < 5) {
                    continue;
                }

                String lcassName = stackTraceElement.getClassName();
                for (String ignore : ignoreClassesForCurrentClass) {
                    if (lcassName.startsWith(ignore)) {
                        return
                    }
                }
                sb.append(sep);
                sb.append("  ");
                sb.append(stackTraceElement.toString());
            }
        }
    }


}
