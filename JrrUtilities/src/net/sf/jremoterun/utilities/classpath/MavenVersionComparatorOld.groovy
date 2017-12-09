/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Copied from http://ant.apache.org/ivy/ from class : org.apache.ivy.plugins.latest.LatestRevisionStrategy
 */
package net.sf.jremoterun.utilities.classpath

import groovy.transform.CompileStatic
import net.sf.jremoterun.utilities.JrrClassUtils

import java.util.logging.Logger

@CompileStatic
class MavenVersionComparatorOld implements MavenVersionComparatorI {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static boolean overrideNewAlgouse = true;

    @Override
    boolean isOverrideMavenId2(MavenId mavenId, String versionSaved, String versionCandidate) {
        boolean res = isOverrideMavenId(versionSaved, versionCandidate)
//        String result = res ? versionCandidate : versionSaved
        return res;
    }


    boolean isOverrideMavenId(String versionSaved, String versionCandidate) {
        versionSaved = versionSaved.toLowerCase()
        versionCandidate = versionCandidate.toLowerCase()

        boolean savedHasNonStableVersion = MavenDefaultSettings.mavenDefaultSettings.badMavenVersionWords.find {
            versionSaved.contains(it)
        } != null
        boolean candidateHasNonStableVersion = MavenDefaultSettings.mavenDefaultSettings.badMavenVersionWords.find {
            versionCandidate.contains(it)
        } != null
        if (savedHasNonStableVersion && !candidateHasNonStableVersion) {
            return true
        }
        if (!savedHasNonStableVersion && candidateHasNonStableVersion) {
            return false
        }
        log.info "${versionSaved} ${versionCandidate}"
        int i = versionSaved.indexOf('.')
        int j = versionCandidate.indexOf('.')
        if (overrideNewAlgouse) {
            String versionSaved2 = versionSaved;
            String versionCandidate2 = versionCandidate;
            String versionSavedRem = ''
            String versionCandidate2Rem = ''
            if (i != -1) {
                versionSaved2 = versionSaved.substring(0, i)
                versionSavedRem = versionSaved.substring(i + 1)
            }
            if (j != -1) {
                versionCandidate2 = versionCandidate.substring(0, j)
                versionCandidate2Rem = versionCandidate.substring(j + 1)
            }
            if (versionSaved2 == versionCandidate2) {
                if (versionSavedRem.length() == 0 && versionCandidate.length() == 0) {
                    log.info "starnge version to compare ${versionSaved} ${versionCandidate}"
                    return false
                }
                if (versionSavedRem.length() == 0) {
                    return true
                }
                if (versionCandidate2Rem.length() == 0) {
                    return false
                }
                return isOverrideMavenId( versionSavedRem, versionCandidate2Rem);
            } else {
                if (versionSaved2.length() != versionCandidate2.length() && versionSaved2.isInteger() && versionCandidate2.isInteger()) {
                    int versionSavedI = versionSaved2 as int
                    int versionCadidateI = versionCandidate2 as int
                    return versionCadidateI > versionSavedI
                }
                return versionCandidate > versionSaved
            }
        } else {
            if (i != -1 && j != -1) {
                String s1 = versionSaved.substring(0, i)
                String s2 = versionCandidate.substring(0, j)
                if (s1 == s2) {
                    String rem1 = versionSaved.substring(i + 1);
                    String rem2 = versionCandidate.substring(j + 1);
                    return isOverrideMavenId( rem1, rem2);
                } else {
                    return isOverrideMavenId( s1, s2);
                }
            } else {
                if (versionSaved.length() != versionCandidate.length() && versionSaved.isInteger() && versionCandidate.isInteger()) {
                    int versionSavedI = versionSaved as int
                    int versionCadidateI = versionCandidate as int
                    return versionCadidateI > versionSavedI
                }
                return versionCandidate > versionSaved
            }
        }
    }


}
