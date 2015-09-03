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
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

public class LoginWithTwitter {
    
 
    final private static String redirectUrl = "http://localhost:8000/test";
    
    private static OAuth1Operations oauthOperations;
    private static OAuthToken requestToken;
    private static TwitterConnectionFactory connectionFactory;
    private static HttpServer server;
    public static boolean   deniedPermission =   false;
    private SSNLoginModel loginModel;
    private SSNHomeForm homeForm;
    private Logger logger = Logger.getLogger(LoginWithTwitter.class);
    
    public LoginWithTwitter(SSNLoginModel loginModel) {
        this.loginModel = loginModel;
    }
    
    public void login() throws IOException {
        try {
            server = SSNHttpServer.createSSNHttpServer("Twitter", loginModel, homeForm);
            if (server != null) {
                connectionFactory = new TwitterConnectionFactory(SSNConstants.SSN_TWITTER_API_KEY, SSNConstants.SSN_TWITTER_SECRET_KEY);
                oauthOperations = connectionFactory.getOAuthOperations();
                requestToken = oauthOperations.fetchRequestToken(redirectUrl, null);
                String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
                
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    
                    System.exit(1);
                }
                
                java.net.URI uri = new java.net.URI(authorizeUrl);
                desktop.browse(uri);
            }
            
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    class TwitterResponseHandler implements HttpHandler {
        
        private SSNLoginModel loginModel;
        private SSNHomeForm homeForm;
       
        public TwitterResponseHandler(SSNLoginModel loginModel, SSNHomeForm homeForm) {
            this.loginModel = loginModel;
            this.homeForm = homeForm;
        }
        
        public void handle(HttpExchange t) throws IOException {
            
            try {
                URI requestURI = t.getRequestURI();
                String[] oauthVerifier = requestURI.toString().split("oauth_verifier=");
                if(requestURI.toString().contains("denied")){
                    deniedPermission = true;
                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"html\" /></head><body><H1 color='red'>User denied for OurHive App permission on twitter.</H1></body></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    server.stop(0);

                }else{
                    OAuthToken accessToken = oauthOperations
                        .exchangeForAccessToken(new AuthorizedRequestToken(
                                        requestToken, oauthVerifier[1]), null);
                
                    if (accessToken.getValue() != null && !accessToken.getValue().isEmpty()) {
                        if (this.loginModel != null) {
                            this.loginModel.getLoginForm().setVisible(false);
                            storeAccessToken(accessToken);
                        }

                        Twitter twitter = new TwitterTemplate(SSNConstants.SSN_TWITTER_API_KEY, SSNConstants.SSN_TWITTER_SECRET_KEY, accessToken.getValue(), accessToken.getSecret());
                        TwitterProfile userProfile = twitter.userOperations().getUserProfile();

                        if (this.loginModel != null) {
                            SSNLoginResponse loginResponse = this.loginModel.processSSNSocialLogin(userProfile.getName(), "Twitter", userProfile.getId() + "");
                            if (loginResponse != null) {
                                SSNSocialModel model = SSNDao.findUserByUsernname(twitter.userOperations().getScreenName(), "Twitter");
                                if (model == null) {
                                    model = new SSNSocialModel();
                                    model.setUsername(twitter.userOperations().getScreenName());
                                    model.setFirstName(userProfile.getName());
                                    model.setAuthoriser("Twitter");

                                    SSNDao.insertUser(model);
                                }

                                homeForm = new SSNHomeForm(loginModel.getLoginForm(), null, model, accessToken);
                                homeForm.setSocialModel(model);
                                homeForm.setLoginResponse(loginResponse);
                                homeForm.setTwitterOAuthToken(accessToken);
                            }
                        } else {
                            homeForm = this.getHomeForm();
                            homeForm.setTwitterOAuthToken(accessToken);
                        }
                    }
               
                
                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"0; url=http://www.twitter.com/\" /></head></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                 }
            } catch (SQLException | IOException e) {
               logger.error(e.getMessage());
            } finally {
                server.stop(0);
            }
        }
        
        public SSNHomeForm getHomeForm() {
            return homeForm;
        }
        
        public void setHomeForm(SSNHomeForm homeForm) {
            this.homeForm = homeForm;
        }
    }
    
    private void storeAccessToken(OAuthToken oAuthToken) {
        try {
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
            File file = new File(RememberMePath);
            if (!file.exists()) {
                file.mkdir();
            }
            
            String serializationFilePath = RememberMePath + File.separator + SSNConstants.SSN_TWITTER_TOKEN + ".ser";
            File serializationFile = new File(serializationFilePath);
            serializationFile.setReadOnly();
            FileOutputStream fout = new FileOutputStream(serializationFile);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            
            oos.writeObject(oAuthToken);
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
