package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovNonpublicOffer
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovNonpublicOffer extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovNonpublicOffer.class.getName());      
    
    private int iID;
    private String sTitle;
    private int iServiceID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovNonpublicOffer() {
        
        this.iID = -1;
        this.sTitle = null;
        this.iServiceID = -1;
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param iServiceID service ID
    */
    public LovNonpublicOffer(int iID, String sTitle, int iServiceID) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.iServiceID = iServiceID;
        
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
    * @return int 
    */
    public int getServiceID() {
        
        return this.iServiceID;
        
    }
    
    /**  
    * @param iServiceID service ID
    */
    public void setServiceID(int iServiceID) {
        
        this.iServiceID = iServiceID;
        
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbNonpublicOffer = new StringBuilder();
        sbNonpublicOffer.append("ID:").append(iID);
        sbNonpublicOffer.append("|Title:").append(sTitle); 
        sbNonpublicOffer.append("|ServiceID:").append(iServiceID);
        
        return sbNonpublicOffer.toString();
        
    }    
    
}