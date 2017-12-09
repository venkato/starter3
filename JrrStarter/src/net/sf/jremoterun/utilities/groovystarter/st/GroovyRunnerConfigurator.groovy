package net.sf.jremoterun.utilities.groovystarter.st;

import groovy.transform.CompileStatic
import net.sf.jremoterun.JrrUtils
import net.sf.jremoterun.utilities.classpath.*
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.GroovyRunnerConfigurator2
import net.sf.jremoterun.utilities.mdep.DropshipClasspath

@CompileStatic
abstract class GroovyRunnerConfigurator extends GroovyRunnerConfigurator2 {


    private static boolean dropshipAdded = false

    @Override
    Object run() {
        gmrp.addL(JrrRunnerPhase2.addClassPathFiles,false, this.&afterClassPathSet);
        doConfig()
        return null
    }

    void afterClassPathSet(){
    }


    @Deprecated
    static void addBinWithSource(BinaryWithSource binary){
        GroovyMethodRunnerParams gmrp4 = GroovyMethodRunnerParams.instance
        File f =gmrp4.addFilesToClassLoaderGroovy?binary.binary:binary.source
        gmrp4.addFilesToClassLoader.addF(f)
    }


    @Deprecated
    static void addJavassistToThisCLassloader(){
        GroovyMethodRunnerParams.instance.addFilesToClassLoader.addM(DropshipClasspath.javaAssist)
    }


    AddFilesToUrlClassLoaderGroovy getAdder() {
        return gmrp.addFilesToClassLoader
    }

    static MavenDefaultSettings getMds(){
        return MavenDefaultSettings.mavenDefaultSettings
    }

    MavenCommonUtils getMcu() {
        return gmrp.addFilesToClassLoader.mavenCommonUtils
    }


    void startJmx(int port) {
        JrrUtils.creatJMXConnectorAndRMIRegistry(null, port, null, null);
    }


    @Deprecated
    static void addIvyToGroovy(){
        gmrp.addFilesToClassLoaderClassaderOfGroovy.addM(DropshipClasspath.ivyMavenId)
    }
}
