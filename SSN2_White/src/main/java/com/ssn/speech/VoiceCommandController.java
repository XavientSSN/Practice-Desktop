package com.ssn.speech;

import com.ssn.app.loader.SSNConstants;
import com.ssn.dao.SSNDao;
import com.ssn.ui.form.SSNHomeForm;
import com.ssn.webcam.SSNWebcamPanel;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 * Controller class for Voice Commands
 * @author Abhinav Tripathi
 */
public class VoiceCommandController {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(VoiceCommandController.class);
    
   // private static VoiceCommandController singleInstance;
    SSNWebcamPanel ssnWebcamPanel = null;
    Thread camRunner = null;
    
    public static boolean isVoiceCammandEnabled(){
        boolean flag=false;
        
        Map<String,String> preferences = null;
        try {
            preferences = SSNDao.getPreferences();
        } catch (SQLException ex) {
            Logger.getLogger(VoiceCommandController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(preferences == null || preferences.isEmpty() || Integer.parseInt(preferences.get(SSNConstants.SSN_VOICE_COMMAND))==1){
            flag=true;
        }
        
        return true;
    }

    public  Thread openCamera(final SSNHomeForm homeForm){
        camRunner=homeForm.getHomeModel().openCamera(new JLabel("Open Camera"),true);

        try {
            Thread.sleep(10000L);
            captureImage(homeForm);
        } catch (InterruptedException ex) {
            Logger.getLogger(VoiceCommandController.class.getName()).log(Level.SEVERE, null, ex);
        }


        //camRunner.start(); 
       // homeForm.setSsnMicrophoneCamThread(camRunner);
        return camRunner;
    }
    
    public  void closeCamera(final SSNHomeForm homeForm){
        try{            
            if(homeForm.getSsnMicrophoneCamThread() != null ){
                homeForm.getHomeModel().getSsnWebcamPanel().getWebcam().removeWebcamListener(getSsnWebcamPanel());
                homeForm.getHomeModel().getSsnWebcamPanel().getWebcam().close();
                homeForm.getHomeModel().getSsnWebcamPanel().getWebcam().getDevice().close();
                
                homeForm.getHomeModel().getSsnWebcamPanel().toBack();
                homeForm.getHomeModel().getSsnWebcamPanel().invalidate();
                
                homeForm.setSsnMicrophoneCamThread(null);
                homeForm.getHomeModel().getSsnWebcamPanel().dispose();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void captureImage(final SSNHomeForm homeForm){
         homeForm.getHomeModel().getSsnWebcamPanel().getFunctionPanel().captureImage();
    }
    
    public void captureVideo(final SSNHomeForm homeForm){
         homeForm.getHomeModel().getSsnWebcamPanel().getFunctionPanel().captureVideo();
    }
    
    public SSNWebcamPanel getSsnWebcamPanel() {
        return ssnWebcamPanel;
    }

    public void setSsnWebcamPanel(SSNWebcamPanel ssnWebcamPanel) {
        this.ssnWebcamPanel = ssnWebcamPanel;
    }
    
    public static String getMappedMethod(String givenVoiceCommand){
        return SSNDao.getMappedMethod(givenVoiceCommand.toUpperCase());
    }
}
