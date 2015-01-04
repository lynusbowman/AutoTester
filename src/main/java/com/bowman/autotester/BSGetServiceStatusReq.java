package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS GetServiceStatusReq
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSGetServiceStatusReq extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSGetServiceStatusReq.class.getName());      

    private BSMessageContext messageContext;
    private BSDataReq data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSGetServiceStatusReq() {
        
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param messageContext message context
     * @param data data
     */
    public BSGetServiceStatusReq(BSMessageContext messageContext, BSDataReq data) {
        
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
        
        StringBuilder sbBSGetServiceStatusReq = new StringBuilder();
        sbBSGetServiceStatusReq.append("BSGetServiceStatusReq#");
        sbBSGetServiceStatusReq.append("MessageContext#").append(messageContext.toString());
        sbBSGetServiceStatusReq.append("#Data#").append(data.toString());
        
        return sbBSGetServiceStatusReq.toString();
        
    }    
    
}