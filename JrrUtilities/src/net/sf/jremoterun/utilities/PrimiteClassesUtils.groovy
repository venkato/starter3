package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic

@CompileStatic
public class PrimiteClassesUtils {

    public static final List<Class> primitiveArraysClasses = [int[],
                                                              long[], boolean[], char[], short[],
                                                              byte[], float[], double[],];

    public static final List<Class> primitiveClasses = [int,
                                                        long, boolean, char, short,
                                                        byte, float, double, void,];

    public static final List<Class> primitiveWrapperClasses = [Integer,
                                                               Long, Boolean, Character, Short,
                                                               Byte, Float, Double, Void,];

    /**
     * Contains internal java names of primitive classes: I, J, Z, C, S, B, F,
     * D, V
     */
    public static final List<String> primitiveClassesShortName;

    /**
     * Contains internal java names of primitive array classes: [I, [J, [Z, [C,
     * [S, [B, [F, [D
     */
    public static final List<String> primitiveClassesArraysShortName;

    /**
     * Contains java names of primitive classes: int, long, boolean, char,
     * short, byte, float, double, void
     */
    public static final List<String> primitiveClassesNames;

    static {
        final List<String> primitiveClassHumanNameList1 = new ArrayList<String>();

        int i = 0;
        for (final Class clazz : primitiveClasses) {
            primitiveClassHumanNameList1.add(clazz.getName());
            i++;
        }
        primitiveClassesNames = Collections
                .unmodifiableList(primitiveClassHumanNameList1);

        final List<String> primitiveClassesShortName2 = new ArrayList<String>();
        final List<String> primitiveClassesArraysShortName2 = new ArrayList<String>();
        for (Class class1 : primitiveArraysClasses) {
            Class nativeClass = class1.getComponentType();
            String s = class1.getName();
            primitiveClassesArraysShortName2.add(s);
            String s2 = s.substring(1);
            primitiveClassesShortName2.add(s2);
        }
        primitiveClassesShortName2.add("V");
        primitiveClassesShortName = Collections
                .unmodifiableList(primitiveClassesShortName2);
        primitiveClassesArraysShortName = primitiveClassesArraysShortName2;
    }

    public static Class loadPrimitiveClass(final String className) {
        int i = PrimiteClassesUtils.primitiveClassesNames.indexOf(className);
        if (i != -1) {
            return PrimiteClassesUtils.primitiveClasses.get(i);
        }
        i = PrimiteClassesUtils.primitiveClassesShortName
                .indexOf(className);
        if (i != -1) {
            return PrimiteClassesUtils.primitiveClasses.get(i);
        }

        i = PrimiteClassesUtils.primitiveClassesArraysShortName
                .indexOf(className);
        if (i != -1) {
            return PrimiteClassesUtils.primitiveArraysClasses.get(i);
        }
        return null;

    }

    public static Class primitiveToWrapper(Class class1) {
        int i = primitiveClasses.indexOf(class1);
        if (i == -1) {
            return null;
        }
        return primitiveWrapperClasses.get(i);
    }

    public static Class wrapperToPrimitive(Class class1) {
        int i = primitiveWrapperClasses.indexOf(class1);
        if (i == -1) {
            return null;
        }
        return primitiveClasses.get(i);
    }

}
