/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package com.ssn.schedule;

import com.ssn.app.loader.SSNConstants;
import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.event.controller.SSNHomeController;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNHomeModel;
import com.ssn.ui.custom.component.SSNCustomBorder;
import com.ssn.ui.custom.component.SSNIconTextField;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.custom.component.SSNMyScrollbarUI;
import com.ssn.ui.form.SSNHomeForm;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hvashistha
 */
public class SSNScheduleTagPanelForm extends JPanel implements Runnable {

    

    private SSNHomeForm homeForm = null;
    private JScrollPane mainScrollPane = null;
    private JPanel ssnScheduleTagCenterPanel = null;
    private JPanel ssnScheduleTagPanel = null;
    private JPanel ssnScheduleTagListPanel = null;
    private JPanel ssnCalenderPanel = null;
    private JLabel ssnCalenderPanelName = null;
    private String ssnCalenderPanelStr = null;
    private String ssnScheduleTagPanelName = null;
    private String ssnScheduleTagListPanelName = null;
    private JLabel ssnScheduleTagTitleLabel = null;
    private JLabel ssnScheduleTagFilenameLabel = null;
    private JLabel ssnScheduleTagVDOFilenameLabel = null;
    private JLabel ssnScheduleEventListTitleLabel = null;
    private JLabel ssnScheduleTagPhotographerLabel = null;
    private JSeparator editSeparator1 = null;
    private JSeparator editSeparator2 = null;
    private JSeparator editCalenderSeparator = null;
    private JLabel ssnScheduleTagCancelLabel = null;
    private JLabel ssnScheduleTagTitleFieldLabel = null;
    private JLabel ssnScheduleTagKeywordsFieldLabel = null;
    private JLabel ssnScheduleTagLocationFieldLabel = null;
    private JLabel ssnScheduleTagCommentsFieldLabel = null;
    private JLabel ssnScheduleTagAlbumFieldLabel = null;
    private JLabel ssnScheduleTagStartdateLabel = null;
    private JDateChooser startDateChooser = null;
    private JDateChooser endDateChooser = null;
    private JLabel ssnScheduleTagEnddateLabel = null;
    private JLabel ssnScheduleTagStarttimeLabel = null;
    private JLabel ssnScheduleTagEndTimeLabel = null;
    private JComboBox ssnScheduleTagAlbumCombo = null;
    private JTextField ssnScheduleTagTitleTxt = null;
    private JTextField ssnScheduleTagFileNameTxt = null;
    private JTextField ssnScheduleTagVDOFileNameTxt = null;
    private JTextField ssnScheduleTagPhotographerNameTxt = null;
    private JTextField ssnScheduleTagKeywordTxt = null;
    private JTextField ssnScheduleTagLocationTxt = null;
    private JTextArea ssnScheduleTagCmmnt = null;
    private JScrollPane ssnCommentScroll = null;
    private JLabel ssnScheduleTagUserNameLabel = null;
    public static JLabel ssnScheduleTagSaveBtn = null;
    private JLabel ssnScheduleTagCancelBtn = null;
    private JLabel ssnTagIdLabel = null;
    private JButton ssnCalenderBtn = null;
    private SSNScheduleTagController scheduleTagController = null;
    private SSNScheduleTagPanelModel scheduleTagPanelModel = null;
    private SpinnerModel startTimeSpinnerModel = null;
    private JSpinner startTimeSpinner = null;
    private JComponent starteditor = null;
    private SpinnerModel endTimeSpinnerModel = null;
    private JSpinner endTimeSpinner = null;
    private JComponent endeditor = null;
    private JLabel createSubtaskBtn = null;
    private JLabel deleteTaskBtn = null;
    private JLabel updateTaskBtn = null;
    private JButton quickCreateAlbumBtn = null;
    private SSNHomeModel homeModel = null;
    private SSNHomeController homeController = null;
    private JPanel scheduleTagMainPanel = null;
    private SSNScheduleFormUtilityLoader scheduleEntryFormPanel = null;
    private String containSubTag = "false";

    private DefaultTableModel tableModel;
    private SSNScheduleFormUtilityLoader formUtilityDemo;
    private String nodeType = null;
    private static Date sDate = null;
    private static Date eDate = null;
    private static String globalParentTitleName = null;
    private static String globalParentAlbumName = null;
    public static HashMap<Object, Object> scheduleDaysMap;
    private boolean displayUpdateBtn = false;
    private boolean displaySubTagBtn = false;
    private boolean displayDeleteBtn = false;
    private String ssnScheduleTagName = null;
    private JLabel ssnScheduleTagLabelName = null;
    public static String ssnScheduleTagErronMsg = "";

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleTagPanelForm.class);

    public SSNScheduleTagPanelForm(SSNHomeForm homeForm, String panelName, Date stDate, Date endDate) {

        setHomeForm(homeForm);
        setSSNCenterPanelName(panelName);
        setSsnCalenderPanelStr("Calendar");
        scheduleTagMainPanel = new JPanel(new BorderLayout());
        setScheduleTagController(new SSNScheduleTagController(this, homeForm));

        scheduleTagPanelModel = new SSNScheduleTagPanelModel(this, homeForm, getScheduleTagController());
        ssnCalenderPanel = new JPanel();
        mainScrollPane = new JScrollPane();
        //SSNMyScrollbarUI ui = new SSNMyScrollbarUI();
        mainScrollPane.getVerticalScrollBar().setUI(new SSNMyScrollbarUI());
        mainScrollPane.setName("SSNScheduleTagPanelForm");
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getScheduleTagController().setScheduleTagPanelModel(this.getScheduleTagPanelModel());

        this.addDirs();
        this.buildDatePanel();
        this.initScheduleTagFormGUIComponents();
        SSNScheduleTagController.preventOverlap = true;
        this.addScheduleTagFormUIElements();
        this.loadScheduleTagForm();

        // set all act button true on startup
        this.getCreateSubtaskBtn().setVisible(true);
        this.getDeleteTaskBtn().setVisible(true);
        this.getUpdateTaskBtn().setVisible(true);

        if (stDate != null && endDate != null) {
            getStartDateChooser().setDate(stDate);
            getEndDateChooser().setDate(endDate);
        }

        if (getHomeForm() != null) {
            getHomeForm().setSsnScheduleTagPanelForm(this);
        }

    }

    private void initScheduleTagFormGUIComponents() {

        Map<String, String> preferences = null;
        try {
            preferences = SSNDao.getPreferences();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        this.setssnScheduleTagLabelName(new JLabel("<html><font color='rgb(255,215,0)'>" + this.getssnScheduleTagName() + "</font></html>"));
        this.getssnScheduleTagLabelName().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setEditSeparator1(new JSeparator(JSeparator.HORIZONTAL));

        this.setSsnTagIdLabel(new JLabel());
        this.getSsnTagIdLabel().setVisible(false);

        this.setSsnCalenderPanelName(new JLabel("<html><font color='rgb(255,215,0)'>" + this.getSsnCalenderPanelStr() + "</font></html>"));
        this.getSsnCalenderPanelName().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setEditCalenderSeparator(new JSeparator(JSeparator.HORIZONTAL));

        this.setSsnScheduleEventListTitleLabel(new JLabel("<html><font color='rgb(255,215,0)'>&nbsp " + this.getSsnScheduleTagListPanelName() + "</font></html>"));
        this.getSsnScheduleEventListTitleLabel().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.setEditSeparator2(new JSeparator(JSeparator.HORIZONTAL));

        this.setSsnScheduleTagCancelLabel(new JLabel(new ImageIcon(getClass().getResource("/images/popup-close.png"))));
        this.getSsnScheduleTagCancelLabel().setName("closeProfile");
        this.getSsnScheduleTagCancelLabel().addMouseListener(this.getScheduleTagController());
        this.setSsnScheduleTagTitleFieldLabel(new JLabel("PHOTO CAPTION:      "));
        this.getSsnScheduleTagTitleFieldLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagTitleFieldLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagFilenameLabel(new JLabel("IMAGE PREFIX:      "));
        this.getSsnScheduleTagFilenameLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagFilenameLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagVDOFilenameLabel(new JLabel("VIDEO PREFIX:      "));
        this.getSsnScheduleTagVDOFilenameLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagVDOFilenameLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagKeywordsFieldLabel(new JLabel("TAG KEYWORDS:      "));
        this.getSsnScheduleTagKeywordsFieldLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagKeywordsFieldLabel().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.setSsnScheduleTagLocationFieldLabel(new JLabel("LOCATION:      "));
        this.getSsnScheduleTagLocationFieldLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagLocationFieldLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagAlbumFieldLabel(new JLabel("ALBUMS:      "));
        this.getSsnScheduleTagAlbumFieldLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagAlbumFieldLabel().setFont(new Font("Verdana", Font.PLAIN, 12));

        String cUserName = getHomeForm().getHomeModel().getLoggedInUserName().equals("") ? "" : getHomeForm().getHomeModel().getLoggedInUserName();
        this.setSsnScheduleTagUserNameLabel(new JLabel(cUserName));
        this.getSsnScheduleTagUserNameLabel().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.setQuickCreateAlbumBtn((new JButton("", new ImageIcon(getClass().getResource("/icon/create-album-normal.png")))));
        this.getQuickCreateAlbumBtn().setName("quickCreateAlbum");
        this.getQuickCreateAlbumBtn().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getHomeForm().getHomeController().getHomeModel().createHiveAlbum(null);
                new SSNScheduleTagPanelForm(homeForm, "Preset Tags", SSNScheduleTagPanelForm.getsDate(), SSNScheduleTagPanelForm.geteDate());
            }
        });

        this.getQuickCreateAlbumBtn().setToolTipText("Create Album");
        this.setSsnScheduleTagStartdateLabel(new JLabel("START DATE:      "));
        this.getSsnScheduleTagStartdateLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagStartdateLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagStarttimeLabel(new JLabel("START TIME:      "));
        this.getSsnScheduleTagStarttimeLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagStarttimeLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagEnddateLabel(new JLabel("END DATE:      "));
        this.getSsnScheduleTagEnddateLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagEnddateLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagEndTimeLabel(new JLabel("    END TIME:      "));
        this.getSsnScheduleTagEndTimeLabel().setHorizontalAlignment(SwingConstants.RIGHT);
        this.getSsnScheduleTagEndTimeLabel().setFont(new Font("Verdana", Font.PLAIN, 12));
        this.setSsnScheduleTagTitleTxt(new SSNIconTextField("", "Photo Caption", "tagName"));

        try {
            this.setSsnScheduleTagFileNameTxt(new SSNIconTextField("","User Preference for Image", "ImageFileName"));
            this.getSsnScheduleTagFileNameTxt().setText(preferences.get(SSNConstants.SSN_IMAGE_PREFIX));
            this.getSsnScheduleTagFileNameTxt().setFont(new Font("Verdana", Font.PLAIN, 12));
            this.setSsnScheduleTagVDOFileNameTxt(new SSNIconTextField("", "User Preference for Video", "VideoFileName"));
            this.getSsnScheduleTagVDOFileNameTxt().setText(preferences.get(SSNConstants.SSN_VIDEO_PREFIX));
            this.getSsnScheduleTagVDOFileNameTxt().setFont(new Font("Verdana", Font.PLAIN, 12));
            this.setSsnScheduleTagKeywordTxt(new SSNIconTextField("", "Keyword 1, Keyword 2, Keyword n", "keyWordName"));
            this.getSsnScheduleTagKeywordTxt().setFont(new Font("Verdana", Font.PLAIN, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }
            //this.setSsnScheduleTagPhotographerNameTxt(new SSNIconTextField("", cUserName ,"photographerName"));
        // this.getSsnScheduleTagPhotographerNameTxt().setText(cUserName);

        //add text area 
        JTextArea ta1 = new JTextArea(15, 20);
        ta1.setLineWrap(true);
        ta1.setWrapStyleWord(true);
        final JScrollPane scrBar = new JScrollPane();
        scrBar.getVerticalScrollBar().setUI(new SSNMyScrollbarUI());
        scrBar.setViewportView(ta1);
        scrBar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // this.setSsnCommentScroll(scrBar);
        // this.getSsnCommentScroll().setPreferredSize(new Dimension(100,65));

        this.setSsnScheduleTagCmmnt(new JTextArea());
        ssnScheduleTagCmmnt = new JTextArea();
        ssnScheduleTagCmmnt.setText(ta1.getText());
//        this.getSsnScheduleTagCmmnt().setText(ta1.getText());

        this.setSsnScheduleTagLocationTxt(new SSNIconTextField("", "Enter Location", "location"));
        this.getssnScheduleTagLabelName().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        this.getSsnScheduleEventListTitleLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagFilenameLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagVDOFilenameLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnCalenderPanelName().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagTitleFieldLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnCalenderPanel().setForeground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagKeywordsFieldLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        //this.getSsnScheduleTagPhotographerLabel().setForeground(SSNConstants.SSN_FONT_COLOR);
        this.getSsnScheduleTagUserNameLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        //this.getSsnScheduleTagCommentsFieldLabel().setForeground(SSNConstants.SSN_FONT_COLOR);
        this.getSsnScheduleTagLocationFieldLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagAlbumFieldLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagStartdateLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagStarttimeLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagEnddateLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagEndTimeLabel().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        this.getSsnScheduleTagAlbumCombo().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagTitleTxt().setForeground(new Color(74, 73, 73));
        this.getSsnScheduleTagFileNameTxt().setForeground(new Color(74, 73, 73));
        //this.getSsnScheduleTagLocationTxt().setPreferredSize(new Dimension(30,3));
        //this.getSsnScheduleTagPhotographerNameTxt().setForeground(new Color(74, 73, 73));
        this.getSsnScheduleTagAlbumCombo().setPreferredSize(new Dimension(20, 2));
        this.getSsnScheduleTagAlbumCombo().setPrototypeDisplayValue("XXXXXXXXXXXXXX");
        this.getSsnScheduleTagLocationTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getStarteditor().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getEndeditor().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getStartTimeSpinner().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getEndTimeSpinner().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnCalenderPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagKeywordTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagKeywordTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getQuickCreateAlbumBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagTitleTxt().setFont(new Font("Verdana", Font.PLAIN, 12));

        Border yellowBorder = BorderFactory.createLineBorder(new Color(255, 215, 0), 2);

        this.getStartTimeSpinner().setBorder(yellowBorder);
        this.getEndTimeSpinner().setBorder(yellowBorder);
        this.getSsnScheduleTagAlbumCombo().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagAlbumCombo().getHeight(), this.getSsnScheduleTagAlbumCombo().getWidth()));
        this.getSsnScheduleTagFileNameTxt().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagFileNameTxt().getHeight(), this.getSsnScheduleTagFileNameTxt().getWidth()));
        this.getSsnScheduleTagVDOFileNameTxt().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagVDOFileNameTxt().getHeight(), this.getSsnScheduleTagVDOFileNameTxt().getWidth()));
        this.getSsnScheduleTagLocationTxt().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagLocationTxt().getHeight(), this.getSsnScheduleTagLocationTxt().getWidth()));
        this.getSsnScheduleTagTitleTxt().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagTitleTxt().getHeight(), this.getSsnScheduleTagTitleTxt().getWidth()));
        this.getSsnScheduleTagKeywordTxt().setBorder(new SSNCustomBorder(true, new Color(242, 242, 10), this.getSsnScheduleTagKeywordTxt().getHeight(), this.getSsnScheduleTagKeywordTxt().getWidth()));
        this.getSsnScheduleTagFileNameTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagVDOFileNameTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagLocationTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagTitleTxt().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getEndTimeSpinner().getEditor().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getStartTimeSpinner().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        ///Changes for text area

        this.getSsnScheduleTagFileNameTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagVDOFileNameTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagLocationTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagTitleTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        this.getEndTimeSpinner().getEditor().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        this.getSsnScheduleTagAlbumCombo().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        JTextField field = (JTextField) this.getSsnScheduleTagAlbumCombo().getEditor().getEditorComponent();
//        field.setAlignmentX(SwingConstants.LEFT);
//        field.setAlignmentY(SwingConstants.CENTER);

        field.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        field.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        field.setBorder(null);

        ((JLabel) this.getSsnScheduleTagAlbumCombo().getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.getStartTimeSpinner().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        this.getSsnScheduleTagKeywordTxt().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        this.getSsnScheduleTagTitleTxt().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                getSsnScheduleTagTitleTxt().setText(input.trim());
            }
        });
        this.getSsnScheduleTagTitleTxt().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                String input = ((JTextField) e.getSource()).getText();
                // using pattern with flags

                Pattern regex = Pattern.compile("[%$&+,:;=?@#|]");
                Matcher matcher = regex.matcher(input);

                if (matcher.find()) {
                    String stemp = input.replaceAll("[%$&+,:;=?@#|]", "");
                    getSsnScheduleTagTitleTxt().setText(stemp);
                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Special characters are not allowed!");

                }

                Pattern regex1 = Pattern.compile("\\s{2,}");
                Matcher matcher1 = regex1.matcher(input);
                if (input != null && input.length() > 0) {
                    if (Character.isWhitespace(input.charAt(0))) {

                        getSsnScheduleTagTitleTxt().setText(input.trim());
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");

                    } else if (matcher1.find()) {
                        String stemp = matcher1.replaceAll(" ");
                        getSsnScheduleTagTitleTxt().setText(stemp);
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Spaces are not allowed!");
                    } else {

                    }

                }

            }

        });

        this.getSsnScheduleTagKeywordTxt().setFont(new Font("Verdana", Font.PLAIN, 12));
//        this.getSsnCommentScroll().setFont(new Font("Arial",Font.PLAIN,14));
        this.getSsnScheduleTagLocationTxt().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.setSsnScheduleTagSaveBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/save_btn_schedule.png"))));
        //this.getSsnScheduleTagSaveBtn().setName("Save");
        this.getSsnScheduleTagSaveBtn().setText("Save");
        this.getSsnScheduleTagSaveBtn().setBackground(new Color(74, 73, 73));
        this.getSsnScheduleTagSaveBtn().setForeground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getSsnScheduleTagSaveBtn().addMouseListener(new save_Action());

        this.setSsnScheduleTagCancelBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/reset_btn_schedule.png"))));
        this.getSsnScheduleTagCancelBtn().setBackground(new Color(74, 73, 73));
        this.getSsnScheduleTagCancelBtn().setForeground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getSsnScheduleTagCancelBtn().addMouseListener(new reset_Action());
        this.setCreateSubtaskBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/Sub-active.png"))));

        this.getCreateSubtaskBtn().setToolTipText("Schedule Sub Event");
        this.getCreateSubtaskBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getCreateSubtaskBtn().setForeground(new Color(0, 0, 0));

        this.getCreateSubtaskBtn().setVisible(false);
        this.getCreateSubtaskBtn().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.getCreateSubtaskBtn().addMouseListener(new CreateSubtask());

        this.setDeleteTaskBtn(new JLabel(new ImageIcon(getClass().getResource("/icon/recycle_bin.png"))));
        this.getDeleteTaskBtn().setToolTipText("Delete");
        this.getDeleteTaskBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getDeleteTaskBtn().setForeground(new Color(0, 0, 0));
        //this.getDeleteTaskBtn().setFocusPainted(false);
        this.getDeleteTaskBtn().setVisible(false);
        this.getDeleteTaskBtn().setFont(new Font("Verdana", Font.PLAIN, 12));

        this.getDeleteTaskBtn().addMouseListener(new DeleteTask());

        this.setUpdateTaskBtn(new JLabel(new ImageIcon(getClass().getResource("/images/update_icon.png"))));
        this.getUpdateTaskBtn().setToolTipText("Update");
        this.getUpdateTaskBtn().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        this.getUpdateTaskBtn().setForeground(new Color(0, 0, 0));
        this.getUpdateTaskBtn().setVisible(false);
        this.getUpdateTaskBtn().setFont(new Font("Verdana", Font.PLAIN, 12));
        //this.getUpdateTaskBtn().addMouseListener(new UpdateTask());
        this.getUpdateTaskBtn().addMouseListener(new UpdateTask());
    }

    private void setSSNCenterPanelName(String pnlName) {
        if (pnlName != null && pnlName.equalsIgnoreCase("Preset Tags")) {

            //setSsnScheduleTagPanelName("Schedule a Tag");
            setssnScheduleTagName("Schedule a Tag");
            setSsnScheduleTagPanelName("Preset Tags");
            setSsnScheduleTagListPanelName("Event List");
            setSsnCalenderPanelStr("Calendar");
        }
        if (pnlName != null && pnlName.equalsIgnoreCase("Sub Tag")) {
            setssnScheduleTagName("Schedule a Tag");
            setSsnScheduleTagPanelName("Sub Tag");
            setSsnScheduleTagListPanelName("Event List");
            setSsnCalenderPanelStr("Calendar");
        }
    }

    private void addDirs() {
        if (!SSNHelper.getSsnHiveDirPath().isEmpty()) {
            String hiveDir = SSNHelper.getSsnHiveDirPath();
            if (!hiveDir.equals("")) {
                final File folder = new File(hiveDir);
                //int i = 0;
                String directories[] = folder.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {

                        return new File(current, name).isDirectory();
                    }
                });
                Arrays.sort(directories);
                Vector<String> albumNames = new Vector<String>();
                for (int i = 0; i < directories.length; i++) {
                    if (!directories[i].equals("OurHive")) {
                        albumNames.add(directories[i]);
                    }
                }
                albumNames.insertElementAt("OurHive", 0);
                this.setSsnScheduleTagAlbumCombo(new JComboBox(albumNames));
                // this.getSsnScheduleTagAlbumCombo().setAlignmentX(CENTER_ALIGNMENT);

            }
        }
    }

    private void buildDatePanel() {

        // System.out.println("buildDatePanel");
        JDateChooser startDate = new JDateChooser();
        startDate.setBackground(new Color(71, 71, 71, 1));
        startDate.setOpaque(false);
        startDate.getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        startDate.getDateEditor().getUiComponent().setBackground(new Color(71, 71, 71, 1));
        startDate.getDateEditor().getUiComponent().setOpaque(false);

        int height = startDate.getDateEditor().getUiComponent().getHeight();
        int width = startDate.getDateEditor().getUiComponent().getWidth();
        startDate.getDateEditor().getUiComponent().setSize(100, height);
        startDate.getDateEditor().getUiComponent().setBorder(new SSNCustomBorder(true, height, width));
        JTextField txtField = (JTextField) startDate.getDateEditor().getUiComponent();
        txtField.setHorizontalAlignment(SwingConstants.CENTER);
        //txtField.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

        startDate.getCalendarButton().setBackground(new Color(71, 71, 71, 0));

        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/icon/calender_icon.png"));
        startDate.getCalendarButton().setSize(2, 1);
        startDate.setIcon(imgIcon);

        setStartDateChooser(startDate);
        JDateChooser endDate = new JDateChooser();
        endDate.setBackground(new Color(71, 71, 71, 1));
        endDate.setOpaque(false);
        endDate.getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        endDate.getDateEditor().getUiComponent().setBackground(new Color(71, 71, 71, 1));
        endDate.getDateEditor().getUiComponent().setOpaque(false);

        int endheight = endDate.getDateEditor().getUiComponent().getHeight();
        int endwidth = endDate.getDateEditor().getUiComponent().getWidth();
        endDate.getDateEditor().getUiComponent().setSize(100, endheight);
        endDate.getDateEditor().getUiComponent().setBorder(new SSNCustomBorder(true, endheight, endwidth));

        JTextField endtxtField = (JTextField) endDate.getDateEditor().getUiComponent();
        endtxtField.setHorizontalAlignment(SwingConstants.CENTER);
        endDate.getCalendarButton().setBackground(new Color(71, 71, 71, 0));
        ImageIcon endicon = new ImageIcon(getClass().getResource("/icon/calender_icon.png"));
        endDate.getCalendarButton().setSize(2, 1);
        endDate.setIcon(endicon);

        setEndDateChooser(endDate);

        Date startDateObj = new Date();
        Date endDateObj = new Date();
        String datePattern = SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN;
        getStartDateChooser().setDateFormatString(datePattern);
        getStartDateChooser().setDate(startDateObj);
        getEndDateChooser().setDateFormatString(datePattern);
        getEndDateChooser().setDate(endDateObj);

        getStartDateChooser().setMinSelectableDate(new Date());
        getEndDateChooser().setMinSelectableDate(new Date());
        getEndDateChooser().setOpaque(false);

        paintSideCalendar();

        for (Component comp1 : getStartDateChooser().getComponents()) {
            if (comp1 instanceof JTextField) {
                ((JTextField) comp1).setColumns(55);
                ((JTextField) comp1).setPreferredSize(new Dimension(((JTextField) comp1).getWidth() + 5, ((JTextField) comp1).getHeight()));
                ((JTextField) comp1).setEditable(false);
                ((JTextField) comp1).setToolTipText("mm-dd-yyyy");

            }

            comp1.addPropertyChangeListener(new PropertyChangeListener() {

                @Override

                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("date")) {
                        getStartDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

                        java.text.SimpleDateFormat sdfSD = new java.text.SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
                        getStartDateChooser().setOpaque(false);
                        if (sDate != null && eDate != null) {
                            getStartDateChooser().setMinSelectableDate(sDate);
                            ((JTextField) (getEndDateChooser().getDateEditor().getUiComponent())).setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

                            //getEndDateChooser().setSelectableDateRange(getStartDateChooser().getDate(), eDate);
                            getEndDateChooser().setMinSelectableDate(getStartDateChooser().getDate());
                        }

//                        } else {
//                            String startTime = null;
//                            startTime = getFormatedTime(getStartTimeSpinner().getValue().toString());
//                            String endTime = getFormatedTime(getEndTimeSpinner().getValue().toString());
//                            getEndDateChooser().setMinSelectableDate(getStartDateChooser().getDate());
//
//                            try {
//                                if (scheduleTagPanelModel.isNewTagOverLap(sdfSD.parse(sdfSD.format(getStartDateChooser().getDate())), startTime, sdfSD.parse(sdfSD.format(getEndDateChooser().getDate())), endTime, getSsnScheduleTagPanelName()) && SSNScheduleTagController.preventOverlap) {
//                                    SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                                    dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Date specified overlaps with another schedule.");
//                                    getStartDateChooser().setFocusable(true);
//
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
                        getScheduleEntryFormPanel().getDateChooserPnl().repaint();

                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            getStartDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

                        }
                    });

                }

            });

        }
        for (Component comp2 : getEndDateChooser().getComponents()) {
            if (comp2 instanceof JTextField) {
                ((JTextField) comp2).setColumns(55);
                ((JTextField) comp2).setPreferredSize(new Dimension(((JTextField) comp2).getWidth() + 5, ((JTextField) comp2).getHeight()));
                ((JTextField) comp2).setEditable(false);
                ((JTextField) comp2).setToolTipText("mm-dd-yyyy");
            }

            comp2.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("date")) {
                        getEndDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
                        getEndDateChooser().setMinSelectableDate(getStartDateChooser().getDate());
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            getEndDateChooser().getDateEditor().getUiComponent().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);

                        }
                    });
                }
            });

        }

        getStartDateChooser().setVisible(true);
        getEndDateChooser().setVisible(true);

        setStartTimeSpinnerModel(new SpinnerDateModel());
        setStartTimeSpinner(new JSpinner(getStartTimeSpinnerModel()));
        setStarteditor(new JSpinner.DateEditor(getStartTimeSpinner(), SSNConstants.SSN_SCHEDULE_TAG_TIME_PATTERN));
        getStartTimeSpinner().setEditor(getStarteditor());
        getStartTimeSpinner().setValue(startDateObj);
        getStartTimeSpinner().setVisible(true);

        setEndTimeSpinnerModel(new SpinnerDateModel());
        setEndTimeSpinner(new JSpinner(getEndTimeSpinnerModel()));
        setEndeditor(new JSpinner.DateEditor(getEndTimeSpinner(), SSNConstants.SSN_SCHEDULE_TAG_TIME_PATTERN));
        getEndTimeSpinner().setEditor(getEndeditor());
        getEndTimeSpinner().setValue(endDateObj);
        getEndTimeSpinner().setVisible(true);

    }

    private void paintSideCalendar() {
        List daysList;
        daysList = null;
        try {
            @SuppressWarnings("UnusedAssignment")
            String key2 = "";
            int ctr = 0;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy");
            SSNScheduleTagPanelForm.scheduleDaysMap = getScheduleTagPanelModel().getScheduleDays();
            GregorianCalendar cal = new GregorianCalendar();
            Iterator itr = scheduleDaysMap.keySet().iterator();
            while (itr.hasNext()) {
                itr.next();
                sdf.format(cal.getTime());
                key2 = cal.getWeekYear() + String.valueOf((Integer.parseInt(new SimpleDateFormat("MM").format(cal.getTime()))));
                key2 = "k" + key2 + ctr;
                List schDayList = (List) scheduleDaysMap.get(key2);
                if (schDayList != null) {
                    daysList = new ArrayList();
                    ListIterator itr1 = schDayList.listIterator();
                    while (itr1.hasNext()) {
                        String str = String.valueOf(itr1.next());
                        int scheduledDay = Integer.valueOf(str);
                        daysList.add(scheduledDay);
                    }

                }
                ctr++;
            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(SSNScheduleTagPanelForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addScheduleTagFormUIElements() {

        formUtilityDemo = new SSNScheduleFormUtilityLoader(this.homeForm, this);
        setScheduleEntryFormPanel(formUtilityDemo);
        getScheduleEntryFormPanel().setVisible(true);
        getScheduleEntryFormPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        Border paddingBorder = BorderFactory.createEmptyBorder(0, 10, 0, 30);
        Border paddingBtnBorder = BorderFactory.createEmptyBorder(0, 2, 0, 40);
        Border border = BorderFactory.createLineBorder(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        JPanel pnlABove = new JPanel(new BorderLayout());
        //pnlABove.add(this.getSsnScheduleTagTitleLabel(), BorderLayout.NORTH);
        pnlABove.add(this.getssnScheduleTagLabelName(), BorderLayout.NORTH);
        this.getEditSeparator1().setBorder(BorderFactory.createCompoundBorder(border, paddingBtnBorder));
        //pnlABove.add(this.getEditSeparator1(), BorderLayout.SOUTH);
        pnlABove.add(Box.createVerticalStrut(15));
        pnlABove.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        pnlABove.add(this.getSsnTagIdLabel());

        pnlABove.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        getScheduleTagMainPanel().add(pnlABove, BorderLayout.NORTH);
        getScheduleTagMainPanel().add(getScheduleEntryFormPanel(), BorderLayout.CENTER);

        JPanel SchdPnl = new JPanel(new BorderLayout());
        SchdPnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        JPanel pnlTree = new JPanel(new BorderLayout());
        pnlTree.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        //
        pnlTree.add(Box.createHorizontalStrut(15));
        pnlTree.add(this.getSsnScheduleEventListTitleLabel(), BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        btnPanel.add(this.createSubtaskBtn);
        btnPanel.add(Box.createHorizontalStrut(3));
        btnPanel.add(this.updateTaskBtn);
        btnPanel.add(Box.createHorizontalStrut(3));
        btnPanel.add(this.deleteTaskBtn);
        btnPanel.setBorder(BorderFactory.createCompoundBorder(border, paddingBtnBorder));
        //btnPanel.setSize(100, 8);
        pnlTree.add(btnPanel, BorderLayout.EAST);
        this.getEditSeparator2().setBorder(BorderFactory.createCompoundBorder(border, paddingBtnBorder));
        //pnlTree.add(this.getEditSeparator2(), BorderLayout.SOUTH);

        SchdPnl.add(pnlTree, BorderLayout.NORTH);

        JPanel treePnl = new JPanel(new BorderLayout());
        treePnl.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        treePnl.add(getScheduleTagController().addScheduleTagTreePanel());

        getHomeForm().setScheduletagTreepanel(getScheduleTagController().addScheduleTagTreePanel());

        getHomeForm().setTreePanel(new JPanel(new BorderLayout()));
        getHomeForm().getTreePanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        getHomeForm().getTreePanel().add(getHomeForm().getScheduletagTreepanel());
        SchdPnl.add(getHomeForm().getTreePanel(), BorderLayout.SOUTH);
        this.getScheduleTagMainPanel().add(SchdPnl, BorderLayout.SOUTH);
        this.getScheduleTagMainPanel().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        this.getMainScrollPane().getVerticalScrollBar().setUI(new SSNMyScrollbarUI(1));
        this.getMainScrollPane().setViewportView(this.getScheduleTagMainPanel());
        this.getMainScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.getMainScrollPane().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add Calender elements to getSsnCalenderPanel()
        this.getSsnCalenderPanel().add(this.getSsnCalenderPanelName());
        this.getSsnCalenderPanel().add(this.getEditCalenderSeparator());

    }

    public void enableActionPanel(String nodeType) {
        this.setNodeType(nodeType);
        if (nodeType.equals(SSNConstants.SSN_SCHEDULAR_CHILD_TAG)) {
            enableAllActionButton(nodeType);
        } else {
            enableAllActionButton(nodeType);
        }

    }

    public void enableAllActionButton(String nodeType) {
        if (nodeType.equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_PARENT_TAG)) {
            this.getUpdateTaskBtn().setIcon(new ImageIcon(getClass().getResource("/images/update_icon.png")));
            this.setDisplayUpdateBtn(true);

            this.getDeleteTaskBtn().setIcon(new ImageIcon(getClass().getResource("/icon/recycle_bin.png")));
            this.setDisplayDeleteBtn(true);

            this.getCreateSubtaskBtn().setIcon(new ImageIcon(getClass().getResource("/icon/Sub-active.png")));
            this.setDisplaySubTagBtn(true);
        } else {
            this.getCreateSubtaskBtn().setIcon(new ImageIcon(getClass().getResource("/icon/Sub-active.png")));
            this.setDisplaySubTagBtn(false);
            this.getUpdateTaskBtn().setIcon(new ImageIcon(getClass().getResource("/images/update_icon.png")));
            this.setDisplayUpdateBtn(true);

            this.getDeleteTaskBtn().setIcon(new ImageIcon(getClass().getResource("/icon/recycle_bin.png")));
            this.setDisplayDeleteBtn(true);
        }

    }

    private void loadScheduleTagForm() {
        getHomeForm().getSsnHomeCenterMainPanel().removeAll();
        getHomeForm().getSsnHomeCenterPanel().removeAll();
        getHomeForm().getSsnHomeRightPanel().removeAll();

        getHomeForm().getSsnHomeRightPanel().revalidate();
        getHomeForm().getSsnHomeRightPanel().repaint();
        getHomeForm().getSsnHomeCenterPanel().add(this.getMainScrollPane(), BorderLayout.CENTER);
        getHomeForm().getSsnHomeRightPanel().add(this.getSsnCalenderPanel(), BorderLayout.NORTH);
        getHomeForm().getSsnHomeRightPanel().add(new SSNScheduleTagCalendar(), BorderLayout.CENTER);
        getHomeForm().getSsnHomeCenterMainPanel().add(getHomeForm().getSsnHomeCenterPanel(), BorderLayout.CENTER);
        getHomeForm().getSsnHomeCenterMainPanel().revalidate();
        getHomeForm().getSsnHomeCenterMainPanel().repaint();
        getHomeForm().revalidate();
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
    private void setHomeForm(SSNHomeForm homeForm) {
        this.homeForm = homeForm;
    }

    /**
     * @return the ssnScheduleTagCenterPanel
     */
    public JPanel getSsnScheduleTagCenterPanel() {
        return ssnScheduleTagCenterPanel;
    }

    /**
     * @param ssnScheduleTagCenterPanel the ssnScheduleTagCenterPanel to set
     */
    public void setSsnScheduleTagCenterPanel(JPanel ssnScheduleTagCenterPanel) {
        this.ssnScheduleTagCenterPanel = ssnScheduleTagCenterPanel;
    }

    /**
     * @return the ssnScheduleTagPanel
     */
    public JPanel getSsnScheduleTagPanel() {
        return ssnScheduleTagPanel;
    }

    /**
     * @param ssnScheduleTagPanel the ssnScheduleTagPanel to set
     */
    public void setSsnScheduleTagPanel(JPanel ssnScheduleTagPanel) {
        this.ssnScheduleTagPanel = ssnScheduleTagPanel;
    }

    /**
     * @return the ssnScheduleTagListPanel
     */
    public JPanel getSsnScheduleTagListPanel() {
        return ssnScheduleTagListPanel;
    }

    /**
     * @param ssnScheduleTagListPanel the ssnScheduleTagListPanel to set
     */
    public void setSsnScheduleTagListPanel(JPanel ssnScheduleTagListPanel) {
        this.ssnScheduleTagListPanel = ssnScheduleTagListPanel;
    }

    /**
     * @return the ssnScheduleTagPanelName
     */
    public String getSsnScheduleTagPanelName() {
        return ssnScheduleTagPanelName;
    }

    /**
     * @param ssnScheduleTagPanelName the ssnScheduleTagPanelName to set
     */
    public void setSsnScheduleTagPanelName(String ssnScheduleTagPanelName) {
        this.ssnScheduleTagPanelName = ssnScheduleTagPanelName;
    }

    /**
     * @return the ssnScheduleTagTitleLabel
     */
    public JLabel getSsnScheduleTagTitleLabel() {
        return ssnScheduleTagTitleLabel;
    }

    /**
     * @param ssnScheduleTagTitleLabel the ssnScheduleTagTitleLabel to set
     */
    public void setSsnScheduleTagTitleLabel(JLabel ssnScheduleTagTitleLabel) {
        this.ssnScheduleTagTitleLabel = ssnScheduleTagTitleLabel;
    }

    /**
     * @return the ssnScheduleTagCancelLabel
     */
    public JLabel getSsnScheduleTagCancelLabel() {
        return ssnScheduleTagCancelLabel;
    }

    /**
     * @param ssnScheduleTagCancelLabel the ssnScheduleTagCancelLabel to set
     */
    public void setSsnScheduleTagCancelLabel(JLabel ssnScheduleTagCancelLabel) {
        this.ssnScheduleTagCancelLabel = ssnScheduleTagCancelLabel;
    }

    /**
     * @return the ssnScheduleTagTitleFieldLabel
     */
    public JLabel getSsnScheduleTagTitleFieldLabel() {
        return ssnScheduleTagTitleFieldLabel;
    }

    /**
     * @param ssnScheduleTagTitleFieldLabel the ssnScheduleTagTitleFieldLabel to
     * set
     */
    public void setSsnScheduleTagTitleFieldLabel(JLabel ssnScheduleTagTitleFieldLabel) {
        this.ssnScheduleTagTitleFieldLabel = ssnScheduleTagTitleFieldLabel;
    }

    /**
     * @return the ssnScheduleTagKeywordsFieldLabel
     */
    public JLabel getSsnScheduleTagKeywordsFieldLabel() {
        return ssnScheduleTagKeywordsFieldLabel;
    }

    /**
     * @param ssnScheduleTagKeywordsFieldLabel the
     * ssnScheduleTagKeywordsFieldLabel to set
     */
    public void setSsnScheduleTagKeywordsFieldLabel(JLabel ssnScheduleTagKeywordsFieldLabel) {
        this.ssnScheduleTagKeywordsFieldLabel = ssnScheduleTagKeywordsFieldLabel;
    }

    /**
     * @return the ssnScheduleTagLocationFieldLabel
     */
    public JLabel getSsnScheduleTagLocationFieldLabel() {
        return ssnScheduleTagLocationFieldLabel;
    }

    /**
     * @param ssnScheduleTagLocationFieldLabel the
     * ssnScheduleTagLocationFieldLabel to set
     */
    public void setSsnScheduleTagLocationFieldLabel(JLabel ssnScheduleTagLocationFieldLabel) {
        this.ssnScheduleTagLocationFieldLabel = ssnScheduleTagLocationFieldLabel;
    }

//    /**
//     * @return the ssnScheduleTagCommentsFieldLabel
//     */
//    public JLabel getSsnScheduleTagCommentsFieldLabel() {
//        return ssnScheduleTagCommentsFieldLabel;
//    }
//
//    /**
//     * @param ssnScheduleTagCommentsFieldLabel the ssnScheduleTagCommentsFieldLabel to set
//     */
//    public void setSsnScheduleTagCommentsFieldLabel(JLabel ssnScheduleTagCommentsFieldLabel) {
//        this.ssnScheduleTagCommentsFieldLabel = ssnScheduleTagCommentsFieldLabel;
//    }
    /**
     * @return the ssnScheduleTagAlbumFieldLabel
     */
    public JLabel getSsnScheduleTagAlbumFieldLabel() {
        return ssnScheduleTagAlbumFieldLabel;
    }

    /**
     * @param ssnScheduleTagAlbumFieldLabel the ssnScheduleTagAlbumFieldLabel to
     * set
     */
    public void setSsnScheduleTagAlbumFieldLabel(JLabel ssnScheduleTagAlbumFieldLabel) {
        this.ssnScheduleTagAlbumFieldLabel = ssnScheduleTagAlbumFieldLabel;
    }

    /**
     * @return the ssnScheduleTagStartdateLabel
     */
    public JLabel getSsnScheduleTagStartdateLabel() {
        return ssnScheduleTagStartdateLabel;
    }

    /**
     * @param ssnScheduleTagStartdateLabel the ssnScheduleTagStartdateLabel to
     * set
     */
    public void setSsnScheduleTagStartdateLabel(JLabel ssnScheduleTagStartdateLabel) {
        this.ssnScheduleTagStartdateLabel = ssnScheduleTagStartdateLabel;
    }

    /**
     * @return the startDateChooser
     */
    public JDateChooser getStartDateChooser() {
        return startDateChooser;
    }

    /**
     * @param startDateChooser the startDateChooser to set
     */
    public void setStartDateChooser(JDateChooser startDateChooser) {
        this.startDateChooser = startDateChooser;
    }

    /**
     * @return the endDateChooser
     */
    public JDateChooser getEndDateChooser() {
        return endDateChooser;
    }

    /**
     * @param endDateChooser the endDateChooser to set
     */
    public void setEndDateChooser(JDateChooser endDateChooser) {
        this.endDateChooser = endDateChooser;
    }

    /**
     * @return the ssnScheduleTagEnddateLabel
     */
    public JLabel getSsnScheduleTagEnddateLabel() {
        return ssnScheduleTagEnddateLabel;
    }

    /**
     * @param ssnScheduleTagEnddateLabel the ssnScheduleTagEnddateLabel to set
     */
    public void setSsnScheduleTagEnddateLabel(JLabel ssnScheduleTagEnddateLabel) {
        this.ssnScheduleTagEnddateLabel = ssnScheduleTagEnddateLabel;
    }

    /**
     * @return the ssnScheduleTagStarttimeLabel
     */
    public JLabel getSsnScheduleTagStarttimeLabel() {
        return ssnScheduleTagStarttimeLabel;
    }

    /**
     * @param ssnScheduleTagStarttimeLabel the ssnScheduleTagStarttimeLabel to
     * set
     */
    public void setSsnScheduleTagStarttimeLabel(JLabel ssnScheduleTagStarttimeLabel) {
        this.ssnScheduleTagStarttimeLabel = ssnScheduleTagStarttimeLabel;
    }

    /**
     * @return the ssnScheduleTagEndTimeLabel
     */
    public JLabel getSsnScheduleTagEndTimeLabel() {
        return ssnScheduleTagEndTimeLabel;
    }

    /**
     * @param ssnScheduleTagEndTimeLabel the ssnScheduleTagEndTimeLabel to set
     */
    public void setSsnScheduleTagEndTimeLabel(JLabel ssnScheduleTagEndTimeLabel) {
        this.ssnScheduleTagEndTimeLabel = ssnScheduleTagEndTimeLabel;
    }

    /**
     * @return the ssnScheduleTagAlbumCombo
     */
    public JComboBox getSsnScheduleTagAlbumCombo() {
        return ssnScheduleTagAlbumCombo;
    }

    /**
     * @param ssnScheduleTagAlbumCombo the ssnScheduleTagAlbumCombo to set
     */
    public void setSsnScheduleTagAlbumCombo(JComboBox ssnScheduleTagAlbumCombo) {
        this.ssnScheduleTagAlbumCombo = ssnScheduleTagAlbumCombo;
    }

    /**
     * @return the ssnScheduleTagTitleTxt
     */
    public JTextField getSsnScheduleTagTitleTxt() {
        return ssnScheduleTagTitleTxt;
    }

    /**
     * @param ssnScheduleTagTitleTxt the ssnScheduleTagTitleTxt to set
     */
    public void setSsnScheduleTagTitleTxt(JTextField ssnScheduleTagTitleTxt) {
        this.ssnScheduleTagTitleTxt = ssnScheduleTagTitleTxt;
    }

    /**
     * @return the ssnScheduleTagKeywordTxt
     */
    public JTextField getSsnScheduleTagKeywordTxt() {
        return ssnScheduleTagKeywordTxt;
    }

    /**
     * @param ssnScheduleTagKeywordTxt the ssnScheduleTagKeywordTxt to set
     */
    public void setSsnScheduleTagKeywordTxt(JTextField ssnScheduleTagKeywordTxt) {
        this.ssnScheduleTagKeywordTxt = ssnScheduleTagKeywordTxt;
    }

    /**
     * @return the ssnScheduleTagLocationTxt
     */
    public JTextField getSsnScheduleTagLocationTxt() {
        return ssnScheduleTagLocationTxt;
    }

    /**
     * @param ssnScheduleTagLocationTxt the ssnScheduleTagLocationTxt to set
     */
    public void setSsnScheduleTagLocationTxt(JTextField ssnScheduleTagLocationTxt) {
        this.ssnScheduleTagLocationTxt = ssnScheduleTagLocationTxt;
    }

    /**
     * @return the ssnScheduleTagCmmnt
     */
    public JTextArea getSsnScheduleTagCmmnt() {
        return ssnScheduleTagCmmnt;
    }

    /**
     * @param ssnScheduleTagCmmnt the ssnScheduleTagCmmnt to set
     */
    public void setSsnScheduleTagCmmnt(JTextArea ssnScheduleTagCmmnt) {
        this.ssnScheduleTagCmmnt = ssnScheduleTagCmmnt;
    }

    /**
     * @return the ssnScheduleTagSaveBtn
     */
    public JLabel getSsnScheduleTagSaveBtn() {
        return ssnScheduleTagSaveBtn;
    }

    /**
     * @param ssnScheduleTagSaveBtn the ssnScheduleTagSaveBtn to set
     */
    public void setSsnScheduleTagSaveBtn(JLabel ssnScheduleTagSaveBtn) {
        this.ssnScheduleTagSaveBtn = ssnScheduleTagSaveBtn;
    }

    /**
     * @return the ssnScheduleTagCancelBtn
     */
    public JLabel getSsnScheduleTagCancelBtn() {
        return ssnScheduleTagCancelBtn;
    }

    /**
     * @param ssnScheduleTagCancelBtn the ssnScheduleTagCancelBtn to set
     */
    public void setSsnScheduleTagCancelBtn(JLabel ssnScheduleTagCancelBtn) {
        this.ssnScheduleTagCancelBtn = ssnScheduleTagCancelBtn;
    }

    /**
     * @return the scheduleTagController
     */
    private SSNScheduleTagController getScheduleTagController() {
        return scheduleTagController;
    }

    /**
     * @param scheduleTagController the scheduleTagController to set
     */
    private void setScheduleTagController(SSNScheduleTagController scheduleTagController) {
        this.scheduleTagController = scheduleTagController;
    }

    /**
     * @return the scheduleTagPanelModel
     */
    private SSNScheduleTagPanelModel getScheduleTagPanelModel() {
        return scheduleTagPanelModel;
    }

    /**
     * @param scheduleTagPanelModel the scheduleTagPanelModel to set
     */
    public void setScheduleTagPanelModel(SSNScheduleTagPanelModel scheduleTagPanelModel) {
        this.scheduleTagPanelModel = scheduleTagPanelModel;
    }

    /**
     * @return the startTimeSpinnerModel
     */
    public SpinnerModel getStartTimeSpinnerModel() {
        return startTimeSpinnerModel;
    }

    /**
     * @param startTimeSpinnerModel the startTimeSpinnerModel to set
     */
    public void setStartTimeSpinnerModel(SpinnerModel startTimeSpinnerModel) {
        this.startTimeSpinnerModel = startTimeSpinnerModel;
    }

    /**
     * @return the startTimeSpinner
     */
    public JSpinner getStartTimeSpinner() {
        return startTimeSpinner;
    }

    /**
     * @param startTimeSpinner the startTimeSpinner to set
     */
    public void setStartTimeSpinner(JSpinner startTimeSpinner) {
        this.startTimeSpinner = startTimeSpinner;
    }

    /**
     * @return the starteditor
     */
    public JComponent getStarteditor() {
        return starteditor;
    }

    /**
     * @param starteditor the starteditor to set
     */
    public void setStarteditor(JComponent starteditor) {
        this.starteditor = starteditor;
    }

    /**
     * @return the endTimeSpinnerModel
     */
    public SpinnerModel getEndTimeSpinnerModel() {
        return endTimeSpinnerModel;
    }

    /**
     * @param endTimeSpinnerModel the endTimeSpinnerModel to set
     */
    public void setEndTimeSpinnerModel(SpinnerModel endTimeSpinnerModel) {
        this.endTimeSpinnerModel = endTimeSpinnerModel;
    }

    /**
     * @return the endTimeSpinner
     */
    public JSpinner getEndTimeSpinner() {
        return endTimeSpinner;
    }

    /**
     * @param endTimeSpinner the endTimeSpinner to set
     */
    public void setEndTimeSpinner(JSpinner endTimeSpinner) {
        this.endTimeSpinner = endTimeSpinner;
    }

    /**
     * @return the endeditor
     */
    public JComponent getEndeditor() {
        return endeditor;
    }

    /**
     * @param endeditor the endeditor to set
     */
    public void setEndeditor(JComponent endeditor) {
        this.endeditor = endeditor;
    }

    /**
     * @return the createSubtaskBtn
     */
    public JLabel getCreateSubtaskBtn() {
        return createSubtaskBtn;
    }

    /**
     * @param createSubtaskBtn the createSubtaskBtn to set
     */
    public void setCreateSubtaskBtn(JLabel createSubtaskBtn) {
        this.createSubtaskBtn = createSubtaskBtn;
    }

    /**
     * @return the quickCreateAlbumBtn
     */
    public JButton getQuickCreateAlbumBtn() {
        return quickCreateAlbumBtn;
    }

    /**
     * @param quickCreateAlbumBtn the quickCreateAlbumBtn to set
     */
    public void setQuickCreateAlbumBtn(JButton quickCreateAlbumBtn) {
        this.quickCreateAlbumBtn = quickCreateAlbumBtn;
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

    @Override
    public void run() {
    }

    /**
     * @return the ssnScheduleTagListPanelName
     */
    public String getSsnScheduleTagListPanelName() {
        return ssnScheduleTagListPanelName;
    }

    /**
     * @param ssnScheduleTagListPanelName the ssnScheduleTagListPanelName to set
     */
    public void setSsnScheduleTagListPanelName(String ssnScheduleTagListPanelName) {
        this.ssnScheduleTagListPanelName = ssnScheduleTagListPanelName;
    }

    /**
     * @return the ssnScheduleEventListTitleLabel
     */
    public JLabel getSsnScheduleEventListTitleLabel() {
        return ssnScheduleEventListTitleLabel;
    }

    /**
     * @param ssnScheduleEventListTitleLabel the ssnScheduleEventListTitleLabel
     * to set
     */
    public void setSsnScheduleEventListTitleLabel(JLabel ssnScheduleEventListTitleLabel) {
        this.ssnScheduleEventListTitleLabel = ssnScheduleEventListTitleLabel;
    }

    /**
     * @return the tableModel
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * @param tableModel the tableModel to set
     */
    private void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * @return the ssnCalenderPanel
     */
    public JPanel getSsnCalenderPanel() {
        return ssnCalenderPanel;
    }

    /**
     * @param ssnCalenderPanel the ssnCalenderPanel to set
     */
    public void setSsnCalenderPanel(JPanel ssnCalenderPanel) {
        this.ssnCalenderPanel = ssnCalenderPanel;
    }

    /**
     * @return the ssnCalenderBtn
     */
    public JButton getSsnCalenderBtn() {
        return ssnCalenderBtn;
    }

    /**
     * @param ssnCalenderBtn the ssnCalenderBtn to set
     */
    public void setSsnCalenderBtn(JButton ssnCalenderBtn) {
        this.ssnCalenderBtn = ssnCalenderBtn;
    }

    /**
     * @return the deleteTaskBtn
     */
    public JLabel getDeleteTaskBtn() {
        return deleteTaskBtn;
    }

    /**
     * @param deleteTaskBtn the deleteTaskBtn to set
     */
    public void setDeleteTaskBtn(JLabel deleteTaskBtn) {
        this.deleteTaskBtn = deleteTaskBtn;
    }

    /**
     * @return the scheduleEntryFormPanel
     */
    public SSNScheduleFormUtilityLoader getScheduleEntryFormPanel() {
        return scheduleEntryFormPanel;
    }

    /**
     * @param scheduleEntryFormPanel the scheduleEntryFormPanel to set
     */
    public void setScheduleEntryFormPanel(SSNScheduleFormUtilityLoader scheduleEntryFormPanel) {
        this.scheduleEntryFormPanel = scheduleEntryFormPanel;
    }

    /**
     * @return the sDate
     */
    public static Date getsDate() {
        return sDate;
    }

    /**
     * @param asDate the sDate to set
     */
    public static void setsDate(Date asDate) {
        sDate = asDate;
    }

    /**
     * @return the eDate
     */
    public static Date geteDate() {
        return eDate;
    }

    /**
     * @param aeDate the eDate to set
     */
    public static void seteDate(Date aeDate) {
        eDate = aeDate;
    }

    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the containSubTag
     */
    public String getContainSubTag() {
        return containSubTag;
    }

    /**
     * @param containSubTag the containSubTag to set
     */
    public void setContainSubTag(String containSubTag) {
        this.containSubTag = containSubTag;
    }

    /**
     * @return the globalParentTitleName
     */
    public static String getGlobalParentTitleName() {
        return globalParentTitleName;
    }

    /**
     * @param aGlobalParentTitleName the globalParentTitleName to set
     */
    public static void setGlobalParentTitleName(String aGlobalParentTitleName) {
        globalParentTitleName = aGlobalParentTitleName;
    }

    /**
     * @return the globalParentAlbumName
     */
    public static String getGlobalParentAlbumName() {
        return globalParentAlbumName;
    }

    /**
     * @param aGlobalParentAlbumName the globalParentAlbumName to set
     */
    public static void setGlobalParentAlbumName(String aGlobalParentAlbumName) {
        globalParentAlbumName = aGlobalParentAlbumName;
    }

    /**
     * @return the updateTaskBtn
     */
    public JLabel getUpdateTaskBtn() {
        return updateTaskBtn;
    }

    /**
     * @param updateTaskBtn the updateTaskBtn to set
     */
    public void setUpdateTaskBtn(JLabel updateTaskBtn) {
        this.updateTaskBtn = updateTaskBtn;
    }

    /**
     * @return the displayUpdateBtn
     */
    public boolean isDisplayUpdateBtn() {
        return displayUpdateBtn;
    }

    /**
     * @param aDisplayUpdateBtn the displayUpdateBtn to set
     */
    public void setDisplayUpdateBtn(boolean aDisplayUpdateBtn) {
        displayUpdateBtn = aDisplayUpdateBtn;
    }

    /**
     * @return the displaySubTagBtn
     */
    public boolean isDisplaySubTagBtn() {
        return displaySubTagBtn;
    }

    /**
     * @param aDisplaySubTagBtn the displaySubTagBtn to set
     */
    public void setDisplaySubTagBtn(boolean aDisplaySubTagBtn) {
        displaySubTagBtn = aDisplaySubTagBtn;
    }

    /**
     * @return the displayDeleteBtn
     */
    public boolean isDisplayDeleteBtn() {
        return displayDeleteBtn;
    }

    /**
     * @param aDisplayDeleteBtn the displayDeleteBtn to set
     */
    public void setDisplayDeleteBtn(boolean aDisplayDeleteBtn) {
        displayDeleteBtn = aDisplayDeleteBtn;
    }

    /**
     * @return the ssnCalenderPanelName
     */
    public JLabel getSsnCalenderPanelName() {
        return ssnCalenderPanelName;
    }

    /**
     * @param ssnCalenderPanelName the ssnCalenderPanelName to set
     */
    public void setSsnCalenderPanelName(JLabel ssnCalenderPanelName) {
        this.ssnCalenderPanelName = ssnCalenderPanelName;
    }

    /**
     * @return the ssnCalenderPanelStr
     */
    public String getSsnCalenderPanelStr() {
        return ssnCalenderPanelStr;
    }

    /**
     * @param ssnCalenderPanelStr the ssnCalenderPanelStr to set
     */
    public void setSsnCalenderPanelStr(String ssnCalenderPanelStr) {
        this.ssnCalenderPanelStr = ssnCalenderPanelStr;
    }

    /**
     * @return the editCalenderSeparator
     */
    public JSeparator getEditCalenderSeparator() {
        return editCalenderSeparator;
    }

    /**
     * @param editCalenderSeparator the editCalenderSeparator to set
     */
    public void setEditCalenderSeparator(JSeparator editCalenderSeparator) {
        this.editCalenderSeparator = editCalenderSeparator;
    }

    public JLabel getSsnTagIdLabel() {
        return ssnTagIdLabel;
    }

    public void setSsnTagIdLabel(JLabel ssnTagIdLabel) {
        this.ssnTagIdLabel = ssnTagIdLabel;
    }

    /**
     * @return the editSeparator1
     */
    public JSeparator getEditSeparator1() {
        return editSeparator1;
    }

    /**
     * @param editSeparator1 the editSeparator1 to set
     */
    public void setEditSeparator1(JSeparator editSeparator1) {
        this.editSeparator1 = editSeparator1;
    }

    /**
     * @return the editSeparator2
     */
    public JSeparator getEditSeparator2() {
        return editSeparator2;
    }

    /**
     * @param editSeparator2 the editSeparator2 to set
     */
    public void setEditSeparator2(JSeparator editSeparator2) {
        this.editSeparator2 = editSeparator2;
    }

    /**
     * @return the ssnCommentScroll
     */
    public JScrollPane getSsnCommentScroll() {
        return ssnCommentScroll;
    }

    /**
     * @param ssnCommentScroll the ssnCommentScroll to set
     */
    public void setSsnCommentScroll(JScrollPane ssnCommentScroll) {
        this.ssnCommentScroll = ssnCommentScroll;
    }

    /**
     * @return the mainScrollPane
     */
    public JScrollPane getMainScrollPane() {
        return mainScrollPane;
    }

    /**
     * @param mainScrollPane the mainScrollPane to set
     */
    public void setMainScrollPane(JScrollPane mainScrollPane) {
        this.mainScrollPane = mainScrollPane;
    }

    /**
     * @return the scheduleTagMainPanel
     */
    public JPanel getScheduleTagMainPanel() {
        return scheduleTagMainPanel;
    }

    /**
     * @param scheduleTagMainPanel the scheduleTagMainPanel to set
     */
    public void setScheduleTagMainPanel(JPanel scheduleTagMainPanel) {
        this.scheduleTagMainPanel = scheduleTagMainPanel;
    }

    /**
     * @return the ssnScheduleTagFilenameLabel
     */
    public JLabel getSsnScheduleTagFilenameLabel() {
        return ssnScheduleTagFilenameLabel;
    }

    /**
     * @param ssnScheduleTagFilenameLabel the ssnScheduleTagFilenameLabel to set
     */
    public void setSsnScheduleTagFilenameLabel(JLabel ssnScheduleTagFilenameLabel) {
        this.ssnScheduleTagFilenameLabel = ssnScheduleTagFilenameLabel;
    }

    /**
     * @return the ssnScheduleTagPhotographerLabel
     */
    public JLabel getSsnScheduleTagPhotographerLabel() {
        return ssnScheduleTagPhotographerLabel;
    }

    /**
     * @param ssnScheduleTagPhotographerLabel the
     * ssnScheduleTagPhotographerLabel to set
     */
    public void setSsnScheduleTagPhotographerLabel(JLabel ssnScheduleTagPhotographerLabel) {
        this.ssnScheduleTagPhotographerLabel = ssnScheduleTagPhotographerLabel;
    }

    /**
     * @return the ssnScheduleTagFileNameTxt
     */
    public JTextField getSsnScheduleTagFileNameTxt() {
        return ssnScheduleTagFileNameTxt;
    }

    /**
     * @param ssnScheduleTagFileNameTxt the ssnScheduleTagFileNameTxt to set
     */
    public void setSsnScheduleTagFileNameTxt(JTextField ssnScheduleTagFileNameTxt) {
        this.ssnScheduleTagFileNameTxt = ssnScheduleTagFileNameTxt;
    }

    /**
     * @return the ssnScheduleTagUserNameLabel
     */
    public JLabel getSsnScheduleTagUserNameLabel() {
        return ssnScheduleTagUserNameLabel;
    }

    /**
     * @param ssnScheduleTagUserNameLabel the ssnScheduleTagUserNameLabel to set
     */
    public void setSsnScheduleTagUserNameLabel(JLabel ssnScheduleTagUserNameLabel) {
        this.ssnScheduleTagUserNameLabel = ssnScheduleTagUserNameLabel;
    }

    /**
     * @return the ssnScheduleTagVDOFilenameLabel
     */
    public JLabel getSsnScheduleTagVDOFilenameLabel() {
        return ssnScheduleTagVDOFilenameLabel;
    }

    /**
     * @param ssnScheduleTagVDOFilenameLabel the ssnScheduleTagVDOFilenameLabel
     * to set
     */
    public void setSsnScheduleTagVDOFilenameLabel(JLabel ssnScheduleTagVDOFilenameLabel) {
        this.ssnScheduleTagVDOFilenameLabel = ssnScheduleTagVDOFilenameLabel;
    }

    public String getssnScheduleTagName() {
        return ssnScheduleTagName;
    }

    public void setssnScheduleTagName(String ssnScheduleTagName) {
        this.ssnScheduleTagName = ssnScheduleTagName;
    }

    public JLabel getssnScheduleTagLabelName() {
        return ssnScheduleTagLabelName;
    }

    public void setssnScheduleTagLabelName(JLabel ssnScheduleTagLabelName) {
        this.ssnScheduleTagLabelName = ssnScheduleTagLabelName;
    }

    /**
     * @return the ssnScheduleTagVDOFileNameTxt
     */
    public JTextField getSsnScheduleTagVDOFileNameTxt() {
        return ssnScheduleTagVDOFileNameTxt;
    }

    /**
     * @param ssnScheduleTagVDOFileNameTxt the ssnScheduleTagVDOFileNameTxt to
     * set
     */
    public void setSsnScheduleTagVDOFileNameTxt(JTextField ssnScheduleTagVDOFileNameTxt) {
        this.ssnScheduleTagVDOFileNameTxt = ssnScheduleTagVDOFileNameTxt;
    }

    class save_Action extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            boolean insertRecord = true;
            boolean sameDateCheck = false;
            String startTime = null;
            String endTime = null;
            Date startDate = null;
            Date endDate = null;

            startTime = getFormatedTime(getStartTimeSpinner().getValue().toString());
            endTime = getFormatedTime(getEndTimeSpinner().getValue().toString());
            startDate = getStartDateChooser().getDate();
            endDate = getEndDateChooser().getDate();

            java.text.SimpleDateFormat sdfSD = new java.text.SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
            sameDateCheck = startDate.equals(endDate);

            // added condition to update child event
            if (getNodeType() != null && getSsnScheduleTagSaveBtn().getText() != null && getNodeType().equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_CHILD_TAG)) {
                SSNScheduleTagController.preventOverlap = true;
                setSsnScheduleTagPanelName("Sub Tag");
            }

            if (getSsnScheduleTagTitleTxt().getText().equals("")) {

                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Photo Caption should not be left blank!");

            } else if (getSsnScheduleTagLocationTxt().getText().equals("")) {

                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Location should not be left blank!");

            } else if (getSsnScheduleTagKeywordTxt().getText().equals("")) {

                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Keyword should not be left blank!");

            } else if (startDate.after(endDate)) {

                SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Start Date cannot be greater than End Date!");
                getStartDateChooser().setDate(new Date());
                getEndDateChooser().setDate(new Date());

            } else {
                // validation for same day event on start time and end time
                try {
                    if (sameDateCheck) {

                        String splitedStartTime[] = (getStartTimeSpinner().getValue().toString()).split(" ");
                        String splitedEndTime[] = (getEndTimeSpinner().getValue().toString()).split(" ");

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                        getStartTimeSpinner().setValue(format.parseObject(splitedStartTime[3].trim()));
                        getEndTimeSpinner().setValue(format.parseObject(splitedEndTime[3].trim()));
                        String startTimeArr[] = (splitedStartTime[3].trim()).split(":");
                        String endTimeArr[] = (splitedEndTime[3].trim()).split(":");
                        if (Integer.parseInt(startTimeArr[0]) > Integer.parseInt(endTimeArr[0])) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "End date/time cannot be earlier than the start date/time.");
                            getStartTimeSpinner().setValue(new Date());
                            getEndTimeSpinner().setValue(new Date());
                            //insertRecord = false;
                            return;

                        } else if ((Integer.parseInt(startTimeArr[0]) == Integer.parseInt(endTimeArr[0]))
                                && (Integer.parseInt(startTimeArr[1]) > Integer.parseInt(endTimeArr[1]))) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "End date/time cannot be earlier than the start date/time.");
                            getStartTimeSpinner().setValue(new Date());
                            getEndTimeSpinner().setValue(new Date());
                            //insertRecord = false;
                            return;
                        } else if ((Integer.parseInt(startTimeArr[0]) == Integer.parseInt(endTimeArr[0]))
                                && (Integer.parseInt(startTimeArr[1]) == Integer.parseInt(endTimeArr[1]))
                                && startDate.compareTo(endDate) == 0) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", "Please specify the correct start date and time and end date and time!");
                            getStartTimeSpinner().setValue(new Date());
                            getEndTimeSpinner().setValue(new Date());
                            // insertRecord = false;
                            return;
                        }

                    }
                    if (!ssnScheduleTagSaveBtn.getText().equalsIgnoreCase("Update")) {
                        if (scheduleTagPanelModel.isNewTagOverLap(startDate, startTime, endDate, endTime, getSsnScheduleTagPanelName())
                                && (getSsnScheduleTagPanelName().equalsIgnoreCase("Preset Tags") || (getSsnScheduleTagPanelName().equalsIgnoreCase("Sub Tag")))) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", ssnScheduleTagErronMsg);
                            // insertRecord = false;
                            return;

                        }
//                        if (scheduleTagPanelModel.isNewTagOverLap(endDate, endTime, getSsnScheduleTagPanelName())
//                                && (getSsnScheduleTagPanelName().equalsIgnoreCase("Preset Tags") || (getSsnScheduleTagPanelName().equalsIgnoreCase("Sub Tag")))) {
//                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
//                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", ssnScheduleTagErronMsg);
//                            // insertRecord = false;
//                            return;
//
//                        }
                    } else {
                        if (scheduleTagPanelModel.isUpdateTagOverLap(startDate, startTime, endDate, endTime, getSsnScheduleTagPanelName())
                                && (getSsnScheduleTagPanelName().equalsIgnoreCase("Preset Tags") || (getSsnScheduleTagPanelName().equalsIgnoreCase("Sub Tag")))) {
                            SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                            dialogBox.initDialogBoxUI(SSNDialogChoice.NOTIFICATION_DIALOG.getType(), "Error", "", ssnScheduleTagErronMsg);
                            // insertRecord = false;
                            return;

                        }
                    }

                    //Update existing record
                    if (getNodeType() != null && getSsnScheduleTagSaveBtn().getText() != null) {

                        insertRecord = false;
                        // update parent
                        if (getNodeType().equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_PARENT_TAG)) {
                            try {

                                scheduleTagPanelModel.updateSchedule(getNodeType(), getSsnScheduleTagTitleTxt().getText(), getSsnScheduleTagAlbumCombo().getSelectedItem().toString(), getSsnScheduleTagLocationTxt().getText(), getSsnScheduleTagKeywordTxt().getText(), "", startDate, endDate, startTime, endTime, "", getSsnScheduleTagVDOFileNameTxt().getText(), getSsnScheduleTagFileNameTxt().getText());
                                new SSNScheduleTagPanelForm(homeForm, "Preset Tags", SSNScheduleTagPanelForm.getsDate(), SSNScheduleTagPanelForm.geteDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(SSNScheduleTagPanelForm.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ew) {
                                ew.printStackTrace();

                            }
                        } // update Child
                        else if (getNodeType().equalsIgnoreCase(SSNConstants.SSN_SCHEDULAR_CHILD_TAG)) {
                            try {
                                scheduleTagPanelModel.updateSchedule(getNodeType(), getSsnScheduleTagTitleTxt().getText(), getSsnScheduleTagAlbumCombo().getSelectedItem().toString(), getSsnScheduleTagLocationTxt().getText(),
                                        getSsnScheduleTagKeywordTxt().getText(), "", startDate, endDate, startTime, endTime, "", getSsnScheduleTagVDOFileNameTxt().getText(), getSsnScheduleTagFileNameTxt().getText());
                                new SSNScheduleTagPanelForm(homeForm, "Preset Tags", SSNScheduleTagPanelForm.getsDate(), SSNScheduleTagPanelForm.geteDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(SSNScheduleTagPanelForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    // inserting new record
                    if (insertRecord) {

                        if (getSsnScheduleTagPanelName().equalsIgnoreCase("Preset Tags")) {
                            scheduleTagPanelModel.insertDataIntoSSNParentTable(SSNScheduleTagPanelForm.this);
                            new SSNScheduleTagPanelForm(homeForm, "Preset Tags", SSNScheduleTagPanelForm.getsDate(), SSNScheduleTagPanelForm.geteDate());
                        } else if (getSsnScheduleTagPanelName().equalsIgnoreCase("Sub Tag")) {
                            setContainSubTag("true");
                            scheduleTagPanelModel.insertDataIntoSSNParentTable(SSNScheduleTagPanelForm.this);
                            new SSNScheduleTagPanelForm(homeForm, "Sub Tag", SSNScheduleTagPanelForm.getsDate(), SSNScheduleTagPanelForm.geteDate());
                        }
                    }

                } catch (ParseException ee) {
                    ee.printStackTrace();
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    class reset_Action implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
            new SSNScheduleTagPanelForm(homeForm, "Preset Tags", null, null);

        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

    }

    class CreateSubtask implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (displaySubTagBtn) {
                setSsnScheduleTagPanelName("Sub Tag");

                new SSNScheduleTagPanelForm(homeForm, "Sub Tag", getsDate(), geteDate());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

    }

    class DeleteTask implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (displayDeleteBtn) {
                if (scheduleTagPanelModel.deleteSchedule(getSsnScheduleTagPanelName())) {
                    new SSNScheduleTagPanelForm(homeForm, "Preset Tags", null, null);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

    }

    class UpdateTask implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            if (displayUpdateBtn) {
                // getSsnScheduleTagTitleTxt().setEditable(false);
//                getSsnScheduleTagTitleTxt().setBackground(Color.gray);
//                getSsnScheduleTagTitleTxt().setForeground(Color.WHITE);
                getSsnScheduleTagSaveBtn().setText("Update");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

    }
//    public static boolean isElapsedTime(Date startDate,String startTime) {
//        boolean result = false;
//        try{
//            SimpleDateFormat df             =   new SimpleDateFormat("MM-dd-yyyy HH:mm");
//            SimpleDateFormat dateFormat     =   new SimpleDateFormat("MM-dd-yyyy");
//            Date currDate           =   df.parse(df.format(new Date()));
//            String dateInStringFromat   =   dateFormat.format(startDate);
//            //System.out.println("date Time " + dateInStringFromat+ " " + startTime);
//            startDate = df.parse( dateInStringFromat+ " " + startTime);
//           // System.out.println("currDate.getTime() " +currDate.getTime() );
//            if(currDate.after( startDate) ){
//                result = true;
//            }
//        }catch(Exception ee){
//            ee.printStackTrace();
//        }
//        return result;
//                
//        
//    }

   
    
    public static String getFormatedTime(String inputTime){
        //=====start
            DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss ZZZ yyyy");
            Date jspinerTime;
            String outputTime = null;
            String endTime = null;
            DateFormat writeFormat1 = new SimpleDateFormat( "HH:mm");
            Date date = null;
            try {
               jspinerTime = readFormat.parse(inputTime);
               outputTime        =   writeFormat1.format(jspinerTime);
           
            } catch ( ParseException ex ) {
                 logger.error(ex);
            }

        return outputTime;

    }
     private static final String[] formats = { 
                "EEE yyyy.MM.dd 'at' HH:mm:ss aa z",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",     "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss",        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",   "yyyy-MM-dd HH:mm:ss", 
                "MM/dd/yyyy HH:mm:ss",          "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'", 
                "MM/dd/yyyy'T'HH:mm:ss.SSSZ",   "MM/dd/yyyy'T'HH:mm:ss.SSS", 
                "MM/dd/yyyy'T'HH:mm:ssZ",       "MM/dd/yyyy'T'HH:mm:ss", 
                "yyyy:MM:dd HH:mm:ss",          "yyyyMMdd", 
                "MM/dd/yyyy HH:mm a",           "M/dd/yy HH:mm a"
                };
     //Mon 2015.06.01 at 02:23:13 PM IST
     
    public static String getFormatedDateTime(String inputTime){
        //=====start
      
        DateFormat readFormat =null;

        Date jspinerTime;
        String outputTime = "";
        String endTime = "";
        DateFormat writeFormat1 = new SimpleDateFormat( "MM-dd-yyyy HH:mm");
        Date date = null;

        if (inputTime != null) {
             if(!inputTime.trim().isEmpty()){
                  for (String parse : formats) {
                        readFormat = new SimpleDateFormat(parse);
                        try {
                            jspinerTime = readFormat.parse(inputTime);
                            //System.out.println(""+parse);
                            outputTime        =   writeFormat1.format(jspinerTime);
                          } catch ( ParseException ex ) {
                            // ex.printStackTrace();
                              //logger.error(ex);
                         }catch ( Exception ex ) {
                            // ex.printStackTrace();
                              logger.error(ex);
                         }
                  }
             }
        }
            return outputTime;
    }
}
