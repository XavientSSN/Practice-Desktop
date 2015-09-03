/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.request;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author aarora1
 */
public class SSNFaceRecognitionRequest {
    public static String API_KEY = "api_key";
    public static String API_SECRET= "api_secret";
    public static String JOB = "jobs";
    public static String NAME_SPACE = "name_space";
    public static String USER_ID = "user_id";
    public static String TAG = "tag";
    public static String NEW_TAG = "new_tag";
    public static String IMG_INDEX = "img_index";
    public static String TAGS = "tags";
    public static String FILE_PARAM = "base64"; 
    public static String API_KEY_VALUE = "eamr6CTwbuo12Rfy"; 
    public static String API_SECRET_VALUE = "exsSoAyJccGNmwPr";
    public static String NAME_SPACE_VALUE = "FacePOC2";
    public static String USER_ID_VALUE ="test2";
    public static String TAG_VALUE = "test";
    
    public Map<String,String> parameters = new HashMap<String, String>();
    
    public SSNFaceRecognitionRequest(){
        parameters.put(API_KEY, API_KEY_VALUE);
        parameters.put(API_SECRET, API_SECRET_VALUE);
        parameters.put(NAME_SPACE, NAME_SPACE_VALUE);
        parameters.put(USER_ID, USER_ID_VALUE);
    }
     public SSNFaceRecognitionRequest(String filePath){
        try {
            parameters.put(API_KEY, API_KEY_VALUE);
            parameters.put(API_SECRET, API_SECRET_VALUE);
            parameters.put(NAME_SPACE, NAME_SPACE_VALUE);
            parameters.put(USER_ID, USER_ID_VALUE);
            File file = new File(filePath);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            String encoded = Base64.encode(bytes);
            parameters.put(FILE_PARAM, encoded);
        } catch (IOException ex) {
            Logger.getLogger(SSNFaceRecognitionRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public SSNFaceRecognitionRequest(byte[] file){
        try {
            parameters.put(API_KEY, API_KEY_VALUE);
            parameters.put(API_SECRET, API_SECRET_VALUE);
            parameters.put(NAME_SPACE, NAME_SPACE_VALUE);
            parameters.put(USER_ID, USER_ID_VALUE);
            //File file = new File(filePath);
            byte[] bytes = file;
            String encoded = Base64.encode(bytes);
            parameters.put(FILE_PARAM, encoded);
        } catch (Exception ex) {
            Logger.getLogger(SSNFaceRecognitionRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
