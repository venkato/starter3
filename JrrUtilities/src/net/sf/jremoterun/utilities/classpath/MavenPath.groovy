package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable

@EqualsAndHashCode
@CompileStatic
@Sortable
public class MavenPath implements Serializable {

    String groupId;
    String artifactId;
    String version;
    String suffix;

    // used in reflection
    private MavenPath(){

    }

    public MavenPath(String mavenId) {
        List<String> ids = mavenId.tokenize(':')
        assert ids.size() == 4
        groupId = ids[0]
        artifactId = ids[1]
        version = ids[2]
        suffix = ids[3]
    }

    public MavenPath(String groupId, String artifactId, String version, String suffix) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        this.suffix = suffix
    }

    public MavenPath normalize() {
        String groupId1 = groupId.trim()
        String artifactId1 = artifactId.trim()
        String version1 = version.trim()
        String suffix1 = suffix.trim()
        return new MavenPath(groupId1, artifactId1, version1, suffix1)
    }


    @Override
    public String toString() {
        return "${groupId}:${artifactId}:${version}:${suffix}";
    }

    String buildMavenPath(){
        String groupId2 = groupId.replace('.', '/')
        return "${groupId2}/${artifactId}/${version}/${artifactId}-${version}${suffix}";
    }


    String buildMavenPath2(){
        List<String> classifiedAndExtention = suffix.substring(1).tokenize('.')
        return "${groupId}:${artifactId}:${classifiedAndExtention[1]}:${classifiedAndExtention[0]}:${version}";
    }
}
