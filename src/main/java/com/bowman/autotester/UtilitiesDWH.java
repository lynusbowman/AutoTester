package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Utilities for DWH
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

public class UtilitiesDWH {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesDWH.class.getName());  
        
    private Utilities util;    
    private DWHBean dwhBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesDWH() {
        
        this.util = null;
        this.dwhBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param dwhBean DWHBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesDWH(Utilities util, DWHBean dwhBean, int iLogID) {
        
        this.util = util;
        this.dwhBean = dwhBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/   
    
    /**
    * Connect to DWH DB
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            dwhBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to DWH");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to DWH on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from DWH DB
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            dwhBean.disconnect();
            util.log(Result.OK, "Disconnect from DWH");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from DWH");
            
            return 0;
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
            sb.append("getCustHierarchy() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            CustHierarchy custHierarchy = dwhBean.getCustHierarchy(sMSISDN);
            
            if (custHierarchy == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get customer hierarchy for MSISDN ").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get customer hierarchy for MSISDN ").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                util.log(custHierarchy.toString());
                
                return custHierarchy;
                
            }
        }
        catch (Exception ex) {
            
            logger.error("getCustHierarchy()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get customer hierarchy for MSISDN ").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getCU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iCU = dwhBean.getCU(sMSISDN);
            
            if (iCU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get CU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get CU: ").append(iCU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iCU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getCU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get CU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getLE() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iLE = dwhBean.getLE(sMSISDN);
            
            if (iLE == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get LE for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get LE: ").append(iLE).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iLE;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getLE()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get LE for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getOU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iOU = dwhBean.getOU(sMSISDN);
            
            if (iOU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get OU: ").append(iOU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iOU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get OU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getEU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iEU = dwhBean.getEU(sMSISDN);
            
            if (iEU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get EU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get EU: ").append(iEU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iEU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getEU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get EU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getBA() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iBA = dwhBean.getBA(sMSISDN);
            
            if (iBA == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get BA: ").append(iBA).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iBA;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBA()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get BA for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getSU() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            int iSU = dwhBean.getSU(sMSISDN);
            
            if (iSU == -1) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return 0;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get SU: ").append(iSU).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return iSU;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSU()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return 0;
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
            sb.append("getStatus() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            String sStatus = dwhBean.getStatus(sMSISDN);
            
            if (sStatus == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get status for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get status: ").append(sStatus).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return sStatus;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getStatus()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get status for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getBillCycle() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            BillCycle billCycle = dwhBean.getBillCycle(sMSISDN);
            
            if (billCycle == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get bill cycle for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get bill cycle: ").append(billCycle.toString()).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
                return billCycle;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBillCycle()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get bill cycle for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getSegment() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            Segment segment = dwhBean.getSegment(sMSISDN);
            
            if (segment == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get segment for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get segment: ").append(segment.toString()).append(" for MSISDN:").append(sMSISDN);
                
                return segment;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSegment()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get segment for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getMarket() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            Market market = dwhBean.getMarket(sMSISDN);
            
            if (market == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get market for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get market: ").append(market.toString()).append(" for MSISDN:").append(sMSISDN);
                
                return market;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getMarket()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get market for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getTariff() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            Tariff tariff = dwhBean.getTariff(sMSISDN);
            
            if (tariff == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get tariff for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get tariff: ").append(tariff.toString()).append(" for MSISDN:").append(sMSISDN);
                
                return tariff;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getTariff()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get tariff for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getContractDuration() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN);
            logger.info(sb);
            
            ContractDuration contractDuration = dwhBean.getContractDuration(sMSISDN);
            
            if (contractDuration == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get contract duration for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get contract duration: ").append(contractDuration.toString()).append(" for MSISDN:").append(sMSISDN);
                
                return contractDuration;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getContractDuration()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get contract duration for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
    }
    
    /**
    * Get SU services
    * @param iExtID EXTID_SU
    * @return List of Service
    */
    public List<Service> getSUServices(int iExtID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUServices() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getSUServices(iExtID, -1, -1);
            
            if (lstServices == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU services for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                // log services
                for(Service service : lstServices)
                    util.log(Result.OK, service.toString());                               
                
                return lstServices;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUServices()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU services for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }   
    
    /**
    * Get SU service
    * @param iExtID EXTID_SU
    * @param iServiceID Service ID
    * @return Service
    */
    public Service getSUService(int iExtID, int iServiceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getSUServices(iExtID, iServiceID, -1);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU service:").append(iServiceID).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU service:").append(iServiceID).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }    
    
    /**
    * Get SU service
    * @param iExtID EXTID_SU
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return Service
    */
    public Service getSUService(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getSUServices(iExtID, iServiceID, iServiceInstanceID);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
                sb.append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
            sb.append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    } 
    
    /**
    * Get BA services
    * @param iExtID EXTID_BA
    * @return List of Service
    */
    public List<Service> getBAServices(int iExtID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getBAServices() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("BA", iExtID, -1, -1);
            
            if (lstServices == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA services for EXTID:").append(iExtID);
                
                return null;
                
            }
            else {
                
                // log services
                for(Service service : lstServices)
                    util.log(Result.OK, service.toString());                               
                
                return lstServices;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBAServices()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get BA services for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }       
    
    /**
    * Get BA service
    * @param iExtID EXTID_BA
    * @param iServiceID Service ID
    * @return Service
    */
    public Service getBAService(int iExtID, int iServiceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getBAService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("BA", iExtID, iServiceID, -1);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA service:").append(iServiceID).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBAService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get BA service:").append(iServiceID).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }   
    
    /**
    * Get BA service
    * @param iExtID EXTID_BA
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return Service
    */
    public Service getBAService(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getBAService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("BA", iExtID, iServiceID, iServiceInstanceID);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
                sb.append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBAService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get BA service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
            sb.append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }    
    
    /**
    * Get OU services
    * @param iExtID EXTID_OU
    * @return List of Service
    */
    public List<Service> getOUServices(int iExtID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getOUServices() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("OU", iExtID, -1, -1);
            
            if (lstServices == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU services for EXTID:").append(iExtID);
                
                return null;
                
            }
            else {
                
                // log services
                for(Service service : lstServices)
                    util.log(Result.OK, service.toString());                               
                
                return lstServices;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOUServices()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get OU services for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }       
    
    /**
    * Get OU service
    * @param iExtID EXTID_OU
    * @param iServiceID Service ID
    * @return Service
    */
    public Service getOUService(int iExtID, int iServiceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getOUService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("OU", iExtID, iServiceID, -1);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU service:").append(iServiceID).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOUService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get OU service:").append(iServiceID).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }   
    
    /**
    * Get OU service
    * @param iExtID EXTID_OU
    * @param iServiceID Service ID
    * @param iServiceInstanceID Service instance ID
    * @return Service
    */
    public Service getOUService(int iExtID, int iServiceID, int iServiceInstanceID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getOUService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            sb.append(", iServiceInstanceID:").append(iServiceInstanceID);
            logger.info(sb);
            
            List<Service> lstServices = dwhBean.getCUServices("OU", iExtID, iServiceID, iServiceInstanceID);
            
            if (lstServices == null || lstServices.isEmpty()) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
                sb.append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                Service service = lstServices.get(0);
                util.log(Result.OK, service.toString());
                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOUService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get OU service:").append(iServiceID).append(" instance:").append(iServiceInstanceID);
            sb.append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getMTXService() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID).append(", iServiceID:").append(iServiceID);
            logger.info(sb);
            
            Service service = dwhBean.getMTXService(iExtID, iServiceID);
            
            if (service == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get service:").append(iServiceID).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                                
                util.log(Result.OK, service.toString());                
                return service;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getMTXService()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get service:").append(iServiceID).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }       
    
}