package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS ModifyServiceReq
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-26 
*/

public class BSModifyServiceReq extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSModifyServiceReq.class.getName());      

    private BSMessageContext messageContext;
    private BSDataReq data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSModifyServiceReq() {
        
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param messageContext message context
     * @param data data
     */
    public BSModifyServiceReq(BSMessageContext messageContext, BSDataReq data) {
        
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
        
        StringBuilder sbBSModifyServiceReq = new StringBuilder();
        sbBSModifyServiceReq.append("BSModifyServiceReq#");
        sbBSModifyServiceReq.append("MessageContext#").append(messageContext.toString());
        sbBSModifyServiceReq.append("#Data#").append(data.toString());
        
        return sbBSModifyServiceReq.toString();
        
    }    
    
}