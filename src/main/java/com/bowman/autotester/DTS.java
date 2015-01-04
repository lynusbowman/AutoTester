package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* DTS
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-12 
*/

public class DTS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(DTS.class.getName());      
    
    private String sTitle;
    private HashMap<String, String> hParameters;
    private String sResult;
    private HashMap<String, String[]> hActions;
    private List<String> lstWarnings;
    
    /**********************************
              constructors
    **********************************/ 
    
    public DTS() {
        
        this.sTitle = null;
        this.hParameters = null;
        this.sResult = null;
        this.hActions = null;
        this.lstWarnings = null;
        
    } 
    
    /**
     * @param sTitle title
     * @param hParameters parameters
     * @param sResult result
     * @param hActions actions
     * @param lstWarnings warnings
     */
    public DTS(String sTitle, HashMap<String, String> hParameters, String sResult,
               HashMap<String, String[]> hActions, List<String> lstWarnings) {
        
        this.sTitle = sTitle;
        this.hParameters = hParameters;
        this.sResult = sResult;
        this.hActions = hActions;
        this.lstWarnings = lstWarnings;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
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
    * @return String 
    */
    public String getResult() {
        
        return this.sResult;
        
    }
    
    /**  
    * @param sResult result
    */
    public void setResult(String sResult) {
        
        this.sResult = sResult;
        
    }        
    
    /**
    * @return HashMap 
    */
    public HashMap<String, String> getParameters() {
        
        return this.hParameters;
        
    }   
    
    /**
    * @return HashMap 
    */
    public HashMap<String, String[]> getActions() {
        
        return this.hActions;
        
    }   
    
    /**
    * @return List 
    */
    public List<String> getWarnings() {
        
        return this.lstWarnings;
        
    }       
    
    /**********************************
                methods
    **********************************/         
    
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbDTS = new StringBuilder();
        sbDTS.append("Title:").append(sTitle);
        sbDTS.append("|Parameters:");
        
        // parameters
        if (hParameters != null) {
            
            for (Entry<String, String> param : hParameters.entrySet())
                sbDTS.append(param.getKey()).append(":").append(param.getValue()).append("#");
        
        }  
        
        sbDTS.append("|Result:").append(sResult);
        sbDTS.append("|Actions:");
        
        // actions
        if (hActions != null) {
            
            for (Entry<String, String[]> attr : hActions.entrySet())
                sbDTS.append(attr.getKey()).append(":").append(Arrays.toString(attr.getValue())).append("#");
            
        }
        
        // warnings
        sbDTS.append("|Warnings:");
        
        if (lstWarnings != null) {
            
            for (String sWarning : lstWarnings)
                sbDTS.append(sWarning).append("#");
            
        }        
        
        return sbDTS.toString();
        
    }  
    
    /**
    * Get parameter
    * @param sParamName Parameter name
    * @return String
    */
    public String getParam(String sParamName) {
        
        try {
            
            logger.debug("getParam() - params sParamName:" + sParamName);
            
            String sValue = hParameters.get(sParamName);
            
            if (sValue == null)
                logger.error("Param " + sParamName + " not found");
            
            return sValue;
            
        }
        catch (Exception ex) {
            
            logger.error("getParam()", ex);
            
            return null;
        }
        
    }    
    
    /**
    * Get action
    * @param sAction Action
    * @return List
    */
    public String[] getAction(String sAction) {
        
        try {
            
            logger.debug("getAction() - params sAction:" + sAction);
            
            String[] sActionParams = hActions.get(sAction);
            
            if (sActionParams == null)
                logger.error("Action " + sAction + " not found");
            
            return sActionParams;
            
        }
        catch (Exception ex) {
            
            logger.error("getAction()", ex);
            
            return null;
        }
        
    }         
    
}