package net.sf.jremoterun.utilities.groovystrans

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import org.codehaus.groovy.classgen.asm.InvocationWriter
import org.codehaus.groovy.classgen.asm.MethodCaller
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

import java.lang.reflect.Constructor
import java.util.logging.Logger

@CompileStatic
class JrrCastRuntime implements Runnable {

    private static final Logger log = Logger.getLogger(JrrCastRuntime.getName());

    @Override
    void run() {
        f1()
    }

    static void f1() {
        MethodCaller setField2 = MethodCaller.newStatic(JrrCastRuntime, "asType")
        JrrClassUtils.setFieldValue(InvocationWriter, "asTypeMethod", setField2);
    }

    static Object asType(Object object, Class type) throws Throwable {
        try {
            return ScriptBytecodeAdapter.asType(object, type)
        } catch (GroovyCastException e) {
            log.fine "${e}"
            Object result = asType2(object, type)
            if (result == null) {
                throw e
            }
            return result
        }
    }

    static Object asType2(Object object, Class type) throws Throwable {
        if (object instanceof String) {
            String s = (String) object;
            try {
                Constructor constructor = type.getConstructor(String)
                return constructor.newInstance(s)
            } catch (NoSuchMethodException e2) {
                log.fine "${e2}"

            }
        }
        if (object instanceof GString) {
            GString s = (GString) object;
            try {
                Constructor constructor = type.getConstructor(String)
                return constructor.newInstance(s.toString())
            } catch (NoSuchMethodException e2) {
                log.fine "${e2}"
            }
        }
        return null
    }


}
