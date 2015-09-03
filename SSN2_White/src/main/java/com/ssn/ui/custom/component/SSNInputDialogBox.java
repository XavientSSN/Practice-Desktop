package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNMessageDialogController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author pkumar2
 */
public class SSNInputDialogBox extends JDialog {
    
    private JPanel dialogPanel = null;
    private JLabel dialogCancelLabel = null;
    private JLabel errorIconlLabel = null;
    
   
    private SSNIconTextField dailogMessageTextField = null;
    private JLabel dialogMessageTitle = null;
    private JLabel textLabel = null;
    private JLabel validationLabel = null;
    private JLabel dialogBoxYesBtn = null;
    private JLabel dialogBoxNoBtn = null;
    private JDialog dialog = null;
    private String textValue = null;
    
    private String dialogType = null;
    private String dialogTitle = null;
    private String dialogLabel = null;
    private String validationMessage = null;    
    private boolean selectBox = false;
    private JComboBox comboBox = null;
    private Vector selectValues = null;
    private boolean checkWronEntry=false;

    public SSNInputDialogBox() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        
    }
    
    public SSNInputDialogBox(boolean selectBox, Vector values) {
        this.setSelectBox(selectBox);
        this.setSelectValues(values);
         
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        
    }
    
    private UIDefaults getUidefaults() {
        
        UIDefaults overrides = new UIDefaults();
        overrides.put("TextArea.background", new ColorUIResource(new Color(241, 240, 238)));
        overrides.put("TextArea[Enabled].backgroundPainter", new Painter<JTextArea>() {
            
            @Override
            public void paint(Graphics2D g, JTextArea field, int width, int height) {
                g.setColor(new Color(241, 240, 238));
                g.fill(new Rectangle(0, 0, width, height));
            }
            
        });
        overrides.put("TextArea[Disabled].backgroundPainter", new Painter<JTextArea>() {
            
            @Override
            public void paint(Graphics2D g, JTextArea field, int width, int height) {
                g.setColor(new Color(241, 240, 238));
                Insets insets = field.getInsets();
                g.fill(new Rectangle(
                        insets.left,
                        insets.top,
                        width - (insets.left + insets.right),
                        height - (insets.top + insets.bottom)));
            }
            
        });
        
        return overrides;
    }
    
    public void initDialogBoxUI(String dialogType, String dialogTitle, String dialogLabel) {
        try {
            
            setDialogType(dialogType);
            setDialogTitle(dialogTitle);
            setDialogLabel(dialogLabel);
            
            renderDialogPanel();
            
            initDialogGUIComponents();
            addDialogUIElements();
            positionDialogUIElements();
            loadCustomDialogBox();
            
            renderDialog();
        } catch (Exception ex) {
            Logger.getLogger(SSNMessageDialogBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initDialogBoxUI(String dialogType, String dialogTitle, String dialogLabel, String validationMessage) {
        try {
            
            setDialogType(dialogType);
            setDialogTitle(dialogTitle);
            setDialogLabel(dialogLabel);
            setValidationMessage(validationMessage);
            renderDialogPanel();
            
            initDialogGUIComponents();
            addDialogUIElements();
            positionDialogUIElements();
            loadCustomDialogBox();
            
            renderDialog();
        } catch (Exception ex) {
            Logger.getLogger(SSNMessageDialogBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void renderDialogPanel() {
        try {
            URL imgURL = getClass().getResource("/images/bg_popup.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            this.setDialogPanel(new SSNImagePanel(background));
            this.getDialogPanel().setLayout(null);
        } catch (IOException ex) {
            Logger.getLogger(SSNMessageDialogBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void renderDialog() {
        try {

            // setDialog( new JDialog((Frame) null, TestBackgroundImage.class.getSimpleName()));
            setResizable(false);
            setUndecorated(true);
            setModal(true);
            
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            URL imgURL = getClass().getResource("/images/bg_popup.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Dimension dim = SSNConstants.SSN_SCREEN_SIZE;
            setBounds(0, 0, image.getWidth(), image.getHeight());
            setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            
            LineBorder border = new LineBorder(new Color(165, 165, 165), 1);
            getRootPane().setBorder(border);
            
            add(this.getDialogPanel());
            setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(SSNMessageDialogBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initDialogGUIComponents() {
        this.setDialogMessageTitle(new JLabel());
       
        this.getDialogMessageTitle().setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        this.getDialogMessageTitle().setFont(new Font("open sans", Font.BOLD, 18));
        
        this.setDialogCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png"))));
        
        this.getDialogCancelLabel().addMouseListener(new SSNMessageDialogController(this));
        this.getDialogCancelLabel().setName("closeDialog");
        this.setErrorIconlLabel(new JLabel());
        
        this.setDailogMessageTextField(new SSNIconTextField("","Please Enter Album Name","hivename"));
         this.getDailogMessageTextField().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
               
                 if(isCheckWronEntry())
                {
                   getDailogMessageTextField().setText("");
                    setCheckWronEntry(false);
                }else
                {
                 String input = ((JTextField) e.getSource()).getText();
                 getDailogMessageTextField().setText(input.trim());
                }
                 
                
            }
        });
        this.getDailogMessageTextField().addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                setValidationMessage("* Special Characters (except '_' ) are not allowed!");
                getValidationLabel().setText(getValidationMessage());
            }
            
            
             @Override
            public void keyPressed(KeyEvent e) {
                if(isCheckWronEntry())
                {
                   getDailogMessageTextField().setText("");
                   setCheckWronEntry(false);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                
                 String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags
             
                Pattern regex = Pattern.compile("[[%$&+,:;=?@#|!`~(){}\"><+-]]");
                Matcher matcher = regex.matcher(input);
               

              
                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                 if (KeyEvent.VK_ALT  == e.getKeyCode()) {
                     setCheckWronEntry(true);
                    setValidationMessage("The entered key combination are not allowed!");
                    getValidationLabel().setText(getValidationMessage());  
             }
              else if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+,:;=?@#|!`~(){}\"><+-]", "");
                    getDailogMessageTextField().setText(stemp);
                    setValidationMessage("Special characters are not allowed!");
                    getValidationLabel().setText(getValidationMessage());
                }
              else if (input != null && input.length() > 0) {


                }


            }
            
       });
        
        
        this.getDailogMessageTextField().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        this.getDailogMessageTextField().setFont(new Font("open sans", Font.PLAIN, 12));
        
        //create a rouded corner for the text field
        this.getDailogMessageTextField().setBackground(new Color(77,77,77));
        this.getDailogMessageTextField().setBorder(new SSNCustomBorder(true,new Color(241,240,238),this.getDailogMessageTextField().getHeight(),this.getDailogMessageTextField().getWidth()));
        
        if (this.isSelectBox()) {
            this.setComboBox(new JComboBox(this.getSelectValues()));
            this.getComboBox().setFont(new Font("open sans", Font.PLAIN, 12));
            this.getComboBox().setForeground(new Color(0, 0, 0));
        }
        
        Border paddingBorder = BorderFactory.createEmptyBorder(9, 5, 9, 10);
        Border border = BorderFactory.createLineBorder(new Color(202, 199, 192));

        
        UIDefaults overrides = getUidefaults();

       
        this.setTextLabel(new JLabel(getDialogLabel()));
        this.getTextLabel().setBackground(new Color(241, 240, 238));
        this.getTextLabel().setFont(new Font("open sans", Font.BOLD, 14));
        this.getTextLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        this.setValidationLabel(new JLabel(getValidationMessage()));
        this.getValidationLabel().setBackground(new Color(241, 240, 238));
        this.getValidationLabel().setFont(new Font("open sans", Font.PLAIN, 9));
        
        this.getValidationLabel().setForeground(SSNConstants.SSN_VALIDATION_LABEL_FORE_COLOR);
        
        
        this.setDialogBoxYesBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/ok_prefe_btn.png"))));
       
        this.getDialogBoxYesBtn().setBackground(new Color(255, 209, 25));
        this.getDialogBoxYesBtn().setForeground(new Color(0, 0, 0));
        this.getDialogBoxYesBtn().setFont(new Font("open sans", Font.PLAIN, 14));
        this.getDialogBoxYesBtn().setName("ok");
        
        this.getDialogBoxYesBtn().addMouseListener(new SSNMessageDialogController(this));
        
        this.setDialogBoxNoBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/cancel_prefe_btn.png"))));
        this.getDialogBoxNoBtn().setBackground(new Color(255, 209, 25));
        this.getDialogBoxNoBtn().setForeground(new Color(0, 0, 0));
        this.getDialogBoxNoBtn().setFont(new Font("open sans", Font.PLAIN, 14));
        this.getDialogBoxNoBtn().setName("cancel");
        
        this.getDialogBoxNoBtn().addMouseListener(new SSNMessageDialogController(this));
        this.getDialogBoxNoBtn().addKeyListener(new SSNMessageDialogController(this));
        this.getDialogBoxYesBtn().addKeyListener(new SSNMessageDialogController(this));
    }
    
    private void addDialogUIElements() {
        
        this.getDialogPanel().add(this.getDialogMessageTitle());
        this.getDialogPanel().add(this.getDialogCancelLabel());
        
        this.getDialogPanel().add(this.getErrorIconlLabel());
        this.getDialogPanel().add(this.getTextLabel());
        if (this.isSelectBox()) {
            this.getDialogPanel().add(this.getComboBox());
        } else {
            this.getDialogPanel().add(this.getDailogMessageTextField());
            if (getValidationMessage() != null) {
                this.getDialogPanel().add(this.getValidationLabel());
            }
        }
        this.getDialogPanel().add(this.getDialogBoxYesBtn());
        this.getDialogPanel().add(this.getDialogBoxNoBtn());
    }
    
    private void positionDialogUIElements() {
        
        this.getDialogMessageTitle().setBounds(10, 5, 250, 30);
        this.getDialogCancelLabel().setBounds(425, 5, 250, 30);
        
        this.getErrorIconlLabel().setBounds(20, 70, 32, 32);
        this.getTextLabel().setBounds(190, 60, 200, 30);
        if (this.isSelectBox()) {
            this.getComboBox().setBounds(190, 90, 200, 30);
        } else {
            this.getDailogMessageTextField().setBounds(180, 100, 250, 30);
            if (getValidationMessage() != null) {
                this.getValidationLabel().setBounds(220, 145, 300, 10);
            }
        }
        this.getDialogBoxYesBtn().setBounds(160, 190, 120, 60);
        this.getDialogBoxNoBtn().setBounds(335, 190, 120, 60);
    }
    
    private void loadCustomDialogBox() {
        
        getDialogMessageTitle().setText(getDialogTitle());
        
    }
    
    public JPanel getDialogPanel() {
        return dialogPanel;
    }
    
    public void setDialogPanel(JPanel dialogPanel) {
        this.dialogPanel = dialogPanel;
    }
    
    public JLabel getDialogCancelLabel() {
        return dialogCancelLabel;
    }
    
    public void setDialogCancelLabel(JLabel dialogCancelLabel) {
        this.dialogCancelLabel = dialogCancelLabel;
    }
    
    public JTextField getDailogMessageTextField() {
        return dailogMessageTextField;
    }
    
    public void setDailogMessageTextField(SSNIconTextField dailogMessageTextField) {
        this.dailogMessageTextField = dailogMessageTextField;
    }
    
    public JLabel getDialogMessageTitle() {
        return dialogMessageTitle;
    }
    
    public void setDialogMessageTitle(JLabel dialogMessageTitle) {
        this.dialogMessageTitle = dialogMessageTitle;
    }
    
    public JLabel getDialogBoxYesBtn() {
        return dialogBoxYesBtn;
    }
    
    public void setDialogBoxYesBtn(JLabel dialogBoxYesBtn) {
        this.dialogBoxYesBtn = dialogBoxYesBtn;
    }
    
    public JLabel getDialogBoxNoBtn() {
        return dialogBoxNoBtn;
    }
    
    public void setDialogBoxNoBtn(JLabel dialogBoxNoBtn) {
        this.dialogBoxNoBtn = dialogBoxNoBtn;
    }
    
    public JDialog getDialog() {
        return dialog;
    }
    
    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }
    
    public String getDialogType() {
        return dialogType;
    }
    
    public void setDialogType(String dialogType) {
        this.dialogType = dialogType;
    }
    
    public String getDialogTitle() {
        return dialogTitle;
    }
    
    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }
    
    public String getTextValue() {
        return textValue;
    }
    
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
    
    public JLabel getTextLabel() {
        return textLabel;
    }
    
    public void setTextLabel(JLabel textLabel) {
        this.textLabel = textLabel;
    }
    
    public String getDialogLabel() {
        return dialogLabel;
    }
    
    public void setDialogLabel(String dialogLabel) {
        this.dialogLabel = dialogLabel;
    }
    
    public JLabel getErrorIconlLabel() {
        return errorIconlLabel;
    }
    
    public void setErrorIconlLabel(JLabel errorIconlLabel) {
        this.errorIconlLabel = errorIconlLabel;
    }
    
    public boolean isSelectBox() {
        return selectBox;
    }
    
    public void setSelectBox(boolean selectBox) {
        this.selectBox = selectBox;
    }
    
    public JComboBox getComboBox() {
        return comboBox;
    }
    
    public void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }
    
    public Vector getSelectValues() {
        return selectValues;
    }
    
    public void setSelectValues(Vector selectValues) {
        this.selectValues = selectValues;
    }
    
    public JLabel getValidationLabel() {
        return validationLabel;
    }
    
    public void setValidationLabel(JLabel validationLabel) {
        this.validationLabel = validationLabel;
    }
    
    public String getValidationMessage() {
        return validationMessage;
    }
    
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                    String title = "This is a custom dialog Box!";
                    String header = "This is message header.";
                    String mesage = "I'm giving away a tip a day, via video, all of this week. Today's one is on my LinkedIn blog, along with a thank you message for all my followers. The tip itself is about selling your culture at job interviews. I'll have another one, right here on LinkedIn, tomorrow morning.";
                    // new SSNMessageDialogBox(SSNDialogChoice.ERROR_DIALOG.getType(),title,header,mesage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @return the checkWronEntry
     */
    public boolean isCheckWronEntry() {
        return checkWronEntry;
    }

    /**
     * @param checkWronEntry the checkWronEntry to set
     */
    public void setCheckWronEntry(boolean checkWronEntry) {
        this.checkWronEntry = checkWronEntry;
    }
    
}
