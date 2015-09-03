/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ssn.helper;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author pkumar2
 */
public class SSNResultHelper {
 private String formatted_address;
 
 private boolean partial_match;
 
 private SSNGeometryHelper geometry;
 
 @JsonIgnore
 private Object address_components;
 
 @JsonIgnore
 private Object types;

 public String getFormatted_address() {
  return formatted_address;
 }

 public void setFormatted_address(String formatted_address) {
  this.formatted_address = formatted_address;
 }

 public boolean isPartial_match() {
  return partial_match;
 }

 public void setPartial_match(boolean partial_match) {
  this.partial_match = partial_match;
 }

 public SSNGeometryHelper getGeometry() {
  return geometry;
 }

 public void setGeometry(SSNGeometryHelper geometry) {
  this.geometry = geometry;
 }

 public Object getAddress_components() {
  return address_components;
 }

 public void setAddress_components(Object address_components) {
  this.address_components = address_components;
 }

 public Object getTypes() {
  return types;
 }

 public void setTypes(Object types) {
  this.types = types;
 }
}
