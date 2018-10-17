package net.sf.jremoterun.utilities.init.v2

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class JrrV2Settings {

    public static boolean setSystemClassLoader = true;

    public static boolean setClassLoaderForSwingThread = true


    public static String showExceptionInSwingWindow = "exception.swing";

    public
    static boolean showExceptionInSwingWindowValue = "true".equalsIgnoreCase(System.getProperty(showExceptionInSwingWindow));




}
