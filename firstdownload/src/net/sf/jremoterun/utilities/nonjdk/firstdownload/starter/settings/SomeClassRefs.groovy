package net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.settings


import net.sf.jremoterun.utilities.classpath.ClRef
import net.sf.jremoterun.utilities.groovystarter.runners.ClRefRef

enum SomeClassRefs implements ClRefRef {
    GitRepoDownloader(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.GitRepoDownloader'))
    , selfUpdateRef(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.starter.SelfUpdater'))
    , ifFrameworkAdder(new ClRef('net.sf.jremoterun.utilities.nonjdk.firstdownload.specclassloader.sep1.IfFrameworkDownloader'))
    ;

    ClRef clRef;

    SomeClassRefs(ClRef clRef) { this.clRef = clRef }
}
