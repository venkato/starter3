package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.callerclass.CallerClassIgnore
import net.sf.jremoterun.callerclass.GetCallerClassS
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.reflection.JrrReflection

import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method;

//import net.sf.jremoterun.utilities.groovystarter.JdkLogFormatter;
import java.lang.reflect.Modifier
import java.util.logging.Logger

@CompileStatic
public class JrrClassUtils {
    private static final Logger log = Logger.getLogger(JrrClassUtils.class.getName());

//    @Deprecated
//    public static Map<Class, Field[]> isReadOnlyFields = new HashMap();
//    @Deprecated
//    public static Method getFieldAccessorMethod;
//    @Deprecated
//    public static Method getFieldAccessorMethod21;
//    @Deprecated
//    public static Method getOverrideFieldAccessor21;
//    @Deprecated
//    public static Method getReferenceKindMethod;
//    @Deprecated
//    public static Method getDirectFieldNoSecurityManagerMethod;
//    @Deprecated
//    public static Field overrideField21;
//    @Deprecated
//    public static Field modifiersField;
//
//    @Deprecated
//    public static java.lang.invoke.MethodHandles.Lookup lookupJava21
//    public static Field lookupField;
    public static String mainMethodName = 'main';

    /**
     * Classes, which will be ignored during searching caller class.
     */

    public static HashSet<String> ignoreClassesForCurrentClass = CallerClassIgnore.ignoreClassesForCurrentClass;
    public static Constructor memberNameConstructor


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
        return JrrReflection.f1.findModifiersField()
    }

    public static Field[] findReadOnlyAndFinalFields(Class class1) throws NoSuchFieldException {
        return JrrReflection.f1.findReadOnlyAndFinalFields(class1)
    }

    /**
     * Second parameter (onInvoke) can be null.
     */
    public static void setFinalFieldValue(Object onInvoke, Field field, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException {
        JrrReflection.f1.setFinalFieldValue(onInvoke,field,fieldValue)
    }

    public static void prepareFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        JrrReflection.f1.prepareFinalField(field,onValue)
    }

    public static boolean isFinalFieldInternal(Field field, Object onValue) {
        return JrrReflection.f1.isFinalFieldInternal(field,onValue)
    }

    public static void returnFinalField(Field field, Object onValue)
            throws NoSuchFieldException, IllegalAccessException {
        JrrReflection.f1.returnFinalField(field,onValue)
    }

    public static void setFieldValueR(ClRef clRef, Object onObjectOrClass, final String fieldName, Object fieldValue){
        setFieldValue(onObjectOrClass,fieldName,fieldValue)
    }

    public static void setFieldValue(Object onObjectOrClass, final String fieldName, Object fieldValue)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        JrrReflection.f1.setFieldValue(onObjectOrClass,fieldName,fieldValue)
    }

    @Deprecated
    public static <T> Constructor<T> findContructor(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        return findConstructorByCount(clazz,numberOfParams) //NOFIELDCHECK
    }

    public static <T> Constructor<T> findConstructorByCount(ClRef clazz, final int numberOfParams){
        return  (Constructor)findConstructorByCount(clazz.loadClass2(),numberOfParams);
    }

    public static <T> Constructor<T> findConstructorByCountR(ClRef clRef,Class<T> clazz, final int numberOfParams){
        return findConstructorByCount(clazz,numberOfParams) //NOFIELDCHECK
    }
    public static <T> Constructor<T> findConstructorByCount(Class<T> clazz, final int numberOfParams)
            throws NoSuchMethodException {
        return JrrReflection.f1.findConstructorByCount(clazz,numberOfParams)
    }

    public static volatile boolean getDeclaredConstructors0Inited = false
    public static volatile Method getDeclaredConstructors0M;


    static Constructor[] getDeclaredConstructors(Class clazz) {
        return JrrReflection.f1.getDeclaredConstructors(clazz)
    }


    public static volatile boolean getDeclaredFields0Inited = false
    public static volatile Method getDeclaredFields0M;


    static Field[] getDeclaredFields(Class clazz) {
        return JrrReflection.f1.getDeclaredFields(clazz)
    }

    static Method getDeclaredFields0(){
        return findMethodByCount(Class, "getDeclaredFields0", 1)
    }


    public static volatile boolean getDeclaredMethodsInited = false
    public static volatile Method getDeclaredMethodsM;

    static Method[] getDeclaredMethods(Class clazz) {
        return JrrReflection.f1.getDeclaredMethods(clazz)

    }

    public static Method findMethodByCount(ClRef clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return findMethodByCount(clazz.loadClass2(),methodName,numberOfParams)
    }

    public static Method findMethodByCountR(ClRef clRef, Class clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return findMethodByCount(clazz,methodName,numberOfParams)
    }

        /**
     * Find first method in <code>clazz</code> with specified
     * <code>methodName</code> and specified <code>numberOfParams</code>. Use
     * also methods from {@link org.springframework.util.ReflectionUtils}
     */
    public static Method findMethodByCount(Class clazz,
                                           final String methodName,
                                           final int numberOfParams) throws NoSuchMethodException {
        return JrrReflection.f1.findMethodByCount(clazz,methodName,numberOfParams)
    }

    @Deprecated
    public static Method findMethod(Class clazz, final String methodName, final int numberOfParams)
            throws NoSuchMethodException {
        return findMethodByCount(clazz, methodName, numberOfParams);
    }

    public static void runMainMethod(final ClRef clazz, final String[] args) throws Exception {
        runMainMethod(clazz.loadClass2(),args) //NOFIELDCHECK
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
        return JrrReflection.f1.findConstructor(clazz,params)
    }

    static boolean checkIfConstuctorMatched(Constructor method, final Object... params) {
        return JrrReflection.f1.checkIfConstuctorMatched(method,params)
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
        return JrrReflection.f1.findMethodByParamTypes2(clazz,methodName,params)
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
        return JrrReflection.f1.findMethodByParamTypes4(clazz,methodName,params)
    }

    @Deprecated
    public static Method findMethod(Class clazz, final String methodName, final Object... params)
            throws NoSuchMethodException {
        return findMethodByParamTypes1(clazz, methodName, params)
    }

    static boolean checkIfMethodParamsMatched(Method method, final Class... params) {
        return JrrReflection.f1.checkIfMethodParamsMatched(method,params)
    }

    static Object invokeConstructor(ClRef clazz, final Object... params) {
        return invokeConstructor((Class) clazz.loadClass2(), params) //NOFIELDCHECK
    }

    public static <T> T invokeConstructorR(ClRef clRef, Class<T> clazz, final Object... params) throws Exception {
        return invokeConstructor(clazz,params) //NOFIELDCHECK
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
        return JrrReflection.f1.invokeJavaMethod2(object,methodName,params)
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
        return JrrReflection.f1.findField(clazzInit,fieldName)
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
        return JrrReflection.f1.getFieldValue(object,fieldName)
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
        Object[] values = invokeJavaMethod(clazzEnum, 'values') as Object[] //NOFIELDCHECK
        return values
    }

}
