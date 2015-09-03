
package com.ssn.speech;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.apache.log4j.Logger;

/**
 * This class detects microphone attached to system, start microphone and process voice command
 * @author ATripathi4
 */
public class SSNMicrophoneFrame extends JFrame implements WindowListener,ItemListener {
    private static Logger logger = Logger.getLogger(SSNMicrophoneFrame.class);
    private static final long               serialVersionUID = 1L;
    
    private SSNHomeForm                     homeForm = null;
    private JLabel                          microphoneToolbarLbl = null;
    private JLabel                          speakLabel = null;
    private Recognizer                      recognizer = null;
    private Microphone                      microphone = null;
    private JPanel                          speakLblPanel = null;
    private ConfigurationManager            cm = null;
    private VoiceCommandController          vcController = null;
    private Thread                          voiceCamRunningThread = null;
    
    public SSNMicrophoneFrame(SSNHomeForm homeForm) {        
        super("SSN Speech Search");           
        try {
            this.homeForm = homeForm;
            initMicrophone( );
        } 
        catch (IllegalArgumentException ex) { 
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(this.homeForm,"*** Voice Search  ***\n Could not detect microphone "," microphone is not ready!!!", JOptionPane.ERROR_MESSAGE);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"microphone is not ready!!!","","*** Voice Search  *** Could not detect microphone");
        }
    }
    
    private void initMicrophone( ) {
     
        initMicrophoneGUIComponent();
    }
    
    private void initMicrophoneGUIComponent() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350,300));
        
        setSpeakLblPanel(new JPanel());        
        getSpeakLblPanel().setBackground(new Color(111,111,111));
        getSpeakLblPanel().setBorder(new BevelBorder(BevelBorder.RAISED));
        
        JLabel microphoneImageLabel = new JLabel();
         microphoneImageLabel.setIcon(new ImageIcon(getClass().getResource("/images/microphone.gif")));
        setSpeakLabel(microphoneImageLabel);
        getSpeakLblPanel().add(getSpeakLabel());
        add(getSpeakLblPanel(),BorderLayout.CENTER);
        
        this.initMicrophoneFrame();
    }
    
    private void initMicrophoneFrame() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);                
        this.setResizable(false);
        //homeForm.setAlwaysOnTop(false);        
        this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());
        this.addWindowListener(this);
        //this.add(getPanel(),BorderLayout.NORTH);   
        this.setPreferredSize(new Dimension(200,150));      
        this.setLocation(300,20);        
        this.pack();
        this.setVisible(true);   
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.homeForm.getHomeModel().setMicrophonePanelFlag(false);
        JLabel label = this.homeForm.getHomeModel().getVoiceSearchLabel();
        label.setIcon(new ImageIcon(getClass().getResource("/icon/voice-search-deactive.png")));
    }

    @Override
    public void windowClosed(WindowEvent e) {
        this.homeForm.getHomeModel().setMicrophonePanelFlag(false);
         this.dispose();
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {        
        this.setAlwaysOnTop(false);
    }
    
    
    
    public JLabel getMicrophoneToolbarLbl() {
        return microphoneToolbarLbl;
    }

    public void setMicrophoneToolbarLbl(JLabel microphoneToolbarLbl) {
        this.microphoneToolbarLbl = microphoneToolbarLbl;
    }
    
    public Recognizer getRecognizer() {
        return recognizer;
    }

    public void setRecognizer(Recognizer recognizer) {
        this.recognizer = recognizer;
    }

    public JLabel getSpeakLabel() {
        return speakLabel;
    }

    public void setSpeakLabel(JLabel speakLabel) {
        this.speakLabel = speakLabel;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public JPanel getSpeakLblPanel() {
        return speakLblPanel;
    }

    public void setSpeakLblPanel(JPanel speakLblPanel) {
        this.speakLblPanel = speakLblPanel;
    }

    public Thread getVoiceCamRunningThread() {
        return voiceCamRunningThread;
    }

    public void setVoiceCamRunningThread(Thread voiceCamRunningThread) {
        this.voiceCamRunningThread = voiceCamRunningThread;
    }

    
}
