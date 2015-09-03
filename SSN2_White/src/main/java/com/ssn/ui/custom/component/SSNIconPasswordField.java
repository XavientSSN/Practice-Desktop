package com.ssn.ui.custom.component;

import com.ssn.helper.SSNHelper;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author vkvarma
 */
public final class SSNIconPasswordField extends JPasswordField {

    private static final long   serialVersionUID = 1L;
    private              Icon   icon             = null;
    private              String hint             = null;
    private              Insets dummyInsets      = null;
    private              String txtId            = null;
   boolean mouseClicked = false;
   boolean focusLost = false;
    
    public SSNIconPasswordField() {
    }

    public SSNIconPasswordField(String icon, String hint,String txtId) {
       
        if(icon != null && (!icon.equals(""))) {
            this.setIcon(createImageIcon(icon, icon)); 
        }
        this.setHint(hint);
        JTextField dummy = new JTextField();
        this.setDummyInsets(dummy.getInsets());
        this.setTxtId(txtId); 
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                 Object mouseEventObj = e.getSource();
                 if(mouseEventObj instanceof SSNIconPasswordField)
                 {
                    mouseClicked=true;
                    focusLost = false;
                 }
              }          
            });
        this.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e) {
                Object focusEventObj = e.getSource();
                 if(focusEventObj instanceof SSNIconPasswordField)
                 {
                      focusLost = true;
                      mouseClicked = false;
                 }
            }
           
        });
    }

    public void setIcon(Icon newIcon) {
        this.icon = newIcon;
    }

    /**
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * @return the dummyInsets
     */
    public Insets getDummyInsets() {
        return dummyInsets;
    }

    /**
     * @param dummyInsets the dummyInsets to set
     */
    public void setDummyInsets(Insets dummyInsets) {
        this.dummyInsets = dummyInsets;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int textX = 2;

        if (this.getIcon() != null) {
            int iconWidth = getIcon().getIconWidth();
            int iconHeight = getIcon().getIconHeight();
            int x = getDummyInsets().left - 5;
            textX = x + iconWidth + 2;
            int y = (this.getHeight() - iconHeight) / 2;
            getIcon().paintIcon(this, g, x, y);
        }

        setMargin(new Insets(2, textX, 2, 2));

        if (this.getText() != null && this.getText().trim().equals("")) { 
            int width = this.getWidth(); 
           int height = this.getHeight();
            Font prev = g.getFont();
            Font italic = prev.deriveFont(Font.BOLD);
            Color prevColor = g.getColor();
            g.setFont(italic);
            g.setColor(UIManager.getColor("textInactiveText"));
            int h = g.getFontMetrics().getHeight();
            int textBottom = (height - h) / 2 + h - 4;
            int x = textX + 28;
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints hints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // set password field String
            if(SSNHelper.getDeviceType().equals("Desktop-Win"))
            {
                if(mouseClicked && !focusLost)
                {
                    g2d.drawString("", x, textBottom);
                }
                else
                {
                    g2d.drawString("\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF", x, textBottom);
                }
            }
            else
            {
                if(mouseClicked && !focusLost)
                {
                    g2d.drawString("", x, textBottom);
                }
                else
                {
                    g2d.drawString("\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF\u25CF", x, textBottom);
                }
                
            }
            
            g2d.setRenderingHints(hints);
            g.setFont(prev);
            g.setColor(new Color(255,255,255));
            repaint();
        }

    }

    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * @return the txtId
     */
    public String getTxtId() {
        return txtId;
    }

    /**
     * @param txtId the txtId to set
     */
    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }
}