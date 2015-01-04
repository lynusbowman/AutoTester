package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS data response
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSDataResp {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSDataResp.class.getName());      
    
    private BSServiceHeader serviceHeader;
    private List<BSAction> lstAllowedActions;
    private BSServiceStatus serviceStatus;
    private BSOmsStatus omsStatus;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSDataResp() {
        
        this.serviceHeader = null;
        this.lstAllowedActions = null;
        this.serviceStatus = null;
        this.omsStatus = null;
        
    } 
    
    /**
     * @param serviceHeader service header
     * @param lstAllowedActions allowed actions
     * @param serviceStatus service status
     * @param omsStatus OMS status
     */
    public BSDataResp(BSServiceHeader serviceHeader, List<BSAction> lstAllowedActions, BSServiceStatus serviceStatus,
                      BSOmsStatus omsStatus) {
        
        this.serviceHeader = serviceHeader;
        this.lstAllowedActions = lstAllowedActions;
        this.serviceStatus = serviceStatus;
        this.omsStatus = omsStatus;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/      
    
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
    * @return List
    */
    public List<BSAction> getAllowedActions() {
        
        return this.lstAllowedActions;
        
    }
    
    /**  
    * @param lstAllowedActions allowed actions
    */
    public void setAllowedActions(List<BSAction> lstAllowedActions) {
        
        this.lstAllowedActions = lstAllowedActions;
        
    }     
    
    /**
    * @return BSServiceStatus
    */
    public BSServiceStatus getServiceStatus() {
        
        return this.serviceStatus;
        
    }
    
    /**  
    * @param serviceStatus service status
    */
    public void setServiceStatus(BSServiceStatus serviceStatus) {
        
        this.serviceStatus = serviceStatus;
        
    }
    
    /**
    * @return BSOmsStatus
    */
    public BSOmsStatus getOmsStatus() {
        
        return this.omsStatus;
        
    }
    
    /**  
    * @param omsStatus OMS status
    */
    public void setOmsStatus(BSOmsStatus omsStatus) {
        
        this.omsStatus = omsStatus;
        
    }       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSDataResp = new StringBuilder();
        sbBSDataResp.append("ServiceHeader#").append(serviceHeader.toString()).append("#");
             
        if (lstAllowedActions != null) {
            
            sbBSDataResp.append("AllowedActions#");
            
            for (BSAction action : lstAllowedActions)
                sbBSDataResp.append("Action#").append(action.toString());
            
            sbBSDataResp.append("#");
        
        }
        
        if (serviceStatus != null)
            sbBSDataResp.append("ServiceStatus#").append(serviceStatus.toString()).append("#");
        
        if (omsStatus != null)
            sbBSDataResp.append("OmsStatus#").append(omsStatus.toString()).append("#");
        
        return sbBSDataResp.toString();
        
    }    
    
}