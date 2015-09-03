/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ssn.ws.rest.service;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.model.SSNLoginModel;
import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ws.rest.service.LoginWithFacebook.FacebookResponseHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author asingh8
 */
public class SSNHttpServer {

    private static HttpServer server = null;

    public static HttpServer createSSNHttpServer(String handler, SSNLoginModel loginModel, SSNHomeForm homeForm) {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            if (handler != null && handler.equalsIgnoreCase("Facebook")) {
                LoginWithFacebook facebook = new LoginWithFacebook(loginModel);
                server.createContext("/test", facebook.new FacebookResponseHandler(loginModel, homeForm));
                
            } else if (handler != null && handler.equalsIgnoreCase("Twitter")) {
                LoginWithTwitter twitter = new LoginWithTwitter(loginModel);
                server.createContext("/test", twitter.new TwitterResponseHandler(loginModel, homeForm));
            } else if (handler != null && handler.equalsIgnoreCase("Instagram")) {
                LoginWithInstagram instagram = new LoginWithInstagram(loginModel);
                server.createContext("/ssn", instagram.new InstagramResponseHandler(loginModel, homeForm));
            }
           // System.out.println("Currunt time : " +new Date());
            server.start();
      
        } catch (Exception e) {
                 // System.out.println("Currunt time catch: " +new Date());
            if (e.getMessage().equals("Address already in use: bind")) {
                //int optionPane = JOptionPane.showConfirmDialog(null, "Another operation is under process. Would you like to continue?");
                SSNConfirmationDialogBox dialogBox = new SSNConfirmationDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Confirm!!", "", "Another operation is under process. Would you like to continue?");
                int optionPane = dialogBox.getResult();
                if (optionPane == JOptionPane.YES_OPTION) {
                    server.stop(0);
                    createSSNHttpServer(handler, loginModel, homeForm);
                } else {
                    return null;
                }

            }
        }

        return server;
    }

    public static HttpServer getHttpServer() {
        return server;
    }
}
