package com.ssn.event.controller;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;

import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNLoginModel;
import com.ssn.ui.custom.component.SSNFileExplorer;
import com.ssn.ui.custom.component.SSNHyperlinkLabel;
import com.ssn.ui.custom.component.SSNIconPasswordField;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ws.rest.response.SSNLoginResponse;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import java.io.IOException;

import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

/**
 *
 * @author vkvarma
 */
public final class SSNLoginController extends SSNBaseController {

    private String lblTextStr = "";
    private SSNLoginForm loginForm = null;
    private SSNLoginModel loginModel = null;
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNLoginController.class);
    public SSNLoginController() {
        super();
    }

    public SSNLoginController(SSNLoginForm loginForm) {
        logger.debug("Start SSNLoginController : ");
        this.setLoginForm(loginForm);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton jBtn = (JButton) actionEventObj;
            if (jBtn.getActionCommand().equalsIgnoreCase("login")) {

                String hiveName = getLoginForm().getSsnHivename().getText();
                String password = getLoginForm().getSsnPassword().getText();
                boolean hiveFlag = false;
                boolean hivePwd = false;

                if (hiveName != null && (!hiveName.equals("")) && hiveName.length() > 0) {
                    hiveName = hiveName.trim();
                    hiveFlag = true;
                }
                if (password != null && (!password.equals("")) && password.length() > 0) {
                    password = password.trim();
                    hivePwd = true;
                }
                if (hiveFlag && hivePwd) {
                    SSNLoginResponse response = this.getLoginModel().processSSNLogin();
                    if (response != null && response.isSuccess()) {
                        Date newDate    =   new Date();
                       
                        if (this.getLoginForm().getSsnRememberMeCheckBox().isSelected()) {

                            getLoginModel().createRememberMe();

                        } else {
                            getLoginModel().deleteRememberMe();
                        }
                         SSNFileExplorer.selectedMedia="home";
                        new SSNHomeForm(this.getLoginForm(), response);
                        Map<String, String> preferences = null;
                        try {
                            preferences = SSNDao.getPreferences();
                        } catch (SQLException ex) {
                            Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (preferences == null || preferences.isEmpty()) {
                            try {
                                SSNDao.savePreferences(new Integer[]{1, 1, 1}, "OurHive", "OurHive");
                            } catch (SQLException ex) {
                                Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                         SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    getLoginForm().setVisible(true);
                                    }
                                
                            });
                        
                    }
                } else {
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Information","",SSNConstants.SSN_LOGIN_FAILED_MESSAGE);
                }

            } else if (jBtn.getActionCommand().equalsIgnoreCase("loginTwitter")) {
                this.getLoginModel().processSSNTwitterLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            } else if (jBtn.getActionCommand().equalsIgnoreCase("loginFacebook")) {
                this.getLoginModel().processSSNFacebookLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            } else if (jBtn.getActionCommand().equalsIgnoreCase("loginInstagram")) {
                this.getLoginModel().processSSNInstagramLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        
        if (mouseEventObj != null && mouseEventObj instanceof SSNHyperlinkLabel ) {
            SSNHyperlinkLabel hLabel = (SSNHyperlinkLabel) mouseEventObj;
            if (hLabel.getLblId().equals("lblForgotPassword")
                    || hLabel.getLblId().equals("lblForgotHivename")
                    || hLabel.getLblId().equals("lblSignup")) {
                if (SSNHelper.isBrowsingSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hLabel.getUri());
                    } catch (IOException ioex) {
                    } catch (Exception ex) {
                    }
                }
            }
        }else if(mouseEventObj != null && mouseEventObj instanceof JLabel){
            JLabel hLabel = (JLabel) mouseEventObj;
           
            if (hLabel.getName().equals("lblForgotPassword")){
                
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI(SSNConstants.SSN_WEB_HOST+"users/forgot_username_password"));
                    } catch (IOException ioex) {
                    } catch (Exception ex) {
                    }
                
            } else if(hLabel.getName().equals("lblForgotHivename")){
                
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI(SSNConstants.SSN_WEB_HOST+"users/forgot_username_password"));
                    } catch (IOException ioex) {
                    } catch (Exception ex) {
                    }
                
            }else if(hLabel.getName().equals("lblSignup")) {
                
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI(SSNConstants.SSN_WEB_HOST+"users/signup"));
                    } catch (IOException ioex) {
                    } catch (Exception ex) {
                    }
                
            }else if  (hLabel.getName().equalsIgnoreCase("home")){
                //
                
            }
            
            if (hLabel.getName().equalsIgnoreCase("login")) {

                String hiveName = getLoginForm().getSsnHivename().getText();
                String password = getLoginForm().getSsnPassword().getText();
                boolean hiveFlag = false;
                boolean hivePwd = false;

                if (hiveName != null && (!hiveName.equals("")) && hiveName.length() > 0) {
                    hiveName = hiveName.trim();
                    hiveFlag = true;
                }
                if (password != null && (!password.equals("")) && password.length() > 0) {
                    password = password.trim();
                    hivePwd = true;
                }
                if (hiveFlag && hivePwd) {
                    SSNLoginResponse response = this.getLoginModel().processSSNLogin();
                    if (response != null && response.isSuccess()) {
                        if (this.getLoginForm().getSsnRememberMeCheckBox().isSelected()) {
                            
                            getLoginModel().createRememberMe();

                        } else {
                            getLoginModel().deleteRememberMe();
                        }
                        SSNFileExplorer.selectedMedia="home";
                        new SSNHomeForm(this.getLoginForm(), response);
                        Map<String, String> preferences = null;
                        try {
                            preferences = SSNDao.getPreferences();
                        } catch (SQLException ex) {
                            Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (preferences == null || preferences.isEmpty()) {
                            try {
                                SSNDao.savePreferences(new Integer[]{1, 1, 1}, "OurHive", "OurHive");
                            } catch (SQLException ex) {
                                Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                         SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    getLoginForm().setVisible(true);
                                    }
                                
                            });
                    }
                } else {
                    //JOptionPane.showMessageDialog(getLoginForm(), " Please enter both \n UserName and Password to login. ", "Information", JOptionPane.INFORMATION_MESSAGE);
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Information","",SSNConstants.SSN_LOGIN_FAILED_MESSAGE);
                }

            }else if (hLabel.getName().equalsIgnoreCase("loginTwitter")) {
                this.getLoginModel().processSSNTwitterLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            } else if (hLabel.getName().equalsIgnoreCase("loginFacebook")) {
                this.getLoginModel().processSSNFacebookLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            } else if (hLabel.getName().equalsIgnoreCase("loginInstagram")) {
                this.getLoginModel().processSSNInstagramLogin(loginModel);
                getLoginForm().getSsnSignInBtn().requestFocus(true);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof SSNHyperlinkLabel) {
            SSNHyperlinkLabel hLabel = (SSNHyperlinkLabel) mouseEventObj;
            if (hLabel.getLblId().equals("lblForgotPassword")
                    || hLabel.getLblId().equals("lblForgotHivename")
                    || hLabel.getLblId().equals("lblSignup")) {
                this.setLblTextStr(hLabel.getText());
                hLabel.setText("<html><u><font color='" + hLabel.getColor() + "'>" + hLabel.getLblText() + "</font></u></html>");
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof SSNHyperlinkLabel) {
            SSNHyperlinkLabel hLabel = (SSNHyperlinkLabel) mouseEventObj;
            if (hLabel.getLblId().equals("lblForgotPassword")
                    || hLabel.getLblId().equals("lblForgotHivename")
                    || hLabel.getLblId().equals("lblSignup")) {
                hLabel.setText(this.getLblTextStr());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void keyTyped(KeyEvent e) {
        Object keyEventObj = e.getSource();
        char c = e.getKeyChar();

        if (keyEventObj != null && keyEventObj instanceof SSNIconTextField) {
            SSNIconTextField iTxt = (SSNIconTextField) keyEventObj;
            if (iTxt.getTxtId() != null && iTxt.getTxtId().equals("hivename")) {
                if (iTxt.getText().trim().equals("") && c == KeyEvent.VK_SPACE) {
                    e.consume();
                }
            }
        } else if (keyEventObj != null && keyEventObj instanceof SSNIconPasswordField) {
            SSNIconPasswordField iTxtPass = (SSNIconPasswordField) keyEventObj;
            if (iTxtPass.getTxtId() != null && iTxtPass.getTxtId().equals("password")) {
                if (iTxtPass.getText().trim().equals("") && c == KeyEvent.VK_SPACE) {
                    e.consume();
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object keyEventObj = e.getSource();
        char c = e.getKeyChar();

        if (keyEventObj != null && (keyEventObj instanceof JButton || keyEventObj instanceof JPasswordField)) {
            if (c == KeyEvent.VK_ENTER) {
                SSNLoginResponse response = this.getLoginModel().processSSNLogin();
                if (response != null && response.isSuccess()) {
                    if (this.getLoginForm().getSsnRememberMeCheckBox().isSelected()) {

                        getLoginModel().createRememberMe();

                    } else {
                        getLoginModel().deleteRememberMe();
                    }
                     SSNFileExplorer.selectedMedia="home";
                    new SSNHomeForm(this.getLoginForm(), response);
                    Map<String, String> preferences = null;
                    try {
                        preferences = SSNDao.getPreferences();
                    } catch (SQLException ex) {
                        Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (preferences == null || preferences.isEmpty()) {
                        try {
                            SSNDao.savePreferences(new Integer[]{1, 1, 1}, "OurHive", "OurHive");
                        } catch (SQLException ex) {
                            Logger.getLogger(SSNLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    getLoginForm().setVisible(true);
                }
            }
        }
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
           windowActivated(e) ;
    }

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e){}

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
     * @return the lblTextStr
     */
    public String getLblTextStr() {
        return lblTextStr;
    }

    /**
     * @param lblTextStr the lblTextStr to set
     */
    public void setLblTextStr(String lblTextStr) {
        this.lblTextStr = lblTextStr;
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
}
