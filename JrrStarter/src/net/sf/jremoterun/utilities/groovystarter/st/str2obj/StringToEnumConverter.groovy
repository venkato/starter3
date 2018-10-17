package net.sf.jremoterun.utilities.groovystarter.st.str2obj

import groovy.transform.CompileStatic;

import java.lang.reflect.Type;

@CompileStatic
public class StringToEnumConverter {


    Enum convertToEnum(String str, Class toType, Type genericArg) {
        return Enum.valueOf(toType, str);
    }

}
