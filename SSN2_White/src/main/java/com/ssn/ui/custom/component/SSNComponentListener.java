package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNHelper;
import com.ssn.schedule.SSNScheduleTagController;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import com.ssn.speech.SSNMicrophoneFrame;
import com.ssn.ui.form.SSNHomeForm;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author pkumar2
 */
public class SSNComponentListener implements ComponentListener {

    private SSNHomeForm homeForm;
      private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNComponentListener.class);
    public SSNComponentListener(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    @Override
    public void componentResized(ComponentEvent e) {

        if (homeForm.getSsnHomeCenterMainPanel() != null) {
            for (Component c : homeForm.getSsnHomeCenterMainPanel().getComponents()) {
                if (c.getName() != null && c.getName().equalsIgnoreCase("buttonPanel")) {
                    try {
                        // SSNImagePanel panel = (SSNImagePanel) homeForm.getSsnImagePanel();
                        BufferedImage image1 = ImageIO.read(homeForm.getCurrentFile());

                        //Image background = image1.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
                        Image background = SSNHelper.getScaledImage(image1, homeForm, false, 1);
                        SSNImagePanel panel = new SSNImagePanel(background);
                        panel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
                        homeForm.setSsnImagePanel(panel);
                        homeForm.setZoomIn(1);
                        homeForm.setRotateAngleMultiple(0);
                        homeForm.getSsnHomeCenterMainPanel().remove(homeForm.getSsnHomeCenterPanel());
                        homeForm.getSsnHomeCenterPanel().removeAll();
                        homeForm.getSsnHomeCenterPanel().add(panel);
                        homeForm.getSsnHomeCenterMainPanel().add(homeForm.getSsnHomeCenterPanel(), BorderLayout.CENTER);
                        homeForm.getSsnHomeCenterMainPanel().remove(homeForm.getSsnHomeCenterButtonPanel());
                        homeForm.getSsnHomeCenterMainPanel().add(homeForm.getCenterButtonPanel(homeForm.getRatingMap().get(homeForm.getCurrentFile().getAbsolutePath())), BorderLayout.SOUTH);
                        homeForm.getSsnHomeCenterMainPanel().revalidate();
                        homeForm.getSsnHomeCenterMainPanel().repaint();
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(SSNComponentListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //for (Component c1 : homeForm.getSsnHomeCenterPanel().getComponents()) {
            if (homeForm.getSsnScheduleTagPanelForm() != null) {
                if (homeForm.getSsnScheduleTagPanelForm().getMainScrollPane() != null && homeForm.getSsnScheduleTagPanelForm().getMainScrollPane().getName().equalsIgnoreCase("SSNScheduleTagPanelForm")) {
                    homeForm.getTreePanel().removeAll();

                    SSNScheduleTagController sNScheduleTagController = new SSNScheduleTagController(homeForm.getSsnScheduleTagPanelForm(), homeForm);
                       // homeForm.setScheduletagTreepanel(sNScheduleTagController.addScheduleTagTreePanel());

                    homeForm.getTreePanel().add(sNScheduleTagController.addScheduleTagTreePanel());
                    //new SSNScheduleTagPanelForm(homeForm, "Tag Event Schedule", null, null);
                    homeForm.getTreePanel().revalidate();
                    homeForm.getTreePanel().repaint();

                    homeForm.revalidate();
                }
            }
            //}
        }

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

}
