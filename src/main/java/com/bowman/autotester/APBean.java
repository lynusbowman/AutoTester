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
import java.sql.ResultSet;
import java.sql.Statement;
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
            // reservation DB
            else if (sEnvironment.equals("reserve")) {
                
                sUrl = properties.getProperty("reserveDBUrl");
                sUser = properties.getProperty("reserveDBUser");
                sPass = properties.getProperty("reserveDBPass");
                
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
            sbQuery.append("FROM SY_QPRO.QPRO_TARIFF_00_MV a, SY_QPRO.QPRO_TARIFF_PROPERTY_00_MV b ");
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
            sbQuery.append("SELECT qpronpo_id, qpronpo_des ");
            sbQuery.append("FROM SY_QPRO.QPRO_NON_PUBLIC_OFFER_00_MV ");
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
                int iServiceID = -1;
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
            sbQuery.append("FROM SY_QPRO.QPRO_TARIFF_PROMO_00_MV ");
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
            sbQuery.append("FROM SY_QPRO.QPRO_DISCOUNT_PROPERTY_00_MV ");
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
            sbQuery.append("FROM SY_QPRO.QPRO_RETENTION_OFFER_00_MV ");
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
            sbQuery.append("FROM SY_QPRO.QPRO_SERVICE_00_MV a, SY_QPRO.QPRO_SERVICE_PARAM_00_MV b, SY_QPRO.QPRO_PARAM_00_MV c, SY_QPRO.QPRO_PARAM_VALUE_00_MV d ");
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
    
    /**
    * Reserve data
    * @param iData data
    */
    public void reserveData(int iData) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("reserveData() - params iData:").append(iData);
            logger.debug(sb);

            // connect to reservation DB
            connect("reserve");
            
            // execute query
            StringBuilder sbQuery = new StringBuilder();
            sbQuery.append("INSERT INTO TST_DATA.DATA_RESERVATION (rectype, status, msisdn, blevel, reserve_from, reqtype, ");
            sbQuery.append("req_des, req_owner, data_owner, apl_des, description) VALUES ");
            sbQuery.append("(1, 'R', '").append(iData).append("', 1, sysdate, 1, 'Automated tests', 'AUTOTESTER', 'AUTOTESTER', ");
            sbQuery.append("'KISS', 'Automated tests')");
            
            Statement stmt = con.createStatement();
            
            if (stmt.executeUpdate(sbQuery.toString()) == 0)
                logger.error("reserveData() - failed to reserve data");
            
            // disconnect from reservation DB
            disconnect();
            
        }
        catch (Exception ex) {

            logger.error("reserveData()", ex);
            
        }  
        
    }
    
}
