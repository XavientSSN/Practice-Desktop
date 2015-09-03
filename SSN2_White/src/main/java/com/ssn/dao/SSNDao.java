package com.ssn.dao;

import com.ssn.app.loader.SSNConstants;
import com.ssn.helper.SSNHelper;
import com.ssn.model.SSNSocialModel;
import com.ssn.model.TaggedFace;
import com.ssn.schedule.SSNScheduleListForm;
import com.ssn.ui.custom.component.SSNGalleryMetaData;
import com.ssn.schedule.SSNScheduleTagPanelForm;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JSpinner;
import org.apache.log4j.Logger;


/**
 *
 * @author pkumar2
 */
public class SSNDao {

    final private static Connection c = getConnection();
    final private static Logger logger = Logger.getLogger(SSNDao.class);
    final private static SimpleDateFormat dateFormat = new SimpleDateFormat();
    
    /**
     * Creates and returns database connection
     * @return Connection
     */
    public static Connection getConnection() {
        Connection connect = null;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
        }

        try {
            connect = DriverManager.getConnection("jdbc:sqlite:" + SSNHelper.getSsnDBDirPath() + "OurHiveDatabase.sqlite");
            //connect = DriverManager.getConnection("jdbc:sqlite:D:\\SSN_WORKSPACE\\OurHiveDatabase.sqlite");
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }

        return connect;
    }

   
    /**
     * Saves the metadata of Scheduled tag in the database 
     * @param fileName
     * @param mediaInfo
     * @param type
     * @return boolean
     * @throws SQLException
     */
    public static boolean isTableExist(String tableName) throws SQLException{
       logger.info("inside isTableExist()");
        boolean result  =   false;
        String sql      =   "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='"+tableName+"'";
        int count       =   0;
        ResultSet   rs  =   null;
        Statement pStmt = null;
      
        try {
            c.setAutoCommit(result);
            pStmt = c.createStatement();

            rs = pStmt.executeQuery(sql);
            if(rs.next()){
                count = Integer.parseInt(rs.getString(1));
            }
           
            if (count > 0)
                result = true;
            
             logger.info("Exit isTableExist()");
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }finally{
            if(pStmt != null)
                pStmt.close();
        }
        return  result;
        
       
    }
    
    public static boolean insertScheduledTagMetaData(String fileName, SSNScheduleListForm mediaInfo, String type) throws SQLException {
        logger.info("inside insertScheduledTagMetaData()");
        boolean result  =   false;
        String sql = "INSERT INTO SSNMedia (ID,NAME,COMMENT,RATING,TYPE,CREATED,MODIFIED,LOCATION,PHOTOGRAPHER) VALUES(? ,'" + fileName + "','" + mediaInfo.getSsnComent() + "','" + mediaInfo.getSsnRatings() + "','" + type + "','" + mediaInfo.getSsncreateDate() + "','" + mediaInfo.getSsnModificationon() + "','" + mediaInfo.getSsnLocation().trim() + "','" + mediaInfo.getSsnPhotoGrapher().trim() + "')";
        int count = 0;
        PreparedStatement pStmt2 = null;
        try {
            c.setAutoCommit(result);
            pStmt2 = c.prepareStatement(sql);

            count = pStmt2.executeUpdate();
            c.commit();
            if (count > 0)
                result = true;
            
            
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }finally{
            if(pStmt2 != null)
                pStmt2.close();
        }
        return  result;
      }

    
    /**
     * Get Media info of an image is already schedule is done
     * @param searchDate
     * @return SSNScheduleListForm
     * @throws SQLException
     */
    
    public static SSNScheduleListForm getScheduleMedia(String searchDate) throws SQLException {
        logger.info("inside getScheduleMedia()");
        SSNScheduleListForm sSNScheduleListForm = null;
        Statement stmt = c.createStatement();
        String childQuery = "SELECT  PARENTTAGTITLE,SUBTAGPLACE,SUBTAGKEYWORDS,SUBTAGCOMMENT,SUBTAGRATINGS,SUBTAGALBUM,SUBTAGTITLE,"
                + "SUBPHOTOGRAPHER,SUBVIDEOPREFIX,SUBIMAGEPREFIX,"
                + "SUBTAGFROMDATE FROM SSNSCHEDULESUBTAG "
                + "WHERE PARENTTAGTITLE =( "
                + "SELECT DISTINCT TITLE from SSNSCHEDULETAG WHERE ('"+searchDate+"' BETWEEN FROMDATE AND TODATE))";
        
        //  ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG WHERE FROMDATE='"+searchDate+"'");
        try{
            ResultSet rs = stmt.executeQuery(childQuery);
            //  rs=null; in case there is not child
            //int row=rs.getRow();
            //if (row>0) {
               if(rs.next()){
               do{
                    sSNScheduleListForm = new SSNScheduleListForm();
                    sSNScheduleListForm.setSsnTitle(rs.getString("SUBTAGTITLE"));
                    sSNScheduleListForm.setSsnkeyWords(rs.getString("SUBTAGKEYWORDS"));
                    sSNScheduleListForm.setSsnComent(rs.getString("SUBTAGCOMMENT"));
                    sSNScheduleListForm.setSsnAlbum(rs.getString("SUBTAGALBUM"));
                    sSNScheduleListForm.setSsnLocation(rs.getString("SUBTAGPLACE"));
                    sSNScheduleListForm.setSsnPhotoGrapher(rs.getString("SUBPHOTOGRAPHER"));
                    sSNScheduleListForm.setSsnImagePrefix(rs.getString("SUBIMAGEPREFIX"));
                    sSNScheduleListForm.setSsnVideoPrefix(rs.getString("SUBVIDEOPREFIX"));
                    int rating = (Integer.valueOf(rs.getString("SUBTAGRATINGS")) != null ? (Integer.valueOf(rs.getString("SUBTAGRATINGS"))) : 0);
                    sSNScheduleListForm.setSsnRatings(rating);
                } while (rs.next()); 
                rs.close();
            } else {
                //ResultSet rsParent = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG WHERE FROMDATE LIKE '" + searchDate + "'");
                ResultSet rsParent = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG WHERE ('"+searchDate+"' BETWEEN FROMDATE AND TODATE)");
                
                while (rsParent.next()) {
                    sSNScheduleListForm = new SSNScheduleListForm();
                    sSNScheduleListForm.setSsnTitle(rsParent.getString("TITLE"));
                    sSNScheduleListForm.setSsnComent(rsParent.getString("COMMENT"));
                    sSNScheduleListForm.setSsnLocation(rsParent.getString("PLACE"));
                    sSNScheduleListForm.setSsnkeyWords(rsParent.getString("KEYWORDS"));
                    sSNScheduleListForm.setSsnAlbum(rsParent.getString("ALBUM"));
                    sSNScheduleListForm.setSsnPhotoGrapher(rs.getString("PHOTOGRAPHER"));
                    sSNScheduleListForm.setSsnImagePrefix(rs.getString("IMAGEPREFIX"));
                    sSNScheduleListForm.setSsnVideoPrefix(rs.getString("VIDEOPREFIX"));
                    int rating = (Integer.valueOf(rsParent.getString("RATINGS")) != null ? (Integer.valueOf(rsParent.getString("RATINGS"))) : 0);
                    sSNScheduleListForm.setSsnRatings(rating);
                }
                rsParent.close();
            }
        }catch(SQLException ee){
            logger.error(ee);
        }finally{
            stmt.close();
        }
        return sSNScheduleListForm;

    }

    /**
     * Inserts data in parent scheduled tag table
     * @param ent
     * @param opt
     * @param duration
     * @param ratings
     * @param subTags
     * @param creationDate
     * @param startDate
     * @param modificationDate
     * @param endDate
     * @param comment
     * @param keywords
     * @param locationName
     * @param titleName
     * @param album
     * @param voiceNotePath
     * @return int
     * @throws SQLException
     */
    public int insertParentScheduleTagTable(int ent, int opt, int duration, int ratings, String subTags, Date creationDate, Date startDate, Date modificationDate, Date endDate, String comment, String keywords, String locationName, String titleName, String album,JSpinner jSpinerStartTime, JSpinner jSpinerEndTime, String voiceNotePath,String photoGrapher, String imagePrefix, String videoPrefix) throws SQLException {
        logger.info("inside insertParentScheduleTagTable()");
        int count = 0;
        String startTime = null;
        String endTime = null;
        try {
            
            startTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerStartTime.getValue().toString());
            endTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerEndTime.getValue().toString());
                
            SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
            String creationDateStr = sdf.format(creationDate);
            String endDateStr = sdf.format(endDate);
            String startDateStr = sdf.format(startDate);
            String modificationDateStr = sdf.format(modificationDate);
            String sql = "INSERT INTO SSNSCHEDULETAG (TAGID,ENT, OPT, DURATION, RATINGS, SUBTAGS, CREATEDON, FROMDATE, MODIFIEDON, TODATE, COMMENT, KEYWORDS, PLACE, TITLE, ALBUM, VOICENOTEPATH, STARTTIME, ENDTIME, PHOTOGRAPHER, VIDEOPREFIX, IMAGEPREFIX) VALUES ( ? ," + ent + ", " + opt + ", " + duration + "," + ratings + ", '" + subTags + "', '" + creationDateStr + "', '" + startDateStr + "', '" + modificationDateStr + "', '" + endDateStr + "', '" + comment + "', '" + keywords + "', '" + locationName + "', '" + titleName + "', '" + album + "' , '" + voiceNotePath + "', '" + startTime + "','" + endTime + "', '"+ photoGrapher +"','"+ videoPrefix +"','"+ imagePrefix +"')";
            c.setAutoCommit(false);
            // Execute SQLStatement
            try (PreparedStatement pStmt = c.prepareStatement(sql)) {
                // Execute SQLStatement
                count = pStmt.executeUpdate();
                c.commit();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return count;
    }

    /**
     * Inserts data in scheduled tag table
     * @param ent
     * @param opt
     * @param duration
     * @param ratings
     * @param subTags
     * @param creationDate
     * @param startDate
     * @param modificationDate
     * @param endDate
     * @param comment
     * @param keywords
     * @param locationName
     * @param titleName
     * @param album
     * @param voiceNotePath
     * @return int
     * @throws SQLException
     */
//    public int insertScheduleTagTable(int ent, int opt, int duration, int ratings, String subTags, Date creationDate, Date startDate, Date modificationDate, Date endDate, String comment, String keywords, String locationName, String titleName, String album, String voiceNotePath) throws SQLException {
//        int count = 0;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
//            String creationDateStr = sdf.format(creationDate);
//            String endDateStr = sdf.format(endDate);
//            String startDateStr = sdf.format(startDate);
//            String modificationDateStr = sdf.format(modificationDate);
//
//            String sql = "INSERT INTO SSNSCHEDULETAG (TAGID,ENT, OPT, DURATION, RATINGS, SUBTAGS, CREATEDON, FROMDATE, MODIFIEDON, TODATE, COMMENT, KEYWORDS, PLACE, TITLE, ALBUM, VOICENOTEPATH) VALUES ( ? ," + ent + ", " + opt + ", " + duration + "," + ratings + ", '" + subTags + "', '" + creationDateStr + "', '" + startDateStr + "', '" + modificationDateStr + "', '" + endDateStr + "', '" + comment + "', '" + keywords + "', '" + locationName + "', '" + titleName + "', '" + album + "' , '" + voiceNotePath + "')";
//            c.setAutoCommit(false);
//            PreparedStatement pStmt = c.prepareStatement(sql);
//
//            // Execute SQLStatement
//            count = pStmt.executeUpdate();
//            c.commit();
//            pStmt.close();
//        } catch (SQLException e) {
//            logger.error(e.getMessage());
//        }
//
//        return count;
//    }

    /**
     * Inserts data in scheduled sub tag  table
     * @param ent
     * @param opt
     * @param duration
     * @param ratings
     * @param parentTagTitle
     * @param creationDate
     * @param startDate
     * @param modificationDate
     * @param endDate
     * @param comment
     * @param keywords
     * @param locationName
     * @param titleName
     * @param album
     * @param voiceNotePath
     * @return
     * @throws SQLException
     */
//    public int insertScheduleSubTagTable(int ent, int opt, int duration, int ratings, String parentTagTitle, Date creationDate, Date startDate, Date modificationDate, Date endDate, String comment, String keywords, String locationName, String titleName, String album,JSpinner jSpinerStartTime,JSpinner jSpinerEndTime, String voiceNotePath, String photoGrapher, String imagePrefix, String videoPrefix) throws SQLException {
//
//        int count = 0;
//        String startTime = null;
//        String endTime = null;
//        
//        startTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerStartTime.getValue().toString());
//        endTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerEndTime.getValue().toString());
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
//        String creationDateStr = sdf.format(creationDate);
//        String endDateStr = sdf.format(endDate);
//        String startDateStr = sdf.format(startDate);
//        String modificationDateStr = sdf.format(modificationDate);
//
//        String sql = "INSERT INTO [SSNSCHEDULESUBTAG] ([ENT], [OPT], [SUBTAGDURATION], [SUBTAGRATINGS], [PARENTTAGTITLE], [SUBTAGCREATEDON], [SUBTAGFROMDATE], [SUBTAGMODIFIEDON], [SUBTAGTODATE], [SUBTAGCOMMENT], [SUBTAGKEYWORDS], [SUBTAGPLACE], [SUBTAGTITLE], [SUBTAGVOICENOTEPATH], [SUBTAGALBUM],[SUBSTARTTIME],[SUBENDTIME], [SUBPHOTOGRAPHER] ,[SUBVIDEOPREFIX],[SUBIMAGEPREFIX]) VALUES "
//                + "                 ( " + ent + ", " + opt + ", " + duration + "," + ratings + ", '" + parentTagTitle + "', '" + creationDateStr + "', '" + startDateStr + "', '" + modificationDateStr + "', '" + endDateStr + "', '" + comment + "', '" + keywords + "', '" + locationName + "', '" + titleName + "', '" + voiceNotePath + "' , '" + album + "', '" +startTime+ "','" +endTime+ "', '" +photoGrapher+ "','" +videoPrefix+ "','" +imagePrefix+ "')";
//        c.setAutoCommit(false);
//        PreparedStatement pStmt = c.prepareStatement(sql);
//
//        // Execute SQLStatement
//        count = pStmt.executeUpdate();
//        c.commit();
//        pStmt.close();
//        return count;
//
//    }
//    
    public int insertScheduleSubTagTable(int parentId,int ent, int opt, int duration, int ratings, String parentTagTitle, Date creationDate, Date startDate, Date modificationDate, Date endDate, String comment, String keywords, String locationName, String titleName, String album,JSpinner jSpinerStartTime,JSpinner jSpinerEndTime, String voiceNotePath, String photoGrapher, String imagePrefix, String videoPrefix) throws SQLException {

        int count = 0;
        String startTime = null;
        String endTime = null;
        startTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerStartTime.getValue().toString());
        endTime=SSNScheduleTagPanelForm.getFormatedTime(jSpinerEndTime.getValue().toString());
        
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        String creationDateStr = sdf.format(creationDate);
        String endDateStr = sdf.format(endDate);
        String startDateStr = sdf.format(startDate);
        String modificationDateStr = sdf.format(modificationDate);

        String sql = "INSERT INTO [SSNSCHEDULESUBTAG] ([ENT], [OPT], [SUBTAGDURATION], [SUBTAGRATINGS], [PARENTTAGTITLE], [SUBTAGCREATEDON], [SUBTAGFROMDATE], [SUBTAGMODIFIEDON], [SUBTAGTODATE], [SUBTAGCOMMENT], [SUBTAGKEYWORDS], [SUBTAGPLACE], [SUBTAGTITLE], [SUBTAGVOICENOTEPATH], [SUBTAGALBUM],[SUBSTARTTIME],[SUBENDTIME], [SUBPHOTOGRAPHER] ,[SUBVIDEOPREFIX],[SUBIMAGEPREFIX],[SUBTAGID],[TAGID]) VALUES "
                + "                 ( " + ent + ", " + opt + ", " + duration + "," + ratings + ", '" + parentTagTitle + "', '" + creationDateStr + "', '" + startDateStr + "', '" + modificationDateStr + "', '" + endDateStr + "', '" + comment + "', '" + keywords + "', '" + locationName + "', '" + titleName + "', '" + voiceNotePath + "' , '" + album + "', '" +startTime+ "','" +endTime+ "', '" +photoGrapher+ "','" +videoPrefix+ "','" +imagePrefix+ "', ?, "+parentId+")";
        c.setAutoCommit(false);
        PreparedStatement pStmt = c.prepareStatement(sql);

        // Execute SQLStatement
        count = pStmt.executeUpdate();
        c.commit();
        pStmt.close();
        return count;

    }
    

    
    /**
     * Returns application version validation
     */
    public static boolean isVersionChanged() throws SQLException{
        boolean result  =   false;
        Statement stmt  =   null;
        ResultSet rs    =   null;
        try{
            StringBuffer sql =  new StringBuffer("SELECT * From SSNVERSION");
            stmt             =  c.createStatement();
            rs               =  stmt.executeQuery(sql.toString());
            
            if(rs.next()){
                if(SSNConstants.APPLICATION_VERSION > rs.getDouble(1)){
                    result = true;
                    logger.info("Version Updated");
                }
            }
        }catch(Exception ee){
            ee.printStackTrace();
            logger.error(ee);
        }finally{
            rs.close();
            stmt.close();
        }
        return result;
    }
    
    /**
     * Returns application version validation
     */
    public static boolean upgradeApplicationDatabaseAndVersion() throws SQLException{
        boolean result  =   false;
        Statement stmt  =   null;
        ResultSet rs    =   null;
        try{
            StringBuffer sql =  new StringBuffer("SELECT * From SSNVERSION");
            stmt             =  c.createStatement();
            rs               =  stmt.executeQuery(sql.toString());
            
            if(rs.next()){
                if(SSNConstants.APPLICATION_VERSION > rs.getDouble(1)){
                    result = true;
                }
            }
        }catch(Exception ee){
            ee.printStackTrace();
            logger.error(ee);
        }finally{
            rs.close();
            stmt.close();
        }
        return result;
    }
    /**
     * Update the parent schedule tag on the basis of title and album
     * @param SSNTitle
     * @param SSNAlbum
     * @return
     * @throws SQLException
     */
    public int updateParentSSNScheduleTagTable(String SSNTitle, String SSNAlbum) throws SQLException {
        
        String updateSql = "Update SSNSCHEDULETAG set SUBTAGS='true' where TITLE = '" + SSNTitle + "' and ALBUM = '" + SSNAlbum + "';";
        c.setAutoCommit(false);
        PreparedStatement pStmt = c.prepareStatement(updateSql);
        int impactedCount = pStmt.executeUpdate();
        c.commit();
        pStmt.close();
        return impactedCount;
    }

    /**
     * Retrieves all the events from database
     * @return
     * @throws SQLException
     */
    public static List<SSNScheduleListForm> getEventTableList() throws SQLException {
        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        String query = "SELECT * FROM SSNSCHEDULETAG order by FROMDATE,STARTTIME";
        
        ResultSet rs = stmt.executeQuery(query);
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);

        while (rs.next()) {
            try {
                SSNScheduleListForm eventListForm = new SSNScheduleListForm();
                eventListForm.setSsnTitle(rs.getString("TITLE"));
                eventListForm.setSsnAlbum(rs.getString("ALBUM"));
                eventListForm.setSsnLocation(rs.getString("PLACE"));
                eventListForm.setSsnkeyWords(rs.getString("KEYWORDS"));
                //eventListForm.setSsnComent(rs.getString("COMMENT"));
                eventListForm.setSsntoDate(sdf.parse(rs.getString("TODATE")));
                eventListForm.setSsnModificationon(rs.getString("MODIFIEDON"));
                eventListForm.setSsnfromDate(sdf.parse(rs.getString("FROMDATE")));
                eventListForm.setSsncreateDate(sdf.parse(rs.getString("CREATEDON")));
                eventListForm.setSsnSubtags(rs.getString("SUBTAGS"));
                eventListForm.setSsnRatings(Integer.parseInt(rs.getString("RATINGS")));
                eventListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("DURATION")));
                eventListForm.setSsnOpt(Integer.parseInt(rs.getString("OPT")));
                eventListForm.setSsnEnt(Integer.parseInt(rs.getString("ENT")));
                eventListForm.setSsnVoiceNotePath(rs.getString("VOICENOTEPATH"));

                eventListFormList.add(eventListForm);
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
        }
        stmt.close();
        return eventListFormList;

    }

    /**
     * Retrieves the parent event list
     * @return
     * @throws SQLException
     */
    public static List<SSNScheduleListForm> getParentEventList() throws SQLException {
        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG order by FROMDATE,STARTTIME");
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");

        while (rs.next()) {
            try {
                SSNScheduleListForm eventListForm = new SSNScheduleListForm();
                eventListForm.setTagId(rs.getString("TAGID") != null ? rs.getInt("TAGID") : 0);
                eventListForm.setSsnTitle(rs.getString("TITLE") != null ? rs.getString("TITLE") : "");
                eventListForm.setSsnAlbum(rs.getString("ALBUM") != null ? rs.getString("ALBUM") : "");
                eventListForm.setSsnLocation(rs.getString("PLACE") != null ? rs.getString("PLACE") : "");
                eventListForm.setSsnkeyWords(rs.getString("KEYWORDS") != null ? rs.getString("KEYWORDS") : "");
                eventListForm.setSsnComent(rs.getString("COMMENT") != null ? rs.getString("COMMENT") : "");
                eventListForm.setSsnComent(rs.getString("COMMENT") != null ? "" : "");
                eventListForm.setSsntoDate(sdf.parse(rs.getString("TODATE") != null ? rs.getString("TODATE") : ""));
                eventListForm.setSsnModificationon(rs.getString("MODIFIEDON") != null ? rs.getString("MODIFIEDON") : "");
                eventListForm.setSsnStrfromDate(rs.getString("FROMDATE") != null ? rs.getString("FROMDATE") : "");
                eventListForm.setSsnStrtoDate(rs.getString("TODATE") != null ? rs.getString("TODATE") : "");
                eventListForm.setSsncreateDate(sdf.parse(rs.getString("CREATEDON") != null ? rs.getString("CREATEDON") : ""));
                eventListForm.setSsnSubtags(rs.getString("SUBTAGS") != null ? rs.getString("SUBTAGS") : "");
                eventListForm.setSsnRatings(Integer.parseInt(rs.getString("RATINGS")));
                eventListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("DURATION")));
                eventListForm.setSsnOpt(Integer.parseInt(rs.getString("OPT")));
                eventListForm.setSsnEnt(Integer.parseInt(rs.getString("ENT")));
                eventListForm.setSsnVoiceNotePath(rs.getString("VOICENOTEPATH"));
                eventListForm.setSsnStartTime(rs.getString("STARTTIME"));
                eventListForm.setSsnEndTime(rs.getString("ENDTIME"));
                eventListFormList.add(eventListForm);
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }

    /**
     * Retrieves the child event list
     * @param title
     * @return
     * @throws SQLException
     */
    public static List<SSNScheduleListForm> getChildEventList(String title) throws SQLException {
        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("select * from SSNSCHEDULESUBTAG where  PARENTTAGTITLE like '%" + title + "' order by SUBTAGFROMDATE,SUBSTARTTIME");
        
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);

        while (rs.next()) {
            try {
                SSNScheduleListForm eventListForm = new SSNScheduleListForm();
                eventListForm.setTagId(rs.getString("TAGID") != null ? rs.getInt("TAGID") :0);
                eventListForm.setSubTagId(rs.getString("SUBTAGID") != null ? rs.getInt("SUBTAGID") :0);
                eventListForm.setSsnTitle(rs.getString("PARENTTAGTITLE") != null ? rs.getString("PARENTTAGTITLE") : "");
                eventListForm.setSsnSubtags(rs.getString("SUBTAGTITLE") != null ? rs.getString("SUBTAGTITLE") : "");
                eventListForm.setSsnAlbum(rs.getString("SUBTAGALBUM") != null ? rs.getString("SUBTAGALBUM") : "");
                eventListForm.setSsnLocation(rs.getString("SUBTAGPLACE") != null ? rs.getString("SUBTAGPLACE") : "");
                eventListForm.setSsnkeyWords(rs.getString("SUBTAGKEYWORDS") != null ? rs.getString("SUBTAGKEYWORDS") : "");
                //eventListForm.setSsnComent(rs.getString("SUBTAGCOMMENT") != null ? rs.getString("SUBTAGCOMMENT") : "");
              //  eventListForm.setSsnComent(rs.getString("SUBTAGCOMMENT") != null ? "" : "");
                eventListForm.setSsnComent("");
                eventListForm.setSsntoDate(sdf.parse(rs.getString("SUBTAGTODATE")));
                eventListForm.setSsnStrtoDate(sdf.format(sdf.parse(rs.getString("SUBTAGTODATE") != null ? rs.getString("SUBTAGTODATE") : "")));
                eventListForm.setSsnStrfromDate(sdf.format(sdf.parse(rs.getString("SUBTAGFROMDATE") != null ? rs.getString("SUBTAGFROMDATE") : "")));
                eventListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("SUBTAGDURATION")));
                eventListForm.setSsnStartTime(rs.getString("SUBSTARTTIME"));
                eventListForm.setSsnEndTime(rs.getString("SUBENDTIME"));

                eventListFormList.add(eventListForm);
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }

    
    
     /**
     * Retrieves the child event list
     * @param title
     * @return
     * @throws SQLException
     */
    public static List<SSNScheduleListForm> getChildEventList(int tagId) throws SQLException {
        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("select * from SSNSCHEDULESUBTAG where  TAGID =" + tagId +" order by SUBTAGFROMDATE,SUBSTARTTIME");
        
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);

        while (rs.next()) {
            try {
                SSNScheduleListForm eventListForm = new SSNScheduleListForm();
                eventListForm.setTagId(rs.getString("TAGID") != null ? rs.getInt("TAGID") :0);
                eventListForm.setSubTagId(rs.getString("SUBTAGID") != null ? rs.getInt("SUBTAGID") :0);
                eventListForm.setSsnTitle(rs.getString("PARENTTAGTITLE") != null ? rs.getString("PARENTTAGTITLE") : "");
                eventListForm.setSsnSubtags(rs.getString("SUBTAGTITLE") != null ? rs.getString("SUBTAGTITLE") : "");
                eventListForm.setSsnAlbum(rs.getString("SUBTAGALBUM") != null ? rs.getString("SUBTAGALBUM") : "");
                eventListForm.setSsnLocation(rs.getString("SUBTAGPLACE") != null ? rs.getString("SUBTAGPLACE") : "");
                eventListForm.setSsnkeyWords(rs.getString("SUBTAGKEYWORDS") != null ? rs.getString("SUBTAGKEYWORDS") : "");
                //eventListForm.setSsnComent(rs.getString("SUBTAGCOMMENT") != null ? rs.getString("SUBTAGCOMMENT") : "");
              //  eventListForm.setSsnComent(rs.getString("SUBTAGCOMMENT") != null ? "" : "");
                eventListForm.setSsnComent("");
                eventListForm.setSsntoDate(sdf.parse(rs.getString("SUBTAGTODATE")));
                eventListForm.setSsnStrtoDate(sdf.format(sdf.parse(rs.getString("SUBTAGTODATE") != null ? rs.getString("SUBTAGTODATE") : "")));
                eventListForm.setSsnStrfromDate(sdf.format(sdf.parse(rs.getString("SUBTAGFROMDATE") != null ? rs.getString("SUBTAGFROMDATE") : "")));
                eventListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("SUBTAGDURATION")));
                eventListForm.setSsnStartTime(rs.getString("SUBSTARTTIME"));
                eventListForm.setSsnEndTime(rs.getString("SUBENDTIME"));

                eventListFormList.add(eventListForm);
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    /**
     * Retrieves the sub tag event details from database on the basis of 
     * sub tag title name
     * @param subTagTitle
     * @return
     * @throws SQLException
     */
    public List<SSNScheduleListForm> getSubTagEventTableList(String subTagTitle) throws SQLException {
        List<SSNScheduleListForm> eventListFormSubList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULESUBTAG where SUBTAGTITLE='" + subTagTitle + "' order by SUBTAGFROMDATE,SUBSTARTTIME");
        
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);

        while (rs.next()) {
            try {
                SSNScheduleListForm eventListFormSub = new SSNScheduleListForm();
                eventListFormSub.setSsnTitle(rs.getString("SUBTAGTITLE"));
                eventListFormSub.setSsnAlbum(rs.getString("SUBTAGALBUM"));
                eventListFormSub.setSsnLocation(rs.getString("SUBTAGPLACE"));
                eventListFormSub.setSsnkeyWords(rs.getString("SUBTAGKEYWORDS"));
                //eventListFormSub.setSsnComent(rs.getString("SUBTAGCOMMENT"));
                eventListFormSub.setSsnComent("");
                eventListFormSub.setSsntoDate(sdf.parse(rs.getString("SUBTAGTODATE")));
                eventListFormSub.setSsnModificationon(rs.getString("SUBTAGMODIFIEDON"));
                eventListFormSub.setSsnfromDate(sdf.parse(rs.getString("SUBTAGFROMDATE")));
                eventListFormSub.setSsncreateDate(sdf.parse(rs.getString("SUBTAGCREATEDON")));
                eventListFormSub.setSsnSubtags(rs.getString("PARENTTAGTITLE"));
                eventListFormSub.setSsnRatings(Integer.parseInt(rs.getString("SUBTAGRATINGS")));
                eventListFormSub.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("SUBTAGDURATION")));
                eventListFormSub.setSsnOpt(Integer.parseInt(rs.getString("OPT")));
                eventListFormSub.setSsnEnt(Integer.parseInt(rs.getString("ENT")));
                eventListFormSub.setSsnVoiceNotePath(rs.getString("SUBTAGVOICENOTEPATH"));

                eventListFormSubList.add(eventListFormSub);
            } catch (ParseException ex) {
                logger.error(ex.getMessage());
            }
        }
        rs.close();
        stmt.close();

        return eventListFormSubList;

    }

    /**
     * Retrieves the event detail from database on the basis of title
     * @param Title
     * @return
     */
    public ResultSet getEventForEditing(String Title) throws SQLException{

        ResultSet rs = null;
        Statement stmt = null;
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();

            rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG where PARENTTAGTITLE='" + Title + "'");
       

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }finally{
            rs.close();
            stmt.close();
        }
        
        return rs;
    }

    /**
     * Retrieves the  start and end time of schedule and then return the number of
     * days 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<SSNScheduleListForm> getEventScheduleDaysWithTagId() throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        String query    =   "SELECT * FROM SSNSCHEDULETAG";
        ResultSet rs = stmt.executeQuery(query);
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
 
            Date stDate = df.parse(rs.getString("FROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("TODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListForm.setSsnStartTime(rs.getString("STARTTIME"));
            eventListForm.setSsnEndTime(rs.getString("ENDTIME"));
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    
    /**
     * Retrieves the  ScheduledParent rective with ID 
     * 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<SSNScheduleListForm> getScheduledParent(int tagId) throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

       // String query    =   "SELECT * FROM SSNSCHEDULETAG";
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG Where TAGID = "+tagId);
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
 
            Date stDate = df.parse(rs.getString("FROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("TODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListForm.setSsnStartTime(rs.getString("STARTTIME"));
            eventListForm.setSsnEndTime(rs.getString("ENDTIME"));
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    
    public List<SSNScheduleListForm> getOtherScheduledParent(int tagId) throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

       // String query    =   "SELECT * FROM SSNSCHEDULETAG";
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG Where TAGID != "+tagId);
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
 
            Date stDate = df.parse(rs.getString("FROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("TODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListForm.setSsnStartTime(rs.getString("STARTTIME"));
            eventListForm.setSsnEndTime(rs.getString("ENDTIME"));
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    
    /**
     * Retrieves the  ScheduledchildList rective with TagID 
     * 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<SSNScheduleListForm> getScheduledChildList(int tagId) throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

       // String query    =   "SELECT * FROM SSNSCHEDULETAG";
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULESUBTAG Where TAGID = "+tagId);
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
 
            Date stDate = df.parse(rs.getString("SUBTAGFROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("SUBTAGTODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListForm.setSsnStartTime(rs.getString("SUBSTARTTIME"));
            eventListForm.setSsnEndTime(rs.getString("SUBENDTIME"));
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    
    public List<SSNScheduleListForm> getOtherScheduledChildList(int tagId,int subTagId) throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

       // String query    =   "SELECT * FROM SSNSCHEDULETAG";
        String query = "SELECT * FROM SSNSCHEDULESUBTAG Where TAGID = "+tagId +" and SUBTAGID !="+ subTagId;
        ResultSet rs = stmt.executeQuery(query);
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
 
            Date stDate = df.parse(rs.getString("SUBTAGFROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("SUBTAGTODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListForm.setSsnStartTime(rs.getString("SUBSTARTTIME"));
            eventListForm.setSsnEndTime(rs.getString("SUBENDTIME"));
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }

    /**
     * Retrieves the  start and end time of schedule and then return the number of
     * days 
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public List<SSNScheduleListForm> getEventScheduleDaysWithoutTagId() throws SQLException, ParseException {

        List<SSNScheduleListForm> eventListFormList = new ArrayList<>();
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNSCHEDULETAG order by FROMDATE,STARTTIME");
        DateFormat df = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        while (rs.next()) {
            SSNScheduleListForm eventListForm = new SSNScheduleListForm();
            Date stDate = df.parse(rs.getString("FROMDATE"));;
            eventListForm.setSsnfromDate(stDate);
            Date toDate = df.parse(rs.getString("TODATE"));;
            eventListForm.setSsntoDate(toDate);
            eventListFormList.add(eventListForm);
        }
        rs.close();
        stmt.close();
        return eventListFormList;

    }
    /**
     * Create media record in database 
     * @param mediaName
     * @param mediaComments
     * @param rating
     * @param location
     * @param type
     * @throws SQLException
     */
    public static void insertMediaTable(String mediaName, String mediaComments, String rating, String location, String type,String keyword,String photographer,String caption) throws SQLException {
          Statement stmt = null;
        try {
          
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "INSERT INTO SSNMedia (KEYWORD,PHOTOGRAPHER,NAME,COMMENT,RATING,LOCATION,TYPE,CAPTION,CREATED,MODIFIED) " + "VALUES ('" + keyword + "','"+ photographer + "','"+ mediaName + "','" + mediaComments + "','" + rating + "','" + location + "','" + type + "','" + caption + "','" + dateFormat.format(Calendar.getInstance().getTime()) + "','" + "" + "' )";
            
            stmt.executeUpdate(sql);

            
            c.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());

        }finally{
            stmt.close();
        }
    }
    
    public static SSNGalleryMetaData getExistingMetadata(String mediaName) throws SQLException
    {
         ResultSet rs=null;
         Statement stmt = null;
         SSNGalleryMetaData metadata=new SSNGalleryMetaData();
        try {
            stmt = c.createStatement();
            
             rs = stmt.executeQuery("SELECT * FROM SSNMedia where NAME='" + mediaName + "'");
            while(rs.next())
            {
              metadata.setUserComments(rs.getString("COMMENT"));
              //metadata.setEditMediaLocation(rs.getString("LOCATION"));
              metadata.setMediaLocation(rs.getString("LOCATION"));
              metadata.setMediaType(rs.getString("TYPE"));
              metadata.setSsnKeywords(rs.getString("KEYWORD"));
              metadata.setPhotoGrapher(rs.getString("PHOTOGRAPHER"));
              //metadata.setFaceTags(rs.getString("FACETAG"));
              metadata.setCaption(rs.getString("CAPTION"));
              
            }
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SSNDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rs.close();
            stmt.close();
        }
        
         return metadata;
    }

    /**
     * Updates the media details in database 
     * @param mediaName`
     * @param mediaComments
     * @param rating
     * @param location
     * @param type
     * @throws SQLException
     */
    public static void updateMediaTable(String mediaName, String mediaComments, String rating, String location, String type,String keyword,String photographer,String caption) throws SQLException {
         Statement stmt = null;
        try {
           
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "UPDATE SSNMedia set COMMENT='" + mediaComments + "',RATING='" + rating + "',LOCATION='" + location + "',TYPE='" + type+ "',CAPTION='" + caption + "',KEYWORD='" + keyword+ "',PHOTOGRAPHER='" + photographer + "',MODIFIED='" + dateFormat.format(Calendar.getInstance().getTime()) + "' where NAME='" + mediaName + "'";
           
            stmt.executeUpdate(sql);
            

            stmt.close();
            c.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally{
             stmt.close();
        }
    }
    
    /**
     * Update keywords of a media in database
     * @param mediaName
     * @param keyword
     * @throws SQLException 
     */
    
     public static void updateMediaTableWithKeyWord(String mediaName,String keyword) throws SQLException {
            Statement stmt = null;
         try {
        
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "UPDATE SSNMedia set KEYWORD='" + keyword + "'" + " where NAME='" + mediaName + "'";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
             stmt.close();
         }
    }

    
    
    /**
     * Retrieves voice command path from database on the basis of file name
     * @param fileName
     * @return
     * @throws SQLException
     */
    public static String getVoiceCommandPath(String fileName) throws SQLException {
        String voiceNote    =       null;
        Statement stmt      =       null;
        ResultSet rs        =       null;
        try{
        c.setAutoCommit(false);
        stmt = c.createStatement();

        rs = stmt.executeQuery("SELECT VOICE_NOTE_PATH FROM SSNMedia WHERE NAME = '" + fileName + "'");
        while (rs.next()) {
            voiceNote = rs.getString("VOICE_NOTE_PATH");

        }
        }catch(SQLException ee){
            logger.error(ee);
        }finally{
            rs.close();
            stmt.close();
        }
        
        return voiceNote;
    }

    /**
     * Checks whether the media details exist in database or not on the basis 
     * of media name
     * @param mediaName
     * @return
     * @throws SQLException
     */
    public static boolean checkMediaExist(String mediaName) throws SQLException {
        int size = 0;
        boolean flag    =   false;
        Statement stmt  =   null;
        ResultSet rs    =   null;
        try{
        
        c.setAutoCommit(false);
        stmt = c.createStatement();

        rs = stmt.executeQuery("SELECT * FROM SSNMedia where NAME='" + mediaName.trim() + "'");


        while (rs.next()) {
            
            size++;
            flag = true;
        }
        if (size > 0) 
            flag    =   true;
        
        
        
        }catch(NullPointerException ee){
            
            logger.error(ee);
        }finally{
            rs.close();
            stmt.close();
        }
                
        return  flag;
    }

    /**
     * Return the metadata from database on the basis of media name
     * @param mediaName
     * @return
     * @throws SQLException
     */
    public static SSNGalleryMetaData getSSNMetaData(String mediaName) throws SQLException {
        SSNGalleryMetaData mdata = new SSNGalleryMetaData();
        Statement stmt = null;
        c.setAutoCommit(false);
        stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM SSNMedia where NAME='" + mediaName + "'");
        while (rs.next()) {
            rs.getInt("id");

            mdata.setTitle(rs.getString("name"));
            mdata.setUserComments(rs.getString("comment"));
            mdata.setSsnRatings(rs.getString("rating"));
            mdata.setMediaLocation(rs.getString("location"));
            mdata.setEditMediaLocation(rs.getString("location"));
            mdata.setMediaType(rs.getString("type"));
            mdata.setCreated(rs.getString("created"));
            mdata.setModiFied(rs.getString("modified"));
            mdata.setVoiceNotePath(rs.getString("VOICE_NOTE_PATH"));
            mdata.setSsnKeywords(rs.getString("keyword"));
            mdata.setPhotoGrapher(rs.getString("photographer"));
            mdata.setCaption(rs.getString("caption"));
            mdata.setAddress(rs.getString("Address")!=null?rs.getString("Address"):"");
        }
        rs.close();
        stmt.close();
        return mdata;
    }

   
    /**
     * Return user from database on the basis of username and authorizer
     * @param username
     * @param authoriser
     * @return
     */
    public static SSNSocialModel findUserByUsernname(String username, String authoriser) throws SQLException {
        SSNSocialModel model    = null;
        Statement stmt          = null;
        ResultSet rs            = null;
        try {
            
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String queryString = "SELECT * FROM SSN_AUTH_MASTER WHERE SSN_USER_NAME = '%s' AND SSN_AUTHORISER = '%s'";
           
            rs = stmt.executeQuery(String.format(queryString, username, authoriser));
            
            if (rs.next()) {
                model = new SSNSocialModel();
                model.setUsername(rs.getString("SSN_USER_NAME"));
                model.setFirstName(rs.getString("SSN_FIRST_NAME"));
                model.setLastName(rs.getString("SSN_LAST_NAME"));
                model.setEmail(rs.getString("SSN_EMAIL"));
                model.setAuthoriser(rs.getString("SSN_AUTHORISER"));

            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally{
            rs.close();
            stmt.close();
        }

        return model;
    }

    /**
     * Create user object in the database
     * @param socialModel
     * @return
     */
    public static boolean insertUser(SSNSocialModel socialModel) {
        boolean success = false;
        Statement stmt = null;
        try {
       
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "INSERT INTO SSN_AUTH_MASTER (SSN_USER_NAME, SSN_FIRST_NAME, SSN_LAST_NAME, SSN_EMAIL, SSN_AUTHORISER) "
                    + "VALUES ('" + socialModel.getUsername() + "','" + socialModel.getFirstName() + "','" + socialModel.getLastName() + "','" + socialModel.getEmail() + "','" + socialModel.getAuthoriser() + "' )";
           
           stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            success = true;

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return success;
    }

    /**
     * Update the user details in database 
     * @param socialModel
     * @return
     */
    public static boolean updateUser(SSNSocialModel socialModel) {
        boolean success = false;
        Statement stmt = null;
        try {
  
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sqlQuery = "UPDATE SSN_AUTH_MASTER SET SSN_FIRST_NAME = '%s', SSN_LAST_NAME = '%s', SSN_EMAIL = '%s',SSN_BIRTHDAY ='%s',SSN_GENDER = '%s',SSN_ZIPCODE = '%s' WHERE SSN_USER_NAME = '%s'";
            sqlQuery = String.format(sqlQuery, socialModel.getFirstName(), socialModel.getLastName(), socialModel.getEmail(),socialModel.getBirthDay(),socialModel.getGender(),socialModel.getZipCode(), socialModel.getUsername());

            int rs =stmt.executeUpdate(sqlQuery);

            stmt.close();
            c.commit();
            success = rs > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return success;
    }
    
    public static Boolean isInsetNeeded_MediaTable(String mediaName, String voiceNotePath) throws SQLException{
        boolean flag = true;
        PreparedStatement stmt = null;
        try {
            String selectIdQuery = "Select id FROM ssnmedia WHERE name='" + mediaName + "'";
            stmt = c.prepareStatement(selectIdQuery);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                flag = false;
            }
            rs.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally{
            stmt.close();
        }
        
        return flag;
    }
   
    /**
     * Save voice note in database on the basis of name and path
     * @param mediaName
     * @param voiceNotePath
     * @throws SQLException
     */
    public static void insertVoiceNote_MediaTable(String mediaName, String voiceNotePath) throws SQLException {
        PreparedStatement stmt  =null;
        try {
            c.setAutoCommit(false);
            String selectIdQuery = "Select id FROM ssnmedia WHERE name='" + mediaName + "'";
            stmt = c.prepareStatement(selectIdQuery);

            ResultSet rs = stmt.executeQuery();
            Long id = null;
            while (rs.next()) {
                id = Long.parseLong(rs.getString("id"));
               
                    String sql = "Update SSNMEDIA SET VOICE_NOTE_PATH=? where NAME=?";
                    PreparedStatement stmt1 = c.prepareStatement(sql);
                    stmt1.setString(1, voiceNotePath);
                    stmt1.setString(2, mediaName);
                    stmt1.executeUpdate();
                    stmt1.close();
                
            }
            stmt.close();

            if (id == null) {
                String sql = "Insert into SSNMEDIA(VOICE_NOTE_PATH,Name) values(?,?) ";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, voiceNotePath);
                stmt.setString(2, mediaName);
                stmt.executeUpdate();
                stmt.close();
            }

            c.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally{
            stmt.close();
        }
    }

     //Delete Schedule

    /**
     * Delete the created schedule from database 
     * @param nodeType
     * @param txtTitle
     * @return
     */
        public static boolean deleteParentSchedule(int tagId,int subTagId) throws SQLException {
            PreparedStatement stmt1 = null;
            try {
                c.setAutoCommit(false);
                String sql = null;
//                    if (nodeType.equalsIgnoreCase("c")) {
//                        sql = "Delete from SSNSCHEDULESUBTAG where SUBTAGTITLE = ?";
//                    } else {
                        sql = "Delete from SSNSCHEDULETAG where TAGID = "+tagId;
                   // }

                stmt1 = c.prepareStatement(sql);
                //stmt1.setString(1, txtTitle.trim());
                
                int ret = stmt1.executeUpdate();
                stmt1.close();
                c.commit();
                    if (ret > 0) {
                        deleteChildSchedule(tagId,subTagId);
                        return true;
                    }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }finally{
                stmt1.close();
        }

        return false;

    }
        public static boolean deleteChildSchedule(int tagId,int subTagId) throws SQLException {
            PreparedStatement stmt1 = null;
            try {
                c.setAutoCommit(false);
                String sql = null;
                if(subTagId>0)
                    sql = "Delete from SSNSCHEDULESUBTAG where SUBTAGID = "+subTagId ;
                else
                    sql = "Delete from SSNSCHEDULESUBTAG where TAGID = "+tagId ;
                
                stmt1 = c.prepareStatement(sql);
                //stmt1.setString(1, txtTitle.trim());
                
                int ret = stmt1.executeUpdate();
                stmt1.close();
                c.commit();
                
                    if (ret > 0) {
                        return true;
                    }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }finally{
                stmt1.close();
        }

        return false;

    }

    /**
     * Deletes the record from database based on voice note path
     * @param voiceNotePath
     */
    public static int removeVoiceNote_MediaTable(String voiceNotePath, String imageMediaFileLocation) throws SQLException {
        int i=0;
        PreparedStatement stmt1 =   null;
        try {
            c.setAutoCommit(false);
            String sql = "update ssnmedia set voice_note_path=null WHERE name=? and voice_note_path=?";
            stmt1 = c.prepareStatement(sql);
            stmt1.setString(1, imageMediaFileLocation);
            stmt1.setString(2, voiceNotePath);
            i = stmt1.executeUpdate();
            stmt1.close();

            c.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally{
            stmt1.close();
        }
            return i;
        
        
    }

    /**
     * Save the preferences to the database
     * 
     * @param preferences
     * @param imagePrefix
     * @param videoPrefix
     * @return
     */
    public static boolean savePreferences(Integer[] preferences, String imagePrefix, String videoPrefix) throws SQLException {
        boolean success = false;
            Statement stmt = null;
        try {
        
            c.setAutoCommit(false);
            stmt = c.createStatement();
            Map<String, String> preferencesFromDb = getPreferences();
            if (preferencesFromDb == null || preferencesFromDb.isEmpty()) {
                
                String sql = "INSERT INTO SSN_PREFERENCES (FACERECOGNITION, VOICECOMMAND, CLOUDSYNC,IMAGENAMEPREFIX,VIDEONAMEPREFIX,SCHEDULEDCOMMAND) "
                        + "VALUES ('" + preferences[0] + "','" + preferences[1] + "','" + 1 + "','" + imagePrefix + "','" + videoPrefix + "','"+preferences[2]+"' )";
                stmt.executeUpdate(sql);   

            } else {
                String sql = "Update SSN_PREFERENCES SET FACERECOGNITION=" + preferences[0] + " ,VOICECOMMAND =" + 1
                        + " ,CLOUDSYNC=" + preferences[2] + " ,IMAGENAMEPREFIX=" + "'" + imagePrefix + "'" + " ,VIDEONAMEPREFIX=" + "'" + videoPrefix + "',SCHEDULEDCOMMAND=" + preferences[1] + " ";
                
                
                stmt.executeUpdate(sql);

            }

            
            c.commit();
            success = true;

        }catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
            stmt.close();
        }

        return success;
    }

    /**
     * Return the user preferences from the database
     * @return
     */
    public static Map<String, String> getPreferences() throws SQLException {

        Map<String, String> preferencesMap = null;
        Statement stmt = null;
        try {
           
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "SELECT * FROM SSN_PREFERENCES ";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                preferencesMap = new HashMap<>();
                preferencesMap.put(SSNConstants.SSN_FACE_RECOGNITION, String.valueOf(rs.getInt("FACERECOGNITION")));
                preferencesMap.put(SSNConstants.SSN_VOICE_COMMAND, String.valueOf(rs.getInt("VOICECOMMAND")));
                
                preferencesMap.put(SSNConstants.SSN_CLOUD_SYNC, String.valueOf(rs.getInt("CLOUDSYNC")));
                preferencesMap.put(SSNConstants.SSN_IMAGE_PREFIX, rs.getString("IMAGENAMEPREFIX"));
                preferencesMap.put(SSNConstants.SSN_VIDEO_PREFIX, rs.getString("VIDEONAMEPREFIX"));
                preferencesMap.put(SSNConstants.SSN_SCHEDULED, rs.getString("SCHEDULEDCOMMAND"));

            }
            else
            {
               preferencesMap = new HashMap<String, String>();
            }

            rs.close();
            c.commit();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
             stmt.close();
        }

        return preferencesMap;
    }
    
    public static boolean saveTaggedFaces(TaggedFace face,String mediaPath) throws SQLException{
        boolean success = false;
        Statement stmt = null;
        try{
            
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO SSN_TAGGED_MEDIA (MediaPath, Img_Index, X_Coordinate,Y_Coordinate,Width,Height,Tags) "
                        + "VALUES ('" + mediaPath + "','" + face.getImageIndex() + "','" + face.getxCoordinate()+ "','" + face.getyCoordinate() + "','" + face.getWidth() + "','" + face.getHeight() +  "','" + face.getTags() +  "' )";
            stmt.executeUpdate(sql);
            
            c.commit();
            success = true;

        }catch(Exception e){
            logger.error(e.getMessage()); 
        }finally{
            stmt.close();
        }
        return success;
    }
    

    public static void updateTaggedFaces(String mediaPath,String imageIndex,String tag) throws SQLException {
        Statement stmt = null;
        try {
        
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "UPDATE SSN_TAGGED_MEDIA set Tags='" + tag + "'" + " where MediaPath='" + mediaPath + "'" + "AND img_Index = '" + imageIndex + "'";
            stmt.executeUpdate(sql);

            
            c.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
                stmt.close();
        }
    }
    
    
    public static String getTaggedFace(String mediaPath,String imageIndex) throws SQLException {
        String tags = "";
        Statement stmt = null;
        try {
        
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            String sql = "SELECT * FROM SSN_TAGGED_MEDIA WHERE MediaPath =" +"'" + mediaPath +"'" + "AND Img_Index ='" +imageIndex + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               tags = rs.getString("Tags");
            }
            
        
            c.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
            stmt.close();
        }
       return tags;
    }
    
    
    public static List<String> getTaggedFaceByMediaPath(String mediaPath) throws SQLException {
        List<String> tagList = new ArrayList<>();
        Statement stmt = null;
        try {
            
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            
            String sql = "SELECT tags FROM SSN_TAGGED_MEDIA WHERE MediaPath =" +"'" + mediaPath +"'" ;
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
               tagList.add(rs.getString("Tags"));
            }
            rs.close();
            stmt.close();
            c.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
       return tagList;
    }
    
    
    public static boolean checkFaceExist(String mediaPath)  {
        int size = 0;
        Statement stmt = null;
        try {
   
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM SSN_TAGGED_MEDIA WHERE MediaPath =" +"'" + mediaPath + "'");
            
            
            while (rs.next()) {
                
                size++;
            }
            rs.close();
            stmt.close();
            
            
            
        } catch (SQLException ex) {
             logger.error(ex.getMessage()); 
        }
        return size > 0;
        
    }
    
    
    public static List<TaggedFace> getTaggedFaces(String mediaPath) throws SQLException{
        List<TaggedFace> faceList = new ArrayList<>();
        Statement stmt = null;
        try{
            
   
            c.setAutoCommit(false);
            stmt = c.createStatement();
            
             String sql = "SELECT * FROM SSN_TAGGED_MEDIA WHERE MediaPath =" +"'" + mediaPath +"'";
             ResultSet rs = stmt.executeQuery(sql);
             
            while(rs.next()){
                TaggedFace face = new TaggedFace();
                face.setxCoordinate(rs.getFloat("X_Coordinate"));
                face.setyCoordinate(rs.getFloat("Y_Coordinate"));
                face.setWidth(rs.getFloat("Width"));
                face.setHeight(rs.getFloat("Height"));
                face.setImageIndex(rs.getString("Img_Index"));
                face.setTags(rs.getString("Tags"));
                faceList.add(face);
            }
            rs.close();
        }catch(Exception e){
           logger.error(e.getMessage()); 
        }finally{
                     stmt.close();
        }
        
        return faceList;
    }
    
    
    
    

    
    

    /**
     * Get the method/command to execute for the given voice command
     * @param givenVoiceCommand String
     * @return String
     */
    public static String getMappedMethod(String givenVoiceCommand) {
        String method = null;
        try {

            String sql = "select * FROM SSN_CUSTOM_COMMAND WHERE COMMANDVALUE_WORDTOSAY='" + givenVoiceCommand + "'";
            PreparedStatement pstmt = c.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                method = rs.getString("COMMANDKEY_METHOD");

            }
            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return method;
    }

    /**
     * Update the already created schedule in the database 
     * @param nodeType
     * @param txtTitle
     * @param album
     * @param txtLocation
     * @param txtKeyword
     * @param txtComments
     * @param startDate
     * @param endDate
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public static boolean updateSchedule(String nodeType, String txtTitle, String album, String txtLocation, String txtKeyword, String txtComments, Date startDate, Date endDate, String startTime, String endTime,
            String photoGrapher, String videoPrefix, String imagePrefix,int tagId, int subTagId) throws SQLException, ParseException {
        
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        int impactedCount = 0;
        String updateSql = null;
        
        if (nodeType.equalsIgnoreCase("p"))
            updateSql = "Update SSNSCHEDULETAG set TITLE = '"+txtTitle+"' , ALBUM = '" + album + "' , PLACE = '" + txtLocation + "' ,KEYWORDS = '" + txtKeyword + "', COMMENT= '" + txtComments + "', FROMDATE ='" + sdf.format(startDate) + "', TODATE = '" + sdf.format(endDate) + "' , STARTTIME = '" +startTime + "' , ENDTIME = '"+endTime+"' , PHOTOGRAPHER= '"+photoGrapher+"', VIDEOPREFIX = '"+videoPrefix+"', IMAGEPREFIX ='"+imagePrefix+"' where TAGID = '" + tagId + "'";
        else
            updateSql = "Update SSNSCHEDULESUBTAG set SUBTAGTITLE = '"+txtTitle+"' , SUBTAGALBUM = '" + album + "' , SUBTAGPLACE = '" + txtLocation + "' ,SUBTAGKEYWORDS = '" + txtKeyword + "', SUBTAGCOMMENT= '" + txtComments + "', SUBTAGFROMDATE ='" + sdf.format(startDate) + "', SUBTAGTODATE = '" + sdf.format(endDate) + "' , SUBSTARTTIME = '" +startTime + "' , SUBENDTIME = '"+endTime+"' , SUBPHOTOGRAPHER= '"+photoGrapher+"', SUBVIDEOPREFIX = '"+videoPrefix+"', SUBIMAGEPREFIX ='"+imagePrefix+"'  where SUBTAGID = '" + subTagId + "'";
        

        c.setAutoCommit(false);
        PreparedStatement pStmt = c.prepareStatement(updateSql);
        impactedCount = pStmt.executeUpdate();
        c.commit();
        pStmt.close();
        return impactedCount > 0;

    }

    /**
     * Saves the media details in the database based on name and album
     * @param id
     * @param name
     * @param album
     * @return
     */
    public static boolean saveCloudMedia(long id, String name, String album) {
        boolean success = false;
        try {
            Statement stmt = null;
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String sql = "INSERT INTO SSN_SYNC_MEDIA (SSN_IMAGE_ID, SSN_NAME, SSN_ALBUM) VALUES (" + id + ",'" + name + "','" + album + "')";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            success = true;

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return success;
    }

    /**
     * Get the media id from the database on the basis of name and album name
     * @param name
     * @param album
     * @return
     */
    public static long findSyncronisedMediaId(String name, String album) throws SQLException {
        long id = -1;
        Statement stmt = null;
        try {
     
            c.setAutoCommit(false);
            stmt = c.createStatement();

            String queryString = "SELECT * FROM SSN_SYNC_MEDIA WHERE SSN_NAME = '%s' AND SSN_ALBUM = '%s'";
            ResultSet rs = stmt.executeQuery(String.format(queryString, name, album));

            if (rs.next()) {
                id = rs.getInt("SSN_IMAGE_ID");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }finally{
                   stmt.close();
        }

        return id;
    }

    /**
     * Return voice note path id from database
     * @param voiceNotePath
     * @return
     * @throws SQLException
     */
    public static boolean selectVoiceNotePath_MediaTable(String voiceNotePath) throws SQLException {
        try {
            c.setAutoCommit(false);
            String selectIdQuery = "Select id FROM ssnmedia WHERE voice_note_path='" + voiceNotePath + "'";
            PreparedStatement stmt = c.prepareStatement(selectIdQuery);

            ResultSet rs = stmt.executeQuery();
            Long id = null;
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return false;
    }
    
    /**
     * Return custom voice command from database
     * @return Map<String, String>
     */
    public static Map<String, List<String>> getCustomVoiceCommand() {
        Map<String, String> voiceCommands = new HashMap<String, String>();
        Map<String, List<String>> voiceCommandsMap = new HashMap<String, List<String>>(); 
        try {
            
            c.setAutoCommit(false);

            
           
            String getAllKeysQuery = "select distinct COMMANDKEY_METHOD from SSN_CUSTOM_COMMAND";
            PreparedStatement stmt = c.prepareStatement(getAllKeysQuery); 
            ResultSet rs = stmt.executeQuery();
            List<String> allKeys = new ArrayList<>();
            
            while (rs.next()) {
                List<String> specificCommandValues = new ArrayList<>();
                allKeys.add(rs.getString("COMMANDKEY_METHOD"));
                
                String getCommandsQuery = "select COMMANDVALUE_WORDTOSAY from SSN_CUSTOM_COMMAND where COMMANDKEY_METHOD='"+rs.getString("COMMANDKEY_METHOD")+"'";
                PreparedStatement stmt1 = c.prepareStatement(getCommandsQuery);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    specificCommandValues.add(rs1.getString("COMMANDVALUE_WORDTOSAY").trim().toUpperCase());
                    
                }
                voiceCommandsMap.put(rs.getString("COMMANDKEY_METHOD"), specificCommandValues);
                stmt1.close();
               
            }
          
            
            
        } catch (Exception e) {
            logger.error(e);
        }
        
        return voiceCommandsMap;
    }
    
    public static List<String> getAllExistingUniqueCommands(){
         Map<String, List<String>> existingCommandMap = getCustomVoiceCommand();
        List<String> commandList = new ArrayList<>();  
        Set<String> commandActions = existingCommandMap.keySet();
            for(String existingKey:commandActions){
                commandList.addAll(existingCommandMap.get(existingKey));                
            }
            return commandList;
    }
    
    /**
     * Updates custom voice commands in database
     * @param voiceCommands
     * @return Map<String, String>
     */
    public static void updateCustomVoiceCommand(Map<String, List<String>> voiceCommands) {
        if(voiceCommands.size()>0){
            Set<String> commandActions = voiceCommands.keySet();
            
            try {
                c.setAutoCommit(false);
                String deleteQuery = "delete from SSN_CUSTOM_COMMAND ";
                Statement stmt = c.createStatement();
                stmt.executeUpdate(deleteQuery);
            
                for(String commandAction:commandActions){
                    List<String> commandList = voiceCommands.get(commandAction);
                    String insertQuery = "insert into SSN_CUSTOM_COMMAND(COMMANDKEY_METHOD,COMMANDVALUE_WORDTOSAY) values(?,?)";
                    for(String command : commandList){
                        PreparedStatement pstmt = c.prepareStatement(insertQuery);
                        pstmt.setString(1, commandAction);
                        pstmt.setString(2, command);

                        pstmt.executeUpdate();
                        pstmt.close();
                    }
                    
                }
                c.commit();
            } catch (SQLException e) {
               logger.error(e);
            }catch (Exception e) {
               logger.error(e);
            }
        }
    }
    
    /**
     * Returns default voice commands from database 
     * @return Map<String,String>
     */
    public static Map<String, List<String>> getDefaultVoiceCommand() throws SQLException {
        Map<String, List<String>> voiceCommands = new HashMap<>();
        PreparedStatement stmt  =null;
        try {
            
            c.setAutoCommit(false);
            String selectIdQuery = "Select COMMANDKEY_METHOD,DEFAULT_WORDTOSAY FROM SSN_CUSTOM_DEFAULT_COMMAND";
            stmt = c.prepareStatement(selectIdQuery);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                List<String> command = new ArrayList<>();
                command.add(rs.getString("DEFAULT_WORDTOSAY"));
                voiceCommands.put(rs.getString("COMMANDKEY_METHOD"),command );
            }
            rs.close();
            
        } catch (Exception e) {
           logger.error(e);
        }finally{
            stmt.close();
        }
        
        return voiceCommands;
    }
    
    public static void deleteMetaData(String mediaPath) throws SQLException{
            PreparedStatement stmt1     =   null;
        try {
            c.setAutoCommit(false);
            String sql = "delete FROM ssnmedia WHERE NAME=?";
            stmt1 = c.prepareStatement(sql);
            stmt1.setString(1, mediaPath);
            stmt1.executeUpdate();
           

            c.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally{
             stmt1.close();
        }
        
    }
    
    public static SSNScheduleListForm getScheduledTag(String tagDetails){
        SSNScheduleListForm sSNScheduleListForm =   null;
        Statement stmt = null;
       
        SimpleDateFormat sdf = new SimpleDateFormat(SSNConstants.SSN_SCHEDULE_TAG_DATE_PATTERN);
        String[] splitedTagDetails    =   tagDetails.split(SSNConstants.SSN_SCHEDULAR_TAG_SEPERATOR);
        try{
           
            String query    =   "SELECT * FROM SSNSCHEDULETAG WHERE ALBUM like '"+splitedTagDetails[0]+"' AND TITLE LIKE '"+splitedTagDetails[1]+"' AND FROMDATE = '"+splitedTagDetails[2].split(" ")[0]+"' AND TODATE like '"+splitedTagDetails[3].split(" ")[0]+"' AND STARTTIME LIKE '"+splitedTagDetails[2].split(" ")[2]+"' AND ENDTIME LIKE '"+splitedTagDetails[3].split(" ")[2]+"'";
            
            stmt    =   c.createStatement();
            ResultSet rs    =   stmt.executeQuery(query);
            if(rs.next()){
                sSNScheduleListForm =   new SSNScheduleListForm();
                /*
                        TAGID|
                        ENT|
                        OPT|
                        DURATION|
                        RATINGS
                        |SUBTAGS
                        |CREATEDON 
                        |FROMDATE  
                        |MODIFIEDON
                        |TODATE    
                        |COMMENT    
                        |KEYWORDS
                        |PLACE        
                        |TITLE  
                        |VOICENOTEPATH
                        |ALBUM 
                        |STARTTIME
                        |ENDTIME
                        |PHOTOGRAPHER
                        |
                        VIDEOPREFIX|
                        IMAGEPREFIX
                        
                        */
                sSNScheduleListForm.setTagId(rs.getString("TAGID") != null ? rs.getInt("TAGID") : 0);
                sSNScheduleListForm.setSsnTitle(rs.getString("TITLE") != null ? rs.getString("TITLE") : "");
                sSNScheduleListForm.setSsnAlbum(rs.getString("ALBUM") != null ? rs.getString("ALBUM") : "");
                sSNScheduleListForm.setSsnLocation(rs.getString("PLACE") != null ? rs.getString("PLACE") : "");
                sSNScheduleListForm.setSsnkeyWords(rs.getString("KEYWORDS") != null ? rs.getString("KEYWORDS") : "");
               // sSNScheduleListForm.setSsnComent(rs.getString("COMMENT") != null ? rs.getString("COMMENT") : "");
                sSNScheduleListForm.setSsnComent(rs.getString("COMMENT") != null ? "" : "");
                sSNScheduleListForm.setSsntoDate(sdf.parse(rs.getString("TODATE") != null ? rs.getString("TODATE") : ""));
                sSNScheduleListForm.setSsnModificationon(rs.getString("MODIFIEDON") != null ? rs.getString("MODIFIEDON") : "");
                sSNScheduleListForm.setSsnStrfromDate(sdf.format(sdf.parse(rs.getString("FROMDATE") != null ? rs.getString("FROMDATE") : "")));
                sSNScheduleListForm.setSsnStrtoDate(sdf.format(sdf.parse(rs.getString("TODATE") != null ? rs.getString("TODATE") : "")));
                sSNScheduleListForm.setSsncreateDate(sdf.parse(rs.getString("CREATEDON") != null ? rs.getString("CREATEDON") : ""));
                sSNScheduleListForm.setSsnSubtags(rs.getString("SUBTAGS") != null ? rs.getString("SUBTAGS") : "");
                sSNScheduleListForm.setSsnRatings(Integer.parseInt(rs.getString("RATINGS")));
                sSNScheduleListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("DURATION")));
                sSNScheduleListForm.setSsnOpt(Integer.parseInt(rs.getString("OPT")));
                sSNScheduleListForm.setSsnEnt(Integer.parseInt(rs.getString("ENT")));
                sSNScheduleListForm.setSsnVoiceNotePath(rs.getString("VOICENOTEPATH"));
                sSNScheduleListForm.setSsnStartTime(rs.getString("STARTTIME"));
                sSNScheduleListForm.setSsnEndTime(rs.getString("ENDTIME"));
                sSNScheduleListForm.setSsnVideoPrefix(rs.getString("VIDEOPREFIX"));
                sSNScheduleListForm.setSsnImagePrefix(rs.getString("IMAGEPREFIX"));
                        
            }else{
                query    =   "SELECT * FROM SSNSCHEDULESUBTAG WHERE SUBTAGALBUM like '"+splitedTagDetails[0]+"' AND SUBTAGTITLE LIKE '"+splitedTagDetails[1]+"' AND SUBTAGFROMDATE = '"+splitedTagDetails[2].split(" ")[0]+"' AND SUBTAGTODATE like '"+splitedTagDetails[3].split(" ")[0]+"'" + "AND SUBSTARTTIME LIKE '"+splitedTagDetails[2].split(" ")[2]+"' AND SUBENDTIME LIKE '"+splitedTagDetails[3].split(" ")[2]+"'";
                
                stmt    =   c.createStatement();
                rs    =   stmt.executeQuery(query);
                if(rs.next()){
                    sSNScheduleListForm =   new SSNScheduleListForm();
                    /*
                            TAGID|
                            ENT|
                            OPT|
                            DURATION|
                            RATINGS
                            |SUBTAGS
                            |CREATEDON 
                            |FROMDATE  
                            |MODIFIEDON
                            |TODATE    
                            |COMMENT    
                            |KEYWORDS
                            |PLACE        
                            |TITLE  
                            |VOICENOTEPATH
                            |ALBUM 
                            |STARTTIME
                            |ENDTIME
                            |PHOTOGRAPHER
                            |
                            VIDEOPREFIX|
                            IMAGEPREFIX

                            */
                    sSNScheduleListForm.setTagId(rs.getString("TAGID") != null ? rs.getInt("TAGID") : 0);
                    sSNScheduleListForm.setSubTagId(rs.getString("SUBTAGID") != null ? rs.getInt("SUBTAGID") : 0);
                    sSNScheduleListForm.setSsnTitle(rs.getString("SUBTAGTITLE") != null ? rs.getString("SUBTAGTITLE") : "");
                    sSNScheduleListForm.setSsnAlbum(rs.getString("SUBTAGALBUM") != null ? rs.getString("SUBTAGALBUM") : "");
                    sSNScheduleListForm.setSsnLocation(rs.getString("SUBTAGPLACE") != null ? rs.getString("SUBTAGPLACE") : "");
                    sSNScheduleListForm.setSsnkeyWords(rs.getString("SUBTAGKEYWORDS") != null ? rs.getString("SUBTAGKEYWORDS") : "");
                   // sSNScheduleListForm.setSsnComent(rs.getString("COMMENT") != null ? rs.getString("COMMENT") : "");
                    sSNScheduleListForm.setSsnComent(rs.getString("SUBTAGCOMMENT") != null ? "" : "");
                    sSNScheduleListForm.setSsntoDate(sdf.parse(rs.getString("SUBTAGTODATE") != null ? rs.getString("SUBTAGTODATE") : ""));
                    sSNScheduleListForm.setSsnModificationon(rs.getString("SUBTAGMODIFIEDON") != null ? rs.getString("SUBTAGMODIFIEDON") : "");
                    sSNScheduleListForm.setSsnStrfromDate(sdf.format(sdf.parse(rs.getString("SUBTAGFROMDATE") != null ? rs.getString("SUBTAGFROMDATE") : "")));
                    sSNScheduleListForm.setSsnStrtoDate(sdf.format(sdf.parse(rs.getString("SUBTAGTODATE") != null ? rs.getString("SUBTAGTODATE") : "")));
                    sSNScheduleListForm.setSsncreateDate(sdf.parse(rs.getString("SUBTAGCREATEDON") != null ? rs.getString("SUBTAGCREATEDON") : ""));
                    //sSNScheduleListForm.setSsnSubtags(rs.getString("SUBTAGS") != null ? rs.getString("SUBTAGS") : "");
                    sSNScheduleListForm.setSsnRatings(Integer.parseInt(rs.getString("SUBTAGRATINGS")));
                    sSNScheduleListForm.setSsnScheduleTagDuration(Integer.parseInt(rs.getString("SUBTAGDURATION")));
                    sSNScheduleListForm.setSsnOpt(Integer.parseInt(rs.getString("OPT")));
                    sSNScheduleListForm.setSsnEnt(Integer.parseInt(rs.getString("ENT")));
                    sSNScheduleListForm.setSsnVoiceNotePath(rs.getString("SUBTAGVOICENOTEPATH"));
                    sSNScheduleListForm.setSsnStartTime(rs.getString("SUBSTARTTIME"));
                    sSNScheduleListForm.setSsnEndTime(rs.getString("SUBENDTIME"));
                    sSNScheduleListForm.setSsnVideoPrefix(rs.getString("SUBVIDEOPREFIX"));
                    sSNScheduleListForm.setSsnImagePrefix(rs.getString("SUBIMAGEPREFIX"));
                }
            }
            rs.close();
        }catch(SQLException ee){
            logger.error(ee);
        } catch (ParseException ex) {
             logger.error( ex);
        }finally{
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
            
        return sSNScheduleListForm;
        
    }
}
