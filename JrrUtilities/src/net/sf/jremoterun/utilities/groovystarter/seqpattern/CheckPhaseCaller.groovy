package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter

@CompileStatic
class CheckPhaseCaller {




    static void checkCallerGeneric(Object object, Object toBeFound) {
        if (toBeFound == null) {
            throw new NullPointerException("toBeFound is null")
        }
        checkCallerGeneric2(object, toBeFound, 1)

    }

    static void checkCallerGeneric2(Object object, Object toBeFound, int depthCount) {
        if (object == null) {
            throw new Exception("Object is null depth=${depthCount}")
        }
        if (object instanceof CallerInfoGetter) {
            checkCallerGeneric2(object.getCallerInfo(), toBeFound, depthCount + 1)
            return
        }
        if (object.getClass() == toBeFound.getClass()) {
            if (object == toBeFound) {
                return
            }else{
                throw new Exception("${object} != ${toBeFound}")
            }

        }
        throw new Exception("unknown object ${object} depth=${depthCount}")
    }

}
