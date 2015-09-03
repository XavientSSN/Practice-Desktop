package com.ssn.ui.form;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.event.controller.SSNUserProfileController;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNEditUserProfileModel;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNImagePanel;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ws.rest.response.SSNLoginResponse;
import com.ssn.ws.rest.response.SSNUserResponse;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author vkvarma
 */
public class SSNEditUserProfileForm extends JFrame {
    
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNEditUserProfileForm.class);


    private static final long serialVersionUID = 1L;
    private JPanel ssnEditUserProfilePanel = null;
    private JLabel ssnEditUserProfileTitleLabel = null;
    private JLabel ssnEditUserProfileCancelLabel = null;
    private JSeparator editSeparator = null;
    private JLabel ssnEditUserProfileFirstNameLabel = null;
    private JLabel ssnEditUserProfileLastNameLabel = null;
    private JLabel ssnEditUserProfileEmailLabel = null;
    private JLabel ssnEditUserProfileMobileLabel = null;
    private JLabel ssnEditUserProfileBirthdayLabel = null;
    private JLabel ssnEditUserProfileGenderLabel = null;
    private JLabel ssnEditUserProfileZipCodeLabel = null;
    private JTextField ssnEditUserProfileFNameTxt = null;
    private JTextField ssnEditUserProfileLNameTxt = null;
    private JTextField ssnEditUserProfileTxt = null;
    private JTextField ssnEditUserProfileEmailTxt = null;
    private JTextField ssnEditUserProfileMobileTxt = null;
    private JDateChooser ssnEditUserProfileDateChooser = null;
    private JTextField ssnEditUserProfileBirthDayText = null;
    private JComboBox ssnEditUserProfileGenderBox = null;
    private JTextField ssnEditUserProfileZipCodeText = null;
    private JLabel ssnEditProfileUpdateBtn = null;
    private JLabel ssnEditProfileCancelBtn = null;
    private SSNHomeForm homeForm = null;
    private SSNUserProfileController userProfileController = null;
    private SSNLoginResponse loginResponse = null;
    private SSNEditUserProfileModel editUserProfileModel = null;

    public SSNEditUserProfileForm() {
               

        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }               

    }

    public SSNEditUserProfileForm(SSNHomeForm homeForm, SSNLoginResponse loginResponse) {
        super("Edit SSN User Profile");

        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        setHomeForm(homeForm);
        setLoginResponse(loginResponse);
        setUserProfileController(new SSNUserProfileController(this));
        setEditUserProfileModel(new SSNEditUserProfileModel(this, this.getHomeForm(), this.getUserProfileController()));
        getUserProfileController().setEditUserProfileModel(this.getEditUserProfileModel());
        this.initEditUserProfileFormGUIComponents();
        this.renderEditUserProfilePanel();
        this.addEditUserProfileFormUIElements();
        this.positionEditUserProfileFormUIElements();
        this.renderEditUserProfileFrame();
        this.loadUserProfileForm();
    }

    private void initEditUserProfileFormGUIComponents() {
        this.setSsnEditUserProfileTitleLabel(new JLabel("<html><font color='rgb(255,215,0)'>Edit Profile</font></html>"));
        this.getSsnEditUserProfileTitleLabel().setFont(new Font("Verdana", Font.BOLD, 20));
        this.setEditSeparator(new JSeparator(JSeparator.HORIZONTAL));
        this.setSsnEditUserProfileCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png"))));
        this.getSsnEditUserProfileCancelLabel().setName("closeProfile");
        this.getSsnEditUserProfileCancelLabel().addMouseListener(this.getUserProfileController());
        this.setSsnEditUserProfileFirstNameLabel(new JLabel("First Name"));
        this.getSsnEditUserProfileFirstNameLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileLastNameLabel(new JLabel("Last Name"));
        this.getSsnEditUserProfileLastNameLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileEmailLabel(new JLabel("Email"));
        this.getSsnEditUserProfileEmailLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileMobileLabel(new JLabel("Mobile"));
        this.getSsnEditUserProfileMobileLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileBirthdayLabel(new JLabel("Birth Date"));
        this.getSsnEditUserProfileBirthdayLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileGenderLabel(new JLabel("Gender"));
        this.getSsnEditUserProfileGenderLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileZipCodeLabel(new JLabel("Zip Code"));
        this.getSsnEditUserProfileZipCodeLabel().setFont(new Font("open sans", Font.BOLD, 12));
        this.setSsnEditUserProfileFNameTxt(new SSNIconTextField("", "First name", "profileFname"));
        this.setSsnEditUserProfileLNameTxt(new SSNIconTextField("", "Last name", "profileLname"));
        this.setSsnEditUserProfileEmailTxt(new SSNIconTextField("", "Email", "profileEmail"));
        this.getSsnEditUserProfileEmailTxt().setHorizontalAlignment(SwingConstants.LEFT);
        this.setSsnEditUserProfileMobileTxt(new SSNIconTextField("", "Mobile number", "profileMobile"));
        this.getSsnEditUserProfileMobileTxt().setHorizontalAlignment(SwingConstants.CENTER);
        this.setSsnEditUserProfileBirthDayText(new SSNIconTextField("", "Birth Date", "profileBirthDay"));
        this.getSsnEditUserProfileBirthDayText().setEditable(false);
        Vector<String> values = new Vector<String>();
        values.add("Male");
        values.add("Female");
        values.add("Transgender");
        values.add("Others");
     
 

        Border yellowBorder = BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,2);
        JComboBox combobox=new JComboBox(values);
        combobox.setEditable(true);
        combobox.setBorder(yellowBorder);
      
        
        combobox.setForeground(Color.white);
        combobox.setOpaque(true);
        this.setSsnEditUserProfileGenderBox(combobox);
        this.setSsnEditUserProfileZipCodeText(new SSNIconTextField("", "Zip Code", "profileZipCode"));
        JDateChooser jDateChoser=new JDateChooser();
        //Below start by sandeep
        jDateChoser.setBackground(new Color(71, 71, 71, 1));
        jDateChoser.getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        jDateChoser.getDateEditor().getUiComponent().setBackground(new Color(71, 71, 71, 1));
        
        
        int height = jDateChoser.getDateEditor().getUiComponent().getHeight();
        int width = jDateChoser.getDateEditor().getUiComponent().getWidth();
        jDateChoser.getDateEditor().getUiComponent().setSize(100, height);
        jDateChoser.getDateEditor().getUiComponent().setBorder(new SSNCustomBorder(true, height, width));
        JTextField txtField = (JTextField) jDateChoser.getDateEditor().getUiComponent();
         txtField.setHorizontalAlignment(SwingConstants.CENTER);
        txtField.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        
        jDateChoser.getCalendarButton().setBackground(new Color(71, 71, 71, 0));

        
        
        //above done
        jDateChoser.setIcon(new ImageIcon(getClass().getResource("/icon/calender_icon.png")));
        this.setSsnEditUserProfileDateChooser(jDateChoser);
        this.getSsnEditUserProfileDateChooser().setOpaque(false);
        this.setSsnEditUserProfileGenderBox(new JComboBox(values));
        this.getSsnEditUserProfileGenderBox().setFocusable(false);
        try {
            Date today;
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            today = sdf.parse(sdf.format(new Date()));
            getSsnEditUserProfileDateChooser().setMaxSelectableDate(today);
        } catch (ParseException ex) {
            log.error(ex.getMessage());
        }
        this.setSsnEditUserProfileZipCodeText(new SSNIconTextField("", "Zip Code", "profileZipCode"));

        
        this.getSsnEditUserProfileDateChooser().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                if (evt.getPropertyName().equals("date")) {

                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    Date userInputDate;
                    Date today;
                    try {
                        userInputDate = sdf.parse(sdf.format(getSsnEditUserProfileDateChooser().getDate()));
                        today = sdf.parse(sdf.format(new Date()));
                        getSsnEditUserProfileDateChooser().setMaxSelectableDate(today);
//                        if (userInputDate.after(today)) {
//                            System.out.println("if::"+userInputDate);
//                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Please select valid date of birth!");
//                        } else {
//                            System.out.println("else:::"+getSsnEditUserProfileDateChooser().getDate());
                         getSsnEditUserProfileBirthDayText().setText(new SimpleDateFormat("MM-dd-yyyy").format(getSsnEditUserProfileDateChooser().getDate()));
//                        }
                         
                    } catch (ParseException ex) {
                       log.error(ex.getMessage());
                    }

                }
            }
        });

        this.getSsnEditUserProfileTitleLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileFirstNameLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileLastNameLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileEmailLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileMobileLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileBirthdayLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileGenderLabel().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileZipCodeLabel().setForeground(new Color(255,255,255));

        
        this.getSsnEditUserProfileFNameTxt().setBackground(new Color(77,77,77));
        this.getSsnEditUserProfileLNameTxt().setBackground(new Color(77,77,77));
        this.getSsnEditUserProfileEmailTxt().setBackground(new Color(77,77,77));
        this.getSsnEditUserProfileMobileTxt().setBackground(new Color(77,77,77));
        this.getSsnEditUserProfileBirthDayText().setBackground(new Color(77,77,77));
        this.getSsnEditUserProfileZipCodeText().setBackground(new Color(77,77,77));

        this.getSsnEditUserProfileFNameTxt().setOpaque(true);
        this.getSsnEditUserProfileLNameTxt().setOpaque(true);
        this.getSsnEditUserProfileEmailTxt().setOpaque(true);
        this.getSsnEditUserProfileMobileTxt().setOpaque(true);
        this.getSsnEditUserProfileBirthDayText().setOpaque(true);
        this.getSsnEditUserProfileZipCodeText().setOpaque(true);
        this.getSsnEditUserProfileBirthDayText().setOpaque(true);

        this.getSsnEditUserProfileFNameTxt().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileLNameTxt().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileEmailTxt().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileMobileTxt().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileBirthDayText().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileZipCodeText().setFont(new Font("open sans", Font.PLAIN, 12));
        this.getSsnEditUserProfileGenderBox().setFont(new Font("open sans", Font.PLAIN, 12));

        Border paddingBorder = BorderFactory.createEmptyBorder(9, 5, 9, 10);
        Border border = BorderFactory.createLineBorder(new Color(202, 199, 192));

        this.getSsnEditUserProfileFNameTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnEditUserProfileLNameTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnEditUserProfileEmailTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnEditUserProfileMobileTxt().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnEditUserProfileBirthDayText().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        this.getSsnEditUserProfileZipCodeText().setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

     UIDefaults overrides = getUidefaults();

        this.getSsnEditUserProfileFNameTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnEditUserProfileLNameTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnEditUserProfileEmailTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnEditUserProfileMobileTxt().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnEditUserProfileBirthDayText().putClientProperty("Nimbus.Overrides", overrides);
        this.getSsnEditUserProfileZipCodeText().putClientProperty("Nimbus.Overrides", overrides);//by ritesh end

        
         this.getSsnEditUserProfileEmailTxt().setName("email");
        this.getSsnEditUserProfileFNameTxt().addKeyListener(this.getUserProfileController());
        this.getSsnEditUserProfileFNameTxt().setActionCommand("userProfileFName");
        this.getSsnEditUserProfileLNameTxt().addKeyListener(this.getUserProfileController());
        this.getSsnEditUserProfileLNameTxt().setActionCommand("userProfileLName");

        
        this.setSsnEditProfileUpdateBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/save_voice_cmd.png"))));
        
        this.getSsnEditProfileUpdateBtn().setFont(new Font("open sans", Font.BOLD, 10));
        this.getSsnEditProfileUpdateBtn().setName("updateUserProfile");
        this.getSsnEditProfileUpdateBtn().addMouseListener(this.getUserProfileController()); 
       
        this.getSsnEditProfileUpdateBtn().addKeyListener(this.getUserProfileController()); 

        
        this.setSsnEditProfileCancelBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/cancel.png"))));
        this.getSsnEditProfileCancelBtn().setName("cancelUserProfile");
        this.getSsnEditProfileCancelBtn().setFont(new Font("open sans", Font.BOLD, 10));
       this.getSsnEditProfileCancelBtn().addMouseListener(this.getUserProfileController()); 
       
        this.getSsnEditProfileCancelBtn().addKeyListener(this.getUserProfileController()); 
        
        this.getSsnEditUserProfileFNameTxt().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileFNameTxt().getHeight(),this.getSsnEditUserProfileFNameTxt().getWidth()));
        this.getSsnEditUserProfileLNameTxt().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileLNameTxt().getHeight(),this.getSsnEditUserProfileLNameTxt().getWidth()));
        this.getSsnEditUserProfileEmailTxt().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileEmailTxt().getHeight(),this.getSsnEditUserProfileEmailTxt().getWidth()));
        this.getSsnEditUserProfileMobileTxt().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileMobileTxt().getHeight(),this.getSsnEditUserProfileMobileTxt().getWidth()));
        this.getSsnEditUserProfileBirthDayText().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileBirthDayText().getHeight(),this.getSsnEditUserProfileBirthDayText().getWidth()));
        this.getSsnEditUserProfileZipCodeText().setBorder(new SSNCustomBorder(true,new Color(242,242,10),this.getSsnEditUserProfileZipCodeText().getHeight(),this.getSsnEditUserProfileZipCodeText().getWidth()));
        
        this.getSsnEditUserProfileGenderBox().setBackground(new Color(71, 71, 71));

       
        this.getSsnEditUserProfileFNameTxt().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileLNameTxt().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileEmailTxt().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileMobileTxt().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileBirthDayText().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileZipCodeText().setForeground(new Color(255,255,255));
        this.getSsnEditUserProfileGenderBox().setForeground(new Color(255,255,255));
        //end
    }

    private UIDefaults getUidefaults() {

        UIDefaults overrides = new UIDefaults();
        overrides.put("TextField.background", new ColorUIResource(new Color(202, 199, 192)));
        overrides.put("TextField[Enabled].backgroundPainter", new Painter<JTextField>() {

            @Override
            public void paint(Graphics2D g, JTextField field, int width, int height) {
                g.setColor(new Color(72,72,72));
                g.fill(new Rectangle(0, 0, width, height));
            }

        });
        return overrides;
    }

    private void renderEditUserProfilePanel() {
        try {
            URL imgURL = getClass().getResource("/images/chbg2.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Image background = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
            this.setSsnEditUserProfilePanel(new SSNImagePanel(background));
            this.getSsnEditUserProfilePanel().setLayout(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addEditUserProfileFormUIElements() {
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileTitleLabel());

        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileCancelLabel());
       
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileFNameTxt());
       
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileLNameTxt());
        
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileEmailTxt());
       
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileMobileTxt());
        
        this.getSsnEditUserProfilePanel().add(this.getSsnEditProfileUpdateBtn());
        this.getSsnEditUserProfilePanel().add(this.getSsnEditProfileCancelBtn());
        this.getSsnEditUserProfileDateChooser().add(this.getSsnEditUserProfileBirthDayText());
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileDateChooser());
        Border yellowBorder = BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,2);
       // Border boxYellowBorder = BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,1);
        this.getSsnEditUserProfileGenderBox().setEditable(true);
        this.getSsnEditUserProfileGenderBox().setUI(com.ssn.schedule.ColorArrowUI.createUI(this.getSsnEditUserProfileGenderBox()));
        this.getSsnEditUserProfileGenderBox().setBorder(yellowBorder);
        
        
         JTextField field=(JTextField)this.getSsnEditUserProfileGenderBox().getEditor().getEditorComponent();
         field.setAlignmentX(SwingConstants.LEFT);
         field.setAlignmentY(SwingConstants.LEFT);
       
         field.setBackground(new Color(71, 71, 71, 0));
         field.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
         field.setBorder(null);


        
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileGenderBox());
        ((JLabel)this.getSsnEditUserProfileGenderBox().getRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        this.getSsnEditUserProfilePanel().add(this.getSsnEditUserProfileZipCodeText());

    }

    private void positionEditUserProfileFormUIElements() {

        
        this.getSsnEditUserProfileTitleLabel().setBounds(20,8, 300, 30);
        
        this.getSsnEditUserProfileCancelLabel().setBounds(250,8,300, 30);
        this.getSsnEditUserProfileFirstNameLabel().setBounds(80, 80, 80, 20);
        this.getSsnEditUserProfileFNameTxt().setBounds(40, 150, 150, 40);

        this.getSsnEditUserProfileLastNameLabel().setBounds(80, 123, 100, 20);
        this.getSsnEditUserProfileLNameTxt().setBounds(250, 150,160, 40);

        this.getSsnEditUserProfileEmailLabel().setBounds(80, 166, 100, 20);
        this.getSsnEditUserProfileEmailTxt().setBounds(40, 210, 150, 40);

        this.getSsnEditUserProfileMobileLabel().setBounds(80, 209, 100, 20);
        this.getSsnEditUserProfileMobileTxt().setBounds(250, 210, 160, 40);

        this.getSsnEditUserProfileBirthdayLabel().setBounds(80, 252, 100, 20);
        this.getSsnEditUserProfileDateChooser().setBounds(40, 270, 210, 40);

        this.getSsnEditUserProfileZipCodeLabel().setBounds(80, 295, 100, 20);
        this.getSsnEditUserProfileZipCodeText().setBounds(250, 270, 160, 40);
        
         this.getSsnEditUserProfileGenderLabel().setBounds(80, 338, 100, 20);
         
        this.getSsnEditUserProfileGenderBox().setBounds(40, 330, 150, 40);

        
        this.getSsnEditProfileUpdateBtn().setBounds(270, 450, 153, 43);
        this.getSsnEditProfileCancelBtn().setBounds(60, 450, 153, 43);
    }

    private void renderEditUserProfileFrame() {
        try {
            this.setIconImage((new ImageIcon(getClass().getResource("/images/ssn-hive-title-logo.png"))).getImage());

            
            this.setResizable(false);
            this.setUndecorated(true);
            LineBorder border = new LineBorder(new Color(165, 165, 165), 1);
            this.getRootPane().setBorder(border);
            

            URL imgURL = getClass().getResource("/images/chbg2.jpg");
            BufferedImage image = ImageIO.read(imgURL);
            Dimension dim = SSNConstants.SSN_SCREEN_SIZE;
            this.setBounds(0, 0, image.getWidth(), image.getHeight());
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
            this.add(this.getSsnEditUserProfilePanel());
            this.setVisible(true);
        } catch (IOException ex) {
        }
    }

    private void loadUserProfileForm() {
        SSNUserResponse userProfileData = null;

        if (getLoginResponse() != null && getLoginResponse().getData() != null
                && getLoginResponse().getData().getUser() != null) {
            userProfileData = loginResponse.getData().getUser();
            getSsnEditUserProfileFNameTxt().setText(userProfileData.getFirst_name());
            getSsnEditUserProfileLNameTxt().setText(userProfileData.getLast_name());
            getSsnEditUserProfileEmailTxt().setText(userProfileData.getEmail());
            getSsnEditUserProfileEmailTxt().setCaretPosition(0);
            getSsnEditUserProfileMobileTxt().setText(userProfileData.getMobile());
            if (userProfileData.getBirth_date() != null && !userProfileData.getBirth_date().isEmpty()) {
                //System.out.println("DOB " +userProfileData.getBirth_date()) ;
                getSsnEditUserProfileBirthDayText().setText(SSNHelper.formatDateInMMDDYYYY(userProfileData.getBirth_date()));
            }
            if (userProfileData.getZipcode() != null && !userProfileData.getZipcode().isEmpty()) {
                getSsnEditUserProfileZipCodeText().setText(userProfileData.getZipcode());
            }
            if (userProfileData.getGender() != null && !userProfileData.getGender().isEmpty()) {
                
                JTextField field=(JTextField)getSsnEditUserProfileGenderBox().getEditor().getEditorComponent();
                field.setHorizontalAlignment(SwingConstants.CENTER);
                getSsnEditUserProfileGenderBox().setSelectedItem(userProfileData.getGender());
            }

        } else if (getHomeForm() != null && getHomeForm().getSocialModel() != null) {
            getSsnEditUserProfileFNameTxt().setText(getHomeForm().getSocialModel().getFirstName());
            getSsnEditUserProfileLNameTxt().setText(getHomeForm().getSocialModel().getLastName());
            getSsnEditUserProfileEmailTxt().setText(getHomeForm().getSocialModel().getEmail());
            getSsnEditUserProfileMobileTxt().setText(getHomeForm().getSocialModel().getMobile());
        } else {
            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "!!! Data unavailable !!!", "", "*** User profile data not available  ***");
        }
    }

    /**
     * @return the ssnEditUserProfilePanel
     */
    public JPanel getSsnEditUserProfilePanel() {
        return ssnEditUserProfilePanel;
    }

    /**
     * @param ssnEditUserProfilePanel the ssnEditUserProfilePanel to set
     */
    public void setSsnEditUserProfilePanel(JPanel ssnEditUserProfilePanel) {
        this.ssnEditUserProfilePanel = ssnEditUserProfilePanel;
    }

    /**
     * @return the ssnEditUserProfileTitleLabel
     */
    public JLabel getSsnEditUserProfileTitleLabel() {
        return ssnEditUserProfileTitleLabel;
    }

    /**
     * @param ssnEditUserProfileTitleLabel the ssnEditUserProfileTitleLabel to
     * set
     */
    public void setSsnEditUserProfileTitleLabel(JLabel ssnEditUserProfileTitleLabel) {
        this.ssnEditUserProfileTitleLabel = ssnEditUserProfileTitleLabel;
    }

    /**
     * @return the ssnEditProfileFirstNameLabel
     */
    public JLabel getSsnEditUserProfileFirstNameLabel() {
        return ssnEditUserProfileFirstNameLabel;
    }

    /**
     * @param ssnEditUserProfileFirstNameLabel the
     * ssnEditUserProfileFirstNameLabel to set
     */
    public void setSsnEditUserProfileFirstNameLabel(JLabel ssnEditUserProfileFirstNameLabel) {
        this.ssnEditUserProfileFirstNameLabel = ssnEditUserProfileFirstNameLabel;
    }

    /**
     * @return the ssnEditUserProfileLastNameLabel
     */
    public JLabel getSsnEditUserProfileLastNameLabel() {
        return ssnEditUserProfileLastNameLabel;
    }

    /**
     * @param ssnEditUserProfileLastNameLabel the
     * ssnEditUserProfileLastNameLabel to set
     */
    public void setSsnEditUserProfileLastNameLabel(JLabel ssnEditUserProfileLastNameLabel) {
        this.ssnEditUserProfileLastNameLabel = ssnEditUserProfileLastNameLabel;
    }

    /**
     * @return the ssnEditUserProfileEmailLabel
     */
    public JLabel getSsnEditUserProfileEmailLabel() {
        return ssnEditUserProfileEmailLabel;
    }

    /**
     * @param ssnEditUserProfileEmailLabel the ssnEditUserProfileEmailLabel to
     * set
     */
    public void setSsnEditUserProfileEmailLabel(JLabel ssnEditUserProfileEmailLabel) {
        this.ssnEditUserProfileEmailLabel = ssnEditUserProfileEmailLabel;
    }

    /**
     * @return the ssnEditUserProfileMobileLabel
     */
    public JLabel getSsnEditUserProfileMobileLabel() {
        return ssnEditUserProfileMobileLabel;
    }

    /**
     * @param ssnEditUserProfileMobileLabel the ssnEditUserProfileMobileLabel to
     * set
     */
    public void setSsnEditUserProfileMobileLabel(JLabel ssnEditUserProfileMobileLabel) {
        this.ssnEditUserProfileMobileLabel = ssnEditUserProfileMobileLabel;
    }

    /**
     * @return the ssnEditUserProfileFNameTxt
     */
    public JTextField getSsnEditUserProfileFNameTxt() {
        return ssnEditUserProfileFNameTxt;
    }

    /**
     * @param ssnEditUserProfileFNameTxt the ssnEditUserProfileFNameTxt to set
     */
    public void setSsnEditUserProfileFNameTxt(JTextField ssnEditUserProfileFNameTxt) {
        this.ssnEditUserProfileFNameTxt = ssnEditUserProfileFNameTxt;
    }

    /**
     * @return the ssnEditUserProfileLNameTxt
     */
    public JTextField getSsnEditUserProfileLNameTxt() {
        return ssnEditUserProfileLNameTxt;
    }

    /**
     * @param ssnEditUserProfileLNameTxt the ssnEditUserProfileLNameTxt to set
     */
    public void setSsnEditUserProfileLNameTxt(JTextField ssnEditUserProfileLNameTxt) {
        this.ssnEditUserProfileLNameTxt = ssnEditUserProfileLNameTxt;
    }

    /**
     * @return the ssnEditUserProfileTxt
     */
    public JTextField getSsnEditUserProfileTxt() {
        return ssnEditUserProfileTxt;
    }

    /**
     * @param ssnEditUserProfileTxt the ssnEditUserProfileTxt to set
     */
    public void setSsnEditUserProfileTxt(JTextField ssnEditUserProfileTxt) {
        this.ssnEditUserProfileTxt = ssnEditUserProfileTxt;
    }

    /**
     * @return the ssnEditUserProfileEmailTxt
     */
    public JTextField getSsnEditUserProfileEmailTxt() {
        return ssnEditUserProfileEmailTxt;
    }

    /**
     * @param ssnEditUserProfileEmailTxt the ssnEditUserProfileEmailTxt to set
     */
    public void setSsnEditUserProfileEmailTxt(JTextField ssnEditUserProfileEmailTxt) {
        this.ssnEditUserProfileEmailTxt = ssnEditUserProfileEmailTxt;
    }

    /**
     * @return the ssnEditUserProfileMobileTxt
     */
    public JTextField getSsnEditUserProfileMobileTxt() {
        return ssnEditUserProfileMobileTxt;
    }

    /**
     * @param ssnEditUserProfileMobileTxt the ssnEditUserProfileMobileTxt to set
     */
    public void setSsnEditUserProfileMobileTxt(JTextField ssnEditUserProfileMobileTxt) {
        this.ssnEditUserProfileMobileTxt = ssnEditUserProfileMobileTxt;
    }

    /**
     * @return the ssnEditProfileUpdateBtn
     */
    public JLabel getSsnEditProfileUpdateBtn() {
        return ssnEditProfileUpdateBtn;
    }

    /**
     * @param ssnEditProfileUpdateBtn the ssnEditProfileUpdateBtn to set
     */
    public void setSsnEditProfileUpdateBtn(JLabel ssnEditProfileUpdateBtn) {
        this.ssnEditProfileUpdateBtn = ssnEditProfileUpdateBtn;
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
     * @return the ssnEditProfileCancelBtn
     */
    public JLabel getSsnEditProfileCancelBtn() {
        return ssnEditProfileCancelBtn;
    }

    /**
     * @param ssnEditProfileCancelBtn the ssnEditProfileCancelBtn to set
     */
    public void setSsnEditProfileCancelBtn(JLabel ssnEditProfileCancelBtn) {
        this.ssnEditProfileCancelBtn = ssnEditProfileCancelBtn;
    }

    /**
     * @return the ssnEditUserProfileCancelLabel
     */
    public JLabel getSsnEditUserProfileCancelLabel() {
        return ssnEditUserProfileCancelLabel;
    }

    /**
     * @param ssnEditUserProfileCancelLabel the ssnEditUserProfileCancelLabel to
     * set
     */
    public void setSsnEditUserProfileCancelLabel(JLabel ssnEditUserProfileCancelLabel) {
        this.ssnEditUserProfileCancelLabel = ssnEditUserProfileCancelLabel;
    }

    /**
     * @return the userProfileController
     */
    public SSNUserProfileController getUserProfileController() {
        return userProfileController;
    }

    /**
     * @param userProfileController the userProfileController to set
     */
    public void setUserProfileController(SSNUserProfileController userProfileController) {
        this.userProfileController = userProfileController;
    }

    /**
     * @return the loginResponse
     */
    public SSNLoginResponse getLoginResponse() {
        return loginResponse;
    }

    /**
     * @param loginResponse the loginResponse to set
     */
    public void setLoginResponse(SSNLoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    // For Development Testing Purpose 
//    public static void main(String[] args) throws Exception {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            @SuppressWarnings("ResultOfObjectAllocationIgnored")
//            public void run() {
//                try {
//                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                        if ("Nimbus".equals(info.getName())) {
//                            UIManager.setLookAndFeel(info.getClassName());
//                            break;
//                        }
//                    }
//                    new SSNEditUserProfileForm();
//                } 
//                catch (Exception ex) {
//                }
//            }
//        });
//    }  
    /**
     * @return the editUserProfileModel
     */
    public SSNEditUserProfileModel getEditUserProfileModel() {
        return editUserProfileModel;
    }

    /**
     * @param editUserProfileModel the editUserProfileModel to set
     */
    public void setEditUserProfileModel(SSNEditUserProfileModel editUserProfileModel) {
        this.editUserProfileModel = editUserProfileModel;
    }

    public JLabel getSsnEditUserProfileBirthdayLabel() {
        return ssnEditUserProfileBirthdayLabel;
    }

    public void setSsnEditUserProfileBirthdayLabel(JLabel ssnEditUserProfileBirthdayLabel) {
        this.ssnEditUserProfileBirthdayLabel = ssnEditUserProfileBirthdayLabel;
    }

    public JLabel getSsnEditUserProfileGenderLabel() {
        return ssnEditUserProfileGenderLabel;
    }

    public void setSsnEditUserProfileGenderLabel(JLabel ssnEditUserProfileGenderLabel) {
        this.ssnEditUserProfileGenderLabel = ssnEditUserProfileGenderLabel;
    }

    public JLabel getSsnEditUserProfileZipCodeLabel() {
        return ssnEditUserProfileZipCodeLabel;
    }

    public void setSsnEditUserProfileZipCodeLabel(JLabel ssnEditUserProfileZipCodeLabel) {
        this.ssnEditUserProfileZipCodeLabel = ssnEditUserProfileZipCodeLabel;
    }

    public JDateChooser getSsnEditUserProfileDateChooser() {
        return ssnEditUserProfileDateChooser;
    }

    public void setSsnEditUserProfileDateChooser(JDateChooser ssnEditUserProfileDateChooser) {
        this.ssnEditUserProfileDateChooser = ssnEditUserProfileDateChooser;
    }

    public JTextField getSsnEditUserProfileBirthDayText() {
        return ssnEditUserProfileBirthDayText;
    }

    public void setSsnEditUserProfileBirthDayText(JTextField ssnEditUserProfileBirthDayText) {
        this.ssnEditUserProfileBirthDayText = ssnEditUserProfileBirthDayText;
    }

    public JComboBox getSsnEditUserProfileGenderBox() {
        return ssnEditUserProfileGenderBox;
    }

    public void setSsnEditUserProfileGenderBox(JComboBox ssnEditUserProfileGenderBox) {
        this.ssnEditUserProfileGenderBox = ssnEditUserProfileGenderBox;
    }

    public JTextField getSsnEditUserProfileZipCodeText() {
        return ssnEditUserProfileZipCodeText;
    }

    public void setSsnEditUserProfileZipCodeText(JTextField ssnEditUserProfileZipCodeText) {
        this.ssnEditUserProfileZipCodeText = ssnEditUserProfileZipCodeText;
    }

}
