package net.sf.jremoterun.utilities.init.commonrunner

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.init.utils.CopyFileUtil
import net.sf.jremoterun.utilities.java11.sep1.Java11ModuleAccessF
import net.sf.jremoterun.utilities.java11.sep1.Java11ModuleAllAccess

@CompileStatic
class CommonRunner {

    public static Date creationDate = new Date()
    public static File GR_HOME
    public static boolean useSystemClassLoader = 'true'.equalsIgnoreCase(System.getProperty(JrrRunnerProperties.jrrcasspathAddToSystemClassLoader));
    public static URLClassLoader cll
    public static List<String> args
    public static Object initialScript2
    public static Runnable commonRunner2 = new CommonRunner2();


    void jrrRunScript(Object initialScript, String[] args3) {
        initialScript2 = initialScript
        assert args3 != null
        args = new ArrayList<>(args3.toList());
        GR_HOME = CopyFileUtil.detectBaseDir()
        CopyFileUtil.runCustomizer()
        Java11ModuleAccessF.disableJavaModuleCheckIfNeeded();
        commonRunner2.run()
    }


}
