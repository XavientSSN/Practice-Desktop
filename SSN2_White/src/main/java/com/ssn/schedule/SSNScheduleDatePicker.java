/*
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
import com.ssn.model.SSNPreferencesModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author hvashistha
 */
public class SSNScheduleDatePicker extends JPanel{

    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel l = new JLabel("", JLabel.CENTER);
    String day = "";
    JButton[] button = new JButton[42];
    SSNScheduleTagPanelModel eventListModel = new SSNScheduleTagPanelModel();
    HashMap<Object, Object> schDaysMap;
    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleDatePicker.class);
    public SSNScheduleDatePicker() throws SQLException, ParseException {
        this.schDaysMap = eventListModel.getScheduleDays();

        String[] header = {"S", "M", "T", "W", "T", "F", "S"};
        JPanel p1 = new JPanel(new GridLayout(6, 7));
        p1.setPreferredSize(new Dimension(275, 275));

        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.darkGray);
            
            if (x > 6) {
                button[x].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        day = button[selection].getActionCommand();
                    }
                });
            }
            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setForeground(Color.red);
            }
            p1.add(button[x]);
        }
        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<");
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month--;
                displayDate();
            }
        });
        previous.setToolTipText("Previous");
        p2.add(previous);
        JButton next = new JButton(">");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month++;
                displayDate();
            }
        });
        next.setToolTipText("Previous");
        p2.add(next);
        //add(p2, BorderLayout.PAGE_END);  
        add(p1, BorderLayout.PAGE_START);
        
        displayDate();
        setVisible(true);

    }

    public void displayDate() {
        day = "";
        for (int x = 7; x < button.length; x++) {
            button[x].setBackground(Color.gray);
            button[x].setText("");
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        List schDayList = null;
        java.util.Date dt = new java.util.Date();
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
            button[x].setFont(new Font("Arial",Font.BOLD,9));
            Iterator itr = schDaysMap.keySet().iterator();
            int ctr = 0;
            while (itr.hasNext()) {
                sdf.format(cal.getTime());
                itr.next();
                String key2 = cal.getWeekYear() + String.valueOf((Integer.parseInt(new SimpleDateFormat("MM").format(cal.getTime()))));
                key2 = "k" + key2 + ctr;

                schDayList = (List) schDaysMap.get(key2);
                    ListIterator itr1 = schDayList.listIterator();
                    while (itr1.hasNext()) {
                        String str = String.valueOf(itr1.next());
                        int scheduledDay = Integer.valueOf(str);
                        if (scheduledDay == day) {
                            button[x].setBackground(Color.pink);
                            button[x].setText("" + day);
                            button[x].setDisabledSelectedIcon(null);
                        } else {
                            button[x].setText("" + day);
                        }
                    }
                    button[x].setText("" + day);
                ctr++;
            }
            button[x].setText("" + day);
        }
        setName(sdf.format(cal.getTime()));
    }

    public String setPickedDate() {
        if (day.equals("")) {
            return day;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, Integer.parseInt(day));
        return sdf.format(cal.getTime());
    }
}
