package net.sf.jremoterun.utilities.groovystarter.seqpattern;

import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class NextPhaseEnumUtil {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


//    static <T extends Enum> T nextPhase(T current,Collection<T> enumValues) {
    static JrrRunnerPhaseI nextPhase(JrrRunnerPhaseI current,Object[] enumValues) {
        assert current!=null
//        Collection enumValues = findEnumValues(current.class)
        boolean nextIsMy = false;
        for (Object phaseI : enumValues) {
            if (nextIsMy) {
                return (JrrRunnerPhaseI)phaseI;
            }
            if (phaseI == current) {
                nextIsMy = true;
            }
        }
        throw new IllegalStateException("failed find next for " + current.class);
    }

    static JrrRunnerPhaseI nextPhase2(JrrRunnerPhaseI current,Object[] enumValues) {
        assert current!=null
//        Collection enumValues = findEnumValues(current.class)
        boolean nextIsMy = false;
        for (Object phaseI : enumValues) {
            if (nextIsMy) {
                return (JrrRunnerPhaseI)phaseI;
            }
            if (phaseI == current) {
                nextIsMy = true;
            }
        }
        return FinalPhase.finalPhase;
    }

//    static Collection findEnumValues(Class enumClazz){
//        Object enumValues = JrrClassUtils.invokeJavaMethod(enumClazz, "values");
//        assert enumValues.class.isArray()
////        assert  enumValues.class.isArray()
//        return enumValues as Collection
//    }

}
