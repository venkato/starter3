package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.ICodeForExecuting
import net.sf.jremoterun.JrrUtils
import sun.misc.Unsafe

import javax.management.ObjectName
import javax.swing.*
import java.awt.*
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.nio.charset.Charset
import java.util.Map.Entry
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern


@CompileStatic
public class JrrUtilitiesMapBuilder {

    private static final Logger log = Logger.getLogger(JrrUtilitiesMapBuilder.class.getName());

    public static Constructor<HashMap> constructorHashMap;

    public static Constructor<ConcurrentHashMap> constructorConcurrentHashMap;

    public static Constructor<TreeSet> constructorTreeSet;

    public static Constructor<ArrayList> constructorArrayList;

    public static Constructor<HashSet> constructorHashSet;

    static void initUtilsConstructors(){
        if(constructorHashMap==null) {
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
            initUtilsConstructors()
        } catch (final Exception e) {
            throw new Error(e);
        }
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
     * constructors see {@link net.sf.jremoterun.utilities.nonjdk.javassist.jrrbean.JrrBeanMaker}
     */
    public static <K, V> V buildObject(final Map<K, V> map, final K key, final Constructor constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        assert constructor!=null
        V value = map.get(key);
        if (value == null) {
            value = (V) constructor.newInstance();
            map.put(key, value);
        }
        return value;
    }



}
