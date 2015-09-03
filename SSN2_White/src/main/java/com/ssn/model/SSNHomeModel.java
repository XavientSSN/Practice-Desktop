package com.ssn.model;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.event.controller.SSNImageViewerController;
import com.ssn.helper.SSNAddressConverterHelper;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNHiveAlbumSelectionListner;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import com.ssn.speech.AudioRecorder;
import com.ssn.speech.SSNStt;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNFileNode;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNIconData;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNImageSlider;
import com.ssn.ui.custom.component.SSNImageThumbnailControl;
import com.ssn.ui.custom.component.SSNInputDialogBox;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNRotatedImagePanel;
import com.ssn.ui.custom.component.SSNSlideShowPanel;
import com.ssn.ui.custom.component.SSNToolBar;
import com.ssn.ui.custom.component.SSNVideoMetadata;
import com.ssn.ui.form.SSNChangePasswordForm;
import com.ssn.ui.form.SSNEditUserProfileForm;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ui.form.SSNPreferencesForm;
import com.ssn.ui.form.SSNVoiceCommandPreferenceForm;
import com.ssn.util.ThumbnailFileChooser;
import com.ssn.webcam.SSNWebcamPanel;
import com.ssn.ws.rest.request.SSNFaceRecognitionJobType;
import com.ssn.ws.rest.request.SSNFaceRecognitionRequest;
import com.ssn.ws.rest.request.SSNLogoutRequest;
import com.ssn.ws.rest.response.SSNAlbum;
import com.ssn.ws.rest.response.SSNAlbumMedia;
import com.ssn.ws.rest.response.SSNFaceRecognitionResponse;
import com.ssn.ws.rest.response.SSNLogoutResponse;
import com.ssn.ws.rest.service.SSNFaceRecognitionService;
import com.ssn.ws.rest.service.SSNLogoutService;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.comparator.SizeFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.imgscalr.Scalr;

/**
 *
 * @author vkvarma
 */
public class SSNHomeModel {

    private SSNHomeForm homeForm = null;
    private SSNHomeController homeController = null;
    private SSNEditUserProfileForm userProfileForm = null;
    private SSNChangePasswordForm changePasswordForm = null;
    private SSNLogoutService ssnLogoutWS = null;
    private SSNPreferencesForm ssnPreferencesForm = null;
    private SSNVoiceCommandPreferenceForm ssnVoiceCommandPreferenceForm = null;
    private SSNWebcamPanel ssnWebcamPanel = null;
    private Thread microphoneRunner = null;
    private volatile boolean microphonePanelFlag = false;

    private List<File> untaggedMedia;

    private JLabel voiceSearchLabel = null;
    Logger logger = Logger.getLogger(SSNHomeModel.class);
    public static List<Integer> removeItemList = new ArrayList<Integer>();
    ;
    static int iT = 0;
    static int dT = 0;
    static boolean isDelete = false;
    public List<File> imageListForOperation = null;
    public static String text = null;
    List<File> imageListFiles;
    JLabel jbtn1 = null;
    public static int nextStartIndex = 0;
    SSNSlideShowPanel ssnSlideShowPanel1 = null;
    int endIndex = 0;
    Map<String,SSNAlbum> ssnAlbumMap = null;

    /**
     *
     */
    public SSNHomeModel() {
        // removeItemList = new ArrayList<Integer>();
    }

    /**
     *
     * @param homeForm
     * @param homeController
     */
    public SSNHomeModel(SSNHomeForm homeForm, SSNHomeController homeController) {

        setHomeForm(homeForm);
        setHomeController(homeController);
    }

    /**
     * Creates a new album with the name provided
     *
     * @param albumLabel
     */
    public void createHiveAlbum(JLabel albumLabel) {

        SSNInputDialogBox dialogBox1 = new SSNInputDialogBox();
        dialogBox1.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Create Album", "", "* Special Characters (except '_' ) are not allowed");
        String inputAlbumName = dialogBox1.getTextValue();
        // Remove all special characters except _ and - 

        // Check if Album name already exists !
        if (inputAlbumName != null) {
            final String albumName = inputAlbumName.replaceAll("[^ a-zA-Z0-9_-]", "");

            String hiveDir = SSNHelper.getSsnHiveDirPath();
            if (!hiveDir.equals("")) {
                final File folder = new File(hiveDir);
                String directories[] = folder.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        return new File(current, name).isDirectory();
                    }
                });
                for (int i = 0; i < directories.length; i++) {
                    if (albumName.equalsIgnoreCase(directories[i])) {
                        // Album exists with same name.

                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Warning", "", "An Album with same name exists."
                                + "\nPlease try again with a different album name."
                                + "<br><B>Note : Special characters are not allowed in album name.");
                    }
                }

            }
            if (albumName != null) {
                final File theDir = new File(SSNHelper.getSsnHiveDirPath() + albumName + File.separator);
                if (!theDir.exists()) {
                    boolean result = theDir.mkdir();
                    if (result) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                renderLeftPanel();
                            }
                        });
                    }
                }
            } else {
            }
        }
    }

    public boolean createAlbumMain() {
        boolean result = false;
        String albumName = this.getHomeForm().getSsnCreateAlbumName().getText();

        if (albumName != null && !albumName.equalsIgnoreCase("")) {

            albumName = albumName.replaceAll("[^ a-zA-Z0-9_-]", "");

            String hiveDir = SSNHelper.getSsnHiveDirPath();
            if (!hiveDir.equals("")) {
                final File folder = new File(hiveDir);
                String directories[] = folder.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        return new File(current, name).isDirectory();
                    }
                });
                for (int i = 0; i < directories.length; i++) {
                    if (albumName.equals(directories[i])) {
                        // Album exists with same name.

                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Warning", "", "An Album with same name exists."
                                + "Please try again with a different album name."
                                + "<br>Note : Special characters are not allowed in album name.");
                    }
                }

            }

            if (albumName != null) {
                final File theDir = new File(SSNHelper.getSsnHiveDirPath() + albumName + File.separator);
                if (!theDir.exists()) {
                    result = theDir.mkdir();
                    if (result) {

                        this.getHomeForm().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        this.getHomeForm().setLblProcessingImage(new JLabel(new ImageIcon(getClass().getResource("/icon/main_screen_loader.gif")), 0));
                        getHomeForm().ssnFileExplorer.m_display.setText(albumName);
                        uploadImage();
                        this.getHomeForm().setLblProcessingImage(new JLabel(new ImageIcon(getClass().getResource("/icon/buffer.png")), 0));
                        this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                renderLeftPanel();
                            }
                        });
                    }
                }
            } else {
            }
        } else {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Warning", "", "Space and Spacial Charectors are not allowed.");
        }
        return result;
    }

    /**
     * Opens web camera panel if the camera is attached
     *
     * @param cameraLabel
     * @param captureImage
     * @return
     */
    public Thread openCamera(JLabel cameraLabel, boolean captureImage) {
        final JLabel jBtn1 = (JLabel) cameraLabel;
        Thread camRunner = new Thread() {

            public void run() {
                try {

                    setSsnWebcamPanel(new SSNWebcamPanel(getHomeForm(), jBtn1));
                    if (getSsnWebcamPanel() != null && getSsnWebcamPanel().isShowing()) {
                        SwingUtilities.invokeAndWait(getSsnWebcamPanel());
                        getSsnWebcamPanel().toFront();
                    } else {
                        SwingUtilities.invokeAndWait(getSsnWebcamPanel());
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        };
        camRunner.start();
        homeForm.setSsnMicrophoneCamThread(camRunner);

        if (captureImage) {
            try {
                Thread.sleep(10000L);
                getSsnWebcamPanel().getFunctionPanel().captureImage();
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
        return camRunner;

    }

    /**
     * Opens slide show of the images in the selected album
     *
     * @param slideshowLabel
     */
    Thread slideShowRunner;

    public void openSlideshow(final JLabel slideshowLabel) {
        final JLabel jBtn1 = (JLabel) slideshowLabel;
        final JLabel homeLabel = SSNToolBar.desktopHomeLabel;
        slideShowRunner = new Thread() {
            @Override
            public void run() {
                try {
                    //jBtn1.addMouseListener(new MyAdjustmentListener());
                    nextStartIndex = 0;
                    endIndex = 0;
                    SSNSlideShowPanel.isSlideShowVisible = true;
                    ssnSlideShowPanel1 = null;
                    imageListFiles = new CopyOnWriteArrayList<File>();
                    String mediaPath = getHomeForm().ssnFileExplorer.m_display.getText();
                    if (SSNHelper.lastAlbum != null) {
                        if (SSNHelper.lastAlbum.equals("All Media") || SSNHelper.lastAlbum.equals("OurHive Media")) {
                            mediaPath = SSNHelper.getSsnHiveDirPath();
                        } else if (SSNHelper.lastAlbum.equals("Instagram Media")) {
                            mediaPath = SSNHelper.getInstagramPhotosDirPath();
                        } else if (SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                            mediaPath = SSNHelper.lastAlbum;
                        }
                    }
                    // System.out.println("openSlideshow mediaPath"+mediaPath);
                    jbtn1 = jBtn1;
                    if (mediaPath != null && !mediaPath.isEmpty()) {

                        if (mediaPath.equalsIgnoreCase(SSNHelper.getSsnHiveDirPath()) && getUntaggedMedia() != null) {
                            imageListFiles = getUntaggedMedia();
                        } else {
                            imageListFiles = SSNHelper.listFiles(new File(mediaPath), imageListFiles);
                        }
                        String[] slideShowSupportedFormats = SSNConstants.SSN_SLIDESHOW_IMAGE_FILE_FORMAT_SUPPORTED;
                        List<String> slideShowSupportedFormatList = Arrays.asList(slideShowSupportedFormats);
                        int fileIndex = 0;
                        for (File file : imageListFiles) {
                            int index = file.getName().lastIndexOf(".");
                            String fileExtention = file.getName().toLowerCase().substring(index + 1, file.getName().length());
                            if (slideShowSupportedFormatList.contains(fileExtention.toUpperCase())) {
                                try {

                                    if (fileIndex < imageListFiles.size()) {
                                        imageListFiles.remove(fileIndex);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    logger.error(ex);
                                }
                            }
                            fileIndex++;
                        }
                        Collections.reverse(imageListFiles);
                        playSlideShow(nextStartIndex);
                    } else {
                        
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Album selected or No valid media found.");
                        
                    }
                    homeController.setIconImage(slideshowLabel,"/icon/slideshow-normal.png","startSlideShow",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                    homeController.setIconImage(homeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }
        };
        slideShowRunner.start();
    }

    public void playSlideShow(int startIndex) {

        ArrayList<ImageIcon> imageList = new ArrayList<ImageIcon>();

        String[] slideShowSupportedFormats = SSNConstants.SSN_SLIDESHOW_IMAGE_FILE_FORMAT_SUPPORTED;
        List<String> slideShowSupportedFormatList = Arrays.asList(slideShowSupportedFormats);

        List<File> subImageListFiles = null;
        if (endIndex == imageListFiles.size()) {
            nextStartIndex = 0;
            startIndex = 0;

        }
        if (imageListFiles.size() > 5 && endIndex < imageListFiles.size()) {

            int rem = (imageListFiles.size() - endIndex) % 5;

            if (rem < 5 && (imageListFiles.size() - endIndex) != rem) {
                endIndex = nextStartIndex + 5;

            } else {
                endIndex = imageListFiles.size();

            }

        } else {
            endIndex = imageListFiles.size();
        }

        subImageListFiles = imageListFiles.subList(startIndex, endIndex);

        if (imageListFiles.size() > startIndex + 5) {
            nextStartIndex = startIndex + 5;
        }

        for (final File fileEntry : subImageListFiles) {
            int index = fileEntry.getName().lastIndexOf(".");
            String fileExtention = fileEntry.getName().toLowerCase().substring(index + 1, fileEntry.getName().length());
            if (slideShowSupportedFormatList.contains(fileExtention.toUpperCase())) {
                try {
                    // System.out.println("fileEntry.getAbsolutePath()::"+fileEntry.getAbsolutePath());
                    imageList.add(new ImageIcon(Toolkit.getDefaultToolkit().createImage(fileEntry.getAbsolutePath())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex);
                }
            }
        }

        if (imageList.size() > 0) {
            if (ssnSlideShowPanel1 == null) {
                ssnSlideShowPanel1 = new SSNSlideShowPanel(getHomeForm(), jbtn1, imageList);
                // System.out.println("subImageList:::" + imageList.size() + "null");
            } else {
                // System.out.println("subImageList:::" + imageList.size() + "!null");
                ssnSlideShowPanel1.updatePanel(imageList);
            }

            try {
                if (ssnSlideShowPanel1 != null) {
                    //System.out.println("ifffffffffffffffffffff"+imageList.size()+subImageListFiles.size());
                    SwingUtilities.invokeAndWait(ssnSlideShowPanel1);
                    ssnSlideShowPanel1.toFront();
                }
//                } else {
//                     System.out.println("eseeeeeeeeeeeeeeeeeeeee"+imageList.size()+subImageListFiles.size());
//                    ///SwingUtilities.invokeAndWait(ssnSlideShowPanel1);
//                }
            } catch (InterruptedException ex) {
                logger.error(ex);
            } catch (InvocationTargetException ex) {
                logger.error(ex);
            }

        } else {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Album selected or No valid media found.");
        }
    }

    public void startSlideShow() {
        if (!SSNSlideShowPanel.isSlideShowVisible) {
            if (slideShowRunner != null) {
                //System.out.println("---slideShowRunner != null----");
                slideShowRunner.stop();
            }
            ssnSlideShowPanel1 = null;
            return;
        }
        playSlideShow(nextStartIndex);

    }

    //public void openImageSlider(JLabel slideshowLabel)
    File[] listFiles = null;
    SSNImageSlider ssnSlideShowPanel = null;

    public void openImageSlider(JLabel slideshowLabel, final File[] listOfFiles, final int index) {
        final JLabel jBtn1 = (JLabel) slideshowLabel;
        Thread slideShowRunner;
        imageListForOperation = new ArrayList<File>();
        slideShowRunner = new Thread() {
            SSNImageSlider ssnSlideShowPanel = null;
            String[] slideShowSupportedFormats = SSNConstants.SSN_SLIDESHOW_IMAGE_FILE_FORMAT_SUPPORTED;
            List<String> slideShowSupportedFormatList = Arrays.asList(slideShowSupportedFormats);

            String indexedImageName = listOfFiles[index].getName();

            public void run() {
                try {

                    int selectedImageIndex = index;
                    String mediaPath = getHomeForm().ssnFileExplorer.m_display.getText();
                    ArrayList<ImageIcon> imageList = new ArrayList<ImageIcon>();
                    //System.out.println("index" + index);
                    if (listOfFiles != null) {
                        listFiles = listOfFiles;
                    } else if (!mediaPath.equals("")) {
                        if (mediaPath.equalsIgnoreCase(SSNHelper.getSsnHiveDirPath()) && getUntaggedMedia() != null) {
                            listFiles = getUntaggedMedia().toArray(new File[getUntaggedMedia().size()]);
                        } else {
                            listFiles = new File(mediaPath).listFiles();
                        }
                    }

                    for (final File fileEntry : listFiles) {
                        if (fileEntry.length() > 0) {
                            int extensionStartIndex = fileEntry.getName().lastIndexOf(".");

                            String fileExtention = fileEntry.getName().toLowerCase().substring(extensionStartIndex + 1, fileEntry.getName().length());
                            if (slideShowSupportedFormatList.contains(fileExtention.toUpperCase())) {
                                try {

                                    imageListForOperation.add(new File(fileEntry.getAbsolutePath()));
                                    imageList.add(new ImageIcon(ImageIO.read(new File(fileEntry.getAbsolutePath()))));

                                    if (indexedImageName.equalsIgnoreCase(fileEntry.getName())) {
                                        selectedImageIndex = imageListForOperation.size() - 1;
                                        //break;
                                    }
                                    //System.out.println("selectedImageIndex" +  selectedImageIndex);
                                    // imageList.add(new ImageIcon(Toolkit.getDefaultToolkit().createImage(fileEntry.getAbsolutePath())));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    logger.error(ex);
                                }
                            }
                        }
                    }
                    if (imageList.size() > 0) {
                        ssnSlideShowPanel = new SSNImageSlider(getHomeForm(), jBtn1, imageList, selectedImageIndex, imageListForOperation);
                        SwingUtilities.invokeAndWait(ssnSlideShowPanel);
                    } else {
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "No Images Found", "", "No Images Found");
                    }
                } catch (Exception e) {
                    logger.error(e);
                    e.printStackTrace();
                }
            }
        };
        slideShowRunner.start();
    }

    /**
     * Opens schedule tag panel on center pane
     *
     * @param slideshowLabel
     */
    public void openTagSchedulePanel(JLabel slideshowLabel) {
        final JLabel jBtn1 = (JLabel) slideshowLabel;
        Thread scheduleTagRunner;
        scheduleTagRunner = new Thread() {
            SSNScheduleTagPanelForm scheduleTagPanelFormMerger = null;

            public void run() {
                try {
                    // Code for Overlaying our ScheduleTagEventPanel on top of everything & hiding behind.
                    getHomeForm().setCurrentSelectedFile(null);
                    getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
                    getHomeForm().ssnFileExplorer.m_display.setText("");
                    SSNHelper.lastAlbum = null;

                    SSNScheduleTagPanelForm.setsDate(null);
                    scheduleTagPanelFormMerger = new SSNScheduleTagPanelForm(getHomeForm(), "Preset Tags", null, null);
                    // if (scheduleTagPanelFormMerger != null) {
                    SwingUtilities.invokeAndWait(scheduleTagPanelFormMerger);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                }
            }
        };
        scheduleTagRunner.start();

    }

    public String exportMediaToDevice(Set<String> exportList) {
        JFileChooser chooser = null;
        String ssnDestPath = getHomeForm().ssnFileExplorer.m_display.getText();
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if(exportList.size()>1){
                chooser = new JFileChooser();
            }else{
                chooser = new JFileChooser();
               //chooser.setSelectedFile(new File(exportList.iterator().next()));
            }
            
            UIManager.setLookAndFeel(previousLF);
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
            logger.error(e);
        }
        int userSelection = chooser.showSaveDialog(getHomeForm());
        String path =   "";
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            path = SSNHelper.createZipFileFromMultipleFiles(exportList, chooser.getSelectedFile().getAbsolutePath());
        }else if (userSelection == JFileChooser.CANCEL_OPTION) {
            path = "cancel";
        }
        return path;
    }

    /**
     * Uploads selected images in the selected album
     */

    public void uploadImage() {
        JFileChooser chooser = null;
        String[] supportedFormats = SSNConstants.SSN_UPLOAD_FILE_FORMAT_SUPPORTED;
        List<String> supportedFormatList = Arrays.asList(supportedFormats);

        String ssnDestPath = getHomeForm().ssnFileExplorer.m_display.getText();

        try {
            if (ssnDestPath != null && !(ssnDestPath.trim().isEmpty()) && !ssnDestPath.trim().equalsIgnoreCase(SSNHelper.getSsnHiveDirPath()) && !ssnDestPath.trim().equalsIgnoreCase("viewAllAlbums") && !ssnDestPath.trim().equalsIgnoreCase("tagUntaggedMedia") && !ssnDestPath.trim().equalsIgnoreCase("instagramMedia") && !ssnDestPath.trim().contains(SSNHelper.getFacebookPhotosDirPath())) {

                LookAndFeel previousLF = UIManager.getLookAndFeel();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    chooser = new ThumbnailFileChooser();
                    chooser.setMultiSelectionEnabled(true);
                    UIManager.setLookAndFeel(previousLF);
                } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
                }
                //Add whatever other settings you want to the method
                if (chooser != null) {
                    chooser.showOpenDialog(getHomeForm());
                    chooser.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                        }
                    });

                    File f[] = chooser.getSelectedFiles();
                    if (f != null) {
                        for (File file : f) {
                            int index = file.getName().lastIndexOf(".");
                            String fileExtention = file.getName().toLowerCase().substring(index + 1, file.getName().length());
                            if (supportedFormatList.contains(fileExtention.toUpperCase())) {
                                
                                try {
                                
                                    File newFile = new File(ssnDestPath + File.separator + file.getName());
                                    FileUtils.copyFile(file, newFile);
                                    SSNGalleryMetaData data = null;
                                    if (SSMMediaGalleryPanel.checkVideo(newFile)) {
                                        String title = SSNVideoMetadata.readMetaDataForTitle(newFile);
                                        data = SSNVideoMetadata.readVideoMetadata(newFile);
                                        data.setTitle(title);
                                    } else {
                                        String title = SSMMediaGalleryPanel.readMetaDataForTitle(newFile);
                                        data = SSMMediaGalleryPanel.readMetaData(newFile);
                                        data.setTitle(title);
                                    }
                                    if (data != null) {
                                        String address = SSNAddressConverterHelper.getAddress(data.getLatitude(), data.getLongitude());
                                        SSNDao.insertMediaTable(newFile.getAbsolutePath(), data.getUserComments(), data.getSsnRatings(), address, data.getMediaType(), data.getSsnKeywords(), data.getPhotoGrapher(), data.getTitle());
                                    }

                                    SSNGalleryHelper contentPane = new SSNGalleryHelper(ssnDestPath, this.getHomeForm(), "ALL");
                                    contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                                    this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                                    this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                                    this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));

                                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                                    this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
                                    this.getHomeForm().revalidate();
                                } catch (Exception e) {
                                    logger.error(e);
                                }
                            } else {
                                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "File type not supported!");
                                chooser.setVisible(false);
                            }
                        }
                    }
                }
            } else {
                //JOptionPane.showMessageDialog(this.getHomeForm().getSsnHomeCenterPanel(), "Please select an album");
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Information", "", "Please select an album");
            }
            homeForm.getHomeController().setIconImage(SSNHomeController.currentLabel,"/icon/import_media.png","uploadPhoto",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
            homeForm.getHomeController().setIconImage(SSNToolBar.desktopHomeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    /**
     * Shares the selected media via default mail client
     */
    public void shareMedia() {
        try {
            String subject = "SSN Subject";
            String body = "";
            String m = "&subject=%s&body=%s";

            String outLookExeDir = "C:\\Program Files\\Microsoft Office\\Office14\\Outlook.exe";
            String mailCompose = "/c";
            String note = "ipm.note";
            String mailBodyContent = "/m";

            m = String.format(m, subject, body);
            String slashA = "/a";

            Set<String> sharedFileList = getHomeForm().getFileNamesToBeDeleted();

            if (sharedFileList.size() == 1) {
                String mailClientConfigParams[] = null;
                Process startMailProcess = null;
                for (String fileFullPath : sharedFileList) {

                    mailClientConfigParams = new String[]{outLookExeDir, mailCompose, note, mailBodyContent, m, slashA, fileFullPath};
                    startMailProcess = Runtime.getRuntime().exec(mailClientConfigParams);
                }
                OutputStream out = startMailProcess.getOutputStream();
            } else if (sharedFileList.size() > 1) {
                //JOptionPane.showMessageDialog(getHomeForm(), "!!! Please select only one photo at a time !!!", "Photo Share", JOptionPane.ERROR_MESSAGE);
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Photo Share", "", "!!! Please select only one photo at a time !!!");
            }

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    /**
     * Create and show the edit profile form to edit the user information
     */
    public void displayEditProfileForm() {
        if (changePasswordForm != null && changePasswordForm.isShowing()) {
            changePasswordForm.dispose();
        }
        if (getHomeForm().isShowing() && getHomeForm().isAlwaysOnTop()) {
            getHomeForm().setAlwaysOnTop(false);
        }
        if (userProfileForm == null || (!userProfileForm.isShowing())) {
            userProfileForm = new SSNEditUserProfileForm(getHomeForm(), getHomeForm().getLoginResponse());
            userProfileForm.getSsnEditUserProfileFNameTxt().requestFocus(true);
        } else {
            userProfileForm.toFront();
        }
    }

    /**
     * Create and show the change password form
     */
    public void displayChangePasswordForm() {
        if (userProfileForm != null && userProfileForm.isShowing()) {
            userProfileForm.dispose();
        }
        if (getHomeForm().isShowing() && getHomeForm().isAlwaysOnTop()) {
            getHomeForm().setAlwaysOnTop(false);
        }
        if (changePasswordForm == null || (!changePasswordForm.isShowing())) {
            changePasswordForm = new SSNChangePasswordForm(getHomeForm(), getHomeForm().getLoginResponse());
            changePasswordForm.getSsnOldPasswordTxt().requestFocus(true);
        } else {
            changePasswordForm.toFront();
        }
    }

    public void addMultipleFiles() {
        JFileChooser chooser = null;
        String[] supportedFormats = SSNConstants.SSN_UPLOAD_FILE_FORMAT_SUPPORTED;
        List<String> supportedFormatList = Arrays.asList(supportedFormats);
        try {

            LookAndFeel previousLF = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                chooser = new ThumbnailFileChooser();
                chooser.setMultiSelectionEnabled(true);
                UIManager.setLookAndFeel(previousLF);
            } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
            }
            //Add whatever other settings you want to the method
            if (chooser != null) {
                chooser.showOpenDialog(getHomeForm());
                chooser.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });

                File f[] = chooser.getSelectedFiles();
                SSNImageThumbnailControl ssnImageThumbnailControl = null;
                if (f != null) {
                    this.getHomeForm().setMultipleSelectedFiles(f);
                    int fileIndex = 0;
                    for (File file : this.getHomeForm().getMultipleSelectedFiles()) {
                        ssnImageThumbnailControl = new SSNImageThumbnailControl();
                        this.getHomeForm().getThumbnailImagePanel().add(ssnImageThumbnailControl.getSsnImageThumbnailControl(file.getAbsolutePath(), fileIndex++));
                        this.getHomeForm().getThumbnailImagePanel().revalidate();
                    }
                }
            }

        } catch (Exception ex) {
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public void renderLeftPanel() {
        getHomeForm().getSsnHomeLeftPanel().removeAll();
        getHomeForm().getSsnHomeLeftMainPanel().remove(getHomeForm().getSsnHomeLeftPanel());
        getHomeForm().ssnFileExplorer = new SSNFileExplorer(getHomeForm());
        getHomeForm().getSsnHomeLeftPanel().add(getHomeForm().ssnFileExplorer);
        getHomeForm().getSsnHomeLeftMainPanel().revalidate();
        if(SSNFileExplorer.selectedMedia.equals("hive"))
            getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(1);
        getHomeForm().revalidate();
    }

    public void uploadMultipleFile() {

        String[] supportedFormats = SSNConstants.SSN_UPLOAD_FILE_FORMAT_SUPPORTED;
        List<String> supportedFormatList = Arrays.asList(supportedFormats);
        boolean isCreateAlbum = this.getHomeForm().getCreateAlbumChkBx().isSelected();
        boolean isAlbumAlreadyExist = false;
        String alertMessage = "Please select some files and then click upload !";
        String ssnDestPath = getHomeForm().ssnFileExplorer.m_display.getText();
        if (ssnDestPath == null || ssnDestPath.equals("")) {

            ssnDestPath = SSNHelper.getSsnDefaultDirPath();
        }

        if (isCreateAlbum) {
            String createAlbum = SSNHelper.getSsnHiveDirPath();
            String albumName = this.getHomeForm().getSsnCreateAlbumName().getText();
            if (albumName != null && !albumName.equals("")) {
                String createAlbumPathString = createAlbum + albumName;
                File checkAlbumExistence = new File(createAlbumPathString);
                if (checkAlbumExistence.exists() && checkAlbumExistence.isDirectory()) {
                    isAlbumAlreadyExist = true;
                    alertMessage = "Album with this name already exist !";
                } else {

                    if (this.getHomeForm().getMultipleSelectedFiles() != null && this.getHomeForm().getMultipleSelectedFiles().length >= 1) {
                        if (checkAlbumExistence.mkdirs()) {
                            ssnDestPath = createAlbumPathString;
                            this.getHomeForm().ssnFileExplorer.m_display.setText(ssnDestPath);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    renderLeftPanel();
                                }
                            });
                        }

                    }
                }
            } else {
                isAlbumAlreadyExist = true;
                this.getHomeForm().setMultipleSelectedFiles(this.getHomeForm().getMultipleSelectedFiles());
                alertMessage = "Please enter album name or uncheck the checkbox!";
            }
        }

        int countIndex = 0;
        if (this.getHomeForm().getMultipleSelectedFiles() != null && this.getHomeForm().getMultipleSelectedFiles().length >= 1) {
            if (!isAlbumAlreadyExist) {
                for (File file : this.getHomeForm().getMultipleSelectedFiles()) {

                    if (!removeItemList.contains(countIndex)) {

                        int index = file.getName().lastIndexOf(".");
                        String fileExtention = file.getName().toLowerCase().substring(index + 1, file.getName().length());
                        if (supportedFormatList.contains(fileExtention.toUpperCase())) {
                            // BufferedImage image = ImageIO.read(file);
                            try {
                                //ImageIO.write(image, "jpg", new File(ssnDestPath + File.separator + file.getName()));
                                File newFile = new File(ssnDestPath + File.separator + file.getName());
                                FileUtils.copyFile(file, newFile);
                                SSNGalleryMetaData data = null;
                                if (SSMMediaGalleryPanel.checkVideo(newFile)) {
                                    String title = SSNVideoMetadata.readMetaDataForTitle(newFile);
                                    data = SSNVideoMetadata.readVideoMetadata(newFile);
                                    data.setTitle(title);
                                } else {
                                    String title = SSMMediaGalleryPanel.readMetaDataForTitle(newFile);
                                    data = SSMMediaGalleryPanel.readMetaData(newFile);
                                    data.setTitle(title);
                                }
                                if (data != null) {
                                    String address = SSNAddressConverterHelper.getAddress(data.getLatitude(), data.getLongitude());
                                    SSNDao.insertMediaTable(newFile.getAbsolutePath(), data.getUserComments(), data.getSsnRatings(), address, data.getMediaType(), data.getSsnKeywords(), data.getPhotoGrapher(), data.getTitle());
                                }

                                SSNGalleryHelper contentPane = new SSNGalleryHelper(ssnDestPath, this.getHomeForm(), "ALL");

                                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

                                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                                this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                                this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));

                                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                                this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
                                this.getHomeForm().revalidate();
                            } catch (Exception e) {
                                logger.error(e);
                                e.printStackTrace();
                                removeItemList.clear();
                                this.getHomeForm().setMultipleSelectedFiles(null);
                            }
                            removeItemList.clear();
                            this.getHomeForm().setMultipleSelectedFiles(null);
                        }
                    }
                    countIndex++;
                }
            } else {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI("Alert", "Alert", "Alert", alertMessage);
                // dialogBox.show();
            }
        } else {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI("Alert", "Alert", "Alert", "Please select some files and then click upload !");
            // dialogBox.show();
        }

    }

    /**
     * This method will process the logoff mechanism to make user logged out
     * from the application
     */
    public void processSSNLogoff() {
        if (getSsnWebcamPanel() != null) {
            getSsnWebcamPanel().dispose();
        }
        if (this.getHomeForm().getLoginResponse() == null) {
            this.getHomeForm().dispose();
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
            File RememberMe = new File(RememberMePath);
            File[] listFiles = RememberMe.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.getName().startsWith("Token_");

                }
            });

            for (File tokenFile : listFiles) {
                tokenFile.delete();
            }

            new SSNLoginForm(false);
        } else {

            // Delete Remember me file.
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
            File file = new File(RememberMePath);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }
            file.delete();
            // end.

            this.setSsnLogoutWS(new SSNLogoutService());
            String HOST_NAME = SSNConstants.SSN_WEB_HOST;
            String SERVICE_NAME = "api/";
            String SERVICE_URI = "users/logout";
            String access_token = this.getLoggedInUserAccessToken();

            Map<String, String> editUserDataMap = new HashMap<String, String>();
            editUserDataMap.put("access_token", access_token);

            SSNLogoutRequest logoutRequest = new SSNLogoutRequest();
            logoutRequest.setRequestParameters(editUserDataMap);

            getSsnLogoutWS().setHostName(HOST_NAME);
            getSsnLogoutWS().setServiceName(SERVICE_NAME);
            getSsnLogoutWS().setRestURI(SERVICE_URI);
            getSsnLogoutWS().setRequest(logoutRequest);
            getSsnLogoutWS().initWSConnection();
            getSsnLogoutWS().prepareRequest(getSsnLogoutWS().getRequest());
            SSNLogoutResponse response = getSsnLogoutWS().getResponse();

            if (response != null && response.isSuccess()) {
                this.getHomeForm().dispose();
                this.getHomeForm().getLoginForm().setVisible(true);
                this.getHomeForm().getLoginForm().getSsnHivename().requestFocus(true);
                this.getHomeForm().getLoginForm().getSsnPassword().setText("");
                //this.getHomeForm().getLoginForm().getSsnHivename().setText("");

            } else if (response != null && (!response.isSuccess())) {
                this.getHomeForm().dispose();
                rootPath = SSNHelper.getSsnWorkSpaceDirPath();
                RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
                File RememberMe = new File(RememberMePath);
                File[] listFiles = RememberMe.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File file) {
                        return file.getName().startsWith("Token_");

                    }
                });

                if (listFiles != null) {
                    for (File tokenFile : listFiles) {
                        tokenFile.delete();
                    }
                }
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", response.getMsg());
                new SSNLoginForm(false);
            } else if (response == null) {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Response from service , Please check internet connectivity or try later.");
            }
        }
    }

    /**
     * Return the user id of logged in user
     *
     * @return
     */
    public long getLoggedInUserId() {
        long uid = 0;

        if (this.getHomeForm().getLoginResponse() != null && this.getHomeForm().getLoginResponse() != null
                && this.getHomeForm().getLoginResponse().getData() != null && this.getHomeForm().getLoginResponse().getData().getUser() != null) {
            uid = this.getHomeForm().getLoginResponse().getData().getUser().getId();
        }
        return uid;
    }

    /**
     * Return first name of the logged in user
     *
     * @return
     */
    public String getLoggedInUserFirstName() {
        String userFirstName = "";

        if (this.getHomeForm().getLoginResponse() != null && this.getHomeForm().getLoginResponse() != null
                && this.getHomeForm().getLoginResponse().getData() != null && this.getHomeForm().getLoginResponse().getData().getUser() != null
                && this.getHomeForm().getLoginResponse().getData().getUser().getFirst_name() != null) {
            userFirstName = this.getHomeForm().getLoginResponse().getData().getUser().getFirst_name();
        }
        return userFirstName;
    }

    /**
     * Return username of the logged in user
     *
     * @return
     */
    public String getLoggedInUserName() {
        String userName = "";

        if (this.getHomeForm().getLoginResponse() != null && this.getHomeForm().getLoginResponse() != null
                && this.getHomeForm().getLoginResponse().getData() != null && this.getHomeForm().getLoginResponse().getData().getUser() != null
                && this.getHomeForm().getLoginResponse().getData().getUser().getUser_name() != null) {
            userName = this.getHomeForm().getLoginResponse().getData().getUser().getUser_name();
        } else if (this.getHomeForm().getSocialModel() != null) {
            userName = this.getHomeForm().getSocialModel().getUsername();
        }
        return userName;
    }

    /**
     * Return the access token of the logged in user
     *
     * @return
     */
    public String getLoggedInUserAccessToken() {
        String accessToken = "";

        if (this.getHomeForm().getLoginResponse() != null && this.getHomeForm().getLoginResponse() != null
                && this.getHomeForm().getLoginResponse().getData() != null && this.getHomeForm().getLoginResponse().getData().getUser() != null
                && this.getHomeForm().getLoginResponse().getData().getUser().getAccess_token() != null) {
            accessToken = this.getHomeForm().getLoginResponse().getData().getUser().getAccess_token();
        }
        return accessToken;
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
     * @return the homeController
     */
    public SSNHomeController getHomeController() {
        return homeController;
    }

    /**
     * @param homeController the homeController to set
     */
    public void setHomeController(SSNHomeController homeController) {
        this.homeController = homeController;
    }

    /**
     * @return the ssnLogoutWS
     */
    public SSNLogoutService getSsnLogoutWS() {
        return ssnLogoutWS;
    }

    /**
     * @param ssnLogoutWS the ssnLogoutWS to set
     */
    public void setSsnLogoutWS(SSNLogoutService ssnLogoutWS) {
        this.ssnLogoutWS = ssnLogoutWS;
    }

    /**
     * This method is used to read and display all the media from all the albums
     * present on the application directory
     */
    public void viewAllAlbums() {
        this.getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
        String rootPath = SSNHelper.getSsnHiveDirPath();
        getHomeForm().ssnFileExplorer.m_display.setText("viewAllAlbums");
        getHomeForm().getFileNamesToBeDeleted().clear();
        getHomeForm().setCurrentSelectedFile(null);
        SSNHelper.toggleDeleteAndShareImages(false, this.getHomeForm());

        try {
            SSNGalleryHelper contentPane = new SSNGalleryHelper(
                    rootPath, this.getHomeForm(), "ALL");
            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
            this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath("All Media")));
            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, "All Media"), BorderLayout.NORTH);
            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
           // String selectedFolderPath = getHomeForm().ssnFileExplorer.m_display.getText();

            this.getHomeForm().getSsnHomeRightPanel().removeAll();
            this.getHomeForm().getSsnHomeRightMainPanel().repaint();

            this.getHomeForm().revalidate();

        } catch (Exception e) {
//           
            e.printStackTrace();
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "!!! Please try again !!!", "", "*** There is some Problem in loading images  ***");
        }
    }

    /**
     * This method will delete the selected media from the album
     */
    public void deletePhotos() {
        try {
            // JOptionPane.showMessageDialog(getHomeForm(),"*** Delete Photo  ***\n Section Under Construction ","!!! Section Not Available !!!", JOptionPane.ERROR_MESSAGE);
            Set<String> filesToBeDeleted = getHomeForm().getFileNamesToBeDeleted();
            if (filesToBeDeleted != null && !filesToBeDeleted.isEmpty()) {
                for (String file : filesToBeDeleted) {
                    File deleteFile = new File(file);
                    deleteFile.delete();
                    SSNDao.deleteMetaData(deleteFile.getAbsolutePath());

                }
                isDelete = true;
                getHomeForm().getFileNamesToBeDeleted().clear();
                SSNHelper.toggleDeleteAndShareImages(false, this.getHomeForm());
                SSNGalleryHelper contentPane = new SSNGalleryHelper(getHomeForm().ssnFileExplorer.m_display.getText(), this.getHomeForm(), "ALL");
                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));

                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
                isDelete = false;
                this.getHomeForm().revalidate();
            } else {
                String currentAlbum = getHomeForm().ssnFileExplorer.m_display.getText();
                File album = new File(currentAlbum);
                if (album.isDirectory()) {
                    File[] files = album.listFiles();
                    for (File file : files) {
                        file.delete();
                        SSNDao.deleteMetaData(file.getAbsolutePath());
                    }
                    if (!album.getName().equalsIgnoreCase("OurHive")) {
                        album.delete();
                    }

                    String rootPath = SSNHelper.getSsnHiveDirPath();
                    renderLeftPanel();
                    getHomeForm().ssnFileExplorer.m_display.setText(rootPath);
                    this.getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);

                }

            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SSNHomeModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is to sort the media on the basis of the property and the
     * sorting order provided
     *
     * @param property
     * @param order
     */
    public void sortImages(String property, String order) {
        boolean isBlankRightPanel = false;
        try {
            String mediaPath = getHomeForm().ssnFileExplorer.m_display.getText();
            if (mediaPath.equals("viewAllAlbums")) {
                mediaPath = SSNHelper.getSsnHiveDirPath();
                isBlankRightPanel = true;
            } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("Instagram Media")) {
                getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                mediaPath = SSNHelper.getInstagramPhotosDirPath();;
            } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                mediaPath = SSNHelper.getSsnHiveDirPath();
                isBlankRightPanel = true;
            } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                getHomeForm().ssnFileExplorer.m_display.setText(SSNHelper.lastAlbum);
                mediaPath = SSNHelper.lastAlbum;
            }

            // System.out.println("mediaPath-sortImages"+mediaPath);
            List<File> fileList = new ArrayList<File>();
            if (mediaPath != null && !mediaPath.trim().isEmpty()) {
                fileList = SSNHelper.listFiles(new File(mediaPath), fileList);
            } else {
                fileList = SSNHelper.listFiles(new File(SSNHelper.getSsnHiveDirPath()), fileList);
            }
            int i = 0;
            File[] fileArray = new File[fileList.size()];
            for (Object obj : fileList.toArray()) {
                fileArray[i] = (File) obj;
                i++;
            }

            switch (property.toLowerCase()) {
                case "name":
                    if (order.equalsIgnoreCase(SSNConstants.SSN_IMAGE_SORT_ORDER_ASC)) {

                        Arrays.sort(fileArray, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
                        break;
                    } else {
                        Arrays.sort(fileArray, NameFileComparator.NAME_INSENSITIVE_REVERSE);
                        break;
                    }
                case "date":
                    if (order.equalsIgnoreCase(SSNConstants.SSN_IMAGE_SORT_ORDER_ASC)) {

                        Arrays.sort(fileArray, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                        break;

                    } else {
                        Arrays.sort(fileArray, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                        break;
                    }
                case "size":
                    if (order.equalsIgnoreCase(SSNConstants.SSN_IMAGE_SORT_ORDER_ASC)) {

                        Arrays.sort(fileArray, SizeFileComparator.SIZE_COMPARATOR);
                        break;

                    } else {
                        Arrays.sort(fileArray, SizeFileComparator.SIZE_REVERSE);
                        break;
                    }
            }
            SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeForm());
            // contentPane.setPreferredSize(new Dimension(450, 10));
            // contentPane.setBackground(new Color(47, 47, 47));

            //contentPane.setPreferredSize(new Dimension(450, 4500));
            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
            //contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

            //this.getHomeForm().getSsnHomeCenterPanel().add(contentPane);
            this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));

            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
            if (!isBlankRightPanel) {
                this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
            }

            this.getHomeForm().revalidate();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * To search the media in different album and show in the gallery
     */
    public void searchMedia() {
        try {
            File[] fileArray = null;
            JToolBar jToolBar = this.getHomeForm().getSsnHomeToolBar();

            text = this.getHomeForm().getSearchMediaTextField().getText();
            if (text != null) {
                List<File> fileList = new ArrayList<File>();
                fileList = SSNHelper.listFiles(new File(SSNHelper.getSsnHiveDirPath()), fileList);
                Iterator<File> iter = fileList.iterator();
                while (iter.hasNext()) {
                    boolean contain = false;
                    File file = iter.next();
                    String fileName = file.getName();
                    SSNGalleryMetaData mdata = SSNDao.getSSNMetaData(file.getAbsolutePath());
                    List<String> tagList = SSNDao.getTaggedFaceByMediaPath(file.getAbsolutePath());

                    if (fileName.toLowerCase().contains(text.toLowerCase())) {
                        contain = true;
//                } else if (mdata.getUserComments() != null && mdata.getUserComments().toLowerCase().contains(text.toLowerCase())) {
//                    contain = true;
                    } else if (mdata.getEditMediaLocation() != null && mdata.getEditMediaLocation().toLowerCase().contains(text.toLowerCase())) {
                        contain = true;
//                } else if (mdata.getSsnRatings() != null && mdata.getSsnRatings().toLowerCase().contains(text.toLowerCase())) {
//                    contain = true;
//                } else if (mdata.getTitle() != null && mdata.getTitle().toLowerCase().contains(text.toLowerCase())) {
//                    contain = true;
                    } else if (mdata.getCaption() != null && mdata.getCaption().toLowerCase().contains(text.toLowerCase())) {
                        contain = true;
//                }else if(mdata.getPhotoGrapher()!=null && mdata.getPhotoGrapher().toLowerCase().contains(text.toLowerCase())){
//                     contain = true;
                    } else if (mdata.getSsnKeywords() != null && !mdata.getSsnKeywords().isEmpty()) {
                        String[] texts = new String[1];
                        if (text.contains(",")) {
                            texts = text.split(",");
                        } else {
                            texts[0] = text;
                        }
                        String keywordString = mdata.getSsnKeywords();
                        if (keywordString.contains(",")) {
                            String[] keywords = keywordString.split(",");
                            for (String key : texts) {
                                for (String keyword : keywords) {
                                    if (keyword.toLowerCase().contains(key.toLowerCase())) {
                                        contain = true;
                                        break;
                                    }
                                }
                            }
                        } else {
                            for (String key : texts) {
                                if (keywordString.toLowerCase().contains(key.toLowerCase())) {
                                    contain = true;
                                    break;
                                }
                            }
                        }

                    } else if (tagList.contains(text.toLowerCase())) {
                        contain = true;
                    }
                    if (!contain) {
                        iter.remove();
                    }

                }
                int i = 0;
                fileArray = new File[fileList.size()];
                for (File file : fileList) {
                    fileArray[i] = file;
                    i++;
                }
                if (fileArray.length > 0) {
                    String rootPath = SSNHelper.getSsnHiveDirPath();
                    getHomeForm().ssnFileExplorer.m_display.setText(rootPath);
                    this.getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                    getHomeForm().getFileNamesToBeDeleted().clear();
                    getHomeForm().setCurrentSelectedFile(null);
                    SSNHelper.toggleDeleteAndShareImages(false, this.getHomeForm());
                    SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeForm());

                    contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                    this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                    this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();

                    this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, "Search Result"));

                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                    this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
                    this.getHomeForm().revalidate();
                } else {
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No media found.");
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

    }

    /**
     * To find the un-tagged media in the application and show in gallery
     */
    public void findTagUntaggedMedia() {
        try {
            File[] fileArray = null;

            List<File> fileList = new CopyOnWriteArrayList<File>();
            fileList = SSNHelper.listFiles(new File(SSNHelper.getSsnHiveDirPath()), fileList);

//            Iterator<File> iter = fileList.iterator();
//            while (iter.hasNext()) {
//                boolean contain = false;
//                File file = iter.next();
//                SSNGalleryMetaData mdata = SSNDao.getSSNMetaData(file.getAbsolutePath());
//
////                if ((mdata.getUserComments() == null || mdata.getUserComments().isEmpty()) && (mdata.getSsnKeywords() == null || mdata.getSsnKeywords().isEmpty())) {
////                    contain = true;
////                }
//                
//                
//                 if ((mdata.getMediaLocation()== null || mdata.getMediaLocation().isEmpty()) && (mdata.getSsnKeywords() == null || mdata.getSsnKeywords().isEmpty())) {
//                if (!contain) {
//                    iter.remove();
//                }
//            }
            int i = 0;
            fileArray = new File[fileList.size()];
            for (File file : fileList) {
                fileArray[i] = file;
                i++;
            }

            untaggedMedia = fileList;
            this.getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
            String rootPath = SSNHelper.getSsnHiveDirPath();
            getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");

            getHomeForm().getFileNamesToBeDeleted().clear();
            getHomeForm().setCurrentSelectedFile(null);
            SSNHelper.toggleDeleteAndShareImages(false, this.getHomeForm());
            SSNGalleryHelper contentPane = new SSNGalleryHelper(fileArray, this.getHomeForm());

            contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();

            this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, "OurHive Media"));
            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
            // this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText(), fileArray);
            this.getHomeForm().getSsnHomeRightPanel().removeAll();
            this.getHomeForm().getSsnHomeRightMainPanel().repaint();
            this.getHomeForm().revalidate();
        } catch (IOException ex) {
            logger.error(ex.getMessage());

        }

    }

    /**
     * To search the media from the cloud and show in gallery
     *
     * @param accessToken
     * @param searchText
     */
    public void searchCloudMedia(String accessToken, String searchText) {
        try {
            String tempDirPath = SSNHelper.getSsnTempDirPath();
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            } else {
                File[] files = tempDir.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }

            List<File> searchedMedia = new ArrayList<File>();
            getSearchedMedia(accessToken, searchText, 1, searchedMedia);
            if (searchedMedia.size() > 0) {
               // String rootPath = SSNHelper.getSsnHiveDirPath();
                //getHomeForm().ssnFileExplorer.m_display.setText(rootPath);
                // this.getHomeForm().ssnFileExplorer.m_tree.setSelectionRow(0);
                this.getHomeForm().ssnFileExplorer.m_tree.setSelectionPath(null);
                getHomeForm().ssnFileExplorer.m_display.setText(SSNHelper.getSsnTempDirPath());
                getHomeForm().getFileNamesToBeDeleted().clear();
                getHomeForm().setCurrentSelectedFile(null);
                SSNHelper.toggleDeleteAndShareImages(false, this.getHomeForm());
                SSNGalleryHelper contentPane = new SSNGalleryHelper(SSNHelper.getSsnTempDirPath(), this.getHomeForm(), "ALL");

                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, "Search Result"));

                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText(), searchedMedia.toArray(new File[searchedMedia.size()]));
                this.getHomeForm().revalidate();
            } else {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "No Media Found.");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void getSearchedMedia(String accessToken, String searchContent, int pageCount, List<File> searchedMedia) {
        try {
            String urlString = SSNConstants.SSN_WEB_HOST + "api/media_files/list/search:%s/page:%s.json";
            // System.out.println("searchmedia : " + urlString);
            URL url = new URL(String.format(urlString, searchContent, pageCount));
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
                    //System.out.println("output : " + output);
                }

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> outputJSON = mapper.readValue(response.toString(), Map.class);

                boolean success = (Boolean) outputJSON.get("success");
                if (success) {
                    List<Map<String, Object>> mediaFileListJSON = (List<Map<String, Object>>) outputJSON.get("MediaFile");
                    if (mediaFileListJSON != null) {
                        for (Map<String, Object> mediaFileJSON : mediaFileListJSON) {
                            SSNAlbumMedia ssnAlbumMedia = mapper.readValue(mapper.writeValueAsString(mediaFileJSON), SSNAlbumMedia.class);
                            try {
                                if (ssnAlbumMedia.getFile_url() != null && !ssnAlbumMedia.getFile_url().equalsIgnoreCase("false")) {
                                    URL imageUrl = new URL(ssnAlbumMedia.getFile_url());
                                    File mediaFile = new File(SSNHelper.getSsnTempDirPath() + File.separator + ssnAlbumMedia.getFile_name());
                                    FileUtils.copyURLToFile(imageUrl, mediaFile);

                                    String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
                                    final List<String> videoSupportedList = Arrays.asList(videoSupported);
                                    String fileExtension = mediaFile.getName().substring(mediaFile.getName().lastIndexOf(".") + 1, mediaFile.getName().length());
                                    if (!videoSupportedList.contains(fileExtension.toUpperCase())) {
                                        if (!fileExtension.equalsIgnoreCase("PNG")) {

                                            SSMMediaGalleryPanel.writeImageMetaData(mediaFile, ssnAlbumMedia.getMeta_title(),
                                                    ssnAlbumMedia.getMeta_keywords(), ssnAlbumMedia.getMeta_comments(),
                                                    ssnAlbumMedia.getMeta_place(), null, null,
                                                    ssnAlbumMedia.getMeta_rating() + "", ssnAlbumMedia.getMeta_author());
                                        }

                                    }

                                    if (mediaFile.length() > 0) {
                                        searchedMedia.add(mediaFile);
                                    } else {
                                        mediaFile.delete();
                                    }
                                }
                            } catch (IOException e) {
                                logger.error(e);
                            }
                        }
                    }

                    Map<String, Object> pagingJSON = (Map<String, Object>) outputJSON.get("paging");
                    if (pagingJSON != null) {
                        boolean hasNextPage = Boolean.parseBoolean(pagingJSON.get("nextPage") + "");
                        if (hasNextPage) {
                            getSearchedMedia(accessToken, searchContent, ++pageCount, searchedMedia);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * To view the next image in the image viewer
     */
    public void nextImageView() {

        try {
            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            int index = 0;
            for (int i = 0; i < this.getHomeForm().getCurrentGallery().length; i++) {
                if (this.getHomeForm().getCurrentGallery()[i].getAbsolutePath().equalsIgnoreCase(this.getHomeForm().getCurrentFile().getAbsolutePath())) {
                    index = i;
                    break;
                }
            }
            if (index != this.getHomeForm().getCurrentGallery().length - 1) {
                BufferedImage image1 = ImageIO.read(this.getHomeForm().getCurrentGallery()[index + 1]);
                this.getHomeForm().setCurrentImage(image1);
                this.getHomeForm().setRotateAngleMultiple(0);
                this.getHomeForm().setZoomIn(1);
                this.getHomeForm().setCurrentRotatedImage(null);
                this.getHomeForm().setCurrentFile(this.getHomeForm().getCurrentGallery()[index + 1]);

                //Image background = image1.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
                Image background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, 1);
                SSNImagePanel panel = new SSNImagePanel(background);
                panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                panel.setName("imageViewer");
                panel.addMouseListener(new SSNImageViewerController(this.getHomeForm().getCurrentFile(), this.getHomeForm()));
                this.getHomeForm().setSsnImagePanel(panel);
                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterButtonPanel());
                //this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
                this.getHomeForm().getSsnHomeCenterPanel().add(panel);
                //this.getHomeForm().getSsnHomeCenterPanel().setBorder(BorderFactory.createMatteBorder(0, 3, 0, 3, SSNConstants.SSN_BORDER_COLOR));

                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(this.getHomeForm().getRatingMap().get(this.getHomeForm().getCurrentFile().getAbsolutePath())), BorderLayout.SOUTH);
                //this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(), BorderLayout.SOUTH);
                this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();
                this.getHomeForm().getSsnHomeRightPanel().removeAll();
                this.getHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(this.getHomeForm().getCurrentFile(), this.getHomeForm()), BorderLayout.NORTH);
                this.getHomeForm().getSsnHomeRightPanel().revalidate();

            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * To view the previous image in the image viewer
     */
    public void previousImageView() {
        try {
            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            int index = 0;
            for (int i = 0; i < this.getHomeForm().getCurrentGallery().length; i++) {
                if (this.getHomeForm().getCurrentGallery()[i].getAbsolutePath().equalsIgnoreCase(this.getHomeForm().getCurrentFile().getAbsolutePath())) {
                    index = i;
                    break;
                }
            }
            if (index != 0) {
                BufferedImage image1 = ImageIO.read(this.getHomeForm().getCurrentGallery()[index - 1]);
                this.getHomeForm().setCurrentImage(image1);
                this.getHomeForm().setRotateAngleMultiple(0);
                this.getHomeForm().setZoomIn(1);
                this.getHomeForm().setCurrentRotatedImage(null);
                this.getHomeForm().setCurrentFile(this.getHomeForm().getCurrentGallery()[index - 1]);
                //Image background = image1.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
                Image background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, 1);
                SSNImagePanel panel = new SSNImagePanel(background);
                panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                panel.setName("imageViewer");
                panel.addMouseListener(new SSNImageViewerController(this.getHomeForm().getCurrentFile(), this.getHomeForm()));
                this.getHomeForm().setSsnImagePanel(panel);
                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
                this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterButtonPanel());

                this.getHomeForm().getSsnHomeCenterPanel().add(panel);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(this.getHomeForm().getRatingMap().get(this.getHomeForm().getCurrentFile().getAbsolutePath())), BorderLayout.SOUTH);

                this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();
                this.getHomeForm().getSsnHomeRightPanel().removeAll();
                this.getHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(this.getHomeForm().getCurrentFile(), this.getHomeForm()), BorderLayout.NORTH);
                this.getHomeForm().getSsnHomeRightPanel().revalidate();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * To rotate the image left 90 degrees
     */
    public void rotateImageLeft() {
        BufferedImage image1 = this.getHomeForm().getCurrentImage();
        int multiplier = this.getHomeForm().getRotateAngleMultiple() + 1;
        this.getHomeForm().setRotateAngleMultiple(multiplier);
        this.getHomeForm().setZoomIn(1);
        Image background = null;
        if (multiplier % 2 == 0) {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, 1);
        } else {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), true, 1);
        }
        SSNRotatedImagePanel panel = new SSNRotatedImagePanel(background, multiplier, this.getHomeForm());
        BufferedImage rotatedImage = this.rotate(image1, multiplier);
        this.getHomeForm().setCurrentRotatedImage(rotatedImage);
        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
        this.getHomeForm().getSsnHomeCenterPanel().add(panel);

        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
        this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();
        this.getHomeForm().revalidate();

    }

    /**
     * To rotate image right 90 degrees
     */
    public void rotateImageRight() {
        BufferedImage image1 = this.getHomeForm().getCurrentImage();
        int multiplier = this.getHomeForm().getRotateAngleMultiple() + 1;
        this.getHomeForm().setRotateAngleMultiple(multiplier);
        this.getHomeForm().setZoomIn(1);
        Image background = null;

        if (multiplier % 2 == 0) {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, 1);
        } else {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), true, 1);
        }

        SSNRotatedImagePanel panel = new SSNRotatedImagePanel(background, multiplier, this.getHomeForm());
        BufferedImage rotatedImage = this.rotate(image1, multiplier);
        this.getHomeForm().setCurrentRotatedImage(rotatedImage);
        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
        this.getHomeForm().getSsnHomeCenterPanel().add(panel);

        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
        this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();
        this.getHomeForm().revalidate();

    }

    /**
     * To zoom in the image in image viewer
     */
    public void zoomIn() {
        BufferedImage image1 = this.getHomeForm().getCurrentImage();
        double zoomIn = this.getHomeForm().getZoomIn() + .2;
        this.getHomeForm().setZoomIn(zoomIn);

        int multiplier = this.getHomeForm().getRotateAngleMultiple();

        Image background = null;
        if (multiplier % 2 == 0) {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, zoomIn);
        } else {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), true, zoomIn);
        }
        SSNRotatedImagePanel panel = new SSNRotatedImagePanel(background, multiplier, this.getHomeForm());

        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
        this.getHomeForm().getSsnHomeCenterPanel().add(panel);
        //this.getHomeForm().getSsnHomeCenterPanel().setBorder(BorderFactory.createMatteBorder(0, 3, 0, 3, SSNConstants.SSN_BORDER_COLOR));

        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
        // this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(), BorderLayout.SOUTH);
        this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();

    }

    /**
     * To zoom out the image in image viewer
     */
    public void zoomOut() {
        BufferedImage image1 = this.getHomeForm().getCurrentImage();
        double zoomIn = this.getHomeForm().getZoomIn() - .2;
        DecimalFormat df = new DecimalFormat("#.##");
        zoomIn = Double.parseDouble(df.format(zoomIn));
        if (zoomIn != 0) {
            this.getHomeForm().setZoomIn(zoomIn);
        } else {

            zoomIn = zoomIn + .2;
        }
        int multiplier = this.getHomeForm().getRotateAngleMultiple();
        Image background = null;
        if (multiplier % 2 == 0) {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, zoomIn);
        } else {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), true, zoomIn);
        }
        SSNRotatedImagePanel panel = new SSNRotatedImagePanel(background, multiplier, this.getHomeForm());
        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
        this.getHomeForm().getSsnHomeCenterPanel().add(panel);
        //this.getHomeForm().getSsnHomeCenterPanel().setBorder(BorderFactory.createMatteBorder(0, 3, 0, 3, SSNConstants.SSN_BORDER_COLOR));

        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
        //  this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(), BorderLayout.SOUTH);
        this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();

    }

    /**
     * To zoom out the image in image Slider
     */
    public void zoomOut(int index) {
        BufferedImage image1 = this.getHomeForm().getCurrentImage();
        double zoomIn = this.getHomeForm().getZoomIn() - .2;
        DecimalFormat df = new DecimalFormat("#.##");
        zoomIn = Double.parseDouble(df.format(zoomIn));
        if (zoomIn != 0) {
            this.getHomeForm().setZoomIn(zoomIn);
        } else {

            zoomIn = zoomIn + .2;
        }
        int multiplier = this.getHomeForm().getRotateAngleMultiple();
        Image background = null;
        if (multiplier % 2 == 0) {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), false, zoomIn);
        } else {
            background = SSNHelper.getScaledImage(image1, this.getHomeForm(), true, zoomIn);
        }
        SSNRotatedImagePanel panel = new SSNRotatedImagePanel(background, multiplier, this.getHomeForm());
        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        SSNImageSlider ssnSlideShowPanel = null;
//        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
//        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());
//        this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterSortPanel());
//        this.getHomeForm().getSsnHomeCenterPanel().add(panel);
//        //this.getHomeForm().getSsnHomeCenterPanel().setBorder(BorderFactory.createMatteBorder(0, 3, 0, 3, SSNConstants.SSN_BORDER_COLOR));
//
//        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
//        //  this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getCenterButtonPanel(), BorderLayout.SOUTH);
//        this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();

    }

    /**
     * To save the rotated image in the album if user wants to save whether as a
     * new or same name
     */
    public void saveRotatedImage() {
        BufferedImage image = this.getHomeForm().getCurrentRotatedImage();
        int multiplier = this.getHomeForm().getRotateAngleMultiple();
        if (image != null && multiplier % 4 != 0) {
            //int result = JOptionPane.showConfirmDialog(null, "Do you want to replace the original file ?", "Replace or Create New ", JOptionPane.YES_NO_OPTION);
            SSNConfirmationDialogBox diaglogBox = new SSNConfirmationDialogBox();
            diaglogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Replace or Create New", "", "Do you want to replace the original file ?");
            int result = diaglogBox.getResult();
            if (result != JOptionPane.CLOSED_OPTION) {
                try {
                    File currentFile = this.getHomeForm().getCurrentFile();
                    int index = currentFile.getAbsolutePath().lastIndexOf(File.separator);
                    String currentPath = currentFile.getAbsolutePath().substring(0, index);
                    String newFileName = "";
                    if (result == JOptionPane.YES_OPTION) {
                        newFileName = currentFile.getName();
                    } else if (result == JOptionPane.NO_OPTION) {
                        newFileName = "rotated_" + currentFile.getName();
                    }
                    ImageIO.write(image, "jpg", new File(currentPath + File.separator + newFileName));
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            }

        } else {
            //JOptionPane.showMessageDialog(this.getHomeForm(), "Only rotated images can be saved");
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Information", "", "Only rotated images can be saved");
        }
    }

    /**
     * To rotate the image on the basis of the angle provided to rotate
     *
     * @param image
     * @param multiplier
     * @return
     */
    public BufferedImage rotate(BufferedImage image, int multiplier) {
        int rotate = multiplier % 4;
        if (rotate < 0) {
            rotate = 4 - (-rotate);
        }
        switch (rotate) {
            case 1:
                image = Scalr.rotate(image, Scalr.Rotation.CW_90);
                break;
            case 2:
                image = Scalr.rotate(image, Scalr.Rotation.CW_180);
                break;
            case 3:
                image = Scalr.rotate(image, Scalr.Rotation.CW_270);
                break;
        }
        return image;

    }

    /**
     *
     * @return
     */
    public SSNWebcamPanel getSsnWebcamPanel() {
        return ssnWebcamPanel;
    }

    /**
     *
     * @param ssnWebcamPanel
     */
    public void setSsnWebcamPanel(SSNWebcamPanel ssnWebcamPanel) {
        this.ssnWebcamPanel = ssnWebcamPanel;
    }

    /**
     *
     * @return
     */
    public Thread getMicrophoneRunner() {
        return microphoneRunner;
    }

    /**
     *
     * @param microphoneRunner
     */
    public void setMicrophoneRunner(Thread microphoneRunner) {
        this.microphoneRunner = microphoneRunner;
    }

    /**
     *
     * @return
     */
    public boolean isMicrophonePanelFlag() {
        return microphonePanelFlag;
    }

    /**
     *
     * @param microphonePanelFlag
     */
    public void setMicrophonePanelFlag(boolean microphonePanelFlag) {
        this.microphonePanelFlag = microphonePanelFlag;
    }

    /**
     *
     * @return
     */
    public JLabel getVoiceSearchLabel() {
        return voiceSearchLabel;
    }

    /**
     *
     * @param voiceSearchLabel
     */
    public void setVoiceSearchLabel(JLabel voiceSearchLabel) {
        this.voiceSearchLabel = voiceSearchLabel;
    }

    /**
     * To display preferences form to save or edit the preferences
     */
    public void displayPreferencesForm() {
        if (userProfileForm != null && userProfileForm.isShowing()) {
            userProfileForm.dispose();
        }
        if (getHomeForm().isShowing() && getHomeForm().isAlwaysOnTop()) {
            getHomeForm().setAlwaysOnTop(false);
        }
        if (ssnPreferencesForm == null || (!ssnPreferencesForm.isShowing())) {
            ssnPreferencesForm = new SSNPreferencesForm(getHomeForm());
            //ssnPreferencesForm.getSsnOldPasswordTxt().requestFocus(true);
        } else {
            ssnPreferencesForm.toFront();
        }
    }

    /**
     * To show the filtered media in the gallery on the basis of the type of the
     * media that user want to filter
     *
     * @param mediaToShow
     */
    public void showFilteredMedia(String mediaToShow) {
        if (StringUtils.isNotBlank(mediaToShow)) {
            try {
                for (JCheckBox chkBox : homeForm.getAllBoxes()) {
                    chkBox.setSelected(false);
                }

                String mediaPath = getHomeForm().ssnFileExplorer.m_display.getText();
                boolean isBlankRightPanel = false;

                if (mediaPath.equals("viewAllAlbums")) {
                    mediaPath = SSNHelper.getSsnHiveDirPath();
                    isBlankRightPanel = true;
                } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("Instagram Media")) {
                    getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
                    mediaPath = SSNHelper.getInstagramPhotosDirPath();;
                } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                    getHomeForm().ssnFileExplorer.m_display.setText(SSNHelper.lastAlbum);
                    mediaPath = SSNHelper.lastAlbum;
                } else if (SSNHelper.lastAlbum != null && SSNHelper.lastAlbum.equals("OurHive Media")) {
                    getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                    mediaPath = SSNHelper.getSsnHiveDirPath();;
                    isBlankRightPanel = true;
                }

                //System.out.println("mediaPath::"+mediaPath+"  text::"+this.getHomeForm().ssnFileExplorer.m_display.getText());
                SSNGalleryHelper contentPane = new SSNGalleryHelper(this.getHomeForm().ssnFileExplorer.m_display.getText(), this.getHomeForm(), mediaToShow);
                // contentPane.setPreferredSize(new Dimension(450,600)); 
                contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                this.getHomeForm().setCurrentSelectedFile(null);
                this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                //ssnHomeForm.remove(contentPane);
                this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                // System.out.println("showFilteredMedia"+this.getHomeForm().ssnFileExplorer.m_display.getText());

                this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                // this.getHomeForm().add(this.getHomeForm().getSsnHomeCenterMainPanel());
                if (!isBlankRightPanel) {
                    this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
                }

                this.getHomeForm().add(this.getHomeForm().getSsnHomeLeftMainPanel(), BorderLayout.WEST);
//                this.getHomeForm().getSplitPane().setLeftComponent(this.getHomeForm().getSsnHomeLeftMainPanel());
//                this.getHomeForm().getSplitPane().setRightComponent(this.getHomeForm().getSsnHomeCenterMainPanel());
//                //ssnHomeForm.getSplitPane().setDividerLocation(200);
//                this.getHomeForm().getSplitPane().revalidate();
//                this.getHomeForm().getSplitPane().repaint();

                this.getHomeForm().revalidate();
                this.getHomeForm().repaint();
                //this.getHomeForm().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private SSNGalleryMetaData getSSNMediaFolderProperties(String mediaPath) {
        //System.out.println("mediaPath-getSSNMediaFolderProperties::::::::::::::"+mediaPath);
        if (mediaPath.equals("viewAllAlbums")) {
            mediaPath = SSNHelper.getSsnHiveDirPath();
        } else if (mediaPath.equals("instagramMedia")) {
            mediaPath = SSNHelper.getSsnWorkSpaceDirPath() + "Instagram Media";
        } else if (mediaPath.equals("tagUnTaggedMedia")) {
            mediaPath = SSNHelper.getSsnHiveDirPath();
        }

        if (this.getHomeForm().getCurrentSelectedFile() == null || isDelete) {
            iT = 0;
            dT = 0;
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

            this.getHomeForm().getSsnHomeRightPanel().removeAll();
            this.getHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaFolderPanel(data), BorderLayout.NORTH);
            this.getHomeForm().getSsnHomeRightPanel().revalidate();

            return data;
        }
        return null;
    }

    public SSNGalleryMetaData getSSNMediaFolderProperties(String mediaPath, File[] fileList) {
        if (this.getHomeForm().getCurrentSelectedFile() == null) {

            if (mediaPath.equals("viewAllAlbums")) {
                mediaPath = SSNHelper.getSsnHiveDirPath();
            } else if (mediaPath.equals("instagramMedia")) {
                mediaPath = SSNHelper.getSsnWorkSpaceDirPath() + "Instagram Media";
            } else if (mediaPath.equals("tagUnTaggedMedia")) {
                mediaPath = SSNHelper.getSsnHiveDirPath();
            }

            iT = 0;
            dT = 0;
            SSNGalleryMetaData data = new SSNGalleryMetaData();
            File folder = new File(mediaPath);
            data.setMediaLocation(mediaPath);
            data.setTitle(folder.getName());
            data.setNoOfFiles(fileList.length);
            data.setNoOfFolders(0);

            //File mdf=new File(fnode.getFile().getPath());
            Date dNow = new Date(folder.lastModified());
            SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
            data.setModiFied(ft.format(dNow));
            long size = 0;
            for (File f : fileList) {
                size += FileUtils.sizeOf(f);
            }
            double flKB = (double) size / 1024;
            double flMB = flKB / 1024;
            String s = String.format("%.2f", flMB);
            data.setSize(s);

            this.getHomeForm().getSsnHomeRightPanel().removeAll();
            this.getHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaFolderPanel(data), BorderLayout.NORTH);
            this.getHomeForm().getSsnHomeRightPanel().revalidate();

            return data;
        }
        return null;
    }

    private static void listFiles(File folder) {

        if (folder != null && folder.listFiles() != null) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    iT++;
                } else if (fileEntry.isDirectory()) {
                    dT++;
                    listFiles(fileEntry);

                }
            }
        }

    }

    /**
     * To synchronize with cloud data of the user this will do both uploading
     * and downloading on the cloud to and fro
     *
     * @param accessToken
     * @param lLabel
     */
    public void synchroniseWithCloud(final String accessToken, final JLabel lLabel) {
        try {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    Set<String> fileList = getHomeForm().getFileNamesToBeDeleted();
                    lLabel.setOpaque(false);
                    lLabel.removeAll();
                    if (!fileList.isEmpty()) {
                       uploadImages(fileList, accessToken);
                       getHomeForm().getFileNamesToBeDeleted().clear();
                    }

                    lLabel.setBackground(new Color(0, 0, 0, 1));
                    List<SSNAlbum> albums = new ArrayList<SSNAlbum>();
                   

                    JTree hiveTree = getHomeForm().getHiveTree();
                    DefaultTreeModel treeModel = (DefaultTreeModel) hiveTree.getModel();
                    DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) treeModel.getRoot();

                    for (SSNAlbum album : albums) {
                        File albumDir = new File(SSNHelper.getSsnHiveDirPath() + album.getName());
                        if (!albumDir.exists()) {
                            DefaultMutableTreeNode albumNode = new DefaultMutableTreeNode(new SSNIconData(new ImageIcon(getClass().getResource("/images/ssn-hive-folder.png")), null, new SSNFileNode(albumDir)));
                            topNode.add(albumNode);
                            albumDir.mkdir();

                        }
                        getAlbumMedia(accessToken, 1, album);
                    }
                    treeModel.reload(topNode);
        
                }
            };
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAlbums(String accessToken, int pageCount, List<SSNAlbum> albums) {
        try {

            String urlString = SSNConstants.SSN_WEB_HOST + "api/albums/list/page:%s.json";
            URL url = new URL(String.format(urlString, pageCount));
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

            int status = 0;
            status = conn.getResponseCode();

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
                    List<Map<String, String>> albumJSON = (List<Map<String, String>>) outputJSON.get("Album");

                    if (albumJSON != null) {
                        for (Map<String, String> album : albumJSON) {
                            SSNAlbum ssnAlbum = mapper.readValue(mapper.writeValueAsString(album), SSNAlbum.class);
                            albums.add(ssnAlbum);
                        }
                    }

                    Map<String, Object> pagingJSON = (Map<String, Object>) outputJSON.get("paging");
                    if (pagingJSON != null) {
                        boolean hasNextPage = Boolean.parseBoolean(pagingJSON.get("nextPage") + "");
                        if (hasNextPage) {
                            getAlbums(accessToken, ++pageCount, albums);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
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
                System.out.println("response " + response);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> outputJSON = mapper.readValue(response.toString(), Map.class);

                boolean success = (Boolean) outputJSON.get("success");
                if (success) {
                    List<Map<String, Object>> mediaFileListJSON = (List<Map<String, Object>>) outputJSON.get("MediaFile");
                    for (Map<String, Object> mediaFileJSON : mediaFileListJSON) {
                        SSNAlbumMedia ssnAlbumMedia = mapper.readValue(mapper.writeValueAsString(mediaFileJSON), SSNAlbumMedia.class);

                        String name = (ssnAlbumMedia.getOriginal_file_name() != null && !ssnAlbumMedia.getOriginal_file_name().isEmpty() && !ssnAlbumMedia.getOriginal_file_name().equalsIgnoreCase("null")) ? ssnAlbumMedia.getOriginal_file_name() : ssnAlbumMedia.getFile_name();
                        File mediaFile = new File(SSNHelper.getSsnHiveDirPath() + album.getName() + File.separator + name);
                        writeHiveMediaMetaData(mediaFile,ssnAlbumMedia,album.getName());
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
        } catch (Exception e) {
            logger.error(e);
        }
    }

    
    public void downloadHiveMediaToLibrary(){
        logger.info("Inside methop downloadHiveMediaToLibrary");
        List<Boolean> createdfileList = new ArrayList<Boolean>();
        Iterator<String> files = this.getHomeForm().getFileNamesToBeDeleted().iterator();
        while (files.hasNext()) {
            try {
                String fileName = files.next();
                if(SSNHelper.getDeviceType().equals("Desktop-Win"))
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                else
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                SSNAlbumMedia ssnAlbumMedia = SSNHiveAlbumSelectionListner.ssnHiveAlbumAllMedia.get(fileName);
                File album = new File(SSNHelper.getSsnHiveDirPath() + File.separator + ssnAlbumMedia.getAlbums());
                album.mkdir();
                File fileToDownload = new File(SSNHelper.getSsnHiveDirPath() + File.separator + ssnAlbumMedia.getAlbums() + File.separator + ssnAlbumMedia.getOriginal_file_name());
                URL imageUrl = new URL(ssnAlbumMedia.getFile_url().replaceAll(" ", "%20"));
                FileUtils.copyURLToFile(imageUrl, fileToDownload);
                createdfileList.add(writeHiveMediaMetaData(fileToDownload, ssnAlbumMedia, ssnAlbumMedia.getAlbums()));
                logger.info(createdfileList.size() +"== "+this.getHomeForm().getFileNamesToBeDeleted().size());
            } catch (MalformedURLException ex) {
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        if (createdfileList.size() == this.getHomeForm().getFileNamesToBeDeleted().size()) {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Media downloaded successfully.");
        } else {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "Some error occured during download. Please retry!.");
        }
        logger.info("Exit methop downloadHiveMediaToLibrary ");
    }
    public boolean writeHiveMediaMetaData(File mediaFile,SSNAlbumMedia ssnAlbumMedia,String albumName){
        int lastModificationComparision = -1;
     
        if (mediaFile.exists()) {
            try {
                String mediaFileModifiedDate = SSNDao.getSSNMetaData(mediaFile.getAbsolutePath()).getModiFied();
                String ssnMediaModifiedDate = ssnAlbumMedia.getModified();
                
                try {
                    lastModificationComparision = compareDates(mediaFileModifiedDate, ssnMediaModifiedDate);
                } catch (ParseException ex) {
                     logger.error(ex);
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }

        if ((!mediaFile.exists() && !ssnAlbumMedia.isIs_deleted()) || (lastModificationComparision < 0)) {
            if (ssnAlbumMedia.getFile_url() != null && !ssnAlbumMedia.getFile_url().isEmpty()
                    && !ssnAlbumMedia.getFile_url().equalsIgnoreCase("false")) {
                try {
                    URL imageUrl = new URL(ssnAlbumMedia.getFile_url().replaceAll(" ", "%20"));
                    FileUtils.copyURLToFile(imageUrl, mediaFile);

                    String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
                    final List<String> videoSupportedList = Arrays.asList(videoSupported);
                    String fileExtension = mediaFile.getName().substring(mediaFile.getName().lastIndexOf(".") + 1, mediaFile.getName().length());
                    if (!videoSupportedList.contains(fileExtension.toUpperCase())) {
                        SSMMediaGalleryPanel.writeImageMetaData(mediaFile, ssnAlbumMedia.getMeta_title(),
                                ssnAlbumMedia.getMeta_keywords(), ssnAlbumMedia.getMeta_comments(),
                                ssnAlbumMedia.getMeta_place(), null, null,
                                ssnAlbumMedia.getMeta_rating() + "", ssnAlbumMedia.getMeta_author());
                    }

                    if (mediaFile.length() > 0) {
                        try {
                            if (SSNDao.checkMediaExist(mediaFile.getAbsolutePath())) {
                                SSNDao.updateMediaTable(mediaFile.getAbsolutePath(), ssnAlbumMedia.getMeta_comments() != null ? ssnAlbumMedia.getMeta_comments() : "",
                                        ssnAlbumMedia.getMeta_rating() + "", ssnAlbumMedia.getMeta_place() != null ? ssnAlbumMedia.getMeta_place() : "",
                                        ssnAlbumMedia.getFile_type() != null ? ssnAlbumMedia.getFile_type() : "", ssnAlbumMedia.getMeta_keywords() != null ? ssnAlbumMedia.getMeta_keywords() : "",
                                        ssnAlbumMedia.getMeta_author() != null ? ssnAlbumMedia.getMeta_author() : "", ssnAlbumMedia.getMeta_title() != null ? ssnAlbumMedia.getMeta_title() : "");
                            } else {
                                
                                SSNDao.insertMediaTable(mediaFile.getAbsolutePath(), ssnAlbumMedia.getMeta_comments() != null ? ssnAlbumMedia.getMeta_comments() : "",
                                        ssnAlbumMedia.getMeta_rating() + "", ssnAlbumMedia.getMeta_place() != null ? ssnAlbumMedia.getMeta_place() : "",
                                        ssnAlbumMedia.getFile_type() != null ? ssnAlbumMedia.getFile_type() : "", ssnAlbumMedia.getMeta_keywords() != null ? ssnAlbumMedia.getMeta_keywords() : "",
                                        ssnAlbumMedia.getMeta_author() != null ? ssnAlbumMedia.getMeta_author() : "", ssnAlbumMedia.getMeta_title() != null ? ssnAlbumMedia.getMeta_title() : "");
                            }
                        } catch (SQLException ex) {
                           logger.error(ex);
                        }

                        long id = 0;
                        try {
                            id = SSNDao.findSyncronisedMediaId(ssnAlbumMedia.getOriginal_file_name(), albumName);
                        } catch (SQLException ex) {
                            logger.error(ex);
                        }
                        if (id <= 0) {
                            SSNDao.saveCloudMedia(ssnAlbumMedia.getId(),
                                    ssnAlbumMedia.getOriginal_file_name(), albumName);
                        }
                       
                    } else {
                        mediaFile.delete();
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        return mediaFile.exists();
    }
    private void uploadImages(Set<String> fileSet, String accessToken) {
        boolean success = false;
        try {
            
            for (String file : fileSet) {

                String pattern = Pattern.quote(System.getProperty("file.separator"));
                String[] albumsubstr = file.split(pattern);
                String album = albumsubstr[albumsubstr.length - 2];
                String fileName = albumsubstr[albumsubstr.length - 1];
                album = (album == null || album.isEmpty()) ? "OurHive" : album;

                long id = SSNDao.findSyncronisedMediaId(fileName, album);
                long updatedId = id;

                
                String urlString = SSNConstants.SSN_WEB_HOST + "api/cloud_containers/uploadfile";
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(urlString);

                File fileToUpload = new File(file);
                if (fileToUpload.length() > 0) {
                    
                    SSNGalleryMetaData metadata = SSMMediaGalleryPanel.readMetaData(fileToUpload);
                    SSNGalleryMetaData metadataDb = SSNDao.getSSNMetaData(fileToUpload.getAbsolutePath());
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    
                    builder.addTextBody("access_token", accessToken, ContentType.TEXT_PLAIN);
                    builder.addTextBody("album_name", album, ContentType.TEXT_PLAIN);
                    String fileExtention = fileToUpload.getName().substring(fileToUpload.getName().lastIndexOf(".") + 1, fileToUpload.getName().length());

                    if (Arrays.asList(SSNConstants.SSN_UPLOAD_FILE_FORMAT_SUPPORTED).contains(fileExtention.toUpperCase())) {
                    
                        builder.addBinaryBody("filename", fileToUpload, ContentType.create(SSNHelper.getContentType(fileExtention.toUpperCase())), fileToUpload.getName());
                    } else {
                        builder.addBinaryBody("filename", fileToUpload, ContentType.create(fileExtention.toUpperCase()), fileToUpload.getName());
                    
                    }
                    
                    
                    if (id > 0) {
                        builder.addTextBody("id", id + "", ContentType.TEXT_PLAIN);
                      
                    }

                    if (metadata != null && metadata.getTitle() != null) {
                        builder.addTextBody("meta_title", metadata.getTitle(), ContentType.TEXT_PLAIN);
                      
                    }

                    if (metadata != null && metadata.getMediaLocation() != null) {
                        builder.addTextBody("meta_place", metadataDb.getEditMediaLocation(), ContentType.TEXT_PLAIN);
                      
                    }

                    if (metadata != null && metadata.getUserComments() != null) {
                        builder.addTextBody("meta_comments", metadata.getUserComments(), ContentType.TEXT_PLAIN);
                    }

                    if (metadata != null && metadata.getSsnRatings() != null && !metadata.getSsnRatings().isEmpty()) {
                        builder.addTextBody("meta_rating", metadata.getSsnRatings(), ContentType.TEXT_PLAIN);
                    }

                    if (metadata != null && metadata.getSsnKeywords() != null) {
                        builder.addTextBody("meta_keywords", metadata.getSsnKeywords(), ContentType.TEXT_PLAIN);
                        
                    }
                   

                    String voiceNotePath = SSNDao.getVoiceCommandPath(fileToUpload.getAbsolutePath());
                    if (voiceNotePath != null && !voiceNotePath.isEmpty()) {
                        File voiceNote = new File(SSNHelper.getSsnVoiceNoteDirPath() + voiceNotePath);
                        builder.addBinaryBody("voice_note", voiceNote, ContentType.create("mp4"), voiceNote.getName());
                    }

                    HttpEntity multipart = builder.build();
                    httppost.setEntity(multipart);
                    Header[] header = httppost.getAllHeaders();

                    org.apache.http.HttpResponse response = httpclient.execute(httppost);

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuilder result = new StringBuilder();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println("\n "+ fileToUpload.getName()+" : " + result);
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> outputJSON = mapper.readValue(result.toString(), Map.class);

                    success = (Boolean) outputJSON.get("success");
                    if (success) {
                        Map<String, Object> dataJSON = (Map<String, Object>) outputJSON.get("data");
                        if (dataJSON != null) {
                            Map<String, Object> mediaFileJSON = (Map<String, Object>) dataJSON.get("MediaFile");
                            if (mediaFileJSON != null) {
                                updatedId = Long.parseLong((String) mediaFileJSON.get("id"));
                            }
                        }

                        if (id != updatedId) {
                            SSNDao.saveCloudMedia(updatedId, fileName, album);
                        }

                    }
                }
            }

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        } finally {

            if (success) {

                SSNMessageDialogBox messageDialogBox = new SSNMessageDialogBox();
                messageDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Media upload with cloud complete.");

            } else {

                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "Media upload could not be done. <br>Please try later.");

            }
            homeController.refreshCenterPanel();
            homeController.setIconImage(SSNHomeController.currentLabel,"/icon/upload-normal.png","uploadMedia",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
            homeController.setIconImage(SSNToolBar.desktopHomeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);

        }
    }

    /**
     * To tag a particular media in the image viewer and also to display all the
     * tags already that image is having
     */
    public void tagImage() {
        File currentFile = this.getHomeForm().getCurrentFile();
        SSNFaceRecognitionRequest request = new SSNFaceRecognitionRequest(currentFile.getAbsolutePath());
        request.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_ADD.toString());
        request.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
        final SSNFaceRecognitionService service = new SSNFaceRecognitionService();
        SSNFaceRecognitionResponse response = service.getResponse(request);
        request.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_RECOGNIZE.toString());
        request.getParameters().remove(SSNFaceRecognitionRequest.TAG);
        SSNFaceRecognitionResponse response2 = service.getResponse(request);

        List<SSNFaceRecognitionResponse.FaceDetection> listForImgIndex = response.getFaceDetection();
        Map<Float, String> imgIndexMap = new HashMap<>();

        List<SSNFaceRecognitionResponse.FaceDetection> list = response2.getFaceDetection();

        SSNImagePanel panel = (SSNImagePanel) this.getHomeForm().getSsnImagePanel();
        int panelX = panel.getxPosition();
        int panelY = panel.getyPosition();
        List<JPanel> tagPanelList = new ArrayList<JPanel>();
        boolean exist = SSNDao.checkFaceExist(currentFile.getAbsolutePath());
        if (listForImgIndex != null && !listForImgIndex.isEmpty()) {
            for (SSNFaceRecognitionResponse.FaceDetection face : listForImgIndex) {
                float x = face.getBoundingbox().getTl().getX();
                String imgIndex = face.getImgIndex();
                imgIndexMap.put(x, imgIndex);
            }
        }
        if (list != null && !list.isEmpty()) {
            JLayeredPane lpane = new JLayeredPane();
            for (SSNFaceRecognitionResponse.FaceDetection face : list) {
                float x = face.getBoundingbox().getTl().getX();
                float y = face.getBoundingbox().getTl().getY();
                //System.out.println("x,y " + x+" , "+y);
                float width = face.getBoundingbox().getSize().getWidth();
                float height = face.getBoundingbox().getSize().getHeight();
                final String imgIndex = imgIndexMap.get(x);
                TaggedFace taggedFace = new TaggedFace();
                taggedFace.setxCoordinate(x);
                taggedFace.setyCoordinate(y);
                taggedFace.setWidth(width);
                taggedFace.setHeight(height);
                taggedFace.setImageIndex(imgIndex);
                float[] coordinates = SSNHelper.getScaledCoordinatesofTaggedFace(x, y, width, height, this.getHomeForm().getCurrentImage(), this.getHomeForm());
                if (coordinates != null) {
                    x = coordinates[0];
                    y = coordinates[1];
                    width = coordinates[2];
                    height = coordinates[3];
                }
                JPanel tag = new JPanel();
                tag.setBackground(new Color(0, 0, 0, 1));
                //tag.setBounds(panelX + (int) x, panelY + (int) y, (int) width, (int) height);
                tag.setBounds(panelX + (int) x, panelY + (int) y, (int) width, (int) height);
                tag.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                tagPanelList.add(tag);
                List<SSNFaceRecognitionResponse.FaceDetection.Matches> matches = face.getMatches();
                String matchString = "";
                final Vector<String> comboList = new Vector<String>();
                for (SSNFaceRecognitionResponse.FaceDetection.Matches match : matches) {
                    String matched = match.getTag();
                    float score = match.getScore();
                    if (score > .75) {
                        comboList.add(matched);
                        matchString += matched + ",";
                    }
                }
                if (matchString.contains(",")) {
                    matchString = matchString.substring(0, matchString.length() - 1);
                }
                taggedFace.setTags(matchString);
                if (!exist) {
                    try {
                        SSNDao.saveTaggedFaces(taggedFace, currentFile.getAbsolutePath());
                    } catch (SQLException ex) {
                        java.util.logging.Logger.getLogger(SSNHomeModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                final JComboBox field = new JComboBox(comboList);
                field.setEditable(true);
                field.setVisible(false);
                int fieldWidth = (int) ((int) width * 1.5);
                int fieldX = panelX + (int) x - (int) ((int) width * 0.25);
                field.setBounds(fieldX, panelY + (int) y + (int) height, fieldWidth, 25);
                field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                lpane.add(field, new Integer(1), 0);
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
                            if (selectedValue != "" && !selectedValue.equalsIgnoreCase(SSNFaceRecognitionRequest.TAG_VALUE)) {
                                SSNFaceRecognitionRequest faceRenameRequest = new SSNFaceRecognitionRequest();
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.NEW_TAG, selectedValue);
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX, String.valueOf(imgIndex));
                                faceRenameRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_RENAME.toString());
                                SSNFaceRecognitionResponse faceRenameResponse = service.getResponse(faceRenameRequest);

                                SSNFaceRecognitionRequest faceTrainRequest = new SSNFaceRecognitionRequest();
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.TAGS, selectedValue);
                                faceTrainRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_TRAIN.toString());
                                SSNFaceRecognitionResponse faceTrainResponse = service.getResponse(faceTrainRequest);

                                SSNFaceRecognitionRequest faceDeleteRequest = new SSNFaceRecognitionRequest();
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.TAG, SSNFaceRecognitionRequest.TAG_VALUE);
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.JOB, SSNFaceRecognitionJobType.FACE_DELETE.toString());
                                faceDeleteRequest.getParameters().put(SSNFaceRecognitionRequest.IMG_INDEX, String.valueOf(imgIndex));
                                SSNFaceRecognitionResponse faceDeleteResponse = service.getResponse(faceDeleteRequest);

                                boolean added = comboList.add(selectedValue);
                                if (added) {
                                    try {
                                        SSNGalleryMetaData mdata = SSNDao.getSSNMetaData(getHomeForm().getCurrentFile().getAbsolutePath());
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
//                                        boolean check = SSNDao.checkMediaExist(getHomeForm().getCurrentFile().getAbsolutePath());
//                                        if (check) {
//                                            SSNDao.updateMediaTableWithKeyWord(getHomeForm().getCurrentFile().getAbsolutePath(), newKeywords);
//                                        } else {
//                                            SSNDao.insertMediaTable(getHomeForm().getCurrentFile().getAbsolutePath(), "", "", "", "", newKeywords, "","");
//                                        }
                                        String tags = SSNDao.getTaggedFace(getHomeForm().getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex));
                                        String newTag = "";
                                        if (tags == null || tags.isEmpty()) {
                                            newTag = selectedValue;
                                            SSNDao.updateTaggedFaces(getHomeForm().getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex), newTag);
                                        } else {
                                            newTag = selectedValue;
                                            SSNDao.updateTaggedFaces(getHomeForm().getCurrentFile().getAbsolutePath(), String.valueOf(imgIndex), newTag);

                                        }
                                        getHomeForm().getSsnHomeRightPanel().removeAll();
                                        getHomeForm().getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(getHomeForm().getCurrentFile(), getHomeForm()), BorderLayout.NORTH);
                                        getHomeForm().getSsnHomeRightPanel().revalidate();
                                    } catch (SQLException ex) {
                                        java.util.logging.Logger.getLogger(SSNHomeModel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            }
                        }
                    }

                });
            }
            this.getHomeForm().getSsnHomeCenterPanel().removeAll();
            this.getHomeForm().getSsnHomeCenterMainPanel().remove(this.getHomeForm().getSsnHomeCenterPanel());

            lpane.add(panel, new Integer(0), 0);
            if (tagPanelList.size() > 0) {
                for (JPanel tag : tagPanelList) {
                    lpane.add(tag, new Integer(1), 0);
                }
            }

            this.getHomeForm().getSsnHomeCenterPanel().add(lpane);
            this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
            this.getHomeForm().getSsnHomeCenterMainPanel().revalidate();
        }

    }

    TargetDataLine searchVoiceMediaTdl = null;
    Boolean searchVoiceMediaFlag = null;

    /**
     *
     * @return
     */
    public Boolean isSearchVoiceMediaFlag() {
        return searchVoiceMediaFlag;
    }

    /**
     *
     * @param searchVoiceMediaFlag
     */
    public void setSearchVoiceMediaFlag(Boolean searchVoiceMediaFlag) {
        this.searchVoiceMediaFlag = searchVoiceMediaFlag;
    }

    /**
     * To search the media by providing the name as voice
     */
    public void searchVoiceMedia(JLabel jBtn) {

        JLabel jLabel = jBtn;//(JLabel) comp;
        if (isSearchVoiceMediaFlag() == null || !isSearchVoiceMediaFlag()) {
            if (getSearchVoiceMediaTdl() == null) {
                // capture voice and create wav file
                try {
                    setSearchVoiceMediaTdl(new AudioRecorder().captureAudio(false));
                    setSearchVoiceMediaFlag(true);
                    jLabel.setIcon(new ImageIcon(getClass().getResource("/icon/voice2.gif")));
                } catch (LineUnavailableException e) {
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Voice Notes", "", "Mic is not ready,<br> please check if it is properly working!!! ");
                } catch (IllegalArgumentException e) {
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Voice Notes", "", "Mic is not ready,<br> please check if it is properly working!!! ");

                }

                // change image
                jLabel.setIcon(new ImageIcon(getClass().getResource("/icon/voice-search-but-normal.gif")));
            }
        } else {
            // change image
            jLabel.setIcon(new ImageIcon(getClass().getResource("/icon/voice-search-but-normal.gif")));
            setSearchVoiceMediaFlag(null);
            searchVoiceMediaStop();
        }

    }

    /**
     * Stop searching of media by voice command
     *
     * @return
     */
    public String searchVoiceMediaStop() {
        if (getSearchVoiceMediaTdl() != null && getSearchVoiceMediaTdl().isRunning()) {

            getSearchVoiceMediaTdl().stop();
            getSearchVoiceMediaTdl().close();
            setSearchVoiceMediaTdl(null);
        }

        // convert wav file to text
        String convertedText = SSNStt.convertToText(new File(AudioRecorder.getSearchVoiceMediaFileName()));

        if (convertedText != null || !convertedText.equals("")) {
            StringBuffer sb = new StringBuffer(convertedText);
            String strToStartAt = "{\"result\":[]}{\"result\":[{\"alternative\":[{\"transcript\":";
            String strToStopAt = "\",\"confidence\":";
            if (sb.indexOf(strToStartAt) >= 0) {
                String result = sb.substring(strToStartAt.length() + 1, sb.indexOf(strToStopAt));
                getHomeForm().getSearchMediaTextField().setText(result);
            }
        }

        // TODO : take the response to search text box to search media
        // TODO : perform search 
        // TODO : show result
        // TODO : delete the file created here
        return AudioRecorder.getSearchVoiceMediaFileName();
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

    /**
     *
     * @return
     */
    public TargetDataLine getSearchVoiceMediaTdl() {
        return searchVoiceMediaTdl;
    }

    /**
     *
     * @param searchVoiceMediaTdl
     */
    public void setSearchVoiceMediaTdl(TargetDataLine searchVoiceMediaTdl) {
        this.searchVoiceMediaTdl = searchVoiceMediaTdl;
    }

    /**
     * To move the media from one album to another album
     *
     * @param moveToAlbum
     * @param files
     */
    public void moveAlbum(String moveToAlbum, Set<String> files) {
        String srcAlbumPath = getHomeForm().ssnFileExplorer.m_display.getText();

        if (SSNHelper.lastAlbum != null) {
            if (SSNHelper.lastAlbum.equals("View All Albums")) {
                srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                getHomeForm().ssnFileExplorer.m_display.setText(srcAlbumPath);
            } else if (SSNHelper.lastAlbum.equals("Instagram Media")) {
                srcAlbumPath = SSNHelper.getInstagramPhotosDirPath();
                getHomeForm().ssnFileExplorer.m_display.setText("instagramMedia");
            } else if (SSNHelper.lastAlbum.equals("OurHive Media")) {
                srcAlbumPath = SSNHelper.getSsnHiveDirPath();
                getHomeForm().ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
            } else if (SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) {
                srcAlbumPath = SSNHelper.lastAlbum;
                getHomeForm().ssnFileExplorer.m_display.setText(srcAlbumPath);
            }
        }

        try {
            if (srcAlbumPath != null && !(srcAlbumPath.trim().isEmpty())) {

                SSNConfirmationDialogBox confirm = new SSNConfirmationDialogBox();
                confirm.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation : ", "", "Do you want files to be copied to selected album?");
                int result = confirm.getResult();
                if (result == JOptionPane.YES_OPTION) {
                    if (StringUtils.isNotBlank(moveToAlbum)) {
                        Set<String> fileNames = files;
                        File destinationAlbumPath = new File(SSNHelper.getSsnHiveDirPath() + File.separator + moveToAlbum);
                        for (String file : fileNames) {

                            try {
                                File fileToBeCopied = new File(file);
                                File destinationFile = new File(destinationAlbumPath + File.separator + fileToBeCopied.getName());
                                FileUtils.copyFile(fileToBeCopied, destinationFile);
                                SSNGalleryMetaData data = SSNDao.getSSNMetaData(fileToBeCopied.getAbsolutePath());
                                SSNDao.insertMediaTable(destinationFile.getAbsolutePath(), data.getUserComments(), data.getSsnRatings(), data.getMediaLocation(), data.getMediaType(), data.getSsnKeywords(), data.getPhotoGrapher(), data.getCaption());
                                if (StringUtils.isNotBlank(data.getVoiceNotePath())) {
                                    SSNDao.insertVoiceNote_MediaTable(destinationFile.getAbsolutePath(), data.getVoiceNotePath());
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                            }
                        }
                        SSNGalleryHelper contentPane = new SSNGalleryHelper(srcAlbumPath, this.getHomeForm(), "ALL");

                        contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

                        this.getHomeForm().getSsnHomeCenterPanel().removeAll();
                        this.getHomeForm().getSsnHomeCenterMainPanel().removeAll();
                                //contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        //this.getHomeForm().getSsnHomeCenterPanel().add(contentPane);
                        this.getHomeForm().getSsnHomeCenterPanel().add(this.getHomeForm().getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.getHomeForm().ssnFileExplorer.m_display.getText())));

                        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                        this.getHomeForm().getSsnHomeCenterMainPanel().add(this.getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
                        this.getSSNMediaFolderProperties(getHomeForm().ssnFileExplorer.m_display.getText());
                        this.getHomeForm().revalidate();

                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Information", "", "Media Copied Successfully!");

                    } else {
                        // SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        //dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Information", "", "Please select destination album");
                    }
                }

            } else {
                //JOptionPane.showMessageDialog(this.getHomeForm().getSsnHomeCenterPanel(), "Please select an album");
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Information", "", "Please select an album");
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }

    /**
     * To show voice command setting form
     */
    public void displayVoiceCommandSettingForm() {
        if (userProfileForm != null && userProfileForm.isShowing()) {
            userProfileForm.dispose();
        }

        if (ssnPreferencesForm != null && ssnPreferencesForm.isShowing()) {
            ssnPreferencesForm.dispose();
        }
        if (getHomeForm().isShowing() && getHomeForm().isAlwaysOnTop()) {
            getHomeForm().setAlwaysOnTop(false);
        }
        if (ssnVoiceCommandPreferenceForm == null || (!ssnVoiceCommandPreferenceForm.isShowing())) {
            ssnVoiceCommandPreferenceForm = new SSNVoiceCommandPreferenceForm();
            ssnVoiceCommandPreferenceForm.toFront();
        }
    }

    /**
     *
     * @return
     */
    public List<File> getUntaggedMedia() {
        return untaggedMedia;
    }

    /**
     *
     * @param untaggedMedia
     */
    public void setUntaggedMedia(List<File> untaggedMedia) {
        this.untaggedMedia = untaggedMedia;
    }

    public SSNPreferencesForm getSsnPreferencesForm() {
        return ssnPreferencesForm;
    }

    public void setSsnPreferencesForm(SSNPreferencesForm ssnPreferencesForm) {
        this.ssnPreferencesForm = ssnPreferencesForm;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public Map<String,SSNAlbum> getHiveAlbums(String accessToken) {
        try {

            String urlString = SSNConstants.SSN_WEB_HOST + "api/albums/list/page:%s.json";
            URL url = new URL(String.format(urlString, 1));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String input = "access_token=%s";
            input = String.format(input, URLEncoder.encode(accessToken, "UTF-8"));
            System.out.println("input " +input);
            OutputStream os = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(input);
            writer.close();
            os.close();

            int status = 0;
            status = conn.getResponseCode();

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
                    List<Map<String, String>> albumJSON = (List<Map<String, String>>) outputJSON.get("Album");
                    if (albumJSON != null) {
                        ssnAlbumMap = new HashMap<String,SSNAlbum>();
                        for (Map<String, String> album : albumJSON) {
                            SSNAlbum ssnAlbum = mapper.readValue(mapper.writeValueAsString(album), SSNAlbum.class);
                            ssnAlbumMap.put(ssnAlbum.getName(),ssnAlbum);
                            
                            File createhiveAlbum = new File(SSNHelper.getSsnTempDirPath() + ssnAlbum.getName());
                            createhiveAlbum.mkdir();

                        }
                    }
                }
            }
        }catch(EOFException ee){
            logger.error(ee);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Message", "", "No Response from server.");
        }catch (Exception ee) {
            ee.printStackTrace();
        }
        return ssnAlbumMap;
    }
}
