package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.event.EventManager
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.resolve.IvyNode
import org.apache.ivy.core.resolve.ResolveEngine
import org.apache.ivy.core.resolve.ResolveEngineSettings
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.sort.SortEngine

import java.text.ParseException;
import java.util.logging.Logger;

@CompileStatic
class IvyResolveEngineJrr extends ResolveEngine {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    IvyResolveEngineJrr(ResolveEngineSettings settings, EventManager eventManager, SortEngine sortEngine) {
        super(settings, eventManager, sortEngine)
    }

    
    @Override
    ResolveReport resolve(ModuleDescriptor md, ResolveOptions options) throws ParseException, IOException {
        return super.resolve(md, options)
    }

    @Override
    IvyNode[] getDependencies(ModuleDescriptor md, ResolveOptions options, ResolveReport report) {
        return super.getDependencies(md, options, report)
    }
}
