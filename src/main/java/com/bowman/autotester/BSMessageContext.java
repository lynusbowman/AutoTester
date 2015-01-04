package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS message context
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSMessageContext {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSMessageContext.class.getName());      
    
    private String sSender;
    private String sCorrelationId;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSMessageContext() {
        
        this.sSender = null;
        this.sCorrelationId = null;
        
    } 
    
    /**
     * @param sSender sender
     * @param sCorrelationId correlation ID 
     */
    public BSMessageContext(String sSender, String sCorrelationId) {
        
        this.sSender = sSender;
        this.sCorrelationId = sCorrelationId;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getSender() {
        
        return this.sSender;
        
    }
    
    /**  
    * @param sSender sender
    */
    public void setSender(String sSender) {
        
        this.sSender = sSender;
        
    }    
    
    /**
    * @return String 
    */
    public String getCorrelationId() {
        
        return this.sCorrelationId;
        
    }
    
    /**  
    * @param sCorrelationId correlation ID
    */
    public void setCorrelationId(String sCorrelationId) {
        
        this.sCorrelationId = sCorrelationId;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSMessageContext = new StringBuilder();
        sbBSMessageContext.append("Sender:").append(sSender);
        sbBSMessageContext.append("|CorrelationId:").append(sCorrelationId);
        
        return sbBSMessageContext.toString();
        
    }    
    
}