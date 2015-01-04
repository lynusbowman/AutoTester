package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS SA parameter
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-26 
*/

public class BSSAParameter {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSSAParameter.class.getName());      
    
    private String sName;
    private String sValue;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSSAParameter() {
        
        this.sName = null;
        this.sValue = null;
        
    } 
    
    /**
     * @param sName name
     * @param sValue value
     */
    public BSSAParameter(String sName, String sValue) {
        
        this.sName = sName;
        this.sValue = sValue;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getName() {
        
        return this.sName;
        
    }
    
    /**  
    * @param sName name
    */
    public void setName(String sName) {
        
        this.sName = sName;
        
    }    
    
    /**
    * @return String 
    */
    public String getValue() {
        
        return this.sValue;
        
    }
    
    /**  
    * @param sValue value
    */
    public void setValue(String sValue) {
        
        this.sValue = sValue;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSSAParameter = new StringBuilder();
        sbBSSAParameter.append("Name:").append(sName);
        sbBSSAParameter.append("|Value:").append(sValue);
        
        return sbBSSAParameter.toString();
        
    }    
    
}