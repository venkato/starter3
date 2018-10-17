package net.sf.jremoterun.utilities.groovystarter.seqpattern;

import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.ClRef

import net.sf.jremoterun.utilities.groovystarter.RunnerFrom
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableClosure
import net.sf.jremoterun.utilities.groovystarter.runners.RunnableFactory
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfo
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.RunnerCreationCallStack

import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
abstract class SeqPatternRunner {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    //Map<JrrRunnerPhaseI,List<Runnable>> afterRunnable = [:]
    List<JrrRunnerPhaseI> passedPhase = []

    public JrrRunnerPhaseI currentPhaseEnhanced
    public PhaseState phaseState

//    @Deprecated
//    JrrRunnerPhaseI jrrRunnerPhase;

    public Map<JrrRunnerPhaseI, List<Runnable>> listenersBefore = new ConcurrentHashMap()

    public Map<JrrRunnerPhaseI, List<Runnable>> listenersAfter = new ConcurrentHashMap()

    void checkCurrentPhaseEnhanced(PhaseState phaseState1,JrrRunnerPhaseI toCheck){
        comparePhases(toCheck,currentPhaseEnhanced)
        if(phaseState1!=phaseState){
            throw new PhaseCheckException("${phaseState1} != current ${phaseState}")
        }
    }

//    @Deprecated
//    void checkCurrentPhase(JrrRunnerPhaseI toCheck){
//        comparePhases(toCheck,jrrRunnerPhase)
//    }

    void comparePhases(JrrRunnerPhaseI toCheckLocal, JrrRunnerPhaseI jrrRunnerPhaseLocal){
        if(jrrRunnerPhaseLocal==null){
            throw new NullPointerException("current phase is null")
        }
        if(toCheckLocal==null){
            throw new NullPointerException("toCheck phase is null")
        }
        if(toCheckLocal.getClass() == jrrRunnerPhaseLocal.getClass()){
            if(toCheckLocal == jrrRunnerPhaseLocal){

            }else{
                throw new PhaseCheckException("${toCheckLocal} != current ${jrrRunnerPhaseLocal}")
            }
        }else{
            throw new PhaseCheckException("${toCheckLocal.getClass().getName()}.${toCheckLocal} != current ${jrrRunnerPhaseLocal.getClass().getName()}.${jrrRunnerPhaseLocal}")
        }
    }

    void start(JrrRunnerPhaseI startPhase) {
        currentPhaseEnhanced = startPhase
        phaseState = PhaseState.pre
//        jrrRunnerPhase = startPhase
        loop()
    }


    void loop() {
        while (true) {
            JrrRunnerPhaseI nextPhase = getNextPhase()
            try {
                changeState3(nextPhase)
            } catch (Throwable e) {
                log.info("failed at : ${nextPhase} ${nextPhase.getClass().getName()}")
                throw e;
            }
            if (nextPhase == FinalPhase.finalPhase) {
                break
            }

        }
    }

    JrrRunnerPhaseI getNextPhase() {
        return currentPhaseEnhanced.nextPhase()

    }

    abstract Runnable getRunnableForPhase(JrrRunnerPhaseI phase);

    Runnable getRunnableForPhase2(JrrRunnerPhaseI phase) {
        if (phase == FinalPhase.finalPhase) {
            return null;
        }
        return getRunnableForPhase(phase)
    }


    void changeState3(JrrRunnerPhaseI jrrRunnerPhaseNew) {
        currentPhaseEnhanced = jrrRunnerPhaseNew
        phaseState = PhaseState.pre
        isPhasePassed(jrrRunnerPhaseNew)
        runRunners(getListeners(jrrRunnerPhaseNew, true))
//        this.jrrRunnerPhase = jrrRunnerPhaseNew
        Runnable r = getRunnableForPhase2(jrrRunnerPhaseNew);
        if (r == null) {
//            log.info "empty runnable for ${jrrRunnerPhaseNew}"
        } else {
            phaseState = PhaseState.exact
            r.run()
        }
        phaseState = PhaseState.after
        runRunners(getListeners(jrrRunnerPhaseNew, false))
        passedPhase.add(jrrRunnerPhaseNew)
    }

    void runRunners(List<Runnable> runable) {
        runable.each {
            try {
                it.run()
            } catch (Throwable e) {
                onException(it, e)
            }
        }
    }

    void onException(Runnable r, Throwable e) {
        log.info "failed on ${r} : ${e}"
        throw e
    }

    List<Runnable> getListeners(JrrRunnerPhaseI phase, boolean before) {
        if (before) {
            List<Runnable> result = listenersBefore.get(phase);
            if (result == null) {
                result = []
                listenersBefore.put(phase, result)
            }
            return result
        }
        List<Runnable> result = listenersAfter.get(phase)
        if (result == null) {
            result = []
            listenersAfter.put(phase, result)
        }
        return result

    }


    void addListener(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        addListenerImpl(phase, before, listener)
//        addListenerImpl(phase, before, new RunnerFrom(listener))
    }

    void addListenerImpl(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        assert currentPhaseEnhanced != null
        assert phase != null
//        if (jrrRunnerPhase.offset() >= phase.offset()) {
        if (isPhasePassed(phase)) {
            throw new Exception("Phase ${phase} passed, current : ${currentPhaseEnhanced}")
        }
        getListeners(phase, before).add(listener)
    }

    boolean isPhasePassed(JrrRunnerPhaseI phase) {
        if(passedPhase.contains(phase)){
            return true
        }
        if( phase != currentPhaseEnhanced){
            return false
        }
        if(phaseState == PhaseState.pre){
            return false
        }
        return true
    }


    void addListenerOrRunIfPassedImpl(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        assert currentPhaseEnhanced != null
        if (isPhasePassed(phase)) {
            listener.run()
        } else {
            RunnerFrom runnerFrom = new RunnerFrom(listener)
            runnerFrom.creationPhase = currentPhaseEnhanced
            runnerFrom.runBeforePhase = phaseState

            if(listener instanceof  RunnerCreationCallStack){
                listener.setCreationInfo(runnerFrom)
            }
            if(listener instanceof  CallerInfo){
                listener.setCallerInfo(runnerFrom)
            }
            getListeners(phase, before).add(runnerFrom)
        }
    }


    void addL(JrrRunnerPhaseI phase, boolean before, Callable listener) {
        Runnable r = new RunnableClosure(listener)
        addListenerOrRunIfPassedImpl(phase, before, r);
    }

    void addL(JrrRunnerPhaseI phase, boolean before, Runnable listener) {
        addListenerOrRunIfPassedImpl(phase, before, listener)
    }

    void addL(JrrRunnerPhaseI phase, boolean before, File listener) {
        addL(phase, before, RunnableFactory.createRunner(listener))
    }


    @Deprecated
    void addL(JrrRunnerPhaseI phase, boolean before, String listener) {
        addL(phase, before, RunnableFactory.createRunner(listener))
    }

    void addL(JrrRunnerPhaseI phase, boolean before, Class listener) {
        addL(phase, before, RunnableFactory.createRunner(listener))
    }


    void addL(JrrRunnerPhaseI phase, boolean before, ClRef listener) {
        addL(phase, before, RunnableFactory.createRunner(listener))
    }


}
