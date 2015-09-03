package com.ssn.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.event.controller.SSNLoginController;
import com.ssn.helper.SSNHelper;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ws.rest.request.SSNLoginRequest;
import com.ssn.ws.rest.response.SSNLoginResponse;
import com.ssn.ws.rest.service.LoginWithFacebook;
import com.ssn.ws.rest.service.LoginWithInstagram;
import com.ssn.ws.rest.service.LoginWithTwitter;
import com.ssn.ws.rest.service.SSNLoginService;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author vkvarma
 */
public class SSNLoginModel {

    private SSNLoginForm loginForm = null;
    private SSNLoginController loginController = null;
    private SSNLoginService ssnLoginWS = null;
    private Logger logger = Logger.getLogger(SSNLoginModel.class);

    /**
     *
     */
    public SSNLoginModel() {
    }

    /**
     * Initialize required fields
     * @param loginForm
     * @param loginController
     */
    public SSNLoginModel(SSNLoginForm loginForm, SSNLoginController loginController) {
        setLoginForm(loginForm);
        setLoginController(loginController);
    }

    /**
     * Process login request by calling login service with user name and password 
     * provided by user
     * @return
     */
    public SSNLoginResponse processSSNLogin() {
        SSNLoginResponse response = null;
        Date newDate    =   new Date();
    
        try{
            this.setSsnLoginWS(new SSNLoginService());
            String HOST_NAME = SSNConstants.SSN_WEB_HOST;
            String SERVICE_NAME = "api/";
            String SERVICE_URI = "users/login";
            String hiveName = getLoginForm().getSsnHivename().getText();
            String password = getLoginForm().getSsnPassword().getText();
            
            if (hiveName != null && (!hiveName.equals("")) && hiveName.length() > 0) {
                hiveName = hiveName.trim();
            }
            if (password != null && (!password.equals("")) && password.length() > 0) {
                password = password.trim();
            }

            Map<String, String> loginDataMap = new HashMap<>();
            loginDataMap.put("user_name", hiveName);
            loginDataMap.put("password", password);
            loginDataMap.put("device_type", SSNHelper.getDeviceType());
            
            SSNLoginRequest loginRequest = new SSNLoginRequest();
            loginRequest.setRequestParameters(loginDataMap);
            
           
            getSsnLoginWS().setHostName(HOST_NAME);
            getSsnLoginWS().setServiceName(SERVICE_NAME);
            getSsnLoginWS().setRestURI(SERVICE_URI);
            getSsnLoginWS().setLoginForm(this.getLoginForm());
            getSsnLoginWS().setRequest(loginRequest);
            getSsnLoginWS().initWSConnection();
            getSsnLoginWS().prepareRequest(getSsnLoginWS().getRequest());
            response = getSsnLoginWS().getResponse();
            System.out.println("response ");
            if (response != null && response.isSuccess() && response.getCode().equalsIgnoreCase("ssn-200")) {
                  
                this.getLoginForm().setVisible(false);
                return response;
            } else if (response != null && !response.isSuccess()) {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Alert","",response.getMsg());
                this.getLoginForm().getSsnPassword().setText("");
                this.getLoginForm().getSsnHivename().setText("");
                this.getLoginForm().getSsnHivename().requestFocus(true);
                
            } else if (response == null) {
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
		dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"No Response From Service","","Server not responding!!");
                this.getLoginForm().getSsnPassword().setText("");
                this.getLoginForm().getSsnHivename().setText("");
                this.getLoginForm().getSsnHivename().requestFocus(true);
            }
           Date newDate1    =   new Date();
           
        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
        return response;
    }

    /**
     * Process login with twitter account
     * @param loginModel
     */
    public void processSSNTwitterLogin(SSNLoginModel loginModel) {
        try {
            new LoginWithTwitter(loginModel).login();
        } catch (Exception e) {
           logger.error(e);
        }
    }

    /**
     * Process login with facebook  account
     * @param loginModel
     */
    public void processSSNFacebookLogin(SSNLoginModel loginModel) {
        try {
            new LoginWithFacebook(loginModel).login();
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    /**
     * Process the login with social media account information
     * @param firstName
     * @param proider
     * @param id
     * @return
     */
    public SSNLoginResponse processSSNSocialLogin(String firstName, String proider, String id) {
        SSNLoginResponse loginResponse = null;
        
        try {
            URL url = new URL(SSNConstants.SSN_WEB_HOST+"api/users/sociallogin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String input = "first_name=%s&social_provider=%s&social_id=%s&device_type=%s";
            
            input = String.format(input, URLEncoder.encode(firstName, "UTF-8"), proider, id,SSNHelper.getDeviceType());

            OutputStream os = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(input);
            writer.close();
            os.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                if(success) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    loginResponse = gson.fromJson(response.toString(), SSNLoginResponse.class);
                }
            }

            

            conn.disconnect();
        } catch (Exception e) {
            logger.error(e);
        }
        return loginResponse;
    }

    /**
     * Process login with instagram
     * @param loginModel
     */
    public void processSSNInstagramLogin(SSNLoginModel loginModel) {
        try {
            new LoginWithInstagram(loginModel).login();
        } catch(Exception e) {
           logger.error(e);
        }
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
     * @return the ssnLoginWS
     */
    public SSNLoginService getSsnLoginWS() {
        return ssnLoginWS;
    }

    /**
     * @param ssnLoginWS the ssnLoginWS to set
     */
    public void setSsnLoginWS(SSNLoginService ssnLoginWS) {
        this.ssnLoginWS = ssnLoginWS;
    }

    /**
     * To create the remember me credentials and save them in a file 
     * in encrypted format
     * @return
     */
    public boolean createRememberMe() {
        String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
        String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
        File file = new File(RememberMePath);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(RememberMePath + File.separator + this.getLoginForm().getSsnHivename().getText() + ".txt");
        if (file2.exists()) {
            try {
                file2.delete();
                file2.createNewFile();
                PrintWriter writer = new PrintWriter(file2.getAbsolutePath(), "UTF-8");
                writer.println(SSNHelper.encrypt(this.getLoginForm().getSsnHivename().getText() + " " + this.getLoginForm().getSsnPassword().getText()));
                file2.setReadOnly();
                writer.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        } else {
            try {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    for (File f : files) {
                        f.delete();
                    }
                }
                file2.createNewFile();
                PrintWriter writer = new PrintWriter(file2.getAbsolutePath(), "UTF-8");
                writer.println(SSNHelper.encrypt(this.getLoginForm().getSsnHivename().getText() + " " + this.getLoginForm().getSsnPassword().getText()));
                file2.setReadOnly();
                writer.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        return true;
    }

    /**
     * To delete the remember me credentials saved in the file 
     * in encrypted format
     * @return
     */
    public boolean deleteRememberMe() {
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
        return true;
    }
}
