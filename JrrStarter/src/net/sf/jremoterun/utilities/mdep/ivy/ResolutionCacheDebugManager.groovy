package net.sf.jremoterun.utilities.mdep.ivy;

import net.sf.jremoterun.utilities.JrrClassUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import groovy.transform.CompileStatic;
import org.apache.ivy.core.cache.ResolutionCacheManager;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

@CompileStatic
public class ResolutionCacheDebugManager implements ResolutionCacheManager {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ResolutionCacheManager resolutionCacheManager;

    @Override
    public File getResolutionCacheRoot() {
        return resolutionCacheManager.getResolutionCacheRoot();
    }

    @Override
    public File getResolvedIvyFileInCache(ModuleRevisionId mrid) {
        File result =  resolutionCacheManager.getResolvedIvyFileInCache(mrid);
//        log.info "${mrid} ${result}"
        return result
    }

    @Override
    public File getResolvedIvyPropertiesInCache(ModuleRevisionId mrid) {
        File result =   resolutionCacheManager.getResolvedIvyPropertiesInCache(mrid);
//        log.info "${mrid} ${result}"
//        Thread.dumpStack()
        return result
    }

    @Override
    public File getConfigurationResolveReportInCache(String resolveId, String conf) {
        File result =   resolutionCacheManager.getConfigurationResolveReportInCache(resolveId, conf);
//        log.info "${resolveId} ${result}"
        return result
    }

    @Override
    public File[] getConfigurationResolveReportsInCache(String resolveId) {
        File[] result = resolutionCacheManager.getConfigurationResolveReportsInCache(resolveId);
//        log.info "${resolveId}"
        return result
    }

    @Override
    public ModuleDescriptor getResolvedModuleDescriptor(ModuleRevisionId mrid) throws ParseException, IOException {
        ModuleDescriptor descriptor = resolutionCacheManager.getResolvedModuleDescriptor(mrid);
//        log.info "${mrid} ${descriptor}"
        return descriptor
    }

    @Override
    public void saveResolvedModuleDescriptor(ModuleDescriptor md) throws ParseException, IOException {
//        log.info "${md}"
        resolutionCacheManager.saveResolvedModuleDescriptor(md);
    }

    @Override
    public void clean() {
        resolutionCacheManager.clean();
    }





}
