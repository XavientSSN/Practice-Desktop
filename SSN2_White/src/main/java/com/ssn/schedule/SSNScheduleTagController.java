/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.schedule;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNBaseController;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.util.SSNScheduleTagJTree;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author hvashistha
 */
public class SSNScheduleTagController extends SSNBaseController {

    private SSNHomeForm               homeForm                         = null;
    private SSNScheduleTagPanelForm   scheduleTagPanelFormMerger   = null;
    private SSNScheduleTagPanelModel  scheduleTagPanelModel  = null;
    
    public static boolean preventOverlap = false;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleTagController.class);
    public SSNScheduleTagController(SSNScheduleTagPanelForm aThis,SSNHomeForm homeForm) {
        setHomeForm(homeForm);
        setScheduleTagPanelFormMerger(aThis);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object actionEventObj = e.getSource();
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) actionEventObj;
            if(jLbl.getName().equalsIgnoreCase("closeProfile")) {
                getHomeForm().getSsnHomeCenterPanel().removeAll();
            }
        }
    }
    
    public JPanel addScheduleTagTreePanel() {
        
        JPanel retPnl = null;
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 15, 0, 20);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        try {
            retPnl = new SSNScheduleTagJTree(this.homeForm,this);   
            retPnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            retPnl.setVisible(true);
            
            
           // retPnl.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        } catch (SQLException ex) {
            
            Logger.getLogger(SSNScheduleTagController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return retPnl;
    }
    
    /**
     * @return the scheduleTagPanelModel
     */
    public SSNScheduleTagPanelModel getScheduleTagPanelModel() {
        return scheduleTagPanelModel;
    }

    /**
     * @param scheduleTagPanelModel the scheduleTagPanelModel to set
     */
    public void setScheduleTagPanelModel(SSNScheduleTagPanelModel scheduleTagPanelModel) {
        this.scheduleTagPanelModel = scheduleTagPanelModel;
    }

    /**
     * @return the scheduleTagPanelFormMerger
     */
    public SSNScheduleTagPanelForm getScheduleTagPanelFormMerger() {
        return scheduleTagPanelFormMerger;
    }

    /**
     * @param scheduleTagPanelFormMerger the scheduleTagPanelFormMerger to set
     */
    private void setScheduleTagPanelFormMerger(SSNScheduleTagPanelForm scheduleTagPanelFormMerger) {
        this.scheduleTagPanelFormMerger = scheduleTagPanelFormMerger;
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
    private void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }
    
}
