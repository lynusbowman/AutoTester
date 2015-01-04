package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Segment
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-06 
*/

public class Segment {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Segment.class.getName());      
    
    private int iID;
    private String sTitle;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Segment() {
        
        this.iID = -1;
        this.sTitle = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title 
     */
    public Segment(int iID, String sTitle) {
        
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
        
        StringBuilder sbSegment = new StringBuilder();
        sbSegment.append("ID:").append(iID);
        sbSegment.append("|Title:").append(sTitle);
        
        return sbSegment.toString();
        
    }    
    
}