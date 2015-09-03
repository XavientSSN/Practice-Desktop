package com.ssn.listener;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNMetaDataPanel;
import com.ssn.ui.custom.component.SSNVideoMetadata;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.util.GoogleAnalyticsUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author pkumar2
 */
public class SSNMetadataListener implements MouseListener {

    private SSNMetaDataPanel panel;
    private ArrayList<JTextField> textFields;
    private SSNHomeForm homeForm;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNMetadataListener.class);
    public SSNMetadataListener(SSNMetaDataPanel panel, ArrayList<JTextField> textFields,SSNHomeForm homeForm) {
        this.panel = panel;
        this.textFields = textFields;
        this.setHomeForm(homeForm);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        ArrayList<JTextField> jtxt = textFields;
        panel.getEditLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        if (panel.getEditLabel().getText().equals("EDIT")) {
             File file = new File(panel.getMediaFileLocation());
            // System.out.println("getHomeForm().ssnFileExplorer.m_display"+getHomeForm().ssnFileExplorer.m_display.getText());
             String[] videoSupported = SSNConstants.SSN_METADATA_FORMAT_SUPPORTED;
                List<String> videoSupportedList = Arrays.asList(videoSupported);
                String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
                if(getHomeForm().getFileNamesToBeDeleted()!=null && getHomeForm().getFileNamesToBeDeleted().size()>0)
                {
                    
                    panel.getEditLabel().setText("SAVE");
                    for (JTextField tx : jtxt) {
                        tx.setEnabled(true);
                       tx.setBackground(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
                    }
                    
                    panel.getFacetagTxt().setEnabled(false);
                }
                else if (!videoSupportedList.contains(fileExtension.toUpperCase())) {
                     SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                     dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Metadata editing is not supported for "+ panel.getTypeTxt().getText()+"!");
                }else
                {
                    panel.getEditLabel().setText("SAVE");
                    for (JTextField tx : jtxt) {
                        tx.setEnabled(true);
                       tx.setBackground(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
                    }
                    panel.getFacetagTxt().setEnabled(false);
                }

            
         
        } else if (panel.getEditLabel().getText().equals("SAVE")) {
            panel.getEditLabel().setText("EDIT");
            for (JTextField tx : jtxt) {
                tx.setEnabled(false);
            }

            try {
                String OS = System.getProperty("os.name").toLowerCase();
                
                if(getHomeForm().getFileNamesToBeDeleted()!=null && getHomeForm().getFileNamesToBeDeleted().size()>0)
                {      
                    panel.getFacetagTxt().setEnabled(false);
                    List<String> fileList=new ArrayList<String>();
                         for (Iterator<String> it = getHomeForm().getFileNamesToBeDeleted().iterator(); it.hasNext(); ) 
                            {
                                fileList.add(it.next());
                                
                            }
                         
                         for(String files:fileList)
                         {
                    
                       // File file = new File(panel.getMediaFileLocation());
                          File file = new File(files);
                       // if (panel.getRatingsTxt().getText().isEmpty() || (StringUtils.isNumeric(panel.getRatingsTxt().getText()) && Integer.parseInt(panel.getRatingsTxt().getText())<6)) {
                           boolean flag=false;
                         if(checkVideo(file))
                         {

                            if(SSNHelper.getDeviceType().equals("Desktop-MAC")){
                                flag = true;
                            }else{
                                flag = SSNVideoMetadata.writeVideoMetadata(file, panel.getTitleTxt().getText(),  panel.getCommentsTxt().getText());
                            }

                         }
                         else
                         {
                             if(panel.getLocationTxt().getText()!=null && !panel.getLocationTxt().getText().equals(""))
                             {
                                 if(!file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1,file.getAbsolutePath().length()).equalsIgnoreCase("PNG")){
                                    flag = SSMMediaGalleryPanel.writeImageMetaData(file, panel.getTitleTxt().getText(), panel.getTagText().getText(), panel.getCommentsTxt().getText(), panel.getLocationTxt().getText()+panel.getAddressTxt().getText(), null, null, "","");
                                 }else{
                                     flag=true;
                                 }
                             }
                             else{
                                 if(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1,file.getAbsolutePath().length()).equalsIgnoreCase("PNG")){
                                       flag = SSMMediaGalleryPanel.writeImageMetaData(file, panel.getTitleTxt().getText(), panel.getTagText().getText(), panel.getCommentsTxt().getText(), panel.getAddressTxt().getText(), null, null, "","");
                                }else{
                                     flag=true;
                                 }
                                 
                             }
                         }
                         if (flag) {
                              //String title=panel.getMediaFileLocation();
                             String title=files;
                              int index = title.lastIndexOf(File.separator);
                              String folderPath = title.substring(0, index);
                              int index2 = folderPath.lastIndexOf(File.separator);
                              String folderName = folderPath.substring(index2+1, folderPath.length());
                              if (folderPath.contains(SSNHelper.getFacebookPhotosDirPath())|| folderName.equalsIgnoreCase("Instagram Media")|| SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                                  File folder=null;
                                 if(folderPath.contains(SSNHelper.getFacebookPhotosDirPath())){ 

                                     getHomeForm().ssnFileExplorer.m_display.setText(folderPath);
                                     getHomeForm().setSsnGalleryMediaPath(folderPath);
                                     folder= new File(SSNHelper.getSsnDefaultDirPath());

                                 }else if(folderName.equalsIgnoreCase("Instagram Media") || folderName.equalsIgnoreCase("Instagram")){
                                     getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                                     getHomeForm().setSsnGalleryMediaPath("instagramMedia");
                                    folder = new File(SSNHelper.getSsnDefaultDirPath());  
                                 }
                                 else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                                     getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                                     getHomeForm().setSsnGalleryMediaPath(SSNHelper.getSsnHiveDirPath());
                                    // folder = new File(SSNHelper.getSsnHiveDirPath());
                            }
                                if(folder != null){
                                    if (!folder.exists()) {
                                        folder.mkdir();
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                getHomeForm().getSsnHomeLeftPanel().removeAll();
                                                getHomeForm().getSsnHomeLeftMainPanel().remove(getHomeForm().getSsnHomeLeftPanel());
                                                getHomeForm().ssnFileExplorer = new SSNFileExplorer(homeForm);
                                                getHomeForm().getSsnHomeLeftPanel().add(getHomeForm().ssnFileExplorer);
                                                getHomeForm().getSsnHomeLeftMainPanel().add(getHomeForm().getSsnHomeLeftPanel(), BorderLayout.CENTER);
                                                getHomeForm().getSsnHomeLeftMainPanel().revalidate();
                                                getHomeForm().revalidate();
                                            }
                                        });

                                    }
                                }
                                
                                if(file != null )
                                { 
                                if((panel.getLocationTxt().getText() != null && !panel.getLocationTxt().getText().trim().isEmpty()) || (panel.getTagText().getText() != null && !panel.getTagText().getText().trim().isEmpty()))
                                {
                                 FileUtils.copyFile(file, new File(folder.getAbsolutePath() + File.separator + file.getName()));
                                 }
                                title = folder.getAbsolutePath() + File.separator + file.getName();
                                }
                              }
                              boolean check = SSNDao.checkMediaExist(title);
                             if (!check ) {

                                SSNDao.insertMediaTable(title, panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                            } else {
                                 
                                 SSNGalleryMetaData data=SSNDao.getExistingMetadata(title);
                                 
                                 SSNDao.updateMediaTable(title, data.getUserComments(), "",panel.getLocationTxt().getText(),data.getMediaType(),data.getSsnKeywords()+","+panel.getTagText().getText(),data.getPhotoGrapher(), panel.getTitleTxt().getText());


                             }
                          boolean large = false;   
                          if (getHomeForm().getSsnHomeCenterMainPanel() != null) {
                             for (Component c : getHomeForm().getSsnHomeCenterMainPanel().getComponents()) {
                                 if (c.getName() != null && c.getName().equalsIgnoreCase("buttonPanel")) {
                                      homeForm.getRatingMap().put(homeForm.getCurrentFile().getAbsolutePath(), Integer.parseInt(""));   
                                      homeForm.getSsnHomeCenterMainPanel().remove(homeForm.getSsnHomeCenterButtonPanel());
                                      homeForm.getSsnHomeCenterMainPanel().add(homeForm.getCenterButtonPanel(homeForm.getRatingMap().get(homeForm.getCurrentFile().getAbsolutePath())), BorderLayout.SOUTH);
                                      homeForm.getSsnHomeCenterMainPanel().revalidate();
                                      large = true;
                                 }
                             }
                          }
                          if(!large && !folderName.equalsIgnoreCase("Instagram Media")){
                          SSNDirSelectionListener listener=new SSNDirSelectionListener();
                          listener.createCompontents(getHomeForm().getSsnGalleryMediaPath(), getHomeForm(), true);
                          }
                         }
                         
                           // Tracking this tagging event in Google Analytics
                           GoogleAnalyticsUtil.track(SSNConstants.SSN_APP_EVENT_METADATA_TAGGING);
                }     
               }
                else
                {
                   File file = new File(panel.getMediaFileLocation());
              //  if (panel.getRatingsTxt().getText().isEmpty() || (StringUtils.isNumeric(panel.getRatingsTxt().getText()) && Integer.parseInt(panel.getRatingsTxt().getText())<6)) {
                      boolean flag=false;
                    if(checkVideo(file))
                    {
                        if(SSNHelper.getDeviceType().equals("Desktop-MAC")){
                            flag = true;
                        }else{
                           flag = SSNVideoMetadata.writeVideoMetadata(file, panel.getTitleTxt().getText(),  panel.getCommentsTxt().getText());
                        }
                    }
                    else
                    {
                        if(panel.getLocationTxt().getText()!=null && !panel.getLocationTxt().getText().equals(""))
                        {
                            if(!file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1,file.getAbsolutePath().length()).equalsIgnoreCase("PNG"))
                                flag = SSMMediaGalleryPanel.writeImageMetaData(file, panel.getTitleTxt().getText(), panel.getTagText().getText(), panel.getCommentsTxt().getText(), panel.getLocationTxt().getText()+panel.getAddressTxt().getText(), null, null, "","");
                            else
                                flag    =   true;
                        }
                        else
                        {
                            if(!file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1,file.getAbsolutePath().length()).equalsIgnoreCase("PNG"))
                                flag = SSMMediaGalleryPanel.writeImageMetaData(file, panel.getTitleTxt().getText(), panel.getTagText().getText(), panel.getCommentsTxt().getText(), panel.getAddressTxt().getText(), null, null, "","");
                            else
                                flag    =   true;
                        }
                    }
                    if (flag) {
                         String title=panel.getMediaFileLocation();
                         int index = title.lastIndexOf(File.separator);
                         String folderPath = title.substring(0, index);
                         int index2 = folderPath.lastIndexOf(File.separator);
                         String folderName = folderPath.substring(index2+1, folderPath.length());
                         if (folderPath.contains(SSNHelper.getFacebookPhotosDirPath()) || folderName.equalsIgnoreCase("Instagram Media") || folderName.equalsIgnoreCase("Instagram") || SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                             File folder=null;
                            if(folderPath.contains(SSNHelper.getFacebookPhotosDirPath())){ 

                                getHomeForm().ssnFileExplorer.m_display.setText(folderPath);
                                getHomeForm().setSsnGalleryMediaPath(folderPath);
                                folder= new File(SSNHelper.getSsnDefaultDirPath());

                            }else if(folderName.equalsIgnoreCase("Instagram Media") || folderName.equalsIgnoreCase("Instagram")){
                                getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                                getHomeForm().setSsnGalleryMediaPath("instagramMedia");
                               folder = new File(SSNHelper.getSsnDefaultDirPath());  
                            }
                            else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                             getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                             getHomeForm().setSsnGalleryMediaPath("tagUnTaggedMedia");
                            // folder = new File(folderPath);  
                            }
                            if(folder!= null){
                                if (!folder.exists()) {
                                    folder.mkdir();
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            getHomeForm().getSsnHomeLeftPanel().removeAll();
                                            getHomeForm().getSsnHomeLeftMainPanel().remove(getHomeForm().getSsnHomeLeftPanel());
                                            getHomeForm().ssnFileExplorer = new SSNFileExplorer(homeForm);
                                            getHomeForm().getSsnHomeLeftPanel().add(getHomeForm().ssnFileExplorer);
                                            getHomeForm().getSsnHomeLeftMainPanel().add(getHomeForm().getSsnHomeLeftPanel(), BorderLayout.CENTER);
                                            getHomeForm().getSsnHomeLeftMainPanel().revalidate();
                                            getHomeForm().revalidate();
                                        }
                                    });

                                }
                            }
                            if(file != null && folder != null)
                            { 
                              if((panel.getLocationTxt().getText() != null && !panel.getLocationTxt().getText().trim().isEmpty()) || (panel.getTagText().getText() != null && !panel.getTagText().getText().trim().isEmpty()))
                              {
                               FileUtils.copyFile(file, new File(folder.getAbsolutePath() + File.separator + file.getName()));
                              }
                             title = folder.getAbsolutePath() + File.separator + file.getName();
                            }
                         }
                         
                         boolean check = SSNDao.checkMediaExist(title);
                        if (!check) {
//                                System.out.println("Last Album : " + SSNHelper.lastAlbum);
                            
                            if(panel.getLocationTxt().getText()!=null && !panel.getLocationTxt().getText().equals("")){

                                SSNDao.insertMediaTable(title, panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                if(SSNHelper.lastAlbum != null ){
                                    if(file.getAbsolutePath().contains(SSNHelper.getFacebookPhotosDirPath()) || SSNHelper.lastAlbum.equalsIgnoreCase("Instagram Media")){
                                        SSNDao.insertMediaTable(file.getAbsolutePath(), panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                    }
                                }
                            }else{
                                SSNDao.insertMediaTable(title, panel.getCommentsTxt().getText(), "", panel.getAddressTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                if(SSNHelper.lastAlbum != null ){
                                    if(file.getAbsolutePath().contains(SSNHelper.getFacebookPhotosDirPath()) || SSNHelper.lastAlbum.equalsIgnoreCase("Instagram Media")){
                                        SSNDao.insertMediaTable(file.getAbsolutePath(), panel.getCommentsTxt().getText(), "", panel.getAddressTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                    }
                                }
                            }
                        } else {
                               if(panel.getLocationTxt().getText()!=null && !panel.getLocationTxt().getText().equals("")){
                                    SSNDao.updateMediaTable(title, panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                    if(SSNHelper.lastAlbum != null ){
                                        if(file.getAbsolutePath().contains(SSNHelper.getFacebookPhotosDirPath()) || SSNHelper.lastAlbum.equalsIgnoreCase("Instagram Media")){
                                            SSNDao.updateMediaTable(file.getAbsolutePath(), panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                        }
                                    }
                                }
                               else{
                                  SSNDao.updateMediaTable(title, panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                  if(SSNHelper.lastAlbum != null ){
                                        if(file.getAbsolutePath().contains(SSNHelper.getFacebookPhotosDirPath()) || SSNHelper.lastAlbum.equalsIgnoreCase("Instagram Media") ){
                                            SSNDao.updateMediaTable(file.getAbsolutePath(), panel.getCommentsTxt().getText(), "", panel.getLocationTxt().getText(), panel.getTypeTxt().getText(),panel.getTagText().getText(),"", panel.getTitleTxt().getText());
                                        }
                                    }
                               }
                        }
                     boolean large = false;   
                     if (getHomeForm().getSsnHomeCenterMainPanel() != null) {
                        for (Component c : getHomeForm().getSsnHomeCenterMainPanel().getComponents()) {
                            if (c.getName() != null && c.getName().equalsIgnoreCase("buttonPanel")) {
                                 homeForm.getRatingMap().put(homeForm.getCurrentFile().getAbsolutePath(), Integer.parseInt(""));   
                                 homeForm.getSsnHomeCenterMainPanel().remove(homeForm.getSsnHomeCenterButtonPanel());
                                 homeForm.getSsnHomeCenterMainPanel().add(homeForm.getCenterButtonPanel(homeForm.getRatingMap().get(homeForm.getCurrentFile().getAbsolutePath())), BorderLayout.SOUTH);
                                 homeForm.getSsnHomeCenterMainPanel().revalidate();
                                 large = true;
                            }
                        }
                     }
                    // if(!large && !folderName.equalsIgnoreCase("Facebook Media") && !folderName.equalsIgnoreCase("Instagram Media")){
                     if(!large){
                        SSNDirSelectionListener listener=new SSNDirSelectionListener();
                         listener.createCompontents(getHomeForm().getSsnGalleryMediaPath(), getHomeForm(), true);
                     }
                // Tracking this tagging event in Google Analytics
                    GoogleAnalyticsUtil.track(SSNConstants.SSN_APP_EVENT_METADATA_TAGGING);
                }
                }
                
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
     private static boolean checkVideo(File file)
        {
            String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
            List<String> videoSupportedList = Arrays.asList(videoSupported);
            String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
            if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                return true;
            }
            return false;
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

}
