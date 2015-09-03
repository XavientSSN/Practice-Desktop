/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.schedule;

import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author skumar13*/

public class ColorArrowUI extends BasicComboBoxUI 
{

 public static ComboBoxUI createUI(JComponent c) {
        return new ColorArrowUI();
    }

    @Override protected JButton createArrowButton() {
        JButton arrowButton =   new JButton(new ImageIcon(getClass().getResource("/icon/down2.png")));
        arrowButton.setBackground(new Color(77,77,77,0));
                arrowButton.setBorder(null);
        arrowButton.setFocusable(false);
        arrowButton.setRolloverEnabled(false);
        arrowButton.setBorderPainted(false);
        arrowButton.setFocusPainted(false);
        arrowButton.setContentAreaFilled(false);
        arrowButton.setOpaque(false);
        
//        BasicArrowButton newArrowButton = new BasicArrowButton(
//        BasicArrowButton.SOUTH,
//        SSNConstants.SSN_BLACK_BACKGROUND_COLOR, SSNConstants.SSN_BLACK_BACKGROUND_COLOR,
//        SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
//        newArrowButton.setBorder(null);
//        newArrowButton.setFocusable(false);
//        newArrowButton.setRolloverEnabled(false);
//        newArrowButton.setBorderPainted(false);
//        newArrowButton.setFocusPainted(false);
//        newArrowButton.setContentAreaFilled(false);
//        newArrowButton.setOpaque(false);
//        return newArrowButton;
        return arrowButton;
    }
}
