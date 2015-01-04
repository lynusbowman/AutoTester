package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS ActivateServiceRes
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSActivateServiceRes extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSActivateServiceRes.class.getName());      

    private BSActivateServiceReq req;
    private BSMessageContext messageContext;
    private BSDataResp data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSActivateServiceRes() {
        
        this.req = null;
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param req req
     * @param messageContext message context
     * @param data data
     */
    public BSActivateServiceRes(BSActivateServiceReq req, BSMessageContext messageContext, BSDataResp data) {
        
        this.req = req;
        this.messageContext = messageContext;
        this.data = data;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
  
    /**
    * @return BSActivateServiceReq
    */
    public BSActivateServiceReq getReq() {
        
        return this.req;
        
    }
    
    /**  
    * @param req req
    */
    public void setReq(BSActivateServiceReq req) {
        
        this.req = req;
        
    }     
    
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
    * @return BSDataResp
    */
    public BSDataResp getData() {
        
        return this.data;
        
    }
    
    /**  
    * @param data data
    */
    public void setData(BSDataResp data) {
        
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
        
        StringBuilder sbBSActivateServiceRes = new StringBuilder();
        sbBSActivateServiceRes.append(req.toString()).append("\n");
        sbBSActivateServiceRes.append("BSActivateServiceRes#");
        sbBSActivateServiceRes.append("MessageContext#").append(messageContext.toString());
        sbBSActivateServiceRes.append("#Data#").append(data.toString());
        
        return sbBSActivateServiceRes.toString();
        
    }    
    
}