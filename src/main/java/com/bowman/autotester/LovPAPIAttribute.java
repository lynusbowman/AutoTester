package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* LovPAPIAttribute
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-20
*/

public class LovPAPIAttribute extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovPAPIAttribute.class.getName());      
    
    private int iID;
    private String sTitle;
    private String sDirection;
    private String sCardinality;
    private String sDataType;
    private String sType;
    private String sLovName;
    private String sLovParentID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovPAPIAttribute() {
        
        this.iID = -1;
        this.sTitle = null;
        this.sDirection = null;
        this.sCardinality = null;
        this.sDataType = null;
        this.sType = null;
        this.sLovName = null;
        this.sLovParentID = null;
        
    } 
    
    /**
     * @param iID ID
     * @param sTitle title
     * @param sDirection direction
     * @param sCardinality cardinality
     * @param sDataType data type
     * @param sType type
     * @param sLovName Lov name
     * @param sLovParentID Lov parent ID
     */
    public LovPAPIAttribute(int iID, String sTitle, String sDirection, String sCardinality,
                            String sDataType, String sType, String sLovName, String sLovParentID) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.sDirection = sDirection;
        this.sCardinality = sCardinality;
        this.sDataType = sDataType;
        this.sType = sType;
        this.sLovName = sLovName;
        this.sLovParentID = sLovParentID;
        
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
    public String getDirection() {
        
        return this.sDirection;
        
    }
    
    /**  
    * @param sDirection direction
    */
    public void setDirection(String sDirection) {
        
        this.sDirection = sDirection;
        
    }    
    
    /**
    * @return String 
    */
    public String getCardinality() {
        
        return this.sCardinality;
        
    }
    
    /**  
    * @param sCardinality cardinality
    */
    public void setCardinality(String sCardinality) {
        
        this.sCardinality = sCardinality;
        
    }     
    
    /**
    * @return String 
    */
    public String getDataType() {
        
        return this.sDataType;
        
    }
    
    /**  
    * @param sDataType data type
    */
    public void setDataType(String sDataType) {
        
        this.sDataType = sDataType;
        
    }  
    
    /**
    * @return String 
    */
    public String getType() {
        
        return this.sType;
        
    }
    
    /**  
    * @param sType type
    */
    public void setType(String sType) {
        
        this.sType = sType;
        
    }    
    
    /**
    * @return String 
    */
    public String getLovName() {
        
        return this.sLovName;
        
    }
    
    /**  
    * @param sLovName Lov name
    */
    public void setLovName(String sLovName) {
        
        this.sLovName = sLovName;
        
    }   
    
    /**
    * @return String 
    */
    public String getLovParentID() {
        
        return this.sLovParentID;
        
    }
    
    /**  
    * @param sLovParentID Lov parent ID
    */
    public void setLovParentID(String sLovParentID) {
        
        this.sLovParentID = sLovParentID;
        
    }       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbPAPIAttribute = new StringBuilder();
        sbPAPIAttribute.append("|ID:").append(iID); 
        sbPAPIAttribute.append("|Title:").append(sTitle); 
        sbPAPIAttribute.append("|Direction:").append(sDirection); 
        sbPAPIAttribute.append("|Cardinality:").append(sCardinality); 
        sbPAPIAttribute.append("|DataType:").append(sDataType); 
        sbPAPIAttribute.append("|Type:").append(sType); 
        sbPAPIAttribute.append("|LovName:").append(sLovName); 
        sbPAPIAttribute.append("|LovParentID:").append(sLovParentID); 
                       
        return sbPAPIAttribute.toString();
        
    }    
    
}