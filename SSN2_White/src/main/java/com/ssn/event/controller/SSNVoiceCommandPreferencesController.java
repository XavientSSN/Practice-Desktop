package com.ssn.event.controller;

import com.ssn.app.loader.SSNDialogChoice;
import com.ssn.dao.SSNDao;
import com.ssn.ui.custom.component.SSNMessageDialogBox;
import com.ssn.ui.form.SSNVoiceCommandPreferenceForm;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Controller for SSNVoiceCommandPreferenceForm
 * @author Abhinav
 */
public class SSNVoiceCommandPreferencesController extends SSNBaseController {
    private static Logger logger = Logger.getLogger(SSNVoiceCommandPreferencesController.class);
    
    private SSNVoiceCommandPreferenceForm preferencesForm = null;
    private Map<String, List<String>> commands = null;
    private List<String> allUniqueCommandList = new ArrayList<>();
    
    /**
     * constructor
     * @param preferencesForm
     * @param commands 
     */
    public SSNVoiceCommandPreferencesController(SSNVoiceCommandPreferenceForm preferencesForm, Map<String, List<String>> commands) {
        this.setPreferencesForm(preferencesForm);
        this.commands = commands;
        //Set<String> commandActions = commands.keySet();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // List<String> allExistingUniqueCommands = SSNDao.getAllExistingUniqueCommands();
        Map<String, String> updatedCommands = new HashMap<>();
        List<String> updatedCommandsList = new ArrayList<>();
        Set<String> commandActions = null;
        if(commands!=null){
            commandActions = commands.keySet();
        }
        Component[] components = getPreferencesForm().getSsnVoiceCommandPreferencePanel().getComponents();
        Map<String,List<String>> newCommandMap = new HashMap<>();
        Object actionEventObj = e.getSource();
        
        if (actionEventObj != null && actionEventObj instanceof JButton) {
            JButton jBtn = (JButton) actionEventObj;
            if (jBtn.getActionCommand().equalsIgnoreCase("updateCommandPreferences")) {
                List<String> duplicateCommand = new ArrayList<>();
                
                if(commands != null){
                    //List<String> newCommandList = null;
                    for(int i=0; i<components.length; i++) {
//                        logger.debug(components[i]);
//                        logger.debug(components[i].getName()+"\n");
                        if(commandActions.contains(components[i].getName())){
                            JTextField textField = (JTextField) components[i];
                            if(textField != null && !textField.getText().equalsIgnoreCase("")){
                                String text = textField.getText();
                                                               
                                // check if new commands already exists : start                                
                                String[] commandFromTextBox = text.split(",");
                                List lst = Arrays.asList(commandFromTextBox);
                                List<String>  newCommandList = new ArrayList<>(lst);
                                List<String> com = new ArrayList<String>();
                                for( String key : commandActions){
                                    if(!key.equals(components[i].getName())){
                                            com.addAll(commands.get(key));
                                    }
                                }
                                
                                List<String> tempList = new ArrayList<String>();
                                for(String command : newCommandList) {

                                    if(tempList.contains(command) || com.contains(command)) {
                                        duplicateCommand.add(command);
                                        break;
                                    } else {
                                        tempList.add(command);
                                    }
                                }
                                
                                if(duplicateCommand!=null && duplicateCommand.size()>0){
                                    break;
                                }else{
                                    
                                    updatedCommands.put(components[i].getName(), text);
                                    updatedCommandsList.add(text);
                                    newCommandMap.put(components[i].getName(), newCommandList);
                                }
                            }
                        }
                    }
                    if(duplicateCommand==null || duplicateCommand.isEmpty()){
                        writeFile(updatedCommandsList);
                        SSNDao.updateCustomVoiceCommand(newCommandMap);
                        getPreferencesForm().dispose();
                    }else{
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.ERROR_DIALOG.getType(), "Error", "", "Given command already exist\n "+duplicateCommand);
                    }
                }
                
            }
            if (jBtn.getActionCommand().equalsIgnoreCase("cancelVoicePreferences")) {
               getPreferencesForm().dispose();
            }
            if (jBtn.getActionCommand().equalsIgnoreCase("setDefaultPreferences")) {
                Map<String, List<String>> defaultCommands = null;
                try {
                    defaultCommands = SSNDao.getDefaultVoiceCommand();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SSNVoiceCommandPreferencesController.class.getName()).log(Level.SEVERE, null, ex);
                }
                // update the DB
                SSNDao.updateCustomVoiceCommand(defaultCommands);   
                List<String> commandList = new ArrayList<>();
                if(commands != null){                    
                    for(int i=0; i<components.length; i++) {
                        if(commandActions.contains(components[i].getName())){
                            JTextField textField = (JTextField) components[i];
                            commandList = defaultCommands.get(components[i].getName());
                            String command="";
                            for(String str : commandList){
                                command=command+str+",";
                            }
                            textField.setText(command);
                        }
                    }
                    
                   
                }
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // List<String> allExistingUniqueCommands = SSNDao.getAllExistingUniqueCommands();
        Map<String, String> updatedCommands = new HashMap<>();
        List<String> updatedCommandsList = new ArrayList<>();
        Set<String> commandActions = null;
        if(commands != null)
            commandActions = commands.keySet();
        Component[] components = getPreferencesForm().getSsnVoiceCommandPreferencePanel().getComponents();
        Map<String,List<String>> newCommandMap = new HashMap<>();
        Object actionEventObj = e.getSource();
        
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLabel = (JLabel) actionEventObj;
            if (jLabel.getName().equalsIgnoreCase("updateCommandPreferences")) {
                List<String> duplicateCommand = new ArrayList<>();
                
                if(commands != null){
                    //List<String> newCommandList = null;
                    for(int i=0; i<components.length; i++) {
                        logger.debug(components[i]);
                        logger.debug(components[i].getName()+"\n");
                        if(commandActions.contains(components[i].getName())){
                            JTextField textField = (JTextField) components[i];
                            if(textField != null && !textField.getText().equalsIgnoreCase("")){
                                String text = textField.getText();
                                                               
                                // check if new commands already exists : start                                
                                String[] commandFromTextBox = text.split(",");
                                List lst = Arrays.asList(commandFromTextBox);
                                List<String>  newCommandList = new ArrayList<>(lst);
                                List<String> com = new ArrayList<String>();
                                for( String key : commandActions){
                                    if(!key.equals(components[i].getName())){
                                            com.addAll(commands.get(key));
                                    }
                                }
                                
                                List<String> tempList = new ArrayList<String>();
                                for(String command : newCommandList) {

                                    if(tempList.contains(command) || com.contains(command)) {
                                        duplicateCommand.add(command);
                                        break;
                                    } else {
                                        tempList.add(command);
                                    }
                                }
                                
                                if(duplicateCommand!=null && duplicateCommand.size()>0){
                                    break;
                                }else{
                                    
                                    updatedCommands.put(components[i].getName(), text);
                                    updatedCommandsList.add(text);
                                    newCommandMap.put(components[i].getName(), newCommandList);
                                }
                            }
                        }
                    }
                    if(duplicateCommand==null || duplicateCommand.isEmpty()){
                        writeFile(updatedCommandsList);
                        SSNDao.updateCustomVoiceCommand(newCommandMap);
                        getPreferencesForm().dispose();
                    }else{
                        SSNMessageDialogBox dialogBox = new SSNMessageDialogBox();
                        dialogBox.initDialogBoxUI(SSNDialogChoice.ERROR_DIALOG.getType(), "Error", "", "Given command already exist\n "+duplicateCommand);
                    }
                }
                
            }
            if (jLabel.getName().equalsIgnoreCase("cancelVoicePreferences")) {
               getPreferencesForm().dispose();
            }
            if (jLabel.getName().equalsIgnoreCase("setDefaultPreferences")) {
                Map<String, List<String>> defaultCommands = null;
                try {
                    defaultCommands = SSNDao.getDefaultVoiceCommand();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SSNVoiceCommandPreferencesController.class.getName()).log(Level.SEVERE, null, ex);
                }
                // update the DB
                SSNDao.updateCustomVoiceCommand(defaultCommands);   
                List<String> commandList = new ArrayList<>();
                if(commands != null){                    
                    for(int i=0; i<components.length; i++) {
                        if(commandActions.contains(components[i].getName())){
                            JTextField textField = (JTextField) components[i];
                            commandList = defaultCommands.get(components[i].getName());
                            StringBuffer command=new StringBuffer("");
                            for(String str : commandList){
                                command.append(str);
                                command.append(",");
                            }
                            textField.setText(command.toString());
                        }
                    }
                    
                   
                }
            }

        }
        if (actionEventObj != null && actionEventObj instanceof JLabel) {
            JLabel jLbl = (JLabel) actionEventObj;
            if (jLbl.getName().equalsIgnoreCase("cancelVoicePreferences")) {
                getPreferencesForm().dispose();
            }
        }
    }

    public SSNVoiceCommandPreferenceForm getPreferencesForm() {
        return preferencesForm;
    }

    public void setPreferencesForm(SSNVoiceCommandPreferenceForm preferencesForm) {
        this.preferencesForm = preferencesForm;
    }
    
    /**
     * writes updatedCommandsList command/action to file ssn.gram 
     * @param updatedCommandsList 
     */
    private void writeFile(List<String> updatedCommandsList){
         //List<String> uniqueCommands = SSNDao.getAllExistingUniqueCommands();
         try {
                URL filePath = this.getClass().getResource("/config/ssn.gram");
                
                File inputFile = new File(filePath.toURI());
                File tempFile = new File("tmp.gram");

                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                
                
                String lineToRemove = "| --------;";
                String currentLine;
                String fileString = FileUtils.readFileToString(inputFile);
                String newCommand ="";
                while((currentLine = reader.readLine()) != null)
                {
                    if(!currentLine.trim().equalsIgnoreCase(lineToRemove)){
                        writer.write(currentLine+"\r\n"); 
                    }else{
                        for(String command : updatedCommandsList){
                            String[] newCommandList = command.split(",");
                            for(int i=0;i<newCommandList.length; i++){
//                                if(uniqueCommands.contains(newCommandList[i])){
//                                    return;
//                                }
                                if(!StringUtils.containsIgnoreCase(fileString, newCommandList[i])){
                                    newCommand = "\r\n| "+newCommandList[i];
                                    writer.write(newCommand);
                                }
                                
                            }
                        }
                        
                    }
                }  
                writer.write("\r\n"+lineToRemove);
                
                writer.flush();
                writer.close();
                reader.close();
                
                
                if(!inputFile.delete())
                {
                    logger.error(new Exception("Could not delete input file").getMessage());
                    return;
                }
                if(!tempFile.renameTo(inputFile)){
                    logger.error(new Exception("Could not rename file").getMessage());
                }
            }
            catch(Exception e)
            {
                logger.error(e.getLocalizedMessage());
            }
    }
    
}
