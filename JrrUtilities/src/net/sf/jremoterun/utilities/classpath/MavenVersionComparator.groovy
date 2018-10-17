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
class MavenVersionComparator implements MavenVersionComparatorI {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static Map DEFAULT_SPECIAL_MEANINGS;

    static {
        DEFAULT_SPECIAL_MEANINGS = new HashMap();
        DEFAULT_SPECIAL_MEANINGS.put("dev", new Integer(-1));
        DEFAULT_SPECIAL_MEANINGS.put("rc", new Integer(1));
        DEFAULT_SPECIAL_MEANINGS.put("final", new Integer(2));
    }


    @Override
    boolean isOverrideMavenId2(MavenId mavenId, String versionSaved, String versionCandidate) {
        if(versionSaved==null){
            throw new NullPointerException("null versionSaved for ${mavenId}")
        }
        if(versionCandidate==null){
            throw new NullPointerException("null versionCandidate for ${mavenId}")
        }
        versionSaved = versionSaved.toLowerCase()
        versionCandidate = versionCandidate.toLowerCase()

        boolean savedHasNonStableVersion = MavenDefaultSettings.mavenDefaultSettings.badMavenVersionWords.find {
            versionSaved.contains(it)
        } != null
        boolean candidateHasNonStableVersion = MavenDefaultSettings.mavenDefaultSettings.badMavenVersionWords.find {
            versionCandidate.contains(it)
        } != null
        if (savedHasNonStableVersion && !candidateHasNonStableVersion) {
//            log.info "vc = ${versionCandidate} ${versionSaved}"
            return true
        }
        if (!savedHasNonStableVersion && candidateHasNonStableVersion) {
//            log.info "vs = ${versionCandidate} ${versionSaved}"
            return false
        }
        /**
         * Return true if 3th param more then 2nd param
         */
        int res = isOverrideMavenId(versionSaved, versionCandidate)
//        String result = res > 0 ? versionCandidate : versionSaved
        return res <0
    }


    int isOverrideMavenId(String rev1, String rev2) {

        rev1 = rev1.replaceAll('([a-zA-Z])(\\d)', '$1.$2');
        rev1 = rev1.replaceAll('(\\d)([a-zA-Z])', '$1.$2');
        rev2 = rev2.replaceAll('([a-zA-Z])(\\d)', '$1.$2');
        rev2 = rev2.replaceAll('(\\d)([a-zA-Z])', '$1.$2');

        String[] parts1 = rev1.split('[\\._\\-\\+]');
        String[] parts2 = rev2.split('[\\._\\-\\+]');

        int i = 0;
        for (; i < parts1.length && i < parts2.length; i++) {
            if (parts1[i].equals(parts2[i])) {
                continue;
            }
            boolean is1Number = isNumber(parts1[i]);
            boolean is2Number = isNumber(parts2[i]);
            if (is1Number && !is2Number) {
                return 1;
            }
            if (is2Number && !is1Number) {
                return -1;
            }
            if (is1Number && is2Number) {
                return Long.valueOf(parts1[i]).compareTo(Long.valueOf(parts2[i]));
            }
            // both are strings, we compare them taking into account special meaning
            Integer sm1 = (Integer) DEFAULT_SPECIAL_MEANINGS.get(parts1[i].toLowerCase(Locale.US));
            Integer sm2 = (Integer) DEFAULT_SPECIAL_MEANINGS.get(parts2[i].toLowerCase(Locale.US));
            if (sm1 != null) {
                sm2 = sm2 == null ? new Integer(0) : sm2;
                return sm1.compareTo(sm2);
            }
            if (sm2 != null) {
                return new Integer(0).compareTo(sm2);
            }
            return parts1[i].compareTo(parts2[i]);
        }
        if (i < parts1.length) {
            return isNumber(parts1[i]) ? 1 : -1;
        }
        if (i < parts2.length) {
            return isNumber(parts2[i]) ? -1 : 1;
        }
        return 0;
    }

    private boolean isNumber(String str) {
        return str.matches('\\d+');

    }


}
