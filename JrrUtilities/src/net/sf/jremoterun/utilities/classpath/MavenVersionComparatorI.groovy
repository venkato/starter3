package net.sf.jremoterun.utilities.classpath;

import net.sf.jremoterun.utilities.JrrClassUtils;

import java.util.logging.Logger;

import groovy.transform.CompileStatic;

@CompileStatic
public interface MavenVersionComparatorI {

    /**
     * Return true if 3th param more then 2nd param
     */
    boolean isOverrideMavenId2(MavenId mavenId, String versionSaved, String versionCandidate);


}
