package net.sf.jremoterun.utilities.mdep.ivy;

import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.cache.CacheMetadataOptions
import org.apache.ivy.core.cache.DefaultResolutionCacheManager
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.resolve.ResolvedModuleRevision
import org.apache.ivy.plugins.parser.ModuleDescriptorParser
import org.apache.ivy.plugins.resolver.DependencyResolver;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class JrrDefaultResolutionCacheManager extends org.apache.ivy.core.cache.DefaultResolutionCacheManager{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JrrDefaultResolutionCacheManager(File basedir) {
        super(basedir)
    }


    public ResolvedModuleRevision findModuleInCache(DependencyDescriptor dd,
                                                    ModuleRevisionId requestedRevisionId, CacheMetadataOptions options,
                                                    String expectedResolver) {
        return null
    }

    @Override
    protected ModuleDescriptorParser getModuleDescriptorParser(File ivyFile) {
        ModuleDescriptorParser parser =  super.getModuleDescriptorParser(ivyFile)
//        ModuleDescriptor depMD = getMdFromCache(parser, options, ivyFile);
//        String resolverName = getSavedResolverName(depMD);
//        String artResolverName = getSavedArtResolverName(depMD);
//        DependencyResolver resolver = settings.getResolver(resolverName);

        return parser
    }
}
