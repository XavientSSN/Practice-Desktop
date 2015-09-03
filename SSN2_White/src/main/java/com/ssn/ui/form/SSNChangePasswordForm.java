package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.event.controller.SSNChangePasswordController;
import com.ssn.model.SSNChangePasswordModel;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNIconPasswordField;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNVideoMetadata;
import com.ssn.ws.rest.response.SSNLoginResponse;
import com.ssn.ws.rest.response.SSNUserResponse;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author vkvarma
 */
public class SSNChangePasswordForm extends JFrame {
    
    private static final long                         serialVersionUID                 = 1L;   
    private              JPanel                       ssnChangePasswordPanel           = null;
    private              JLabel                       ssnChangePasswordTitleLabel      = null; 
    private              JLabel                       ssnChangePasswordCancelLabel     = null; 
    private              JSeparator                   changePasswordSeparator          = null;  
    private              JLabel                       ssnOldPasswordLabel              = null;
    private              JLabel                       ssnNewPasswordLabel              = null;
    private              JLabel                       ssnConfirmNewPasswordLabel       = null;
    private              JPasswordField               ssnOldPasswordTxt                = null;
    private              JPasswordField               ssnNewPasswordTxt                = null;
    private              JPasswordField               ssnConfirmNewPasswordTxt         = null;
    private              String                       ssnAccessToken                   = null; 
    private              JLabel                      ssnChangePasswordBtn             = null;
    private              JButton                      ssnChangePasswordCancelBtn       = null;
    private              SSNHomeForm                  homeForm                         = null;
    private              SSNChangePasswordController  changePasswordController         = null;
    private              SSNLoginResponse             loginResponse                    = null;
    private              SSNChangePasswordModel       changePasswordModel              = null;       
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNChangePasswordForm.class);
    public SSNChangePasswordForm() { 
          try {
                      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
              } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }

    }
    
    public SSNChangePasswordForm(SSNHomeForm homeForm,SSNLoginResponse loginResponse) {
        super("SSN Change Password Form");
        
        
          try {
                      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
              }

        
        setHomeForm(homeForm);
        setLoginResponse(loginResponse);
        setChangePasswordController(new SSNChangePasswordController(this));
        setChangePasswordModel(new SSNChangePasswordModel(this,this.getHomeForm(),this.getChangePasswordController())); 
        getChangePasswordController().setChangePasswordModel(getChangePasswordModel()); 
        this.initChangePasswordFormGUIComponents();
        this.renderChangePasswordPanel();
        this.addChangePasswordFormUIElements();
        this.positionChangePasswordFormUIElements();
        this.renderChangePasswordFrame();   
        this.loadAccessToken();
    }
    
    private void initChangePasswordFormGUIComponents() {
        this.setSsnChangePasswordTitleLabel(new JLabel("<html><font color='rgb(255,215,0)'>Change Password</font></html>")); 
        this.getSsnChangePasswordTitleLabel().setFont(new Font("open sans",Font.BOLD,18));         
        //this.setChangePasswordSeparator(new JSeparator(JSeparator.HORIZONTAL));        
        this.setSsnChangePasswordCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png")))); 
        this.getSsnChangePasswordCancelLabel().setName("closeChangePassword");
        this.getSsnChangePasswordCancelLabel().addMouseListener(this.getChangePasswordController());       
        
        this.setSsnOldPasswordLabel(new JLabel("OLD PASSWORD:"));
        this.getSsnOldPasswordLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        
        this.getSsnOldPasswordLabel().setBackground(new Color(0,0,0,1));
        this.getSsnOldPasswordLabel().setOpaque(true);
        this.getSsnOldPasswordLabel().setFont(new Font("open sans",Font.BOLD,12));
        this.getSsnOldPasswordLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        
        this.setSsnNewPasswordLabel(new JLabel("NEW PASSWORD:"));
        this.getSsnNewPasswordLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnNewPasswordLabel().setBackground(new Color(0,0,0,1));
        this.getSsnNewPasswordLabel().setOpaque(true);
        this.getSsnNewPasswordLabel().setFont(new Font("open sans",Font.BOLD,12));
        this.getSsnNewPasswordLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        this.setSsnConfirmNewPasswordLabel(new JLabel("CONFIRM NEW PASSWORD:"));
        this.getSsnConfirmNewPasswordLabel().setBackground(new Color(0,0,0,1));
       
        this.getSsnConfirmNewPasswordLabel().setFont(new Font("open sans",Font.BOLD,12));
        this.getSsnConfirmNewPasswordLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
         this.getSsnConfirmNewPasswordLabel().setOpaque(true);
         this.getSsnConfirmNewPasswordLabel().setHorizontalAlignment(SwingConstants.RIGHT);
         
        this.setSsnOldPasswordTxt(new SSNIconPasswordField("","Enter Old Password","oldPassword"));
        this.getSsnOldPasswordTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnOldPasswordTxt().setEchoChar('\u25CF'); 
        
        this.setSsnNewPasswordTxt(new SSNIconPasswordField("","Enter New Password","newPassword"));
        this.getSsnNewPasswordTxt().setBackground(new Color(0,0,0,1));
        this.getSsnNewPasswordTxt().setEchoChar('\u25CF'); 
        
        this.setSsnConfirmNewPasswordTxt(new SSNIconPasswordField("","Confirm New Password","confirmNewPassword"));
        this.getSsnConfirmNewPasswordTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnConfirmNewPasswordTxt().setEchoChar('\u25CF');   
        
        
// by ritesh       this.getSsnOldPasswordLabel().setForeground(new Color(0,0,0));
// by ritesh       this.getSsnNewPasswordLabel().setForeground(new Color(0,0,0));
// by ritesh       this.getSsnConfirmNewPasswordLabel().setForeground(new Color(0,0,0));
                
        this.getSsnOldPasswordTxt().setBackground(new Color(202,199,192));
        this.getSsnNewPasswordTxt().setBackground(new Color(202,199,192));
        this.getSsnConfirmNewPasswordTxt().setBackground(new Color(202,199,192));
        
        
                
        this.getSsnOldPasswordTxt().setFont(new Font("open sans",Font.PLAIN,12));
        this.getSsnNewPasswordTxt().setFont(new Font("open sans",Font.PLAIN,12));
        this.getSsnConfirmNewPasswordTxt().setFont(new Font("open sans",Font.PLAIN,12));
        
        this.getSsnOldPasswordTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnNewPasswordTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnConfirmNewPasswordTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        
        
        
        //==
        this.getSsnOldPasswordTxt().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                try{
                 if (Character.isWhitespace(input.charAt(0))) {

                        getSsnOldPasswordTxt().setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } 
                }catch(Exception ee){
                    
                }
            }
        });
        this.getSsnNewPasswordTxt().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                try{
                    
                
                 if (Character.isWhitespace(input.charAt(0))) {

                        getSsnNewPasswordTxt().setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } 
                 }catch(Exception ee){
                     
                 }
            }
        });
        this.getSsnConfirmNewPasswordTxt().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                 if (Character.isWhitespace(input.charAt(0))) {

                        getSsnConfirmNewPasswordTxt().setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } 
            }
        });
        this.getSsnNewPasswordTxt().setFont(new Font("open sans",Font.PLAIN,12));
        this.getSsnConfirmNewPasswordTxt().setFont(new Font("open sans",Font.PLAIN,12));
        //==
        //Border paddingBorder = BorderFactory.createEmptyBorder(9,5,9,10);	
	//Border border = BorderFactory.createLineBorder(new Color(202,199,192));
        
        UIDefaults overrides =getUidefaults();
                
        this.getSsnOldPasswordTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnNewPasswordTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnConfirmNewPasswordTxt().putClientProperty("Nimbus.Overrides", overrides);
                        

        this.getSsnOldPasswordTxt().setBorder(new SSNCustomBorder(true,this.getSsnOldPasswordTxt().getHeight(),this.getSsnOldPasswordTxt().getWidth()));
        this.getSsnNewPasswordTxt().setBorder(new SSNCustomBorder(true,this.getSsnNewPasswordTxt().getHeight(),this.getSsnNewPasswordTxt().getWidth()));
        this.getSsnConfirmNewPasswordTxt().setBorder(new SSNCustomBorder(true,this.getSsnConfirmNewPasswordTxt().getHeight(),this.getSsnConfirmNewPasswordTxt().getWidth()));
      
       
         this.setSsnChangePasswordBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/change_password_btn.png"))));            
        
        this.getSsnChangePasswordBtn().addMouseListener(this.getChangePasswordController());
        
        this.getSsnChangePasswordBtn().setBackground(new Color(255,209,25)); 
        this.getSsnChangePasswordBtn().setForeground(new Color(0,0,0));
        this.getSsnChangePasswordBtn().setFont(new Font("open sans",Font.PLAIN,16));
        this.getSsnChangePasswordBtn().setName("changeUserPassword");
        this.getSsnChangePasswordBtn().addKeyListener(this.getChangePasswordController());
        
       
        
        this.setSsnChangePasswordCancelBtn(new JButton("Cancel",new ImageIcon(getClass().getResource("/images/ssn-profile-cancel.png"))));            
        this.getSsnChangePasswordCancelBtn().setForeground(new Color(68,68,68));
        this.getSsnChangePasswordCancelBtn().setFocusPainted(false);
        this.getSsnChangePasswordCancelBtn().setActionCommand("cancelChangeUserPassword"); 
        this.getSsnChangePasswordCancelBtn().addActionListener(this.getChangePasswordController());             
         
    }
    
    
    
     private UIDefaults getUidefaults()
    {
    
       UIDefaults overrides = new UIDefaults();
                overrides.put("PasswordField.background", new ColorUIResource(new Color(202,199,192)));
                overrides.put("PasswordField[Enabled].backgroundPainter", new Painter<JPasswordField>() {

                    @Override
                    public void paint(Graphics2D g, JPasswordField field, int width, int height) {
                      
                    }

                });
               
                
       return overrides;
    }
    
    
    
    private void renderChangePasswordPanel() {
        try {
            URL imgURL          = getClass().getResource("/images/bg.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Image background    = image.getScaledInstance(image.getWidth(),image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnChangePasswordPanel(new SSNImagePanel(background));    
            this.getSsnChangePasswordPanel().setLayout(null);         
        } 
        catch (Exception ex) {  
            ex.printStackTrace();
        }
    }
    
    private void addChangePasswordFormUIElements() {        
        this.getSsnChangePasswordPanel().add(this.getSsnChangePasswordTitleLabel());        
       
        this.getSsnChangePasswordPanel().add(this.getSsnChangePasswordCancelLabel());        
        this.getSsnChangePasswordPanel().add(this.getSsnOldPasswordLabel());
        this.getSsnChangePasswordPanel().add(this.getSsnOldPasswordTxt());
        this.getSsnChangePasswordPanel().add(this.getSsnNewPasswordLabel());
        this.getSsnChangePasswordPanel().add(this.getSsnNewPasswordTxt());
        this.getSsnChangePasswordPanel().add(this.getSsnConfirmNewPasswordLabel());
        this.getSsnChangePasswordPanel().add(this.getSsnConfirmNewPasswordTxt());  
        this.getSsnChangePasswordPanel().add(this.getSsnChangePasswordBtn()); 
         
    }
    
    private void positionChangePasswordFormUIElements() {
       
        this.getSsnChangePasswordTitleLabel().setBounds(10,5,200,30);
      
        this.getSsnChangePasswordCancelLabel().setBounds(475,5,150,30);        
        this.getSsnOldPasswordLabel().setBounds(80,110,180,20);
        this.getSsnNewPasswordLabel().setBounds(80,165,180,20);
        this.getSsnConfirmNewPasswordLabel().setBounds(80,215,180,20);
        this.getSsnOldPasswordTxt().setBounds(270,100,240,40); 
        this.getSsnNewPasswordTxt().setBounds(270,153,240,40);
        this.getSsnConfirmNewPasswordTxt().setBounds(270,205,240,40);  
        this.getSsnChangePasswordBtn().setBounds(214,330,200,40);         
        
    }
    
    private void renderChangePasswordFrame() {
        try {
            this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());
            
            
            this.setResizable(false);
            this.setUndecorated(true);
            
            LineBorder border = new LineBorder(new Color(165,165,165),1);
            this.getRootPane().setBorder(border);  
            
            URL           imgURL = getClass().getResource("/images/bg.jpg");
            BufferedImage image  = ImageIO.read(imgURL);
            Dimension     dim    = SSNConstants.SSN_SCREEN_SIZE;
            this.setBounds(0,0,image.getWidth(),image.getHeight());
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.add(this.getSsnChangePasswordPanel());
            this.setVisible(true);
        }
        catch (IOException ex) {
        }
    }
    
    private void loadAccessToken() {
        SSNUserResponse userProfileData = null;
        
        if(getLoginResponse() != null && getLoginResponse() .getData() != null &&
           getLoginResponse().getData().getUser() != null) {
             userProfileData =  loginResponse.getData().getUser();
             setSsnAccessToken(userProfileData.getAccess_token());
        }
        else {
            
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"!!! Token unavailable !!!","","*** User access token not available  ***");
        }
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
     * @return the ssnChangePasswordPanel
     */
    public JPanel getSsnChangePasswordPanel() {
        return ssnChangePasswordPanel;
    }

    /**
     * @param ssnChangePasswordPanel the ssnChangePasswordPanel to set
     */
    public void setSsnChangePasswordPanel(JPanel ssnChangePasswordPanel) {
        this.ssnChangePasswordPanel = ssnChangePasswordPanel;
    }

    /**
     * @return the ssnChangePasswordTitleLabel
     */
    public JLabel getSsnChangePasswordTitleLabel() {
        return ssnChangePasswordTitleLabel;
    }

    /**
     * @param ssnChangePasswordTitleLabel the ssnChangePasswordTitleLabel to set
     */
    public void setSsnChangePasswordTitleLabel(JLabel ssnChangePasswordTitleLabel) {
        this.ssnChangePasswordTitleLabel = ssnChangePasswordTitleLabel;
    }

    /**
     * @return the ssnChangePasswordCancelLabel
     */
    public JLabel getSsnChangePasswordCancelLabel() {
        return ssnChangePasswordCancelLabel;
    }

    /**
     * @param ssnChangePasswordCancelLabel the ssnChangePasswordCancelLabel to set
     */
    public void setSsnChangePasswordCancelLabel(JLabel ssnChangePasswordCancelLabel) {
        this.ssnChangePasswordCancelLabel = ssnChangePasswordCancelLabel;
    }

    /**
     * @return the changePasswordSeparator
     */
    public JSeparator getChangePasswordSeparator() {
        return changePasswordSeparator;
    }

    /**
     * @param changePasswordSeparator the changePasswordSeparator to set
     */
    public void setChangePasswordSeparator(JSeparator changePasswordSeparator) {
        this.changePasswordSeparator = changePasswordSeparator;
    }

    /**
     * @return the ssnOldPasswordLabel
     */
    public JLabel getSsnOldPasswordLabel() {
        return ssnOldPasswordLabel;
    }

    /**
     * @param ssnOldPasswordLabel the ssnOldPasswordLabel to set
     */
    public void setSsnOldPasswordLabel(JLabel ssnOldPasswordLabel) {
        this.ssnOldPasswordLabel = ssnOldPasswordLabel;
    }

    /**
     * @return the ssnNewPasswordLabel
     */
    public JLabel getSsnNewPasswordLabel() {
        return ssnNewPasswordLabel;
    }

    /**
     * @param ssnNewPasswordLabel the ssnNewPasswordLabel to set
     */
    public void setSsnNewPasswordLabel(JLabel ssnNewPasswordLabel) {
        this.ssnNewPasswordLabel = ssnNewPasswordLabel;
    }

    /**
     * @return the ssnConfirmNewPasswordLabel
     */
    public JLabel getSsnConfirmNewPasswordLabel() {
        return ssnConfirmNewPasswordLabel;
    }

    /**
     * @param ssnConfirmNewPasswordLabel the ssnConfirmNewPasswordLabel to set
     */
    public void setSsnConfirmNewPasswordLabel(JLabel ssnConfirmNewPasswordLabel) {
        this.ssnConfirmNewPasswordLabel = ssnConfirmNewPasswordLabel;
    }

    /**
     * @return the ssnOldPasswordTxt
     */
    public JPasswordField getSsnOldPasswordTxt() {
        return ssnOldPasswordTxt;
    }

    /**
     * @param ssnOldPasswordTxt the ssnOldPasswordTxt to set
     */
    public void setSsnOldPasswordTxt(JPasswordField ssnOldPasswordTxt) {
        this.ssnOldPasswordTxt = ssnOldPasswordTxt;
    }

    /**
     * @return the ssnNewPasswordTxt
     */
    public JPasswordField getSsnNewPasswordTxt() {
        return ssnNewPasswordTxt;
    }

    /**
     * @param ssnNewPasswordTxt the ssnNewPasswordTxt to set
     */
    public void setSsnNewPasswordTxt(JPasswordField ssnNewPasswordTxt) {
        this.ssnNewPasswordTxt = ssnNewPasswordTxt;
    }

    /**
     * @return the ssnConfirmNewPasswordTxt
     */
    public JPasswordField getSsnConfirmNewPasswordTxt() {
        return ssnConfirmNewPasswordTxt;
    }

    /**
     * @param ssnConfirmNewPasswordTxt the ssnConfirmNewPasswordTxt to set
     */
    public void setSsnConfirmNewPasswordTxt(JPasswordField ssnConfirmNewPasswordTxt) {
        this.ssnConfirmNewPasswordTxt = ssnConfirmNewPasswordTxt;
    }

    /**
     * @return the ssnChangePasswordBtn
     */
    public JLabel getSsnChangePasswordBtn() {
        return ssnChangePasswordBtn;
    }

    /**
     * @param ssnChangePasswordBtn the ssnChangePasswordBtn to set
     */
    public void setSsnChangePasswordBtn(JLabel ssnChangePasswordBtn) {
        this.ssnChangePasswordBtn = ssnChangePasswordBtn;
    }

    /**
     * @return the ssnChangePasswordCancelBtn
     */
    public JButton getSsnChangePasswordCancelBtn() {
        return ssnChangePasswordCancelBtn;
    }

    /**
     * @param ssnChangePasswordCancelBtn the ssnChangePasswordCancelBtn to set
     */
    public void setSsnChangePasswordCancelBtn(JButton ssnChangePasswordCancelBtn) {
        this.ssnChangePasswordCancelBtn = ssnChangePasswordCancelBtn;
    }
      
    /**
     * @return the ssnAccessToken
     */
    public String getSsnAccessToken() {
        return ssnAccessToken;
    }

    /**
     * @param ssnAccessToken the ssnAccessToken to set
     */
    public void setSsnAccessToken(String ssnAccessToken) {
        this.ssnAccessToken = ssnAccessToken;
    }

    /**
     * @return the changePasswordController
     */
    public SSNChangePasswordController getChangePasswordController() {
        return changePasswordController;
    }

    /**
     * @param changePasswordController the changePasswordController to set
     */
    public void setChangePasswordController(SSNChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }
    
    // For Development Testing Purpose 
//    public static void main(String[] args) throws Exception {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            @SuppressWarnings("ResultOfObjectAllocationIgnored")
//            public void run() {
//                try {
//                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                        if ("Nimbus".equals(info.getName())) {
//                            UIManager.setLookAndFeel(info.getClassName());
//                            break;
//                        }
//                    }
//                    new SSNChangePasswordForm();
//                } 
//                catch (Exception ex) {
//                }
//            }
//        });
//    }  

    /**
     * @return the changePasswordModel
     */
    public SSNChangePasswordModel getChangePasswordModel() {
        return changePasswordModel;
    }

    /**
     * @param changePasswordModel the changePasswordModel to set
     */
    public void setChangePasswordModel(SSNChangePasswordModel changePasswordModel) {
        this.changePasswordModel = changePasswordModel;
    }
}