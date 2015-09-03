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

/**
 *
 * @author hvashistha
 */

import com.ssn.app.loader.SSNConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.*;

public class SSNScheduleTagCalendar extends JPanel {

     JLabel lblMonth, lblYear;
     JButton btnPrev, btnNext;
     JTable tblCalendar;
     JComboBox cmbYear;
     Container pane;
     DefaultTableModel mtblCalendar; //Table model
     JScrollPane stblCalendar; //The scrollpane
     JPanel pnlCalendar;
     int realYear, realMonth, realDay, currentYear, currentMonth;

    static SSNScheduleTagPanelModel eventListModel = new SSNScheduleTagPanelModel();

    static HashMap<Object, Object> schDaysMap;
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    GregorianCalendar cal = null;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleTagCalendar.class);
    public SSNScheduleTagCalendar() {
        LookAndFeel previousLF = UIManager.getLookAndFeel();
      
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
           // System.out.println("Exception is "+ex);
            ex.printStackTrace();
            
        }
        //Look and feel
        cal = new GregorianCalendar();
        try {                    
                SSNScheduleTagCalendar.schDaysMap = eventListModel.getScheduleDays();
             } catch (SQLException | ParseException e) {}
        
        //setSize(320, 340); //Set size to 400x400 pixels

        setLayout(null); //Apply null layout

        setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);

        //Create controls
        lblMonth = new JLabel("JANUARY",JLabel.CENTER);
        lblMonth.setHorizontalTextPosition(JLabel.CENTER);
        
        lblMonth.setFont(new Font("Verdana",Font.BOLD,12));
        lblYear = new JLabel("CHANGE YEAR");
        Border fieldborder = BorderFactory.createLineBorder(new Color(77,77,77,0),0);
        cmbYear = new JComboBox();
        cmbYear.setEditable(true);
        cmbYear.setUI(ColorArrowUI.createUI(cmbYear));
        
        JTextField filed=((JTextField)cmbYear.getEditor().getEditorComponent());
        filed.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        filed.setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
        filed.setHorizontalAlignment(SwingConstants.CENTER);
        filed.setBorder(null);
        
        
      //Added by Sandeep
        lblYear.setFont(new Font("Verdana",Font.BOLD,12));
        Border yellowBorder = BorderFactory.createLineBorder(SSNConstants.SSN_TEXT_LABEL_YELLOW_COLOR,2);
       
         
        cmbYear.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        //btnPrev = new JButton("<<");
        btnPrev= new JButton(new ImageIcon(getClass().getResource("/icon/take_me_arrow.png")));
        btnPrev.setBackground(new Color(0,0,0,1));
        btnPrev.setOpaque(false);
         btnPrev.setBorderPainted(false);
        btnNext= new JButton(new ImageIcon(getClass().getResource("/icon/take_me_arrow2.png")));
        btnNext.setBackground(new Color(0,0,0,1));
        btnNext.setOpaque(false);
        btnNext.setBorderPainted(false);
        mtblCalendar = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };

        //tblCalendar = new JTable(mtblCalendar);
        tblCalendar = new JTable();
        
        tblCalendar.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        //tblCalendar.setBorder(BorderFactory.createEmptyBorder());
        tblCalendar.setShowVerticalLines(true);
        tblCalendar.setShowHorizontalLines(false);
        tblCalendar.setModel(mtblCalendar);
       
       tblCalendar.setGridColor(Color.YELLOW);
        
        /* To Change the look and feel of code */
        //LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
           // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            tblCalendar.getTableHeader().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            tblCalendar.getTableHeader().setForeground(SSNConstants.SSN_WHITE_FONT_COLOR);
            tblCalendar.getTableHeader().setOpaque(true);
                        
            stblCalendar = new JScrollPane(tblCalendar);
            stblCalendar.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            
            pnlCalendar = new JPanel(null);
            pnlCalendar.setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            //UIManager.setLookAndFeel(previousLF);
        }
        catch(Exception e) {
        
       // System.out.println("Exception is coming");
            e.printStackTrace();
        }
        /* End */
      
        btnPrev.addActionListener(new btnPrev_Action());
        btnNext.addActionListener(new btnNext_Action());
        cmbYear.addActionListener(new cmbYear_Action());

       //Add controls to pane
        add(pnlCalendar);
       
        pnlCalendar.add(btnPrev);
        pnlCalendar.add(lblMonth);
        pnlCalendar.add(btnNext);
        pnlCalendar.add(lblYear);
        pnlCalendar.add(cmbYear);
        stblCalendar.setBorder(BorderFactory.createEmptyBorder());
        pnlCalendar.add(stblCalendar);
        
        lblMonth.setForeground(Color.WHITE);
        lblYear.setForeground(Color.WHITE);
        cmbYear.setBorder(yellowBorder);
        
        //Set bound
        pnlCalendar.setBounds(0, 0, 290, 275);
        btnPrev.setBounds(30, 10, 20, 25);
        lblMonth.setBounds(75, 10,110, 25);
        btnNext.setBounds(210, 10, 20, 25);
        stblCalendar.setBounds(2, 50, 250, 190);
        lblYear.setBounds(30, 230, 100, 30);
        cmbYear.setBounds(142, 230, 70, 30);
        //Get real month/year
        GregorianCalendar gregCal = new GregorianCalendar(); //Create calendar
        realDay = gregCal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = gregCal.get(GregorianCalendar.MONTH); //Get month
        realYear = gregCal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;
        //Add headers
        String[] headers = {"S", "M", "T", "W", "T", "F", "S"}; //All headers
        for (int i = 0; i < 7; i++) {
            mtblCalendar.addColumn(headers[i]);
           
        }
          
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
        //No resize/reorder
        tblCalendar.getTableHeader().setResizingAllowed(false);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
        tblCalendar.getTableHeader().setOpaque(true);
        tblCalendar.setFont(new Font("open sans",Font.BOLD,10));
             //Single cell selection
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //added by sandeep
        tblCalendar.setBorder(yellowBorder);
        //Set row/column count
        tblCalendar.setRowHeight(25);
        tblCalendar.setAutoResizeMode(1);

        mtblCalendar.setColumnCount(7);
        mtblCalendar.setRowCount(6);
        //Populate table
        for (int i = realYear - 100; i <= realYear + 100; i++) {

            cmbYear.addItem(String.valueOf(i));

        }
        //Refresh calendar
        refreshCalendar(realMonth, realYear); //Refresh calendar
        try {
            UIManager.setLookAndFeel(previousLF);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SSNScheduleTagCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void refreshCalendar(int month, int year) {
    public void refreshCalendar(int month, int year) {    
        
        cal = new GregorianCalendar(year, month, 1);
        cal.set(year, month, 1);
        //Variables
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som; //Number Of Days, Start Of Month

        //Allow/disallow buttons
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);

        if (month == 0 && year <= realYear - 10) {
            btnPrev.setEnabled(false);
        } //Too early

        if (month == 11 && year >= realYear + 100) {
            btnNext.setEnabled(false);
        } //Too late

        lblMonth.setText(months[month]); //Refresh the month label (at the top)
       // lblMonth.setBounds(130 - lblMonth.getPreferredSize().width / 2, 10, 180, 25); //Re-align label with calendar
        
        cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
        tblCalendar.setOpaque(false);
        tblCalendar.getTableHeader().setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
        
        //Clear table
        for (int i = 0; i < 6; i++) {

            for (int j = 0; j < 7; j++) {

                mtblCalendar.setValueAt(null, i, j);
            }
        }
        //Get first day of month and number of day
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
       
        //Draw calendar
        for (int i = 1; i <= nod; i++) {
            int row = new Integer((i + som - 2) / 7);
            int column = (i + som - 2) % 7;
            mtblCalendar.setValueAt(i, row, column);

        }
        //Apply renderers
        tblCalendarRenderer tblRender =  new tblCalendarRenderer();
        tblRender.setHorizontalAlignment( JLabel.CENTER );
        
        tblCalendar.getColumnModel().getColumn(0).setCellRenderer( tblRender );
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), tblRender);
    }

    class tblCalendarRenderer extends DefaultTableCellRenderer {
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            
            
            setBackground(SSNConstants.SSN_BLACK_BACKGROUND_COLOR);
            Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
             
             String key2 = "";
             int ctr = 0;
            if (value != null)  {
                try {
                    //System.out.println("value::"+Integer.parseInt(value.toString()));
                    SSNScheduleTagPanelForm.scheduleDaysMap = eventListModel.getScheduleDays();
                    Iterator itr = SSNScheduleTagPanelForm.scheduleDaysMap.keySet().iterator();
                    while (itr.hasNext()) {
                        itr.next();
                        sdf.format(cal.getTime());
                        key2 = cal.getWeekYear() + String.valueOf((Integer.parseInt(new SimpleDateFormat("MM").format(cal.getTime()))));
                        key2 = "k" + key2 + ctr;
                       
                        List schDayList = (List) SSNScheduleTagPanelForm.scheduleDaysMap.get(key2);
                        if (schDayList != null) {
                            ListIterator itr1 = schDayList.listIterator();
                            while (itr1.hasNext()) {
                                String str = String.valueOf(itr1.next());
                                int scheduledDay = Integer.valueOf(str);
                               //System.out.println("scheduled Month : "+itr.hasNext()+"cal month : "+cal.getTime());
                                if (scheduledDay == Integer.parseInt(value.toString())) {
                               // System.out.println("true check"+scheduledDay+"---"+Integer.parseInt(value.toString()));
                                    setBackground(new Color(244, 206, 89));
                                   // break;
                                }
                               
                            }
                        }

                        ctr++;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SSNScheduleTagCalendar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) { 
                    Logger.getLogger(SSNScheduleTagCalendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            setBorder(null);
            setForeground(Color.WHITE);
            return this;
        }
    }

//    static class btnPrev_Action implements ActionListener {
    class btnPrev_Action implements ActionListener {    

        public void actionPerformed(ActionEvent e) {            
            
            if (currentMonth == 0) { //Back one year
                currentMonth = 11;
                currentYear -= 1;

            } else { //Back one month
                currentMonth -= 1;
            }
            refreshCalendar(currentMonth, currentYear);
        }
    }

//    static class btnNext_Action implements ActionListener {
    class btnNext_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (currentMonth == 11) { //Foward one year
                currentMonth = 0;
                currentYear += 1;
            } else { //Foward one month
                currentMonth += 1;
            }
            refreshCalendar(currentMonth, currentYear);

        }
            }

//    static class cmbYear_Action implements ActionListener {
    class cmbYear_Action implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (cmbYear.getSelectedItem() != null) {
                String b = cmbYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                refreshCalendar(currentMonth, currentYear);
            }
        }
    }
}
