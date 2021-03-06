package com.ssn.event.controller;

import com.github.sarxos.webcam.WebcamPanel;
import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNHiveAlbumSelectionListner;
import com.ssn.model.SSNHomeModel;
import com.ssn.speech.SSNMicrophoneFrame;
import com.ssn.speech.VoiceCommandController;
import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNToolBar;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ui.form.SSNShareForm;
import com.ssn.ui.form.SSNUntaggedMediaForm;
import com.ssn.ws.rest.response.SSNAlbumMedia;
import com.ssn.ws.rest.response.SSNLoginResponse;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openimaj.image.processing.face.detection.DetectedFace;

/**
 *
 * @author vkvarma
 */
public final class SSNHomeController extends SSNBaseController {    
    public static boolean   isUnTaggedOpen  =   false;
    private SSNHomeForm homeForm = null;
    private SSNLoginForm loginForm = null;
    private WebcamPanel.Painter painter = null;
    private List<DetectedFace> faces = null;
    private SSNHomeModel homeModel = null;
    private ConfigurationManager cm = null;
    private Recognizer recognizer = null;
    private Microphone microphone = null;
    List<String> uniqueCommands = null;
    public static JLabel currentLabel = null;
    private final static Logger logger = Logger.getLogger(SSNHomeController.class);
    public SSNHomeController() {
    }

    public SSNHomeController(SSNHomeForm homeForm, SSNLoginForm loginForm) {
        logger.info("init method");
        this.setHomeForm(homeForm);
        this.setLoginForm(loginForm);
        uniqueCommands = SSNDao.getAllExistingUniqueCommands();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info("actionPerformed method ");
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            
        } else if (actionEventObj != null && actionEventObj instanceof JMenuItem) {
            JMenuItem jMItem = (JMenuItem) actionEventObj;
            logger.info("actionPerformed method " + jMItem.getActionCommand());
            if (jMItem.getActionCommand().equalsIgnoreCase("logoffMenu")) {
                this.getHomeModel().processSSNLogoff();
            } else if (jMItem.getActionCommand().equalsIgnoreCase("editUserProfileShowForm")) {
            
                this.getHomeModel().displayEditProfileForm();
            } else if (jMItem.getActionCommand().equalsIgnoreCase("changePasswordShowForm")) {
            
                this.getHomeModel().displayChangePasswordForm();
            }else if (jMItem.getActionCommand().equalsIgnoreCase("preferencesMenu")) {
            
                this.getHomeModel().displayPreferencesForm();
            }else if (jMItem.getActionCommand().equalsIgnoreCase("ALL")) {
            
                this.getHomeModel().showFilteredMedia("ALL");
            }else if (jMItem.getActionCommand().equalsIgnoreCase("IMAGES")) {
            
                this.getHomeModel().showFilteredMedia("IMAGES");
            }else if (jMItem.getActionCommand().equalsIgnoreCase("VIDEOS")) {
            
                this.getHomeModel().showFilteredMedia("VIDEOS");
            }else if (jMItem.getActionCommand().equalsIgnoreCase("voiceCommandSettingMenu")) {
            
                this.getHomeModel().displayVoiceCommandSettingForm();
            }
            //exportPhoto
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object mouseEventObj = e.getSource();
         this.getHomeForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
                JLabel jBtn = (JLabel) mouseEventObj;
                if (!(jBtn.getName().equalsIgnoreCase("downloadMedia")))
                    refreshIconImageToDefault();
                
                currentLabel = jBtn;
                JLabel homeLabel = SSNToolBar.desktopHomeLabel;
               
                if (jBtn.getName().equalsIgnoreCase("home")) {
                    try {
                        SSNFileExplorer.selectedMedia = "home";
                        homeModel.renderLeftPanel();
                        getHomeForm().getSsnDirSelectionListener().valueChanged(null, true);
                        setIconImage(jBtn,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }else if (jBtn.getName().equalsIgnoreCase("hive")) {
                    try {
                        if(SSNHelper.isInternetReachable()){
                            SSNFileExplorer.selectedMedia = "hive";
                            File deleteDir = new File(SSNHelper.getSsnTempDirPath());
                      
                            String[]entries = deleteDir.list();
                            
                            for(String s: entries){
                                
                                File currentFile = new File(deleteDir.getPath(),s);
                                if(currentFile.isDirectory()){
                                    FileUtils.deleteDirectory(currentFile);
                                }else{
                                    currentFile.delete();
                                }
                            }
                            
                           homeModel.renderLeftPanel();
                           setIconImage(jBtn,"/icon/white_icon/view-hive-icon.png","hive",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                           setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        }else{
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No internet connection available or may be host not responding.");
                        }
                    } catch (Exception ee) {
                        logger.error(ee);
                    }
                } else if (jBtn.getName().equalsIgnoreCase("createAlbum") || jBtn.getName().equalsIgnoreCase("createAlbum2") || jBtn.getName().equalsIgnoreCase("createAlbum3")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                        setIconImage(jBtn,"/icon/white_icon/create-album-normal.png","createAlbum",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        this.getHomeModel().createHiveAlbum(jBtn);
                        setIconImage(jBtn,"/icon/create-album-normal.png","createAlbum",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
              
                } else if (jBtn.getName().equalsIgnoreCase("openCamera")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                        setIconImage(jBtn,"/icon/white_icon/camera-normal.png","openCamera",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        this.getHomeModel().openCamera(jBtn, false);
                   
                } else if (jBtn.getName().equalsIgnoreCase("uploadPhoto") || jBtn.getName().equalsIgnoreCase("uploadPhoto2")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                        setIconImage(jBtn,"/icon/white_icon/import_media.png","uploadPhoto",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        this.getHomeModel().uploadImage();

              
                } else if (jBtn.getName().equalsIgnoreCase("addFiles")) {
              
                    this.getHomeModel().addMultipleFiles();
                } else if (jBtn.getName().equalsIgnoreCase("uploadMultipleFiles")) {
                    ImageIcon gifImage = new ImageIcon(getClass().getResource("/icon/main_screen_loader.gif"), "cloudAnimation");
                    this.getHomeForm().setLblProcessingImage(new JLabel(gifImage));
                    this.getHomeModel().uploadMultipleFile();
                    this.getHomeForm().setLblProcessingImage(new JLabel(new ImageIcon(getClass().getResource("/icon/buffer.png"))));
              
                } else if (jBtn.getName().equalsIgnoreCase("deletePhoto")) {
                    if (this.getHomeForm().getFileNamesToBeDeleted() != null && this.getHomeForm().getFileNamesToBeDeleted().size() > 0) {
                        if (SSNHelper.lastAlbum != null && (SSNHelper.lastAlbum.equalsIgnoreCase("Instagram Media") || SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath()))) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "Media can not be deleted.");
                        } else {
                            SSNConfirmationDialogBox dialogBox = new SSNConfirmationDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation : ", "", "Are you sure to delete the selected media?");
                            int result = dialogBox.getResult();
                            if (result == JOptionPane.YES_OPTION) {
                                this.getHomeModel().deletePhotos();
                            }
                        }

                    } else if ((StringUtils.isNotBlank(this.getHomeForm().ssnFileExplorer.m_display.getText()) && !this.getHomeForm().ssnFileExplorer.m_display.getText().equalsIgnoreCase(SSNHelper.getSsnHiveDirPath()))) {

                        if (this.getHomeForm().ssnFileExplorer.m_display.getText().equals("viewAllAlbums") || this.getHomeForm().ssnFileExplorer.m_display.getText().equals("instagramMedia") || this.getHomeForm().ssnFileExplorer.m_display.getText().equals("tagUnTaggedMedia")) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Media File Selected OR No Album Selected");
                        } else if (this.getHomeForm().ssnFileExplorer.getSelectedFolder() != null && this.getHomeForm().ssnFileExplorer.getSelectedFolder().equals(SSNConstants.SSN_DEFAULT_DIRECTORY)) {
                            SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                            messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Warning", "", "Default album is not allowed to delete!.");

                        } else {
                            SSNConfirmationDialogBox dialogBox = new SSNConfirmationDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Exit Confirmation : ", "", "Are you sure to delete the selected album?");
                            int result = dialogBox.getResult();
                            if (result == JOptionPane.YES_OPTION) {
                                this.getHomeModel().deletePhotos();
                            } else if (result == JOptionPane.NO_OPTION) {

                            }
                        }

                    }
                } else if (jBtn.getName().equalsIgnoreCase("startSlideShow")) {
                    if(SSNFileExplorer.selectedMedia.equals("home")){
                        setIconImage(jBtn,"/icon/white_icon/slideshow-normal.png","startSlideShow",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        this.getHomeModel().openSlideshow(jBtn);
                    }else{
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "Can not play Slideshow for Hive Media.");
                    }
                } else if (jBtn.getName().equalsIgnoreCase("viewAllAlbum")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                        setIconImage(jBtn,"/icon/white_icon/view-albums-normal.png","viewAllAlbum",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        this.getHomeForm().getHomeModel().viewAllAlbums();

                } else if (jBtn.getName().equalsIgnoreCase("allUntagged")) {
                    setIconImage(jBtn,"/icon/white_icon/tagged-untagged-media.png","allUntagged",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                    if (!SSNHomeController.isUnTaggedOpen) {
                        SSNHomeController.isUnTaggedOpen = true;
                        new SSNUntaggedMediaForm(this.getHomeModel());
                    }
                    
                } else if (jBtn.getName().equalsIgnoreCase("share")) {
                    if (this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().size() >= 1) {
                        new SSNShareForm(this.getHomeModel(), this.getHomeForm().getFileNamesToBeDeleted());
                    } else {
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Media Selected.");
                    }
                } else if (jBtn.getName().equalsIgnoreCase("gift")) {
                    JOptionPane.showMessageDialog(getHomeForm(), "*** Souvenir Gift  ***<br> Section Under Construction ", "!!! Section Not Available !!!", JOptionPane.ERROR_MESSAGE);
                } else if (jBtn.getName().equalsIgnoreCase("scheduleTag")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                    setIconImage(jBtn,"/icon/white_icon/schedule-active.png","scheduleTag",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                    this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    Map<String, String> preferences = null;
                    try {
                        preferences = SSNDao.getPreferences();
                    } catch (SQLException ex) {
                        logger.error(ex);
                    }
                    if (Integer.parseInt(preferences.get(SSNConstants.SSN_SCHEDULED)) == 1) {
                        this.getHomeModel().openTagSchedulePanel(jBtn);
                    }else{
                        setIconImage(jBtn,"/icon/schedule-active.png","scheduleTag",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                        messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Schedule A Tag is OFF.");
                    }
                    

                } else if (jBtn.getName().equalsIgnoreCase("recentlyAdded")) {
                    JOptionPane.showMessageDialog(getHomeForm(), "*** Recently Added  ***<br> Section Under Construction ", "!!! Section Not Available !!!", JOptionPane.ERROR_MESSAGE);
                } else if (jBtn.getName().equalsIgnoreCase("starredMedia")) {
                    JOptionPane.showMessageDialog(getHomeForm(), "*** Starred Media  ***<br> Section Under Construction ", "!!! Section Not Available !!!", JOptionPane.ERROR_MESSAGE);
                } else if (jBtn.getName().equalsIgnoreCase("storeToCloud")) {

                    JOptionPane.showMessageDialog(getHomeForm(), "*** Cloud Storage  ***<br> Section Under Construction ", "!!! Section Not Available !!!", JOptionPane.ERROR_MESSAGE);
                } else if (jBtn.getName().equalsIgnoreCase("voiceSearch")) {
                // its working as VOICE COMMAND rather than voice search

                    if (VoiceCommandController.isVoiceCammandEnabled()) {
                        jBtn.setIcon(new ImageIcon(getClass().getResource("/icon/voice-search-hover.png")));
                        this.homeModel.setVoiceSearchLabel(jBtn);

                        getHomeForm().getHomeModel().setMicrophonePanelFlag(true);
                        new SSNMicrophoneFrame(getHomeForm());
                        Thread microphoneRunner = new Thread() {
                            SSNMicrophoneFrame microFrame = null;

                            public void run() {
                                while (getHomeForm().getHomeModel().isMicrophonePanelFlag()) {
                                    // load configuration file with ConfigurationManager
                                    if (cm == null) {
                                        cm = new ConfigurationManager(SSNHomeForm.class.getResource("/config/ssn.speech.config.xml"));

                                        // Recognizer is Sphinx-4 recognizer. It is the main entry point for Sphinx-4.
                                        recognizer = (Recognizer) cm.lookup("recognizer");
                                        recognizer.allocate();

                                        // look for the mircophone 
                                        microphone = (Microphone) cm.lookup("microphone");
                                        microphone.startRecording();
                                    }
                                // if could not find microphone or could not start it then deallocate memory assigned to
                                    // mocrophone object 
                                    if (microphone == null) {

                                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Microphone is not ready!!!", "", "*** Voice Search  *** Could not start microphone");
                                        recognizer.deallocate();
                                    } else {
                                    //if microphone could start recording then show microphone panel and
                                        //start capturing voice command
                                        recordSpeech(recognizer);
                                    }

                                }
                            }
                        };
                        microphoneRunner.setName("microphoneThread");
                        microphoneRunner.start();
                    } else {

                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Could not start microphone", "", "*** Voice Search  *** Kindly change voice command setting from Preferences screen!!! ");
                    }
                } else if (jBtn.getName().equalsIgnoreCase("homeMediaSearch")) {
                    JToolBar jToolBar = this.getHomeForm().getSsnHomeToolBar();
                    SSNToolBar ssnHomeToolBar = (SSNToolBar) jToolBar;
                    String selectedOption = getSelectedButtonText(this.getHomeForm().getSearchOptionButtonGroup());
                    if (selectedOption != null && selectedOption.equals("Hive Search")) {
                        SSNLoginResponse loginResponse = this.getHomeForm().getLoginResponse();

                        if (loginResponse != null && loginResponse.getData() != null
                                && loginResponse.getData().getUser() != null) {
                            String searchText = this.getHomeForm().getSearchMediaTextField().getText();
                            this.getHomeModel().searchCloudMedia(loginResponse.getData().getUser().getAccess_token(), searchText);
                        } else {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Cloud Search could not be done. Please try later.");
                        }
                    } else {
                        this.getHomeForm().getHomeModel().searchMedia();
                    }

                } else if (jBtn.getName().equalsIgnoreCase("searchVoiceMedia")) {
                    if (VoiceCommandController.isVoiceCammandEnabled()) {
                        this.getHomeForm().getHomeModel().searchVoiceMedia(jBtn);
                    } else {
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Could not start microphone", "", "*** Voice Search  ***<br> Kindly change voice command setting from Preferences screen!!! ");
                    }
                } else if (jBtn.getName().equalsIgnoreCase("next")) {
                    this.getHomeModel().nextImageView();
                } else if (jBtn.getName().equalsIgnoreCase("previous")) {
                    this.getHomeModel().previousImageView();
                } else if (jBtn.getName().equalsIgnoreCase("back")) {
                    try {
                        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                        this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                        SSNGalleryHelper contentPane = new SSNGalleryHelper(getHomeForm().ssnFileExplorer.m_display.getText(), this.getHomeForm(), "ALL");
                        contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                        this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));
                        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                        this.getHomeForm().revalidate();
                    } catch (IOException ex) {
                        logger.error(ex);
                    }

                } else if (jBtn.getName().equalsIgnoreCase("rotateLeft")) {
                    this.getHomeModel().rotateImageLeft();
                } else if (jBtn.getName().equalsIgnoreCase("rotateRight")) {
                    this.getHomeModel().rotateImageRight();
                } else if (jBtn.getName().equalsIgnoreCase("zoomIn")) {
                    this.getHomeModel().zoomIn();
                } else if (jBtn.getName().equalsIgnoreCase("zoomOut")) {
                    this.getHomeModel().zoomOut();

                } else if (jBtn.getName().equalsIgnoreCase("saveRotatedImage")) {
                    this.getHomeModel().saveRotatedImage();
                } else if (jBtn.getName().equalsIgnoreCase("shareImage")) {
                    this.getHomeForm().getFileNamesToBeDeleted().clear();
                    this.getHomeForm().getFileNamesToBeDeleted().add(this.getHomeForm().getCurrentFile().getAbsolutePath());
                    new SSNShareForm(this.getHomeModel(), this.getHomeForm().getFileNamesToBeDeleted());
                } else if (jBtn.getName().equalsIgnoreCase("uploadMedia")) {
                    if(!SSNFileExplorer.selectedMedia.equals("home")){
                        SSNFileExplorer.selectedMedia="home";
                         homeModel.renderLeftPanel();
                    }
                        setIconImage(jBtn,"/icon/white_icon/upload-normal.png","uploadMedia",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                        Map<String, String> preferences = null;
                        try {
                            preferences = SSNDao.getPreferences();
                        } catch (SQLException ex) {
                            logger.error(ex);
                        }
                        if (Integer.parseInt(preferences.get(SSNConstants.SSN_CLOUD_SYNC)) == 1) {

                            String desc = ((ImageIcon) jBtn.getIcon()).getDescription();
                            if (desc == null || !desc.equalsIgnoreCase("cloudAnimation")) {
                                SSNLoginResponse loginResponse = this.getHomeForm().getLoginResponse();
                                if(!(this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().size()>0)){
                                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No media selected.");
                                    setIconImage(jBtn,"/icon/upload-normal.png","uploadMedia",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                                    setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR); 
                                }

                                if (loginResponse != null && loginResponse.getData() != null
                                        && loginResponse.getData().getUser() != null) {
                                    try {
    //                                    
                                        this.getHomeModel().synchroniseWithCloud(loginResponse.getData().getUser().getAccess_token(), jBtn);

                                    } catch (Exception ee) {
                                        logger.error(ee);
                                    }
                                } else {

                                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Synchronization could not be done. <br>Please try later.");
                                    refreshCenterPanel();
                                    setIconImage(jBtn,"/icon/upload-normal.png","uploadMedia",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                                    setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                                }
                            }
                        } else {

                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Cloud Synchronization is OFF.");
                            refreshCenterPanel();
                            setIconImage(jBtn,"/icon/upload-normal.png","uploadMedia",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                            setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        }
//                    }else{
//                         SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                         dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Please selectt desktop media to upload on hive.");
//                    }
                    
                }else if (jBtn.getName().equalsIgnoreCase("exportPhoto")) {
                    if(this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().size()>0){
                        String path =this.getHomeModel().exportMediaToDevice(this.getHomeModel().getHomeForm().getFileNamesToBeDeleted());
                        
                        if(path.equals("cancel")){
                            
                        }else if(path.equals("")){
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Some error occure during export.");
                        }else{
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Media exported successfully");
                        }
                    }else{
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No media selected.");
                    }
                    
                }else if (jBtn.getName().equalsIgnoreCase("downloadMedia")) {
                    setIconImage(SSNToolBar.hiveLabel,"/icon/white_icon/view-hive-icon.png","hive",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    setIconImage(homeLabel,"/icon/home.png","home",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                    if(this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().size()>0){
                        if(SSNHelper.isInternetReachable()){
                            logger.info("InternetReachable");
                            this.getHomeModel().downloadHiveMediaToLibrary();
                        }else{
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No internet connection available or may be host not responding.");
                        }
                    }else{
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No media selected.");
                    }
                } else if (jBtn.getName().equalsIgnoreCase("tagImage")) {
                    this.getHomeModel().tagImage();
                } else if (jBtn.getName().equalsIgnoreCase("addSubscription")) {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        java.net.URI uri;
                        uri = new java.net.URI(SSNConstants.SSN_SUBCRTIPTION_URL);
                        desktop.browse(uri);
                    } catch (URISyntaxException | IOException ex) {
                        ex.printStackTrace();
                    }

                }
            } else if (mouseEventObj != null && mouseEventObj instanceof JPanel) {
                JPanel jBtn = (JPanel) mouseEventObj;
                if (jBtn.getName().equalsIgnoreCase("Name" + "up")) {
                    this.getHomeModel().sortImages("name", SSNConstants.SSN_IMAGE_SORT_ORDER_DESC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Name", false, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                } else if (jBtn.getName().equalsIgnoreCase("Date" + "up")) {
                    this.getHomeModel().sortImages("date", SSNConstants.SSN_IMAGE_SORT_ORDER_DESC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                } else if (jBtn.getName().equalsIgnoreCase("Size" + "up")) {
                    this.getHomeModel().sortImages("size", SSNConstants.SSN_IMAGE_SORT_ORDER_DESC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Size", false, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                } else if (jBtn.getName().equalsIgnoreCase("Name" + "down")) {
                    this.getHomeModel().sortImages("name", SSNConstants.SSN_IMAGE_SORT_ORDER_ASC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Name", true, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                } else if (jBtn.getName().equalsIgnoreCase("Date" + "down")) {
                    this.getHomeModel().sortImages("date", SSNConstants.SSN_IMAGE_SORT_ORDER_ASC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", true, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                } else if (jBtn.getName().equalsIgnoreCase("Size" + "down")) {
                    this.getHomeModel().sortImages("size", SSNConstants.SSN_IMAGE_SORT_ORDER_ASC);
                    this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Size", true, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().revalidate();
                }
            
           
        }
        
        this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
   

    public BufferedImage rotate(BufferedImage image, double angle) {
        int w = image.getWidth();
        int h = image.getHeight();
          GraphicsConfiguration configuration = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();
          BufferedImage buffer = configuration.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = buffer.createGraphics();
        g.rotate(Math.toRadians(angle), w/2, h/2);
        
        g.drawImage(image, null, 0, 0);
       
        return buffer;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) mouseEventObj;
           
           
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) mouseEventObj;
              logger.info("mouseExited method " +jLbl.getName());
            if (jLbl.getName().equalsIgnoreCase("addSubscription")) {
                this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object keyEventObj = e.getSource();
        char c = e.getKeyChar();
          
        if (keyEventObj != null && (keyEventObj instanceof JTextField)) {
            if (c == KeyEvent.VK_ENTER) {
                JToolBar jToolBar = this.getHomeForm().getSsnHomeToolBar();
                SSNToolBar ssnHomeToolBar = (SSNToolBar) jToolBar;
                String selectedOption = getSelectedButtonText(this.getHomeForm().getSearchOptionButtonGroup());

                this.getHomeForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                if (selectedOption != null && selectedOption.equals("Cloud Search")) {
                    SSNLoginResponse loginResponse = this.getHomeForm().getLoginResponse();

                    if (loginResponse != null && loginResponse.getData() != null
                            && loginResponse.getData().getUser() != null) {
                        String searchText = ssnHomeToolBar.getSearchMediaTextField().getText();
                        this.getHomeModel().searchCloudMedia(loginResponse.getData().getUser().getAccess_token(), searchText);
                    } else {
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Cloud Search could not be done.<br> Please try later.");
                    }
                } else {
                    this.getHomeForm().getHomeModel().searchMedia();
                }
                this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
   

    @Override
    public void windowActivated(WindowEvent e) {
        this.getHomeForm().repaint();
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
       this.getHomeForm().repaint();

    }
    @Override
    public void windowIconified(WindowEvent e) {
       
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
       this.getHomeForm().repaint();
    }
    
    public void windowOpened(WindowEvent e) {
        
        e.getWindow().setVisible(true);
      }

    
    public void windowClosed(WindowEvent e) {
        
      }

    /**
     * @return the loginForm
     */
    public SSNLoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * @param loginForm the loginForm to set
     */
    public void setLoginForm(SSNLoginForm loginForm) {
        this.loginForm = loginForm;
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

    /**
     * @return the painter
     */
    public WebcamPanel.Painter getPainter() {
        return painter;
    }

    /**
     * @param painter the painter to set
     */
    public void setPainter(WebcamPanel.Painter painter) {
        this.painter = painter;
    }

    /**
     * @return the faces
     */
    public List<DetectedFace> getFaces() {
        return faces;
    }

    /**
     * @param faces the faces to set
     */
    public void setFaces(List<DetectedFace> faces) {
        this.faces = faces;
    }

    /**
     * @return the homeModel
     */
    public SSNHomeModel getHomeModel() {
        return homeModel;
    }

    /**
     * @param homeModel the homeModel to set
     */
    public void setHomeModel(SSNHomeModel homeModel) {
        this.homeModel = homeModel;
    }

    private void recordSpeech(Recognizer recognizer) {
        logger.info("inside recordSpeech  "  );
        VoiceCommandController vc = new VoiceCommandController();
        while (true) {
            Result result = null;
            if(!recognizer.getState().toString().equalsIgnoreCase("RECOGNIZING")){
                result = recognizer.recognize();
            }
            if (result != null && getHomeForm().getHomeModel().isMicrophonePanelFlag()) {

                String resultText = result.getBestFinalResultNoFiller().trim().toUpperCase();
                if(resultText != null && !resultText.equals("")){
                    
                    if(uniqueCommands.contains(resultText)){                        
                        String command = VoiceCommandController.getMappedMethod(resultText);

                        if(StringUtils.isBlank(command) || command==null){
                            return;
                        }
                        if (StringUtils.equalsIgnoreCase(command.trim(),"close")) {

                            if (this.homeForm.getSsnMicrophoneCamThread() != null) {
                                vc.closeCamera(this.homeForm);
                                this.homeForm.setSsnMicrophoneCamThread(null);

                            }
                        } else if (StringUtils.equalsIgnoreCase(command.trim(),"take photo")) {
                            if (this.homeForm.getSsnMicrophoneCamThread() == null) {
                                vc.openCamera(this.homeForm);
                            }
                            vc.captureImage(this.homeForm);
                        } else if (StringUtils.equalsIgnoreCase(command.trim(),"START VIDEO")) {
                            if (this.homeForm.getSsnMicrophoneCamThread() == null) {
                                vc.openCamera(this.homeForm);
                            }
                            vc.captureVideo(this.homeForm);
                        }
                    }
                }
            }
            logger.info("Exit recordSpeech ");
        }
           
    }
    
    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        if(buttonGroup != null)
        {
            for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); 
                    buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();

                if (button.isSelected()) {
                    return button.getText();
                }
            }
        }

        return null;
    }
    
    public void setIconImage(JLabel label,String iconName,String labelName,Color color)
    {
        label.removeAll();
        label.setIcon(null);
        ImageIcon iconImage = new ImageIcon(getClass().getResource(iconName), labelName);
        label.setIcon(iconImage);
        label.setForeground(color);
        label.repaint();
        getHomeForm().getSsnHomeToolBar().repaint();
    }
        
      public void refreshIconImageToDefault() 
      {
          setIconImage(SSNToolBar.desktopHomeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
          setIconImage(SSNToolBar.hiveLabel,"/icon/view-hive-icon.png","hive",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
          if(currentLabel != null)
          {
              if(currentLabel.getName().equals("scheduleTag"))
            setIconImage(currentLabel,"/icon/schedule-active.png","scheduleTag",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
              else if(currentLabel.getName().equals("viewAllAlbum"))
            setIconImage(currentLabel,"/icon/view-albums-normal.png","viewAllAlbum",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
              else if(currentLabel.getName().equals("hive"))
            setIconImage(currentLabel,"/icon/view-hive-icon.png","hive",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
              
          }
      }
      public void refreshCenterPanel()
      {
          String srcAlbumPath = homeForm.ssnFileExplorer.m_display.getText();
          if (SSNHelper.lastAlbum != null) {
              if (SSNHelper.lastAlbum.equals("View All Albums")) {
                  srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                  homeForm.ssnFileExplorer.m_display.setText(srcAlbumPath);
              } else if (SSNHelper.lastAlbum.equals("Instagram Media")) {
                  srcAlbumPath = SSNHelper.getInstagramPhotosDirPath();
                  homeForm.ssnFileExplorer.m_display.setText("instagramMedia");
              } else if (SSNHelper.lastAlbum.equals("Tag UnTagged Media")) {
                  srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                  homeForm.ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
              } else if (SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                  srcAlbumPath = SSNHelper.lastAlbum;
                  homeForm.ssnFileExplorer.m_display.setText(srcAlbumPath);
              }
          }

          SSNGalleryHelper contentPane;
        try {
            contentPane = new SSNGalleryHelper(srcAlbumPath, homeForm, "ALL");
            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
          homeForm.getSsnHomeCenterPanel().removeAll();
          homeForm.getSsnHomeCenterMainPanel().removeAll();
          homeForm.getSsnHomeCenterPanel().add(homeForm.getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())));
          homeForm.getSsnHomeCenterMainPanel().add(homeForm.getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
          homeForm.getSsnHomeCenterMainPanel().add(homeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
          homeForm.revalidate();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SSNHomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
      }

}
