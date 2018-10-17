package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef

@CompileStatic
class ClRefPreloadCheck extends ClRef {

    public String preClassCheck;

    ClRefPreloadCheck(String c,String preClassCheck) {
        super(c)
        this.preClassCheck = preClassCheck
    }


    ClRefPreloadCheck(Class clazz,String preClassCheck) {
        this(clazz.getName(),preClassCheck)
    }


    boolean equals(o) {
        if(o==null){
            return false
        }
        if (this.is(o)) return true;
        if(!super.equals(o)){
            return false
        }
//        if (getClass() != o.class) return false;
        if (!(o instanceof ClRefPreloadCheck)) {
            return false
        }
        ClRefPreloadCheck clRef = (ClRefPreloadCheck) o;
//
//        if (className != clRef.className) return false;
        if (preClassCheck != clRef.preClassCheck) return false;
//
        return true;
    }

    int hashCode() {
        return (className != null ? className.hashCode() : 0)
    }

}
