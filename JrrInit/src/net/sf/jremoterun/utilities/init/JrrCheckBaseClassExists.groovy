package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.AddFilesToUrlClassLoaderGroovy
import net.sf.jremoterun.utilities.classpath.JrrZipUtils
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.mdep.DropClassAdder

import java.util.logging.Logger

@CompileStatic
class JrrCheckBaseClassExists implements Runnable {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();




    @Override
    void run() {
         GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrpn
        AddFilesToUrlClassLoaderGroovy adder = gmrp.addFilesToClassLoader
        doCheckExitInZip(adder)
        doCheckLoaded(JrrClassUtils.getCurrentClassLoader() as GroovyClassLoader)
    }

    static void doCheckExitInZip(AddFilesToUrlClassLoaderGroovy adder ){
        HashSet<File> entriesFiles = new HashSet<>()
        HashSet<String> entriesClasses = new HashSet<>()

        adder.addedFiles2.each {
            if(it.getName().endsWith('.jar')) {
                entriesFiles.add(it)
                entriesClasses.addAll(JrrZipUtils.listEntries(it))
            }
        }
        DropClassAdder.values().toList().each {
            String pathWithShlash = it.clRef.getClassPath()+'.class'
            if(!entriesClasses.contains(pathWithShlash)){
                throw new Exception("${it.clRef} not found in ${entriesFiles} ; classes : ${entriesClasses}")
            }
        }
    }


    static void doCheckLoaded(GroovyClassLoader classLoader1){
        DropClassAdder.values().toList().each {
            try {
                it.clRef.loadClass(classLoader1)
            }catch(Throwable e){
                log.info "failed check ${it.clRef}"
                throw e
            }
        }
    }

}
