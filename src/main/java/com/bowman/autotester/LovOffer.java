package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovOffer
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovOffer extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovOffer.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sServiceLevel;
    private String sSocType;
    private List<String> lstParams;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovOffer() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sServiceLevel = null;
        this.sSocType = null;
        this.lstParams = null;       
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param lstParams params
    */
    public LovOffer(int iID, String sTitle, List<String> lstParams) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sServiceLevel = null;
        this.sSocType = null;        
        this.lstParams = lstParams;       
        
    }   
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param sServiceLevel service level
    * @param sSocType soc type
    */
    public LovOffer(int iID, String sTitle, String sServiceLevel, String sSocType) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sServiceLevel = sServiceLevel;
        this.sSocType = sSocType;        
        this.lstParams = null;       
        
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
    public String getServiceLevel() {
        
        return this.sServiceLevel;
        
    }
    
    /**  
    * @param sServiceLevel service level
    */
    public void setServiceLevel(String sServiceLevel) {
        
        this.sServiceLevel = sServiceLevel;
        
    }
    
    /**
    * @return String 
    */
    public String getSocType() {
        
        return this.sSocType;
        
    }
    
    /**  
    * @param sSocType soc type
    */
    public void setSocType(String sSocType) {
        
        this.sSocType = sSocType;
        
    }    
    
    /**
    * @return HashMap 
    */
    public List<String> getParams() {
        
        return this.lstParams;
        
    }      
    
    /**
    * @param lstParams params 
    */
    public void setParams(List<String> lstParams) {
        
        this.lstParams = lstParams;
        
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
        
        // detail from AMX
        if (sServiceLevel != null) {
            
            sbOffer.append("|ServiceLevel:").append(sServiceLevel);
            sbOffer.append("|SocType:").append(sSocType);
            
        }
        
        // params from CLF                  
        if (lstParams != null) {
            
            sbOffer.append("|Params#");
            
            for (String sParam : lstParams)
                sbOffer.append(sParam).append("|");
        
        }         
        
        return sbOffer.toString();
        
    }        
    
}