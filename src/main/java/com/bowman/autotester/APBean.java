package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Types;

import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;

/**
* Stateless bean, manages AP database
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

@Stateless
public class APBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(APBean.class.getName()); 
    
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
    
    public APBean() {
        
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
    * Connect to AP database
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
                
                sUrl = properties.getProperty("test1APUrl");
                sUser = properties.getProperty("test1APUser");
                sPass = properties.getProperty("test1APPass");
                
            }
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) {
                
                sUrl = properties.getProperty("test2APUrl");
                sUser = properties.getProperty("test2APUser");
                sPass = properties.getProperty("test2APPass");
                
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
    * Disconnect from AP database
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
    * Call DTS
    * @param sLevel Cust level
    * @param iExtID EXTID
    * @param sTitle DTS name
    * @param sParamNames Parameter names
    * @param sParamValues Parameter values
    * @return PAPI
    */
    public DTS callDTS(String sLevel, int iExtID, String sTitle, Object[] sParamNames, Object[] sParamValues) {
        
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("callDTS() - params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sTitle:").append(sTitle).append(", sParamNames:").append(Arrays.toString(sParamNames));
            sb.append(", sParamValues:").append(Arrays.toString(sParamValues));
            logger.debug(sb);             
            
            // prepare call
            String sCall = "{? = call SY_DTS.DTS_RUN_TIBCO(?,?,?,?,?,?)}";
            CallableStatement stmt = con.prepareCall(sCall);     
            
            // input parameters
            StringBuilder sbParameters = new StringBuilder();
            sbParameters.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><parameters>");
            HashMap<String, String> hParameters = new HashMap<String, String>();
            
            // cust level
            if (sLevel.equals("SU")) {
                
                sbParameters.append("<param><paramName>SUB_ID</paramName><paramValue>").append(iExtID).append("</paramValue></param>");
                hParameters.put("SUB_ID", String.valueOf(iExtID));
                
            }
            else if (sLevel.equals("BA")) {
                
                sbParameters.append("<param><paramName>BILL_ARRANGEMENT</paramName><paramValue>").append(iExtID).append("</paramValue></param>");
                hParameters.put("BILL_ARRANGEMENT", String.valueOf(iExtID));
                
            }
            else if (sLevel.equals("OU")) {
                
                sbParameters.append("<param><paramName>CUSTOMER_ID</paramName><paramValue>").append(iExtID).append("</paramValue></param>");
                hParameters.put("CUSTOMER_ID", String.valueOf(iExtID));                
                
            }
            
            // DTS name
            sbParameters.append("<param><paramName>DTS_NAME</paramName><paramValue>").append(sTitle).append("</paramValue></param>");
            hParameters.put("DTS_NAME", sTitle);
            
            // app name
            sbParameters.append("<param><paramName>IAPP_NAME</paramName><paramValue>AUTOTESTER</paramValue></param>");
            hParameters.put("IAPP_NAME", "AUTOTESTER");
            
            // IDT_CODE, to log warnings
            sbParameters.append("<param><paramName>IDT_CODE</paramName><paramValue>AUTOTESTER</paramValue></param>");
            hParameters.put("IDT_CODE", sTitle);            
            
            if (sParamNames != null)
            {
                
                String sParamName;
                String sParamValue;
                
                for (int i = 0; i < sParamNames.length; i++) {
                      
                    sParamName = sParamNames[i].toString();
                    sParamValue = sParamValues[i].toString();
                    hParameters.put(sParamName, sParamValue);
                    
                    sbParameters.append("<param><paramName>").append(sParamName).append("</paramName>");
                    sbParameters.append("<paramValue>").append(sParamValue).append("</paramValue></param>");
                    
                }                                
                
            }    
            
            sbParameters.append("</parameters>");

            // in parameters
            stmt.setString(2, sbParameters.toString());
            stmt.setString(6, null);
            stmt.setString(7, "single");
            
            // out parameters
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.NUMERIC);
            stmt.registerOutParameter(5, Types.VARCHAR);        
            
            // execute 
            stmt.execute();            
            
            // create detail
            if (stmt.getInt(1) == 0) {                                               
                
                // parse result                      
                String sResultRaw = stmt.getString(3);
                InputStream isResult = new ByteArrayInputStream(sResultRaw.getBytes());
                InputSource is = new InputSource(isResult);
                is.setEncoding("ISO-8859-2"); // diacritics
                Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);                
                Element elRoot = (Element) xml.getElementsByTagName("root").item(0); 

                NodeList nodes = elRoot.getChildNodes();
                Node node;
                String sNodeName;
                NodeList nActions = null; 
                HashMap<String, String[]> hActions = new HashMap<String, String[]>();      
                List<String> lstWarnings = new ArrayList();
                String sResult = null;
                
                for (int i = 0; i < nodes.getLength(); i++) {                                    
                    
                    node = nodes.item(i);
                    sNodeName = node.getNodeName();
                    
                    if (sNodeName.equals("result"))
                        sResult = node.getTextContent();
                    else if (sNodeName.equals("action"))
                        hActions.put(node.getTextContent(), null);
                    else if (sNodeName.equals("actions"))
                        nActions = ((Element) node).getElementsByTagName("action");
                    // store not empty warnings
                    else if (sNodeName.equals("warning") && node.getTextContent().length() > 1)
                        lstWarnings.add(node.getTextContent().replaceAll("\n", ""));
                    
                }
                
                // action parameters
                if (nActions != null) {
                    
                    Element elAction;
                    NodeList nParams;
                    String sParam1 = null;
                    String sParam2 = null;
                    String sParam3 = null;
                
                    for (int i = 0; i < nActions.getLength(); i++) {
                    
                        elAction = (Element) nActions.item(i);
                    
                        nParams = elAction.getElementsByTagName("param1");
                        if (nParams.getLength() > 0)
                            sParam1 = nParams.item(0).getTextContent();
                    
                        nParams = elAction.getElementsByTagName("param2");
                        if (nParams.getLength() > 0)
                            sParam2 = nParams.item(0).getTextContent();
                    
                        nParams = elAction.getElementsByTagName("param3");
                        if (nParams.getLength() > 0)
                            sParam3 = nParams.item(0).getTextContent();   
                    
                        hActions.put(sParam1, new String[] {sParam1, sParam2, sParam3});

                    }
                
                }
                    
                DTS dts = new DTS(sTitle, hParameters, sResult, hActions, lstWarnings);
                stmt.close();
                isResult.close();
                
                return dts;                    
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("callDTS() - error:").append(stmt.getInt(4)).append(", error_text:").append(stmt.getString(5));
                logger.error(sb);
                stmt.close();
                
                return null;                 
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callDTS()", ex);
            return null;
            
        }
        
    }             
    
}
