package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.JrrUtilities3
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenPath
import org.apache.ivy.Ivy
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.module.descriptor.ModuleDescriptor
import org.apache.ivy.core.module.id.ModuleId
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.resolve.IvyNode
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.parser.m2.PomDependencyMgt
import org.apache.ivy.plugins.parser.m2.PomModuleDescriptorBuilder
import org.apache.ivy.plugins.resolver.ChainResolver
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.DualResolver
import org.apache.ivy.plugins.resolver.FileSystemResolver
import org.apache.ivy.plugins.resolver.IBiblioResolver
import org.apache.ivy.util.DefaultMessageLogger

import java.util.logging.Logger

@CompileStatic
class IvyDepResolver2 implements MavenDependenciesResolver {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String defaultS = 'default'
    public static String sourcesS = 'sources'

    Ivy ivy;

    static File userHome = System.getProperty("user.home") as File

    public ChainResolver chainResolver;
    int log1 = org.apache.ivy.util.Message.MSG_INFO;
    String log2 = ResolveOptions.LOG_DOWNLOAD_ONLY;
    DependencyResolverDebugger dependencyResolverDebugger;
    IvySettings ivySettings;
//    ResolutionCacheDebugManager resolutionCacheDebugManager = new ResolutionCacheDebugManager();

    IBiblioResolver failBackDr;
    JrrDefaultResolutionCacheManager resolutionCacheManager;

    DefaultMessageLogger defaultMessageLogger = new DefaultMessageLogger(log1) {
        @Override
        void log(String msg, int level) {
            super.log(msg, level)
            if (level <= this.level) {
//                    Thread.dumpStack()
            }
        }

        @Override
        void debug(String msg) {
//                if (msg.contains('resolver not found:')) {
//                    super.error(msg)
//                } else {
            super.debug(msg)
//                }
        }

        @Override
        void error(String msg) {
            super.error(msg)
        }
    }

    List<DependencyResolver> customRepos = []

    IvyDepResolver2() {
    }

    /**
     * Level one of
     * @see org.apache.ivy.util.Message
     */
    void setLogLevel(int logLevel) {
        JrrClassUtils.setFieldValue(defaultMessageLogger, 'level', logLevel)
//        defaultMessageLogger.@level = logLevel
    }

    void setLogDebug() {
        setLogLevel(org.apache.ivy.util.Message.MSG_DEBUG)
        log2 = ResolveOptions.LOG_DEFAULT
    }

    void setLogDefault() {
        setLogLevel(org.apache.ivy.util.Message.MSG_INFO)
        log2 = ResolveOptions.LOG_DOWNLOAD_ONLY
    }


    static IvyDepResolver2 setDepResolverImpl() {
        IvyDepResolver2 ivyDepResolver2 = new IvyDepResolver2();
        ivyDepResolver2.initIvySettings()
        MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver = ivyDepResolver2
        return ivyDepResolver2;

    }

    static boolean setDepResolver() {
        if (MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver == null) {
            setDepResolverImpl()
            return true
        } else {
            log.info "dependcy resolver already set : ${MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver}"
            return false
        }
    }

    void initIvySettings() {
        IvySettings ivySettings = buildSettings()
        initIvySettingsImpl(ivySettings)
    }

    private boolean defaultResolverSet = false;

    IvySettings buildSettings() {

        org.apache.ivy.util.Message.setDefaultLogger(defaultMessageLogger)
        ivySettings = new IvySettings() {
            @Override
            void setDefaultResolver(String resolverName) {
                if (defaultResolver) {
                    log.info "default resolver already set, ignore default ${resolverName}"
                } else {
                    super.setDefaultResolver(resolverName)
                }
            }
        };
//        log.info "cp1"
        File cache = getIvyCachDir()
        JrrUtilities3.checkFileExist(cache)
//        FileUtils.deleteQuietly(cache)
//        cache.mkdir()
        ivySettings.setDefaultCache(cache);

        resolutionCacheManager = new JrrDefaultResolutionCacheManager(
                ivySettings.getDefaultResolutionCacheBasedir())

//        resolutionCacheDebugManager.resolutionCacheManager = ivySettings.resolutionCacheManager
        resolutionCacheManager.setSettings(ivySettings)
        ivySettings.resolutionCacheManager = resolutionCacheManager;

        chainResolver = buildRepoChain()
        ivySettings.addResolver(chainResolver)
        DependencyResolver dr3 = chainResolver
        dependencyResolverDebugger = new DependencyResolverDebugger(chainResolver, failBackDr)
//        dependencyResolverDebugger.dependencyResolver =
        dr3 = dependencyResolverDebugger
        assert dependencyResolverDebugger.ibiblio != null
        dependencyResolverDebugger.addResolver(ivySettings)
//        if (false) {
//            dependencyResolverDebugger.dependencyResolver = defaultDr
//            dependencyResolverDebugger.addResolver(ivySettings)
//            dr3 = dependencyResolverDebugger
//        }
//        log.info "${defaultDr.name}"

//        log.info "${ivySettings.repositoryCacheManagers.length}"
//        log.info "${ivySettings.repositoryCacheManagers.toList()}"
        ivySettings.addResolver(dr3);
//        ivySettings.logResolvedRevision()
//        ivySettings.resolve
        ivySettings.setDefaultResolver(dr3.getName());
        return ivySettings
    }

    void addResolverAfterInit(DependencyResolver dependencyResolver) {
        chainResolver.add(dependencyResolver)
        ivySettings.addResolver(dependencyResolver)
    }

    void addResolverAfterInit(IBiblioRepository repoEnum) {
        addResolverAfterInit(buildResolver(repoEnum))
    }


    void initIvySettingsImpl(IvySettings ivySettings) {
        ivy = Ivy.newInstance(ivySettings);
//        log.info "cp1"
        ivy.getLoggerEngine().pushLogger(defaultMessageLogger)
//        log.info "cp2"
    }

    DependencyResolver buildMainResolver() {
        return buildDualResolver();
    }

    DualResolver buildDualResolver() {
        DualResolver dualResolver = new DualResolver();
        dualResolver.name = 'dualresolver'
        dualResolver.add(buildLocalGrapeRepo())
        dualResolver.add(buildPublicIbiblio())
        return dualResolver
    }

    DualResolver buildDualResolver3(DependencyResolver first, DependencyResolver second) {
        DualResolver dualResolver = new DualResolver();
        dualResolver.name = "${first.name}.${second.name}"
        dualResolver.add(first)
        dualResolver.add(second)
        return dualResolver
    }

    DependencyResolver buildDualResolver2(List<DependencyResolver> list) {
        if (list.size() == 0) {
            throw new IllegalStateException('no repos')
        }
        DependencyResolver dualResolverBefore = null
        list.each {
            if (dualResolverBefore == null) {
                dualResolverBefore = it
            } else {
                dualResolverBefore = buildDualResolver3(dualResolverBefore, it);
            }
        }

        return dualResolverBefore
    }


    DependencyResolver buildLocalRepo3() {
        List<DependencyResolver> dr = [];
        dr.add(buildLocalIvyRepo());
        dr.add buildLocalMavenRepo()
        dr.add buildLocalGrapeRepo()
//        dr.add buildPublicIbiblio()
        return buildDualResolver2(dr)
    }


    DependencyResolver buildRepoChain2() {
        ChainResolver chainResolver = buildRepoChain();
        return buildDualResolver3(chainResolver, buildPublicIbiblio());
    }

    ChainResolver buildRepoChain() {
        ChainResolver chainResolver = new ChainResolver();
        chainResolver.returnFirst = false
        chainResolver.dual = true
        chainResolver.name = 'jrrchainresolver'
//        chainResolver.name = IvyDepResolver2.defaultS
        chainResolver.add(buildLocalIvyRepo())
        chainResolver.add(buildLocalGrapeRepo())
        if (getMavenLocalDir() != null && getMavenLocalDir().exists()) {
            chainResolver.add(buildLocalMavenRepo())
        }
        chainResolver.add(buildPublicIbiblio())
        customRepos.each {
            chainResolver.add it
        }
//        chainResolver.add(mavenRepositories.buildJavassh())
        if (getMavenLocalDir() != null && getMavenLocalDir().exists()) {
            chainResolver.add(buildLocalMavenRepo())
        }
//        chainResolver.returnFirst = false
        return chainResolver
    }

    File getIvyCachDir() {
        return MavenDefaultSettings.mavenDefaultSettings.grapeLocalDir;
    }

    IBiblioResolver buildBintray() {
        // https://jitpack.io/
        IBiblioResolver local = new IBiblioResolver();
        local.m2compatible = false
//        local.usepoms = true
        local.root = 'https://jcenter.bintray.com/'
        local.name = 'bintray'
        return local
    }

    IBiblioResolver buildPublicIbiblio() {
        IBiblioResolver local = buildPublicIbiblioCustom('ibiblio', MavenDefaultSettings.mavenDefaultSettings.mavenServer);
        failBackDr = local
        return local
    }

    IBiblioResolver buildPublicIbiblioCustom(String name, String root) {
        IBiblioResolver local = new IBiblioResolver();
//        local.setCheckconsistency(false)
//        local.setCache(null)
        local.root = root
        local.m2compatible = true
//        local.usepoms = true
        local.name = name
        return local
    }


    IBiblioResolver buildLocalMavenRepo() {
        IBiblioResolver local = new IBiblioResolver();
        local.name = 'localm2'
        local.root = getMavenLocalDir().toURL().toString()
        local.m2compatible = true
        local.changingMatcher = 'regexp'
        local.changingPattern = '.*'
//        local.namespace
        local.checkmodified = true
        return local
    }

    FileSystemResolver buildIvyLocalRepo(File baseDir) {
        FileSystemResolver local = new FileSystemResolver();
        local.addIvyPattern("${baseDir.absolutePath}/[organisation]/[module]/ivy-[revision].xml")
        local.addArtifactPattern("${baseDir.absolutePath}/[organisation]/[module]/[type]s/[artifact]-[revision](-[classifier]).[ext]")
        return local
    }


    FileSystemResolver buildLocalGrapeRepo() {
//        local.ivyroot = new File(userHome, '.ivy2/cache/').toURL().toString()
        File baseDir = new File(userHome, ".groovy/grapes")
        FileSystemResolver local = buildIvyLocalRepo(baseDir);
        local.name = 'localgrape'
        return local
    }


    FileSystemResolver buildLocalIvyRepo() {
        File baseDir = getIvyCachDir()
        FileSystemResolver local = buildIvyLocalRepo(baseDir);
        local.name = 'localivy'
        return local
    }

    @Override
    void downloadMavenPath(MavenPath path, boolean dep) {

    }

    @Override
    void downloadPathImplSpecific(String path, boolean dep) {

    }

    @Override
    List<MavenId> resolveAndDownloadDeepDependencies(MavenId mavenId, boolean downloadSource3, boolean dep) {
        List<MavenId> downloaded = []
        ResolveReport resolveReport = downloadIvyImpl(mavenId, dep, downloaded)
        if (resolveReport.hasError()) {
            throw new IvyDepResolverException(resolveReport);
        }

        downloaded = downloaded.unique()
//        log.info "downloadSource3 : ${downloadSource3} ${downloaded}"
//            Thread.dumpStack()
        if (downloadSource3) {
            downloaded.each {
                downloadSource(it);
            }
        }
        return downloaded
    }


    DefaultModuleDescriptor prepare(MavenId mavenId, String depConf, boolean dep, ResolveOptions ro) {
        String[] confs = [IvyDepResolver2.defaultS]
        ro.setConfs(confs)
        ro.setLog(log2)
        ro.setTransitive(true);
        ro.setDownload(true);
        ModuleRevisionId moduleRevisionId
        if (mavenId.modification == null) {
            moduleRevisionId = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId + "-envelope", mavenId.version)
        } else {
            moduleRevisionId = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId + "-envelope", mavenId.modification, mavenId.version)
        }
        DefaultModuleDescriptor md = DefaultModuleDescriptor.newDefaultInstance(moduleRevisionId);
        ModuleRevisionId ri
        if (mavenId.modification == null) {
            ri = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId, mavenId.version);
        } else {
            ri = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId,mavenId.modification,  mavenId.version);
        }
        DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(md, ri, false, false, dep);
        dd.addDependencyConfiguration(IvyDepResolver2.defaultS, depConf);
//        if (downloadSource) {
//            dd.addDependencyConfiguration(IvyDepResolver2.defaultS, 'sources');
//        } else {
//            dd.addDependencyConfiguration(IvyDepResolver2.defaultS, IvyDepResolver2.defaultS);
//        }
        md.addDependency(dd);

        return md;
    }

    ResolveReport downloadIvyImpl(MavenId mavenId, boolean dep, List<MavenId> downloaded) {
        if (ivy == null) {
            throw new NullPointerException('ivy is null')
        }
        ResolveOptions ro = new ResolveOptions();
        DefaultModuleDescriptor md = prepare(mavenId, IvyDepResolver2.defaultS, dep, ro)
        ResolveReport rr = ivy.resolve(md, ro);
//        if (rr.hasError()) {
//            rr.getAllProblemMessages().each {
//                java.lang.Object obj = it
//                log.info "${obj.class.name} ${obj}"
//            }
//            throw new IvyDepResolverException(rr)
////            throw new RuntimeException(rr.getAllProblemMessages().toString());
//        }
        List<MavenId> mavenIds = rr.getAllArtifactsReports().toList().collect {
//            log.info "${it.localFile}"
            ModuleRevisionId r = it.artifact.moduleRevisionId
            new MavenId(r.organisation, r.name, r.revision,r.branch)
        }
        downloaded.addAll(mavenId)
        downloaded.addAll(mavenIds)
        if (false) {
            List<MavenId> additionalDeps = []
            log.info "cp1 = ${rr.dependencies.size()}"
            rr.dependencies.each { Object ivyNode2 ->
                IvyNode ivyNode = ivyNode2 as IvyNode
                ModuleDescriptor descriptor = ivyNode.descriptor;
                log.info "${descriptor}"
                if (descriptor instanceof PomModuleDescriptorBuilder.PomModuleDescriptor) {
                    PomModuleDescriptorBuilder.PomModuleDescriptor desc = (PomModuleDescriptorBuilder.PomModuleDescriptor) descriptor;
                    Map<ModuleId, PomDependencyMgt> map = desc.dependencyManagementMap as Map<ModuleId, PomDependencyMgt>
                    List<PomDependencyMgt> deps = map.collect { it.value };
                    deps = deps.findAll { it.scope != 'test' }
                    additionalDeps.addAll(deps.collect { new MavenId(it.groupId, it.artifactId, it.version) })
                }
            }
            additionalDeps = additionalDeps - downloaded
            log.info "additionalDeps : ${additionalDeps}"
            additionalDeps.each {
                if (!downloaded.contains(it)) {
                    downloadIvyImpl(it, true, downloaded)
                }
            }
        }
        return rr;
    }

    @Override
    void downloadSource(MavenId mavenId) {
        downloadCustomPackage(mavenId, sourcesS)
    }

    void downloadCustomPackage(MavenId mavenId, String packageId) {
        ResolveOptions ro = new ResolveOptions();
        DefaultModuleDescriptor md = prepare(mavenId, packageId, false, ro)
        ResolveReport rr = ivy.resolve(md, ro);
        if (rr.hasError()) {
//            rr.getAllProblemMessages().each {
//                java.lang.Object obj = it
//                log.info "${obj.class.name} ${obj}"
//            }
            throw new RuntimeException(rr.getAllProblemMessages().toString());
        }
        List<MavenId> mavenIds = rr.getAllArtifactsReports().toList().collect {
            log.info "${it.localFile}"
            ModuleRevisionId r = it.artifact.moduleRevisionId
            new MavenId(r.organisation, r.name, r.revision)
        }
    }

    @Override
    File getMavenLocalDir() {
        return MavenDefaultSettings.mavenDefaultSettings.mavenLocalDir
    }

    @Override
    URL getMavenRepoUrl() {
        return new URL(failBackDr.root)
    }


    IBiblioResolver buildResolver(IBiblioRepository mavenRepositoriesEnum) {
        return buildPublicIbiblioCustom(mavenRepositoriesEnum.name(), mavenRepositoriesEnum.getUrl())
    }

}
