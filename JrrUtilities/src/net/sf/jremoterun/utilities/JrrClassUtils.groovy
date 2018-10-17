package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.callerclass.CallerClassIgnore
import net.sf.jremoterun.callerclass.GetCallerClassS
import net.sf.jremoterun.utilities.classpath.ClRef

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method;

//import net.sf.jremoterun.utilities.groovystarter.JdkLogFormatter;
import java.lang.reflect.Modifier
import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
public class JrrClassUtils {
    private static final Logger log = Logger.getLogger(JrrClassUtils.class.getName());

    public static Map<Class, Field[]> isReadOnlyFields = new HashMap();
    public static Method getFieldAccessorMethod;
    public static Method getFieldAccessorMethod21;
    public static Method getOverrideFieldAccessor21;
    public static Field overrideField21;
    public static Field modifiersField;
    public static String mainMethodName = 'main';

    /**
     * Classes, which will be ignored during searching caller class.
     */

    public static HashSet<String> ignoreClassesForCurrentClass = CallerClassIgnore.ignoreClassesForCurrentClass;


    static void addIgnoreClass(Class clazz) {
        assert clazz != Class
        ignoreClassesForCurrentClass.add(clazz.getName())
    }

    public static StringBuilder printExceptionWithoutIgnoreClasses(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t);
        sb.append("\n");
        sb.append(printExceptionWithoutIgnoreClasses2(t).toString());
        return sb;
    }

    public static StringBuilder printExceptionWithoutIgnoreClasses2(Throwable t) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = t.getStackTrace();
        boolean stackTraceFound = false;
        for (StackTraceElement te : stackTrace) {
            if (checkIfStackTraceMatched(te)) {
                sb.append("\tat ");
                sb.append(te);
                sb.append("\n");
                stackTraceFound = true;
            }
        }
        if (!stackTraceFound) {
            for (StackTraceElement te : stackTrace) {
                String lcassName = te.getClassName();
                sb.append("\tat ");
                sb.append(te);
                sb.append("\n");
            }
        }
        return sb;
    }

    static boolean checkIfStackTraceMatched(StackTraceElement te) {
        String lcassName = te.getClassName();
        for (String ignore : JrrClassUtils.ignoreClassesForCurrentClass) {
            if (lcassName.startsWith(ignore)) {
                return false
            }
        }
        return true
    }


    public static Field findModifiersField() throws NoSuchFieldException {
        if (modifiersField == null) {
            modifiersField = findField(Field, "modifiers");
            modifiersField.setAccessible(true);
            try {
                try {
                    getFieldAccessorMethod = findMethodByParamTypes3(Field, "getFieldAccessor", Object);
                    getFieldAccessorMethod.setAccessible(true);
                }catch(java.lang.NoSuchMethodException e3){
                    log.log(Level.FINE, "can't find field modifiers 10", e3);
                    getFieldAccessorMethod21  = findMethodByParamTypes3(Field, "getFieldAccessor");
                    getFieldAccessorMethod21.setAccessible(true)
                    getOverrideFieldAccessor21  = findMethodByParamTypes3(Field, "getOverrideFieldAccessor");
                    getOverrideFieldAccessor21.setAccessible(true)
                    overrideField21  = findField(Field, "override");
                    overrideField21.setAccessible(true)
                }catch(Exception e3){
                    log.log(Level.SEVERE, "can't find field modifiers12 ${e3.getClass().getName()}", e3);
                    getFieldAccessorMethod21  = findMethodByParamTypes3(Field, "getFieldAccessor");
                    getFieldAccessorMethod21.setAccessible(true)
                    getOverrideFieldAccessor21  = findMethodByParamTypes3(Field, "getOverrideFieldAccessor");
                    getOverrideFieldAccessor21.setAccessible(true)
                    overrideField21  = findField(Field, "override");
                    overrideField21.setAccessible(true)
                }
            } catch (java.lang.NoSuchMethodException e) {
                log.log(Level.SEVERE, "can't find field modifiers30", e);
            } catch (Exception e) {
                log.log(Level.SEVERE, "can't find field modifiers31", e);
            }
        }
        return modifiersField;
    }

    public static Field[] findReadOnlyAndFinalFields(Class class1) throws NoSuchFieldException {
        Field[] fields = isReadOnlyFields.get(class1);
        if (fields == null) {
            Field isReadOnlyField = null;
            Field isFinalField = null;
            try {
                isReadOnlyField = JrrClassUtils.findField(class1, "isReadOnly");
                isReadOnlyField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                log.fine("Can't find isReadOnly field in class ${class1.getName()}");
            }
            try {
                isFinalField = JrrClassUtils.findField(class1, "isFinal");
                isFinalField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                log.fine("Can't find isFinal field in class ${class1.getName()}");
            }
            fields = [isReadOnlyField, isFinalField];
            isReadOnlyFields.put(class1, fields);
        }
        return fields;
    }

    /**
     * Second parameter (onInvoke) can be null.
     */
    public static void setFinalFieldValue(Object onInvoke, Field field, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException {


        field.setAccessible(true);
        prepareFinalField(field, onInvoke);
        try {
            field.set(onInvoke, fieldValue);
        } finally {
            returnFinalField(field, onInvoke);
        }

    }

    public static void prepareFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        findModifiersField();
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        try {
            field.setAccessible(true);
//            println("set ${field.name}")
            if (!isFinalFieldInternal(field, onValue) ) {
                if(getFieldAccessorMethod != null) {
                    Object overrideFieldAccessor = getFieldAccessorMethod.invoke(field, onValue);
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
                if(getFieldAccessorMethod21 != null) {
                    boolean  override = overrideField21.get(field)
                    Object overrideFieldAccessor
                    if(override) {
                        overrideFieldAccessor = getFieldAccessorMethod21.invoke(field);
                    }else{
                        overrideFieldAccessor = getOverrideFieldAccessor21.invoke(field);
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

    public static boolean isFinalFieldInternal(Field field, Object onValue) {
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

    public static void returnFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        findModifiersField();
        try {
            if (!isFinalFieldInternal(field, onValue) && getFieldAccessorMethod != null) {
                Object overrideFieldAccessor = getFieldAccessorMethod.invoke(field, onValue);
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
        modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
    }

    public static void setFieldValueR(ClRef clRef, Object onObjectOrClass, final String fieldName, Object fieldValue){
        setFieldValue(onObjectOrClass,fieldName,fieldValue)
    }

    public static void setFieldValue(Object onObjectOrClass, final String fieldName, Object fieldValue)
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
    public static <T> Constructor<T> findContructor(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        return findConstructorByCount(clazz,numberOfParams)
    }

    public static <T> Constructor<T> findConstructorByCount(ClRef clazz, final int numberOfParams){
        return  (Constructor)findConstructorByCount(clazz.loadClass2(),numberOfParams);
    }

    public static <T> Constructor<T> findConstructorByCount(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null when finding : ${numberOfParams}")
        }
        for (final Constructor method : JrrClassUtils.getDeclaredConstructors(clazz)) {
            final Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == numberOfParams) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new NoSuchMethodException(clazz.getName() + " number of args " + numberOfParams);
    }

    public static volatile boolean getDeclaredConstructors0Inited = false
    public static volatile Method getDeclaredConstructors0M;


    static Constructor[] getDeclaredConstructors(Class clazz) {
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


    public static volatile boolean getDeclaredFields0Inited = false
    public static volatile Method getDeclaredFields0M;


    static Field[] getDeclaredFields(Class clazz) {
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

    static Method getDeclaredFields0(){
        return findMethodByCount(Class, "getDeclaredFields0", 1)
    }


    public static volatile boolean getDeclaredMethodsInited = false
    public static volatile Method getDeclaredMethodsM;

    static Method[] getDeclaredMethods(Class clazz) {
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

    public static Method findMethodByCount(ClRef clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return findMethodByCount(clazz.loadClass2(),methodName,numberOfParams)
    }


        /**
     * Find first method in <code>clazz</code> with specified
     * <code>methodName</code> and specified <code>numberOfParams</code>. Use
     * also methods from {@link org.springframework.util.ReflectionUtils}
     */
    public static Method findMethodByCount(Class clazz,
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
    public static Method findMethod(Class clazz, final String methodName, final int numberOfParams)
            throws NoSuchMethodException {
        return findMethodByCount(clazz, methodName, numberOfParams);
    }

    public static void runMainMethod(final ClRef clazz, final String[] args) throws Exception {
        runMainMethod(clazz.loadClass2(),args)
    }

    public static void runMainMethod(final Class clazz, final String[] args) throws Exception {
        final Method mainMethod = clazz.getMethod(mainMethodName, String[]);
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
    public static <T> Constructor<T> findConstructor(Class<T> clazz, final Object... params)
            throws NoSuchMethodException {
        for (final Constructor method : JrrClassUtils.getDeclaredConstructors(clazz)) {
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

    static boolean checkIfConstuctorMatched(Constructor method, final Object... params) {
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


    public static Method findMethodByParamTypes1(ClRef clazz, final String methodName, final Object... params){
        findMethodByParamTypes1(clazz.loadClass2(),methodName,params)
    }

    public static Method findMethodByParamTypes1(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException {
        return findMethodByParamTypes2(clazz, methodName, params);
    }

    public static Method findMethodByParamTypes2(ClRef clazz, final String methodName, final Object[] params) {
        findMethodByParamTypes2(clazz.loadClass2(),methodName,params)
    }

    public static Method findMethodByParamTypes2(Class clazz, final String methodName, final Object[] params) {
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

    public static Method findMethodByParamTypes3(ClRef clazz, final String methodName, final Class... params)throws NoSuchMethodException {
        findMethodByParamTypes4(clazz.loadClass2(), methodName, params)
    }

    public static Method findMethodByParamTypes3(Class clazz, final String methodName, final Class... params)
            throws NoSuchMethodException {
        findMethodByParamTypes4(clazz, methodName, params)
    }

    public static Method findMethodByParamTypes4(ClRef clazz, final String methodName, final Class[] params)
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
    public static Method findMethodByParamTypes4(Class clazz, final String methodName, final Class[] params)
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
            for (Object object : params) {
                paramsAsString += "${object},".toString();
            }
        }
        throw new NoSuchMethodException(clazzOrig.getName() + " " + methodName + " " + paramsAsString);
    }

    @Deprecated
    public static Method findMethod(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException {
        return findMethodByParamTypes1(clazz, methodName, params)
    }

    static boolean checkIfMethodParamsMatched(Method method, final Class... params) {
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

    static Object invokeConstructor(ClRef clazz, final Object... params) {
        return invokeConstructor((Class) clazz.loadClass2(), params)
    }

    public static <T> T invokeConstructor(Class<T> clazz, final Object... params) throws Exception {
        Constructor<T> method = findConstructor(clazz, params);
        try {
            return method.newInstance(params);
        } catch (InvocationTargetException e) {
            JrrUtils.throwCause(e);
            throw new Error();
        }
    }

//    static Object invokeJavaMethod(Object object, final String methodName, final Object... params) throws Exception {

    @Deprecated
    static Object invokeMethod(Object object, final String methodName, final Object... params) throws Exception {
        return invokeJavaMethod(object, methodName, params);
    }


    static Object invokeJavaMethodR(ClRef clRef, Object object, final String methodName, final Object... params) throws Exception {
        invokeJavaMethod(object,methodName,params)
    }

    static Object invokeJavaMethod(Object object, final String methodName, final Object... params) throws Exception {
        return invokeJavaMethod2(object, methodName, params)
    }

    static Object invokeJavaMethod2R(ClRef clRef, Object object, final String methodName, final Object[] params) throws Exception {
        invokeJavaMethod2(object,methodName,params)
    }

    static Object invokeJavaMethod2(Object object, final String methodName, final Object[] params) throws Exception {
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

    /**
     * Copy class thru coping fields. Class toInstance must be subtype or the
     * same type of fromInstance.
     */
    public final static <T> void makeClone(final T fromInstance, final T toInstance) throws IllegalAccessException {
        Class class1 = fromInstance.getClass();
        while (class1 != Object) {
            makeClone(fromInstance, toInstance, class1);
            class1 = class1.getSuperclass();
        }
    }

    /**
     * Copy class thru coping fields.
     */
    public static <T> void makeClone(final T fromInstance, final T toInstance, final Class clazz)
            throws IllegalAccessException {
        for (final Field field : getDeclaredFields(clazz)) {
            final int modif = field.getModifiers();
            if (!Modifier.isStatic(modif)) {
                field.setAccessible(true);
                final Object value = field.get(fromInstance);
                field.set(toInstance, value);
            }
        }
    }

    /**
     * Classes may be different types.
     */
    public final static <T> void makeCloneDiffClassloaders(final T fromInstance, final T toInstance)
            throws IllegalAccessException {
        Class class1 = fromInstance.getClass();
        Class class2 = toInstance.getClass();
        while (class1 != Object) {
            if (class1 == class2) {
                makeClone(fromInstance, toInstance, class2);
            } else {
                if (class1.getName().equals(class2.getName())) {
                    makeCloneDiffClassloaders(fromInstance, toInstance, class1, class2);
                } else {
                    throw new IllegalStateException(
                            "Different class names " + class1.getName() + " " + class2.getName());
                }

            }
            class1 = class1.getSuperclass();
            class2 = class2.getSuperclass();
        }
    }


    public static void checkInstanceOf(final Object fromInstance, Class fromInstanceClazz) {
        if (!fromInstanceClazz.isInstance(fromInstance)) {
            throw new IllegalArgumentException("instance is not instance of class' : instance class = " + fromInstance.getClass().getName() + ", instance cl = " + fromInstance.getClass().getClassLoader() + ", class = " + fromInstanceClazz.getName() + ", class cl = " + fromInstanceClazz.getClassLoader());
        }
    }

    /**
     * coping fields values of specified class from first to second bean. This
     * method should be used, when beans are defined in different class loaders.
     */
    public static <T> void makeCloneDiffClassloaders(final T fromInstance, final T toInstance,
                                                     final Class fromInstanceClazz,
                                                     final Class toInstanceClazz) throws IllegalAccessException {
        checkInstanceOf(fromInstance, fromInstanceClazz);
        checkInstanceOf(toInstance, toInstanceClazz);
        final Map<String, Field> toFieldsNameMap = new HashMap();
        for (final Field field : getDeclaredFields(toInstanceClazz)) {
            toFieldsNameMap.put(field.getName(), field);
        }
        for (final Field fromField : getDeclaredFields(fromInstanceClazz)) {
            final int modif = fromField.getModifiers();
            if (!Modifier.isStatic(modif)) {
                fromField.setAccessible(true);

                final Object value = fromField.get(fromInstance);
//                log.info(" name=" + fromField.getName() + ", v=" + value + ", class=" + value.getClass().getName());
                if (value != null && value.getClass().getName() == "groovy.lang.MetaClassImpl" && "metaClass".equals(fromField.getName())) {
                    log.fine("skip groovy field");
                } else {
                    final Field toField = toFieldsNameMap.get(fromField.getName());
                    if (toField == null) {
                        log.severe("field not found for class " + fromInstanceClazz + " fields " + fromField.getName());
                    } else {
                        toField.setAccessible(true);
                        toField.set(toInstance, value);
                    }
                }
            }
        }
    }

    static Field findField(ClRef clazzInit, final String fieldName) {
        return findField(clazzInit.loadClass2(), fieldName)
    }

    /**
     * Find first declared field in class
     */
    public static Field findField(Class clazzInit, final String fieldName) throws NoSuchFieldException {
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

    public static volatile boolean isCheckForSpaces = true

    public static void checkForSpaces(String fieldName) {
        if (isCheckForSpaces) {
            if (fieldName.startsWith(' ')) {
                throw new IllegalArgumentException("Start with space ${fieldName}")
            }
            if (fieldName.endsWith(' ')) {
                throw new IllegalArgumentException("Ends with space ${fieldName} .")
            }
        }
    }

    static Object getFieldValueR(ClRef clRef, Object object, final String fieldName){
        return getFieldValue(object,fieldName)
    }

    /**
     * Find first declared field in class and receives value
     */
    static Object getFieldValue(Object object, final String fieldName)
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

    /**
     * Return calling class stack: first element is class where this method is
     * called. Last element is class where current thread is created. Classes
     * associated with java.lang.reflect.Method.invoke() are ignored.
     */
    /*
    public static ArrayList<Class> buildClassStack() {
        final ArrayList<Class> result = new ArrayList();
        int i = 2;
        Class clazz = Reflection.getCallerClass(i);
        while (clazz != null) {
            result.add(clazz);
            clazz = Reflection.getCallerClass(i);
            i++;
        }
        return result;
    }

     */

    public static Logger getJdkLogForCurrentClass() {
//		Thread.dumpStack();
        Class currentClass = getCurrentClass();
        return Logger.getLogger(currentClass.getName());
    }

    public static Class getCurrentClass() {
        return getCurrentClassImpl();
    }

    private static Class getCurrentClassImpl() {
        return GetCallerClassS.getCallerClassImpl().getCallerClass();
    }

    static Class getCurrentClassImpl4(int depth) {
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

    static boolean checkIfClassIgnored(String name) {
        for (String ignore : ignoreClassesForCurrentClass) {
            if (name.startsWith(ignore)) {
                return true
            }
        }
        return false
    }


    public static File getResourceFromCurrentClassLoader(String resource) {
        ClassLoader classLoader = getCurrentClassImpl().getClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("classloader is null");
        }
        URL resource1 = classLoader.getResource(resource);
        if (!resource1.toString().startsWith("file:/")) {
            throw new IllegalArgumentException("Failed resolve resource " + resource + " " + resource1);
        }
        File file2 = UrlToFileConverter.c.convert resource1;
        if (!file2.exists()) {
            throw new IllegalArgumentException("Failed resolve resource " + resource + " " + resource1);
        }
        return file2;


    }

    public static ClassLoader getCurrentClassLoader() {
        return getCurrentClassImpl().getClassLoader();
    }


    static URLClassLoader getCurrentClassLoaderUrl() {
        return (URLClassLoader) getCurrentClassImpl().getClassLoader();
    }

    static GroovyClassLoader getCurrentClassLoaderGroovy() {
        return (GroovyClassLoader) getCurrentClassImpl().getClassLoader();
    }

    static Object[] getEnumValues(ClRef clazzEnum){
        return getEnumValues(clazzEnum.loadClass2()) ;
    }

    static Object[] getEnumValues(Class clazzEnum){
        if(!clazzEnum.isEnum()){
            throw new IllegalArgumentException("Class is not enum ${clazzEnum}")
        }
        Object[] values = invokeJavaMethod(clazzEnum, 'values') as Object[]
        return values
    }

}
