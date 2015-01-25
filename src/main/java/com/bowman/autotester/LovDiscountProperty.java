package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovDiscountProperty
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovDiscountProperty extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovDiscountProperty.class.getName());      
    
    private String sID;
    private String sTitle;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovDiscountProperty() {
        
        this.sID = null;
        this.sTitle = null;
        
    } 
    
   /**
    * @param sID ID
    * @param sTitle title
    */
    public LovDiscountProperty(String sID, String sTitle) {
        
        this.sID = sID;
        this.sTitle = sTitle;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getID() {
        
        return this.sID;
        
    }
    
    /**  
    * @param sID ID
    */
    public void setID(String sID) {
        
        this.sID = sID;
        
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
        
        StringBuilder sbDiscountProperty = new StringBuilder();
        sbDiscountProperty.append("ID:").append(sID);
        sbDiscountProperty.append("|Title:").append(sTitle); 
        
        return sbDiscountProperty.toString();
        
    }    
    
}