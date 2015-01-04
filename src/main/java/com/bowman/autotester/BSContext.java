package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* BS context
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

public class BSContext {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSContext.class.getName());      
    
    private String sAuthType;
    private BSTargetContext targetContext;
    private BSCallerContext callerContext;
    private String sChannel;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSContext() {
        
        this.sAuthType = null;
        this.targetContext = null;
        this.callerContext = null;
        this.sChannel = null;
        
    } 
    
    /**
     * @param sAuthType authentication type
     * @param targetContext target context
     * @param callerContext caller context
     * @param sChannel channel
     */
    public BSContext(String sAuthType, BSTargetContext targetContext, BSCallerContext callerContext,
                     String sChannel) {
        
        this.sAuthType = sAuthType;
        this.targetContext = targetContext;
        this.callerContext = callerContext;
        this.sChannel = sChannel;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getAuthType() {
        
        return this.sAuthType;
        
    }
    
    /**  
    * @param sAuthType authentication type
    */
    public void setAuthType(String sAuthType) {
        
        this.sAuthType = sAuthType;
        
    }    
    
    /**
    * @return BSTargetContext 
    */
    public BSTargetContext getTargetContext() {
        
        return this.targetContext;
        
    }
    
    /**  
    * @param targetContext target context
    */
    public void setTargetContext(BSTargetContext targetContext) {
        
        this.targetContext = targetContext;
        
    }      
    
    /**
    * @return BSCargetContext 
    */
    public BSCallerContext getCallerContext() {
        
        return this.callerContext;
        
    }
    
    /**  
    * @param callerContext caller context
    */
    public void setCallerContext(BSCallerContext callerContext) {
        
        this.callerContext = callerContext;
        
    }      
    
    /**
    * @return String 
    */
    public String getChannel() {
        
        return this.sChannel;
        
    }
    
    /**  
    * @param sChannel channel
    */
    public void setChannel(String sChannel) {
        
        this.sChannel = sChannel;
        
    }                       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSContext = new StringBuilder();
        sbBSContext.append("AuthType:").append(sAuthType);
        sbBSContext.append("|TargetContext#").append(targetContext.toString());
        sbBSContext.append("|CallerContext#").append(callerContext.toString());
        sbBSContext.append("|Channel:").append(sChannel);
        
        return sbBSContext.toString();
        
    }    
    
}