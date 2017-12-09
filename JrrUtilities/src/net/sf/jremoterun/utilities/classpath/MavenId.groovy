package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable

@EqualsAndHashCode
@CompileStatic
public class MavenId implements Serializable, MavenIdContains,ToFileRef2 {

    /**
     * First
     */
    String groupId;

    /**
     * Second
     */
    String artifactId;
    String version;
    String modification;

    // used in reflection
    private MavenId() {}

    public MavenId(String m) {
        List<String> ids = m.tokenize(':')
        int size = ids.size()
        if (size <3 ||size>4) {
            throw new IllegalArgumentException("Bad: ${m}, got count = ${size}")
        }
        groupId = ids[0]
        artifactId = ids[1]
        version = ids[2]
        if(size==4){
            modification = ids[3]
        }
    }

    public MavenId(Map<String, String> map) {
        this(map.get('group'), map.get('name'), map.get('version'))

    }

    public MavenId(String groupId, String artifactId, String version) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
    }
    public MavenId(String groupId, String artifactId, String version,String modification) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.modification = modification
    }

    public MavenId normalize() {
        String groupId1 = groupId.trim()
        String artifactId1 = artifactId.trim()
        String version1 = version.trim()
        return new MavenId(groupId1, artifactId1, version1,modification)
    }


    @Override
    public String toString() {
        if(modification==null) {
            return "${groupId}:${artifactId}:${version}";
        }
        return "${groupId}:${artifactId}:${version}:${modification}";

    }

    @Override
    MavenId getM() {
        return this
    }

    boolean isGroupAndArtifact(MavenIdContains other){
        MavenId mOther = other.m
        return mOther.groupId == groupId && mOther.artifactId == artifactId
    }

    @Override
    File resolveToFile() {
        AddFilesToClassLoaderCommonDummy classLoaderCommonDummy = new AddFilesToClassLoaderCommonDummy()
        classLoaderCommonDummy.addM this
        if(classLoaderCommonDummy.addedFiles2.size()==0){
            throw new FileNotFoundException(this.toString())
        }
        return classLoaderCommonDummy.addedFiles2[0]
    }


}
