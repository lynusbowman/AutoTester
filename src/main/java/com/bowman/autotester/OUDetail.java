package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* OU detail
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-08 
*/

public class OUDetail {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(OUDetail.class.getName());      
    
    private int iExtidOU;
    private int iExtidOUHigh;
    private int iFCN;
    private int iBusinessID;
    private String sCustomerStatus;
    private String sBillCycle;
    private int iSegment;
    private String sFraudNote;
    private int iHasLE;
    private int iPrimaryLE;
    private String sMAOperator;
    private String sName;
    private int iObjClass;
    private String sPartnerNote;
    private int iSegmentAttr;
    private int iVPNID;
    private String sHierarchyType;
    private int iNoSynchro;
    private int iPredeactivation;
         
    /**********************************
              constructors
    **********************************/ 
    
    public OUDetail() {

        this.iExtidOU = -1;
        this.iExtidOUHigh = -1;
        this.iFCN = -1;
        this.iBusinessID = -1;
        this.sCustomerStatus = null;
        this.sBillCycle = null;
        this.iSegment = -1;
        this.sFraudNote = null;
        this.iHasLE = -1;
        this.iPrimaryLE = -1;
        this.sMAOperator = null;
        this.sName = null;
        this.iObjClass = -1;
        this.sPartnerNote = null;
        this.iSegmentAttr = -1;
        this.iVPNID = -1;
        this.sHierarchyType = null;
        this.iNoSynchro = -1;
        this.iPredeactivation = -1;
        
    } 
    
    /**
     * @param iExtidOU EXTID_OU
     * @param iExtidOUHigh EXTID_OU high
     * @param iFCN FCN
     * @param iBusinessID business ID
     * @param sCustomerStatus customer status
     * @param sBillCycle billlcycle
     * @param iSegment segment
     * @param sFraudNote fraud note
     * @param iHasLE has LE
     * @param iPrimaryLE primary LE
     * @param sMAOperator MA operator
     * @param sName name
     * @param iObjClass obj class
     * @param sPartnerNote partner note
     * @param iSegmentAttr segment attribute
     * @param iVPNID VPN ID
     * @param sHierarchyType hierarchy type
     * @param iNoSynchro no synchro
     * @param iPredeactivation predeactivation
     */
    public OUDetail(int iExtidOU, int iExtidOUHigh, int iFCN, int iBusinessID, String sCustomerStatus, 
                    String sBillCycle, int iSegment, String sFraudNote, int iHasLE, int iPrimaryLE,
                    String sMAOperator, String sName, int iObjClass, String sPartnerNote,
                    int iSegmentAttr, int iVPNID, String sHierarchyType, int iNoSynchro, int iPredeactivation) {
        
        this.iExtidOU = iExtidOU;
        this.iExtidOUHigh = iExtidOUHigh;
        this.iFCN = iFCN;
        this.iBusinessID = iBusinessID;
        this.sCustomerStatus = sCustomerStatus;
        this.sBillCycle = sBillCycle;
        this.iSegment = iSegment;
        this.sFraudNote = sFraudNote;
        this.iHasLE = iHasLE;
        this.iPrimaryLE = iPrimaryLE;
        this.sMAOperator = sMAOperator;
        this.sName = sName;
        this.iObjClass = iObjClass;
        this.sPartnerNote = sPartnerNote;
        this.iSegmentAttr = iSegmentAttr;
        this.iVPNID = iVPNID;
        this.sHierarchyType = sHierarchyType;
        this.iNoSynchro = iNoSynchro;
        this.iPredeactivation = iPredeactivation;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
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
    public int getExtidOUHigh() {
        
        return this.iExtidOUHigh;
        
    }
    
    /**  
    * @param iExtidOUHigh EXTID_OU high
    */
    public void setExtidOUHigh(int iExtidOUHigh) {
        
        this.iExtidOUHigh = iExtidOUHigh;
        
    }      
    
    /**
    * @return int 
    */
    public int getFCN() {
        
        return this.iFCN;
        
    }
    
    /**  
    * @param iFCN FCN
    */
    public void setFCN(int iFCN) {
        
        this.iFCN = iFCN;
        
    }   
    
    /**
    * @return int 
    */
    public int getBusinessID() {
        
        return this.iBusinessID;
        
    }
    
    /**  
    * @param iBusinessID business ID
    */
    public void setBusinessID(int iBusinessID) {
        
        this.iBusinessID = iBusinessID;
        
    }    
    
    /**
    * @return String 
    */
    public String getCustomerStatus() {
        
        return this.sCustomerStatus;
        
    }
    
    /**  
    * @param sCustomerStatus customer status
    */
    public void setCustomerStatus(String sCustomerStatus) {
        
        this.sCustomerStatus = sCustomerStatus;
        
    }      
    
    /**
    * @return String 
    */
    public String getBillCycle() {
        
        return this.sBillCycle;
        
    }
    
    /**  
    * @param sBillCycle billcycle
    */
    public void setBillCycle(String sBillCycle) {
        
        this.sBillCycle = sBillCycle;
        
    }      
    
    /**
    * @return int 
    */
    public int getSegment() {
        
        return this.iSegment;
        
    }
    
    /**  
    * @param iSegment segment
    */
    public void setSegment(int iSegment) {
        
        this.iSegment = iSegment;
        
    }  
    
    /**
    * @return String 
    */
    public String getFraudNote() {
        
        return this.sFraudNote;
        
    }
    
    /**  
    * @param sFraudNote fraud note
    */
    public void setFraudNote(String sFraudNote) {
        
        this.sFraudNote = sFraudNote;
        
    }   
    
    /**
    * @return int 
    */
    public int getHasLE() {
        
        return this.iHasLE;
        
    }
    
    /**  
    * @param iHasLE has LE
    */
    public void setHasLE(int iHasLE) {
        
        this.iHasLE = iHasLE;
        
    }  
    
    /**
    * @return int 
    */
    public int getPrimaryLE() {
        
        return this.iPrimaryLE;
        
    }
    
    /**  
    * @param iPrimaryLE primary LE
    */
    public void setPrimaryLE(int iPrimaryLE) {
        
        this.iPrimaryLE = iPrimaryLE;
        
    }      
    
    /**
    * @return String 
    */
    public String getMAOperator() {
        
        return this.sMAOperator;
        
    }
    
    /**  
    * @param sMAOperator MA operator
    */
    public void setMAOperator(String sMAOperator) {
        
        this.sMAOperator = sMAOperator;
        
    }   
    
    /**
    * @return String 
    */
    public String getName() {
        
        return this.sName;
        
    }
    
    /**  
    * @param sName name
    */
    public void setName(String sName) {
        
        this.sName = sName;
        
    }      
    
    /**
    * @return int 
    */
    public int getObjClass() {
        
        return this.iObjClass;
        
    }
    
    /**  
    * @param iObjClass obj class
    */
    public void setObjClass(int iObjClass) {
        
        this.iObjClass = iObjClass;
        
    }  
    
    /**
    * @return String 
    */
    public String getPartnerNote() {
        
        return this.sPartnerNote;
        
    }
    
    /**  
    * @param sPartnerNote partner note
    */
    public void setPartnerNote(String sPartnerNote) {
        
        this.sPartnerNote = sPartnerNote;
        
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
    public int getVPNID() {
        
        return this.iVPNID;
        
    }
    
    /**  
    * @param iVPNID VPN ID
    */
    public void setVPNID(int iVPNID) {
        
        this.iVPNID = iVPNID;
        
    }   
    
    /**
    * @return String 
    */
    public String getHierarchyType() {
        
        return this.sHierarchyType;
        
    }
    
    /**  
    * @param sHierarchyType hierarchy type
    */
    public void setHierarchyType(String sHierarchyType) {
        
        this.sHierarchyType = sHierarchyType;
        
    }    
    
    /**
    * @return int 
    */
    public int getNoSynchro() {
        
        return this.iNoSynchro;
        
    }
    
    /**  
    * @param iNoSynchro no synchro
    */
    public void setNoSynchro(int iNoSynchro) {
        
        this.iNoSynchro = iNoSynchro;
        
    }     
    
    /**
    * @return int 
    */
    public int getPredeactivation() {
        
        return this.iPredeactivation;
        
    }
    
    /**  
    * @param iPredeactivation predeactivation
    */
    public void setPredeactivation(int iPredeactivation) {
        
        this.iPredeactivation = iPredeactivation;
        
    }    
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbOUDetail = new StringBuilder();
        sbOUDetail.append("ExtidOU:").append(iExtidOU);
        sbOUDetail.append("|ExtidOUHigh:").append(iExtidOUHigh);
        sbOUDetail.append("|FCN:").append(iFCN);
        sbOUDetail.append("|BusinessID:").append(iBusinessID);
        sbOUDetail.append("|CustomerStatus:").append(sCustomerStatus);
        sbOUDetail.append("|BillCycle:").append(sBillCycle);
        sbOUDetail.append("|Segment:").append(iSegment);
        sbOUDetail.append("|FraudNote:").append(sFraudNote);
        sbOUDetail.append("|HasLE:").append(iHasLE);
        sbOUDetail.append("|PrimaryLE:").append(iPrimaryLE);
        sbOUDetail.append("|MAOperator:").append(sMAOperator);
        sbOUDetail.append("|Name:").append(sName);
        sbOUDetail.append("|ObjClass:").append(iObjClass);
        sbOUDetail.append("|PartnerNote:").append(sPartnerNote);
        sbOUDetail.append("|SegmentAttr:").append(iSegmentAttr);
        sbOUDetail.append("|VPNID:").append(iVPNID);
        sbOUDetail.append("|HierarchyType:").append(sHierarchyType);
        sbOUDetail.append("|NoSynchro:").append(iNoSynchro);
        sbOUDetail.append("|Predeactivation:").append(iPredeactivation);
        
        return sbOUDetail.toString();
        
    }    
    
}