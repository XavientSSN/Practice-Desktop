package com.ssn.ui.custom.component;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author vkvarma
 */

public final class SSNImagePanel extends JPanel {

    private static final long  serialVersionUID = 1L;
    private              Image panelImage       = null;
    private              int   panelWidth       = 0;
    private              int   panelHeight      = 0;
    private              int   xPosition        = 0;
    private              int   yPosition        = 0;
    
    public SSNImagePanel(Image image) {
        this.setPanelImage(image);
        this.setPanelWidth(image.getWidth(this) / 2);
        this.setPanelHeight(image.getHeight(this) / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.getPanelImage() != null) {
            int x = this.getParent().getWidth() / 2 - this.getPanelWidth();
            int y = this.getParent().getHeight() / 2 - this.getPanelHeight();
            this.setxPosition(x);
            this.setyPosition(y);
            g.drawImage(this.getPanelImage(), x, y, this);
        }
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

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    
    
}