package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class JrrStarterVariables {

    public static File userHome = new File(System.getProperty("user.home"));
    public static File jrrConfigDir2;
    public static File filesDir;
    public static File classesDir;
    public static List<Integer> debugFlags = [];

    @Deprecated
    public static File jrrConfigDir2Debug;


    static File detectHomeFromWindows() {
        String homeDrive = System.getenv(JrrStarterConstatnts.jrrConfigDirWindowsHomeDrive)
        if (homeDrive == null) {
            debugFlags.add 51;
            return null
        }
        String homePath = System.getenv(JrrStarterConstatnts.jrrConfigDirWindowsHomePath)
        if (homePath == null) {
            debugFlags.add 52;
            return null
        }
        File homeDriveF = new File(homeDrive)
        if(!homeDriveF.exists()){
            debugFlags.add 53;
            return null
        }
        File homeFromWindows = new File(homeDriveF,homePath)
        if (homeFromWindows.exists()) {
            return homeFromWindows
        }
        debugFlags.add 54;
        return null;
    }

    static File detectJrrConfig2Dir() {
        String config2DirS = System.getProperty(JrrStarterConstatnts.jrrConfig2DirSystemProperty)
        if (config2DirS != null) {
            debugFlags.add 1;
            return new File(config2DirS)
        }
        String config2DirVarS = System.getenv(JrrStarterConstatnts.jrrConfig2DirSystemProperty)
        if (config2DirVarS != null) {
            debugFlags.add 2;
            return new File(config2DirVarS)
        }
        File config2DirUH2 = new File(userHome, JrrStarterConstatnts.jrrConfig2Dir);
        if (config2DirUH2.exists()) {
            debugFlags.add 3;
            return config2DirUH2;
        }
        File homeFromWindows = detectHomeFromWindows()
        if (homeFromWindows != null) {
            File config2DirUH = new File(homeFromWindows, JrrStarterConstatnts.jrrConfig2Dir);
            debugFlags.add 4;
            if (config2DirUH.exists()) {
                debugFlags.add 5;
                return config2DirUH;
            }
        }
        debugFlags.add 6;
        return null;
    }

    static {
        jrrConfigDir2Debug = detectJrrConfig2Dir();
        if (jrrConfigDir2Debug != null && jrrConfigDir2Debug.exists()) {
            debugFlags.add 7;
            jrrConfigDir2 = jrrConfigDir2Debug;
            File filesDir2 = new File(jrrConfigDir2, JrrStarterConstatnts.jrrConfig2Files)
            if (filesDir2.exists()) {
                filesDir = filesDir2;
                debugFlags.add 8;
            }
            File classesDir2 = new File(jrrConfigDir2, JrrStarterConstatnts.jrrConfig2Classes)
            if (classesDir2.exists()) {
                classesDir = classesDir2;
                debugFlags.add 9;
            }
            debugFlags.add 10;

        }
    }


}
