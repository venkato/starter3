package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic;
import net.sf.jremoterun.ICodeForExecuting;
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.utilities.classpath.ClRef;
import sun.awt.AppContext;
import sun.misc.Unsafe

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @see net.sf.jremoterun.utilities.JrrUtilities extends this class
 */
@Deprecated
@CompileStatic
public class JrrUtilities3 {

    public static final String domainName = "JRemoteRun";

//    public static final Pattern numberPattern = Pattern.compile("\\d*");
//
//    public static Runnable runnableCode;
//
//    public static ICodeForExecuting runnableCodeRich;

    public static Method classLoaderAddUrlMethod;

    private static Unsafe javaUnsafe;

    private static final Logger log = Logger.getLogger(JrrUtilities3.class.getName());

    public static Map<String, Set<String>> securityServices;

    @Deprecated
    public static Constructor<HashMap> constructorHashMap;

    @Deprecated
    public static Constructor<ConcurrentHashMap> constructorConcurrentHashMap;

    @Deprecated
    public static Constructor<TreeSet> constructorTreeSet;

    @Deprecated
    public static Constructor<ArrayList> constructorArrayList;

    @Deprecated
    public static Constructor<HashSet> constructorHashSet;

    static void initUtilsConstructors(){
        if(constructorHashMap!=null) {
            initUtilsConstructors1()
        }
    }

    static void initUtilsConstructors1(){
            constructorHashMap = HashMap.class.getConstructor();
            constructorConcurrentHashMap = ConcurrentHashMap.class.getConstructor();
            constructorHashSet = HashSet.class.getConstructor();
            constructorTreeSet = TreeSet.class.getConstructor();
            constructorArrayList = ArrayList.class.getConstructor();

    }

    static {
        try {
            classLoaderAddUrlMethod = JrrClassUtils.findMethodByParamTypes3(URLClassLoader,"addURL", URL);
            classLoaderAddUrlMethod.setAccessible(true);
        } catch (final Exception e) {
            // may throw security exception
            log.log(Level.WARNING, null, e);
        }
        try {
            initUtilsConstructors()
        } catch (final Exception e) {
            throw new Error(e);
        }
    }

    public static Object findJvmObject() throws NoSuchFieldException, IllegalAccessException {
        RuntimeMXBean compilationMXBean = ManagementFactory.getRuntimeMXBean();
        Object jvm = JrrClassUtils.getFieldValueR(new ClRef('sun.management.RuntimeImpl'), compilationMXBean, "jvm");
        return jvm;
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

                dialog.setAlwaysOnTop(true);

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
        initUtilsConstructors1()
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
