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
* BS SAParameters
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-25 
*/

public class BSSAParameters {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(BSSAParameters.class.getName());      
    
    private List<BSSAParameter> lstGenericServiceSpecificParameters;
    private List<BSSAParameter> lstGenericActionSpecificParameters;
    
    /**********************************
              constructors
    **********************************/ 
    
    public BSSAParameters() {
        
        this.lstGenericServiceSpecificParameters = null;
        this.lstGenericActionSpecificParameters = null;
        
    } 
    
    /**
     * @param lstGenericServiceSpecificParameters generic service specific parameters
     * @param lstGenericActionSpecificParameters generic action specific parameters
     */
    public BSSAParameters(List<BSSAParameter> lstGenericServiceSpecificParameters,
                          List<BSSAParameter> lstGenericActionSpecificParameters) {
        
        this.lstGenericServiceSpecificParameters = lstGenericServiceSpecificParameters;
        this.lstGenericActionSpecificParameters = lstGenericActionSpecificParameters;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/      
    
    /**
    * @return List
    */
    public List<BSSAParameter> getGenericServiceSpecificParameters() {
        
        return this.lstGenericServiceSpecificParameters;
        
    }
    
    /**  
    * @param lstGenericServiceSpecificParameters generic service specific parameters
    */
    public void setGenericServiceSpecificParameters(List<BSSAParameter> lstGenericServiceSpecificParameters) {
        
        this.lstGenericServiceSpecificParameters = lstGenericServiceSpecificParameters;
        
    }  
    
    /**
    * @return List
    */
    public List<BSSAParameter> getGenericActionSpecificParameters() {
        
        return this.lstGenericActionSpecificParameters;
        
    }
    
    /**  
    * @param lstGenericActionSpecificParameters generic action specific parameters
    */
    public void setGenericActionSpecificParameters(List<BSSAParameter> lstGenericActionSpecificParameters) {
        
        this.lstGenericActionSpecificParameters = lstGenericActionSpecificParameters;
        
    }       
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbBSSAParameters = new StringBuilder();
        
        // generic service specific parameters
        sbBSSAParameters.append("GenericServiceSpecificParameters#");
        
        if (lstGenericServiceSpecificParameters != null) {
            
            for (BSSAParameter saParameter : lstGenericServiceSpecificParameters)
                sbBSSAParameters.append(saParameter.toString()).append("#");
        
        }
 
        
        // generic action specific parameters
        sbBSSAParameters.append("GenericActionSpecificParameters#");
        
        if (lstGenericActionSpecificParameters != null) {
            
            for (BSSAParameter saParameter : lstGenericActionSpecificParameters)
                sbBSSAParameters.append(saParameter.toString()).append("#"); 
        
        }
        
        return sbBSSAParameters.toString();
        
    }    
    
}