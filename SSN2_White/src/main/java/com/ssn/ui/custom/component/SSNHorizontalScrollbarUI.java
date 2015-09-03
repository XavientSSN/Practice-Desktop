
package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
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
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author pkumar2
 */
public class SSNHorizontalScrollbarUI extends BasicScrollBarUI{
    private Icon leftArrow, rightArrow;
    private int ratio;
    private boolean leftPanel;
    private Image imageThumb, imageTrack;
        public SSNHorizontalScrollbarUI() {

                    leftArrow = new ImageIcon(getClass().getResource("/images/h-left.png"));
                    rightArrow = new ImageIcon(getClass().getResource("/images/h-right.png"));
                    imageThumb = SSNHorizontalScrollbarUI.FauxImage.create(32, 32,  SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    imageTrack = SSNHorizontalScrollbarUI.FauxImage.create(32, 32, Color.lightGray);

        }
        public SSNHorizontalScrollbarUI(int ratio) {

                    leftArrow = new ImageIcon(getClass().getResource("/images/h-left.png"));
                    rightArrow = new ImageIcon(getClass().getResource("/images/h-right.png"));
                    this.ratio = ratio;
                    imageThumb = SSNHorizontalScrollbarUI.FauxImage.create(32, 32,  SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                    imageTrack = SSNHorizontalScrollbarUI.FauxImage.create(32, 32, Color.lightGray);

        }
    
    
        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton decreaseButton = new JButton(leftArrow){
                @Override
              public Dimension getPreferredSize() {
                   return new Dimension(15, 15);
                }
            };
            Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 1);
            Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
           
            return decreaseButton;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton increaseButton = new JButton(rightArrow){
                @Override
              public Dimension getPreferredSize() {
                   return new Dimension(15, 15);
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
    g.setColor(new Color(137,144,144));

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
            g.setColor(new Color(202,207,203));

            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                int x = insets.left+decrButton.getWidth()/2-2;
                int y = thumbR.y;
                int w = 4;
                int h = incrButton.getY() - y;

                g.fillRect(x, y, w, h);
            }
        }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
    
        try {

            g.setColor(Color.blue);
            ((Graphics2D) g).drawImage(imageThumb,
                r.x, r.y, r.width, r.height, null);
        } catch (Exception ex) {
            Logger.getLogger(SSNMyScrollbarUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        
         BufferedImage image = null;
            try {

                 image = ImageIO.read(getClass().getResource("/images/h-bg.png"));
            } catch (IOException ex) {
                Logger.getLogger(SSNMyScrollbarUI.class.getName()).log(Level.SEVERE, null, ex);
            }
     
        
          g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
         ((Graphics2D)g).drawImage(image, trackBounds.x, trackBounds.y, trackBounds.width,image.getHeight(), null);
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
