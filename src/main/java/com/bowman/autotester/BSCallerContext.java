package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS caller context
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSCallerContext {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSCallerContext.class.getName());      
    
    private int iCallerId;
    private String sCallerType;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSCallerContext() {
        
        this.iCallerId = -1;
        this.sCallerType = null;
        
    } 
    
    /**
     * @param iCallerId caller ID
     * @param sCallerType caller type
     */
    public BSCallerContext(int iCallerId, String sCallerType) {
        
        this.iCallerId = iCallerId;
        this.sCallerType = sCallerType;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public int getCallerId() {
        
        return this.iCallerId;
        
    }
    
    /**  
    * @param iCallerId calller ID
    */
    public void setCallerId(int iCallerId) {
        
        this.iCallerId = iCallerId;
        
    }    
    
    /**
    * @return String 
    */
    public String getCallerType() {
        
        return this.sCallerType;
        
    }
    
    /**  
    * @param sCallerType caller type
    */
    public void setCallerType(String sCallerType) {
        
        this.sCallerType = sCallerType;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSCallerContext = new StringBuilder();
        sbBSCallerContext.append("CallerId:").append(iCallerId);
        sbBSCallerContext.append("|CallerType:").append(sCallerType);
        
        return sbBSCallerContext.toString();
        
    }    
    
}