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
* Utilities for AP
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

public class UtilitiesAP {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesAP.class.getName());  
        
    private Utilities util;    
    private APBean apBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesAP() {
        
        this.util = null;
        this.apBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param apBean APBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesAP(Utilities util, APBean apBean, int iLogID) {
        
        this.util = util;
        this.apBean = apBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/   
    
    /**
    * Connect to AP DB
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            apBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to AP");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to AP on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from AP DB
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            apBean.disconnect();
            util.log(Result.OK, "Disconnect from AP");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from AP");
            
            return 0;
        }
    } 
    
    /**
    * Call DTS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sTitle DTS name
    * @return DTS
    */
    public DTS callDTS(String sLevel, int iExtID, String sTitle) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callDTS() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sTitle:").append(sTitle);
            logger.info(sb);
            
            DTS dts = apBean.callDTS(sLevel, iExtID, sTitle, null, null);
            
            if (dts == null) {
                
                util.log(Result.ERR, "Failed to call DTS");
                return null;
                
            }
            else {

                util.log(Result.OK, dts.toString());                
                return dts;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callDTS()", ex);
            util.log(Result.ERR, "Failed to call DTS");
            
            return null;
        }
        
    }     
    
    /**
    * Call DTS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sTitle DTS name
    * @param sParamNames Parameter names
    * @param sParamValues Parameter values
    * @return DTS
    */
    public DTS callDTS(String sLevel, int iExtID, String sTitle, 
                      List<String> sParamNames, List<String> sParamValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callDTS() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sTitle:").append(sTitle).append(", sParamNames:").append(Arrays.toString(sParamNames.toArray()));
            sb.append(", sParamValues:").append(Arrays.toString(sParamValues.toArray()));
            logger.info(sb);
            
            DTS dts = apBean.callDTS(sLevel, iExtID, sTitle, sParamNames.toArray(), sParamValues.toArray());
            
            if (dts == null) {
                
                util.log(Result.ERR, "Failed to call DTS");
                return null;
                
            }
            else {

                util.log(Result.OK, dts.toString());                
                return dts;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callDTS()", ex);
            util.log(Result.ERR, "Failed to call DTS");
            
            return null;
        }
        
    }   
    
    /**
    * Get Lov
    * @param sLovType Lov type
    * @param oID ID
    * @return Lov
    */
    public Lov getLov(String sLovType, Object oID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getLov() - corrId=").append(iLogID).append(", params sLovType:").append(sLovType);
            sb.append(", oID:").append(oID);
            logger.info(sb);
            
            Lov lov = null;
            
            if (sLovType.equals("TARIFF"))
                lov = apBean.getLovTariff((Integer) oID);
            else if (sLovType.equals("NON_PUBLIC_OFFER"))
                lov = apBean.getLovNonpublicOffer((Integer) oID);
            else if (sLovType.equals("TARIFF_PROMO"))
                lov = apBean.getLovTariffPromo((Integer) oID);
            else if (sLovType.equals("DISCOUNT_PROPERTY"))
                lov = apBean.getLovDiscountProperty(String.valueOf(oID));  
            else if (sLovType.equals("RETENTION_OFFER"))
                lov = apBean.getLovRetentionOffer((Integer) oID);  
            else if (sLovType.equals("SERVICE"))
                lov = apBean.getLovService((Integer) oID);               
            
            if (lov == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get Lov:").append(sLovType).append(" for ID:").append(oID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                util.log(Result.OK, lov.toString());                
                return lov;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLov()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get Lov:").append(sLovType).append(" for ID:").append(oID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }        
    
}