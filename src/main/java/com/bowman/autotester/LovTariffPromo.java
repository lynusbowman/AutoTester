package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovTariffPromo
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovTariffPromo extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovTariffPromo.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sDescription;
    private String sReasonOfUse;
    private int iExtraMinutes;
    private int iExtraMessages;
    private int iFupLimit;
    private float fMfDiscount;    
    private int iTariff;
    private int iExtraCreditPcnt;
    private int iExtraMinutesTmcz;    
    private float fMbdMfDiscount;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovTariffPromo() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sDescription = null;
        this.sReasonOfUse = null;
        this.iExtraMinutes = -1;
        this.iExtraMessages = -1;
        this.iFupLimit = -1;
        this.fMfDiscount = -1;        
        this.iTariff = -1;
        this.iExtraCreditPcnt = -1;
        this.iExtraMinutesTmcz = -1;        
        this.fMbdMfDiscount = -1;        
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title
     * @param sDescription description
     * @param sReasonOfUse reason of use
     * @param iExtraMinutes extra minutes
     * @param iExtraMessages extra messages
     * @param iFupLimit fup limit
     * @param fMfDiscount mf discount 
     * @param iTariff tariff
     * @param iExtraCreditPcnt extra credit pcnt
     * @param iExtraMinutesTmcz extra minutes TMCZ     
     * @param fMbdMfDiscount mbd mf discount
     */
    public LovTariffPromo(int iID, String sTitle, String sDescription, String sReasonOfUse,
                          int iExtraMinutes, int iExtraMessages, int iFupLimit, float fMfDiscount, int iTariff,
                          int iExtraCreditPcnt, int iExtraMinutesTmcz,  float fMbdMfDiscount) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sDescription = sDescription;
        this.sReasonOfUse = sReasonOfUse;
        this.iExtraMinutes = iExtraMinutes;
        this.iExtraMessages = iExtraMessages;
        this.iFupLimit = iFupLimit;
        this.fMfDiscount = fMfDiscount;
        this.iTariff = iTariff;
        this.iExtraCreditPcnt = iExtraCreditPcnt;
        this.iExtraMinutesTmcz = iExtraMinutesTmcz;        
        this.fMbdMfDiscount = fMbdMfDiscount;          
        
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
    * @return String 
    */
    public String getDescription() {
        
        return this.sDescription;
        
    }
    
    /**  
    * @param sDescription description
    */
    public void setDescription(String sDescription) {
        
        this.sDescription = sDescription;
        
    }   
    
    /**
    * @return String 
    */
    public String getReasonOfUse() {
        
        return this.sReasonOfUse;
        
    }
    
    /**  
    * @param sReasonOfUse reason of use
    */
    public void setReasonOfUse(String sReasonOfUse) {
        
        this.sReasonOfUse = sReasonOfUse;
        
    }  
    
    /**
    * @return int 
    */
    public int getExtraMinutes() {
        
        return this.iExtraMinutes;
        
    }
    
    /**  
    * @param iExtraMinutes extra minutes
    */
    public void setExtraMinutes(int iExtraMinutes) {
        
        this.iExtraMinutes = iExtraMinutes;
        
    }   
    
    /**
    * @return int 
    */
    public int getExtraMessages() {
        
        return this.iExtraMessages;
        
    }
    
    /**  
    * @param iExtraMessages extra messages
    */
    public void setExtraMessages(int iExtraMessages) {
        
        this.iExtraMessages = iExtraMessages;
        
    }   
    
    /**
    * @return int 
    */
    public int getFupLimit() {
        
        return this.iFupLimit;
        
    }
    
    /**  
    * @param iFupLimit fup limit
    */
    public void setFupLimit(int iFupLimit) {
        
        this.iFupLimit = iFupLimit;
        
    }  
    
    /**
    * @return float 
    */
    public float getMfDiscount() {
        
        return this.fMfDiscount;
        
    }
    
    /**  
    * @param fMfDiscount mf discount
    */
    public void setMfDiscount(int fMfDiscount) {
        
        this.fMfDiscount = fMfDiscount;
           
    }     
    
    /**
    * @return int 
    */
    public int getTariff() {
        
        return this.iTariff;
        
    }
    
    /**  
    * @param iTariff tariff
    */
    public void setTariff(int iTariff) {
        
        this.iTariff = iTariff;
        
    } 
    
    /**
    * @return int 
    */
    public int getExtraCreditPcnt() {
        
        return this.iExtraCreditPcnt;
        
    }
    
    /**  
    * @param iExtraCreditPcnt extra credit pcnt
    */
    public void setExtraCreditPcnt(int iExtraCreditPcnt) {
        
        this.iExtraCreditPcnt = iExtraCreditPcnt;
        
    }  
    
    /**
    * @return int 
    */
    public int getExtraMinutesTmcz() {
        
        return this.iExtraMinutesTmcz;
        
    }
    
    /**  
    * @param iExtraMinutesTmcz extra minutes TMCZ
    */
    public void setExtraMinutesTmcz(int iExtraMinutesTmcz) {
        
        this.iExtraMinutesTmcz = iExtraMinutesTmcz;
        
    }     
    
    /**
    * @return float 
    */
    public float getMbdMfDiscount() {
        
        return this.fMbdMfDiscount;
        
    }
    
    /**  
    * @param fMbdMfDiscount mbd mf discount
    */
    public void setMbdMfDiscount(float fMbdMfDiscount) {
        
        this.fMbdMfDiscount = fMbdMfDiscount;
           
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbTariffPromo = new StringBuilder();
        sbTariffPromo.append("ID:").append(iID);
        sbTariffPromo.append("|Title:").append(sTitle); 
        sbTariffPromo.append("|Description:").append(sDescription); 
        sbTariffPromo.append("|ReasonOfUse:").append(sReasonOfUse); 
        sbTariffPromo.append("|ExtraMinutes:").append(iExtraMinutes); 
        sbTariffPromo.append("|ExtraMesages:").append(iExtraMessages); 
        sbTariffPromo.append("|FupLimit:").append(iFupLimit); 
        sbTariffPromo.append("|MfDiscount:").append(fMfDiscount);        
        sbTariffPromo.append("|Tariff:").append(iTariff); 
        sbTariffPromo.append("|ExtraCreditPcnt:").append(iExtraCreditPcnt); 
        sbTariffPromo.append("|ExtarMinutesTmcz:").append(iExtraMinutesTmcz);          
        sbTariffPromo.append("|MbdMfDiscount:").append(fMbdMfDiscount); 
        
        return sbTariffPromo.toString();
        
    }    
    
}