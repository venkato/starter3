package net.sf.jremoterun.utilities.mdep.ivy

import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.Ivy
import org.apache.ivy.core.cache.ArtifactOrigin;
import org.apache.ivy.core.cache.RepositoryCacheManager;
import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.DownloadReport;
import org.apache.ivy.core.resolve.DownloadOptions;
import org.apache.ivy.core.resolve.ResolveData;
import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.core.search.ModuleEntry;
import org.apache.ivy.core.search.OrganisationEntry;
import org.apache.ivy.core.search.RevisionEntry
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.namespace.Namespace
import org.apache.ivy.plugins.repository.Resource
import org.apache.ivy.plugins.repository.url.URLResource
import org.apache.ivy.plugins.resolver.ChainResolver;
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.DualResolver
import org.apache.ivy.plugins.resolver.IBiblioResolver

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Logger;
import groovy.transform.CompileStatic;
import org.apache.ivy.plugins.resolver.ResolverSettings;
import org.apache.ivy.plugins.resolver.util.ResolvedResource;


@CompileStatic
class DependencyResolverDebugger implements DependencyResolver{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();


    DependencyResolver dependencyResolver;
    IBiblioResolver ibiblio

    DependencyResolverDebugger(DependencyResolver dependencyResolver, IBiblioResolver ibiblio) {
        this.dependencyResolver = dependencyResolver
        this.ibiblio = ibiblio
    }

    public String getName() {
        return dependencyResolver.getName()+".proxy";
    }

    public void setName(String name) {
        dependencyResolver.setName(name);
    }

    boolean needRetry(  ResolvedModuleRevision dr){
        ModuleDescriptor descriptor = dr.descriptor
        if (descriptor instanceof DefaultModuleDescriptor) {
            DefaultModuleDescriptor drr = (DefaultModuleDescriptor) descriptor;
            Resource resource = drr.resource
            if (resource instanceof URLResource) {
                URLResource  urlResource= (URLResource) resource;
                File file = urlResource.file
                if(file.exists()) {
                    if(file.name.endsWith('.original')){
//                        log.info "file already original : ${file}"
                        return false
                    }
                    File orgi = "${file.absolutePath}.original" as File
                    boolean originalExist = orgi.exists()
//                    log.info "retry?${!originalExist} ${dr} ${orgi}"
                    if(!originalExist){
                        return true
                    }
                }

            }
        }
        return false
    }

    public ResolvedModuleRevision getDependency(DependencyDescriptor dd, ResolveData data) throws ParseException {
        ResolvedModuleRevision dr =  dependencyResolver.getDependency(dd, data);
        if(dr!=null&&needRetry(dr)) {
            dr = ibiblio.getDependency(dd, data)
            log.info "after retry : ${dr}"
        }
//        Thread.sleep(Long.MAX_VALUE)
//        log.info "${dd} ${dr}"
//        log.info "${dr.class.name}"
//        log.info "${dr.descriptor}"
//        log.info "${dr.descriptor.class.name}"
//        DefaultModuleDescriptor dd3 = dr.descriptor as DefaultModuleDescriptor
//
////        log.info "${dr.resolver}"
//        log.info "${dr.report}"
//        log.info "${dr.artifactResolver}"
//        log.info "${dr.artifactResolver.class.name}"
////        InitializerHomePcAll.init()/
        return dr
    }

    public ResolvedResource findIvyFileRef(DependencyDescriptor dd, ResolveData data) {
        ResolvedResource resolvedResource =  dependencyResolver.findIvyFileRef(dd, data);
        log.info "${dd} ${resolvedResource}"
        return resolvedResource
    }

    public DownloadReport download(Artifact[] artifacts, DownloadOptions options) {
        DownloadReport downloadReport =  dependencyResolver.download(artifacts, options);
        log.info "${artifacts.toList()} ${downloadReport}"
        return downloadReport
    }

    public ArtifactDownloadReport download(ArtifactOrigin artifact, DownloadOptions options) {
        ArtifactDownloadReport artifactDownloadReport =  dependencyResolver.download(artifact, options);
        log.info "${artifact} ${artifactDownloadReport}"
        return artifactDownloadReport;
    }

    public boolean exists(Artifact artifact) {
        boolean res = dependencyResolver.exists(artifact);
//        log.info "${artifact} ${res}"
        return res
    }

    public ArtifactOrigin locate(Artifact artifact) {
        ArtifactOrigin artifactOrigin =  dependencyResolver.locate(artifact);
//        log.info "${artifact} ${artifact}"
        return artifactOrigin
    }

    public void publish(Artifact artifact, File src, boolean overwrite) throws IOException {
        dependencyResolver.publish(artifact, src, overwrite);
    }

    public void beginPublishTransaction(ModuleRevisionId module, boolean overwrite) throws IOException {
        dependencyResolver.beginPublishTransaction(module, overwrite);
    }

    public void abortPublishTransaction() throws IOException {
        dependencyResolver.abortPublishTransaction();
    }

    public void commitPublishTransaction() throws IOException {
        dependencyResolver.commitPublishTransaction();
    }

    public void reportFailure() {
        dependencyResolver.reportFailure();
    }

    public void reportFailure(Artifact art) {
        dependencyResolver.reportFailure(art);
    }

    public String[] listTokenValues(String token, Map otherTokenValues) {
//        log.info "${token}"
        return dependencyResolver.listTokenValues(token, otherTokenValues);
    }

    public Map[] listTokenValues(String[] tokens, Map criteria) {
//        log.info "${tokens.toList()}"
        return dependencyResolver.listTokenValues(tokens, criteria);
    }

    public OrganisationEntry[] listOrganisations() {
//        log.info "cp1"
        return dependencyResolver.listOrganisations();
    }

    public ModuleEntry[] listModules(OrganisationEntry org) {
        log.info "${org}"
        return dependencyResolver.listModules(org);
    }

    public RevisionEntry[] listRevisions(ModuleEntry module) {
        log.info "${module}"
        return dependencyResolver.listRevisions(module);
    }

    public Namespace getNamespace() {
        return dependencyResolver.getNamespace();
    }

    public void dumpSettings() {
        dependencyResolver.dumpSettings();
    }


    public synchronized void addResolver(IvySettings ivySettings) {
        if (dependencyResolver == null) {
            throw new NullPointerException("null resolver");
        }
        if (dependencyResolver instanceof ChainResolver) {
            List subresolvers = ((ChainResolver) dependencyResolver).getResolvers();
            for (Iterator iter = subresolvers.iterator(); iter.hasNext();) {
                DependencyResolver dr = (DependencyResolver) iter.next();
                assert dr.name!=null
                ivySettings.addResolver(dr);
            }
        } else if (dependencyResolver instanceof DualResolver) {
            DependencyResolver ivyResolver = ((DualResolver) dependencyResolver).getIvyResolver();
            if (ivyResolver != null) {
                assert ivyResolver.name!=null
                ivySettings.addResolver(ivyResolver);
            }
            DependencyResolver artifactResolver = ((DualResolver) dependencyResolver).getArtifactResolver();
            if (artifactResolver != null) {
                assert artifactResolver.name!=null
                ivySettings.addResolver(artifactResolver);
            }
        }else{
            log.info "strange : ${dependencyResolver.class.name}"
        }
    }

    public void setSettings(ResolverSettings settings) {
//        log.info "setting setted ${settings}"
        dependencyResolver.setSettings(settings);
        dependencyResolver.dumpSettings()
    }

    public RepositoryCacheManager getRepositoryCacheManager() {
        return dependencyResolver.getRepositoryCacheManager();
    }





}
