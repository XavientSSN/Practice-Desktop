/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNShareController;
import com.ssn.model.SSNHomeModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author asingh8
 */
public class SSNShareForm extends JFrame{
    
    JLabel facebookLabel, twitterLabel, instagramLabel, mailLabel,moveLabel;
     private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNShareForm.class);
    private SSNShareController shareController = null;
    
    public SSNShareForm() {
        
    }
    
    public SSNShareForm(SSNHomeModel homeModel,Set<String> files) {
        super("Share");
        shareController = new SSNShareController(this, homeModel,files);
        this.initShareForm();
    }
    
    private void initShareForm() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
       
        Dimension     dim    =   SSNConstants.SSN_SCREEN_SIZE;
        Icon facebookIcon = new ImageIcon(getClass().getResource("/icon/fb-normal.png"));
        Icon twitterIcon = new ImageIcon(getClass().getResource("/icon/twitter-normal.png"));
        Icon mailIcon = new ImageIcon(getClass().getResource("/icon/mail-normal.png"));
        Icon moveIcon = new ImageIcon(getClass().getResource("/icon/move-normal.png"));
        setSize(225, 125);
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        facebookLabel = new JLabel(facebookIcon);
        facebookLabel.setName("FacebookSharing");
        facebookLabel.addMouseListener(shareController);
        
        twitterLabel = new JLabel(twitterIcon);
        twitterLabel.setName("TwitterSharing");
        twitterLabel.addMouseListener(shareController);
        
        mailLabel = new JLabel(mailIcon);
        mailLabel.setName("MailSharing");
        mailLabel.addMouseListener(shareController);
        
        moveLabel = new JLabel(moveIcon);
        moveLabel.setName("moveCopy");
        moveLabel.addMouseListener(shareController);
        buttonPanel.add(facebookLabel);
        buttonPanel.add(twitterLabel);
        buttonPanel.add(mailLabel);
        buttonPanel.add(moveLabel);
        //buttonPanel.add(albumName);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(shareController);
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(cancelButton);


        add(buttonPanel);
        add(southPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public JLabel getFacebookLabel() {
        return facebookLabel;
    }

    public void setFacebookLabel(JLabel facebookLabel) {
        this.facebookLabel = facebookLabel;
    }

    public JLabel getTwitterLabel() {
        return twitterLabel;
    }

    public void setTwitterLabel(JLabel twitterLabel) {
        this.twitterLabel = twitterLabel;
    }

    public JLabel getInstagramLabel() {
        return instagramLabel;
    }

    public void setInstagramLabel(JLabel instagramLabel) {
        this.instagramLabel = instagramLabel;
    }

    public JLabel getMailLabel() {
        return mailLabel;
    }

    public void setMailLabel(JLabel mailLabel) {
        this.mailLabel = mailLabel;
    }

   
   
    
    
    
    
}
