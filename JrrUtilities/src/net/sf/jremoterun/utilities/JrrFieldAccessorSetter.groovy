package net.sf.jremoterun.utilities;

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import org.codehaus.groovy.classgen.AsmClassGenerator;
import org.codehaus.groovy.classgen.asm.MethodCallerMultiAdapter;

@CompileStatic
public class JrrFieldAccessorSetter implements Runnable{

    public static boolean inited = false;

    public static void setFieldAccessors() throws NoSuchFieldException, IllegalAccessException {
        if (!inited) {
            setFieldAccessorsImpl();
            inited = true;
        }

    }

    public static void setFieldAccessorsImpl() throws NoSuchFieldException, IllegalAccessException {
        MethodCallerMultiAdapter setField2 = MethodCallerMultiAdapter.newStatic(JrrFieldAccessorSetter, "setField2", false, false);
        JrrClassUtils.setFieldValue(AsmClassGenerator, "setField", setField2);
        JrrClassUtils.setFieldValue(AsmClassGenerator, "setGroovyObjectField", setField2);


        MethodCallerMultiAdapter getField2 = MethodCallerMultiAdapter.newStatic(JrrFieldAccessorSetter, "getField2", false, false);
        JrrClassUtils.setFieldValue(AsmClassGenerator, "getField", getField2);
        JrrClassUtils.setFieldValue(AsmClassGenerator, "getGroovyObjectField", getField2);
    }

    @Override
    public void run() {
        try {
            setFieldAccessors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Deprecated
    public static Object getField(Class senderClass, Object receiver, String messageName) throws Throwable {
        return getField2(senderClass,receiver,messageName);
    }

    /**
     * Used from groovy
     */
    @Deprecated
    public static Object getField2(Class senderClass, Object receiver, String messageName) throws Throwable {
        return JrrClassUtils.getFieldValue(receiver, messageName);
    }



    @Deprecated
    static void setField(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
        setField2(messageArgument,senderClass,receiver,messageName);
    }

    /**
     * Used from groovy
     */
    @Deprecated
    static void setField2(Object messageArgument, Class senderClass, Object receiver, String messageName) throws Throwable {
        JrrClassUtils.setFieldValue(receiver, messageName, messageArgument);
    }



}
