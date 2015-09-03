package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNLoginController;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNLoginModel;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNIconPasswordField;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ws.rest.response.SSNLoginResponse;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author vkvarma
 */

public class SSNLoginForm extends JFrame {
    
    private static final long               serialVersionUID       = 1L;
    private              Dimension          ssnScreenSize          = null;
    private              JPanel             ssnLoginPanel          = null;
    private              JLabel             ssnHiveLogoLabel       = null; 
    private              JLabel             ssnSignInLabel         = null;
    private              JCheckBox          ssnRememberMeCheckBox  = null; 
    private              JLabel             ssnHivenameLabel       = null;
    private              JLabel             ssnPasswordLabel       = null; 
    private              JTextField         ssnHivename            = null;
    private              JPasswordField     ssnPassword            = null;    
    private              JButton            ssnLoginBtn            = null;     
    private              JLabel             ssnForgotMsgLabel      = null; 
    private              JLabel             ssnForgotPasswordLabel = null; 
    private              JLabel             ssnLoginSectionLabel   = null;        
    private              JLabel             ssnSignUpMsgLabel      = null; 
    private              JLabel             ssnSignUpLabel         = null; 
    private              JLabel             ssnSignInBtn             = null;
   // private              JLabel             ssnORLabel              = null;
    //private              JLabel             ssnSignInTwitterBtn      = null;   
    private              JLabel             ssnKeepMeSignInImg      = null;   
   // private              JLabel             ssnSignInFacebookBtn     = null;   
   // private              JLabel             ssnSignInInstagramBtn    = null;   
    private              JLabel             ssnCopyrightLabel       = null;
    private              JLabel             ssnSignInUsingLabel     =   null;
    private              JLabel             ssnYellowArrowLabel1    =   null;
    private              JLabel             ssnYellowArrowLabel2    =   null;

    private              SSNLoginController loginController        = null;
    private              SSNLoginModel      loginModel             = null;
    private              String[]           usernamePasswordArray  = null;
    private              boolean            autoLogin              =true;
    private             boolean             chkLoginState =false;
    
    


    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNLoginForm.class);
    public SSNLoginForm() {
        super("OurHive SignIn");
        logger.info("Start SSNLoginForm : ");
        this.initLoginForm();
    }
    
    public SSNLoginForm(boolean autoLogin){
        
        super("OurHive SignIn");
        this.autoLogin = autoLogin;
        this.initLoginForm();
    }
    private void initLoginForm() {
         logger.info("Start initLoginForm : ");
        try {
            
             this.setLoginController(new SSNLoginController(this));  
             this.setLoginModel(new SSNLoginModel(this,this.getLoginController()));
             this.getLoginController().setLoginModel(this.getLoginModel()); 
             this.initLoginFormGUIComponents();
             this.renderLoginPanel();
             this.addLoginFormUIElements();
             this.positionLoginFormUIElements();
             this.renderLoginFrame();
             this.getSsnHivename().requestFocus(true);
             this.addWindowListener(this.getLoginController());
             if(chkLoginState)
             {
                //this.setVisible(true);
                 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                         setVisible();
                         
                     }
                 });
             }  

        } 
        catch (Exception ex) {
        //ex.printStackTrace();
         logger.info("Start initLoginForm : "+ex);
        }
    }
    
    public void setVisible(){
  
            this.setVisible(true);
        
        
    }
    private void initLoginFormGUIComponents() {        
        try {  
            usernamePasswordArray = SSNHelper.getRememberMeCredentials();
            
            Dimension dimension =  SSNConstants.SSN_SCREEN_SIZE;
            this.setSsnScreenSize(dimension); 
            this.setSsnHiveLogoLabel(new JLabel(new ImageIcon(getClass().getResource("/images/ssn-hive-logo -login.png")))); 
            this.setSsnSignInLabel(new JLabel("<html><font color='rgb(255,215,0)'>SIGN IN</font></html>")); 
            this.setSsnYellowArrowLabel1(new JLabel(new ImageIcon(getClass().getResource("/icon/signUp_arrow.png"))));
            this.setSsnYellowArrowLabel2(new JLabel(new ImageIcon(getClass().getResource("/icon/signUp_arrow.png"))));
            this.getSsnSignInLabel().setFont(new Font("open sans",Font.BOLD,20)); 
            this.setSsnRememberMeCheckBox(new JCheckBox("",false));    
            this.getSsnRememberMeCheckBox().setFont(new Font("open sans",Font.BOLD,12)); 
            this.getSsnRememberMeCheckBox().setForeground(new Color(255,255,255)); 
            // this.getSsnRememberMeCheckBox().setEnabled(false);
            this.getSsnRememberMeCheckBox().setToolTipText("Keeps you Logged in till you explicitly Logout "); 
            
            
            this.setSsnKeepMeSignInImg(new JLabel(new ImageIcon(getClass().getResource("/images/keepme.png"))));
            this.setSsnHivenameLabel(new JLabel(new ImageIcon(getClass().getResource("/images/ssn-username.jpg"))));
            this.setSsnPasswordLabel(new JLabel(new ImageIcon(getClass().getResource("/images/ssn-password.jpg"))));
            this.setSsnHivename(new SSNIconTextField("","Username","hivename")); 
            this.getSsnHivename().setHorizontalAlignment((int) SSNIconTextField.CENTER_ALIGNMENT);
            //this.getSsnHivename()
            this.getSsnHivename().setBackground(new Color(72,72,72,128));
            this.getSsnHivename().setForeground(new Color(255,255,255));
            this.getSsnHivename().setFont(new Font("open sans",Font.BOLD,15));
            this.getSsnHivename().addKeyListener(this.getLoginController()); 
            
            this.setSsnPassword(new SSNIconPasswordField("","Password","password"));            
            this.getSsnPassword().setHorizontalAlignment((int) JPasswordField.CENTER_ALIGNMENT);
          //  this.getSsnPassword().se
            this.getSsnPassword().setBackground(new Color(72,72,72,128));
            this.getSsnPassword().setForeground(new Color(255,255,255));
            this.getSsnPassword().setFont(new Font("open sans",Font.PLAIN,15));
            //this.getSsnPassword().setEchoChar('\u200b');        
            this.getSsnPassword().setEchoChar('\u25CF');    
            this.getSsnPassword().addKeyListener(this.getLoginController());
            if(usernamePasswordArray!=null && usernamePasswordArray.length>0){
                this.getSsnHivename().setText(usernamePasswordArray[0]);
                this.getSsnPassword().setText(usernamePasswordArray[1]);
                this.getSsnRememberMeCheckBox().setSelected(true);
                
            }
            this.setSsnSignInBtn(new JLabel("Sign In",new ImageIcon(getClass().getResource("/icon/sign_in_btn.png")),0));
            this.getSsnSignInBtn().setName("login");
            this.getSsnSignInBtn().setBackground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            this.getSsnSignInBtn().setForeground(new Color(72,72,72));
         //   this.getSsnSignInBtn().setFocusPainted(false);
            this.getSsnSignInBtn().setFont(new Font("open sans",Font.BOLD,18)); 
           // this.getSsnSignInBtn().setActionCommand("login"); 
          //  this.getSsnSignInBtn().addActionListener(this.getLoginController());
            this.getSsnSignInBtn().addMouseListener(this.getLoginController());
            this.getSsnSignInBtn().addKeyListener(this.getLoginController()); 
            
            this.setSsnForgotMsgLabel(new JLabel("Forgot Password? "));
            this.getSsnForgotMsgLabel().setFont(new Font("open sans",Font.BOLD,12)); 
            this.getSsnForgotMsgLabel().setForeground(new Color(255,255,255)); 
            this.setSsnForgotPasswordLabel(SSNHelper.getHyperlinkLabel("Click Here","lblForgotPassword",SSNConstants.SSN_WEB_HOST+"users/forgot_username_password","rgb(255,215,0)","login"));
            this.getSsnForgotPasswordLabel().setName("lblForgotPassword");
            this.getSsnForgotPasswordLabel().setFont(new Font("open sans",Font.BOLD,12)); 
            this.setSsnSignUpMsgLabel(new JLabel("<HTML><font color='rgb(255,255,255)'><b>Don't have an account ?</b></font><HTML>"));
            this.getSsnSignUpMsgLabel().setFont(new Font("open sans",Font.PLAIN,12)); 
            this.getSsnSignUpMsgLabel().setForeground(new Color(123,123,123)); 
            //this.setSsnSignUpLabel(SSNHelper.getHyperlinkLabel("Sign up for free","lblSignup","http://162.209.99.244/ssn/users/signup","rgb(72,72,72)","login"));
             this.setSsnSignUpLabel(new JLabel(new ImageIcon(getClass().getResource("/icon/sign_up_free_btn.png"))));
             this.getSsnSignUpLabel().setName("lblSignup");
            this.getSsnSignUpLabel().setFont(new Font("open sans",Font.BOLD,12)); 
            this.getSsnSignUpLabel().setBackground(new Color(72,72,72));
            this.getSsnSignUpLabel().setOpaque(true);
            this.getSsnSignUpLabel().addMouseListener(this.getLoginController());
            
            this.setSsnLoginSectionLabel(new JLabel(new ImageIcon(getClass().getResource("/images/ssn-login-section-sep.png"))));
//            this.setSsnORLabel(new JLabel("OR"));
//            this.getSsnORLabel().setForeground(new Color(162,162,162));
//            this.getSsnORLabel().setFont(new Font("open sans",Font.PLAIN,9)); 
//            
//            
//            this.setSsnSignInTwitterBtn(new JLabel("Sign In With Twitter",new ImageIcon(getClass().getResource("/images/ssn-twitter-login.png")),0));            
//            this.setSsnSignInFacebookBtn(new JLabel("Sign In With Facebook",new ImageIcon(getClass().getResource("/images/ssn-facebook-login.png")),0));            
//            this.setSsnSignInInstagramBtn(new JLabel("Sign In With Instagram",new ImageIcon(getClass().getResource("/images/ssn-instagram-login.png")),0));            
//            
//            this.getSsnSignInTwitterBtn().setName("loginTwitter");
//            this.getSsnSignInFacebookBtn().setName("loginFacebook");
//            this.getSsnSignInInstagramBtn().setName("loginInstagram");
//            
//            this.getSsnSignInTwitterBtn().addMouseListener(this.getLoginController());
//            this.getSsnSignInFacebookBtn().addMouseListener(this.getLoginController());
//            this.getSsnSignInInstagramBtn().addMouseListener(this.getLoginController());
            
            this.setSsnSignInUsingLabel(new JLabel("Or Sign In Using"));
            this.getSsnSignInUsingLabel().setForeground(new Color(255,255,255));
            this.getSsnSignInUsingLabel().setFont(new Font("open sans",Font.BOLD,11));
            
            
            this.setSsnCopyrightLabel(new JLabel("Copyright @ OurHive.All rights reserved."));
            this.getSsnCopyrightLabel().setFont(new Font("open sans",Font.PLAIN,11)); 
            this.getSsnCopyrightLabel().setForeground(new Color(100,98,92)); 
            
           // this.getSsnSignUpLabel().setBorder(new SSNCustomBorder(false,new Color(241,240,238),this.getSsnSignUpLabel().getHeight(),this.getSsnSignUpLabel().getWidth()));
            this.getSsnPassword().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getSsnPassword().getHeight(),this.getSsnPassword().getWidth()));
             this.getSsnHivename().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getSsnHivename().getHeight(),this.getSsnHivename().getWidth()));
             // this.getSsnSignInBtn().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getSsnSignInBtn().getHeight(),this.getSsnSignInBtn().getWidth()));
        } 
        catch (HeadlessException ex) {
            logger.error(ex);
        }
    }
    
    private void renderLoginPanel() {        
        try {
            URL imgURL          = getClass().getResource("/images/ssn-login-panel.png");
            BufferedImage image = ImageIO.read(imgURL);
            Image background    = image.getScaledInstance(image.getWidth(),image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnLoginPanel(new SSNImagePanel(background));    
            this.getSsnLoginPanel().setLayout(null); 
        } 
        catch (Exception ex) { 
               logger.error(ex);
        }
    }
    
    private void addLoginFormUIElements() {
        this.getSsnLoginPanel().add(this.getSsnHiveLogoLabel());
        this.getSsnLoginPanel().add(this.getSsnSignInLabel());
        this.getSsnLoginPanel().add(this.getSsnRememberMeCheckBox());
       // this.getSsnLoginPanel().add(this.getSsnHivenameLabel());
       // this.getSsnLoginPanel().add(this.getSsnPasswordLabel());
        this.getSsnLoginPanel().add(this.getSsnHivename());
        this.getSsnLoginPanel().add(this.getSsnPassword());
        this.getSsnLoginPanel().add(this.getSsnSignInBtn());        
        this.getSsnLoginPanel().add(this.getSsnForgotMsgLabel());
        this.getSsnLoginPanel().add(this.getSsnForgotPasswordLabel());
        this.getSsnLoginPanel().add(this.getSsnSignUpMsgLabel());
        this.getSsnLoginPanel().add(this.getSsnSignUpLabel());
        this.getSsnLoginPanel().add(this.getSsnLoginSectionLabel());
       // this.getSsnLoginPanel().add(this.getSsnORLabel()); 
       // this.getSsnLoginPanel().add(this.getSsnSignInUsingLabel());
       // this.getSsnLoginPanel().add(this.getSsnSignInFacebookBtn());
       // this.getSsnLoginPanel().add(this.getSsnSignInTwitterBtn());
       // this.getSsnLoginPanel().add(this.getSsnSignInInstagramBtn());
        this.getSsnLoginPanel().add(this.getSsnYellowArrowLabel1());
        this.getSsnLoginPanel().add(this.getSsnYellowArrowLabel2());
        this.getSsnLoginPanel().add(this.getSsnKeepMeSignInImg());
       // this.getSsnLoginPanel().add(this.getSsnCopyrightLabel());
    }
    
    private void positionLoginFormUIElements() {        
        this.getSsnHiveLogoLabel().setBounds(125,50,172,76);
        this.getSsnSignInLabel().setBounds(165,135,135,76);
        this.getSsnYellowArrowLabel1().setBounds(205,180,10,20);
         this.getSsnHivename().setBounds(90,210,240,43);
        this.getSsnPassword().setBounds(90,260,240,43);
        
        this.getSsnRememberMeCheckBox().setBounds(135,320, 20, 30);
        this.getSsnKeepMeSignInImg().setBounds(155,325,133,15);
       
        this.getSsnSignInBtn().setBounds(90,360,260,48); 
        
        this.getSsnForgotMsgLabel().setBounds(130,425,120,25);
        this.getSsnForgotPasswordLabel().setBounds(240,425,90,25);
        
        
        this.getSsnLoginSectionLabel().setBounds(30,490,350,26);
        this.getSsnSignUpMsgLabel().setBounds(135,550,160,25);
        this.getSsnSignUpLabel().setBounds(90,600,240,43);
        
        
        //this.getSsnORLabel().setBounds(245,530,35,35);
        this.getSsnYellowArrowLabel2().setBounds(200,570,10,20);
       // this.getSsnSignInFacebookBtn().setBounds(120,440,60,39);
       // this.getSsnSignInTwitterBtn().setBounds(180,440,60,39);
       // this.getSsnSignInInstagramBtn().setBounds(240,440,60,39);
       // this.getSsnCopyrightLabel().setBounds(140,595,266,39);
       // this.getSsnSignInUsingLabel().setBounds(170,400,100,39);
       
        
        
    }
    
    private void renderLoginFrame() {
        try {
            //this.setUndecorated(true);
            this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            //this.setAlwaysOnTop(true);
            
            URL           imgURL = getClass().getResource("/images/ssn-login-panel.png");
            BufferedImage image  = ImageIO.read(imgURL);
            Dimension     dim    =  SSNConstants.SSN_SCREEN_SIZE;
            this.setBounds(0,0,image.getWidth(),image.getHeight());
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.add(this.getSsnLoginPanel());
            if(usernamePasswordArray!=null && usernamePasswordArray.length>0){
                chkLoginState = false;
                SSNLoginResponse response = null;
                if(autoLogin){
                    response = this.getLoginModel().processSSNLogin();
                }
                if (response != null && response.isSuccess()) {
                    this.setVisible(false);
                    new SSNHomeForm(this, response);
                } else {
                    //this.setVisible(true);
                    SwingUtilities.invokeLater(new Runnable() {

                     @Override
                     public void run() {
                         setVisible();
                         
                     }
                 });
                }
            }else{
                chkLoginState = true;
                //this.setVisible(true);
                SwingUtilities.invokeLater(new Runnable() {

                     @Override
                     public void run() {
                         setVisible();
                         
                     }
                 });
            }
            
        } 
        catch (IOException ex) {   logger.error(ex);}
    }

    public JLabel getSsnSignInUsingLabel() {
        return ssnSignInUsingLabel;
    }

    public void setSsnSignInUsingLabel(JLabel ssnSignInUsingLabel) {
        this.ssnSignInUsingLabel = ssnSignInUsingLabel;
    }
    
    
    
    /**
     * @return the ssnLoginPanel
     */
    public JPanel getSsnLoginPanel() {
        return ssnLoginPanel;
    }

    /**
     * @param ssnLoginPanel the ssnLoginPanel to set
     */
    public void setSsnLoginPanel(JPanel ssnLoginPanel) {
        this.ssnLoginPanel = ssnLoginPanel;
    }

    /**
     * @return the ssnScreenSize
     */
    public Dimension getSsnScreenSize() {
        return ssnScreenSize;
    }

    /**
     * @param ssnScreenSize the ssnScreenSize to set
     */
    public void setSsnScreenSize(Dimension ssnScreenSize) {
        this.ssnScreenSize = ssnScreenSize;
    }

    /**
     * @return the ssnUsername
     */
    public JTextField getSsnHivename() {
        return ssnHivename;
    }

    /**
     * @param ssnUsername the ssnUsername to set
     */
    public void setSsnHivename(JTextField ssnUsername) {
        this.ssnHivename = ssnUsername;
    }

    /**
     * @return the ssnPassword
     */
    public JPasswordField getSsnPassword() {
        return ssnPassword;
    }

    /**
     * @param ssnPassword the ssnPassword to set
     */
    public void setSsnPassword(JPasswordField ssnPassword) {
        this.ssnPassword = ssnPassword;
    }

    /**
     * @return the ssnSignInBtn
     */
    public JLabel getSsnSignInBtn() {
        return ssnSignInBtn;
    }

    /**
     * @param ssnSignInBtn the ssnSignInBtn to set
     */
    public void setSsnSignInBtn(JLabel ssnSignInBtn) {
        this.ssnSignInBtn = ssnSignInBtn;
    }

    /**
     * @return the ssnLoginBtn
     */
    public JButton getSsnLoginBtn() {
        return ssnLoginBtn;
    }

    /**
     * @param ssnLoginBtn the ssnLoginBtn to set
     */
    public void setSsnLoginBtn(JButton ssnLoginBtn) {
        this.ssnLoginBtn = ssnLoginBtn;
    }

    /**
     * @return the ssnForgotPasswordLabel
     */
    public JLabel getSsnForgotPasswordLabel() {
        return ssnForgotPasswordLabel;
    }

    /**
     * @param ssnForgotPasswordLabel the ssnForgotPasswordLabel to set
     */
    public void setSsnForgotPasswordLabel(JLabel ssnForgotPasswordLabel) {
        this.ssnForgotPasswordLabel = ssnForgotPasswordLabel;
    }

    /**
     * @return the ssnSignUpLabel
     */
    public JLabel getSsnSignUpLabel() {
        return ssnSignUpLabel;
    }

    /**
     * @param ssnSignUpLabel the ssnSignUpLabel to set
     */
    public void setSsnSignUpLabel(JLabel ssnSignUpLabel) {
        this.ssnSignUpLabel = ssnSignUpLabel;
    }

    /**
     * @return the ssnHiveLogoLabel
     */
    public JLabel getSsnHiveLogoLabel() {
        return ssnHiveLogoLabel;
    }

    /**
     * @param ssnHiveLogoLabel the ssnHiveLogoLabel to set
     */
    public void setSsnHiveLogoLabel(JLabel ssnHiveLogoLabel) {
        this.ssnHiveLogoLabel = ssnHiveLogoLabel;
    }

    /**
     * @return the ssnSignInLabel
     */
    public JLabel getSsnSignInLabel() {
        return ssnSignInLabel;
    }

    /**
     * @param ssnSignInLabel the ssnSignInLabel to set
     */
    public void setSsnSignInLabel(JLabel ssnSignInLabel) {
        this.ssnSignInLabel = ssnSignInLabel;
    }

    /**
     * @return the ssnUsernameLabel
     */
    public JLabel getSsnHivenameLabel() {
        return ssnHivenameLabel;
    }

    /**
     * @param ssnUsernameLabel the ssnUsernameLabel to set
     */
    public void setSsnHivenameLabel(JLabel ssnUsernameLabel) {
        this.ssnHivenameLabel = ssnUsernameLabel;
    }

    /**
     * @return the ssnPasswordLabel
     */
    public JLabel getSsnPasswordLabel() {
        return ssnPasswordLabel;
    }

    /**
     * @param ssnPasswordLabel the ssnPasswordLabel to set
     */
    public void setSsnPasswordLabel(JLabel ssnPasswordLabel) {
        this.ssnPasswordLabel = ssnPasswordLabel;
    }

    /**
     * @return the ssnSignUpMsgLabel
     */
    public JLabel getSsnSignUpMsgLabel() {
        return ssnSignUpMsgLabel;
    }

    /**
     * @param ssnSignUpMsgLabel the ssnSignUpMsgLabel to set
     */
    public void setSsnSignUpMsgLabel(JLabel ssnSignUpMsgLabel) {
        this.ssnSignUpMsgLabel = ssnSignUpMsgLabel;
    }
    
    /**
     * @return the loginController
     */
    public SSNLoginController getLoginController() {
        return loginController;
    }

    /**
     * @param loginController the loginController to set
     */
    public void setLoginController(SSNLoginController loginController) {
        this.loginController = loginController;
    }
        
    /**
     * @return the ssnForgotMsgLabel
     */
    public JLabel getSsnForgotMsgLabel() {
        return ssnForgotMsgLabel;
    }

    /**
     * @param ssnForgotMsgLabel the ssnForgotMsgLabel to set
     */
    public void setSsnForgotMsgLabel(JLabel ssnForgotMsgLabel) {
        this.ssnForgotMsgLabel = ssnForgotMsgLabel;
    }

    /**
     * @return the ssnRememberMeCheckBox
     */
    public JCheckBox getSsnRememberMeCheckBox() {
        return ssnRememberMeCheckBox;
    }

    /**
     * @param ssnRememberMeCheckBox the ssnRememberMeCheckBox to set
     */
    public void setSsnRememberMeCheckBox(JCheckBox ssnRememberMeCheckBox) {
        this.ssnRememberMeCheckBox = ssnRememberMeCheckBox;
    }

    /**
     * @return the ssnSignInTwitterBtn
     */
//    public JLabel getSsnSignInTwitterBtn() {
//        return ssnSignInTwitterBtn;
//    }
//
//    /**
//     * @param ssnSignInTwitterBtn the ssnSignInTwitterBtn to set
//     */
//    public void setSsnSignInTwitterBtn(JLabel ssnSignInTwitterBtn) {
//        this.ssnSignInTwitterBtn = ssnSignInTwitterBtn;
//    }
//
//    /**
//     * @return the ssnSignInFacebookBtn
//     */
//    public JLabel getSsnSignInFacebookBtn() {
//        return ssnSignInFacebookBtn;
//    }
//
//    /**
//     * @param ssnSignInFacebookBtn the ssnSignInFacebookBtn to set
//     */
//    public void setSsnSignInFacebookBtn(JLabel ssnSignInFacebookBtn) {
//        this.ssnSignInFacebookBtn = ssnSignInFacebookBtn;
//    }

    /**
     * @return the ssnSignInInstagramBtn
     */
//    public JLabel getSsnSignInInstagramBtn() {
//        return ssnSignInInstagramBtn;
//    }
//
//    /**
//     * @param ssnSignInInstagramBtn the ssnSignInInstagramBtn to set
//     */
//    public void setSsnSignInInstagramBtn(JLabel ssnSignInInstagramBtn) {
//        this.ssnSignInInstagramBtn = ssnSignInInstagramBtn;
//    }

    /**
     * @return the ssnLoginSectionLabel
     */
    public JLabel getSsnLoginSectionLabel() {
        return ssnLoginSectionLabel;
    }

    /**
     * @param ssnLoginSectionLabel the ssnLoginSectionLabel to set
     */
    public void setSsnLoginSectionLabel(JLabel ssnLoginSectionLabel) {
        this.ssnLoginSectionLabel = ssnLoginSectionLabel;
    }
    
    /**
     * @return the ssnORLabel
     */
//    public JLabel getSsnORLabel() {
//        return ssnORLabel;
//    }
//
//    /**
//     * @param ssnORLabel the ssnORLabel to set
//     */
//    public void setSsnORLabel(JLabel ssnORLabel) {
//        this.ssnORLabel = ssnORLabel;
//    }
    
    /**
     * @return the ssnCopyrightLabel
     */
    public JLabel getSsnCopyrightLabel() {
        return ssnCopyrightLabel;
    }

    /**
     * @param ssnCopyrightLabel the ssnCopyrightLabel to set
     */
    public void setSsnCopyrightLabel(JLabel ssnCopyrightLabel) {
        this.ssnCopyrightLabel = ssnCopyrightLabel;
    }
    
    /**
     * @return the loginModel
     */
    public SSNLoginModel getLoginModel() {
        return loginModel;
    }

    /**
     * @param loginModel the loginModel to set
     */
    public void setLoginModel(SSNLoginModel loginModel) {
        this.loginModel = loginModel;
    }
    
    /* For Testing */
    public JLabel getSsnYellowArrowLabel1() {
        return ssnYellowArrowLabel1;
    }

    public void setSsnYellowArrowLabel1(JLabel ssnYellowArrowLabel1) {
        this.ssnYellowArrowLabel1 = ssnYellowArrowLabel1;
    }

    public JLabel getSsnYellowArrowLabel2() {
        return ssnYellowArrowLabel2;
    }

    public void setSsnYellowArrowLabel2(JLabel ssnYellowArrowLabel2) {
        this.ssnYellowArrowLabel2 = ssnYellowArrowLabel2;
    }

    public JLabel getSsnKeepMeSignInImg() {
        return ssnKeepMeSignInImg;
    }

    public void setSsnKeepMeSignInImg(JLabel ssnKeepMeSignInImg) {
        this.ssnKeepMeSignInImg = ssnKeepMeSignInImg;
    }
    
    
    
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            @SuppressWarnings("ResultOfObjectAllocationIgnored")
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                    new SSNLoginForm();
                } 
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                       logger.error(ex);
                }
            }
        });
    } 
}