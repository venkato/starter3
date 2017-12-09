package net.sf.jremoterun.utilities.groovystarter;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JrrStarterConstatnts {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String configFileName = 'jrrgroovyconfig.groovy'
    public static String rawConfigFileName = 'jrrgroovyconfig_raw.groovy'
    public static String jrrConfigDir = 'jrr/configs'
    public static String jrrConfigDirWindowsAllUsers = 'c:/Users/All Users/jrr'
    public static String jrrConfigDirLinuxAllUsers = '/etc/jrr'

}
