package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Offer
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-10 
*/

public class Offer {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Offer.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sStatus;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Offer() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sStatus = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title 
     * @param sStatus status
     */
    public Offer(int iID, String sTitle, String sStatus) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sStatus = sStatus;
        
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
    public String getStatus() {
        
        return this.sStatus;
        
    }
    
    /**  
    * @param sStatus status
    */
    public void setStatus(String sStatus) {
        
        this.sStatus = sStatus;
        
    }          
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbOffer = new StringBuilder();
        sbOffer.append("ID:").append(iID);
        sbOffer.append("|Title:").append(sTitle);
        sbOffer.append("|Status:").append(sStatus);
        
        return sbOffer.toString();
        
    }    
    
}