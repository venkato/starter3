package net.sf.jremoterun.utilities.reflection

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

@CompileStatic
interface JrrClassUtilsFindMethodI2 {

    public Field findModifiersField() throws NoSuchFieldException

    public Field[] findReadOnlyAndFinalFields(Class class1) throws NoSuchFieldException

    public void setFinalFieldValue(Object onInvoke, Field field, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException

    public void prepareFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException

    public boolean isFinalFieldInternal(Field field, Object onValue)

    public void returnFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException

    public void setFieldValueR(ClRef clRef, Object onObjectOrClass, final String fieldName, Object fieldValue)

    public void setFieldValue(Object onObjectOrClass, final String fieldName, Object fieldValue)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException

    public <T> Constructor<T> findContructor(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException


    public <T> Constructor<T> findConstructorByCount(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException

    public <T> Constructor<T> findConstructorByCount(ClRef clazz, final int numberOfParams)


    public <T> Constructor<T> findConstructorByCountR(ClRef clRef, Class<T> clazz, final int numberOfParams)

    Constructor[] getDeclaredConstructors(Class clazz)

    Field[] getDeclaredFields(Class clazz)

    Method getDeclaredFields0()


    Method[] getDeclaredMethods(Class clazz)



    public Method findMethodByCount(ClRef clazz,
                                    final String methodName,
                                    final int numberOfParams) throws NoSuchMethodException

    public Method findMethodByCountR(ClRef clRef, Class clazz,
                                     final String methodName,
                                     final int numberOfParams) throws NoSuchMethodException

    public Method findMethodByCount(Class clazz,
                                    final String methodName,
                                    final int numberOfParams) throws NoSuchMethodException


    public Method findMethod(Class clazz, final String methodName, final int numberOfParams)
            throws NoSuchMethodException

    public void runMainMethod(final ClRef clazz, final String[] args) throws Exception

    public void runMainMethod(final Class clazz, final String[] args) throws Exception


    public <T> Constructor<T> findConstructor(Class<T> clazz, final Object... params)
            throws NoSuchMethodException

    boolean checkIfConstuctorMatched(Constructor method, final Object... params)


    public Method findMethodByParamTypes1(ClRef clazz, final String methodName, final Object... params)



    public Method findMethodByParamTypes1(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException

    public Method findMethodByParamTypes2(ClRef clazz, final String methodName, final Object[] params)

    public Method findMethodByParamTypes2(Class clazz, final String methodName, final Object[] params)


    public Method findMethodByParamTypes3(ClRef clazz, final String methodName, final Class... params)throws NoSuchMethodException

    public Method findMethodByParamTypes3(Class clazz, final String methodName, final Class... params)
            throws NoSuchMethodException

    public Method findMethodByParamTypes4(ClRef clazz, final String methodName, final Class[] params)
            throws NoSuchMethodException

    public Method findMethodByParamTypes4(Class clazz, final String methodName, final Class[] params)
            throws NoSuchMethodException

    public Method findMethod(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException

    boolean checkIfMethodParamsMatched(Method method, final Class... params)


    Object invokeConstructor(ClRef clazz, final Object... params)

    public <T> T invokeConstructorR(ClRef clRef, Class<T> clazz, final Object... params) throws Exception

    public <T> T invokeConstructor(Class<T> clazz, final Object... params) throws Exception


    @Deprecated
    Object invokeMethod(Object object, final String methodName, final Object... params) throws Exception


    Object invokeJavaMethodR(ClRef clRef, Object object, final String methodName, final Object... params) throws Exception

    Object invokeJavaMethod(Object object, final String methodName, final Object... params) throws Exception

    Object invokeJavaMethod2R(ClRef clRef, Object object, final String methodName, final Object[] params) throws Exception
    Object invokeJavaMethod2(Object object, final String methodName, final Object[] params) throws Exception

    public Field findField(Class clazzInit, final String fieldName) throws NoSuchFieldException

    Field findField(ClRef clazzInit, final String fieldName)

    Object getFieldValue(Object object, final String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException



    Object[] getEnumValues(ClRef clazzEnum)

    Object[] getEnumValues(Class clazzEnum)
}
