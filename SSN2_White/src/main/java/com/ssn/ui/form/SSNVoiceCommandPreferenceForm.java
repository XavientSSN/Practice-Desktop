package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNVoiceCommandPreferencesController;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

/**
 *
 * @author ATripathi4
 */
public class SSNVoiceCommandPreferenceForm extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel ssnVoiceCommandPreferencePanel = null;
    private JLabel ssnVoiceCommandPreferencesTitleLabel = null;
    private JLabel closeLabel = null;
    private JSeparator editSeparator = null;
    private JLabel updateBtn = null;
    private JLabel closeBtn = null;
    private JLabel defaultBtn = null;
    private SSNHomeForm homeForm = null;
    private SSNVoiceCommandPreferencesController voiceCommandPreferencesController = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNVoiceCommandPreferenceForm.class);
    public SSNVoiceCommandPreferenceForm() {
        super("Voice Command Preferences");

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        Map<String, List<String>> commands = SSNDao.getCustomVoiceCommand();
        setVoiceCommandPreferencesController(new SSNVoiceCommandPreferencesController(this, commands));
        this.renderPreferencePanel();
        this.initGUIComponents(commands);

        this.addGUIComponents();
        this.positionGUIElements();
        this.renderPreferenceFrame();
        
    }

    private void initGUIComponents(Map<String, List<String>> commands) {

        Set<String> commandActions = commands.keySet();
        //UIDefaults overrides = getGUIdefaults();

        this.setSsnVoiceCommandPreferencesTitleLabel(new JLabel("<html><font color='rgb(255,215,0)'>Voice Command Setting</font></html>"));
        this.getSsnVoiceCommandPreferencesTitleLabel().setFont(new Font("open sans", Font.BOLD, 16));
        //this.setEditSeparator(new JSeparator(JSeparator.HORIZONTAL));

        this.setCloseLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png"))));
        this.getCloseLabel().setName("cancelVoicePreferences");
        this.getCloseLabel().addMouseListener(this.getVoiceCommandPreferencesController());

        //this.getSsnVoiceCommandPreferencesTitleLabel().setForeground(new Color(0, 0, 0));
        int yLable = 120;
        int yTxt = 110;
        
        for (String commandAction : commandActions) {
            JLabel label = new JLabel(commandAction+":");
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            label.setFont(new Font("open sanse", Font.BOLD, 12));
            
            
            final JTextField textField = new JTextField();
            textField.setForeground(Color.WHITE);
            textField.setBackground(new Color(72,72,72));
            textField.setVisible(true);
            textField.setBorder(new SSNCustomBorder(true,textField.getHeight(),textField.getWidth()));
            
             
            List<String> commandList = commands.get(commandAction);
            if(commandList!=null ){
                // remove first and last comma(,) from command
                StringBuffer commandTxt = new StringBuffer();
                for(String command : commandList){
                    commandTxt.append(",");
                    commandTxt.append(command);
                }
                if(commandTxt.toString().trim().startsWith(",")){
                    commandTxt = new StringBuffer(commandTxt.substring(1));
                }
                textField.setText(commandTxt.toString().trim());
                
            }
            

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String input = ((JTextField) e.getSource()).getText();
                    textField.setText(input.trim());
                }
            });
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    String input = ((JTextField) e.getSource()).getText();
                    // using pattern with flags
                    // Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
                    Pattern regex = Pattern.compile("[%$&+:;=?@#|()`~!^]");
                    Matcher matcher = regex.matcher(input);
                    if (matcher.find()) {
                        //String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");
                        String stemp = input.replaceAll("[%$&+:;=?@#|()`~!^]", "");

                        textField.setText(stemp);
                        //JOptionPane.showMessageDialog(null, "Special characters are not allowed!");
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed");

                    }

                    Pattern regex1 = Pattern.compile("\\s{2,}");
                    Matcher matcher1 = regex1.matcher(input);
                    if (input != null && input.length() > 0) {
                        if (Character.isWhitespace(input.charAt(0))) {

                            textField.setText(input.trim());
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                        } else if (matcher1.find()) {
                            String stemp = matcher1.replaceAll(" ");
                            textField.setText(stemp);
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                        }
                    }else if (input != null && input.length() <= 0) {
                        textField.setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Command could not be blank!!!");
                    }
                }

            });

            label.setForeground(new Color(255,255,255));
           
            
            
            label.setBounds(90, yLable, 100, 20);
            textField.setBounds(205, yTxt, 330, 40);
            textField.setName(commandAction);

            yLable = yLable + 45;
            yTxt = yTxt + 45;

            
            this.getSsnVoiceCommandPreferencePanel().add(label);
            this.getSsnVoiceCommandPreferencePanel().add(textField);
        }
   
        this.setUpdateBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/save_voice_cmd.png"))));
        this.getUpdateBtn().setName("updateCommandPreferences");
        this.getUpdateBtn().setBackground(new Color(72,72,72));
        this.getUpdateBtn().setForeground(new Color(255,255,255));
        this.getUpdateBtn().setFont(new Font("open sans", Font.BOLD, 12));
        this.getUpdateBtn().addMouseListener(this.getVoiceCommandPreferencesController());
       // this.getUpdateBtn().setFocusPainted(false);
       // this.getUpdateBtn().setActionCommand("updateCommandPreferences");
       // this.getUpdateBtn().addActionListener(this.getVoiceCommandPreferencesController());

        this.setCloseBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/cancel.png"))));
        this.getCloseBtn().setName("cancelVoicePreferences");
        this.getCloseBtn().setFont(new Font("open sans", Font.BOLD, 12));
        this.getCloseBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getCloseBtn().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getCloseBtn().addMouseListener(this.getVoiceCommandPreferencesController());
       // this.getCloseBtn().setFocusPainted(false);
       // this.getCloseBtn().setActionCommand("cancelVoicePreferences");
        //this.getCloseBtn().addActionListener(this.getVoiceCommandPreferencesController());
        this.getCloseBtn().addKeyListener(this.getVoiceCommandPreferencesController());

        this.setDefaultBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/default_voice_cmd.png"))));
        this.getDefaultBtn().setName("setDefaultPreferences");
        this.getDefaultBtn().setFont(new Font("open sans", Font.BOLD, 12));
        this.getDefaultBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getDefaultBtn().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        this.getDefaultBtn().addMouseListener(this.getVoiceCommandPreferencesController());
       // this.getDefaultBtn().setFocusPainted(false);
       // this.getDefaultBtn().setActionCommand("setDefaultPreferences");
       // this.getDefaultBtn().addActionListener(this.getVoiceCommandPreferencesController());
        this.getDefaultBtn().addKeyListener(this.getVoiceCommandPreferencesController());
        
        //change by ritesh start
         //this.textField.setBorder(new SSNCustomBorder(false,new Color(242,242,10),this.textField().getHeight(),this.textField().getWidth()));
//        this.getUpdateBtn().setBorder(new SSNCustomBorder(false,new Color(242,242,10),this.getUpdateBtn().getHeight(),this.getUpdateBtn().getWidth()));
//        this.getCloseBtn().setBorder(new SSNCustomBorder(false,new Color(242,242,10),this.getCloseBtn().getHeight(),this.getCloseBtn().getWidth()));
//        this.getDefaultBtn().setBorder(new SSNCustomBorder(false,new Color(242,242,10),this.getDefaultBtn().getHeight(),this.getDefaultBtn().getWidth()));
        
        //change by ritesh end
    }

    private UIDefaults getGUIdefaults() {

        UIDefaults overrides = new UIDefaults();
      // overrides.put("TextField.background", new ColorUIResource(Color.BLUE));
     /*   overrides.put("TextField[Enabled].backgroundPainter", new Painter<JTextField>() {

            @Override
            public void paint(Graphics2D g, JTextField field, int width, int height) {
               // g.setColor(new Color(202, 199, 192));
              // g.fill(new Rectangle(0, 0, width, height));
            }

        });*/
        return overrides;
    }

    private void renderPreferencePanel() {
        try {
            URL imgURL = getClass().getResource("/images/bg.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnVoiceCommandPreferencePanel(new SSNImagePanel(background));
            this.getSsnVoiceCommandPreferencePanel().setLayout(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addGUIComponents() {
        this.getSsnVoiceCommandPreferencePanel().add(this.getSsnVoiceCommandPreferencesTitleLabel());

        //this.getSsnVoiceCommandPreferencePanel().add(this.getEditSeparator());
        this.getSsnVoiceCommandPreferencePanel().add(this.getCloseLabel());
        this.getSsnVoiceCommandPreferencePanel().add(this.getUpdateBtn());
        this.getSsnVoiceCommandPreferencePanel().add(this.getCloseBtn());
        this.getSsnVoiceCommandPreferencePanel().add(this.getDefaultBtn());
    }

    private void positionGUIElements() {

        this.getSsnVoiceCommandPreferencesTitleLabel().setBounds(10, 5, 350, 30);
        //this.getEditSeparator().setBounds(0, 35, 510, 30);
        this.getCloseLabel().setBounds(450, 5, 200, 30);

        this.getUpdateBtn().setBounds(60, 275, 153, 42);
        this.getCloseBtn().setBounds(223, 275, 153, 42);
        this.getDefaultBtn().setBounds(383, 275, 153, 42);

    }

    private void renderPreferenceFrame() {
        try {
            this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());

            //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setUndecorated(true);
            LineBorder border = new LineBorder(new Color(165, 165, 165), 1);
            this.getRootPane().setBorder(border);
            //this.setAlwaysOnTop(true);

            URL imgURL = getClass().getResource("/images/bg.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Dimension dim =  SSNConstants.SSN_SCREEN_SIZE;
            this.setBounds(0, 0, image.getWidth(), image.getHeight());
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            this.add(this.getSsnVoiceCommandPreferencePanel());
            this.setVisible(true);
        } catch (IOException ex) {
        }
    }

    /**
     * @return the ssnVoiceCommandPreferencePanel
     */
    public JPanel getSsnVoiceCommandPreferencePanel() {
        return ssnVoiceCommandPreferencePanel;
    }

    /**
     * @param ssnVoiceCommandPreferencePanel the ssnVoiceCommandPreferencePanel
     * to set
     */
    public void setSsnVoiceCommandPreferencePanel(JPanel ssnVoiceCommandPreferencePanel) {
        this.ssnVoiceCommandPreferencePanel = ssnVoiceCommandPreferencePanel;
    }

    /**
     * @return the ssnEditUserProfileTitleLabel
     */
    public JLabel getSsnVoiceCommandPreferencesTitleLabel() {
        return ssnVoiceCommandPreferencesTitleLabel;
    }

    /**
     * @param ssnVoiceCommandPreferencesTitleLabel the
     * ssnVoiceCommandPreferencesTitleLabel to set
     */
    public void setSsnVoiceCommandPreferencesTitleLabel(JLabel ssnVoiceCommandPreferencesTitleLabel) {
        this.ssnVoiceCommandPreferencesTitleLabel = ssnVoiceCommandPreferencesTitleLabel;
    }

    /**
     * @return the updateBtn
     */
    public JLabel getUpdateBtn() {
        return updateBtn;
    }

    /**
     * @param updateBtn the updateBtn to set
     */
    public void setUpdateBtn(JLabel updateBtn) {
        this.updateBtn = updateBtn;
    }

    /**
     * @return the homeForm
     */
    public SSNHomeForm getHomeForm() {
        return homeForm;
    }

    /**
     * @param homeForm the homeForm to set
     */
    public void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    /**
     * @return the editSeparator
     */
    public JSeparator getEditSeparator() {
        return editSeparator;
    }

    /**
     * @param editSeparator the editSeparator to set
     */
    public void setEditSeparator(JSeparator editSeparator) {
        this.editSeparator = editSeparator;
    }

    /**
     * @return the closeBtn
     */
    public JLabel getCloseBtn() {
        return closeBtn;
    }

    /**
     * @param closeBtn the closeBtn to set
     */
    public void setCloseBtn(JLabel closeBtn) {
        this.closeBtn = closeBtn;
    }

    /**
     * @return the closeLabel
     */
    public JLabel getCloseLabel() {
        return closeLabel;
    }

    /**
     * @param closeLabel the closeLabel to set
     */
    public void setCloseLabel(JLabel closeLabel) {
        this.closeLabel = closeLabel;
    }

    /**
     * @return the voiceCommandPreferencesController
     */
    public SSNVoiceCommandPreferencesController getVoiceCommandPreferencesController() {
        return voiceCommandPreferencesController;
    }

    /**
     * @param voiceCommandPreferencesController the
     * voiceCommandPreferencesController to set
     */
    public void setVoiceCommandPreferencesController(SSNVoiceCommandPreferencesController voiceCommandPreferencesController) {
        this.voiceCommandPreferencesController = voiceCommandPreferencesController;
    }

    /**
     * @return defaultBtn
     */
    public JLabel getDefaultBtn() {
        return defaultBtn;
    }

    /**
     * @param defaultBtn the defaultBtn to set
     */
    public void setDefaultBtn(JLabel defaultBtn) {
        this.defaultBtn = defaultBtn;
    }
    
    

}
