package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
abstract class AddFilesToClassLoaderGroovySave extends AddFilesToClassLoaderGroovy implements AddFilesWithSourcesI{

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

//    abstract List getElements() ;

    abstract protected void addElement(Object obj);

    abstract List getSourceElements() ;

    @Override
    void addFileImpl(File file) throws Exception {
        assert file.exists()
        addElement(file)
    }

    @Override
    void addM(MavenId artifact) throws IOException {
        addElement(artifact)
    }

    @Override
    void addU(MavenId artifact) throws IOException {
        addElement(artifact)
    }

    @Override
    void addUrl(URL url) {
        addElement(url)
    }

    @Override
    void addMavenPath(MavenPath mavenPath) {
        addElement(mavenPath)
    }

    @Override
    void addBinaryWithSource(BinaryWithSourceI fileWithSource) throws Exception {
        addElement(fileWithSource)
    }

    @Override
    void addGenericEntery(Object object) {
        if (object instanceof File) {
            File  f= (File) object;
            assert f.exists()
        }
        if( object instanceof Collection ) {
            throw new IllegalArgumentException("Collection : ${object}")
        }
        addElement(object)
    }

    @Override
    void addSourceGenericAll(List objects) {
        objects.each {addSourceGeneric(it)}
    }

    void addSourceF(File source) throws Exception{
        getSourceElements().add(source);
    }

    void addSourceM(MavenId mavenId){
        getSourceElements().add(mavenId);
    }

    @Override
    void addSourceGeneric(Object object) {
        if( object instanceof Collection ) {
            throw new IllegalArgumentException("Collection : ${object}")
        }
        getSourceElements().add(object);
    }

    @Override
    void addSourceS(String source) throws Exception {
        getSourceElements().add(source);
    }
}
