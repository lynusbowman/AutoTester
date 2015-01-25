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
* LovServiceParam
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-01-17 
*/

public class LovServiceParam extends Lov {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(LovServiceParam.class.getName());      
    
    private int iID;
    private String sTitle;
    private HashMap<String, String> hValues;
    
    private int iDataType;
    private String sDefaultValue;
    private int iRequired;
    private int iMultiple;
    private int iNetworkIndicator;
    
    /**********************************
              constructors
    **********************************/ 
    
    public LovServiceParam() {
        
        this.iID = -1;
        this.sTitle = null;
        this.hValues = null;
        
        this.iDataType = -1;
        this.sDefaultValue = null;
        this.iRequired = -1;
        this.iMultiple = -1;
        this.iNetworkIndicator = -1;        
        
    } 
    
   /**
    * @param iID ID
    * @param sTitle title
    * @param hValues values
    */
    public LovServiceParam(int iID, String sTitle, HashMap<String, String> hValues) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.hValues = hValues;
        
        this.iDataType = -1;
        this.sDefaultValue = null;
        this.iRequired = -1;
        this.iMultiple = -1;
        this.iNetworkIndicator = -1;         
        
    }    
    
    /**
     * @param iID ID
     * @param sTitle title
     * @param hValues values
     * @param iDataType data type
     * @param sDefaultValue default value
     * @param iRequired required
     * @param iMultiple multiple
     * @param iNetworkIndicator network indicator
     */
    public LovServiceParam(int iID, String sTitle, HashMap<String, String> hValues, int iDataType,
                           String sDefaultValue, int iRequired, int iMultiple, int iNetworkIndicator) {
        
        this.iID = iID;
        this.sTitle = sTitle;
        this.hValues = hValues;
        
        this.iDataType = iDataType;
        this.sDefaultValue = sDefaultValue;
        this.iRequired = iRequired;
        this.iMultiple = iMultiple;
        this.iNetworkIndicator = iNetworkIndicator;         
        
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
    * @return HashMap 
    */
    public HashMap<String, String> getValues() {
        
        return this.hValues;
        
    }  
    
    /**
    * @return int 
    */
    public int getDataType() {
        
        return this.iDataType;
        
    }
    
    /**  
    * @param iDataType data type
    */
    public void setDataType(int iDataType) {
        
        this.iDataType = iDataType;
        
    } 
    
    /**
    * @return String 
    */
    public String getDefaultValue() {
        
        return this.sDefaultValue;
        
    }
    
    /**  
    * @param sDefaultValue default value
    */
    public void setDefaultValue(String sDefaultValue) {
        
        this.sDefaultValue = sDefaultValue;
        
    }   
    
    /**
    * @return int 
    */
    public int getRequired() {
        
        return this.iRequired;
        
    }
    
    /**  
    * @param iRequired required
    */
    public void setRequired(int iRequired) {
        
        this.iRequired = iRequired;
        
    } 
    
    /**
    * @return int 
    */
    public int getMultiple() {
        
        return this.iMultiple;
        
    }
    
    /**  
    * @param iMultiple multiple
    */
    public void setMultiple(int iMultiple) {
        
        this.iMultiple = iMultiple;
        
    } 
    
    /**
    * @return int 
    */
    public int getNetworkIndicator() {
        
        return this.iNetworkIndicator;
        
    }
    
    /**  
    * @param iNetworkIndicator network indicator
    */
    public void setNetworkIndicator(int iNetworkIndicator) {
        
        this.iNetworkIndicator = iNetworkIndicator;
        
    }     
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbServiceParam = new StringBuilder();
        sbServiceParam.append("ID:").append(iID);
        sbServiceParam.append("|Title:").append(sTitle);                
        
        // param detail from CLF
        if (iDataType != -1) {
            
            sbServiceParam.append("|DataType:").append(iDataType);
            sbServiceParam.append("|DefaultValue:").append(sDefaultValue);
            sbServiceParam.append("|Required:").append(iRequired);
            sbServiceParam.append("|Multiple:").append(iMultiple);
            sbServiceParam.append("|NetworkIndicator:").append(iNetworkIndicator);
            
        }
        
        // values
        sbServiceParam.append("|Values#");  
        
        if (hValues != null) {
            
            for (Entry<String, String> value : hValues.entrySet())
                sbServiceParam.append(value.getKey()).append(":").append(value.getValue()).append("|");
        
        }         
        
        return sbServiceParam.toString();
        
    }  
    
    /**
    * Get value
    * @param sValue value
    * @return String
    */
    public String getValue(String sValue) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getValue() - params sValue:").append(sValue);
            logger.debug(sb);
            String sTitle = hValues.get(sValue);
            
            if (sTitle == null) {
                
                sb = new StringBuilder();
                sb.append("Value ").append(sValue).append(" not found");
                logger.error(sb);
                
            }
            
            return sTitle;
            
        }
        catch (Exception ex) {
            
            logger.error("getValue()", ex);
            
            return null;
        }
        
    }       
    
}