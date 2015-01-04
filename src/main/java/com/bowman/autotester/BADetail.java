package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BA detail
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-08 
*/

public class BADetail {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BADetail.class.getName());      
    
    private int iExtidBA;
    private int iExtidCU;
    private int iExtidOULE;
    private int iBAN;
    private String sBAName;
    private String sBAStatus;
    private String sSegmentAttr;
         
    /**********************************
              constructors
    **********************************/ 
    
    public BADetail() {

        this.iExtidBA = -1;
        this.iExtidCU = -1;
        this.iExtidOULE = -1;
        this.iBAN = -1;
        this.sBAName = null;
        this.sBAStatus = null;
        this.sSegmentAttr = null;
        
    } 
    
    /**
     * @param iExtidBA EXTID_BA
     * @param iExtidCU EXTID_CU
     * @param iExtidOULE EXTID_OULE
     * @param iBAN BAN
     * @param sBAName BA name
     * @param sBAStatus BA status
     * @param sSegmentAttr segment attribute
     */
    public BADetail(int iExtidBA, int iExtidCU, int iExtidOULE, int iBAN,
                    String sBAName, String sBAStatus, String sSegmentAttr) {
        
        this.iExtidBA = iExtidBA;
        this.iExtidCU = iExtidCU;
        this.iExtidOULE = iExtidOULE;
        this.iBAN = iBAN;
        this.sBAName = null;
        this.sBAStatus = sBAStatus;
        this.sSegmentAttr = sSegmentAttr;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
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
    public int getExtidCU() {
        
        return this.iExtidCU;
        
    }
    
    /**  
    * @param iExtidCU EXTID_CU
    */
    public void setExtidCU(int iExtidCU) {
        
        this.iExtidCU = iExtidCU;
        
    }     
    
    /**
    * @return int 
    */
    public int getExtidOULE() {
        
        return this.iExtidOULE;
        
    }
    
    /**  
    * @param iExtidOULE EXTID_OULE
    */
    public void setExtidOULE(int iExtidOULE) {
        
        this.iExtidOULE = iExtidOULE;
        
    }      
    
    /**
    * @return int 
    */
    public int getBAN() {
        
        return this.iBAN;
        
    }
    
    /**  
    * @param iBAN BAN
    */
    public void setBAN(int iBAN) {
        
        this.iBAN = iBAN;
        
    }     
    
    /**
    * @return String 
    */
    public String getBAName() {
        
        return this.sBAName;
        
    }
    
    /**  
    * @param sBAName BA name
    */
    public void setBAName(String sBAName) {
        
        this.sBAName = sBAName;
        
    }       
    
    /**
    * @return String 
    */
    public String getBAStatus() {
        
        return this.sBAStatus;
        
    }
    
    /**  
    * @param sBAStatus BA status
    */
    public void setBAStatus(String sBAStatus) {
        
        this.sBAStatus = sBAStatus;
        
    }            
    
    /**
    * @return String 
    */
    public String getSegmentAttr() {
        
        return this.sSegmentAttr;
        
    }
    
    /**  
    * @param sSegmentAttr segment attribute
    */
    public void setSegmentAttr(String sSegmentAttr) {
        
        this.sSegmentAttr = sSegmentAttr;
        
    }              
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBADetail = new StringBuilder();
        sbBADetail.append("ExtidBA:").append(iExtidBA);
        sbBADetail.append("|ExtidCU:").append(iExtidCU);
        sbBADetail.append("|ExtidOULE:").append(iExtidOULE);
        sbBADetail.append("|BAN:").append(iBAN);
        sbBADetail.append("|BAName:").append(sBAName);
        sbBADetail.append("|BAStatus:").append(sBAStatus);
        sbBADetail.append("|SegmentAttr:").append(sSegmentAttr);
        
        return sbBADetail.toString();
        
    }    
    
}