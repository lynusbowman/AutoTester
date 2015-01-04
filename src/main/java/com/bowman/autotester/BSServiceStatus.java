package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS service status
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22
*/

public class BSServiceStatus {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSServiceStatus.class.getName());      
    
    private String sStatus;
    private BSSAParameters saParameters;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSServiceStatus() {
        
        this.sStatus = null;
        this.saParameters = null;
        
    } 
    
    /**
     * @param sStatus status
     * @param saParameters SA parameters
     */
    public BSServiceStatus(String sStatus, BSSAParameters saParameters) {
        
        this.sStatus = sStatus;
        this.saParameters = saParameters;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
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
    * @return BSSAParameters 
    */
    public BSSAParameters getSAParameters() {
        
        return this.saParameters;
        
    }
    
    /**  
    * @param saParameters SA parameters
    */
    public void setSAParameters(BSSAParameters saParameters) {
        
        this.saParameters = saParameters;
        
    }      
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSServiceStatus = new StringBuilder();
        sbBSServiceStatus.append("Status:").append(sStatus);
        sbBSServiceStatus.append("|SAParameters#").append(saParameters.toString());
        
        return sbBSServiceStatus.toString();
        
    }    
    
}