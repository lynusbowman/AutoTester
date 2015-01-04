package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Contract duration
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-06 
*/

public class ContractDuration {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(ContractDuration.class.getName());      
    
    private int iID;
    private String sTitle;
    
    /**********************************
              constructors
    **********************************/ 
    
    public ContractDuration() {
        
        this.iID = -1;
        this.sTitle = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title 
     */
    public ContractDuration(int iID, String sTitle) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        
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
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbContractDuration = new StringBuilder();
        sbContractDuration.append("ID:").append(iID);
        sbContractDuration.append("|Title:").append(sTitle);
        
        return sbContractDuration.toString();
        
    }    
    
}