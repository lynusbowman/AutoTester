package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Utilities for CNH
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-11 
*/

public class UtilitiesCNH {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesCNH.class.getName());  
        
    private Utilities util;    
    private CNHBean cnhBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesCNH() {
        
        this.util = null;
        this.cnhBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param cnhBean CNHBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesCNH(Utilities util, CNHBean cnhBean, int iLogID) {
        
        this.util = util;
        this.cnhBean = cnhBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/   
    
    /**
    * Connect to CNH DB
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            cnhBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to CNH");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to CNH on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from CNH DB
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            cnhBean.disconnect();
            util.log(Result.OK, "Disconnect from CNH");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from CNH");
            
            return 0;
        }
    }   
    
    /**
    * Get notification
    * @param sMSISDN MSISDN
    * @return Notification
    */
    public Notification getNotification(String sMSISDN) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getNotification() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            Notification notification = cnhBean.getNotification(sMSISDN);
            
            if (notification == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get notification for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                
                util.log(Result.OK, notification.toString());
                return notification;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getNotification()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get notification for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
    }         
    
}