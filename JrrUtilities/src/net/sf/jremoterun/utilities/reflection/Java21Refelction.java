package net.sf.jremoterun.utilities.reflection;

import net.sf.jremoterun.utilities.JrrClassUtils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class Java21Refelction {

    public static void f1(Object onInvoke, Field field, Object fieldValue) throws Throwable {
        java.lang.reflect.Member member1 = (java.lang.reflect.Member) JrrClassUtils. memberNameConstructor.newInstance(field, true);
        byte kind1 =(Byte)  JrrClassUtils. getReferenceKindMethod.invoke(member1) ;

        MethodHandle methodHandle = (MethodHandle) JrrClassUtils. getDirectFieldNoSecurityManagerMethod.invoke( JrrClassUtils. lookupJava21, kind1, field.getDeclaringClass(), member1);
        if(Modifier.isStatic(field.getModifiers())){
            methodHandle.invoke(fieldValue);
        }else {
            methodHandle.invoke(onInvoke, fieldValue);
        }
    }
}
