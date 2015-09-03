/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.listener;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.ui.custom.component.SSNAlbumNode;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNIconData;
import com.ssn.ui.custom.component.SSNTreeHelper;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Cursor;
import org.springframework.social.facebook.api.Photo.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.io.FileUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.MediaOperations;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Photo;

/**
 *
 * @author asingh8
 */
public class SSNFacebookAlbumSelectionListener implements TreeSelectionListener {
    private SSNTreeHelper treeHelper;
    private SSNFileExplorer fileTree;
    private SSNHomeForm form;
    private Facebook facebook;
     static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNFacebookAlbumSelectionListener.class);
    public SSNFacebookAlbumSelectionListener(SSNFileExplorer fileTree,SSNHomeForm form, Facebook facebook) {
        this.treeHelper = new SSNTreeHelper();
        this.fileTree = fileTree;
        this.form = form;
        this.facebook = facebook;
    }

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node =treeHelper.getTreeNode(event.getPath());
        
        if(this.form.getHiveTree() != null) {
            this.form.getHiveTree().clearSelection();
        }
        if(this.form.getInstagramTree() != null) {
            this.form.getInstagramTree().clearSelection();
        }
        
        if(node.isLeaf()) {
            this.form.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SSNAlbumNode fnode = null;
            try{
                if(((SSNIconData) node.getUserObject()).getObject() instanceof SSNAlbumNode )
                    fnode = (SSNAlbumNode) ((SSNIconData) node.getUserObject()).getObject();
            }catch(ClassCastException ee){
                ee.printStackTrace();
                this.form.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            String facebookDirPath = SSNHelper.getFacebookPhotosDirPath();
            if(fnode != null)
                fileTree.m_display.setText(facebookDirPath+File.separator+fnode);
            
            SSNIconData iconData = (SSNIconData) node.getUserObject();
            SSNAlbumNode albumNode = (SSNAlbumNode) iconData.getObject();
            
            MediaOperations mediaOperations = facebook.mediaOperations();
            
            List<Photo> listPhoto, completePhotoList = new ArrayList<Photo>();
            do {
                    PagingParameters pagingParameters = new PagingParameters(
                                    100, completePhotoList.size(), null, Calendar.getInstance()
                                                    .getTimeInMillis());
                    listPhoto = mediaOperations.getPhotos(albumNode.getAlbum().getId(), pagingParameters);
                    completePhotoList.addAll(listPhoto);

            } while(listPhoto.size() > 0);
            
            
            createComponents(this.form, completePhotoList, albumNode);
        }
        
    }
    
    private void createComponents(SSNHomeForm ssnHomeForm, List<Photo> completePhotoList, SSNAlbumNode albumNode) {
        SSNHelper.toggleDeleteAndShareImages(false,ssnHomeForm);
        
        try {
            List<File> listOfFiles = new ArrayList<File>();
            
            File facebookPhotosDir = new File(SSNHelper.getFacebookPhotosDirPath() + albumNode.getAlbum().getName() + File.separator);
            if(!facebookPhotosDir.exists()) {
                facebookPhotosDir.mkdir();
            }
            
            for (Photo photo : completePhotoList) {
                String imageUrl = "";
                for(Image image : photo.getImages()) {
                    if(image != null && image.getHeight() <= 500) {
                        imageUrl = image.getSource();
                        break;
                    }
                }
                
                if(imageUrl.isEmpty()) {
                    imageUrl = photo.getSource();
                }
                URL url = new URL(imageUrl);
                File file = new File(facebookPhotosDir.getAbsolutePath() + File.separator + photo.getId() + ".jpg");
                if(!file.exists()) {
                    try {
                        FileUtils.copyURLToFile(url, file);
                        listOfFiles.add(file);
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                } else {
                    listOfFiles.add(file);
                }
            }

            File[] fileArray = listOfFiles.toArray(new File[0]);
            SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, ssnHomeForm);
            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            ssnHomeForm.getSsnHomeCenterPanel().removeAll();
            ssnHomeForm.getSsnHomeCenterMainPanel().removeAll();

            ssnHomeForm.getSsnHomeCenterPanel().add(ssnHomeForm.getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(ssnHomeForm.ssnFileExplorer.m_display.getText())));
            ssnHomeForm.getSsnHomeCenterMainPanel().add(ssnHomeForm.getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(ssnHomeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
            ssnHomeForm.getSsnHomeCenterMainPanel().add(ssnHomeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
            ssnHomeForm.getHomeModel().getSSNMediaFolderProperties(ssnHomeForm.getHomeModel().getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
           // ssnHomeForm.add(ssnHomeForm.getSsnHomeCenterMainPanel());
            
//             ssnHomeForm.getSplitPane().setLeftComponent(ssnHomeForm.getSsnHomeLeftMainPanel());
//             ssnHomeForm.getSplitPane().setRightComponent(ssnHomeForm.getSsnHomeCenterMainPanel());
//                //ssnHomeForm.getSplitPane().setDividerLocation(200);
//             ssnHomeForm.getSplitPane().revalidate();
//             ssnHomeForm.getSplitPane().repaint();
            
            ssnHomeForm.revalidate();
            ssnHomeForm.repaint();
            ssnHomeForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
