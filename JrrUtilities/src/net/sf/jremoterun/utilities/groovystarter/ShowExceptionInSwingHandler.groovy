package net.sf.jremoterun.utilities.groovystarter;

import groovy.transform.CompileStatic;
import net.sf.jremoterun.utilities.JrrClassUtils;
import net.sf.jremoterun.utilities.JrrUtilities3;
import net.sf.jremoterun.utilities.NewValueListener;

import javax.swing.*
import java.awt.BorderLayout
import java.awt.FlowLayout;
import java.util.logging.Logger;

@CompileStatic
public class ShowExceptionInSwingHandler extends PrintExceptionListener {

    private static final Logger log = JrrClassUtils.getJdkLogForCurrentClass();

    ShowExceptionInSwingHandler() {
    }

    @Override
    public void newValue(Throwable throwable) {
        genericStuff(throwable)
        JFrame frame = JrrUtilities3.showException("jrr groovy runner", throwable);
        SwingUtilities.invokeLater{
            JPanel panel = new JPanel(new FlowLayout())
            JButton terminateButton = new JButton("Terminate JVM");
            terminateButton.addActionListener{
                System.exit(1);
            }
            panel.add(terminateButton)
            JButton closeButton = new JButton("Close without terminate");
            closeButton.addActionListener{
                frame.dispose();
            }
            panel.add(closeButton)
            frame.getContentPane().add(panel,BorderLayout.SOUTH)
        };
    }
}
