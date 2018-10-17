package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.cache.ArtifactOrigin
import org.apache.ivy.core.cache.CacheDownloadOptions
import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.resolve.DownloadOptions
import org.apache.ivy.core.resolve.ResolveData
import org.apache.ivy.core.resolve.ResolvedModuleRevision
import org.apache.ivy.plugins.repository.Repository
import org.apache.ivy.plugins.repository.Resource
import org.apache.ivy.plugins.repository.url.URLRepository
import org.apache.ivy.plugins.resolver.IBiblioResolver
import org.apache.ivy.plugins.resolver.ResolverSettings
import org.apache.ivy.plugins.resolver.util.ResolvedResource
import org.apache.ivy.plugins.resolver.util.ResourceMDParser

import java.text.ParseException;
import java.util.logging.Logger;

@CompileStatic
class JrrBiblioResolver extends IBiblioResolver {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static ThreadLocal<Boolean> isDownloadSources = new ThreadLocal<>()
    public static boolean isDownloadJavaDocS = false
    public static String sourcePackageTypeS = 'source'
    public static String javaDocPackageTypeS = 'javadoc'
    public Boolean isDownloadJavaDoc = isDownloadJavaDocS
    public IvyDepResolver2 depResolver2

    JrrBiblioResolver(IvyDepResolver2 depResolver2) {
        this.depResolver2 = depResolver2
        setRepository(new URLRepositoryJrr(this,new LazyTimeoutConstraint(this)));
    }

    @Override
    ResolverSettings getSettings() {
        return super.getSettings()
    }

    @Override
    void setSettings(ResolverSettings settings) {
        super.setSettings(settings)
    }

    @Override
    ResolvedModuleRevision parse(ResolvedResource mdRef, DependencyDescriptor dd, ResolveData data) throws ParseException {
        return super.parse(mdRef, dd, data)
    }

    @Override
    ArtifactOrigin locate(Artifact artifact) {
        if (artifact.getType() == sourcePackageTypeS) {
            Boolean get1 = isDownloadSources.get()
            if (get1 == null) {

            } else {
                if (!get1) {
                    return null;
                }
            }
        }
        if (artifact.getType() == javaDocPackageTypeS) {
            if (!isDownloadJavaDoc) {
                return null
            }
        }
        return super.locate(artifact)
    }

    @Override
    ResolvedResource findArtifactRef(Artifact artifact, Date date) {
        return super.findArtifactRef(artifact, date)
    }

    @Override
    ResolvedResource findResourceUsingPattern(ModuleRevisionId mrid, String pattern, Artifact artifact, ResourceMDParser rmdparser, Date date) {
        return super.findResourceUsingPattern(mrid, pattern, artifact, rmdparser, date)
    }

    @Override
    protected long get(Resource resource, File dest) throws IOException {
        return super.get(resource, dest)
    }

    @Override
    ResolvedModuleRevision getDependency(DependencyDescriptor dd, ResolveData data) throws ParseException {
        return super.getDependency(dd, data)
    }
}
