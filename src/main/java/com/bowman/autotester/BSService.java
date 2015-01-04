package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS service
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSService {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSService.class.getName());      
    
    private String sServiceName;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSService() {
        
        this.sServiceName = null;
        
    } 
    
    /**
     * @param sServiceName service name
     */
    public BSService(String sServiceName) {
        
        this.sServiceName = sServiceName;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
    /**
    * @return String 
    */
    public String getServiceName() {
        
        return this.sServiceName;
        
    }
    
    /**  
    * @param sServiceName service name
    */
    public void setServiceName(String sServiceName) {
        
        this.sServiceName = sServiceName;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSService = new StringBuilder();
        sbBSService.append("ServiceName:").append(sServiceName);
        
        return sbBSService.toString();
        
    }    
    
}