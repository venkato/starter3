package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.Ivy
import org.apache.ivy.core.settings.IvySettings;

import java.util.logging.Logger;

@CompileStatic
class JrrIvySettings extends IvySettings{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JrrIvy buildIvy(){
        JrrIvy ivy = new JrrIvy();
        ivy.setSettings(this);
        ivy.bind();
        return ivy;
    }

    void setDefaultResolver(String resolverName) {
        if (getDefaultResolver()!=null) {
            log.info "default resolver already set, ignore default ${resolverName}"
        } else {
            super.setDefaultResolver(resolverName)
        }
    }

}
