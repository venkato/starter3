package net.sf.jremoterun.utilities.groovystarter.st.str2obj
import java.lang.reflect.Type;

interface StringToObjectConverterI {

    Object convert(String str,Type genericArg);
}