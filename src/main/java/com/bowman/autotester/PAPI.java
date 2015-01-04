package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* PAPI
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-10 
*/

public class PAPI {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(PAPI.class.getName());      
    
    private int iID;
    private String sAction;
    private String sStatus;
    private HashMap<Integer, String> hInAttributes;
    private HashMap<Integer, String> hOutAttributes;
    
    /**********************************
              constructors
    **********************************/ 
    
    public PAPI() {
        
        this.iID = -1;
        this.sAction = null;
        this.sStatus = null;
        this.hInAttributes = null;
        this.hOutAttributes = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sAction action
     * @param sStatus status
     * @param hInAttributes input attributes
     * @param hOutAttributes output attributes
     */
    public PAPI(int iID, String sAction, String sStatus, HashMap<Integer, String> hInAttributes,
                HashMap<Integer, String> hOutAttributes) {
        
        this.iID = iID;
        this.sAction = sAction;
        this.sStatus = sStatus;
        this.hInAttributes = hInAttributes;
        this.hOutAttributes = hOutAttributes;
        
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
    public String getAction() {
        
        return this.sAction;
        
    }
    
    /**  
    * @param sAction action
    */
    public void setAction(String sAction) {
        
        this.sAction = sAction;
        
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
    
    /**
    * @return HashMap 
    */
    public HashMap<Integer, String> getInAttributes() {
        
        return this.hInAttributes;
        
    }   
    
    /**
    * @return HashMap 
    */
    public HashMap<Integer, String> getOutAttributes() {
        
        return this.hOutAttributes;
        
    }     
    
    /**********************************
                methods
    **********************************/         
    
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbPAPI = new StringBuilder();
        sbPAPI.append("ID:").append(iID);
        sbPAPI.append("|Action:").append(sAction);
        sbPAPI.append("|Status:").append(sStatus);
        sbPAPI.append("|InAttributes:");
        
        // input attributes
        if (hInAttributes != null) {
            
            for (Entry<Integer, String> attr : hInAttributes.entrySet())
                sbPAPI.append(attr.getKey()).append(":").append(attr.getValue()).append("#");
        
        }
        
        // output attributes
        sbPAPI.append("|OutAttributes:");
        
        if (hOutAttributes != null) {
            
            for (Entry<Integer, String> attr : hOutAttributes.entrySet())
                sbPAPI.append(attr.getKey()).append(":").append(attr.getValue()).append("#");
        
        }        
        
        return sbPAPI.toString();
        
    }  
    
    /**
    * Get out attribute
    * @param iAttrID Attribute ID
    * @return String
    */
    public String getAttr(int iAttrID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getAttr() - params iAttrID:").append(iAttrID);
            logger.debug(sb);
            String sAttr = hOutAttributes.get(iAttrID);
            
            if (sAttr == null) {
                
                sb = new StringBuilder();
                sb.append("Attr ").append(iAttrID).append(" not found");
                logger.error(sb);
                
            }
            
            return sAttr;
            
        }
        catch (Exception ex) {
            
            logger.error("getAttr()", ex);
            
            return null;
        }
        
    }     
    
    /**
    * Get in attribute
    * @param iAttrID Attribute ID
    * @return String
    */
    public String getInAttr(int iAttrID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getInAttr() - params iAttrID:").append(iAttrID);
            logger.debug(sb);
            String sAttr = hInAttributes.get(iAttrID);
            
            if (sAttr == null) {
                
                sb = new StringBuilder();
                sb.append("Attr ").append(iAttrID).append(" not found");
                logger.error(sb);
                
            }
            
            return sAttr;
            
        }
        catch (Exception ex) {
            
            logger.error("getAttr()", ex);
            
            return null;
        }
        
    }      
    
}