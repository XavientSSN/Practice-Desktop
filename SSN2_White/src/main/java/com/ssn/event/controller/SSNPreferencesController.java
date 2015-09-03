package com.ssn.event.controller;

import com.ssn.model.SSNPreferencesModel;
import com.ssn.ui.form.SSNPreferencesForm;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author vkvarma
 */
public class SSNPreferencesController extends SSNBaseController {

    private SSNPreferencesForm preferencesForm = null;
    private SSNPreferencesModel preferencesModel = null;
final    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNPreferencesController.class);
    public SSNPreferencesController(SSNPreferencesForm preferencesForm) {
        this.setPreferencesForm(preferencesForm);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton jBtn = (JButton) actionEventObj;
            if (jBtn.getActionCommand().equalsIgnoreCase("savePreferences")) {
                this.getPreferencesModel().savePreferences();
            }
            if (jBtn.getActionCommand().equalsIgnoreCase("cancelPreferences")) {
               getPreferencesForm().dispose();
            }

        }
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jBtn = (JLabel) actionEventObj;
            
            if (jBtn.getName().equalsIgnoreCase("savePreferences")) {
                this.getPreferencesModel().savePreferences();
            }
            if (jBtn.getName().equalsIgnoreCase("cancelPreferences")) {
               getPreferencesForm().dispose();
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) actionEventObj;
            if (jLbl.getName().equalsIgnoreCase("closePreferences")) {
                getPreferencesForm().dispose();
            }
        }
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jBtn = (JLabel) actionEventObj;
          
            if (jBtn.getName().equalsIgnoreCase("savePreferences")) {
                this.getPreferencesModel().savePreferences();
            }
            if (jBtn.getName().equalsIgnoreCase("cancelPreferences")) {
               getPreferencesForm().dispose();
            }

        }
    }

    public SSNPreferencesForm getPreferencesForm() {
        return preferencesForm;
    }

    public void setPreferencesForm(SSNPreferencesForm preferencesForm) {
        this.preferencesForm = preferencesForm;
    }

    public SSNPreferencesModel getPreferencesModel() {
        return preferencesModel;
    }

    public void setPreferencesModel(SSNPreferencesModel preferencesModel) {
        this.preferencesModel = preferencesModel;
    }

}
