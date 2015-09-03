package com.ssn.ui.custom.component;

import com.ssn.app.loader.SSNConstants;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.ui.form.SSNLoginForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;


/**
 *
 * @author vkvarma
 */
public class SSNToolBar extends JToolBar {


	   


    private static final long                 serialVersionUID    = 1L;
    private ArrayList<JToolBar> ssnToolBarList = null;
    private Insets ssnToolBarBtnMargin = null;
    private SSNLoginForm loginForm = null;
    private SSNHomeForm homeForm = null;
    private SSNHomeController homeController = null;
    private SSNHomeModel homeModel = null;
    private SSNIconTextField searchMediaTextField = null;
    private ButtonGroup searchOptionButtonGroup = null;
    public static JLabel desktopHomeLabel = null;
    public static JLabel hiveLabel = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNToolBar.class);
    public SSNToolBar() {
        
    }
    
    public SSNToolBar(Map<String,String> toolBarItemsMap,SSNLoginForm loginForm,SSNHomeForm homeForm,SSNHomeController homeController) {
        super();
        this.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.setOpaque(true);        
        setLoginForm(loginForm); 
        setHomeForm(homeForm);
        setHomeController(homeController);
        setHomeModel(getHomeForm().getHomeModel()); 
        this.initToolBar(toolBarItemsMap);
        this.getHomeForm().isolatedComponents.add(this);
        
    }
    
    private void initToolBar(Map<String,String> toolBarItemsMap) {
        Dimension dim       =SSNConstants.SSN_SCREEN_SIZE;
        int screenWidth     =   (int)dim.getWidth();
        int screeHeight     =   (int)dim.getHeight();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setSsnToolBarList(new ArrayList<JToolBar>());  
        this.setSsnToolBarBtnMargin(new Insets(0,10,0,0)); 
        
        int counter =0;
        JPanel panelLeft        =   new JPanel();
        JPanel panelCenter      =   new JPanel();
        JPanel panelRight       =   new JPanel();
        
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));
        
        JLabel ourHiveItem     = new JLabel(new ImageIcon(getClass().getResource("/images/ssn-hive-logo.png")),JLabel.LEFT);
        panelLeft.add(ourHiveItem);
        
        for (Map.Entry<String,String> entry : toolBarItemsMap.entrySet()) {
            counter++;
            String key    = entry.getKey();
            String values = entry.getValue();
            if(values != null && values.length() > 2) {
                String [] valueArray = values.split("\\|");
                if(valueArray != null && valueArray.length >=2) {
                    String title          = valueArray[0];
                    String fTitle         = "<html><b>"+title+"</b></html>";                    
                    String iconPath       = valueArray[1];  
                    String actionCommand  = valueArray[2];  
                    String toolTip        = valueArray[3];
                    JLabel toolBarItem      = null;
                    
                    toolBarItem   = new JLabel(fTitle,new ImageIcon(getClass().getResource(iconPath)),JLabel.CENTER);
                    toolBarItem.setVerticalTextPosition(BOTTOM);                    
                    toolBarItem.setHorizontalTextPosition(JLabel.CENTER);
                    toolBarItem.setFont(new Font("open sans",Font.PLAIN,9));
                    toolBarItem.setToolTipText(toolTip);   
                   
                    UIManager.put("ToolTip.font", new Font("open sans", Font.PLAIN, 11));
                    
                    if (title.contains("DESKTOP MEDIA")) {
                        toolBarItem.setForeground(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR);
                        desktopHomeLabel = toolBarItem;
                    } else {
                        toolBarItem.setForeground(SSNConstants.SSN_TOOLBAR_WHITE_FONT_COLOR);
                    }
                    
                    if (title.contains("HIVE MEDIA")) {
                        hiveLabel = toolBarItem;
                    }
                    
                    toolBarItem.setSize(150,32);
                    toolBarItem.addMouseListener(this.getHomeController());   
                   
                    toolBarItem.setName(actionCommand); 
                    toolBarItem.setOpaque(false);
                    panelCenter.add(toolBarItem);
                    panelCenter.setOpaque(false);
                    
                    //if(key % 3 == 0) {  
                    if(actionCommand.equalsIgnoreCase("deletePhoto") ||
                       actionCommand.equalsIgnoreCase("gift") ||
                       actionCommand.equalsIgnoreCase("taggedFaces")) {
                            this.add(new JLabel(new ImageIcon(getClass().getResource("/icon/top-icons-seprator.jpg")))); 
                            //this.add(Box.createHorizontalStrut(20)); 
                    } 
                }
            }
        }
        String[] ssnMenuNames = {""/*,"Edit","View","Folder","Picture","Create","Tools"*/, ""};
        JMenuBar profileMenu = new SSNMenuBar(ssnMenuNames, this.getLoginForm(), this.getHomeForm(), this.getHomeController());
        
        panelRight.add(profileMenu,BorderLayout.EAST);
        panelLeft.setBackground(new Color(0,0,0,1));
        panelCenter.setBackground(new Color(0,0,0,1));
        panelRight.setBackground(new Color(0,0,0,1));
        
        this.add(panelLeft);
        this.add(panelCenter);//this.add(Box.createHorizontalStrut(20));
        this.add(panelRight);
       
    }   

    /**
     * @return the ssnToolBarList
     */
    public ArrayList<JToolBar> getSsnToolBarList() {
        return ssnToolBarList;
    }

    /**
     * @param ssnToolBarList the ssnToolBarList to set
     */
    public void setSsnToolBarList(ArrayList<JToolBar> ssnToolBarList) {
        this.ssnToolBarList = ssnToolBarList;
    }

    /**
     * @return the ssnToolBarBtnMargin
     */
    public Insets getSsnToolBarBtnMargin() {
        return ssnToolBarBtnMargin;
    }

    /**
     * @param ssnToolBarBtnMargin the ssnToolBarBtnMargin to set
     */
    public void setSsnToolBarBtnMargin(Insets ssnToolBarBtnMargin) {
        this.ssnToolBarBtnMargin = ssnToolBarBtnMargin;
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

    public SSNIconTextField getSearchMediaTextField() {
        return searchMediaTextField;
    }

    public void setSearchMediaTextField(SSNIconTextField searchMediaTextField) {
        this.searchMediaTextField = searchMediaTextField;
    }

    public ButtonGroup getSearchOptionButtonGroup() {
        return searchOptionButtonGroup;
    }

    public void setSearchOptionButtonGroup(ButtonGroup searchOptionButtonGroup) {
        this.searchOptionButtonGroup = searchOptionButtonGroup;
    }
    
    
}