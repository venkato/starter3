package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

@CompileStatic
class ShortcutSelector {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    Map shortcuts
    Map<String, Object> shortcutsNarmalized
//    Map<String, Object> shortcutsHuman
    String msgHelp

    ShortcutSelector(Map shortcuts) {
        this.shortcuts = shortcuts
        normalize()
    }


    void printHelp() {
        StackTraceElement[] trace = new Exception().getStackTrace()
        StackTraceElement find = trace.find { it.className != ShortcutSelector.name }
        System.out.println """${find} - available ${shortcuts.size()} shortcuts:
${msgHelp}"""
//        log.info "\n${msg}"
    }

    static String separator = " \t: ";

    String getInfo(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("key is null for key ${value}")
        }
        if (value == null) {
            throw new NullPointerException("value is null for key ${key}")
        }
        final String key2 = convertKey(key)
        final String value2 = convertValueToHuman(value)
        if (key instanceof ConsoleSymbols) {
            return "${key.s}${separator}${key.name()}"
        }
        return "${key2}${separator}${value2}"
    }

    Object findAction(String msg) {
        return shortcutsNarmalized.get(msg)
    }

    void normalize() {
        createHelpMsg()
        shortcutsNarmalized = (Map) shortcuts.collectEntries { [(convertKey(it.key)): it.value] }
//        shortcutsHuman = (Map) shortcutsNarmalized.collectEntries { [(it.key): convertValueToHuman(it.value)] }
    }

    void createHelpMsg() {
        msgHelp = shortcuts.collect { getInfo(it.key, it.value) }.join('\n')
    }

    String convertValueToHuman(Object value) {
        if (value instanceof ClRefRef) {
            return value.getClRef().getClassName()
        }
        if (value instanceof Class) {
            return value.getName()
        }
        if (value instanceof MethodClosure) {
            return value.getMethod()
        }
        throw new IllegalArgumentException("${value}")
    }

    void runAction(Object value) {
        if (value instanceof ClRefRef) {
            value = value.clRef.loadClass2()
//            RunnableFactory.runRunner value
//            return
        }

        if (value instanceof Class) {
            Class clazz23 =ClassNameSynonym
            if(clazz23.isAssignableFrom(value)){
                GroovyMethodRunnerParams.gmrp.args.add(0,value.name)
            }else {
                LoadScriptFromFileUtils.runNoParams(value.newInstance(),null)
            }
            return
        }
        if (value instanceof MethodClosure) {
            value.call()
            return
        }
        throw new IllegalArgumentException("${value}")
    }

    private void runAction223(Object value) {
        if (value instanceof ClRefRef) {
            RunnableFactory.runRunner value
            return
        }

        if (value instanceof Class) {
            Runnable r = value.newInstance() as Runnable
            r.run()
            return
        }
        if (value instanceof MethodClosure) {
            value.call()
            return
        }
        throw new IllegalArgumentException("${value}")
    }


    String convertKey(Object key) {
        if (key instanceof ShortcutInfo) {
            return key.getS()
        }
        if (key instanceof String) {
            return key;
        }
        if (key instanceof Character) {
            return key.toString();
        }
        if (key instanceof CharSequence) {
            return key.toString();
        }
        throw new IllegalArgumentException("Stange key : ${key}")
    }



    @Deprecated
    static boolean runActionRemoveFirstParam(Map params) {
        boolean actionSeleted = runAction2(params)
        if (actionSeleted) {
            GroovyRunnerConfigurator2.removeFirstParam()
        }
        return actionSeleted
    }

    /**
     * return true if some action started
     */
    static boolean runActionRemoveFirstParam2(Map params) {
        ShortcutSelector ss = new ShortcutSelector(params)
        return ss.runActionRemoveFirstParam3()
    }

    /**
     * return true if some action started
     */
    boolean runActionRemoveFirstParam3() {
        ShortcutSelector ss = this
        String firstParam = GroovyRunnerConfigurator2.getFirstParam()
        if (firstParam == ConsoleSymbols.question.s) {
            ss.printHelp()
            return false
        }
        Object action = ss.findAction(firstParam)
        if (action == null) {
            return false
        }
        GroovyRunnerConfigurator2.removeFirstParam()
        ss.runAction(action)
        return true
    }

    static boolean runAction2(Map params) {
        ShortcutSelector ss = new ShortcutSelector(params)
        boolean actionSeleted = ss.runAction()
        return actionSeleted
    }

    boolean runAction() {
        String firstParam = GroovyRunnerConfigurator2.getFirstParam()
        if (firstParam == ConsoleSymbols.question.s) {
            printHelp()
            return false
        }
        Object action = findAction(firstParam)
        if (action == null) {
            return false
        }
        runAction(action)
        return true
    }
}
