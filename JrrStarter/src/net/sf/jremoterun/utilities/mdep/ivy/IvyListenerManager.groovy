package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic
import org.apache.ivy.core.event.IvyListener

@CompileStatic
interface IvyListenerManager {


    void addIvyListener(IvyListener ideaIvyEvent)

    void removeIvyListener(IvyListener ideaIvyEvent)


}
