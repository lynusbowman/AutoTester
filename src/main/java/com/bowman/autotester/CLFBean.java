package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Date;
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
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;

/**
* Stateless bean, manages CLF database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-03 
*/

@Stateless
public class CLFBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(CLFBean.class.getName()); 
    
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
    
    public CLFBean() {
        
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
                sb.append("Config file ").append(sUrl).append(" not found");
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
    * Connect to CLF database
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
                
                sUrl = properties.getProperty("test1CLFUrl");
                sUser = properties.getProperty("test1CLFUser");
                sPass = properties.getProperty("test1CLFPass");
                
            }
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) {
                
                sUrl = properties.getProperty("test2CLFUrl");
                sUser = properties.getProperty("test2CLFUser");
                sPass = properties.getProperty("test2CLFPass");
                
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
    * Disconnect from CLF database
    * @return boolean 
    */
    public boolean disconnect() {
        
        try {

            logger.debug("disconnect()");
            
            if (con != null)
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
            sbQuery.append("SELECT CUST_LEVEL, INT_ID, EXT_ID FROM (");
            sbQuery.append("SELECT tge.title AS CUST_LEVEL, ts.site_id AS INT_ID, ts.x_external_id AS EXT_ID ");
            sbQuery.append("FROM sa.table_site ts, sa.table_gbst_elm tge ");
            sbQuery.append("WHERE ts.x_site2ou_type = tge.objid ");
            sbQuery.append("START WITH ts.objid = (");
            sbQuery.append("SELECT tsp.site_part2site FROM sa.table_site_part tsp WHERE tsp.s_serial_no = '").append(sMSISDN).append("') ");
            sbQuery.append("CONNECT BY PRIOR ts.child_site2site = ts.objid ");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'BA' AS CUST_LEVEL, tba.x_business_id AS INT_ID, tba.x_external_id AS EXT_ID ");
            sbQuery.append("FROM sa.table_blg_argmnt tba ");
            sbQuery.append("WHERE tba.objid = (");
            sbQuery.append("SELECT tsp.x_site_part2blg_argmnt FROM sa.table_site_part tsp WHERE tsp.s_serial_no = '").append(sMSISDN).append("')");
            sbQuery.append("UNION ");
            sbQuery.append("SELECT 'SU' AS CUST_LEVEL, tsp.x_subs_agrm_id AS INT_ID, tsp.x_external_id  AS EXT_ID ");
            sbQuery.append("FROM sa.table_site_part tsp ");
            sbQuery.append("WHERE tsp.s_serial_no = '").append(sMSISDN).append("'");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_bus_org WHERE objid = ");
            sbQuery.append("(SELECT x_site2customer FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("'))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iCU = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_bus_org WHERE objid = ");
            sbQuery.append("(SELECT primary2bus_org FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("'))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iLE = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT child_site2site FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("'))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iOU = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iEU = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_blg_argmnt WHERE objid = ");
            sbQuery.append("(SELECT x_site_part2blg_argmnt FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iBA = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_external_id FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                rs.next();
                int iSU = rs.getInt("x_external_id");
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
            sbQuery.append("SELECT x_attr2 FROM sa.table_hgbst_elm WHERE objid = ");
            sbQuery.append("(SELECT x_site_part2status_su FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                String sStatus = rs.getString("x_attr2");
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
            sbQuery.append("SELECT x_extid, title FROM sa.table_gbst_elm WHERE objid = ");
            sbQuery.append("(SELECT x_bus_org2billcycle FROM sa.table_bus_org WHERE objid = ");
            sbQuery.append("(SELECT x_site2customer FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
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
            sbQuery.append("SELECT x_extid, title FROM sa.table_gbst_elm WHERE objid = ");
            sbQuery.append("(SELECT x_bus_org2market_segment FROM sa.table_bus_org WHERE objid = ");
            sbQuery.append("(SELECT x_site2customer FROM sa.table_site WHERE objid = ");
            sbQuery.append("(SELECT site_part2site FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')))");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
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
            sbQuery.append("SELECT x_extid, title FROM sa.table_gbst_elm WHERE objid = ");
            sbQuery.append("(SELECT x_site_part2csr_market FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
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
            sbQuery.append("SELECT tge.x_extid, tge.title ");
            sbQuery.append("FROM sa.table_site_part tsp, sa.table_x_tariff_detail ttd, sa.table_gbst_elm tge ");
            sbQuery.append("WHERE tsp.objid = ttd.x_tarif_detail2site_part ");
            sbQuery.append("AND ttd.x_tariff_detail2tariff = tge.objid ");
            sbQuery.append("AND ttd.x_active = 1 ");
            sbQuery.append("AND tsp.s_serial_no = '").append(sMSISDN).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;                
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
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
            sbQuery.append("SELECT x_attr5, title FROM sa.table_hgbst_elm WHERE objid = ");
            sbQuery.append("(SELECT x_site_part2contract_duration FROM sa.table_site_part WHERE s_serial_no = '").append(sMSISDN).append("')");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_attr5"));
                String sTitle = rs.getString("title");
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
    * Get SU detail
    * @param iExtID EXTID_SU
    * @return SUDetail
    */
    public SUDetail getSUDetail(int iExtID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSUDetail() - params iExtID:").append(iExtID);
            logger.debug(sb);

            // prepare call
            String sCall = "{? = call CLFAPI_OWN.SU.sf_Read(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // in parameters
            stmt.setInt(6, iExtID);
            stmt.setString(30, null);
            stmt.setString(31, null);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.NUMERIC);
            stmt.registerOutParameter(5, Types.NUMERIC);            
            stmt.registerOutParameter(6, Types.NUMERIC);
            stmt.registerOutParameter(7, Types.VARCHAR);
            stmt.registerOutParameter(8, Types.NUMERIC);
            stmt.registerOutParameter(9, Types.NUMERIC);
            stmt.registerOutParameter(10, Types.NUMERIC);
            stmt.registerOutParameter(11, Types.NUMERIC);
            stmt.registerOutParameter(12, Types.NUMERIC);
            stmt.registerOutParameter(13, Types.NUMERIC);
            stmt.registerOutParameter(14, Types.NUMERIC);
            stmt.registerOutParameter(15, Types.NUMERIC);
            stmt.registerOutParameter(16, Types.VARCHAR);
            stmt.registerOutParameter(17, Types.NUMERIC);
            stmt.registerOutParameter(18, Types.NUMERIC);
            stmt.registerOutParameter(19, Types.NUMERIC);
            stmt.registerOutParameter(20, Types.VARCHAR);
            stmt.registerOutParameter(21, Types.VARCHAR);
            stmt.registerOutParameter(22, Types.VARCHAR);
            stmt.registerOutParameter(23, Types.VARCHAR);
            stmt.registerOutParameter(24, Types.NUMERIC);
            stmt.registerOutParameter(25, Types.NUMERIC);
            stmt.registerOutParameter(26, Types.NUMERIC);
            stmt.registerOutParameter(27, Types.VARCHAR);
            stmt.registerOutParameter(28, Types.NUMERIC);
            stmt.registerOutParameter(29, Types.VARCHAR);
            
            // execute 
            stmt.execute();
            
            // create detail
            if (stmt.getInt(1) == 1) {
                
                SUDetail suDetail = new SUDetail();
                
                suDetail.setActivationType(stmt.getString(2));
                suDetail.setContrDuration(stmt.getInt(3));
                suDetail.setExtidBA(stmt.getInt(4));
                suDetail.setExtidOU(stmt.getInt(5));
                suDetail.setExtidSU(stmt.getInt(6));
                suDetail.setNote(stmt.getString(7));
                suDetail.setOutlet(stmt.getInt(8));
                suDetail.setPCN(stmt.getInt(9));
                suDetail.setSegmentAttr(stmt.getInt(10));
                suDetail.setSegmentAttr2(stmt.getInt(11));
                suDetail.setSegmentAttr3(stmt.getInt(12));
                suDetail.setStatusReasonIDC(stmt.getInt(13));
                suDetail.setStatusReasonIDF(stmt.getInt(14));
                suDetail.setStatusReasonIDU(stmt.getInt(15));
                suDetail.setSubsStatus(stmt.getString(16));
                suDetail.setSubsTariff(stmt.getInt(17));
                suDetail.setDunningSMS(stmt.getInt(18));
                suDetail.setWelcomeLetter(stmt.getInt(19));
                suDetail.setSubsNetStatus(stmt.getString(20));
                suDetail.setMSISDN(stmt.getString(21));
                suDetail.setICCID(stmt.getString(22));
                suDetail.setIMSI(stmt.getString(23));
                suDetail.setSUMarket(stmt.getInt(24));
                suDetail.setSUContractType(stmt.getInt(25));
                       
                return suDetail;
                
            }
            else
                return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getSUDetail()", ex);
            return null;
            
        }
        
    }     
    
    /**
    * Get BA detail
    * @param iExtID EXTID_BA
    * @return BADetail
    */
    public BADetail getBADetail(int iExtID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getBADetail() - params iExtID:").append(iExtID);
            logger.debug(sb);

            // prepare call
            String sCall = "{? = call CLFAPI_OWN.BA.sf_Read(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // in parameters
            stmt.setInt(5, iExtID);
            stmt.setString(13, null);
            stmt.setString(14, null);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.NUMERIC);
            stmt.registerOutParameter(6, Types.NUMERIC);            
            stmt.registerOutParameter(7, Types.NUMERIC);
            stmt.registerOutParameter(8, Types.VARCHAR);
            stmt.registerOutParameter(9, Types.NUMERIC);
            stmt.registerOutParameter(10, Types.VARCHAR);
            stmt.registerOutParameter(11, Types.NUMERIC);
            stmt.registerOutParameter(12, Types.VARCHAR);
            
            // execute 
            stmt.execute();
            
            // create detail
            if (stmt.getInt(1) == 1) {
                
                BADetail baDetail = new BADetail();
                
                baDetail.setBAStatus(stmt.getString(2));
                baDetail.setBAN(stmt.getInt(3));
                baDetail.setBAName(stmt.getString(4));
                baDetail.setExtidBA(stmt.getInt(5));
                baDetail.setExtidCU(stmt.getInt(6));
                baDetail.setExtidOULE(stmt.getInt(7));
                baDetail.setSegmentAttr(stmt.getString(8));                
                       
                return baDetail;
                
            }
            else
                return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getBADetail()", ex);
            return null;
            
        }
        
    } 
    
    /**
    * Get OU detail
    * @param iExtID EXTID_OU
    * @return OUDetail
    */
    public OUDetail getOUDetail(int iExtID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getOUDetail() - params iExtID:").append(iExtID);
            logger.debug(sb);

            // prepare call
            String sCall = "{? = call CLFAPI_OWN.OU.sf_Read(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // in parameters
            stmt.setInt(5, iExtID);
            stmt.setString(25, null);
            stmt.setString(26, null);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(2, Types.VARCHAR);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.VARCHAR);          
            stmt.registerOutParameter(6, Types.NUMERIC);
            stmt.registerOutParameter(7, Types.NUMERIC);
            stmt.registerOutParameter(8, Types.VARCHAR);
            stmt.registerOutParameter(9, Types.NUMERIC);
            stmt.registerOutParameter(10, Types.VARCHAR);
            stmt.registerOutParameter(11, Types.VARCHAR);
            stmt.registerOutParameter(12, Types.NUMERIC);
            stmt.registerOutParameter(13, Types.VARCHAR);
            stmt.registerOutParameter(14, Types.NUMERIC);
            stmt.registerOutParameter(15, Types.NUMERIC);
            stmt.registerOutParameter(16, Types.NUMERIC);
            stmt.registerOutParameter(17, Types.VARCHAR);
            stmt.registerOutParameter(18, Types.NUMERIC);
            stmt.registerOutParameter(19, Types.NUMERIC);
            stmt.registerOutParameter(20, Types.NUMERIC);
            stmt.registerOutParameter(21, Types.NUMERIC);
            stmt.registerOutParameter(22, Types.VARCHAR);
            stmt.registerOutParameter(23, Types.NUMERIC);
            stmt.registerOutParameter(24, Types.VARCHAR);
            
            // execute 
            stmt.execute();
            
            // create detail
            if (stmt.getInt(1) == 1) {
                
                OUDetail ouDetail = new OUDetail();
                
                ouDetail.setBillCycle(stmt.getString(2));
                ouDetail.setBusinessID(stmt.getInt(3));
                ouDetail.setCustomerStatus(stmt.getString(4));
                ouDetail.setExtidOU(iExtID);
                ouDetail.setExtidOUHigh(stmt.getInt(6));
                ouDetail.setFCN(stmt.getInt(7));
                ouDetail.setFraudNote(stmt.getString(8));
                ouDetail.setHasLE(stmt.getInt(9));
                ouDetail.setMAOperator(stmt.getString(10));
                ouDetail.setName(stmt.getString(11));
                ouDetail.setObjClass(stmt.getInt(12));
                ouDetail.setPartnerNote(stmt.getString(13));
                ouDetail.setSegment(stmt.getInt(14));
                ouDetail.setSegmentAttr(stmt.getInt(15));
                ouDetail.setVPNID(stmt.getInt(16));
                ouDetail.setHierarchyType(stmt.getString(16));
                ouDetail.setNoSynchro(stmt.getInt(17));
                ouDetail.setPrimaryLE(stmt.getInt(18));
                ouDetail.setPredeactivation(stmt.getInt(19));
                       
                return ouDetail;
                
            }
            else
                return null;
            
        }
        catch (Exception ex) {
            
            logger.error("getOUDetail()", ex);
            return null;
            
        }
        
    }   
    
    /**
    * Get SU services
    * @param iExtID EXTID_SU
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return List of Services
    */
    public List<Service> getSUServices(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSUServices() - params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT b.machine_id, b.instance_name, b.instance_id, c.x_attr1, ");
            sbQuery.append("d.x_value, NVL(f.x_id, -1) AS x_id, e.attribute_value ");
            sbQuery.append("FROM sa.table_site_part a, sa.table_site_part b, sa.table_hgbst_elm c, ");
            sbQuery.append("sa.table_x_csr_res d, sa.table_fa_site_part e, sa.table_flex_defn f ");
            sbQuery.append("WHERE b.site_part2site_part = a.objid AND a.x_external_id = ").append(iExtID).append(" ");
            
            // given service
            if (iServiceID != -1)
                sbQuery.append("AND b.machine_id = ").append(iServiceID).append(" ");
            
            // given instance
            if (iServiceInstanceID != -1)
                sbQuery.append("AND b.instance_id = ").append(iServiceInstanceID).append(" ");
                    
            sbQuery.append("AND b.x_site_part2status_su = c.objid AND b.x_site_part2resource = d.objid(+) ");
            sbQuery.append("AND b.objid = e.fa_site_part2site_part(+) AND e.fa_site_part2flex_defn = f.objid(+) ");
            sbQuery.append("ORDER BY 1,4,3,6");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }

            List<Service> lstServices = new ArrayList();
            int iID = -1;
            String sTitle = null;
            int iInstanceID = -1;
            String sStatus = null;
            String sResource = null;
            HashMap<Integer, String> hParameters = null;
            int iParameterID;
            String sParameterValue;
            int iCurr = -1;
            
            while (rs.next()) {                
                
                iInstanceID = rs.getInt("instance_id");
                
                // store service
                if ((iCurr != -1 && iCurr != iInstanceID))
                    lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));                
                
                // new service instance
                if (iCurr == -1 || iCurr != iInstanceID) {
                
                    iCurr = iInstanceID;
                    iID = rs.getInt("machine_id");
                    sTitle = rs.getString("instance_name");
                    sStatus = rs.getString("x_attr1");
                    sResource = rs.getString("x_value");
                    hParameters = new HashMap<Integer, String>();                                        
                    
                }                   
                
                // store parameter
                if (rs.getInt("x_id") != -1) {
                    
                    iParameterID = rs.getInt("x_id");
                    sParameterValue = rs.getString("attribute_value");
                    hParameters.put(iParameterID, sParameterValue);
                    
                }                
                
            }
            
            // last service
            if (iID != -1)
                lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));     
            
            rs.close();
            stmt.close();
            return lstServices;
            
        }
        catch (Exception ex) {
            
            logger.error("getSUServices()", ex);
            return null;
            
        }
        
    }      
    
    /**
    * Get BA services
    * @param iExtID EXTID_BA
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return List of Services
    */
    public List<Service> getBAServices(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getBAServices() - params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT b.machine_id, b.instance_name, b.instance_id, c.x_attr1, ");
            sbQuery.append("d.x_value, NVL(f.x_id, -1) AS x_id, e.attribute_value ");
            sbQuery.append("FROM sa.table_blg_argmnt a, sa.table_site_part b, sa.table_hgbst_elm c, ");
            sbQuery.append("sa.table_x_csr_res d, sa.table_fa_site_part e, sa.table_flex_defn f ");
            sbQuery.append("WHERE b.x_csr_service2ba = a.objid AND a.x_external_id = ").append(iExtID).append(" ");
            
            // given service
            if (iServiceID != -1)
                sbQuery.append("AND b.machine_id = ").append(iServiceID).append(" ");
            
            // given instance
            if (iServiceInstanceID != -1)
                sbQuery.append("AND b.instance_id = ").append(iServiceInstanceID).append(" ");
                    
            sbQuery.append("AND b.x_site_part2status_su = c.objid AND b.x_site_part2resource = d.objid(+) ");
            sbQuery.append("AND b.objid = e.fa_site_part2site_part(+) AND e.fa_site_part2flex_defn = f.objid(+) ");
            sbQuery.append("ORDER BY 1,4,3,6");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }

            List<Service> lstServices = new ArrayList();
            int iID = -1;
            String sTitle = null;
            int iInstanceID = -1;
            String sStatus = null;
            String sResource = null;
            HashMap<Integer, String> hParameters = null;
            int iParameterID;
            String sParameterValue;
            int iCurr = -1;
            
            while (rs.next()) {                
                
                iInstanceID = rs.getInt("instance_id");
                
                // store service
                if ((iCurr != -1 && iCurr != iInstanceID))
                    lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));                
                
                // new service instance
                if (iCurr == -1 || iCurr != iInstanceID) {
                
                    iCurr = iInstanceID;
                    iID = rs.getInt("machine_id");
                    sTitle = rs.getString("instance_name");
                    sStatus = rs.getString("x_attr1");
                    sResource = rs.getString("x_value");
                    hParameters = new HashMap<Integer, String>();                                        
                    
                }                   
                
                // store parameter
                if (rs.getInt("x_id") != -1) {
                    
                    iParameterID = rs.getInt("x_id");
                    sParameterValue = rs.getString("attribute_value");
                    hParameters.put(iParameterID, sParameterValue);
                    
                }                
                
            }
            
            // last service
            if (iID != -1)
                lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));     
            
            rs.close();
            stmt.close();
            return lstServices;
            
        }
        catch (Exception ex) {
            
            logger.error("getBAServices()", ex);
            return null;
            
        }
        
    }       
    
    /**
    * Get OU services
    * @param iExtID EXTID_OU
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return List of Services
    */
    public List<Service> getOUServices(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getOUServices() - params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT b.machine_id, b.instance_name, b.instance_id, c.x_attr1, ");
            sbQuery.append("d.x_value, NVL(f.x_id, -1) AS x_id, e.attribute_value ");
            sbQuery.append("FROM sa.table_site a, sa.table_site_part b, sa.table_hgbst_elm c, ");
            sbQuery.append("sa.table_x_csr_res d, sa.table_fa_site_part e, sa.table_flex_defn f ");
            sbQuery.append("WHERE b.x_csr_service2ou = a.objid AND a.x_external_id = ").append(iExtID).append(" ");
            
            // given service
            if (iServiceID != -1)
                sbQuery.append("AND b.machine_id = ").append(iServiceID).append(" ");
            
            // given instance
            if (iServiceInstanceID != -1)
                sbQuery.append("AND b.instance_id = ").append(iServiceInstanceID).append(" ");
                    
            sbQuery.append("AND b.x_site_part2status_su = c.objid AND b.x_site_part2resource = d.objid(+) ");
            sbQuery.append("AND b.objid = e.fa_site_part2site_part(+) AND e.fa_site_part2flex_defn = f.objid(+) ");
            sbQuery.append("ORDER BY 1,4,3,6");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }

            List<Service> lstServices = new ArrayList();
            int iID = -1;
            String sTitle = null;
            int iInstanceID = -1;
            String sStatus = null;
            String sResource = null;
            HashMap<Integer, String> hParameters = null;
            int iParameterID;
            String sParameterValue;
            int iCurr = -1;
            
            while (rs.next()) {                
                
                iInstanceID = rs.getInt("instance_id");
                
                // store service
                if ((iCurr != -1 && iCurr != iInstanceID))
                    lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));                
                
                // new service instance
                if (iCurr == -1 || iCurr != iInstanceID) {
                
                    iCurr = iInstanceID;
                    iID = rs.getInt("machine_id");
                    sTitle = rs.getString("instance_name");
                    sStatus = rs.getString("x_attr1");
                    sResource = rs.getString("x_value");
                    hParameters = new HashMap<Integer, String>();                                        
                    
                }                   
                
                // store parameter
                if (rs.getInt("x_id") != -1) {
                    
                    iParameterID = rs.getInt("x_id");
                    sParameterValue = rs.getString("attribute_value");
                    hParameters.put(iParameterID, sParameterValue);
                    
                }                
                
            }
            
            // last service
            if (iID != -1)
                lstServices.add(new Service(iID, sTitle, iInstanceID, sStatus, sResource, hParameters));     
            
            rs.close();
            stmt.close();
            return lstServices;
            
        }
        catch (Exception ex) {
            
            logger.error("getOUServices()", ex);
            return null;
            
        }
        
    } 
    
    /**
    * PAPI get
    * @param iExtID EXTID_OU
    * @param iCsType CS type
    * @param iPropertyID PAPI ID
    * @param iAttrIDs Attribute IDs
    * @param sAttrValues Attribute values
    * @return PAPI
    */
    public PAPI getPAPI(int iExtID, int iCsType, int iPropertyID, 
                        Object[] iAttrIDs, Object[] sAttrValues) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getPAPI() - params iExtID:").append(iExtID).append(", iCsType:").append(iCsType);
            sb.append(", iPropertyID:").append(iPropertyID).append(", iAttrIDs:").append(Arrays.toString(iAttrIDs));
            sb.append(", sAttrValues:").append(Arrays.toString(sAttrValues));
            logger.debug(sb);
            
            // prepare call            
            String sCall = "{? = call SY_CRM.PROPERTY_API.get_property(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // input attributes
            StringBuilder sbAttributes = new StringBuilder();
            HashMap<Integer, String> hInAttributes = null;            
            
            if (iAttrIDs == null)
                sbAttributes.append("<propertyAttributes />");
            // create xml
            else {
                
                sbAttributes.append("<?xml version=\"1.0\"?><propertyAttributes>");
                hInAttributes = new HashMap<Integer, String>();
                int iAttrID;
                String sAttrValue;                
                
                for (int i = 0; i < iAttrIDs.length; i++) {
                    
                    iAttrID = (Integer) iAttrIDs[i];
                    sAttrValue = sAttrValues[i].toString();
                    hInAttributes.put(iAttrID, sAttrValue);                    
                      
                    sbAttributes.append("<propertyAttribute><attrId>").append(iAttrID).append("</attrId>");
                    sbAttributes.append("<attrValue>").append(sAttrValue).append("</attrValue></propertyAttribute>");
                    
                }
                
                sbAttributes.append("</propertyAttributes>");
                
            }
            
            // in parameters
            stmt.setInt(2, iExtID);
            stmt.setInt(3, iCsType);
            stmt.setInt(4, iPropertyID);
            stmt.setNull(5, Types.INTEGER);
            stmt.setString(6, sbAttributes.toString());
            stmt.setString(7, "APPTEST");
            stmt.setInt(8, 1);
            stmt.setInt(9, 1);
            stmt.setString(18, null);
            stmt.setString(19, null);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(10, Types.VARCHAR);
            stmt.registerOutParameter(11, Types.VARCHAR);
            stmt.registerOutParameter(12, Types.NUMERIC);
            stmt.registerOutParameter(13, Types.VARCHAR);
            stmt.registerOutParameter(14, Types.NUMERIC);
            stmt.registerOutParameter(15, Types.VARCHAR);
            stmt.registerOutParameter(16, Types.NUMERIC);
            stmt.registerOutParameter(17, Types.VARCHAR);            
            
            // execute 
            stmt.execute();
            
            // create detail
            if (stmt.getInt(1) == 0) {               
                
                String sStatus = (stmt.getString(10) == null) ? "" : stmt.getString(10);
                String sAttrsRaw = stmt.getString(6);
                HashMap<Integer, String> hOutAttributes = null;
                
                // attributes
                if (sAttrsRaw != null) {                                        

                    InputStream isAttrs = new ByteArrayInputStream(sAttrsRaw.getBytes());
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(isAttrs);
                    NodeList lstAttrs = xml.getElementsByTagName("propertyAttribute");
                    Element elAttr;
                    int iAttributeID;
                    String sAttributeValue;
                    
                    // param elements
                    hOutAttributes = new HashMap<Integer, String>();
                    
                    for (int i = 0; i < lstAttrs.getLength(); i++) {
                    
                        elAttr = (Element) lstAttrs.item(i);
                        iAttributeID = Integer.valueOf(elAttr.getElementsByTagName("attrId").item(0).getTextContent());
                        sAttributeValue = elAttr.getElementsByTagName("attrValue").item(0).getTextContent();
                        hOutAttributes.put(iAttributeID, sAttributeValue); 
                    }
                    
                    isAttrs.close();                    
                    
                }                
                
                PAPI papi = new PAPI(iPropertyID, "G", sStatus, hInAttributes, hOutAttributes);
                stmt.close();
                
                return papi;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("getPAPI() - err_code:").append(stmt.getInt(12)).append(", err_txt:").append(stmt.getString(13));
                sb.append("war_code:").append(stmt.getInt(14)).append(", war_txt:").append(stmt.getString(15));
                sb.append("ora_code:").append(stmt.getInt(16)).append(", ora_txt:").append(stmt.getString(17));
                logger.error(sb);
                stmt.close();
                
                return null; 
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getPAPI()", ex);
            return null;
            
        }
        
    }    
    
    /**
    * PAPI set
    * @param iExtID EXTID_OU
    * @param iCsType CS type
    * @param iPropertyID PAPI ID
    * @param sAction Action
    * @param iAttrIDs Attribute IDs
    * @param sAttrValues Attribute values
    * @return PAPI
    */
    public PAPI setPAPI(int iExtID, int iCsType, int iPropertyID, String sAction, 
                        Object[] iAttrIDs, Object[] sAttrValues) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("setPAPI() - params iExtID:").append(iExtID).append(", iCsType:").append(iCsType);
            sb.append(", iPropertyID:").append(iPropertyID).append(", sAction:").append(sAction);
            sb.append(", iAttrIDs:").append(Arrays.toString(iAttrIDs)).append(", sAttrValues:").append(Arrays.toString(sAttrValues));
            logger.debug(sb);
            
            // prepare call
            String sCall = "{? = call SY_CRM.PROPERTY_API.set_property(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);
            
            // input attributes
            StringBuilder sbAttributes = new StringBuilder();
            HashMap<Integer, String> hInAttributes = null;
            
            if (iAttrIDs == null)
                sbAttributes.append("<propertyAttributes />");
            // create xml
            else {
                
                sbAttributes.append("<?xml version=\"1.0\"?><propertyAttributes>");
                hInAttributes = new HashMap<Integer, String>();
                int iAttrID;
                String sAttrValue;
                
                for (int i = 0; i < iAttrIDs.length; i++) {
                      
                    iAttrID = (Integer) iAttrIDs[i];
                    sAttrValue = sAttrValues[i].toString();
                    hInAttributes.put(iAttrID, sAttrValue);
                    
                    sbAttributes.append("<propertyAttribute><attrId>").append(iAttrID).append("</attrId>");
                    sbAttributes.append("<attrValue>").append(sAttrValue).append("</attrValue></propertyAttribute>");
                    
                }
                
                sbAttributes.append("</propertyAttributes>");
                
            }
            
            // in parameters
            stmt.setInt(2, iExtID);
            stmt.setInt(3, iCsType);
            stmt.setInt(4, iPropertyID);
            stmt.setNull(5, Types.INTEGER);
            stmt.setString(6, sAction);
            stmt.setString(7, sbAttributes.toString());
            stmt.setString(8, "APPTEST");
            stmt.setNull(9, Types.INTEGER);
            stmt.setString(10, null);
            stmt.setInt(11, 1);
            stmt.setInt(12, 1);
            stmt.setNull(13, Types.INTEGER);
            stmt.setNull(14, Types.INTEGER);
            stmt.setNull(15, Types.INTEGER);
            stmt.setString(16, null);
            stmt.setNull(17, Types.INTEGER);
            stmt.setString(18, null);
            stmt.setString(25, null);
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(7, Types.VARCHAR);
            stmt.registerOutParameter(19, Types.NUMERIC);
            stmt.registerOutParameter(20, Types.VARCHAR);
            stmt.registerOutParameter(21, Types.NUMERIC);
            stmt.registerOutParameter(22, Types.VARCHAR);
            stmt.registerOutParameter(23, Types.NUMERIC);
            stmt.registerOutParameter(24, Types.VARCHAR);            
            
            // execute 
            stmt.execute();
            
            // create detail
            if (stmt.getInt(1) == 0) {               
                
                String sAttrsRaw = stmt.getString(7);
                HashMap<Integer, String> hOutAttributes = null;
                
                // attributes
                if (sAttrsRaw != null) {                                        

                    InputStream isAttrs = new ByteArrayInputStream(sAttrsRaw.getBytes());
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(isAttrs);
                    NodeList lstAttrs = xml.getElementsByTagName("propertyAttribute");
                    Element elAttr;
                    int iAttributeID;
                    String sAttributeValue;
                    
                    // param elements
                    hOutAttributes = new HashMap<Integer, String>();
                    
                    for (int i = 0; i < lstAttrs.getLength(); i++) {
                    
                        elAttr = (Element) lstAttrs.item(i);
                        iAttributeID = Integer.valueOf(elAttr.getElementsByTagName("attrId").item(0).getTextContent());
                        sAttributeValue = elAttr.getElementsByTagName("attrValue").item(0).getTextContent();
                        hOutAttributes.put(iAttributeID, sAttributeValue); 
                    }
                    
                    isAttrs.close();                   
                    
                }                
                
                PAPI papi = new PAPI(iPropertyID, sAction, "", hInAttributes, hOutAttributes);
                stmt.close();
                
                return papi;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("setPAPI() - err_code:").append(stmt.getInt(19)).append(", err_txt:").append(stmt.getString(20));
                sb.append("setPAPI() - war_code:").append(stmt.getInt(21)).append(", war_txt:").append(stmt.getString(22));
                sb.append("setPAPI() - ora_code:").append(stmt.getInt(23)).append(", ora_txt:").append(stmt.getString(24));
                logger.error(sb);
                stmt.close();
                
                return null;     
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("setPAPI()", ex);
            return null;
            
        }
        
    }  
    
    /**
    * Get case
    * @param sMSISDN MSISDN
    * @param sCaseType Casetype
    * @return Case
    */
    public Case getCase(String sMSISDN, String sCaseType) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getCase() - params sMSISDN:").append(sMSISDN).append(", sCaseType:").append(sCaseType);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT id, case_history, status, casetype, process, subject, subsubject, step, ");
            sbQuery.append("x_date1, x_date2, x_mffnum1, x_mffnum2, x_mffnum3, x_mffnum4, x_mffnum5, ");
            sbQuery.append("x_mffield1, x_mffield2, x_mffield3, x_mffield4, x_mffield5, x_mffield6, x_mffield7 ");
            sbQuery.append("FROM ");
            sbQuery.append("(SELECT a.id_number AS id, a.case_history, j.s_title AS status, ");
            sbQuery.append("d.x_proc_id AS casetype, g.title AS process, h.title AS subject, i.title AS subsubject, e.title AS step, ");
            sbQuery.append("f.x_date1, f.x_date2, f.x_mffnum1, f.x_mffnum2, f.x_mffnum3, f.x_mffnum4, f.x_mffnum5, ");
            sbQuery.append("f.x_mffield1, f.x_mffield2, f.x_mffield3, f.x_mffield4, f.x_mffield5, f.x_mffield6, f.x_mffield7 ");
            sbQuery.append("FROM sa.table_case a, sa.table_site_part b, sa.table_x_procedures d, sa.table_hgbst_elm e, sa.table_x_mff f, ");
            sbQuery.append("sa.table_hgbst_elm g, sa.table_hgbst_elm h, sa.table_hgbst_elm i, sa.table_condition j ");
            sbQuery.append("WHERE a.case_prod2site_part = b.objid AND b.s_serial_no = '").append(sMSISDN).append("' AND d.x_proc_id = '").append(sCaseType).append("' ");
            sbQuery.append("AND a.case2x_procedures = d.objid AND d.x_process2hgbst_elm = g.objid AND d.x_subject2hgbst_elm = h.objid " );
            sbQuery.append("AND d.x_subsubject2hgbst_elm = i.objid AND a.case2x_step_id = e.objid AND a.case2x_mff = f.objid(+) ");
            sbQuery.append("AND a.case_state2condition = j.objid ");
            sbQuery.append("ORDER BY a.creation_time DESC)");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            Case cCase = null;
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                String sID = rs.getString("id");
                String sHistory = rs.getString("case_history");
                String sStatus = rs.getString("status");
                String sCT = rs.getString("casetype");
                String sProcess = rs.getString("process");
                String sSubject = rs.getString("subject");
                String sSubsubject = rs.getString("subsubject");
                String sStep = rs.getString("step");
                
                Date[] MFFD = new Date[] {
                    rs.getDate("x_date1"), rs.getDate("x_date2")
                };
                
                float[] MFFN = new float[] {
                    rs.getFloat("x_mffnum1"), rs.getFloat("x_mffnum2"), rs.getFloat("x_mffnum3"),
                    rs.getFloat("x_mffnum4"), rs.getFloat("x_mffnum5")
                };
                
                String[] MFFT = new String[] {
                    rs.getString("x_mffield1"), rs.getString("x_mffield2"), rs.getString("x_mffield3"),
                    rs.getString("x_mffield4"), rs.getString("x_mffield5"), rs.getString("x_mffield6"),
                    rs.getString("x_mffield7")
                };
                
                cCase = new Case(sID, sStatus, sCaseType, sProcess, sSubject, sSubsubject, sStep, 
                                 sHistory, MFFD, MFFN, MFFT);
                
            }
                
                rs.close();
                stmt.close();
                return cCase;                
            
        }
        catch (Exception ex) {
            
            logger.error("getCase()", ex);
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
            sbQuery.append("SELECT a.x_extid, a.title, a.x_title_en, a.x_attr11, a.x_attr5, a.state, a.x_attr10, a.x_attr16, ");
            sbQuery.append("a.x_attr23, a.x_attr29, a.x_attr30, a.x_attr31, a.x_attr32, a.x_attr33, a.x_attr34, a.x_attr35, a.x_attr36 ");
            sbQuery.append("FROM SA.TABLE_GBST_ELM a,  SA.TABLE_GBST_LST b ");
            sbQuery.append("WHERE a.gbst_elm2gbst_lst = b.objid AND b.title = 'TARIFF' AND a.x_Extid = '").append(iTariffID).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
                String sCzTitle = rs.getString("x_title_en");
                String sOffer = rs.getString("x_attr11");
                float fMonthlyFee = Float.valueOf(rs.getString("x_attr5"));
                int iActive = Integer.valueOf(rs.getString("state"));
                String sGroup = rs.getString("x_attr10"); 
                String sType = rs.getString("x_attr16"); 
                String sReportingType = rs.getString("x_attr23"); 
                String sProfile = "";
                String sBundleFlg = rs.getString("x_attr29"); 
                Float fBundleDiscount = (rs.getString("x_attr30") != null ? Float.valueOf(rs.getString("x_attr30")) : -1); 
                String sVPNCompatibleFlg = rs.getString("x_attr31");
                String sNonpublicFlg = rs.getString("x_attr32");
                String sLTEFlg = rs.getString("x_attr33");
                String sFlatType = rs.getString("x_attr34");
                int iSQDProfile = (rs.getString("x_attr35") != null ? Integer.valueOf(rs.getString("x_attr35")) : -1);
                String sSharedTariffComp = rs.getString("x_attr36");
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
            sbQuery.append("SELECT a.x_extid, a.title, a.x_attr1 ");
            sbQuery.append("FROM SA.TABLE_GBST_ELM a, SA.TABLE_GBST_LST b ");
            sbQuery.append("WHERE a.gbst_elm2gbst_lst = b.objid AND b.title = 'NON_PUBLIC_OFFERS' AND a.x_extid = '");
            sbQuery.append(iNonpublicOfferID).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
                int iServiceID = Integer.valueOf(rs.getString("x_attr1"));
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
            sbQuery.append("select a.x_extid, a.title, a.description, nvl(a.x_attr1,-1) as x_attr1, nvl(a.x_attr2,-1) as x_attr2, ");
            sbQuery.append("nvl(a.x_attr3,-1) as x_attr3, nvl(a.x_attr4,-1) as x_attr4, nvl(a.x_attr5,-1) as x_attr5, ");
            sbQuery.append("nvl(a.x_attr6,-1) as x_attr6, nvl(a.x_attr7,-1) as x_attr7, nvl(a.x_attr8,-1) as x_attr8, nvl(a.x_attr9,-1) as x_attr9 ");
            sbQuery.append("FROM SA.TABLE_GBST_ELM a,  SA.TABLE_GBST_LST b ");
            sbQuery.append("WHERE a.gbst_elm2gbst_lst = b.objid AND b.title = 'TARIFF_PROMOS' AND a.x_Extid = '");
            sbQuery.append(iTariffPromoID).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
                String sDescription = rs.getString("description");
                String sReasonOfUse = rs.getString("x_attr1");
                int iExtraMinutes = Integer.valueOf(rs.getString("x_attr2"));
                int iExtraMessages = Integer.valueOf(rs.getString("x_attr3"));
                int iFupLimit = Integer.valueOf(rs.getString("x_attr4"));
                float fMfDiscount = Float.valueOf(rs.getString("x_attr5"));
                int iTariff = Integer.valueOf(rs.getString("x_attr6"));
                int iExtraCreditPcnt = Integer.valueOf(rs.getString("x_attr7"));
                int iExtraMinutesTmcz = Integer.valueOf(rs.getString("x_attr8"));
                float fMbdMfDiscount = Float.valueOf(rs.getString("x_attr9"));
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
            sbQuery.append("SELECT a.x_extid, a.title, a.x_attr1, a.x_attr2, a.x_attr3 ");
            sbQuery.append("FROM SA.TABLE_GBST_ELM a, SA.TABLE_GBST_LST b ");
            sbQuery.append("WHERE a.gbst_elm2gbst_lst = b.objid AND b.title = 'RETENTION_OFFER' AND a.x_extid = '");
            sbQuery.append(iRetentionOfferID).append("'");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("x_extid"));
                String sTitle = rs.getString("title");
                String sOption = rs.getString("x_attr1");
                String sGroup = rs.getString("x_attr2");
                int iSubsubject = Integer.valueOf(rs.getString("x_attr3"));
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

            // params query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT a.s_model_num, a.description, f.x_id, f.attribute_name, g.x_value, g.x_description ");
            sbQuery.append("FROM SA.TABLE_PART_NUM a, SA.TABLE_MOD_LEVEL b, SA.MTM_MOD_LEVEL53_FLEX_TMPL1 c, SA.TABLE_FLEX_TMPL d, ");
            sbQuery.append("SA.MTM_FLEX_DEFN0_FLEX_TMPL0 e, SA.TABLE_FLEX_DEFN f, SA.TABLE_X_CSR_PARAM_VALUE g ");
            sbQuery.append("WHERE a.s_model_num = '").append(iServiceID).append("' AND a.objid = b.part_info2part_num ");
            sbQuery.append("AND b.objid = c.mod_level2flex_tmpl AND c.flex_tmpl2mod_level = d.objid AND e.flex_tmpl2flex_defn = d.objid ");
            sbQuery.append("AND e.flex_defn2flex_tmpl = f.objid (+) AND f.objid = g.focus_lowid (+)");
            
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
                LovServiceParam param = null;
                int iParamID = -1;
                String sParamTitle = null;
                HashMap<String, String> hValues = new HashMap<String, String>();
                String sValue;
                String sValueTitle;
                
                int iDataType = -1;
                String sDefaultValue = null;
                int iRequired = -1;
                int iMultiple = -1;
                int iNetworkIndicator = -1;
                
                while (rs.next()) {
                    
                    if (iID == -1)
                        iID = Integer.valueOf(rs.getString("s_model_num"));
                    
                    if (sTitle == null)
                        sTitle = rs.getString("description");
                    
                    iCurr = (rs.getString("x_id") != null) ? Integer.valueOf(rs.getString("x_id")) : -1; 
                    
                    // store param 
                    if (iParamID != -1 && iCurr != iParamID ) {
                        
                        if (hValues.isEmpty())
                            hValues = null;
                        
                        hParams.put(iParamID, param);
                        hValues = new HashMap<String, String>();
                            
                    }
                    
                    // new param
                    if (iCurr != -1 && iCurr != iParamID) {
                                                                        
                        iParamID = iCurr;
                        sParamTitle = rs.getString("attribute_name");                                                                      
                        param = new LovServiceParam(iParamID, sParamTitle, hValues);
                        
                        // param details
                        StringBuilder sbQueryParam = new StringBuilder();
                        sbQueryParam.append("SELECT attribute_datatype, default_value, is_required, x_is_multiple, x_csr_network_indicator ");
                        sbQueryParam.append("FROM SA.TABLE_FLEX_DEFN ");
                        sbQueryParam.append("WHERE x_id = ").append(iParamID);   
                        
                        Statement stmtParam = con.createStatement();
                        ResultSet rsParam = stmtParam.executeQuery(sbQueryParam.toString());
                        
                        // parse result
                        if (rsParam != null) {
                            
                            rsParam.next();
                            
                            iDataType = Integer.valueOf(rsParam.getString("attribute_datatype"));
                            sDefaultValue = (rsParam.getString("default_value") == null) ? "" : rsParam.getString("default_value");
                            iRequired = Integer.valueOf(rsParam.getString("is_required"));        
                            iMultiple = Integer.valueOf(rsParam.getString("x_is_multiple"));
                            iNetworkIndicator = Integer.valueOf(rsParam.getString("x_csr_network_indicator"));
                            
                            param.setDataType(iDataType);
                            param.setDefaultValue(sDefaultValue);
                            param.setRequired(iRequired);
                            param.setMultiple(iMultiple);
                            param.setNetworkIndicator(iNetworkIndicator);
                            
                        }
                        
                    }
                    
                    // store value
                    sValue = rs.getString("x_value");
                    sValueTitle = (rs.getString("x_description") == null) ? "" : rs.getString("x_description");
                    
                    if (sValue != null)
                        hValues.put(sValue, sValueTitle);
                  
                }
                
                // last param
                if (iParamID != -1) 
                    hParams.put(iParamID, new LovServiceParam(iParamID, sParamTitle, hValues, iDataType, sDefaultValue,
                                                              iRequired, iMultiple, iNetworkIndicator));
                               
                if (hParams.isEmpty())
                    hParams = null;
                
                // store service
                service = new LovService(iID, sTitle, hParams);                
                
                // service detail
                sbQuery = new StringBuilder();
                sbQuery.append("SELECT a.eff_date, a.end_date, a.x_is_multiple, a.x_csr_su, a.x_csr_cu, a.x_csr_le, ");
                sbQuery.append("a.x_csr_ou, a.x_csr_eu, a.x_csr_ba, a.x_csr_co, a.x_csr_network_indicator, a.x_is_suspendable, ");
                sbQuery.append("a.x_planning_to_future, a.x_is_assign_push, a.x_is_change_push, a.x_create_time_bomb, ");
                sbQuery.append("d.x_extid, d.title, c.x_shared, c.x_csr_network_indicator AS res_network_indicator ");
                sbQuery.append("FROM SA.TABLE_MOD_LEVEL a, SA.TABLE_PART_NUM b, SA.TABLE_X_CSR_RES_DEF c, SA.TABLE_GBST_ELM d ");
                sbQuery.append("WHERE b.s_model_num = '").append(iServiceID).append("' AND b.objid = a.part_info2part_num ");
                sbQuery.append("AND a.x_mod_level2csr_res_def = c.objid (+) AND c.x_csr_res_def2type = d.objid (+)");
            
                stmt = con.createStatement();
                rs = stmt.executeQuery(sbQuery.toString());   
                
                // parse result
                if (rs != null) {
                    
                    rs.next();
                    
                    service.setStartDate(rs.getString("eff_date"));
                    service.setEndDate(rs.getString("end_date"));
                    service.setNetworkIndicator(Integer.valueOf(rs.getString("x_csr_network_indicator")));
                    service.setMultiple(Integer.valueOf(rs.getString("x_is_multiple")));
                    service.setSuspendable(Integer.valueOf(rs.getString("x_is_suspendable")));
                    service.setSchedulable(Integer.valueOf(rs.getString("x_planning_to_future")));
                    service.setAssignPush(Integer.valueOf(rs.getString("x_is_assign_push")));
                    service.setChangePush(Integer.valueOf(rs.getString("x_is_change_push")));
                    service.setTimeBomb(Integer.valueOf(rs.getString("x_create_time_bomb")));
                    service.setSU(Integer.valueOf(rs.getString("x_csr_su")));
                    service.setBA(Integer.valueOf(rs.getString("x_csr_ba")));
                    service.setEU(Integer.valueOf(rs.getString("x_csr_eu")));
                    service.setOU(Integer.valueOf(rs.getString("x_csr_ou")));
                    service.setLE(Integer.valueOf(rs.getString("x_csr_le")));
                    service.setCU(Integer.valueOf(rs.getString("x_csr_cu")));
                    service.setCO(Integer.valueOf(rs.getString("x_csr_co")));
                    
                    // resource
                    if (rs.getString("x_extid") != null) {
                        
                        service.setResourceID(Integer.valueOf(rs.getString("x_extid")));
                        service.setResourceTitle(rs.getString("title"));
                        service.setResourceShared(Integer.valueOf(rs.getString("x_shared")));
                        service.setResourceNetworkIndicator(Integer.valueOf(rs.getString("res_network_indicator")));
                    
                    }
                    
                }   
                
                // offers
                sbQuery = new StringBuilder();
                sbQuery.append("SELECT DISTINCT c.x_offer_id, c.x_description, d.x_name ");
                sbQuery.append("FROM SA.TABLE_PART_NUM a, SA.TABLE_MOD_LEVEL b, SA.TABLE_X_CSR_OFFER_CFG c, SA.TABLE_X_CSR_OFFER_CFG_PARAM d ");
                sbQuery.append("WHERE a.s_model_num = '").append(iServiceID).append("' AND a.objid = b.part_info2part_num AND b.objid = c.x_csr_offer_cfg2service ");
                sbQuery.append("AND c.objid = d.x_csr_offer_cfg_param2offer (+) ");
                sbQuery.append("ORDER BY 1, 3");
            
                stmt = con.createStatement();
                rs = stmt.executeQuery(sbQuery.toString());   
                
                // parse result
                LovOffer offer = null;
                HashMap<Integer, LovOffer> hOffers = new HashMap<Integer, LovOffer>();
                List<String> lstParams = new ArrayList<String>();
                iID = -1;
                sTitle = null;
                iCurr = -1;
                
                if (rs != null) {
                
                    while (rs.next()) {

                        iCurr = Integer.valueOf(rs.getString("x_offer_id")); 
                    
                        // store offer 
                        if (iID != -1 && iCurr != iID ) {
                        
                            if (lstParams.isEmpty())
                                lstParams = null;
                        
                            hOffers.put(iID, offer);
                            lstParams = new ArrayList<String>();
                            
                        }
                    
                        // new offer
                        if (iCurr != -1 && iCurr != iID) {
                                                                        
                            iID = iCurr;
                            sTitle = rs.getString("x_description");                                                                      
                            offer = new LovOffer(iID, sTitle, lstParams);   
                        
                        }
                    
                        // store param
                        sParamTitle = rs.getString("x_name");
                    
                        if (sParamTitle != null)
                            lstParams.add(sParamTitle);
                    
                    }
                
                    // last offer
                    if (iID != -1)
                        hOffers.put(iID, new LovOffer(iID, sTitle, lstParams));  
                
                    service.setOffers(hOffers);
                
                }
                
                // clear resources
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
    
    /**
    * Get Lov PAPI
    * @param iPAPIID
    * @return LovPAPI
    */
    public LovPAPI getLovPAPI(int iPAPIID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovPAPI() - params iPAPIID:").append(iPAPIID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT a.property_id, a.name, c.action_id, d.attribute_id, e.name as attribute, f.name AS direction, g.name as cardinality, ");
            sbQuery.append("h.name as datatype, i.name as type, e.clf_lov_name, e.clf_lov_parent_id ");
            sbQuery.append("FROM SY_CRM.CRM_PROPERTY a, SY_CRM.CRM_PROPERTY_VERSION b, SY_CRM.CRM_PROPERTY_ACTION c, ");
            sbQuery.append("SY_CRM.CRM_PROPERTY_ATTRIBUTE d, SY_CRM.CRM_ATTRIBUTE e, SY_CRM.CRM_LOV_DIRECTION f, SY_CRM.CRM_LOV_CARDINALITY g, ");
            sbQuery.append("SY_CRM.CRM_LOV_DATATYPE h, SY_CRM.CRM_LOV_ATTRIBUTE_TYPE i ");
            sbQuery.append("WHERE a.property_id = ").append(iPAPIID).append(" AND a.property_id = b.property_id AND b.property_version_id = c.property_version_id ");
            sbQuery.append("AND c.property_action_id = d.property_action_id and d.attribute_id = e.attribute_id ");
            sbQuery.append("AND d.direction_id = f.direction_id AND d.cardinality_id = g.cardinality_id AND e.datatype_id = h.datatype_id ");
            sbQuery.append("AND e.attribute_type_id = i.attribute_type_id ");
            sbQuery.append("ORDER BY 3, 4");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                LovPAPI papi;
                int iID = -1; 
                String sTitle = null;
                String sCurr;
                HashMap<String, LovPAPIAction> hActions = new HashMap<String, LovPAPIAction>();
                LovPAPIAction action = null;
                String sAction = null;
                HashMap<Integer, LovPAPIAttribute> hAttributes = new HashMap<Integer, LovPAPIAttribute>();
                int iAttrID;
                String sAttrTitle;
                String sDirection;
                String sCardinality;
                String sDataType;
                String sType;
                String sLovName;
                String sLovParentID;                
                
                while (rs.next()) {     
                    
                    if (iID == -1)
                        iID = Integer.valueOf(rs.getString("property_id"));
                    
                    if (sTitle == null)
                        sTitle = rs.getString("name");
                    
                    sCurr = rs.getString("action_id"); 
                    
                    // store action 
                    if (sAction != null && !sCurr.equals(sAction)) {
                        
                        if (hAttributes.isEmpty())
                            hAttributes = null;
                        
                        hActions.put(sAction, action);
                        hAttributes = new HashMap<Integer, LovPAPIAttribute>();
                            
                    }
                    
                    // new action
                    if (sCurr != null && !sCurr.equals(sAction)) {
                                                                        
                        sAction = sCurr;                                                                    
                        action = new LovPAPIAction(sAction, hAttributes);
                        
                    }
                    
                    // store attribute
                    iAttrID = rs.getInt("attribute_id");
                    sAttrTitle = rs.getString("attribute");
                    sDirection = rs.getString("direction");
                    sCardinality = rs.getString("cardinality");
                    sDataType = rs.getString("datatype");
                    sType = rs.getString("type");
                    sLovName = (rs.getString("clf_lov_name") == null) ? "" : rs.getString("clf_lov_name");
                    sLovParentID = (rs.getString("clf_lov_parent_id") == null) ? "" : rs.getString("clf_lov_parent_id");
                    
                    hAttributes.put(iAttrID, new LovPAPIAttribute(iAttrID, sAttrTitle, sDirection, sCardinality,
                                                                  sDataType, sType, sLovName, sLovParentID));
                  
                }    
                
                // last action
                if (sAction != null)
                    hActions.put(sAction, new LovPAPIAction(sAction, hAttributes));
                
                // store service
                papi = new LovPAPI(iID, sTitle, hActions);   
                
                // clear resources
                rs.close();
                stmt.close();
                return papi;                
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovPAPI()", ex);
            return null;
            
        }
        
    }      
    
}
