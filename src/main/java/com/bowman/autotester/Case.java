package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Case
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-11 
*/

public class Case {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Market.class.getName());      
    
    private String sID;
    private String sStatus;
    private String sCaseType;
    private String sProcess;
    private String sSubject;
    private String sSubsubject;
    private String sStep;
    private String sHistory;
    private Date[] MFFD;
    private float[] MFFN;
    private String[] MFFT;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Case() {
        
        this.sID = null;
        this.sStatus = null;
        this.sCaseType = null;
        this.sProcess = null;
        this.sSubject = null;
        this.sSubsubject = null;
        this.sStep = null;
        this.sHistory = null;
        this.MFFD = null;
        this.MFFN = null;
        this.MFFT = null;
        
    } 
    
    /**
     * @param sID ID
     * @param sStatus status
     * @param sCaseType case type
     * @param sProcess process
     * @param sSubject subject
     * @param sSubsubject subsubject
     * @param sStep step
     * @param sHistory history
     * @param MFFD MFFD
     * @param MFFN MFFN
     * @param MFFT MFFT
     */
    public Case(String sID, String sStatus, String sCaseType, String sProcess, String sSubject,
                String sSubsubject, String sStep, String sHistory, 
                Date[] MFFD, float[] MFFN, String[] MFFT) {
        
        this.sID = sID;
        this.sStatus = sStatus;
        this.sCaseType = sCaseType;
        this.sProcess = sProcess;
        this.sSubject = sSubject;
        this.sSubsubject = sSubsubject;
        this.sStep = sStep;
        this.sHistory = sHistory;
        this.MFFD = MFFD;
        this.MFFN = MFFN;
        this.MFFT = MFFT;
        
    }     
    
    /**********************************
              getters, setters
    **********************************/ 
    
    /**
    * @return String 
    */
    public String getID() {
        
        return this.sID;
        
    }
    
    /**  
    * @param sID ID
    */
    public void setID(String sID) {
        
        this.sID = sID;
        
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
    * @return String 
    */
    public String getCaseType() {
        
        return this.sCaseType;
        
    }
    
    /**  
    * @param sCaseType case type
    */
    public void setCaseType(String sCaseType) {
        
        this.sCaseType = sCaseType;
        
    }     
    
    /**
    * @return String 
    */
    public String getProcess() {
        
        return this.sProcess;
        
    }
    
    /**  
    * @param sProcess process
    */
    public void setProcess(String sProcess) {
        
        this.sProcess = sProcess;
        
    }     
    
    /**
    * @return String 
    */
    public String getSubject() {
        
        return this.sSubject;
        
    }
    
    /**  
    * @param sSubject subject
    */
    public void setSubject(String sSubject) {
        
        this.sSubject = sSubject;
        
    }     
    
    /**
    * @return String 
    */
    public String getSubsubject() {
        
        return this.sSubsubject;
        
    }
    
    /**  
    * @param sSubsubject subsubject
    */
    public void setSubsubject(String sSubsubject) {
        
        this.sSubsubject = sSubsubject;
        
    }     
    
    /**
    * @return String 
    */
    public String getStep() {
        
        return this.sStep;
        
    }
    
    /**  
    * @param sStep step
    */
    public void setStep(String sStep) {
        
        this.sStep = sStep;
        
    }   
    
    /**
    * @return String 
    */
    public String getHistory() {
        
        return this.sHistory;
        
    }
    
    /**  
    * @param sHistory history
    */
    public void setHistory(String sHistory) {
        
        this.sHistory = sHistory;
        
    }   
    
    /**
    * @return Date[] 
    */
    public Date[] getMFFD() {
        
        return this.MFFD;
        
    }
    
    /**  
    * @param MFFD MFFD
    */
    public void setMFFD(Date[] MFFD) {
        
        this.MFFD = MFFD;
        
    }   
    
    /**
    * @return float[] 
    */
    public float[] getMFFN() {
        
        return this.MFFN;
        
    }
    
    /**  
    * @param MFFN MFFN
    */
    public void setMFFN(float[] MFFN) {
        
        this.MFFN = MFFN;
        
    }  
    
    /**
    * @return String[] 
    */
    public String[] getMFFT() {
        
        return this.MFFT;
        
    }
    
    /**  
    * @param MFFT MFFT
    */
    public void setMFFT(String[] MFFT) {
        
        this.MFFT = MFFT;
        
    }      
    
    /**********************************
                methods
    **********************************/   
        
    /**   
    * @return String 
    */
    @Override
    public String toString() {
        
        StringBuilder sbCase = new StringBuilder();
        sbCase.append("ID:").append(sID);
        sbCase.append("|Status:").append(sStatus);
        sbCase.append("|CaseType:").append(sCaseType);
        sbCase.append("|Process:").append(sProcess);
        sbCase.append("|Subject:").append(sSubject);
        sbCase.append("|Subsubject:").append(sSubsubject);
        sbCase.append("|Step:").append(sStep);
        sbCase.append("|History:").append(sHistory);
        sbCase.append("|MFFD:").append(Arrays.toString(MFFD));
        sbCase.append("|MFFN:").append(Arrays.toString(MFFN));
        sbCase.append("|MFFT:").append(Arrays.toString(MFFT));
        
        return sbCase.toString();
        
    }    
    
}