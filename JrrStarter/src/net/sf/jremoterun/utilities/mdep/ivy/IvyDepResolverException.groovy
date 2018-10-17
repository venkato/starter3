package net.sf.jremoterun.utilities.mdep.ivy;

import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.core.report.ResolveReport;

import java.util.logging.Logger;
import groovy.transform.CompileStatic;


@CompileStatic
class IvyDepResolverException extends Exception {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ResolveReport rr;

    IvyDepResolverException(String msg, Throwable cause) {
        super(msg, cause)
    }

    IvyDepResolverException(ResolveReport rr) {
        super(rr.getAllProblemMessages().toString())
        this.rr = rr
    }
}
