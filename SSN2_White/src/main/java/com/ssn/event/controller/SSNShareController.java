/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.event.controller;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.custom.component.SSNInputDialogBox;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNShareForm;
import com.ssn.util.GoogleAnalyticsUtil;
import com.ssn.ws.rest.service.LoginWithFacebook;
import com.ssn.ws.rest.service.LoginWithTwitter;
import com.ssn.ws.rest.service.SSNHttpServer;
import com.sun.net.httpserver.HttpServer;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.MediaOperations;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.TweetData;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 *
 * @author asingh8
 */
public class SSNShareController extends SSNBaseController {

    private SSNShareForm shareForm;
    private SSNHomeModel homeModel;
    private Set<String> files;
    final private Logger logger = Logger.getLogger(SSNShareController.class);
    

    public SSNShareController() {

    }

    public SSNShareController(SSNShareForm shareForm, SSNHomeModel homeModel, Set<String> files) {
        this.shareForm = shareForm;
        this.homeModel = homeModel;
        this.files = files;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton button = (JButton) actionEventObj;
            String actionCommand = button.getActionCommand();
            if (actionCommand != null && actionCommand.equals("Cancel")) {
                shareForm.dispose();
                HttpServer httpserver = SSNHttpServer.getHttpServer();
                if (httpserver != null) {
                    httpserver.stop(1);
                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel label = (JLabel) mouseEventObj;
             getShareForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            // Tracking this sharing event in Google Analytics
            GoogleAnalyticsUtil.track(SSNConstants.SSN_APP_EVENT_SHARING);
            Thread thread   =   null;
            switch(label.getName()){
                case "FacebookSharing": thread = new Thread() {
                                        boolean isAlreadyLoggedIn = false;
                                        @Override
                                            public void run() {

                                                Set<String> sharedFileList = getFiles();
                                                AccessGrant facebookAccessGrant = getHomeModel().getHomeForm().getFacebookAccessGrant();
                                                if (facebookAccessGrant == null) {
                                                    try {
                                                        LoginWithFacebook loginWithFacebook = new LoginWithFacebook(null);
                                                        loginWithFacebook.setHomeForm(getHomeModel().getHomeForm());
                                                        loginWithFacebook.login();

                                                        boolean processFurther = false;
                                                        while (!processFurther) {
                                                            facebookAccessGrant = getHomeModel().getHomeForm().getFacebookAccessGrant();
                                                            if (facebookAccessGrant == null) {
                                                                Thread.sleep(10000);
                                                            } else {
                                                                processFurther = true;
                                                                isAlreadyLoggedIn = true;
                                                            }
                                                        }
                                                    } catch (InterruptedException ex) {
                                                        logger.error(ex);
                                                    }
                                                }


                                                FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(SSNConstants.SSN_FACEBOOK_API_KEY,
                                                        SSNConstants.SSN_FACEBOOK_SECRET_KEY);
                                                Connection<Facebook> connection = connectionFactory
                                                        .createConnection(facebookAccessGrant);
                                                Facebook facebook = connection.getApi();
                                                MediaOperations mediaOperations = facebook.mediaOperations();

                                                if(!isAlreadyLoggedIn){
                                                   // SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                                                    SSNConfirmationDialogBox confirmeDialog =   new SSNConfirmationDialogBox();
                                                    FacebookProfile userProfile = facebook.userOperations().getUserProfile();
                                                    String userName = "";
                                                    if(userProfile != null){
                                                        userName = userProfile.getName()!=null?userProfile.getName():userProfile.getFirstName();
                                                    }
                                                    confirmeDialog.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation", "", "You are already logged in with "+userName +", Click OK to continue.");
                                                    int result = confirmeDialog.getResult();
                                                    if (result == JOptionPane.YES_OPTION) 
                                                    {
                                                        SwingUtilities.invokeLater(new Runnable() {
                                                            public void run() {
                                                                SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                                                                messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Successfully uploaded.");
                                                                messageDialogBox.setFocusable(true);
                                                               
                                                            }
                                                        });
                                                    } else if (result == JOptionPane.NO_OPTION) {

                                                        AccessGrant facebookAccessGrant1 = null;
                                                        if (facebookAccessGrant1 == null) {
                                                            try {
                                                                LoginWithFacebook loginWithFacebook = new LoginWithFacebook(null);
                                                                loginWithFacebook.setHomeForm(getHomeModel().getHomeForm());
                                                                loginWithFacebook.login();

                                                                boolean processFurther = false;
                                                                while (!processFurther) {
                                                                    facebookAccessGrant1 = getHomeModel().getHomeForm().getFacebookAccessGrant();
                                                                    if (facebookAccessGrant1 == null) {
                                                                        Thread.sleep(10000);
                                                                    } else {
                                                                        processFurther = true;
                                                                        //isAlreadyLoggedIn = true;
                                                                    }
                                                                }
                                                                 connectionFactory = new FacebookConnectionFactory(SSNConstants.SSN_FACEBOOK_API_KEY,
                                                                        SSNConstants.SSN_FACEBOOK_SECRET_KEY);
                                                                 connection = connectionFactory
                                                                        .createConnection(facebookAccessGrant);
                                                                 facebook = connection.getApi();
                                                                 mediaOperations = facebook.mediaOperations();
                                                            } catch (InterruptedException ex) {
                                                                logger.error(ex);
                                                            }
                                                        }
                                                    }
                                                }

                                                String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
                                                final List<String> videoSupportedList = Arrays.asList(videoSupported);

                                                for (String file : sharedFileList) {
                                                    String fileExtension = file.substring(file.lastIndexOf(".") + 1, file.length());
                                                    Resource resource = new FileSystemResource(file);

                                                    if (!videoSupportedList.contains(fileExtension.toUpperCase())) {
                                                        String output = mediaOperations.postPhoto(resource);
                                                    } else {
                                                        String output = mediaOperations.postVideo(resource);
                                                    }

                                                }


                                            getShareForm().dispose();
                                            }
                                        };
                                        thread.start();
                                        break;
                case "TwitterSharing":  LoginWithTwitter.deniedPermission = false;
                                        thread = new Thread() {
                                        boolean isAlreadyLoggedIn = false;
                                            @Override
                                            public void run() {
                                                Set<String> sharedFileList = getFiles();
                                                OAuthToken twitterOAuthToken = getHomeModel().getHomeForm().getTwitterOAuthToken();
                                                if (twitterOAuthToken == null) {
                                                    try {
                                                        LoginWithTwitter loginWithTwitter = new LoginWithTwitter(null);
                                                        loginWithTwitter.setHomeForm(getHomeModel().getHomeForm());
                                                        loginWithTwitter.login();

                                                        boolean processFurther = false;
                                                        while (!processFurther) {
                                                            if(LoginWithTwitter.deniedPermission)
                                                                break;
                                                            
                                                            twitterOAuthToken = getHomeModel().getHomeForm().getTwitterOAuthToken();
                                                            if (twitterOAuthToken == null) {
                                                                Thread.sleep(10000);
                                                            } else {
                                                                processFurther = true;
                                                                isAlreadyLoggedIn = true;
                                                            }
                                                        }
                                                    } catch (IOException | InterruptedException ex) {
                                                        logger.error(ex);
                                                    }
                                                }
                                                if(!LoginWithTwitter.deniedPermission){
                                                Twitter twitter = new TwitterTemplate(SSNConstants.SSN_TWITTER_API_KEY, SSNConstants.SSN_TWITTER_SECRET_KEY,
                                                        twitterOAuthToken.getValue(), twitterOAuthToken.getSecret());
                                                TimelineOperations timelineOperations = twitter.timelineOperations();
                                                if(!isAlreadyLoggedIn){
                                                    SSNConfirmationDialogBox confirmeDialog =   new SSNConfirmationDialogBox();
                                                     TwitterProfile userProfile = twitter.userOperations().getUserProfile();

                                                    String userName = "";
                                                    if(userProfile != null){
                                                        userName = twitter.userOperations().getScreenName()!=null?twitter.userOperations().getScreenName():userProfile.getName();
                                                    }
                                                    confirmeDialog.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation", "", "You are already logged in with "+userName +", Click OK to continue.");
                                                    int result = confirmeDialog.getResult();
                                                    if (result == JOptionPane.YES_OPTION) {
                                                        SwingUtilities.invokeLater(new Runnable() {
                                                            public void run() {
                                                                SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                                                                messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Successfully uploaded.");
                                                                messageDialogBox.setFocusable(true);
                                                               
                                                            }
                                                        });
                                                    }else if(result == JOptionPane.NO_OPTION){

                                                         twitterOAuthToken = null;
                                                        if (twitterOAuthToken == null) {
                                                            try {
                                                                LoginWithTwitter loginWithTwitter = new LoginWithTwitter(null);
                                                                loginWithTwitter.setHomeForm(getHomeModel().getHomeForm());
                                                                loginWithTwitter.login();

                                                                boolean processFurther = false;
                                                                while (!processFurther) {
                                                                    twitterOAuthToken = getHomeModel().getHomeForm().getTwitterOAuthToken();
                                                                    if (twitterOAuthToken == null) {
                                                                        Thread.sleep(10000);
                                                                    } else {
                                                                        processFurther = true;

                                                                    }
                                                                }
                                                                twitter = new TwitterTemplate(SSNConstants.SSN_TWITTER_API_KEY, SSNConstants.SSN_TWITTER_SECRET_KEY,
                                                                twitterOAuthToken.getValue(), twitterOAuthToken.getSecret());
                                                                timelineOperations = twitter.timelineOperations();
                                                            } catch (IOException | InterruptedException ex) {
                                                                logger.error(ex);
                                                            }
                                                        }
                                                    }
                                                }

                                                for (String file : sharedFileList) {
                                                    Resource image = new FileSystemResource(file);

                                                    TweetData tweetData = new TweetData("At " + new Date());
                                                    tweetData.withMedia(image);
                                                    timelineOperations.updateStatus(tweetData);
                                                }
                                            } else {
                                                    SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                                                    messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "User denied for OurHive App permission on twitter.");
                                                    messageDialogBox.setFocusable(true);
                                                }
                                                getShareForm().dispose();
                                            }
                                                
                                        };
                                        thread.start(); 
                                        break;
                case "InstagramSharing":
                                        break;
                case "MailSharing":     try {
                                            String OS = System.getProperty("os.name").toLowerCase();

                                            Set<String> sharedFileList = getFiles();
                                            Set<String> voiceNoteList = new HashSet<String>();
                                            for (String sharedFile : sharedFileList) {
                                                String voiceNote = SSNDao.getVoiceCommandPath(new File(sharedFile).getAbsolutePath());
                                                if (voiceNote != null && !voiceNote.isEmpty()) {
                                                    voiceNoteList.add(voiceNote);
                                                }
                                            }
                                            sharedFileList.addAll(voiceNoteList);

                                            String fileFullPath = "";
                                            String caption = "";
                                            if (sharedFileList.size() == 1) {
                                                fileFullPath = sharedFileList.toArray(new String[0])[0];

                                                caption = SSMMediaGalleryPanel.readMetaDataForTitle(new File(fileFullPath));

                                            } else if (sharedFileList.size() > 1) {
                                                fileFullPath = SSNHelper.createZipFileFromMultipleFiles(sharedFileList);
                                            }

                                            if (OS.contains("win")) {

                                               // String subject = "SSN Subject";
                                                 String subject = caption.equals("")?SSNConstants.SSN_SHARE_WITH_MAIL_SUBJECT:caption ;
                                                String body = "";
                                                String m = "&subject=%s&body=%s";

                                                String outLookExeDir = "C:\\Program Files\\Microsoft Office\\Office14\\Outlook.exe";
                                                String mailCompose = "/c";
                                                String note = "ipm.note";
                                                String mailBodyContent = "/m";

                                                m = String.format(m, subject, body);
                                                String slashA = "/a";

                                                String mailClientConfigParams[] = null;
                                                Process startMailProcess = null;

                                                mailClientConfigParams = new String[]{outLookExeDir, mailCompose, note, mailBodyContent, m, slashA, fileFullPath};
                                                startMailProcess = Runtime.getRuntime().exec(mailClientConfigParams);
                                                OutputStream out = startMailProcess.getOutputStream();
                                                File zipFile = new File(fileFullPath);
                                                zipFile.deleteOnExit();
                                            } else if (OS.indexOf("mac") >= 0) {
                                                //Process p = Runtime.getRuntime().exec(new String[]{String.format("open -a mail ", fileFullPath)});
                                                Desktop desktop = Desktop.getDesktop();
                                                String mailTo = caption.equals("")?"?SUBJECT="+SSNConstants.SSN_SHARE_WITH_MAIL_SUBJECT:caption ;
                                                URI uriMailTo = null;
                                                uriMailTo = new URI("mailto", mailTo, null);
                                                desktop.mail(uriMailTo);
                                            }

                                            this.getShareForm().dispose();
                                        } catch (Exception ex) {
                                            logger.error(ex);
                                        }
                                        break;
                    case "moveCopy":getShareForm().dispose();
                                    File album = new File(SSNHelper.getSsnHiveDirPath());
                                    File[] albumPaths = album.listFiles();
                                    Vector albumNames = new Vector();
                                    for (int i = 0; i < albumPaths.length; i++) {
                                            if(!(albumPaths[i].getName().equals("OurHive")) && SSNHelper.lastAlbum != null && !(albumPaths[i].getName().equals(SSNHelper.lastAlbum)))
                                                albumNames.add(albumPaths[i].getName());
                                    }
                                    if(SSNHelper.lastAlbum != null && !SSNHelper.lastAlbum.equals("OurHive"))
                                    albumNames.insertElementAt("OurHive", 0);
                                    
                                    SSNInputDialogBox inputBox = new SSNInputDialogBox(true, albumNames);
                                    inputBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Copy Media", "Please Select Album Name");
                                    String destAlbumName = inputBox.getTextValue();
                                    if (StringUtils.isNotBlank(destAlbumName)) {
                                        homeModel.moveAlbum(destAlbumName, getFiles());
                                    }
                                    
                }
            getShareForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) mouseEventObj;
            if (jLbl.getName().equalsIgnoreCase("FacebookSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/fb-hover.png")));
            } else if (jLbl.getName().equalsIgnoreCase("TwitterSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/twitter-hover.png")));
            } else if (jLbl.getName().equalsIgnoreCase("moveCopy")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/move-hover.png")));
            } else if (jLbl.getName().equalsIgnoreCase("MailSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/mail-hover.png")));
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) mouseEventObj;
            if (jLbl.getName().equalsIgnoreCase("FacebookSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/fb-normal.png")));
            } else if (jLbl.getName().equalsIgnoreCase("TwitterSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/twitter-normal.png")));
            } else if (jLbl.getName().equalsIgnoreCase("moveCopy")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/move-normal.png")));
            } else if (jLbl.getName().equalsIgnoreCase("MailSharing")) {
                jLbl.setIcon(new ImageIcon(getClass().getResource("/icon/mail-normal.png")));
            }
        }
    }

    public SSNShareForm getShareForm() {
        return shareForm;
    }

    public void setShareForm(SSNShareForm shareForm) {
        this.shareForm = shareForm;
    }

    public SSNHomeModel getHomeModel() {
        return homeModel;
    }

    public void setHomeModel(SSNHomeModel homeModel) {
        this.homeModel = homeModel;
    }

    public Set<String> getFiles() {
        return files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

}
