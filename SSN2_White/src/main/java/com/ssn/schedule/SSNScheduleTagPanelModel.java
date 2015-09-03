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
import com.ssn.dao.SSNDao;
import com.ssn.ui.form.SSNHomeForm;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 *
 * @author hvashistha
 */
public class SSNScheduleTagPanelModel {

   private              SSNHomeForm                     homeForm                = null;
   private              SSNScheduleTagController        scheduleTagController   = null;
   private              SSNDao                          scheduleTagDAO          = null;
   static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNScheduleTagPanelModel.class);
   public SSNScheduleTagPanelModel() {       
       scheduleTagDAO = new SSNDao();
       setScheduleTagDAO(scheduleTagDAO);
   }
   
   public SSNScheduleTagPanelModel(SSNScheduleTagPanelForm scheduleTagPanelFormMerger,SSNHomeForm homeForm,SSNScheduleTagController scheduleTagController){    
       setHomeForm(homeForm);                
       setScheduleTagController(scheduleTagController);   
       scheduleTagDAO = new SSNDao();
       setScheduleTagDAO(scheduleTagDAO);
       
   }
   
   public int insertDataIntoSSNParentTable(SSNScheduleTagPanelForm scheduleTagPanelFormMerger) {
       int retVal = 0;     
       int insideRetVal = 0;
       try {
          // JViewport viewport = scheduleTagPanelFormMerger.getSsnCommentScroll().getViewport(); 
           JTextArea commentPane = new JTextArea();
            scheduleTagPanelFormMerger.setSsnScheduleTagCmmnt(commentPane);
           if(scheduleTagPanelFormMerger.getContainSubTag().equalsIgnoreCase("true")) {

               //1st Step ------> Update SSNScheduleTag with SUbtag as "True" 
               
              
               
               scheduleTagPanelFormMerger.setSsnScheduleTagCmmnt(commentPane);
               retVal =  scheduleTagDAO.updateParentSSNScheduleTagTable(
                       scheduleTagPanelFormMerger.getGlobalParentTitleName(),
                       scheduleTagPanelFormMerger.getGlobalParentAlbumName());
              // System.out.println("insertDataIntoSSNParentTable");
               //2nd Step ------> Insert into SSNScheduleSubTag with ParentTitle in ParentTagTitle
               //if(retVal == 1) {
                   insideRetVal =  scheduleTagDAO.insertScheduleSubTagTable(SSNHomeForm.tagId,1, 1, 12, 3,
                   scheduleTagPanelFormMerger.getGlobalParentTitleName(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getEndDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagCmmnt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagKeywordTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagLocationTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagTitleTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagAlbumCombo().getSelectedItem().toString(),                   
                   scheduleTagPanelFormMerger.getStartTimeSpinner(),
                   scheduleTagPanelFormMerger.getEndTimeSpinner(),
                   "Check",
                   "",
                   scheduleTagPanelFormMerger.getSsnScheduleTagFileNameTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagVDOFileNameTxt().getText()); 
                   
                   retVal = insideRetVal;
//               } else {
//               }
                
           } else {
               retVal =  scheduleTagDAO.insertParentScheduleTagTable(1, 1, 12, 3, 
                   scheduleTagPanelFormMerger.getContainSubTag(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getStartDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getEndDateChooser().getDate(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagCmmnt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagKeywordTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagLocationTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagTitleTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagAlbumCombo().getSelectedItem().toString(),
		   scheduleTagPanelFormMerger.getStartTimeSpinner(),
                   scheduleTagPanelFormMerger.getEndTimeSpinner(),
                   "Check", 
                   "",
                   scheduleTagPanelFormMerger.getSsnScheduleTagFileNameTxt().getText(),
                   scheduleTagPanelFormMerger.getSsnScheduleTagVDOFileNameTxt().getText());
                       
               
               
           }
               
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return retVal;
   }
   
   
   public List<SSNScheduleListForm> getEventList(SSNScheduleTagPanelModel scheduleTagPanelModel) throws SQLException{
       if(scheduleTagPanelModel instanceof SSNScheduleTagPanelModel) {
            scheduleTagDAO = new SSNDao();
            setScheduleTagDAO(scheduleTagDAO);
       }
            List<SSNScheduleListForm> eventListFormList = scheduleTagDAO.getEventTableList();
        return eventListFormList;
   }

   public List<SSNScheduleListForm> getSubTagEventList(SSNScheduleTagPanelModel scheduleTagPanelModel,SSNScheduleTagPanelForm scheduleTagPanelFormMerger) throws SQLException{
       if(scheduleTagPanelModel instanceof SSNScheduleTagPanelModel) {
            scheduleTagDAO = new SSNDao();
            setScheduleTagDAO(scheduleTagDAO);
       } 
       List<SSNScheduleListForm> eventListFormList = scheduleTagDAO.getSubTagEventTableList(SSNScheduleTagPanelForm.getGlobalParentTitleName());
        return eventListFormList;
   }
   
   public HashMap getScheduleDays() throws SQLException, ParseException{
             // int tagId =   getTagId();
              List<SSNScheduleListForm> eventScheduleList= scheduleTagDAO.getEventScheduleDaysWithoutTagId();
              HashMap<String,List> eventScheduleMap = new HashMap<String,List>();
              String key="";
              int ctr=0;
              List<Integer> scheduleDayList= null;
              List<Integer> scheduleDayListAxMonth= null;
              List<Integer> scheduleDayListAxMonthEnd= null;
              for(SSNScheduleListForm item : eventScheduleList){
                  
               scheduleDayList= new ArrayList<Integer>();
               
               Date startDate =  item.getSsnfromDate();
               Date endDate = item.getSsntoDate();
               int startDay = startDate.getDate();
               int stDateTemp = startDay;
               int endDay = endDate.getDate();
               int stMonth  = startDate.getMonth();
               int endMonth = endDate.getMonth();
               //key=  getKey(startDate);
               if(stMonth==endMonth){
                    key=  getKey(startDate);
                    for(; startDay<=endDay;startDay++){
                        scheduleDayList.add(startDay);
                    }
                    eventScheduleMap.put(key+ctr, scheduleDayList);
                 
               }
               if(endMonth>stMonth){
                   scheduleDayListAxMonth= new ArrayList<Integer>();
                   scheduleDayListAxMonthEnd= new ArrayList<Integer>();
                    key=  getKey(startDate);
                    for(;stDateTemp<=getDaysOfMonth(startDate.getMonth(),startDate.getYear());stDateTemp++){
                        scheduleDayListAxMonth.add(stDateTemp);
                    }
                  eventScheduleMap.put(key+ctr, scheduleDayListAxMonth);
                  int dtOfEndDate=endDate.getDate();
                  key=getKey(endDate);
                  for(int tempCtr=1;tempCtr<=dtOfEndDate;tempCtr++){
                      scheduleDayListAxMonthEnd.add(tempCtr);
                  }
                  eventScheduleMap.put(key+ctr, scheduleDayListAxMonthEnd);
                  
               }
                 ctr++;
              }
        return  eventScheduleMap;
                                
    }
   
   public boolean isUpdateTagOverLap(Date startDateIn,String startTimeIn,Date endDateIn,String endTimeIn,String presentTag) throws ParseException, SQLException{

       
        int tagId       = SSNHomeForm.tagId;
        int SubtagId       = SSNHomeForm.subTagId;
        SimpleDateFormat dateFormat =   new SimpleDateFormat("MM-dd-yyyy HH:mm");
        SimpleDateFormat dateFormat1 =   new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        
        startDateIn   =   dateFormat.parse(dateFormat1.format(startDateIn)+" "+startTimeIn);
        endDateIn   =   dateFormat.parse(dateFormat1.format(endDateIn)+" "+endTimeIn);
        
        List<SSNScheduleListForm> eventScheduleList = null;
        if(presentTag.equalsIgnoreCase("Preset Tags"))
        {
            eventScheduleList = scheduleTagDAO.getScheduledChildList(tagId);
                for(SSNScheduleListForm item : eventScheduleList){

                    Date startDate = dateFormat.parse(dateFormat1.format(item.getSsnfromDate()) + " " + item.getSsnStartTime());
                    Date endDate = dateFormat.parse(dateFormat1.format(item.getSsntoDate()) + " " + item.getSsnEndTime());
                   
                    if (!(startDateIn.before(startDate) && startDateIn.before(endDate)) && !(endDateIn.after(endDate) && endDateIn.after(startDate))) {
                        SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with  sub-event. Please choose another date/time";
                         return true;
                    } 
                    
                }
             eventScheduleList = scheduleTagDAO.getOtherScheduledParent(tagId);
                
        }
        else
        {
          eventScheduleList = scheduleTagDAO.getScheduledParent(tagId);
            for(SSNScheduleListForm item : eventScheduleList){
               
            Date startDate =  dateFormat.parse(dateFormat1.format(item.getSsnfromDate())+" "+item.getSsnStartTime()); 
            Date endDate =dateFormat.parse(dateFormat1.format(item.getSsntoDate())+" "+item.getSsnEndTime()); 
           
            if((startDateIn.after(startDate) && startDateIn.before(endDate)) && (endDateIn.after(startDate) && endDateIn.before(endDate))){
                eventScheduleList = scheduleTagDAO.getOtherScheduledChildList(tagId,SubtagId);
            }
            else
            {
                SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with parent-event. Please choose another date/time";
                return true;   
            }
        }  
        }
        for(SSNScheduleListForm item : eventScheduleList){
               
            Date startDate =  dateFormat.parse(dateFormat1.format(item.getSsnfromDate())+" "+item.getSsnStartTime()); 
            Date endDate =dateFormat.parse(dateFormat1.format(item.getSsntoDate())+" "+item.getSsnEndTime()); 
            
            if((startDateIn.compareTo(startDate)==0 || startDateIn.compareTo(endDate)==0 || (startDateIn.after(startDate) && startDateIn.before(endDate))) || (endDateIn.compareTo(startDate)==0 || endDateIn.compareTo(endDate)==0 || (endDateIn.after(startDate) && endDateIn.before(endDate)))|| ((startDate.after(startDateIn) && startDate.before(endDateIn)) || ((endDate.after(startDateIn) && endDate.before(endDateIn))))) {
                if(presentTag.equalsIgnoreCase("Preset Tags"))
                SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with another parent-event. Please choose another date/time";
                else
                SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with another sub-event. Please choose another date/time";
                return true;
            }
        }
       return false;
   }

   public boolean isNewTagOverLap(Date startDateIn,String startTimeIn,Date endDateIn,String endTimeIn,String presentTag) throws ParseException, SQLException{

       
       int tagId = SSNHomeForm.tagId;
       SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
       SimpleDateFormat dateFormat1 = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);

       startDateIn = dateFormat.parse(dateFormat1.format(startDateIn) + " " + startTimeIn);
       endDateIn = dateFormat.parse(dateFormat1.format(endDateIn) + " " + endTimeIn);

       List<SSNScheduleListForm> eventScheduleList = null;
       if (presentTag.equalsIgnoreCase("Preset Tags")) {
           eventScheduleList = scheduleTagDAO.getEventScheduleDaysWithTagId();
       } else {
           eventScheduleList = scheduleTagDAO.getScheduledParent(tagId);
           for (SSNScheduleListForm item : eventScheduleList) {

               Date startDate = dateFormat.parse(dateFormat1.format(item.getSsnfromDate()) + " " + item.getSsnStartTime());
               Date endDate = dateFormat.parse(dateFormat1.format(item.getSsntoDate()) + " " + item.getSsnEndTime());

               if (((startDateIn.after(startDate) && startDateIn.before(endDate)) && (endDateIn.after(startDate) && endDateIn.before(endDate))) || (((startDate.after(startDateIn))&& (startDate.before(endDateIn)))&&((endDate.after(startDateIn)&&(endDate.before(endDateIn)))))){
                   eventScheduleList = scheduleTagDAO.getScheduledChildList(tagId);
               } else {
                   SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with parent-event. Please choose another date/time";
                   return true;
               }
           }
       }

       for (SSNScheduleListForm item : eventScheduleList) {

           Date startDate = dateFormat.parse(dateFormat1.format(item.getSsnfromDate()) + " " + item.getSsnStartTime());
           Date endDate = dateFormat.parse(dateFormat1.format(item.getSsntoDate()) + " " + item.getSsnEndTime());
           if ((startDateIn.compareTo(startDate) == 0 || startDateIn.compareTo(endDate) == 0 || (startDateIn.after(startDate) && startDateIn.before(endDate))) || (endDateIn.compareTo(startDate) == 0 || endDateIn.compareTo(endDate) == 0 || (endDateIn.after(startDate) && endDateIn.before(endDate)))|| ((startDate.after(startDateIn) && startDate.before(endDateIn)) || ((endDate.after(startDateIn) && endDate.before(endDateIn))))) {
               if (presentTag.equalsIgnoreCase("Preset Tags")) {
                   SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with another parent-event. Please choose another date/time";
               } else {
                   SSNScheduleTagPanelForm.ssnScheduleTagErronMsg = "Conflicts with another sub-event. Please choose another date/time";
               }
               return true;
           }
       }
       return false;
   }
   
   private String getKey(Date dateValue){
         
         final DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
         String dateString = df.format(dateValue);
         String key="k"+dateString.substring(dateString.length()-4, dateString.length())+(Integer.parseInt(new SimpleDateFormat("MM").format(dateValue)));
         return key;
     }
   
   public boolean deleteSchedule(String pnlName){
        boolean flag    =   false;
            try {
                if(pnlName.equalsIgnoreCase("Preset Tags"))
                    flag    =    scheduleTagDAO.deleteParentSchedule(homeForm.tagId,homeForm.subTagId);
                else
                    flag    =    scheduleTagDAO.deleteChildSchedule(homeForm.tagId,homeForm.subTagId);
                
            } catch (SQLException ex) {
                Logger.getLogger(SSNScheduleTagPanelModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return flag;
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
     * @return the scheduleTagController
     */
    public SSNScheduleTagController getScheduleTagController() {
        return scheduleTagController;
    }

    /**
     * @param scheduleTagController the scheduleTagController to set
     */
    private void setScheduleTagController(SSNScheduleTagController scheduleTagController) {
        this.scheduleTagController = scheduleTagController;
    }

    /**
     * @return the scheduleTagDAO
     */
    public SSNDao getScheduleTagDAO() {
        return scheduleTagDAO;
    }

    /**
     * @param scheduleTagDAO the scheduleTagDAO to set
     */
    public void setScheduleTagDAO(SSNDao scheduleTagDAO) {
        this.scheduleTagDAO = scheduleTagDAO;
    }

    public void updateSchedule(String nodetype,String txtTitle, String album, String txtLocation, String txtKeyword, String txtComments, Date stDate, Date endDate,String startTime, String endTime, String photoGrapher, String videoPrefix, String imagePrefix) throws SQLException {
       try {
           scheduleTagDAO.updateSchedule(nodetype,txtTitle,album,txtLocation,txtKeyword,txtComments,stDate,endDate,startTime,endTime,photoGrapher,videoPrefix,imagePrefix,SSNHomeForm.tagId,SSNHomeForm.subTagId);
       } catch (ParseException ex) {
           ex.printStackTrace();;
       }
    }
    public int getDaysOfMonth(int month, int year){
        int iYear = year;
        int iMonth = month;
        int iDay = 1;

        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

        // Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); 
        return daysInMonth;
    }
    private int getTagId() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return scheduleTagController.getScheduleTagPanelFormMerger().getSsnTagIdLabel().getText().equals("")?0:Integer.parseInt(scheduleTagController.getScheduleTagPanelFormMerger().getSsnTagIdLabel().getText());
    }
    
}
