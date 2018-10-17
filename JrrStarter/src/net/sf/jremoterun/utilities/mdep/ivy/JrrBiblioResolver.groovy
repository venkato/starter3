package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.cache.CacheDownloadOptions
import org.apache.ivy.core.resolve.DownloadOptions
import org.apache.ivy.plugins.resolver.IBiblioResolver;

import java.util.logging.Logger;

@CompileStatic
class JrrBiblioResolver extends IBiblioResolver{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


}
