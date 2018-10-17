package net.sf.jremoterun.utilities.groovystarter

import groovy.transform.CompileStatic

@CompileStatic
class JrrStarterVariables2 {

    public static File userHome = new File(System.getProperty("user.home"));
    public File jrrConfigDir2;
    public File filesDir;
    public File classesDir;
    public List<Integer> debugFlags = [];

    @Deprecated
    static File jrrConfigDir2Debug;

    public static final Object lock = new Object();
    public static volatile JrrStarterVariables2 instance1;

    public static JrrStarterVariables2 getInstance(){
        if(instance1==null){
            synchronized (lock) {
                if(instance1==null) {
                    JrrStarterVariables2 instance2 = new JrrStarterVariables2();
                    instance2.init()
                    instance1 = instance2
                }
            }
        }
        return instance1
    }


    File detectHomeFromWindows() {
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

    File detectJrrConfig2Dir() {
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
        File config2DirL2 = new File(userHome, JrrStarterConstatnts.jrrConfig2DirLocationFromTxtFile);
        if (config2DirL2.exists()) {
            debugFlags.add 13;
            return new File(config2DirL2.text.trim())
        }
        File homeFromWindows = detectHomeFromWindows()
        if (homeFromWindows != null) {
            File config2DirUH = new File(homeFromWindows, JrrStarterConstatnts.jrrConfig2Dir);
            debugFlags.add 4;
            if (config2DirUH.exists()) {
                debugFlags.add 5;
                return config2DirUH;
            }
            File config2DirL3 = new File(homeFromWindows, JrrStarterConstatnts.jrrConfig2DirLocationFromTxtFile);
            if (config2DirL3.exists()) {
                debugFlags.add 14;
                return new File(config2DirL3.text.trim())
            }
        }
        debugFlags.add 6;

        return null;
    }



    void init() {
        jrrConfigDir2Debug = detectJrrConfig2Dir();
        if (jrrConfigDir2Debug != null && jrrConfigDir2Debug.exists()) {
            debugFlags.add 7;
            jrrConfigDir2 = jrrConfigDir2Debug;
            if(jrrConfigDir2==null){
                throw new NullPointerException('jrrConfigDir2 is null')
            }
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
        debugFlags.add 14;
    }


}
