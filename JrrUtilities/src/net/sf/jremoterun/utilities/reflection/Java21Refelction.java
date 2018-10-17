package net.sf.jremoterun.utilities.reflection;


import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Java21Refelction {

    public static void f1(Object onInvoke, Field field, Object fieldValue) throws Throwable {
        java.lang.reflect.Member member1 = (java.lang.reflect.Member) JrrClassUtilsFindMethodFields. memberNameConstructor.newInstance(field, true);
        byte kind1 =(Byte)  JrrClassUtilsFindMethodFields. getReferenceKindMethod.invoke(member1) ;

        MethodHandle methodHandle = (MethodHandle) JrrClassUtilsFindMethodFields. getDirectFieldNoSecurityManagerMethod.invoke( JrrClassUtilsFindMethodFields. lookupJava21, kind1, field.getDeclaringClass(), member1);
        if(Modifier.isStatic(field.getModifiers())){
            methodHandle.invoke(fieldValue);
        }else {
            methodHandle.invoke(onInvoke, fieldValue);
        }
    }
}
