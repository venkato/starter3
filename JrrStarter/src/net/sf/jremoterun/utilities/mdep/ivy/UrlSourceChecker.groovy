package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import java.util.logging.Logger;

@CompileStatic
class UrlSourceChecker {
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    public static volatile UrlSourceChecker defaultUrlSourceChecker = new UrlSourceChecker();


    boolean isCheckUrl(URL url) {
        return true;
    }

    void urlExists(boolean b, URL url) {

    }
}
