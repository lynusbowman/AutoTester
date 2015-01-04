package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS OMS status
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-22
*/

public class BSOmsStatus {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSOmsStatus.class.getName());      
    
    private String sOrderId;
    private String sOrderStatus;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSOmsStatus() {
        
        this.sOrderId = null;
        this.sOrderStatus = null;
        
    } 
    
    /**
     * @param sOrderId order ID
     * @param sOrderStatus order status
     */
    public BSOmsStatus(String sOrderId, String sOrderStatus) {
        
        this.sOrderId = sOrderId;
        this.sOrderStatus = sOrderStatus;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getOrderId() {
        
        return this.sOrderId;
        
    }
    
    /**  
    * @param sOrderId order ID
    */
    public void setOrderId(String sOrderId) {
        
        this.sOrderId = sOrderId;
        
    }    
    
    /**
    * @return String 
    */
    public String getOrderStatus() {
        
        return this.sOrderStatus;
        
    }
    
    /**  
    * @param sOrderStatus order status
    */
    public void setOrderStatus(String sOrderStatus) {
        
        this.sOrderStatus = sOrderStatus;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSOmsStatus = new StringBuilder();
        sbBSOmsStatus.append("OrderId:").append(sOrderId);
        sbBSOmsStatus.append("|OrderStatus:").append(sOrderStatus);
        
        return sbBSOmsStatus.toString();
        
    }    
    
}