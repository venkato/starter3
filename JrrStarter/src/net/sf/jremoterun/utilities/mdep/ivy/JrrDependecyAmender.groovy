package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.resolve.ResolveData
import org.apache.ivy.core.resolve.ResolvedModuleRevision;

import java.util.logging.Logger;

@CompileStatic
class JrrDependecyAmender {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ResolvedModuleRevision amendIfNeeded(ResolvedModuleRevision resolvedModuleRevision,DependencyDescriptor dd, ResolveData data){
         return resolvedModuleRevision;
    }

}
