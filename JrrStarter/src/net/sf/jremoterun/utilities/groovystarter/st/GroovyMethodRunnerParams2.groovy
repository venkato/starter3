package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic
import net.sf.jremoterun.SharedObjectsUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.JrrStarterConstatnts
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef
import net.sf.jremoterun.utilities.st.ProxyOutputStream2

@CompileStatic
public class GroovyMethodRunnerParams2 {

    public Exception creationCallStack;

    public final Date creationDate = new Date()

    public volatile GroovyMethodRunner2 groovyMethodRunner2;

    public volatile boolean annotationParser2 = true;
    public volatile Boolean runMainJavaMethod ;
    public volatile ClassPreProcessor annotationParser3 ;
    public volatile ClassByteCodeHandler annotationParser4 ;


    public volatile File runnerGroovyFile;
    public volatile ClRefRef mainClass;
    public volatile Class mainClassFound;

    private volatile File userHome = new File(System.getProperty("user.home"));



    public volatile File groovyUserConfig = new File(userHome, "${JrrStarterConstatnts.jrrConfigDir}/${JrrStarterConstatnts.configFileName}");

    private volatile File userDir = new File(System.getProperty("user.dir"));

    public volatile File directoryConfig = new File(userDir, JrrStarterConstatnts.configFileName);

    public volatile boolean loadUserWindowsConfigShared = true;

    public volatile File groovyHostConfigLinux = new File("${JrrStarterConstatnts.jrrConfigDirLinuxAllUsers}/${JrrStarterConstatnts.configFileName}");

    public volatile File groovyHostConfigWindows = new File("${JrrStarterConstatnts.jrrConfigDirWindowsAllUsers}/${JrrStarterConstatnts.configFileName}");

    public volatile Runnable onNormalExit = {
        if (GroovyMethodRunnerParams.gmrpn. fileOut != null) {
            GroovyMethodRunnerParams.gmrpn.fileOut.flush()
        }
    };


    public volatile String classPathProperty = "jrrclasspath";

    // inited in getInstance
//    public volatile List<File> groovyClassPathFiles;

    public volatile ProxyOutputStream2 stdOutProxy;
    public volatile ProxyOutputStream2 stdErrProxy;
    public volatile PrintStream stdOut;
    public volatile PrintStream stdErr;
    volatile Object result;


    // use getInstance() method
    @Deprecated
    static volatile GroovyMethodRunnerParams2 gmrp2;
    public volatile Object scriptNameInstance


    static GroovyMethodRunnerParams2 getInstanceNoCreate() {
        if(gmrp2==null){
            throw new NullPointerException("gmrp2 is null")
        }
        return gmrp2
    }

    static GroovyMethodRunnerParams2 getInstance() {
        if (gmrp2 != null) {
            return gmrp2;
        }
        Map classloaders = SharedObjectsUtils.getGlobalMap();
        gmrp2 = (GroovyMethodRunnerParams2) classloaders
                .get(GroovyMethodRunnerParams2.name);
        if (gmrp2 == null) {
            gmrp2 = new GroovyMethodRunnerParams2();
        }
        return gmrp2;
    }




}
