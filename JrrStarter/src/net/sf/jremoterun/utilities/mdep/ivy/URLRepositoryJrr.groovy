package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.module.descriptor.Artifact
import org.apache.ivy.core.settings.TimeoutConstraint
import org.apache.ivy.plugins.repository.Resource
import org.apache.ivy.plugins.repository.url.URLRepository
import org.apache.ivy.plugins.repository.url.URLResource;

import java.util.logging.Logger;

@CompileStatic
class URLRepositoryJrr extends URLRepository {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public final Map<String, Resource> resourcesCache2
    public JrrBiblioResolver biblioResolver;

    URLRepositoryJrr(JrrBiblioResolver biblioResolver) {
        this.biblioResolver = biblioResolver
    }

    URLRepositoryJrr(JrrBiblioResolver biblioResolver, TimeoutConstraint timeoutConstraint) {
        super(timeoutConstraint)
        this.biblioResolver = biblioResolver
        resourcesCache2 = JrrClassUtils.getFieldValue(this, 'resourcesCache') as Map<String, Resource>
    }

    @Override
    Resource getResource(String source) throws IOException {
        Resource res = resourcesCache2.get(source);
        if (res == null) {
            res = biblioResolver.depResolver2.createUrlResource(biblioResolver, new URL(source), this.getTimeoutConstraint())
            resourcesCache2.put(source, res);
        }
        return res;
    }

    @Override
    void get(String source, File destination) throws IOException {
        super.get(source, destination)
    }

    @Override
    void put(Artifact artifact, File source, String destination, boolean overwrite) throws IOException {
        super.put(artifact, source, destination, overwrite)
    }
}
