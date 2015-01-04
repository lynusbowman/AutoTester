package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Utilities for web services
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class UtilitiesWS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesWS.class.getName());  
        
    private Utilities util;    
    private WSBean wsBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesWS() {
        
        this.util = null;
        this.wsBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param wsBean WSBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesWS(Utilities util, WSBean wsBean, int iLogID) {
        
        this.util = util;
        this.wsBean = wsBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/ 
    
    /**
    * Connect to WS
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            wsBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to WS");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to WS on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from WS
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            wsBean.disconnect();
            util.log(Result.OK, "Disconnect from WS");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from WS");
            
            return 0;
        }
    }     
    
    /**
    * Call WS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sService service name
    * @param sAction action
    * @return WS
    */
    public WS callWS(String sLevel, int iExtID, String sService, String sAction) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callWS() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sService:").append(sService).append(", sAction:").append(sAction);
            logger.info(sb);
            
            WS ws = wsBean.callWS(sLevel, iExtID, sService, sAction, null, null, null, null);
            
            if (ws == null) {
                
                util.log(Result.ERR, "Failed to call WS");
                return null;
                
            }
            else {

                util.log(Result.OK, ws.toString());                
                return ws;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callWS()", ex);
            util.log(Result.ERR, "Failed to call WS");
            
            return null;
        }
        
    }   
    
    /**
    * Call WS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sService service name
    * @param sAction action
    * @param sParamsType parameters type
    * @param sParamNames parameter names
    * @param sParamValues parameter values
    * @return WS
    */
    public WS callWS(String sLevel, int iExtID, String sService, String sAction,
                     String sParamsType, List<String> sParamNames, List<String> sParamValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callWS() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sService:").append(sService).append(", sAction:").append(sAction).append(", sParamsType:").append(sParamsType);
            sb.append(", sParamNames:").append(Arrays.toString(sParamNames.toArray()));
            sb.append(", sParamValues:").append(Arrays.toString(sParamValues.toArray()));
            logger.info(sb);
            
            // service specific params
            WS ws = null;
            
            if (sParamsType.equals("service"))
                ws = wsBean.callWS(sLevel, iExtID, sService, sAction, sParamNames.toArray(), sParamValues.toArray(), 
                                                                      null, null);
            // action specific params
            else if (sParamsType.equals("action"))
                ws = wsBean.callWS(sLevel, iExtID, sService, sAction, null, null, 
                                                                      sParamNames.toArray(), sParamValues.toArray());
            
            if (ws == null) {
                
                util.log(Result.ERR, "Failed to call WS");
                return null;
                
            }
            else {

                util.log(Result.OK, ws.toString());                
                return ws;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callWS()", ex);
            util.log(Result.ERR, "Failed to call WS");
            
            return null;
        }
        
    }  
    
    /**
    * Call WS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sService service name
    * @param sAction action
    * @param sParamsType1 parameters type 1
    * @param sParamNames1 parameter names 1
    * @param sParamValues1 parameter values 1
    * @param sParamsType2 parameters type 2
    * @param sParamNames2 parameter names 2
    * @param sParamValues2 parameters values 2
    * @return WS
    */
    public WS callWS(String sLevel, int iExtID, String sService, String sAction,
                     String sParamsType1, List<String> sParamNames1, List<String> sParamValues1,
                     String sParamsType2, List<String> sParamNames2, List<String> sParamValues2) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callWS() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sService:").append(sService).append(", sAction:").append(sAction).append(", sParamsType1:").append(sParamsType1);
            sb.append(", sParamNames1:").append(Arrays.toString(sParamNames1.toArray()));
            sb.append(", sParamValues1:").append(Arrays.toString(sParamValues1.toArray()));
            sb.append(", sParamsType2:").append(sParamsType2).append(", sParamNames2:").append(Arrays.toString(sParamNames2.toArray()));
            sb.append(", sParamValues2:").append(Arrays.toString(sParamValues2.toArray()));
            logger.info(sb);
            
            WS ws = null;
                    
            // service/action specific params
            if (sParamsType1.equals("service") && sParamsType2.equals("action"))
                ws = wsBean.callWS(sLevel, iExtID, sService, sAction, sParamNames1.toArray(), sParamValues1.toArray(),
                                                                      sParamNames2.toArray(), sParamValues2.toArray());
            // action/service specific params
            else if (sParamsType1.equals("action") && sParamsType2.equals("service"))
                ws = wsBean.callWS(sLevel, iExtID, sService, sAction, sParamNames2.toArray(), sParamValues2.toArray(),
                                                                      sParamNames1.toArray(), sParamValues1.toArray());            
            
            if (ws == null) {
                
                util.log(Result.ERR, "Failed to call WS");
                return null;
                
            }
            else {

                util.log(Result.OK, ws.toString());                
                return ws;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callWS()", ex);
            util.log(Result.ERR, "Failed to call WS");
            
            return null;
        }
        
    }      
    
}