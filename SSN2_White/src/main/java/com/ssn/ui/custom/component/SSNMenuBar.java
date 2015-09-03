package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
/**
 *
 * @author vkvarma
 */
public class SSNMenuBar extends JMenuBar {
    
    private static final long              serialVersionUID = 1L;    
    private              ArrayList<JMenu>  ssnMenuList      = null;
    private              SSNLoginForm      loginForm        = null;
    private              SSNHomeForm       homeForm         = null;
    private              SSNHomeController homeController   = null;
    private              SSNHomeModel      homeModel        = null;
    private              JMenu             userProfileMenu  = null;
    private              JMenu             iconMenu         = null;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNMenuBar.class);
    public SSNMenuBar() { 
        
    }
    
    public SSNMenuBar(String [] ssnMenues,SSNLoginForm loginForm,SSNHomeForm homeForm,SSNHomeController homeController) {
        setLoginForm(loginForm); 
        setHomeForm(homeForm);
        setHomeController(homeController);
        setHomeModel(getHomeForm().getHomeModel()); 
        initMenu(ssnMenues); 
        
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
    
    private void initMenu(String [] ssnMenuNames) {  
        
        this.setSsnMenuList(new ArrayList<JMenu>());        
        for(String ssnMenu : ssnMenuNames) {            
            JMenu jm = new JMenu(ssnMenu);
            jm.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR); 
            if(ssnMenu.length() > 0) {
                switch(ssnMenu.toUpperCase()) {
                    case "FILE":
                        jm.setMnemonic(KeyEvent.VK_F);
                        jm.addSeparator();
                        this.initMenuItems("File",jm);
                        break;
                    case "EDIT":
                        jm.setMnemonic(KeyEvent.VK_E);
                        this.initMenuItems("Edit",jm);
                        break;
                    case "VIEW":
                        jm.setMnemonic(KeyEvent.VK_V);
                        break;
                    case "FOLDER":
                        jm.setMnemonic(KeyEvent.VK_O);
                        break;
                    case "PICTURE":
                        jm.setMnemonic(KeyEvent.VK_P);
                        break;
                    case "CREATE":
                        jm.setMnemonic(KeyEvent.VK_C);
                        break;
                    case "TOOLS":
                        jm.setMnemonic(KeyEvent.VK_T);
                        break; 
                    case "HELP":
                    jm.setMnemonic(KeyEvent.VK_H);
                    break; 
                }
            }
            this.add(jm);            
        }
        //this.add(Box.createHorizontalGlue());
        this.addSSNUserProfileMenu();
    }
    
    private Image getImageFromWeb(){
        Image image = null;
        URL url = null;
        try {
            String imgUrl = null;
            if (this.getHomeForm().getSocialModel() == null) {
                imgUrl = this.getHomeForm().getLoginResponse().getData().getUser().getUser_profile_picture();

            }
            Toolkit kit = Toolkit.getDefaultToolkit();
            if (imgUrl != null) {
                url = new URL(imgUrl);
                //image = ImageIO.read(url);
                image = kit.getImage(url);
            } else {
                url = getClass().getResource("/icon/uploadPic.png");
                image = kit.getImage(url);
            }

        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return image;
    }
    
    private void addSSNUserProfileMenu() {        
        String cUserName = getHomeForm().getHomeModel().getLoggedInUserName().equals("") ? "" : getHomeForm().getHomeModel().getLoggedInUserName().toUpperCase();
       
        if(cUserName.length()>9)
            cUserName = cUserName.substring(0,9)+"...";
        
        userProfileMenu = new JMenu(cUserName);
        userProfileMenu.setToolTipText(getHomeForm().getHomeModel().getLoggedInUserName().equals("") ? "" : getHomeForm().getHomeModel().getLoggedInUserName().toUpperCase());
        userProfileMenu.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        userProfileMenu.setName("Profile");
        
        // Adding profile image of user
        
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = getImageFromWeb();
        img = img.getScaledInstance(50, -1, Image.SCALE_SMOOTH);
       
        userProfileMenu.setIcon(new ImageIcon(img));
        //userProfileMenu.setToolTipText(cUserName);
        userProfileMenu.setHorizontalTextPosition(SwingConstants.LEFT);
        this.initMenuItems("Profile",userProfileMenu);
        
        Border borderFilterMenu = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.setBorder(borderFilterMenu);
        this.add(userProfileMenu);   

    }
    
    private void initMenuItems(String menuName,JMenu jm) {
        if(menuName.equalsIgnoreCase("File")) {            
  
        }
        if(menuName.equalsIgnoreCase("Edit")) {
            JMenu styleMenuItem = new JMenu("Style");
            
                ActionListener lookAndFeelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
				setLookAndFeel(item.getText());
			}
		};
		
		ButtonGroup lookAndFeelGroup = new ButtonGroup();		
		LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
		for(int i=0; i<info.length; i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(info[i].getName());
			lookAndFeelGroup.add(item);
			item.addActionListener(lookAndFeelListener);
			styleMenuItem.add(item);
			item.setSelected(UIManager.getLookAndFeel().getName().equals(item.getText()));
		}
                jm.add(styleMenuItem);
        }
        if(menuName.equalsIgnoreCase("Profile")) {
            
          
            
            JMenuItem editUserProfile = new JMenuItem("EDIT PROFILE");
            JMenuItem changePassword  = new JMenuItem("CHANGE PASSWORD");
            JMenuItem logoff          = new JMenuItem("LOGOUT");
            JMenuItem preferences     = new JMenuItem("PREFERENCES");
            JMenuItem voiceCommandSettiing     = new JMenuItem("VOICE COMMAND SETTING");
            
            editUserProfile.setFont(new Font("open sans", Font.BOLD, 11));
            changePassword.setFont(new Font("open sans", Font.BOLD, 11));
            logoff.setFont(new Font("open sans", Font.BOLD, 11));
            preferences.setFont(new Font("open sans", Font.BOLD, 11));
            voiceCommandSettiing.setFont(new Font("open sans", Font.BOLD, 11));
            
            editUserProfile.setActionCommand("editUserProfileShowForm");
            changePassword.setActionCommand("changePasswordShowForm");
            logoff.setActionCommand("logoffMenu"); 
            preferences.setActionCommand("preferencesMenu");
            voiceCommandSettiing.setActionCommand("voiceCommandSettingMenu");
            
            editUserProfile.setBackground(new Color(232,231,230)); 
            changePassword.setBackground(new Color(232,231,230)); 
            logoff.setBackground(new Color(232,231,230));            
            preferences.setBackground(new Color(232,231,230)); 
            
            
            editUserProfile.setForeground(new Color(134,132,133)); 
            changePassword.setForeground(new Color(134,132,133)); 
            logoff.setForeground(new Color(123,123,123));            
            preferences.setForeground(new Color(123,123,123)); 
            
            
            editUserProfile.addActionListener(this.getHomeController());  
            changePassword.addActionListener(this.getHomeController());  
            logoff.addActionListener(this.getHomeController());  
            preferences.addActionListener(this.getHomeController());  
            voiceCommandSettiing.addActionListener(this.getHomeController());
            
            
            jm.add(editUserProfile);
            jm.addSeparator();
            jm.add(changePassword);  
            jm.addSeparator();
            jm.add(preferences);
            
            jm.addSeparator();
            jm.add(logoff);
            
        }
        
        

    }
    
    public void setLookAndFeel(String lafName) {
        this.setEnabled(false);
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < info.length; i++) {
            if (info[i].getName().equals(lafName)) {
                try {
                    UIManager.setLookAndFeel(info[i].getClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    for (Component c : getHomeForm().isolatedComponents) {
                        SwingUtilities.updateComponentTreeUI(c);
                    }
                } catch (Exception e) {
                    System.err.println("L&F failed: " + lafName);
                }
                this.setEnabled(true);
                return;
            }
        }
    }
    
    /**
     * @return the ssnMenuList
     */
    public ArrayList<JMenu> getSsnMenuList() {
        return ssnMenuList;
    }

    /**
     * @param ssnMenuList the ssnMenuList to set
     */
    public void setSsnMenuList(ArrayList<JMenu> ssnMenuList) {
        this.ssnMenuList = ssnMenuList;
    }

    /**
     * @return the loginForm
     */
    public SSNLoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * @param loginForm the loginForm to set
     */
    public void setLoginForm(SSNLoginForm loginForm) {
        this.loginForm = loginForm;
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
     * @return the homeController
     */
    public SSNHomeController getHomeController() {
        return homeController;
    }

    /**
     * @param homeController the homeController to set
     */
    public void setHomeController(SSNHomeController homeController) {
        this.homeController = homeController;
    }

    /**
     * @return the homeModel
     */
    public SSNHomeModel getHomeModel() {
        return homeModel;
    }

    /**
     * @param homeModel the homeModel to set
     */
    public void setHomeModel(SSNHomeModel homeModel) {
        this.homeModel = homeModel;
    }

    /**
     * @return the userProfileMenu
     */
    public JMenu getUserProfileMenu() {
        return userProfileMenu;
    }

    /**
     * @param userProfileMenu the userProfileMenu to set
     */
    public void setUserProfileMenu(JMenu userProfileMenu) {
        this.userProfileMenu = userProfileMenu;
    }
}