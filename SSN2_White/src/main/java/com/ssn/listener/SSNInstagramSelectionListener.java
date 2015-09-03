/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.model.InstagramMedia;
import com.ssn.ui.custom.component.SSNAlbumNode;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNIconData;
import com.ssn.ui.custom.component.SSNTreeHelper;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.io.FileUtils;
import org.springframework.social.oauth2.AccessGrant;

/**
 *
 * @author asingh8
 */
public class SSNInstagramSelectionListener implements TreeSelectionListener {
    private SSNTreeHelper treeHelper;
    private SSNFileExplorer fileTree;
    private SSNHomeForm form;
    AccessGrant accessGrant;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNInstagramSelectionListener.class);
    public SSNInstagramSelectionListener(SSNFileExplorer fileTree,SSNHomeForm form, AccessGrant accessGrant) {
        this.treeHelper = new SSNTreeHelper();
        this.fileTree = fileTree;
        this.form = form;
        this.accessGrant = accessGrant;
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node =treeHelper.getTreeNode(event.getPath());
        
        if(this.form.getHiveTree() != null) {
            this.form.getHiveTree().clearSelection();
        }
        
        if(this.form.getFacebookTree() != null) {
            this.form.getFacebookTree().clearSelection();
        }
        
        if(node.isLeaf()) {
            try {
                this.form.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String instagramDirPath = SSNHelper.getInstagramPhotosDirPath();
                fileTree.m_display.setText("instagramMedia");
                String urlString = String.format("https://api.instagram.com/v1/users/self/media/recent/?access_token=%s", accessGrant.getAccessToken());

                List<InstagramMedia> imageList = new ArrayList<>();
                getMedia(urlString, imageList);


                createComponents(this.form, imageList);
            } catch(Exception e) {
                
            }
        }
        
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
    
    private void createComponents(SSNHomeForm ssnHomeForm, List<InstagramMedia> completePhotoList) {
        SSNHelper.toggleDeleteAndShareImages(false,ssnHomeForm);
        
        try {
            List<File> listOfFiles = new ArrayList<File>();
            
            File instagramPhotosDir = new File(SSNHelper.getInstagramPhotosDirPath());
            if(!instagramPhotosDir.exists()) {
                instagramPhotosDir.mkdir();
            }
            
            for (InstagramMedia photo : completePhotoList) {
                String imageUrl = photo.getImageUrl();
                
                URL url = new URL(imageUrl);
                File file = new File(SSNHelper.getInstagramPhotosDirPath() + photo.getId() + ".jpg");
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
            //ssnHomeForm.add(ssnHomeForm.getSsnHomeCenterMainPanel());
            
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
