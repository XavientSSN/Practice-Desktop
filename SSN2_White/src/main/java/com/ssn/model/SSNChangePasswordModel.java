package com.ssn.model;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.event.controller.SSNChangePasswordController;
import com.ssn.listener.SSNDirExpansionListener;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNChangePasswordForm;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.request.SSNChangePasswordRequest;
import com.ssn.ws.rest.response.SSNChangePasswordResponse;
import com.ssn.ws.rest.service.SSNChangePasswordService;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public class SSNChangePasswordModel {
    
    private SSNHomeForm                   homeForm                 = null;
    private SSNChangePasswordForm         changePasswordForm       = null;
    private SSNChangePasswordService      ssnChangePasswordWS      = null;
    private SSNChangePasswordController   changePasswordController = null;
        static Logger logger = Logger.getLogger(SSNChangePasswordModel.class);
    /**
     *
     */
    public SSNChangePasswordModel() {
    }
    
    /**
     * Initialize the required fields
     * @param changePasswordForm
     * @param homeForm
     * @param changePasswordController
     */
    public SSNChangePasswordModel(SSNChangePasswordForm changePasswordForm,SSNHomeForm homeForm,SSNChangePasswordController  changePasswordController) {
         setHomeForm(homeForm);        
         setChangePasswordForm(changePasswordForm);
         setChangePasswordController(changePasswordController);  
    }
    
    /**
     * To process the change password request by calling the 
     * change password web service
     */
    public void processSSNChangePassword() {        
        setSsnChangePasswordWS(new SSNChangePasswordService());         
        String HOST_NAME    = SSNConstants.SSN_WEB_HOST;
        String SERVICE_NAME = "/api/";
        String SERVICE_URI  = "users/change_password";
        String accesToken   = getChangePasswordForm().getSsnAccessToken();
        String oldPassword  = getChangePasswordForm().getSsnOldPasswordTxt().getText();
        String newPassword  = getChangePasswordForm().getSsnNewPasswordTxt().getText();
        String confirmPassword  = getChangePasswordForm().getSsnConfirmNewPasswordTxt().getText();
        SSNMessageDialogBox dialogBox=  new SSNMessageDialogBox();
        if(StringUtils.isEmpty(oldPassword) )
        {
           // JOptionPane.showMessageDialog(null, "Old Password  should not be blank !", "", JOptionPane.ERROR_MESSAGE);
       
           dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Change Password","","Old Password should not be blank!");
        }
        else if(StringUtils.isEmpty(newPassword) )
        {
            //JOptionPane.showMessageDialog(null, "New Password  should not be blank !", "", JOptionPane.ERROR_MESSAGE);
             dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Change Password","","New Password should not be blank!");
        }else if(StringUtils.isEmpty(confirmPassword) )
        {
            //JOptionPane.showMessageDialog(null, "Confirm Password  should not be blank !", "", JOptionPane.ERROR_MESSAGE);
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Change Password","","Confirm Password should not be blank!");
        }
        else if(!newPassword.equals(confirmPassword))
        {
            
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Change Password","","New Password and Confirm password should be same!");
        }
        else
        {

                Map<String, String> changePassDataMap = new HashMap<>();
                changePassDataMap.put("access_token", accesToken);
                changePassDataMap.put("old_password", oldPassword);
                changePassDataMap.put("password", newPassword);

                SSNChangePasswordRequest changePasswordRequest = new SSNChangePasswordRequest();
                changePasswordRequest.setRequestParameters(changePassDataMap);

                getSsnChangePasswordWS().setHostName(HOST_NAME);
                getSsnChangePasswordWS().setServiceName(SERVICE_NAME);
                getSsnChangePasswordWS().setRestURI(SERVICE_URI);        
                getSsnChangePasswordWS().setRequest(changePasswordRequest);

                getSsnChangePasswordWS().initWSConnection();
                getSsnChangePasswordWS().prepareRequest(getSsnChangePasswordWS().getRequest());

                SSNChangePasswordResponse responseCP = getSsnChangePasswordWS().getResponse();

                if(responseCP != null && responseCP.isSuccess() && responseCP.getCode().equalsIgnoreCase("ssn-200")) {
                   // JOptionPane.showMessageDialog(getChangePasswordForm(),responseCP.getMsg(),"success",JOptionPane.INFORMATION_MESSAGE);
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Change Password","success",responseCP.getMsg());
                    clearPasswordFields();
                    getChangePasswordForm().dispose();
                }
                else if(responseCP != null && (!responseCP.isSuccess())) {
                   // JOptionPane.showMessageDialog(getChangePasswordForm(),responseCP.getMsg(),responseCP.getCode(),JOptionPane.ERROR_MESSAGE);
                     dialogBox.initDialogBoxUI(SSNDialogChoice.ERROR_DIALOG.getType(),"failure",responseCP.getCode(),responseCP.getMsg());
                    clearPasswordFields();
                }
//                else if(responseCP == null) {
//                    //JOptionPane.showMessageDialog(getChangePasswordForm(),"Null Response","No Response From Service",JOptionPane.ERROR_MESSAGE);
//                    dialogBox.initDialogBoxUI(SSNDialogChoice.ERROR_DIALOG.getType(),"Null Response","","No Response From Service");
//                    clearPasswordFields();
//                }
                //return responseCP.getCode();
        
        }
    }
    
    private void clearPasswordFields() {
        getChangePasswordForm().getSsnOldPasswordTxt().setText("");
        getChangePasswordForm().getSsnNewPasswordTxt().setText("");
        getChangePasswordForm().getSsnConfirmNewPasswordTxt().setText("");
        getChangePasswordForm().getSsnOldPasswordTxt().requestFocus();
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
     * @return the ssnChangePasswordWS
     */
    public SSNChangePasswordService getSsnChangePasswordWS() {
        return ssnChangePasswordWS;
    }

    /**
     * @param ssnChangePasswordWS the ssnChangePasswordWS to set
     */
    public void setSsnChangePasswordWS(SSNChangePasswordService ssnChangePasswordWS) {
        this.ssnChangePasswordWS = ssnChangePasswordWS;
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
}