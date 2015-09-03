package com.ssn.helper;

/**
 *
 * @author vkvarma
 */
import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import org.apache.log4j.Logger;

public class SSNNotificationHelper extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton cloesButton = null;
    private int alpha = 255;
    private int increment = -5;
    private Timer timer = null;
    private JLabel messageLabel = null;
    private JLabel headingLabel = null;
   static Logger logger = Logger.getLogger(SSNNotificationHelper.class);
    public SSNNotificationHelper() {
        String message = "message.";
        String header = "This is header of notification message";
        messageLabel = new JLabel(message);
        headingLabel = new JLabel(header);
        initSSNNotificationFrame();
    }

    private void initSSNNotificationFrame() {
        this.setSize(300, 125);
        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        //setTranslucency(this);
        this.setBackground(new Color(0f, 0f, 0f, 1f / 3f));
        //this.setBackground(Color.ORANGE);
        Dimension scrSize = SSNConstants.SSN_SCREEN_SIZE;
        // height of the task bar
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        this.setLocation(scrSize.width - this.getWidth(), scrSize.height - toolHeight.bottom - this.getHeight());

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;

        //headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.
        //headingLabel.setOpaque(false);
        this.add(headingLabel, constraints);

        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
        cloesButton = new JButton(new AbstractAction("x") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cloesButton.setMargin(new Insets(1, 4, 1, 4));
        cloesButton.setFocusable(false);
        this.add(cloesButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(2, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;

        timer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        this.add(messageLabel, constraints);

        this.setVisible(true);
    }

}
