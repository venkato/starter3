package net.sf.jremoterun.utilities;

import groovy.transform.CompileStatic;
import net.sf.jremoterun.ICodeForExecuting;
import net.sf.jremoterun.JrrUtils;
import sun.misc.Unsafe;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;



@CompileStatic
public class JrrUtilitiesShowE {


    private static final Logger log = Logger.getLogger(JrrUtilitiesShowE.class.getName());




    public static JFrame showException(final String title, final Throwable throwable) {
        final JFrame dialog = new JFrame(throwable.getClass().getSimpleName());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                final Container rootContentPane = dialog.getContentPane();
                rootContentPane.setLayout(new BorderLayout());
                rootContentPane.add(new JLabel(" " + title), BorderLayout.NORTH);
                final String exceptionS = JrrUtils.exceptionToString(throwable);
                final JTextArea textArea = new JTextArea(exceptionS);
                textArea.setEditable(false);
                rootContentPane.add(new JScrollPane(textArea), BorderLayout.CENTER);
                final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                dialog.setLocation((int)screenSize.width / 2 ,(int) screenSize.height / 2 );
//                dialog.setLocation(screenSize.width / 2 as int, screenSize.height / 2 as int);
                dialog.setSize(200, 200);
                // dialog.setModal(true);
//                if (java5) {
                dialog.setAlwaysOnTop(true);
//                }
                dialog.setVisible(true);
            }
        });
        return dialog;
    }




}
