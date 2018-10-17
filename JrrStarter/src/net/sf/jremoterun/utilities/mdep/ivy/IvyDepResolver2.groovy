package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.classpath.MavenDefaultSettings
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver
import net.sf.jremoterun.utilities.classpath.MavenId
import net.sf.jremoterun.utilities.classpath.MavenPath
import org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.report.ArtifactDownloadReport
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.resolver.ChainResolver
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.DualResolver
import org.apache.ivy.plugins.resolver.FileSystemResolver
import org.apache.ivy.plugins.resolver.IBiblioResolver

import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
class IvyDepResolver2 implements MavenDependenciesResolver {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static String defaultS = 'default'
    public static String sourcesS = 'sources'
    //public String logResolveOption = LogOptions.LOG_DEFAULT

    JrrIvy ivy;

    public static File userHome = System.getProperty("user.home") as File

    public ChainResolver chainResolver;
    public static int log1Default = org.apache.ivy.util.Message.MSG_WARN;
    public static String log2Default = ResolveOptions.LOG_DOWNLOAD_ONLY;
    int log1 = log1Default;
    String log2 = log2Default;
    DependencyResolverDebugger dependencyResolverDebugger;
    JrrIvySettings ivySettings;
//    ResolutionCacheDebugManager resolutionCacheDebugManager = new ResolutionCacheDebugManager();

    JrrBiblioResolver failBackDr;
    JrrDefaultResolutionCacheManager resolutionCacheManager;

    JrrIvyMessageLogger defaultMessageLogger = new JrrIvyMessageLogger(log1);

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
            log.info "dependency resolver already set : ${MavenDefaultSettings.mavenDefaultSettings.mavenDependenciesResolver}"
            return false
        }
    }

    void initIvySettings() {
        IvySettings ivySettings = buildSettings()
        initIvySettingsImpl(ivySettings)
    }

//    private boolean defaultResolverSet = false;

    JrrIvySettings buildSettings() {

        org.apache.ivy.util.Message.setDefaultLogger(defaultMessageLogger)
        ivySettings = new JrrIvySettings();
//        log.info "cp1"
        File cache = getIvyCachDir()
        net.sf.jremoterun.utilities.JrrUtilitiesFile.checkFileExist(cache)
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


    void initIvySettingsImpl(JrrIvySettings ivySettings) {
        ivy = ivySettings.buildIvy()
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
        return MavenDefaultSettings.mavenDefaultSettings.grapeFileFinder.getMavenLocalDir2();
    }

    IBiblioResolver buildBintray() {
        // https://jitpack.io/
        IBiblioResolver local =  new JrrBiblioResolver();
        local.m2compatible = false
//        local.usepoms = true
        local.root = 'https://jcenter.bintray.com/'
        local.name = 'bintray'
        return local
    }

    IBiblioResolver buildPublicIbiblio() {
        JrrBiblioResolver local = buildPublicIbiblioCustom('ibiblio', MavenDefaultSettings.mavenDefaultSettings.mavenServer);
        failBackDr = local
        return local
    }

    JrrBiblioResolver buildPublicIbiblioCustom(String name, String root) {
        JrrBiblioResolver local = new JrrBiblioResolver();
//        local.setCheckconsistency(false)
//        local.setCache(null)
        local.root = root
        local.m2compatible = true
//        local.usepoms = true
        local.name = name
        return local
    }


    JrrBiblioResolver buildLocalMavenRepo() {
        JrrBiblioResolver local =  new JrrBiblioResolver();
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
    List<MavenId> resolveAndDownloadDeepDependencies(MavenId mavenId, boolean downloadSource, boolean dep, IBiblioRepository repo) {
        if (repo != null) {
            throw new UnsupportedOperationException("loading from custom repo not supported : ${repo}")
        }
        return resolveAndDownloadDeepDependencies(mavenId, downloadSource, dep)
    }

    void downloadDeepDependenciesSpec(MavenId mavenId, boolean dep, String ext) {
        downloadDeepDependenciesSpec(mavenId, dep, mavenId.artifactId, ext)
    }

    void downloadDeepDependenciesSpec(MavenId mavenId, boolean dep, String name, String ext) {
        List<MavenId> downloaded = []
        ResolveReport resolveReport = downloadIvyImpl(mavenId, dep, downloaded, name, ext)
        if (resolveReport.hasError()) {
            throw handledFailedDownload(mavenId, dep, resolveReport)
        }
    }

    @Override
    List<MavenId> resolveAndDownloadDeepDependencies(MavenId mavenId, boolean downloadSource3, boolean dep) {
        List<MavenId> downloaded = []
        ResolveReport resolveReport = downloadIvyImpl(mavenId, dep, downloaded, mavenId.artifactId, mavenId.modification)
        if (resolveReport.hasError()) {
            throw handledFailedDownload(mavenId, dep, resolveReport)
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

    Throwable handledFailedDownload(MavenId mavenId, boolean dep, ResolveReport resolveReport) {
        Collection<Exception> excs = resolveReport.getDependencies().collect {
            if (it instanceof org.apache.ivy.core.resolve.IvyNode) {
                org.apache.ivy.core.resolve.IvyNode ivyNode = (org.apache.ivy.core.resolve.IvyNode) it;
                return ivyNode.getProblem()
            }
            return null
        }.findAll { it != null }
        if (excs.size() == 1 && resolveReport.getAllProblemMessages().size() == 1) {
            Exception get1 = excs.get(0)
            return new IvyDepResolverException("failed resolve ${mavenId}", get1)

        }
        if (excs.size() > 0) {
            excs.each { log.log(Level.WARNING, "failed download ${mavenId}", it) }
        }
        return new IvyDepResolverException(resolveReport);
    }

    DefaultModuleDescriptor prepare(MavenId mavenId, String depConf, boolean dep, ResolveOptions ro, String name, String ext) {
        String[] confs = [IvyDepResolver2.defaultS]
        ro.setConfs(confs)
        ro.setLog(log2)
        ro.setTransitive(true);
        ro.setDownload(true);
        ModuleRevisionId moduleRevisionId
//        if (mavenId.modification == null) {
        moduleRevisionId = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId + "-envelope", mavenId.version)
//        } else {
//            moduleRevisionId = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId + "-envelope", mavenId.modification, mavenId.version)
//        }
        DefaultModuleDescriptor md = DefaultModuleDescriptor.newDefaultInstance(moduleRevisionId);
        ModuleRevisionId ri
//        if (mavenId.modification == null) {
        ri = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId, mavenId.version);
//        } else {
//            ri = ModuleRevisionId.newInstance(mavenId.groupId, mavenId.artifactId,mavenId.modification,  mavenId.version);
//        }
        DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(md, ri, false, false, dep);
//        dd.addExcludeRule(dep)
        // log4j-1.2.16.zip
        // name-1.2.16.zip
        if (ext != null) {
            DefaultDependencyArtifactDescriptor dad = new DefaultDependencyArtifactDescriptor(dd, name, ext, ext, null, null)
            dd.addDependencyArtifact(IvyDepResolver2.defaultS, dad)
        }
        dd.addDependencyConfiguration(IvyDepResolver2.defaultS, depConf);
//        if (downloadSource) {
//            dd.addDependencyConfiguration(IvyDepResolver2.defaultS, 'sources');
//        } else {
//            dd.addDependencyConfiguration(IvyDepResolver2.defaultS, IvyDepResolver2.defaultS);
//        }
        md.addDependency(dd);

        return md;
    }

    public static volatile boolean tryVariant = true


    ResolveReport downloadIvyImpl(MavenId mavenId, boolean dep, List<MavenId> downloaded, String name, String ext) {
        if (ivy == null) {
            throw new NullPointerException('ivy is null')
        }
        ResolveOptions ro = new ResolveOptions();
        DefaultModuleDescriptor md = prepare(mavenId, IvyDepResolver2.defaultS, dep, ro, name, ext)
        ResolveReport rr = ivy.resolve(md, ro);
        if(rr.hasError()){
            if(tryVariant &&  ext!=null){
                ResolveReport rr2= downloadIvyImpl(mavenId,dep,downloaded,name,null);
                if(!rr.hasError()){
                    return rr2
                }
            }
            return rr;
        }
        List<MavenId> mavenIds = rr.getAllArtifactsReports().toList().collect {
            return convertArtifactDownloadReport2MavenId(it)
        }
        downloaded.addAll(mavenId)
        downloaded.addAll(mavenIds)
        return rr;
    }


    MavenId convertArtifactDownloadReport2MavenId(ArtifactDownloadReport adr){
        ModuleRevisionId r = adr.getArtifact().getModuleRevisionId();
        return new MavenId(r.organisation, r.name, r.revision, r.branch)
    }

    @Override
    void downloadSource(MavenId mavenId) {
        downloadCustomPackage(mavenId, sourcesS)
    }

    @Override
    void downloadSource(MavenId mavenId, IBiblioRepository repo) {
        if (repo != null) {
            throw new UnsupportedOperationException("loading from custom repo not supported ${repo}")
        }
        downloadSource(mavenId)
    }

    ResolveReport downloadCustomPackage(MavenId mavenId, String packageId) {
        ResolveOptions ro = new ResolveOptions();
        DefaultModuleDescriptor md = prepare(mavenId, packageId, false, ro, null, null)
        ResolveReport rr = ivy.resolve(md, ro);
        if (rr.hasError()) {
//            rr.getAllProblemMessages().each {
//                java.lang.Object obj = it
//                log.info "${obj.class.name} ${obj}"
//            }
            throw handledFailedDownload(mavenId, false, rr)
            //throw new RuntimeException(rr.getAllProblemMessages().toString());
        }
//        List<MavenId> mavenIds = rr.getAllArtifactsReports().toList().collect {
//            log.info "${it.localFile}"
//            ModuleRevisionId r = it.artifact.moduleRevisionId
//            new MavenId(r.organisation, r.name, r.revision)
//        }
        return rr;
    }

    List<File> extractFilesFromReport(ResolveReport rr){
        ArtifactDownloadReport[] ar = rr.getAllArtifactsReports()
        List<File> files = ar.toList().collect { it.getLocalFile() }
        return files
    }


    @Override
    File getMavenLocalDir() {
        return MavenDefaultSettings.mavenDefaultSettings.mavenFileFinder.getMavenLocalDir2()
    }

    @Override
    URL getMavenRepoUrl() {
        return new URL(failBackDr.root)
    }


    IBiblioResolver buildResolver(IBiblioRepository mavenRepositoriesEnum) {
        return buildPublicIbiblioCustom(mavenRepositoriesEnum.name(), mavenRepositoriesEnum.getUrl())
    }

}
