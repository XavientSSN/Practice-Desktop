/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssn.ws.rest.request.SSNFaceRecognitionRequest;
import com.ssn.ws.rest.response.SSNFaceRecognitionResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;


/**
 *
 * @author aarora1
 */
public class SSNFaceRecognitionService {
    
    private        String                  REST_ENDPOINT         = "";
    private        String                  hostName              = "http://rekognition.com/";
    private        String                  serviceName           = "func/api/";
   
    public SSNFaceRecognitionService(){
        this.REST_ENDPOINT = getHostName() + getServiceName();
    }
    
    
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

     public  SSNFaceRecognitionResponse getResponse(SSNFaceRecognitionRequest request) {
         SSNFaceRecognitionResponse ssnFaceRecognitionResponse = null;
        try {
            
            Client client = Client.create();
            WebResource webResource = client
                    .resource(REST_ENDPOINT);
            Map<String,String> parameterMap = request.getParameters();
            Form f = new Form();
            for(String parameterKey:parameterMap.keySet()){
                String parameterValue = parameterMap.get(parameterKey);
                 f.add(parameterKey, parameterValue);
            }  
            ClientResponse response = webResource.accept("application/json")
                    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, f);
            StringBuilder responseStr=new StringBuilder();
            String line= "";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(response.getEntityInputStream()));
            while (( line = bufferReader.readLine()) != null) {
                responseStr.append(line );
                responseStr.append("\n");
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ssnFaceRecognitionResponse=  gson.fromJson(responseStr.toString(), SSNFaceRecognitionResponse.class);
   
                    } catch (IOException ex) {
            Logger.getLogger(SSNFaceRecognitionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ssnFaceRecognitionResponse;
    }
     
     
}
