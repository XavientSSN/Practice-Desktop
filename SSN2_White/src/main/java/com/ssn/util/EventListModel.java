/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.util;

import com.ssn.dao.SSNDao;
import com.ssn.schedule.SSNScheduleListForm;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mamir1
 */
public class EventListModel {
    
    
    
	 List<SSNScheduleListForm> getParentEventList() throws SQLException{
	 List<SSNScheduleListForm> eventListFormList= new ArrayList<SSNScheduleListForm>();

        List<SSNScheduleListForm> eventListForm= SSNDao.getParentEventList();
        
        for(SSNScheduleListForm item : eventListForm ){
            SSNScheduleListForm sSNScheduleListForm = new SSNScheduleListForm();
            sSNScheduleListForm.setSsnAlbum(item.getSsnAlbum());
            sSNScheduleListForm.setSsnComent(item.getSsnComent());
            sSNScheduleListForm.setSsnLocation(item.getSsnLocation());
            sSNScheduleListForm.setSsnTitle(item.getSsnTitle());
            sSNScheduleListForm.setSsnStrfromDate(item.getSsnStrfromDate());
            sSNScheduleListForm.setSsnStrtoDate(item.getSsnStrtoDate());
            sSNScheduleListForm.setSsnkeyWords(item.getSsnkeyWords());
            eventListFormList.add(sSNScheduleListForm);
            
        }
	 return eventListFormList;
		
	}
	 
	 
	 List<SSNScheduleListForm> getEventListChild(String title) throws SQLException{
	
		 List<SSNScheduleListForm> eventListFormList= new ArrayList<SSNScheduleListForm>();

        List<SSNScheduleListForm> eventListForm= SSNDao.getChildEventList(title);
        
        for(SSNScheduleListForm item : eventListForm ){
            SSNScheduleListForm sSNScheduleListForm = new SSNScheduleListForm();
            sSNScheduleListForm.setSsnAlbum(item.getSsnAlbum());
            sSNScheduleListForm.setSsnComent(item.getSsnComent());
            sSNScheduleListForm.setSsnLocation(item.getSsnLocation());
            sSNScheduleListForm.setSsnTitle(item.getSsnTitle());
            sSNScheduleListForm.setSsnStrfromDate(item.getSsnStrfromDate());
            sSNScheduleListForm.setSsnStrtoDate(item.getSsnStrtoDate());
            sSNScheduleListForm.setSsnkeyWords(item.getSsnkeyWords());
            eventListFormList.add(sSNScheduleListForm);
            
        }
        return eventListFormList;
    }
	 List<String> getScheduleDays(){
		 
		 List<String> schDayList= new ArrayList<String>();
		 schDayList.add("2");
		 schDayList.add("3");
		 schDayList.add("8");
		 schDayList.add("12");
		 return schDayList;
		 
	 }
}
