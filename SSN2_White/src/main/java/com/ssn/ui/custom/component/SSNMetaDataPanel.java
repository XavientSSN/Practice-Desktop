package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.helper.SSNGalleryHelper;
import com.ssn.helper.SSNHelper;
import com.ssn.listener.SSNMetadataListener;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import com.ssn.speech.AudioPlayer;
import com.ssn.speech.AudioRecorder;
import com.ssn.speech.VoiceCommandController;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.Player;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.border.Border;

/**
 *
 * @author pkumar2
 */
public class SSNMetaDataPanel extends JPanel {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNMetaDataPanel.class);
    JLabel panelHeading;
    //private JLabel photoGrapher;
    private JLabel fileName;
    JLabel title;
    JLabel type;
    JLabel location;
    private JLabel address;
    JLabel comments;
    JLabel created;
    JLabel modified;
    private JLabel tag;
    private JLabel faceTag;
    //JLabel ratings;
   // JLabel ratingValidationMessage;

    private JLabel editLabel;

    private JLabel fileNameTxt = null;
    //private JTextField photoGrapherTxt = null;
    JTextField titleTxt = null;
    JLabel typeTxt = null;
    JTextField locationTxt = null;
    private JLabel addressTxt = null;
    JTextField commentsTxt = null;
    JTextField createdTxt = null;
    JTextField modifiedTxt = null;
    private JTextField tagText=null;
    private JTextField facetagTxt=null;
  //  JTextField ratingsTxt = null;

    private JLabel fileInfoLabel = null;
    ButtonGroup voiceTagBtnGroup = null;
    TargetDataLine targetDataLine = null;
    private SSNHomeForm homeForm = null;
    private String mediaFileLocation;
    private String mediaFileName;
    
    private boolean checkWronEntry=false;
    private String mediaLocation;
   

    public SSNMetaDataPanel(SSNGalleryMetaData galleryData, boolean isFolderProps, SSNHomeForm homeForm) {
        this.setHomeForm(homeForm);
        
        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } 
        if (isFolderProps) {
            this.removeAll();
            initMetadataGUIComponentsFolder(galleryData);
            this.revalidate();
        } else {
            this.removeAll();
            initMetadataGUIComponents(galleryData);
            this.revalidate();
        }
        this.repaint();
    }

    public void initMetadataGUIComponentsFolder(SSNGalleryMetaData galleryData) {
  

        Border paddingBorder = BorderFactory.createEmptyBorder(9, 5, 9, 10);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
   
        panelHeading = new JLabel("PROPERTIES");
        panelHeading.setForeground(Color.WHITE);
        panelHeading.setFont(new Font("open sans", Font.BOLD, 12));

        title = new JLabel("NAME : ");
        title.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        title.setFont(new Font("open sans", Font.BOLD, 11));

        location = new JLabel("LOCATION : ");
        location.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        location.setFont(new Font("open sans", Font.BOLD, 11));

        type = new JLabel("SIZE : ");
        type.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        type.setFont(new Font("open sans", Font.BOLD, 11));
        
        comments = new JLabel("CONTAINS : ");
        comments.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        comments.setFont(new Font("open sans", Font.BOLD, 11));
        
        modified = new JLabel("MODIFIED : ");
        modified.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        modified.setFont(new Font("open sans", Font.BOLD, 11));
        
        String title    =   galleryData.getTitle()!=null?galleryData.getTitle():"";
        JLabel titleTxt = new JLabel(title);
        //titleTxt.setEnabled(false);
        titleTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        titleTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        titleTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        titleTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        if(!title.isEmpty())
        {
        titleTxt.setToolTipText(title);
        }
        
        typeTxt = new JLabel(galleryData.getSize() + " MB");
        typeTxt.setToolTipText(galleryData.getSize() + " MB");
        typeTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        typeTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        typeTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        typeTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        mediaLocation    =   "";
        if(galleryData.getMediaLocation() != null)
        {
            mediaLocation   =   galleryData.getMediaLocation();
        }
        JLabel locationTxt = new JLabel(mediaLocation);
        //locationTxt.setEnabled(false);
        locationTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        locationTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        locationTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        locationTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        if(!mediaLocation.isEmpty())
         locationTxt.setToolTipText(mediaLocation);

        //int validFileCount = (galleryData.getNoOfFiles() -SSNGalleryHelper.curruptFileCount); 

        JLabel commentsTxt = new JLabel(galleryData.getNoOfFiles() + " Files," + galleryData.getNoOfFolders() + " Folders");
        //commentsTxt.setEnabled(false);
        commentsTxt.setToolTipText(galleryData.getNoOfFiles() + " Files," + galleryData.getNoOfFolders() + " Folders");
        commentsTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        commentsTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        commentsTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        commentsTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        
      
        JLabel modifiedTxt = null;
        modifiedTxt = new JLabel(SSNScheduleTagPanelForm.getFormatedDateTime(galleryData.getModiFied()));
        //modifiedTxt.setEnabled(false);
        if(galleryData.getModiFied() != null && !galleryData.getModiFied().isEmpty())
        {
            modifiedTxt.setToolTipText(galleryData.getModiFied());
        }
        modifiedTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        modifiedTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        modifiedTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        modifiedTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        editLabel = new JLabel();
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder((BorderFactory.createEmptyBorder(11, 2, 11, 5)));
        northPanel.setSize(200, 10);
        northPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        northPanel.add(panelHeading, BorderLayout.WEST);
        northPanel.add(editLabel, BorderLayout.EAST);

        JPanel northSouthPanel = new JPanel(new BorderLayout());
        northSouthPanel.setSize(200, 10);
        northSouthPanel.setBorder((BorderFactory.createEmptyBorder(0, 10, 10, 10)));
        northSouthPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
       

        JPanel mainPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(boxLayout);
        mainPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        mainPanel.add(northPanel);
        mainPanel.add(northSouthPanel);
        mainPanel.add(populateFolderSouthPanel(titleTxt, locationTxt, commentsTxt, modifiedTxt));


        setLayout(new BorderLayout());
        setSize(200, 60);
        setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        add(mainPanel, BorderLayout.CENTER);


    }

    private void initMetadataGUIComponents(final SSNGalleryMetaData galleryData) {
      

        setMediaFileLocation(galleryData.getMediaFileLocation());
        setMediaFileName(galleryData.getMediaFileName());
        mediaLocation    =   "";
        if(galleryData.getMediaLocation() != null)
        {
                mediaLocation   =   galleryData.getMediaLocation();
        }
        Border paddingBorder = BorderFactory.createEmptyBorder(9, 5, 9, 10);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        panelHeading = new JLabel("PROPERTIES");
        panelHeading.setForeground(Color.WHITE);
        panelHeading.setFont(new Font("open sans", Font.BOLD, 12));

        setFileName(new JLabel("FILE NAME : "));
        getFileName().setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        getFileName().setFont(new Font("open sans", Font.BOLD, 11)); 
        
        title = new JLabel("CAPTION : ");
        title.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        title.setFont(new Font("open sans", Font.BOLD, 11));
        
        type = new JLabel("TYPE : ");
        type.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        type.setFont(new Font("open sans", Font.BOLD, 11));
        
        location = new JLabel("LOCATION : ");
        location.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        location.setFont(new Font("open sans", Font.BOLD, 11));
        
        setAddress(new JLabel("ADDRESS : "));
        getAddress().setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        getAddress().setFont(new Font("open sans", Font.BOLD, 11));
        
        comments = new JLabel("COMMENTS : ");
        comments.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        comments.setFont(new Font("open sans", Font.BOLD, 11));
        
//        photoGrapher = new JLabel(" :  ");
//        photoGrapher.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
//        photoGrapher.setFont(new Font("open sans", Font.BOLD, 11));
        
        created = new JLabel("CREATED : ");
        created.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        created.setFont(new Font("open sans", Font.BOLD, 11));
        
        modified = new JLabel("UPDATED ON : ");
        modified.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        modified.setFont(new Font("open sans", Font.BOLD, 11));
        
        

        tag = new JLabel("TAG : ");
        tag.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        tag.setFont(new Font("open sans", Font.BOLD, 11));
        
        faceTag = new JLabel("FACE TAG : ");
        faceTag.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        faceTag.setFont(new Font("open sans", Font.BOLD, 11));
        
//        ratings = new JLabel("RATINGS : ");
//        ratings.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
//        ratings.setFont(new Font("open sans", Font.BOLD, 11));

        setFileNameTxt(new JLabel(galleryData.getMediaFileName()));
        getFileNameTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        getFileNameTxt().setFont(new Font("open sans", Font.PLAIN, 11));
        getFileNameTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        getFileNameTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        getFileNameTxt().setToolTipText(galleryData.getMediaFileName());
        
        String title = removeExtention(galleryData.getTitle()!=null?galleryData.getTitle():"");
        titleTxt = new JTextField(title != null?title.trim():"");
        titleTxt.setToolTipText(title);
        titleTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        titleTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        titleTxt.setFont(new Font("open sans", Font.PLAIN, 11));
        titleTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        if (title != null && title.length() > 29) {
            titleTxt.setToolTipText(title);
        }
        titleTxt.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
               
                
                 if(isCheckWronEntry())
                {
                   getTitleTxt().setText("");
                    setCheckWronEntry(false);
                }else
                {
                 String input = ((JTextField) e.getSource()).getText();
                 titleTxt.setText(input.trim());
                }
                if(titleTxt.getText().length() > 50)
                  titleTxt.setText(titleTxt.getText().substring(0,50));
            }
        });
        titleTxt.addKeyListener(new KeyAdapter() {
            
             @Override
            public void keyPressed(KeyEvent e) {
                if(isCheckWronEntry())
                {
                   getTitleTxt().setText("");
                   setCheckWronEntry(false);
                }
                 if(titleTxt.getText().length() > 50)
                  titleTxt.setText(titleTxt.getText().substring(0,50));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags
             if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
                }
                Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
                Matcher matcher = regex.matcher(input);

                if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");
                    titleTxt.setText(stemp);
                   
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed!");

                }

               // using Matcher find(), group(), start() and end() methods

                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                if (input != null && input.length() > 0) {
                    if (Character.isWhitespace(input.charAt(0))) {

                        titleTxt.setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } else if (matcher1.find()) {
                        String stemp = matcher1.replaceAll(" ");
                        titleTxt.setText(stemp);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                    } else {

                    }

                }

            }

        });

        typeTxt = new JLabel(galleryData.getMediaType());
        typeTxt.setToolTipText(galleryData.getMediaType());
        typeTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        typeTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        typeTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        typeTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        
        setAddressTxt(new JLabel((galleryData.getAddress()!=null && !galleryData.getAddress().isEmpty())?galleryData.getAddress():" "));
        getAddressTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        getAddressTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        getAddressTxt().setFont(new Font("Arial", Font.PLAIN, 11));
        getAddressTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        
    
        locationTxt = new JTextField();
       
        if (galleryData.getEditMediaLocation() != null && galleryData.getEditMediaLocation().length() > 20) 
            locationTxt.setText(galleryData.getEditMediaLocation().substring(0, 20)+"...");
        else
            locationTxt.setText(galleryData.getEditMediaLocation());
        
        if(galleryData.getEditMediaLocation()!= null && !galleryData.getEditMediaLocation().isEmpty())
        {
        locationTxt.setToolTipText(galleryData.getEditMediaLocation());
        }
        locationTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        locationTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        locationTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        locationTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        

        locationTxt.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                File file = new File(getMediaFileLocation());
               // if (checkVideo(file)) {
//                    ratingValidationMessage.setText("*Not supported for " + galleryData.getMediaType());
//                    ratingValidationMessage.setVisible(true);
//                    ratingValidationMessage.setForeground(Color.RED);
                    SSNMetaDataPanel.this.repaint();
                    if(locationTxt.getText().length() > 50)
                    locationTxt.setText(locationTxt.getText().substring(0,50));
              //  }
            }

            @Override
            public void focusLost(FocusEvent e) {
//                ratingValidationMessage.setText("");
//                ratingValidationMessage.setVisible(false);
                 File file = new File(getMediaFileLocation());
//                if (checkVideo(file)) {
//                      getLocationTxt().setText("");
//                }
                 
                 if(isCheckWronEntry())
                {
                   getLocationTxt().setText("");
                    setCheckWronEntry(false);
                }else
                {
                String input = ((JTextField) e.getSource()).getText();
                  //  System.out.println("locationTxt"+input.trim());
                 locationTxt.setText(input.trim());
                   if(locationTxt.getText().length() > 50)
                    locationTxt.setText(locationTxt.getText().substring(0,50));
                }
            }
        });
        locationTxt.addKeyListener(new KeyAdapter() {

             @Override
            public void keyPressed(KeyEvent e) {
                if(isCheckWronEntry())
                {
                   getLocationTxt().setText("");
                    setCheckWronEntry(false);
                }
                  if(locationTxt.getText().length() > 50)
                    locationTxt.setText(locationTxt.getText().substring(0,50));
            }
            
            @Override
            public void keyReleased(KeyEvent e) {

                String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags
              File file = new File(getMediaFileLocation());
                if(locationTxt.getText().length() > 50)
                    locationTxt.setText(locationTxt.getText().substring(0,50));
                if (checkVideo(file)) {
                    //locationTxt.setText("");
                    if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
                   }
                }else
                { 
                   if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
                   }
                }
                Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
                Matcher matcher = regex.matcher(input);
                if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");
                   
                    locationTxt.setText(stemp);
                    //JOptionPane.showMessageDialog(null, "Special characters are not allowed!");
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed");

                }

                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                if (input != null && input.length() > 0) {
                    if (Character.isWhitespace(input.charAt(0))) {
                        
                        locationTxt.setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } else if (matcher1.find()) {
                        String stemp = matcher1.replaceAll(" ");
                        
                        locationTxt.setText(stemp);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                    } else {

                    }

                }
            }
        });
        commentsTxt = new JTextField(galleryData.getUserComments());
        commentsTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        commentsTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        commentsTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        commentsTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        commentsTxt.setPreferredSize(new Dimension(10, commentsTxt.getHeight() ));
        if (galleryData.getUserComments() != null && !galleryData.getUserComments().isEmpty()) {
            commentsTxt.setToolTipText(galleryData.getUserComments());
        }
        
       
        commentsTxt.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                
                if(isCheckWronEntry())
                {
                   getCommentsTxt().setText("");
                    setCheckWronEntry(false);
                }else
                {
                String input = ((JTextField) e.getSource()).getText();
                commentsTxt.setText(input.trim());
                }
                
                
            }
        });
        commentsTxt.addKeyListener(new KeyAdapter() {
            
           

            @Override
            public void keyPressed(KeyEvent e) {
                if(isCheckWronEntry())
                {
                   getCommentsTxt().setText("");
                    setCheckWronEntry(false);
                }
            }
            
            

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags
               
                Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
                Matcher matcher = regex.matcher(input);
                

               // using Matcher find(), group(), start() and end() methods
//                if (input.trim().equals("")) {
//                    //JOptionPane.showMessageDialog(null, "Spaces are not allowed!");
//                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed");
//                }
                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                 if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
                }
                else if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");

                    commentsTxt.setText(stemp);
                    //JOptionPane.showMessageDialog(null, "Special characters are not allowed!");
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed");

                }
                else if (input != null && input.length() > 0) {
                    if (Character.isWhitespace(input.charAt(0))) {

                        commentsTxt.setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } else if (matcher1.find()) {
                        String stemp = matcher1.replaceAll(" ");
                        commentsTxt.setText(stemp);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                    } else {

                    }

                }

            }

        });
        


        createdTxt = new JTextField(SSNScheduleTagPanelForm.getFormatedDateTime(galleryData.getCreated()));
        if(galleryData.getCreated() !=null && !galleryData.getCreated().isEmpty())
        {
        createdTxt.setToolTipText(galleryData.getCreated());
        }
        createdTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        createdTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        createdTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        createdTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

        modifiedTxt = new JTextField();
        String modText=galleryData.getModiFied();
        if(modText != null && !modText.isEmpty())
        {
            modifiedTxt.setToolTipText(modText);
            if(modText.length()>18)
              modifiedTxt.setText( modText.substring(0, 17)+"...");
            else
            modifiedTxt.setText( modText);    
        }
        modifiedTxt.setEditable(false);
       
        
        modifiedTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        modifiedTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        modifiedTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        modifiedTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        
        facetagTxt = new JTextField(galleryData.getFaceTags());
        facetagTxt.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        facetagTxt.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        facetagTxt.setFont(new Font("Arial", Font.PLAIN, 11));
        facetagTxt.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        
        tagText = new JTextField(galleryData.getSsnKeywords());
        if(!galleryData.getSsnKeywords().isEmpty())
            tagText.setToolTipText(galleryData.getSsnKeywords());
        
        tagText.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        tagText.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        tagText.setFont(new Font("Arial", Font.PLAIN, 11));
        tagText.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        tagText.addFocusListener(new FocusAdapter() {
            
            
             @Override
            public void focusGained(FocusEvent e) {
                File file = new File(getMediaFileLocation());
                if (checkVideo(file)) {
//                    ratingValidationMessage.setText("*Not supported for " + galleryData.getMediaType());
//                    ratingValidationMessage.setVisible(true);
//                    ratingValidationMessage.setForeground(Color.RED);
                    SSNMetaDataPanel.this.repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
//                ratingValidationMessage.setText("");
//                ratingValidationMessage.setVisible(false);
                if(tagText.getText().length() > 50)
                    tagText.setText(tagText.getText().substring(0,50));
                 File file = new File(getMediaFileLocation());
                if (checkVideo(file)) {
                    //  getTagText().setText("");
                }
                 if(isCheckWronEntry())
                {
                   getTagText().setText("");
                    setCheckWronEntry(false);
                }else
                {
                   String input = ((JTextField) e.getSource()).getText();
                   tagText.setText(input.trim());
                }
            }
        });
        tagText.addKeyListener(new KeyAdapter() {
            
            
              @Override
            public void keyPressed(KeyEvent e) {
                if(isCheckWronEntry())
                {
                   //getTagText().setText("");
                   setCheckWronEntry(false);
                }
                if(tagText.getText().length() > 50)
                    tagText.setText(tagText.getText().substring(0,50));
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags
                if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
                }
                
                if(tagText.getText().length() > 25)
                    tagText.setText(tagText.getText().substring(0,25));
                
                Pattern regex = Pattern.compile("[%$&+:;=?@#|]");
                Matcher matcher = regex.matcher(input);
                if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+:;=?@#|]", "");

                    tagText.setText(stemp);
                   
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed");

                }

               // using Matcher find(), group(), start() and end() methods

                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                if (input != null && input.length() > 0) {
                    if (Character.isWhitespace(input.charAt(0))) {

                        tagText.setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } else if (matcher1.find()) {
                        String stemp = matcher1.replaceAll(" ");
                        tagText.setText(stemp);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                    } else {

                    }

                }

            }
            

        });

        Icon icon = new ImageIcon(getClass().getResource("/icon/edit-icons.png"));
        
        editLabel = new JLabel("EDIT");
        editLabel.setBorder((BorderFactory.createEmptyBorder(0, 0, 0, 1)));
        editLabel.setIcon(icon);
        editLabel.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        editLabel.setFont(new Font("Arial", Font.BOLD, 11));
        editLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if(getHomeForm().getCheckMultiSelection()){
            //editLabel.setEnabled(false);
            editLabel.setVisible(false);
        }
        else
        {
            editLabel.setVisible(true);
        }
        

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder((BorderFactory.createEmptyBorder(11, 2, 11, 5)));
        northPanel.setSize(200, 10);
        northPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        northPanel.add(panelHeading, BorderLayout.WEST);
        northPanel.add(editLabel, BorderLayout.EAST);

        JPanel northSouthPanel = new JPanel(new BorderLayout());
        northSouthPanel.setSize(200, 10);
        //int top, int left, int bottom, int right
        northSouthPanel.setBorder((BorderFactory.createEmptyBorder(0, 0, 5, 0)));
        northSouthPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
//        northSouthPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/right-pannel.jpg")), SwingConstants.HORIZONTAL), BorderLayout.WEST);
//        northSouthPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/right-pannel.jpg")), SwingConstants.HORIZONTAL), BorderLayout.EAST);

        setFileInfoLabel(new JLabel("INFORMATION    "));
        getFileInfoLabel().setForeground(Color.WHITE);
        getFileInfoLabel().setFont(new Font("Arial", Font.BOLD, 12));
        
        JPanel mainPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
        mainPanel.setLayout(boxLayout);
        mainPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        mainPanel.add(northPanel);
        mainPanel.add(northSouthPanel);
        mainPanel.add(populateImageSouthPanel(galleryData));

        //UIDefaults overrides = getUidefaults();

        editLabel.addMouseListener(new SSNMetadataListener(this, getAllTextField(), getHomeForm()));


        setLayout(new BorderLayout());
        setSize(200, 60);
        setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        add(mainPanel, BorderLayout.CENTER);
    }

    public static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory()) {
            return filePath;
        }

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0) {
            // No period after first character - return name as it was passed in
            return filePath;
        } else {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }
// private JPanel populateFolderSouthPanel(JLabel titleTxtFld,JLabel locationTxtFld,JLabel commentsTxtFld,JLabel modifiedTxtFld)

    private JPanel populateFolderSouthPanel(JLabel titleTxtFld, JLabel locationTxtFld, JLabel commentsTxtFld, JLabel modifiedTxtFld) {
        JPanel southFolder = new JPanel();
        try {

            southFolder.setLayout(new GridLayout(5, 1));
            southFolder.setSize(200, 50);
            southFolder.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel titleJP = new JPanel();
            titleJP.setLayout(new BorderLayout());
            titleJP.add(title, BorderLayout.WEST);
            titleJP.add(titleTxtFld, BorderLayout.CENTER);
            titleJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel titleType = new JPanel();
            titleType.setLayout(new BorderLayout());
            titleType.add(type, BorderLayout.WEST);
            titleType.add(typeTxt, BorderLayout.CENTER);
            titleType.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel titleLoc = new JPanel();
            titleLoc.setLayout(new BorderLayout());
            titleLoc.add(location, BorderLayout.WEST);
            titleLoc.add(locationTxtFld, BorderLayout.CENTER);
            titleLoc.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel commentJP = new JPanel();
            commentJP.setLayout(new BorderLayout());
            commentJP.add(comments, BorderLayout.WEST);
            commentJP.add(commentsTxtFld, BorderLayout.CENTER);
            commentJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel ratJp = new JPanel();
            ratJp.setLayout(new BorderLayout());
            ratJp.add(modified, BorderLayout.WEST);
            ratJp.add(modifiedTxtFld, BorderLayout.CENTER);
            ratJp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel jp = new JPanel();
            jp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            jp.add(new JLabel());

            JPanel jp1 = new JPanel();
            jp1.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            jp1.add(new JLabel());

            JPanel jp2 = new JPanel();
            jp2.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            jp2.add(new JLabel());

            southFolder.add(titleJP);
            southFolder.add(titleType);
            southFolder.add(titleLoc);
            southFolder.add(commentJP);
            southFolder.add(ratJp);
            //southFolder.add(jp1);
            //southFolder.add(jp);

        } catch (Exception ex) {
            log.error(ex);

        }

        return southFolder;
    }
    private AudioPlayer localAudioPlayer = null;
    boolean audioPlayerCreated = false;

    private JPanel populateImageSouthPanel(final SSNGalleryMetaData galleryData) {
        JPanel south = new JPanel();
        try {
            // to stop already playing media on clicking some other media file 
            for(Player player : AudioPlayer.playerList){
                if(player != null){
                    player.close();
                }
            }
            AudioPlayer.playerList.clear();

            if (getLocalAudioPlayer() != null) {
                getLocalAudioPlayer().getAudioPlayer().stop();
                getLocalAudioPlayer().getAudioPlayer().close();
                getLocalAudioPlayer().getAudioPlayer().deallocate();
                getLocalAudioPlayer().setAudioPlayer(null);
                setLocalAudioPlayer(null);
            }

            //GridBagLayout layout = new GridBagLayout();
            //GridBagConstraints gbc = new GridBagConstraints();

            //south.setLayout(layout);
            //south.setSize(200, 70);
            south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
            south.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel fileNameJP = new JPanel();
            fileNameJP.setLayout(new BorderLayout());
            fileNameJP.add(getFileName(), BorderLayout.WEST);
            fileNameJP.add(getFileNameTxt(), BorderLayout.CENTER);
            fileNameJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel titleJP = new JPanel();
            titleJP.setLayout(new BorderLayout());
            titleJP.add(title, BorderLayout.WEST);
            titleJP.add(titleTxt, BorderLayout.CENTER);
            titleJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel titleType = new JPanel();
            titleType.setLayout(new BorderLayout());
            titleType.add(type, BorderLayout.WEST);
            titleType.add(typeTxt, BorderLayout.CENTER);
            titleType.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            JPanel tagWordJp = new JPanel();
            tagWordJp.setLayout(new BorderLayout());
            tagWordJp.add(tag, BorderLayout.WEST);
            tagWordJp.add(tagText, BorderLayout.CENTER);
            tagWordJp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel facetagJp = new JPanel();
            facetagJp.setLayout(new BorderLayout());
            facetagJp.add(faceTag, BorderLayout.WEST);
            facetagJp.add(facetagTxt, BorderLayout.CENTER);
            facetagJp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            JPanel titleLoc = new JPanel();
            titleLoc.setLayout(new BorderLayout());
            titleLoc.add(location, BorderLayout.WEST);
            titleLoc.add(locationTxt, BorderLayout.CENTER);
            titleLoc.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            
            
            JPanel titleAddress = new JPanel();
            titleAddress.setLayout(new BorderLayout());
            titleAddress.add(getAddress(), BorderLayout.WEST);
            titleAddress.add(getAddressTxt(), BorderLayout.CENTER);
            titleAddress.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

            JPanel commentJP = new JPanel();
            commentJP.setLayout(new BorderLayout());
//            commentJP.add(comments, BorderLayout.WEST);
//            commentJP.add(commentsTxt, BorderLayout.CENTER);
            commentJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            
            JPanel photographerJP = new JPanel();
            photographerJP.setLayout(new BorderLayout());
//            photographerJP.add(photoGrapher, BorderLayout.WEST);
//            photographerJP.add(photoGrapherTxt, BorderLayout.CENTER);
            photographerJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
//            JPanel ratJp = new JPanel();
//            ratJp.setLayout(new BorderLayout());
//            ratJp.add(ratings, BorderLayout.WEST);
//            ratJp.add(ratingsTxt, BorderLayout.CENTER);
//            ratJp.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
//
//            JPanel ratValidationJP = new JPanel();
//            ratValidationJP.setLayout(new BorderLayout());
//            ratValidationJP.add(ratingValidationMessage, BorderLayout.CENTER);
//            ratValidationJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            JPanel updateOn =   new JPanel();
            updateOn.setLayout(new BorderLayout());
            updateOn.add(getModified(),BorderLayout.WEST);
            updateOn.add(getModifiedTxt(),BorderLayout.CENTER);
            updateOn.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            // Create and add buttons for Voice note

            Border paddingBorder = BorderFactory.createEmptyBorder(9, 5, 9, 10);
            Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
           
            JTextField txtForSetUI = new JTextField();
            txtForSetUI.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
            txtForSetUI.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            txtForSetUI.setFont(new Font("Arial", Font.PLAIN, 11));
            txtForSetUI.setEditable(false);
            txtForSetUI.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
            
            
           
            JPanel fileInfoJP = new JPanel();
            
            fileInfoJP.setLayout(new BorderLayout());
            fileInfoJP.add(getFileInfoLabel(), BorderLayout.WEST);
            fileInfoJP.add(txtForSetUI, BorderLayout.CENTER);
            fileInfoJP.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            
           // int i = 0;
            
            //gbc.fill = GridBagConstraints.HORIZONTAL;
            //.gridx = 0;
           // gbc.gridy = i;
            //south.add(titleJP, gbc);
            south.add(titleJP);
           // gbc.fill = GridBagConstraints.HORIZONTAL;
           // gbc.gridx = 0;
           // gbc.gridy = ++i;
           // south.add(tagWordJp, gbc);
            south.add(tagWordJp);
           // gbc.fill = GridBagConstraints.HORIZONTAL;
           // gbc.gridx = 0;
           // gbc.gridy = ++i;
           // south.add(facetagJp, gbc);
            south.add(facetagJp);
          //  gbc.fill = GridBagConstraints.HORIZONTAL;
           // gbc.gridx = 0;
           // gbc.gridy = ++i;
           // south.add(titleLoc, gbc);
            south.add(titleLoc);
            
          //  gbc.fill = GridBagConstraints.HORIZONTAL;
          //  gbc.gridx = 0;
          //  gbc.gridy = ++i;
           // south.add(titleAddress, gbc);
            south.add(titleAddress);

          //  gbc.fill = GridBagConstraints.HORIZONTAL;
          //  gbc.gridx = 0;
            // i= i+5;
         //   gbc.gridy = ++i;
            //south.add(ratValidationJP, gbc);
            // south.add(ratValidationJP);
            
          //  gbc.fill = GridBagConstraints.HORIZONTAL;
          //  gbc.gridx = 0;
           // gbc.gridy = ++i;
            //south.add(fileInfoJP, gbc);
            south.add(fileInfoJP);
          //  gbc.fill = GridBagConstraints.HORIZONTAL;
          //  gbc.gridx = 0;
          //  gbc.gridy = ++i;
            //south.add(fileNameJP, gbc);
             south.add(fileNameJP);
         //   gbc.fill = GridBagConstraints.HORIZONTAL;
          //  gbc.gridx = 0;
          //  gbc.gridy = ++i;
            //south.add(titleType, gbc);
             south.add(titleType);
            
           // gbc.fill = GridBagConstraints.HORIZONTAL;
         //   gbc.gridx = 0;
          //  gbc.gridy = ++i;
           // south.add(updateOn, gbc);
            south.add(updateOn);
            
            
            JLabel voiceNoteHeadingPanel1 = new JLabel("VOICE NOTE");
            voiceNoteHeadingPanel1.setBorder((BorderFactory.createEmptyBorder(10, 0, 5, 0)));
            voiceNoteHeadingPanel1.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            voiceNoteHeadingPanel1.setFont(new Font("Arial", Font.BOLD, 12));
//            gbc.fill = GridBagConstraints.HORIZONTAL;
//            gbc.gridx = 0;
//            gbc.gridy = ++i;
            
            
             
            JLabel voiceNoteHeadingPanel2 = new JLabel("Duration");
            voiceNoteHeadingPanel2.setBorder((BorderFactory.createEmptyBorder(10, 0, 5, 0)));
            voiceNoteHeadingPanel2.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            voiceNoteHeadingPanel2.setFont(new Font("Arial", Font.BOLD, 12));
            
            JPanel voiceNoteMainPanel = new JPanel(new BorderLayout());
            voiceNoteMainPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            voiceNoteMainPanel.add(voiceNoteHeadingPanel1,BorderLayout.WEST);
           // voiceNoteMainPanel.add(voiceNoteHeadingPanel2,BorderLayout.EAST);
            
            //south.add(voiceNoteMainPanel,gbc);
            south.add(voiceNoteMainPanel);
            
            JPanel voiceNoteSeperatorPanel = new JPanel(new BorderLayout());
            //voiceNoteSeperatorPanel.setSize(200, 10);
            
            //int top, int left, int bottom, int right
            voiceNoteSeperatorPanel.setBorder((BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            voiceNoteSeperatorPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            //voiceNoteSeperatorPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/right-pannel.jpg")), SwingConstants.HORIZONTAL), BorderLayout.WEST);
            voiceNoteSeperatorPanel.add(new JLabel(new ImageIcon(getClass().getResource("/images/right-pannel.jpg")), SwingConstants.HORIZONTAL), BorderLayout.EAST);
            voiceNoteSeperatorPanel.setVisible(true);
            
//            gbc.fill = GridBagConstraints.HORIZONTAL;
//            gbc.gridx = 0;
//            gbc.gridy = ++i; //gbc.gridheight=30;
           //south.add(voiceNoteSeperatorPanel, gbc);
            south.add(voiceNoteSeperatorPanel);

            //final JPanel voiceNoteBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 11, 8));
            final JPanel voiceNoteBtnPanel = new JPanel();
            voiceNoteBtnPanel.setLayout(new BoxLayout(voiceNoteBtnPanel, BoxLayout.X_AXIS));
            voiceNoteBtnPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
//            gbc.fill = GridBagConstraints.HORIZONTAL;
//            gbc.gridx = 0;
//            gbc.gridy = ++i;
            //south.add(voiceNoteBtnPanel, gbc);
            south.add(voiceNoteBtnPanel);

            final String attachVoiceNote = "Create Voice Note";
            final String stopRecording = "Stop Recording";
            final JLabel startVoiceNoteBtn = new JLabel(attachVoiceNote);

            final List<JLabel> lblList = new ArrayList<JLabel>();
            lblList.add(startVoiceNoteBtn);

            startVoiceNoteBtn.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            startVoiceNoteBtn.setFont(new Font("Arial", Font.BOLD, 12));
            startVoiceNoteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

          
            startVoiceNoteBtn.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    AudioRecorder audioRecorder = null;
                    if (VoiceCommandController.isVoiceCammandEnabled()) {
                        if (startVoiceNoteBtn.getText().equalsIgnoreCase(attachVoiceNote)) {
                            try{                          
                                audioRecorder = new AudioRecorder();
                                try{
                                    setTargetDataLine(audioRecorder.captureAudio(true));
                                }catch(LineUnavailableException e){
                                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Voice Notes", "", "Mic is not ready, please check if it is properly working!!! ");
                                }catch(IllegalArgumentException e){
                                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Voice Notes", "", "Mic is not ready, please check if it is properly working!!! ");
                                    throw new Exception(e);
                                }
                                startVoiceNoteBtn.setText(stopRecording);
                            }catch(Exception e){
                                e.printStackTrace();
                                log.error(e);
                            }
                        } else if (startVoiceNoteBtn.getText().equalsIgnoreCase(stopRecording)) {
                            try {
                                startVoiceNoteBtn.setText(attachVoiceNote);

                                voiceNoteBtnPanel.remove(startVoiceNoteBtn);
                                voiceNoteBtnPanel.removeAll();
                                try {
                                    getTargetDataLine().stop();
                                    getTargetDataLine().close();
                                } catch (NullPointerException e) {
                                    log.error(e);
                                }
                                try {
                                    
                                    /* applying logic for adding voice noce on multiple selection */
                                    if(getHomeForm().getFileNamesToBeDeleted()!=null && getHomeForm().getFileNamesToBeDeleted().size()>0){
                                        // converting returned set into a list
                                        List<String> list = new ArrayList<>(getHomeForm().getFileNamesToBeDeleted()); 
                                        for(String filePath : list){
                                            if(SSNDao.isInsetNeeded_MediaTable(getMediaFileLocation(), AudioRecorder.getVoiceNoteFileName())){
                                                //Insert record in ssnmedia table if it is not already there
                                                SSNDao.insertMediaTable(filePath, getCommentsTxt().getText(), "", getLocationTxt().getText(), getTypeTxt().getText(),getTagText().getText(),"", getTitleTxt().getText());
                                            }
                                            // update the record with voice note
                                            SSNDao.insertVoiceNote_MediaTable(filePath, AudioRecorder.getVoiceNoteFileName());
                                        }
                                    }else{
                                        // adding voice note for single selection
                                        if(SSNDao.isInsetNeeded_MediaTable(getMediaFileLocation(), AudioRecorder.getVoiceNoteFileName())){
                                            SSNDao.insertMediaTable(galleryData.getMediaFileLocation(), getCommentsTxt().getText(), "", getLocationTxt().getText(), getTypeTxt().getText(),getTagText().getText(),"", getTitleTxt().getText());
                                        }
                                        SSNDao.insertVoiceNote_MediaTable(galleryData.getMediaFileLocation(), AudioRecorder.getVoiceNoteFileName());
                                    }  
                                } catch (SQLException ex) {
                                    Logger.getLogger(SSNMetaDataPanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (audioPlayerCreated) {
                                    getLocalAudioPlayer().initAudioPlayer(SSNHelper.getSsnVoiceNoteDirPath() + AudioRecorder.getVoiceNoteFileName());
                                    getLocalAudioPlayer().initComponents(true);
                                } else {
                                    setLocalAudioPlayer(new AudioPlayer(SSNHelper.getSsnVoiceNoteDirPath() + AudioRecorder.getVoiceNoteFileName(), lblList,  mediaLocation));
                                }

                                JPanel panel = AudioPlayer.getMainPanel();
                                if (panel != null) {
                                    panel.setVisible(true);
                                    voiceNoteBtnPanel.add(panel);
                                }

                                voiceNoteBtnPanel.getParent().revalidate();
                                voiceNoteBtnPanel.getParent().repaint();
                            } catch (NullPointerException e) {
                               log.error(e);
                            } catch (Exception e) {
                                log.error(e);
                            }
                        }
                    } else {

                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Voice Notes", "", "*** Voice Command is not active  ***\n Kindly change voice command setting from Preferences screen!!! ");

                    }
                }
            });

            if (galleryData.getVoiceNotePath() != null && !galleryData.getVoiceNotePath().equals("")) {
               
                setLocalAudioPlayer(new AudioPlayer(SSNHelper.getSsnVoiceNoteDirPath() + galleryData.getVoiceNotePath(), lblList, galleryData.getMediaFileLocation()));
                audioPlayerCreated = true;
               
            }

            // create and add buttons to play voice notes
            if (galleryData.getVoiceNotePath() != null && !galleryData.getVoiceNotePath().equals("")) {

                JPanel panel = AudioPlayer.getMainPanel();
                if (panel != null) {
                    panel.setVisible(true);
                    voiceNoteBtnPanel.add(panel);
                } else {
                    voiceNoteBtnPanel.add(startVoiceNoteBtn);
                }
            } else {
                voiceNoteBtnPanel.add(startVoiceNoteBtn);
            }
        } catch (Exception ex) {
            log.error(ex);
        }

        return south;
    }

    public ArrayList<JTextField> getAllTextField() {
        ArrayList<JTextField> mdTextFieldList = new ArrayList<JTextField>();
        mdTextFieldList.add(titleTxt);
        mdTextFieldList.add(locationTxt);
       // mdTextFieldList.add(addressTxt);
        mdTextFieldList.add(commentsTxt);
        mdTextFieldList.add(createdTxt);
         mdTextFieldList.add(modifiedTxt);
       // mdTextFieldList.add(photoGrapherTxt);
        mdTextFieldList.add(tagText);
        mdTextFieldList.add(facetagTxt);
       // mdTextFieldList.add(ratingsTxt);
        return mdTextFieldList;
    }

    private static boolean checkVideo(File file) {
        String[] videoSupported = SSNConstants.SSN_VIDEO_FORMAT_SUPPORTED;
        List<String> videoSupportedList = Arrays.asList(videoSupported);
        String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        if (videoSupportedList.contains(fileExtension.toUpperCase())) {
            return true;
        }
        return false;
    }

    /**
     * @return the editLabel
     */
    public JLabel getEditLabel() {
        return editLabel;
    }

    /**
     * @param editLabel the editLabel to set
     */
    public void setEditLabel(JLabel editLabel) {
        this.editLabel = editLabel;
    }

    public JLabel getPanelHeading() {
        return panelHeading;
    }

    public void setPanelHeading(JLabel panelHeading) {
        this.panelHeading = panelHeading;
    }

    public JLabel getTitle() {
        return title;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    public JLabel getType() {
        return type;
    }

    public void setType(JLabel type) {
        this.type = type;
    }

    public JLabel getComments() {
        return comments;
    }

    public void setComments(JLabel comments) {
        this.comments = comments;
    }

    public JLabel getCreated() {
        return created;
    }

    public void setCreated(JLabel created) {
        this.created = created;
    }

    public JLabel getModified() {
        return modified;
    }

    public void setModified(JLabel modified) {
        this.modified = modified;
    }

//    public JLabel getRatings() {
//        return ratings;
//    }
//
//    public void setRatings(JLabel ratings) {
//        this.ratings = ratings;
//    }

    public JTextField getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(JTextField titleTxt) {
        this.titleTxt = titleTxt;
    }

    public JLabel getTypeTxt() {
        return typeTxt;
    }

    public void setTypeTxt(JLabel typeTxt) {
        this.typeTxt = typeTxt;
    }

    public JTextField getLocationTxt() {
        return locationTxt;
    }

    public void setLocationTxt(JTextField locationTxt) {
        this.locationTxt = locationTxt;
    }

    public JTextField getCommentsTxt() {
        return commentsTxt;
    }

    public void setCommentsTxt(JTextField commentsTxt) {
        this.commentsTxt = commentsTxt;
    }

    public JTextField getCreatedTxt() {
        return createdTxt;
    }

    public void setCreatedTxt(JTextField createdTxt) {
        this.createdTxt = createdTxt;
    }

    public JTextField getModifiedTxt() {
        return modifiedTxt;
    }

    public void setModifiedTxt(JTextField modifiedTxt) {
        this.modifiedTxt = modifiedTxt;
    }

//    public JTextField getRatingsTxt() {
//        return ratingsTxt;
//    }
//
//    public void setRatingsTxt(JTextField ratingsTxt) {
//        this.ratingsTxt = ratingsTxt;
//    }

    /**
     * @return the mediaFileLocation
     */
    public String getMediaFileLocation() {
        return mediaFileLocation;
    }

    /**
     * @param mediaFileLocation the mediaFileLocation to set
     */
    public void setMediaFileLocation(String mediaFileLocation) {
        this.mediaFileLocation = mediaFileLocation;
    }

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void setTargetDataLine(TargetDataLine targetDataLine) {
        this.targetDataLine = targetDataLine;
    }

    /**
     * @return the mediaFileName
     */
    public String getMediaFileName() {
        return mediaFileName;
    }

    /**
     * @param mediaFileName the mediaFileName to set
     */
    public void setMediaFileName(String mediaFileName) {
        this.mediaFileName = mediaFileName;
    }

    /**
     * @return the localAudioPlayer
     */
    public AudioPlayer getLocalAudioPlayer() {
        return localAudioPlayer;
    }

    /**
     * @param localAudioPlayer the localAudioPlayer to set
     */
    public void setLocalAudioPlayer(AudioPlayer localAudioPlayer) {
        this.localAudioPlayer = localAudioPlayer;
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
     * @return the fileName
     */
    public JLabel getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(JLabel fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileNameTxt
     */
    public JLabel getFileNameTxt() {
        return fileNameTxt;
    }

    /**
     * @param fileNameTxt the fileNameTxt to set
     */
    public void setFileNameTxt(JLabel fileNameTxt) {
        this.fileNameTxt = fileNameTxt;
    }

    /**
     * @return the photoGrapher
     */
//    public JLabel getPhotoGrapher() {
//        return photoGrapher;
//    }
//
//    /**
//     * @param photoGrapher the photoGrapher to set
//     */
//    public void setPhotoGrapher(JLabel photoGrapher) {
//        this.photoGrapher = photoGrapher;
//    }

    /**
     * @return the photoGrapherTxt
     */
//    public JTextField getPhotoGrapherTxt() {
//        return photoGrapherTxt;
//    }
//
//    /**
//     * @param photoGrapherTxt the photoGrapherTxt to set
//     */
//    public void setPhotoGrapherTxt(JTextField photoGrapherTxt) {
//        this.photoGrapherTxt = photoGrapherTxt;
//    }

    /**
     * @return the tag
     */
    public JLabel getTag() {
        return tag;
    }

    /**
     * @param keywords the tag to set
     */
    public void setTag(JLabel keywords) {
        this.tag = keywords;
    }

    /**
     * @return the tagText
     */
    public JTextField getTagText() {
        return tagText;
    }

    /**
     * @param tagText the tagText to set
     */
    public void setTagText(JTextField tagText) {
        this.tagText = tagText;
    }

    /**
     * @return the checkWronEntry
     */
    public boolean isCheckWronEntry() {
        return checkWronEntry;
    }

    /**
     * @param checkWronEntry the checkWronEntry to set
     */
    public void setCheckWronEntry(boolean checkWronEntry) {
        this.checkWronEntry = checkWronEntry;
    }

    /**
     * @return the address
     */
    public JLabel getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(JLabel address) {
        this.address = address;
    }

   

    /**
     * @return the faceTag
     */
    public JLabel getFaceTag() {
        return faceTag;
    }

    /**
     * @param faceTag the faceTag to set
     */
    public void setFaceTag(JLabel faceTag) {
        this.faceTag = faceTag;
    }

    /**
     * @return the facetagTxt
     */
    public JTextField getFacetagTxt() {
        return facetagTxt;
    }

    /**
     * @param facetagTxt the facetagTxt to set
     */
    public void setFacetagTxt(JTextField facetagTxt) {
        this.facetagTxt = facetagTxt;
    }

    /**
     * @return the addressTxt
     */
    public JLabel getAddressTxt() {
        return addressTxt;
    }

    /**
     * @param addressTxt the addressTxt to set
     */
    public void setAddressTxt(JLabel addressTxt) {
        this.addressTxt = addressTxt;
    }

    public JLabel getFileInfoLabel() {
        return fileInfoLabel;
    }

    public void setFileInfoLabel(JLabel fileInfoLabel) {
        this.fileInfoLabel = fileInfoLabel;
    }
}
