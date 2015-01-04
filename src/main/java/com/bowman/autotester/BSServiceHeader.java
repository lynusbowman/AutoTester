package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS service header
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSServiceHeader {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSServiceHeader.class.getName());      
    
    private String sUserName;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSServiceHeader() {
        
        this.sUserName = null;
        
    } 
    
    /**
     * @param sUserName user name
     */
    public BSServiceHeader(String sUserName) {
        
        this.sUserName = sUserName;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/   
    
    /**
    * @return String 
    */
    public String getUserName() {
        
        return this.sUserName;
        
    }
    
    /**  
    * @param sUserName user name
    */
    public void setUserName(String sUserName) {
        
        this.sUserName = sUserName;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSServiceHeader = new StringBuilder();
        sbBSServiceHeader.append("UserName:").append(sUserName);
        
        return sbBSServiceHeader.toString();
        
    }    
    
}