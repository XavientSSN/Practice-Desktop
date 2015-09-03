
package com.ssn.app.loader;

/**
 *
 * @author pkumar2
 */
public enum SSNDialogChoice {
       ERROR_DIALOG("ERROR"), NOTIFICATION_DIALOG("NOTIFICATION");
 
	final private String type;
 
	private SSNDialogChoice(String choice) {
		type =choice;
	}
 
	public String getType() {
		return type;
	}
}
