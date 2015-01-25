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
* LovPAPIAction
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-20
*/

public class LovPAPIAction extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovPAPIAction.class.getName());      
    
    private String sTitle;
    private HashMap<Integer, LovPAPIAttribute> hAttributes;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovPAPIAction() {
        
        this.sTitle = null;
        this.hAttributes = null;
        
    } 
    
   /**
    * @param sTitle title
    * @param hAttributes attributes
    */
    public LovPAPIAction(String sTitle, HashMap<Integer, LovPAPIAttribute> hAttributes) {
        
        this.sTitle = sTitle;
        this.hAttributes = hAttributes;
        
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
    * @return HashMap 
    */
    public HashMap<Integer, LovPAPIAttribute> getAttributes() {
        
        return this.hAttributes;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbPAPIAction = new StringBuilder();
        sbPAPIAction.append("|Title:").append(sTitle); 
        
        // attributes
        sbPAPIAction.append("|Attributes#");
        
        if (hAttributes != null) {
            
            for (Entry<Integer, LovPAPIAttribute> attr : hAttributes.entrySet())
                sbPAPIAction.append(attr.getValue().toString()).append("#");
        
        }          
        
        return sbPAPIAction.toString();
        
    }  
    
    /**
    * Get attribute
    * @param iID ID
    * @return LovPAPIAttribute
    */
    public LovPAPIAttribute getAttr(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getAttr() - params iID:").append(iID);
            logger.debug(sb);
            LovPAPIAttribute attr = hAttributes.get(iID);
            
            if (attr == null) {
                
                sb = new StringBuilder();
                sb.append("Attribute ").append(iID).append(" not found");
                logger.error(sb);
                
            }
            
            return attr;
            
        }
        catch (Exception ex) {
            
            logger.error("getAttr()", ex);
            
            return null;
        }
        
    }       
    
}