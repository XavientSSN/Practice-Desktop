/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.model.InstagramMedia;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNToolBar;
import com.ssn.ui.form.SSNUntaggedMediaForm;
import com.ssn.ws.rest.service.LoginWithFacebook;
import com.ssn.ws.rest.service.LoginWithInstagram;
import com.ssn.ws.rest.service.SSNHttpServer;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Album;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.MediaOperations;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Photo;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

/**
 *
 * @author aarora1
 */
public class SSNUntaggedMediaController extends SSNBaseController {
    
    private SSNUntaggedMediaForm untaggedMediaForm;
    private SSNHomeModel homeModel;
    final    private Logger logger = Logger.getLogger(SSNUntaggedMediaController.class);

    public SSNUntaggedMediaController() {

    }

    public SSNUntaggedMediaController(SSNUntaggedMediaForm untaggedMediaForm, SSNHomeModel homeModel) {
        this.untaggedMediaForm = untaggedMediaForm;
        this.homeModel = homeModel;
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton button = (JButton) actionEventObj;
            String actionCommand = button.getActionCommand();
            if (actionCommand != null && actionCommand.equals("Cancel")) {
                SSNHomeController.isUnTaggedOpen  =   false;
                untaggedMediaForm.dispose();
            }
        }

    }
    
     @Override
    public void mouseClicked(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
            JLabel label = (JLabel) mouseEventObj;
            JLabel toolbarLabel = SSNHomeController.currentLabel;
            this.getHomeModel().getHomeForm().getHomeController().setIconImage(toolbarLabel,"/icon/tagged-untagged-media.png","allUntagged",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
            this.getHomeModel().getHomeForm().getHomeController().setIconImage(SSNToolBar.desktopHomeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            this.getHomeModel().getHomeForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            int timoutCount = 0;
            logger.info("mouseClicked() label " + label.getName() );
            switch(label.getName()){
                case "FacebookMedia":
                    untaggedMediaForm.dispose();
                    String facebookMessage  =   "User denied for OurHive App permission on facebook.";
                    if ((this.getHomeModel().getHomeForm().getFacebookAccessGrant() != null && !this.getHomeModel().getHomeForm().isLoggedInFromFaceBook() && getFaceBookConnection() != null)  && this.getHomeModel().getHomeForm().isIsSocialSearched()) 
                    {

                        try {
                            File searchFolder = new File(SSNHelper.getFacebookPhotosDirPath());
                            File folder = new File(SSNHelper.getSsnDefaultDirPath());
                            File[] files = searchFolder.listFiles();
                            String defaultAlbumPath = "";
                            if(searchFolder.list().length > 0){
                                defaultAlbumPath = (searchFolder.listFiles())[0].getAbsolutePath();
                                files = new File(defaultAlbumPath).listFiles();
                            }
                            
                            List<File> fileList = new ArrayList<File>();
                            for (File f : files) {
                                fileList.add(f);
                            }
                            Iterator<File> iterator = fileList.iterator();
                            while (iterator.hasNext()) {
                                File f = iterator.next();
                                boolean check = false;
                                try {
                                    check = SSNDao.checkMediaExist(folder.getAbsolutePath() + File.separator + f.getName());
                                } catch (SQLException ex) {
                                    //ex.printStackTrace();
                                    logger.error(ex);
                                }
                                if (check) {
                                   // iterator.remove();
                                }
                            }
                            File[] fileArray = new File[fileList.size()];
                            for (int i = 0; i < fileList.size(); i++) {
                                fileArray[i] = fileList.get(i);
                            }

                            
                            //this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText("facebookMedia");
                           // this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                            this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText(defaultAlbumPath);
                            
                            //this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                            this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().clear();
                            //this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);
                            SSNHelper.toggleDeleteAndShareImages(false, this.getHomeModel().getHomeForm());
                            SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeModel().getHomeForm());

                            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                            this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().removeAll();
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().removeAll();

                            this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().add(this.getHomeModel().getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())));
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                            this.getHomeModel().getSSNMediaFolderProperties(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
                            
                            renderLeftPanel();
                            this.getHomeModel().getHomeForm().revalidate();
                        } catch (IOException ex) {
                            //java.util.logging.Logger.getLogger(SSNUntaggedMediaController.class.getName()).log(Level.SEVERE, null, ex);
                            logger.error(ex);
                        }

                    } else {
                        
                        logger.info("Facebook login user not logged in mouseClicked switch case else part");
                        LoginWithFacebook.deniedPermission = false;
                        AccessGrant facebookAccessGrant = getHomeModel().getHomeForm().getFacebookAccessGrant();
                        if (facebookAccessGrant == null) {
                            try {

                                LoginWithFacebook loginWithFacebook = new LoginWithFacebook(null);
                                loginWithFacebook.setHomeForm(getHomeModel().getHomeForm());
                                loginWithFacebook.login();
                                boolean processFurther = false;
                                
                                while (!processFurther) {
                                    if (LoginWithFacebook.deniedPermission) {
                                        break;
                                    }
                                    facebookAccessGrant = getHomeModel().getHomeForm().getFacebookAccessGrant();
                                    if (facebookAccessGrant == null) {
                                        
                                        if(timoutCount > (5*5000)){
                                            LoginWithFacebook.deniedPermission = true;
                                            facebookMessage = "No response from Facebook.";
                                            SSNHttpServer.getHttpServer().stop(0);
                                            break;
                                        }else{
                                            Thread.sleep(5000);
                                            timoutCount+=5000;
                                        }
                                    } else {
                                        processFurther = true;
                                    }
                                }
                            } catch (InterruptedException ex) {
                                logger.error(ex);
                                //ex.printStackTrace();
                            } catch (Exception ex) {
                                logger.error(ex);
                                //ex.printStackTrace();
                            }
                        }
                        if (!LoginWithFacebook.deniedPermission) {
//                            FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(SSNConstants.SSN_FACEBOOK_API_KEY,
//                                    SSNConstants.SSN_FACEBOOK_SECRET_KEY);
                            Connection<Facebook> connection = getFaceBookConnection();
                            Facebook facebook = connection.getApi();
                            MediaOperations mediaOperations = facebook.mediaOperations();

                            PagedList<Album> albums = mediaOperations.getAlbums();
                                
                             if (albums.size() > 0) {
                                 Collections.sort(albums, new Comparator<Album>() {
                                    @Override
                                    public int compare(final Album object1, final Album object2) {
                                        return object1.getName().compareTo(object2.getName());
                                    }
                                   } );
                               }
                            
                            if (albums != null && !albums.isEmpty()) {
                                try {
                                    List<Photo> completePhotoList = new ArrayList<Photo>();
                                    String albumName    =   "";
                                    for (Album album : albums) {
                                        List<Photo> listPhoto = new ArrayList<Photo>();
                                        int captured = 0;
                                        do {

                                            PagingParameters pagingParameters = new PagingParameters(
                                                    100, captured, null, Calendar.getInstance()
                                                    .getTimeInMillis());
                                            listPhoto = mediaOperations.getPhotos(album.getId(), pagingParameters);
                                            captured += listPhoto.size();
                                            completePhotoList.addAll(listPhoto);
                                            albumName   =    album.getName();
                                            File facebookPhotosDir = new File(SSNHelper.getFacebookPhotosDirPath() + album.getName() + File.separator);
                                            if(!facebookPhotosDir.exists()) {
                                                facebookPhotosDir.mkdir();
                                            }
                                            
                                        } while (listPhoto.size() > 0);
                                        break;
                                    }
                                    File searchFolder = new File(SSNHelper.getFacebookPhotosDirPath()+File.separator + albumName);
                                    if (!searchFolder.exists()) {
                                        searchFolder.mkdirs();
                                    }else{
                                        //delete whole directory and create new one each time  
                                        FileUtils.deleteDirectory(searchFolder);
                                        searchFolder.mkdir();
                                    }

                                    for (Photo photo : completePhotoList) {
                                        try {
                                            String imageUrl = "";
                                            for (Photo.Image image : photo.getImages()) {
                                                if (image != null && image.getHeight() <= 500) {
                                                    imageUrl = image.getSource();
                                                    break;
                                                }
                                            }

                                            if (imageUrl.isEmpty()) {
                                                imageUrl = photo.getSource();
                                            }
                                            URL url = new URL(imageUrl);
                                            File file = new File(searchFolder.getAbsolutePath() + File.separator + photo.getId() + ".jpg");
                                            if (!file.exists()) {
                                                try {
                                                    FileUtils.copyURLToFile(url, file);
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    logger.error(ex);
                                                }
                                            }
                                        } catch (MalformedURLException ex) {
                                            //    java.util.logging.Logger.getLogger(SSNUntaggedMediaController.class.getName()).log(Level.SEVERE, null, ex);
                                                logger.error(ex);
                                        }

                                    }
                                    
                                    this.getHomeModel().getHomeForm().setIsSocialSearched(true);
                                    File[] files = searchFolder.listFiles();
                                    File folder = new File(SSNHelper.getSsnDefaultDirPath());
                                    List<File> tempFileList = Arrays.asList(files);

                                    List<File> fileList = new ArrayList<File>();
                                    fileList.addAll(tempFileList);
                                    
                                    Iterator<File> iterator = fileList.iterator();
                                    while (iterator.hasNext()) {
                                        File f = iterator.next();
                                        boolean check = false;
                                        try {
                                            check = SSNDao.checkMediaExist(folder.getAbsolutePath() + File.separator + f.getName());
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                        if (check) {
                                            //iterator.remove();
                                        }
                                    }
                                    File[] fileArray = new File[fileList.size()];
                                    for (int j = 0; j < fileList.size(); j++) {
                                        fileArray[j] = fileList.get(j);
                                    }
                                    String rootPath = SSNHelper.getSsnHiveDirPath();
                                    //this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText("facebookMedia");
                                    //this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                                    
                                    this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText(searchFolder.getAbsolutePath());
                                   // this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().clear();
                                   // this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);
                                   // this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                                    SSNHelper.toggleDeleteAndShareImages(false, this.getHomeModel().getHomeForm());
                                    SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeModel().getHomeForm());

                                    contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                                    this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().removeAll();
                                    this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().removeAll();

                                    this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().add(this.getHomeModel().getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())));
                                    this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                                    this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                                    this.getHomeModel().getSSNMediaFolderProperties(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
                                    
                                    renderLeftPanel();
                                    this.getHomeModel().getHomeForm().revalidate();
                                   // System.out.println("Show gallery  " + new Date());
                                } catch (IOException ex) {
                                        logger.error(ex);
                                }

                            }
                        } else {
                            SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                            messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", facebookMessage);
                            messageDialogBox.setFocusable(true);
                        }
                    }
                    break;
                case "InstagramMedia":
                    untaggedMediaForm.dispose();
                    String instagarmMessage =   "User denied for OurHive App permission on instagram!";
                    if ((this.getHomeModel().getHomeForm().getInstagramAccessGrant() != null && !this.getHomeModel().getHomeForm().isLoggedInFromInstagram()) || this.getHomeModel().getHomeForm().isInstagramSearched()) {
                        try {
                           // System.out.println("inside ");
                            File searchFolder = new File(SSNHelper.getSsnWorkSpaceDirPath() + File.separator + "Instagram Media");
                            File folder = new File(SSNHelper.getSsnHiveDirPath() + File.separator + "InstagramMedia");
                            File[] files = searchFolder.listFiles();
                            List<File> fileList = new ArrayList<File>();
                            for (File f : files) {
                                fileList.add(f);
                            }
                            Iterator<File> iterator = fileList.iterator();
                            while (iterator.hasNext()) {
                                File f = iterator.next();
                                boolean check = false;
                                try {
                                    check = SSNDao.checkMediaExist(folder.getAbsolutePath() + File.separator + f.getName());
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                if (check) {
                                   // iterator.remove();
                                }
                            }
                            File[] fileArray = new File[fileList.size()];
                            for (int i = 0; i < fileList.size(); i++) {
                                fileArray[i] = fileList.get(i);
                            }

                            String rootPath = SSNHelper.getSsnHiveDirPath();
                            
                            this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);
                            this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
                            this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                            
                            //this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                            //this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText(searchFolder.getAbsolutePath());
                            this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().clear();
                            this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);
                            SSNHelper.toggleDeleteAndShareImages(false, this.getHomeModel().getHomeForm());
                            SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeModel().getHomeForm());

                            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                            this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().removeAll();
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().removeAll();

                            this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().add(this.getHomeModel().getHomeForm().getScrollPane(contentPane, "Instagram Media"));
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                            this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                            this.getHomeModel().getSSNMediaFolderProperties(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
                            renderLeftPanel();
                            this.getHomeModel().getHomeForm().revalidate();
                        } catch (IOException ex) {
                            logger.error(ex);
                        }
                    } else {
                        try {
                           // System.out.println("cancle " + LoginWithInstagram.deniedInstagramPermission);
                            LoginWithInstagram.deniedInstagramPermission = false;
                            AccessGrant instgramAccessGrant = getHomeModel().getHomeForm().getInstagramAccessGrant();
                            if (instgramAccessGrant == null) {
                                try {

                                    LoginWithInstagram loginWithInstagram = new LoginWithInstagram(getHomeModel().getHomeForm());

                                    loginWithInstagram.login();
                                    boolean processFurther = false;
                                    while (!processFurther) {
                                        if (LoginWithInstagram.deniedInstagramPermission) {
                                            break;
                                        }
                                        instgramAccessGrant = getHomeModel().getHomeForm().getInstagramAccessGrant();
                                        if (instgramAccessGrant == null) {
                                           if(timoutCount > (5*5000)){
                                            LoginWithInstagram.deniedInstagramPermission = true;
                                            instagarmMessage = "No response from instagram.";
                                            SSNHttpServer.getHttpServer().stop(0);
                                            break;
                                        }else{
                                            Thread.sleep(5000);
                                            timoutCount+=5000;
                                        }
                                        } else {
                                            processFurther = true;
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    logger.error(ex);
                                }
                            }
                            if (!LoginWithInstagram.deniedInstagramPermission) {
                                String urlString = String.format("https://api.instagram.com/v1/users/self/media/recent/?access_token=%s", instgramAccessGrant.getAccessToken());
                                List<InstagramMedia> imageList = new ArrayList<>();
                                getMedia(urlString, imageList);
                                File searchFolder = new File(SSNHelper.getSsnWorkSpaceDirPath() + File.separator + "Instagram Media");
                                if (!searchFolder.exists()) {
                                        searchFolder.mkdirs();
                                    }else{
                                        //delete whole directory and create new one each time  
                                        FileUtils.deleteDirectory(searchFolder);
                                        searchFolder.mkdir();
                                    }
                                for (InstagramMedia photo : imageList) {
                                    String imageUrl = photo.getImageUrl();

                                    URL url = new URL(imageUrl);
                                    File file = new File(searchFolder.getAbsolutePath() + File.separator + photo.getId() + ".jpg");
                                    if (!file.exists()) {
                                        try {

                                            FileUtils.copyURLToFile(url, file);

                                        } catch (Exception ex) {
                                            logger.error(ex);
                                        }
                                    }
                                }
                                this.getHomeModel().getHomeForm().setInstagramSearched(true);
                                File[] files = searchFolder.listFiles();
                                File folder = new File(SSNHelper.getSsnDefaultDirPath());
                                List<File> fileList = new ArrayList<File>();
                                for (File f : files) {
                                    fileList.add(f);
                                }
                                Iterator<File> iterator = fileList.iterator();
                                while (iterator.hasNext()) {
                                    File f = iterator.next();
                                    boolean check = false;
                                    try {
                                        check = SSNDao.checkMediaExist(folder.getAbsolutePath() + File.separator + f.getName());
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        logger.error(ex);
                                    }
                                    if (check) {
                                       //  iterator.remove();
                                    }
                                }
                                File[] fileArray = new File[fileList.size()];
                                for (int j = 0; j < fileList.size(); j++) {
                                    fileArray[j] = fileList.get(j);
                                }
                                String rootPath = SSNHelper.getSsnHiveDirPath();
                               
                                this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);
                                this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
                                this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                                
                                //this.getHomeModel().getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                               // this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.setText(searchFolder.getAbsolutePath());
                                this.getHomeModel().getHomeForm().getFileNamesToBeDeleted().clear();
                                //this.getHomeModel().getHomeForm().setCurrentSelectedFile(null);

                                SSNHelper.toggleDeleteAndShareImages(false, this.getHomeModel().getHomeForm());
                                SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeModel().getHomeForm());

                                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                                this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().removeAll();
                                this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().removeAll();

                                this.getHomeModel().getHomeForm().getSsnHomeCenterPanel().add(this.getHomeModel().getHomeForm().getScrollPane(contentPane, "Instagram Media"));
                                this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                                this.getHomeModel().getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeModel().getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                                this.getHomeModel().getSSNMediaFolderProperties(this.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
                                
                                renderLeftPanel();
                                this.getHomeModel().getHomeForm().revalidate();
                            } else {
                                SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                                messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", instagarmMessage);
                                messageDialogBox.setFocusable(true);
                            }

                        } catch (ProtocolException ex) {
                            // ex.printStackTrace();
                            logger.error(ex);
               //             java.util.logging.Logger.getLogger(SSNUntaggedMediaController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            //ex.printStackTrace();
                            logger.error(ex);
                 //         java.util.logging.Logger.getLogger(SSNUntaggedMediaController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    break;
                case "deviceMedia":
                    untaggedMediaForm.dispose();
                    this.getHomeModel().findTagUntaggedMedia();
                    break;
                case "Cancel":
                    untaggedMediaForm.dispose();
            }
            SSNHomeController.isUnTaggedOpen  =   false;
            untaggedMediaForm.dispose();
            this.getHomeModel().getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
     public void renderLeftPanel(){
        
        this.getHomeModel().getHomeForm().getSsnHomeLeftPanel().removeAll();
        this.getHomeModel().getHomeForm().getSsnHomeLeftMainPanel().remove(this.getHomeModel().getHomeForm().getSsnHomeLeftPanel());
        this.getHomeModel().getHomeForm().ssnFileExplorer = new SSNFileExplorer(this.getHomeModel().getHomeForm());
        this.getHomeModel().getHomeForm().getSsnHomeLeftPanel().add(this.getHomeModel().getHomeForm().ssnFileExplorer);
        this.getHomeModel().getHomeForm().getSsnHomeLeftMainPanel().revalidate();
        this.getHomeModel().getHomeForm().revalidate();
        
    }
    
    private void getMedia(String urlString, List<InstagramMedia> imageList) throws MalformedURLException, ProtocolException, IOException {
        StringBuilder builder = new StringBuilder();
        
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        if (conn.getResponseCode() == 200) {
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
        }
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> outputJSON = mapper.readValue(builder.toString(), Map.class);
        List<Object> dataList = (List<Object>) outputJSON.get("data");
        if(dataList != null) {
            for(Object data : dataList) {
                InstagramMedia media = new InstagramMedia();
                
                Map<String, Object> dataNode = (Map<String, Object>) data;
                String id = (String) dataNode.get("id");
                media.setId(id);
                
                Map<String, Object> images = (Map<String, Object>) dataNode.get("images");
                if(images != null) {
                    Map<String, Object> standardImage = (Map<String, Object>) images.get("standard_resolution");
                    if(standardImage != null) {
                        String imageURL = (String) standardImage.get("url");
                        media.setImageUrl(imageURL);
                                
                    }
                }
                imageList.add(media);
            }
            
        }
        
        Map<String, Object> pagination = (Map<String, Object>) outputJSON.get("pagination");
        if(pagination != null) {
            String nextUrl = (String) pagination.get("next_url");
            if(nextUrl != null && !nextUrl.isEmpty()) {
                getMedia(nextUrl, imageList);
            }
        }
    }
    
    public Connection<Facebook> getFaceBookConnection(){
        Connection<Facebook> connection = null;
        try{
            FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(SSNConstants.SSN_FACEBOOK_API_KEY,SSNConstants.SSN_FACEBOOK_SECRET_KEY);
            connection = connectionFactory.createConnection(this.getHomeModel().getHomeForm().getFacebookAccessGrant());
            return connection;
        }catch(InvalidAuthorizationException ee){
            getHomeModel().getHomeForm().setFacebookAccessGrant(null) ;
            return connection;
        }
    }

    public SSNUntaggedMediaForm getUntaggedMediaForm() {
        return untaggedMediaForm;
    }

    public void setUntaggedMediaForm(SSNUntaggedMediaForm untaggedMediaForm) {
        this.untaggedMediaForm = untaggedMediaForm;
    }

    public SSNHomeModel getHomeModel() {
        return homeModel;
    }

    public void setHomeModel(SSNHomeModel homeModel) {
        this.homeModel = homeModel;
    }
    
    
    
}
