package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS DeactivateServiceRes
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSDeactivateServiceRes extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSDeactivateServiceRes.class.getName());      

    private BSDeactivateServiceReq req;
    private BSMessageContext messageContext;
    private BSDataResp data;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSDeactivateServiceRes() {
        
        this.req = null;
        this.messageContext = null;
        this.data = null;
        
    } 
    
    /**
     * @param req req
     * @param messageContext message context
     * @param data data
     */
    public BSDeactivateServiceRes(BSDeactivateServiceReq req, BSMessageContext messageContext, BSDataResp data) {
        
        this.req = req;
        this.messageContext = messageContext;
        this.data = data;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
  
    /**
    * @return BSDeactivateServiceReq
    */
    public BSDeactivateServiceReq getReq() {
        
        return this.req;
        
    }
    
    /**  
    * @param req req
    */
    public void setMessageContext(BSDeactivateServiceReq req) {
        
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
        
        StringBuilder sbBSDeactivateServiceRes = new StringBuilder();
        sbBSDeactivateServiceRes.append(req.toString()).append("\n");
        sbBSDeactivateServiceRes.append("BSDeactivateServiceRes#");
        sbBSDeactivateServiceRes.append("MessageContext#").append(messageContext.toString());
        sbBSDeactivateServiceRes.append("#Data#").append(data.toString());
        
        return sbBSDeactivateServiceRes.toString();
        
    }    
    
}