package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.Ivy
import org.apache.ivy.core.cache.DefaultRepositoryCacheManager
import org.apache.ivy.core.cache.RepositoryCacheManager
import org.apache.ivy.core.settings.IvySettings

import java.lang.reflect.Field;
import java.util.logging.Logger;

@CompileStatic
class JrrIvySettings extends IvySettings {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public Field defCacheManagerF
    public IvyDepResolver2 ivyDepResolver2

    JrrIvySettings(IvyDepResolver2 ivyDepResolver2) {
        this.ivyDepResolver2 = ivyDepResolver2
        defCacheManagerF = JrrClassUtils.findField(getClass(), 'defaultRepositoryCacheManager')
    }

    @Deprecated
    JrrIvySettings() {
        defCacheManagerF = JrrClassUtils.findField(getClass(), 'defaultRepositoryCacheManager')
    }

    JrrIvy buildIvy() {
        JrrIvy ivy = new JrrIvy(ivyDepResolver2);
        ivy.setSettings(this);
        ivy.bind();
        return ivy;
    }

    void setDefaultResolver(String resolverName) {
        if (getDefaultResolver() != null) {
            log.info "default resolver already set, ignore default ${resolverName}"
        } else {
            super.setDefaultResolver(resolverName)
        }
    }


    /**
     *
     * @see org.apache.ivy.plugins.parser.m2.PomModuleDescriptorParser#addSourcesAndJavadocArtifactsIfPresent
     */
    void setResolveSourcesAndJavaDoc(boolean value) {
        setVariable('ivy.maven.lookup.sources', '' + value)
        setVariable('ivy.maven.lookup.javadoc', '' + value)
    }

    @Override
    synchronized RepositoryCacheManager getDefaultRepositoryCacheManager() {
        RepositoryCacheManager get4 = defCacheManagerF.get(this) as RepositoryCacheManager

        if (get4 == null) {
            RepositoryCacheManager defaultRepositoryCacheManager1 = createRepositoryCacheManagerJrr();
            setDefaultRepositoryCacheManager(defaultRepositoryCacheManager1)
            addRepositoryCacheManager(defaultRepositoryCacheManager1);
            return defaultRepositoryCacheManager1
        }
        return get4;
    }

    RepositoryCacheManager createRepositoryCacheManagerJrr() {
        return new RepositoryCacheManagerJrr("default-cache", this, getDefaultRepositoryCacheBasedir());
    }
}
