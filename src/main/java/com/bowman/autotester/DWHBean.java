package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Types;
import oracle.jdbc.driver.OracleTypes;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateful;

/**
* Stateful bean, manages DWH database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

@Stateful
public class DWHBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(DWHBean.class.getName()); 
    
    // properties
    private Properties properties;
    
    // connection
    private String sUrl;
    private String sUser;
    private String sPass;   
    private Connection con;           
    
    /**********************************
              constructors
    **********************************/       
    
    public DWHBean() {
        
        sUrl = null;
        sUser = null;
        sPass = null;
        con = null;
        properties = null;
        
    }
    
    /**********************************
              methods
    **********************************/           
    
    /**
    * Load configuration
    */    
    public void loadConfig() {
        
        InputStream isConfig = null;
        properties = new Properties();
        
        try {
            
            // load configuration as properties
            logger.debug("loadConfig()");
            String sConfig = "config.properties";
            isConfig = TestCaseBean.class.getClassLoader().getResourceAsStream(sConfig);
            
            if (isConfig == null) {
                
                StringBuilder sb = new StringBuilder();
                sb.append("Config file " ).append(sUrl).append(" not found");
                logger.error(sb);
                return;
                
            }
            
            properties.load(isConfig);            
            
        }
        catch (Exception ex) {
            
            logger.error("loadConfig()", ex);
            
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
    * Connect to DWH database
    * @param sEnvironment environment
    * @return boolean 
    */
    public boolean connect(String sEnvironment) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("connect() - params sEnvironment:").append(sEnvironment);
            logger.debug(sb);
            
            // set connection string
            loadConfig();            
            
            // test 1
            if (sEnvironment.equals(properties.getProperty("test1"))) {
                
                sUrl = properties.getProperty("test1DWHUrl");
                sUser = properties.getProperty("test1DWHUser");
                sPass = properties.getProperty("test1DWHPass");
                
            }
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) {
                
                sUrl = properties.getProperty("test2DWHUrl");
                sUser = properties.getProperty("test2DWHUser");
                sPass = properties.getProperty("test2DWHPass");
                
            }
            
            // connect
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(sUrl, sUser, sPass);
            
            return true;

        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            return false;
            
        }
        
    }    
    
    /**
    * Disconnect from DWH database
    * @return boolean 
    */
    public boolean disconnect() {
        
        try {

            logger.debug("disconnect()");
            con.close();
            
            return true;

        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            return false;
            
        }
        
    }
    
    /**
    * Get customer hierarchy
    * @param sMSISDN MSISDN
    * @return CustHierarchy
    */
    public CustHierarchy getCustHierarchy(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getHierarchy() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT CUST_LEVEL, INT_ID, EXT_ID FROM ");
            sbQuery.append("(SELECT 'CU' AS CUST_LEVEL, qcus_custcode AS INT_ID, qcus_id AS EXT_ID from sy_qcus.qcus_cus_personal_info WHERE qcus_id = ");
            sbQuery.append("(SELECT qcus_cu_id  FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)) ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'LE' AS CUST_LEVEL, qcus_custcode AS INT_ID, qcus_id AS EXT_ID FROM sy_qcus.qcus_cus_personal_info WHERE qcus_id = ");
            sbQuery.append("(SELECT qcus_le_id  FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)) ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'OU' AS CUST_LEVEL, qcus_custcode AS INT_ID, qcus_id AS EXT_ID FROM sy_qcus.qcus_cus_personal_info WHERE qcus_id = ");
            sbQuery.append("(SELECT qcus_parent_id  FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)) ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'EU' AS CUST_LEVEL, qcus_custcode AS INT_ID, qcus_id AS EXT_ID from sy_qcus.qcus_cus_personal_info WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1) ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'BA' AS CUST_LEVEL, qcuscac_des AS INT_ID, qcuscac_id AS EXT_ID from sy_qcus.qcus_account where qcuscac_id = ");
            sbQuery.append("(SELECT qsub_qcuscac_id FROM ");
            sbQuery.append("(SELECT qsub_qcuscac_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1) ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'SU' AS CUST_LEVEL, qsub_pcn AS INT_ID, qsub_id AS EXT_ID from sy_qsub.qsub_sub_life where qsub_id = ");
            sbQuery.append("(SELECT qsub_id FROM "); 
            sbQuery.append("(SELECT qsub_id FROM sy_qsub.QSUB_SUB_LIFE where qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1) ");
            sbQuery.append(") ORDER BY DECODE(CUST_LEVEL, 'CU',0, 'LE',1, 'OU',2, 'EU',3, 'BA',4, 'SU',5)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            
            CustHierarchy custHierarchy = new CustHierarchy();
            String sType;
            int iIntID;
            int iExtID;
            
            while (rs.next()) {
                
                sType = rs.getString("CUST_LEVEL");
                iIntID = rs.getInt("INT_ID");
                iExtID = rs.getInt("EXT_ID");
                
                // set cust levels
                if (sType.equals("CU"))
                    custHierarchy.setCU(iIntID, iExtID);
                else if (sType.equals("LE"))
                    custHierarchy.setLE(iIntID, iExtID);
                else if (sType.equals("OU"))
                    custHierarchy.setOU(iIntID, iExtID);
                else if (sType.equals("EU"))
                    custHierarchy.setEU(iIntID, iExtID);
                else if (sType.equals("BA"))
                    custHierarchy.setBA(iIntID, iExtID);
                else if (sType.equals("SU"))
                    custHierarchy.setSU(iIntID, iExtID);
                
            }
            
            rs.close();
            stmt.close();
            return custHierarchy;
            
        }
        catch (Exception ex) {
            
            logger.error("getHierarchy()", ex);
            return null;
            
        }
        
    }      
    
    /**
    * Get CU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getCU(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getCU() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qcus_cu_id FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iCU = rs.getInt("qcus_cu_id");
                rs.close();
                stmt.close();
                return iCU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getCU()", ex);
            return -1;
            
        }
        
    }         
    
    /**
    * Get LE
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getLE(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLE() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qcus_le_id FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iLE = rs.getInt("qcus_le_id");
                rs.close();
                stmt.close();
                return iLE;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLE()", ex);
            return -1;
            
        }
        
    }         
    
    /**
    * Get OU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getOU(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getOU() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qcus_parent_id FROM sy_qcus.qcus_unit WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iOU = rs.getInt("qcus_parent_id");
                rs.close();
                stmt.close();
                return iOU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOU()", ex);
            return -1;
            
        }
        
    }         
    
    /**
    * Get EU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getEU(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getEU() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iEU = rs.getInt("qsub_qcus_id");
                rs.close();
                stmt.close();
                return iEU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getEU()", ex);
            return -1;
            
        }
        
    }      
    
    /**
    * Get BA
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getBA(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getBA() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsub_qcuscac_id FROM ");
            sbQuery.append("(SELECT qsub_qcuscac_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iBA = rs.getInt("qsub_qcuscac_id");
                rs.close();
                stmt.close();
                return iBA;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBA()", ex);
            return -1;
            
        }
        
    }          
    
    /**
    * Get SU
    * @param sMSISDN MSISDN
    * @return int
    */
    public int getSU(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSU() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsub_id FROM ");
            sbQuery.append("(SELECT qsub_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY qsub_status_change_dat DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iSU = rs.getInt("qsub_id");
                rs.close();
                stmt.close();
                return iSU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSU()", ex);
            return -1;
            
        }
        
    }    
    
    /**
    * Get status
    * @param sMSISDN MSISDN
    * @return String
    */
    public String getStatus(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getStatus() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsub_qsubst_code FROM ");
            sbQuery.append("(SELECT qsub_qsubst_code FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY qsub_status_change_dat DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                String sStatus = rs.getString("qsub_qsubst_code");
                rs.close();
                stmt.close();
                return sStatus;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getStatus()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Get bill cycle
    * @param sMSISDN MSISDN
    * @return BillCycle
    */
    public BillCycle getBillCycle(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getBillCycle() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qcusbcy_code, qcusbcy_des FROM sy_qcus.qcus_billcycle WHERE qcusbcy_code = ");
            sbQuery.append("(SELECT qcus_qcusbcy_code FROM sy_qcus.qcus_cus_paystat WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qcusbcy_code"));
                String sTitle = rs.getString("qcusbcy_des");
                rs.close();
                stmt.close();
                return new BillCycle(iID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBillCycle()", ex);
            return null;
            
        }
        
    }       
    
    /**
    * Get segment
    * @param sMSISDN MSISDN
    * @return Segment
    */
    public Segment getSegment(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSegment() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qcusmks_id, qcusmks_des FROM sy_qcus.qcus_mkt_segment WHERE qcusmks_id = ");
            sbQuery.append("(SELECT qcus_qcusmks_id FROM sy_qcus.qcus_cus_personal_info WHERE qcus_id = ");
            sbQuery.append("(SELECT qsub_qcus_id FROM ");
            sbQuery.append("(SELECT qsub_qcus_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qcusmks_id"));
                String sTitle = rs.getString("qcusmks_des");
                rs.close();
                stmt.close();
                return new Segment(iID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSegment()", ex);
            return null;
            
        }
        
    }      
    
    /**
    * Get market
    * @param sMSISDN MSISDN
    * @return Market
    */
    public Market getMarket(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getMarket() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsubm_id, qsubm_des FROM sy_qsub.qsub_market WHERE qsubm_id = ");
            sbQuery.append("(SELECT qsub_qsubm_id FROM ");
            sbQuery.append("(SELECT qsub_qsubm_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qsubm_id"));
                String sTitle = rs.getString("qsubm_des");
                rs.close();
                stmt.close();
                return new Market(iID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getMarket()", ex);
            return null;
            
        }
        
    }         
    
    /**
    * Get tariff
    * @param sMSISDN MSISDN
    * @return Tariff
    */
    public Tariff getTariff(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getTariff() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qprotar_id, qprotar_des FROM sy_qpro.qpro_tariff WHERE qprotar_id = ");
            sbQuery.append("(SELECT qsub_qprotar_id FROM ");
            sbQuery.append("(SELECT qsub_qprotar_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qprotar_id"));
                String sTitle = rs.getString("qprotar_des");
                rs.close();
                stmt.close();
                return new Tariff(iID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getTariff()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Get contract duration
    * @param sMSISDN MSISDN
    * @return ContractDuration
    */
    public ContractDuration getContractDuration(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getContractDuration() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qsubcd_id, qsubcd_des FROM sy_qsub.qsub_contr_duration WHERE qsubcd_id = ");
            sbQuery.append("(SELECT qsub_qsubcd_id FROM sy_qsub.qsub_sub_prolongation WHERE qsub_id = ");
            sbQuery.append("(SELECT qsub_id FROM ");
            sbQuery.append("(SELECT qsub_id FROM sy_qsub.qsub_sub_life WHERE qsub_msisdn = '").append(sMSISDN).append("' ORDER BY modify_dat DESC) ");
            sbQuery.append("WHERE rownum = 1))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qsubcd_id"));
                String sTitle = rs.getString("qsubcd_des");
                rs.close();
                stmt.close();
                return new ContractDuration(iID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getContractDuration()", ex);
            return null;
            
        }
        
    }     
    
    /**
    * Get SU services
    * @param iExtID EXTID_SU
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return List of Service
    */
    public List<Service> getSUServices(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSUServices() - params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iInstanceID:").append(iServiceInstanceID);
            logger.debug(sb);

            // prepare call
            String sCall = "{? = call SY_QSUB.QSUB_READ_API_04_DTW2.qsub_qsub_services_list(?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // in parameters
            stmt.setString(2, null);
            stmt.setInt(3, iExtID);
            
            // all services
            if (iServiceID == -1)
                stmt.setNull(4, Types.INTEGER);
            // given service
            else
                stmt.setInt(4, iServiceID);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.registerOutParameter(6, Types.NUMERIC);
            
            // execute 
            stmt.execute();
            
            // parse result            
            if (stmt.getInt(1) == 0) {
                
                // parse refcursor
                ResultSet rs = (ResultSet) stmt.getObject(5);
                
                if (rs != null) {
                    
                    List<Service> lstServices = new ArrayList();
                    int iID;
                    String sTitle;
                    int iInstanceID;
                    String sStatus;
                    String sResource;
                    String sRawParameters;
                    HashMap<Integer, String> hParameters;
                    
                    while (rs.next()) {
                        
                        iInstanceID = rs.getInt("qcusas_inst");
                        
                        // given instance if requested
                        if (iServiceInstanceID == -1 || iServiceInstanceID == iInstanceID) {
                            
                            iID = rs.getInt("qsubaso_qprosr_id");
                            sTitle = rs.getString("qprosr_des");                        
                            sStatus = rs.getString("qsubaso_qsubst_code");
                            sResource = rs.getString("qcusas_resource_values");
                            sRawParameters = rs.getString("qsubaso_params");
                        
                            // parameters
                            if (sRawParameters != null) {
                            
                                hParameters = new HashMap<Integer, String>();
                                String[] sParameters = sRawParameters.split("#");
                                String[] sRawParameter; 
                        
                                for (String sParameter : sParameters) {
                                   
                                    sRawParameter = sParameter.split("\\|"); 
                                
                                    if (sRawParameter.length > 1)
                                        hParameters.put(Integer.valueOf(sRawParameter[0]), sRawParameter[1]);
                                    else
                                        hParameters.put(Integer.valueOf(sRawParameter[0]), null);
                            
                                }
                        
                            }
                            else
                                hParameters = null;
                                                
                            lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));
                        
                        }
                        
                    }
                    
                    rs.close();
                    stmt.close();
                    
                    return lstServices;
                }
                else {
                    
                    stmt.close();
                    sb = new StringBuilder();
                    sb.append("getSUServices() - errno:").append(rs.getInt(6));
                    logger.error(sb);
                    
                    return null;
                    
                }
                
            }
            else {
                
                stmt.close();
                return null;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUServices()", ex);
            return null;
            
        }
        
    }     
    
    /**
    * Get CU services
    * @param sLevel Level type
    * @param iExtID EXTID
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return List of Service
    */
    public List<Service> getCUServices(String sLevel, int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getCUServices() - params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", iServiceID:").append(iServiceID).append(", iInstanceID:").append(iServiceInstanceID);
            logger.debug(sb);

            // prepare call
            String sCall = "{? = call SY_QCUS.QCUS_READ_API_03_DTW2.qcus_services_list_rc(?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // in parameters
            stmt.setInt(2, iExtID);
            
            // level
            if (sLevel.equals("OU"))
                stmt.setInt(3, 1);
            else if (sLevel.equals("BA"))
                stmt.setInt(3, 2);
            
            // all services
            if (iServiceID == -1)
                stmt.setNull(4, Types.INTEGER);
            // given service
            else
                stmt.setInt(4, iServiceID);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(5, OracleTypes.CURSOR);
            stmt.registerOutParameter(6, Types.NUMERIC);
            
            // execute 
            stmt.execute();
            
            // parse result            
            if (stmt.getInt(1) == 0) {
                
                // parse refcursor
                ResultSet rs = (ResultSet) stmt.getObject(5);
                
                if (rs != null) {
                    
                    List<Service> lstServices = new ArrayList();
                    int iID;
                    String sTitle;
                    int iInstanceID;
                    String sStatus;
                    String sResource;
                    String sRawParameters;
                    HashMap<Integer, String> hParameters;
                    
                    while (rs.next()) {
                        
                        iInstanceID = rs.getInt("qcusas_inst");
                        
                        // given instance if requested
                        if (iServiceInstanceID == -1 || iServiceInstanceID == iInstanceID) {
                            
                            iID = rs.getInt("qcusas_qprosr_id");
                            sTitle = rs.getString("qprosr_des");                        
                            sStatus = rs.getString("qcusas_qcusst_code");
                            sResource = rs.getString("qcusas_resource_param");
                            sRawParameters = rs.getString("qcusas_params");
                        
                            // parameters
                            if (sRawParameters != null) {
                            
                                hParameters = new HashMap<Integer, String>();
                                String[] sParameters = sRawParameters.split("#");
                                String[] sRawParameter; 
                        
                                for (String sParameter : sParameters) {
                                   
                                    sRawParameter = sParameter.split("\\|"); 
                                
                                    if (sRawParameter.length > 1)
                                        hParameters.put(Integer.valueOf(sRawParameter[0]), sRawParameter[1]);
                                    else
                                        hParameters.put(Integer.valueOf(sRawParameter[0]), null);
                            
                                }
                        
                            }
                            else
                                hParameters = null;
                                                
                            lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));
                        
                        }
                        
                    }
                    
                    rs.close();
                    stmt.close();
                    
                    return lstServices;
                }
                else {
                    
                    stmt.close();
                    sb = new StringBuilder();
                    sb.append("getCUServices() - errno:").append(rs.getInt(6));
                    logger.error(sb);
                    
                    return null;
                    
                }
                
            }
            else {
                
                stmt.close();
                return null;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getCUServices()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Get service from MTX
    * @param iExtID EXTID_CS
    * @param iServiceID Service ID
    * @return Service
    */
    public Service getMTXService(int iExtID, int iServiceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getMTXService() - params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT service_id, extid_inst, status, resource_value, params FROM ");
            sbQuery.append("(SELECT * FROM SY_DWCSR.DWCSR_SERVICES_CHANGE_ISTG ");
            sbQuery.append("WHERE extid_cs = ").append(iExtID).append(" AND service_id = ").append(iServiceID); 
            sbQuery.append(" ORDER BY dwcsrsci_id desc) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {

                int iID = rs.getInt("service_id");
                int iInstanceID = rs.getInt("extid_inst");
                String sStatus = rs.getString("status");
                String sResource = rs.getString("resource_value");
                String sParamsRaw = rs.getString("params");
                HashMap<Integer, String> hParameters = null;
                
                // parameters
                if (sParamsRaw != null) {                                        

                    InputStream isParams = new ByteArrayInputStream(sParamsRaw.getBytes());
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(isParams);
                    NodeList lstParams = xml.getElementsByTagName("param");
                    Element elParam;
                    int iParameterID;
                    String sParameterValue;
                    
                    // param elements
                    hParameters = new HashMap<Integer, String>();
                    
                    for (int i = 0; i < lstParams.getLength(); i++) {
                    
                        elParam = (Element) lstParams.item(i);
                        iParameterID = Integer.valueOf(elParam.getElementsByTagName("paramId").item(0).getTextContent());
                        sParameterValue = elParam.getElementsByTagName("paramValue").item(0).getTextContent();
                        hParameters.put(iParameterID, sParameterValue); 
                    }
                    
                    isParams.close();                    
                    
                }
                
                Service service = new Service(iID, null, iInstanceID, sStatus, sResource, hParameters);                
                rs.close();
                stmt.close();
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getMTXService()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Get Lov tariff
    * @param iTariffID
    * @return LovTariff
    */
    public LovTariff getLovTariff(int iTariffID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovTariff() - params iTariffID:").append(iTariffID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT a.qprotar_id, a.qprotar_des, a.qprotar_cz_des, a.qprotar_qproaof_code, b.qprotar_monthly_fee, ");
            sbQuery.append("a.qprotar_active_flg, a.qprotar_qprotarg_code, a.qprotar_qprotart_code, a.qprotar_indicator, ");
            sbQuery.append("a.qprotar_profile, b.qprotar_bundle_flg, b.qprotar_bundle_discount, a.qprotar_vpn_compatible_flg, ");
            sbQuery.append("a.qprotar_non_public_flg, a.qprotar_lte_flg, a.qprotar_flat_type, a.qprotar_sqd_profile, ");
            sbQuery.append("a.qprotar_shared_tariff_comp ");
            sbQuery.append("FROM SY_QPRO.QPRO_TARIFF a, SY_QPRO.QPRO_TARIFF_PROPERTY b ");
            sbQuery.append("WHERE a.qprotar_id = ").append(iTariffID).append(" AND b.qprotar_id = ").append(iTariffID);
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qprotar_id"));
                String sTitle = rs.getString("qprotar_des");
                String sCzTitle = rs.getString("qprotar_cz_des");
                String sOffer = rs.getString("qprotar_qproaof_code");
                float fMonthlyFee = Float.valueOf(rs.getString("qprotar_monthly_fee"));
                int iActive = Integer.valueOf(rs.getString("qprotar_active_flg"));
                String sGroup = rs.getString("qprotar_qprotarg_code"); 
                String sType = rs.getString("qprotar_qprotart_code"); 
                String sReportingType = rs.getString("qprotar_indicator"); 
                String sProfile = rs.getString("qprotar_profile");
                String sBundleFlg = rs.getString("qprotar_bundle_flg"); 
                Float fBundleDiscount = (rs.getString("qprotar_bundle_discount") == null ? -1 : Float.valueOf(rs.getString("qprotar_bundle_discount"))); 
                String sVPNCompatibleFlg = rs.getString("qprotar_vpn_compatible_flg");
                String sNonpublicFlg = rs.getString("qprotar_non_public_flg");
                String sLTEFlg = rs.getString("qprotar_lte_flg");
                String sFlatType = rs.getString("qprotar_flat_type");
                int iSQDProfile = (rs.getString("qprotar_sqd_profile") == null ? -1 : Integer.valueOf(rs.getString("qprotar_sqd_profile")));
                String sSharedTariffComp = rs.getString("qprotar_shared_tariff_comp");
                rs.close();
                stmt.close();
                return new LovTariff(iID, sTitle, sCzTitle, sOffer, fMonthlyFee, iActive, sGroup, sType,
                                     sReportingType, sProfile, sBundleFlg, fBundleDiscount, sVPNCompatibleFlg,
                                     sNonpublicFlg, sLTEFlg, sFlatType, iSQDProfile, sSharedTariffComp);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovTariff()", ex);
            return null;
            
        }
        
    }      
    
    /**
    * Get Lov nonpublic offer
    * @param iNonpublicOfferID
    * @return LovNonpublicOffer
    */
    public LovNonpublicOffer getLovNonpublicOffer(int iNonpublicOfferID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovNonpublicOffer() - params iNonpublicOfferID:").append(iNonpublicOfferID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qpronpo_id, qpronpo_des, qpronpo_qprosr_id ");
            sbQuery.append("FROM SY_QPRO.QPRO_NON_PUBLIC_OFFER ");
            sbQuery.append("WHERE qpronpo_id = ").append(iNonpublicOfferID);
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qpronpo_id"));
                String sTitle = rs.getString("qpronpo_des");
                int iServiceID = Integer.valueOf(rs.getString("qpronpo_qprosr_id"));
                rs.close();
                stmt.close();
                return new LovNonpublicOffer(iID, sTitle, iServiceID);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovNonpublicOffer()", ex);
            return null;
            
        }
        
    }
    
    /**
    * Get Lov tariff promo
    * @param iTariffPromoID
    * @return LovTariffPromo
    */
    public LovTariffPromo getLovTariffPromo(int iTariffPromoID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovTariffPromo() - params iTariffPromoID:").append(iTariffPromoID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qprotarp_id, qprotarp_des, qprotarp_long_des, qprotarp_reason_of_use, ");
            sbQuery.append("nvl(qprotarp_extra_minutes,-1) as qprotarp_extra_minutes, nvl(qprotarp_extra_messages,-1) as qprotarp_extra_messages, ");
            sbQuery.append("nvl(qprotarp_fup_limit,-1) as qprotarp_fup_limit, nvl(qprotarp_mf_discount,-1) as qprotarp_mf_discount, ");
            sbQuery.append("nvl(qprotarp_qprotar_id,-1) as qprotarp_qprotar_id, nvl(qprotarp_extra_credit_pcnt,-1) as qprotarp_extra_credit_pcnt, ");
            sbQuery.append("nvl(qprotarp_extra_minutes_tmcz,-1) as qprotarp_extra_minutes_tmcz, nvl(qprotarp_mbd_mf_discount,-1) as qprotarp_mbd_mf_discount ");
            sbQuery.append("FROM SY_QPRO.QPRO_TARIFF_PROMO ");
            sbQuery.append("WHERE qprotarp_id = ").append(iTariffPromoID);
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qprotarp_id"));
                String sTitle = rs.getString("qprotarp_des");
                String sDescription = rs.getString("qprotarp_long_des");
                String sReasonOfUse = rs.getString("qprotarp_reason_of_use");
                int iExtraMinutes = Integer.valueOf(rs.getString("qprotarp_extra_minutes"));
                int iExtraMessages = Integer.valueOf(rs.getString("qprotarp_extra_messages"));
                int iFupLimit = Integer.valueOf(rs.getString("qprotarp_fup_limit"));
                float fMfDiscount = Float.valueOf(rs.getString("qprotarp_mf_discount"));
                int iTariff = Integer.valueOf(rs.getString("qprotarp_qprotar_id"));
                int iExtraCreditPcnt = Integer.valueOf(rs.getString("qprotarp_extra_credit_pcnt"));
                int iExtraMinutesTmcz = Integer.valueOf(rs.getString("qprotarp_extra_minutes_tmcz"));
                float fMbdMfDiscount = Float.valueOf(rs.getString("qprotarp_mbd_mf_discount"));
                rs.close();
                stmt.close();
                return new LovTariffPromo(iID, sTitle, sDescription, sReasonOfUse, iExtraMinutes, iExtraMessages,
                                          iFupLimit, fMfDiscount, iTariff, iExtraCreditPcnt, iExtraMinutesTmcz,
                                          fMbdMfDiscount);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovTariffPromo()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * Get Lov discount property
    * @param sDiscountPropertyID
    * @return LovDiscountProperty
    */
    public LovDiscountProperty getLovDiscountProperty(String sDiscountPropertyID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovDiscountProperty() - params sDiscountPropertyID:").append(sDiscountPropertyID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qprodp_code, qprodp_des ");
            sbQuery.append("FROM SY_QPRO.QPRO_DISCOUNT_PROPERTY ");
            sbQuery.append("WHERE qprodp_code = '").append(sDiscountPropertyID).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                String sID = rs.getString("qprodp_code");
                String sTitle = rs.getString("qprodp_des");
                rs.close();
                stmt.close();
                return new LovDiscountProperty(sID, sTitle);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovDiscountProperty()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * Get Lov retention offer
    * @param iRetentionOfferID
    * @return LovRetentionOffer
    */
    public LovRetentionOffer getLovRetentionOffer(int iRetentionOfferID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovRetentionOffer() - params iRetentionOfferID:").append(iRetentionOfferID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT qprorof_id, qprorof_des, qprorof_qprorto_code, qprorof_qprortg_code ");
            sbQuery.append("FROM SY_QPRO.QPRO_RETENTION_OFFER ");
            sbQuery.append("WHERE qprorof_id = ").append(iRetentionOfferID);
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                int iID = Integer.valueOf(rs.getString("qprorof_id"));
                String sTitle = rs.getString("qprorof_des");
                String sOption = rs.getString("qprorof_qprorto_code");
                String sGroup = rs.getString("qprorof_qprortg_code");
                int iSubsubject = -1;
                rs.close();
                stmt.close();
                return new LovRetentionOffer(iID, sTitle, sOption, sGroup, iSubsubject);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovRetentionOffer()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * Get Lov service
    * @param iServiceID
    * @return LovService
    */
    public LovService getLovService(int iServiceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovService() - params iServiceID:").append(iServiceID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT a.qprosr_id, a.qprosr_des, c.qprop_id, c.qprop_des, d.qpropv_value, d.qpropv_des ");
            sbQuery.append("FROM SY_QPRO.QPRO_SERVICE a, SY_QPRO.QPRO_SERVICE_PARAM b, SY_QPRO.QPRO_PARAM c, SY_QPRO.QPRO_PARAM_VALUE d ");
            sbQuery.append("WHERE a.qprosr_id = ").append(iServiceID).append(" AND a.qprosr_id = b.qprosrp_qprosr_id (+) ");
            sbQuery.append("AND b.qprosrp_id = c.qprop_id (+) AND c.qprop_id = d.qpropv_qprosrp_id (+) ");
            sbQuery.append("ORDER BY 3,5");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                LovService service;
                int iID = -1; 
                String sTitle = null;
                int iCurr;
                HashMap<Integer, LovServiceParam> hParams = new HashMap<Integer, LovServiceParam>();
                int iParamID = -1;
                String sParamTitle = null;
                HashMap<String, String> hValues = new HashMap<String, String>();
                String sValue;
                String sValueTitle;
                
                while (rs.next()) {
                    
                    if (iID == -1)
                        iID = Integer.valueOf(rs.getString("qprosr_id"));
                    
                    if (sTitle == null)
                        sTitle = rs.getString("qprosr_des");
                    
                    iCurr = (rs.getString("qprop_id") != null) ? Integer.valueOf(rs.getString("qprop_id")) : -1; 
                    
                    // store param 
                    if (iParamID != -1 && iCurr != iParamID ) {
                        
                        if (hValues.isEmpty())
                            hValues = null;
                        
                        hParams.put(iParamID, new LovServiceParam(iParamID, sParamTitle, hValues));
                        hValues = new HashMap<String, String>();
                            
                    }
                    
                    // new param
                    if (iCurr != -1 && iCurr != iParamID) {
                                                                        
                        iParamID = iCurr;
                        sParamTitle = rs.getString("qprop_des");                                                                      
                        
                    }
                    
                    // store value
                    sValue = rs.getString("qpropv_value");
                    sValueTitle = (rs.getString("qpropv_des") == null) ? "" : rs.getString("qpropv_des");
                    
                    if (sValue != null)
                        hValues.put(sValue, sValueTitle);
                  
                }
                
                // last param
                if (iParamID != -1)
                    hParams.put(iParamID, new LovServiceParam(iParamID, sParamTitle, hValues));
                
                // store service                
                if (hParams.isEmpty())
                    hParams = null;
                
                service = new LovService(iID, sTitle, hParams);                 
                
                rs.close();
                stmt.close();
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovService()", ex);
            return null;
            
        }
        
    }        
    
}
