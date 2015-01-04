package com.bowman.autotester;

/**********************************
           imports
 **********************************/

import com.bowman.autotester.TestCaseBean.Result;

import java.lang.String;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Utilities for CLF
* 
* @author  Petr Rasek
* @version 1.0
* @since   2014-12-05 
*/

public class UtilitiesCLF {           
    
    /**********************************
              attributes
    **********************************/ 
    
    // logger
    private final static Logger logger = LogManager.getLogger(UtilitiesCLF.class.getName());  
        
    private Utilities util;    
    private CLFBean clfBean;    
    private int iLogID;
    
    /**********************************
              constructors
    **********************************/ 
    
    public UtilitiesCLF() {
        
        this.util = null;
        this.clfBean = null;        
        this.iLogID = -1;
        
    }    

    /**    
    * @param util Utilities      
    * @param clfBean CLFBean
    * @param iLogID log ID
    * */        
    
    public UtilitiesCLF(Utilities util, CLFBean clfBean, int iLogID) {
        
        this.util = util;
        this.clfBean = clfBean;
        this.iLogID = iLogID;

    }
    
    /**********************************
                methods
    **********************************/   
    
    /**
    * Connect to CLF DB
    * @param sEnvironment environment
    * @return int
    */
    public int connect(String sEnvironment) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("connect() - corrId=").append(iLogID).append(", params sEnvironment:").append(sEnvironment);
            logger.info(sb);
            
            clfBean.connect(sEnvironment);
            util.log(Result.OK, "Connect to CLF");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("connect()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to CLF on ").append(sEnvironment);
            util.log(Result.ERR, sb.toString());
            
            return 0;
        }
    }  
    
    /**
    * Disconnect from CLF DB
    * @return int
    */
    public int disconnect() {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("disconnect() - corrId=").append(iLogID);
            logger.info(sb);
            
            clfBean.disconnect();
            util.log(Result.OK, "Disconnect from CLF");
            
            return 1;
        }
        catch (Exception ex) {
            
            logger.error("disconnect()", ex);
            util.log(Result.ERR, "Failed to disconnect from CLF");
            
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
            
            CustHierarchy custHierarchy = clfBean.getCustHierarchy(sMSISDN);
            
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
            
            int iCU = clfBean.getCU(sMSISDN);
            
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
            
            int iLE = clfBean.getLE(sMSISDN);
            
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
            
            int iOU = clfBean.getOU(sMSISDN);
            
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
            
            int iEU = clfBean.getEU(sMSISDN);
            
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
            
            int iBA = clfBean.getBA(sMSISDN);
            
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
            
            int iSU = clfBean.getSU(sMSISDN);
            
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
            
            String sStatus = clfBean.getStatus(sMSISDN);
            
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
            
            BillCycle billCycle = clfBean.getBillCycle(sMSISDN);
            
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
            
            Segment segment = clfBean.getSegment(sMSISDN);
            
            if (segment == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get segment for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get segment: ").append(segment.toString()).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
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
            
            Market market = clfBean.getMarket(sMSISDN);
            
            if (market == null) {
                               
                sb = new StringBuilder();
                sb.append("Failed to get market for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get market: ").append(market.toString()).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
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
            
            Tariff tariff = clfBean.getTariff(sMSISDN);
            
            if (tariff == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get tariff for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get tariff: ").append(tariff.toString()).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
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
            
            ContractDuration contractDuration = clfBean.getContractDuration(sMSISDN);
            
            if (contractDuration == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get contract duration for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get contract duration: ").append(contractDuration.toString()).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.OK, sb.toString());
                
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
    * Get SU detail
    * @param iExtID EXTID_SU
    * @return SUDetail
    */
    public SUDetail getSUDetail(int iExtID) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("getSUDetail() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            SUDetail suDetail = clfBean.getSUDetail(iExtID);
            
            if (suDetail == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get SU detail for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get SU detail: ").append(suDetail.toString()).append(" for EXTID:").append(iExtID);
                util.log(Result.OK, sb.toString());
                
                return suDetail;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getSUDetail()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get SU detail for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getBADetail() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            BADetail baDetail = clfBean.getBADetail(iExtID);
            
            if (baDetail == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA detail for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get BA detail: ").append(baDetail.toString()).append(" for EXTID:").append(iExtID);
                util.log(Result.OK, sb.toString());
                
                return baDetail;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getBADetail()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get BA detail for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getOUDetail() - corrId=").append(iLogID).append(", params iExtID:").append(iExtID);
            logger.info(sb);
            
            OUDetail ouDetail = clfBean.getOUDetail(iExtID);
            
            if (ouDetail == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU detail for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                
                return null;
                
            }
            else {
                
                sb = new StringBuilder();
                sb.append("Get OU detail: ").append(ouDetail.toString()).append(" for EXTID:").append(iExtID);
                util.log(Result.OK, sb.toString());
                
                return ouDetail;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getOUDetail()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get OU detail for EXTID:").append(iExtID);
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
            
            List<Service> lstServices = clfBean.getSUServices(iExtID, -1, -1);
            
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
            
            List<Service> lstServices = clfBean.getSUServices(iExtID, iServiceID, -1);
            
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
            
            List<Service> lstServices = clfBean.getSUServices(iExtID, iServiceID, iServiceInstanceID);
            
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
            
            List<Service> lstServices = clfBean.getBAServices(iExtID, -1, -1);
            
            if (lstServices == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get BA services for EXTID:").append(iExtID);
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
            
            List<Service> lstServices = clfBean.getBAServices(iExtID, iServiceID, -1);
            
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
            
            List<Service> lstServices = clfBean.getBAServices(iExtID, iServiceID, iServiceInstanceID);
            
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
            
            List<Service> lstServices = clfBean.getOUServices(iExtID, -1, -1);
            
            if (lstServices == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get OU services for EXTID:").append(iExtID);
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
            
            List<Service> lstServices = clfBean.getOUServices(iExtID, iServiceID, -1);
            
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
            
            List<Service> lstServices = clfBean.getOUServices(iExtID, iServiceID, iServiceInstanceID);
            
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
    * Call PAPI
    * @param sLevel Level    
    * @param iExtID EXTID_CS
    * @param iPropertyID Property ID
    * @param sAction Action
    * @return PAPI
    */
    public PAPI callPAPI(String sLevel, int iExtID, int iPropertyID, String sAction) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callPAPI() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", iPropertyID:").append(iPropertyID).append(", sAction:").append(sAction);
            logger.info(sb);
            
            PAPI papi = null;
            
            // cs type
            int iCsType = -1;
            
            if (sLevel.equals("SU"))
                iCsType = 4;
            else if (sLevel.equals("BA"))
                iCsType = 2;
            else if (sLevel.equals("OU"))
                iCsType = 1;
                
            // action  
            if (sAction.equals("G"))
                papi = clfBean.getPAPI(iExtID, iCsType, iPropertyID, null, null);
            else if (sAction.equals("a") || sAction.equals("m") || sAction.equals("d") ||
                     sAction.equals("v") || sAction.equals("c") || sAction.equals("x"))
                papi = clfBean.setPAPI(iExtID, iCsType, iPropertyID, sAction, null, null);
            
            if (papi == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to call PAPI:").append(iPropertyID).append(".").append(sAction).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                                
                util.log(Result.OK, papi.toString());                
                return papi;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callPAPI()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to call PAPI:").append(iPropertyID).append(".").append(sAction).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
        
    }   
    
    /**
    * Call PAPI
    * @param sLevel Level    
    * @param iExtID EXTID_CS
    * @param iPropertyID Property ID
    * @param sAction Action
    * @param iAttrIDs Attribute IDs
    * @param sAttrValues Attribute values
    * @return PAPI
    */
    public PAPI callPAPI(String sLevel, int iExtID, int iPropertyID, String sAction,
                         List<Integer> iAttrIDs, List<String> sAttrValues) {
        
        try {
            
            StringBuilder sb = new StringBuilder();
            sb.append("callPAPI() - corrId=").append(iLogID).append(", params sLevel:").append(sLevel).append(", iExtID:").append(iExtID);
            sb.append(", iPropertyID:").append(iPropertyID).append(", sAction:").append(sAction);
            sb.append(", iAttrIDs:").append(Arrays.toString(iAttrIDs.toArray()));
            sb.append(", sAttrValues:").append(Arrays.toString(sAttrValues.toArray()));
            logger.info(sb);
            
            PAPI papi = null;
            
            // cs type
            int iCsType = -1;
            
            if (sLevel.equals("SU"))
                iCsType = 4;
            else if (sLevel.equals("BA"))
                iCsType = 2;
            else if (sLevel.equals("OU"))
                iCsType = 1;
                
            // action  
            if (sAction.equals("G"))
                papi = clfBean.getPAPI(iExtID, iCsType, iPropertyID, iAttrIDs.toArray(), sAttrValues.toArray());
            else if (sAction.equals("a") || sAction.equals("m") || sAction.equals("d") ||
                     sAction.equals("v") || sAction.equals("c") || sAction.equals("x"))
                papi = clfBean.setPAPI(iExtID, iCsType, iPropertyID, sAction, iAttrIDs.toArray(), sAttrValues.toArray());
            
            if (papi == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to call PAPI:").append(iPropertyID).append(".").append(sAction).append(" for EXTID:").append(iExtID);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                                
                util.log(Result.OK, papi.toString());                
                return papi;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("callPAPI()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to call PAPI:").append(iPropertyID).append(".").append(sAction).append(" for EXTID:").append(iExtID);
            util.log(Result.ERR, sb.toString());
            
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
            sb.append("getcase() - corrId=").append(iLogID).append(", params sMSISDN:").append(sMSISDN).append(", sCaseType:").append(sCaseType);
            logger.info(sb);
            
            Case cCase = clfBean.getCase(sMSISDN, sCaseType);
            
            if (cCase == null) {
                
                sb = new StringBuilder();
                sb.append("Failed to get case:").append(sCaseType).append(" for MSISDN:").append(sMSISDN);
                util.log(Result.ERR, sb.toString());
                return null;
                
            }
            else {
                
                util.log(Result.OK, cCase.toString());
                return cCase;
                
            }
            
        }
        catch (Exception ex) {
            
            logger.error("getCase()", ex);
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to get case:").append(sCaseType).append(" for MSISDN:").append(sMSISDN);
            util.log(Result.ERR, sb.toString());
            
            return null;
        }
    }      
    
}