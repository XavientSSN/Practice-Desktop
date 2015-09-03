package com.ssn.ws.rest.service;

import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNLoginModel;
import com.ssn.model.SSNSocialModel;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.response.SSNLoginResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.client.HttpClientErrorException;

public class LoginWithFacebook {

    private static final String apiKey = SSNConstants.SSN_FACEBOOK_API_KEY;
    private static final String secretKey = SSNConstants.SSN_FACEBOOK_SECRET_KEY;
    private static final String redirectUri = "http://localhost:8000/test";

    private static OAuth2Operations oauthOperations = null;
    private static HttpServer server = null;
    private static FacebookConnectionFactory connectionFactory = null;
    
    public static boolean   deniedPermission =   false;
    private SSNLoginModel loginModel;
    private SSNHomeForm homeForm;
    private Logger logger = Logger.getLogger(LoginWithFacebook.class);

    public LoginWithFacebook(SSNLoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public void login() {
        try {
            server = SSNHttpServer.createSSNHttpServer("Facebook", loginModel, homeForm);
            logger.info("inLoginWithFacebook constructor");
            if (server != null) {
                logger.info("inLoginWithFacebook constructor ++ server not null");
                connectionFactory = new FacebookConnectionFactory(
                        apiKey, secretKey);
                oauthOperations = connectionFactory
                        .getOAuthOperations();
                OAuth2Parameters params = new OAuth2Parameters();
                params.setRedirectUri(redirectUri);
                params.setScope("user_about_me,user_birthday,user_likes,user_status,publish_stream,publish_actions,user_photos,user_videos");
                String authorizeUrl = oauthOperations.buildAuthorizeUrl(
                        GrantType.AUTHORIZATION_CODE, params);
                  logger.info("inLoginWithFacebook constructor ++ redirecting to browser");
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    System.exit(1);
                }

                java.net.URI uri = new java.net.URI(authorizeUrl);
                desktop.browse(uri);
            }

        } catch (URISyntaxException | IOException e) {
           // e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    class FacebookResponseHandler implements HttpHandler {

        private SSNLoginModel loginModel;
        private SSNHomeForm homeForm;

        public FacebookResponseHandler(SSNLoginModel loginModel, SSNHomeForm homeForm) {
            this.loginModel = loginModel;
            this.homeForm = homeForm;
        }

        @Override
        public void handle(HttpExchange t) {
            try {
                logger.info("FacebookResponseHandler Handler ++ recieving response from facebook");
                URI requestURI = t.getRequestURI();
                String code[] = requestURI.toString().split("code=");
                 logger.info("FacebookResponseHandler Handler ++ response " +requestURI.toString());
                //System.out.println("requestURI " + requestURI.toString());
                if(requestURI.toString().contains("error")){
                    deniedPermission = true;
                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"html\" /></head><body><H1 color='red'>User denied for OurHive App permission on facebook</H1></body></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    server.stop(0);

                }else{
                    AccessGrant accessGrant = oauthOperations.exchangeForAccess(
                            code[1], redirectUri, null);
                    String accessToken = accessGrant.getAccessToken();

                    if (accessToken != null && !accessToken.isEmpty()) {
                           if (this.loginModel != null) {
                            this.loginModel.getLoginForm().setVisible(false);
                            storeAccessToken(accessGrant);
                        }

                        Connection<Facebook> facebookConnection = connectionFactory.createConnection(accessGrant);
                        Facebook facebook = facebookConnection.getApi();
                        FacebookProfile userProfile = facebook.userOperations().getUserProfile();

                        if (this.loginModel != null) {
                            SSNLoginResponse loginResponse = this.loginModel.processSSNSocialLogin(userProfile.getName(), "Facebook", userProfile.getId() + "");
                            if (loginResponse != null) {
                                SSNSocialModel model = SSNDao.findUserByUsernname(userProfile.getName()!=null?userProfile.getName():userProfile.getFirstName(), "Facebook");
                                if (model == null) {
                                    model = new SSNSocialModel();
                                    model.setEmail(userProfile.getEmail());
                                    model.setFirstName(userProfile.getFirstName());
                                    model.setLastName(userProfile.getLastName());
                                    model.setUsername(userProfile.getName()!=null?userProfile.getName():userProfile.getFirstName());
                                    model.setAuthoriser("Facebook");

                                    SSNDao.insertUser(model);
                                }

                                homeForm = new SSNHomeForm(this.loginModel.getLoginForm(), null, model, accessGrant, "Facebook");
                                homeForm.setSocialModel(model);
                                homeForm.setLoginResponse(loginResponse);
                                homeForm.setFacebookAccessGrant(accessGrant);
                            }
                        } else {
                            homeForm = this.getHomeForm();
                            homeForm.setFacebookAccessGrant(accessGrant);
                        }
                    }

                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"0; url=http://www.facebook.com/\" /></head></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }

            } catch(HttpClientErrorException ee){
                ee.printStackTrace();
                if(ee.getStatusCode() == HttpStatus.BAD_REQUEST){
                      logger.error(ee.getMessage());
                }
            }catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                
            } finally {
                server.stop(0);
                //System.out.println("exit " );
            }
        }

        public SSNHomeForm getHomeForm() {
            return homeForm;
        }

        public void setHomeForm(SSNHomeForm homeForm) {
            this.homeForm = homeForm;
        }
    }

    private void storeAccessToken(AccessGrant accessGrant) {
        try {
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
            File file = new File(RememberMePath);
           
            if (!file.exists()) {
                file.mkdir();
            }

            String serializationFilePath = RememberMePath + File.separator + SSNConstants.SSN_FACEBOOK_TOKEN + ".ser";
            File serializationFile = new File(serializationFilePath);
            serializationFile.setReadOnly();
            FileOutputStream fout = new FileOutputStream(serializationFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);

            oos.writeObject(accessGrant);
            oos.close();
            fout.close();

        } catch (Exception ex) {
          
            logger.error(ex.getMessage());
        }
    }

    public SSNHomeForm getHomeForm() {
        return homeForm;
    }

    public void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }
}
