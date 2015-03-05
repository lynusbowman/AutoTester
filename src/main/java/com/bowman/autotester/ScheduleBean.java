package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.ejb.Asynchronous;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
* Singleton bean, manages scheduled actions
* 
* @author  Petr Rasek
* @version 1.0
* @since   2015-02-17 
*/

@Singleton
public class ScheduleBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(ScheduleBean.class.getName());
    
    // beans
    private TestCaseBean testCaseBean;
    
    /**********************************
              methods
    **********************************/           
    
    /**
    * lookup beans
    */
    public void lookupBeans() {
        
        try {
            
            logger.debug("lookupBeans()");
            
            // test case bean
            testCaseBean = null;
            Context ctx = new InitialContext();
            testCaseBean = (TestCaseBean) ctx.lookup("java:global/AutoTester/TestCaseBean");            
            
        }
        catch (Exception ex) {
            
            logger.error("lookupBeans()", ex);
            
        }
    }       
    
    /**
    * Load configuration
    * @return Properties
    */    
    public Properties loadConfig() {
        
        InputStream isConfig = null;
        Properties properties = new Properties();
        
        try {
            
            // load configuration as properties
            logger.debug("loadConfig()");
            String sConfig = "config.properties";
            isConfig = TestCaseBean.class.getClassLoader().getResourceAsStream(sConfig);
            
            if (isConfig == null) {
                
                StringBuilder sb = new StringBuilder();
                sb.append("Config file ").append(sConfig).append(" not found");
                logger.error(sb);
                return null;
                
            }
            
            properties.load(isConfig);
            
            // log properties
            Enumeration<?> eProperties = properties.propertyNames();
            String sKey;
            String sValue;
            StringBuilder sb;
            
            while (eProperties.hasMoreElements()) {
                
                sKey = eProperties.nextElement().toString();
                sValue = properties.getProperty(sKey);
                
                sb = new StringBuilder();
                sb.append("Key: ").append(sKey).append(", Value: ").append(sValue);
                logger.debug(sb);  
                                               
            }
            
            logger.info("Configuration loaded");
            return properties;
            
        }
        catch (Exception ex) {
            
            logger.error("loadConfig()", ex);
            return null;
            
        }
        finally {
            
            if (isConfig != null) {
                
                try {
                    
                    isConfig.close();   
                    
                }
                catch (Exception ex) {
                    
                    logger.error("loadConfig()", ex);
                    
                }
                
            }
            
        }
        
    }        
    
    /**
    * Run auto test cases
    */
    @Asynchronous
    @Schedule(hour = "*", minute = "45")
    public void runAutoTestCases() {
        
        try {

            logger.info("runAutoTestCases()");
            StringBuilder sb;
            
            lookupBeans();
            
            // get environment
            Properties properties = loadConfig();
            String sEnvironment = properties.getProperty("autoEnvironment");            
            
            // run test cases per group
            List<TestCaseGroup> lstTestCaseGroups = testCaseBean.getAllTestCaseGroups();       
            
            for (TestCaseGroup testCaseGroup : lstTestCaseGroups) {
                
                List<TestCase> lstTestCases = testCaseBean.getAutoTestCasesInGroup(testCaseGroup.getID());
                
                sb = new StringBuilder();
                sb.append("Run ").append(lstTestCases.size()).append(" auto test cases in group").append(testCaseGroup.getTitle());
                logger.info(sb.toString());
                
                // run test cases
                for (TestCase testCase : lstTestCases)
                    testCaseBean.runTestCase(testCase.getID(), sEnvironment);
                
                testCaseBean.storeGroupResult(testCaseGroup.getID(), sEnvironment);
                
            }
            
            // mail report
            testCaseBean.sendReport();

        }
        catch (Exception ex) {
            
            logger.error("runAutoTestCases()", ex);
            
        }
        
    }            
    
    /**
    * Execute maintenance
    */
    @Asynchronous
    @Schedule(hour = "23", minute = "15")
    public void execMaintenance() {
        
        try {

            logger.info("execMaintenance()");
            
            lookupBeans();
            
            // delete old test runs
            testCaseBean.deleteOldTestRunLogs();
            testCaseBean.deleteOldTestRuns();

        }
        catch (Exception ex) {
            
            logger.error("execMaintenance()", ex);
            
        }
        
    }  
    
}
