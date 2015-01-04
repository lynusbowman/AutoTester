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
* Service
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-09 
*/

public class Service {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Service.class.getName());      
    
    private int iID;
    private String sTitle;
    private int iInstanceID;
    private String sStatus;
    private String sResource;
    private HashMap<Integer, String> hParameters;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Service() {
        
        this.iID = -1;
        this.sTitle = null;
        this.iInstanceID = -1;
        this.sStatus = null;
        this.sResource = null;
        this.hParameters = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title
     * @param iInstanceID instance ID
     * @param sStatus status
     * @param sResource resource
     * @param hParameters parameters
     */
    public Service(int iID, String sTitle, int iInstanceID, String sStatus, String sResource,
                   HashMap<Integer, String> hParameters) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.iInstanceID = iInstanceID;
        this.sStatus = sStatus;
        this.sResource = sResource;
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
    * @return int 
    */
    public int getInstanceID() {
        
        return this.iInstanceID;
        
    }
    
    /**  
    * @param iInstanceID instance ID
    */
    public void setInstanceID(int iInstanceID) {
        
        this.iInstanceID = iInstanceID;
        
    }  
    
    /**
    * @return String 
    */
    public String getStatus() {
        
        return this.sStatus;
        
    }
    
    /**  
    * @param sStatus status
    */ 
    public void setStatus(String sStatus) {
        
        this.sStatus = sStatus;
        
    }    
    
    /**
    * @return String 
    */
    public String getResource() {
        
        return this.sResource;
        
    }
    
    /**  
    * @param sResource resource
    */
    public void setResource(String sResource) {
        
        this.sResource = sResource;
        
    }   
    
    /**
    * @return HashMap 
    */
    public HashMap<Integer, String> getParameters() {
        
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
        
        StringBuilder sbService = new StringBuilder();
        sbService.append("ID:").append(iID);
        sbService.append("|Title:").append(sTitle);
        sbService.append("|InstanceID:").append(iInstanceID);
        sbService.append("|Status:").append(sStatus);
        sbService.append("|Resource:").append((sResource == null ? sbService.append("") : sbService.append(sResource)));
        sbService.append("|Parameters:");
        
        // parameters
        if (hParameters != null) {
            
            for (Entry<Integer, String> param : hParameters.entrySet())
                sbService.append(param.getKey()).append(":").append(param.getValue()).append("#");
        
        }
        
        return sbService.toString();
        
    }  
    
    /**
    * Get parameter
    * @param iParamID Parameter ID
    * @return String
    */
    public String getParam(int iParamID) {
        
        try {
            
            logger.debug("getParam() - params iParamID:" + iParamID);
            
            return hParameters.get(iParamID);
            
        }
        catch (NullPointerException ex) {
            
            logger.error("Param " + iParamID + " not found");
            
            return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getParam()", ex);
            
            return null;
        }
        
    }            
    
}