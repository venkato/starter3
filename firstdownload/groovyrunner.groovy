void addFile(File file) {
    assert file.exists()
    assert file.canRead()
    URLClassLoader cll = detectCLassLoader()
    cll.addURL(file.toURL())
}

URLClassLoader detectCLassLoader(){
    boolean useSystemClassLoader = 'true'.equalsIgnoreCase(System.getProperty('jrrcasspathAddToSystemClassLoader'))
    URLClassLoader cll
    if (useSystemClassLoader) {
        cll = ClassLoader.getSystemClassLoader() as URLClassLoader
    } else {
        cll = this.getClass().classLoader as GroovyClassLoader;
    }
    return cll;
}

static File detectBaseDir() {
    ClassLoader classLoader = GroovyObject.classLoader
    URL resource = classLoader.getResource(GroovyObject.name.replace('.', '/') + '.class')
    String res2 = resource.toString();
    if (res2.startsWith("jar:")) {
        res2 = res2.substring(4, res2.length());
    }
    res2 = res2.replace('!/groovy/lang/GroovyObject.class', '')
    URL url3 = new URL(res2)
    File baseDir2 = url3.file as File
    assert baseDir2.exists();
    return baseDir2.parentFile.parentFile.parentFile
}


File GR_HOME = detectBaseDir()
addFile new File(GR_HOME, "JrrInit/src")
//List<String> args2 = new ArrayList<>(args.toList());
//assert args2.size() > 0
String classToRun = 'net.sf.jremoterun.utilities.init.commonrunner.CommonRunner';

GroovyClassLoader cl = this.getClass().classLoader as GroovyClassLoader;
Object groovyMethodRunner = cl.loadClass(classToRun).newInstance()
groovyMethodRunner.jrrRunScript(this, args)



