package com.ssn.model;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNUserProfileController;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNEditUserProfileForm;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.request.SSNEditUserRequest;
import com.ssn.ws.rest.response.SSNEditUserResponse;
import com.ssn.ws.rest.service.SSNEditUserService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public class SSNEditUserProfileModel {

    private SSNHomeForm homeForm = null;
    private SSNEditUserProfileForm userProfileForm = null;
    private SSNEditUserService ssnEditUserProfileWS = null;
    private SSNUserProfileController userProfileController = null;
    static Logger logger = Logger.getLogger(SSNEditUserProfileModel.class);
    /**
     *
     */
    public SSNEditUserProfileModel() {
    }

    /**
     * Initialize required fields
     * @param userProfileForm
     * @param homeForm
     * @param userProfileController
     */
    public SSNEditUserProfileModel(SSNEditUserProfileForm userProfileForm, SSNHomeForm homeForm, SSNUserProfileController userProfileController) {
        setHomeForm(homeForm);
        setUserProfileForm(userProfileForm);
        setUserProfileController(userProfileController);
    }

    /**
     * Process edit user profile by calling edit user profile 
     * service
     */
    public void updateUserProfile() {
        if (this.getHomeForm().getLoginResponse() == null
                && this.getHomeForm().getSocialModel() != null) {

            String username = getHomeForm().getHomeModel().getLoggedInUserName();
            String firstName = getUserProfileForm().getSsnEditUserProfileFNameTxt().getText();
            String lastName = getUserProfileForm().getSsnEditUserProfileLNameTxt().getText();
            String email = getUserProfileForm().getSsnEditUserProfileEmailTxt().getText();
            String birthDay = getUserProfileForm().getSsnEditUserProfileBirthDayText().getText();
            String gender = getUserProfileForm().getSsnEditUserProfileGenderBox().getSelectedItem().toString();
            String zipCode = getUserProfileForm().getSsnEditUserProfileZipCodeText().getText();

            SSNSocialModel model = new SSNSocialModel();
            model.setUsername(username);
            model.setFirstName(firstName);
            model.setLastName(lastName);
            model.setEmail(email);
            model.setBirthDay(birthDay);
            model.setGender(gender);
            model.setZipCode(zipCode);
            
            boolean success = SSNDao.updateUser(model);

            if (success) {
                this.getHomeForm().setSocialModel(model);
                //JOptionPane.showMessageDialog(getUserProfileForm(), "Profile Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Success","","Profile Updated Successfully");
                clearEditUserProfileFields();
                getUserProfileForm().dispose();
            } else {
                //JOptionPane.showMessageDialog(getUserProfileForm(), "Profile could not be updated", "Error", JOptionPane.ERROR_MESSAGE);
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Profile could not be updated");
                clearEditUserProfileFields();
                getUserProfileForm().dispose();
            }
        } else {

            setSsnEditUserProfileWS(new SSNEditUserService());
            
            SimpleDateFormat  sd    =   new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat  sd1    =   new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
            
            String HOST_NAME = SSNConstants.SSN_WEB_HOST;
            String SERVICE_NAME = "/api/";
            String SERVICE_URI = "users/edit";
            String id = Long.toString(getHomeForm().getHomeModel().getLoggedInUserId());
            String firstName = getUserProfileForm().getSsnEditUserProfileFNameTxt().getText();
            String last_name = getUserProfileForm().getSsnEditUserProfileLNameTxt().getText();
            String email = getUserProfileForm().getSsnEditUserProfileEmailTxt().getText();
            String mobile = getUserProfileForm().getSsnEditUserProfileMobileTxt().getText();
            String birthDay = getUserProfileForm().getSsnEditUserProfileBirthDayText().getText();
            String gender = getUserProfileForm().getSsnEditUserProfileGenderBox().getSelectedItem().toString();
            String zipCode = getUserProfileForm().getSsnEditUserProfileZipCodeText().getText();
            Pattern p = Pattern.compile("\\d{10}");
            Matcher m = p.matcher(mobile.replaceAll("-", ""));
            mobile      = mobile.replaceAll("0", " ");
            
            //String pattern= "[a-zA-Z0-9]{6}";
            
            String pattern= "\\d*";
            Pattern p2 = Pattern.compile(pattern);
            Matcher m2 = p2.matcher(zipCode);
            zipCode     =   zipCode.replaceAll("0", " ");
            
            
            if (!(m.matches())) {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Only 10 digit numbers are allowed for mobile number! ");
            }else if(!(m2.matches())){
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Only digits are allowed for zip code");
            }else if(zipCode.length() < 5 ) {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Minimum 5 digits are allowed for zip code");
            }else if(zipCode.length() > 10 ){
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Maximum 10 digits are allowed  for zip code");
            }else if(zipCode.trim().isEmpty()){
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Invalid Zipcode");
            }else if(!birthDay.matches("\\d{2}-\\d{2}-\\d{4}") ){
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Invalid Birth date");
            }else if(mobile.trim().isEmpty()){
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Invalid Mobile Number");
            }else{
                zipCode     =   zipCode.replaceAll(" ", "0");
                mobile      = mobile.replaceAll(" ", "0");
                Map<String, String> editUserDataMap = new HashMap<String, String>();
                editUserDataMap.put("id", id);
                editUserDataMap.put("first_name", firstName);
                editUserDataMap.put("last_name", last_name);
                editUserDataMap.put("email", email);
                editUserDataMap.put("mobile", mobile);
                try {
                    birthDay = sd.format(sd1.parse(getUserProfileForm().getSsnEditUserProfileBirthDayText().getText()));
                } catch (ParseException ex) {
                    logger.error(ex);
                }
                
                editUserDataMap.put("birth_date", birthDay);
                editUserDataMap.put("gender", gender);
                editUserDataMap.put("zipcode", zipCode);
                SSNEditUserRequest editUserRequest = new SSNEditUserRequest();
                editUserRequest.setRequestParameters(editUserDataMap);

                getSsnEditUserProfileWS().setHostName(HOST_NAME);
                getSsnEditUserProfileWS().setServiceName(SERVICE_NAME);
                getSsnEditUserProfileWS().setRestURI(SERVICE_URI);
                getSsnEditUserProfileWS().setEditUserProfileForm(getUserProfileForm());
                getSsnEditUserProfileWS().setRequest(editUserRequest);
                getSsnEditUserProfileWS().initWSConnection();
                getSsnEditUserProfileWS().prepareRequest(getSsnEditUserProfileWS().getRequest());
                SSNEditUserResponse responseEUP = getSsnEditUserProfileWS().getResponse();

                if (responseEUP != null && responseEUP.isSuccess() && responseEUP.getCode().equalsIgnoreCase("ssn-200")) {
                    //JOptionPane.showMessageDialog(getUserProfileForm(), responseEUP.getMsg(), "success", JOptionPane.INFORMATION_MESSAGE);
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Success","",responseEUP.getMsg());
                    clearEditUserProfileFields();
                    updateUserLoginResponseObject(editUserDataMap);
                    getUserProfileForm().dispose();
                } else if (responseEUP != null && (!responseEUP.isSuccess())) {
                    //JOptionPane.showMessageDialog(getUserProfileForm(), responseEUP.getMsg(), responseEUP.getCode(), JOptionPane.ERROR_MESSAGE);
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","",responseEUP.getMsg());
                   // clearEditUserProfileFields();
                    //getUserProfileForm().dispose();
                } else if (responseEUP == null) {
                    //JOptionPane.showMessageDialog(getUserProfileForm(), "Null Response", "No Response From Service", JOptionPane.ERROR_MESSAGE);
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Null Response","","No Response From Service");
                    clearEditUserProfileFields();
                    getUserProfileForm().dispose();
                }
            }
        }

    }

    private void clearEditUserProfileFields() {
        getUserProfileForm().getSsnEditUserProfileFNameTxt().setText("");
        getUserProfileForm().getSsnEditUserProfileLNameTxt().setText("");
        getUserProfileForm().getSsnEditUserProfileEmailTxt().setText("");
        getUserProfileForm().getSsnEditUserProfileMobileTxt().requestFocus();
        getUserProfileForm().getSsnEditUserProfileBirthDayText().setText("");
        getUserProfileForm().getSsnEditUserProfileGenderBox().setSelectedIndex(0);
        getUserProfileForm().getSsnEditUserProfileZipCodeText().setText("");
    }

    private void updateUserLoginResponseObject(Map<String, String> editUserDataMap) {
        getHomeForm().getLoginResponse().getData().getUser().setFirst_name(editUserDataMap.get("first_name"));
        getHomeForm().getLoginResponse().getData().getUser().setLast_name(editUserDataMap.get("last_name"));
        getHomeForm().getLoginResponse().getData().getUser().setEmail(editUserDataMap.get("email"));
        getHomeForm().getLoginResponse().getData().getUser().setMobile(editUserDataMap.get("mobile"));
        getHomeForm().getLoginResponse().getData().getUser().setBirth_date(editUserDataMap.get("birth_date"));
        getHomeForm().getLoginResponse().getData().getUser().setGender(editUserDataMap.get("gender"));
        getHomeForm().getLoginResponse().getData().getUser().setZipcode(editUserDataMap.get("zipcode"));
    }
//user_profile_picture
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
     * @return the userProfileForm
     */
    public SSNEditUserProfileForm getUserProfileForm() {
        return userProfileForm;
    }

    /**
     * @param userProfileForm the userProfileForm to set
     */
    public void setUserProfileForm(SSNEditUserProfileForm userProfileForm) {
        this.userProfileForm = userProfileForm;
    }

    /**
     * @return the ssnEditUserProfileWS
     */
    public SSNEditUserService getSsnEditUserProfileWS() {
        return ssnEditUserProfileWS;
    }

    /**
     * @param ssnEditUserProfileWS the ssnEditUserProfileWS to set
     */
    public void setSsnEditUserProfileWS(SSNEditUserService ssnEditUserProfileWS) {
        this.ssnEditUserProfileWS = ssnEditUserProfileWS;
    }

    /**
     * @return the userProfileController
     */
    public SSNUserProfileController getUserProfileController() {
        return userProfileController;
    }

    /**
     * @param userProfileController the userProfileController to set
     */
    public void setUserProfileController(SSNUserProfileController userProfileController) {
        this.userProfileController = userProfileController;
    }
}
