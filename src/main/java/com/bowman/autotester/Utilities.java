package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.lang.StringBuilder;
import java.lang.Thread;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
* Utilities for scripting
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-03 
*/

public class Utilities {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(Utilities.class.getName());  
    
    // beans
    private TestCaseBean testCaseBean; 
    private CLFBean clfBean;
    private DWHBean dwhBean;
    private APBean apBean;
    private CNHBean cnhBean;
    private AMXBean amxBean;
    private WSBean wsBean;
    
    // utitilities
    private UtilitiesCLF clf;
    private UtilitiesDWH dwh;
    private UtilitiesAP ap;
    private UtilitiesCNH cnh;
    private UtilitiesAMX amx;
    private UtilitiesWS ws;
    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public Utilities() {
        
        this.testCaseBean = null;
        this.clfBean = null;
        this.dwhBean = null;
        this.apBean = null;
        this.cnhBean = null;
        this.amxBean = null;
        this.wsBean = null;
        
        this.clf = null;
        this.dwh = null;
        this.ap = null;
        this.cnh = null;
        this.amx = null;
        this.ws = null;
        
        this.iLogID = -1;
        
    }    

    /**    
    * @param iLogID log ID
    * */        
    
    public Utilities(int iLogID) {
        
        this.iLogID = iLogID;
        
        lookupBeans();
        setUtilities();
    }
    
    /**********************************
                methods
    **********************************/     
    
    /**
    * lookup beans
    * TestCaseBean, CLFBean, DWHBean, APBean, CNHBean, AMXBean, WSBean
    */
    public void lookupBeans() {
        
        try {
            
            logger.debug("lookupBeans()");
            
            // test case bean
            testCaseBean = null;
            Context ctx = new InitialContext();
            testCaseBean = (TestCaseBean) ctx.lookup("java:global/AutoTester/TestCaseBean"); 
            
            // CLF bean
            clfBean = null;
            clfBean = (CLFBean) ctx.lookup("java:global/AutoTester/CLFBean");      
            
            // DWH bean
            dwhBean = null;
            dwhBean = (DWHBean) ctx.lookup("java:global/AutoTester/DWHBean");    
            
            // AP bean
            apBean = null;
            apBean = (APBean) ctx.lookup("java:global/AutoTester/APBean"); 
            
            // CNH bean
            cnhBean = null;
            cnhBean = (CNHBean) ctx.lookup("java:global/AutoTester/CNHBean");             
            
            // AMX bean
            amxBean = null;
            amxBean = (AMXBean) ctx.lookup("java:global/AutoTester/AMXBean");    
            
            // WS bean
            wsBean = null;
            wsBean = (WSBean) ctx.lookup("java:global/AutoTester/WSBean");             
            
        }
        catch (Exception ex) {
            
            logger.error("lookupBeans()", ex);
            
        }
    }   
    
    /**
    * set utilities
    * UtilitiesCLF, UtilitiesDWH, UtilitiesAP, UtilitiesCNH, UtilitiesAMX, UtilitiesWS
    */
    public void setUtilities() {
        
        try {
            
            logger.debug("setUtilities()");
            
            clf = new UtilitiesCLF(this, this.clfBean, this.iLogID);
            dwh = new UtilitiesDWH(this, this.dwhBean, this.iLogID);
            ap = new UtilitiesAP(this, this.apBean, this.iLogID);
            cnh = new UtilitiesCNH(this, this.cnhBean, this.iLogID);
            amx = new UtilitiesAMX(this, this.amxBean, this.iLogID);
            ws = new UtilitiesWS(this, this.wsBean, this.iLogID);
            
        }
        catch (Exception ex) {
            
            logger.error("setUtilities()", ex);
            
        }
    }       
    
    /**
    * Write to log in DB
    * @param sResult result
    * @param sLog log text
    */
    public void log(Result sResult, String sLog) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("log() - corrId=").append(iLogID).append(", params sResult:").append(sResult).append(", sLog:").append(sLog);
            logger.info(sb);
            
            testCaseBean.createTestRunLog(iLogID, sResult, sLog);
            
        }
        catch (Exception ex) {
            
            logger.error("log()", ex);
            
        }
    } 
    
    /**
    * Write to log in DB
    * @param oLog log text
    */
    public void log(Object oLog) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("log() - corrId=").append(iLogID).append(", params oLog:").append(oLog);
            logger.info(sb);
            
            testCaseBean.createTestRunLog(iLogID, Result.INFO, String.valueOf(oLog));
            
        }
        catch (Exception ex) {
            
            logger.error("log()", ex);
            
        }
    }    
    
    /**
    * Write error to log in DB
    * @param oErr error text
    */
    public void err(Object oErr) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("err() - corrId=").append(iLogID).append(", params oErr:").append(oErr);
            logger.info(sb);
            
            testCaseBean.createTestRunLog(iLogID, Result.ERR, String.valueOf(oErr));
            
        }
        catch (Exception ex) {
            
            logger.error("err()", ex);
            
        }
    }  
    
    /**
    * Connect to applications
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        String sDB = null;
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            // CLF
            sDB = "CLF";
            clf.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);
            log(Result.OK, sb.toString());
            
            // DWH
            sDB = "DWH";
            dwh.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);            
            log(Result.OK, sb.toString());
            
            // AP
            sDB = "AP";
            ap.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);            
            log(Result.OK, sb.toString());
            
            // CNH
            sDB = "CNH";
            cnh.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);            
            log(Result.OK, sb.toString());            
            
            // AMX
            sDB = "AMX";
            amx.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);            
            log(Result.OK, sb.toString());
            
            // WS
            sDB = "WS";
            ws.connect(sEnvironment);
            
            sb = new StringBuilder();
            sb.append("Connect to ").append(sDB);            
            log(Result.OK, sb.toString());            
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to ").append(sDB).append(" on ").append(sEnvironment);
            log(Result.ERR, "Failed to connect to " + sDB + " on " + sEnvironment);
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from applications
    * @return int
    */
    public int disconnect() {
        
        String sDB = null;
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            // CLF
            sDB = "CLF";
            clf.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());
            
            // DWH
            sDB = "DWH";
            dwh.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());
            
            // AP
            sDB = "AP";
            ap.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());
            
            // CNH
            sDB = "CNH";
            cnh.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());          
            
            // AMX
            sDB = "AMX";
            amx.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());
            
            // WS
            sDB = "WS";
            ws.disconnect();
            
            sb = new StringBuilder();
            sb.append("Disconnect from ").append(sDB);
            log(Result.OK, sb.toString());          
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to disconnect from ").append(sDB);
            log(Result.ERR, sb.toString());
            
            return 0;
        }
    }                    
   
    /**
    * Check single value
    * @param sLog log text
    * @param oValue tested value
    * @param oCheck checked value
    * @return int
    */
    public int check(String sLog, Object oValue, Object oCheck) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("check() - corrId=").append(iLogID).append(", params sLog:").append(sLog).append(", oValue:").append(oValue);
            sb.append(", oCheck:").append(oCheck);
            logger.info(sb);
            
            // check passed
            if (oValue.equals(oCheck)) {
                         
                sb = new StringBuilder();
                sb.append("Check ").append(sLog).append(" - ").append(oValue).append("=").append(oCheck);
                log(Result.OK, sb.toString());
                
                return 1;
                
            }
            // check failed
            else {
                
                sb = new StringBuilder();
                sb.append("Failed check ").append(sLog).append(" - ").append(oValue).append("=").append(oCheck);
                log(Result.ERR, sb.toString());
                
                return 0;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("check()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check ").append(oValue).append("=").append(oCheck);
            log(Result.ERR, sb.toString());
            
            return 0;
        }
        
    }  
    
    /**
    * Check array values
    * @param sLog log text
    * @param oValues tested values
    * @param oChecks checked values
    * @return int
    */
    public int check(String sLog, List<Object> oValues, List<Object> oChecks) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("check() - corrId=").append(iLogID).append(", params sLog:").append(sLog);
            sb.append(", oValues:").append(Arrays.toString(oValues.toArray())).append(", oChecks:").append(Arrays.toString(oChecks.toArray()));
            logger.info(sb);
            
            // loop check
            boolean bPassed = true;
            StringBuilder sbText = new StringBuilder();
            Object oValue;
            Object oCheck;
            
            for (int i = 0; i < oValues.size(); i++) {
                
                oValue = oValues.get(i);
                oCheck = oChecks.get(i);
                
                // check passed
                if (oValue != null && oValue.equals(oCheck))
                    sbText.append("OK:").append(oValue).append("=").append(oCheck).append("|");
                // check failed
                else {
                    
                    sbText.append("ERR:").append(oValue).append("=").append(oCheck).append("|");
                    bPassed = false;
                    
                }
                
            }
            
            if (bPassed) {
                
                sb = new StringBuilder();
                sb.append("Check ").append(sLog).append(" - ").append(sbText);
                log(Result.OK, sb.toString());    
                
                return 1;
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Failed check ").append(sLog).append(" - ").append(sbText);
                log(Result.ERR, sb.toString()); 
                
                return 0;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("check()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check ").append(Arrays.toString(oValues.toArray())).append("=").append(Arrays.toString(oChecks.toArray()));
            log(Result.ERR, sb.toString());
            
            return 0;
        }
        
    }   
    
    /**
    * Check map values
    * @param sLog log text
    * @param oValues tested values
    * @param oCheckedKeys checked keys
    * @param oCheckedValues checked values
    * @return int
    */
    public int check(String sLog, HashMap<Object, Object> oValues, List<Object> oCheckedKeys, List<Object> oCheckedValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("check() - corrId=").append(iLogID).append(", params sLog:").append(sLog);
            sb.append(", oValues:").append(Arrays.toString(oValues.keySet().toArray()));
            sb.append(":").append(Arrays.toString(oValues.values().toArray()));
            sb.append(", oCheckedKeys:").append(Arrays.toString(oCheckedKeys.toArray()));
            sb.append(", oCheckedValues:").append(Arrays.toString(oCheckedValues.toArray()));
            logger.info(sb);
            
            // loop check
            boolean bPassed = true;
            StringBuilder sbText = new StringBuilder();
            Object oKey;
            Object oValue;
            Object oCheckedValue;
            
            for (int i = 0; i < oCheckedKeys.size(); i++) {
                
                // key presence
                oKey = oCheckedKeys.get(i);
                
                if (oValues.containsKey(oKey)) {
                    
                   oValue = oValues.get(oKey);
                   oCheckedValue = oCheckedValues.get(i);
                    
                   // checked passed
                   if (oValue != null && oValue.equals(oCheckedValue))
                       sbText.append("OK:").append(oKey).append("#").append(oValue).append("=").append(oCheckedValue).append("|");
                   // check failed
                   else {
                       
                       sbText.append("ERR:").append(oKey).append("#").append(oValue).append("=").append(oCheckedValue).append("|");
                       bPassed = false;
                       
                   }
                    
                }   
                // key missing
                else {
                    
                    sbText.append("ERR:key ").append(oKey).append(" missing|");
                    bPassed = false;
                    
                }
                
            }
            
            if (bPassed) {
                
                sb = new StringBuilder();
                sb.append("Check ").append(sLog).append(" - ").append(sbText);
                log(Result.OK, sb.toString()); 
                
                return 1;
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Failed check ").append(sLog).append(" - ").append(sbText);
                log(Result.ERR, sb.toString());  
                
                return 0;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("check()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check ").append(Arrays.toString(oValues.keySet().toArray()));
            sb.append(":").append(Arrays.toString(oValues.values().toArray())).append("=");
            sb.append(Arrays.toString(oCheckedKeys.toArray())).append(":").append(Arrays.toString(oCheckedValues.toArray()));
            log(Result.ERR, sb.toString());
            
            return 0;
        }
        
    }   
    
    /**
    * Check single value as negative
    * @param sLog log text
    * @param oValue tested value
    * @param oCheck checked value
    * @return int
    */
    public int checkNok(String sLog, Object oValue, Object oCheck) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("checkNok() - corrId=").append(iLogID).append(", params sLog:").append(sLog).append(", oValue:").append(oValue);
            sb.append(", oCheck:").append(oCheck);
            logger.info(sb);
            
            // check passed
            if (oValue != null && !oValue.equals(oCheck)) {
                         
                sb = new StringBuilder();
                sb.append("Check ").append(sLog).append(" - ").append(oValue).append("!=").append(oCheck);
                log(Result.OK, sb.toString());
                
                return 1;
                
            }
            // check failed
            else {
                
                sb = new StringBuilder();
                sb.append("Failed check ").append(sLog).append(" - ").append(oValue).append("!=").append(oCheck);
                log(Result.ERR, sb.toString());
                
                return 0;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("checkNok()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check ").append(oValue).append("!=").append(oCheck);
            log(Result.ERR, sb.toString());
            
            return 0;
        }
        
    }  
    
    /**
    * Check single value as like
    * @param sLog log text
    * @param oValue tested value
    * @param oCheck checked value
    * @return int
    */
    public int checkLike(String sLog, Object oValue, Object oCheck) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("checkLike() - corrId=").append(iLogID).append("params sLog:").append(sLog).append(", oValue:").append(oValue);
            sb.append(", oCheck:").append(oCheck);
            logger.info(sb);
            
            // check passed
            if (oValue != null && String.valueOf(oValue).indexOf(String.valueOf(oCheck)) > 0) {
                
                sb = new StringBuilder();
                sb.append("Check ").append(sLog).append(" - ").append(oValue).append(" like").append(oCheck);
                log(Result.OK, sb.toString());
                
                return 1;
                
            }
            // check failed
            else {
                
                sb = new StringBuilder();
                sb.append("Failed check ").append(sLog).append(" - ").append(oValue).append(" like").append(oCheck);
                log(Result.ERR, sb.toString());
                
                return 0;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("checkLike()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to check ").append(oValue).append(" like").append(oCheck);
            log(Result.ERR, sb.toString());
            
            return 0;
        }
        
    }  
    
    /**
    * pause
    * @param fDuration duration
    */
    public void pause(float fDuration) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("pause() - corrId=").append(iLogID).append(", params fDuration:").append(fDuration);
            logger.info(sb);
            
            if (fDuration > 0) {
                         
                sb = new StringBuilder();
                sb.append("Pause ").append(fDuration).append(" seconds");
                log(Result.INFO, sb.toString());
                
                Thread.sleep((long) (fDuration * 1000));
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append(fDuration).append(" must be > 0");
                log(Result.ERR, sb.toString());
                
            }

        }
        catch (Exception ex) {
            
            logger.error("pause()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to pause ").append(fDuration).append(" seconds");
            log(Result.ERR, sb.toString());
            
        }
        
    }   
    
    /**
    * Get test data
    * @param sEnvironment environment
    * @return int
    */
    public int getTestData(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestData() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            if (sEnvironment.equals("TEST 1") || sEnvironment.equals("TEST 2")) {
                       
              int iData = testCaseBean.getTestData(iLogID, sEnvironment).get(0).getData();
              
              sb = new StringBuilder();
              sb.append("Get data ").append(iData);
              log(Result.OK, sb.toString());
              return iData;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append(sEnvironment).append(" must be in test1 or test2");
                log(Result.ERR, sb.toString());
                return -1;
                
            }

        }
        catch (Exception ex) {
            
            logger.error("pause()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get data ");
            log(Result.ERR, sb.toString());
            return -1;
            
        }
        
    }       
    
}