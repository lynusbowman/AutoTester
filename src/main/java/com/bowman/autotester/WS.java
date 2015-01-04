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
* Web service
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(WS.class.getName());      
    
    /**********************************
              constructors
    **********************************/ 
    
    public WS() {        
        
    } 
    
    /**********************************
                methods
    **********************************/  
  
    /**
    * Get correlation Id
    * @return String
    */
    public String getCorrelationId() {
        
        try {
            
            logger.debug("getCorrelationId()");
            
            String sCorrelationId = null;
            
            // get
            if (this instanceof BSGetServiceStatusRes) {
             
                BSGetServiceStatusRes wsRes = (BSGetServiceStatusRes) this;
                sCorrelationId = wsRes.getMessageContext().getCorrelationId();
                
            }
            // activate
            else if (this instanceof BSActivateServiceRes) {
             
                BSActivateServiceRes wsRes = (BSActivateServiceRes) this;
                sCorrelationId = wsRes.getMessageContext().getCorrelationId();
                
            }
            // modify
            else if (this instanceof BSModifyServiceRes) {
             
                BSModifyServiceRes wsRes = (BSModifyServiceRes) this;
                sCorrelationId = wsRes.getMessageContext().getCorrelationId();
                
            }     
            // deactivate
            else if (this instanceof BSDeactivateServiceRes) {
             
                BSDeactivateServiceRes wsRes = (BSDeactivateServiceRes) this;
                sCorrelationId = wsRes.getMessageContext().getCorrelationId();
                
            }    
            // fault
            else if (this instanceof BSFault) {
             
                BSFault wsFault = (BSFault) this;
                sCorrelationId = wsFault.getMessageContext().getCorrelationId();
                
            }            
                
            return sCorrelationId;
            
        }
        catch (Exception ex) {
            
            logger.error("getCorrelationId()", ex);
            
            return null;
        }
        
    }      
    
    /**
    * Get status
    * @return String
    */
    public String getStatus() {
        
        try {
            
            logger.debug("getStatus()");
            
            String sStatus = null;
            
            // get
            if (this instanceof BSGetServiceStatusRes) {
             
                BSGetServiceStatusRes wsRes = (BSGetServiceStatusRes) this;
                sStatus = wsRes.getData().getServiceStatus().getStatus();
                
            }
                
            return sStatus;
            
        }
        catch (Exception ex) {
            
            logger.error("getStatus()", ex);
            
            return null;
        }
        
    }  
    
    /**
    * Get allowed actions
    * @return List
    */
    public List<BSAction> getAllowedActions() {
        
        try {
            
            logger.debug("getAllowedActions()");
            
            List<BSAction> lstAllowedActions = null;
            
            // get
            if (this instanceof BSGetServiceStatusRes) {
             
                BSGetServiceStatusRes wsRes = (BSGetServiceStatusRes) this;
                lstAllowedActions = wsRes.getData().getAllowedActions();
                
            }
                
            return lstAllowedActions;
            
        }
        catch (Exception ex) {
            
            logger.error("getAllowedActions()", ex);
            
            return null;
        }
        
    }    
    
    /**
    * Get allowed action
    * @param sAction action
    * @return BSAction
    */
    public BSAction getAllowedAction(String sAction) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getAllowedAction() - params sParam:").append(sAction);
            logger.debug(sb);
            
            List<BSAction> lstAllowedActions = getAllowedActions();
            
            for (BSAction action : lstAllowedActions) {
                
                if (action.getActionName().equals(sAction))
                    return action;
                
            }
            
            sb = new StringBuilder();
            sb.append("Action ").append(sAction).append(" not found");
            logger.error(sb);
            
            return null;

            
        }
        catch (Exception ex) {
            
            logger.error("getAllowedAction()", ex);
            
            return null;
        }
        
    }     
    
    /**
    * Get service specific parameters
    * @return List
    */
    public List<BSSAParameter> getServiceParams() {
        
        try {
            
            logger.debug("getServiceParams()");
            
            List<BSSAParameter> lstServiceSpecificParameters = null;
            
            // get
            if (this instanceof BSGetServiceStatusRes) {
             
                BSGetServiceStatusRes wsRes = (BSGetServiceStatusRes) this;
                lstServiceSpecificParameters = wsRes.getData().getServiceStatus().getSAParameters().getGenericServiceSpecificParameters();
                
            }
            // activate
            else if (this instanceof BSActivateServiceRes) {
             
                BSActivateServiceRes wsRes = (BSActivateServiceRes) this;
                lstServiceSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericServiceSpecificParameters();
                
            }   
            // modify 
            else if (this instanceof BSModifyServiceRes) {
             
                BSModifyServiceRes wsRes = (BSModifyServiceRes) this;
                lstServiceSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericServiceSpecificParameters();
                
            }   
            // deactivate
            if (this instanceof BSDeactivateServiceRes) {
             
                BSDeactivateServiceRes wsRes = (BSDeactivateServiceRes) this;
                lstServiceSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericServiceSpecificParameters();
                
            }               
                
            return lstServiceSpecificParameters;
            
        }
        catch (Exception ex) {
            
            logger.error("getServiceParams()", ex);
            
            return null;
        }
        
    }  
    
    /**
    * Get service specific parameter
    * @param sParam param
    * @return HashMap
    */
    public String getServiceParam(String sParam) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getServiceParam() - params sParam:").append(sParam);
            logger.debug(sb);
            
            List<BSSAParameter> lstServiceParams = getServiceParams();
            
            for (BSSAParameter saParameter : lstServiceParams) {
                
                if (saParameter.getName().equals(sParam))
                    return saParameter.getValue();
                
            }
            
            sb = new StringBuilder();
            sb.append("Param ").append(sParam).append(" not found");
            logger.error(sb);
            
            return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getServiceParam()", ex);
            
            return null;
        }
        
    }     
    
    /**
    * Get action params
    * @return List
    */
    public List<BSSAParameter> getActionParams() {
        
        try {
            
            logger.debug("getActionParams()");
            
            List<BSSAParameter> lstActionSpecificParameters = null;
            
            // get
            if (this instanceof BSGetServiceStatusRes) {
             
                BSGetServiceStatusRes wsRes = (BSGetServiceStatusRes) this;
                lstActionSpecificParameters = wsRes.getData().getServiceStatus().getSAParameters().getGenericActionSpecificParameters();
                
            }
            // activate
            else if (this instanceof BSActivateServiceRes) {
             
                BSActivateServiceRes wsRes = (BSActivateServiceRes) this;
                lstActionSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericActionSpecificParameters();
                
            }
            // modify
            else if (this instanceof BSModifyServiceRes) {
             
                BSModifyServiceRes wsRes = (BSModifyServiceRes) this;
                lstActionSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericActionSpecificParameters();
                
            }
            // deactivate
            else if (this instanceof BSDeactivateServiceRes) {
             
                BSDeactivateServiceRes wsRes = (BSDeactivateServiceRes) this;
                lstActionSpecificParameters = wsRes.getReq().getData().getSAParameters().getGenericActionSpecificParameters();
                
            }            
                
            return lstActionSpecificParameters;
            
        }
        catch (Exception ex) {
            
            logger.error("getActionParams()", ex);
            
            return null;
        }
        
    }  
    
    /**
    * Get action specific parameter
    * @param sParam param
    * @return HashMap
    */
    public String getActionParam(String sParam) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getActionParam() - params sParam:").append(sParam);
            logger.debug(sb);
            
            List<BSSAParameter> lstActionParams = getActionParams();
            
            for (BSSAParameter saParameter : lstActionParams) {
                
                if (saParameter.getName().equals(sParam))
                    return saParameter.getValue();
                
            }
            
            sb = new StringBuilder();
            sb.append("Param ").append(sParam).append(" not found");
            logger.error(sb);
            
            return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getActionParam()", ex);
            
            return null;
        }
        
    }      
    
    /**
    * Get order status
    * @return String
    */
    public String getOrderStatus() {
        
        try {
            
            logger.debug("getOrderStatus()");
            
            String sOrderStatus = null;
            
            // activate
            if (this instanceof BSActivateServiceRes) {
             
                BSActivateServiceRes wsRes = (BSActivateServiceRes) this;
                sOrderStatus = wsRes.getData().getOmsStatus().getOrderStatus();
                
            }
            // modify
            else if (this instanceof BSModifyServiceRes) {
             
                BSModifyServiceRes wsRes = (BSModifyServiceRes) this;
                sOrderStatus = wsRes.getData().getOmsStatus().getOrderStatus();
                
            }            
            // deactivate
            else if (this instanceof BSDeactivateServiceRes) {
             
                BSDeactivateServiceRes wsRes = (BSDeactivateServiceRes) this;
                sOrderStatus = wsRes.getData().getOmsStatus().getOrderStatus();
                
            }
                
            return sOrderStatus;
            
        }
        catch (Exception ex) {
            
            logger.error("getOrderStatus()", ex);
            
            return null;
        }
        
    }      
    
    /**
    * Get exception class
    * @return String
    */
    public String getExceptionClass() {
        
        try {
            
            logger.debug("getExceptionClass()");
            
            String sExceptionClass = null;
            
            // fault
            if (this instanceof BSFault) {
             
                BSFault wsFault = (BSFault) this;
                sExceptionClass = wsFault.getExceptionClass();
                
            }
                
            return sExceptionClass;
            
        }
        catch (Exception ex) {
            
            logger.error("getExceptionClass()", ex);
            
            return null;
        }
        
    }    
    
    /**
    * Get further info
    * @return String
    */
    public String getFurtherInfo() {
        
        try {
            
            logger.debug("getFurtherInfo()");
            
            String sFurtherInfo = null;
            
            // fault
            if (this instanceof BSFault) {
             
                BSFault wsFault = (BSFault) this;
                sFurtherInfo = wsFault.getFurtherInfo();
                
            }
                
            return sFurtherInfo;
            
        }
        catch (Exception ex) {
            
            logger.error("getFurtherInfo()", ex);
            
            return null;
        }
        
    }      
    
}