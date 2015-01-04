package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS action
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSAction {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSAction.class.getName());      
    
    private boolean bAllowed;
    private String sActionName;
    private List<String> lstBreData;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSAction() {
        
        this.bAllowed = false;
        this.sActionName = null;
        this.lstBreData = null;
        
    } 
    
    /**
     * @param bAllowed allowed
     * @param sActionName action name
     * @param lstBreData Bre data
     */
    public BSAction(boolean bAllowed, String sActionName, List<String> lstBreData) {
        
        this.bAllowed = bAllowed;
        this.sActionName = sActionName;
        this.lstBreData = lstBreData;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return boolean 
    */
    public boolean getAllowed() {
        
        return this.bAllowed;
        
    }
    
    /**  
    * @param bAllowed allowed
    */
    public void setAllowed(boolean bAllowed) {
        
        this.bAllowed = bAllowed;
        
    }         
    
    /**
    * @return String
    */
    public String getActionName() {
        
        return this.sActionName;
        
    }
    
    /**  
    * @param sActionName action name
    */
    public void setActionName(String sActionName) {
        
        this.sActionName = sActionName;
        
    }    
    
    /**
    * @return List 
    */
    public List<String> getBreData() {
        
        return this.lstBreData;
        
    }
    
    /**  
    * @param lstBreData Bre data
    */
    public void setBreData(List<String> lstBreData) {
        
        this.lstBreData = lstBreData;
        
    }        
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSAction = new StringBuilder();
        sbBSAction.append("Allowed:").append(bAllowed);
        sbBSAction.append("|ActionName:").append(sActionName);
        sbBSAction.append("|BreData#");
        
        for (String sReason : lstBreData)
            sbBSAction.append("Reason:").append(sReason).append("|");
        
        return sbBSAction.toString();
        
    }    
    
}