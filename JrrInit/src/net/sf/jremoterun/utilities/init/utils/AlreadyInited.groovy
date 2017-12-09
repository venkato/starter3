package net.sf.jremoterun.utilities.init.utils

import net.sf.jremoterun.utilities.CreationInfo;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class AlreadyInited {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    static void alreadyInited(CreationInfo inited,Object obj){
        //log.info "already inited : ${obj.class.name}"
        throw new Exception("already inited")
//        throw inited.createExc()
    }

}
