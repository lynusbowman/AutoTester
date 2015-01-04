package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS GetServiceStatusRes
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSGetServiceStatusRes extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSGetServiceStatusRes.class.getName());      

    private BSGetServiceStatusReq req;
    private BSMessageContext messageContext;
    private BSDataResp data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSGetServiceStatusRes() {
        
        this.req = null;
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param req req
     * @param messageContext message context
     * @param data data
     */
    public BSGetServiceStatusRes(BSGetServiceStatusReq req, BSMessageContext messageContext, BSDataResp data) {
        
        this.req = req;
        this.messageContext = messageContext;
        this.data = data;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
    /**
    * @return BSGetServiceStatusReq
    */
    public BSGetServiceStatusReq getReq() {
        
        return this.req;
        
    }
    
    /**  
    * @param req req
    */
    public void setReq(BSGetServiceStatusReq req) {
        
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
        
        StringBuilder sbBSGetServiceStatusRes = new StringBuilder();
        sbBSGetServiceStatusRes.append(req.toString()).append("\n");
        sbBSGetServiceStatusRes.append("BSGetServiceStatusRes#");
        sbBSGetServiceStatusRes.append("MessageContext#").append(messageContext.toString());
        sbBSGetServiceStatusRes.append("#Data#").append(data.toString());
        
        return sbBSGetServiceStatusRes.toString();
        
    }    
    
}