package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovPAPI
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-20
*/

public class LovPAPI extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovPAPI.class.getName());      
    
    private int iID;
    private String sTitle;
    private HashMap<String, LovPAPIAction> hActions;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovPAPI() {
        
        this.iID = -1;
        this.sTitle = null;
        this.hActions = null;
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param hActions actions
    */
    public LovPAPI(int iID, String sTitle, HashMap<String, LovPAPIAction> hActions) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.hActions = hActions;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return int 
    */
    public int getID() {
        
        return this.iID;
        
    }
    
    /**  
    * @param iID ID
    */
    public void setID(int iID) {
        
        this.iID = iID;
        
    }    
    
    /**
    * @return String 
    */
    public String getTitle() {
        
        return this.sTitle;
        
    }
    
    /**  
    * @param sTitle title
    */
    public void setTitle(String sTitle) {
        
        this.sTitle = sTitle;
        
    }  
    
    /**
    * @return HashMap 
    */
    public HashMap<String, LovPAPIAction> getActions() {
        
        return this.hActions;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbPAPI = new StringBuilder();
        sbPAPI.append("ID:").append(iID);
        sbPAPI.append("|Title:").append(sTitle); 
        
        // actions
        sbPAPI.append("|Actions#");
        
        if (hActions != null) {
            
            for (Entry<String, LovPAPIAction> action : hActions.entrySet())
                sbPAPI.append(action.getValue().toString()).append("#");
        
        }          
        
        return sbPAPI.toString();
        
    }   
    
    /**
    * Get action
    * @param sAction action
    * @return LovPAPIAction
    */
    public LovPAPIAction getAction(String sAction) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getAction() - params sAction:").append(sAction);
            logger.debug(sb);
            LovPAPIAction action = hActions.get(sAction);
            
            if (action == null) {
                
                sb = new StringBuilder();
                sb.append("Action ").append(sAction).append(" not found");
                logger.error(sb);
                
            }
            
            return action;
            
        }
        catch (Exception ex) {
            
            logger.error("getAction()", ex);
            
            return null;
        }
        
    }         
    
}