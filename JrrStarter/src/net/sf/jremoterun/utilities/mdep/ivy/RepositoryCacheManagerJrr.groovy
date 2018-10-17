package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.cache.DefaultRepositoryCacheManager
import org.apache.ivy.core.cache.RepositoryCacheManager
import org.apache.ivy.core.settings.IvySettings;

import java.util.logging.Logger;

@CompileStatic
class RepositoryCacheManagerJrr extends DefaultRepositoryCacheManager{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    RepositoryCacheManagerJrr(String name, IvySettings settings, File basedir) {
        super(name, settings, basedir)
    }

    RepositoryCacheManagerJrr() {
    }
}
