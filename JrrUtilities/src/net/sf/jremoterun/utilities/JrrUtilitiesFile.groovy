package net.sf.jremoterun.utilities

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils

import javax.management.ObjectName
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.Map.Entry
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

@CompileStatic
public class JrrUtilitiesFile {

    private static final Logger log = Logger.getLogger(JrrUtilitiesFile.class.getName());

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


    public static <T extends Serializable> T serializeDesirialize(T obj, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        return (T) JrrUtils.deserialize(JrrUtils.serialize(obj), classLoader);
    }



    public static Object deserializeObjectFromFile(File file) throws IOException, ClassNotFoundException {
        final FileInputStream inStream = new FileInputStream(file);
        try {
            final ObjectInputStream in2 = new ObjectInputStream(inStream);
            return in2.readObject();
        } finally {
            inStream.close();
        }
    }

}
