package com.ssn.app.loader;

import com.ssn.ui.custom.component.SSNMessageDialogBox;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public class SSNMainLoader {

    private static final int PORT = 9999;
    private static ServerSocket socket;
    final    private static Logger logger = Logger.getLogger(SSNMainLoader.class);

    @SuppressWarnings({"ResultOfObjectAllocationIgnored", "UseSpecificCatch"})
    public static void main(String[] args) throws Exception {
         logger.info("inside main()");
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                              
                if ("Nimbus".equals(info.getName())) {
                   UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            checkIfRunning();
            SplashDemo splashDemo;
            splashDemo = new SplashDemo();

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            logger.error(ex);
        }
        logger.info("Exit main()");
    }

    private static void checkIfRunning() {
        logger.info("inside checkIfRunning()");
        try {
            //Bind to localhost adapter with a zero connection queue 
            socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
        } catch (BindException e) {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "", "", "Application is already running");
            System.exit(1);
        } catch (IOException e) {
            System.exit(2);
        }
        logger.info("exit checkIfRunning()");
    }
}
