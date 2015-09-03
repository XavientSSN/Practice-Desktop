
package com.ssn.speech;

/**
 *
 * @author ATripathi4
 */
import com.ssn.app.loader.SSNConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSNStt {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SSNStt.class);
    /**
     * Converts a .wav file to text
     * @param wavFile : File must be in .wav format
     * @return String 
     */
    public static String convertToText(File wavFile){
        String convertedText = "";
        if(wavFile != null && wavFile.exists()){
            StringBuffer fileName = new StringBuffer(wavFile.getName());
            if(fileName.substring(fileName.lastIndexOf(".")+1).equalsIgnoreCase("wav")){
            
                try {

                    StringBuilder sb = new StringBuilder(SSNConstants.SSN_STT_SERVICE_LINK);
                    URL url1 = null;
                    try {
                        url1 = new URL(sb.toString());
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(SSNStt.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    HttpURLConnection urlConn = (HttpURLConnection) url1.openConnection();
                    urlConn.setDoOutput(true);
                    urlConn.setRequestMethod("POST");
                    urlConn.setRequestProperty("Content-Type", "audio/l16; rate=16000"); 
                    OutputStream outputStream1 = urlConn.getOutputStream();

                    FileInputStream fileInputStream = new FileInputStream(wavFile);
                    byte[] buffer = new byte[1024];
                    while ((fileInputStream.read(buffer, 0, 256)) != -1) {
                        outputStream1.write(buffer, 0, 256);
                        outputStream1.flush();
                    }
                    fileInputStream.close();
                    outputStream1.close(); 
                    logger.debug("urlConn.getContent()========="+urlConn.getContent());


                    BufferedReader b = new BufferedReader(new InputStreamReader(
                            (urlConn.getInputStream())));

                    String output1;
                    StringBuilder response = new StringBuilder();

                    while ((output1 = b.readLine()) != null) {
                        response.append(output1);
                    }
                    logger.debug("Response From SSNStt === " + response);
                    
                    convertedText = response.toString(); //TODO : Need to return proper response from JSON

                } catch (IOException ex) {
                    ex.printStackTrace();
                } 
            }
        }
        return convertedText;
    }
    
    
}
