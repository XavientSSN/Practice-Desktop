package com.ssn.speech;

/**
 * Create a custom audio player
 * @author Abhinav Tripathi
 */
import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class AudioPlayer {
    private static Logger logger = Logger.getLogger(AudioPlayer.class);

    Player audioPlayer = null;
    String audioPath = "";
    String imageMediaFileLocation = "";

    static private javax.swing.JPanel mainPanel;
    
    private volatile List<JLabel> componentToAddToPassedPanel;
    
    private Boolean playLblFlag = null;
    private boolean playerPaused = false;
    public static List<Player> playerList = new ArrayList<Player>();
        
    public AudioPlayer(String audioPath, List<JLabel> componentToAddToPassedPanel, String imageMediaFileLocation) { 
        
        this.audioPath = audioPath;        
        this.componentToAddToPassedPanel = componentToAddToPassedPanel;
        this.imageMediaFileLocation = imageMediaFileLocation;
        
        if(audioPath != null && !audioPath.equals("")){
            File f = new File(audioPath);
            if(f.exists()){
                initAudioPlayer(audioPath);
                setMainPanel(initComponents(true));
            }else{
                //JOptionPane.showMessageDialog(null, "*** File not exist!!! ", " Voice Notes ", JOptionPane.ERROR_MESSAGE);
                logger.error("wav file "+audioPath+ " not exist");
            }
        }
        
    }

    public void initAudioPlayer(String pathname) {
        try {
            if(getAudioPlayer()!=null){
                // setAudioPlayer(null);
            }
            if(pathname != null){
                URL url = new File(pathname).toURL();  
                Player player = Manager.createRealizedPlayer(url);
                setAudioPlayer(player);
                playerList.add(player);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NoPlayerException ex) {
            ex.printStackTrace();
        } catch (CannotRealizeException ex) {
            ex.printStackTrace();
        }
    }
    
    public JPanel initComponents(boolean showInJLabel) {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(61, 61, 61));
        
        final JLabel playLbl = new JLabel(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
        final JLabel pauseLbl = new JLabel(new ImageIcon(getClass().getResource("/icon/pause_voice.png")));
        final JLabel stopLbl = new JLabel(new ImageIcon(getClass().getResource("/icon/stop_voice.png")));
        final JLabel discardLbl = new JLabel(new ImageIcon(getClass().getResource("/icon/delete_voice.png")));
        
        playLbl.setForeground(new Color(184, 184, 184));
        playLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        playLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playLbl.setToolTipText("Play");
        playLbl.setVisible(true);
         
        pauseLbl.setForeground(new Color(184, 184, 184));
        pauseLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        pauseLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseLbl.setToolTipText("Pause");
        pauseLbl.setVisible(true);
        
        stopLbl.setForeground(new Color(184, 184, 184));
        stopLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        stopLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stopLbl.setToolTipText("Stop");
        stopLbl.setVisible(true);
        
        discardLbl.setForeground(new Color(184, 184, 184));
        discardLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        discardLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));         
        discardLbl.setToolTipText("Delete");
        discardLbl.setVisible(true);
        
        playLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                 if(VoiceCommandController.isVoiceCammandEnabled()){
                    getAudioPlayer().stop();
                    
                    if(!isPlayerPaused()){
                        audioPlayer.setMediaTime(new Time(0.0));
                    }else{
                        audioPlayer.setMediaTime(new Time(audioPlayer.getMediaNanoseconds()));
                        setPlayerPaused(false);
                    }
                    logger.info("File to play" + audioPath);
                        getAudioPlayer().start();
                        if(playLblFlag == null || !isPlayLblFlag()){
                            playLbl.setIcon(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
                            setPlayLblFlag(true);
                        }
                }else{
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Voice Notes","","*** Voice Command is not active  ***\n Kindly change voice command setting from Preferences screen!!!");
                }
            }
            public void mouseEntered(MouseEvent e) {
                if(!isPlayLblFlag()){
                    playLbl.setIcon(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
                }
            }
            public void mouseExited(MouseEvent e) {
                if(!isPlayLblFlag()){
                    playLbl.setIcon(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
                }
            }
        });

        pauseLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(VoiceCommandController.isVoiceCammandEnabled()){
                    if(audioPlayer != null){                        
                        audioPlayer.stop();
                        setPlayLblFlag(false);
                        playLbl.setIcon(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
                        setPlayerPaused(true);
                    }
                }else{
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Voice Notes","","*** Voice Command is not active  ***\n Kindly change voice command setting from Preferences screen!!! ");
                }
            }
            public void mouseEntered(MouseEvent e) {
                pauseLbl.setIcon(new ImageIcon(getClass().getResource("/icon/pause_voice.png")));
            }
            public void mouseExited(MouseEvent e) {
                pauseLbl.setIcon(new ImageIcon(getClass().getResource("/icon/pause_voice.png")));
            }
        });

        stopLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(VoiceCommandController.isVoiceCammandEnabled()){
                    if(audioPlayer != null){
                        audioPlayer.stop();
                        audioPlayer.setMediaTime(new Time(0.0));
                        setPlayLblFlag(false);
                        playLbl.setIcon(new ImageIcon(getClass().getResource("/icon/play_voice.png")));
                    }
                }else{
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Voice Notes","","*** Voice Command is not active  ***\n Kindly change voice command setting from Preferences screen!!!");
                }
            }
            public void mouseEntered(MouseEvent e) {
                stopLbl.setIcon(new ImageIcon(getClass().getResource("/icon/stop_voice.png")));
            }
            public void mouseExited(MouseEvent e) {
                stopLbl.setIcon(new ImageIcon(getClass().getResource("/icon/stop_voice.png")));
            }
        });

        discardLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if(VoiceCommandController.isVoiceCammandEnabled()){
                    if(audioPlayer != null){
                        audioPlayer.stop();
                        audioPlayer.setMediaTime(new Time(0.0));
                    }
                    String audioFile = audioPath.substring(audioPath.lastIndexOf(File.separator) + 1);
                    int success = 0;                    
                    try {
                        success = SSNDao.removeVoiceNote_MediaTable(audioFile,imageMediaFileLocation);
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    JPanel parentContainer = (JPanel)evt.getComponent().getParent();
                    parentContainer.remove(playLbl);
                    parentContainer.remove(pauseLbl);
                    parentContainer.remove(stopLbl);
                    parentContainer.remove(discardLbl);

                    for(JLabel label : componentToAddToPassedPanel){
                        logger.debug("label name = " + label.getText());
                        parentContainer.add(label);
                        label.setVisible(true);
                    }

                    parentContainer.validate();
                    parentContainer.repaint();   
                    parentContainer.getParent().validate();
                    parentContainer.getParent().repaint(); 
                    
                    if(getAudioPlayer()!= null && getAudioPlayer().Started==1){
                        getAudioPlayer().stop();
                        getAudioPlayer().close();
                    }else if (getAudioPlayer()!= null){
                         getAudioPlayer().close();
                    }
                    
                    if(success>0){
                        File file = new File(SSNHelper.getSsnVoiceNoteDirPath()+audioFile);
                        if(file.exists()){
                            file.deleteOnExit();
                        }
                    }
                }else{
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Voice Notes","","*** Voice Command is not active  ***\n Kindly change voice command setting from Preferences screen!!!");
                }
            }
            public void mouseEntered(MouseEvent e) {
                discardLbl.setIcon(new ImageIcon(getClass().getResource("/icon/delete_voice.png")));
            }
            public void mouseExited(MouseEvent e) {
                discardLbl.setIcon(new ImageIcon(getClass().getResource("/icon/delete_voice.png")));
            }
        });

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        mainPanel.add(playLbl);
        mainPanel.add(pauseLbl); 
        mainPanel.add(stopLbl);
        mainPanel.add(discardLbl);
        mainPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        return mainPanel;
    }


    public static JPanel getMainPanel() {
        return mainPanel;
    }

    public static void setMainPanel(JPanel mainPanel) {
        AudioPlayer.mainPanel = mainPanel;
    }

    public Boolean isPlayLblFlag() {
        if(playLblFlag==null)
            playLblFlag=false;
        return playLblFlag;
    }

    public void setPlayLblFlag(Boolean playLblFlag) {
        this.playLblFlag = playLblFlag;
    }
 
    public Player getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(Player audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public boolean isPlayerPaused() {
        return playerPaused;
    }

    public void setPlayerPaused(boolean playerPaused) {
        this.playerPaused = playerPaused;
    }
    
}
