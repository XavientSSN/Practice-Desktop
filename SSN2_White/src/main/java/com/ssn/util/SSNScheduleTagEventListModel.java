/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.util;

import com.ssn.dao.SSNDao;
import com.ssn.schedule.SSNScheduleListForm;
import com.ssn.ui.custom.component.SSNToolBar;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mamir1
 */
public class SSNScheduleTagEventListModel {
    
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SSNScheduleTagEventListModel.class);
    
	 List<SSNScheduleListForm> getParentEventList() throws SQLException{
	 List<SSNScheduleListForm> eventListFormList= new ArrayList<SSNScheduleListForm>();

        List<SSNScheduleListForm> eventListForm= SSNDao.getParentEventList();
        
        for(SSNScheduleListForm item : eventListForm ){
            SSNScheduleListForm sSNScheduleListForm = new SSNScheduleListForm();
            sSNScheduleListForm.setTagId(item.getTagId());
            sSNScheduleListForm.setSsnAlbum(item.getSsnAlbum());
            sSNScheduleListForm.setSsnComent(item.getSsnComent());
            sSNScheduleListForm.setSsnLocation(item.getSsnLocation());
            sSNScheduleListForm.setSsnTitle(item.getSsnTitle());
            sSNScheduleListForm.setSsnStrfromDate(item.getSsnStrfromDate());
            sSNScheduleListForm.setSsnStrtoDate(item.getSsnStrtoDate());
            sSNScheduleListForm.setSsnkeyWords(item.getSsnkeyWords());
            sSNScheduleListForm.setSsnStartTime(item.getSsnStartTime());
            sSNScheduleListForm.setSsnEndTime(item.getSsnEndTime());
            eventListFormList.add(sSNScheduleListForm);
            
        }
	 return eventListFormList;
		
	}
	 
	 
        List<SSNScheduleListForm> getEventListChild(String title) throws SQLException{
	
		 List<SSNScheduleListForm> eventListFormList= new ArrayList<SSNScheduleListForm>();

        List<SSNScheduleListForm> eventListForm= SSNDao.getChildEventList(title);
        
        for(SSNScheduleListForm item : eventListForm ){
            
            SSNScheduleListForm sSNScheduleListForm = new SSNScheduleListForm();
            sSNScheduleListForm.setTagId(item.getTagId());
            sSNScheduleListForm.setSubTagId(item.getSubTagId());
            sSNScheduleListForm.setSsnAlbum(item.getSsnAlbum());
            sSNScheduleListForm.setSsnSubtags(item.getSsnSubtags());
            sSNScheduleListForm.setSsnComent(item.getSsnComent());
            sSNScheduleListForm.setSsnLocation(item.getSsnLocation());
            sSNScheduleListForm.setSsnTitle(item.getSsnTitle());
            sSNScheduleListForm.setSsnSubtags(item.getSsnSubtags());
            sSNScheduleListForm.setSsnStrfromDate(item.getSsnStrfromDate());
            sSNScheduleListForm.setSsnStrtoDate(item.getSsnStrtoDate());
            sSNScheduleListForm.setSsnkeyWords(item.getSsnkeyWords());
            sSNScheduleListForm.setSsnStartTime(item.getSsnStartTime());
            sSNScheduleListForm.setSsnEndTime(item.getSsnEndTime());
            eventListFormList.add(sSNScheduleListForm);
            
        }
        return eventListFormList;
    }
     
        List<SSNScheduleListForm> getEventListChild(int tagId) throws SQLException{
	
		 List<SSNScheduleListForm> eventListFormList= new ArrayList<SSNScheduleListForm>();

        List<SSNScheduleListForm> eventListForm= SSNDao.getChildEventList(tagId);
        
        for(SSNScheduleListForm item : eventListForm ){
            
            SSNScheduleListForm sSNScheduleListForm = new SSNScheduleListForm();
            sSNScheduleListForm.setTagId(item.getTagId());
            sSNScheduleListForm.setSubTagId(item.getSubTagId());
            sSNScheduleListForm.setSsnAlbum(item.getSsnAlbum());
            sSNScheduleListForm.setSsnSubtags(item.getSsnSubtags());
            sSNScheduleListForm.setSsnComent(item.getSsnComent());
            sSNScheduleListForm.setSsnLocation(item.getSsnLocation());
            sSNScheduleListForm.setSsnTitle(item.getSsnTitle());
            sSNScheduleListForm.setSsnSubtags(item.getSsnSubtags());
            sSNScheduleListForm.setSsnStrfromDate(item.getSsnStrfromDate());
            sSNScheduleListForm.setSsnStrtoDate(item.getSsnStrtoDate());
            sSNScheduleListForm.setSsnkeyWords(item.getSsnkeyWords());
            sSNScheduleListForm.setSsnStartTime(item.getSsnStartTime());
            sSNScheduleListForm.setSsnEndTime(item.getSsnEndTime());
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
