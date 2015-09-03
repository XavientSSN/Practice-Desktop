package com.ssn.event.controller;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.app.loader.SSNMainLoader;
import com.ssn.model.SSNChangePasswordModel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNChangePasswordForm;

import com.ssn.ws.rest.service.SSNChangePasswordService;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import org.apache.log4j.Logger;


/**
 *
 * @author vkvarma
 */
public class SSNChangePasswordController extends SSNBaseController {
    private SSNChangePasswordForm       changePasswordForm   = null; 
    private SSNChangePasswordService    ssnChangePassWS      = null;
    private SSNChangePasswordModel      changePasswordModel  = null;
      final    private static Logger logger = Logger.getLogger(SSNChangePasswordController.class);
    public SSNChangePasswordController(SSNChangePasswordForm changePasswordForm) {
        this.setChangePasswordForm(changePasswordForm);
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton jBtn = (JButton) actionEventObj;
            if(jBtn.getActionCommand().equalsIgnoreCase("changeUserPassword")) {   
                String newPassword = changePasswordForm.getSsnNewPasswordTxt().getText();
                if((Character.isWhitespace(newPassword.charAt(newPassword.length()-1)))){
                    this.getChangePasswordModel().processSSNChangePassword();
                }else{
                 //   System.out.println(" Invalid String" );
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed at start and end position");
                }
            }
            else if(jBtn.getActionCommand().equalsIgnoreCase("cancelChangeUserPassword")) {
                getChangePasswordForm().dispose(); 
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) actionEventObj;
            if(jLbl.getName().equalsIgnoreCase("closeChangePassword")) {
                getChangePasswordForm().dispose(); 
            }else  if(jLbl.getName().equalsIgnoreCase("changeUserPassword")) {    
                  String newPassword = changePasswordForm.getSsnNewPasswordTxt().getText();
                //System.out.println("newPassword " + newPassword );
                
                 if(!(Character.isWhitespace(newPassword.charAt(newPassword.length()-1))))
                    this.getChangePasswordModel().processSSNChangePassword();
                 else{
                        // System.out.println(" Invalid String" );
                         SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed at start and end position");
                 }
                    
               //this.getChangePasswordModel().processSSNChangePassword();
            }
            
                
        }
    }
    
//     public String processSSNChangePassword() {        
//        setSsnChangePassWS(new SSNChangePasswordService());         
//        String HOST_NAME    = "http://ssn.xavient.com/";
//        String SERVICE_NAME = "ssn/api/";
//        String SERVICE_URI  = "users/change_password";
//        String accesToken   = getChangePasswordForm().getSsnAccessToken();
//        String oldPassword  = getChangePasswordForm().getSsnOldPasswordTxt().getText();
//        String newPassword  = getChangePasswordForm().getSsnNewPasswordTxt().getText();
//
//        Map<String, String> changePassDataMap = new HashMap<>();
//        changePassDataMap.put("access_token", accesToken);
//        changePassDataMap.put("old_password", oldPassword);
//        changePassDataMap.put("password", newPassword);
//        
//        SSNChangePasswordRequest changePasswordRequest = new SSNChangePasswordRequest();
//        changePasswordRequest.setRequestParameters(changePassDataMap);
//
//        getSsnChangePassWS().setHostName(HOST_NAME);
//        getSsnChangePassWS().setServiceName(SERVICE_NAME);
//        getSsnChangePassWS().setRestURI(SERVICE_URI);        
//        getSsnChangePassWS().setRequest(changePasswordRequest);
//        
//        getSsnChangePassWS().initWSConnection();
//        getSsnChangePassWS().prepareRequest(getSsnChangePassWS().getRequest());
//        
//        SSNChangePasswordResponse responseCP = getSsnChangePassWS().getResponse();
//
//        if(responseCP.isSuccess() && responseCP.getCode().equalsIgnoreCase("ssn-200")) {
//            JOptionPane.showMessageDialog(getChangePasswordForm(),responseCP.getMsg(),"success",JOptionPane.INFORMATION_MESSAGE);
//            clearPasswordFields();
//            getChangePasswordForm().dispose();
//        }
//        else if(!responseCP.isSuccess()) {
//            JOptionPane.showMessageDialog(getChangePasswordForm(),responseCP.getMsg(),responseCP.getCode(),JOptionPane.ERROR_MESSAGE);
//            clearPasswordFields();
//        }
//        return responseCP.getCode();
//     }
    
//    private void clearPasswordFields() {
//        getChangePasswordForm().getSsnOldPasswordTxt().setText("");
//        getChangePasswordForm().getSsnNewPasswordTxt().setText("");
//        getChangePasswordForm().getSsnConfirmNewPasswordTxt().setText("");
//        getChangePasswordForm().getSsnOldPasswordTxt().requestFocus();
//    }
    
    /**
     * @return the changePasswordForm
     */
    public SSNChangePasswordForm getChangePasswordForm() {
        return changePasswordForm;
    }

    /**
     * @param changePasswordForm the changePasswordForm to set
     */
    public void setChangePasswordForm(SSNChangePasswordForm changePasswordForm) {
        this.changePasswordForm = changePasswordForm;
    }

    /**
     * @return the ssnChangePassWS
     */
    public SSNChangePasswordService getSsnChangePassWS() {
        return ssnChangePassWS;
    }

    /**
     * @param ssnChangePassWS the ssnChangePassWS to set
     */
    public void setSsnChangePassWS(SSNChangePasswordService ssnChangePassWS) {
        this.ssnChangePassWS = ssnChangePassWS;
    }

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