package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS target context
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSTargetContext {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSTargetContext.class.getName());      
    
    private int iTargetId;
    private String sTargetType;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSTargetContext() {
        
        this.iTargetId = -1;
        this.sTargetType = null;
        
    } 
    
    /**
     * @param iTargetId target ID
     * @param sTargetType target type
     */
    public BSTargetContext(int iTargetId, String sTargetType) {
        
        this.iTargetId = iTargetId;
        this.sTargetType = sTargetType;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public int getTargetId() {
        
        return this.iTargetId;
        
    }
    
    /**  
    * @param iTargetId target ID
    */
    public void setTargetId(int iTargetId) {
        
        this.iTargetId = iTargetId;
        
    }    
    
    /**
    * @return String 
    */
    public String getTargetType() {
        
        return this.sTargetType;
        
    }
    
    /**  
    * @param sTargetType target type
    */
    public void setTargetType(String sTargetType) {
        
        this.sTargetType = sTargetType;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSTargetContext = new StringBuilder();
        sbBSTargetContext.append("TargetId:").append(iTargetId);
        sbBSTargetContext.append("|TargetType:").append(sTargetType);
        
        return sbBSTargetContext.toString();
        
    }    
    
}