package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Properties;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;

/**
* Stateless bean, manages CNH database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-11 
*/

@Stateless
public class CNHBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(CNHBean.class.getName()); 
    
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
    
    public CNHBean() {
        
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
    * Connect to CNH database
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
                
                sUrl = properties.getProperty("test1CNHUrl");
                sUser = properties.getProperty("test1CNHUser");
                sPass = properties.getProperty("test1CNHPass");
                
            }
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) {
                
                sUrl = properties.getProperty("test2CNHUrl");
                sUser = properties.getProperty("test2CNHUser");
                sPass = properties.getProperty("test2CNHPass");
                
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
    * Disconnect from CNH database
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
    * Get notification
    * @param sMSISDN MSISDN
    * @return Notification
    */
    public Notification getNotification(String sMSISDN) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("getNotification() - params sMSISDN:").append(sMSISDN);
            logger.debug(sb);

            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("SELECT notif_id, template_id, notif_status, bundle_params FROM ");
            sbQuery.append("(SELECT notif_id, template_id, notif_status, bundle_params ");
            sbQuery.append("FROM sy_cnh.cnh_bundle WHERE notif_key = '").append(sMSISDN).append("' ");
            sbQuery.append("AND timestamp_creation > trunc(sysdate) ");
            sbQuery.append("ORDER BY timestamp_creation DESC) ");
            sbQuery.append("WHERE rownum = 1");
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sbQuery.toString());
            
            // parse result
            if (rs == null) {
                
                stmt.close();
                return null;
                
            }
            else {             
                
                rs.next();
                int iID = rs.getInt("notif_id");
                String sTemplate = rs.getString("template_id");
                int iStatus = rs.getInt("notif_status");
                String sParamsRaw = rs.getString("bundle_params");
                HashMap<String, String> hParameters = null;
                
                // parameters
                if (sParamsRaw != null) {
                   
                    InputStream isParams = new ByteArrayInputStream(sParamsRaw.getBytes());
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(isParams);
                    NodeList lstParams = xml.getElementsByTagName("cnh:param");
                    Element elParam;
                    String sParameterName;
                    String sParameterValue;

                    // parameters in nested xml
                    hParameters = new HashMap<String, String>();
                    InputStream isParam = new ByteArrayInputStream(lstParams.item(1).getLastChild().getTextContent().getBytes());
                    Document xmlParam = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(isParam);
                    NodeList nodes = xmlParam.getFirstChild().getChildNodes();
                    Node node;
                    
                    for (int i = 0; i < nodes.getLength(); i++) {
                        
                        node = nodes.item(i);
                        sParameterName = node.getNodeName().replaceAll("ns0:", "");                        
                        sParameterValue = node.getTextContent();
                        hParameters.put(sParameterName, sParameterValue); 
                        
                    }
                      
                    isParam.close();
                    isParams.close();                     
                    
                }
                
                Notification notification = new Notification(iID, sTemplate, iStatus, hParameters);            
                rs.close();
                stmt.close();
                
                return notification;
            
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getNotification()", ex);
            return null;
            
        }
        
    }    
    
}
