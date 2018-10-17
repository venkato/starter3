package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.event.EventManager
import org.apache.ivy.core.retrieve.RetrieveEngine
import org.apache.ivy.core.retrieve.RetrieveEngineSettings;

import java.util.logging.Logger;

@CompileStatic
class JrrRetrieveEngine extends RetrieveEngine{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JrrRetrieveEngine(RetrieveEngineSettings settings, EventManager eventManager) {
        super(settings, eventManager)
    }


}
