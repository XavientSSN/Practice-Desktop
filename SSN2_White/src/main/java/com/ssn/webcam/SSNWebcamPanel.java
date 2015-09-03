package com.ssn.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;
import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNDirSelectionListener;
import com.ssn.schedule.SSNScheduleListForm;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import org.apache.log4j.Logger;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;


public class SSNWebcamPanel extends JFrame implements Runnable, WebcamListener, WindowListener, Thread.UncaughtExceptionHandler, ItemListener,WebcamPanel.Painter, SSNConstants {

    private static final long                serialVersionUID = 1L;
    final    private static Logger logger = Logger.getLogger(SSNWebcamPanel.class);
    private static final Executor            EXECUTOR         = Executors.newSingleThreadExecutor();
    private static       HaarCascadeDetector detector         = new HaarCascadeDetector();
    private              WebcamPanel.Painter painter          = null;
    private              List<DetectedFace>  faces            = null;
    private              Webcam              webcam           = null;
    private              WebcamPanel         panel            = null;
    private              WebcamPicker        picker           = null;
    private              SSNHomeForm         homeForm         = null;
    private              JPanel              settingPanel     = null; 
    private              JPanel              functionPanel    = null;
    private              JLabel              webcamInfo       = new JLabel();
    private              Date                webcamOpenTime   = null;
    private              JLabel              webcamTollbarLbl = null;
    private              JFrame              microphoneFrame  = null;
    private              File                tmpImageDir   = null;
       
    public SSNWebcamPanel(SSNHomeForm homeForm,JLabel webcamTollbarLbl) {        
        super("OurHive Webcamera");           
        try {
            this.homeForm = homeForm;
            setWebcamTollbarLbl(webcamTollbarLbl);
            
            // create a tmp folder for placing image in
            File tmpDir = new File(SSNHelper.getSsnWorkSpaceDirPath()+File.separator+"tmpFolder");
            if(!tmpDir.exists()){
                tmpDir.mkdir();
            }
            
            this.setTmpImageDir(tmpDir);
            
            initWebcam();    
        } 
        catch (Exception ex) {            
        }
    }
    
    private ScheduledExecutorService executorService = null;
    
    @Override
    public void run() { 
        if (getWebcam() != null) {
                if(homeForm != null && homeForm.isShowing() && homeForm.isAlwaysOnTop()) {
                        homeForm.setAlwaysOnTop(false); 
                        this.setAlwaysOnTop(true);
                }
                
                Thread t = new Thread() {
                     public void run() {                          
                         if(getPanel() != null)
                            getPanel().start();                                                  
                         setAlwaysOnTop(true); 
                     }
                };
                t.setName("ssn-webcam");
                t.setDaemon(true);
                t.start();
        }
    }

    private void initWebcam() {
        
        setPicker(new WebcamPicker());
        setWebcam(getPicker().getSelectedWebcam());          
        if (getWebcam() == null) {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","!!! No WebCam found !!!");
        }
        else { 
            if(getWebcam() != null && getWebcam().isOpen()) {    
                //JOptionPane.showMessageDialog(this.homeForm,"Cam already open");
//                if(homeForm != null && homeForm.isShowing() && homeForm.isAlwaysOnTop()) {
//                    homeForm.setAlwaysOnTop(false); 
//                }
            }
            else {
                String resolution = "";
                Dimension d [] = webcam.getViewSizes();
                if(d.length > 0) {
                    for(Dimension dim : d) { 
                         getWebcam().setViewSize(dim);                        
                         resolution = Integer.toString((int)dim.getWidth())+" X "+Integer.toString((int)dim.getHeight());
                         //break;
                    }
                    webcamInfo.setText(webcam.getName()+","+resolution); 
                    getWebcam().addWebcamListener(SSNWebcamPanel.this);
                    getWebcam().open(true);
                    webcamOpenTime = new Date();                    
                    setPanel(new WebcamPanel(getWebcam(),false));                
                    //getPanel().setFPSDisplayed(true); 
                    getPanel().setFillArea(false);
                    getPanel().setPainter(this);
                    getPanel().setPreferredSize(new Dimension(300,375));
                    setPainter(getPanel().getDefaultPainter());
                    this.initWebcamGUIComponents();     
                }
                else {
                    //JOptionPane.showMessageDialog(null,"No Suitable Resolution Available");
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","No Suitable Resolution Available");
                } 
            }
        }
    }
    
    private void initWebcamGUIComponents() {        
        getPanel().setBorder(new BevelBorder(BevelBorder.LOWERED)); 
        setFunctionPanel(new SSNWebcamFunctionPanel(getWebcam(),webcamOpenTime.getTime(), this));
        getFunctionPanel().setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.initWebcamFrame();
    }
    
    private void initWebcamFrame() {
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);                
        this.setResizable(false);
        this.setAlwaysOnTop(true); 
        this.setUndecorated(false);
       
        
        this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());
        this.addWindowListener(this);
        this.add(getPanel(),BorderLayout.NORTH);
        this.add(getFunctionPanel(),BorderLayout.CENTER);        
        this.setPreferredSize(new Dimension(500,575));      
        this.setLocation(300,20);        
        this.pack();
        this.setVisible(true);        
    }
    
    @Override
    public void paintPanel(WebcamPanel panel, Graphics2D g2) {
            if (getPainter() != null) {
                    getPainter().paintPanel(panel, g2);
            }
    }

    @Override
    public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

            if (getPainter() != null) {
                    getPainter().paintImage(panel, image, g2);
            }

            if (getFaces() == null) {
                    return;
            }

            Iterator<DetectedFace> dfi = getFaces().iterator();
            while (dfi.hasNext()) {
                    DetectedFace face = dfi.next();
                    Rectangle bounds = face.getBounds();

                    int dx = (int) (0.1 * bounds.width);
                    int dy = (int) (0.2 * bounds.height);
                    int x = (int) bounds.x - dx;
                    int y = (int) bounds.y - dy-10;
                   // int w = (int) bounds.width + 2 * dx;
                   // int h = (int) bounds.height + dy;
                    g2.setColor(Color.RED);
                    g2.drawRect(x,y,280,240);
            }
    }
   
    @Override
    public void webcamOpen(WebcamEvent we) {
        logger.info("webcam open");
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
//        deleteDir(getTmpImageDir(),SSNHelper.getSsnDefaultDirPath());
//        if(homeForm.getSsnMicrophoneCamThread()!=null){
//            homeForm.getSsnMicrophoneCamThread().stop();
//            homeForm.setSsnMicrophoneCamThread(null);
//        }
        logger.info("webcam closed");
        getWebcamTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/camera-normal.png"))); 
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        logger.info("webcam disposed");
        getWebcamTollbarLbl().setIcon(new ImageIcon(getClass().getResource("/icon/camera-normal.png"))); 
        //homeForm.setAlwaysOnTop(true);
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
        getPanel().resume();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        getWebcam().close();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        logger.debug("windowClosing");
        getWebcam().close();
        //getTmpImageDir().delete();
        SSNScheduleListForm sSNScheduleListForm = null;
        String destinationFolder = "";
        String selectedFolder = "";
        if(getTmpImageDir().listFiles().length > 0){
            try{
                sSNScheduleListForm = getScheduledTagMediaInfo();
            }catch(SQLException ee){
                logger.error(ee.getMessage());
            }
            if(sSNScheduleListForm !=null){
                destinationFolder =SSNHelper.getSsnHiveDirPath() + sSNScheduleListForm.getSsnAlbum() ;

               SSNHelper.lastAlbum = SSNHelper.getAlbumNameFromPath(selectedFolder);
                selectedFolder  =   destinationFolder+File.separator;
                this.homeForm.ssnFileExplorer.m_display.setText(destinationFolder);

            }else{
                selectedFolder = this.homeForm.ssnFileExplorer.m_display.getText();
                destinationFolder = this.homeForm.ssnFileExplorer.m_display.getText() + File.separator;
            }
            selectedFolder = selectedFolder.replaceAll(File.separator+File.separator, "&&");
            String str[] = selectedFolder.split("&&");
            String folderName = "";
            if(str.length>0){
                folderName = str[str.length-1];
             //   logger.debug("folder Name = "+folderName);
            }

            if(selectedFolder.equals("") || folderName.equalsIgnoreCase(SSNConstants.SSN_HIVE_DIRECTORY)){
                deleteDir(getTmpImageDir(),SSNHelper.getSsnDefaultDirPath());
                this.homeForm.ssnFileExplorer.m_display.setText(SSNHelper.getSsnDefaultDirPath());
                 SSNHelper.lastAlbum = "OurHive";
            }else{
                deleteDir(getTmpImageDir(),destinationFolder);
            }

            refreshImagePanel();
            if(homeForm.getSsnMicrophoneCamThread()!=null){
                homeForm.getSsnMicrophoneCamThread().stop();
                homeForm.setSsnMicrophoneCamThread(null);
            }
        }
    }
    
    /*
        This method move all the file from @dirPath to destination path
        and delete the files in @dirPath
    */
    public void deleteDir(File srcDirPath, String destDirPath){
        String files[] = srcDirPath.list();
        
        	   for (String srcFile : files) {
                       
                       srcFile = getTmpImageDir() + File.separator + srcFile;
        	      //construct the file structure
        	      File fileDelete = new File( srcFile);
                      String destFile = destDirPath +File.separator + fileDelete.getName();
                      movefile( srcFile, destFile);
        	      fileDelete.delete();
        	   }
    }
    
    public void movefile(String srcFile, String destFile) {
        // SSNScheduleListForm mediaInfo=null; 
        try {
            /*   
             The media info of destFile is to be written in db and exif.
            
             */
            SSNScheduleListForm mediaInfo = getScheduledTagMediaInfo();

            File afile = new File(srcFile);
            File destinationFile    =   new File(destFile);
//            if (mediaInfo != null) {
//                destFile = SSNHelper.getSsnHiveDirPath() + mediaInfo.getSsnAlbum() + File.separator + afile.getName();
//            }
            boolean flag = afile.renameTo(destinationFile);
            if (!flag) {
                logger.error(afile + " couldn't be moved successfully to " + destFile);
            }

           // Schedule a tag functionality from here on; nothing to do with normal webcam 
            // capture event.
            if (mediaInfo != null && mediaInfo.getSsnImagePrefix() != null) {

                String type = afile.getName().substring(new File(destFile).getName().lastIndexOf(".") + 1, afile.getName().length());
                String fileName = afile.getName();

                String subString = fileName.substring(0, fileName.indexOf("_"));
                String f1 = SSNHelper.getSsnHiveDirPath() + mediaInfo.getSsnAlbum() + File.separator + fileName;
                String f2 = SSNHelper.getSsnHiveDirPath() + mediaInfo.getSsnAlbum() + File.separator + (afile.getName().replace(subString, mediaInfo.getSsnImagePrefix()));

                if (insertMetaDataInfo(afile.getName().replace(subString, mediaInfo.getSsnImagePrefix()), mediaInfo, type)) {
                    if(Arrays.asList(SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED).contains(type.toUpperCase())){
                        SSMMediaGalleryPanel.writeVideoMetadata(destinationFile,mediaInfo.getSsnTitle(),mediaInfo.getSsnkeyWords());
                    }else{
                        SSMMediaGalleryPanel.writeImageMetaData(new File(destFile), mediaInfo.getSsnTitle(), mediaInfo.getSsnkeyWords(), mediaInfo.getSsnComent(), mediaInfo.getSsnLocation(), mediaInfo.getSsnAlbum(), "", String.valueOf(mediaInfo.getSsnRatings()), mediaInfo.getSsnPhotoGrapher());
                    }
                    
                    File fSource = new File(f1);
                    File fDesc = new File(f2);
                    fSource.renameTo(fDesc);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public SSNScheduleListForm getScheduledTagMediaInfo() throws SQLException{
        Date currentDate= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        logger.debug("date:::"+sdf.format(currentDate));
        SSNScheduleListForm sSNScheduleListForm=SSNDao.getScheduleMedia(sdf.format(currentDate));
       // SSNScheduleListForm sSNScheduleListForm=SSNDao.getScheduleMedia("05-06-2014");
        return sSNScheduleListForm;
    
    }

    boolean insertMetaDataInfo(String newDestFile,SSNScheduleListForm mediaInfo,String type){
        try {
            return SSNDao.insertScheduledTagMetaData(newDestFile,mediaInfo,type);
        } catch (SQLException ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return false;
    
    }

    @Override
    public void windowOpened(WindowEvent e) {
        getPanel().resume();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        getPanel().pause();
        //homeForm.setAlwaysOnTop(false); 
        this.setAlwaysOnTop(false);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        logger.info("webcam viewer resumed");
        getPanel().resume();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        logger.info("webcam viewer paused");
        this.setAlwaysOnTop(false);
        getPanel().pause();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //logger.error(String.format("Exception in thread %s", t.getName())); 
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() != getWebcam()) {
            if (getWebcam() != null) {

                getPanel().stop();

                remove(getPanel());

                getWebcam().removeWebcamListener(this);
                getWebcam().close();

                setWebcam((Webcam) e.getItem());
                getWebcam().setViewSize(WebcamResolution.VGA.getSize());
                getWebcam().addWebcamListener(this);

                logger.info("selected " + getWebcam().getName());

                setPanel(new WebcamPanel(getWebcam(), false));

                add(getPanel(), BorderLayout.CENTER);
                pack();

                Thread t = new Thread() {

                    @Override
                    public void run() {
                        getPanel().start();
                        setFaces(getDetector().detectFaces(ImageUtilities.createFImage(getWebcam().getImage())));
                    }
                };
                t.setName("example-stoper");
                t.setDaemon(true);
                t.setUncaughtExceptionHandler(this);
                t.start();
            }
        }
    }

    /**
     * @return the painter
     */
    public WebcamPanel.Painter getPainter() {
        return painter;
    }

    /**
     * @param painter the painter to set
     */
    public void setPainter(WebcamPanel.Painter painter) {
        this.painter = painter;
    }

    /**
     * @return the faces
     */
    public List<DetectedFace> getFaces() {
        return faces;
    }

    /**
     * @param faces the faces to set
     */
    public void setFaces(List<DetectedFace> faces) {
        this.faces = faces;
    }

    /**
     * @return the webcam
     */
    public Webcam getWebcam() {
        return webcam;
    }

    /**
     * @param webcam the webcam to set
     */
    public void setWebcam(Webcam webcam) {
        this.webcam = webcam;
    }

    /**
     * @return the panel
     */
    public WebcamPanel getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(WebcamPanel panel) {
        this.panel = panel;
    }

    /**
     * @return the picker
     */
    public WebcamPicker getPicker() {
        return picker;
    }

    /**
     * @param picker the picker to set
     */
    public void setPicker(WebcamPicker picker) {
        this.picker = picker;
    }

    /**
     * @return the detector
     */
    public static HaarCascadeDetector getDetector() {
        return detector;
    }

    /** 
     * @param aDetector the detector to set
     */
    public static void setDetector(HaarCascadeDetector aDetector) {
        detector = aDetector;
    }
    
    /**
     * @return the settingPanel
     */
    public JPanel getSettingPanel() {
        return settingPanel;
    }
    

    /**
     * @param settingPanel the settingPanel to set
     */
    public void setSettingPanel(JPanel settingPanel) {
        this.settingPanel = settingPanel;
    }

    /**
     * @return the functionPanel
     */
    public SSNWebcamFunctionPanel getFunctionPanel() {
        return (SSNWebcamFunctionPanel) functionPanel;
    }

    /**
     * @param functionPanel the functionPanel to set
     */
    public void setFunctionPanel(JPanel functionPanel) {
        this.functionPanel = functionPanel;
    }
    


    /**
     * @return the webcamTollbarLbl
     */
    public JLabel getWebcamTollbarLbl() {
        return webcamTollbarLbl;
    }

    /**
     * @param webcamTollbarLbl the webcamTollbarLbl to set
     */
    public void setWebcamTollbarLbl(JLabel webcamTollbarLbl) {
        this.webcamTollbarLbl = webcamTollbarLbl;
    }

    public File getTmpImageDir() {
        return tmpImageDir;
    }

    public void setTmpImageDir(File tmpImageDir) {
        this.tmpImageDir = tmpImageDir;
    }

    public void refreshImagePanel(){
        String ssnDestPath = this.homeForm.ssnFileExplorer.m_display.getText();
        try {
            if (ssnDestPath != null && !(ssnDestPath.trim().isEmpty()) && !ssnDestPath.trim().equalsIgnoreCase(SSNHelper.getSsnHiveDirPath())) {
                
                // BufferedImage image = ImageIO.read(file);
                try {
                    SSNGalleryHelper contentPane = new SSNGalleryHelper(ssnDestPath, this.homeForm, "ALL");
                    contentPane.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

                    this.homeForm.getSsnHomeCenterPanel().removeAll();
                    this.homeForm.getSsnHomeCenterMainPanel().removeAll();
                    //contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    //this.getHomeForm().getSsnHomeCenterPanel().add(contentPane);
                    this.homeForm.getSsnHomeCenterPanel().add(this.homeForm.getScrollPane(contentPane, SSNHelper.getAlbumNameFromPath(this.homeForm.ssnFileExplorer.m_display.getText())));

                    this.homeForm.getSsnHomeCenterMainPanel().add(this.homeForm.getSortPanel("Date", false, SSNHelper.getAlbumNameFromPath(this.homeForm.ssnFileExplorer.m_display.getText())), BorderLayout.NORTH);
                    this.homeForm.getSsnHomeCenterMainPanel().add(this.homeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
                    
                    //this.homeForm.getSsnHomeRightPanel().removeAll();
                    //this.homeForm.getSsnHomeRightMainPanel().remove(this.homeForm.getSsnHomeRightPanel());
                    
                    SSNDirSelectionListener dirSelectionListener = new  SSNDirSelectionListener();
                    dirSelectionListener.setForm(this.homeForm);
                    SSNDirSelectionListener.setdT(0);
                    SSNDirSelectionListener.setiT(0);
                    SSNGalleryMetaData gmp = dirSelectionListener.getSSNMediaFolderProperties(ssnDestPath.trim());
                    
                    SSMMediaGalleryPanel.populateMediaFolderPanel(gmp);
                    
                    
                    this.homeForm.revalidate();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }else{
                SSNDirSelectionListener dirSelectionListener = new  SSNDirSelectionListener();
                dirSelectionListener.setForm(this.homeForm);
                SSNDirSelectionListener.setdT(0);
                SSNDirSelectionListener.setiT(0);
                SSNGalleryMetaData gmp = dirSelectionListener.getSSNMediaFolderProperties(SSNHelper.getSsnHiveDirPath());

                SSMMediaGalleryPanel.populateMediaFolderPanel(gmp);


                this.homeForm.revalidate();
            } 

        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
