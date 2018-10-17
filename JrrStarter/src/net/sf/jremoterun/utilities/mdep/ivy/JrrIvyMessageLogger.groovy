package net.sf.jremoterun.utilities.mdep.ivy

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils
import org.apache.ivy.util.DefaultMessageLogger
import org.apache.ivy.util.Message
import org.apache.ivy.util.MessageLogger

import java.util.logging.Level;
import java.util.logging.Logger;

@CompileStatic
class JrrIvyMessageLogger extends DefaultMessageLogger{
    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    JrrIvyMessageLogger(int level) {
        super(level)
    }

    static void  setCustomLogger(){
        MessageLogger logger = Message.getDefaultLogger()
        int levelDefault = Message.MSG_INFO;
        if (logger instanceof DefaultMessageLogger) {
            DefaultMessageLogger ll = (DefaultMessageLogger) logger;
            levelDefault = ll.getLevel()
        }
        JrrIvyMessageLogger ivyMessageLogger = new JrrIvyMessageLogger(levelDefault)
        Message.setDefaultLogger(ivyMessageLogger)
    }

    @Override
    void log(String msg, int level) {
        Level level1 = map(level)
        log.log(level1,msg);
    }

    static Level map(int levelIvy){
        switch (levelIvy){
            case Message.MSG_DEBUG:
                return Level.FINEST;
            case Message.MSG_ERR:
                return Level.SEVERE;
            case Message.MSG_INFO:
                return Level.INFO;
            case Message.MSG_VERBOSE:
                return Level.FINE;
            case Message.MSG_WARN:
                return Level.WARNING;
        }
    }
}
