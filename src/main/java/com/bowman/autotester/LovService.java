package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovService
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovService extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovService.class.getName());      
    
    // basic
    private int iID;
    private String sTitle;
    private HashMap<Integer, LovServiceParam> hParams;
    
    // detail
    private String sStartDate;
    private String sEndDate;
    private int iNetworkIndicator;
    private int iMultiple;
    private int iSuspendable;
    private int iSchedulable;
    private int iAssignPush;
    private int iChangePush;
    private int iTimeBomb;
    private int iSU;
    private int iBA;
    private int iEU;
    private int iOU;
    private int iLE;
    private int iCU;
    private int iCO;
    
    // resource
    private int iResourceID;
    private String sResourceTitle;
    private int iResourceShared;
    private int iResourceNetworkIndicator;
    
    // offers
    private HashMap<Integer, LovOffer> hOffers;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovService() {
        
        this.iID = -1;
        this.sTitle = null;
        this.hParams = null;
        
        this.sStartDate = null;
        this.sEndDate = null;
        this.iNetworkIndicator = -1;
        this.iMultiple = -1;
        this.iSuspendable = -1;
        this.iSchedulable = -1;
        this.iAssignPush = -1;
        this.iChangePush = -1;
        this.iTimeBomb = -1;
        this.iSU = -1;
        this.iBA = -1;
        this.iEU = -1;
        this.iOU = -1;
        this.iLE = -1;
        this.iCU = -1;
        this.iCO = -1;    
        
        this.iResourceID = -1;
        this.sResourceTitle = null;
        this.iResourceShared = -1;
        this.iResourceNetworkIndicator = -1; 
        
        this.hOffers = null;
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param hParams parameters
    */
    public LovService(int iID, String sTitle, HashMap<Integer, LovServiceParam> hParams) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.hParams = hParams;
        
        this.sStartDate = null;
        this.sEndDate = null;
        this.iNetworkIndicator = -1;
        this.iMultiple = -1;
        this.iSuspendable = -1;
        this.iSchedulable = -1;
        this.iAssignPush = -1;
        this.iChangePush = -1;
        this.iTimeBomb = -1;
        this.iSU = -1;
        this.iBA = -1;
        this.iEU = -1;
        this.iOU = -1;
        this.iLE = -1;
        this.iCU = -1;
        this.iCO = -1;   
        
        this.iResourceID = -1;
        this.sResourceTitle = null;
        this.iResourceShared = -1;
        this.iResourceNetworkIndicator = -1;   
        
        this.hOffers = null;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return int 
    */
    public int getID() {
        
        return this.iID;
        
    }
    
    /**  
    * @param iID ID
    */
    public void setID(int iID) {
        
        this.iID = iID;
        
    }    
    
    /**
    * @return String 
    */
    public String getTitle() {
        
        return this.sTitle;
        
    }
    
    /**  
    * @param sTitle title
    */
    public void setTitle(String sTitle) {
        
        this.sTitle = sTitle;
        
    }     
    
    /**
    * @return HashMap 
    */
    public HashMap<Integer, LovServiceParam> getParams() {
        
        return this.hParams;
        
    }   
    
    /**
    * @return String 
    */
    public String getStartDate() {
        
        return this.sStartDate;
        
    }
    
    /**  
    * @param sStartDate start date
    */
    public void setStartDate(String sStartDate) {
        
        this.sStartDate = sStartDate;
        
    }  
    
    /**
    * @return String 
    */
    public String getEndDate() {
        
        return this.sEndDate;
        
    }
    
    /**  
    * @param sEndDate end date
    */
    public void setEndDate(String sEndDate) {
        
        this.sEndDate = sEndDate;
        
    } 
    
    /**
    * @return int 
    */
    public int getNetworkIndicator() {
        
        return this.iNetworkIndicator;
        
    }
    
    /**  
    * @param iNetworkIndicator network indicator
    */
    public void setNetworkIndicator(int iNetworkIndicator) {
        
        this.iNetworkIndicator = iNetworkIndicator;
        
    }     
    
    /**
    * @return int 
    */
    public int getMultiple() {
        
        return this.iMultiple;
        
    }
    
    /**  
    * @param iMultiple multiple
    */
    public void setMultiple(int iMultiple) {
        
        this.iMultiple = iMultiple;
        
    }    
    
    /**
    * @return int 
    */
    public int getSuspendable() {
        
        return this.iSuspendable;
        
    }
    
    /**  
    * @param iSuspendable suspendable
    */
    public void setSuspendable(int iSuspendable) {
        
        this.iSuspendable = iSuspendable;
        
    }  
    
    /**
    * @return int 
    */
    public int getSchedulable() {
        
        return this.iSchedulable;
        
    }
    
    /**  
    * @param iSchedulable schedulable
    */
    public void setSchedulable(int iSchedulable) {
        
        this.iSchedulable = iSchedulable;
        
    } 
    
    /**
    * @return int 
    */
    public int getAssignPush() {
        
        return this.iAssignPush;
        
    }
    
    /**  
    * @param iAssignPush assign push
    */
    public void setAssignPush(int iAssignPush) {
        
        this.iAssignPush = iAssignPush;
        
    }    
    
    /**
    * @return int 
    */
    public int getChangePush() {
        
        return this.iChangePush;
        
    }
    
    /**  
    * @param iChangePush change push
    */
    public void setChangePush(int iChangePush) {
        
        this.iChangePush = iChangePush;
        
    }  
    
    /**
    * @return int 
    */
    public int getTimeBomb() {
        
        return this.iTimeBomb;
        
    }
    
    /**  
    * @param iTimeBomb time bomb
    */
    public void setTimeBomb(int iTimeBomb) {
        
        this.iTimeBomb = iTimeBomb;
        
    }   
    
    /**
    * @return int 
    */
    public int getSU() {
        
        return this.iSU;
        
    }
    
    /**  
    * @param iSU SU
    */
    public void setSU(int iSU) {
        
        this.iSU = iSU;
        
    }   
    
    /**
    * @return int 
    */
    public int getBA() {
        
        return this.iBA;
        
    }
    
    /**  
    * @param iBA BA
    */
    public void setBA(int iBA) {
        
        this.iBA = iBA;
        
    }   
    
    /**
    * @return int 
    */
    public int getEU() {
        
        return this.iEU;
        
    }
    
    /**  
    * @param iEU EU
    */
    public void setEU(int iEU) {
        
        this.iEU = iEU;
        
    } 
    
    /**
    * @return int 
    */
    public int getOU() {
        
        return this.iOU;
        
    }
    
    /**  
    * @param iOU OU
    */
    public void setOU(int iOU) {
        
        this.iOU = iOU;
        
    }     
    
    /**
    * @return int 
    */
    public int getLE() {
        
        return this.iLE;
        
    }
    
    /**  
    * @param iLE LE
    */
    public void setLE(int iLE) {
        
        this.iLE = iLE;
        
    }     
    
    /**
    * @return int 
    */
    public int getCU() {
        
        return this.iCU;
        
    }
    
    /**  
    * @param iCU CU
    */
    public void setCU(int iCU) {
        
        this.iCU = iCU;
        
    }     
    
    /**
    * @return int 
    */
    public int getCO() {
        
        return this.iCO;
        
    }
    
    /**  
    * @param iCO CO
    */
    public void setCO(int iCO) {
        
        this.iCO = iCO;
        
    }  
    
    /**
    * @return int 
    */
    public int getResourceID() {
        
        return this.iResourceID;
        
    }
    
    /**  
    * @param iResourceID resource ID
    */
    public void setResourceID(int iResourceID) {
        
        this.iResourceID = iResourceID;
        
    } 
    
    /**
    * @return String 
    */
    public String getResourceTitle() {
        
        return this.sResourceTitle;
        
    }
    
    /**  
    * @param sResourceTitle resource title
    */
    public void setResourceTitle(String sResourceTitle) {
        
        this.sResourceTitle = sResourceTitle;
        
    }   
    
    /**
    * @return int 
    */
    public int getResourceShared() {
        
        return this.iResourceShared;
        
    }
    
    /**  
    * @param iResourceShared resource shared
    */
    public void setResourceShared(int iResourceShared) {
        
        this.iResourceShared = iResourceShared;
        
    }    
    
    /**
    * @return int 
    */
    public int getResourceNetworkIndicator() {
        
        return this.iResourceNetworkIndicator;
        
    }
    
    /**  
    * @param iResourceNetworkIndicator resource network indicator
    */
    public void setResourceNetworkIndicator(int iResourceNetworkIndicator) {
        
        this.iResourceNetworkIndicator = iResourceNetworkIndicator;
        
    }  
    
    /**
    * @return HashMap 
    */
    public HashMap<Integer, LovOffer> getOffers() {
        
        return this.hOffers;
        
    }    
    
    /**
    * @param hOffers offers
    */
    public void setOffers(HashMap<Integer, LovOffer> hOffers) {
        
        this.hOffers = hOffers;
        
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbService = new StringBuilder();
        sbService.append("ID:").append(iID);
        sbService.append("|Title:").append(sTitle);         
        
        // service detail from CLF
        if (sStartDate != null) {
            
            sbService.append("|StartDate:").append(sStartDate);
            sbService.append("|EndDate:").append(sEndDate);
            sbService.append("|NetworkIndicator:").append(iNetworkIndicator);
            sbService.append("|Multiple:").append(iMultiple);
            sbService.append("|Suspendable:").append(iSuspendable);
            sbService.append("|Schedulable:").append(iSchedulable);
            sbService.append("|AssignPush:").append(iAssignPush);
            sbService.append("|ChangePush:").append(iChangePush);
            sbService.append("|TimeBomb:").append(iTimeBomb);
            sbService.append("|SU:").append(iSU);
            sbService.append("|BA:").append(iBA);
            sbService.append("|EU:").append(iEU);
            sbService.append("|OU:").append(iOU);
            sbService.append("|LE:").append(iLE);
            sbService.append("|CU:").append(iCU);
            sbService.append("|CO:").append(iCO);
            
        }   
        
        // resource from CLF
        sbService.append("|Resource#");
        
        if (iResourceID != -1) {
            
            sbService.append("ID:").append(iResourceID);
            sbService.append("|Title:").append(sResourceTitle);
            sbService.append("|Shared:").append(iResourceShared);
            sbService.append("|NetworkIndicator:").append(iResourceNetworkIndicator);
            
        }
        
        // parameters
        sbService.append("|Parameters#");
        
        if (hParams != null) {
            
            for (Entry<Integer, LovServiceParam> param : hParams.entrySet())
                sbService.append(param.getValue().toString()).append("#");
        
        }    
        
        // offers
        sbService.append("|Offers#");
        
        if (hOffers != null) {
            
            for (Entry<Integer, LovOffer> offer : hOffers.entrySet())
                sbService.append(offer.getValue().toString()).append("#");
        
        }          
        
        return sbService.toString();
        
    }  
    
    /**
    * Get param
    * @param iID ID
    * @return LovServiceParam
    */
    public LovServiceParam getParam(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getParam() - params iID:").append(iID);
            logger.debug(sb);
            LovServiceParam param = hParams.get(iID);
            
            if (param == null) {
                
                sb = new StringBuilder();
                sb.append("Param ").append(iID).append(" not found");
                logger.error(sb);
                
            }
            
            return param;
            
        }
        catch (Exception ex) {
            
            logger.error("getParam()", ex);
            
            return null;
        }
        
    }   
    
    /**
    * Get offer
    * @param iID ID
    * @return LovOffer
    */
    public LovOffer getOffer(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getOffer() - params iID:").append(iID);
            logger.debug(sb);
            LovOffer offer = hOffers.get(iID);
            
            if (offer == null) {
                
                sb = new StringBuilder();
                sb.append("Offer ").append(iID).append(" not found");
                logger.error(sb);
                
            }
            
            return offer;
            
        }
        catch (Exception ex) {
            
            logger.error("getOffer()", ex);
            
            return null;
        }
        
    }      
    
}