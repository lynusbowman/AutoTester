package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* SU detail
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-08 
*/

public class SUDetail {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(SUDetail.class.getName());      
    
    private int iExtidSU;
    private int iExtidBA;
    private int iExtidOU;
    private int iPCN;
    private String sMSISDN;
    private String sICCID;
    private String sIMSI;
    private String sSubsStatus;
    private String sSubsNetStatus;
    private int iSubsTariff;
    private int iSUMarket;
    private int iSUContractType; 
    private String sActivationType;
    private int iContrDuration;
    private int iSegmentAttr;
    private int iSegmentAttr2;
    private int iSegmentAttr3;
    private int iStatusReasonIDC;
    private int iStatusReasonIDF;
    private int iStatusReasonIDU;
    private int iOutlet;
    private int iDunningSMS;
    private int iWelcomeLetter;
    private String sNote;
     
    
    /**********************************
              constructors
    **********************************/ 
    
    public SUDetail() {

        this.iExtidSU = -1;
        this.iExtidBA = -1;
        this.iExtidOU = -1;
        this.iPCN = -1;
        this.sMSISDN = null;
        this.sICCID = null;
        this.sIMSI = null;
        this.sSubsStatus = null;
        this.sSubsNetStatus = null;
        this.iSubsTariff = -1;
        this.iSUMarket = -1;
        this.iSUContractType = -1; 
        this.sActivationType = null;
        this.iContrDuration = -1;
        this.iSegmentAttr = -1;
        this.iSegmentAttr2 = -1;
        this.iSegmentAttr3 = -1;
        this.iStatusReasonIDC = -1;
        this.iStatusReasonIDF = -1;
        this.iStatusReasonIDU = -1;
        this.iOutlet = -1;
        this.iDunningSMS = -1;
        this.iWelcomeLetter = -1;
        this.sNote = null;
        
    } 
    
    /**
     * @param iExtidSU EXTID_SU
     * @param iExtidBA EXTID_BA
     * @param iExtidOU EXTID_BA
     * @param iPCN PCN
     * @param sMSISDN MSISDN
     * @param sICCID ICCID
     * @param sIMSI IMSI
     * @param sSubsStatus subscriber status
     * @param sSubsNetStatus subscriber net status
     * @param iSubsTariff subscriber tariff
     * @param iSUMarket SU market
     * @param iSUContractType SU contract type
     * @param sActivationType activation type
     * @param iContrDuration contract duration
     * @param iSegmentAttr segment attribute
     * @param iSegmentAttr2 segment attribute 2
     * @param iSegmentAttr3 segment attribute 3
     * @param iStatusReasonIDC status reason IDC
     * @param iStatusReasonIDF status reason IDF
     * @param iStatusReasonIDU status reason IDU
     * @param iOutlet outlet
     * @param iDunningSMS dunning SMS
     * @param iWelcomeLetter welcome letter
     * @param sNote note
     */
    public SUDetail(int iExtidSU, int iExtidBA, int iExtidOU, int iPCN, String sMSISDN, String sICCID, String sIMSI,
                    String sSubsStatus, String sSubsNetStatus, int iSubsTariff, int iSUMarket, int iSUContractType,
                    String sActivationType, int iContrDuration, int iSegmentAttr, int iSegmentAttr2, int iSegmentAttr3,
                    int iStatusReasonIDC, int iStatusReasonIDF, int iStatusReasonIDU, int iOutlet, int iDunningSMS,
                    int iWelcomeLetter, String sNote) {
        
        this.iExtidSU = iExtidSU;
        this.iExtidBA = iExtidBA;
        this.iExtidOU = iExtidOU;
        this.iPCN = iPCN;
        this.sMSISDN = sMSISDN;
        this.sICCID = sICCID;
        this.sIMSI = sIMSI;
        this.sSubsStatus = sSubsStatus;
        this.sSubsNetStatus = sSubsNetStatus;
        this.iSubsTariff = iSubsTariff;
        this.iSUMarket = iSUMarket;
        this.iSUContractType = iSUContractType; 
        this.sActivationType = sActivationType;
        this.iContrDuration = iContrDuration;
        this.iSegmentAttr = iSegmentAttr;
        this.iSegmentAttr2 = iSegmentAttr2;
        this.iSegmentAttr3 = iSegmentAttr3;
        this.iStatusReasonIDC = iStatusReasonIDC;
        this.iStatusReasonIDF = iStatusReasonIDF;
        this.iStatusReasonIDU = iStatusReasonIDU;
        this.iOutlet = iOutlet;
        this.iDunningSMS = iDunningSMS;
        this.iWelcomeLetter = iWelcomeLetter;
        this.sNote = sNote;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return int 
    */
    public int getExtidSU() {
        
        return this.iExtidSU;
        
    }
    
    /**  
    * @param iExtidSU EXTID_SU
    */
    public void setExtidSU(int iExtidSU) {
        
        this.iExtidSU = iExtidSU;
        
    }   
    
    /**
    * @return int 
    */
    public int getExtidBA() {
        
        return this.iExtidBA;
        
    }
    
    /**  
    * @param iExtidBA EXTID_BA
    */
    public void setExtidBA(int iExtidBA) {
        
        this.iExtidBA = iExtidBA;
        
    }     
    
    /**
    * @return int 
    */
    public int getExtidOU() {
        
        return this.iExtidOU;
        
    }
    
    /**  
    * @param iExtidOU EXTID_OU
    */
    public void setExtidOU(int iExtidOU) {
        
        this.iExtidOU = iExtidOU;
        
    }      
    
    /**
    * @return int 
    */
    public int getPCN() {
        
        return this.iPCN;
        
    }
    
    /**  
    * @param iPCN PCN
    */
    public void setPCN(int iPCN) {
        
        this.iPCN = iPCN;
        
    }  
    
    /**
    * @return String 
    */
    public String getMSISDN() {
        
        return this.sMSISDN;
        
    }
    
    /**  
    * @param sMSISDN MSISDN
    */
    public void setMSISDN(String sMSISDN) {
        
        this.sMSISDN = sMSISDN;
        
    } 
    
    /**
    * @return String 
    */
    public String getICCID() {
        
        return this.sICCID;
        
    }
    
    /**  
    * @param sICCID ICCID
    */
    public void setICCID(String sICCID) {
        
        this.sICCID = sICCID;
        
    }    
    
    /**
    * @return String 
    */
    public String getIMSI() {
        
        return this.sIMSI;
        
    }
    
    /**  
    * @param sIMSI IMSI
    */
    public void setIMSI(String sIMSI) {
        
        this.sIMSI = sIMSI;
        
    }  
    
    /**
    * @return String 
    */
    public String getSubsStatus() {
        
        return this.sSubsStatus;
        
    }
    
    /**  
    * @param sSubsStatus subscriber status
    */
    public void setSubsStatus(String sSubsStatus) {
        
        this.sSubsStatus = sSubsStatus;
        
    }      
    
    /**
    * @return String 
    */
    public String getSubsNetStatus() {
        
        return this.sSubsNetStatus;
        
    }    
    
    /**  
    * @param sSubsNetStatus subscriber net status
    */
    public void setSubsNetStatus(String sSubsNetStatus) {
        
        this.sSubsNetStatus = sSubsNetStatus;
        
    }    
    
    /**
    * @return int 
    */
    public int getSubsTariff() {
        
        return this.iSubsTariff;
        
    }
    
    /**  
    * @param iSubsTariff subscriber tariff
    */
    public void setSubsTariff(int iSubsTariff) {
        
        this.iSubsTariff = iSubsTariff;
        
    }     
    
    /**
    * @return int 
    */
    public int getSUMarket() {
        
        return this.iSUMarket;
        
    }
    
    /**  
    * @param iSUMarket SU market
    */
    public void setSUMarket(int iSUMarket) {
        
        this.iSUMarket = iSUMarket;
        
    }    
    
    /**
    * @return int 
    */
    public int getSUContractType() {
        
        return this.iSUContractType;
        
    }
    
    /**  
    * @param iSUContractType SU contract type
    */
    public void setSUContractType(int iSUContractType) {
        
        this.iSUContractType = iSUContractType;
        
    }       
    
    /**
    * @return String 
    */
    public String getActivationType() {
        
        return this.sActivationType;
        
    }
    
    /**  
    * @param sActivationType activation type
    */
    public void setActivationType(String sActivationType) {
        
        this.sActivationType = sActivationType;
        
    }
    
    /**
    * @return int 
    */
    public int getContrDuration() {
        
        return this.iContrDuration;
        
    }
    
    /**  
    * @param iContrDuration contract duration
    */
    public void setContrDuration(int iContrDuration) {
        
        this.iContrDuration = iContrDuration;
        
    }   
    
    /**
    * @return int 
    */
    public int getSegmentAttr() {
        
        return this.iSegmentAttr;
        
    }
    
    /**  
    * @param iSegmentAttr segment attribute
    */
    public void setSegmentAttr(int iSegmentAttr) {
        
        this.iSegmentAttr = iSegmentAttr;
        
    }   
    
    /**
    * @return int 
    */
    public int getSegmentAttr2() {
        
        return this.iSegmentAttr2;
        
    }
    
    /**  
    * @param iSegmentAttr2 segment attribute 2
    */
    public void setSegmentAttr2(int iSegmentAttr2) {
        
        this.iSegmentAttr2 = iSegmentAttr2;
        
    }  
    
    /**
    * @return int 
    */
    public int getSegmentAttr3() {
        
        return this.iSegmentAttr3;
        
    }
    
    /**  
    * @param iSegmentAttr3 segment attribute 3
    */
    public void setSegmentAttr3(int iSegmentAttr3) {
        
        this.iSegmentAttr3 = iSegmentAttr3;
        
    }   
    
    /**
    * @return int 
    */
    public int getStatusReasonIDC() {
        
        return this.iStatusReasonIDC;
        
    }
    
    /**  
    * @param iStatusReasonIDC status reason IDC
    */
    public void setStatusReasonIDC(int iStatusReasonIDC) {
        
        this.iStatusReasonIDC = iStatusReasonIDC;
        
    }    
    
    /**
    * @return int 
    */
    public int getStatusReasonIDF() {
        
        return this.iStatusReasonIDF;
        
    }
    
    /**  
    * @param iStatusReasonIDF status reason IDF
    */
    public void setStatusReasonIDF(int iStatusReasonIDF) {
        
        this.iStatusReasonIDF = iStatusReasonIDF;
        
    }  
    
    /**
    * @return int 
    */
    public int getStatusReasonIDU() {
        
        return this.iStatusReasonIDU;
        
    }
    
    /**  
    * @param iStatusReasonIDU status reason IDU
    */
    public void setStatusReasonIDU(int iStatusReasonIDU) {
        
        this.iStatusReasonIDU = iStatusReasonIDU;
        
    }    
    
    /**
    * @return int 
    */
    public int getOutlet() {
        
        return this.iOutlet;
        
    }
    
    /**  
    * @param iOutlet outlet
    */
    public void setOutlet(int iOutlet) {
        
        this.iOutlet = iOutlet;
        
    }   
    
    /**
    * @return int 
    */
    public int getDunningSMS() {
        
        return this.iDunningSMS;
        
    }
    
    /**  
    * @param iDunningSMS dunning SMS
    */
    public void setDunningSMS(int iDunningSMS) {
        
        this.iDunningSMS = iDunningSMS;
        
    }  
    
    /**
    * @return int 
    */
    public int getWelcomeLetter() {
        
        return this.iWelcomeLetter;
        
    }
    
    /**  
    * @param iWelcomeLetter welcome letter
    */
    public void setWelcomeLetter(int iWelcomeLetter) {
        
        this.iWelcomeLetter = iWelcomeLetter;
        
    }    
    
    /**
    * @return String 
    */
    public String getNote() {
        
        return this.sNote;
        
    }
    
    /**  
    * @param sNote note
    */
    public void setNote(String sNote) {
        
        this.sNote = sNote;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbSUDetail = new StringBuilder();
        sbSUDetail.append("ExtidSU:").append(iExtidSU);
        sbSUDetail.append("|ExtidBA:").append(iExtidBA);
        sbSUDetail.append("|ExtidOU:").append(iExtidOU);
        sbSUDetail.append("|PCN:").append(iPCN);
        sbSUDetail.append("|MSISDN:").append(sMSISDN);
        sbSUDetail.append("|ICCID:").append(sICCID);
        sbSUDetail.append("|IMSI:").append(sIMSI);
        sbSUDetail.append("|SubsStatus:").append(sSubsStatus);
        sbSUDetail.append("|SubsNetStatus:").append(sSubsNetStatus);
        sbSUDetail.append("|SubsTariff:").append(iSubsTariff);
        sbSUDetail.append("|SUMarket:").append(iSUMarket);
        sbSUDetail.append("|SUContractType:").append(iSUContractType); 
        sbSUDetail.append("|ActivationType:").append(sActivationType);
        sbSUDetail.append("|ContrDuration:").append(iContrDuration);
        sbSUDetail.append("|SegmentAttr:").append(iSegmentAttr);
        sbSUDetail.append("|SegmentAttr2:").append(iSegmentAttr2);  
        sbSUDetail.append("|SegmentAttr3:").append(iSegmentAttr3);
        sbSUDetail.append("|StatusReasonIDC:").append(iStatusReasonIDC);
        sbSUDetail.append("|StatusReasonIDF:").append(iStatusReasonIDF);
        sbSUDetail.append("|StatusReasonIDU:").append(iStatusReasonIDU);
        sbSUDetail.append("|Outlet:").append(iOutlet);
        sbSUDetail.append("|DunningSMS:").append(iDunningSMS);
        sbSUDetail.append("|WelcomeLetter:").append(iWelcomeLetter);
        sbSUDetail.append("|Note:").append(sNote);
        
        return sbSUDetail.toString();
        
    }    
    
}