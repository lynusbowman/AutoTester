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
* BS fault
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22 
*/

public class BSFault extends WS {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSFault.class.getName());      
    
    private BSMessageContext messageContext;
    private String sTimestamp;
    private String sExceptionClass;
    private String sFurtherInfo;
    private List<String> lstBreData;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSFault() {
        
        this.messageContext = null;
        this.sTimestamp = null;
        this.sExceptionClass = null;
        this.sFurtherInfo = null;
        this.lstBreData = null;
        
    } 
    
    /**
     * @param messageContext message context
     * @param sTimestamp timestamp
     * @param sExceptionClass exception class
     * @param sFurtherInfo further info
     * @param lstBreData Bre data
     */
    public BSFault(BSMessageContext messageContext, String sTimestamp, String sExceptionClass,
                   String sFurtherInfo, List<String> lstBreData) {
        
        this.messageContext = messageContext;
        this.sTimestamp = sTimestamp;
        this.sExceptionClass = sExceptionClass;
        this.sFurtherInfo = sFurtherInfo;
        this.lstBreData = lstBreData;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/      
    
    /**
    * @return BSMessageContext
    */
    public BSMessageContext getMessageContext() {
        
        return this.messageContext;
        
    }
    
    /**  
    * @param messageContext message context
    */
    public void setMessageContext(BSMessageContext messageContext) {
        
        this.messageContext = messageContext;
        
    }    
    
    /**
    * @return String
    */
    public String getTimestamp() {
        
        return this.sTimestamp;
        
    }
    
    /**  
    * @param sTimestamp timestamp
    */
    public void setTimestamp(String sTimestamp) {
        
        this.sTimestamp = sTimestamp;
        
    }     
    
    /**
    * @return String
    */
    public String getExceptionClass() {
        
        return this.sExceptionClass;
        
    }
    
    /**  
    * @param sExceptionClass exception class
    */
    public void setExceptionClass(String sExceptionClass) {
        
        this.sExceptionClass = sExceptionClass;
        
    }   
    
    /**
    * @return String
    */
    public String getFurtherInfo() {
        
        return this.sFurtherInfo;
        
    }
    
    /**  
    * @param sFurtherInfo further info
    */
    public void setFurtherInfo(String sFurtherInfo) {
        
        this.sFurtherInfo = sFurtherInfo;
        
    }    
    
    /**
    * @return List
    */
    public List<String> getBreData() {
        
        return this.lstBreData;
        
    }
    
    /**  
    * @param lstBreData Bre data
    */
    public void setBreData(List<String> lstBreData) {
        
        this.lstBreData = lstBreData;
        
    }        
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSFault = new StringBuilder();
        
        sbBSFault.append("MessageContext#").append(messageContext.toString());
        sbBSFault.append("#Timestamp:").append(sTimestamp);
        sbBSFault.append("|ExceptionClass:").append(sExceptionClass);
        sbBSFault.append("|FurtherInfo:").append(sFurtherInfo);
        sbBSFault.append("|BreData#");
        
        for (String sTextInfo : lstBreData)
            sbBSFault.append("TextInfo:").append(sTextInfo).append("|");
        
        return sbBSFault.toString();
        
    }    
    
}