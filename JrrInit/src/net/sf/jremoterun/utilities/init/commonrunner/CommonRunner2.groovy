package net.sf.jremoterun.utilities.init.commonrunner

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.init.onejar.OneJarClassPathBuilder
import net.sf.jremoterun.utilities.init.utils.CopyFileUtil
import net.sf.jremoterun.utilities.init.v2.InitClassPathBuilder

//import static net.sf.jremoterun.utilities.init.commonrunner.CommonRunner.*;


@CompileStatic
class CommonRunner2 implements Runnable {

    public static String runner = 'net.sf.jremoterun.utilities.init.v2.JrrInitV2';
    public static boolean isOneJar = false;
    public static Runnable afterInit
    public static boolean isUsedJavaw

    @Override
    void run() {
        CopyFileUtil.copyLibs CommonRunner.GR_HOME
//        assert args.size() > 0
//        detectIsOneJar();
        assert CommonRunner.args.size() > 0
        isUsedJavaw = usedJavaw2(CommonRunner.args[0])
        CommonRunner.args.remove(0)
        if (CommonRunner.cll == null) {
            CommonRunner.cll = detectCLassLoader();
        }
        if (isOneJar) {
            new OneJarClassPathBuilder().addDirs()
        } else {
            new InitClassPathBuilder().addDirs()
        }
        if (afterInit != null) {
            afterInit.run()
        }
        Class runnerLoaded = CommonRunner.cll.loadClass(runner)
        RunnerInterface groovyMethodRunner = (RunnerInterface) runnerLoaded.newInstance();
        groovyMethodRunner.jrrRunScript()
    }

    URLClassLoader detectCLassLoader() {
        URLClassLoader cll
        if (CommonRunner.useSystemClassLoader) {
            isOneJar = true
            cll = ClassLoader.getSystemClassLoader() as URLClassLoader
        } else {
            cll = this.getClass().classLoader as GroovyClassLoader;
        }
        return cll;
    }

    boolean usedJavaw2(String arg) {
        byte b = arg as byte
        switch (b) {
            case JrrRunnerProperties.javaUsed:
                return false
            case JrrRunnerProperties.javawUsed:
                return true;
            default:
                throw new IllegalStateException("wrong arg : ${arg}")

        }
    }


}
