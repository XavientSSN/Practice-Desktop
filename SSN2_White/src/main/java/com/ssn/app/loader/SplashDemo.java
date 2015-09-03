/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.app.loader;

import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNLoginModel;
import com.ssn.model.SSNSocialModel;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import com.ssn.ws.rest.response.SSNLoginResponse;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 *
 * @author hvashistha
 */
public class SplashDemo {

    private static SplashScreen mySplash;
    private static java.awt.geom.Rectangle2D.Double splashTextArea, splashProgressArea, splashRightsArea, splashVersionArea, splashPercentArea,splashLoadingArea;
    private static java.awt.Graphics2D splashGraphics;
    private static java.awt.Font font1, font2;
    private final static String loadingText =   "Loading...";
    //private final static String copyRightText = "\u00A9" + "Smart Screen Network.All rights reserved.";
   private final static String versionAreaText = "Ver.1.0.11 build 2015";
    private final static String copyRightText   =   "Copyright 2014";
    private final static Logger logger = Logger.getLogger(SplashDemo.class);

    /**
     * @param args the command line arguments
     */
    //public static void main(String[] args) {
    public SplashDemo() {
        // TODO code application logic here
        logger.info("SplashDemo Start : ");
        splashInit();           // initialize splash overlay drawing parameters
        appInit();              // simulate what an application would do 
        // before starting
        HttpURLConnection conn = null;
        boolean isLoggedIn = SSNHelper.isLoggedInWithSocial();
        if (isLoggedIn) {
            Map<String, Object> map = SSNHelper.deserializeAccessToken();
            SSNLoginModel loginModel = new SSNLoginModel();
            if (map.keySet().contains("Facebook")) {
                AccessGrant accessGrant = (AccessGrant) map.get("Facebook");

                FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(
                        SSNConstants.SSN_FACEBOOK_API_KEY, SSNConstants.SSN_FACEBOOK_SECRET_KEY);
                Connection<Facebook> facebookConnection = connectionFactory.createConnection(accessGrant);
                Facebook facebook = facebookConnection.getApi();
                FacebookProfile userProfile = facebook.userOperations().getUserProfile();

                SSNSocialModel model = null;
                try {
                    model = SSNDao.findUserByUsernname(userProfile.getName(), "Facebook");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SplashDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                SSNLoginResponse loginResponse = loginModel.processSSNSocialLogin(userProfile.getFirstName(), "Facebook", userProfile.getId() + "");

                SSNHomeForm homeForm = new SSNHomeForm(null, loginResponse, model, accessGrant, "Facebook");
                // homeForm.setFacebookAccessGrant(accessGrant);
            } else if (map.keySet().contains("Twitter")) {
                OAuthToken oAuthToken = (OAuthToken) map.get("Twitter");

                Twitter twitter = new TwitterTemplate(SSNConstants.SSN_TWITTER_API_KEY,
                        SSNConstants.SSN_TWITTER_SECRET_KEY,
                        oAuthToken.getValue(), oAuthToken.getSecret());
                TwitterProfile userProfile = twitter.userOperations().getUserProfile();

                SSNSocialModel model = null;
                try {
                    model = SSNDao.findUserByUsernname(twitter.userOperations().getScreenName(), "Twitter");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SplashDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                SSNLoginResponse loginResponse = loginModel.processSSNSocialLogin(userProfile.getName(), "Twitter", userProfile.getId() + "");

                SSNHomeForm homeForm = new SSNHomeForm(null, loginResponse, model, oAuthToken);
                // homeForm.setTwitterOAuthToken(oAuthToken);
            } else if (map.keySet().contains("Instagram")) {
                try {
                    AccessGrant accessGrant = (AccessGrant) map.get("Instagram");

                    URL url = new URL(String.format("https://api.instagram.com/v1/users/self?access_token=%s", accessGrant.getAccessToken()));
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    String username = "", fullName = "", id = "";
                    if (conn.getResponseCode() == 200) {
                        StringBuilder builder = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

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
                        br.close();
                    }

                    SSNSocialModel model = SSNDao.findUserByUsernname(username, "Instagram");
                    SSNLoginResponse loginResponse = loginModel.processSSNSocialLogin(fullName, "Instagram", id);

                    SSNHomeForm homeForm = new SSNHomeForm(null, loginResponse, model, accessGrant, "Instagram");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                    
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SplashDemo.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    
                }
            }

        } else {
            new SSNLoginForm();
        }

        //new SSNSplashScreen(5000,750,477);  
        if (mySplash != null) // check if we really had a spash screen
        {
            if(mySplash.isVisible())
            mySplash.close();   // if so we're now done with it
        }
        // begin with the interactive portion of the program

    }

    /**
     * Prepare the global variables for the other splash functions
     */
    private static void splashInit() {
        logger.debug("Start splashInit() ");
        mySplash = SplashScreen.getSplashScreen();

        if (mySplash != null) {   // if there are any problems displaying the splash this will be null
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(width / 2 + 80, height / 2 - 130, width * .27, 25.);
            splashProgressArea = new Rectangle2D.Double(width / 2 - 253, height - 129, width / 2 + 163, 3);
            splashRightsArea = new Rectangle2D.Double(width / 2 - 240, height / 2 + 110, width * .27, 25.);
            splashVersionArea = new Rectangle2D.Double(width / 2 - 240, height / 2 - 110, width * .27, 25.);
            splashPercentArea = new Rectangle2D.Double(width / 2 - 13, height / 2 + 60, width * .27, 25.);
            splashLoadingArea = new Rectangle2D.Double(width / 2 - 240, height / 2 - 110, width * .27, 25.);
            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font1 = new Font("open sans", Font.PLAIN, 11);
            font2 = new Font("open sans", Font.PLAIN, 10);

            splashGraphics.setFont(font1);
            splashGraphics.setFont(font2);
            
            // initialize the status info
            splashProgress(0);
        }
    }

    /**
     * just a stub to simulate a long initialization task that updates the text
     * and progress parts of the status in the Splash
     */
    private static void appInit() {
        splashText("Initializing Server Behaviours...");
        splashText(copyRightText);
        splashText(versionAreaText);
        splashText(loadingText);
        for (int i = 0; i <= 2; i++) {
            int pctDone = i * 50;
            splashText(pctDone + "%");
            // splashProgress(pctDone);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage());
            }
        }

    }

    /**
     * Display text in status area of Splash. Note: no validation it will fit.
     *
     * @param str - text to be displayed
     */
    public static void splashText(String str) {
        if (mySplash != null && mySplash.isVisible()) {

            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // draw the text
            splashGraphics.setPaint(new Color(140, 137, 132));

            if (str.equalsIgnoreCase("Initializing Server Behaviours...")) {
                splashGraphics.setFont(font1);
                //splashGraphics.drawString(str, (int) (splashTextArea.getX() + 10), (int) (splashTextArea.getY() + 15));
            }
            if (str.equalsIgnoreCase("Copyright 2014")) {
                splashGraphics.setFont(new Font("open sans", Font.PLAIN,14));
                splashGraphics.setColor(new Color(225,225,225));
                splashGraphics.drawString(str, (int) (splashRightsArea.getX() + 360), (int) (splashRightsArea.getY() + 110));
            }
            if (str.equalsIgnoreCase("Ver.1.0.11 build 2015")) {
                splashGraphics.setFont(new Font("open sans", Font.PLAIN,14));
                splashGraphics.setColor(new Color(225,225,225));
                splashGraphics.drawString(str, (int) (splashVersionArea.getX() + 20), (int) (splashVersionArea.getY() + 330));
            }
            if (str.equalsIgnoreCase("Loading...")) {
                splashGraphics.setFont(new Font("open sans", Font.BOLD,12));
                splashGraphics.setColor(new Color(225,225,225));
                splashGraphics.drawString(str, (int) (splashLoadingArea.getX() +220), (int) (splashLoadingArea.getY() + 240));
            }
            if (str.endsWith("%")) {
                splashGraphics.setFont(new Font("open sans", Font.PLAIN,14));
                splashGraphics.setBackground(new Color(22, 21, 21,1));
//                splashGraphics.setFont(new Font("open sans", Font.PLAIN,9));
//                splashGraphics.setBackground(new Color(22, 21, 21,1));
                splashGraphics.setColor(new Color(225,225,225));
                splashGraphics.clearRect(width / 2 - 16, height / 2 + 150, (int) (width * .08), (int) 21.);
                splashGraphics.drawString(str, (int) (splashPercentArea.getX() + 10), (int) (splashPercentArea.getY() + 105));
            }

            // make sure it's displayed
            mySplash.update();
        }
    }

    /**
     * Display a (very) basic progress bar
     *
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct) {
        if (mySplash != null && mySplash.isVisible()) {
            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct * wid / 100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid - 1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(new Color(255, 209, 25));
            splashGraphics.fillRect(x, y + 1, doneWidth, hgt - 1);

            // make sure it's displayed
            mySplash.update();
        }
    }

}
