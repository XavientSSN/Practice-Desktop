package com.ssn.event.controller;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.model.SSNEditUserProfileModel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNEditUserProfileForm;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author vkvarma
 */
public class SSNUserProfileController extends SSNBaseController {

    private SSNEditUserProfileForm userProfileForm = null;
    private SSNEditUserProfileModel editUserProfileModel = null;
    private boolean checkWronEntry = false;
    final    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNUserProfileController.class);
    public SSNUserProfileController(SSNEditUserProfileForm userProfileForm) {
        this.setUserProfileForm(userProfileForm);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton jBtn = (JButton) actionEventObj;
            if (jBtn.getActionCommand().equalsIgnoreCase("updateUserProfile")) {
                this.getEditUserProfileModel().updateUserProfile();
            } else if (jBtn.getActionCommand().equalsIgnoreCase("cancelUserProfile")) {
                getUserProfileForm().dispose();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) actionEventObj;
            if (jLbl.getName().equalsIgnoreCase("closeProfile")) {
                getUserProfileForm().dispose();
            }else  if (jLbl.getName().equalsIgnoreCase("updateUserProfile")) {
                this.getEditUserProfileModel().updateUserProfile();
            } else if (jLbl.getName().equalsIgnoreCase("cancelUserProfile")) {
                getUserProfileForm().dispose();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isCheckWronEntry()) {
            ((JTextField) e.getSource()).setText("");
            setCheckWronEntry(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
            String input = ((JTextField) e.getSource()).getText();
            // using pattern with flags
//            if (KeyEvent.VK_ALT == e.getKeyCode()) {
//                setCheckWronEntry(true);
//
//                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "The entered key combination are not allowed");
//            }

            String returnString = keyReleaseEvent(input);
            if(returnString!=null && !returnString.equals(""))
            {
               ((JTextField) e.getSource()).setText(returnString);
            }
            
       
    }

    private String keyReleaseEvent(String input) {
        String returnString = "";
        Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
        Matcher matcher = regex.matcher(input);

       

        Pattern regex1 = Pattern.compile("\\s{2,}");
        Matcher matcher1 = regex1.matcher(input);
         if (matcher.find()) {
            String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");
            returnString = stemp;
            //JOptionPane.showMessageDialog(null, "Special characters are not allowed!");
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed!");

        }
         else if (input != null && input.length() > 0) {
            if (Character.isWhitespace(input.charAt(0))) {

                returnString = input.trim();
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

            } else if (matcher1.find()) {
                String stemp = matcher1.replaceAll(" ");
                returnString = stemp;
                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
            } else {

            }

        }else
         {
          
         }

        return returnString;
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
     * @return the editUserProfileModel
     */
    public SSNEditUserProfileModel getEditUserProfileModel() {
        return editUserProfileModel;
    }

    /**
     * @param editUserProfileModel the editUserProfileModel to set
     */
    public void setEditUserProfileModel(SSNEditUserProfileModel editUserProfileModel) {
        this.editUserProfileModel = editUserProfileModel;
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

}
