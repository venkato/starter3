package net.sf.jremoterun.utilities.reflection

import groovy.transform.CompileStatic

@CompileStatic
public class JrrReflection {

    public static JrrClassUtilsFindMethodI2 f1;


    static {
        f1 = JrrClassUtilsFindMethodRef.clRef1.newInstance3() as JrrClassUtilsFindMethodI2
    }


}
