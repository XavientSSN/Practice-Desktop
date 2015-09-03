package com.ssn.listener;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import static com.ssn.helper.SSNHelper.getFacebookPhotosDirPath;
import static com.ssn.helper.SSNHelper.lastAlbum;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNFileNode;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNTreeHelper;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.MenuSelectionManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author pkumar2
 */
public class SSNDirSelectionListener implements TreeSelectionListener {

    private SSNTreeHelper treeHelper;
    private SSNFileExplorer fileTree;
    private SSNHomeForm form;
    static int iT = 0;
    static int dT = 0;
    public SSNGalleryHelper contentPane = null;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNDirSelectionListener.class);
    public SSNDirSelectionListener() {
    }
  
    
    public SSNDirSelectionListener(SSNFileExplorer fileTree, SSNHomeForm form) {

        this.treeHelper = new SSNTreeHelper();
        this.fileTree = fileTree;
        this.form = form;
    }
    
    Boolean homeClicked = null;

    
    public void valueChanged(TreeSelectionEvent event, Boolean homeClicked) {

        if(homeClicked != null){
             
            this.homeClicked = homeClicked;
        }
        valueChanged(event);
    }

    public void valueChanged(TreeSelectionEvent event) {
        iT = 0;
        dT = 0;
        
        form.setCheckMultiSelection(false);
        this.form.setCurrentSelectedFile(null);
        DefaultMutableTreeNode node ;
        SSNFileNode fnode = null;
        List hiveFiles = new ArrayList();
        //System.out.println("valueChanged  ");
        if(event != null){
            node = treeHelper.getTreeNode(event.getPath());
            fnode = treeHelper.getFileNode(node);
        }
        MenuSelectionManager.defaultManager().clearSelectedPath();
        if (this.form.getFacebookTree() != null) {
            this.form.getFacebookTree().clearSelection();
        }

        if (this.form.getInstagramTree() != null) {
            this.form.getInstagramTree().clearSelection();
        }

        if (fnode != null) {
           
            form.getHomeController().refreshIconImageToDefault();
            if(fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia")&& !fileTree.m_display.getText().equals("tagUnTaggedMedia"))
        
            fileTree.m_display.setText(fnode.getFile().getAbsolutePath());
            
            if (fnode.getFile().isDirectory()) 
            {
                this.fileTree.setSelectedFolder(fnode.getFile().getName());
                getSSNMediaFolderProperties(fnode.getFile().getPath());
                File[] file = fnode.getFile().listFiles();
                for (File f : file) {
                    if (f.isFile()) {
                        hiveFiles.add(f.getName());
                    } else {
                    }
                }
            }

            if (hiveFiles != null && hiveFiles.size() > 0) {

                createCompontents(fnode.getFile().getAbsolutePath(), form, true);
            } else {
                createCompontents(fnode.getFile().getAbsolutePath(), form, false);
            }

        } else {
            
                SSNHelper.toggleDeleteAndShareImages(false, form);
                String path = SSNHelper.getSsnHiveDirPath();
                if(this.homeClicked != null && this.homeClicked){
                    path = SSNHelper.getSsnDefaultDirPath();
                }
                
                form.getFileNamesToBeDeleted().clear();
                getSSNMediaFolderProperties(path);
         

                if(fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia")&& !fileTree.m_display.getText().equals("tagUnTaggedMedia"))

                fileTree.m_display.setText(path);
                
                getForm().ssnFileExplorer.m_tree.setSelectionRow(1);
                long size = FileUtils.sizeOfDirectory(new File(path));
                
                if(this.homeClicked != null && this.homeClicked){
                    this.homeClicked = null;
                    File file1 = new File(path);
                   

                    if(fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia")&& !fileTree.m_display.getText().equals("tagUnTaggedMedia"))

                        fileTree.m_display.setText(file1.getAbsolutePath());
                        //just copied from above if block
                        if (file1.isDirectory()) {

                            this.form.setCurrentSelectedFile(null);
                            fileTree.setSelectedFolder(file1.getName());


                            File[] file = file1.listFiles();
                            for (File f : file) {
                                if (f.isFile()) {
                                    hiveFiles.add(f.getName());
                                } else {
                                }
                            }
                        }

                        if (hiveFiles != null && hiveFiles.size() > 0) {

                            createCompontents(file1.getAbsolutePath(), form, true);
                        } else {
                            createCompontents(file1.getAbsolutePath(), form, false);
                        }
                    
                }else if(size > 0){
                    // if there are images then show all 
//                    getForm().getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
//                    getForm().getHomeModel().viewAllAlbums();
//                    getForm().getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }else{
                    // show welcome screen
                    addWelcomeIcons();
                }
                 //this.form.initHomePanels();
                
                this.form.getSsnHomeCenterMainPanel().revalidate();
                this.form.getSsnHomeCenterMainPanel().repaint();
              
           
        }
    }

    public SSNGalleryMetaData getSSNMediaFolderProperties(String mediaPath) {
        
        if(null == mediaPath  || mediaPath.equals("viewAllAlbums") || mediaPath.equals("instagramMedia") || mediaPath.equals("tagUnTaggedMedia")|| mediaPath.contains(getFacebookPhotosDirPath()))
        {
            mediaPath = SSNHelper.getSsnDefaultDirPath();
        }

        SSNGalleryMetaData data = new SSNGalleryMetaData();
        File folder = new File(mediaPath);
        listFiles(folder);
        data.setMediaLocation(mediaPath);
        data.setTitle(folder.getName());
        data.setNoOfFiles(iT);
        data.setNoOfFolders(dT);

        Date dNow = new Date(folder.lastModified());
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        data.setModiFied(ft.format(dNow));

        long size = FileUtils.sizeOfDirectory(folder);
        double flKB = (double) size / 1024;
        double flMB = flKB / 1024;
        String s = String.format("%.2f", flMB);
        data.setSize(s);

        form.getSsnHomeRightPanel().removeAll();
        form.getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaFolderPanel(data), BorderLayout.NORTH);
        form.getSsnHomeRightPanel().revalidate();
        form.getSsnHomeRightPanel().repaint();
        form.revalidate();
        form.repaint();

        return data;
    }

    private static void listFiles(File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                if(fileEntry.length() > 0)
                    iT++;
            } else if (fileEntry.isDirectory()) {
                dT++;
                listFiles(fileEntry);

            }
        }

    }

    private void removeCompontents(SSNHomeForm ssnHomeForm) {
        ssnHomeForm.remove(ssnHomeForm.getSsnHomeCenterPanel());
        ssnHomeForm.revalidate();
        ssnHomeForm.repaint();

    }

    public void createCompontents(String mediaPath, SSNHomeForm ssnHomeForm, boolean fileShowFlag) {
        ssnHomeForm.getFileNamesToBeDeleted().clear();
        SSNHelper.toggleDeleteAndShareImages(false, ssnHomeForm);
       // System.out.println("mediaPath-createCompontents::"+mediaPath);
        SSNHelper.toggleDeleteImage(true,ssnHomeForm);  
        try {
            if (fileShowFlag) {
                ssnHomeForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                contentPane = new SSNGalleryHelper(mediaPath, ssnHomeForm, "ALL");
                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                ssnHomeForm.getSsnHomeCenterMainPanel().removeAll();
                ssnHomeForm.getSsnHomeCenterPanel().removeAll();
                

                ssnHomeForm.getSsnHomeCenterPanel().add(ssnHomeForm.getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(ssnHomeForm.ssnFileExplorer.m_display.getText())));
                ssnHomeForm.getSsnHomeCenterMainPanel().add(ssnHomeForm.getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(ssnHomeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                ssnHomeForm.getSsnHomeCenterMainPanel().add(ssnHomeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
                ssnHomeForm.revalidate();
                ssnHomeForm.repaint();
                ssnHomeForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            } else {
                if (contentPane != null) {
                    ssnHomeForm.remove(contentPane);
                }

                addWelcomeIcons();

                ssnHomeForm.revalidate();
                
               
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
       
    }
     private void addWelcomeIcons(){
        try {
            URL imgURL = getClass().getResource("/images/dashboard-welcome.png");
            BufferedImage image = ImageIO.read(imgURL);
            
            this.form.getSsnHomeCenterMainPanel().removeAll();
            this.form.getSsnHomeCenterPanel().removeAll();
            //this.form.getSsnHomeCenterPanel().add(welcomeLabel, BorderLayout.CENTER);
            this.form.getSsnHomeCenterMainPanel().add(this.form.getSsnHomeCenterPanel(), BorderLayout.NORTH);
            this.form.getSsnHomeCenterMainPanel().add(this.getForm().getWelcomePanelVerticalScroller(this.getForm().getWelcomePanel()), BorderLayout.CENTER);
            
            
           // this.form.add(this.form.getSsnHomeCenterMainPanel())
            //this.form.getSplitPane().setLeftComponent(this.form.getSsnHomeLeftMainPanel());
            //this.form.getSplitPane().setRightComponent(this.form.getSsnHomeCenterMainPanel());
            
        } catch (IOException ex) {
            Logger.getLogger(SSNDirSelectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    public SSNHomeForm getForm() {
        return form;
    }

    public void setForm(SSNHomeForm form) {
        this.form = form;
    }

    public static int getiT() {
        return iT;
    }

    public static void setiT(int iT) {
        SSNDirSelectionListener.iT = iT;
    }

    public static int getdT() {
        return dT;
    }

    public static void setdT(int dT) {
        SSNDirSelectionListener.dT = dT;
    }
     
     
}
