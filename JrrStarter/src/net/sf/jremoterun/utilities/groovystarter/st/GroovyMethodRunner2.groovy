package net.sf.jremoterun.utilities.groovystarter.st

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils
import net.sf.jremoterun.utilities.NewValueListener
import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.ConsoleSymbols
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunner
import net.sf.jremoterun.utilities.groovystarter.GroovyMethodRunnerParams
import net.sf.jremoterun.utilities.groovystarter.JrrStarterConstatnts

import net.sf.jremoterun.utilities.groovystarter.JrrStarterVariables2
import net.sf.jremoterun.utilities.java11.Java11ModuleSetDisable

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.logging.Level
import java.util.logging.Logger

@CompileStatic
public class GroovyMethodRunner2 implements Runnable {

    private static final Logger log = Logger.getLogger(GroovyMethodRunner.getName());

    volatile GroovyMethodRunnerParams gmrp = GroovyMethodRunnerParams.gmrpn
    volatile GroovyMethodRunnerParams2 gmrp2 = GroovyMethodRunnerParams2.getInstance()
    public GroovyMethodFinder groovyMethodFinder = new GroovyMethodFinder();

    GroovyMethodRunner2() {
    }

    public NewValueListener<GroovyMethodFinderException> methodNotFound = new NewValueListener<GroovyMethodFinderException>() {

        @Override
        void newValue(GroovyMethodFinderException e) {
            gmrp.groovyMethodRunner.systemExit "${e}"
        }
    }

    void run() {
        selfCheck()
        gmrp2.groovyMethodRunner2 = this;
        gmrp.setPhaseChanger(JrrRunnerPhase2.setSystemOut, this.&setSysOut)
        gmrp.setPhaseChanger(JrrRunnerPhase2.java11ModuleDisable, this.&java11ModuleDisable)
        gmrp.setPhaseChanger(JrrRunnerPhase2.userConfigEnriched, this.&userConfigEnriched)
        gmrp.setPhaseChanger(JrrRunnerPhase2.userConfigWinEnriched, this.&loadUserConfigShared)
        gmrp.setPhaseChanger(JrrRunnerPhase2.userConfig2Enriched, this.&loadUserConfig2Enriched)
        gmrp.setPhaseChanger(JrrRunnerPhase2.hostConfigLinuxEnriched, this.&loadHostLinuxConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase2.hostConfigWindowsEnriched, this.&loadHostWindowConfig)
        gmrp.setPhaseChanger(JrrRunnerPhase2.directoryConfigEnriched, this.&directoryConfigEnriched)
        // gmrp.setPhaseChanger(JrrRunnerPhase2.userConfigEnriched, this.&userConfigEnriched)
        gmrp.setPhaseChanger(JrrRunnerPhase2.addClassPathFiles, this.&addClassPathFiles)
        gmrp.setPhaseChanger(JrrRunnerPhase2.findMainClass, this.&findMainClass)
//        gmrp.setPhaseChanger(JrrRunnerPhase2.runUserScript,this.&runUserScript)
        gmrp.setPhaseChanger(JrrRunnerPhase2.classImplementionTasks, this.&classImplementationTasks)
        gmrp.setPhaseChanger(JrrRunnerPhase2.createClassInstance, this.&createClassInstance)
        gmrp.setPhaseChanger(JrrRunnerPhase2.checkIfMainJavaMethod, this.&checkIfMainJavaMethod)
        gmrp.setPhaseChanger(JrrRunnerPhase2.runTargetMethod, this.&runTargetMethod)
        gmrp.setPhaseChanger(JrrRunnerPhase2.normalExit, this.&onNormalExit)
//        gmrp.setPhaseChanger(JrrRunnerPhase2.,this.&)
    }


    void java11ModuleDisable() {
        Java11ModuleSetDisable.doIfNeeded();
    }

    void selfCheck() {
        if (gmrp2.groovyMethodRunner2 != null) {
            gmrp2.creationCallStack.printStackTrace()
            throw new Exception('GroovyMethodRunner2 was initialized before')
        } else {
            gmrp2.creationCallStack = new Exception('Call stack')
        }

    }


    void setSysOut() {
        assert gmrp2.stdOutProxy == null
        assert gmrp2.stdErrProxy == null
        assert gmrp2.stdOut == null
        assert gmrp2.stdErr == null
        assert gmrp.fileOut == null

        SetConsoleOut2.setConsoleOutIfNotInited()
        gmrp2.stdOut = SetConsoleOut2.originalOut
        gmrp2.stdErr = SetConsoleOut2.originalErr
        gmrp2.stdOutProxy = SetConsoleOut2.proxyOut
        gmrp2.stdErrProxy = SetConsoleOut2.proxyErr
    }

    void loadUserConfigShared() {
        if (gmrp2.loadUserWindowsConfigShared) {
            File homeFromWindows = gmrp.detectHomeFromWindows()
            if (homeFromWindows != null) {
                String path1 = homeFromWindows.canonicalFile.absolutePath.replace('\\', '/')
                String path2 = gmrp.userHome.canonicalFile.absolutePath.replace('\\', '/')
                if (path1 != path2) {
                    // otherwise file loaded
                    File f = new File(homeFromWindows, JrrStarterConstatnts.configFileName)
                    if (f.exists()) {
                        gmrp.groovyMethodRunner.loadScriptFromFile(f)
                    }
                }
            }
        }
    }

    void loadUserConfig2Enriched() {
        if (JrrStarterVariables2.getInstance().filesDir!=null) {
            File config2File = new File(JrrStarterVariables2.getInstance().filesDir,JrrStarterConstatnts.configFileName)
            if(config2File.exists()) {
                gmrp.groovyMethodRunner.loadScriptFromFile(config2File);
            }
        }
    }

    void userConfigEnriched() {
        if (gmrp2.groovyUserConfig != null && gmrp2.groovyUserConfig.exists()) {
            gmrp.groovyMethodRunner.loadScriptFromFile(gmrp2.groovyUserConfig)
        }
    }

    void directoryConfigEnriched() {
        if (gmrp2.directoryConfig != null && gmrp2.directoryConfig.exists()) {
            gmrp.groovyMethodRunner.loadScriptFromFile(gmrp2.directoryConfig)
        }
    }


    void loadHostLinuxConfig() {
        if (gmrp2.groovyHostConfigLinux != null && gmrp2.groovyHostConfigLinux.exists()) {
            gmrp.groovyMethodRunner.loadScriptFromFile(gmrp2.groovyHostConfigLinux)
        }
    }

    void loadHostWindowConfig() {
        if (gmrp2.groovyHostConfigWindows != null && gmrp2.groovyHostConfigWindows.exists()) {
            gmrp.groovyMethodRunner.loadScriptFromFile(gmrp2.groovyHostConfigWindows)
        }
    }

    void onNormalExit() {
        gmrp2.onNormalExit.run();
    }

    void findMainClass() {
        if (gmrp2.mainClassFound == null && gmrp2.scriptNameInstance == null) {
            gmrp2.mainClassFound = findMainClassImpl();
        }
    }


    Class findMainClassImpl() {
        if (gmrp2.annotationParser2 && gmrp2.annotationParser3 == null) {
            gmrp2.annotationParser3 = new DefaultClassPreProcessor(gmrp2, gmrp);
        }
        ClassPreProcessor annotationParser = gmrp2.annotationParser3
        if (gmrp2.mainClass != null) {
            ClRef clRef = gmrp2.mainClass.clRef
            if (clRef.className == ConsoleSymbols.question.s) {
                gmrp.groovyMethodRunner.systemExit("Not loading ${ConsoleSymbols.question}, exiting ...")
            }
            if (annotationParser != null) {
                annotationParser.detectAnnotationsOnMainCLass1(clRef.className)
            }
            return clRef.loadClass(gmrp.groovyClassLoader)
        }
        File scriptName
        if (gmrp2.runnerGroovyFile == null) {
            if (gmrp.args.size() == 0) {
                gmrp.groovyMethodRunner.printUsageAndExit()
                return null;
            }
            String fileOrClassName = gmrp.args[0] as File
            if (fileOrClassName == ConsoleSymbols.question.s) {
                gmrp.groovyMethodRunner.printUsageAndExit()
                return null;
            }
            gmrp.args.remove(0)
            scriptName = fileOrClassName as File
            if (!scriptName.exists()) {
//                try {
                if (annotationParser != null) {
                    annotationParser.detectAnnotationsOnMainCLass1(fileOrClassName)
                }
                Class mainClassImpl23 = gmrp.groovyClassLoader.loadClass(fileOrClassName);
                return mainClassImpl23
            }
        } else {
            scriptName = gmrp2.runnerGroovyFile
        }
        try {
            scriptName = scriptName.canonicalFile.absoluteFile
        } catch (IOException e) {
            log.log(Level.INFO, "failed resolve ${scriptName}", e)
            throw new FileNotFoundException("${scriptName}");
        }
        if (!scriptName.exists()) {
            gmrp.groovyMethodRunner.systemExit "Script not found : ${scriptName}";
        }
        if (!scriptName.isFile()) {
            gmrp.groovyMethodRunner.systemExit "Not a file : ${scriptName}";
        }
        if (annotationParser != null) {
            annotationParser.detectAnnotationsOnMainCLass2(scriptName.text)
        }
        return gmrp.groovyClassLoader.parseClass(scriptName)
    }

    void classImplementationTasks() {
        if (gmrp2.mainClassFound == null) {
            throw new IllegalStateException("gmrp2.mainClassFound is null")
        }
        if (ConsoleFormatterColor.isAssignableFrom(gmrp2.mainClassFound)) {
            JdkLogFormatter.setLogFormatter()
        }
    }


    void checkIfMainJavaMethod() {
        if (gmrp2.runMainJavaMethod == null) {
            boolean found = false
            if (gmrp.args.size() > 0 && gmrp.args[0] == 'main') {
                List<Method> methods = gmrp2.mainClassFound.getMethods().toList().findAll {
                    it.name == 'main'
                }.findAll {
                    Modifier.isStatic(it.getModifiers())
                }
                if(methods.size()==1){
                    found = true
                }
            }
            gmrp2.runMainJavaMethod = found
            if(found){
                gmrp.args.remove(0)
            }else{

            }
        } else {

        }

    }

    void createClassInstance() {
        if (gmrp2.runMainJavaMethod) {

        } else {
            if (gmrp2.scriptNameInstance == null) {
                assert gmrp2.mainClassFound != null
                Object newInstance = gmrp2.mainClassFound.newInstance()
                if (newInstance instanceof MainClassInstanceSelector) {
                    newInstance = newInstance.getMainInstance()
                }
                assert newInstance != null
                gmrp2.scriptNameInstance = newInstance
            }
        }
    }

    void runTargetMethod() {
        try {
            List<String> args2 = new ArrayList<>(gmrp.args)
            if (gmrp2.runMainJavaMethod) {
                String[] args3 = args2.toArray(new String[0])
                JrrClassUtils.runMainMethod(gmrp2.mainClassFound,args3 ) //NOFIELDCHECK
            } else {
                gmrp2.result = groovyMethodFinder.runMethod(gmrp2.scriptNameInstance, args2)
            }
        } catch (GroovyMethodFinderException e) {
            methodNotFound.newValue(e)
        }
    }

    void addClassPathFiles() {
        String classPathFiles = System.getProperty(gmrp2.classPathProperty)
//        log.info "classPathFiles : ${classPathFiles}"
        if (classPathFiles == null) {
        } else {
            classPathFiles.tokenize(',').collect { it as File }.each {
                gmrp.addFilesToClassLoader.addFromGroovyFile(it)
            }
        }
    }

}
