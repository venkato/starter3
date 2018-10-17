package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.cache.ArtifactOrigin
import org.apache.ivy.core.cache.CacheDownloadOptions
import org.apache.ivy.core.cache.CacheMetadataOptions
import org.apache.ivy.core.cache.DefaultRepositoryCacheManager
import org.apache.ivy.core.cache.RepositoryCacheManager
import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.report.ArtifactDownloadReport
import org.apache.ivy.core.resolve.ResolvedModuleRevision
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.repository.ArtifactResourceResolver
import org.apache.ivy.plugins.repository.ResourceDownloader
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.util.ResolvedResource

import java.text.ParseException;
import java.util.logging.Logger;

@CompileStatic
class RepositoryCacheManagerJrr extends DefaultRepositoryCacheManager{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    RepositoryCacheManagerJrr(String name, IvySettings settings, File basedir) {
        super(name, settings, basedir)
    }

    RepositoryCacheManagerJrr() {
    }

    @Override
    ArtifactDownloadReport download(Artifact artifact, ArtifactResourceResolver resourceResolver, ResourceDownloader resourceDownloader, CacheDownloadOptions options) {
        ArtifactDownloadReport artifactDownloadReport =  super.download(artifact, resourceResolver, resourceDownloader, options)
        return artifactDownloadReport
    }

    @Override
    File getArchiveFileInCache(Artifact artifact) {
        File f =  super.getArchiveFileInCache(artifact)
//        log.info "${artifact} saving to ${f}"
        return f
    }

    @Override
    File getArchiveFileInCache(Artifact artifact, ArtifactOrigin origin) {
        return super.getArchiveFileInCache(artifact, origin)
    }

    @Override
    ResolvedModuleRevision cacheModuleDescriptor(DependencyResolver resolver, ResolvedResource mdRef, DependencyDescriptor dd, Artifact moduleArtifact, ResourceDownloader downloader, CacheMetadataOptions options) throws ParseException {
        return super.cacheModuleDescriptor(resolver, mdRef, dd, moduleArtifact, downloader, options)
    }
}
