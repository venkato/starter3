package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable

@CompileStatic
public class MavenId implements Serializable, MavenIdContains, ToFileRef2 {

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
        if(m.contains(' ')){
            throw new IllegalArgumentException("Maven id contains space : ${m}")
        }
        List<String> ids = m.tokenize(':')
        int size = ids.size()
        if (size < 3 || size > 4) {
            throw new IllegalArgumentException("Bad: ${m}, got count = ${size}")
        }
        groupId = ids[0]
        artifactId = ids[1]
        version = ids[2]
        if (size == 4) {
            modification = ids[3]
        }
        check()
    }

    public MavenId(Map<String, String> map) {
        this(map.get('group'), map.get('name'), map.get('version'))
    }

    public MavenId(String groupId, String artifactId, String version) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        check()
    }

    private void check(){
        if(groupId==null){
            throw new NullPointerException('groupid is null')
        }
        if(artifactId==null){
            throw new NullPointerException('artifactId is null')
        }
        if(version==null){
            throw new NullPointerException('version is null')
        }
    }

    public MavenId(String groupId, String artifactId, String version, String modification) {
        this(groupId,artifactId,version)
        this.modification = modification
    }

    public MavenId normalize() {
        String groupId1 = groupId.trim()
        String artifactId1 = artifactId.trim()
        String version1 = version.trim()
        return new MavenId(groupId1, artifactId1, version1, modification)
    }


    @Override
    public String toString() {
        if (modification == null) {
            return "${groupId}:${artifactId}:${version}";
        }
        return "${groupId}:${artifactId}:${version}:${modification}";

    }

    @Override
    MavenId getM() {
        return this
    }

    boolean isGroupAndArtifact(MavenIdContains other) {
        MavenId mOther = other.m
        return mOther.groupId == groupId && mOther.artifactId == artifactId
    }

    @Override
    File resolveToFile() {
        AddFilesToClassLoaderCommonDummy classLoaderCommonDummy = new AddFilesToClassLoaderCommonDummy()
        classLoaderCommonDummy.addM this
        if (classLoaderCommonDummy.addedFiles2.size() == 0) {
            throw new FileNotFoundException(this.toString())
        }
        return classLoaderCommonDummy.addedFiles2[0]
    }

    boolean equals(o) {
        if(o==null){
            return false
        }
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        MavenId mavenId = (MavenId) o;

        if (artifactId != mavenId.artifactId) return false;
        if (groupId != mavenId.groupId) return false;
        if (modification != mavenId.modification) return false;
        if (version != mavenId.version) return false;

        return true;
    }

    int hashCode() {
        int result
        result = (groupId != null ? groupId.hashCode() : 0)
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (modification != null ? modification.hashCode() : 0)
        return result
    }
}
