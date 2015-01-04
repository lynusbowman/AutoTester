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
* Notification
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-11
*/

public class Notification {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Notification.class.getName());      
    
    private int iID;
    private String sTemplate;
    private int iStatus;
    private HashMap<String, String> hParameters;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Notification() {
        
        this.iID = -1;
        this.sTemplate = null;
        this.iStatus = -1;
        this.hParameters = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTemplate template
     * @param iStatus status
     * @param hParameters parameters
     */
    public Notification(int iID, String sTemplate, int iStatus, HashMap<String, String> hParameters) {
        
        this.iID = iID;
        this.sTemplate = sTemplate;
        this.iStatus = iStatus;
        this.hParameters = hParameters;
        
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
    public String getTemplate() {
        
        return this.sTemplate;
        
    }
    
    /**  
    * @param sTemplate template
    */
    public void setTemplate(String sTemplate) {
        
        this.sTemplate = sTemplate;
        
    }   
    
    /**
    * @return int 
    */
    public int getStatus() {
        
        return this.iStatus;
        
    }
    
    /**  
    * @param iStatus status
    */
    public void setStatus(int iStatus) {
        
        this.iStatus = iStatus;
        
    }  
    
    /**
    * @return HashMap 
    */
    public HashMap<String, String> getParameters() {
        
        return this.hParameters;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbNotification = new StringBuilder();
        sbNotification.append("ID:").append(iID);
        sbNotification.append("|Template:").append(sTemplate);
        sbNotification.append("|Status:").append(iStatus);
        
        // output attributes
        sbNotification.append("|Parameters:");
        
        if (hParameters != null) {
            
            for (Entry<String, String> attr : hParameters.entrySet())
                sbNotification.append(attr.getKey()).append(":").append(attr.getValue()).append("#");
        
        }            
        
        return sbNotification.toString();
        
    }    
    
    /**
    * Get parameter
    * @param sParamName Parameter name
    * @return String
    */
    public String getParam(String sParamName) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getParam() - params sParamName:").append(sParamName);
            logger.debug(sb);
            String sValue = hParameters.get(sParamName);
            
            if (sValue == null) {
                
                sb = new StringBuilder();
                sb.append("Param ").append(sParamName).append(" not found");
                logger.error(sb);
                
            }
            
            return sValue;
            
        }
        catch (Exception ex) {
            
            logger.error("getParam()", ex);
            
            return null;
        }
        
    }        
    
}