package net.sf.jremoterun.utilities.groovystarter.st.str2obj

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

import java.lang.reflect.Array
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Type
import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
class StringToObjectConverter {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static StringToObjectConverter defaultConverter = new StringToObjectConverter();

    public String listSeparator = ','
    public String nullObject = 'null'

    public Map<Class, StringToObjectConverterI> customConverters = [:]


    Object convertFromStringToType(String str, Class toType, Type genericArg) {
        if (isObjectNull(str, toType)) {
            return null;
        }
        StringToObjectConverterI customConverter = customConverters.get(toType)
        if (customConverter != null) {
            Object result = customConverter.convert(str, genericArg)
            return result
        }
        return convertFromStringToType2(str, toType, genericArg)
    }

    Object convertFromStringToType2(String str, Class toType, Type genericArg) {
        switch (toType) {
            case String:
                return str
            case boolean:
            case Boolean:
                return convertToBoolean(str)
            case { toType.isArray() }:
                return convertFromStringToArray(str, toType)
            case File:
                return convertToFile(str)
            default:
                return convertFromStringToTypeDefault(str, toType, genericArg)
        }

    }

    Object convertFromStringToTypeDefault(String str, Class toType, Type genericArg) {
        try {
            return str.asType(toType);
        } catch (GroovyCastException e) {
            log.log(Level.FINE, "${toType.name}", e)
            Constructor constructor;
            try {
                constructor = toType.getConstructor(String)
            } catch (NoSuchMethodException e2) {
                throw new IllegalArgumentException("failed create ${toType.name} from ${str}")
            } catch (Exception e2) {
                log.log(Level.FINE, "${toType.name}", e2)
                throw e;
            }
            try {
                return constructor.newInstance(str)
            } catch (InvocationTargetException e3) {
                throw e3.cause;
            }
        }
    }


    boolean isObjectNull(String str, Class toType) {
        return str == nullObject
    }

    List<String> trueList = ['true','t','y']
    List<String> falseList = ['false','f','n']

    boolean convertToBoolean(String str) {
        if(trueList.contains(str)){
            return true
        }
        if(falseList.contains(str)){
            return false
        }
        String lowerCase= str.toLowerCase()
        if(trueList.contains(lowerCase)){
            return true
        }
        if(falseList.contains(lowerCase)){
            return false
        }
        throw new IllegalArgumentException("Failed cast : ${str} to boolean");
    }

    File convertToFile(String str) {
        return new File(str).getCanonicalFile().getAbsoluteFile()
    }

    Object convertFromStringToArray(String str, Class toType) {
        Class elementType = toType.getComponentType()
        List<String> tokenize = str.tokenize(listSeparator)
        List<Object> list = tokenize.collect { convertFromStringToType(it, elementType, null) }
        return convertListToArray(list, elementType);
    }

    static Object convertListToArray(List list, Class elementType) {
        int size = list.size();
        Object array = Array.newInstance(elementType, size);

        int idx = 0;
        for (Iterator iter = list.iterator(); iter.hasNext(); idx++) {
            Object element = iter.next();
            Array.set(array, idx, element);
        }
        return array;
    }


}
