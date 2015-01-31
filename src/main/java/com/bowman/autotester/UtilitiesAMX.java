package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Utilities for AMX
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

public class UtilitiesAMX {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesAMX.class.getName());  
        
    private Utilities util;    
    private AMXBean amxBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesAMX() {
        
        this.util = null;
        this.amxBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param amxBean AMXBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesAMX(Utilities util, AMXBean amxBean, int iLogID) {
        
        this.util = util;
        this.amxBean = amxBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/   
    
    /**
    * Connect to AMX DB
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            amxBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to AMX");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to AMX on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from AMX DB
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            amxBean.disconnect();
            util.log(Result.OK, "Disconnect from AMX");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from AMX");
            
            return 0;
        }
    } 
    
    /**
    * Get CU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getCU(String sMSISDN) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getCU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iCU = amxBean.getCU(sMSISDN);
            
            if (iCU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get CU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get CU: ").append(iCU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iCU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getCU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get CU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }    
    
    /**
    * Get SU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getSU(String sMSISDN) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iSU = amxBean.getSU(sMSISDN);
            
            if (iSU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get SU: ").append(iSU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iSU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    } 
    
    /**
    * Get status
    * @param sMSISDN MSISDN
    * @return String
    */
    public String getStatus(String sMSISDN) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getStatus() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            String sStatus = amxBean.getStatus(sMSISDN);
            
            if (sStatus == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get status for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get status: ").append(sStatus).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return sStatus;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getStatus()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get status for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
    }      
    
    /**
    * Get SU offers
    * @param iExtID EXTID_SU
    * @return List of Offer
    */
    public List<Offer> getSUOffers(int iExtID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUOffers() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            List<Offer> lstOffers = amxBean.getSUOffers(iExtID, -1);
            
            if (lstOffers == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU offers for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                
                // log services
                for(Offer offer : lstOffers)
                    util.log(Result.OK, offer.toString());                               
                
                return lstOffers;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUOffers()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU offers for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
    }  
    
    /**
    * Get SU offer
    * @param iExtID EXTID_SU
    * @param iOfferID Offer ID
    * @return Offer
    */
    public Offer getSUOffer(int iExtID, int iOfferID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUOffer() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iOfferID:").append(iOfferID);
            logger.info(sb);
            
            List<Offer> lstOffers = amxBean.getSUOffers(iExtID, iOfferID);
            
            if (lstOffers == null || lstOffers.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU offer:").append(iOfferID).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                                
                Offer offer = lstOffers.get(0);
                util.log(Result.OK, offer.toString());
                
                return offer;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUOffer()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU offer:").append(iOfferID).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
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
            
            if (sLovType.equals("OFFER"))
                lov = amxBean.getLovOffer((Integer) oID);
            
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