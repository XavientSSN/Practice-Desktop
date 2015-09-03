/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.schedule;

import com.ssn.app.loader.SSNConstants;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author hvashistha
 */

public class SSNScheduleFormUtilityLoader extends JPanel{
        JPanel dateChooserPnl = null;
        static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleFormUtilityLoader.class);
      public SSNScheduleFormUtilityLoader(SSNHomeForm sSNHomeForm,SSNScheduleTagPanelForm panelForm) {
          
        Dimension fieldsHgt = panelForm.getSsnScheduleTagKeywordTxt().getPreferredSize(); 
        Dimension comboDim = panelForm.getSsnScheduleTagKeywordTxt().getPreferredSize(); 
        Dimension quickAlbmDim = panelForm.getSsnScheduleTagKeywordTxt().getPreferredSize(); 
        Dimension fieldsWid = panelForm.getSsnScheduleTagKeywordTxt().getPreferredSize(); 
        Dimension dateChooserDim = panelForm.getStartDateChooser().getPreferredSize();
        dateChooserDim.height = 30;
        dateChooserDim.width = 200;
        quickAlbmDim.height = 40;
        quickAlbmDim.width = 40;
        comboDim.height = 30;
        comboDim.width = 400;
        fieldsHgt.height = 40;
        fieldsWid.width = 140;
        Dimension btnWid = panelForm.getSsnScheduleTagKeywordTxt().getPreferredSize(); 
        btnWid.width = 40;
        JPanel form = new JPanel();
        form.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        Border borderVer = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 2, 0, 2);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        form.setBorder(borderVer);
        add(form);

        
        form.setLayout(new GridBagLayout());
        SSNScheduleFormUtility formUtility = new SSNScheduleFormUtility();

        
        formUtility.addLabel(panelForm.getSsnScheduleTagAlbumFieldLabel(), form);
        
        panelForm.getSsnScheduleTagAlbumCombo().setPreferredSize(comboDim); 
        panelForm.getSsnScheduleTagAlbumCombo().setFocusable(false);
        
        
        JPanel albumPnl = new JPanel();
        
        albumPnl.setLayout(new BorderLayout());
        
        //Added by sandeep
        Border boxYellowBorder = BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,2);
        panelForm.getSsnScheduleTagAlbumCombo().setEditable(true);
        panelForm.getSsnScheduleTagAlbumCombo().setUI(com.ssn.schedule.ColorArrowUI.createUI(panelForm.getSsnScheduleTagAlbumCombo()));
        panelForm.getSsnScheduleTagAlbumCombo().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        panelForm.getSsnScheduleTagAlbumCombo().setBorder(boxYellowBorder);
        
        JTextField field=(JTextField)panelForm.getSsnScheduleTagAlbumCombo().getEditor().getEditorComponent();
        field.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        field.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        field.setAlignmentX(SwingConstants.LEFT);
        field.setAlignmentY(SwingConstants.LEFT);
        field.setBorder(null);
        
        //End 
        ((JLabel)panelForm.getSsnScheduleTagAlbumCombo().getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        albumPnl.add(panelForm.getSsnScheduleTagAlbumCombo(),BorderLayout.WEST);
       
        formUtility.addLastField(albumPnl,form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagTitleFieldLabel(), form);
        
        formUtility.addLastField(panelForm.getSsnScheduleTagTitleTxt(), form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagFilenameLabel(), form);
        formUtility.addLastField(panelForm.getSsnScheduleTagFileNameTxt(), form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagVDOFilenameLabel(), form);
        formUtility.addLastField(panelForm.getSsnScheduleTagVDOFileNameTxt(), form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagLocationFieldLabel(), form);
        //panelForm.getSsnScheduleTagLocationTxt().setPreferredSize(fieldsHgt);
        
        formUtility.addLastField(panelForm.getSsnScheduleTagLocationTxt(), form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagStartdateLabel(), form);
        
        
        dateChooserPnl = new JPanel();
        JPanel timeChooserPnl = new JPanel();
        
        dateChooserPnl.setLayout(new BorderLayout());
        timeChooserPnl.setLayout(new BorderLayout());
        //System.out.println("dateChooserPnl");
        dateChooserPnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        timeChooserPnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
       
        panelForm.getStartDateChooser().setPreferredSize(dateChooserDim);
        panelForm.getEndDateChooser().setPreferredSize(dateChooserDim);
        
        panelForm.getStartDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        panelForm.getStartDateChooser().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        panelForm.getStartDateChooser().getDateEditor().getUiComponent().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        dateChooserPnl.add(panelForm.getStartDateChooser(),BorderLayout.WEST);
        
        timeChooserPnl.add(panelForm.getSsnScheduleTagStarttimeLabel(),BorderLayout.WEST);
        
        JPanel startTimePnl = new JPanel();
        startTimePnl.setLayout(new GridBagLayout());
        startTimePnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        Dimension timeSpinnerDim1 = panelForm.getStartTimeSpinner().getPreferredSize();
        timeSpinnerDim1.width = 80;
        panelForm.getStartTimeSpinner().setPreferredSize(timeSpinnerDim1);
        
        timeChooserPnl.add(panelForm.getStartTimeSpinner(),BorderLayout.EAST);
        
        this.setDateChooserPnl(dateChooserPnl);
        dateChooserPnl.setOpaque(false);
        
        formUtility.addMiddleField(dateChooserPnl, form);
        formUtility.addLastField(timeChooserPnl, form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagEnddateLabel(), form);
        panelForm.getEndDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        formUtility.addMiddleField(panelForm.getEndDateChooser(), form);
        
        JPanel endTimePnl = new JPanel();
        endTimePnl.setLayout(new GridBagLayout());
        endTimePnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        formUtility.addLabel(panelForm.getSsnScheduleTagEndTimeLabel(), endTimePnl);
        Dimension timeSpinnerDim2 = panelForm.getEndTimeSpinner().getPreferredSize();
        timeSpinnerDim2.width = 80;
        panelForm.getEndTimeSpinner().setPreferredSize(timeSpinnerDim2);
        
        formUtility.addLabel(panelForm.getEndTimeSpinner(), endTimePnl);
        formUtility.addLastField(endTimePnl, form);
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel(panelForm.getSsnScheduleTagKeywordsFieldLabel(), form);
        
        formUtility.addLastField(panelForm.getSsnScheduleTagKeywordTxt(), form);        
       
        formUtility.addLastVerticalStruts(Box.createVerticalStrut(10), form);
        formUtility.addLabel("", form);
        
        JPanel btnPnl = new JPanel();
        btnPnl.setLayout(new BorderLayout());
        btnPnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        btnPnl.add(panelForm.getSsnScheduleTagSaveBtn(),BorderLayout.WEST);
        btnPnl.add(panelForm.getSsnScheduleTagCancelBtn(),BorderLayout.EAST);
        
        formUtility.addMiddleField(btnPnl, form);

        // Add an little padding around the form
        setVisible(true);
    }

    public JPanel getDateChooserPnl() {
        return dateChooserPnl;
    }

    public void setDateChooserPnl(JPanel dateChooserPnl) {
        this.dateChooserPnl = dateChooserPnl;
    }
      
}
