/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ui.custom.component;

import com.ssn.ui.form.SSNHomeForm;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author aarora1
 */
public class SSNRotatedImagePanel extends JPanel{
    
     private static final long  serialVersionUID = 1L;
    private              Image panelImage       = null;
    private              int   panelWidth       = 0;
    private              int   panelHeight      = 0;
    private              int   multiplier       =0; 
    private              int x,y;
    private              boolean drag=false;
   
    
    public SSNRotatedImagePanel(Image image,int multiplier,SSNHomeForm ssnHomeForm) {
        this.setPanelImage(image);
        this.setPanelWidth(image.getWidth(this) / 2);
        this.setPanelHeight(image.getHeight(this) / 2);
        this.setMultiplier(multiplier);
        int screenWidth = ssnHomeForm.getSsnHomeCenterPanel().getWidth();
        int screenHeight = ssnHomeForm.getSsnHomeCenterPanel().getHeight();
        if(image.getWidth(this)>screenWidth || image.getHeight(this)>screenHeight){
            addMouseMotionListener(new MouseMotionHandler());
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w2 = getWidth() / 2;
        int h2 = getHeight() / 2;
        g2d.rotate(Math.PI / 2 * getMultiplier(), w2, h2);
        if(!drag){
         x = this.getParent().getWidth() / 2 - this.getPanelWidth();
         y = this.getParent().getHeight() / 2 - this.getPanelHeight();
        }
        g.drawImage(this.getPanelImage(), x, y, this);
        
    }

    
    /**
     * @return the panelImage
     */
    public Image getPanelImage() {
        return panelImage;
    }

    /**
     * @param panelImage the panelImage to set
     */
    public void setPanelImage(Image panelImage) {
        this.panelImage = panelImage;
    }

    /**
     * @return the panelWidth
     */
    public int getPanelWidth() {
        return panelWidth;
    }

    /**
     * @param panelWidth the panelWidth to set
     */
    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    /**
     * @return the panelHeight
     */
    public int getPanelHeight() {
        return panelHeight;
    }

    /**
     * @param panelHeight the panelHeight to set
     */
    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }
    
     public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
    
   class MouseMotionHandler extends MouseMotionAdapter {
        
   public void mouseDragged(MouseEvent e) {
      drag =true;
      x = e.getX()-200;
      y = e.getY()-200;
      int multiplier = SSNRotatedImagePanel.this.getMultiplier();
      if(multiplier%2==0 && multiplier%4!=0){
          x = -(e.getX()-200);
          y = -(e.getY()-200);
      }
      if((multiplier-1)%4==0){
          y = -(e.getX()-200);
          x = e.getY()-200;
      }
      if((multiplier-1)%2==0 && (multiplier-1)%4!=0){
          y = e.getX()-200;
          x = -(e.getY()-200);
      } 
      repaint();
    }
  }

      
  }
    
