/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.ws.rest.request;

/**
 *
 * @author aarora1
 */
public enum SSNFaceRecognitionJobType {
    FACE_ADD("face_add"),
    FACE_RENAME("face_rename"), 
    FACE_TRAIN("face_train_sync"), 
    FACE_RECOGNIZE("face_recognize"),
    FACE_DELETE("face_delete");
    
    private final String job;
    
    SSNFaceRecognitionJobType(String job){
        this.job = job;
    }
    
    @Override
    public String toString() {
        return job;
    }
    
}
