/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.listener;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import static com.ssn.helper.SSNHelper.getFacebookPhotosDirPath;

import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNFileNode;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNIconData;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNTreeHelper;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.response.SSNAlbum;
import com.ssn.ws.rest.response.SSNAlbumMedia;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author RDiwakar
 */
public class SSNHiveAlbumSelectionListner implements TreeSelectionListener {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNHiveAlbumSelectionListner.class);
    public SSNHomeForm form = null;
    private SSNTreeHelper treeHelper;
    private SSNFileExplorer fileTree;
    static int iT = 0;
    static int dT = 0;
    public SSNGalleryHelper contentPane = null;
    public static Map<String,SSNAlbumMedia> ssnHiveAlbumAllMedia = null;

    public SSNHiveAlbumSelectionListner(SSNHomeForm homeForm, SSNFileExplorer fileTree) {
        this.form = homeForm;
        this.treeHelper = new SSNTreeHelper();
        this.fileTree = fileTree;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        iT = 0;
        dT = 0;
        DefaultMutableTreeNode node = treeHelper.getTreeNode(e.getPath());
        List hiveFiles = new ArrayList();
        if (this.form.getHiveTree() != null) {
            this.form.getHiveTree().clearSelection();
        }

        this.form.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        SSNFileNode fnode = null;
        try {
            if (((SSNIconData) node.getUserObject()).getObject() instanceof SSNFileNode) {
                fnode = (SSNFileNode) ((SSNIconData) node.getUserObject()).getObject();
            }
        } catch (ClassCastException ee) {
            logger.error(ee);
            this.form.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        getAlbumMedia(form.getHomeModel().getLoggedInUserAccessToken(), 1, fileTree.hiveAlbumMap.get(fnode.getFile().getName()));

        /**
         * *******
         *
         */
        if (fnode != null) {

            if (fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia") && !fileTree.m_display.getText().equals("tagUnTaggedMedia")) {
                fileTree.m_display.setText(fnode.getFile().getAbsolutePath());
            }

            if (fnode.getFile().isDirectory()) {
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
            path = SSNHelper.getSsnTempDirPath();
            form.getFileNamesToBeDeleted().clear();
            getSSNMediaFolderProperties(path);

            if (fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia") && !fileTree.m_display.getText().equals("tagUnTaggedMedia")) {
                fileTree.m_display.setText(path);
            }

            getForm().ssnFileExplorer.m_tree.setSelectionRow(1);
            long size = FileUtils.sizeOfDirectory(new File(path));

            if (size > 0) {

                File file1 = new File(path);

                if (fileTree.m_display.getText() != null && !fileTree.m_display.getText().equals("viewAllAlbums") && !fileTree.m_display.getText().equals("instagramMedia") && !fileTree.m_display.getText().equals("tagUnTaggedMedia")) {
                    fileTree.m_display.setText(file1.getAbsolutePath());
                }
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
            } else {
                // show welcome screen
                addWelcomeIcons();
            }
            this.form.getSsnHomeCenterMainPanel().revalidate();
            this.form.getSsnHomeCenterMainPanel().repaint();
            this.form.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        this.form.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void getAlbumMedia(String accessToken, int pageCount, SSNAlbum album) {
        try {
            String urlString = SSNConstants.SSN_WEB_HOST + "api/albums/view/%s/page:%s.json";
            URL url = new URL(String.format(urlString, album.getId(), pageCount));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String input = "access_token=%s";
            input = String.format(input, URLEncoder.encode(accessToken, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(input);
            writer.close();
            os.close();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                StringBuilder response = new StringBuilder();

                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> outputJSON = mapper.readValue(response.toString(), Map.class);

                boolean success = (Boolean) outputJSON.get("success");
                if (success) {
                    List<Map<String, Object>> mediaFileListJSON = (List<Map<String, Object>>) outputJSON.get("MediaFile");
                    ssnHiveAlbumAllMedia = new HashMap<String, SSNAlbumMedia>();
                    if(mediaFileListJSON.size()>0){
                    for (Map<String, Object> mediaFileJSON : mediaFileListJSON) {
                        SSNAlbumMedia ssnAlbumMedia = mapper.readValue(mapper.writeValueAsString(mediaFileJSON), SSNAlbumMedia.class);
                        String name = "";
                        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
                        
                        final List<String> videoSupportedList = Arrays.asList(videoSupported);
                        String fileType = ssnAlbumMedia.getFile_type().substring(ssnAlbumMedia.getFile_type().lastIndexOf("/") + 1, ssnAlbumMedia.getFile_type().length()).toUpperCase();
                        
                        if (videoSupportedList.contains(fileType)) {
                            name = ssnAlbumMedia.getFile_name();
                        } else {
                            name = ssnAlbumMedia.getThumbnail().substring(ssnAlbumMedia.getThumbnail().lastIndexOf("/") + 1, ssnAlbumMedia.getThumbnail().length());
                        }
                        
                        File mediaFile = new File(SSNHelper.getSsnTempDirPath() + album.getName() + File.separator + name);
                        ssnHiveAlbumAllMedia.put(name, ssnAlbumMedia);
                        
                        int lastModificationComparision = -1;
                        if (mediaFile.exists()) {
                            String mediaFileModifiedDate = SSNDao.getSSNMetaData(mediaFile.getAbsolutePath()).getModiFied();
                            String ssnMediaModifiedDate = ssnAlbumMedia.getModified();
                            lastModificationComparision = compareDates(mediaFileModifiedDate, ssnMediaModifiedDate);
                        }

                        if ((!mediaFile.exists() && !ssnAlbumMedia.isIs_deleted()) || (lastModificationComparision < 0)) {
                            if (ssnAlbumMedia.getFile_url() != null && !ssnAlbumMedia.getFile_url().isEmpty()
                                    && !ssnAlbumMedia.getFile_url().equalsIgnoreCase("false")) {
                                try {
                                    URL imageUrl = null;
                                    if (videoSupportedList.contains(fileType)) {
                                        imageUrl = new URL(ssnAlbumMedia.getFile_url().replaceAll(" ", "%20"));
                                    } else {
                                        imageUrl = new URL(ssnAlbumMedia.getThumbnail().replaceAll(" ", "%20"));
                                    }
                                    FileUtils.copyURLToFile(imageUrl, mediaFile);
                                } catch (IOException e) {
                                    logger.error(e);
                                }
                            }
                        }
                    }
                }

                    Map<String, Object> pagingJSON = (Map<String, Object>) outputJSON.get("paging");
                    if (pagingJSON != null) {
                        boolean hasNextPage = Boolean.parseBoolean(pagingJSON.get("nextPage") + "");
                        if (hasNextPage) {
                            getAlbumMedia(accessToken, ++pageCount, album);
                        }
                    }
                }
            }
            
        } catch (EOFException e) {
            logger.error(e);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No Response from server.");
        }catch(Exception ee){
            logger.error(ee);
        }
    }

    private int compareDates(String dateString1, String dateString2) throws ParseException {
        if ((dateString1 == null || dateString1.isEmpty()) && (dateString2 == null || dateString2.isEmpty())) {
            return 0;
        }

        if (dateString1 == null || dateString1.isEmpty()) {
            return -1;
        }

        if (dateString2 == null || dateString2.isEmpty()) {
            return 1;
        }

        SimpleDateFormat metadataDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");
        SimpleDateFormat ssnDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date1 = metadataDateFormat.parse(dateString1);
        Date date2 = ssnDateFormat.parse(dateString2);

        int output = (int) (date1.getTime() - date2.getTime());
        return output;
    }

    public SSNGalleryMetaData getSSNMediaFolderProperties(String mediaPath) {

        if (null == mediaPath || mediaPath.equals("viewAllAlbums") || mediaPath.equals("instagramMedia") || mediaPath.equals("tagUnTaggedMedia") || mediaPath.contains(getFacebookPhotosDirPath())) {
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
                if (fileEntry.length() > 0) {
                    iT++;
                }
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
        SSNHelper.toggleDeleteImage(true, ssnHomeForm);
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

    private void addWelcomeIcons() {
        try {
            URL imgURL = getClass().getResource("/images/dashboard-welcome.png");
            BufferedImage image = ImageIO.read(imgURL);

            this.form.getSsnHomeCenterMainPanel().removeAll();
            this.form.getSsnHomeCenterPanel().removeAll();
    
            this.form.getSsnHomeCenterMainPanel().add(this.form.getSsnHomeCenterPanel(), BorderLayout.NORTH);
            this.form.getSsnHomeCenterMainPanel().add(this.getForm().getWelcomePanelVerticalScroller(this.getForm().getWelcomePanel()), BorderLayout.CENTER);
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
