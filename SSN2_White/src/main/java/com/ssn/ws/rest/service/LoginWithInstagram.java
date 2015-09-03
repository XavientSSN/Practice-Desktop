/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.instagram.connect.InstagramConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author asingh8
 */
public class LoginWithInstagram {

   
    private static final String redirectUri = "http://localhost:8000/ssn";
    public static boolean deniedInstagramPermission = false;
    private static HttpServer server = null;
    private static OAuth2Operations oauthOperations = null;
    private static InstagramConnectionFactory connectionFactory = null;
 
    private SSNLoginModel loginModel;
    private SSNHomeForm homeForm;
    private Logger logger = Logger.getLogger(LoginWithInstagram.class);
    public LoginWithInstagram(SSNLoginModel loginModel) {
        this.loginModel = loginModel;
    }
    
    public LoginWithInstagram(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }
    
    public void login() {
        try {
            server = SSNHttpServer.createSSNHttpServer("Instagram", loginModel, homeForm);
            if(server != null) {

                connectionFactory = new InstagramConnectionFactory(
                        SSNConstants.SSN_INSTAGRAM_CLIENT_ID, SSNConstants.SSN_INSTAGRAM_CLIENT_SECRET);
                oauthOperations = connectionFactory
                        .getOAuthOperations();
                OAuth2Parameters params = new OAuth2Parameters();
                params.setRedirectUri(redirectUri);
                String authorizeUrl = oauthOperations.buildAuthorizeUrl(
                        GrantType.AUTHORIZATION_CODE, params);

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

    class InstagramResponseHandler implements HttpHandler {
        
        private SSNLoginModel loginModel;
        private SSNHomeForm homeForm;
        
        public InstagramResponseHandler(SSNLoginModel loginModel, SSNHomeForm homeForm) {
            this.loginModel = loginModel;
            this.homeForm = homeForm;
        }

        @Override
        public void handle(HttpExchange t) {
            try {
                URI requestURI = t.getRequestURI();
               
                String code[] = requestURI.toString().split("code=");
                //System.out.println("requestURI  "+requestURI.toString());
                if(!requestURI.toString().contains("error")){
                    MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
                    mvm.add("client_id", SSNConstants.SSN_INSTAGRAM_CLIENT_ID);
                    mvm.add("client_secret", SSNConstants.SSN_INSTAGRAM_CLIENT_SECRET);
                    mvm.add("grant_type", "authorization_code");
                    mvm.add("redirect_uri", redirectUri);
                    mvm.add("code", code[1]);

                    RestTemplate restTemplate = new RestTemplate();
                    final List<HttpMessageConverter<?>> listHttpMessageConverters = new ArrayList< HttpMessageConverter<?>>();

                    listHttpMessageConverters.add(new FormHttpMessageConverter());
                    listHttpMessageConverters.add(new StringHttpMessageConverter());
                    restTemplate.setMessageConverters(listHttpMessageConverters);

                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(mvm, requestHeaders);
                    ResponseEntity responseEntity = restTemplate.exchange("https://api.instagram.com/oauth/access_token", HttpMethod.POST, requestEntity, String.class);

                    String body = (String) responseEntity.getBody();
                    String accessToken = null;

                    JsonFactory jfactory = new JsonFactory();
                    JsonParser jParser = jfactory.createParser(body);
                    while (jParser.nextToken() != JsonToken.END_OBJECT) {

                        String fieldname = jParser.getCurrentName();
                        if ("access_token".equals(fieldname)) {
                            jParser.nextToken();
                            accessToken = jParser.getText();

                        }

                    }
                    jParser.close();

                    AccessGrant accessGrant = new AccessGrant(accessToken, null, null, null);


                    if (accessToken != null && !accessToken.isEmpty()) {
                        if(this.loginModel != null) {
                            this.loginModel.getLoginForm().setVisible(false);
                            storeAccessToken(accessGrant);
                        }

                        URL url = new URL(String.format("https://api.instagram.com/v1/users/self?access_token=%s", accessToken));

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Accept", "application/json");

                        String username = "", fullName = "", id = "";
                        if (conn.getResponseCode() == 200) {
                            StringBuilder builder = new StringBuilder();
                            BufferedReader br = new BufferedReader(new InputStreamReader(
                                    (conn.getInputStream())));

                            String output;
                            while ((output = br.readLine()) != null) {
                                builder.append(output);
                            }

                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> outputJSON = mapper.readValue(builder.toString(), Map.class);

                            Map<String, Object> data = (Map<String, Object>) outputJSON.get("data");
                            username = (String) data.get("username");
                            fullName = (String) data.get("full_name");
                            id = (String) data.get("id");
                        }

                        if (this.loginModel != null) {
                            SSNLoginResponse loginResponse = this.loginModel.processSSNSocialLogin(fullName, "Instagram", id);
                            if (loginResponse != null) {
                                SSNSocialModel model = SSNDao.findUserByUsernname(username, "Instagram");
                                if (model == null) {
                                    model = new SSNSocialModel();
                                    model.setFirstName(fullName);
                                    model.setUsername(username);
                                    model.setAuthoriser("Instagram");

                                    SSNDao.insertUser(model);
                                }


                                homeForm = new SSNHomeForm(this.loginModel.getLoginForm(), null, model, accessGrant, "Instagram");
                                homeForm.setSocialModel(model);
                                homeForm.setLoginResponse(loginResponse);
                                homeForm.setInstagramAccessGrant(accessGrant);
                            }
                        } else {
                            homeForm = this.getHomeForm();
                            homeForm.setInstagramAccessGrant(accessGrant);
                        }
                    }

                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"0; url=http://www.instagram.com/\" /></head></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }else{
                    deniedInstagramPermission = true;
                    String response = "<html><head><meta http-equiv=\"refresh\" content=\"html\" /></head><body><H1 color='red'>User denied for OurHive App permission on Instagram</H1></body></html>";
                    t.sendResponseHeaders(200, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    server.stop(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
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
    
    private void storeAccessToken(AccessGrant accessGrant) {
        try {
            String rootPath = SSNHelper.getSsnWorkSpaceDirPath();
            String RememberMePath = rootPath + File.separator + SSNConstants.SSN_REMMEBER_ME_DIRECTORY;
            File file = new File(RememberMePath);
            
            if (!file.exists()) {
                file.mkdir();
            }
            
            String serializationFilePath = RememberMePath + File.separator + SSNConstants.SSN_INSTAGRAM_TOKEN + ".ser";
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
}
