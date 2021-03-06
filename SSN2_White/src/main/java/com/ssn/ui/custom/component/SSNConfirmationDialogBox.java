package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.event.controller.SSNMessageDialogController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
public class SSNConfirmationDialogBox extends JDialog{

   
     private              JPanel                    dialogPanel                   = null;
     private              JLabel                    dialogCancelLabel             = null; 
     private              JLabel                    errorIconlLabel               = null;
     private              JLabel                    notificationIconlLabel        = null;
     private              JLabel                 dailogMessageTextArea         = null;
     private              JLabel                    dialogMessageTitle            = null;
     private              JLabel                    dialogMessageHeader           = null;
     private              JLabel                    dialogBoxYesBtn                = null;
     private              JLabel                    dialogBoxNoBtn                = null;
     private              JDialog                   dialog                        = null;
     
     
     private              String                    dialogType                    = null;
     private              String                    dialogTitle                   = null;
     private              String                    dialogHeader                  = null;
     private              String                    dialogMessage                 = null;
     private              int                       result;

     private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNConfirmationDialogBox.class);
    public SSNConfirmationDialogBox() {
        try {
              UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
         }
       
    }
     
     
      private UIDefaults getUidefaults()
    {
    
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
     

    public void initDialogBoxUI(String dialogType,String dialogTitle,String dialogHeader,String dialogMessage)  {
         try {
             
             
              setDialogType(dialogType);
              setDialogTitle(dialogTitle);
              setDialogHeader(dialogHeader);
              setDialogMessage(getFormattedMessage(dialogMessage));
             
             
             
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
             URL imgURL          = getClass().getResource("/images/Confirmation_bg.jpg");
             BufferedImage image = ImageIO.read(imgURL);
             Image background    = image.getScaledInstance(image.getWidth(),image.getHeight(), Image.SCALE_SMOOTH);
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
             URL imgURL          = getClass().getResource("/images/Confirmation_bg.jpg");
             BufferedImage image = ImageIO.read(imgURL);
               Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
             setBounds(0,0,image.getWidth(),image.getHeight());
             setLocation(dim.width/2- this.getSize().width/2, dim.height/2- this.getSize().height/2);
             
           //  LineBorder border = new LineBorder(new Color(165,165,165),1);
            // getRootPane().setBorder(border);
             
             
             add(this.getDialogPanel());
             setVisible(true);
             
             
             

         } catch (IOException ex) {
             Logger.getLogger(SSNMessageDialogBox.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    private String getFormattedMessage(String message) {

        message = message.replace("<br>", "");

        return "<html><div style=\"align:center;\"><p>" + message + "</div></html>";
    }
     private void initDialogGUIComponents() {
         this.setDialogMessageTitle(new JLabel());
         this.getDialogMessageTitle().setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
         this.getDialogMessageTitle().setFont(new Font("open sans",Font.BOLD,16)); 
         
         this.setDialogCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png")))); 
         this.getDialogCancelLabel().setName("no");
         if(getDialogType().equals(SSNDialogChoice.ERROR_DIALOG.getType()))
         {
              this.setErrorIconlLabel(new JLabel(new ImageIcon(getClass().getResource("/icon/error-icon.png")))); 
         }
         else
         {
             //this.setNotificationIconlLabel(new JLabel(new ImageIcon(getClass().getResource("/icon/notification-icon.png")))); 
             this.setErrorIconlLabel(new JLabel(new ImageIcon(getClass().getResource("/icon/notification-icon.png")))); 
         }
         
        this.getDialogCancelLabel().addMouseListener(new SSNMessageDialogController(this));
         
        this.setDialogMessageHeader(new JLabel());
        this.getDialogMessageHeader().setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
        this.getDialogMessageHeader().setFont(new Font("open sans",Font.BOLD,16));
        
        this.setDailogMessageTextArea(new JLabel(getDialogMessage(),SwingUtilities.CENTER));
       // this.getDailogMessageTextArea().setPreferredSize(new Dimension(100, 100));
//        this.getDailogMessageTextArea().setLineWrap(true);
//        this.getDailogMessageTextArea().setEditable(false);
//        this.getDailogMessageTextArea().setWrapStyleWord(true);
        this.getDailogMessageTextArea().setForeground(new Color(255,255,255));
        
        this.getDailogMessageTextArea().setFont(new Font("open sans",Font.BOLD,14));
        this.getDailogMessageTextArea().setOpaque(false);
        
        
        Border paddingBorder = BorderFactory.createEmptyBorder(9,5,9,10);	
	Border border = BorderFactory.createLineBorder(new Color(202,199,192));
        
        //this.getDailogMessageTextArea().setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
        this.getDailogMessageTextArea().setBorder(paddingBorder);
        
        UIDefaults overrides =getUidefaults();
                  // this.getDailogMessageTextArea().setSize(450, 350);
        this.getDailogMessageTextArea().putClientProperty("Nimbus.Overrides", overrides);
        
        this.setDialogBoxYesBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/ok_prefe_btn.png"))));                        
        this.getDialogBoxYesBtn().setBackground(new Color(255,209,25)); 
        this.getDialogBoxYesBtn().setForeground(new Color(0,0,0));
        this.getDialogBoxYesBtn().setFont(new Font("open sans",Font.PLAIN,14));
        this.getDialogBoxYesBtn().setName("yes");
        this.getDialogBoxYesBtn().addMouseListener(new SSNMessageDialogController(this));
        
        this.setDialogBoxNoBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/cancel_prefe_btn.png"))));        
        this.getDialogBoxNoBtn().setBackground(Color.BLACK); 
        this.getDialogBoxNoBtn().setForeground(new Color(0,0,0));
        this.getDialogBoxNoBtn().setFont(new Font("open sans",Font.PLAIN,14));
        this.getDialogBoxNoBtn().setName("no");
        this.getDialogBoxNoBtn().addMouseListener(new SSNMessageDialogController(this));
        
     }
     
     
      private void addDialogUIElements() {
          
          this.getDialogPanel().add(this.getDialogMessageTitle());
          this.getDialogPanel().add(this.getDialogCancelLabel());
        
          this.getDialogPanel().add(this.getDailogMessageTextArea());
          this.getDialogPanel().add(this.getDialogMessageHeader());
          this.getDialogPanel().add(this.getDialogBoxYesBtn());
          this.getDialogPanel().add(this.getDialogBoxNoBtn());
      }
      
      private void positionDialogUIElements() {
          
          this.getDialogMessageTitle().setBounds(10,5,250,30);
          this.getDialogCancelLabel().setBounds(370,5,150,30);
          this.getErrorIconlLabel().setBounds(20,70,32,32);
          //this.getDialogMessageHeader().setBounds(60,70,250,10);
          this.getDailogMessageTextArea().setBounds(60,80,400,100);
          this.getDialogBoxYesBtn().setBounds(140,240,120,30);
          this.getDialogBoxNoBtn().setBounds(290,240,120,30);
      }
    
       private void loadCustomDialogBox() {
           //getDialogMessageTitle().setText("This is a custom dialog Box!");
          // getDialogMessageHeader().setText("This is message header.");
          // getDailogMessageTextArea().setText("I'm giving away a tip a day, via video, all of this week. Today's one is on my LinkedIn blog, along with a thank you message for all my followers. The tip itself is about selling your culture at job interviews. I'll have another one, right here on LinkedIn, tomorrow morning.");
           getDialogMessageTitle().setText(getDialogTitle());
           getDialogMessageHeader().setText(getDialogHeader());
           
           getDailogMessageTextArea().setText(getDialogMessage());
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

    public JLabel getErrorIconlLabel() {
        return errorIconlLabel;
    }

    public void setErrorIconlLabel(JLabel errorIconlLabel) {
        this.errorIconlLabel = errorIconlLabel;
    }

    public JLabel getNotificationIconlLabel() {
        return notificationIconlLabel;
    }

    public void setNotificationIconlLabel(JLabel notificationIconlLabel) {
        this.notificationIconlLabel = notificationIconlLabel;
    }

    public JLabel getDailogMessageTextArea() {
        return dailogMessageTextArea;
    }

    public void setDailogMessageTextArea(JLabel dailogMessageTextArea) {
        this.dailogMessageTextArea = dailogMessageTextArea;
    }

    public JLabel getDialogMessageTitle() {
        return dialogMessageTitle;
    }

    public void setDialogMessageTitle(JLabel dialogMessageTitle) {
        this.dialogMessageTitle = dialogMessageTitle;
    }

    public JLabel getDialogMessageHeader() {
        return dialogMessageHeader;
    }

    public void setDialogMessageHeader(JLabel dialogMessageHeader) {
        this.dialogMessageHeader = dialogMessageHeader;
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

    public String getDialogHeader() {
        return dialogHeader;
    }

    public void setDialogHeader(String dialogHeader) {
        this.dialogHeader = dialogHeader;
    }

    public String getDialogMessage() {
        return dialogMessage;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
    
    
    
    
    
    
       public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    String title="This is a custom dialog Box!";
                    String header="This is message header.";
                    String mesage="I'm giving away a tip a day, via video, all of this week. Today's one is on my LinkedIn blog, along with a thank you message for all my followers. The tip itself is about selling your culture at job interviews. I'll have another one, right here on LinkedIn, tomorrow morning.";
                   // new SSNMessageDialogBox(SSNDialogChoice.ERROR_DIALOG.getType(),title,header,mesage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    
    
}