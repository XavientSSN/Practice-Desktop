/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.event.controller.SSNUntaggedMediaController;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNToolBar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author asingh8
 */
public class SSNUntaggedMediaForm  extends JFrame{
    
    JLabel facebookLabel, instagramLabel,moveLabel,importUntaggedMediaHeader,importMediaFromSocialNetworkLabel,notTaggedMediaLabel;
    JPanel ssnUntaggedMediaPanel;
    private SSNUntaggedMediaController ssnUntaggedMediaController = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNUntaggedMediaForm.class);
    public SSNUntaggedMediaForm() {
        
    }
    
    public SSNUntaggedMediaForm(final SSNHomeModel homeModel) {
        
        ssnUntaggedMediaController = new SSNUntaggedMediaController(this, homeModel);
        this.addWindowListener( new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                SSNHomeController.isUnTaggedOpen = false;
                        
                homeModel.getHomeForm().getHomeController().setIconImage(SSNHomeController.currentLabel,"/icon/tagged-untagged-media.png","allUntagged",SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                homeModel.getHomeForm().getHomeController().setIconImage(SSNToolBar.desktopHomeLabel,"/icon/white_icon/home.png","home",SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                }
        } );
        this.initShareForm();
       
    }
    
    private void initShareForm() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
      
        Dimension     dim    =  SSNConstants.SSN_SCREEN_SIZE;
        Icon facebookIcon = new ImageIcon(getClass().getResource("/icon/facebook2.png"));        
        Icon instagramIcon = new ImageIcon(getClass().getResource("/icon/instagram2.png"));       
        Icon moveIcon = new ImageIcon(getClass().getResource("/icon/folder_icon_untagged.png"));
        setSize(500, 500);
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

        
        try {
            URL imgURL = getClass().getResource("/images/untaged_media_bg.png");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnUntaggedMediaPanel(new SSNImagePanel(background));
            
            this.getSsnUntaggedMediaPanel().setLayout(null);
        } catch (Exception ex) {
        }
        
        
        
        
       // JPanel buttonPanel = new JPanel();
        importUntaggedMediaHeader = new JLabel("Import Tag Untagged Media",JLabel.CENTER);
        importUntaggedMediaHeader.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        importUntaggedMediaHeader.setFont(new Font("open sans",Font.BOLD,23));
        importUntaggedMediaHeader.setBounds(50, 15, 400, 50);

        importUntaggedMediaHeader.setBackground(new Color(0,0,0,1));
        
        importUntaggedMediaHeader.setOpaque(true);
        

        importMediaFromSocialNetworkLabel = new JLabel("Import Media From Your Social Networks!");
        importMediaFromSocialNetworkLabel.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        importMediaFromSocialNetworkLabel.setFont(new Font("open sans",Font.BOLD,12));
        importMediaFromSocialNetworkLabel.setBounds(125, 55, 300, 50);
        importMediaFromSocialNetworkLabel.setBackground(new Color(0,0,0,1));
        
        importMediaFromSocialNetworkLabel.setOpaque(true);
        
        
        facebookLabel = new JLabel(facebookIcon);
        facebookLabel.setName("FacebookMedia");
        facebookLabel.addMouseListener(ssnUntaggedMediaController);
        facebookLabel.setBounds(140, 90, 100, 100);
        
        instagramLabel = new JLabel(instagramIcon);
        instagramLabel.setName("InstagramMedia");
        instagramLabel.addMouseListener(ssnUntaggedMediaController);
        instagramLabel.setBounds(280, 90, 100, 100);
        
        notTaggedMediaLabel = new JLabel("Or, view the media that you haven't tagged yet in your Hive");
        
        notTaggedMediaLabel.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        notTaggedMediaLabel.setFont(new Font("open sans",Font.BOLD,12));
        notTaggedMediaLabel.setBounds(75, 190, 400, 50);
        notTaggedMediaLabel.setBackground(new Color(0,0,0,1));
        
        notTaggedMediaLabel.setOpaque(true);
        
        
        
        moveLabel = new JLabel(moveIcon);
        moveLabel.setName("deviceMedia");
        moveLabel.addMouseListener(ssnUntaggedMediaController);
        moveLabel.setBounds(200, 240, 100, 100);

        this.getSsnUntaggedMediaPanel().add(importUntaggedMediaHeader,JLabel.CENTER);
        this.getSsnUntaggedMediaPanel().add(importMediaFromSocialNetworkLabel,JLabel.CENTER);
        this.getSsnUntaggedMediaPanel().add(instagramLabel);
        this.getSsnUntaggedMediaPanel().add(facebookLabel);
        this.getSsnUntaggedMediaPanel().add(notTaggedMediaLabel,JLabel.CENTER);
        this.getSsnUntaggedMediaPanel().add(moveLabel,JLabel.CENTER);

       
        JLabel cancelButton = new JLabel(new ImageIcon(getClass().getResource("/icon/cancel.png")));
        cancelButton.addMouseListener(ssnUntaggedMediaController);
        cancelButton.setName("Cancel");
        cancelButton.setBounds(175,400,155,50);

        this.getSsnUntaggedMediaPanel().add(cancelButton,JLabel.CENTER);

        add(this.getSsnUntaggedMediaPanel());
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setVisible(true);
        this.setIconImage((new ImageIcon(getClass().getResource("/icon/ssn-minimized-icon.png"))).getImage());
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);

    }

    
    public JPanel getSsnUntaggedMediaPanel() {
        return ssnUntaggedMediaPanel;
    }

    public void setSsnUntaggedMediaPanel(JPanel ssnUntaggedMediaPanel) {
        this.ssnUntaggedMediaPanel = ssnUntaggedMediaPanel;
    }

    
    public JLabel getFacebookLabel() {
        return facebookLabel;
    }

    public void setFacebookLabel(JLabel facebookLabel) {
        this.facebookLabel = facebookLabel;
    }


    public JLabel getInstagramLabel() {
        return instagramLabel;
    }

    public void setInstagramLabel(JLabel instagramLabel) {
        this.instagramLabel = instagramLabel;
    }
   
   
    
    
    
    
}
