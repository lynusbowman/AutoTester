package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateful;

/**
* Stateful bean, manages AMX database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

@Stateful
public class AMXBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(AMXBean.class.getName()); 
    
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
    
    public AMXBean() {
        
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
                sb.append("Config file ").append(sConfig).append(" not found");
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
    * Connect to AMX database
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
                
                sUrl = properties.getProperty("test1AMXUrl");
                sUser = properties.getProperty("test1AMXUser");
                sPass = properties.getProperty("test1AMXPass");
                
            }
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) {
                
                sUrl = properties.getProperty("test2AMXUrl");
                sUser = properties.getProperty("test2AMXUser");
                sPass = properties.getProperty("test2AMXPass");
                
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
    * Disconnect from AMX database
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
            sbQuery.append("SELECT external_id FROM tmcdbo2.customer WHERE customer_id = ");
            sbQuery.append("(SELECT customer_id FROM ");
            sbQuery.append("(SELECT customer_id FROM tmcdbo2.subscriber WHERE prim_resource_val = '420").append(sMSISDN);
            sbQuery.append("' ORDER BY sys_creation_date DESC) ");
            sbQuery.append("WHERE rownum = 1)");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iCU = rs.getInt("external_id");
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
            sbQuery.append("SELECT external_id FROM ");
            sbQuery.append("(SELECT external_id FROM tmcdbo2.subscriber WHERE prim_resource_val = '420").append(sMSISDN);
            sbQuery.append("' ORDER BY sys_creation_date DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return -1;
                
            }
            else {
                
                int iSU = rs.getInt("external_id");
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
            sbQuery.append("SELECT sub_status FROM ");
            sbQuery.append("(SELECT sub_status FROM TMCDBO2.SUBSCRIBER WHERE prim_resource_val = '420").append(sMSISDN).append("' ORDER BY effective_date DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (!rs.next()) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                String sStatus = rs.getString("sub_status");
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
    * Get SU offers
    * @param iExtID EXTID_SU
    * @param iOfferID Offer ID
    * @return List of Offer
    */
    public List<Offer> getSUOffers(int iExtID, int iOfferID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getSUOffers() - params iExtID:").append(iExtID).append(",iOfferID:").append(iOfferID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT c.soc_cd, c.soc_name, b.soc_status ");
            sbQuery.append("FROM subscriber a, service_agreement b, csm_offer c ");
            sbQuery.append("WHERE a.external_id = '").append(iExtID).append("' ");
            
            // given offer
            if (iOfferID != -1)
                sbQuery.append("AND c.soc_cd = ").append(iOfferID).append(" ");
                            
            sbQuery.append("AND a.subscriber_no = b.agreement_no AND b.soc = c.soc_cd ");
            sbQuery.append("ORDER BY 1,b.effective_date DESC");
                    
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }

            List<Offer> lstOffers = new ArrayList();
            int iID = -1;
            String sTitle = null;
            String sStatus = null;
            int iCurr = -1;
            
            while (rs.next()) {                
                
                iID = rs.getInt("soc_cd");
                
                // store offer
                if (iCurr != -1 && iCurr != iID)
                    lstOffers.add(new Offer(iID, sTitle, sStatus));                
                
                // new offer instance
                if (iCurr == -1 || iCurr != iID) {
                
                    iCurr = iID;
                    iID = rs.getInt("soc_cd");
                    sTitle = rs.getString("soc_name");
                    sStatus = rs.getString("soc_status");                                   
                    
                }                                
                
            }
            
            // last service
            if (iID != -1)
                lstOffers.add(new Offer(iID, sTitle, sStatus));     
            
            rs.close();
            stmt.close();
            
            return lstOffers;
            
        }
        catch (Exception ex) {
            
            logger.error("getSUOffers()", ex);
            return null;
            
        }
        
    }   
    
    /**
    * Get Lov offer
    * @param iOfferID
    * @return LovOffer
    */
    public LovOffer getLovOffer(int iOfferID) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getLovOffer() - params iOfferID:").append(iOfferID);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT soc_cd, soc_name, service_level, soc_type ");
            sbQuery.append("FROM CSM_OFFER ");
            sbQuery.append("WHERE soc_cd = ").append(iOfferID);
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {
                
                rs.next();
                int iID = Integer.valueOf(rs.getString("soc_cd"));
                String sTitle = rs.getString("soc_name");
                String sServiceLevel = rs.getString("service_level");
                String sSocType = rs.getString("soc_type");
                rs.close();
                stmt.close();
                return new LovOffer(iID, sTitle, sServiceLevel, sSocType);
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLovOffer()", ex);
            return null;
            
        }
        
    }      
    
}
