package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNDirSelectionListener;
import com.ssn.listener.SSNHiveAlbumSelectionListner;
import com.ssn.model.SSNHomeModel;
import com.ssn.model.SSNSocialModel;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import com.ssn.ui.custom.component.SSMMediaGalleryPanel;
import com.ssn.ui.custom.component.SSNBubbleBorder;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNHorizontalScrollbarUI;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNMenuBar;
import com.ssn.ui.custom.component.SSNMyScrollbarUI;
import com.ssn.ui.custom.component.SSNToolBar;
import com.ssn.ws.rest.response.SSNLoginResponse;
import edu.cmu.sphinx.frontend.util.Microphone;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;

/**
 *
 * @author vkvarma
 */
public class SSNHomeForm extends JFrame {


    private static final long serialVersionUID = 1L;
    private JMenuBar ssnHomeMenuBar = null;
    private JToolBar ssnHomeToolBar = null;
    private SSNHomeController homeController = null;
    private SSNLoginForm loginForm = null;
    public SSNFileExplorer ssnFileExplorer = null;
    private JPanel ssnHomeLeftPanel = null;
    private JPanel ssnHomeCenterPanel = null;
    private JPanel ssnHomeRightPanel = null;
    private JPanel ssnHomeCenterMainPanel = null;
    private JPanel ssnHomeCenterSortPanel = null;
    private JPanel ssnHomeLeftMainPanel = null;
    private JPanel ssnHomeLeftDrivePanel = null;
    private JPanel ssnHomeLeftDriveMainPanel = null;
    private JPanel northSouthPanel = null;
    private JPanel ssnHomeCenterButtonPanel = null;
    private JPanel ssnImagePanel = null;
    private JSplitPane splitPane;
    private JPanel ssnHomeRightMainPanel = null;
    private JCheckBox createAlbumChkBx = null;
    public ArrayList<Component> isolatedComponents = new ArrayList<Component>();
    private SSNLoginResponse loginResponse = null;
    private SSNHomeModel homeModel = null;
    private Set<String> fileNamesToBeDeleted = new HashSet<String>();

    private Thread ssnMicrophoneCamThread = null;
    private Microphone microphone = null;
    private AccessGrant facebookAccessGrant = null;
    private AccessGrant instagramAccessGrant = null;
    private String mediaToShow = "ALL";

    private SSNSocialModel socialModel;
    private File[] currentGallery = null;
    private File currentSelectedFile = null;
    private File currentFile = null;
    private BufferedImage currentImage = null;
    private BufferedImage currentRotatedImage = null;
    private SSNGalleryHelper ssnGalleryHelper = null;
    private Map<String, Integer> ratingMap = new HashMap<String, Integer>();
    private List<JCheckBox> allBoxes = new ArrayList<JCheckBox>();
    private List<JPanel> allChkBoxPanel =   new ArrayList<JPanel>();
    private JTextArea ssnMultipleFileNameAdd   =   null;
    private File[] multipleSelectedFiles = null;
    private JPanel welcomeImagePanel    = null;
    private JTree hiveTree;
    private JTree facebookTree;
    private JTree instagramTree;

    private int rotateAngleMultiple = 0;
    private double zoomIn = 1;
    static Logger log = Logger.getLogger(SSNHomeForm.class.getName());
    
    private JPanel treePanel=null;
    
    private JPanel scheduletagTreepanel=null;
    
    private SSNScheduleTagPanelForm ssnScheduleTagPanelForm=null;
    
    private SSNIconTextField ssnCreateAlbumName   = null;
    private JLabel ssnUploadFiles = null;
    private String ssnGalleryMediaPath;
   
    private boolean loggedInFromFaceBook;
    private boolean isSocialSearched;
    
    private boolean loggedInFromInstagram;
    private boolean instagramSearched;
    private JXCollapsiblePane leftColapsablePane;
    private boolean checkMultiSelection;
    private JLabel lblProcessingImage = null;
    private JPanel searchPanel=null;
    private              JTextField     searchMediaTextField    = null;
    private              ButtonGroup          searchOptionButtonGroup = null;
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNHomeForm.class);
    private SSNDirSelectionListener ssnDirSelectionListener ;
    JPanel thumbnailImagePanel = null;
    private SSNHiveAlbumSelectionListner    ssnHiveAlbumSelectionListner  = null;  
    //schedule a tag
    public static int tagId = 0;
    public static int subTagId = 0;

    public SSNHomeForm(SSNLoginForm loginForm, SSNLoginResponse loginResponse) {
        super("Our Hive");
        try{
            SSNHelper.createAndloadSSNDirs();
            this.setLoginForm(loginForm);
            this.setLoginResponse(loginResponse);
          
            this.initHomeForm();
            this.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            this.addWindowListener(this.getHomeController());
            setFrameSize();
            
        }catch(Exception ex){
               logger.error(ex);
        }
    }


    public SSNHomeForm(SSNLoginForm loginForm, SSNLoginResponse loginResponse,
            SSNSocialModel model, AccessGrant accessGrant, String oAuthProvider) {
        super("Our Hive");
        try{
            SSNHelper.createAndloadSSNDirs();
            this.setLoginForm(loginForm);
            this.setLoginResponse(loginResponse);
            this.setSocialModel(model);
           
            if(loginResponse != null){
                if (oAuthProvider != null && oAuthProvider.equals("Facebook")) {
                    this.setFacebookAccessGrant(accessGrant);
                    this.setLoggedInFromFaceBook(true);
                } else if (oAuthProvider != null && oAuthProvider.equals("Instagram")) {
                    this.setInstagramAccessGrant(accessGrant);
                    this.setLoggedInFromInstagram(true);
                }
            }

            this.initHomeForm();
            this.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
         
            setFrameSize();
             
        }catch(Exception ex){
            logger.error(ex);
        }
    }

    public SSNHomeForm(SSNLoginForm loginForm, SSNLoginResponse loginResponse,
            SSNSocialModel model, OAuthToken oAuthToken) {
        super("Our Hive");
        try{
            SSNHelper.createAndloadSSNDirs();
            this.setLoginForm(loginForm);
            this.setLoginResponse(loginResponse);
            this.setSocialModel(model);
            this.setTwitterOAuthToken(oAuthToken);
            this.initHomeForm();
            this.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
             
            setFrameSize();
           
        }catch(Exception ee){
            logger.error(ee);
        }
    }

    public void initHomeForm() {
        
        this.setHomeController(new SSNHomeController(this, this.getLoginForm()));
        this.setHomeModel(new SSNHomeModel(this, this.getHomeController()));
        this.getHomeController().setHomeModel(this.getHomeModel());
        ssnFileExplorer = new SSNFileExplorer(this);
        this.initHomeFormGUIComponents();
        this.renderHomeFrame();
        setFrameSize();
        
    }

     private void initHomeFormGUIComponents() {
    
        this.initHomeToolBar();
        this.initSearchPanel();
        this.initHomePanels();
    }
    
    private void initSearchPanel(){
        JLabel searchMediaIcon = new JLabel(new ImageIcon(getClass().getResource("/icon/search-normal.png")));
        searchMediaIcon.setSize(10,10);
        searchMediaIcon.setName("homeMediaSearch");
        searchMediaIcon.addMouseListener(this.getHomeController()); 
        
        this.add(Box.createHorizontalStrut(5)); 
        JLabel searchVoiceMediaStart = new JLabel(new ImageIcon(getClass().getResource("/icon/voice-search-but-normal.gif")));
        searchVoiceMediaStart.setSize(32,32);
        searchVoiceMediaStart.setName("searchVoiceMedia");
        searchVoiceMediaStart.addMouseListener(this.getHomeController()); 
     
        

        setSearchMediaTextField(new SSNIconTextField("","    What memories are you looking for? Search by keyword here!","searchMedia"));
        getSearchMediaTextField().setName("searchMedia");
        getSearchMediaTextField().setColumns(12); 
        getSearchMediaTextField().setSize(380,100);
        
        getSearchMediaTextField().setHorizontalAlignment(JTextField.CENTER);
        // it will take the color of its parent panel background
        getSearchMediaTextField().setBackground(new Color(32,47,54,0));
        getSearchMediaTextField().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        getSearchMediaTextField().setBorder(BorderFactory.createEmptyBorder());
        getSearchMediaTextField().setFont(new Font("open sans",Font.BOLD,14));
        getSearchMediaTextField().addKeyListener(this.getHomeController());
        
        JRadioButton localSearch = new JRadioButton("Local Search");
        JRadioButton cloudSearch = new JRadioButton("Hive Search");
        ButtonGroup buttonGroup = new ButtonGroup();
        localSearch.setSelected(true);
        buttonGroup.add(localSearch);
        buttonGroup.add(cloudSearch);
        this.setSearchOptionButtonGroup(buttonGroup);
       cloudSearch.setFocusable(false);
        
        
        JPanel radioPanel = new JPanel(new BorderLayout());
        radioPanel.add(localSearch, BorderLayout.NORTH);
        radioPanel.add(cloudSearch, BorderLayout.SOUTH);
        radioPanel.setBackground(new Color(255,215,0,1));
        radioPanel.setForeground(SSNConstants.SSN_FONT_COLOR);
        radioPanel.setFont(new Font("open sans",Font.PLAIN,12));
        radioPanel.setOpaque(false);
    
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        
      
        panel2.add(searchMediaIcon,BorderLayout.NORTH);
        panel3.add(searchVoiceMediaStart,BorderLayout.NORTH);
        
        panel2.setOpaque(false);
        panel3.setOpaque(false);
        
    
        panel2.setBackground(new Color(223,223,223,0));
        panel3.setBackground(new Color(223,223,223,0));
            
        try{
             
             // creating panel with textbox and buttons
            URL img = getClass().getResource("/images/search_bar.png");
            BufferedImage image = ImageIO.read(img);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            JPanel jPanel = new CustomImagePanel(background);
            
            GridBagLayout layout = new GridBagLayout();
            
            jPanel.setLayout(layout);        
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(2,5,10,5);
            gbc.ipadx = 475;
            gbc.gridx = 0;
            gbc.gridy = 0;
            jPanel.add(getSearchMediaTextField(), gbc); 
            
            gbc.ipadx = 1;
            gbc.gridx = 1;
            gbc.gridy = 0;
            jPanel.add(panel2, gbc); 
            
        
            gbc.gridx = 2;
            gbc.gridy = 0;
            jPanel.add(panel3, gbc); 
            
            // make panel border transparent
            jPanel.setOpaque(false);
            
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();                
            int width = (int) dim.getWidth();
                
           
            URL img1 = getClass().getResource("/images/search_bar_bg.png");
            BufferedImage image1 = ImageIO.read(img1);
            Image background1 = image1.getScaledInstance(width, image1.getHeight()+60, Image.SCALE_SMOOTH);
            this.setSearchPanel(new SSNImagePanel(background1));
            this.getSearchPanel().setForeground(SSNConstants.SSN_FONT_COLOR);
            this.getSearchPanel().setFont(new Font("Arial",Font.PLAIN,12));
            jPanel.setBorder(null);
        
            GridBagLayout layout1 = new GridBagLayout();
            
            this.getSearchPanel().setLayout(layout1);        
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.fill = GridBagConstraints.HORIZONTAL;
          
        
        this.getSearchPanel().setBackground(new Color(32,47,54,0));
        
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        JPanel blankPanel = new JPanel();
        blankPanel.setBackground(new Color(32,47,54,0));
        this.getSearchPanel().add(blankPanel, gbc1);
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        this.getSearchPanel().add(jPanel, gbc1);
        
        gbc1.insets = new Insets(2,5,10,5);
        gbc1.gridx = 1;
        gbc1.gridy = 1;
        gbc1.fill = 20;
        this.getSearchPanel().add(radioPanel, gbc1);
            
        }catch(IOException e){
            log.error(e);
        }
        
        
      
        
        
    }



    private void initHomeToolBar() {
        Map<String, String> toolBarItemsMap = new LinkedHashMap<String, String>();
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_HOME, "<B><>DESKTOP MEDIA|/icon/white_icon/home.png|home|Desktop Media");
        toolBarItemsMap.put("Hive", "<B><>HIVE MEDIA|/icon/view-hive-icon.png|hive|Hive Media");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_CREATE_ALBUM, "<B>&nbsp;CREATE &nbsp;ALBUM|/icon/create-album-normal.png|createAlbum|Create Album");

        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_VIEW_ALL_ALBUM, "<B>&nbsp;VIEW ALL &nbsp;MEDIA|/icon/view-albums-normal.png|viewAllAlbum|View All Media");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_SLIDE_SHOW, "<B>&nbsp;SLIDE &nbsp;SHOW|/icon/slideshow-normal.png|startSlideShow|Start Slide Show");

        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_UPLOAD_PHOTO, "<B>&nbsp;IMPORT&nbsp;MEDIA|/icon/import_media.png|uploadPhoto|Import Media");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_SYNC_MEDIA, "<B>&nbsp;UPLOAD&nbsp;TO&nbsp;HIVE|/icon/upload-normal.png|uploadMedia|Upload To Hive");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_OPEN_CAMERA, "<B>&nbsp;CAPTURE &nbsp;MEDIA|/icon/camera-normal.png|openCamera|Capture Media");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_SCHEDULE_TAG, "<B>&nbsp;SCHEDULE&nbsp;&nbsp;A &nbsp;TAG|/icon/schedule-active.png|scheduleTag|Schedule A Tag");
        toolBarItemsMap.put(SSNConstants.SSN_TOOLBAR_ALL_UNTAGGED, "<B>TAG &nbsp;UNTAGGED &nbsp;MEDIA|/icon/tagged-untagged-media.png|AllUntagged|Tag Untagged Media");

        
        this.setSsnHomeToolBar(new SSNToolBar(toolBarItemsMap, this.getLoginForm(), this, this.getHomeController()));
        this.getSsnHomeToolBar().setMargin(new Insets(13, 5, 13, 20));
        this.getSsnHomeToolBar().setRollover(false);
        this.getSsnHomeToolBar().setFloatable(false);

        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 20, 10);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_TOOL_BAR_BORDER_BLACK_COLOR);
        this.getSsnHomeToolBar().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

        
    }

    public void initHomePanels() {
        try {
            this.setFocusable(true);
            this.setSsnHomeLeftDriveMainPanel(new JPanel());
            this.setSsnHomeLeftMainPanel(new JPanel(new BorderLayout()));
            this.getSsnHomeLeftMainPanel().setPreferredSize(new Dimension(215, 500));

            this.setSsnHomeLeftDrivePanel(new JPanel(new GridLayout(1, 3)));
            this.getSsnHomeLeftDrivePanel().setPreferredSize(new Dimension(210, 38));

          
            this.setSsnHomeLeftPanel(ssnFileExplorer);
            
            Dimension dim   =   Toolkit.getDefaultToolkit().getScreenSize();
            int leftPanelwidth = (dim.width*15)/100;
            leftPanelwidth = leftPanelwidth< 209? 209 : leftPanelwidth;
            this.getSsnHomeLeftPanel().setPreferredSize(new Dimension(leftPanelwidth, 20));
            
            this.getSsnHomeLeftPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
           
            this.getSsnHomeLeftMainPanel().add(this.getSsnHomeLeftPanel(), BorderLayout.CENTER);
            this.getSsnHomeLeftMainPanel().setBackground(Color.red);
            this.getSsnHomeLeftMainPanel().revalidate();
            JPanel northSouthPanel = new JPanel(new BorderLayout());
            northSouthPanel.setSize(200, 10);

            northSouthPanel.setBorder((BorderFactory.createEmptyBorder(0, 6, 10, 12)));
            northSouthPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            northSouthPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/left-panne-drivel.jpg")), SwingConstants.HORIZONTAL), BorderLayout.WEST);

            
            BoxLayout boxLayout = new BoxLayout(this.getSsnHomeLeftDriveMainPanel(), BoxLayout.PAGE_AXIS);
            this.getSsnHomeLeftDriveMainPanel().setLayout(boxLayout);
            this.getSsnHomeLeftDriveMainPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            JPanel drivePanel = new JPanel(new BorderLayout());
            drivePanel.add(this.getSsnHomeLeftDrivePanel(), BorderLayout.WEST);
            drivePanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            
            this.getSsnHomeLeftDriveMainPanel().add(drivePanel);
            this.getSsnHomeLeftDriveMainPanel().add(northSouthPanel);

            URL imgURL = getClass().getResource("/images/ssn-dashboard-bg.png");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            

            this.setSsnHomeCenterMainPanel(new JPanel(new BorderLayout()));
            this.getSsnHomeCenterMainPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            this.getSsnHomeCenterMainPanel().setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, SSNConstants.SSN_BLACK_BORDER_COLOR));
            

            
            this.setSsnHomeCenterPanel(new SSNImagePanel(background));
            this.getSsnHomeCenterPanel().setLayout(new BorderLayout());
            
            this.getSsnHomeCenterPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            this.getSsnHomeCenterPanel().add(Box.createVerticalGlue());
            
            this.getSsnHomeCenterMainPanel().add(this.getSsnHomeCenterPanel(), BorderLayout.NORTH);

            this.getSsnHomeCenterMainPanel().add(getWelcomePanelVerticalScroller(getWelcomePanel()));

            leftColapsablePane = new JXCollapsiblePane(JXCollapsiblePane.Direction.LEFT);
            setLeftCollapsablePaneButtons();
            this.add(this.getSsnHomeLeftMainPanel(),BorderLayout.WEST);

            
            JXCollapsiblePane cp = new JXCollapsiblePane(JXCollapsiblePane.Direction.LEFT);
            
            cp.setLayout(new BorderLayout());
            this.setSsnHomeRightPanel(new JPanel());
            this.getSsnHomeRightPanel().setLayout(new BorderLayout());
            
            int rightPanelwidth = (dim.width*19)/100;
            rightPanelwidth =   rightPanelwidth < 250 ? 250 : rightPanelwidth;
            
            this.getSsnHomeRightPanel().setPreferredSize(new Dimension(rightPanelwidth, 50));
            this.getSsnHomeRightPanel().setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR));
            this.getSsnHomeRightPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            cp.add(BorderLayout.CENTER, this.getSsnHomeRightPanel());

            Action toggleAction = cp.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
            
            toggleAction.putValue(JXCollapsiblePane.COLLAPSE_ICON, new ImageIcon(getClass().getResource("/images/hide.png")));
            toggleAction.putValue(JXCollapsiblePane.EXPAND_ICON, new ImageIcon(getClass().getResource("/images/hide.png")));
            JToggleButton toggleButt = new JToggleButton(toggleAction);

            
            toggleButt.setPreferredSize(new Dimension(15, toggleButt.getSize().height));
            Border paddingBorder1 = BorderFactory.createEmptyBorder(0, 1, 0, 0);
            Border border1 = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            toggleButt.setBorder(BorderFactory.createCompoundBorder(border1, paddingBorder1));
            toggleButt.setBackground(new Color(237, 237, 237,1));
            toggleButt.setContentAreaFilled(false);
            toggleButt.setFocusable(false);
            toggleButt.setOpaque(true);
            toggleButt.setText("");
            toggleButt.setToolTipText("Collapse/Expand");

            
            this.setSsnHomeRightMainPanel(new JPanel());
            this.getSsnHomeRightMainPanel().setLayout(new BorderLayout());
            
            this.getSsnHomeRightMainPanel().setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR));
            
            this.getSsnHomeRightMainPanel().add(toggleButt, BorderLayout.CENTER);
            this.getSsnHomeRightMainPanel().add(cp, BorderLayout.EAST);
            
            this.add(this.getSsnHomeRightMainPanel(), BorderLayout.EAST);
            this.add(this.getSsnHomeCenterMainPanel(),BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setLeftCollapsablePaneButtons(){
            leftColapsablePane.setLayout(new BorderLayout());
            leftColapsablePane.add(BorderLayout.CENTER, this.getSsnHomeLeftPanel());
            Action toggleActionLeft = leftColapsablePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION);
            
            toggleActionLeft.putValue(JXCollapsiblePane.COLLAPSE_ICON, new ImageIcon(getClass().getResource("/images/splitter.png")));
            toggleActionLeft.putValue(JXCollapsiblePane.EXPAND_ICON, new ImageIcon(getClass().getResource("/images/splitter.png")));
            toggleActionLeft.putValue(JXCollapsiblePane.COLLAPSE_ICON, new ImageIcon(getClass().getResource("/images/splitter.png")));
            JToggleButton toggleButtonLeft = new JToggleButton(toggleActionLeft);

            
            toggleButtonLeft.setPreferredSize(new Dimension(15, toggleButtonLeft.getSize().height));
            Border paddingBorder11 = BorderFactory.createEmptyBorder(0, 1, 0, 0);
            Border border11 = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            toggleButtonLeft.setBorder(BorderFactory.createCompoundBorder(border11, paddingBorder11));
            toggleButtonLeft.setBackground(new Color(0,0,0,1));
            toggleButtonLeft.setContentAreaFilled(false);
            toggleButtonLeft.setFocusable(false);
            toggleButtonLeft.setOpaque(true);
            toggleButtonLeft.setText("");
            toggleButtonLeft.setToolTipText("Collapse/Expand");

            
            this.setSsnHomeLeftMainPanel(new JPanel());
            this.getSsnHomeLeftMainPanel().setLayout(new BorderLayout());
           
          
            this.getSsnHomeLeftMainPanel().setBorder(BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR));
            this.getSsnHomeLeftMainPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            this.getSsnHomeLeftMainPanel().add(toggleButtonLeft, BorderLayout.CENTER);
            this.getSsnHomeLeftMainPanel().add(leftColapsablePane, BorderLayout.WEST);
    }
    public JPanel getWelcomePanel(){
        
        JPanel welcomePanel         =   new JPanel();
        welcomeImagePanel    =   new JPanel();
        JPanel panelAlbumForm       =   new JPanel(null);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimensionPanelAlbumForm = toolkit.getScreenSize();
        
        
        JLabel welcomeLabel = null;
        int screenWidth     =   (int)dimensionPanelAlbumForm.getWidth();
        int screenHeight    =   (int)dimensionPanelAlbumForm.getHeight();
        try{
            URL imgURL2             = getClass().getResource("/images/dashboard-welcome.png");
            BufferedImage image2    = ImageIO.read(imgURL2);
            welcomeLabel            = new JLabel(new ImageIcon(imgURL2));
            welcomeLabel.setPreferredSize(new Dimension(image2.getWidth(), image2.getHeight()));
        }catch(Exception ee){
            ee.printStackTrace();
        }
        
        panelAlbumForm.setPreferredSize(new Dimension(screenWidth, screenHeight/2));
        
        lblProcessingImage   =   new JLabel(new ImageIcon(getClass().getResource("/icon/buffer.png")), 0);
        lblProcessingImage.setBounds(screenWidth/2-70,0,120,50);

        final JLabel browseAndSelectFilesHereLabel = new JLabel("Browse and select files here or");
        browseAndSelectFilesHereLabel.setBounds(screenWidth/2-220,60,300,50);
        browseAndSelectFilesHereLabel.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        browseAndSelectFilesHereLabel.setBackground(new Color(0,0,0,1));
        browseAndSelectFilesHereLabel.setFont(new Font("open sans", Font.BOLD,19));
        browseAndSelectFilesHereLabel.setOpaque(true);
        
        final JLabel labelUploadMedia =   new JLabel(new ImageIcon(getClass().getResource("/icon/add_file_btn.png")), 0);
        labelUploadMedia.addMouseListener(homeController);
        labelUploadMedia.setName("addFiles");
        labelUploadMedia.setBounds(screenWidth/2+90,60,140,50);
        
        JLabel lblCreateNewAlbumLabel = new JLabel("CREATE NEW ALBUM FROM FILES?",SwingConstants.RIGHT);
        lblCreateNewAlbumLabel.setBounds(screenWidth/2-150,120,190,30);
        lblCreateNewAlbumLabel.setFont(new Font("open sans", Font.BOLD, 9));
        lblCreateNewAlbumLabel.setForeground(Color.WHITE);
        lblCreateNewAlbumLabel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        lblCreateNewAlbumLabel.setOpaque(true);
        
        createAlbumChkBx        =   new JCheckBox(" YES");
        createAlbumChkBx.setBounds(screenWidth/2+50,110,50,50);
        createAlbumChkBx.setFont(new Font("open sans", Font.BOLD,8));
        createAlbumChkBx.setForeground(Color.WHITE);
        
        
        
        ssnCreateAlbumName      =   new SSNIconTextField("", "Enter Album Name", "ssnCreateAlbumName");
        ssnCreateAlbumName.setForeground(Color.WHITE);
         ssnCreateAlbumName.setBorder(new SSNCustomBorder(true,new Color(241,240,238),ssnCreateAlbumName.getHeight(),ssnCreateAlbumName.getWidth()));
        ssnCreateAlbumName.setFont(new Font("open sans", Font.BOLD, 12));
        ssnCreateAlbumName.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        ssnCreateAlbumName.setBounds(screenWidth/2-190,160,400,50);
        ssnCreateAlbumName.setOpaque(true);
          ssnCreateAlbumName.setVisible(false);
        createAlbumChkBx.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox object   =   (JCheckBox)e.getSource();
                ssnCreateAlbumName.setText("");
                if(object.isSelected()){
                    ssnCreateAlbumName.setVisible(true);
                }else{
                    ssnCreateAlbumName.setVisible(false);
                }
            }
        });
        
        
        thumbnailImagePanel = new JPanel();
        thumbnailImagePanel.setBounds(screenWidth/2-190,230,400,80);
        thumbnailImagePanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        JScrollPane thumbnailScrollPane;
        
        thumbnailScrollPane = getThumbnailScroller(thumbnailImagePanel);
        thumbnailScrollPane.setBounds(screenWidth/2-190,230,400,70);
        ssnUploadFiles = new JLabel(new ImageIcon(getClass().getResource("/icon/upload_file_btn.png")), 0);
        ssnUploadFiles.setBounds(screenWidth/2-80,310,180,50);
        ssnUploadFiles.setName("uploadMultipleFiles");
        ssnUploadFiles.addMouseListener(homeController);
        welcomePanel.setPreferredSize(new Dimension(screenWidth-550,screenHeight));
        panelAlbumForm.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        welcomePanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        welcomeImagePanel.add(welcomeLabel);
        
       // panelAlbumForm.add(lblProcessingImage);
        panelAlbumForm.add(browseAndSelectFilesHereLabel);
        panelAlbumForm.add(labelUploadMedia);
        panelAlbumForm.add(lblCreateNewAlbumLabel);
        panelAlbumForm.add(createAlbumChkBx);
        panelAlbumForm.add(ssnCreateAlbumName);
        panelAlbumForm.add(thumbnailScrollPane);
        panelAlbumForm.add(ssnUploadFiles);
        
        welcomePanel.add(welcomeImagePanel);
        welcomeImagePanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        welcomePanel.add(panelAlbumForm);
        
        return welcomePanel;
    }
    
    public JPanel getCenterButtonPanel(Integer rating) {
        this.setSsnHomeCenterButtonPanel(new JPanel(new BorderLayout()));
        this.getSsnHomeCenterButtonPanel().setName("buttonPanel");
        this.getSsnHomeCenterButtonPanel().setPreferredSize(new Dimension(450, 39));
        this.getSsnHomeCenterButtonPanel().setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        
        JPanel leftPanel = new JPanel(new GridLayout(1, 3));
        leftPanel.setPreferredSize(new Dimension(180, 39));
        leftPanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        JLabel backLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/Back-normal.png")));
        backLabel.setName("back");
        backLabel.addMouseListener(this.getHomeController());
       
        leftPanel.add(backLabel);
        JPanel rotatePanel = new JPanel(new GridLayout(1, 2));
        rotatePanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
       
        JLabel rotateLeft = new JLabel(new ImageIcon(getClass().getResource("/icon/rotate-left-normal.png")));
        rotateLeft.setName("rotateLeft");
        rotateLeft.addMouseListener(this.getHomeController());
        JLabel rotateRight = new JLabel(new ImageIcon(getClass().getResource("/icon/rotate-right-normal.png")));
        rotateRight.setName("rotateRight");
        rotateRight.addMouseListener(this.getHomeController());
        rotatePanel.add(rotateLeft);
        rotatePanel.add(rotateRight);
        leftPanel.add(rotatePanel);
        JPanel zoomPanel = new JPanel(new GridLayout(1, 2));
        zoomPanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        JLabel zoomIn = new JLabel(new ImageIcon(getClass().getResource("/icon/zoomin-normal.png")));
       
        zoomIn.setName("zoomIn");
        zoomIn.addMouseListener(this.getHomeController());
        JLabel zoomOut = new JLabel(new ImageIcon(getClass().getResource("/icon/zoomout-normal.png")));
       
        zoomOut.setName("zoomOut");
        zoomOut.addMouseListener(this.getHomeController());
        zoomPanel.add(zoomIn);
        zoomPanel.add(zoomOut);
        leftPanel.add(zoomPanel);
        this.getSsnHomeCenterButtonPanel().add(leftPanel, BorderLayout.WEST);

        JPanel mainCentrePanel = new JPanel(new BorderLayout());
        JPanel centrePanel = new JPanel(new GridLayout(1, 2));
        int centreWidth = getSsnHomeCenterPanel().getWidth();
        int padding = (centreWidth - 430) / 2;
        Border paddingBorder = BorderFactory.createEmptyBorder(0, padding, 0, padding);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        centrePanel.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        centrePanel.setPreferredSize(new Dimension(90, 39));
        centrePanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        JLabel previous = new JLabel(new ImageIcon(getClass().getResource("/icon/previous-normal.png")));
        previous.setName("previous");
        int index = 0;
        for (int i = 0; i < this.getCurrentGallery().length; i++) {
            if (this.getCurrentGallery()[i].getAbsolutePath().equalsIgnoreCase(this.getCurrentFile().getAbsolutePath())) {
                index = i;
                break;
            }
        }
        if (index != 0) {
            previous.addMouseListener(this.getHomeController());
        }
        JLabel next = new JLabel(new ImageIcon(getClass().getResource("/icon/next-normal.png")));
        next.setName("next");
        if (index != this.getCurrentGallery().length - 1) {
            next.addMouseListener(this.getHomeController());
        }
        centrePanel.add(previous);
        centrePanel.add(next);
        mainCentrePanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        mainCentrePanel.add(centrePanel, BorderLayout.CENTER);
        this.getSsnHomeCenterButtonPanel().add(mainCentrePanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        rightPanel.setPreferredSize(new Dimension(200, 39));
        Border paddingBorder2 = BorderFactory.createEmptyBorder(0, 40, 0, 0);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder2));
        JPanel stars = new JPanel(new GridLayout(1, 5));
        stars.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);
        stars.setPreferredSize(new Dimension(70, 39));
        JLabel info = null;

        if (rating != null && rating > 0 && rating < 6) {
            for (int k = 1; k <= rating; k++) {
                info = new JLabel(new ImageIcon(getClass().getResource("/icon/star-rated.png")));
                stars.add(info);
            }
            for (int j = 1; j <= 5 - rating; j++) {
                info = new JLabel(new ImageIcon(getClass().getResource("/icon/img-nonrated.png")));
                stars.add(info);
            }
        } else {
            for (int k = 0; k < 5; k++) {
                info = new JLabel(new ImageIcon(getClass().getResource("/icon/img-nonrated.png")));
                stars.add(info);

            }
        }
        rightPanel.add(stars, BorderLayout.WEST);
        JPanel buttons = new JPanel(new GridLayout(1, 3));
        buttons.setPreferredSize(new Dimension(90, 39));
        buttons.setBackground(SSNConstants.SSN_IMAGE_TOOLBAR_BLACK_COLOR);

        JLabel tag = new JLabel(new ImageIcon(getClass().getResource("/icon/tag-normal.png")));
        tag.setToolTipText("tag media");
        buttons.add(tag);
        tag.setName("tagImage");
        tag.addMouseListener(this.getHomeController());

        JLabel share = new JLabel(new ImageIcon(getClass().getResource("/icon/img-share-normal.png")));
        share.setToolTipText("share media");
        buttons.add(share);
        share.setName("shareImage");
        share.addMouseListener(this.getHomeController());
        JLabel save = new JLabel(new ImageIcon(getClass().getResource("/icon/save-normal.png")));
        save.setToolTipText("save media");
        buttons.add(save);
        save.setName("saveRotatedImage");
        save.addMouseListener(this.getHomeController());
    
        rightPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        rightPanel.add(buttons, BorderLayout.EAST);

        this.getSsnHomeCenterButtonPanel().add(rightPanel, BorderLayout.EAST);
        return this.getSsnHomeCenterButtonPanel();

    }

    public JScrollPane getScrollPane(JPanel galleryPanel, String album) {
        JScrollPane scroller = null;

        try {
            scroller = new JScrollPane(galleryPanel);
            scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            
            double screenHeight  = dimension.getHeight();
            double screenWidth  = dimension.getWidth();
                    
            double centerPanelWidth =(screenWidth - (this.getSsnHomeLeftPanel().getWidth()+this.getSsnHomeRightPanel().getWidth()));
            double centerPanelHeight = screenHeight- (this.getSsnHomeToolBar().getHeight()+(this.getSearchPanel().getHeight()*2)) ;
            int noOfImagesInRow = (int)(centerPanelWidth/SSNConstants.SSN_IMAGE_THUMBNAIL_WIDTH);
           
            int fraction    =   this.getSsnGalleryHelper().getImgs().length%noOfImagesInRow;
            int totalRows = this.getSsnGalleryHelper().getImgs().length/noOfImagesInRow;
            totalRows =fraction==0?totalRows:totalRows+1 ;
            
            scroller.getVerticalScrollBar().setUI(new SSNMyScrollbarUI(centerPanelHeight));
            Border borderVer = BorderFactory.createEmptyBorder(0, 0, 0, 1);
            scroller.setBorder(borderVer);

            URL imgURL = getClass().getResource("/images/ssn-dashboard-bg.png");
            BufferedImage image = ImageIO.read(imgURL);
            
            this.setNorthSouthPanel(new SSNImagePanel(image));

            this.getNorthSouthPanel().setLayout(new BorderLayout());
           
            this.getNorthSouthPanel().setBorder(new EmptyBorder(7, 18, 0, 10));
            this.getNorthSouthPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            if(album.isEmpty())
            {
               album = SSNHelper.lastAlbum ;
            }
            JLabel albumName = new JLabel(album);
            
            SSNHelper.lastAlbum = album;
            if(SSNHelper.lastAlbum != null)
            {
                if (SSNHelper.lastAlbum.equals("All Media")) {
                    ssnFileExplorer.m_display.setText("viewAllAlbums");
                } else if (SSNHelper.lastAlbum.equals("OurHive Media")) {
                    ssnFileExplorer.m_display.setText("tagUnTaggedMedia");
                }
            }
            albumName.setToolTipText(album);
            // for search textField  change value
                        if(album != null && album.equalsIgnoreCase("Search Result"))
                            getSearchMediaTextField().setText(SSNHomeModel.text);
                        else
                        {
                            if(getSearchMediaTextField()instanceof SSNIconTextField)
                            {
                                SSNIconTextField textField = (SSNIconTextField)getSearchMediaTextField();
                                textField.setText("");
                                textField.setHint("    What memories are you looking for? Search by keyword here!");
                                repaint();
                            }
                        }
            albumName.setFont(new Font("open sans", Font.PLAIN, 12));
            albumName.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
            Border border = BorderFactory.createEmptyBorder(5, 0, 5, 0);
            albumName.setBorder(border);

            topPanel.add(albumName, BorderLayout.WEST);
            
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
           // panel.setPreferredSize(new Dimension(80, 30));
            
            //JLabel lblDeletSelectedMedia    =   new JLabel(new ImageIcon(getClass().getResource("/icon/delete_seleted_media.png")));
            
            // select all Checkbox
            JLabel selectAll = new JLabel("Select All");
            selectAll.setFont(new Font("open sans", Font.PLAIN, 12));
            selectAll.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
            selectAll.setBorder(border);
            
            final JCheckBox chk = new JCheckBox();
            chk.setName("selectAll");
            chk.addItemListener(new ItemListener() {
               
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (chk.isSelected()) {
                        setCheckMultiSelection(true);
                        for (JCheckBox ch : getAllBoxes()) {
                            ch.setSelected(true);
                            
                        }
                        for(JPanel panel : getAllChkBoxPanel()){
                            panel.setBackground(new Color(1,1,1,125));
                           //panel.setToolTipText("Hellooooooooooooooo");
                            panel.setVisible(true);
                            panel.repaint();
                            
                            
                        }
                        
                        
                        if(getFileNamesToBeDeleted()!=null && getCheckMultiSelection())
                           {
                             
                              getSsnHomeRightPanel().removeAll();
                              getSsnHomeRightPanel().add(SSMMediaGalleryPanel.populateMediaSummaryPanel(new File(""),SSNHomeForm.this), BorderLayout.NORTH);
                              getSsnHomeRightPanel().revalidate();
                       }
                        
                        
                         //code for single multiselection end
                        
                        
                        
                    } else {
                        setCheckMultiSelection(false);
                        for (JCheckBox ch : getAllBoxes()) {
                            ch.setSelected(false);
                        }
                        for(JPanel panel : getAllChkBoxPanel()){
                            panel.setVisible(false);
                        }
                    }
                }
            });
            
         // Create filter Menu bar on gallery center Panel
         JPanel filterPanel = new JPanel(new BorderLayout());
        //filterPanel.setPreferredSize(new Dimension(125, 20));
        filterPanel.setBackground(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
        Border paddingBorderFilter = BorderFactory.createEmptyBorder(0, 5, 3, 0);
        Border borderFilter = BorderFactory.createLineBorder(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(borderFilter, paddingBorderFilter));
        SSNMenuBar filterMenuBar = new SSNMenuBar();
        filterMenuBar.setBorder(null);
        
        JMenu filterMenu = new JMenu("Filter");
      
        
        filterMenu.setFont(new Font("open sans", Font.PLAIN,12));
        filterMenu.setBackground(SSNConstants.SSN_BUTTON_COLOR);
        filterMenu.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        Border paddingBorderMenu = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        Border borderFilterMenu = BorderFactory.createLineBorder(SSNConstants.SSN_BUTTON_BLACK_COLOR);
        filterMenuBar.setBorder(BorderFactory.createCompoundBorder(borderFilterMenu, paddingBorderMenu));
        
        JMenuItem all = new JMenuItem("All Media");
        all.setFont(new Font("open sans", Font.BOLD, 10));
        all.setActionCommand("ALL");
        all.addActionListener(homeController);
        all.setBackground(new Color(232,231,230));
        all.setForeground(new Color(134,132,133)); 
        
        JMenuItem img = new JMenuItem("Images");
        img.setFont(new Font("open sans", Font.BOLD, 10));
        img.setActionCommand("IMAGES");
        img.addActionListener(homeController);
        img.setBackground(new Color(232,231,230));
        img.setForeground(new Color(134,132,133)); 
        JMenuItem video = new JMenuItem("Videos");
        video.setFont(new Font("open sans", Font.BOLD, 10));
        video.setActionCommand("VIDEOS");
        video.addActionListener(homeController);
        video.setForeground(new Color(134,132,133)); 
        
        video.setBackground(new Color(232,231,230));
        
        filterMenu.add(all);
        filterMenu.addSeparator();
        filterMenu.add(img);
        filterMenu.addSeparator();
        filterMenu.add(video);
        filterMenuBar.add(filterMenu);
        
        filterPanel.add(filterMenuBar);
        
        selectAll.setVisible(true);
        chk.setVisible(true);
        
        if(SSNFileExplorer.selectedMedia.equals("hive")){
            selectAll.setVisible(false);
            chk.setVisible(false);
        }
        
       // add select all checkbox and filter menu on panel
       panel.add(selectAll);  
       panel.add(new JLabel(" "));
       panel.add(chk);
       panel.add(filterPanel);
       
        topPanel.add(panel, BorderLayout.EAST);
            
            this.getNorthSouthPanel().add(topPanel, BorderLayout.NORTH);
            JSeparator seperator = new JSeparator() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(new Color(226, 226, 226));
                    g.fillRect(0, 0, getWidth(), 1);
                }
            };
            this.getNorthSouthPanel().add(seperator, BorderLayout.SOUTH);
            this.getSsnHomeCenterPanel().add(this.getNorthSouthPanel(), BorderLayout.NORTH);

        } catch (IOException ex) {
            log.error(ex);
        }
        return scroller;
    }

    public JScrollPane getWelcomePanelVerticalScroller(JPanel welcomePanel){
        JScrollPane scroller    =   null;

        scroller    =   new JScrollPane(welcomePanel);
        
        scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        Dimension dimention     =   Toolkit.getDefaultToolkit().getScreenSize();
        
        int height  =   (int)(dimention.getHeight() -   this.getSsnHomeToolBar().getHeight()    +   this.getSearchPanel().getHeight());
        
        scroller.getVerticalScrollBar().setUI(new SSNMyScrollbarUI(height,false));
        
        return scroller;
    }
    
    public JScrollPane getThumbnailScroller(JPanel thumbnailPanel){
        JScrollPane scroller = null;
        
        scroller = new JScrollPane(thumbnailPanel);
        scroller.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroller.getHorizontalScrollBar().setUI(new SSNHorizontalScrollbarUI(10));
        
        return scroller;
    }


    public JPanel getSortPanel(String column, boolean direction, String albumName) {
        Set<String> buttonSet = new LinkedHashSet<String>();
        buttonSet.add("NAME");
        buttonSet.add("DATE");
        buttonSet.add("SIZE");
        
        JPanel sortPanel    =   new JPanel(new BorderLayout());
       
        this.setSsnHomeCenterSortPanel(sortPanel);
        this.getSsnHomeCenterSortPanel().setName("SortPanel");
        this.getSsnHomeCenterSortPanel().setPreferredSize(new Dimension(450, 39));
       
        this.getSsnHomeCenterSortPanel().setBackground(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
       
        buttonPanel.setBackground(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
         buttonPanel.setMinimumSize(new Dimension(130, 20));
        buttonPanel.setPreferredSize(new Dimension(300, 20));
        buttonPanel.setMaximumSize(new Dimension(300, 20));

        Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 1, 0);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnHomeCenterSortPanel().add(Box.createVerticalStrut(5), BorderLayout.NORTH);
        for (String button : buttonSet) {
            if (button.equalsIgnoreCase(column)) {
                if (direction) {
                    buttonPanel.add(getsortbutton(button, 1, true, true));
                } else {
                    buttonPanel.add(getsortbutton(button, 1, true, false));
                }
            } else {
                buttonPanel.add(getsortbutton(button, 1, false, false));
            }
        }

        
        this.getSsnHomeCenterSortPanel().add(buttonPanel, BorderLayout.WEST);
        
        
        
        this.getSsnHomeCenterSortPanel().add(getDesktopMedialPanel(),BorderLayout.EAST);
        /* Create delete and share button panel : end*/
       // this.getSsnHomeCenterSortPanel().add(filterPanel, JPanel.CENTER_ALIGNMENT);
        if(albumName.equals("viewAllAlbums"))
        {
           albumName="All Media"; 
        }
        else if(albumName.equals("instagramMedia"))
        {
           albumName="Instagram Media"; 
        }
        else if(albumName.equals("tagUnTaggedMedia"))
        {
           albumName="OurHive Media"; 
        }
        JLabel label = new JLabel(albumName);
        label.setFont(new Font("open sans", Font.BOLD, 11));
        label.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        Border paddingBorder2 = BorderFactory.createEmptyBorder(0, 0, 0, 10);
        Border border2 = BorderFactory.createLineBorder(SSNConstants.SSN_BUTTON_BLACK_COLOR);
        label.setBorder(BorderFactory.createCompoundBorder(border2, paddingBorder2));
        if(albumName.equals("viewAllAlbums")){
           albumName="All Media"; 
        }else if(albumName.equals("instagramMedia")){
           albumName="Instagram Media"; 
        }
        
        this.getSsnHomeCenterSortPanel().add(Box.createVerticalStrut(1), BorderLayout.SOUTH);
        return this.getSsnHomeCenterSortPanel();
    }
    
    private JPanel getDesktopMedialPanel()
    {
        JLabel downloadMediaToLibrary = new JLabel(new ImageIcon(getClass().getResource("/icon/download_selected_media_to_library.png")));
        downloadMediaToLibrary.setName("downloadMedia");
        downloadMediaToLibrary.addMouseListener(homeController);
        
        JLabel exportMediaLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/export-selected-media-to-device.png")));
        exportMediaLabel.setName("exportPhoto");
        exportMediaLabel.addMouseListener(homeController);
        
        /* Create delete and share button panel : start*/
        JLabel deleteMediaLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/delete-deactive.png")));
        deleteMediaLabel.setName("deletePhoto");
        deleteMediaLabel.addMouseListener(homeController);
        
        JLabel shareMediaLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/share-deactive.png")));
        shareMediaLabel.setName("share");        
        shareMediaLabel.addMouseListener(homeController);
        
        downloadMediaToLibrary.setVisible(false);
        exportMediaLabel.setVisible(true);
        deleteMediaLabel.setVisible(true);
        shareMediaLabel.setVisible(true);
        
        if(SSNFileExplorer.selectedMedia.equals("hive")){
            downloadMediaToLibrary.setVisible(true);
            exportMediaLabel.setVisible(false);
            deleteMediaLabel.setVisible(false);
            shareMediaLabel.setVisible(false);
        }
        JPanel deleteSharePanel = new JPanel();
        deleteSharePanel.add(downloadMediaToLibrary);
        deleteSharePanel.add(exportMediaLabel);
        deleteSharePanel.add(deleteMediaLabel);
        deleteSharePanel.add(shareMediaLabel);
       
        deleteSharePanel.setBackground(new Color(5,5,0,1));
        return deleteSharePanel;
    }

    private JPanel getsortbutton(String text, int width, boolean addDirection, boolean direction) {
        String directionName = "up";
        AbstractBorder brdr = new SSNBubbleBorder(SSNConstants.SSN_PANEL_BLACK_BARS_COLOR, 1, 1, 0);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        if (addDirection) {
            JLabel imgLabel = null;
            if (direction) {
                imgLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/up.png")));
            } else {
                imgLabel = new JLabel(new ImageIcon(getClass().getResource("/icon/down.png")));
                directionName = "down";
            }

            Border border = BorderFactory.createEmptyBorder(0, 0, 0, 8);
            imgLabel.setBorder(border);
            buttonPanel.add(imgLabel, BorderLayout.EAST);
        }
        buttonPanel.setName(text + directionName);
       // buttonPanel.setBorder(brdr);
        buttonPanel.setBackground(SSNConstants.SSN_BUTTON_BLACK_COLOR);
        buttonPanel.setPreferredSize(new Dimension(width, 20));
        buttonPanel.addMouseListener(this.getHomeController());
        JLabel label = new JLabel("<html>&nbsp;&nbsp;&nbsp;" + text + "</html>");
        label.setFont(new Font("open sans", Font.BOLD, 11));
        label.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        buttonPanel.add(label, BorderLayout.CENTER);
     
        return buttonPanel;

    }
    
    public void setFrameSize()
    {
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.setMinimumSize(new Dimension((int) width, winSize.height));
        this.setMaximumSize((new Dimension((int) width, winSize.height)));
        int w = this.getSize().width;
        int h = winSize.height;
        int x = (scrnSize.width - w) / 2;
        int y = (winSize.height - h) / 2;
        setResizable(true);
        this.setLocation(x, y-5);
    }

    private void renderHomeFrame() {
        try {
            this.setIconImage((new ImageIcon(getClass().getResource("/icon/ssn-minimized-icon.png"))).getImage());
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setFrameSize();
            this.setJMenuBar(this.getSsnHomeMenuBar());
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(this.getSsnHomeToolBar(), BorderLayout.NORTH);
            topPanel.add(this.getSearchPanel(), BorderLayout.SOUTH);
            this.add(topPanel, BorderLayout.NORTH  );
          
            this.isolatedComponents.add(this);
            this.setVisible(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the ssnHomeMenuBar
     */
    public JMenuBar getSsnHomeMenuBar() {
        return ssnHomeMenuBar;
    }

    /**
     * @param ssnHomeMenuBar the ssnHomeMenuBar to set
     */
    public void setSsnHomeMenuBar(JMenuBar ssnHomeMenuBar) {
        this.ssnHomeMenuBar = ssnHomeMenuBar;
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
     * @return the ssnHomeToolBar
     */
    public JToolBar getSsnHomeToolBar() {
        return ssnHomeToolBar;
    }

    /**
     * @param ssnHomeToolBar the ssnHomeToolBar to set
     */
    public void setSsnHomeToolBar(JToolBar ssnHomeToolBar) {
        this.ssnHomeToolBar = ssnHomeToolBar;
    }

    /**
     * @return the loginForm
     */
    public SSNLoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * @param loginForm the loginForm to set
     */
    public void setLoginForm(SSNLoginForm loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * @return the ssnHomeLeftPanel
     */
    public JPanel getSsnHomeLeftPanel() {
        return ssnHomeLeftPanel;
    }

    /**
     * @param ssnHomeLeftPanel the ssnHomeLeftPanel to set
     */
    public void setSsnHomeLeftPanel(JPanel ssnHomeLeftPanel) {
        this.ssnHomeLeftPanel = ssnHomeLeftPanel;
    }

    /**
     * @return the ssnHomeCenterPanel
     */
    public JPanel getSsnHomeCenterPanel() {
        return ssnHomeCenterPanel;
    }

    /**
     * @param ssnHomeCenterPanel the ssnHomeCenterPanel to set
     */
    public void setSsnHomeCenterPanel(JPanel ssnHomeCenterPanel) {
        this.ssnHomeCenterPanel = ssnHomeCenterPanel;
    }

    /**
     * @return the ssnHomeRightPanel
     */
    public JPanel getSsnHomeRightPanel() {
        return ssnHomeRightPanel;
    }

    /**
     * @param ssnHomeRightPanel the ssnHomeRightPanel to set
     */
    public void setSsnHomeRightPanel(JPanel ssnHomeRightPanel) {
        this.ssnHomeRightPanel = ssnHomeRightPanel;
    }

    /**
     * @return the loginResponse
     */
    public SSNLoginResponse getLoginResponse() {
        return loginResponse;
    }

    /**
     * @param loginResponse the loginResponse to set
     */
    public void setLoginResponse(SSNLoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    /**
     * @return the homeModel
     */
    public SSNHomeModel getHomeModel() {
        return homeModel;
    }

    /**
     * @param homeModel the homeModel to set
     */
    public void setHomeModel(SSNHomeModel homeModel) {
        this.homeModel = homeModel;
    }

    /**
     * @return the fileNamesToBeDeleted
     */
    public Set<String> getFileNamesToBeDeleted() {
        return fileNamesToBeDeleted;
    }

    public JXCollapsiblePane getLeftColapsablePane() {
        return leftColapsablePane;
    }

    public void setLeftColapsablePane(JXCollapsiblePane leftColapsablePane) {
        this.leftColapsablePane = leftColapsablePane;
    }

    /**
     * @param fileNamesToBeDeleted the fileNamesToBeDeleted to set
     */
    public void setFileNamesToBeDeleted(Set<String> fileNamesToBeDeleted) {
        this.fileNamesToBeDeleted = fileNamesToBeDeleted;
    }

    public JPanel getSsnHomeCenterMainPanel() {
        return ssnHomeCenterMainPanel;
    }

    public void setSsnHomeCenterMainPanel(JPanel ssnHomeCenterMainPanel) {
        this.ssnHomeCenterMainPanel = ssnHomeCenterMainPanel;
    }

    public JPanel getSsnHomeCenterSortPanel() {
        return ssnHomeCenterSortPanel;
    }

    public void setSsnHomeCenterSortPanel(JPanel ssnHomeCenterSortPanel) {
        this.ssnHomeCenterSortPanel = ssnHomeCenterSortPanel;
    }

    public JPanel getSsnHomeLeftMainPanel() {
        return ssnHomeLeftMainPanel;
    }

    public void setSsnHomeLeftMainPanel(JPanel ssnHomeLeftMainPanel) {
        this.ssnHomeLeftMainPanel = ssnHomeLeftMainPanel;
    }

    public JPanel getSsnHomeLeftDrivePanel() {
        return ssnHomeLeftDrivePanel;
    }

    public void setSsnHomeLeftDrivePanel(JPanel setSsnHomeLeftDrivePanel) {
        this.ssnHomeLeftDrivePanel = setSsnHomeLeftDrivePanel;
    }

    public JPanel getSsnHomeLeftDriveMainPanel() {
        return ssnHomeLeftDriveMainPanel;
    }

    public void setSsnHomeLeftDriveMainPanel(JPanel ssnHomeLeftDriveMainPanel) {
        this.ssnHomeLeftDriveMainPanel = ssnHomeLeftDriveMainPanel;
    }

    /**
     * @return the ssnMicrophoneCamThread
     */
    public Thread getSsnMicrophoneCamThread() {
        return ssnMicrophoneCamThread;
    }

    /**
     * @param ssnMicrophoneCamThread the ssnMicrophoneCamThread to set
     */
    public void setSsnMicrophoneCamThread(Thread ssnMicrophoneCamThread) {
        this.ssnMicrophoneCamThread = ssnMicrophoneCamThread;
    }

    public Microphone getMicrophone() {
        return microphone;
    }

    public void setMicrophone(Microphone microphone) {
        this.microphone = microphone;
    }

    public AccessGrant getFacebookAccessGrant() {
        return facebookAccessGrant;
    }

    public void setFacebookAccessGrant(AccessGrant facebookAccessGrant) {
        this.facebookAccessGrant = facebookAccessGrant;
    }

    public OAuthToken getTwitterOAuthToken() {
        return twitterOAuthToken;
    }

    public void setTwitterOAuthToken(OAuthToken twitterOAuthToken) {
        this.twitterOAuthToken = twitterOAuthToken;
    }

    public SSNSocialModel getSocialModel() {
        return socialModel;
    }

    public void setSocialModel(SSNSocialModel socialModel) {
        this.socialModel = socialModel;
    }

    public JPanel getNorthSouthPanel() {
        return northSouthPanel;
    }

    public void setNorthSouthPanel(JPanel northSouthPanel) {
        this.northSouthPanel = northSouthPanel;
    }

    public JPanel getSsnHomeCenterButtonPanel() {
        return ssnHomeCenterButtonPanel;
    }

    public void setSsnHomeCenterButtonPanel(JPanel ssnHomeCenterButtonPanel) {
        this.ssnHomeCenterButtonPanel = ssnHomeCenterButtonPanel;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }
    private OAuthToken twitterOAuthToken = null;

    public File[] getCurrentGallery() {
        return currentGallery;
    }

    public void setCurrentGallery(File[] currentGallery) {
        this.currentGallery = currentGallery;
    }

    public SSNGalleryHelper getSsnGalleryHelper() {
        return ssnGalleryHelper;
    }

    public void setSsnGalleryHelper(SSNGalleryHelper ssnGalleryHelper) {
        this.ssnGalleryHelper = ssnGalleryHelper;
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(BufferedImage currentImage) {
       
        this.currentImage = currentImage;
    }

    public int getRotateAngleMultiple() {
        return rotateAngleMultiple;
    }

    public void setRotateAngleMultiple(int rotateAngleMultiple) {
        this.rotateAngleMultiple = rotateAngleMultiple;
    }

    public double getZoomIn() {
        return zoomIn;
    }

    public void setZoomIn(double zoomIn) {
        this.zoomIn = zoomIn;
    }

    public JTree getHiveTree() {
        return hiveTree;
    }

    public void setHiveTree(JTree hiveTree) {
        this.hiveTree = hiveTree;
    }

    public JTree getFacebookTree() {
        return facebookTree;
    }

    public void setFacebookTree(JTree facebookTree) {
        this.facebookTree = facebookTree;
    }

    public BufferedImage getCurrentRotatedImage() {
        return currentRotatedImage;
    }

    public void setCurrentRotatedImage(BufferedImage currentRotatedImage) {
        this.currentRotatedImage = currentRotatedImage;
    }

    public String getMediaToShow() {
        return mediaToShow;
    }

    public void setMediaToShow(String mediaToShow) {
        this.mediaToShow = mediaToShow;
    }

    public AccessGrant getInstagramAccessGrant() {
        return instagramAccessGrant;
    }

    public void setInstagramAccessGrant(AccessGrant instagramAccessGrant) {
        this.instagramAccessGrant = instagramAccessGrant;
    }

    public JTree getInstagramTree() {
        return instagramTree;
    }

    public void setInstagramTree(JTree instagramTree) {
        this.instagramTree = instagramTree;
    }

    public Map<String, Integer> getRatingMap() {
        return ratingMap;
    }

    public void setRatingMap(Map<String, Integer> ratingMap) {
        this.ratingMap = ratingMap;
    }

    public List<JCheckBox> getAllBoxes() {
        return allBoxes;
    }

    public void setAllBoxes(List<JCheckBox> allBoxes) {
        this.allBoxes = allBoxes;
    }

    public List<JPanel> getAllChkBoxPanel() {
        return allChkBoxPanel;
    }

    public void setAllChkBoxPanel(List<JPanel> allChkBoxPanel) {
        this.allChkBoxPanel = allChkBoxPanel;
    }

    
    public JPanel getSsnImagePanel() {
        return ssnImagePanel;
    }

    public void setSsnImagePanel(JPanel ssnImagePanel) {
        this.ssnImagePanel = ssnImagePanel;
    }

    public File getCurrentSelectedFile() {
        return currentSelectedFile;
    }

    public void setCurrentSelectedFile(File currentSelectedFile) {
        this.currentSelectedFile = currentSelectedFile;
    }

    /**
     * @return the ssnHomeRightMainPanel
     */
    public JPanel getSsnHomeRightMainPanel() {
        return ssnHomeRightMainPanel;
    }

    /**
     * @param ssnHomeRightMainPanel the ssnHomeRightMainPanel to set
     */
    public void setSsnHomeRightMainPanel(JPanel ssnHomeRightMainPanel) {
        this.ssnHomeRightMainPanel = ssnHomeRightMainPanel;
    }

    /**
     * @return the splitPane
     */
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    /**
     * @param splitPane the splitPane to set
     */
    public void setSplitPane(JSplitPane splitPane) {
        this.splitPane = splitPane;
    }

    /**
     * @return the treePanel
     */
    public JPanel getTreePanel() {
        return treePanel;
    }

    /**
     * @param treePanel the treePanel to set
     */
    public void setTreePanel(JPanel treePanel) {
        this.treePanel = treePanel;
    }

    /**
     * @return the scheduletagTreepanel
     */
    public JPanel getScheduletagTreepanel() {
        return scheduletagTreepanel;
    }

    /**
     * @param scheduletagTreepanel the scheduletagTreepanel to set
     */
    public void setScheduletagTreepanel(JPanel scheduletagTreepanel) {
        this.scheduletagTreepanel = scheduletagTreepanel;
    }

    /**
     * @return the ssnScheduleTagPanelForm
     */
    public SSNScheduleTagPanelForm getSsnScheduleTagPanelForm() {
        return ssnScheduleTagPanelForm;
    }

    /**
     * @param ssnScheduleTagPanelForm the ssnScheduleTagPanelForm to set
     */
    public void setSsnScheduleTagPanelForm(SSNScheduleTagPanelForm ssnScheduleTagPanelForm) {
        this.ssnScheduleTagPanelForm = ssnScheduleTagPanelForm;
    }

  

    /**
     * @return the ssnGalleryMediaPath
     */
    public String getSsnGalleryMediaPath() {
        return ssnGalleryMediaPath;
    }

    /**
     * @param ssnGalleryMediaPath the ssnGalleryMediaPath to set
     */
    public void setSsnGalleryMediaPath(String ssnGalleryMediaPath) {
        this.ssnGalleryMediaPath = ssnGalleryMediaPath;
    }

    public boolean isLoggedInFromFaceBook() {
        return loggedInFromFaceBook;
    }

    public void setLoggedInFromFaceBook(boolean loggedInFromFaceBook) {
        this.loggedInFromFaceBook = loggedInFromFaceBook;
    }

    public boolean isIsSocialSearched() {
        return isSocialSearched;
    }

    public void setIsSocialSearched(boolean isSocialSearched) {
        this.isSocialSearched = isSocialSearched;
    }

    public boolean isLoggedInFromInstagram() {
        return loggedInFromInstagram;
    }

    public void setLoggedInFromInstagram(boolean loggedInFromInstagram) {
        this.loggedInFromInstagram = loggedInFromInstagram;
    }

    public boolean isInstagramSearched() {
        return instagramSearched;
    }

    public void setInstagramSearched(boolean instagramSearched) {
        this.instagramSearched = instagramSearched;
    }

    /**
     * @return the checkMultiSelection
     */
    public boolean getCheckMultiSelection() {
        return checkMultiSelection;
    }

    /**
     * @param checkMultiSelection the checkMultiSelection to set
     */
    public void setCheckMultiSelection(boolean checkMultiSelection) {
        this.checkMultiSelection = checkMultiSelection;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    public void setSearchPanel(JPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

    public JTextField getSearchMediaTextField() {
        return searchMediaTextField;
    }

    public void setSearchMediaTextField(JTextField searchMediaTextField) {
        this.searchMediaTextField = searchMediaTextField;
    }

    public ButtonGroup getSearchOptionButtonGroup() {
        return searchOptionButtonGroup;
    }

    public void setSearchOptionButtonGroup(ButtonGroup searchOptionButtonGroup) {
        this.searchOptionButtonGroup = searchOptionButtonGroup;
    }

    public SSNDirSelectionListener getSsnDirSelectionListener() {
        return ssnDirSelectionListener;
    }

    public void setSsnDirSelectionListener(SSNDirSelectionListener ssnDirSelectionListener) {
        this.ssnDirSelectionListener = ssnDirSelectionListener;
    }

    public SSNIconTextField getSsnCreateAlbumName() {
        return ssnCreateAlbumName;
    }

    public void setSsnCreateAlbumName(SSNIconTextField ssnCreateAlbumName) {
        this.ssnCreateAlbumName = ssnCreateAlbumName;
    }

    public JLabel getSsnUploadFiles() {
        return ssnUploadFiles;
    }

    public void setSsnUploadFiles(JLabel ssnUploadFiles) {
        this.ssnUploadFiles = ssnUploadFiles;
    }

    public JLabel getLblProcessingImage() {
        return lblProcessingImage;
    }

    public void setLblProcessingImage(JLabel lblProcessingImage) {
        this.lblProcessingImage = lblProcessingImage;
    }

    public JTextArea getSsnMultipleFileNameAdd() {
        return ssnMultipleFileNameAdd;
    }

    public void setSsnMultipleFileNameAdd(JTextArea ssnMultipleFileNameAdd) {
        this.ssnMultipleFileNameAdd = ssnMultipleFileNameAdd;
    }

    public File[] getMultipleSelectedFiles() {
        return multipleSelectedFiles;
    }

    public void setMultipleSelectedFiles(File[] multipleSelectedFiles) {
        this.multipleSelectedFiles = multipleSelectedFiles;
    }

    public JCheckBox getCreateAlbumChkBx() {
        return createAlbumChkBx;
    }

    public void setCreateAlbumChkBx(JCheckBox createAlbumChkBx) {
        this.createAlbumChkBx = createAlbumChkBx;
    }

    public JPanel getWelcomeImagePanel() {
        return welcomeImagePanel;
    }

    public void setWelcomeImagePanel(JPanel welcomeImagePanel) {
        this.welcomeImagePanel = welcomeImagePanel;
    }

    public JPanel getThumbnailImagePanel() {
        return thumbnailImagePanel;
    }

    public void setThumbnailImagePanel(JPanel thumbnailImagePanel) {
        this.thumbnailImagePanel = thumbnailImagePanel;
    }

    public SSNHiveAlbumSelectionListner getSsnHiveAlbumSelectionListner() {
        return ssnHiveAlbumSelectionListner;
    }

    public void setSsnHiveAlbumSelectionListner(SSNHiveAlbumSelectionListner ssnHiveAlbumSelectionListner) {
        this.ssnHiveAlbumSelectionListner = ssnHiveAlbumSelectionListner;
    }
    
    

}


class CustomImagePanel extends JPanel {

  private Image img;

  public CustomImagePanel(String img) {
    this(new ImageIcon(img).getImage());
  }

  public CustomImagePanel(Image img) {
    this.img = img;
    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
    setPreferredSize(size);
    setMinimumSize(size);
    setMaximumSize(size);
    setSize(size);
    setLayout(null);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(img, 0, 0, null);
  }
  
  

}
