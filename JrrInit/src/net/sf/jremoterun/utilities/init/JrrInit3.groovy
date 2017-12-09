package net.sf.jremoterun.utilities.init

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.CreationInfo
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.MBeanClient
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.st.GroovyMethodRunner2
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.st.JdkLogFormatter
import net.sf.jremoterun.utilities.groovystarter.JrrRunnerPhase
import net.sf.jremoterun.utilities.groovystarter.st.JrrRunnerPhase2
import net.sf.jremoterun.utilities.init.utils.AlreadyInited

import java.util.logging.Logger

@CompileStatic
class JrrInit3 implements Runnable {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.instance

    static ClRef setDepResolver = new ClRef('net.sf.jremoterun.utilities.init.SetDependecyResolver')


    private static CreationInfo inited

    @Override
    void run() {
        if(inited==null) {
            inited = new CreationInfo();
        }else{
            // called twice, toto improve
            // AlreadyInited.alreadyInited(inited,this)
            return
        }
        JdkLogFormatter.setLogFormatter()
//        log.info "cp3"
        gmrp.addL(JrrRunnerPhase.setNextPhase, false, this.&f2)
        gmrp.jrrUtilsPhaseDoneAfter = JrrRunnerPhase2.afterCoreLibAdded
        gmrp.setPhaseChanger(JrrRunnerPhase2.setDependecyResolver,setDepResolver)
    }


    void f2() {
        new GroovyMethodRunner2().run()
//        f1()
//        checkIfIdea()
    }


}
