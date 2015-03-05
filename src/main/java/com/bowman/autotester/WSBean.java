package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import java.lang.String;
import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateful;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.MimeHeaders;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
* Stateful bean, manages web services
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-19 
*/

@Stateful
public class WSBean {
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(WSBean.class.getName()); 
    
    // properties
    private Properties properties;  
    
    // connection
    private String sHost;
    private String sEndpoint;
    private SOAPConnection con;      
    
    /**********************************
              constructors
    **********************************/       
    
    public WSBean() {
        
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
    * Create WS connection
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
            if (sEnvironment.equals(properties.getProperty("test1"))) 
                sHost = properties.getProperty("test1TIBCO");                
            // test 2
            else if (sEnvironment.equals(properties.getProperty("test2"))) 
                sHost = properties.getProperty("test1TIBCO");
            
            sEndpoint = properties.getProperty("testWSEndpoint");
            
            // connect
            con = SOAPConnectionFactory.newInstance().createConnection();
            
            return true;

        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            return false;
            
        }
        
    }  
    
    /**
    * Close WS connection
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
     * Call WS
     * @param sLevel cust level
     * @param iExtID EXTID
     * @param sService service
     * @param sAction action
     * @param sServiceParamNames service parameter names
     * @param sServiceParamValues service parameter values
     * @param sActionParamNames action parameter names
     * @param sActionParamValues action parameter values
     * @return WS
     */
    public WS callWS(String sLevel, int iExtID, String sService, String sAction,
                     Object[] sServiceParamNames, Object[] sServiceParamValues,
                     Object[] sActionParamNames, Object[] sActionParamValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callWS() - params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sService:").append(sService).append(", sAction:").append(sAction);
            sb.append(", sServiceParamNames:").append(Arrays.toString(sServiceParamNames)).append(", sServiceParamValues:").append(Arrays.toString(sServiceParamValues));
            sb.append(", sActionParamNames:").append(Arrays.toString(sActionParamNames)).append(", sActionParamValues:").append(Arrays.toString(sActionParamValues));
            logger.debug(sb);
            
            // create WS request
            WS wsReq = createWSReq(sLevel, iExtID, sService, sAction, sServiceParamNames, sServiceParamValues,
                                   sActionParamNames, sActionParamValues);
            
            // create SOAP request
            SOAPMessage soapReq = createSOAPMessage(wsReq);
            
            // log soap request
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            soapReq.writeTo(os);
            logger.debug(new String(os.toByteArray()));
            
            // change validate to correct action
            if (sAction.equals("validateActivate"))
                sAction = "activate";
            else if (sAction.equals("validateModify"))
                sAction = "modify";
            else if (sAction.equals("validateDeactivate"))
                sAction = "deactivate";
            
            // call web service       
            MimeHeaders headers = soapReq.getMimeHeaders();
            
            sb = new StringBuilder();
            sb.append(sEndpoint).append("/").append(sAction);
            headers.setHeader("SOAPAction", sb.toString());        
            
            sb = new StringBuilder();
            sb.append(sHost).append(sEndpoint);
	    SOAPMessage soapRes = con.call(soapReq, sb.toString()); 
            
            // log soap response
            os = new ByteArrayOutputStream();
            soapRes.writeTo(os);
            logger.debug(new String(os.toByteArray()));            
            
            // create WS response
            WS wsResp = createWSRes(soapRes, wsReq);
            
            return wsResp;
            
        }
        catch (Exception ex) {
            
            logger.error("callWS()", ex);
            return null;
            
        }
        
    }  
    
    /**
     * Create WS request
     * @param sLevel cust level
     * @param iExtID EXTID
     * @param sService service
     * @param sAction action
     * @param sServiceParamNames service parameter names
     * @param sServiceParamValues service parameter values
     * @param sActionParamNames action parameter names
     * @param sActionParamValues action parameter values
     * @return WS
     */
    public WS createWSReq(String sLevel, int iExtID, String sService, String sAction,
                          Object[] sServiceParamNames, Object[] sServiceParamValues,
                          Object[] sActionParamNames, Object[] sActionParamValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createWSReq() - param sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", sService:").append(sService).append(", sAction:").append(sAction);
            sb.append(", sServiceParamNames:").append(Arrays.toString(sServiceParamNames)).append(", sServiceParamValues:").append(Arrays.toString(sServiceParamValues));
            sb.append(", sActionParamNames:").append(Arrays.toString(sActionParamNames)).append(", sActionParamValues:").append(Arrays.toString(sActionParamValues));
            logger.debug(sb);                          
            
            // level
            String sType = null;
            
            if (sLevel.equals("SU"))
                sType = "SUBSCRIBER";    
            
            // create sub elements
            BSMessageContext messageContext = new BSMessageContext("sndr:vcc", String.valueOf(System.currentTimeMillis()));  
            
            BSDataReq data = new BSDataReq();
            data.setContext(new BSContext("INTERNAL", 
                                          new BSTargetContext(iExtID, sType),
                                          new BSCallerContext(iExtID, sType),
                                          "WEB_INT"));
            data.setServiceHeader(new BSServiceHeader("APPTEST"));
            data.setService(new BSService(sService));
            
            // service specific parameters
            List<BSSAParameter> lstServiceParams = null;
            
            if (sServiceParamNames != null) {
                
                lstServiceParams = new ArrayList<BSSAParameter>();
                
                for (int i = 0; i < sServiceParamNames.length; i++)
                    lstServiceParams.add(new BSSAParameter(sServiceParamNames[i].toString(), sServiceParamValues[i].toString()));
                
            }
            
            // action specific parameters
            List<BSSAParameter> lstActionParams = null;
            
            if (sActionParamNames != null) {
                
                lstActionParams = new ArrayList<BSSAParameter>();
                
                for (int i = 0; i < sActionParamNames.length; i++)
                    lstActionParams.add(new BSSAParameter(sActionParamNames[i].toString(), sActionParamValues[i].toString()));
                
            }  
            
            data.setSAParameters(new BSSAParameters(lstServiceParams, lstActionParams));
            
            // actions  
            WS wsReq = null;
            
            if (sAction.equals("getStatus"))
                wsReq = new BSGetServiceStatusReq(messageContext, data);  
            else if (sAction.equals("validateActivate")) {
                
                data.setValidateOnly(true);
                wsReq = new BSActivateServiceReq(messageContext, data);
                
            }
            else if (sAction.equals("activate"))
                wsReq = new BSActivateServiceReq(messageContext, data); 
            else if (sAction.equals("validateModify")) {
                
                data.setValidateOnly(true);
                wsReq = new BSModifyServiceReq(messageContext, data);
                
            }
            else if (sAction.equals("modify"))
                wsReq = new BSModifyServiceReq(messageContext, data);             
            else if (sAction.equals("validateDeactivate")) {
                
                data.setValidateOnly(true);
                wsReq = new BSDeactivateServiceReq(messageContext, data);
                
            }
            else if (sAction.equals("deactivate"))
                wsReq = new BSDeactivateServiceReq(messageContext, data);             
                
            return wsReq;                   
            
        }
        catch (Exception ex) {
            
            logger.error("createWSReq()", ex);
            return null;
            
        }
        
    }  
    
    /**
     * Create SOAP message
     * @param wsReq WS request
     * @return SOAPMessage
     */
    public SOAPMessage createSOAPMessage(WS wsReq) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("createSOAPMessage() - params wsReq:").append(wsReq.toString());
            logger.debug(sb);
            
            // create SOAP message
            SOAPMessage message = MessageFactory.newInstance().createMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            SOAPBody body = envelope.getBody();
            
            // set namespaces
            envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            envelope.addNamespaceDeclaration("dat", "http://selfcare.ei.tmobile.cz/datatypes");
            envelope.addNamespaceDeclaration("dat1", "http://messaging.ei.tmobile.net/datatypes");                
            
            // get request attributes
            SOAPElement elReq = null;
            String sSender = null;
            String sCorrelationId = null;
            String sTargetId = null;
            String sTargetType = null;
            String sCallerId = null;
            String sCallerType = null;
            String sService = null;
            String sValidateOnly = null;
            StringBuilder sbServiceParamsNs = new StringBuilder();
            List<BSSAParameter> lstServiceParams = null;            
            StringBuilder sbActionParamsNs = new StringBuilder();
            List<BSSAParameter> lstActionParams = null;
            
            // get
            if (wsReq instanceof BSGetServiceStatusReq) {
                
                BSGetServiceStatusReq wsGet = (BSGetServiceStatusReq) wsReq;
                elReq = body.addChildElement("GetServiceStatusReq", "dat");
                sSender = wsGet.getMessageContext().getSender();
                sCorrelationId = wsGet.getMessageContext().getCorrelationId();
                sTargetId = String.valueOf(wsGet.getData().getContext().getTargetContext().getTargetId());
                sTargetType = wsGet.getData().getContext().getTargetContext().getTargetType();
                sCallerId = String.valueOf(wsGet.getData().getContext().getCallerContext().getCallerId());
                sCallerType = wsGet.getData().getContext().getCallerContext().getCallerType();
                sService = wsGet.getData().getService().getServiceName();
                sbServiceParamsNs.append("dat:").append(sService).append("GetServiceSpecificParameters");
                lstServiceParams = wsGet.getData().getSAParameters().getGenericServiceSpecificParameters();
                sbActionParamsNs.append("dat:").append(sService).append("GetActionSpecificParameters");
                lstActionParams = wsGet.getData().getSAParameters().getGenericActionSpecificParameters();
                
            }
            // activate
            else if (wsReq instanceof BSActivateServiceReq) {
                
                BSActivateServiceReq wsAct = (BSActivateServiceReq) wsReq;
                elReq = body.addChildElement("ActivateServiceReq", "dat");
                sSender = wsAct.getMessageContext().getSender();
                sCorrelationId = wsAct.getMessageContext().getCorrelationId();
                sTargetId = String.valueOf(wsAct.getData().getContext().getTargetContext().getTargetId());
                sTargetType = wsAct.getData().getContext().getTargetContext().getTargetType();
                sCallerId = String.valueOf(wsAct.getData().getContext().getCallerContext().getCallerId());
                sCallerType = wsAct.getData().getContext().getCallerContext().getCallerType();
                sService = wsAct.getData().getService().getServiceName();
                sValidateOnly = String.valueOf(wsAct.getData().getValidateOnly());
                sbServiceParamsNs.append("dat:").append(sService).append("ActivationServiceSpecificParameters");
                lstServiceParams = wsAct.getData().getSAParameters().getGenericServiceSpecificParameters();
                sbActionParamsNs.append("dat:").append(sService).append("ActivationActionSpecificParameters");  
                lstActionParams = wsAct.getData().getSAParameters().getGenericActionSpecificParameters();
                
            }
            // modify
            else if (wsReq instanceof BSModifyServiceReq) {
                
                BSModifyServiceReq wsMod = (BSModifyServiceReq) wsReq;
                elReq = body.addChildElement("UpdateServiceReq", "dat");
                sSender = wsMod.getMessageContext().getSender();
                sCorrelationId = wsMod.getMessageContext().getCorrelationId();
                sTargetId = String.valueOf(wsMod.getData().getContext().getTargetContext().getTargetId());
                sTargetType = wsMod.getData().getContext().getTargetContext().getTargetType();
                sCallerId = String.valueOf(wsMod.getData().getContext().getCallerContext().getCallerId());
                sCallerType = wsMod.getData().getContext().getCallerContext().getCallerType();
                sService = wsMod.getData().getService().getServiceName();
                sValidateOnly = String.valueOf(wsMod.getData().getValidateOnly());
                sbServiceParamsNs.append("dat:").append(sService).append("ModificationServiceSpecificParameters");
                lstServiceParams = wsMod.getData().getSAParameters().getGenericServiceSpecificParameters();
                sbActionParamsNs.append("dat:").append(sService).append("ModificationActionSpecificParameters");  
                lstActionParams = wsMod.getData().getSAParameters().getGenericActionSpecificParameters();
                
            }            
            // deactivate
            else if (wsReq instanceof BSDeactivateServiceReq) {
                
                BSDeactivateServiceReq wsDct = (BSDeactivateServiceReq) wsReq;
                elReq = body.addChildElement("DeactivateServiceReq", "dat");
                sSender = wsDct.getMessageContext().getSender();
                sCorrelationId = wsDct.getMessageContext().getCorrelationId();
                sTargetId = String.valueOf(wsDct.getData().getContext().getTargetContext().getTargetId());
                sTargetType = wsDct.getData().getContext().getTargetContext().getTargetType();
                sCallerId = String.valueOf(wsDct.getData().getContext().getCallerContext().getCallerId());
                sCallerType = wsDct.getData().getContext().getCallerContext().getCallerType();
                sService = wsDct.getData().getService().getServiceName();
                sValidateOnly = String.valueOf(wsDct.getData().getValidateOnly());
                sbServiceParamsNs.append("dat:").append(sService).append("DeactivationServiceSpecificParameters");
                lstServiceParams = wsDct.getData().getSAParameters().getGenericServiceSpecificParameters();
                sbActionParamsNs.append("dat:").append(sService).append("DeactivationActionSpecificParameters"); 
                lstActionParams = wsDct.getData().getSAParameters().getGenericActionSpecificParameters();
                
            }            
            
            // message context
            SOAPElement elMessageContext = elReq.addChildElement("eiMessageContext", "dat");
            elMessageContext.addChildElement("sender", "dat1").addTextNode(sSender);
            elMessageContext.addChildElement("correlationId", "dat1").addTextNode(sCorrelationId);
            
            // context
            SOAPElement elData = elReq.addChildElement("data", "dat");            
            SOAPElement elContext = elData.addChildElement("context", "dat");                                    
            elContext.addChildElement("authType", "dat").addTextNode("INTERNAL");
            SOAPElement elTargetContext = elContext.addChildElement("targetContext", "dat");
            SOAPElement elCallerContext = elContext.addChildElement("callerContext", "dat");
            elContext.addChildElement("channel", "dat").addTextNode("WEB_INT");
            
            // target context            
            elTargetContext.addChildElement("targetId", "dat").addTextNode(sTargetId);
            elTargetContext.addChildElement("targetType", "dat").addTextNode(sTargetType);
            
            // caller context            
            elCallerContext.addChildElement("callerId", "dat").addTextNode(sCallerId);
            elCallerContext.addChildElement("callerType", "dat").addTextNode(sCallerType);                        
            
            // service header
            SOAPElement elServiceHeader = elData.addChildElement("ServiceHeader", "dat");
            elServiceHeader.addChildElement("userName", "dat").addTextNode("APPTEST");
            
            // validate only
            if (wsReq instanceof BSActivateServiceReq ||
                wsReq instanceof BSModifyServiceReq ||
                wsReq instanceof BSDeactivateServiceReq) 
                elData.addChildElement("validateOnly", "dat").addTextNode(sValidateOnly);
            
            // service
            SOAPElement elService = elData.addChildElement("service", "dat");
            elService.addChildElement("serviceName", "dat").addTextNode(sService);        
            
            // SA parameters
            SOAPElement elSAParameters = elData.addChildElement("SAParameters", "dat");
            
            // generic service specific parameters
            SOAPElement elServiceParams = elSAParameters.addChildElement("GenericServiceSpecificParameters", "dat");
            elServiceParams.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance"); 
            elServiceParams.addAttribute(envelope.createName("xsi:type"), sbServiceParamsNs.toString());
            
            if (lstServiceParams != null) {
                
                for (BSSAParameter saParameter : lstServiceParams)
                    elServiceParams.addChildElement(saParameter.getName(), "dat").addTextNode(saParameter.getValue());
                
            }
            
            // generic action specific parameters
            SOAPElement elActionParams = elSAParameters.addChildElement("GenericActionSpecificParameters", "dat");
            elActionParams.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance"); 
            elActionParams.addAttribute(envelope.createName("xsi:type"), sbActionParamsNs.toString());
            
            if (lstActionParams != null) {
                
                for (BSSAParameter saParameter : lstActionParams)
                    elActionParams.addChildElement(saParameter.getName(), "dat").addTextNode(saParameter.getValue());
                
            }            
            
            return message;
            
        }
        catch (Exception ex) {
            
            logger.error("createSOAPMessage()", ex);
            return null;
            
        }
        
    }   
    
    /**
     * Create WS response
     * @param soapRes soap response
     * @param wsReq WS request
     * @return WS
     */
    public WS createWSRes(SOAPMessage soapRes, WS wsReq) {
        
        try {
            
            logger.debug("createWSResp()");
            
            Document xml = soapRes.getSOAPBody().extractContentAsDocument();
            
            // message context
            String sAction = xml.getFirstChild().getNodeName();
            Element el = (Element) xml.getElementsByTagName("ns0:eiMessageContext").item(0);
            String sSender = null;
            String sCorrelationId = null;

            if (sAction.equals("ns0:GetServiceStatusRes") ||
                sAction.equals("ns0:ActivateServiceRes") ||
                sAction.equals("ns0:UpdateServiceRes") ||    
                sAction.equals("ns0:DeactivateServiceRes")) {
            
                sSender = el.getElementsByTagName("ns1:sender").item(0).getTextContent();
                sCorrelationId = el.getElementsByTagName("ns1:correlationId").item(0).getTextContent();
            
            }
            else if (sAction.equals("SOAP-ENV:Fault")) {
                
                sSender = el.getElementsByTagName("ns0:sender").item(0).getTextContent();
                sCorrelationId = el.getElementsByTagName("ns0:correlationId").item(0).getTextContent();                
                
            }
            
            BSMessageContext messageContext = new BSMessageContext(sSender, sCorrelationId);
            
            // service header            
            String sUserName = null;
            
            if (sAction.equals("ns0:GetServiceStatusRes")) {
                
                el = (Element) xml.getElementsByTagName("dat:ServiceHeader").item(0);
                sUserName = el.getElementsByTagName("dat:userName").item(0).getTextContent(); 
            
            }
            else if (sAction.equals("ns0:ActivateServiceRes") ||
                     sAction.equals("ns0:UpdateServiceRes") ||
                     sAction.equals("ns0:DeactivateServiceRes")) {
                
                el = (Element) xml.getElementsByTagName("ns1:ServiceHeader").item(0);
                sUserName = el.getElementsByTagName("ns1:userName").item(0).getTextContent();                 
                
            }
            
            BSDataResp data = new BSDataResp();
            data.setServiceHeader(new BSServiceHeader(sUserName));

            // allowed actions
            NodeList nodes = xml.getElementsByTagName("ns0:action");
            
            if (nodes.getLength() > 0) {
                
                List<BSAction> lstAllowedActions = new ArrayList<BSAction>();
                boolean bAllowed;
                String sActionName;
            
                for (int i = 0; i < nodes.getLength(); i++) {
                
                    el = (Element) nodes.item(i);
                    bAllowed = Boolean.valueOf(el.getElementsByTagName("ns0:allowed").item(0).getTextContent());
                    sActionName = el.getElementsByTagName("ns0:actionName").item(0).getTextContent();
                
                    // bre data
                    el = (Element )el.getElementsByTagName("ns0:breData").item(0);
                    List<String> lstBreData = new ArrayList<String>();
                    
                    // breData is sometimes missing
                    if (el != null) {
                        
                        NodeList nodesBreData = el.getElementsByTagName("ns0:reason");                    
                        String sReason;
                 
                        for (int j = 0; j < nodesBreData.getLength(); j++) {
                    
                            sReason = nodesBreData.item(j).getTextContent();
                            lstBreData.add(sReason);
                    
                        }
                    
                    }
                
                    lstAllowedActions.add(new BSAction(bAllowed, sActionName, lstBreData));
                
                }
            
                data.setAllowedActions(lstAllowedActions);
            
            }
            
            // service status
            el = (Element) xml.getElementsByTagName("pfx:ServiceStatus").item(0);
            
            if (el != null) {
                
                String sStatus = el.getElementsByTagName("pfx:status").item(0).getTextContent(); 
                
                // generic service specific parameters
                nodes = el.getElementsByTagName("pfx:GenericServiceSpecificParameters").item(0).getChildNodes();
                Node node;
                List<BSSAParameter> lstGenericServiceSpecificParameters = new ArrayList<BSSAParameter>();
                String sParamName;
                String sParamValue;
                
                for (int i = 0; i < nodes.getLength(); i++) {
                    
                    node = nodes.item(i);
                    sParamName = node.getNodeName().replaceAll("pfx:", "");   
                    sParamValue = node.getTextContent();
                    lstGenericServiceSpecificParameters.add(new BSSAParameter(sParamName, sParamValue));
                    
                }
                
                // generic action specific parameters
                nodes = el.getElementsByTagName("pfx:GenericActionSpecificParameters").item(0).getChildNodes();
                List<BSSAParameter> lstGenericActionSpecificParameters = new ArrayList<BSSAParameter>();
                
                for (int i = 0; i < nodes.getLength(); i++) {
                    
                    node = nodes.item(i);
                    sParamName = node.getNodeName().replaceAll("pfx:", "");   
                    sParamValue = node.getTextContent();
                    lstGenericActionSpecificParameters.add(new BSSAParameter(sParamName, sParamValue));
                    
                }                
                
                data.setServiceStatus(new BSServiceStatus(sStatus, new BSSAParameters(lstGenericServiceSpecificParameters, 
                                                                                      lstGenericActionSpecificParameters)));
                
            }
            
            // OMS status
            el = (Element) xml.getElementsByTagName("ns0:OmsStatus").item(0);
            
            if (el != null) {
                
                String sOrderId = el.getElementsByTagName("ns0:orderId").item(0).getTextContent(); 
                String sOrderStatus = el.getElementsByTagName("ns0:orderStatus").item(0).getTextContent(); 
                data.setOmsStatus(new BSOmsStatus(sOrderId, sOrderStatus));
                
            }   
            
            // fault detail
            el = (Element) xml.getElementsByTagName("detail").item(0);
            BSFault fault = null;
            
            if (el != null) {
                
                String sTimestamp = el.getElementsByTagName("ns1:timestamp").item(0).getTextContent();
                String sExceptionClass = el.getElementsByTagName("ns1:exceptionClass").item(0).getTextContent();
                String sFurtherInfo = el.getElementsByTagName("ns1:furtherInfo").item(0).getTextContent();
                
                // bre data
                el = (Element )el.getElementsByTagName("ns:breData").item(0);
                NodeList nodesBreData = el.getElementsByTagName("ns1:textInfo");
                List<String> lstBreData = new ArrayList<String>();
                String sTextInfo;
                
                for (int j = 0; j < nodesBreData.getLength(); j++) {
                    
                    sTextInfo = nodesBreData.item(j).getTextContent();
                    lstBreData.add(sTextInfo);
                    
                }              
                
                fault = new BSFault(messageContext, sTimestamp, sExceptionClass, sFurtherInfo, lstBreData);
                
            }
            
            // action            
            WS wsRes = null;
            
            if (sAction.equals("ns0:GetServiceStatusRes"))
                wsRes = new BSGetServiceStatusRes((BSGetServiceStatusReq) wsReq, messageContext, data);
            else if (sAction.equals("ns0:ActivateServiceRes"))
                wsRes = new BSActivateServiceRes((BSActivateServiceReq) wsReq, messageContext, data);
            else if (sAction.equals("ns0:UpdateServiceRes"))
                wsRes = new BSModifyServiceRes((BSModifyServiceReq) wsReq, messageContext, data);            
            else if (sAction.equals("ns0:DeactivateServiceRes"))
                wsRes = new BSDeactivateServiceRes((BSDeactivateServiceReq) wsReq, messageContext, data);  
            else if (sAction.equals("SOAP-ENV:Fault"))
                wsRes = fault;
            
            return wsRes;
            
        }
        catch (Exception ex) {
            
            logger.error("createWSResp()", ex);
            return null;
            
        }
        
    }      
    
}
