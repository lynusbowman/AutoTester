package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS ModifyServiceRes
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-26 
*/

public class BSModifyServiceRes extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSModifyServiceRes.class.getName());      

    private BSModifyServiceReq req;
    private BSMessageContext messageContext;
    private BSDataResp data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSModifyServiceRes() {
        
        this.req = null;
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param req req
     * @param messageContext message context
     * @param data data
     */
    public BSModifyServiceRes(BSModifyServiceReq req, BSMessageContext messageContext, BSDataResp data) {
        
        this.req = req;
        this.messageContext = messageContext;
        this.data = data;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
  
    /**
    * @return BSModifyServiceReq
    */
    public BSModifyServiceReq getReq() {
        
        return this.req;
        
    }
    
    /**  
    * @param req req
    */
    public void setReq(BSModifyServiceReq req) {
        
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
        
        StringBuilder sbBSModifyServiceRes = new StringBuilder();
        sbBSModifyServiceRes.append(req.toString()).append("\n");
        sbBSModifyServiceRes.append("BSModifyServiceRes#");
        sbBSModifyServiceRes.append("MessageContext#").append(messageContext.toString());
        sbBSModifyServiceRes.append("#Data#").append(data.toString());
        
        return sbBSModifyServiceRes.toString();
        
    }    
    
}