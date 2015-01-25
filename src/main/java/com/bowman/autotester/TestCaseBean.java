package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import java.util.Date;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.Schedule;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javax.persistence.NoResultException;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.Transport;

/**
* Stateless bean, manages TEST database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-11-01 
*/

@Stateless
public class TestCaseBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(TestCaseBean.class.getName()); 
    
    /**
    * Status
    */    
    public static enum Status {
        
        /**
         * NOTRUN
         */
        NOTRUN,
        
        /**
         * PASSED
         */
        PASSED,
        
        /**
         * FAILED
         */
        FAILED;
        
    }
    
    /**
    * Result
    */    
    public static enum Result {
        
        /**
         * INFO
         */
        INFO,
        
        /**
         * OK
         */
        OK,
        
        /**
         * WARN
         */
        WARN,
        
        /**
         * ERR
         */
        ERR;
    }
    
    @PersistenceContext(unitName = "AutoTester_TEST_PU")
    private EntityManager em;
    
    /**********************************
              methods
    **********************************/    
    
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
    * Read all test case groups from DB
    * @return List of TestCaseGroup 
    */
    public List<TestCaseGroup> getAllTestCaseGroups() {
        
        try { 
            
            logger.debug("getAllTestCaseGroups()");
            TypedQuery<TestCaseGroup> query = em.createNamedQuery("getAllTestCaseGroups", TestCaseGroup.class);
            return query.getResultList();
            
        }
        catch (Exception ex) {
            
            logger.error("getAllTestCaseGroups()", ex);
            return null;
            
        }
        
    }
    
    /**
    * Read requested test case group with ID from DB
    * @param iID group PK
    * @return TestCaseGroup 
    */
    public TestCaseGroup getTestCaseGroup(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCaseGroup() - params iID:").append(iID);
            logger.debug(sb);
            return em.find(TestCaseGroup.class, iID);
            
        }
        catch(Exception ex) {
            
            logger.error("getTestCaseGroup()", ex);
            return null;
            
        }
        
    }
    
    /**
    * Read test case group with title from DB
    * @param sTitle title
    * @return TestCaseGroup 
    */    
    public TestCaseGroup getTestCaseGroup(String sTitle) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCaseGroup() - params sTitle:").append(sTitle);
            logger.debug(sb);
            
            TypedQuery<TestCaseGroup> query = em.createNamedQuery("getTestCaseGroup", TestCaseGroup.class);
            query.setParameter("title", sTitle);
            return query.getSingleResult();
            
        }
        catch (NoResultException ex) {
            
            return null;
            
        }
        catch(Exception ex) {
            
            logger.error("getTestCaseGroup()", ex);
            return null;
            
        }        
        
    }    
    
    /**
    * Create requested test case group in DB and directory
    * @param sTitle title
    * @param sDescription description
    * @param sDirectory directory
    * @return TestCaseGroup  
    */
    public TestCaseGroup createTestCaseGroup(String sTitle, String sDescription, String sDirectory) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestCaseGroup() - params sTitle:").append(sTitle).append(", sDescription:").append(sDescription);
            sb.append(", sDirectory:").append(sDirectory);
            logger.debug(sb);
            
            // duplicate check
            TestCaseGroup testCaseGroup = getTestCaseGroup(sTitle);
            
            if (testCaseGroup == null) {
                
                // create directory
                File dir = new File(sDirectory);
                
                if (dir.mkdir()) {
                    
                    sb = new StringBuilder();
                    sb.append("Directory " ).append(sDirectory).append(" created");
                    logger.info(sb);
                    
                    // set permissions to non-owner
                    dir.setExecutable(true, false);
                    dir.setReadable(true, false);
                    dir.setWritable(true, false);
                    
                    // create group
                    testCaseGroup = new TestCaseGroup(sTitle, sDescription, sDirectory, new Date(), null, null);
                    em.persist(testCaseGroup);
                    
                    sb = new StringBuilder();
                    sb.append("Test case group ").append(sTitle).append(" created");
                    logger.info(sb);   
                    
                    return testCaseGroup;
                    
                }
                else {
                    
                    sb = new StringBuilder();
                    sb.append("Failed to create directory ").append(sDirectory).append(" for test case group ").append(sTitle);
                    logger.error(sb);
                    return null;
                    
                }                                             
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestCaseGroup() - test case group with title ").append(sTitle).append(" already exists");
                logger.error(sb);
                return null;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("createTestCaseGroup()", ex);
            return null;
            
        }
        
    }
    
    /**
    * Update requested test case group in DB
    * @param iID group PK
    * @param sTitle title
    * @param sDescription description
    * @param sDirectory directory
    * @return boolean 
    */
    public boolean updateTestCaseGroup (int iID, String sTitle, String sDescription, String sDirectory) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("updateTestCaseGroup() - params iID:").append(iID).append("sTitle:").append(sTitle);
            sb.append(", sDescription:").append(sDescription).append(", sDirectory:").append(sTitle);
            logger.debug(sb);
            
            TestCaseGroup testCaseGroup = getTestCaseGroup(iID);
            
            if (testCaseGroup != null) {
                
                // rename directory if needed
                if (!testCaseGroup.getDirectory().equals(sDirectory)) {
                    
                    String sPath = testCaseGroup.getDirectory();
                    File dir = new File(sPath);
                
                    if (dir.renameTo(new File(sDirectory))) {
                        
                        sb = new StringBuilder();
                        sb.append("Directory ").append(sPath).append(" renamed to ").append(sDirectory);
                        logger.info(sb);  
                        
                    }
                    else {
                    
                        sb = new StringBuilder();
                        sb.append("Failed to rename directory ").append(sPath).append(" to ").append(sDirectory);
                        logger.error(sb);
                        return false;
                    
                    }
                }
                
                // update group
                testCaseGroup.setTitle(sTitle);
                testCaseGroup.setDescription(sDescription);
                testCaseGroup.setDirectory(sDirectory);
                testCaseGroup.setModifyDate(new Date());
                em.merge(testCaseGroup);
                
                sb = new StringBuilder();
                sb.append("Test case group ").append(iID).append(" updated");
                logger.info(sb);
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("updateTestCaseGroup() - test case group ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("updateTestCaseGroup()", ex);
            return false;
            
        }
        
    }
    
    /**
    * Delete requested test case group in DB 
    * @param iID group PK
    * @return boolean 
    */
    public boolean deleteTestCaseGroup(int iID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestCaseGroup() - params iID:").append(iID);
            logger.debug(sb);
            
            TestCaseGroup testCaseGroup = getTestCaseGroup(iID);
            
            if (testCaseGroup != null) {                
                
                // get test cases from group
                List<TestCase> lstTestCases = getTestCasesInGroup(iID);
                
                // delete test cases
                for (TestCase testCase : lstTestCases)
                    deleteTestCase(testCase.getID());
                
                // delete directory
                String sPath = testCaseGroup.getDirectory();
                File dir = new File(sPath);
                
                if (dir.delete()) {
                    
                    sb = new StringBuilder();
                    sb.append("Directory ").append(sPath).append(" deleted");
                    logger.info(sb);
                    
                }
                else {
                    
                    sb = new StringBuilder();
                    sb.append("Failed to delete directory ").append(sPath);
                    logger.error(sb);
                    return false;
                    
                }
                
                // delete group
                em.remove(testCaseGroup);
                
                sb = new StringBuilder();
                sb.append("Test case group ").append(iID).append(" deleted");
                logger.info(sb);
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("deleteTestCaseGroup() - test case group ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("deleteTestCaseGroup()", ex);
            return false;
            
        }
        
    }
    
    /**
    * Read all test cases from DB
    * @return List of TestCase 
    */
    public List<TestCase> getAllTestCases() {
        
        try {
            
            logger.debug("getAllTestCases()");
            TypedQuery<TestCase> query = em.createNamedQuery("getAllTestCases", TestCase.class);
            return query.getResultList();     
        
        }
        catch (Exception ex) {
            
            logger.error("getAllTestCases()", ex);
            return null;
            
        }
        
    }
    
    /**
    * Read auto test cases from DB
    * @return List of TestCase 
    */
    public List<TestCase> getAutoTestCases() {
        
        try {
            
            logger.debug("getAutoTestCases()");
            TypedQuery<TestCase> query = em.createNamedQuery("getAutoTestCases", TestCase.class);
            return query.getResultList();     
        
        }
        catch (Exception ex) {
            
            logger.error("getAutoTestCases()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * Read requested test case with ID from DB
    * @param iID test case PK
    * @return TestCase 
    */
    public TestCase getTestCase(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCase() - params iID:").append(iID);
            logger.debug(sb);
            
            return em.find(TestCase.class, iID);
            
        }
        catch(Exception ex) {
            
            logger.error("getTestCase()", ex);
            return null;
            
        }
        
    }   
    
    /**
    * Read test case with title from DB
    * @param sTitle title
    * @return TestCase 
    */    
    public TestCase getTestCase(String sTitle) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCase() - params sTitle:").append(sTitle);
            logger.debug(sb);
            
            TypedQuery<TestCase> query = em.createNamedQuery("getTestCase", TestCase.class);
            query.setParameter("title", sTitle);
            
            return query.getSingleResult();
            
        }
        catch (NoResultException ex) {
            
            return null;
            
        }
        catch(Exception ex) {
            
            logger.error("getTestCase()", ex);
            return null;
            
        }        
        
    }    
    
    /**
    * Read test cases in group with ID from DB
    * @param iGroupID group FK
    * @return List of TestCase 
    */
    public List<TestCase> getTestCasesInGroup(int iGroupID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCasesInGroup() - params iGroupID:").append(iGroupID);
            logger.debug(sb);
            
            TypedQuery<TestCase> query = em.createNamedQuery("getTestCasesInGroup", TestCase.class);
            query.setParameter("group_id", iGroupID);
            
            return query.getResultList();            
                        
        }
        catch(Exception ex) {
            
            logger.error("getTestCasesInGroup()", ex);
            return null;
            
        }
        
    }        
    
    /**
    * Create requested test case in DB
    * @param sTitle title
    * @param sDescription description
    * @param sFilename filename
    * @param iGroupID group FK
    * @param bAuto auto
    * @return TestCase 
    */
    public TestCase createTestCase(String sTitle, String sDescription, String sFilename, int iGroupID, boolean bAuto) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestCase() - params sTitle:").append(sTitle).append(", sDescription:").append(sDescription);
            sb.append(", sFilename:").append(sFilename).append(", iGroupID:").append(iGroupID).append(", bAuto:").append(bAuto);
            logger.debug(sb);
            
            // duplicate check
            TestCase testCase = getTestCase(sTitle);
            
            if (testCase == null) {

                // get group
                TestCaseGroup testCaseGroup = getTestCaseGroup(iGroupID);
            
                if (testCaseGroup != null) {
                
                    // create file
                    String sPath = testCaseGroup.getDirectory() + "/" + sFilename;
                    File file = new File(sPath);
                    
                    if (file.createNewFile()) {
                    
                        sb = new StringBuilder();
                        sb.append("File ").append(sPath).append(" created");
                        logger.info(sb);
                        
                        // set permissions to non-owner
                        file.setExecutable(true, false);
                        file.setReadable(true, false);
                        file.setWritable(true, false);
                        
                        // create test case
                        testCase = new TestCase(sTitle, sDescription, sFilename, testCaseGroup, bAuto, Status.NOTRUN.toString(), 
                                                new Date(), null, null);
                        em.persist(testCase);
                
                        sb = new StringBuilder();
                        sb.append("Test case ").append(sTitle).append(" created");
                        logger.info(sb);
                        
                        return testCase;
                    
                    }
                    else {
                        
                        sb = new StringBuilder();
                        sb.append("Failed to create file ").append(sPath);
                        logger.error(sb);
                        return null;
                        
                    }
                }
                else {
                
                    sb = new StringBuilder();
                    sb.append("createTestCase() - test case group ").append(iGroupID).append(" not found");
                    logger.error(sb);
                    return null;
                
                }
            
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestCase() - test case ").append(sTitle).append(" already exists");
                logger.error(sb);
                return null;                
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("createTestCase()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Update requested test case in DB
    * @param iID test case PK
    * @param sTitle title
    * @param sDescription description
    * @param sFilename filename
    * @param iGroupID group FK
    * @param bAuto auto
    * @return boolean 
    */
    public boolean updateTestCase (int iID, String sTitle, String sDescription, String sFilename, int iGroupID, boolean bAuto) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("updateTestCase() - params iID:").append(iID).append(", sTitle:").append(sTitle).append(", sDescrition:").append(sDescription);
            sb.append(", sFilename:").append(sFilename).append(", iGroupID:").append(iGroupID).append(", bAuto:").append(bAuto);
            logger.debug(sb);
            
            TestCase testCase = getTestCase(iID);
            
            if (testCase != null) {
                
                // get group
                TestCaseGroup testCaseGroup = getTestCaseGroup(iGroupID);
                
                if (testCaseGroup != null) {
                    
                    // rename file if needed
                    if (!testCase.getFilename().equals(sFilename)) {
                        
                        String sOld = testCaseGroup.getDirectory() + "/" + testCase.getFilename();
                        File fOld = new File(sOld);
                        String sNew = testCaseGroup.getDirectory() + "/" + sFilename;
                        File fNew = new File(sNew);
                        
                        if (fOld.renameTo(fNew)) {
                            
                            sb = new StringBuilder();
                            sb.append("File ").append(sOld).append(" renamed to ").append(sNew);
                            logger.info(sb);
                            
                        }
                        else {
                            
                            sb = new StringBuilder();
                            sb.append("Failed to rename file ").append(sOld).append(" to ").append(sNew);
                            logger.error(sb);
                            return false;
                            
                        }
                        
                    }
                    
                    // update test case
                    testCase.setTitle(sTitle);
                    testCase.setDescription(sDescription);
                    testCase.setFilename(sFilename);
                    testCase.setTestCaseGroup(testCaseGroup);
                    testCase.setAuto(bAuto);
                    testCase.setModifyDate(new Date());
                    em.merge(testCase);
                    
                    sb = new StringBuilder();
                    sb.append("Test case ").append(iID).append(" updated");
                    logger.info(sb);
                    
                    return true;
                    
                }
                else {
                    
                    sb.append("updateTestCase() - test case group ").append(iGroupID).append(" not found");
                    logger.error(sb);
                    return false;
                    
                }
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("updateTestCase() - test case ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("updateTestCase()", ex);
            return false;
            
        }
        
    }         
    
    /**
    * Delete requested test case in DB
    * @param iID test case PK
    * @return boolean 
    */
    public boolean deleteTestCase(int iID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append(" not found").append(iID);
            logger.debug(sb);
            
            TestCase testCase = getTestCase(iID);
            
            if (testCase != null) {
                
                // delete file
                String sPath = testCase.getTestCaseGroup().getDirectory() + "/" + testCase.getFilename();
                File file = new File(sPath);
                
                if (file.delete()) {
                    
                    sb = new StringBuilder();
                    sb.append("File ").append(sPath).append(" deleted");
                    logger.info(sb);
                    
                }
                else {
                    
                    sb = new StringBuilder();
                    sb.append("Failed to delete file ").append(sPath);
                    logger.error(sb);
                    return false;
                    
                }
                
                // get test case runs
                List<TestRun> lstTestRuns = getTestCaseRuns(iID);
                
                // delete test cases
                for (TestRun testRun : lstTestRuns)
                    deleteTestRun(testRun.getID());                
                
                // delete test case
                em.remove(testCase);
                
                sb = new StringBuilder();
                sb.append("Test case ").append(iID).append(" deleted");
                logger.info(sb);
                
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("deleteTestCase() - test case ").append(iID).append(" not found");
                logger.error(sb);
                
                return false;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("deleteTestCase()", ex);
            return false;
            
        }
        
    } 
    
    /**
    * Get test runs for test case with ID from DB
    * @param iTestCaseID group FK
    * @return List of TestRun 
    */
    public List<TestRun> getTestCaseRuns(int iTestCaseID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestCaseRuns() - params iTestCaseID:").append(iTestCaseID);
            logger.debug(sb);
            
            TypedQuery<TestRun> query = em.createNamedQuery("getTestCaseRuns", TestRun.class);
            query.setParameter("test_case_id", iTestCaseID);
            
            return query.getResultList();            
                        
        }
        catch(Exception ex) {
            
            logger.error("getTestCaseRuns()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Read requested test run with ID from DB
    * @param iID test run PK
    * @return TestRun 
    */
    public TestRun getTestRun(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestRun() - params iID:").append(iID);
            logger.debug(sb);
            
            return em.find(TestRun.class, iID);
            
        }
        catch(Exception ex) {
            
            logger.error("getTestRun()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * Create requested test run in DB
    * @param iTestCaseID test case FK
    * @param sEnvironment environment
    * @return TestRun 
    */
    public TestRun createTestRun(int iTestCaseID, String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestRun() - params iTestCaseID:").append(iTestCaseID);
            sb.append(", sEnvironment:").append(sEnvironment);
            logger.debug(sb);

            // get test case
            TestCase testCase = getTestCase(iTestCaseID);
            
            if (testCase != null) {
                
                // create test run
                TestRun testRun = new TestRun(testCase, Status.NOTRUN.toString(), sEnvironment, new Date(), -1, null);
                em.persist(testRun);                
                                
                return testRun;
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestRun() - test case ").append(iTestCaseID).append(" not found");
                logger.error(sb);
                return null;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("createTestRun()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Update requested test run in DB
    * @param iID test run PK
    * @param sStatus status
    * @param lDuration duration
    * @return boolean 
    */
    public boolean updateTestRun(int iID, String sStatus, long lDuration) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("updateTestRun() - params iID:").append(iID).append(", sStatus:").append(sStatus);
            sb.append(", lDuration:").append(lDuration);
            logger.debug(sb);
            
            TestRun testRun = getTestRun(iID);
            
            if (testRun != null) {              
                
                // update test run
                testRun.setStatus(sStatus);
                testRun.setDuration(lDuration);
                em.merge(testRun);
                
                sb = new StringBuilder();
                sb.append("Test run ").append(iID).append(" updated");
                logger.info(sb);
                
                // update test case status
                TestCase testCase = testRun.getTestCase();
                testCase.setStatus(sStatus);
                em.merge(testCase);
                
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("updateTestRun() - test run ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("deleteTestRun()", ex);
            return false;
            
        }
        
    }     
    
    /**
    * Delete requested test run in DB
    * @param iID test run PK
    * @return boolean 
    */
    public boolean deleteTestRun(int iID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestRun() - params iID:").append(iID);
            logger.debug(sb);
            
            TestRun testRun = getTestRun(iID);
            
            if (testRun != null) {              
                
                // get test run logs
                List<TestRunLog> lstTestRunLogs = getTestRunLogs(testRun.getID());
                
                // delete test run logs
                for (TestRunLog testRunLog : lstTestRunLogs)
                    deleteTestRunLog(testRunLog.getID());                 
                
                // delete test run
                em.remove(testRun);
                
                sb = new StringBuilder();
                sb.append("Test run ").append(iID).append(" deleted");
                logger.info(sb);
                
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("deleteTestRun() - test run ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("deleteTestRun()", ex);
            return false;
            
        }
        
    } 
    
    /**
    * Delete old test runs
    */
    public void deleteOldTestRuns() {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("deleteOldTestRuns()");
            logger.debug(sb);

            Query query = em.createNativeQuery("DELETE FROM TEST_RUN WHERE run_date < {fn TIMESTAMPADD(SQL_TSI_MONTH, -1, CURRENT_TIMESTAMP)} ");
            query.executeUpdate();
            
        }
        catch (Exception ex) {
            
            logger.error("deleteOldTestRuns()", ex);
            
        }
        
    }     
    
    /**
    * Get test run logs for test run with ID from DB
    * @param iTestRunID group FK
    * @return List of TestRunLog 
    */
    public List<TestRunLog> getTestRunLogs(int iTestRunID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestRunLogs() - params iTestRunID:").append(iTestRunID);
            logger.debug(sb);
            
            TypedQuery<TestRunLog> query = em.createNamedQuery("getTestRunLogs", TestRunLog.class);
            query.setParameter("test_run_id", iTestRunID);
            
            return query.getResultList();            
                        
        }
        catch(Exception ex) {
            
            logger.error("getTestRunLogs()", ex);
            return null;
            
        }
        
    }   
    
    /**
    * Read requested test run log with ID from DB
    * @param iID test run log PK
    * @return TestRunLog 
    */
    public TestRunLog getTestRunLog(int iID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestRunLog() - params iID:").append(iID);
            logger.debug(sb);
            
            return em.find(TestRunLog.class, iID);
            
        }
        catch(Exception ex) {
            
            logger.error("getTestRunLog()", ex);
            return null;
            
        }
        
    }        
    
    /**
    * Create requested test run log in DB
    * @param iTestRunID test run FK
    * @param sResult result
    * @param sLog log
    * @return boolean 
    */
    public boolean createTestRunLog(int iTestRunID, Result sResult, String sLog) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createTestRunLog() - params iTestRunID:").append(iTestRunID).append(", sLog:").append(sLog);
            logger.debug(sb);

            // get test run
            TestRun testRun = getTestRun(iTestRunID);
            
            if (testRun != null) {
                
                // create test run log
                TestRunLog testRunLog = new TestRunLog(testRun, new Date(), sResult.toString(), sLog);
                em.persist(testRunLog);                
                                
                return true;
            }
            else {
                
                sb = new StringBuilder();
                sb.append("createTestRunLog() - test run ").append(iTestRunID).append(" not found");
                logger.error(sb);
                return false;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("createTestRunLog()", ex);
            return false;
            
        }
        
    }      
    
    /**
    * Delete requested test run log in DB
    * @param iID test run log PK
    * @return boolean 
    */
    public boolean deleteTestRunLog(int iID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("deleteTestRunLog() - params iID:").append(iID);
            logger.debug(sb);
            
            TestRunLog testRunLog = getTestRunLog(iID);
            
            if (testRunLog != null) {              
                
                // delete test run log
                em.remove(testRunLog);
                
                sb = new StringBuilder();
                sb.append("Test run log ").append(iID).append(" deleted");
                logger.info(sb);
                
                return true;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("deleteTestRunLog() - test run log ").append(iID).append(" not found");
                logger.error(sb);
                return false;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("deleteTestRunLog()", ex);
            return false;
            
        }
        
    }   
    
    /**
    * Delete old test runs logs
    */
    public void deleteOldTestRunLogs() {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("deleteOldTestRunLogs()");
            logger.debug(sb);

            Query query = em.createNativeQuery("DELETE FROM TEST_RUN_LOG WHERE log_date < {fn TIMESTAMPADD(SQL_TSI_MONTH, -1, CURRENT_TIMESTAMP)} ");
            query.executeUpdate();
            
        }
        catch (Exception ex) {
            
            logger.error("deleteOldTestRunLogs()", ex);
            
        }
        
    }  
    
    /**
    * Get test data for given test case and environment
    * @param iTestRunID test run ID
    * @param sEnvironment environment
    * @return List of TestData 
    */
    public List<TestData> getTestData(int iTestRunID, String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getTestData() - params iTestRunID:").append(iTestRunID).append(", sEnvironment:").append(sEnvironment);
            logger.debug(sb);
            
            TestRun testRun = em.find(TestRun.class, iTestRunID);
            TypedQuery<TestData> query = em.createNamedQuery("getTestData", TestData.class);
            query.setParameter("test_case_id", testRun.getTestCase().getID());
            query.setParameter("environment", sEnvironment);
            
            return query.getResultList();
            
        }
        catch(Exception ex) {
            
            logger.error("getTestData()", ex);
            return null;
            
        }
        
    }       
    
    /**
    * Run auto test cases
    */
    @Schedule(hour = "17", minute = "15")
    public void runAutoTestCases() {
        
        try {

            logger.info("runAutoTestCases()");
            
            // get auto test cases
            List<TestCase> lstTestCases = getAutoTestCases();
            
            StringBuilder sb = new StringBuilder();
            sb.append("Run ").append(lstTestCases.size()).append(" test cases");
            logger.info(sb);
            
            // get environment
            Properties properties = loadConfig();
            String sEnvironment = properties.getProperty("autoEnvironment");
            
            // run test cases
            for (TestCase testCase : lstTestCases)
                runTestCase(testCase.getID(), sEnvironment);
            
            // mail report
            sendReport();

        }
        catch (Exception ex) {
            
            logger.error("runAutoTestCases()", ex);
            
        }
        
    }        
    
    /**
    * Run requested test case
    * @param iID test case PK
    * @param sEnvironment environment
    * @return String 
    */
    public String runTestCase(int iID, String sEnvironment) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("runTestCase() - params iID:").append(iID).append(", sEnvironment:").append(sEnvironment);
            logger.debug(sb);
            
            Date dStart = new Date();
            TestCase testCase = getTestCase(iID);
            
            if (testCase != null) {              
                
                sb = new StringBuilder();
                sb.append("Run test case ").append(testCase.getTitle()).append(" on environment ").append(sEnvironment);
                logger.info(sb);                
                
                // create initial test run
                TestRun testRun = createTestRun(iID, sEnvironment);                
                
                // run test script
                String sStatus = runTestScript(testCase.getTestCaseGroup().getDirectory(), testCase.getFilename(),
                                               testRun.getID(), sEnvironment);                 
                
                // update test run
                Date dFinish = new Date();
                long lDuration = dFinish.getTime() - dStart.getTime();
                updateTestRun(testRun.getID(), sStatus, lDuration);
                
                if (testRun != null) {
                    
                    sb = new StringBuilder();
                    sb.append("Test case ").append(testCase.getTitle()).append(" run with status:").append(sStatus);
                    logger.info(sb);
                    
                    return sStatus;
                    
                }
                else {
                    
                   sb = new StringBuilder();
                   sb.append("runTestCase() - failed to create test run for test case ").append(iID);
                   logger.error(sb);
                   
                   return Status.NOTRUN.toString(); 
                   
                }
                
            }
            else {
                
                logger.error("runTestCase() - test case " + iID + " not found");
                return Status.NOTRUN.toString();
                
            }
        }
        catch (Exception ex) {
            
            logger.error("runTestCase()", ex);
            return Status.NOTRUN.toString();
            
        }
        
    }    
    
    /**
    * Run test script
    * @param sDirectory script directory
    * @param sFilename script filename
    * @param iTestRunID test run ID
    * @param sEnvironment environment
    * @return String 
    */
    public String runTestScript(String sDirectory, String sFilename, int iTestRunID, String sEnvironment) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("runTestScript() - params sDirectory:").append(sDirectory).append(", sFilename:").append(sFilename);
            sb.append(", iTestRunID:").append(iTestRunID).append(", sEnvironment:").append(sEnvironment);
            logger.debug(sb);
                      
            GroovyScriptEngine gse = new GroovyScriptEngine(sDirectory);            
            Binding binding = new Binding();            
            
            // run script
            binding.setVariable("logID", iTestRunID);
            binding.setVariable("env", sEnvironment);
            
            gse.run(sFilename, binding);
            int iResult = (Integer) binding.getVariable("result");
            
            sb = new StringBuilder();
            sb.append("Test script ").append(sFilename).append(" run with result:").append(iResult);
            logger.info(sb);
            
            // status translation
            if (iResult == 1)
                return Status.PASSED.toString();
            else if (iResult == 0)
                return Status.FAILED.toString();
            
            return Status.FAILED.toString();
            
        }
        catch (Exception ex) {
            
            logger.error("runTestScript()", ex);
            return Status.FAILED.toString();
            
        }
        
    }     
    
    /**
    * Send email report
    */
    public void sendReport() {
        
        try {

            logger.debug("sendReport()");
            
            // get test cases results
            List<TestCase> lstTestCases = getAutoTestCases(); 
            int iTotal = lstTestCases.size();
            
            if (iTotal == 0)
                return;
            
            // create html content            
            int iPassed = 0;
            int iFailed = 0;
            int iNotRun = 0;
            String sStatus;
            
            // set test case report
            StringBuilder sbReport = new StringBuilder();
            StringBuilder sbTestCaseReport = new StringBuilder();
            
            sbReport.append("<HTML><BODY>This is automatic report for executed test cases");
            sbTestCaseReport.append("<BR><H2>Test case report</H2>");
            sbTestCaseReport.append("<TABLE cellspacing=\"1\" cellpadding=\"1\" border=\"1\">");
            sbTestCaseReport.append("<THEAD><TR><TH>Group</TH><TH>Title</TH><TH>Status</TH></TR></THEAD><TBODY>");
            
            for (TestCase testCase : lstTestCases) {                                                  
                 
                 // set status counters
                 sStatus = testCase.getStatus();
                 
                 if (sStatus.equals("PASSED"))
                     iPassed++;
                 else if (sStatus.equals("FAILED"))
                     iFailed++;
                 else if (sStatus.equals("NOTRUN"))
                     iNotRun++;
                 
                 sbTestCaseReport.append("<TR><TD>").append(testCase.getTestCaseGroup().getTitle()).append("</TD><TD>");
                 sbTestCaseReport.append(testCase.getTitle()).append("</TD><TD>").append(sStatus).append("</TD></TR>");                 
                
            }
            
            sbTestCaseReport.append("</TBODY>");
            
            // set status report
            StringBuilder sbStatusReport = new StringBuilder();
            sbStatusReport.append("<H2>Status report</H2>");
            sbStatusReport.append("<TABLE cellspacing=\"1\" cellpadding=\"1\" border=\"1\">");
            sbStatusReport.append("<THEAD><TR><TH>Type</TH><TH>Count</TH><TH>Percentage</TH></TR></THEAD><TBODY>");
            sbStatusReport.append("<TR><TD>Total</TD><TD>").append(iTotal).append("</TD><TD></TD></TR>");
            sbStatusReport.append("<TR><TD>Passed</TD><TD>").append(iPassed).append("</TD><TD>").append(100 * iPassed / iTotal).append("%</TD></TR>");
            sbStatusReport.append("<TR><TD>Failed</TD><TD>").append(iFailed).append("</TD><TD>").append(100 * iFailed / iTotal).append("%</TD></TR>");
            sbStatusReport.append("<TR><TD>Not run</TD><TD>").append(iNotRun).append("</TD><TD>").append(100 * iNotRun / iTotal).append("%</TD></TR>");
            sbStatusReport.append("<TBODY></TABLE>");
            
            sbReport.append(sbStatusReport).append(sbTestCaseReport).append("</BODY></HTML>");
            
            // send email
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", "smtp.rdm.cz");
            Session session = Session.getDefaultInstance(properties);
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("autotester@t-mobile.cz"));
            message.addRecipient(RecipientType.TO, new InternetAddress("petr.rasek@t-mobile.cz"));
            message.setSubject("AUTOTESTER report");
            message.setContent(sbReport.toString(), "text/html");
            
            Transport.send(message);             
            
        }
        catch (Exception ex) {
            
            logger.error("sendReport()", ex);
            
        }
        
    }  
    
    /**
    * Execute maintenance
    */
    @Schedule(hour = "23", minute = "15")
    public void execMaintenance() {
        
        try {

            logger.info("execMaintenance()");
            
            // delete old test runs
            deleteOldTestRunLogs();
            deleteOldTestRuns();

        }
        catch (Exception ex) {
            
            logger.error("execMaintenance()", ex);
            
        }
        
    }            
    
}
