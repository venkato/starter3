package net.sf.jremoterun.utilities.test;

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import net.sf.jremoterun.utilities.classpath.MavenDependenciesResolver;
import net.sf.jremoterun.utilities.classpath.MavenId;
import net.sf.jremoterun.utilities.classpath.MavenPath;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@CompileStatic
public class SimpleService {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public String noParams() {
        return getClass().getClassLoader().getClass().getName();
    }

    public String v3(boolean a1, MavenId v2) {
        return v2.getVersion();
    }

    public String v2(boolean a1, MavenId v2) {
        if (v2 == null) {
            throw new NullPointerException("npe");
        }
        if (v2.getClass().getClassLoader() == null) {
            return "boot classloader";
        }
        return v2.getClass().getClassLoader().getClass().getName();
    }


    public void passException() throws Exception {
        throw new IllegalArgumentException("check");
    }

    public String v1(boolean a1, Object v2) {
        if (v2 == null) {
            throw new NullPointerException("npe");
        }
        if (v2.getClass().getClassLoader() == null) {
            return "boot classloader";
        }
        return v2.getClass().getClassLoader().getClass().getName();
    }

    String protectedMethod(MavenId v2) {
        return v2.getVersion();
    }

    MavenId returnDiffClassloader() {
        return new MavenId("log4j:log4j:1.1.1");
    }


    int nonSerializableParam(Runnable r) {
        r.run();
        return r.hashCode();
    }

    String listMavenIdParam(List<MavenId> list) {
        return list.get(0).getVersion();
    }


    List<MavenId> resultListMavenIdParam() {
        return Arrays.asList(new MavenId[]{new MavenId("log4j:log4j:1.1")});
    }

    String listStringParam(List<String> list) {
        return list.get(0);
    }

    MavenDependenciesResolver first;

    void passMavenDependenciesResolver(MavenDependenciesResolver mdr, int count) throws Exception {
        if (count == 0) {
            first = mdr;
        } else {
            if (first != mdr) {
                throw new Exception("failed");
            }
//                log.info("works!");
        }
    }


    MavenDependenciesResolver getMavenDependenciesResolver() {
        return new MavenDependenciesResolver() {

            @Override
            public void downloadMavenPath(MavenPath path, boolean dep) {

            }

            @Override
            public void downloadPathImplSpecific(String path, boolean dep) {

            }

            @Override
            public List<MavenId> resolveAndDownloadDeepDependencies(MavenId mavenId, boolean downloadSource, boolean dep) {
                List<MavenId> res = new ArrayList<>();
                res.add(mavenId);
                return res;
            }

            @Override
            public void downloadSource(MavenId mavenId) {
                mavenId.getVersion();
            }

            @Override
            public File getMavenLocalDir() {
                return null;
            }

            @Override
            public URL getMavenRepoUrl() {
                return null;
            }
        };
    }

}

/* 
 * JRemoteRun.sf.net. License:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
 