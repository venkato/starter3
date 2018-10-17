package net.sf.jremoterun.utilities.reflection

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.callerclass.GetCallerClassS
import net.sf.jremoterun.utilities.PrimiteClassesUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import java.lang.invoke.MethodHandles
import java.lang.reflect.*

//import net.sf.jremoterun.utilities.groovystarter.JdkLogFormatter;

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
public class JrrClassUtilsFindMethod implements JrrClassUtilsFindMethodI2{
    private static final Logger log = Logger.getLogger(JrrClassUtilsFindMethod.class.getName());

//    public Map<Class, Field[]> isReadOnlyFields = new HashMap();
//    public Method getFieldAccessorMethod;
//    public Method getFieldAccessorMethod21;
//    public Method getOverrideFieldAccessor21;
//    public Method getReferenceKindMethod;
//    public Method getDirectFieldNoSecurityManagerMethod;
//    public Field overrideField21;
//    public Field modifiersField;
////    public Field lookupField;
//    public String mainMethodName = 'main';
//    public java.lang.invoke.MethodHandles.Lookup lookupJava21
//
//    public Constructor memberNameConstructor




    @Override
    public Field findModifiersField() throws NoSuchFieldException {
        if (JrrClassUtilsFindMethodFields.modifiersField == null) {
            JrrClassUtilsFindMethodFields.modifiersField = findField(Field, "modifiers");
            JrrClassUtilsFindMethodFields.modifiersField.setAccessible(true);
            try {
                try {
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod = findMethodByParamTypes3(Field, "getFieldAccessor", Object);
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod.setAccessible(true);
                }catch(java.lang.NoSuchMethodException e3){
                    log.log(Level.FINE, "can't find field modifiers 10", e3);
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod21  = findMethodByParamTypes3(Field, "getFieldAccessor"); //NOFIELDCHECK
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod21.setAccessible(true)
                    JrrClassUtilsFindMethodFields.getOverrideFieldAccessor21  = findMethodByParamTypes3(Field, "getOverrideFieldAccessor"); //NOFIELDCHECK
                    JrrClassUtilsFindMethodFields.getOverrideFieldAccessor21.setAccessible(true)
                    JrrClassUtilsFindMethodFields.overrideField21  = findField(Field, "override");
                    JrrClassUtilsFindMethodFields.overrideField21.setAccessible(true)
                }catch(Exception e3){
                    log.log(Level.SEVERE, "can't find field modifiers12 ${e3.getClass().getName()}", e3);
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod21  = findMethodByParamTypes3(Field, "getFieldAccessor"); //NOFIELDCHECK
                    JrrClassUtilsFindMethodFields.getFieldAccessorMethod21.setAccessible(true)
                    JrrClassUtilsFindMethodFields.getOverrideFieldAccessor21  = findMethodByParamTypes3(Field, "getOverrideFieldAccessor"); //NOFIELDCHECK
                    JrrClassUtilsFindMethodFields.getOverrideFieldAccessor21.setAccessible(true)
                    JrrClassUtilsFindMethodFields.overrideField21  = findField(Field, "override");
                    JrrClassUtilsFindMethodFields.overrideField21.setAccessible(true)
                }

                try {
                    JrrClassUtilsFindMethodFields.memberNameConstructor = findConstructorByCount(new ClRef("java.lang.invoke.MemberName"), 2);
                    JrrClassUtilsFindMethodFields.memberNameConstructor.setAccessible(true);
                    JrrClassUtilsFindMethodFields.getReferenceKindMethod = findMethodByCount(new ClRef("java.lang.invoke.MemberName"), "getReferenceKind", 0)
//                    lookupField = findField(java.lang.invoke.MethodHandles.Lookup, "IMPL_LOOKUP")
                    JrrClassUtilsFindMethodFields.getDirectFieldNoSecurityManagerMethod = findMethodByCount(new ClRef(java.lang.invoke.MethodHandles.Lookup), "getDirectFieldNoSecurityManager", 3)

                    JrrClassUtilsFindMethodFields.lookupJava21 = (MethodHandles.Lookup) getFieldValue(java.lang.invoke.MethodHandles.Lookup, "IMPL_LOOKUP");
                }catch(ClassNotFoundException e3){
                    log.log(Level.SEVERE, "can't find field java21 1", e3);
                }catch(java.lang.NoSuchMethodException e3){
                    log.log(Level.SEVERE, "can't find field java21 2", e3);
                }
            } catch (java.lang.NoSuchMethodException e) {
                log.log(Level.SEVERE, "can't find field modifiers30", e);
            } catch (Exception e) {
                log.log(Level.SEVERE, "can't find field modifiers31", e);
            }
        }
        return JrrClassUtilsFindMethodFields.modifiersField;
    }

    public Field[] findReadOnlyAndFinalFields(Class class1) throws NoSuchFieldException {
        Field[] fields = JrrClassUtilsFindMethodFields.isReadOnlyFields.get(class1);
        if (fields == null) {
            Field isReadOnlyField = null;
            Field isFinalField = null;
            try {
                isReadOnlyField = findField(class1, "isReadOnly"); //NOFIELDCHECK
                isReadOnlyField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                log.fine("Can't find isReadOnly field in class ${class1.getName()}");
            }
            try {
                isFinalField = findField(class1, "isFinal"); //NOFIELDCHECK
                isFinalField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                log.fine("Can't find isFinal field in class ${class1.getName()}");
            }
            fields = [isReadOnlyField, isFinalField];
            JrrClassUtilsFindMethodFields.isReadOnlyFields.put(class1, fields);
        }
        return fields;
    }

    /**
     * Second parameter (onInvoke) can be null.
     */
    public void setFinalFieldValue(Object onInvoke, Field field, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        findModifiersField();
        if(JrrClassUtilsFindMethodFields.getDirectFieldNoSecurityManagerMethod!=null){
            try {
                Java21Refelction.f1(onInvoke,field,fieldValue);
//                java.lang.reflect.Member member1 = (java.lang.reflect.Member) memberNameConstructor.newInstance(field, true)
//                byte kind1 = getReferenceKindMethod.invoke(member1) as byte
//
//                MethodHandle methodHandle = getDirectFieldNoSecurityManagerMethod.invoke(lookupJava21, kind1, field.getDeclaringClass(), member1) as MethodHandle
//                if(Modifier.isStatic(field.getModifiers())){
//                    methodHandle.invoke(fieldValue)
//                }else {
//                    methodHandle.invoke(onInvoke, fieldValue)
//                }

                return
            }catch (Throwable e){
                log.log(Level.SEVERE,"failed set ${field.getDeclaringClass().getName()} ${field.getName()}",e)
                e.printStackTrace()
            }
        }
            prepareFinalField(field, onInvoke);
            try {
                field.set(onInvoke, fieldValue);
            } finally {
                returnFinalField(field, onInvoke);
            }


    }

    public void prepareFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        findModifiersField();
        JrrClassUtilsFindMethodFields.modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        try {
            field.setAccessible(true);
//            println("set ${field.name}")
            if (!isFinalFieldInternal(field, onValue) ) {
                if(JrrClassUtilsFindMethodFields.getFieldAccessorMethod != null) {
                    Object overrideFieldAccessor = JrrClassUtilsFindMethodFields.getFieldAccessorMethod.invoke(field, onValue);
                    if (overrideFieldAccessor == null) {
                        log.info("overrideFieldAccessor is null " + field.getName());
                    } else {
                        Field[] fields = findReadOnlyAndFinalFields(overrideFieldAccessor.getClass());
                        if (fields[0] != null) {
                            fields[0].set(overrideFieldAccessor, false);
                        }
                        if (fields[1] != null) {
                            // if (overrideFieldAccessor == onValue) {
                            // fields[1].set(overrideFieldAccessor, false);
                            // } else {
                            setFinalFieldValue(overrideFieldAccessor, fields[1], false);
                            // }
                        }
                    }
                }
                if(JrrClassUtilsFindMethodFields.getFieldAccessorMethod21 != null) {
                    boolean  override = JrrClassUtilsFindMethodFields.overrideField21.get(field)
                    Object overrideFieldAccessor
                    if(override) {
                        overrideFieldAccessor = JrrClassUtilsFindMethodFields.getFieldAccessorMethod21.invoke(field);
                    }else{
                        overrideFieldAccessor = JrrClassUtilsFindMethodFields.getOverrideFieldAccessor21.invoke(field);
                    }
                    if (overrideFieldAccessor == null) {
                        log.info("overrideFieldAccessor is null ${override} " + field.getName());
                    } else {
                        Field[] fields = findReadOnlyAndFinalFields(overrideFieldAccessor.getClass());
                        Field f0 =  fields[0];
                        if (f0 != null) {
                            log.info("setting field ${f0} ${overrideFieldAccessor.getClass().getName()}")
                            f0.set(overrideFieldAccessor, false);
                        }
                        Field f1 =  fields[0];
                        if (f1 != null) {
                            // if (overrideFieldAccessor == onValue) {
                            // fields[1].set(overrideFieldAccessor, false);
                            // } else {
                            log.info("setting field ${f1} ${overrideFieldAccessor.getClass().getName()}")
                            setFinalFieldValue(overrideFieldAccessor, f1, false);
                            // }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "${field.getClass()} ${field.getName()}", e);
        }
    }

    public boolean isFinalFieldInternal(Field field, Object onValue) {
//        log.info "field from class : ${field.getDeclaringClass()}"
//        jdk.internal.reflect.UnsafeFieldAccessorImpl
        String pkgName = field.getDeclaringClass().getPackage().getName();
        if ('sun.reflect' == pkgName || pkgName == 'jdk.internal.reflect') {
            if ('isFinal' == field.getName()) {
                return true;
            }
        }
        return false;
    }

    public void returnFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        findModifiersField();
        try {
            if (!isFinalFieldInternal(field, onValue) && JrrClassUtilsFindMethodFields.getFieldAccessorMethod != null) {
                Object overrideFieldAccessor = JrrClassUtilsFindMethodFields.getFieldAccessorMethod.invoke(field, onValue);
                if (overrideFieldAccessor != null) {
                    Field[] fields = findReadOnlyAndFinalFields(overrideFieldAccessor.getClass());
                    if (fields[0] != null) {
                        fields[0].set(overrideFieldAccessor, true);
                    }
                    if (fields[1] != null) {
                        setFinalFieldValue(overrideFieldAccessor, fields[1], true);
                    }
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "${field.getClass()} ${field.getName()}", e);
        }
        JrrClassUtilsFindMethodFields.modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
    }

    public void setFieldValueR(ClRef clRef, Object onObjectOrClass, final String fieldName, Object fieldValue){
        setFieldValue(onObjectOrClass,fieldName,fieldValue)
    }

    public void setFieldValue(Object onObjectOrClass, final String fieldName, Object fieldValue)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (onObjectOrClass == null) {
            throw new NullPointerException("object is null for field " + fieldName);
        }
        final Object onInvoke;
        final Class clazz;
        if (onObjectOrClass instanceof ClRef) {
            clazz = onObjectOrClass.loadClass2()
            onInvoke = null;
        } else if (onObjectOrClass instanceof Class) {
            clazz = (Class) onObjectOrClass;
            onInvoke = null;
        } else {
            clazz = onObjectOrClass.getClass();
            onInvoke = onObjectOrClass;
        }

        Field field = findField(clazz, fieldName);
        if (onInvoke == null && !Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("field is not static, but onInvoke is null");
        }
        boolean fieldFinal = Modifier.isFinal(field.getModifiers());
        if (fieldFinal) {
            setFinalFieldValue(onInvoke, field, fieldValue);
        } else {
            field.set(onInvoke, fieldValue);
        }
    }

    @Deprecated
    public <T> Constructor<T> findContructor(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        return findConstructorByCount(clazz,numberOfParams) //NOFIELDCHECK
    }

    public <T> Constructor<T> findConstructorByCount(ClRef clazz, final int numberOfParams){
        return  (Constructor)findConstructorByCount(clazz.loadClass2(),numberOfParams);
    }

    public <T> Constructor<T> findConstructorByCountR(ClRef clRef, Class<T> clazz, final int numberOfParams){
        return findConstructorByCount(clazz,numberOfParams) //NOFIELDCHECK
    }
    public <T> Constructor<T> findConstructorByCount(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null when finding : ${numberOfParams}")
        }
        for (final Constructor method : getDeclaredConstructors(clazz)) {
            final Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == numberOfParams) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new NoSuchMethodException(clazz.getName() + " number of args " + numberOfParams);
    }

    public volatile boolean getDeclaredConstructors0Inited = false
    public volatile Method getDeclaredConstructors0M;


    Constructor[] getDeclaredConstructors(Class clazz) {
        if (!getDeclaredConstructors0Inited) {
            getDeclaredConstructors0Inited = true
            try {
                getDeclaredConstructors0M = findMethodByCount(Class, "getDeclaredConstructors0", 1)
            } catch (NoSuchMethodException e) {
                log.log(Level.WARNING, "failed find getDeclaredConstructors0", e)
            }
//            getDeclaredMethodsM = Class.getDeclaredMethod("getDeclaredFields0", boolean.class)
        }
        if (getDeclaredConstructors0M == null) {
            return clazz.getDeclaredConstructors()
        }
        return getDeclaredConstructors0M.invoke(clazz, false) as Constructor[]
    }


    public volatile boolean getDeclaredFields0Inited = false
    public volatile Method getDeclaredFields0M;


    Field[] getDeclaredFields(Class clazz) {
        if (!getDeclaredFields0Inited) {
            getDeclaredFields0Inited = true
            try {
                getDeclaredFields0M = getDeclaredFields0()
            } catch (NoSuchMethodException e) {
                log.log(Level.WARNING, "failed find getDeclaredFields0", e)
            }
//            getDeclaredMethodsM = Class.getDeclaredMethod("getDeclaredFields0", boolean.class)
        }
        if (getDeclaredFields0M == null) {
            return clazz.getDeclaredFields()
        }
        return getDeclaredFields0M.invoke(clazz, false) as Field[]
    }

    Method getDeclaredFields0(){
        return findMethodByCount(Class, "getDeclaredFields0", 1)
    }


    public volatile boolean getDeclaredMethodsInited = false
    public volatile Method getDeclaredMethodsM;

    Method[] getDeclaredMethods(Class clazz) {
        if (!getDeclaredMethodsInited) {
            getDeclaredMethodsInited = true
            try {
                getDeclaredMethodsM = Class.getDeclaredMethod("getDeclaredMethods0", boolean)
                getDeclaredMethodsM.setAccessible(true)
            } catch (NoSuchMethodException e) {
                log.log(Level.WARNING, "failed find getDeclaredMethods0", e)
            }
//            getDeclaredMethodsM = Class.getDeclaredMethod("getDeclaredFields0", boolean.class)
        }
        if (getDeclaredMethodsM == null) {
            return clazz.getDeclaredMethods()
        }
        return getDeclaredMethodsM.invoke(clazz, false) as Method[]

    }

    public Method findMethodByCount(ClRef clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return findMethodByCount(clazz.loadClass2(),methodName,numberOfParams)
    }

    public Method findMethodByCountR(ClRef clRef, Class clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return findMethodByCount(clazz,methodName,numberOfParams)
    }

    public Method findMethodByCount(Class clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null when finding : ${methodName} ${numberOfParams}")
        }
        Class classOriginal = clazz;
        while (true) {
            for (final Method method : getDeclaredMethods(clazz)) {
                if (method.getName().equals(methodName)) {
                    final Class[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == numberOfParams) {
                        method.setAccessible(true);
                        return method;
                    }
                }
            }
            clazz = clazz.getSuperclass();
            if (clazz == Object) {
                break;
            }
            if (clazz == null) {
                // interface
                break
            }
        }
        throw new NoSuchMethodException("Class: " + classOriginal.getName() + ", method name: " + methodName + ", params count: " + numberOfParams);
    }

    @Deprecated
    public Method findMethod(Class clazz, final String methodName, final int numberOfParams)
            throws NoSuchMethodException {
        return findMethodByCount(clazz, methodName, numberOfParams);
    }

    public void runMainMethod(final ClRef clazz, final String[] args) throws Exception {
        runMainMethod(clazz.loadClass2(),args) //NOFIELDCHECK
    }

    public void runMainMethod(final Class clazz, final String[] args) throws Exception {
        final Method mainMethod = clazz.getMethod(JrrClassUtilsFindMethodFields.mainMethodName, String[]);
        Object[] args2 = [args]
        mainMethod.invoke(null, args2);
    }

    /**
     * Find first constructor in <code>clazz</code> with specified
     * <code>params</code> can be casted to types of method parameters.
     *
     * @param clazz
     * @param params if null then find method without arguments
     * @throws NoSuchMethodException
     */
    public <T> Constructor<T> findConstructor(Class<T> clazz, final Object... params)
            throws NoSuchMethodException {
        for (final Constructor method : getDeclaredConstructors(clazz)) {
            if (checkIfConstuctorMatched(method, params)) {
                method.setAccessible(true)
                return method;
            }
        }
        String paramsAsString = "";
        if (params != null) {
            for (Object object : params) {
                paramsAsString += "${object},".toString();
            }
        }
        throw new NoSuchMethodException(clazz.getName() + " " + paramsAsString);
    }

    boolean checkIfConstuctorMatched(Constructor method, final Object... params) {
        final Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0 && (params == null || params.length == 0)) {
            return true;
        }
        if (params != null && paramTypes.length == params.length) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (params[i] != null) {
                    if (paramTypes[i].isPrimitive()) {
                        Class primitiveClass = PrimiteClassesUtils.wrapperToPrimitive(params[i].getClass());
                        if (primitiveClass != paramTypes[i]) {
                            return false
                        }
                    } else if (!paramTypes[i].isInstance(params[i])) {
                        return false
                    }
                }
            }
            return true;
        }
        return false
    }


    public Method findMethodByParamTypes1(ClRef clazz, final String methodName, final Object... params){
        findMethodByParamTypes1(clazz.loadClass2(),methodName,params)
    }

    public Method findMethodByParamTypes1(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException {
        return findMethodByParamTypes2(clazz, methodName, params);
    }

    public Method findMethodByParamTypes2(ClRef clazz, final String methodName, final Object[] params) {
        findMethodByParamTypes2(clazz.loadClass2(),methodName,params)
    }

    public Method findMethodByParamTypes2(Class clazz, final String methodName, final Object[] params) {
        Class[] params2
        if (params == null) {
            params2 = []
        } else {
            params2 = new Class[params.size()]

            for (int i = 0; i < params.length; i++) {
                Object o = params[i]
                if (o != null) {
                    params2[i] = o.getClass()
                }
            }
        }
        findMethodByParamTypes4(clazz, methodName, params2)
    }

    public Method findMethodByParamTypes3(ClRef clazz, final String methodName, final Class... params)throws NoSuchMethodException {
        findMethodByParamTypes4(clazz.loadClass2(), methodName, params)
    }

    public Method findMethodByParamTypes3(Class clazz, final String methodName, final Class... params)
            throws NoSuchMethodException {
        findMethodByParamTypes4(clazz, methodName, params)
    }

    public Method findMethodByParamTypes4(ClRef clazz, final String methodName, final Class[] params)
            throws NoSuchMethodException {
        findMethodByParamTypes4(clazz.loadClass2(),methodName,params)
    }

    /**
     * Find first method in <code>clazz</code> with specified
     * <code>methodName</code> and <code>params</code> can be casted to types of
     * method parameters.
     *
     * @param clazz
     * @param methodName
     * @param params if null then find method without arguments
     * @throws NoSuchMethodException
     */
    public Method findMethodByParamTypes4(Class clazz, final String methodName, final Class[] params)
            throws NoSuchMethodException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null when finding : ${methodName} ${params}")
        }
        checkForSpaces(methodName);
        Class clazzOrig = clazz;
        while (true) {
            for (final Method method : getDeclaredMethods(clazz)) {
                if (method.getName() == methodName) {
                    final Class[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == 0 && (params == null || params.length == 0)) {
                        method.setAccessible(true);
                        return method;
                    }
                    if (params != null && paramTypes.length == params.length) {
                        if (checkIfMethodParamsMatched(method, params)) {
                            method.setAccessible(true);
                            return method;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
            if (clazz == Object) {
                break;
            }

            if (clazz == null) {
                // interface
                break
            }
        }
        String paramsAsString = "";
        if (params != null) {
            for (Class object : params) {
                paramsAsString += "${object.getName()},".toString();
            }
        }
        throw new NoSuchMethodException(clazzOrig.getName() + " " + methodName + " " + paramsAsString);
    }

    @Deprecated
    public Method findMethod(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException {
        return findMethodByParamTypes1(clazz, methodName, params)
    }

    boolean checkIfMethodParamsMatched(Method method, final Class... params) {
        final Class[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (params[i] != null) {
                if (paramTypes[i].isPrimitive()) {
                    Class primitiveClass = PrimiteClassesUtils.wrapperToPrimitive(params[i]);
                    if (primitiveClass != paramTypes[i]) {
                        return false
                    }
                } else if (!paramTypes[i].isAssignableFrom(params[i])) {
                    return false
                }
            }
        }
        return true
    }

    Object invokeConstructor(ClRef clazz, final Object... params) {
        return invokeConstructor((Class) clazz.loadClass2(), params) //NOFIELDCHECK
    }

    public <T> T invokeConstructorR(ClRef clRef, Class<T> clazz, final Object... params) throws Exception {
        return invokeConstructor(clazz,params) //NOFIELDCHECK
    }

    public <T> T invokeConstructor(Class<T> clazz, final Object... params) throws Exception {
        Constructor<T> method = findConstructor(clazz, params);
        try {
            return method.newInstance(params);
        } catch (InvocationTargetException e) {
            JrrUtils.throwCause(e);
            throw new Error();
        }
    }

//    Object invokeJavaMethod(Object object, final String methodName, final Object... params) throws Exception {

    @Deprecated
    Object invokeMethod(Object object, final String methodName, final Object... params) throws Exception {
        return invokeJavaMethod(object, methodName, params);
    }


    Object invokeJavaMethodR(ClRef clRef, Object object, final String methodName, final Object... params) throws Exception {
        invokeJavaMethod(object,methodName,params)
    }

    Object invokeJavaMethod(Object object, final String methodName, final Object... params) throws Exception {
        return invokeJavaMethod2(object, methodName, params)
    }

    Object invokeJavaMethod2R(ClRef clRef, Object object, final String methodName, final Object[] params) throws Exception {
        invokeJavaMethod2(object,methodName,params)
    }

    Object invokeJavaMethod2(Object object, final String methodName, final Object[] params) throws Exception {
        if (object == null) {
            throw new NullPointerException("object is null for method " + methodName);
        }
        final Object onInvoke;
        final Class clazz;
        if (object instanceof ClRef) {
            clazz = object.loadClass2()
            onInvoke = null;
        } else if (object instanceof Class) {
            clazz = (Class) object;
            onInvoke = null;
        } else {
            clazz = object.getClass();
            onInvoke = object;
        }

        Method method = findMethodByParamTypes1(clazz, methodName, params);
        if (onInvoke == null && !Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("method is not static, but onInvoke is null");
        }
        try {
            return method.invoke(onInvoke, params);
        } catch (InvocationTargetException e) {
            JrrUtils.throwCause(e);
            throw new Error();
        }
    }

    public void checkInstanceOf(final Object fromInstance, Class fromInstanceClazz) {
        if (!fromInstanceClazz.isInstance(fromInstance)) {
            throw new IllegalArgumentException("instance is not instance of class' : instance class = " + fromInstance.getClass().getName() + ", instance cl = " + fromInstance.getClass().getClassLoader() + ", class = " + fromInstanceClazz.getName() + ", class cl = " + fromInstanceClazz.getClassLoader());
        }
    }


    Field findField(ClRef clazzInit, final String fieldName) {
        return findField(clazzInit.loadClass2(), fieldName)
    }

    /**
     * Find first declared field in class
     */
    public Field findField(Class clazzInit, final String fieldName) throws NoSuchFieldException {
        if (clazzInit == null) {
            throw new IllegalArgumentException("class is null when finding : ${fieldName}")
        }
        checkForSpaces(fieldName);
        Class clazz = clazzInit;
        while (true) {
            for (final Field field : getDeclaredFields(clazz)) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
            if (clazz == Object) {
                break;
            }
            if (clazz == null) {
                // that is interface
                break
            }
        }
        throw new NoSuchFieldException(fieldName + " " + clazzInit.getName());
    }

    public volatile boolean isCheckForSpaces = true

    public void checkForSpaces(String fieldName) {
        if (isCheckForSpaces) {
            if (fieldName.startsWith(' ')) {
                throw new IllegalArgumentException("Start with space ${fieldName}")
            }
            if (fieldName.endsWith(' ')) {
                throw new IllegalArgumentException("Ends with space ${fieldName} .")
            }
        }
    }

    Object getFieldValueR(ClRef clRef, Object object, final String fieldName){
        return getFieldValue(object,fieldName)
    }

    /**
     * Find first declared field in class and receives value
     */
    Object getFieldValue(Object object, final String fieldName)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (object == null) {
            throw new NullPointerException("object is null for field : " + fieldName);
        }
        final Object onInvoke;
        final Class clazz;
        if (object instanceof ClRef) {
            clazz = object.loadClass2()
            onInvoke = null;
        } else if (object instanceof Class) {
            clazz = (Class) object;
            onInvoke = null;
        } else {
            clazz = object.getClass();
            onInvoke = object;
        }
        if (clazz == null) {
            throw new IllegalStateException("failed find class for : ${object}")
        }
        Field field = findField(clazz, fieldName);
        if (onInvoke == null && !Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("field is not static, but onInvoke is null");
        }

        return field.get(onInvoke);
    }


    public Class getCurrentClass() {
        return getCurrentClassImpl();
    }

    private Class getCurrentClassImpl() {
        return GetCallerClassS.getCallerClassImpl().getCallerClass();
    }

    Class getCurrentClassImpl4(int depth) {
        return GetCallerClassS.getCallerClassI.getCallerClass();
//        int i = 1;
//        while (true) {
//            i++;
//            Class<?> callerClass = Reflection.getCallerClass(i);
//            String name = callerClass.getName();
//            if (!checkIfClassIgnored(name)) {
//                return callerClass;
//            }
//        }
    }



    URLClassLoader getCurrentClassLoaderUrl() {
        return (URLClassLoader) getCurrentClassImpl().getClassLoader();
    }

    GroovyClassLoader getCurrentClassLoaderGroovy() {
        return (GroovyClassLoader) getCurrentClassImpl().getClassLoader();
    }

    Object[] getEnumValues(ClRef clazzEnum){
        return getEnumValues(clazzEnum.loadClass2()) ;
    }

    Object[] getEnumValues(Class clazzEnum){
        if(!clazzEnum.isEnum()){
            throw new IllegalArgumentException("Class is not enum ${clazzEnum}")
        }
        Object[] values = invokeJavaMethod(clazzEnum, 'values') as Object[] //NOFIELDCHECK
        return values
    }

}
