package com.ssn.helper;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.listener.SSNHiveAlbumSelectionListner;
import com.ssn.ui.custom.component.BackGroundImagePanel;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import static com.ssn.ui.custom.component.SSMMediaGalleryPanel.readMetaData;
import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNMyScrollbarUI;
import com.ssn.ui.custom.component.WrapLayout;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNShareForm;
import com.ssn.ws.rest.response.SSNAlbumMedia;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

/**
 *
 * @author pkumar2
 */
public class SSNGalleryHelper extends JPanel implements AdjustmentListener, ComponentListener {

    private JPanel jp = null;
    private static final GridBagConstraints gbc;
    private static Logger log = Logger.getLogger(SSNGalleryHelper.class);

    List<String> videoSupportedList = null;
    List<String> mediaSupportedList = null;
    List<String> imagesSupportedList = null;
    SSNHomeForm ssnHomeForm = null;

    static {
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

    }

    {
        videoSupportedList = Arrays.asList(SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED);
        mediaSupportedList = Arrays.asList(SSNConstants.SSN_UPLOAD_FILE_FORMAT_SUPPORTED);
        imagesSupportedList = Arrays.asList(SSNConstants.SSN_SLIDESHOW_IMAGE_FILE_FORMAT_SUPPORTED);
    }

    public static final int SPACEING = 5;
    public static final int IMAGE_DIM = 50;
    private BufferedImage[] imgs;
    private JLabel[] icons;
    public File[] listOfFiles = null;
    /**
     * Nubmer of rows that are visible in the viewport
     */
    private double visibleNbRows;

    /**
     * Model for the vertical scroll bar
     */
    private BoundedRangeModel brm;
    public final int nbCols = 4;
    private int nbRows;
    private String selectedPhotoName = "";
    static int iT = 0;
    static int iF = 0;
    private BufferedImage videoFrame = null;
    private JPanel previousPanel = null;

    /**
     *
     * @return
     */
    public BufferedImage getVideoFrame() {
        return videoFrame;
    }

    /**
     *
     * @param videoFrame
     */
    public void setVideoFrame(BufferedImage videoFrame) {
        this.videoFrame = videoFrame;
    }

    /**
     *
     * @param mediaPath
     * @param ssnHomeForm
     * @param mediaToShow
     * @throws java.io.IOException
     */
    public SSNGalleryHelper(String mediaPath, final SSNHomeForm ssnHomeForm, String mediaToShow) throws IOException {
        this.ssnHomeForm = ssnHomeForm;
        ssnHomeForm.setMediaToShow(mediaToShow);
       
        //get media path for diiffert media selection
        mediaPath = getMediaPath(mediaPath);
        // System.out.println("SSNGalleryHelper mediaPath"+ mediaPath);
        File folder = new File(mediaPath);
        // File[] listOfFiles = folder.listFiles();
        List<File> fileList = new ArrayList<>();
        fileList = SSNHelper.listFiles(folder, fileList);
        listOfFiles = new File[fileList.size()];
        int j = fileList.size() - 1;
        for (Object obj : fileList) {
            listOfFiles[j] = (File) obj;
            j--;
        }
        //System.out.println("listOfFiles::"+fileList.size());
        ssnHomeForm.setSsnGalleryMediaPath(mediaPath);
        createGalleryPanel(listOfFiles, ssnHomeForm);

    }
    
    public String getMediaPath(String mediaPath)
    {
        if(mediaPath.equals("viewAllAlbums")){
            mediaPath = SSNHelper.getSsnHiveDirPath();
        }else if(mediaPath.equals("instagramMedia")){
            mediaPath = SSNHelper.getInstagramPhotosDirPath();
        }
        else if(mediaPath.equals("tagUnTaggedMedia"))
        {
            mediaPath = SSNHelper.getSsnHiveDirPath();
        }
        else if(!SSNHelper.getAlbumNameFromPath(mediaPath).isEmpty()){
            ssnHomeForm.ssnFileExplorer.m_display.setText(mediaPath);
        }
        
        return mediaPath;
    }

    /**
     *
     * @param listOfFiles
     * @param ssnHomeForm
     * @throws IOException
     */
    public SSNGalleryHelper(File[] listOfFiles, final SSNHomeForm ssnHomeForm) throws IOException {
        this.listOfFiles = listOfFiles;
        this.ssnHomeForm = ssnHomeForm;
        createGalleryPanel(listOfFiles, ssnHomeForm);

    }
    public static int curruptFileCount = 0;

    /**
     * Create the gallery panel to show the media.
     *
     * @param listOfFiles
     * @param ssnHomeForm
     * @throws IOException
     */
    public void createGalleryPanel(final File[] listOfFiles, final SSNHomeForm ssnHomeForm) throws IOException {
    
        ssnHomeForm.getAllBoxes().clear();
        ssnHomeForm.setCheckMultiSelection(false);
        ssnHomeForm.getAllChkBoxPanel().clear();
        
      //  ssnHomeForm.getSearchMediaTextField().setText("");

        List<File> fileList = new ArrayList<File>(Arrays.asList(listOfFiles));
//        Iterator<File> iter = fileList.iterator();
        //ssnHomeForm.getRatingMap().clear();
//        while(iter.hasNext()){
//            File f = iter.next();
//            String fileExtension = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());
//            if(videoSupportedList.contains(fileExtension.toUpperCase())){
//                iter.remove();
//            }
//        }

        File[] imageFiles = new File[fileList.size()];
        int l = 0;
        for (File f : fileList) {
            imageFiles[l] = f;
            l++;
        }
        ssnHomeForm.setCurrentGallery(imageFiles);

        imgs = new BufferedImage[listOfFiles.length];
        icons = new JLabel[imgs.length];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = null;
        }
        nbRows = (int) Math.ceil(1.0 * imgs.length / nbCols);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 10, 0, 0));

        URL imgURL = getClass().getResource("/images/ssn-dashboard-bg.png");
        BufferedImage image1 = ImageIO.read(imgURL);
        SSNImagePanel panel = new SSNImagePanel(image1);
        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        setJp(panel);
       //getJp().setLayout(new WrapLayout(WrapLayout.LEFT, 10, 10));
        getJp().setLayout(new WrapLayout(WrapLayout.LEFT,10,10));
        getJp().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        JPanel northSouthPanel = new JPanel(new BorderLayout());
        northSouthPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        northSouthPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/heading-underline.jpg")), SwingConstants.HORIZONTAL), BorderLayout.CENTER);

        // load starting 1 images
        loadImages(0, 1);
        
        JScrollPane scroller = new JScrollPane(jp);
        scroller.setBorder(new EmptyBorder(0, 0, 0, 10));
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension dimention = Toolkit.getDefaultToolkit().getScreenSize();
       // System.out.println("dimention width " + dimention.getWidth());
        int height = (int) (dimention.getHeight() - ssnHomeForm.getSsnHomeToolBar().getHeight() + ssnHomeForm.getSearchPanel().getHeight());

        scroller.getVerticalScrollBar().setUI(new SSNMyScrollbarUI(height));
        scroller.getVerticalScrollBar().addAdjustmentListener(new MyAdjustmentListener());
        scroller.setViewportView(jp);
        add(jp, BorderLayout.CENTER);
        ssnHomeForm.setSsnGalleryHelper(this);
    }   

    public void loadImages(int startIndex, int endIndex) {

        if (listOfFiles.length > startIndex) { 

            JLabel iconLabel = null;
            BackGroundImagePanel totalPanel = null;
            BufferedImage thumbImg1 = null;
            IMediaReader reader = null;
            BufferedImage image = null;
            BufferedImage thumbImg = null;
            Icon icon = null;
            JPanel foregroundPanel = null;
            SSNGalleryMetaData data = null;
            boolean check = false;
            for (int i = startIndex; i < endIndex; i++) {
                if (i >= listOfFiles.length) {
                    break;
                }
                iF = 0;
                final File fileToRead = listOfFiles[i];
                final String file_name = listOfFiles[i].getName();

               // System.out.println("file_name"+file_name);
                final String fileExtension = file_name.substring(file_name.lastIndexOf(".") + 1, file_name.length());
                if (fileToRead.length() > 0) {
                    if (videoSupportedList.contains(fileExtension.toUpperCase())) {

                        try {

                            if (StringUtils.isNotBlank(ssnHomeForm.getMediaToShow()) && (ssnHomeForm.getMediaToShow().equalsIgnoreCase("VIDEOS") || ssnHomeForm.getMediaToShow().equalsIgnoreCase("ALL"))) {
                                reader = ToolFactory.makeReader(fileToRead.getAbsolutePath());
                                reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
                                reader.addListener(new MediaListenerAdapter() {
                                    @Override
                                    public void onVideoPicture(IVideoPictureEvent event) {
                                        setVideoFrame(event.getImage());
                                        iF++;
                                    }

                                });
                               while (reader.readPacket() == null && iF == 0);
                                thumbImg1 = SSNHelper.resizeImage(getVideoFrame(), SSNConstants.SSN_IMAGE_THUMBNAIL_WIDTH, SSNConstants.SSN_IMAGE_THUMBNAIL_HEIGHT);
                            } else {
                                continue;
                            }
                        } catch (Throwable e) {
                           // e.printStackTrace();
                            log.error("******" + e);

                        } finally {
                            if (reader != null) {
                                reader.close();
                            }
                        }
                    } else if(imagesSupportedList.contains(fileExtension.toUpperCase())){
                        if (StringUtils.isNotBlank(ssnHomeForm.getMediaToShow()) && (ssnHomeForm.getMediaToShow().equalsIgnoreCase("IMAGES") || ssnHomeForm.getMediaToShow().equalsIgnoreCase("ALL"))) {
                            try {
                                image = ImageIO.read(new File(fileToRead.getAbsolutePath()));
                                thumbImg1 = SSNHelper.resizeImage(image, SSNConstants.SSN_IMAGE_THUMBNAIL_WIDTH, SSNConstants.SSN_IMAGE_THUMBNAIL_HEIGHT);
                                thumbImg = Scalr.resize(image, Method.BALANCED, Mode.FIT_EXACT, 120, 122, Scalr.OP_ANTIALIAS);
                            } catch (IIOException ee) {
                               // ee.printStackTrace();
                                log.error("*********** " + ee);

                                continue;
                            } catch (IOException ex) {
                                //java.util.logging.Logger.getLogger(SSNGalleryHelper.class.getName()).log(Level.SEVERE, null, ex);
                                log.error(ex);
                            }
                        } else {
                            continue;
                        }
                    }else{
                        fileToRead.delete();
                    }
                } else {
                    curruptFileCount++;
                    continue;
                }

                try {
                    if (thumbImg1 != null && thumbImg1.getSource() != null) {
                        icon = new ImageIcon(thumbImg1);
                    } else {
                        continue;
                    }

                } catch (Exception ee) {

                    log.error(ee);

                }

                if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                    foregroundPanel = new JPanel(new BorderLayout());
                    foregroundPanel.setOpaque(false);
                    foregroundPanel.add(new JLabel(new ImageIcon(getClass().getResource("/icon/video-normal.png"))), BorderLayout.CENTER);
                    totalPanel = (BackGroundImagePanel) wrapInBackgroundImage(foregroundPanel,
                            thumbImg1.getScaledInstance(SSNConstants.SSN_IMAGE_THUMBNAIL_WIDTH, SSNConstants.SSN_IMAGE_THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH));
                } else {
                    totalPanel = new BackGroundImagePanel(thumbImg1.getScaledInstance(SSNConstants.SSN_IMAGE_THUMBNAIL_WIDTH, SSNConstants.SSN_IMAGE_THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH));
                    totalPanel.setLayout(new BorderLayout());
                    totalPanel.setBorder(new EmptyBorder(-2, 0, 0, 0));
                    icons[i] = new JLabel(icon);
                    iconLabel = icons[i];
                }

                data = checkMetaData(fileToRead);

                final JPanel thumbnil = new JPanel();
                if (ssnHomeForm.getCurrentSelectedFile() != null) {
                    if (ssnHomeForm.getCurrentSelectedFile().getAbsolutePath().equalsIgnoreCase(fileToRead.getAbsolutePath())) {
                        ssnHomeForm.getSsnHomeRightPanel().removeAll();
                        ssnHomeForm.getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(fileToRead, ssnHomeForm), BorderLayout.NORTH);
                        ssnHomeForm.getSsnHomeRightPanel().revalidate();
                        thumbnil.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                        previousPanel = thumbnil;
                    }
                }
                final int count = i;

                thumbnil.setLayout(new BorderLayout());
                thumbnil.setPreferredSize(new Dimension(145, 122));
                thumbnil.setOpaque(true);
                thumbnil.setBackground(SSNConstants.SSN_GALLERY_THUMBNAIL_BLACK_COLOR);
                final JCheckBox chk = new JCheckBox();
                ssnHomeForm.getAllBoxes().add(chk);

                chk.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (chk.isSelected()) {
                            if (ssnHomeForm.getFileNamesToBeDeleted().size() == 0) {
                                SSNHelper.toggleDeleteAndShareImages(true, ssnHomeForm);
                            }
                            ssnHomeForm.getFileNamesToBeDeleted().add(fileToRead.getAbsolutePath());
                            //code for single selection  start
                            if (ssnHomeForm.getFileNamesToBeDeleted() != null && !ssnHomeForm.getCheckMultiSelection()) {
                                ssnHomeForm.getSsnHomeRightPanel().removeAll();
                                for (Iterator<String> it = ssnHomeForm.getFileNamesToBeDeleted().iterator(); it.hasNext();) {
                                    it.next();
                                    ssnHomeForm.getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(new File(""), ssnHomeForm), BorderLayout.NORTH);
                                }
                                ssnHomeForm.getSsnHomeRightPanel().revalidate();
                            }
                            //code for singe selection end
                        } else {
                            ssnHomeForm.getFileNamesToBeDeleted().remove(fileToRead.getAbsolutePath());
                            if (ssnHomeForm.getFileNamesToBeDeleted().size() == 0) {
                                SSNHelper.toggleDeleteAndShareImages(false, ssnHomeForm);
                                if (!ssnHomeForm.ssnFileExplorer.m_display.getText().equalsIgnoreCase(SSNHelper.getSsnHiveDirPath())) {
                                    SSNHelper.toggleDeleteImage(true, ssnHomeForm);
                                }
                            }
                        }
                    }
                });

                final JPanel panelChk = new JPanel(new BorderLayout());
                panelChk.add(chk, BorderLayout.WEST);
                panelChk.setBackground(new Color(0, 0, 0, 125));
                panelChk.setBounds(0, 0, 5, 5);
                panelChk.setSize(chk.getSize());
                panelChk.setVisible(false);
                panelChk.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        manageMouseClickOverThumbnail(e,  fileExtension, fileToRead, ssnHomeForm, listOfFiles, count, thumbnil);
                    }
                });

                panelChk.addMouseMotionListener(new MouseMotionAdapter() {

                    @Override
                    public void mouseMoved(MouseEvent e) {
                        
                        panelChk.setVisible(true);
                        
                    }
                });

                final JPanel bottomImg = new JPanel(new BorderLayout());
                bottomImg.setPreferredSize(new Dimension(155, 31));

                JPanel buttons = new JPanel(new GridLayout(1, 4));
                buttons.add(Box.createHorizontalStrut(5));

                final JLabel delete = new JLabel(new ImageIcon(getClass().getResource("/icon/delete.png")));
                buttons.setPreferredSize(new Dimension(155, 31));

                delete.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Set<String> deleteFile = new HashSet<String>();
                        if(SSNHelper.lastAlbum != null && (SSNHelper.lastAlbum.equals("Instagram Media") || SSNHelper.lastAlbum.contains(SSNHelper.getFacebookPhotosDirPath())) )
                        {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Alert", "", "Media can not be deleted.");
                        }
                        else
                        {
                           SSNConfirmationDialogBox confirmationDialogBox = new SSNConfirmationDialogBox();
                           confirmationDialogBox.setAlwaysOnTop(true);
                           confirmationDialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirmation ", "", "Are you sure to delete the selected media?");
                        
                        int result = confirmationDialogBox.getResult();
                        if (result == JOptionPane.YES_OPTION) {
                                ssnHomeForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                                deleteFile.add(fileToRead.getAbsolutePath());
                                ssnHomeForm.setFileNamesToBeDeleted(deleteFile);
                           
                                ssnHomeForm.getHomeModel().deletePhotos();
                                ssnHomeForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            }
                        }
                     }

                });
                
                buttons.add(delete);
                buttons.add(Box.createHorizontalStrut(5));

                if (data != null) {
                    if (isTagged(data.getEditMediaLocation(),data.getSsnKeywords())) {
                        final JLabel tagged = new JLabel(new ImageIcon(getClass().getResource("/icon/taggedMedia.png")));
                        buttons.add(tagged);
                        buttons.add(Box.createHorizontalStrut(5));
                    }
                }
                buttons.setBackground(SSNConstants.SSN_GALLERY_THUMBNAIL_BLACK_COLOR);
                final JLabel share = new JLabel(new ImageIcon(getClass().getResource("/icon/image-share-icon-normal.png")));
                buttons.add(share);
                buttons.add(Box.createHorizontalStrut(5));

                chk.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {

                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);
                    }
                });
                delete.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {

                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);
                    }
                });
                share.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {

                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);
                    }
                });
                share.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Set<String> fileSet = new HashSet<>();
                        fileSet.add(fileToRead.getAbsolutePath());
                        new SSNShareForm(ssnHomeForm.getHomeModel(), fileSet);
                    }

                });

                ssnHomeForm.getAllChkBoxPanel().add(panelChk);
           
                buttons.add(Box.createHorizontalStrut(5));
                buttons.setBackground(new Color(0, 0, 0, 125));

                bottomImg.add(Box.createHorizontalGlue(), BorderLayout.WEST);
                bottomImg.setBackground(new Color(0, 0, 0, 125));
                if(!SSNFileExplorer.selectedMedia.equals("hive"))
                    bottomImg.add(buttons, BorderLayout.EAST);
                bottomImg.setVisible(false);
                
                totalPanel.add(bottomImg, BorderLayout.SOUTH);
                totalPanel.add(panelChk, BorderLayout.NORTH);
                thumbnil.add(totalPanel, BorderLayout.CENTER);
                getJp().add(thumbnil);

                totalPanel.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);

                    }
                });
                totalPanel.addMouseListener(new MouseAdapter() {
                  // this.getssnhsetCursor(new Cursor(Cursor.WAIT_CURSOR));
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int i = 0;
                        manageMouseClickOverThumbnail(e, fileExtension, fileToRead, ssnHomeForm, listOfFiles, count, thumbnil);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(!chk.isSelected()){
                          
                            panelChk.setVisible(false);
                        }
                        bottomImg.setVisible(false);
                    }
                });
                bottomImg.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);
                    }
                });
                bottomImg.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        if(!chk.isSelected()){
                         
                            panelChk.setVisible(false);
                        }
                        bottomImg.setVisible(false);
                    }
                });
                buttons.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        bottomImg.setVisible(true);
                        panelChk.setVisible(true);

                    }
                });
                buttons.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        manageMouseClickOverThumbnail(e, fileExtension, fileToRead, ssnHomeForm, listOfFiles, count, thumbnil);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(!chk.isSelected()){
                           
                            panelChk.setVisible(false);
                        }
                         bottomImg.setVisible(false);
                    }
                });
            }
        }
    }
    
     public SSNGalleryMetaData checkMetaData(File fileToRead)
    {
        SSNGalleryMetaData data = null;
        boolean check = false;
        try {
            check = SSNDao.checkMediaExist(fileToRead.getAbsolutePath());
            if (check) {
                data = SSNDao.getSSNMetaData(fileToRead.getAbsolutePath());
            } else {
                data = readMetaData(fileToRead);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return data;
    }

    class MyAdjustmentListener implements AdjustmentListener {

      
        public void adjustmentValueChanged(AdjustmentEvent e) {
            //System.out.println(" value change...");
            new GuiWorker().execute();

        }
    }

    class GuiWorker extends SwingWorker<Integer, Integer> {

        public GuiWorker() {}

        @Override
        protected Integer doInBackground() throws Exception 
        {
            //System.out.println("GuiWorker.doInBackground");
            int offset = (listOfFiles.length - 1);
            int startIndex = 1;
            int endIndex = 0;
            if (offset != 0) {

                for (int i = 1; i <= offset; i++) {
                    endIndex = startIndex + 1;

                    if (endIndex > listOfFiles.length) {
                        endIndex = listOfFiles.length;
                    }

                    loadImages(startIndex, endIndex);
                    startIndex = startIndex + 1;
                    jp.validate();
                }
            } else {
                // if(endIndex > listOfFiles.length)

                endIndex = listOfFiles.length;
                loadImages(startIndex, endIndex);
                jp.validate();
                //ssnHomeForm.repaint();
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ssnHomeForm.repaint();
                    updateUI();
                }
            });
            return 0;
        }

        @Override
        protected void done() 
        {
        }

    }

    private void manageMouseClickOverThumbnail(MouseEvent e,  String fileExtension, File fileToRead, SSNHomeForm ssnHomeForm, File[] listOfFiles, int count, JPanel thumbnil) {
        ssnHomeForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (e.getClickCount() == 2) {

            /* For video start */

            if (videoSupportedList.contains(fileExtension.toUpperCase())) {
                final String operatingSystem = System.getProperty("os.name");
                File file = new File(SSNHelper.getSsnWorkSpaceDirPath() + File.separator + "tempVideo");
                final File video = new File(file.getAbsolutePath() + File.separator + fileToRead.getName());
                try {
                    if (file.exists() && file.isDirectory()) {
                        File[] fileList = file.listFiles();
                        if (fileList.length == 1) {
                            if (!fileList[0].getName().equals(fileToRead.getName())) {
                                fileList[0].delete();
                                FileUtils.copyFile(fileToRead, video);
                            }
                        }
                        
                    } else {
                        file.mkdir();
                        FileUtils.copyFile(fileToRead, video);
                    }

                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (operatingSystem.toLowerCase().contains("windows")) {
                                    Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + video.getAbsolutePath());
                                } else if (operatingSystem.toLowerCase().contains("mac")) {
                                    Runtime.getRuntime().exec("open " + video.getAbsolutePath());
                                }
                            } catch (IOException ex) {
                                log.error(ex);
                            }

                        }

                    };
                    t.start();
                } catch (IOException ex) {
                    log.error(ex);
                }
                /*  For video end*/
            } else {
                // System.out.println("value of " + count);
                if(SSNFileExplorer.selectedMedia.equals("hive")){
                    SSNAlbumMedia sSNAlbumMedia =   SSNHiveAlbumSelectionListner.ssnHiveAlbumAllMedia.get(listOfFiles[count].getName());
                    File getFilefromHive        =   new File(SSNHelper.getSsnTempDirPath()+sSNAlbumMedia.getAlbums()+File.separator+listOfFiles[count].getName())   ;
                    URL url = null;
                    try {
                        url = new URL(sSNAlbumMedia.getFile_url());
                    } catch (MalformedURLException ex) {
                       log.error(ex);
                    }
                    try {
                        FileUtils.copyURLToFile(url, getFilefromHive);
                    } catch (IOException ex) {
                       log.error(ex);
                    }
                    ssnHomeForm.getHomeModel().openImageSlider(new JLabel(), listOfFiles, count);  
                }else{
                    ssnHomeForm.getHomeModel().openImageSlider(new JLabel(), listOfFiles, count);
                }
            }
        }

        ssnHomeForm.getSsnHomeRightPanel().removeAll();
        ssnHomeForm.getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(fileToRead, ssnHomeForm), BorderLayout.NORTH);
        ssnHomeForm.getSsnHomeRightPanel().revalidate();
        ssnHomeForm.setCurrentSelectedFile(fileToRead);
        if (previousPanel != null) {
            previousPanel.setBorder(null);
        }
        thumbnil.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        previousPanel = thumbnil;
        ssnHomeForm.revalidate();

        iT++;
        ssnHomeForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * This method is called when the scroll bar is moved.
     *
     * @param e
     * @see AdjustmentListener#adjustmentValueChanged(AdjustmentEvent)
     */
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        updateView();
    }

    /**
     * Make sure that everything that is visible is loaded
     */
    private void updateView() {
        double percentage = 100.0 * getBrm().getValue() / (getBrm().getMaximum() - getBrm().getExtent());
        int firstRow = computeFirstRowIndex(percentage / 100);
        int lastRow = computeLastRowIndex(percentage / 100);
        updateImages(firstRow, lastRow);
    }

    /**
     * Load and unload the images that are contained between the rows
     *
     * @param firstRow
     * @param lastRow
     */
    private void updateImages(int firstRow, int lastRow) {
        int firtsImageIndex = firstRow * nbCols;
        int lastImageIndex = Math.min(lastRow * nbCols + nbCols - 1, getImgs().length - 1); // make sure we do not try accessing an element that is not there
        for (int i = firtsImageIndex; i <= lastImageIndex; i++) {
            if (getImgs()[i] == null) {
                BufferedImage blackSquare = new BufferedImage(IMAGE_DIM, IMAGE_DIM, BufferedImage.TYPE_BYTE_GRAY);
                Graphics2D g = (Graphics2D) blackSquare.getGraphics();
                BufferedImage img = new BufferedImage(IMAGE_DIM, IMAGE_DIM, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2d = (Graphics2D) img.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                FontRenderContext frc = g2d.getFontRenderContext();

                Font font = g2d.getFont();
                font = font.deriveFont(24);

                GlyphVector gv = font.createGlyphVector(frc, String.valueOf(i));
                Rectangle2D rect = gv.getVisualBounds();
                int x = (int) ((IMAGE_DIM - rect.getWidth()) / 2);
                int y = (int) ((IMAGE_DIM - rect.getHeight()) / 2);
                g2d.drawGlyphVector(gv, x, y);
                g.drawImage(img, 0, 0, this);
                getIcons()[i].setIcon(new ImageIcon(blackSquare));
            }
        }
    }

    /**
     * Figure out the index of the last row to be shown
     *
     * @param percentage of scrolling [0,1]
     * @return
     */
    private int computeLastRowIndex(double percentage) {
        double pos = computeTopPosition(percentage);
        int result = (int) Math.floor((pos - SPACEING) / (SPACEING + IMAGE_DIM) + getVisibleNbRows());
        return Math.min(result, getNbRows());
    }

    /**
     * Figure out the index of the first row to be shown
     *
     * @param percentage of scrolling [0,1]
     * @return
     */
    private int computeFirstRowIndex(double percentage) {
        double pos = computeTopPosition(percentage);
        int result = (int) Math.floor((pos - SPACEING) / (SPACEING + IMAGE_DIM));
        return Math.max(0, result);
    }

    /**
     * Compute the top position in the panel that is visible.
     *
     * @param percentage
     * @return
     */
    private double computeTopPosition(double percentage) {
        /*
         * The percentage indicates the scroll position, so 0% means all to
         * the top, 100% is all the bottom. The top position of the bottom however
         * is the height of the panel - the height of the viewport (parent). anything
         * in between is procentual 
         */
        double top = getBounds().height * percentage;
        double correction = getParent().getBounds().height * percentage;
        return top - correction;
    }

    /**
     *
     * @param brm
     */
    public void setBrm(BoundedRangeModel brm) {
        this.brm = brm;
    }

    /**
     * @return the brm
     */
    public BoundedRangeModel getBrm() {
        return brm;
    }

    /**
     * We are not interested in reacting when the component becomes hidden
     *
     * @param e
     * @see ComponentListener#componentHidden(ComponentEvent)
     */
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * We are not interested in reacting when the component is moved
     *
     * @param e
     * @see ComponentListener#componentMoved(ComponentEvent)
     */
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * When the component is resized, the visible portion changes, take kare of
     * the appropriate actions.
     *
     * @see ComponentListener#componentResized(ComponentEvent)
     */
    /**
     * When the component is resized, the visible portion changes, take kare of
     * the appropriate actions.
     *
     * @param e
     * @see ComponentListener#componentResized(ComponentEvent)
     */
    public void componentResized(ComponentEvent e) {
        int rowHeigth = SPACEING + IMAGE_DIM;
        visibleNbRows = 1.0 * getParent().getBounds().height / rowHeigth;
        updateView(); // make sure the view is properly updated
    }

    /**
     * We are not interested in reacting when the component is shown again.
     *
     * @param e
     * @see ComponentListener#componentShown(ComponentEvent)
     */
    public void componentShown(ComponentEvent e) {
    }

    /**
     * @return the imgs
     */
    public BufferedImage[] getImgs() {
        return imgs;
    }

    /**
     * @return the icons
     */
    public JLabel[] getIcons() {
        return icons;
    }

    /**
     * @return the visibleNbRows
     */
    public double getVisibleNbRows() {
        return visibleNbRows;
    }

    /**
     * @return the nbRows
     */
    public int getNbRows() {
        return nbRows;
    }

    /**
     * @return the selectedPhotoName
     */
    public String getSelectedPhotoName() {
        return selectedPhotoName;
    }

    /**
     * @param selectedPhotoName the selectedPhotoName to set
     */
    public void setSelectedPhotoName(String selectedPhotoName) {
        this.selectedPhotoName = selectedPhotoName;
    }

    /**
     *
     * @return
     */
    public JPanel getJp() {
        return jp;
    }

    /**
     *
     * @param jp
     */
    public void setJp(JPanel jp) {
        this.jp = jp;
    }

    /**
     * This method will automatically wrap the image according to the size of
     * background
     *
     * @param component
     * @param backgroundIcon
     * @return
     */
    public static JPanel wrapInBackgroundImage(JComponent component,
            Icon backgroundIcon) {
        component.setOpaque(false);
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.add(component, gbc);
        JLabel backgroundImage = new JLabel(backgroundIcon);
        backgroundPanel.add(backgroundImage, gbc);
        return backgroundPanel;
    }

    public static BackGroundImagePanel wrapInBackgroundImage(JComponent component,
            Image backgroundIcon) {
        component.setOpaque(false);
        BackGroundImagePanel backgroundPanel = new BackGroundImagePanel(backgroundIcon);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(component, BorderLayout.CENTER);
       
        return backgroundPanel;
    }


    public static boolean isTagged(String location,String tagValue) {
        boolean flag = false;
        if ((location != null && !location.trim().isEmpty())&& (tagValue != null && !tagValue.trim().isEmpty())) {
            flag = true;
        }
        return flag;
    }
    public static void downloadMedia(File[] listOfFiles,int index){
        SSNAlbumMedia sSNAlbumMedia =   SSNHiveAlbumSelectionListner.ssnHiveAlbumAllMedia.get(listOfFiles[index].getName());
                    File getFilefromHive        =   new File(SSNHelper.getSsnTempDirPath()+sSNAlbumMedia.getAlbums()+File.separator+listOfFiles[index].getName())   ;
                    URL url = null;
                    try {
                        url = new URL(sSNAlbumMedia.getFile_url());
                    } catch (MalformedURLException ex) {
                       log.error(ex);
                    }
                    try {
                        FileUtils.copyURLToFile(url, getFilefromHive);
                    } catch (IOException ex) {
                       log.error(ex);
                    }
    }
}
