package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic;
import net.sf.jremoterun.ICodeForExecuting;
import net.sf.jremoterun.JrrUtils;
import sun.awt.AppContext;
import sun.misc.Unsafe;
import sun.reflect.Reflection;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.security.Provider;
import java.security.Security;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@CompileStatic
public class JrrUtilities3 {

    public static final String domainName = "JRemoteRun";

    public static final Pattern numberPattern = Pattern.compile("\\d*");

    /**
     * Display if java runtime >= 1.5
     */
    public static final boolean java5;

    public static Runnable runnableCode;

    public static ICodeForExecuting runnableCodeRich;

    public static Method classLoaderAddUrlMethod;

    private static Unsafe javaUnsafe;

    private static final Logger log = Logger.getLogger(JrrUtilities3.class.getName());

    public static Map<String, Set<String>> securityServices;

    public static Constructor<HashMap> constructorHashMap;
    public static Constructor<ConcurrentHashMap> constructorConcurrentHashMap;
    public static Constructor<TreeSet> constructorTreeSet;
    public static Constructor<ArrayList> constructorArrayList;
    public static Constructor<HashSet> constructorHashSet;

    // this field is null for openjdk 6
    public static Object rootTableKey;
    public static Map<ThreadGroup, AppContext> threadGroup2appContext;

    static {
        final String version = System.getProperty("java.specification.version");
        java5 = !"1.4".equals(version);
        try {
            classLoaderAddUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            classLoaderAddUrlMethod.setAccessible(true);
        } catch (final Exception e) {
            // may throw security exception
            log.log(Level.FINE, null, e);
        }
        try {
            final Field threadGroup2appContextField = AppContext.class.getDeclaredField("threadGroup2appContext");
            threadGroup2appContextField.setAccessible(true);
            threadGroup2appContext = (Map) threadGroup2appContextField.get(null);
            final Class class1 = Class.forName("javax.swing.SystemEventQueueUtilities");
            final Field rootTableKeyField = class1.getDeclaredField("rootTableKey");
            rootTableKeyField.setAccessible(true);
            rootTableKey = rootTableKeyField.get(null);
        } catch (final Exception e) {
            // TODO correctly catch headless exception
            log.log(Level.FINE, null, e);
        }
        try {
            constructorHashMap = HashMap.class.getConstructor();
            constructorConcurrentHashMap = ConcurrentHashMap.class.getConstructor();
            constructorHashSet = HashSet.class.getConstructor();
            constructorTreeSet = TreeSet.class.getConstructor();
            constructorArrayList = ArrayList.class.getConstructor();
        } catch (final Exception e) {
            throw new Error(e);
        }
    }

    public static Object findJvmObject() throws NoSuchFieldException, IllegalAccessException {
        RuntimeMXBean compilationMXBean = ManagementFactory.getRuntimeMXBean();
        Object jvm = JrrClassUtils.getFieldValue(compilationMXBean, "jvm");
        return jvm;
    }


    public static Object getAwtAppContexts() {
        final Collection<AppContext> contexts = threadGroup2appContext.values();
        if (contexts.size() == 1) {
            return contexts.iterator().next();
        }
        return contexts;
    }

    /**
     * Methods works since java 6, due to Window#getWindows method
     */
    public static Collection<Window> findAwtWindows() {
        if (rootTableKey == null) {
            log.fine("rootTableKey is null");
            return Arrays.asList(Window.getWindows());
        }
        HashSet<Window> windows = new HashSet<Window>();
        Collection<AppContext> appContexts = threadGroup2appContext.values();
        for (AppContext context : appContexts) {

            final Map awtComponentsMap = (Map) context.get(rootTableKey);
            if (awtComponentsMap == null) {
                log.info("awp map is null");
            } else {
                final Collection windowList = awtComponentsMap.keySet();
                windows.addAll((Collection)windowList);
            }
        }
        return windows;
    }

    public static Collection<Window> findVisibleAwtWindows() {
        final Collection windowList = findAwtWindows();
        final Collection<Window> result = new HashSet();
        if (windowList == null) {
            log.info("can't find windows");
            return result;
        }
        for (final Object object : windowList) {
            if (object instanceof Window) {
                final Window window = (Window) object;
                if (window.isVisible()) {
                    result.add(window);
                }
            }
        }
        return result;
    }


    public static JFrame showException(final String title, final Throwable throwable) {
        final JFrame dialog = new JFrame(throwable.getClass().getSimpleName());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                final Container rootContentPane = dialog.getContentPane();
                rootContentPane.setLayout(new BorderLayout());
                rootContentPane.add(new JLabel(" " + title), BorderLayout.NORTH);
                final String exceptionS = JrrUtils.exceptionToString(throwable);
                final JTextArea textArea = new JTextArea(exceptionS);
                textArea.setEditable(false);
                rootContentPane.add(new JScrollPane(textArea), BorderLayout.CENTER);
                final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                dialog.setLocation(screenSize.width / 2 as int , screenSize.height / 2 as int );
                dialog.setSize(200, 200);
                // dialog.setModal(true);
                if (java5) {
                    dialog.setAlwaysOnTop(true);
                }
                dialog.setVisible(true);
            }
        });
        return dialog;
    }

    public static void addFileToClassLoader(final URLClassLoader classLoader, File file)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
        file = file.getAbsoluteFile();
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        classLoaderAddUrlMethod.invoke(classLoader, file.toURI().toURL());
    }

    /**
     * Create statistics for object names
     *
     * @return as Map<Domain, Map<Key, Map<Value, Set<ObjectName>>>>
     */
    public static Map<String, Map<String, Map<String, Set<ObjectName>>>> buildMBeansInfo(
            final Collection<ObjectName> set) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final Map<String, Map<String, Map<String, Set<ObjectName>>>> map = new HashMap();
        for (final ObjectName objectName : set) {
            final String domain = objectName.getDomain();
            final Map<String, Map<String, Set<ObjectName>>> domainBy = buildObject(map, domain, constructorHashMap);
            final Set<Entry<String, String>> nameAndValuePairs = objectName.getKeyPropertyList().entrySet();
            for (final Entry<String, String> nameAndValue : nameAndValuePairs) {
                final Map<String, Set<ObjectName>> keyBy = buildObject(domainBy, nameAndValue.getKey(),
                        constructorHashMap);
                final Set<ObjectName> valueBy = buildObject(keyBy, nameAndValue.getValue(), constructorHashSet);
                valueBy.add(objectName);
            }
        }
        return map;
    }

    public static Map<String, Map<String, Map<String, Set<ObjectName>>>> buildMBeansInfoReadable(
            final Collection<ObjectName> set) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final Map<String, Map<String, Map<String, Set<ObjectName>>>> r1 = buildMBeansInfo(set);
        removeCollection(r1);
        return r1;
    }

    /**
     * Iterate on each values and if value is collection and this collection
     * contains only one element, replace value of map. E.g Map<Key , Collection
     * <SomeObject>> and collection has one element, then transforms to Map<Key
     * , SomeObject>.
     */
    public static void removeCollection(final Map map) {
        final Set<Entry> set = map.entrySet();
        for (final Entry entry : set) {
            final Object value = entry.getValue();
            if (value instanceof Collection) {
                final Collection new_name = (Collection) value;
                if (new_name.size() == 1) {
                    final Object value1 = new_name.iterator().next();
                    map.put(entry.getKey(), value1);
                }
            } else if (value instanceof Map) {
                removeCollection((Map) value);
            }
        }
    }

    /**
     * @see #buildObject(Map, Object, Constructor)
     */
    public static <K, V> V buildObjectNoEx(final Map<K, V> map, final K key, final Constructor constructor) {
        try {
            return buildObject(map, key, constructor);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for build constructions Map&lt;ArrayList&gt; Map&lt;ArrayList&gt;.
     * In this case use ArrayList value =
     * buildObject(someMap,"key",JrrBeanMaker.constructorArrayList). For
     * constructors see {@link net.sf.jremoterun.utilities.jrrbean.JrrBeanMaker}
     */
    public static <K, V> V buildObject(final Map<K, V> map, final K key, final Constructor constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        V value = map.get(key);
        if (value == null) {
            value = (V) constructor.newInstance();
            map.put(key, value);
        }
        return value;
    }

    public static Class getCurrentClass() {
        return Reflection.getCallerClass(2);
    }

    public static ClassLoader getCurrentClassLoader() {
        return Reflection.getCallerClass(2).getClassLoader();
    }

    public static LinkedHashSet<URL> getInitClassPath() {
        final URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        final LinkedHashSet<URL> hashSet = new LinkedHashSet();
        hashSet.addAll(Arrays.asList(classLoader.getURLs()));
        return hashSet;
    }

    public static Unsafe getJavaUnsafe() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        if (javaUnsafe == null) {
            javaUnsafe = (Unsafe) JrrClassUtils.findField(Unsafe.class, "theUnsafe").get(null);
        }
        return javaUnsafe;
    }

    public static String convertCharset(final String string, final Charset charset1, final Charset charset2)
            throws UnsupportedEncodingException {
        final byte[] bs = string.getBytes(charset1.toString());
        return new String(bs, charset2.toString());
    }

    public static void checkFileExist(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File is null");
        }
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new FileNotFoundException("Unable read file : " + file.getAbsoluteFile().getCanonicalPath());
        }
    }



}
