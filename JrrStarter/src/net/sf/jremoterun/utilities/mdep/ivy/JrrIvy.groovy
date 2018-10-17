package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.Ivy
import org.apache.ivy.core.deliver.DeliverEngine
import org.apache.ivy.core.event.EventManager
import org.apache.ivy.core.install.InstallEngine
import org.apache.ivy.core.publish.PublishEngine
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.repository.RepositoryManagementEngine
import org.apache.ivy.core.resolve.ResolveEngine
import org.apache.ivy.core.retrieve.RetrieveEngine
import org.apache.ivy.core.search.SearchEngine
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.core.sort.SortEngine

import java.text.ParseException;
import java.util.logging.Logger;

@CompileStatic
class JrrIvy extends Ivy {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();
    public IvyDepResolver2 ivyDepResolver2
//    JrrRetrieveEngine jrrRetrieveEngine;
    JrrIvy(IvyDepResolver2 ivyDepResolver2) {
        this.ivyDepResolver2 = ivyDepResolver2
    }

    void bindSuper() {
        super.bind()
    }

    @Override
    void bind() {
        IvySettings settings3 = getSettings()
        if (settings3 == null) {
            JrrIvySettings settingsIvy1 = ivyDepResolver2.ivySettings
            if (settingsIvy1 == null) {
                ivyDepResolver2.ivySettings = ivyDepResolver2.createJrrIvySettings()
            }
            setSettings(settingsIvy1)
        }
        if (eventManager == null) {
            setEventManager(new EventManager());
        }
        if (sortEngine == null) {
            setSortEngine(new SortEngine(settings3));
        }
        if (searchEngine == null) {
            setSearchEngine(new SearchEngine(settings3));
        }
        if (resolveEngine == null) {
            IvyResolveEngineJrr engineJrr = ivyDepResolver2.createResolveEngine(eventManager, sortEngine)
            setResolveEngine(engineJrr);
        }

        super.bind()
    }

//    @Override
//    void bind() {
//        pushContext();
//        try {
//            if (settings == null) {
//                throw new Exception("Setting is null")
//            }
//            if (eventManager == null) {
//                setEventManager ( new EventManager());
//            }
//            if (sortEngine == null) {
//                setSortEngine( new SortEngine(settings));
//            }
//            if (searchEngine == null) {
//                setSearchEngine ( new SearchEngine(settings));
//            }
//            if (resolveEngine == null) {
//                setResolveEngine (new ResolveEngine(settings, eventManager, sortEngine));
//            }
//            if (retrieveEngine == null) {
//                jrrRetrieveEngine =new JrrRetrieveEngine(settings, eventManager)
//                setRetrieveEngine(jrrRetrieveEngine );
//            }
////            if (deliverEngine == null) {
////                deliverEngine = new DeliverEngine(settings);
////            }
////            if (publishEngine == null) {
////                publishEngine = new PublishEngine(settings, eventManager);
////            }
////            if (installEngine == null) {
////                installEngine = new InstallEngine(settings, searchEngine, resolveEngine);
////            }
////            if (repositoryEngine == null) {
////                repositoryEngine = new RepositoryManagementEngine(settings, searchEngine,
////                        resolveEngine);
////            }
//        }finally{
//            popContext()
//        }
//        super.bind()
//    }
}
