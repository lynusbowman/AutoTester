package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Customer level
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-06 
*/

public class CustLevel {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(CustLevel.class.getName());  
    
    /**
     * Type
     */
    public static enum Type {
        
        /**
         * CU
         */
        CU,
        
        /**
         * LE
         */
        LE,
        
        /**
         * OU
         */
        OU,
        
        /**
         * EU
         */
        EU,
        
        /**
         * BA
         */
        BA,
        
        /**
         * SU
         */
        SU
        
    }
    
    private String sType;
    private int iIntID;
    private int iExtID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public CustLevel() {
        
        this.sType = null;
        this.iIntID = -1;
        this.iExtID = -1;
        
    } 
    
    /**
     * @param sType type
     * @param iIntID INTID
     * @param iExtID EXTID
     */
    public CustLevel(Type sType, int iIntID, int iExtID) {
        
        this.sType = sType.toString();
        this.iIntID = iIntID;
        this.iExtID = iExtID;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getType() {
        
        return this.sType;
        
    }
    
    /**  
    * @param sType type
    */
    public void setType(Type sType) {
        
        this.sType = sType.toString();
        
    }   
    
    /**
    * @return int 
    */
    public int getIntID() {
        
        return this.iIntID;
        
    }
    
    /**  
    * @param iIntID INTID
    */
    public void setIntID(int iIntID) {
        
        this.iIntID = iIntID;
        
    }     
    
    /**
    * @return int 
    */
    public int getExtID() {
        
        return this.iExtID;
        
    }
    
    /**  
    * @param iExtID EXTID
    */
    public void setExtID(int iExtID) {
        
        this.iExtID = iExtID;
        
    }        
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbCustLevel = new StringBuilder();
        sbCustLevel.append("Level:").append(sType);
        sbCustLevel.append("|IntID:").append(iIntID);
        sbCustLevel.append("|ExtID:").append(iExtID);
        
        return sbCustLevel.toString();
        
    }    
    
}