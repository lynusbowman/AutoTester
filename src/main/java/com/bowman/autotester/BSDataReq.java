package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS data request
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSDataReq {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSDataReq.class.getName());      
    
    private BSContext context;
    private BSServiceHeader serviceHeader;
    private boolean bValidateOnly;
    private BSService service;
    private BSSAParameters saParameters;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSDataReq() {
        
        this.context = null;
        this.serviceHeader = null;
        this.bValidateOnly = false;
        this.service = null;
        this.saParameters = null;
        
    } 
    
    /**
     * @param context context
     * @param serviceHeader service header
     * @param bValidateOnly validate only
     * @param service service
     * @param saParameters SA parameters
     */
    public BSDataReq(BSContext context, BSServiceHeader serviceHeader, boolean bValidateOnly, BSService service,
                     BSSAParameters saParameters) {
        
        this.context = context;
        this.serviceHeader = serviceHeader;
        this.bValidateOnly = bValidateOnly;
        this.service = service;
        this.saParameters = saParameters;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
    /**
    * @return BSContext
    */
    public BSContext getContext() {
        
        return this.context;
        
    }
    
    /**  
    * @param context context
    */
    public void setContext(BSContext context) {
        
        this.context = context;
        
    }    
    
    /**
    * @return BSServiceHeader
    */
    public BSServiceHeader getServiceHeader() {
        
        return this.serviceHeader;
        
    }
    
    /**  
    * @param serviceHeader service header
    */
    public void setServiceHeader(BSServiceHeader serviceHeader) {
        
        this.serviceHeader = serviceHeader;
        
    } 
    
    /**
    * @return boolean
    */
    public boolean getValidateOnly() {
        
        return this.bValidateOnly;
        
    }
    
    /**  
    * @param bValidateOnly validate only
    */
    public void setValidateOnly(boolean bValidateOnly) {
        
        this.bValidateOnly = bValidateOnly;
        
    }        
    
    /**
    * @return BSService
    */
    public BSService getService() {
        
        return this.service;
        
    }
    
    /**  
    * @param service service
    */
    public void setService(BSService service) {
        
        this.service = service;
        
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
        
        StringBuilder sbBSDataReq = new StringBuilder();
        sbBSDataReq.append("Context#").append(context.toString());
        sbBSDataReq.append("|ServiceHeader#").append(serviceHeader.toString());
        sbBSDataReq.append("|ValidateOnly:").append(bValidateOnly);
        sbBSDataReq.append("|Service#").append(service.toString());
        sbBSDataReq.append("|SAParameters#").append(saParameters.toString());
        
        return sbBSDataReq.toString();
        
    }    
    
}