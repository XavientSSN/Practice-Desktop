/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.event.controller;

import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNHomeModel;
import com.ssn.model.TaggedFace;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.request.SSNFaceRecognitionJobType;
import com.ssn.ws.rest.request.SSNFaceRecognitionRequest;
import com.ssn.ws.rest.response.SSNFaceRecognitionResponse;
import com.ssn.ws.rest.service.SSNFaceRecognitionService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author aarora1
 */
public class SSNImageViewerController extends SSNBaseController{
    
    private File image;
    
    private SSNHomeForm ssnHomeForm;
            
     final    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNImageViewerController.class);
    public SSNImageViewerController(File file,SSNHomeForm ssnHomeForm){
        this.image = file;
        this.ssnHomeForm = ssnHomeForm;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       Object obj = e.getSource();
       if(obj !=null &&  obj instanceof JPanel){
           JPanel panel = (JPanel) obj;
           if(panel.getName().equalsIgnoreCase("imageViewer")){
               List<TaggedFace> faceList = null;
               try {
                   faceList = SSNDao.getTaggedFaces(image.getAbsolutePath());
               } catch (SQLException ex) {
                   Logger.getLogger(SSNImageViewerController.class.getName()).log(Level.SEVERE, null, ex);
               }
               SSNImagePanel imagepanel = (SSNImagePanel) this.getSsnHomeForm().getSsnImagePanel();
               int panelX = imagepanel.getxPosition();
               int panelY = imagepanel.getyPosition();
               List<JPanel> tagPanelList = new ArrayList<JPanel>();
               JLayeredPane lpane = new JLayeredPane();
               if(faceList!=null && !faceList.isEmpty()){
               for (TaggedFace face : faceList) {
                   float x = face.getxCoordinate();
                   float y = face.getyCoordinate();
                   float height = face.getHeight();
                   float width = face.getWidth();
                   final String imageIndex = face.getImageIndex();
                   final String tags = face.getTags();
                   float[] coordinates = SSNHelper.getScaledCoordinatesofTaggedFace(x, y, width, height, this.getSsnHomeForm().getCurrentImage(), this.getSsnHomeForm());
                   if (coordinates != null) {
                       x = coordinates[0];
                       y = coordinates[1];
                       width = coordinates[2];
                       height = coordinates[3];
                   }
                   JPanel tag = new JPanel();
                   tag.setBackground(new Color(0, 0, 0, 1));
                   tag.setBounds(panelX + (int) x, panelY + (int) y, (int) width, (int) height);
                   tag.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                   tagPanelList.add(tag);
                   final Vector<String> comboList = new Vector<String>();
                   String[] tagArray = new String[1];
                   if(tags.contains(",")){
                       tagArray = tags.split(",");
                   }else{
                       tagArray[0] = tags;
                   }
                   for(String val:tagArray){
                       comboList.add(val);
                   }
                   final JComboBox field = new JComboBox(comboList);
                field.setEditable(true);
                field.setVisible(false);
                int fieldWidth = (int) ((int) width * 1.5);
                int fieldX = panelX + (int) x - (int) ((int) width * 0.25);
                field.setBounds(fieldX, panelY + (int) y + (int) height, fieldWidth, 25);
                field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                lpane.add(field, 1, 0);
                tag.addMouseListener(new MouseAdapter() {
                    boolean show = true;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (show) {
                            field.setVisible(true);
                            show = false;
                        } else {
                            field.setVisible(false);
                            show = true;
                        }
                    }
                });
                
                field.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            String selectedValue = field.getEditor().getItem().toString();
                            if (!selectedValue.equals("") && !selectedValue.equalsIgnoreCase(SSNFaceRecognitionRequest.TAG_VALUE)) {
                                SSNFaceRecognitionService service = new SSNFaceRecognitionService();
                                SSNFaceRecognitionRequest faceRenameRequest = new SSNFaceRecognitionRequest();
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.NEW_TAG, selectedValue);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX,imageIndex);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_RENAME.toString());
                                SSNFaceRecognitionResponse faceRenameResponse = service.getResponse(faceRenameRequest);

                                SSNFaceRecognitionRequest faceTrainRequest = new SSNFaceRecognitionRequest();
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.TAGS, selectedValue);
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_TRAIN.toString());
                                SSNFaceRecognitionResponse faceTrainResponse = service.getResponse(faceTrainRequest);

                                SSNFaceRecognitionRequest faceDeleteRequest = new SSNFaceRecognitionRequest();
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_DELETE.toString());
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX, imageIndex);
                                SSNFaceRecognitionResponse faceDeleteResponse = service.getResponse(faceDeleteRequest);

                                boolean added = comboList.add(selectedValue);
                                if (added) {
                                    try {
                                        SSNGalleryMetaData mdata = SSNDao.getSSNMetaData(image.getAbsolutePath());
                                        String newKeywords = "";
                                        if (mdata.getSsnKeywords() == null || mdata.getSsnKeywords().isEmpty()) {
                                            newKeywords = selectedValue;
                                        } else {
                                            boolean exists = false;
                                            if (mdata.getSsnKeywords().contains(",")) {
                                                String[] keywords = mdata.getSsnKeywords().split(",");
                                                for (String s : keywords) {
                                                    if (s.equalsIgnoreCase(selectedValue)) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                            } else {
                                                if (mdata.getSsnKeywords().equalsIgnoreCase(selectedValue)) {
                                                    exists = true;
                                                }
                                            }
                                            if (!exists) {
                                                newKeywords = mdata.getSsnKeywords() + "," + selectedValue;
                                            } else {
                                                newKeywords = mdata.getSsnKeywords();
                                            }
                                        }

                                        
                                        String newTag = "";
                                        if (tags == null || tags.isEmpty()) {
                                            newTag = selectedValue;
                                            SSNDao.updateTaggedFaces(image.getAbsolutePath(), imageIndex, newTag);
                                        } else {
                                            boolean exists = false;

                                            if (!exists) {
                                           
                                                 newTag =  selectedValue;
                                                SSNDao.updateTaggedFaces(image.getAbsolutePath(), imageIndex, newTag);
                                            } 
                                        }
                                        getSsnHomeForm().getSsnHomeRightPanel().removeAll();
                                        getSsnHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(image, getSsnHomeForm()), BorderLayout.NORTH);
                                        getSsnHomeForm().getSsnHomeRightPanel().revalidate();
                                    } catch (SQLException ex) {
                                        java.util.logging.Logger.getLogger(SSNHomeModel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            }
                        }
                    }

                });
           }
        }
               
            this.getSsnHomeForm().getSsnHomeCenterPanel().removeAll();
            this.getSsnHomeForm().getSsnHomeCenterMainPanel().remove(this.getSsnHomeForm().getSsnHomeCenterPanel());

            lpane.add(panel, 0, 0);
            if (tagPanelList.size() > 0) {
                for (JPanel tag : tagPanelList) {
                    lpane.add(tag, 1, 0);
                }
            }

            this.getSsnHomeForm().getSsnHomeCenterPanel().add(lpane);
            this.getSsnHomeForm().getSsnHomeCenterMainPanel().add(this.getSsnHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
            this.getSsnHomeForm().getSsnHomeCenterMainPanel().revalidate();
       }
           
    }
    }
    
    

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public SSNHomeForm getSsnHomeForm() {
        return ssnHomeForm;
    }

    public void setSsnHomeForm(SSNHomeForm ssnHomeForm) {
        this.ssnHomeForm = ssnHomeForm;
    }
    
    
    
    
    
    
    
}
