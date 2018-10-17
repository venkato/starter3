package net.sf.jremoterun.utilities.groovystarter;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JrrStarterConstatnts {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String configFileName = 'jrrgroovyconfig.groovy'
    public static String rawConfigFileName = 'jrrgroovyconfig_raw.groovy'
    public static String rawConfiGrHomeFileName = 'jrrgroovyconfig_grhome_raw.groovy'
    public static String jrrConfigDir = 'jrr/configs'
    public static String jrrConfigDirWindowsAllUsers = 'c:/Users/All Users/jrr'
    public static String jrrConfigDirWindowsHomeDrive = 'HOMEDRIVE'
    public static String jrrConfigDirWindowsHomePath = 'HOMEPATH'
    public static String jrrConfigDirLinuxAllUsers = '/etc/jrr'
    public static String jrrConfig2DirSystemProperty = 'jrrconfigs2dir'
    public static String jrrConfig2Dir = 'jrr/configs2/'
    public static String jrrConfig2DirLocationFromTxtFile = 'jrrconfigdirlocation.txt'
//    public static String jrrConfig2Raw = 'files/jrrgroovyconfig_raw.groovy'
//    public static String jrrConfig2File = 'files/jrrgroovyconfig.groovy'
    public static String jrrConfig2Files = 'files/'
    public static String jrrConfig2Classes = 'classes/'

}
