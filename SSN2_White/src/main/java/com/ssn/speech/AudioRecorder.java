package com.ssn.speech;

import com.ssn.helper.SSNHelper;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 * Captures/records audio/voice in .wav format
 * @author Abhinav Tripathi
 */
public class AudioRecorder extends JFrame {
    private static Logger logger = Logger.getLogger(AudioRecorder.class);
    
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    boolean isVoiceNote = false;

    final JButton captureBtn = new JButton("Capture");
    final JButton stopBtn = new JButton("Stop");

    final JPanel btnPanel = new JPanel();
    final ButtonGroup btnGroup = new ButtonGroup();
    
    
    private static String voiceNoteFileName = null;
    private static String searchVoiceMediaFileName= null;
    private static final String FILE_FORMAT = ".wav";

    public static void main(String args[]) {
        new AudioRecorder();
    }

    public AudioRecorder() {

    }
    
    /**
     * capture/record audio
     * @param isVoiceNote boolean 
     * @return targetDataLine TargetDataLine
     */
    public TargetDataLine captureAudio(boolean isVoiceNote) throws LineUnavailableException {
//        try {
            this.isVoiceNote = isVoiceNote;
            if(isVoiceNote){
                audioFormat = getAudioFormat();
            }else{
                audioFormat = getAudioFormatVoiceSerch();
            }
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if(targetDataLine==null){
                targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            }
            new CaptureThread().start();
//        } 
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        return targetDataLine;             

    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    
    private AudioFormat getAudioFormatVoiceSerch() {
        //Audio format for Voice Note/tag
        float sampleRate = 16000.0F;    // 8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;      // 8,16
        int channels = 1;               
        boolean signed = true;          
        boolean bigEndian = false;      
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    class CaptureThread extends Thread {

        public void run() {
            AudioFileFormat.Type fileType = null;
            File audioFile = null;

            fileType = AudioFileFormat.Type.WAVE;
            
            String fileNamePart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
            fileNamePart = fileNamePart.replace(":", "_").replace("-", "_").replace(" ", "_");
            
            File theDir = new File(SSNHelper.getSsnVoiceNoteDirPath());
            if(!theDir.exists()){
                theDir.mkdir();
            }
                        
            String file = SSNHelper.getSsnVoiceNoteDirPath()+"SSN_" +fileNamePart+FILE_FORMAT;
            logger.debug("file path = "+file);
            
            if(isVoiceNote){ 
                audioFile = new File(file);
                setVoiceNoteFileName(audioFile.getName());
            }else{
                audioFile = new File(file);
                setSearchVoiceMediaFileName(audioFile.getAbsolutePath());
            }
            
            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                e.printStackTrace();
            }
            finally{
                targetDataLine.flush();
                targetDataLine.stop();
                targetDataLine.close();
            }
        }
    }

    public static String getVoiceNoteFileName() {
        return voiceNoteFileName;
    }

    public static void setVoiceNoteFileName(String voiceNoteFileName) {
        AudioRecorder.voiceNoteFileName = voiceNoteFileName;
    }
    
    public static String getSearchVoiceMediaFileName() {
        return searchVoiceMediaFileName;
    }

    public static void setSearchVoiceMediaFileName(String searchVoiceMediaFileName) {
        AudioRecorder.searchVoiceMediaFileName = searchVoiceMediaFileName;
    }

    

}
