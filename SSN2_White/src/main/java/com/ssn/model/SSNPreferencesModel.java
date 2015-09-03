package com.ssn.model;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNPreferencesController;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNPreferencesForm;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author vkvarma
 */
public class SSNPreferencesModel {
    
    private SSNHomeForm homeForm = null;
    private SSNPreferencesForm preferencesForm = null;
    private SSNPreferencesController preferencesController = null;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNPreferencesModel.class);
    /**
     *
     */
    public SSNPreferencesModel() {
    }
    
    /**
     *
     * @param preferencesForm
     * @param homeForm
     * @param preferencesController
     */
    public SSNPreferencesModel(SSNPreferencesForm preferencesForm, SSNHomeForm homeForm, SSNPreferencesController preferencesController) {
        setHomeForm(homeForm);        
        setPreferencesForm(preferencesForm);
        setPreferencesController(preferencesController);
    }

    /**
     * Saves the user preferences in the database
     */
    public void savePreferences(){
       Integer[] preferences = new Integer[3];
       //preferences[0] = preferencesForm.getFaceOn().isSelected()?1:0;
       preferences[0] = 1;
       preferences[1] = preferencesForm.getScheduleTagOn().isSelected()?1:0;
       preferences[2] = preferencesForm.getCloudOn().isSelected()?1:0;
       
       String imagePrefix = preferencesForm.getImage().getText();
       String videoPrefix = preferencesForm.getVideo().getText();
       boolean result = false;
        try {
            result = SSNDao.savePreferences(preferences,imagePrefix,videoPrefix);
        } catch (SQLException ex) {
            Logger.getLogger(SSNPreferencesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
       if(result) {
                //JOptionPane.showMessageDialog(preferencesForm ,"Preferences Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Success","","Preferences saved successfully");
            getPreferencesForm().dispose();
        } else {
            //JOptionPane.showMessageDialog(preferencesForm ,"Preferences could not be updated", "Error", JOptionPane.ERROR_MESSAGE);
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(),"Error","","Preferences could not be updated");
            getPreferencesForm().dispose();
        }
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
     *
     * @return
     */
    public SSNPreferencesForm getPreferencesForm() {
        return preferencesForm;
    }
    
    /**
     *
     * @param preferencesForm
     */
    public void setPreferencesForm(SSNPreferencesForm preferencesForm) {
        this.preferencesForm = preferencesForm;
    }
    
    /**
     *
     * @return
     */
    public SSNPreferencesController getPreferencesController() {
        return preferencesController;
    }
    
    /**
     *
     * @param preferencesController
     */
    public void setPreferencesController(SSNPreferencesController preferencesController) {
        this.preferencesController = preferencesController;
    }
    
}
