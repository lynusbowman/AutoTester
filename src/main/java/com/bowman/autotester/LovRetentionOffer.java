package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovRetentionOffer
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovRetentionOffer extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovRetentionOffer.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sOption;
    private String sGroup;
    private int iSubsubject;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovRetentionOffer() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sOption = null;
        this.sGroup = null;
        this.iSubsubject = -1;
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param sOption option
    * @param sGroup group
    * @param iSubsubject subsubject
    */
    public LovRetentionOffer(int iID, String sTitle, String sOption, String sGroup, int iSubsubject) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sOption = sOption;
        this.sGroup = sGroup;
        this.iSubsubject = iSubsubject;
        
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
    public String getOption() {
        
        return this.sOption;
        
    }
    
    /**  
    * @param sOption option
    */
    public void setOption(String sOption) {
        
        this.sOption = sOption;
        
    }  
    
    /**
    * @return String 
    */
    public String getGroup() {
        
        return this.sGroup;
        
    }
    
    /**  
    * @param sGroup group
    */
    public void setGroup(String sGroup) {
        
        this.sGroup = sGroup;
        
    } 
    
    /**
    * @return int 
    */
    public int getSubsubject() {
        
        return this.iSubsubject;
        
    }
    
    /**  
    * @param iSubsubject subsubject
    */
    public void setSubsubject(int iSubsubject) {
        
        this.iSubsubject = iSubsubject;
        
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbRetentionOffer = new StringBuilder();
        sbRetentionOffer.append("ID:").append(iID);
        sbRetentionOffer.append("|Title:").append(sTitle); 
        sbRetentionOffer.append("|Option:").append(sOption);
        sbRetentionOffer.append("|Group:").append(sGroup);
        sbRetentionOffer.append("|Subsubject:").append(iSubsubject);
        
        return sbRetentionOffer.toString();
        
    }    
    
}