package com.ssn.ui.custom.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import org.apache.log4j.Logger;

/**
 *
 * @author vkvarma
 */
public final class SSNIconTextField extends JTextField {

    private static final long   serialVersionUID = 1L;    
    private              Icon   icon             = null;
    private              String hint             = null;
    private              Insets dummyInsets      = null;
    private              String txtId            = null;
     private              String tempHint             = null;
    private final static Logger logger = Logger.getLogger(SSNIconTextField.class);
     
    public SSNIconTextField(String icon, String hint,String txtId) {
        
        
        try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        
        if(icon != null && (!icon.equals(""))) {
            this.setIcon(createImageIcon(icon, icon)); 
        }
        tempHint = hint;
        this.setHint(hint);
        this.setHorizontalAlignment(JTextField.CENTER);
        JTextField dummy = new JTextField();
        dummy.setHorizontalAlignment(JTextField.CENTER);
        
        this.setDummyInsets(dummy.getInsets());
        this.setTxtId(txtId); 
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) 
            {
                Object keyEventObj = e.getSource();
                JTextField textField = (JTextField) e.getSource();
                String text = textField.getText();
                if(text != null && text.length()>=40)
                {
                    textField.setText(text.substring(0, 40));
                }
                
            }
           
         @Override
            public void keyReleased(KeyEvent e) {
                Object keyEventObj = e.getSource();
                
                JTextField textField = (JTextField) e.getSource();
                //System.out.println(textField.getName());
                 String input = textField.getText();
                // using pattern with flags
             
                //
                // using pattern with flags
                 String expression = "[%$&+,:;=?@#|!^*`~(){}\"><+-,.']";
                 
                 if(textField.getName()!=null && textField.getName().equalsIgnoreCase("email")){
                     expression = "[%$&+,:;=?#|!^*`~(){}\"><+-,']";
                 }
                Pattern regex = Pattern.compile(expression);
                Matcher matcher = regex.matcher(input);
               

              
                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                 if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     //setCheckWronEntry(true);
                    //setValidationMessage("The entered key combination are not allowed!");
                    //getValidationLabel().setText(getValidationMessage());  
             }
              else if (matcher.find()) {
                    String stemp = input.replaceAll(expression, "");
                    textField.setText(stemp);
                    //setValidationMessage("Special characters are not allowed!");
                    //getValidationLabel().setText(getValidationMessage());
                }
              else if (input != null && input.length() > 0) {


                }


            }
        });
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                 Object mouseEventObj = e.getSource();
                 if(mouseEventObj instanceof SSNIconTextField)
                 {
                    SSNIconTextField textField = (SSNIconTextField) e.getSource();
                    textField.setHint("");
                    repaint();
                   }
              }          
            });
        this.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e) {
                Object focusEventObj = e.getSource();
                 if(focusEventObj instanceof SSNIconTextField)
                 {
                     SSNIconTextField textField = (SSNIconTextField) e.getSource();
                      if((textField.getText() == null || textField.getText().isEmpty())&& !textField.hasFocus())
                          {
                            textField.setText("");
                            textField.setHint(tempHint);
                          }
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
    
    public String getFormattedHint(String hint)
    {
        return hint;
    }
       
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int textX = 2;

//        if(this.getIcon() != null) {
//            int iconWidth = getIcon().getIconWidth();
//            int iconHeight = getIcon().getIconHeight();
//            int x = getDummyInsets().left - 5;
//            textX = x+iconWidth+2;
//            int y = (this.getHeight() - iconHeight)/2;
//            getIcon().paintIcon(this, g, x, y);           
//        }
         
        setMargin(new Insets(2, textX, 2, 2));        
          
        if ( this.getText().trim().equals("")) {
            int width = this.getWidth();
            int height = this.getHeight();
            Font prev = g.getFont();
            Font italic = prev.deriveFont(Font.BOLD);
            Color prevColor = g.getColor();
            
            g.setFont(new Font("Open sans",Font.BOLD,14));
            Graphics2D g2d          = (Graphics2D) g;
            RenderingHints hints    = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
           // g2d.drawString(getHint(), x, textBottom);     
            Dimension d = this.getSize();
            drawCenteredString(getHint(), d.width, d.height, g);

            g2d.setRenderingHints(hints);
            
            g.setFont(prev);     
            g.setColor(new Color(163,159,156));
        }
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);

        g.drawString(s, x, y);
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