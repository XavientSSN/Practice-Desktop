/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.helper;

/**
 *
 * @author pkumar2
 */
public class SSNGoogleResponseHelper {
    private SSNResultHelper[] results ;
 private String status ;
 public SSNResultHelper[] getResults() {
  return results;
 }
 public void setResults(SSNResultHelper[] results) {
  this.results = results;
 }
 public String getStatus() {
  return status;
 }
 public void setStatus(String status) {
  this.status = status;
 }
 
}
