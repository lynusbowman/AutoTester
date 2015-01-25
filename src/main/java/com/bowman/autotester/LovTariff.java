package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovTariff
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovTariff extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovTariff.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sCzTitle;
    private String sOffer;
    private float fMonthlyFee;
    private int iActive;
    private String sGroup;
    private String sType;
    private String sReportingType;
    private String sProfile;
    private String sBundleFlg;
    private float fBundleDiscount;
    private String sVPNCompatible;
    private String sNonpublicFlg;
    private String sLTEFlg;
    private String sFlatType;
    private int iSQDProfile;
    private String sSharedTariffComp;    
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovTariff() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sCzTitle = null;
        this.sOffer = null;
        this.fMonthlyFee = -1;
        this.iActive = -1;
        this.sGroup = null;
        this.sType = null;
        this.sReportingType = null;
        this.sProfile = null;
        this.sBundleFlg = null;
        this.fBundleDiscount = -1;
        this.sVPNCompatible = null;
        this.sNonpublicFlg = null;
        this.sLTEFlg = null;
        this.sFlatType = null;
        this.iSQDProfile = -1;
        this.sSharedTariffComp = null;
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param sCzTitle CZ title
    * @param sOffer offer
    * @param fMonthlyFee monthly fee
    * @param iActive active
    * @param sGroup group
    * @param sType type
    * @param sReportingType reporting type
    * @param sProfile profile
    * @param sBundleFlg bundle flg
    * @param fBundleDiscount bundle discount
    * @param sVPNCompatible VPN compatible
    * @param sNonpublicFlg nonpublic flg
    * @param sLTEFlg LTE flg
    * @param sFlatType flat type
    * @param iSQDProfile SQD profile
    * @param sSharedTariffComp shared tariff comp
    */
    public LovTariff(int iID, String sTitle, String sCzTitle, String sOffer, float fMonthlyFee,
                     int iActive, String sGroup, String sType, String sReportingType, String sProfile, 
                     String sBundleFlg, float fBundleDiscount, String sVPNCompatible, String sNonpublicFlg, 
                     String sLTEFlg, String sFlatType, int iSQDProfile, String sSharedTariffComp) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sCzTitle = sCzTitle;
        this.sOffer = sOffer;
        this.fMonthlyFee = fMonthlyFee;
        this.iActive = iActive;
        this.sGroup = sGroup;
        this.sType = sType;
        this.sReportingType = sReportingType;
        this.sProfile = sProfile;
        this.sBundleFlg = sBundleFlg;
        this.fBundleDiscount = fBundleDiscount;
        this.sVPNCompatible = sVPNCompatible;
        this.sNonpublicFlg = sNonpublicFlg;
        this.sLTEFlg = sLTEFlg;
        this.sFlatType = sFlatType;
        this.iSQDProfile = iSQDProfile;
        this.sSharedTariffComp = sSharedTariffComp;
        
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
    public String getCzTitle() {
        
        return this.sCzTitle;
        
    }
    
    /**  
    * @param sCzTitle CZ title
    */
    public void setCzTitle(String sCzTitle) {
        
        this.sCzTitle = sCzTitle;
        
    }     
    
    /**
    * @return String 
    */
    public String getOffer() {
        
        return this.sOffer;
        
    }
    
    /**  
    * @param sOffer offer
    */
    public void setOffer(String sOffer) {
        
        this.sOffer = sOffer;
        
    }      
    
    /**
    * @return float 
    */
    public float getMonthlyFee() {
        
        return this.fMonthlyFee;
        
    }
    
    /**  
    * @param fMonthlyFee monthly fee
    */
    public void setMonthlyFee(float fMonthlyFee) {
        
        this.fMonthlyFee = fMonthlyFee;
        
    }  
    
    /**
    * @return int 
    */
    public int getActive() {
        
        return this.iActive;
        
    }
    
    /**  
    * @param iActive active
    */
    public void setActive(int iActive) {
        
        this.iActive = iActive;
        
    } 
    
    /**
    * @return String 
    */
    public String getGroup() {
        
        return this.sGroup;
        
    }
    
    /**  
    * @param sGroup group
    */
    public void setGroup(String sGroup) {
        
        this.sGroup = sGroup;
        
    }  
    
    /**
    * @return String 
    */
    public String getType() {
        
        return this.sType;
        
    }
    
    /**  
    * @param sType type
    */
    public void setType(String sType) {
        
        this.sType = sType;
        
    }  
    
    /**
    * @return String 
    */
    public String getReportingType() {
        
        return this.sReportingType;
        
    }
    
    /**  
    * @param sReportingType reporting type
    */
    public void setReportingType(String sReportingType) {
        
        this.sReportingType = sReportingType;
        
    }    
    
    /**
    * @return String 
    */
    public String getProfile() {
        
        return this.sProfile;
        
    }
    
    /**  
    * @param sProfile profile
    */
    public void setProfile(String sProfile) {
        
        this.sProfile = sProfile;
        
    }  
    
    /**
    * @return String 
    */
    public String getBundleFlg() {
        
        return this.sBundleFlg;
        
    }
    
    /**  
    * @param sBundleFlg bundle flg
    */
    public void setBundleFlg(String sBundleFlg) {
        
        this.sBundleFlg = sBundleFlg;
        
    }   
    
    /**
    * @return float 
    */
    public float getBundleDiscount() {
        
        return this.fBundleDiscount;
        
    }
    
    /**  
    * @param fBundleDiscount bundle discount
    */
    public void setBundleDiscount(float fBundleDiscount) {
        
        this.fBundleDiscount = fBundleDiscount;
        
    }   
    
    /**
    * @return String 
    */
    public String getVPNCompatible() {
        
        return this.sVPNCompatible;
        
    }
    
    /**  
    * @param sVPNCompatible VPN compatible
    */
    public void setVPNCompatible(String sVPNCompatible) {
        
        this.sVPNCompatible = sVPNCompatible;
        
    }   
    
    /**
    * @return String 
    */
    public String getNonpublicFlg() {
        
        return this.sNonpublicFlg;
        
    }
    
    /**  
    * @param sNonpublicFlg nonpublic flg
    */
    public void setNonpublicFlg(String sNonpublicFlg) {
        
        this.sNonpublicFlg = sNonpublicFlg;
        
    }  
    
    /**
    * @return String 
    */
    public String getLTEFlg() {
        
        return this.sLTEFlg;
        
    }
    
    /**  
    * @param sLTEFlg LTE flg
    */
    public void setLTEFlg(String sLTEFlg) {
        
        this.sLTEFlg = sLTEFlg;
        
    } 
    
    /**
    * @return String 
    */
    public String getFlatType() {
        
        return this.sFlatType;
        
    }
    
    /**  
    * @param sFlatType flat type
    */
    public void setFlatType(String sFlatType) {
        
        this.sFlatType = sFlatType;
        
    }   
    
    /**
    * @return int 
    */
    public int getSQDProfile() {
        
        return this.iSQDProfile;
        
    }
    
    /**  
    * @param iSQDProfile SQD profile
    */
    public void setSQDProfile(int iSQDProfile) {
        
        this.iSQDProfile = iSQDProfile;
        
    }  
    
    /**
    * @return String 
    */
    public String getSharedTariffComp() {
        
        return this.sSharedTariffComp;
        
    }
    
    /**  
    * @param sSharedTariffComp shared tariff comp
    */
    public void setSharedTariffComp(String sSharedTariffComp) {
        
        this.sSharedTariffComp = sSharedTariffComp;
        
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbLovTariff = new StringBuilder();
        sbLovTariff.append("ID:").append(iID);
        sbLovTariff.append("|Title:").append(sTitle);
        sbLovTariff.append("|CzTitle:").append(sCzTitle);
        sbLovTariff.append("|Offer:").append(sOffer);
        sbLovTariff.append("|MonthlyFee:").append(fMonthlyFee);
        sbLovTariff.append("|Active:").append(iActive);
        sbLovTariff.append("|Group:").append(sGroup);
        sbLovTariff.append("|Type:").append(sType);
        sbLovTariff.append("|ReportingType:").append(sReportingType);
        sbLovTariff.append("|Profile:").append(sProfile);
        sbLovTariff.append("|BundleFlg:").append( sBundleFlg);
        sbLovTariff.append("|BundleDiscount:").append(   fBundleDiscount);
        sbLovTariff.append("|VPNCompatible:").append(sVPNCompatible);
        sbLovTariff.append("|NonpublicFlg:").append(sNonpublicFlg);
        sbLovTariff.append("|LTEFlg:").append(sLTEFlg);
        sbLovTariff.append("|FlatType:").append(sFlatType);
        sbLovTariff.append("|SQDProfile:").append(iSQDProfile);
        sbLovTariff.append("|SharedTariffComp:").append(sSharedTariffComp);  
        
        
        
        return sbLovTariff.toString();
        
    }    
    
}