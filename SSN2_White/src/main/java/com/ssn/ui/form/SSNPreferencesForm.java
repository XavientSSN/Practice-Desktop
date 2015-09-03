package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNPreferencesController;
import com.ssn.model.SSNPreferencesModel;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNImagePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author vkvarma
 */
public class SSNPreferencesForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel ssnPreferencesPanel = null;
    private JLabel ssnPreferencesTitleLabel = null;
    private JLabel ssnPreferencesCancelLabel = null;

  //by ritesh  private JLabel scheduleCommandLabel = null;
    private JLabel scheduleCommandLabel = null;
    private JLabel cloudSyncLabel = null;
    private JLabel ssnSavePreferencesBtn = null;
    private JLabel ssnCancelButton = null;
    private SSNHomeForm homeForm = null;
    


    private SSNPreferencesController preferencesController = null;
    private SSNPreferencesModel preferencesModel = null;

    private JRadioButton scheduleTagOn = null;
    private JRadioButton scheduleTagOff = null;
    private JRadioButton cloudOn = null;
    private JRadioButton cloudOff = null;
    private JTextField image = null;
    private JTextField video = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNPreferencesForm.class);
    public SSNPreferencesForm() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

    }

    public SSNPreferencesForm(SSNHomeForm homeForm) {
        super("SSN Preferences Form");

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        setHomeForm(homeForm);
        setPreferencesController(new SSNPreferencesController(this));
        setPreferencesModel(new SSNPreferencesModel(this, homeForm, this.getPreferencesController()));
        getPreferencesController().setPreferencesModel(getPreferencesModel());
        this.initPreferencesFormGUIComponents();
        this.renderPreferencesPanel();
        this.addPreferencesFormUIElements();
        this.positionPreferencesFormUIElements();
        this.renderPreferencesFrame();
    }

    private void initPreferencesFormGUIComponents() {
        this.setSsnPreferencesTitleLabel(new JLabel("<html><font color='rgb(255,215,0)'>Preferences</font></html>"));
        this.getSsnPreferencesTitleLabel().setFont(new Font("open sans", Font.BOLD, 18));
        this.setSsnPreferencesCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png"))));
        this.getSsnPreferencesCancelLabel().setName("closePreferences");
        this.getSsnPreferencesCancelLabel().addMouseListener(this.getPreferencesController());

        
        this.setScheduleCommandLabel(new JLabel("SCHEDULED TAG:"));
        this.getScheduleCommandLabel().setFont(new Font("open sans", Font.BOLD, 10));
        this.getScheduleCommandLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.setCloudSyncLabel(new JLabel("CLOUD SYNCHRONIZATION:"));
        this.getCloudSyncLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getCloudSyncLabel().setFont(new Font("open sans", Font.BOLD, 10));
        
        
         
      
        this.getScheduleCommandLabel().setForeground(new Color(255,255,255));
        this.getCloudSyncLabel().setForeground(new Color(255,255,255));

        this.setSsnSavePreferencesBtn(new JLabel("",new ImageIcon(getClass().getResource("/icon/ok_prefe_btn.png")),0));   

        this.getSsnSavePreferencesBtn().setBackground(new Color(255, 209, 25));
        this.getSsnSavePreferencesBtn().setForeground(new Color(0, 0, 0));
        this.getSsnSavePreferencesBtn().setFont(new Font("open sans", Font.PLAIN, 16));
        this.getSsnSavePreferencesBtn().addMouseListener(this.getPreferencesController());
       // this.getSsnSavePreferencesBtn().setFocusPainted(false);
       this.getSsnSavePreferencesBtn().setName("savePreferences");
       // this.getSsnSavePreferencesBtn().addActionListener(this.getPreferencesController());
        
        
      //  this.setSsnCancelButton(new JLabel("Cancel"));
        this.setSsnCancelButton(new JLabel("",new ImageIcon(getClass().getResource("/icon/cancel_prefe_btn.png")),0));            
        this.getSsnCancelButton().setFont(new Font("open sans", Font.PLAIN, 16));
        this.getSsnCancelButton().setBackground(Color.BLACK);
        this.getSsnCancelButton().setForeground(Color.WHITE);
        this.getSsnCancelButton().addMouseListener(this.getPreferencesController());
       this.getSsnCancelButton().setName("cancelPreferences");
        //this.getSsnCancelButton().addActionListener(this.getPreferencesController());

    }

    private void renderPreferencesPanel() {
        try {
            URL imgURL = getClass().getResource("/images/preferences-bg.png");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnPreferencesPanel(new SSNImagePanel(background));
            this.getSsnPreferencesPanel().setLayout(null);
        } catch (Exception ex) {
        }
    }

    private void addPreferencesFormUIElements() {
        
        this.getSsnPreferencesPanel().add(this.getSsnPreferencesTitleLabel());
        this.getSsnPreferencesPanel().add(this.getSsnPreferencesCancelLabel());
        
        JPanel p = new JPanel();
        p.setBackground(new Color(77,77,77));
       // LineBorder roundedLineBorder = new LineBorder(Color.white, 1, false);
        TitledBorder roundedTitledBorder = new TitledBorder(BorderFactory.createEmptyBorder(), "GENERAL");
        roundedTitledBorder.setTitleColor(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        p.setBorder(roundedTitledBorder);
       //by ritesh p.setBounds(5, 50, 250, 120);
        p.setBounds(160, 70, 270, 150);
        p.setLayout(null);
        

        
        p.add(this.getScheduleCommandLabel());
        ButtonGroup grp2 = new ButtonGroup();
        
        this.setScheduleTagOn(new JRadioButton());
        this.getScheduleTagOn().setBounds(160, 22, 17, 16);
        JLabel scheduleOnLabel = new JLabel("ON");
        scheduleOnLabel.setFont(new Font("open sans", Font.BOLD, 10));
        scheduleOnLabel.setForeground(new Color(255,255,255));
        scheduleOnLabel.setBounds(177,22,20,20);
        
        this.setScheduleTagOff(new JRadioButton());
        this.getScheduleTagOff().setBounds(195, 22, 17, 16);
        JLabel scheduleOffLabel = new JLabel("OFF");
        scheduleOffLabel.setForeground(new Color(255,255,255));
        scheduleOffLabel.setFont(new Font("open sans", Font.BOLD, 10));
        scheduleOffLabel.setBounds(212,22,20,20);
        
        grp2.add(this.getScheduleTagOn());
        grp2.add(this.getScheduleTagOff());
        p.add(this.getScheduleTagOn());
        p.add(scheduleOnLabel);
        p.add(this.getScheduleTagOff());
        p.add(scheduleOffLabel);
        
        p.add(this.getCloudSyncLabel());
        ButtonGroup grp3 = new ButtonGroup();
       
        this.setCloudOn(new JRadioButton());
        this.getCloudOn().setBounds(160, 52, 17, 16);
        JLabel cloudOnLabel = new JLabel("ON");
        cloudOnLabel.setForeground(new Color(255,255,255));
        cloudOnLabel.setFont(new Font("open sans", Font.BOLD, 10));
        cloudOnLabel.setBounds(177,52,20,20);
        
        this.setCloudOff(new JRadioButton());
        this.getCloudOff().setBounds(195, 52, 17, 16);
        
        JLabel cloudOffLabel = new JLabel("OFF");
        cloudOffLabel.setForeground(new Color(255,255,255));
        cloudOffLabel.setFont(new Font("open sans", Font.BOLD, 10));
        cloudOffLabel.setBounds(212,52,20,20);
        
        grp3.add(this.getCloudOn());
        grp3.add(this.getCloudOff());
        p.add(this.getCloudOn());
        p.add(cloudOnLabel);
        p.add(this.getCloudOff());
        p.add(cloudOffLabel);
        this.getSsnPreferencesPanel().add(p);
     
          
        JPanel p2 = new JPanel();
        p2.setBackground(new Color(77,77,77));
        TitledBorder roundedTitledBorder2 = new TitledBorder(BorderFactory.createEmptyBorder(), "CAMERA");
        roundedTitledBorder2.setTitleColor(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        p2.setBorder(roundedTitledBorder2);
        p2.setBounds(160, 220, 270, 150);
        p2.setLayout(null);
        
       //by ritesh  JLabel namingPreferences = new JLabel("Naming Preferences");
         JLabel namingPreferences = new JLabel("FILE NAME PREFERENCES");
        namingPreferences.setFont(new Font("open sans", Font.BOLD, 10));
        namingPreferences.setBounds(10,25,150,20);
        namingPreferences.setForeground(new Color(255,255,255));
        
        JLabel imageLabel = new JLabel("IMAGE:");
        imageLabel.setFont(new Font("open sans", Font.BOLD, 10));
        imageLabel.setBounds(10,50,50,20);
        imageLabel.setForeground(new Color(255,255,255));
        
        this.setImage(new SSNIconTextField("", "Image Prefix", "ImageFileName"));
        this.getImage().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getImage().getHeight(),this.getImage().getWidth()));
        this.getImage().setBounds(70,50,150,25);
        this.getImage().setFont(new Font("open sans", Font.BOLD, 10));
        this.getImage().setBackground(new Color(77,77,77));
        this.getImage().setForeground(new Color(255,255,255));
        this.getImage().setOpaque(true);
        
        
        JLabel videoLabel = new JLabel("VIDEO:");
        videoLabel.setFont(new Font("open sans", Font.BOLD, 10));
        videoLabel.setBounds(10,80,50,20);
        videoLabel.setForeground(new Color(255,255,255));
        
        this.setVideo(new SSNIconTextField("", "Video Prefix", "VideoFileName"));
        this.getVideo().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getVideo().getHeight(),this.getVideo().getWidth()));
        this.getVideo().setBounds(70,80,150,25);
        this.getVideo().setFont(new Font("open sans", Font.BOLD, 10));
        this.getVideo().setBackground(new Color(77,77,77));
        this.getVideo().setForeground(new Color(255,255,255));
        
        this.getVideo().setOpaque(true);
        
        p2.add(namingPreferences);
        p2.add(imageLabel);
        p2.add(this.getImage());
        p2.add(videoLabel);
        p2.add(this.getVideo());
        this.getSsnPreferencesPanel().add(p2); 
        
        Map<String, String> preferences = null;
        try {
            preferences = SSNDao.getPreferences();
        } catch (SQLException ex) {
            Logger.getLogger(SSNPreferencesForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(preferences!=null){
            if(Integer.parseInt(preferences.get(SSNConstants.SSN_SCHEDULED)) == 1){
               this.getScheduleTagOn().setSelected(true);
            }else{
               this.getScheduleTagOff().setSelected(true);
            }
            if(Integer.parseInt(preferences.get(SSNConstants.SSN_CLOUD_SYNC)) == 1){
               this.getCloudOn().setSelected(true);
                    
            }else{
                 this.getCloudOff().setSelected(true);
            }
            this.getImage().setText(preferences.get(SSNConstants.SSN_IMAGE_PREFIX));
            this.getVideo().setText(preferences.get(SSNConstants.SSN_VIDEO_PREFIX));
        }else{
            this.getScheduleTagOn().setSelected(true);
            this.getCloudOn().setSelected(true);
            
            this.getImage().setText("OurHive");
            this.getVideo().setText("OurHive");
        }
        
        this.getSsnPreferencesPanel().add(this.getSsnCancelButton());
        this.getSsnPreferencesPanel().add(this.getSsnSavePreferencesBtn());

    }

    private void positionPreferencesFormUIElements() {

        this.getSsnPreferencesTitleLabel().setBounds(10, 5, 200, 30);
        this.getSsnPreferencesCancelLabel().setBounds(440, 5, 100, 60);
        //this.getFaceRecognitionLabel().setBounds(10, 25, 130, 20);
        this.getScheduleCommandLabel().setBounds(10,22,140, 20);
        this.getCloudSyncLabel().setBounds(10,52, 140, 20);
        this.getSsnSavePreferencesBtn().setBounds(120, 380, 140, 43);
        this.getSsnCancelButton().setBounds(300, 380, 140, 43);
    }

    private void renderPreferencesFrame() {
        try {
            this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());

            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(true);
            this.setUndecorated(true);
            //this.setAlwaysOnTop(true);
            LineBorder border = new LineBorder(new Color(165, 165, 165), 1);
            this.getRootPane().setBorder(border);

            URL imgURL = getClass().getResource("/images/preferences-bg.jpg");
          //  BufferedImage image = ImageIO.read(imgURL);
            Dimension dim =  SSNConstants.SSN_SCREEN_SIZE;
           // this.setBounds(0, 0, 587, 550);
             this.setBounds(0, 0, 580, 520);
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            this.add(this.getSsnPreferencesPanel());
            this.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }
    }

    /**
     * @return the homeForm
     */
    public SSNHomeForm getHomeForm() {
        return homeForm;
    }

    /**
     * @param homeForm the homeForm to set
     */
    public void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    public JPanel getSsnPreferencesPanel() {
        return ssnPreferencesPanel;
    }

    public void setSsnPreferencesPanel(JPanel ssnPreferencesPanel) {
        this.ssnPreferencesPanel = ssnPreferencesPanel;
    }

    public JLabel getSsnPreferencesTitleLabel() {
        return ssnPreferencesTitleLabel;
    }

    public void setSsnPreferencesTitleLabel(JLabel ssnPreferencesTitleLabel) {
        this.ssnPreferencesTitleLabel = ssnPreferencesTitleLabel;
    }

    public JLabel getSsnPreferencesCancelLabel() {
        return ssnPreferencesCancelLabel;
    }

    public void setSsnPreferencesCancelLabel(JLabel ssnPreferencesCancelLabel) {
        this.ssnPreferencesCancelLabel = ssnPreferencesCancelLabel;
    }



  



    public JLabel getScheduleCommandLabel() {
        return scheduleCommandLabel;
    }

    public void setScheduleCommandLabel(JLabel voiceCommandLabel) {
        this.scheduleCommandLabel = voiceCommandLabel;
    }

    public JLabel getCloudSyncLabel() {
        return cloudSyncLabel;
    }

    public void setCloudSyncLabel(JLabel cloudSyncLabel) {
        this.cloudSyncLabel = cloudSyncLabel;
    }

    public JLabel getSsnSavePreferencesBtn() {
        return ssnSavePreferencesBtn;
    }

    public void setSsnSavePreferencesBtn(JLabel ssnSavePreferencesBtn) {
        this.ssnSavePreferencesBtn = ssnSavePreferencesBtn;
    }

    public SSNPreferencesController getPreferencesController() {
        return preferencesController;
    }

    public void setPreferencesController(SSNPreferencesController preferencesController) {
        this.preferencesController = preferencesController;
    }

    public SSNPreferencesModel getPreferencesModel() {
        return preferencesModel;
    }

    public void setPreferencesModel(SSNPreferencesModel preferencesModel) {
        this.preferencesModel = preferencesModel;
    }



    public JRadioButton getScheduleTagOn() {
        return scheduleTagOn;
    }

    public void setScheduleTagOn(JRadioButton scheduleTagOn) {
        this.scheduleTagOn = scheduleTagOn;
    }

    public JRadioButton getScheduleTagOff() {
        return scheduleTagOff;
    }

    public void setScheduleTagOff(JRadioButton scheduleTagOff) {
        this.scheduleTagOff = scheduleTagOff;
    }

    public JRadioButton getCloudOn() {
        return cloudOn;
    }

    public void setCloudOn(JRadioButton cloudOn) {
        this.cloudOn = cloudOn;
    }

    public JRadioButton getCloudOff() {
        return cloudOff;
    }

    public void setCloudOff(JRadioButton cloudOff) {
        this.cloudOff = cloudOff;
    }

    public JTextField getImage() {
        return image;
    }

    public void setImage(JTextField image) {
        this.image = image;
    }

    public JTextField getVideo() {
        return video;
    }

    public void setVideo(JTextField video) {
        this.video = video;
    }

    public JLabel getSsnCancelButton() {
        return ssnCancelButton;
    }

    public void setSsnCancelButton(JLabel ssnCancelButton) {
        this.ssnCancelButton = ssnCancelButton;
    }
    
    
    
    

}
