package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS DeactivateServiceReq
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSDeactivateServiceReq extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSDeactivateServiceReq.class.getName());      

    private BSMessageContext messageContext;
    private BSDataReq data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSDeactivateServiceReq() {
        
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param messageContext message context
     * @param data data
     */
    public BSDeactivateServiceReq(BSMessageContext messageContext, BSDataReq data) {
        
        this.messageContext = messageContext;
        this.data = data;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
    /**
    * @return BSMessageContext
    */
    public BSMessageContext getMessageContext() {
        
        return this.messageContext;
        
    }
    
    /**  
    * @param messageContext message context
    */
    public void setMessageContext(BSMessageContext messageContext) {
        
        this.messageContext = messageContext;
        
    }    
    
    /**
    * @return BSDataReq
    */
    public BSDataReq getData() {
        
        return this.data;
        
    }
    
    /**  
    * @param data data
    */
    public void setData(BSDataReq data) {
        
        this.data = data;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSDeactivateServiceReq = new StringBuilder();
        sbBSDeactivateServiceReq.append("BSDeactivateServiceReq#");
        sbBSDeactivateServiceReq.append("MessageContext#").append(messageContext.toString());
        sbBSDeactivateServiceReq.append("#Data#").append(data.toString());
        
        return sbBSDeactivateServiceReq.toString();
        
    }    
    
}