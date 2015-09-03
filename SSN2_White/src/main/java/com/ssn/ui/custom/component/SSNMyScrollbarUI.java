package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author pkumar2
 */
public class SSNMyScrollbarUI extends BasicScrollBarUI{
        private Icon downArrow, upArrow;
        private double ratio=1;
        private boolean leftPanel = false;
        private Image imageThumb;
        private Image imageTrack;
        public Dimension dimension  =   Toolkit.getDefaultToolkit().getScreenSize();

    public SSNMyScrollbarUI() {
     
                upArrow = new ImageIcon(getClass().getResource("/icon/up.png"));
                downArrow = new ImageIcon(getClass().getResource("/icon/down.png"));
                      
    }
    public SSNMyScrollbarUI(double ratio) {
     

            upArrow = new ImageIcon(getClass().getResource("/icon/up.png"));
            downArrow = new ImageIcon(getClass().getResource("/icon/down.png"));
            this.ratio = ratio;     
            imageThumb = FauxImage.create(32, 32,  SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            imageTrack = FauxImage.create(32, 32, Color.lightGray);
    }
    
     public SSNMyScrollbarUI(double ratio,boolean leftPanel) {
     
            upArrow = new ImageIcon(getClass().getResource("/icon/up.png"));
            downArrow = new ImageIcon(getClass().getResource("/icon/down.png"));
            this.ratio = ratio;
            this.leftPanel = leftPanel;
            imageThumb = FauxImage.create(32, 32, SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            imageTrack = FauxImage.create(32, 32, Color.BLACK);
    }
    
    
        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton decreaseButton = new JButton(upArrow){
                @Override
              public Dimension getPreferredSize() {
                   return new Dimension(16, 16);
                }
            };
            Border paddingBorder = BorderFactory.createEmptyBorder(2, 0, 3, 0);
            Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            decreaseButton.setBorder(paddingBorder);
            return decreaseButton;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton increaseButton = new JButton(downArrow){
                @Override
              public Dimension getPreferredSize() {
                   return new Dimension(16, 16);
                }
                
            };
            
            Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 1);
            Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            return increaseButton;
        }


    @Override
    protected void setThumbRollover(boolean active) {
        super.setThumbRollover(false); 
    }
     
     
    
  @Override
    protected void paintDecreaseHighlight(Graphics g)
    {
    Insets insets = scrollbar.getInsets();
    Rectangle thumbR = getThumbBounds();
    g.setColor(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);

    if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
        int x = insets.left+decrButton.getWidth()/2-2;
        int y = decrButton.getY() + decrButton.getHeight();
        int w = 4;
        int h = thumbR.y - y;
        
        g.fillRect(x, y, w, h);
    } 
    else    {
        int x, w;
        if (scrollbar.getComponentOrientation().isLeftToRight()) {
            x = decrButton.getX() + decrButton.getWidth();
            w = thumbR.x - x;
        } else {
            x = thumbR.x + thumbR.width;
            w = decrButton.getX() - x;
        }
        int y = insets.top;
        int h = scrollbar.getHeight() - (insets.top + insets.bottom);
   
        g.fillRect(x, y, w, h);
    }
    }

    @Override
    protected void paintIncreaseHighlight(Graphics g) {
            Insets insets = scrollbar.getInsets();
            Rectangle thumbR = getThumbBounds();
            g.setColor(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);

            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                int x = insets.left+decrButton.getWidth()/2-2;
                int y = thumbR.y;
                int w = 4;
                int h = incrButton.getY() - y;
                g.fillRect(x, y, w, h);
            }
        }

    @Override
    protected void paintThumb(Graphics g1, JComponent c, Rectangle r) {
         g1.setColor(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
            ((Graphics2D) g1).drawImage(imageThumb,
                r.x, r.y, r.width, r.height, null);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        
          
            try {
        
                 BufferedImage   image = ImageIO.read(getClass().getResource("/images/h-temp-scroll-bigNEW.jpg"));
                // ((Graphics2D)g).drawImage(image, trackBounds.x, trackBounds.y, image.getWidth()+1,550, null);
                  ((Graphics2D)g).drawImage(image, trackBounds.x, trackBounds.y, image.getWidth()+1,(int)dimension.getHeight(), null);

            } catch (IOException ex) {
                Logger.getLogger(SSNMyScrollbarUI.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        
    }
    private static class FauxImage {

        static public Image create(int w, int h, Color c) {
            BufferedImage bi = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bi.createGraphics();
            g2d.setPaint(c);
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
            return bi;
        }
    }
 
    
}
