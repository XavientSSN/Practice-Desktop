package com.ssn.event.controller;

import com.ssn.ui.custom.component.SSNConfirmationDialogBox;
import com.ssn.ui.custom.component.SSNInputDialogBox;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pkumar2
 */
public class SSNMessageDialogController extends SSNBaseController {

    private SSNMessageDialogBox dialogBox;
    private SSNConfirmationDialogBox confirmBox;
    private SSNInputDialogBox inputBox;
    final    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNMessageDialogController.class);
    public SSNMessageDialogController(SSNMessageDialogBox dialogBox) {
        this.dialogBox = dialogBox;
    }

    public SSNMessageDialogController(SSNConfirmationDialogBox confirmBox) {
        this.confirmBox = confirmBox;
    }

    public SSNMessageDialogController(SSNInputDialogBox inputBox) {
        this.inputBox = inputBox;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object mouseEventObj = e.getSource();
        if (mouseEventObj != null && mouseEventObj instanceof JButton) {
            JButton jBtn = (JButton) mouseEventObj;
            if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("yes")) {
                confirmBox.setResult(JOptionPane.YES_OPTION);
                confirmBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("no")) {
                confirmBox.setResult(JOptionPane.NO_OPTION);
                confirmBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("ok")) {
                if (inputBox.isSelectBox()) {
                   
                    inputBox.setTextValue(inputBox.getComboBox().getSelectedItem().toString());
                } else {
                    inputBox.setTextValue(inputBox.getDailogMessageTextField().getText());
                }
                 if(StringUtils.isBlank(inputBox.getTextValue()))
                    {
                      inputBox.setValidationMessage("Album name should not be blank!");
                      inputBox.getValidationLabel().setText(inputBox.getValidationMessage());  
                    }
                 else
                 {
                     inputBox.dispose();
                 }
                
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("cancel")) {
                inputBox.setTextValue(null);
                inputBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("closeDialog")) {
                inputBox.setTextValue(null);
                inputBox.dispose();
            } else {
                if (dialogBox != null) {
                    dialogBox.dispose();
                }
            }
        }else if(mouseEventObj != null && mouseEventObj instanceof JLabel){
            JLabel jBtn = (JLabel) mouseEventObj;
            if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("yes")) {
                confirmBox.setResult(JOptionPane.YES_OPTION);
                confirmBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("no")) {
                confirmBox.setResult(JOptionPane.NO_OPTION);
                confirmBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("ok")) {
                if (inputBox.isSelectBox()) {
                   
                    inputBox.setTextValue(inputBox.getComboBox().getSelectedItem().toString());
                } else {
                    
                    
                    
                    inputBox.setTextValue(inputBox.getDailogMessageTextField().getText());
                }
                 if(StringUtils.isBlank(inputBox.getTextValue()))
                    {
                      inputBox.setValidationMessage("Album name should not be blank!");
                      inputBox.getValidationLabel().setText(inputBox.getValidationMessage());  
                    }
                 else
                 {
                     inputBox.dispose();
                 }
                
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("cancel")) {
                inputBox.setTextValue(null);
                inputBox.dispose();
            } else if (jBtn.getName() != null && jBtn.getName().equalsIgnoreCase("closeDialog")) {
                inputBox.setTextValue(null);
                inputBox.dispose();
            } else {
                if (dialogBox != null) {
                    dialogBox.dispose();
                }
            }
        }
//        if (mouseEventObj != null && mouseEventObj instanceof JLabel) {
//
//            if (dialogBox != null) {
//                dialogBox.dispose();
//            }
//            if (confirmBox != null) {
//                confirmBox.setResult(JOptionPane.CLOSED_OPTION);
//                confirmBox.dispose();
//            }
//            if (inputBox != null) {
//                inputBox.setTextValue(null);
//                inputBox.dispose();
//            }
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object keyEventObj = e.getSource();
        char c = e.getKeyChar();

        if (keyEventObj != null && (keyEventObj instanceof JButton)) {
            if (c == KeyEvent.VK_ENTER) {
                JButton btn = (JButton) keyEventObj;
                if (btn.getName().equalsIgnoreCase("ok")) {
                    if (inputBox.isSelectBox()) {
                        inputBox.setTextValue(inputBox.getComboBox().getSelectedItem().toString());
                    } else {
                        inputBox.setTextValue(inputBox.getDailogMessageTextField().getText());
                    }
                    inputBox.dispose();
                } else if (btn.getName().equalsIgnoreCase("cancel")) {
                    inputBox.setTextValue(null);
                    inputBox.dispose();
                }

            }
        }
    }
}
