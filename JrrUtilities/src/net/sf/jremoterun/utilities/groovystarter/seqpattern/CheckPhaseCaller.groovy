package net.sf.jremoterun.utilities.groovystarter.seqpattern

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.classpath.ToFileRef2
import net.sf.jremoterun.utilities.groovystarter.seqrunnerhelper.CallerInfoGetter

@CompileStatic
class CheckPhaseCaller {

    public static CheckPhaseCaller instance = new CheckPhaseCaller();

    static void checkCallerGeneric(Object object, Object toBeFound) {
        checkCallerGeneric2(object, toBeFound, 1)
    }

    static void checkCallerGeneric2(Object object, Object toBeFound, int depthCount) {
        instance.checkCallerGeneric2Impl(object, toBeFound, depthCount)
    }

    void checkCallerGeneric2Impl(Object object, Object toBeFound, int depthCount) {
        if (object == null) {
            throw new Exception("Object is null depth=${depthCount}")
        }
        if (object instanceof CallerInfoGetter) {
            checkCallerGeneric2(object.getCallerInfo(), toBeFound, depthCount + 1)
            return
        }
        boolean allGood = false
        if (toBeFound instanceof ClRef) {
            if (object instanceof Class) {
                ClRef toBeFound2 = toBeFound as ClRef
                Class object2 = object
                if (toBeFound.className == object2.getName()) {
                    allGood = true
                } else {
                    throw new Exception("${object2.getName()} != ${toBeFound2.className}")
                }
            }
        }
        if (toBeFound instanceof ToFileRef2) {
            if (object instanceof File) {
                ToFileRef2 toBeFound2 = toBeFound as ToFileRef2
                File toBeFound3 = toBeFound2.resolveToFile()
                File object2 = object
                if (toBeFound3 == object2) {
                    allGood = true
                } else {
                    throw new Exception("${object2} != ${toBeFound3}")
                }
            }
        }
        if (object.getClass() == toBeFound.getClass()) {
            if (object == toBeFound) {
                allGood = true
            } else {
                throw new Exception("${object} != ${toBeFound}")
            }

        }
        if (!allGood) {
            throw new Exception("unknown object ${object} depth=${depthCount}. Classes diff got : ${object.getClass().getName()}, needed : ${toBeFound.getClass().getName()}")
        }
    }


    static Object getCallerGeneric3(Object object, int depthCount) {
        return instance.getCallerGeneric3Impl(object,depthCount)
    }

    Object getCallerGeneric3Impl(Object object, int depthCount) {
        if (object == null) {
            throw new Exception("Object is null depth=${depthCount}")
        }
        if (object instanceof CallerInfoGetter) {
            return getCallerGeneric3(object.getCallerInfo(), depthCount + 1)
        }
        return object
    }

}
