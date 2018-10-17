package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Need manually remove from libs/groovy.jar and add groovy.jar from repo
 * @see groovy.grape.GrapeIvy
 */
@CompileStatic
class SetGrapeWrapper implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public static volatile boolean setOnlyIfClassLoaderCorrect = true
    public static volatile Boolean shouldLoad
    public static volatile Boolean triedLoaded
    public static volatile Boolean loadedFine
    public static volatile Boolean grapeClassHasWrongClassloader

    public static ClRef cnr = new ClRef('net.sf.jremoterun.utilities.init.SetGrape')

    @Override
    void run() {
        if(setOnlyIfClassLoaderCorrect){
            shouldLoad = shouldLoad1()
            log.info "shouldLoad = ${shouldLoad}"
            if(shouldLoad){
                loadGrape()
            }
        }else{
            loadGrape();
        }
    }

    boolean shouldLoad1(){
        try {
            GroovyObject.getClassLoader().loadClass('groovy.grape.GrapeIvy')
            grapeClassHasWrongClassloader = true
            return false
        }catch(ClassNotFoundException e){
            grapeClassHasWrongClassloader = false
            log.log(Level.FINE,'failed load GrapeIvy, that is ok',e)
            return true
        }
    }

    void loadGrape() {
        triedLoaded = true
        RunnableFactory.runRunner(cnr)
        //GroovyMethodRunnerParams.gmrp.addL(JrrRunnerPhase2.addClassPathFiles, false, cnr)
        loadedFine = true
    }

}
