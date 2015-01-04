package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.CustLevel.Type;
import java.lang.StringBuilder;

import java.lang.String;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Customer hierarchy
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

public class CustHierarchy {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(CustHierarchy.class.getName());  
    
    // customer levels
    private CustLevel CU;
    private CustLevel LE;
    private CustLevel OU;
    private CustLevel EU;
    private CustLevel BA;
    private CustLevel SU;
    
    /**********************************
              constructors
    **********************************/ 
    
    public CustHierarchy() {
        
        this.CU = null;
        this.LE = null;
        this.OU = null;
        this.EU = null;
        this.BA = null;
        this.SU = null;
        
    } 
    
    /**
     * @param CU CU
     * @param LE LE
     * @param OU OU
     * @param EU EU
     * @param BA BA
     * @param SU SU
     */
    public CustHierarchy(CustLevel CU, CustLevel LE, CustLevel OU,
                         CustLevel EU, CustLevel BA, CustLevel SU) {
        
        this.CU = CU;
        this.LE = LE;
        this.OU = OU;
        this.EU = EU;
        this.BA = BA;
        this.SU = SU;
        
    }
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return CustLevel
    */
    public CustLevel getCU() {
        
        return this.CU;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setCU(int iIntID, int iExtID) {
        
        this.CU = new CustLevel(Type.CU, iIntID, iExtID);
        
    }    
    
    /**
    * @return CustLevel
    */
    public CustLevel getLE() {
        
        return this.LE;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setLE(int iIntID, int iExtID) {
        
        this.LE = new CustLevel(Type.LE, iIntID, iExtID);
        
    }   
    
    /**
    * @return CustLevel
    */
    public CustLevel getOU() {
        
        return this.OU;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setOU(int iIntID, int iExtID) {
        
        this.OU = new CustLevel(Type.OU, iIntID, iExtID);
        
    }      
    
    /**
    * @return CustLevel
    */
    public CustLevel getEU() {
        
        return this.EU;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setEU(int iIntID, int iExtID) {
        
        this.EU = new CustLevel(Type.EU, iIntID, iExtID);
        
    }      
    
    /**
    * @return CustLevel
    */
    public CustLevel getBA() {
        
        return this.BA;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setBA(int iIntID, int iExtID) {
        
        this.BA = new CustLevel(Type.BA, iIntID, iExtID);
        
    }      
    
    /**
    * @return CustLevel
    */
    public CustLevel getSU() {
        
        return this.SU;
        
    }
    
    /**  
    * @param iIntID INTID
    * @param iExtID EXTID
    */
    public void setSU(int iIntID, int iExtID) {
        
        this.SU = new CustLevel(Type.SU, iIntID, iExtID);
        
    }      
    
    /**********************************
                methods
    **********************************/   
    
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbCustHierarchy = new StringBuilder();
        sbCustHierarchy.append(CU.toString()).append("|");
        sbCustHierarchy.append(LE.toString()).append("|");
        sbCustHierarchy.append(OU.toString()).append("|");
        sbCustHierarchy.append(EU.toString()).append("|");
        sbCustHierarchy.append(BA.toString()).append("|");
        sbCustHierarchy.append(SU.toString()).append("|");
        
        return sbCustHierarchy.toString();
        
    }        
    
}